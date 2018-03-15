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
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.GenericItemEJBImpl;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class GenericItemTypeTest extends HybrisJUnit4TransactionalTest
{
	private TypeManager typeManager;

	@Before
	public void setUp() throws Exception
	{
		typeManager = jaloSession.getTypeManager();
	}

	@Test
	public void testGenericItemType() throws JaloItemNotFoundException
	{
		final ComposedType theType = typeManager.getRootComposedType(Constants.TC.GenericTestItem);
		assertFalse("generic test item type should not be abstract", theType.isAbstract());
		assertEquals("typecode didnt match", Constants.TC.GenericTestItem, theType.getItemTypeCode());
		//is now wrong: assertEquals( "jndi name didnt match", PersistenceManager.getJNDIName(Constants.TC.GenericTestItem), theType.getJNDIName() );
		assertTrue("generic test item deployment must be 'generic'",
				Registry.getPersistenceManager().getItemDeployment(Constants.TC.GenericTestItem).isGeneric());
		assertEquals("jndi name didnt match", "de.hybris.platform.persistence.GenericTestItem", Registry.getPersistenceManager()
				.getJNDIName(Constants.TC.GenericTestItem));
		assertTrue("jalo class didnt match", GenericItem.class.isAssignableFrom(theType.getJaloClass()));
		assertEquals(typeManager.getComposedType(GenericItem.class), theType.getSuperType());
	}

	@Test
	public void testGenericItemCreationViaType() throws JaloBusinessException
	{
		final ComposedType theType = typeManager.getRootComposedType(Constants.TC.GenericTestItem);
		final GenericItem item;
		assertNotNull(item = (GenericItem) theType.newInstance(Collections.EMPTY_MAP));
		assertNotNull(item);
		assertEquals(theType, item.getComposedType());
		final int tc = item.getPK().getTypeCode();
		assertEquals(tc, Constants.TC.GenericTestItem);
		assertEquals(jaloSession.getItem(item.getPK()), item);
	}

	@Test
	public void testGenericItemCreationViaTypeCode() throws JaloBusinessException
	{
		final GenericItem item;
		assertNotNull(item = GenericItemEJBImpl.createGenericItem((PK) null, Constants.TC.GenericTestItem, null));
		assertNotNull(item);

		final int tc = item.getPK().getTypeCode();

		assertEquals(tc, Constants.TC.GenericTestItem);
		assertEquals(jaloSession.getItem(item.getPK()), item);
	}
}
