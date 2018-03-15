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
package de.hybris.platform.servicelayer.media.impl;


import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.media.LegacyMediaMigrator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.MediaUtil;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.fest.assertions.Assertions.assertThat;


@IntegrationTest
public class LegacyMediaMigratorTest extends ServicelayerBaseTest
{
	@Resource
	LegacyMediaMigrator legacyMediaMigrator;

	@Resource
	ModelService modelService;

	@Resource
	FlexibleSearchService flexibleSearchService;

	CatalogModel catalog;
	CatalogVersionModel catalogVersion;

	@Before
	public void setupCatalog()
	{
		catalog = modelService.create(CatalogModel.class);
		catalog.setId(UUID.randomUUID().toString());

		catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setVersion(UUID.randomUUID().toString());
		catalogVersion.setCatalog(catalog);
	}


	@Test
	public void shouldMigrateLegacyMedias()
	{
		// given
		createMediaWithoutDataPk();
		createMediaWithoutLocation();
		createNonLegacyMedia();

		assertThat(countLegacyMedias()).isEqualTo(2);

		// when
		legacyMediaMigrator.migrateMediaLocationAttribute();

		// then
		assertThat(countLegacyMedias()).isEqualTo(0);
	}

	@Test
	public void shouldMigrateMimeAttributesTranslation()
	{
		// given
		createMediaWithUnderscoreInsteadOfEmptyMime();

		// when
		legacyMediaMigrator.migrateLegacyMimeAndRealFileName();

		// then
		assertThat(getMediasWithMimeAsUnderscore()).isEmpty();
	}

	@Test
	public void shouldMigrateRealFilenameAttributesTranslation()
	{
		// given
		createMediaWithUnderscoreInsteadOfEmptyRealFilename();

		// when
		legacyMediaMigrator.migrateLegacyMimeAndRealFileName();

		// then
		assertThat(getMediasWithRealFilenameAsUnderscore()).isEmpty();
	}

	private int countLegacyMedias() {
		final List<Map<String, Object>> rows = new JdbcTemplate(Registry.getCurrentTenant().getDataSource()).queryForList(
				"SELECT  *  FROM junit_medias item_t0 WHERE ( item_t0.p_internalurl  = 'replicated273654712'  AND ( item_t0.p_datapk  IS NULL OR  item_t0.p_location  IS NULL))");
		return rows.size();
	}

	private List<Object> getMediasWithMimeAsUnderscore() {
		return flexibleSearchService.search("SELECT {" + MediaModel.PK + "} FROM {" + MediaModel._TYPECODE + "} WHERE {" + MediaModel.MIME + "}='_'").getResult();
	}

	private List<Object> getMediasWithRealFilenameAsUnderscore() {
		return flexibleSearchService.search("SELECT {" + MediaModel.PK + "} FROM {" + MediaModel._TYPECODE + "} WHERE {" + MediaModel.REALFILENAME + "}='_'").getResult();
	}

	private MediaModel createMediaWithUnderscoreInsteadOfEmptyMime()
	{
		final MediaModel mediaWithoutDataPK = modelService.create(MediaModel.class);
		mediaWithoutDataPK.setCode("MediaWithoutDataPK");
		mediaWithoutDataPK.setCatalogVersion(catalogVersion);
		mediaWithoutDataPK.setInternalURL(MediaUtil.URL_HAS_DATA);
		mediaWithoutDataPK.setMime("_");
		modelService.save(mediaWithoutDataPK);
		return mediaWithoutDataPK;
	}

	private MediaModel createMediaWithUnderscoreInsteadOfEmptyRealFilename()
	{
		final MediaModel mediaWithoutDataPK = modelService.create(MediaModel.class);
		mediaWithoutDataPK.setCode("MediaWithoutDataPK");
		mediaWithoutDataPK.setCatalogVersion(catalogVersion);
		mediaWithoutDataPK.setInternalURL(MediaUtil.URL_HAS_DATA);
		mediaWithoutDataPK.setRealFileName("_");
		modelService.save(mediaWithoutDataPK);
		return mediaWithoutDataPK;
	}

	private MediaModel createMediaWithoutDataPk()
	{
		final MediaModel mediaWithoutDataPK = modelService.create(MediaModel.class);
		mediaWithoutDataPK.setCode("MediaWithoutDataPK");
		mediaWithoutDataPK.setCatalogVersion(catalogVersion);
		mediaWithoutDataPK.setInternalURL(MediaUtil.URL_HAS_DATA);
		modelService.save(mediaWithoutDataPK);
		return mediaWithoutDataPK;
	}

	private MediaModel createMediaWithoutLocation()
	{
		final MediaModel mediaWithoutLocation = modelService.create(MediaModel.class);
		mediaWithoutLocation.setCode("MediaWithoutLocation");
		mediaWithoutLocation.setCatalogVersion(catalogVersion);
		mediaWithoutLocation.setInternalURL(MediaUtil.URL_HAS_DATA);
		mediaWithoutLocation.setDataPK(Long.valueOf(123));
		modelService.save(mediaWithoutLocation);
		return mediaWithoutLocation;
	}

	private MediaModel createNonLegacyMedia()
	{
		final MediaModel nonLegacyMedia = modelService.create(MediaModel.class);
		nonLegacyMedia.setCode("NonLegacyMedia");
		nonLegacyMedia.setCatalogVersion(catalogVersion);
		nonLegacyMedia.setInternalURL(MediaUtil.URL_HAS_DATA);
		nonLegacyMedia.setDataPK(Long.valueOf(55));
		nonLegacyMedia.setLocation("some-location");
		modelService.save(nonLegacyMedia);

		return nonLegacyMedia;
	}
}
