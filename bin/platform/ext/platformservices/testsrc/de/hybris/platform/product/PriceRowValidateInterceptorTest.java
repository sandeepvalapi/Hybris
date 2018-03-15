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
package de.hybris.platform.product;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.europe1.PDTRowTestDataBuilder;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import javax.annotation.Resource;

import de.hybris.platform.testframework.PropertyConfigSwitcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.fest.assertions.Assertions.assertThat;


@IntegrationTest
public class PriceRowValidateInterceptorTest extends ServicelayerTransactionalTest
{

	@Resource
	ModelService modelService;

	@Resource
	TypeService typeService;

	@Resource
	EnumerationService enumerationService;

	ProductModel product;
	UserModel user;
	EnumerationValueModel userPriceGroup;
	UnitModel unit;


	private static final PropertyConfigSwitcher pdtRowProductModified = new PropertyConfigSwitcher(
			Europe1Constants.PDTROW_MARK_PRODUCT_MODIFIED);

	CurrencyModel currency;

	@After
	public void tearDown()
	{
		pdtRowProductModified.switchBackToDefault();
	}

	@Before
	public void doBefore()
	{
		pdtRowProductModified.switchToValue("true");

		final PDTRowTestDataBuilder testDataBuilder = new PDTRowTestDataBuilder(modelService);
		currency = testDataBuilder.createCurrency("pln", "zł");
		unit = testDataBuilder.createUnit("pieces", "pieces");
	}

	@Test(expected = ModelSavingException.class)
	public void shouldNotAllowNegativeMinQuantity()
	{
		final PriceRowModel priceRow = createPriceRowWithMinQtdAndUnitFactor(-1, 1);
		modelService.save(priceRow);
	}

	@Test(expected = ModelSavingException.class)
	public void shouldNotAllowZeroUnitFactor()
	{
		final PriceRowModel priceRow = createPriceRowWithMinQtdAndUnitFactor(1, 0);
		modelService.save(priceRow);
	}

	private PriceRowModel createPriceRowWithMinQtdAndUnitFactor(final long minQtd, final int unitFactor)
	{
		final PriceRowModel priceRow = modelService.create(PriceRowModel.class);
		priceRow.setProductId(UUID.randomUUID().toString());
		priceRow.setUnit(unit);
		priceRow.setCurrency(currency);
		priceRow.setPrice(5.0);
		priceRow.setUser(user);
		priceRow.setMinqtd(minQtd);
		priceRow.setUnitFactor(unitFactor);

		modelService.save(priceRow);
		return priceRow;
	}
}
