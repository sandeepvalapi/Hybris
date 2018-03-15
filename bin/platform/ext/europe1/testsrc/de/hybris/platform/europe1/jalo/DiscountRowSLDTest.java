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
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.europe1.PDTRowTestDataBuilder;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.internal.model.impl.PersistenceTestUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.model.attribute.DiscountStringHandler;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.util.persistence.PersistenceUtils;

import java.util.UUID;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DiscountRowSLDTest extends ServicelayerBaseTest
{
	@Resource
	ModelService modelService;
	@Resource
	TypeService typeService;
	@Resource
	EnumerationService enumerationService;
	@Resource
	DiscountStringHandler abstractDiscountRowDiscountStringHandler;

	CurrencyModel currency;

	private static final PropertyConfigSwitcher pdtRowProductModified = new PropertyConfigSwitcher(Europe1Constants.PDTROW_MARK_PRODUCT_MODIFIED);

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
			final DiscountModel discount = createDiscount();
			modelService.save(discount);

			final DiscountRowModel discountRow = createDiscountRow(discount);
			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, discountRow);

			assertThat(discountRow.getAbsolute()).isFalse();
			discountRow.setCurrency(currency);

			assertThat(discountRow.getAbsolute()).isTrue();

			PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(discountRow);
			return null;
		});
	}

	@Test
	public void shouldReturnDiscountStringHandler()
	{
		PersistenceUtils.<Void> doWithSLDPersistence(() -> {
			final DiscountModel discount = createDiscount();
			modelService.save(discount);

			final DiscountRowModel discountRow = createDiscountRow(discount);
			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, discountRow);

			assertThat(discountRow.getDiscountString()).isEqualTo("0%");

			discount.setValue(50.0);

			assertThat(discountRow.getDiscountString()).isEqualTo("50%");
			return null;
		});
	}

	@Test(expected = ModelSavingException.class)
	public void shouldNotAllowToCreateWithoutDiscount()
	{
		PersistenceUtils.<Void> doWithSLDPersistence(() -> {
			final DiscountRowModel discountRow = modelService.create(DiscountRowModel.class);
			discountRow.setProductId("lool");
			modelService.save(discountRow);
			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, discountRow);

			return null;
		});
	}

	private DiscountModel createDiscount()
	{
		final DiscountModel discount = modelService.create(DiscountModel.class);
		discount.setCode(UUID.randomUUID().toString());
		return discount;
	}

	private DiscountRowModel createDiscountRow(final DiscountModel discount)
	{
		final DiscountRowModel discountRow = modelService.create(DiscountRowModel.class);
		discountRow.setProductId(UUID.randomUUID().toString());
		discountRow.setDiscount(discount);

		return discountRow;
	}
}
