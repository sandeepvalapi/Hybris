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
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class UnitTypeTest extends HybrisJUnit4TransactionalTest
{
	TypeManager typeManager;
	ComposedType unitType;

	@Before
	public void setUp() throws Exception, JaloItemNotFoundException
	{
		typeManager = jaloSession.getTypeManager();
		unitType = typeManager.getComposedType(Unit.class);
	}

	@Test
	public void testInstance() throws ConsistencyCheckException, JaloInvalidParameterException
	{
		final Unit unit = jaloSession.getProductManager().createUnit("unitcode", "unittype");
		assertNotNull(unit);
		assertEquals(unitType, unit.getComposedType());
	}

	@Test
	public void testCreateViaType() throws Exception
	{
		final Map attributeValues = new HashMap();
		attributeValues.put(Unit.CODE, "myunit");
		attributeValues.put(Unit.UNITTYPE, "STUECK");
		attributeValues.put(Unit.CONVERSION, new Double(12));

		final Unit unit = (Unit) unitType.newInstance(attributeValues);
		assertNotNull(unit);

		assertEquals(unitType, unit.getComposedType());

		assertEquals("myunit", unit.getCode());

		assertEquals("STUECK", unit.getUnitType());
		assertTrue(12 == unit.getConversionFactor());
	}

	/** Create via type and give a string instead of an double for 'conversation' */
	@Test
	public void testCreateException()
	{
		final Map attributeValues = new HashMap();
		attributeValues.put(Unit.CODE, "myunit");
		attributeValues.put(Unit.TYPE, "STUECK");
		attributeValues.put(Unit.CONVERSION, "baeh!!");

		try
		{
			final Unit unit = (Unit) unitType.newInstance(attributeValues);
			assertNotNull(unit);
			fail("Exception expected!");
		}
		catch (final JaloInvalidParameterException e)
		{
			// DOCTODO document reason why this is empty
		}
		catch (final JaloGenericCreationException e)
		{
			fail("invalid exception type: " + e);
		}
		catch (final JaloAbstractTypeException e)
		{
			fail("unittype should not be abstract");
		}
	}

}
