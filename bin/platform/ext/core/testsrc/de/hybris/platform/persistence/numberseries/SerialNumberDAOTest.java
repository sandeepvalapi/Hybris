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
package de.hybris.platform.persistence.numberseries;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.numberseries.NumberGenerator;
import de.hybris.platform.testframework.HybrisJUnit4ClassRunner;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.testframework.RunListeners;
import de.hybris.platform.testframework.Transactional;
import de.hybris.platform.testframework.runlistener.PlatformRunListener;
import de.hybris.platform.testframework.runlistener.TransactionRunListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(HybrisJUnit4ClassRunner.class)
@RunListeners(
{ TransactionRunListener.class, PlatformRunListener.class })
@Transactional
public class SerialNumberDAOTest extends HybrisJUnit4TransactionalTest
{
	private static final String TEMPLATE = "Order-$-DE";
	private static final String KEY = "KEY";
	private SerialNumberDAO serialNumberDAO;

	@Before
	public void setUp()
	{
		serialNumberDAO = new DefaultSerialNumberDAO(Registry.getCurrentTenant(), Registry.getCurrentTenant().getDataSource());
		serialNumberDAO.createSeries(KEY, NumberGenerator.NumberSeriesConstants.TYPES.ALPHANUMERIC, 0, TEMPLATE);
	}

	@Test
	public void testSerialNumberDAO()
	{
		assertEquals(NumberGenerator.NumberSeriesConstants.TYPES.ALPHANUMERIC, serialNumberDAO.getCurrent(KEY).getType());
		assertEquals(TEMPLATE, serialNumberDAO.getCurrent(KEY).getTemplate());
		assertEquals("Order-00000-DE", serialNumberDAO.getCurrent(KEY).getFormatted(5));
		serialNumberDAO.resetSeries(KEY, NumberGenerator.NumberSeriesConstants.TYPES.NUMERIC, 5);
		assertEquals("Order-00005-DE", serialNumberDAO.getCurrent(KEY).getFormatted(5));
	}

	@After
	public void tearDown()
	{
		serialNumberDAO.removeSeries(KEY);
	}

}
