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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class UnicodeTest extends HybrisJUnit4TransactionalTest
{
	Language language;
	C2LManager c2lManager;

	@Before
	public void setUp() throws Exception
	{
		c2lManager = null;
		language = null;
		c2lManager = C2LManager.getInstance();
		try
		{
			language = c2lManager.createLanguage(null, "unicodeL");
		}
		catch (final ConsistencyCheckException e)
		{
			language = c2lManager.getLanguageByIsoCode("unicodeL");
		}
	}

	@After
	public void tearDown() throws Exception
	{
		if (language != null)
		{
			language.remove();
			language = null;
		}
	}

	static final String value = "Z\u0142oty-\u00c4pfel";

	@Test
	public void testExtendedProperty() throws Exception
	{
		//SystemEJB.getInstance().err.println("--------------"+value+"---");
		assertEquals(11, value.length());
		language.setProperty("key", value);
		assertEquals(value, language.getProperty("key"));
	}

	@Test
	public void testLocalizedProperty() throws Exception
	{
		//SystemEJB.getInstance().err.println("--------------"+value+"---");
		assertEquals(11, value.length());
		language.setLocalizedProperty("key", value);
		assertEquals(value, language.getLocalizedProperty("key"));
	}

}
