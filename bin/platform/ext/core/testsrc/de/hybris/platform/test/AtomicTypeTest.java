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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.testframework.Assert;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class AtomicTypeTest extends HybrisJUnit4TransactionalTest
{
	private TypeManager typeManager;

	@Before
	public void setUp() throws Exception
	{
		typeManager = jaloSession.getTypeManager();
	}

	@Test
	public void testCreate() throws Exception
	{
		final AtomicType type = typeManager.createAtomicType(java.io.IOException.class);
		assertNotNull("Creation of type results in null", type);
		assertEquals("Unexpected type code after creation", "java.io.IOException", type.getCode());
		assertEquals("Unexpected java class of created type", java.io.IOException.class, type.getJavaClass());
		final AtomicType sub = typeManager.createAtomicType(type, "atTest");
		assertNotNull("Creation of sub type results in null", sub);
		assertEquals("Unexpected type code after creation of sub type", "atTest", sub.getCode());
		assertEquals("Unexpected java class of created sub type", java.io.IOException.class, sub.getJavaClass());
	}

	@Test
	public void testJavaClass() throws JaloDuplicateCodeException, JaloInvalidParameterException, ConsistencyCheckException
	{
		try
		{
			final AtomicType type = typeManager.createAtomicType(de.hybris.platform.jalo.user.Employee.class);
			type.remove();
			fail("created atomic type for subclass of Item");
		}
		catch (final JaloInvalidParameterException e)//NOPMD
		{
			// fine...
		}
	}

	@Test
	public void testCode() throws JaloDuplicateCodeException, ConsistencyCheckException, JaloInvalidParameterException
	{
		final Collection composedTypes = typeManager.getAllComposedTypes();
		assertFalse("No composed types in system", composedTypes.isEmpty());
		final String usedCode = ((ComposedType) composedTypes.iterator().next()).getCode();
		try
		{
			AtomicType type;
			type = typeManager.createAtomicType(java.lang.Integer.class);
			assertNotNull("Creation of type results in null", type);
			final AtomicType sub = typeManager.createAtomicType(type, usedCode);
			assertNotNull("Creation of type results in null", sub);
			fail("expected JaloDuplicateCodeException for " + usedCode);
		}
		catch (final JaloDuplicateCodeException e) //NOPMD
		{
			// fine...
		}
	}

	@Test
	public void testAtomicTypeHierarchy() throws JaloItemNotFoundException, JaloInvalidParameterException,
			JaloDuplicateCodeException
	{
		final AtomicType stringType = typeManager.getRootAtomicType(String.class);
		assertEquals("Unexpected java class of created type", String.class, stringType.getJavaClass());
		assertEquals("Unexpected sub types found", Collections.EMPTY_SET, stringType.getSubTypes());
		final AtomicType descriptionType = typeManager.createAtomicType(stringType, "at-description");
		assertNotNull("Type creation results in null", descriptionType);
		assertEquals("Unexpected java class of created type", String.class, descriptionType.getJavaClass());
		assertEquals("Description was not set at creation", "at-description", descriptionType.getCode());
		assertEquals("Super type is null after creation", stringType, descriptionType.getSuperType());
		Assert.assertCollectionElements(stringType.getSubTypes(), descriptionType);
		assertTrue("Super type not assignable by sub type", stringType.isAssignableFrom(descriptionType));
		assertTrue("Sub type is not instance of super type", descriptionType.isInstance("string"));
	}
}
