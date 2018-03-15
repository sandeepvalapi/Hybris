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
package de.hybris.platform.catalog;


import static org.fest.assertions.Assertions.assertThat;

import de.hybris.platform.catalog.model.AgreementModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.internal.model.impl.PersistenceTestUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;


public class CatalogSLDTest extends ServicelayerBaseTest
{
	@Resource
	ModelService modelService;

	private static String asUUID()
	{
		return UUID.randomUUID().toString();
	}

	private final PropertyConfigSwitcher persistenceLegacyModeSwitch = new PropertyConfigSwitcher("persistence.legacy.mode");

	@Before
	public void enableDirectPersistence()
	{
		persistenceLegacyModeSwitch.switchToValue("false");
	}

	@After
	public void resetPersistence()
	{
		persistenceLegacyModeSwitch.switchBackToDefault();
	}

	@Test
	public void shouldSetActiveCatalog()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(asUUID());

		final CatalogVersionModel version1 = modelService.create(CatalogVersionModel.class);
		version1.setCatalog(catalog);
		version1.setVersion(asUUID());

		final CatalogVersionModel version2 = modelService.create(CatalogVersionModel.class);
		version2.setCatalog(catalog);
		version2.setVersion(asUUID());

		modelService.saveAll(catalog, version1, version2);

		assertThat(version1.getActive()).isFalse();
		assertThat(version2.getActive()).isFalse();

		catalog.setActiveCatalogVersion(version2);
		modelService.saveAll();

		assertThat(version1.getActive()).isFalse();
		assertThat(version2.getActive()).isTrue();
	}

	@Test
	public void shouldReplaceDefaultCatalogViaModelService()
	{
		final CatalogModel defaultCatalog = modelService.create(CatalogModel.class);
		defaultCatalog.setId(asUUID());
		defaultCatalog.setDefaultCatalog(Boolean.TRUE);

		modelService.saveAll();

		assertThat(defaultCatalog.getDefaultCatalog()).isTrue();

		final CatalogModel newCatalog = modelService.create(CatalogModel.class);
		newCatalog.setId(asUUID());
		newCatalog.setDefaultCatalog(Boolean.TRUE);

		modelService.saveAll();

		assertThat(defaultCatalog.getDefaultCatalog()).isFalse();
		assertThat(newCatalog.getDefaultCatalog()).isTrue();
	}

	@Test
	public void shouldGetRootCategories()
	{
		final CatalogModel defaultCatalog = modelService.create(CatalogModel.class);
		defaultCatalog.setId(asUUID());
		defaultCatalog.setDefaultCatalog(Boolean.TRUE);

		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setCatalog(defaultCatalog);
		catalogVersion.setVersion(asUUID());
		catalogVersion.setActive(Boolean.TRUE);

		final CategoryModel rootCategory1 = modelService.create(CategoryModel.class);
		rootCategory1.setCode(asUUID());
		rootCategory1.setCatalogVersion(catalogVersion);

		final CategoryModel rootCategory2 = modelService.create(CategoryModel.class);
		rootCategory2.setCode(asUUID());
		rootCategory2.setCatalogVersion(catalogVersion);

		final CategoryModel subCategory1 = modelService.create(CategoryModel.class);
		subCategory1.setCode(asUUID());
		subCategory1.setCatalogVersion(catalogVersion);
		subCategory1.setSupercategories(ImmutableList.of(rootCategory1));

		final CategoryModel subCategory2 = modelService.create(CategoryModel.class);
		subCategory2.setCode(asUUID());
		subCategory2.setCatalogVersion(catalogVersion);
		subCategory2.setSupercategories(ImmutableList.of(rootCategory1));

		final CategoryModel subCategory3 = modelService.create(CategoryModel.class);
		subCategory3.setCode(asUUID());
		subCategory3.setCatalogVersion(catalogVersion);
		subCategory3.setSupercategories(ImmutableList.of(rootCategory2));

		modelService.saveAll();

		final List<CategoryModel> rootCategories = defaultCatalog.getRootCategories();
		assertThat(rootCategories).hasSize(2);
	}


	@Test
	public void shouldGetActiveCatalogs()
	{
		final CatalogModel defaultCatalog = modelService.create(CatalogModel.class);
		defaultCatalog.setId(asUUID());
		defaultCatalog.setDefaultCatalog(Boolean.TRUE);

		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setCatalog(defaultCatalog);
		catalogVersion.setVersion(asUUID());
		catalogVersion.setActive(Boolean.TRUE);

		final CatalogVersionModel catalogVersion2 = modelService.create(CatalogVersionModel.class);
		catalogVersion2.setCatalog(defaultCatalog);
		catalogVersion2.setVersion(asUUID());

		final CatalogVersionModel catalogVersion3 = modelService.create(CatalogVersionModel.class);
		catalogVersion3.setCatalog(defaultCatalog);
		catalogVersion3.setVersion(asUUID());

		modelService.saveAll();

		final CatalogVersionModel activeCatalogVersion = defaultCatalog.getActiveCatalogVersion();
		assertThat(activeCatalogVersion).isEqualTo(catalogVersion);
	}

	@Test
	public void shouldGetVersion()
	{
		final CatalogModel defaultCatalog = modelService.create(CatalogModel.class);
		defaultCatalog.setId(asUUID());
		defaultCatalog.setDefaultCatalog(Boolean.TRUE);

		final CatalogVersionModel inactiveCatalogVersion = modelService.create(CatalogVersionModel.class);
		inactiveCatalogVersion.setCatalog(defaultCatalog);
		inactiveCatalogVersion.setVersion(asUUID());

		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setCatalog(defaultCatalog);
		catalogVersion.setVersion(asUUID());
		catalogVersion.setActive(Boolean.TRUE);

		modelService.saveAll();

		assertThat(defaultCatalog.getVersion()).isEqualTo(catalogVersion.getVersion());
	}

	@Test
	public void shouldGetActiveVersionAttributes()
	{
		final CatalogModel defaultCatalog = modelService.create(CatalogModel.class);
		defaultCatalog.setId(asUUID());
		defaultCatalog.setDefaultCatalog(Boolean.TRUE);

		final CatalogVersionModel inactiveCatalogVersion = modelService.create(CatalogVersionModel.class);
		inactiveCatalogVersion.setCatalog(defaultCatalog);
		inactiveCatalogVersion.setVersion(asUUID());

		final CurrencyModel currency = modelService.create(CurrencyModel.class);
		currency.setIsocode(asUUID());
		currency.setSymbol(asUUID());

		final AgreementModel agreement = modelService.create(AgreementModel.class);
		agreement.setId(asUUID());
		agreement.setEnddate(new Date());

		final LanguageModel language = modelService.create(LanguageModel.class);
		language.setIsocode(asUUID());

		final CountryModel country = modelService.create(CountryModel.class);
		country.setIsocode(asUUID());


		inactiveCatalogVersion.setGenerationDate(new Date());
		inactiveCatalogVersion.setDefaultCurrency(null);
		inactiveCatalogVersion.setInclPacking(Boolean.FALSE);
		inactiveCatalogVersion.setInclFreight(Boolean.FALSE);

		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setCatalog(defaultCatalog);
		catalogVersion.setVersion(asUUID());
		catalogVersion.setActive(Boolean.TRUE);
		catalogVersion.setMimeRootDirectory("foo");

		catalogVersion.setGenerationDate(new Date());
		catalogVersion.setDefaultCurrency(currency);
		catalogVersion.setInclPacking(Boolean.TRUE);
		catalogVersion.setInclFreight(Boolean.TRUE);

		catalogVersion.setAgreements(ImmutableList.of(agreement));
		catalogVersion.setGeneratorInfo(asUUID());

		catalogVersion.setInclAssurance(Boolean.TRUE);
		catalogVersion.setInclDuty(Boolean.TRUE);
		catalogVersion.setLanguages(ImmutableList.of(language));
		catalogVersion.setTerritories(ImmutableList.of(country));

		modelService.saveAll();

		assertThat(defaultCatalog.getMimeRootDirectory()).isEqualTo(catalogVersion.getMimeRootDirectory());
		assertThat(defaultCatalog.getGenerationDate()).isEqualTo(catalogVersion.getGenerationDate());
		assertThat(defaultCatalog.getDefaultCurrency()).isEqualTo(catalogVersion.getDefaultCurrency());
		assertThat(defaultCatalog.getInclPacking()).isEqualTo(catalogVersion.getInclPacking());
		assertThat(defaultCatalog.getInclFreight()).isEqualTo(catalogVersion.getInclFreight());

		assertThat(defaultCatalog.getAgreements()).hasSize(1);
		assertThat(defaultCatalog.getGeneratorInfo()).isEqualTo(catalogVersion.getGeneratorInfo());
		assertThat(defaultCatalog.getInclAssurance()).isEqualTo(catalogVersion.getInclAssurance());
		assertThat(defaultCatalog.getInclDuty()).isEqualTo(catalogVersion.getInclDuty());

		assertThat(defaultCatalog.getLanguages()).hasSize(1);
		assertThat(defaultCatalog.getTerritories()).hasSize(1);
	}

	@Test
	public void shouldSetDefaultCatalogAndUndefaultFormer()
	{
		// given
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(asUUID());
		catalog.setDefaultCatalog(Boolean.TRUE);

		final CatalogModel catalog2 = modelService.create(CatalogModel.class);
		catalog2.setId(asUUID());
		catalog2.setDefaultCatalog(Boolean.FALSE);

		modelService.saveAll();

		// when
		catalog2.setDefaultCatalog(Boolean.TRUE);
		modelService.saveAll();

		// then
		assertThat(catalog.getDefaultCatalog()).isFalse();
		assertThat(catalog2.getDefaultCatalog()).isTrue();
	}

	@Test
	public void shouldSaveViaDirectPersistence()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(asUUID());
		catalog.setDefaultCatalog(Boolean.TRUE);

		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, catalog);
		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(catalog);
	}

}
