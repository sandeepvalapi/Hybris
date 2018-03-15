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
package de.hybris.platform.servicelayer.impex;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.util.Config;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class SpecialValueTranslatorIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private ImportService importService;
	@Resource
	private CommonI18NService commonI18NService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createHardwareCatalog();
	}

	/**
	 * HOR-1404 fix.
	 * 
	 * @see de.hybris.platform.servicelayer.impex.MyTranslator
	 */
	@Test
	public void shouldImportImportWholeScriptMarkingProblematicLineUnresolvedEvenWhenDeclaredMyTranslatorThrowsException()
			throws Exception // NOPMD
	{
		// given
		Config.setParameter("impex.nonexistend.clsattrvalue.fallback.enabled", "false");
		final StringBuilder builder = new StringBuilder("$systemName=SampleClassification");
		builder.append("\n").append("$systemVersion=1.0").append("\n");
		builder.append("$YCL=system='$systemName',version='$systemVersion',");
		// this translator throws exception
		builder.append("translator=de.hybris.platform.servicelayer.impex.MyTranslator;");
		builder.append("\n").append("UPDATE Product;code[unique=true];@lanSpeed[$YCL];");
		builder.append("catalogVersion[unique=true](catalog(id),version)[virtual=true,default=hwcatalog:Online]").append("\n");
		builder.append(";HW2200-0878;SomeNonExistendOne").append("\n");
		builder.append("INSERT Language;isocode;active\n;test;true");
		final ImportConfig importConfig = new ImportConfig();
		importConfig.setScript(builder.toString());
		importConfig.setRemoveOnSuccess(false);

		// when
		try
		{
			commonI18NService.getLanguage("test");
			fail("there should be no \"test\" language yet");
		}
		catch (final UnknownIdentifierException e)
		{
			// fine, there should be no "test" language yet
		}

		ImportResult result = null;
		try
		{
			TestUtils.disableFileAnalyzer("The import must result in one unresolved line. This is ok here", 100);
			result = importService.importData(importConfig);
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}

		Assert.assertNotNull(result);
		// then
		assertThat(result.hasUnresolvedLines()).isTrue();
		assertThat(commonI18NService.getLanguage("test")).isNotNull();
	}
}
