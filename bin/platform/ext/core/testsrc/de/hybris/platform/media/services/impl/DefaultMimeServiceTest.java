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
package de.hybris.platform.media.services.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.media.services.MimeService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class DefaultMimeServiceTest
{
	private MimeService mimeService;

	@Before
	public void setUp() throws Exception
	{
		mimeService = new DefaultMimeService()
		{
			@Override
			protected String getConfigParameter(final String key)
			{
				if ("image/jpeg".equals(key))
				{
					return "jpg";
				}
				else if ("image/png".equals(key))
				{
					return "png";
				}
				else if ("media.customextension.image.jpeg".equals(key))
				{
					return "jpg";
				}
				else if ("mediatype.by.fileextension.jpg".equals(key))
				{
					return "image/jpeg";
				}
				return null;
			}

			@Override
			protected Map<String, String> getConfigParametersByPattern(final String keyPrefix)
			{
				final Map<String, String> result = new HashMap<String, String>();
				result.put("media.customextension.image.jpeg", "jpg");
				result.put("media.customextension.image.png", "png");
				return result;
			}

		};

		((DefaultMimeService) mimeService).afterPropertiesSet();
	}

	@Test
	public void shouldReturnFallbackFileExtensionFormMimeAsBestOneIfItIsNotConfiguredInProperties()
	{
		// given
		final String mime = "not-configured/mime";

		// when
		final String fileExtension = mimeService.getBestExtensionFromMime(mime);

		// then
		assertThat(fileExtension).isNotNull().isEqualTo(DefaultMimeService.FALLBACK_FILE_EXT);
	}

	@Test
	public void shouldReturnFallbackFileExtensionAsBestOneFromBlankMime()
	{
		// given
		final String mime = "";

		// when
		final String fileExtension = mimeService.getBestExtensionFromMime(mime);

		// then
		assertThat(fileExtension).isNotNull().isEqualTo(DefaultMimeService.FALLBACK_FILE_EXT);
	}

	@Test
	public void shouldReturnFallbackFileExtensionAsBestOneFromNullMime()
	{
		// given
		final String mime = null;

		// when
		final String fileExtension = mimeService.getBestExtensionFromMime(mime);

		// then
		assertThat(fileExtension).isNotNull().isEqualTo(DefaultMimeService.FALLBACK_FILE_EXT);
	}


	@Test
	public void shouldReturnConfiguredFileExtensionAsBestOneFromConfiguredMime()
	{
		// given
		final String mime = "image/jpeg";

		// when
		final String fileExtension = mimeService.getBestExtensionFromMime(mime);

		// then
		assertThat(fileExtension).isNotNull().isEqualTo("jpg");
	}


	@Test
	public void shouldReturnNullFileExtensionForBlankMime()
	{
		// given
		final String mime = "";

		// when
		final String fileExtension = mimeService.getFileExtensionFromMime(mime);

		// then
		assertThat(fileExtension).isNull();
	}

	@Test
	public void shouldReturnNullFileExtensionForNullMime()
	{
		// given
		final String mime = null;

		// when
		final String fileExtension = mimeService.getFileExtensionFromMime(mime);

		// then
		assertThat(fileExtension).isNull();
	}

	@Test
	public void shouldReturnConfiguredFileExtensionFromConfiguredMime()
	{
		// given
		final String mime = "image/jpeg";

		// when
		final String fileExtension = mimeService.getFileExtensionFromMime(mime);

		// then
		assertThat(fileExtension).isNotNull().isEqualTo("jpg");
	}


	@Test
	public void shouldReturnFallbackMimeAsBestMimeWhenMimeFromFileExtBytesAndOverrideIsBlank()
	{
		// given
		final String fileName = null;
		final byte[] firstBytes = null;
		final String overrideMime = "nonexistent/mime";

		// when
		final String bestMime = mimeService.getBestMime(fileName, firstBytes, overrideMime);

		// then
		assertThat(bestMime).isNotNull().isEqualTo(DefaultMimeService.FALLBACK_MIME);
	}

	@Test
	public void shouldReturnMimeFromFilenameAsBestMimeWhenMimeFromFileNameIsPresent()
	{
		// given
		final String fileName = "FileName.jpg";
		final byte[] firstBytes =
		{ -119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 2, -68 };
		final String overrideMime = "image/jpeg";

		// when
		final String bestMime = mimeService.getBestMime(fileName, firstBytes, overrideMime);

		// then
		assertThat(bestMime).isNotNull().isEqualTo("image/jpeg");
	}

	@Test
	public void shouldReturnMimeFromOverridingMimeAsBestMimeWhenMimeFromFileNameAndBytesIsNotPresentButOverridingMimeIsConfigured()
	{
		// given
		final String fileName = null;
		final byte[] firstBytes = null;
		final String overrideMime = "image/jpeg";

		// when
		final String bestMime = mimeService.getBestMime(fileName, firstBytes, overrideMime);

		// then
		assertThat(bestMime).isNotNull().isEqualTo("image/jpeg");
	}

	@Test
	public void shouldReturnMimeFromBytesAsBestMimeWhenMimeFromFileNameIsNotPresent()
	{
		// given
		final String fileName = null;
		// These bytes are taken from real png image
		final byte[] firstBytes =
		{ -119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 2, -68 };
		final String overrideMime = "image/jpeg";

		// when
		final String bestMime = mimeService.getBestMime(fileName, firstBytes, overrideMime);

		// then
		assertThat(bestMime).isNotNull().isEqualTo("image/png");
	}


	@Test
	public void shouldReturnNullMimeWhenFileExtensionIsNotConfigured()
	{
		// given
		final String fileName = "NotConfigured.ext";

		// when
		final String mime = mimeService.getMimeFromFileExtension(fileName);

		// then
		assertThat(mime).isNull();
	}

	@Test
	public void shouldReturnMimeWhenFileExtensionIsConfigured()
	{
		// given
		final String fileName = "Configured.jpg";

		// when
		final String mime = mimeService.getMimeFromFileExtension(fileName);

		// then
		assertThat(mime).isNotNull().isEqualTo("image/jpeg");
	}

	@Test
	public void shouldReturnNullMimeWhenBytesDontContainMimeInfo()
	{
		// given
		final byte[] firstBytes =
		{ 0, 1, 2, 3, 4 };

		// when
		final String mime = mimeService.getMimeFromFirstBytes(firstBytes);

		// then
		assertThat(mime).isNull();
	}

	@Test
	public void shouldReturnMimeWhenBytesContainMimeInfo()
	{
		// given
		final byte[] firstBytes =
		{ -119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 2, -68 };

		// when
		final String mime = mimeService.getMimeFromFirstBytes(firstBytes);

		// then
		assertThat(mime).isNotNull().isEqualTo("image/png");
	}


	@Test
	public void shouldReturnSupportedMimeTypes()
	{
		// when
		final Set<String> supportedMimeTypes = mimeService.getSupportedMimeTypes();

		// then
		assertThat(supportedMimeTypes).isNotNull().isNotEmpty().containsOnly("image/jpeg", "image/png");
	}


	@Test
	public void shouldCheckWheterMimeIsZipRelatedMime()
	{
		// given
		final String applicationZip = "application/zip";
		final String applicationXZip = "application/x-zip";
		final String applicationXZipCompressed = "application/x-zip-compressed";
		final String applicationOctetStream = "application/octet-stream";

		// when
		final boolean zipRelated1 = mimeService.isZipRelatedMime(applicationZip);
		final boolean zipRelated2 = mimeService.isZipRelatedMime(applicationXZip);
		final boolean zipRelated3 = mimeService.isZipRelatedMime(applicationXZipCompressed);
		final boolean zipRelated4 = mimeService.isZipRelatedMime(applicationOctetStream);
		final boolean zipRelated5 = mimeService.isZipRelatedMime(null);
		final boolean zipRelated6 = mimeService.isZipRelatedMime("");

		// then
		assertThat(zipRelated1).isTrue();
		assertThat(zipRelated2).isTrue();
		assertThat(zipRelated3).isTrue();
		assertThat(zipRelated4).isFalse();
		assertThat(zipRelated5).isFalse();
		assertThat(zipRelated6).isFalse();
	}

	@Test
	public void shouldGetProperMimeTypeForPdfWithoutBOM()
	{
		final String applicationPdf = "application/pdf";
		final byte[] pdfWithoutBOM = new byte[]
		{ 37, 80, 68, 70, 45, 49, 46, 51, 10, 37, -60, -27, -14, -27, -21, -89, -13, -96, -48, -60 };

		final String mime = mimeService.getMimeFromFirstBytes(pdfWithoutBOM);

		assertThat(mime).isNotNull().isEqualToIgnoringCase(applicationPdf);
	}

	@Test
	public void shouldGetProperMimeTypeForPdfWithBOM()
	{
		final String applicationPdf = "application/pdf";
		final byte[] pdfWithBOM = new byte[]
		{ -17, -69, -65, 37, 80, 68, 70, 45, 49, 46, 50, 13, 37, -30, -29, -49, -45, 13, 10, 53 };

		final String mime = mimeService.getMimeFromFirstBytes(pdfWithBOM);

		assertThat(mime).isNotNull().isEqualToIgnoringCase(applicationPdf);
	}
}
