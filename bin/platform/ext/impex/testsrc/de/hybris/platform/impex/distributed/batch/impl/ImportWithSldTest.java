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
package de.hybris.platform.impex.distributed.batch.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.processing.distributed.DistributedProcessService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.ClasspathImpExResource;
import de.hybris.platform.servicelayer.internal.model.extractor.impl.DefaultPersistenceTypeService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.Config;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for import wit direct persistence for data.
 */
@IntegrationTest
public class ImportWithSldTest extends ServicelayerBaseTest
{

	@Resource
	private ImportService importService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private DistributedProcessService distributedProcessService;

	private boolean legacyModeBefore = false;

	@Before
	public void setUp()
	{
		legacyModeBefore = DefaultPersistenceTypeService.getLegacyPersistenceGlobalSettingFromConfig();
	}

	@After
	public void tearDown()
	{
		Config.setParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE, String.valueOf(legacyModeBefore));
	}

	@Test
	public void testDistributedImpexWithSldEnabledInImportConfig() throws Exception
	{
		//given
		forceLegacyMode();
		final ImportConfig config = new ImportConfig();

		final ClasspathImpExResource impExResource = new ClasspathImpExResource("/impex/testfiles/testImpexWithSld.csv", "UTF-8");
		config.setScript(impExResource);
		config.setDistributedImpexEnabled(true);
		config.setDistributedImpexProcessCode("TEST_PROCESS");
		config.setSldForData(true);

		// when
		final ImportResult result = importService.importData(config);
		distributedProcessService.wait("TEST_PROCESS", 20);

		// then
		assertJaloInUse();
		assertThat(result.isSuccessful()).isTrue();
		assertThat(result.hasUnresolvedLines()).isFalse();
		assertThat(findTitleForCode("foo_default").isPresent()).isTrue();
		assertThat(findTitleForCode("bar_default").isPresent()).isTrue();
		assertThat(findTitleForCode("foo_sld_forced_by_header").isPresent()).isTrue();
		assertThat(findTitleForCode("bar_sld_forced_by_header").isPresent()).isTrue();
	}

	@Test
	public void testDistributedImpexWithSldDisabledInImportConfig() throws Exception
	{
		//given
		forceLegacyMode();
		final ImportConfig config = new ImportConfig();

		final ClasspathImpExResource impExResource = new ClasspathImpExResource("/impex/testfiles/testImpexWithSld.csv", "UTF-8");
		config.setScript(impExResource);
		config.setDistributedImpexEnabled(true);
		config.setDistributedImpexProcessCode("TEST_PROCESS");
		config.setSldForData(false);

		// when
		final ImportResult result = importService.importData(config);
		distributedProcessService.wait("TEST_PROCESS", 20);

		// then
		assertJaloInUse();
		assertThat(result.isSuccessful()).isTrue();
		assertThat(result.hasUnresolvedLines()).isFalse();
		assertThat(findTitleForCode("foo_default").isPresent()).isTrue();
		assertThat(findTitleForCode("bar_default").isPresent()).isTrue();
		assertThat(findTitleForCode("foo_sld_forced_by_header").isPresent()).isTrue();
		assertThat(findTitleForCode("bar_sld_forced_by_header").isPresent()).isTrue();
	}

	@Test
	public void testDistributedImpexWithDefaultSldSettingsInImportConfigAndSldEnabledGlobally() throws Exception
	{
		//given
		forceSldMode();
		final ImportConfig config = new ImportConfig();

		final ClasspathImpExResource impExResource = new ClasspathImpExResource("/impex/testfiles/testImpexWithSld.csv", "UTF-8");
		config.setScript(impExResource);
		config.setDistributedImpexEnabled(true);
		config.setDistributedImpexProcessCode("TEST_PROCESS");
		assertThat(config.isSldForData()).isNull();

		// when
		final ImportResult result = importService.importData(config);
		distributedProcessService.wait("TEST_PROCESS", 20);

		// then
		assertSldInUse();
		assertThat(result.isSuccessful()).isTrue();
		assertThat(result.hasUnresolvedLines()).isFalse();
		assertThat(findTitleForCode("foo_default").isPresent()).isTrue();
		assertThat(findTitleForCode("bar_default").isPresent()).isTrue();
		assertThat(findTitleForCode("foo_sld_forced_by_header").isPresent()).isTrue();
		assertThat(findTitleForCode("bar_sld_forced_by_header").isPresent()).isTrue();
	}

	@Test
	public void testDistributedImpexWithDefaultSldSettingsInImportConfigAndSldDisabledGlobally() throws Exception
	{
		//given
		forceLegacyMode();
		final ImportConfig config = new ImportConfig();

		final ClasspathImpExResource impExResource = new ClasspathImpExResource("/impex/testfiles/testImpexWithSld.csv", "UTF-8");
		config.setScript(impExResource);
		config.setDistributedImpexEnabled(true);
		config.setDistributedImpexProcessCode("TEST_PROCESS");
		assertThat(config.isSldForData()).isNull();

		// when
		final ImportResult result = importService.importData(config);
		distributedProcessService.wait("TEST_PROCESS", 20);

		// then
		assertJaloInUse();
		assertThat(result.isSuccessful()).isTrue();
		assertThat(result.hasUnresolvedLines()).isFalse();
		assertThat(findTitleForCode("foo_default").isPresent()).isTrue();
		assertThat(findTitleForCode("bar_default").isPresent()).isTrue();
		assertThat(findTitleForCode("foo_sld_forced_by_header").isPresent()).isTrue();
		assertThat(findTitleForCode("bar_sld_forced_by_header").isPresent()).isTrue();
	}

	private Optional<TitleModel> findTitleForCode(final String code)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Title} WHERE {code}=?code");
		fQuery.addQueryParameter("code", code);

		final SearchResult<TitleModel> searchResult = flexibleSearchService.search(fQuery);

		if (searchResult.getCount() == 0)
		{
			return Optional.empty();
		}

		if (searchResult.getCount() > 1)
		{
			fail("Found more than one TitleModel with code: " + code);
		}

		final List<TitleModel> result = searchResult.getResult();
		return Optional.of(result.get(0));
	}

	private void forceLegacyMode()
	{
		Config.setParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE, "true");
	}

	private void forceSldMode()
	{
		Config.setParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE, "false");
	}

	private void assertSldInUse()
	{
		assertThat(Config.getBoolean(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE, false)).isFalse();
	}

	private void assertJaloInUse()
	{
		assertThat(Config.getBoolean(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE, false)).isTrue();
	}
}
