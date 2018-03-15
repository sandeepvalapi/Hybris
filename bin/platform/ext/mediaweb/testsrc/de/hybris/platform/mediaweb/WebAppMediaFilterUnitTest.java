/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.mediaweb;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.media.exceptions.MediaNotFoundException;
import de.hybris.platform.media.services.MediaHeadersRegistry;
import de.hybris.platform.servicelayer.web.WebAppMediaFilter;
import de.hybris.platform.util.config.ConfigIntf;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class WebAppMediaFilterUnitTest
{
	private static final String LAST_MODIFIED_DATE = "Thu, 01 Jan 1970 00:00:01 GMT";
	private static final String FORCE_DOWNLOAD_DIALOG_FILE_EXTENSIONS = "xls";
	private static final String ALLOWED_EXTENSIONS_FOR_CLASSLOADER_KEY = "media.allowed.extensions.for.ClassLoader";
	private static final String ALLOWED_RESOURCE_PATH_FROM_CLASSLOADER = "/fromjar/somepath/xxxx/file.png";
	private static final String NOT_ALLOWED_RESOURCE_PATH_FROM_CLASSLOADER = "/fromjar/somepath/xxxx/file.foo";
	private static final String ALLOWED_EXTENSIONS_FOR_CLASSLOADER = "jpg,png,csv";

	private TestMediaFilter mediaFilter;
	@Mock
	private ConfigIntf config;
	@Mock
	private HttpServletRequest httpRequest;
	@Mock
	private HttpServletResponse httpResponse;
	@Mock
	private FilterChain chain;
	@Mock
	private ServletOutputStream servletOutputStream;
	@Mock
	private InputStream inputStream;
	@Mock
	private ServletOutputStream outputStream;
	@Mock
	private MediaManager mediaMgr;
	@Mock
	private MediaHeadersRegistry mediaHeadersRegistry;
	private Map<String, String> configuredHeaders;
	private boolean prettyURLLegacyMode;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		this.mediaFilter = new TestMediaFilter();

		configuredHeaders = new HashMap<>();
		configuredHeaders.put("Last-Modified", LAST_MODIFIED_DATE);

		given(mediaHeadersRegistry.getHeaders()).willReturn(configuredHeaders);
		given(mediaMgr.getMediaHeadersRegistry()).willReturn(mediaHeadersRegistry);
		given(httpRequest.getParameter("context"))
				.willReturn("bWFzdGVyfHJvb3R8MTIzNDV8aW1hZ2UvanBlZ3xoMDEvaDAyL2Zvby5qcGd8cXdlcnR5MTIzNDU");
		//		given(config.getParameter("media.header.Last-Modified")).willReturn(LAST_MODIFIED_DATE);
		given(Integer.valueOf(inputStream.read(any(byte[].class)))).willReturn(Integer.valueOf(1), Integer.valueOf(0),
				Integer.valueOf(-1));

		prettyURLLegacyMode = false;
	}

	@Test
	public void shouldPrintNotAllowedMessageWhenRequestingResourceFromClassloaderAndItDoesntContainAllowedExtension()
			throws Exception
	{
		// given
		given(httpRequest.getServletPath()).willReturn(NOT_ALLOWED_RESOURCE_PATH_FROM_CLASSLOADER);
		given(httpResponse.getOutputStream()).willReturn(servletOutputStream);
		given(config.getParameter(ALLOWED_EXTENSIONS_FOR_CLASSLOADER_KEY)).willReturn(ALLOWED_EXTENSIONS_FOR_CLASSLOADER);

		// when
		mediaFilter.doFilter(httpRequest, httpResponse, chain);

		// then
		verify(httpResponse, times(1)).setContentType("text/plain");
		verify(servletOutputStream, times(1))
				.println("not allowed to load media '/somepath/xxxx/file.foo' from classloader. Check parameter "
						+ ALLOWED_EXTENSIONS_FOR_CLASSLOADER_KEY
						+ " in advanced.properties to change the file extensions (e.g. *.gif) that are allowed to download.");
	}

	@Test
	public void shouldPrintFileNotFoundMessageWhenRequestingResourceFromClassloaderAndItDoesntExist() throws Exception
	{
		// given
		given(httpRequest.getServletPath()).willReturn(ALLOWED_RESOURCE_PATH_FROM_CLASSLOADER);
		given(httpResponse.getOutputStream()).willReturn(servletOutputStream);
		given(config.getParameter(ALLOWED_EXTENSIONS_FOR_CLASSLOADER_KEY)).willReturn(ALLOWED_EXTENSIONS_FOR_CLASSLOADER);

		// when
		mediaFilter.doFilter(httpRequest, httpResponse, chain);

		// then
		verify(httpResponse, times(1)).setContentType("text/plain");
		verify(servletOutputStream, times(1)).println("file '/somepath/xxxx/file.png' not found!");
	}

	@Test
	public void shouldLoadFileFromClassloaderWhenRequestingResourceFromJarAndItExist() throws Exception
	{
		// given
		final byte[] buffer = new byte[4096];
		given(httpRequest.getServletPath()).willReturn(ALLOWED_RESOURCE_PATH_FROM_CLASSLOADER);
		given(httpResponse.getOutputStream()).willReturn(servletOutputStream);
		given(config.getParameter(ALLOWED_EXTENSIONS_FOR_CLASSLOADER_KEY)).willReturn(ALLOWED_EXTENSIONS_FOR_CLASSLOADER);
		given(Integer.valueOf(inputStream.read(buffer))).willReturn(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(-1));
		mediaFilter.setResourceStream(inputStream);

		// when
		mediaFilter.doFilter(httpRequest, httpResponse, chain);

		// then
		verify(inputStream, times(3)).read(buffer);
		verify(servletOutputStream, times(1)).write(buffer, 0, 1);
		verify(servletOutputStream, times(1)).write(buffer, 0, 2);
		verify(inputStream, times(1)).close();
	}

	@Test
	public void shouldSend404ResponseWhenRequestingNormalResourceAndItDoesntExist() throws Exception
	{
		// given
		given(httpRequest.getServletPath()).willReturn("/realFilename.jpg");
		given(mediaMgr.getMediaAsStream("root", "h01/h02/foo.jpg")).willThrow(new MediaNotFoundException("Media not found"));

		// when
		mediaFilter.doFilter(httpRequest, httpResponse, chain);

		// then
		verify(httpResponse, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
	}

	@Test
	public void shouldSend403WhenRequestTriesToAccessSecuredFolderViaNonSecureMediaFilter() throws Exception
	{
		// given
		// master|root|13355|text/plain|privatemedia/h67/hb0/8796158951454.txt|-
		given(httpRequest.getParameter("context"))
				.willReturn("bWFzdGVyfHJvb3R8MTMzNTV8dGV4dC9wbGFpbnxwcml2YXRlbWVkaWEvaDY3L2hiMC84Nzk2MTU4OTUxNDU0LnR4dHwt");

		given(httpRequest.getServletPath()).willReturn("/realFilename.jpg");
		given(Boolean.valueOf(mediaMgr.isFolderConfiguredAsSecured("root"))).willReturn(Boolean.TRUE);

		// when
		mediaFilter.doFilter(httpRequest, httpResponse, chain);

		// then
		verify(httpResponse, times(1)).setStatus(HttpServletResponse.SC_FORBIDDEN);
	}



	@Test
	public void shouldSend404ResponseWhenRequestingNormalResourceAndUnderlyingStorageStrategyWillThrowMediaNotFoundException()
			throws Exception
	{
		// given
		given(httpRequest.getServletPath()).willReturn("/realFilename.jpg");
		given(mediaMgr.getMediaAsStream("root", "h01/h02/foo.jpg")).willThrow(new MediaNotFoundException("Media not found"));

		// when
		mediaFilter.doFilter(httpRequest, httpResponse, chain);

		// then
		verify(httpResponse, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
	}


	@Test
	public void shouldServeMediaFileAsStreamWhenRequestingNormalResource() throws Exception
	{
		// given
		given(httpRequest.getServletPath()).willReturn("/realFilename.jpg");
		setUpMocksBehaviorForStandardMediaResource();

		// when
		mediaFilter.doFilter(httpRequest, httpResponse, chain);

		// then
		verifyMocksForStandardMediaResource();
	}

	@Test
	public void shouldServeMediaFileAsStreamWhenRequestingNormalResourceWithPrettyURL() throws Exception
	{
		// given
		prettyURLLegacyMode = true;
		given(httpRequest.getServletPath())
				.willReturn("medias/sys_master/folder/folderPath/h01/h02/123456789/pretty-url-needs-extra-work.jpg");
		setUpMocksBehaviorForStandardMediaResource();
		given(mediaMgr.getMediaAsStream("folder", "folderPath/h01/h02/123456789.jpg")).willReturn(inputStream);

		// when
		mediaFilter.doFilter(httpRequest, httpResponse, chain);

		// then
		verify(httpResponse, times(1)).setHeader("Last-Modified", LAST_MODIFIED_DATE);
		verify(httpResponse, times(1)).getOutputStream();
		verify(mediaMgr, times(1)).getMediaAsStream("folder", "folderPath/h01/h02/123456789.jpg");
	}

	@Test
	public void shouldServeMediaFileAsStreamWhenRequestingNormalResourceWithPrettyURLFromFolderWithFancyName() throws Exception
	{
		// given
		prettyURLLegacyMode = true;
		given(httpRequest.getServletPath())
				.willReturn("medias/sys_master/f-olde2_3r/folderPath/h01/h02/123456789/pretty-url-needs-extra-work.jpg");
		setUpMocksBehaviorForStandardMediaResource();
		given(mediaMgr.getMediaAsStream("f-olde2_3r", "folderPath/h01/h02/123456789.jpg")).willReturn(inputStream);

		// when
		mediaFilter.doFilter(httpRequest, httpResponse, chain);

		// then
		verify(httpResponse, times(1)).setHeader("Last-Modified", LAST_MODIFIED_DATE);
		verify(httpResponse, times(1)).getOutputStream();
		verify(mediaMgr, times(1)).getMediaAsStream("f-olde2_3r", "folderPath/h01/h02/123456789.jpg");
	}

	@Test
	public void shouldServeMediaFileAsStreamWhenRequestingNormalResourceWithPrettyURLFromFancyFolderPath() throws Exception
	{
		// given
		prettyURLLegacyMode = true;
		given(httpRequest.getServletPath())
				.willReturn("medias/sys_master/folder/_f_o-l2d3erPath_/h01/h02/123456789/pretty-url-needs-extra-work.jpg");
		setUpMocksBehaviorForStandardMediaResource();
		given(mediaMgr.getMediaAsStream("folder", "_f_o-l2d3erPath_/h01/h02/123456789.jpg")).willReturn(inputStream);

		// when
		mediaFilter.doFilter(httpRequest, httpResponse, chain);

		// then
		verify(httpResponse, times(1)).setHeader("Last-Modified", LAST_MODIFIED_DATE);
		verify(httpResponse, times(1)).getOutputStream();
		verify(mediaMgr, times(1)).getMediaAsStream("folder", "_f_o-l2d3erPath_/h01/h02/123456789.jpg");
	}

	@Test
	public void shouldServeMediaFileAsStreamWhenRequestingNormalResourceWithPrettyURLFromRootMediaFolder() throws Exception
	{
		// given
		prettyURLLegacyMode = true;
		given(httpRequest.getServletPath()).willReturn("medias/sys_master/root/h01/h02/123456789/pretty-url-needs-extra-work.jpg");
		setUpMocksBehaviorForStandardMediaResource();
		given(mediaMgr.getMediaAsStream("root", "h01/h02/123456789.jpg")).willReturn(inputStream);

		// when
		mediaFilter.doFilter(httpRequest, httpResponse, chain);

		// then
		verify(httpResponse, times(1)).setHeader("Last-Modified", LAST_MODIFIED_DATE);
		verify(httpResponse, times(1)).getOutputStream();
		verify(mediaMgr, times(1)).getMediaAsStream("root", "h01/h02/123456789.jpg");
	}

	@Test
	public void shouldSend400ResponseWhenRequestingResourceHasInvalidTenantIdPrefixForPrettyURL() throws Exception
	{
		// given
		prettyURLLegacyMode = true;
		given(httpRequest.getServletPath())
				.willReturn("medias/wrong_tenant_id/root/h01/h02/123456789/pretty-url-needs-extra-work.jpg");
		setUpMocksBehaviorForStandardMediaResource();

		// when
		mediaFilter.doFilter(httpRequest, httpResponse, chain);

		// then fine
        verify(httpResponse, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Test
	public void shouldServeMediaFileAsStreamWithContentDispositionHeaderWhenRequestingXLSFile() throws Exception
	{
		// given
		given(httpRequest.getServletPath()).willReturn("/realFilename.xls");
		setUpMocksBehaviorForStandardMediaResource();

		// when
		mediaFilter.doFilter(httpRequest, httpResponse, chain);

		// then
		verifyMocksForStandardMediaResource();
		verify(httpResponse, times(1)).addHeader("Content-Disposition", " attachment; filename=realFilename.xls");
	}

	@Test
	public void shouldSend304ResponseWhenRequestingResourceAndRequestContainMatchingETagHeader() throws Exception
	{
		// given
		given(httpRequest.getServletPath()).willReturn("/realFilename.jpg");
		given(httpRequest.getHeader("If-None-Match")).willReturn("8a7101697a29d819b3e6c2cb0063d1b8");
		given(config.getParameter("media.header.Last-Modified")).willReturn(LAST_MODIFIED_DATE);

		// when
		mediaFilter.doFilter(httpRequest, httpResponse, chain);

		// then
		verify(httpResponse, times(1)).setHeader("ETag", "8a7101697a29d819b3e6c2cb0063d1b8");
		verify(httpRequest, times(1)).getHeader("If-None-Match");
		verify(httpResponse, times(1)).setHeader("Last-Modified", LAST_MODIFIED_DATE);
		verify(httpResponse, times(1)).setStatus(HttpServletResponse.SC_NOT_MODIFIED);
	}

	@Test
	public void shouldProcessStandardResponseWhenRequestingResourceAndResourcePathContainETagValueButRequestDoesNotHaveIfNonMatch()
			throws Exception
	{
		// given
		given(httpRequest.getServletPath()).willReturn("/realFilename.jpg");
		given(httpRequest.getHeader("If-None-Match")).willReturn(null);
		setUpMocksBehaviorForStandardMediaResource();

		// when
		mediaFilter.doFilter(httpRequest, httpResponse, chain);

		// then
		verifyMocksForStandardMediaResource();
	}

	@Test
	public void shouldSendBadRequestResponseStatusIfMalformedURLExceptionWillBeThrown() throws Exception
	{
		// given
		given(httpRequest.getServletPath()).willReturn("/foobar.xls");
		setUpMocksBehaviorForStandardMediaResource();
		doThrow(new IllegalArgumentException()).when(httpResponse).addHeader("Content-Disposition",
				" attachment; filename=foobar.xls");

		// when
		mediaFilter.doFilter(httpRequest, httpResponse, chain);

		// then
		verify(httpResponse, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}

	@Test
	public void shouldAddConfiguredHeadersToResponse() throws Exception
	{
		// given
		configuredHeaders.put("cache-control", "max-age=3600");
		given(mediaMgr.getMediaHeadersRegistry()).willReturn(mediaHeadersRegistry);
		given(mediaHeadersRegistry.getHeaders()).willReturn(configuredHeaders);
		given(httpRequest.getServletPath()).willReturn("/realFilename.jpg");
		setUpMocksBehaviorForStandardMediaResource();

		// when
		mediaFilter.doFilter(httpRequest, httpResponse, chain);

		// then
		verifyMocksForStandardMediaResource();
		verify(httpResponse, times(1)).setHeader("cache-control", "max-age=3600");
	}

	private void setUpMocksBehaviorForStandardMediaResource() throws IOException
	{
		given(config.getParameter("media.force.download.dialog.fileextensions")).willReturn(FORCE_DOWNLOAD_DIALOG_FILE_EXTENSIONS);
		given(config.getParameter("media.header.Last-Modified")).willReturn(LAST_MODIFIED_DATE);
		given(mediaMgr.getMediaAsStream("root", "h01/h02/foo.jpg")).willReturn(inputStream);
		given(httpResponse.getOutputStream()).willReturn(outputStream);
	}

	private void verifyMocksForStandardMediaResource()
	{
		verify(httpResponse, times(1)).setContentLength(12345);
		verify(httpResponse, times(1)).setHeader("Last-Modified", LAST_MODIFIED_DATE);
		verify(httpResponse, times(0)).setStatus(HttpServletResponse.SC_FORBIDDEN);
		verify(httpResponse, times(0)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
		verify(httpResponse, times(0)).setStatus(HttpServletResponse.SC_NOT_FOUND);
	}

	@Test
	public void shouldProcessNormalFilterChainIfRequestOrResponseAreNotHttpServletRequest() throws Exception
	{
		// given
		final ServletRequest request = null;
		final ServletResponse response = null;

		// when
		mediaFilter.doFilter(request, response, chain);

		// then
		verify(chain, times(1)).doFilter(request, response);
	}

	private class TestMediaFilter extends WebAppMediaFilter
	{
		private InputStream resourceStream;

		public void setResourceStream(final InputStream resourceStream)
		{
			this.resourceStream = resourceStream;
		}

		@Override
		protected boolean isLegacyPrettyUrlSupport()
		{
			return prettyURLLegacyMode;
		}

		@Override
		protected void addContentType(final HttpServletResponse httpResponse, final Iterable<String> mediaContext,
				final String resourcePath)
		{
			// nothing
		}

		@Override
		protected ConfigIntf getConfig()
		{
			return config;
		}

		@Override
		protected InputStream getResourceAsStream(final String resourceName)
		{
			return resourceStream;
		}

		@Override
		protected boolean isMedia(final String resourcePath)
		{
			// TODO this is to get the (legacy) test working on the new filter -> should be adjusted and tested separately !!!
			return true;
		}

		@Override
		protected void setSecureURLRendererForThread(final HttpServletRequest httpRequest)
		{
			// TODO this test isn't ready for it yet - need to add it later !!!
		}

		@Override
		protected MediaManager getMediaManager()
		{
			return mediaMgr;
		}
	}
}
