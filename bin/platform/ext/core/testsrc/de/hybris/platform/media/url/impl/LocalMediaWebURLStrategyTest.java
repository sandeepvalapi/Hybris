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
package de.hybris.platform.media.url.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.media.MediaSource;
import de.hybris.platform.media.storage.MediaStorageConfigService.MediaFolderConfig;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;


@UnitTest
public class LocalMediaWebURLStrategyTest
{

	private final LocalMediaWebURLStrategy strategy = new LocalMediaWebURLStrategy()
	{
		@Override
		protected String getTenantId()
		{
			return "master";
		}
	};
	@Mock
	private MediaSource mediaSource;
	@Mock
	private MediaFolderConfig folderConfig;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		given(mediaSource.getRealFileName()).willReturn("realFilename.jpg");
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenGettingUrlForMediaAndFolderQualifierIsNull()
	{
		// given
		final MediaFolderConfig folderConfig = null;

		try
		{
			// when
			strategy.getUrlForMedia(folderConfig, mediaSource);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("Folder config is required to perform this operation");
		}
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenGettingUrlForMediaAndMediaIsNull()
	{
		// given
		final MediaSource media = null;

		try
		{
			// when
			strategy.getUrlForMedia(folderConfig, media);
			fail("Should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then
			assertThat(e).hasMessage("MediaSource is required to perform this operation");
		}
	}


	@Test
	public void shouldReturnValidUrlForGivenMediaWithEncodedFullContextWithNormalizedRealFilename()
	{
		// given
		given(folderConfig.getFolderQualifier()).willReturn("root");
		given(mediaSource.getLocation()).willReturn("h01/h02/foo.jpg");
		given(mediaSource.getSize()).willReturn(Long.valueOf(12345));
		given(mediaSource.getLocationHash()).willReturn("qwerty12345");
		given(mediaSource.getMime()).willReturn("image/jpeg");
		given(mediaSource.getRealFileName()).willReturn("real%%Filename-_za��������G����---l��Ja����/\\!@#$%^&*()-+=\"w.jpg");

		// when
		final String urlForMedia = strategy.getUrlForMedia(folderConfig, mediaSource);

		// then
		assertThat(urlForMedia).isNotNull();
		assertThat(urlForMedia).isEqualTo(
				"/medias/real-Filename-za-G-l-Ja-w"
						+ ".jpg?context=bWFzdGVyfHJvb3R8MTIzNDV8aW1hZ2UvanBlZ3xoMDEvaDAyL2Zvby5qcGd8cXdlcnR5MTIzNDU");
		final Map<String, String> urlParameters = getUrlParameters(urlForMedia);
		assertThat(urlParameters.get("context")).isNotNull();
		assertThat(decodeBase64(urlParameters.get("context")))
				.isEqualTo("master|root|12345|image/jpeg|h01/h02/foo.jpg|qwerty12345");
	}

	@Test
	public void shouldReturnValidUrlForGivenFolderAndMediaWithEncodedFullContext()
	{
		// given
		given(folderConfig.getFolderQualifier()).willReturn("root");
		given(mediaSource.getLocation()).willReturn("h01/h02/foo.jpg");
		given(mediaSource.getSize()).willReturn(Long.valueOf(12345));
		given(mediaSource.getLocationHash()).willReturn("qwerty12345");
		given(mediaSource.getMime()).willReturn("image/jpeg");

		// when
		final String urlForMedia = strategy.getUrlForMedia(folderConfig, mediaSource);

		// then
		assertThat(urlForMedia).isNotNull();
		assertThat(urlForMedia).isEqualTo(
				"/medias/realFilename.jpg?context=bWFzdGVyfHJvb3R8MTIzNDV8aW1hZ2UvanBlZ3xoMDEvaDAyL2Zvby5qcGd8cXdlcnR5MTIzNDU");
		final Map<String, String> urlParameters = getUrlParameters(urlForMedia);
		assertThat(urlParameters.get("context")).isNotNull();
		assertThat(decodeBase64(urlParameters.get("context")))
				.isEqualTo("master|root|12345|image/jpeg|h01/h02/foo.jpg|qwerty12345");
	}

	@Test
	public void shouldReturnValidUrlForGivenFolderAndMediaWithEncodedFullContextWithFixedFolderQualifier()
	{
		// given
		given(folderConfig.getFolderQualifier()).willReturn("ro|ot");
		given(mediaSource.getLocation()).willReturn("h01/h02/foo.jpg");
		given(mediaSource.getSize()).willReturn(Long.valueOf(12345));
		given(mediaSource.getLocationHash()).willReturn("qwerty12345");
		given(mediaSource.getMime()).willReturn("image/jpeg");

		// when
		final String urlForMedia = strategy.getUrlForMedia(folderConfig, mediaSource);

		// then
		assertThat(urlForMedia).isNotNull();
		assertThat(urlForMedia).isEqualTo(
				"/medias/realFilename.jpg?context=bWFzdGVyfHJvb3R8MTIzNDV8aW1hZ2UvanBlZ3xoMDEvaDAyL2Zvby5qcGd8cXdlcnR5MTIzNDU");
		final Map<String, String> urlParameters = getUrlParameters(urlForMedia);
		assertThat(urlParameters.get("context")).isNotNull();
		assertThat(decodeBase64(urlParameters.get("context")))
				.isEqualTo("master|root|12345|image/jpeg|h01/h02/foo.jpg|qwerty12345");
	}

	@Test
	public void shouldReturnValidUrlWithoutRealFileNameForGivenFolderAndMediaWithEncodedFullContextWhenRealFileNameIsNull()
	{
		// given
		given(folderConfig.getFolderQualifier()).willReturn("root");
		given(mediaSource.getLocation()).willReturn("h01/h02/foo.jpg");
		given(mediaSource.getSize()).willReturn(Long.valueOf(12345));
		given(mediaSource.getLocationHash()).willReturn("qwerty12345");
		given(mediaSource.getMime()).willReturn("image/jpeg");
		given(mediaSource.getRealFileName()).willReturn(null);

		// when
		final String urlForMedia = strategy.getUrlForMedia(folderConfig, mediaSource);

		// then
		assertThat(urlForMedia).isNotNull();
		assertThat(urlForMedia).isEqualTo(
				"/medias/?context=bWFzdGVyfHJvb3R8MTIzNDV8aW1hZ2UvanBlZ3xoMDEvaDAyL2Zvby5qcGd8cXdlcnR5MTIzNDU");
		final Map<String, String> urlParameters = getUrlParameters(urlForMedia);
		assertThat(urlParameters.get("context")).isNotNull();
		assertThat(decodeBase64(urlParameters.get("context")))
				.isEqualTo("master|root|12345|image/jpeg|h01/h02/foo.jpg|qwerty12345");
	}

	@Test
	public void shouldReturnValidUrlForGivenFolderAndMediaWithEncodedContextWithNoMimeMarkerWhenMimeIsNull()
	{
		// given
		given(folderConfig.getFolderQualifier()).willReturn("root");
		given(mediaSource.getLocation()).willReturn("h01/h02/foo.jpg");
		given(mediaSource.getSize()).willReturn(Long.valueOf(12345));
		given(mediaSource.getLocationHash()).willReturn("qwerty12345");
		given(mediaSource.getMime()).willReturn(null);

		// when
		final String urlForMedia = strategy.getUrlForMedia(folderConfig, mediaSource);

		// then
		assertThat(urlForMedia).isNotNull();
		assertThat(urlForMedia).isEqualTo(
				"/medias/realFilename.jpg?context=bWFzdGVyfHJvb3R8MTIzNDV8LXxoMDEvaDAyL2Zvby5qcGd8cXdlcnR5MTIzNDU");
		final Map<String, String> urlParameters = getUrlParameters(urlForMedia);
		assertThat(urlParameters.get("context")).isNotNull();
		assertThat(decodeBase64(urlParameters.get("context"))).isEqualTo("master|root|12345|-|h01/h02/foo.jpg|qwerty12345");
	}

	@Test
	public void shouldReturnValidDownloadUrlForGivenFolder()
	{
		// given
		given(folderConfig.getFolderQualifier()).willReturn("root");
		given(mediaSource.getLocation()).willReturn("h01/h02/foo.jpg");
		given(mediaSource.getSize()).willReturn(Long.valueOf(12345));
		given(mediaSource.getLocationHash()).willReturn("qwerty12345");
		given(mediaSource.getMime()).willReturn("image/jpeg");

		// when
		final String urlForMedia = strategy.getDownloadUrlForMedia(folderConfig, mediaSource);

		// then
		assertThat(urlForMedia).isNotNull();
		assertThat(urlForMedia)
				.isEqualTo(
						"/medias/realFilename.jpg?context=bWFzdGVyfHJvb3R8MTIzNDV8aW1hZ2UvanBlZ3xoMDEvaDAyL2Zvby5qcGd8cXdlcnR5MTIzNDU&attachment=true");
		final Map<String, String> urlParameters = getUrlParameters(urlForMedia);
		assertThat(urlParameters.get("context")).isNotNull();
		assertThat(decodeBase64(urlParameters.get("context")))
				.isEqualTo("master|root|12345|image/jpeg|h01/h02/foo.jpg|qwerty12345");
	}

	@Test
	public void shouldReturnValidUrlForGivenFolderAndMediaWithEncodedContextWithNotSecuredMarkerWhenThereIsNoLocationHash()
	{
		// given
		given(folderConfig.getFolderQualifier()).willReturn("root");
		given(mediaSource.getLocation()).willReturn("h01/h02/foo.jpg");
		given(mediaSource.getSize()).willReturn(Long.valueOf(12345));
		given(mediaSource.getLocationHash()).willReturn(null);
		given(mediaSource.getMime()).willReturn("image/jpeg");

		// when
		final String urlForMedia = strategy.getUrlForMedia(folderConfig, mediaSource);

		// then
		assertThat(urlForMedia).isNotNull();
		assertThat(urlForMedia).isEqualTo(
				"/medias/realFilename.jpg?context=bWFzdGVyfHJvb3R8MTIzNDV8aW1hZ2UvanBlZ3xoMDEvaDAyL2Zvby5qcGd8LQ");
		final Map<String, String> urlParameters = getUrlParameters(urlForMedia);
		assertThat(urlParameters.get("context")).isNotNull();
		assertThat(decodeBase64(urlParameters.get("context"))).isEqualTo("master|root|12345|image/jpeg|h01/h02/foo.jpg|-");
	}

	@Test
	public void shouldReturnValidPrettyUrlForRootMediaFolder() throws Exception
	{
		// given
		given(folderConfig.getFolderQualifier()).willReturn("root");
		given(mediaSource.getLocation()).willReturn("h01/h02/foo.jpg");
		given(mediaSource.getSize()).willReturn(Long.valueOf(12345));
		given(mediaSource.getLocationHash()).willReturn("qwerty12345");
		given(mediaSource.getMime()).willReturn("image/jpeg");

		// when
		strategy.setPrettyUrlEnabled(true);
		final String urlForMedia = strategy.getUrlForMedia(folderConfig, mediaSource);

		// then
		assertThat(urlForMedia).isEqualTo("/medias/sys_master/root/h01/h02/foo/realFilename.jpg");
	}

	private Map<String, String> getUrlParameters(final String url)
	{
		final Map<String, String> ret = new HashMap<String, String>();

		final Splitter urlSplitter = Splitter.on("?");
		final Iterable<String> splittedUrl = urlSplitter.split(url);

		final Splitter paramSplitter = Splitter.on("&");
		final Iterable<String> paramsString = paramSplitter.split(Iterables.get(splittedUrl, 1));

		final Splitter keyValSplitter = Splitter.on("=");
		for (final String keyVal : paramsString)
		{
			final Iterable<String> splittedKeyVal = keyValSplitter.split(keyVal);
			ret.put(Iterables.get(splittedKeyVal, 0), Iterables.get(splittedKeyVal, 1));
		}

		return ret;
	}

	private String decodeBase64(final String value)
	{
		String decodedValue = "";
		if (StringUtils.isNotBlank(value))
		{
			try
			{
				decodedValue = new String(new Base64(-1, null, true).decode(value));
			}
			catch (final Exception e)
			{
				throw new RuntimeException("Cannot decode base32 coded string: " + value);
			}
		}
		return decodedValue;
	}

}
