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
package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class UniqueAttributesInterceptorTest extends ServicelayerTransactionalBaseTest
{

	private static final Logger log = Logger.getLogger(UniqueAttributesInterceptorTest.class.getName());


	@Resource
	private ModelService modelService;

	@Test
	public void testSaveCatalogCatalogVersionModels() throws JaloInvalidParameterException, JaloDuplicateCodeException,
			ConsistencyCheckException
	{

		final CatalogModel cm1 = modelService.create(CatalogModel.class);
		cm1.setId("sl_a");
		modelService.save(cm1);
		final CatalogVersionModel cmv1 = modelService.create(CatalogVersionModel.class);
		cmv1.setCatalog(cm1);
		cmv1.setVersion("v1.0");
		modelService.save(cmv1);

		try
		{
			final CatalogModel cm2 = modelService.create(CatalogModel.class);
			cm2.setId("sl_b");
			final CatalogVersionModel cmv2 = modelService.create(CatalogVersionModel.class);
			cmv2.setCatalog(cm2);
			cmv2.setVersion("v1.0");
			modelService.save(cmv2);
			log.debug(cm2 + " " + cmv2);
		}
		catch (final Exception e)
		{
			Assert.fail(e.getMessage());
			//e.printStackTrace();
		}

		try
		{
			final CatalogModel cm2 = modelService.create(CatalogModel.class);
			cm2.setId("sl_c");
			final CatalogVersionModel cmv2 = modelService.create(CatalogVersionModel.class);
			cmv2.setCatalog(cm2);
			cmv2.setVersion("v1.0");

			modelService.saveAll(Arrays.asList(new Object[]
			{ cmv2 }));
			log.debug(cm2 + " " + cmv2);
		}
		catch (final Exception e)
		{
			Assert.fail(e.getMessage());
			//e.printStackTrace();
		}

		try
		{
			final CatalogModel cm2 = modelService.create(CatalogModel.class);
			cm2.setId("sl_d");
			final CatalogVersionModel cmv2 = modelService.create(CatalogVersionModel.class);
			cmv2.setCatalog(cm2);
			cmv2.setVersion("v1.0");

			modelService.saveAll(Arrays.asList(new Object[]
			{ cm2, cmv2 }));
			log.debug(cm2 + " " + cmv2);
		}
		catch (final Exception e)
		{
			Assert.fail(e.getMessage());
			//			e.printStackTrace();
		}

		try
		{
			final CatalogModel cm2 = modelService.create(CatalogModel.class);
			cm2.setId("sl_e");
			final CatalogVersionModel cmv2 = modelService.create(CatalogVersionModel.class);
			cmv2.setCatalog(cm2);
			cmv2.setVersion("v1.0");
			modelService.saveAll(Arrays.asList(new Object[]
			{ cmv2, cm2 }));
			log.debug(cm2 + " " + cmv2);
		}
		catch (final Exception e)
		{
			Assert.fail(e.getMessage());
			//			e.printStackTrace();
		}

		try
		{
			final CatalogModel cm2 = modelService.create(CatalogModel.class);
			cm2.setId("sl_f");
			final CatalogVersionModel cmv2 = modelService.create(CatalogVersionModel.class);
			cmv2.setCatalog(cm2);
			cmv2.setVersion("v1.0");
			modelService.attach(cm2);
			modelService.attach(cmv2);
			modelService.saveAll();
			log.debug(cm2 + " " + cmv2);
		}
		catch (final Exception e)
		{
			Assert.fail(e.getMessage());
			//			e.printStackTrace();
		}

		try
		{
			final CatalogModel cm2 = modelService.create(CatalogModel.class);
			cm2.setId("sl_g");
			final CatalogVersionModel cmv2 = modelService.create(CatalogVersionModel.class);
			cmv2.setCatalog(cm2);
			cmv2.setVersion("v1.0");
			modelService.attach(cmv2);
			modelService.attach(cm2);
			modelService.saveAll();
			log.debug(cm2 + " " + cmv2);
		}
		catch (final Exception e)
		{
			Assert.fail(e.getMessage());
			//			e.printStackTrace();
		}

	}
}
