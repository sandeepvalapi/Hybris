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
package de.hybris.platform.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.numberseries.NumberGenerator;
import de.hybris.platform.jalo.numberseries.NumberSeries;
import de.hybris.platform.jalo.numberseries.NumberSeriesManager;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class NumberSeriesTest extends HybrisJUnit4Test
{
	private NumberSeriesManager numberSeriesManager;

	private static final int DECIMALS = 10;
	private static final String SERIES1 = "numSeries";
	private static final String SERIES2 = "alphanumSeries";
	private static final String SERIES3 = "nonTxSeries";
	private static final String SERIES4 = "templateSeries";

	@Before
	@SuppressWarnings("deprecation")
	public void setUp() throws Exception
	{
		numberSeriesManager = jaloSession.getNumberSeriesManager();
		numberSeriesManager.createNumberSeries(SERIES1, "034", NumberGenerator.NumberSeriesConstants.TYPES.NUMERIC, -1, null); // digits = 3 this way
		numberSeriesManager.createNumberSeries(SERIES2, "1ZY", NumberGenerator.NumberSeriesConstants.TYPES.ALPHANUMERIC, DECIMALS,
				null);

		numberSeriesManager.createNumberSeries(SERIES4, "135", NumberGenerator.NumberSeriesConstants.TYPES.ALPHANUMERIC, DECIMALS,
				"@-Order-$-DE\\$\\@\\\\");



	}

	@After
	public void tearDown() throws Exception
	{
		try
		{
			numberSeriesManager.removeNumberSeries(SERIES1);
		}
		catch (final JaloInvalidParameterException e) //NOPMD
		{
			//ok here
		}
		try
		{
			numberSeriesManager.removeNumberSeries(SERIES2);
		}
		catch (final JaloInvalidParameterException e)//NOPMD
		{
			//ok here
		}
		try
		{
			numberSeriesManager.removeNumberSeries(SERIES3);
		}
		catch (final JaloInvalidParameterException e)//NOPMD
		{
			//ok here
		}
		try
		{
			numberSeriesManager.removeNumberSeries("foo");
		}
		catch (final JaloInvalidParameterException e)//NOPMD
		{
			//ok here
		}
		try
		{
			numberSeriesManager.removeNumberSeries("bar");
		}
		catch (final JaloInvalidParameterException e)//NOPMD
		{
			//ok here
		}
		try
		{
			numberSeriesManager.removeNumberSeries("rawTest");
		}
		catch (final JaloInvalidParameterException e)//NOPMD
		{
			//ok here
		}
		try
		{
			numberSeriesManager.removeNumberSeries(SERIES4);
		}
		catch (final JaloInvalidParameterException e) //NOPMD
		{
			//ok here
		}
	}

	@Test
	public void testTA()
	{
		try
		{
			Config.setParameter(NumberSeriesManager.CONFIG_PARAM_NUMBER_CACHE + "." + SERIES3, "1");
			NumberSeries series = null;
			NumberSeries expectedSeries = null;

			// 1. test generate + rollback
			final Transaction tx1 = Transaction.current();
			tx1.begin();

			try
			{
				series = numberSeriesManager.createNumberSeries(SERIES3, "0010", NumberGenerator.NumberSeriesConstants.TYPES.NUMERIC,
						4);
				assertNotNull(series);
				assertEquals(SERIES3, series.getKey());
				assertEquals(NumberGenerator.NumberSeriesConstants.TYPES.NUMERIC, series.getType());

				assertEquals("0010", numberSeriesManager.getUniqueNumber(SERIES3));
				assertEquals("0011", numberSeriesManager.getUniqueNumber(SERIES3));

				expectedSeries = numberSeriesManager.getNumberSeries(SERIES3);
				assertNotNull(expectedSeries);
				assertEquals(series.getKey(), expectedSeries.getKey());
				assertEquals(series.getType(), expectedSeries.getType());

				assertEquals("12", expectedSeries.getFormatted(-1));
			}
			finally
			{
				tx1.rollback();
			}

			// XXX since digits are stored in meta information they're rolled back indeed - we have to fix it manually !!!
			numberSeriesManager.setDigits(SERIES3, 4);

			expectedSeries = numberSeriesManager.getNumberSeries(SERIES3);
			assertNotNull(expectedSeries);
			assertEquals(series.getKey(), expectedSeries.getKey());
			assertEquals(series.getType(), expectedSeries.getType());
			assertEquals("12", expectedSeries.getFormatted(-1));

			final Transaction tx2 = Transaction.current();
			tx2.begin();
			try
			{
				assertEquals("0012", numberSeriesManager.getUniqueNumber(SERIES3));
				assertEquals("0013", numberSeriesManager.getUniqueNumber(SERIES3));
				expectedSeries = numberSeriesManager.getNumberSeries(SERIES3);
				assertEquals("14", expectedSeries.getFormatted(-1));

			}
			finally
			{
				tx2.rollback();
			}

			expectedSeries = numberSeriesManager.getNumberSeries(SERIES3);
			assertEquals("14", expectedSeries.getFormatted(-1));

			final Transaction tx3 = Transaction.current();
			tx3.begin();
			boolean success = false;
			try
			{
				assertEquals("0014", numberSeriesManager.getUniqueNumber(SERIES3));
				assertEquals("0015", numberSeriesManager.getUniqueNumber(SERIES3));
				expectedSeries = numberSeriesManager.getNumberSeries(SERIES3);
				assertEquals("16", expectedSeries.getFormatted(-1));
				success = true;
			}
			finally
			{
				if (success)
				{
					tx3.commit();
				}
				else
				{
					tx3.rollback();
				}
			}

			expectedSeries = numberSeriesManager.getNumberSeries(SERIES3);
			assertEquals("16", expectedSeries.getFormatted(-1));
		}
		finally
		{
			Config.setParameter(NumberSeriesManager.CONFIG_PARAM_NUMBER_CACHE + "." + SERIES3, null);
		}
	}

	@Test
	public void testSeries()
	{
		try
		{
			// numeric
			assertNotNull(numberSeriesManager.getNumberSeries(SERIES1));
			assertEquals("34", numberSeriesManager.getNumberSeries(SERIES1).getFormatted(-1));
			assertEquals(34L, numberSeriesManager.getNumberSeries(SERIES1).getCurrentNumber());
			assertEquals("034", numberSeriesManager.getUniqueNumber(SERIES1));
			assertEquals("0000000035", numberSeriesManager.getUniqueNumber(SERIES1, 10));
			assertEquals("000036", numberSeriesManager.getUniqueNumber(SERIES1, 6));
			// alphanumeric
			assertNotNull(numberSeriesManager.getNumberSeries(SERIES2));
			assertEquals("1ZY", numberSeriesManager.getNumberSeries(SERIES2).getFormatted(-1));
			assertEquals(Long.parseLong("1ZY", 36), numberSeriesManager.getNumberSeries(SERIES2).getCurrentNumber());
			assertEquals("00000001ZY", numberSeriesManager.getUniqueNumber(SERIES2));
			assertEquals("000001ZZ", numberSeriesManager.getUniqueNumber(SERIES2, 8));
			assertEquals("000000000200", numberSeriesManager.getUniqueNumber(SERIES2, 12));
			assertEquals("00201", numberSeriesManager.getUniqueNumber(SERIES2, 5));
			assertEquals("202", numberSeriesManager.getNumberSeries(SERIES2).getFormatted(-1));
			assertEquals(Long.parseLong("202", 36), numberSeriesManager.getNumberSeries(SERIES2).getCurrentNumber());

			// test wrong key
			try
			{
				numberSeriesManager.getUniqueNumber("this series should not exists", 10);
				fail("expected JaloInvalidParameterException for key 'this series should not exists' but nothing happened");
			}
			catch (final JaloInvalidParameterException e) //NOPMD
			{
				// fine here
			}
			// test removal
			numberSeriesManager.removeNumberSeries(SERIES1);
			try
			{
				numberSeriesManager.getUniqueNumber(SERIES1, 10);
				fail("S1 was not propertly removed");
			}
			catch (final JaloInvalidParameterException e)//NOPMD
			{
				//fine here
			}

			numberSeriesManager.removeNumberSeries(SERIES2);
			try
			{
				numberSeriesManager.getUniqueNumber(SERIES2, 10);
				fail("S2 was not propertly removed");
			}
			catch (final JaloInvalidParameterException e)//NOPMD
			{
				// fine here
			}
		}
		catch (final JaloInvalidParameterException e)
		{
			fail("unexpected JaloInvalidParameterException : " + e);
		}
	}

	@Test
	public void testTemplateSeries()
	{
		assertNotNull(numberSeriesManager.getNumberSeries(SERIES4));
		assertEquals(MasterTenant.getInstance().getClusterIslandPK() + "-Order-135-DE\\$\\@\\", numberSeriesManager
				.getNumberSeries(SERIES4).getFormatted(3));
	}
}
