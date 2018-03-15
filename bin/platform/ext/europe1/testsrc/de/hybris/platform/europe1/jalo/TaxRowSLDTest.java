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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.order.price.TaxModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.europe1.PDTRowTestDataBuilder;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.enums.ProductPriceGroup;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.europe1.model.TaxRowModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.internal.model.impl.PersistenceTestUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.util.persistence.PersistenceUtils;

import java.util.UUID;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class TaxRowSLDTest extends ServicelayerBaseTest
{
	private static final PropertyConfigSwitcher pdtRowProductModified = new PropertyConfigSwitcher(
			Europe1Constants.PDTROW_MARK_PRODUCT_MODIFIED);

	@Resource
	ModelService modelService;
	@Resource
	TypeService typeService;
	@Resource
	EnumerationService enumerationService;

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

		currency = testDataBuilder.createCurrency(UUID.randomUUID().toString(), "zł");
	}

	@Test
	public void shouldDisplayAbsoluteIfCurrencyIsSet()
	{
		PersistenceUtils.<Void> doWithSLDPersistence(() -> {
			final TaxModel tax = modelService.create(TaxModel.class);
			tax.setCode(UUID.randomUUID().toString());
			modelService.save(tax);

			final TaxRowModel taxRow = modelService.create(TaxRowModel.class);
			taxRow.setProductId(UUID.randomUUID().toString());
			taxRow.setTax(tax);

			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, taxRow);

			assertThat(taxRow.getAbsolute()).isFalse();
			taxRow.setCurrency(currency);

			assertThat(taxRow.getAbsolute()).isTrue();

			PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(taxRow);
			return null;
		});
	}


	@Test(expected = ModelSavingException.class)
	public void shouldNotAllowToCreateWithoutDiscount()
	{
		PersistenceUtils.<Void> doWithSLDPersistence(() -> {
			final TaxRowModel taxRow = modelService.create(TaxRowModel.class);
			taxRow.setProductId("lool");

			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, taxRow);

			return null;
		});
	}
}
