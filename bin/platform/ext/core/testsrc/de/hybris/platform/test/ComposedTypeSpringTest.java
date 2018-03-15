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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


@IntegrationTest
public class ComposedTypeSpringTest extends HybrisJUnit4TransactionalTest
{

	@Test
	public void testSpringCreation() throws JaloInvalidParameterException, JaloDuplicateCodeException,
			JaloGenericCreationException, JaloAbstractTypeException
	{
		final ComposedType unitType = TypeManager.getInstance().getComposedType(Unit.class);
		final ComposedType ct = TypeManager.getInstance().createComposedType(unitType, "MyUnit");
		ct.setJaloClass(MyUnit.class);

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(Unit.CODE, "code");
		params.put(Unit.UNITTYPE, "type");

		final MyUnit u = (MyUnit) ct.newInstance(params);

		assertEquals("<code>", u.getCode());
		assertEquals("type", u.getUnitType());
		assertTrue(u instanceof MySpringUnit);

		params.clear();
		params.put(Unit.CODE, "code2");
		params.put(Unit.UNITTYPE, "type");

		final Unit controlUnit = (Unit) unitType.newInstance(params);

		assertEquals("code2", controlUnit.getCode());
		assertEquals("type", controlUnit.getUnitType());
		assertFalse(controlUnit instanceof MyUnit);
	}
}
