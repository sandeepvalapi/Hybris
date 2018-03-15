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
package de.hybris.platform.commons.renderer.daos.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.daos.RendererTemplateDao;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultRendererDaoTest extends ServicelayerTransactionalTest
{
	@Resource
	private RendererTemplateDao rendererTemplateDao;

	@Before
	public void setUp() throws Exception
	{
		//given
		createCoreData();
		createDefaultCatalog();
		importCsv("/cronjob/DefaultCronJobFinishNotificationTemplate.csv", "windows-1252");
	}

	@Test
	public void testFindTemplatesForExistingOne()
	{
		//when
		final List<RendererTemplateModel> templates = rendererTemplateDao
				.findRendererTemplatesByCode("DefaultCronJobFinishNotificationTemplate");

		//then
		Assert.assertEquals(templates.size(), 1);
		Assert.assertEquals(templates.get(0).getCode(), "DefaultCronJobFinishNotificationTemplate");
	}

	@Test
	public void testFindTemplatesWithUnknownCodes()
	{
		//when
		final List<RendererTemplateModel> templates = rendererTemplateDao.findRendererTemplatesByCode("unknown");

		//then
		Assert.assertTrue(templates.isEmpty());
	}

	@Test
	public void testFindTemplatesWithNullCode()
	{
		//when
		try
		{
			rendererTemplateDao.findRendererTemplatesByCode(null);
			Assert.fail("Should throw IllegalArgumentException because code is null");
		}
		catch (final IllegalArgumentException ex)
		{
			//OK
		}
	}
}
