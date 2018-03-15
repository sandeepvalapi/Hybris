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
package de.hybris.platform.impex.jalo.media;

import static org.fest.assertions.Assertions.assertThat;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.MediaUtil;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class DefaultMediaDataHandlerTest extends ServicelayerTransactionalBaseTest
{
	private static final String CODE = "fancy";
	private static final String FILENAME = "image.png";
	private static final String MIME = "image/png";
	private static final String PLATFORM_HOME = ConfigUtil.getPlatformConfig(DefaultMediaDataHandlerTest.class). //
			  getPlatformHome().getAbsolutePath();

	private DefaultMediaDataHandler dataHandler;
	@Resource
	private ModelService modelService;
	private CatalogVersionModel catalogVersion;
	private MediaModel mediaModel;
	private Media media;

	@Before
	public void setUp() throws Exception
	{
		prepareCatalogVersion();
		prepareMedia();
		dataHandler = new DefaultMediaDataHandler();

		assertThat(media.hasData()).isFalse();
	}

	private void prepareMedia()
	{
		mediaModel = modelService.create(MediaModel.class);
		mediaModel.setCode(CODE);
		mediaModel.setCatalogVersion(catalogVersion);
		mediaModel.setRealFileName(FILENAME);
		mediaModel.setMime(MIME);
		modelService.save(mediaModel);

		media = modelService.getSource(mediaModel);
	}

	private void prepareCatalogVersion()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId("my_favorite_catalog");
		modelService.save(catalog);

		catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setVersion("tolle_version");
		catalogVersion.setCatalog(catalog);
		modelService.save(catalogVersion);
	}

	@Test
	public void shouldSetDataOnMediaFromAbsolutePathJaloWay() throws Exception
	{
		// given
		dataHandler.setLegacyMode(true);
		final String path = "file:" + PLATFORM_HOME + "/ext/impex/resources/impex/testfiles/import/media/dummymedia/image.png";

		// when
		dataHandler.importData(media, path);

		// then
		assertThat(media.hasData()).isTrue();
	}

	@Test
	public void shouldSetDataOnMediaFromAbsolutePathSLWay() throws Exception
	{
		// given
		dataHandler.setLegacyMode(false);
		final String path = "file:" + PLATFORM_HOME + "/ext/impex/resources/impex/testfiles/import/media/dummymedia/image.png";

		// when
		dataHandler.importData(media, path);

		// then
		assertThat(media.hasData()).isTrue();
	}

	@Test
	public void shouldSetDataOnMediaFromClasspathJaloWayUsingItemClassClassloader() throws Exception
	{
		// given
		dataHandler.setLegacyMode(true);
		final String path = "jar:/impex/testfiles/import/media/dummymedia/image.png";

		// when
		dataHandler.importData(media, path);

		// then
		assertThat(media.hasData()).isTrue();
	}

	@Test
	public void shouldSetDataOnMediaFromClasspathSLWayUsingItemClassClassloader() throws Exception
	{
		// given
		dataHandler.setLegacyMode(false);
		final String path = "jar:/impex/testfiles/import/media/dummymedia/image.png";

		// when
		dataHandler.importData(media, path);

		// then
		assertThat(media.hasData()).isTrue();
	}

	@Test
	public void shouldSetDataOnMediaFromClasspathJaloWayUsingCustomClassClassloader() throws Exception
	{
		// given
		dataHandler.setLegacyMode(true);
		final String path = "jar:de.hybris.platform.impex.jalo.media" +
				  ".DefaultMediaDataHandlerTest&/impex/testfiles/import/media/dummymedia/image.png";

		// when
		dataHandler.importData(media, path);

		// then
		assertThat(media.hasData()).isTrue();
	}

	@Test
	public void shouldSetDataOnMediaFromClasspathSLWayUsingCustomClassClassloader() throws Exception
	{
		// given
		dataHandler.setLegacyMode(false);
		final String path = "jar:de.hybris.platform.impex.jalo.media" +
				  ".DefaultMediaDataHandlerTest&/impex/testfiles/import/media/dummymedia/image.png";

		// when
		dataHandler.importData(media, path);

		// then
		assertThat(media.hasData()).isTrue();
	}

	@Test
	public void shouldSetDataOnMediaFromZipFileInAbsoluteLocationJaloWay() throws Exception
	{
		// given
		dataHandler.setLegacyMode(true);
		final String path = "zip:" + PLATFORM_HOME + "/ext/impex/resources/impex/testfiles/import/media/dummymedia/package.zip" +
				  "&image.png";

		// when
		dataHandler.importData(media, path);

		// then
		assertThat(media.hasData()).isTrue();
	}

	@Test
	public void shouldSetDataOnMediaFromZipFileInAbsoluteLocationSLWay() throws Exception
	{
		// given
		dataHandler.setLegacyMode(false);
		final String path = "zip:" + PLATFORM_HOME + "/ext/impex/resources/impex/testfiles/import/media/dummymedia/package.zip" +
				  "&image.png";

		// when
		dataHandler.importData(media, path);

		// then
		assertThat(media.hasData()).isTrue();
	}

	@Test
	public void shouldSetExternalURLToMediaJaloWay() throws Exception
	{
		// given
		dataHandler.setLegacyMode(true);
		final String path = "http:http://www.fnordware.com/superpng/pnggrad16rgb.png";

		// when
		dataHandler.importData(media, path);

		// then
		assertThat(media.hasData()).isFalse();
		assertThat(media.getURL()).isEqualTo("http://www.fnordware.com/superpng/pnggrad16rgb.png");
	}

	@Test
	public void shouldSetExternalURLToMediaSLWay() throws Exception
	{
		// given
		dataHandler.setLegacyMode(false);
		final String path = "http:http://www.fnordware.com/superpng/pnggrad16rgb.png";

		// when
		dataHandler.importData(media, path);

		// then
		assertThat(media.hasData()).isFalse();
		assertThat(media.getURL()).isEqualTo("http://www.fnordware.com/superpng/pnggrad16rgb.png");
		assertThat(media.getMime()).isEqualTo("image/png");
	}

	@Test
	public void shouldSetExternalURLUsingFromJARToMediaJaloWay() throws Exception
	{
		// given
		dataHandler.setLegacyMode(true);
		final String path = "/medias/fromjar/impex/testfiles/import/media/dummymedia/image.png";

		// when
		dataHandler.importData(media, path);

		// then
		assertThat(media.hasData()).isFalse();
		assertThat(media.getURL()).isEqualTo(MediaUtil.getLocalMediaWebRootUrl() +
				  "/fromjar/impex/testfiles/import/media/dummymedia/image.png");
		assertThat(media.getMime()).isEqualTo("image/png");
	}

	@Test
	public void shouldSetExternalURLUsingFromJARToMediaSLWay() throws Exception
	{
		// given
		dataHandler.setLegacyMode(false);
		final String path = "/medias/fromjar/impex/testfiles/import/media/dummymedia/image.png";

		// when
		dataHandler.importData(media, path);

		// then
		assertThat(media.hasData()).isFalse();
		assertThat(media.getURL()).isEqualTo(MediaUtil.getLocalMediaWebRootUrl() +
				  "/fromjar/impex/testfiles/import/media/dummymedia/image.png");
		assertThat(media.getMime()).isEqualTo("image/png");
	}

}
