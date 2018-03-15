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

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


@IntegrationTest
public class OptionalAttributeTest extends HybrisJUnit4TransactionalTest
{
	/**
	 * Try to create a unit without providing any attributes: at the moment the exception is not usable.
	 */
	@Test
	public void testSaveWithEmptyValues() throws Exception
	{
		// this is, what GenericItemChip.performSave does
		final Map attributeValues = new HashMap();
		attributeValues.put(Unit.CODE, null);
		attributeValues.put(Unit.UNITTYPE, null);

		try
		{
			final Item unit = jaloSession.getTypeManager().getRootComposedTypeForJaloClass(Unit.class).newInstance(attributeValues);
			assertNotNull(unit);
		}
		catch (final JaloAbstractTypeException e)
		{
			fail("Is not an abstract type!!");
		}
		catch (final JaloItemNotFoundException e)
		{
			fail("Type for Unit not found!!");
		}
		catch (final JaloInvalidParameterException e)
		{
			// fine here
			//assertEquals(JaloInvalidParameterException.class, e.getThrowable().getClass());
		}
	}

	/** Try to create a unit with providing all attributes. */
	@Test
	public void testSaveWithAllValues() throws Exception
	{
		final Map attributeValues = new HashMap();
		attributeValues.put(Unit.CODE, "unit1");
		attributeValues.put(Unit.UNITTYPE, "Stueck");
		attributeValues.put(Unit.CONVERSION, new Double(0.4));

		final Item unit = jaloSession.getTypeManager().getRootComposedTypeForJaloClass(Unit.class).newInstance(attributeValues);
		assertNotNull(unit);

		assertTrue(jaloSession.getProductManager().searchUnits("unit1", null).contains(unit));
	}
}
