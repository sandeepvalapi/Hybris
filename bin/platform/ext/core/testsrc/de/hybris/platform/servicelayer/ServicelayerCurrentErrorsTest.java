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
package de.hybris.platform.servicelayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;


/**
 * Collects test for currently missing or broken functionality. Therefore these tests are <b>supposed</b> to fail!
 */
@IntegrationTest
public class ServicelayerCurrentErrorsTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Test
	@Ignore("see PLA-7617")
	// ^-- remove this to run the test !!!
	// see PLA-7617
	public void testModelAfterRollback()
	{
		final TransactionTemplate transactionTemplate = new TransactionTemplate((PlatformTransactionManager) Registry.getApplicationContext()
				.getBean("txManager"));

		final Unit unit = ProductManager.getInstance().createUnit("foo", "bar");
		final double conv = unit.getConversionFactor();

		final UnitModel model = modelService.get(unit);

		assertEquals(Double.valueOf(conv), model.getConversion());

		try
		{
			transactionTemplate.execute(new TransactionCallbackWithoutResult()
			{
				@Override
				protected void doInTransactionWithoutResult(final TransactionStatus status)
				{
					final UnitModel unitModel2 = modelService.get(unit);

					assertSame(model, unitModel2); // really ??? what about isolation ???

					unitModel2.setConversion(Double.valueOf(30));

					assertEquals(Double.valueOf(30), unitModel2.getConversion());

					modelService.save(unitModel2);

					assertEquals(Double.valueOf(30), unitModel2.getConversion());
					assertEquals(30d, unit.getConversionFactor(), 0.000001d);

					throw new RuntimeException("rollback"); // make TA rollback
				}
			});
		}
		catch (final RuntimeException e)
		{
			assertEquals("rollback", e.getMessage());
		}

		assertEquals(conv, unit.getConversionFactor(), 0.000001d);
		assertEquals(Double.valueOf(conv), model.getConversion()); // still got the old value ???
	}

}
