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
package de.hybris.platform.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaFolder;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.MediaUtil;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@ManualTest
public class SecureMediaFolderTest extends HybrisJUnit4TransactionalTest
{

	private static final Logger LOG = Logger.getLogger(SecureMediaFolderTest.class.getName());
	private MediaManager mediaManager;

	@Before
	public void setUp() throws Exception
	{
		mediaManager = jaloSession.getMediaManager();
	}

	@After
	public void tearDown() throws Exception
	{
		Config.setParameter("media.folder.securefolder.secured", "false");
		assertFalse(mediaManager.isSecuredFolder("securefolder"));
	}


	@Test
	public void testSecureFolder() throws Exception
	{
		final Media media = MediaManager.getInstance().createMedia("test");
		final MediaFolder folder = MediaManager.getInstance().createMediaFolder("securefolder", "securefolder");
		media.setData(new DataInputStream(new ByteArrayInputStream("secret message".getBytes())), "msg.txt", "text/plain", folder);
		assertEquals("secret message", new String(media.getData()));
		final String insecureURL = media.getURL();
		assertEquals(HttpServletResponse.SC_OK, getErrorCode(insecureURL));

		assertFalse(mediaManager.isSecuredFolder("securefolder"));
		Config.setParameter("media.folder.securefolder.secured", "true");
		assertTrue(mediaManager.isSecuredFolder("securefolder"));

		MediaUtil.setCurrentSecureMediaURLRenderer(media1 -> "<test>/securemedias?mediaPK=" + media1.getMediaPk());
		assertEquals("<test>/securemedias?mediaPK=" + media.getPK().toString(), media.getURL());
		assertEquals(HttpServletResponse.SC_FORBIDDEN, getErrorCode(insecureURL));
	}

	private int getErrorCode(final String mediaURL) throws IOException
	{
		final int port = Config.getInt("tomcat.http.port", 9001);
		final URL url = new URL("http", "127.0.0.1", port, mediaURL);
		final URLConnection connection = url.openConnection();

		try
		{
			connection.connect();
		}
		catch (final IOException e)
		{
			LOG.error("error connecting to " + url);
			throw e;
		}

		if (connection instanceof HttpURLConnection)
		{
			final HttpURLConnection httpConnection = (HttpURLConnection) connection;
			try
			{
				return httpConnection.getResponseCode();
			}
			finally
			{
				httpConnection.disconnect();
			}
		}
		else
		{
			throw new IllegalArgumentException("url " + url + " is not http");
		}
	}
}
