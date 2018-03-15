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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.localization.TypeLocalization;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class LanguageTest extends HybrisJUnit4Test
{
	private static final Logger log = Logger.getLogger(LanguageTest.class.getName());

	Language l1 = null;
	Language l2 = null;

	@Before
	public void setUp() throws Exception
	{
		l1 = jaloSession.getC2LManager().createLanguage("TESTCODE1");
		l2 = jaloSession.getC2LManager().createLanguage("TESTCODE2");

	}

	@After
	public void tearDown() throws Exception
	{
		l1.remove();
		l2.remove();
	}

	@Test
	public void testGetAllLanguages()
	{
		assertFalse(jaloSession.getC2LManager().getAllLanguages().isEmpty());
	}

	@Test
	public void testBug307() throws Exception
	{
		///*conv-log*/ log.debug("testing isocodes...");
		l1.setIsoCode("THESAME");
		try
		{
			l2.setIsoCode("THESAME");
			fail("same isocode possible");
		}
		catch (final ConsistencyCheckException e)
		{
			// DOCTODO Document reason, why this block is empty
		}
	}

	@Test
	public void testLocalizationMap() throws Exception
	{
		final int cnt = C2LManager.getInstance().getAllLanguages().size();
		final int cnt2 = TypeLocalization.getInstance().getLocalizations().keySet().size();
		assertEquals(cnt, cnt2);
		Language l = null;
		try
		{
			log.info("now should be cleared!");
			l = C2LManager.getInstance().createLanguage("anewone");
			assertEquals(cnt + 1, TypeLocalization.getInstance().getLocalizations().keySet().size());
		}
		catch (final Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				if (l != null)
				{
					l.remove();
				}
			}
			finally
			{
				// DOCTODO Document reason, why this block is empty
			}
		}
		assertEquals(cnt, TypeLocalization.getInstance().getLocalizations().keySet().size());
	}

}
