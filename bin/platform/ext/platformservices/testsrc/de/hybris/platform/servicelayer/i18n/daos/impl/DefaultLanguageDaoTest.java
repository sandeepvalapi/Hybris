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
package de.hybris.platform.servicelayer.i18n.daos.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.daos.LanguageDao;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultLanguageDaoTest extends ServicelayerTest
{

	private int initLanguageCount = 0;
	private static final int CREATED_COUNT = 4;

	@Resource(name = "languageDao")
	private LanguageDao languageDao;

	@Resource
	private ModelService modelService;

	private static final String OLD_HEBREW_ISO = "iw"; //NOPMD
	private static final String NEW_HEBREW_ISO = "he"; //NOPMD
	private static final String OLD_YIDDISH_ISO = "ji"; //NOPMD
	private static final String NEW_YIDDISH_ISO = "yi"; //NOPMD
	private static final String OLD_INDONESIAN_ISO = "in"; //NOPMD
	private static final String NEW_INDONESIAN_ISO = "id"; //NOPMD

	private static final String OLD_YIDDISH_ISO_DE = "ji_DE";
	private static final String NEW_YIDDISH_ISO_DE = "yi_DE";

	private static final String OLD_HEBREW_ISO_DE = "iw_DE";
	private static final String NEW_HEBREW_ISO_DE = "he_DE";

	private static final String OLD_INDONESIAN_ISO_DE = "in_DE";
	private static final String NEW_INDONESIAN_ISO_DE = "id_DE";

	@Before
	public void prepare()
	{
		initLanguageCount = languageDao.findLanguages().size();

		final LanguageModel lModel0 = modelService.create(LanguageModel.class);
		lModel0.setIsocode("tinyRed");

		final LanguageModel lModel1 = modelService.create(LanguageModel.class);
		lModel1.setIsocode("bigGrey");

		final LanguageModel lModel2 = modelService.create(LanguageModel.class);
		lModel2.setIsocode("smallWhite");

		final LanguageModel lModel3 = modelService.create(LanguageModel.class);
		lModel3.setIsocode("hugePink");

		modelService.saveAll();
	}

	@Test
	public void testGetAllLanguages()
	{
		final List<LanguageModel> result = languageDao.findLanguages();

		assertThat(result).isNotNull().hasSize(CREATED_COUNT + initLanguageCount);
	}


	@Test
	public void testLanguageByNotExistingCode()
	{
		final List<LanguageModel> result = languageDao.findLanguagesByCode("notExisting");

		assertThat(result).isEmpty();
	}


	@Test
	public void testLanguageByExistingCode()
	{
		final List<LanguageModel> result = languageDao.findLanguagesByCode("bigGrey");

		assertThat(result).isNotNull().hasSize(1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLanguageByExistingCodeWithNull()
	{
		languageDao.findLanguagesByCode(null);
	}

	@Test
	public void testLanguageByEmptyCode()
	{
		final List<LanguageModel> result = languageDao.findLanguagesByCode("");

		assertThat(result).isEmpty();
	}


	// PLA-12635
	@Test
	public void testLanguagesWithOldLocaleWhenBothExist()
	{
		// given both "iw" and "he" are created as models
		final LanguageModel oldHebrewLanguage = modelService.create(LanguageModel.class);
		oldHebrewLanguage.setIsocode(OLD_HEBREW_ISO);
		final LanguageModel newHebrewLanguage = modelService.create(LanguageModel.class);
		newHebrewLanguage.setIsocode(NEW_HEBREW_ISO);
		modelService.saveAll();

		// when
		final List<LanguageModel> oldHebrewIsoResults = languageDao.findLanguagesByCode(OLD_HEBREW_ISO);
		final List<LanguageModel> newHebrewIsoResults = languageDao.findLanguagesByCode(NEW_HEBREW_ISO);

		// then they both must be found
		assertThat(oldHebrewIsoResults).hasSize(1);
		assertThat(newHebrewIsoResults).hasSize(1);
	}

	// PLA-12635
	@Test
	public void cannotFindLanguageByNewIsoWhenOnlyOldExists()
	{
		// given only model for old yiddish is created so
		final LanguageModel oldYiddishLanguage = modelService.create(LanguageModel.class);
		oldYiddishLanguage.setIsocode(OLD_YIDDISH_ISO);
		modelService.save(oldYiddishLanguage);

		// when
		final List<LanguageModel> oldIsoResult = languageDao.findLanguagesByCode(OLD_YIDDISH_ISO);
		final List<LanguageModel> newIsoResult = languageDao.findLanguagesByCode(NEW_YIDDISH_ISO);

		// then new "yi" CANNOT be found
		assertThat(newIsoResult).hasSize(0);
		assertThat(oldIsoResult).hasSize(1);
	}


	//PLA-12635
	@Test
	public void findLanguageByOldAndNewIsoWhenNewModelExists()
	{
		// given
		final LanguageModel newIndonesianLanguage = modelService.create(LanguageModel.class);
		newIndonesianLanguage.setIsocode(NEW_INDONESIAN_ISO);
		modelService.save(newIndonesianLanguage);

		// when
		final List<LanguageModel> oldIsoResults = languageDao.findLanguagesByCode(OLD_INDONESIAN_ISO);
		final List<LanguageModel> newIsoResults = languageDao.findLanguagesByCode(NEW_INDONESIAN_ISO);

		// then
		assertThat(oldIsoResults).hasSize(1);
		assertThat(newIsoResults).hasSize(1);
	}


	// HORST-721
	@Test
	public void testQueryingLocalizedLanguagesByNewIsoCode()
	{
		// given
		final LanguageModel newIndonesianDeLanguage = modelService.create(LanguageModel.class);
		newIndonesianDeLanguage.setIsocode(NEW_INDONESIAN_ISO_DE);

		final LanguageModel newYiddishDeLanguage = modelService.create(LanguageModel.class);
		newYiddishDeLanguage.setIsocode(NEW_YIDDISH_ISO_DE);

		final LanguageModel newHebrewDeLanguage = modelService.create(LanguageModel.class);
		newHebrewDeLanguage.setIsocode(NEW_HEBREW_ISO_DE);

		modelService.saveAll();

		// when
		final List<LanguageModel> indonesiaNewIsoResults = languageDao.findLanguagesByCode(NEW_INDONESIAN_ISO_DE);
		final List<LanguageModel> indonesiaOldIsoResults = languageDao.findLanguagesByCode(OLD_INDONESIAN_ISO_DE);

		final List<LanguageModel> yiddishNewIsoResults = languageDao.findLanguagesByCode(NEW_YIDDISH_ISO_DE);
		final List<LanguageModel> yiddishOldIsoResults = languageDao.findLanguagesByCode(OLD_YIDDISH_ISO_DE);

		final List<LanguageModel> hebrewNewIsoResults = languageDao.findLanguagesByCode(NEW_HEBREW_ISO_DE);
		final List<LanguageModel> hebrewOldIsoResults = languageDao.findLanguagesByCode(OLD_HEBREW_ISO_DE);

		// then
		assertThat(indonesiaNewIsoResults).hasSize(1);
		assertThat(indonesiaOldIsoResults).hasSize(1);

		assertThat(yiddishNewIsoResults).hasSize(1);
		assertThat(yiddishOldIsoResults).hasSize(1);

		assertThat(hebrewNewIsoResults).hasSize(1);
		assertThat(hebrewOldIsoResults).hasSize(1);
	}

}
