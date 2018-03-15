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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.testframework.Assert;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.StopWatch;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class EnumerationTest extends HybrisJUnit4TransactionalTest
{
	private EnumerationType enumTypeA, enumTypeB;

	@Before
	public void setUp() throws Exception
	{
		enumTypeA = jaloSession.getEnumerationManager().createDefaultEnumerationType("enumTestA");
		enumTypeB = jaloSession.getEnumerationManager().createDefaultEnumerationType("enumTestB");
	}

	@Test
	public void testCreate() throws ConsistencyCheckException, JaloInvalidParameterException, JaloItemNotFoundException
	{
		final EnumerationManager enumManager = jaloSession.getEnumerationManager();
		final EnumerationValue vA1 = enumManager.createEnumerationValue(enumTypeA, "a1");

		assertNull(vA1.getName());
		vA1.setName("va1Name");
		assertEquals("va1Name", vA1.getName());
		vA1.setName(null);
		assertNull(vA1.getName());

		final EnumerationValue vA2 = enumManager.createEnumerationValue(enumTypeA, "a2");

		assertNull(vA2.getName());
		vA2.setName("va2Name");
		assertEquals("va2Name", vA2.getName());
		vA2.setName(null);
		assertNull(vA2.getName());

		final EnumerationValue vB1 = enumManager.createEnumerationValue(enumTypeB, "a1");

		assertEquals("a1", vA1.getCode());
		Assert.assertCollectionElements(enumTypeA.getValues(), vA1, vA2);
		Assert.assertCollectionElements(enumTypeB.getValues(), vB1);
		assertEquals(vA2, enumManager.getEnumerationValue(enumTypeA, "a2"));
		try
		{
			final EnumerationValue vA3 = enumManager.createEnumerationValue(enumTypeA, "a2");
			vA3.remove();
			fail("duplicate value code a2");
		}
		catch (final ConsistencyCheckException e)
		{
			// fine...
		}
		vA2.setCode("a2");
		assertEquals("a2", vA2.getCode());
		assertEquals(vA1, enumManager.getEnumerationValue(enumTypeA, "a1"));
		try
		{
			vA2.setCode("a1");
			fail("duplicate value code a1");
		}
		catch (final ConsistencyCheckException e)
		{
			// fine...
		}
	}

	@Test
	public void testEnumCaching() throws Exception
	{
		final EnumerationManager em = EnumerationManager.getInstance();
		final StopWatch w = new StopWatch("getenumerationtype");
		for (int i = 0; i < 10000; i++)
		{
			em.getEnumerationType("enumTestA");
		}
		w.stop();
	}

	@Test
	public void testEnumCaching2() throws Exception
	{
		final EnumerationManager em = EnumerationManager.getInstance();
		/* final EnumerationValue vA1 = */em.createEnumerationValue(enumTypeA, "a1");

		final StopWatch w = new StopWatch("getenumerationvalue");
		for (int i = 0; i < 10000; i++)
		{
			em.getEnumerationValue("enumTestA", "a1");
		}
		w.stop();
	}

	@Test
	public void testSequence() throws JaloInvalidParameterException, ConsistencyCheckException
	{
		final EnumerationManager enumManager = jaloSession.getEnumerationManager();
		EnumerationValue v1, v2, v3;
		v2 = enumManager.createEnumerationValue(enumTypeA, "2");

		v3 = enumManager.createEnumerationValue(enumTypeA, "3");

		v1 = enumManager.createEnumerationValue(enumTypeA, "1");

		assertEquals(Arrays.asList(new Object[]
		{ v2, v3, v1 }), enumTypeA.getValues());
		enumTypeA.sortValues(Arrays.asList(new Object[]
		{ v1, v2, v3 }));
		assertEquals(Arrays.asList(new Object[]
		{ v1, v2, v3 }), enumTypeA.getValues());
		//		try
		//		{
		//			enumTypeA.sortValues( Arrays.asList( new Object[]{ v1, v2 } ) );
		//			fail("incomplete sequence");
		//		}
		//		catch( JaloInvalidParameterException e )
		//		{
		//			// fine
		//		}
	}

	@Test
	public void testDynamicEnum() throws ConsistencyCheckException
	{
		final EnumerationType enumType = EnumerationManager.getInstance().getEnumerationType("EncodingEnum");
		assertTrue(enumType.isDynamic());
		final EnumerationValue enumValue = EnumerationManager.getInstance().createEnumerationValue(enumType, "Test");
		assertNotNull(enumValue);
		assertEquals("Test", enumValue.getCode());
	}

	@Test(expected = ConsistencyCheckException.class)
	public void testFixedEnum() throws ConsistencyCheckException
	{
		final EnumerationType enumType = EnumerationManager.getInstance().getEnumerationType("ImpExProcessModeEnum");
		assertFalse(enumType.isDynamic());
		EnumerationManager.getInstance().createEnumerationValue(enumType, "test");
	}

	@Test
	public void testDynamicEnumCreate() throws ConsistencyCheckException, JaloDuplicateCodeException
	{
		final EnumerationType enumType = EnumerationManager.getInstance().createEnumerationType("bla", null);
		assertTrue(enumType.isDynamic());
		final EnumerationValue enumValue = EnumerationManager.getInstance().createEnumerationValue(enumType, "test");
		assertNotNull(enumValue);
	}

	// HORST-1599
	@Test
	public void testCodeLowerCaseAttribute() throws ConsistencyCheckException, JaloDuplicateCodeException,
			JaloInvalidParameterException, JaloSecurityException
	{
		final EnumerationType enumType = EnumerationManager.getInstance().createEnumerationType("SomeType", null);
		assertTrue(enumType.isDynamic());
		final EnumerationValue enumValue = EnumerationManager.getInstance().createEnumerationValue(enumType, "UPPER_CODE");
		assertNotNull(enumValue);
		assertEquals("UPPER_CODE", enumValue.getCode());
		assertEquals("UPPER_CODE", enumValue.getAttribute(EnumerationValue.CODE));
		assertEquals("upper_code", enumValue.getAttribute("codeLowerCase"));
	}

}
