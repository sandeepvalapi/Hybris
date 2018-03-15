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
package de.hybris.platform.order.daos;


import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.order.daos.impl.DefaultPaymentModeDao;
import de.hybris.platform.paymentstandard.model.StandardPaymentModeModel;
import de.hybris.platform.paymentstandard.model.StandardPaymentModeValueModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class PaymentModeDaoTest extends ServicelayerTransactionalTest
{
	@Resource
	private DefaultPaymentModeDao defaultPaymentModeDao;

	@Resource
	private TypeService typeService;

	@Resource
	private DefaultModelService modelService;

	private PaymentModeModel paymentModeModel1;
	private PaymentModeModel paymentModeModel2;

	private StandardPaymentModeModel paymentModeModel3;
	private StandardPaymentModeModel paymentModeModel4;

	private StandardPaymentModeValueModel standardPaymentModeValue1;
	private StandardPaymentModeValueModel standardPaymentModeValue2;


	private CurrencyModel currency;

	/**
	 * 
	 */
	@Before
	public void setUp() throws Exception
	{
		paymentModeModel1 = modelService.create(PaymentModeModel.class);
		paymentModeModel1.setCode("code1");
		paymentModeModel1.setActive(Boolean.FALSE);
		paymentModeModel1.setPaymentInfoType(typeService.getComposedTypeForClass(PaymentInfoModel.class));
		modelService.save(paymentModeModel1);

		paymentModeModel2 = modelService.create(PaymentModeModel.class);
		paymentModeModel2.setCode("code2");
		paymentModeModel2.setActive(Boolean.TRUE);
		paymentModeModel2.setPaymentInfoType(typeService.getComposedTypeForClass(PaymentInfoModel.class));
		modelService.save(paymentModeModel2);


		paymentModeModel3 = modelService.create(StandardPaymentModeModel.class);
		paymentModeModel3.setCode("code3");
		paymentModeModel3.setActive(Boolean.TRUE);
		paymentModeModel3.setPaymentInfoType(typeService.getComposedTypeForClass(PaymentInfoModel.class));
		modelService.save(paymentModeModel3);

		paymentModeModel4 = modelService.create(StandardPaymentModeModel.class);
		paymentModeModel4.setCode("code4");
		paymentModeModel4.setActive(Boolean.TRUE);
		paymentModeModel4.setPaymentInfoType(typeService.getComposedTypeForClass(PaymentInfoModel.class));
		modelService.save(paymentModeModel4);

		currency = modelService.create(CurrencyModel.class);
		currency.setActive(Boolean.TRUE);
		currency.setBase(Boolean.TRUE);
		currency.setConversion(Double.valueOf(1));
		currency.setDigits(Integer.valueOf(5));
		currency.setIsocode("MuCurr");
		currency.setSymbol("MySymb");
		currency.setName("myname");
		modelService.save(currency);



		standardPaymentModeValue1 = modelService.create(StandardPaymentModeValueModel.class);
		standardPaymentModeValue1.setCurrency(currency);
		standardPaymentModeValue1.setValue(Double.valueOf(11));
		standardPaymentModeValue1.setPaymentMode(paymentModeModel3);
		modelService.save(standardPaymentModeValue1);


		standardPaymentModeValue2 = modelService.create(StandardPaymentModeValueModel.class);
		standardPaymentModeValue2.setCurrency(currency);
		standardPaymentModeValue2.setValue(Double.valueOf(211));
		standardPaymentModeValue2.setPaymentMode(paymentModeModel4);
		modelService.save(standardPaymentModeValue2);
	}

	/**
	 * Test method for {@link DefaultPaymentModeDao#findPaymentModeForCode(java.lang.String)}.
	 */
	@Test
	public void testFindPaymentModeByCode()
	{
		Assert.assertEquals(paymentModeModel1, defaultPaymentModeDao.findPaymentModeForCode("code1").get(0));
	}

	/**
	 * Test method for {@link DefaultPaymentModeDao#findPaymentModeForCode(java.lang.String)}.
	 */
	@Test
	public void testFindPaymentModeByCodenotFound()
	{
		Assert.assertTrue(defaultPaymentModeDao.findPaymentModeForCode("code10").isEmpty());
	}

	/**
	 * Test method for {@link DefaultPaymentModeDao#findAllPaymentModes()}.
	 */
	@Test
	public void testFindAllPaymentModes()
	{
		final List<PaymentModeModel> res = defaultPaymentModeDao.findAllPaymentModes();
		Assert.assertEquals(4, res.size());
		Assert.assertTrue(res.contains(paymentModeModel1));
		Assert.assertTrue(res.contains(paymentModeModel2));
	}



}
