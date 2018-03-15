/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.test;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.media.url.impl.LocalMediaWebURLStrategy;
import de.hybris.platform.util.Config;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class MediaManagerIntegrationTest extends AbstractMediaManagerIntegrationTest
{
	private static final String DOWNLOAD_URL_PARAM = LocalMediaWebURLStrategy.ATTACHEMENT_PARAM + "=true";
	private MediaFolderModel securedMediaFolder;

	@Override
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		testMedia = testDataCreator.createMedia(catalogVersion);
		testMediaWithExternalUrl = testDataCreator.createMediaWithExternalUrl(catalogVersion);
		securedMediaFolder = testDataCreator.createMediaFolder("securedFolder");
	}

	@After
	public void tearDown() throws Exception
	{
		Config.setParameter("media.folder.securedFolder.secured", null);
	}

	@Test
	public void shouldReturnEmptyUrlWhenMediaIsInSecuredFolderButThereIsNoRendererInPlace() throws Exception
	{
		// given
		Config.setParameter("media.folder.securedFolder.secured", "true");
		final MediaModel media = testDataCreator.createMedia(securedMediaFolder, catalogVersion);

		// when
		final String urlForMedia = urlForMedia(media);

		// then
		assertThat(urlForMedia).isNotNull().isEmpty();
	}

	@Test
	public void shouldReturnSecureUrlWhenMediaIsInSecuredFolder() throws Exception
	{
		// given
		Config.setParameter("media.folder.securedFolder.secured", "true");
		final MediaModel media = testDataCreator.createMedia(securedMediaFolder, catalogVersion);

		// when
		final String urlForMedia = urlForMediaWithSecureRenderer(media, dontAddContextPath(), forDisplay());

		// then
		assertThat(urlForMedia).isNotNull();
		assertThat(urlForMedia).isEqualTo("medias/__secure__?mediaPK=" + media.getPk());
	}

	@Test
	public void shouldReturnSecureUrlWithWebAppCtxPrefixWhenMediaIsInSecuredFolder() throws Exception
	{
		// given
		Config.setParameter("media.folder.securedFolder.secured", "true");
		final MediaModel media = testDataCreator.createMedia(securedMediaFolder, catalogVersion);

		// when
		final String urlForMedia = urlForMediaWithSecureRenderer(media, addContextPath(), forDisplay());

		// then
		assertThat(urlForMedia).isNotNull();
		assertThat(urlForMedia).isEqualTo("/testwebapp/medias/__secure__?mediaPK=" + media.getPk());
	}

	@Test
	public void shouldReturnSecureDownloadUrlWhenMediaIsInSecuredFolder() throws Exception
	{
		// given
		Config.setParameter("media.folder.securedFolder.secured", "true");
		final MediaModel media = testDataCreator.createMedia(securedMediaFolder, catalogVersion);

		// when
		final String urlForMedia = urlForMediaWithSecureRenderer(media, dontAddContextPath(), forDownload());

		// then
		assertThat(urlForMedia).isNotNull();
		assertThat(urlForMedia).isEqualTo("medias/__secure__?mediaPK=" + media.getPk() + "&" + DOWNLOAD_URL_PARAM);
	}

	@Test
	public void shouldReturnSecureDownloadUrlWithWebAppCtxWhenMediaIsInSecuredFolder() throws Exception
	{
		// given
		Config.setParameter("media.folder.securedFolder.secured", "true");
		final MediaModel media = testDataCreator.createMedia(securedMediaFolder, catalogVersion);

		// when
		final String urlForMedia = urlForMediaWithSecureRenderer(media, addContextPath(), forDownload());

		// then
		assertThat(urlForMedia).isNotNull();
		assertThat(urlForMedia).isEqualTo("/testwebapp/medias/__secure__?mediaPK=" + media.getPk() + "&" + DOWNLOAD_URL_PARAM);
	}

	@Test
	public void shouldReturnInternalUrlForMediaWithoutData() throws Exception
	{
		// when
		final String urlForMedia = urlForMedia(testMediaWithExternalUrl);

		// then
		assertThat(urlForMedia).isNotNull();
		assertThat(urlForMedia).isEqualTo(testMediaWithExternalUrl.getInternalURL());
	}

	@Test
	public void shouldReturnInternalDownloadUrlForMediaWithoutData() throws Exception
	{
		// when
		final String urlForMedia = downloadUrlForMedia(testMediaWithExternalUrl);

		// then
		assertThat(urlForMedia).isNotNull();
		assertThat(urlForMedia).isEqualTo(testMediaWithExternalUrl.getInternalURL());
	}

	@Test
	public void shouldReturnMediaUrlWithoutWebAppContextRootPrefix() throws Exception
	{
		// when
		final String urlForMedia = urlForMedia(testMedia);

		// then
		assertThat(urlForMedia).isNotNull();
		assertThat(urlForMedia).startsWith("/medias/?context=");
	}

	@Test
	public void shouldReturnMediaDownloadUrlWithoutWebAppContextRootPrefix() throws Exception
	{
		// when
		final String urlForMedia = downloadUrlForMedia(testMedia);

		// then
		assertThat(urlForMedia).isNotNull();
		assertThat(urlForMedia).startsWith("/medias/?context=");
		assertThat(urlForMedia).endsWith(DOWNLOAD_URL_PARAM);
	}

	@Test
	public void shouldReturnMediaUrltRootPrefix() throws Exception
	{
		// when
		final String urlForMedia = urlForMediaWithRenderer(testMedia, dontAddContextPath(), forDisplay());

		// then
		assertThat(urlForMedia).isNotNull();
		assertThat(urlForMedia).startsWith("/medias/?context=");
	}

	@Test
	public void shouldReturnMediaDownloadUrlRootPrefix() throws Exception
	{
		// when
		final String urlForMedia = urlForMediaWithRenderer(testMedia, dontAddContextPath(), forDownload());

		// then
		assertThat(urlForMedia).isNotNull();
		assertThat(urlForMedia).startsWith("/medias/?context=");
		assertThat(urlForMedia).endsWith(DOWNLOAD_URL_PARAM);
	}

	@Test
	public void shouldReturnMediaUrlWithWebAppContextRootPrefix() throws Exception
	{
		// when
		final String urlForMedia = urlForMediaWithRenderer(testMedia, addContextPath(), forDisplay());

		// then
		assertThat(urlForMedia).isNotNull();
		assertThat(urlForMedia).startsWith("/testwebapp/medias/?context=");
	}

	@Test
	public void shouldReturnMediaDownloadUrlWithWebAppContextRootPrefix() throws Exception
	{
		// when
		final String urlForMedia = urlForMediaWithRenderer(testMedia, addContextPath(), forDownload());

		// then
		assertThat(urlForMedia).isNotNull();
		assertThat(urlForMedia).startsWith("/testwebapp/medias/?context=");
		assertThat(urlForMedia).endsWith(DOWNLOAD_URL_PARAM);
	}

}
