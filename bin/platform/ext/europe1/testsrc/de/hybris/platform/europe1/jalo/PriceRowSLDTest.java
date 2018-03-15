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
package de.hybris.platform.europe1.jalo;


import static org.fest.assertions.Assertions.assertThat;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.europe1.PDTRowTestDataBuilder;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.internal.model.impl.PersistenceTestUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.util.persistence.PersistenceUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class PriceRowSLDTest extends ServicelayerBaseTest
{

	private static final PropertyConfigSwitcher pdtRowProductModified = new PropertyConfigSwitcher(
			Europe1Constants.PDTROW_MARK_PRODUCT_MODIFIED);

	@Resource
	ModelService modelService;

	@Resource
	TypeService typeService;

	@Resource
	EnumerationService enumerationService;

	UserModel user;
	UnitModel unit;
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

		testDataBuilder.createProductPriceGroup(UUID.randomUUID().toString());

		user = testDataBuilder.createUser(UUID.randomUUID().toString());
		currency = testDataBuilder.createCurrency("pln", "zł");
		unit = testDataBuilder.createUnit("pieces", "pieces");
	}

	@Test
	public void savePriceRowViaSLD()
	{
		PersistenceUtils.<Void> doWithSLDPersistence(() -> {
			final PriceRowModel priceRow = modelService.create(PriceRowModel.class);
			priceRow.setProductId(UUID.randomUUID().toString());
			priceRow.setUnit(unit);
			priceRow.setCurrency(currency);
			priceRow.setPrice(Double.valueOf(5.0));
			priceRow.setUser(user);

			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, priceRow);

			// for set Product and User match value should be 9
			assertThat(priceRow.getMatchValue()).isEqualTo(9);

			PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(priceRow);

			return null;
		});
	}


	@Test
	public void savePriceRowViaJalo() throws JaloGenericCreationException, JaloAbstractTypeException
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(PriceRow.CURRENCY, modelService.getSource(currency));
		params.put(PriceRow.PRICE, Double.valueOf("123.45"));
		params.put(PriceRow.UNIT, modelService.getSource(unit));
		params.put(PriceRow.PRODUCTID, "foo");

		ComposedType.newInstance(jaloSession.getSessionContext(), PriceRow.class, params);
	}

	@Test(expected = ModelSavingException.class)
	public void shouldNotAllowLowerThanZeroQty()
	{
		PersistenceUtils.<Void> doWithSLDPersistence(() -> {
			final PriceRowModel priceRow = modelService.create(PriceRowModel.class);
			priceRow.setProductId(UUID.randomUUID().toString());
			priceRow.setUnit(unit);
			priceRow.setCurrency(currency);
			priceRow.setPrice(Double.valueOf(5.0));
			priceRow.setUser(user);
			priceRow.setMinqtd(Long.valueOf(-1L));
			priceRow.setUnitFactor(Integer.valueOf(0));

			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, priceRow);

			// for set Product and User match value should be 9
			assertThat(priceRow.getMatchValue()).isEqualTo(9);
			assertThat(priceRow.getMinqtd()).isEqualTo(-1L);
			assertThat(priceRow.getUnitFactor()).isEqualTo(0);

			PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(priceRow);

			return null;
		});
	}

}
