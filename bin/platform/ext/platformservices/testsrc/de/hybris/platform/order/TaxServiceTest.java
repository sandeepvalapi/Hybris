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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.price.TaxModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class TaxServiceTest extends ServicelayerTransactionalTest
{

	@Resource
	private TaxService taxService;

	/**
	 * Creates the core data, and two taxes.
	 */
	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		importCsv("/servicelayer/test/testTax.csv", "windows-1252");
	}

	/**
	 * Tries to search the tax with code:
	 * <ul>
	 * <li>successful with "VAT_FULL",</li>
	 * <li>caught UnknownIdentifierException with "No_Such_Tax",</li>
	 * <li>found two taxes, that both contains "VAT".</li>
	 * </ul>
	 */
	@Test
	public void testGetTax()
	{
		String taxCode = "VAT_FULL";
		TaxModel tax = taxService.getTaxForCode(taxCode);
		assertNotNull(tax);
		assertEquals(taxCode, tax.getCode());

		taxCode = "No_Such_Tax";
		try
		{
			tax = taxService.getTaxForCode(taxCode);
			fail("the tax code [" + taxCode + "] should NOT be found.");
		}
		catch (final UnknownIdentifierException ue)
		{
			//expected
		}

		taxCode = "VAT";
		final Collection<TaxModel> taxes = taxService.getTaxesForCode("%" + taxCode + "%");
		assertEquals(2, taxes.size());
		for (final TaxModel _tax : taxes)
		{
			final boolean found = _tax.getCode().indexOf(taxCode) != -1;
			assertTrue(found);
		}
	}

}
