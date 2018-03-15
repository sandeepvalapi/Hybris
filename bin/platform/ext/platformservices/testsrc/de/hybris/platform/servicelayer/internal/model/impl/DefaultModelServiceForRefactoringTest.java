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
package de.hybris.platform.servicelayer.internal.model.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.servicelayer.ExtendedServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.bethecoder.ascii_table.ASCIITable;
import com.google.common.base.Stopwatch;


@IntegrationTest
public class DefaultModelServiceForRefactoringTest extends ExtendedServicelayerTransactionalBaseTest
{
	@Resource
	private ModelService modelService;
	private UnitModel unit;
	private Unit unitItem;
	private Stopwatch stopWatch;

	@Before
	public void setUp() throws Exception
	{
		assertThat(modelService).isNotNull();
		createCoreData();
		createDefaultCatalog();
		stopWatch = Stopwatch.createUnstarted();

		unit = modelService.create(UnitModel.class);
		unit.setCode("pint");
		unit.setUnitType("measurement");
		modelService.save(unit);

		unitItem = modelService.getSource(unit);
	}


	@Test
	public void writeProductsJaloVsModelService()
	{
		final int numOfProducts = 1000;

		final String modelsResult = writeModelsAndGetStats(numOfProducts);
		final String itemsResult = writeItemsAndGetStats(numOfProducts);

		final String[][] data = new String[1][];
		final String[] result =
		{ "Save " + numOfProducts + " products", modelsResult, itemsResult };
		data[0] = result;
		writeResultTable(data);
	}

	private String writeModelsAndGetStats(final int num)
	{
		stopWatch.start();

		for (int i = 0; i < num; i++)
		{
			modelService.save(prepareProductModel("code" + i));
		}
		stopWatch.stop();
		final String result = stopWatch.toString();
		stopWatch.reset();

		return result;
	}

	private ProductModel prepareProductModel(final String code)
	{
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode(code);
		product.setUnit(unit);
		product.setDescription(
				"Stealth design meets high-performance, maximized ventilation and precise optics allow for unparalleled perfomance.",
				Locale.ENGLISH);
		product.setDescription("This is name for product.", Locale.ENGLISH);
		return product;
	}

	private String writeItemsAndGetStats(final int num)
	{
		final ProductManager manager = ProductManager.getInstance();
		final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
		stopWatch.start();
		for (int i = 0; i < num; i++)
		{
			final Product product = manager.createProduct(ctx, "code" + i);
			product
					.setDescription("Stealth design meets high-performance, maximized ventilation and precise optics allow for unparalleled perfomance.");
			product.setName("This is name for product.");
			product.setUnit(unitItem);
		}
		stopWatch.stop();
		final String result = stopWatch.toString();
		stopWatch.reset();

		return result;
	}

	private void writeResultTable(final String[][] data)
	{
		final String[] header =
		{ " ", "ModelService", "Jalo" };
		ASCIITable.getInstance().printTable(header, data);
	}
}
