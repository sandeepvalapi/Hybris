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
package de.hybris.platform.order;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;


@DemoTest
public class DeliveryModeServiceTest extends ServicelayerTransactionalTest
{

	@Resource
	private DeliveryModeService deliveryModeService;
	@Resource
	private PaymentModeService paymentModeService;

	/**
	 * Creates the core data, and necessary data for delivery modes.
	 */
	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		importCsv("/servicelayer/test/testDeliveryMode.csv", "windows-1252");
	}

	/**
	 * Tries to search for the delivery mode with code:
	 * <ul>
	 * <li>successful with "courier",</li>
	 * <li>caught UnknownIdentifierException with "No_Such_DeliveryMode",</li>
	 * <li>found all delivery modes, whose size is 3.</li>
	 * </ul>
	 */
	@Test
	public void testGetDeliveryMode()
	{
		String deliveryModeCode = "courier";
		DeliveryModeModel deliveryMode = deliveryModeService.getDeliveryModeForCode(deliveryModeCode);
		assertNotNull(deliveryMode);
		assertEquals(deliveryModeCode, deliveryMode.getCode());

		deliveryModeCode = "No_Such_DeliveryMode";
		try
		{
			deliveryMode = deliveryModeService.getDeliveryModeForCode(deliveryModeCode);
			fail("the delivery mode code [" + deliveryMode + "] should NOT be found.");
		}
		catch (final UnknownIdentifierException ue) //NOPMD
		{
			//expected
		}

		final String[] expectedDeliveryModes =
		{ "collect", "courier", "dhl", "fedex", "post", "postService", "ups" };
		final Collection<DeliveryModeModel> deliveryModes = deliveryModeService.getAllDeliveryModes();
		assertEquals(expectedDeliveryModes.length, deliveryModes.size());
		final List<String> _expectedDeliveryModes = Arrays.asList(expectedDeliveryModes);
		final List<String> _deliveryModes = new ArrayList<String>();
		for (final DeliveryModeModel mode : deliveryModes)
		{
			_deliveryModes.add(mode.getCode());
		}
		final boolean same = CollectionUtils.isEqualCollection(_expectedDeliveryModes, _deliveryModes);
		assertTrue(same);
	}

	/**
	 * Tries to search for the supported delivery modes with the payment mode:
	 * <ul>
	 * <li>found payment mode with "creditcard",</li>
	 * <li>found all supported delivery modes for this payment mode, which is 5</li>
	 * </ul>
	 */
	@Test
	public void testGetSupportedDeliveryModes()
	{
		final PaymentModeModel creditCartPaymentMode = paymentModeService.getPaymentModeForCode("creditcard");
		assertNotNull(creditCartPaymentMode);
		final Collection<DeliveryModeModel> deliveryModes = deliveryModeService.getSupportedDeliveryModes(creditCartPaymentMode);
		//2 DeliveryMode and 3 ZoneDeliveryMode
		assertEquals(5, deliveryModes.size());
	}

}
