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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Constants;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.RelationDescriptor;
import de.hybris.platform.jalo.type.RelationType;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.type.AtomicTypeRemote;
import de.hybris.platform.persistence.type.AttributeDescriptorRemote;
import de.hybris.platform.persistence.type.CollectionTypeRemote;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.persistence.type.EJBAbstractTypeException;
import de.hybris.platform.persistence.type.EJBDuplicateCodeException;
import de.hybris.platform.persistence.type.EJBDuplicateQualifierException;
import de.hybris.platform.persistence.type.TypeManagerEJB;
import de.hybris.platform.persistence.type.TypeRemote;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.EJBTools;
import de.hybris.platform.util.ItemPropertyValue;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class TypeManagerTest extends HybrisJUnit4TransactionalTest
{
	TypeManagerEJB typeManager;

	@Before
	public void setUp() throws Exception
	{
		typeManager = SystemEJB.getInstance().getTypeManager();
	}

	@Test
	public void testGetComposedType() throws EJBItemNotFoundException
	{
		final ComposedTypeRemote productType = typeManager.getRootComposedType(Constants.TC.Product);
		assertNotNull(productType);
		//assertEquals( ProductHome.class, productType.getItemHomeInterface() );
		try
		{
			final TypeRemote type = typeManager.getType("thetypewhodoesntexist");
			fail("expected EJBItemNotFoundException, found " + type);
		}
		catch (final EJBItemNotFoundException e)
		{
			// fine..
		}
	}

	@Test
	public void testGetRootAtomicType() throws EJBItemNotFoundException
	{
		final AtomicTypeRemote atomicType = typeManager.getRootAtomicType(String.class);
		assertEquals("java.lang.String", atomicType.getCode());
		assertEquals(String.class, atomicType.getJavaClass());
	}

	@Test
	public void testCreateComposedType() throws EJBItemNotFoundException, EJBDuplicateCodeException, EJBInvalidParameterException,
			EJBDuplicateQualifierException, ConsistencyCheckException, EJBAbstractTypeException
	{
		final ComposedTypeRemote productType = typeManager.getRootComposedType(Constants.TC.Product);
		ComposedTypeRemote customProductType = null;
		try
		{
			customProductType = typeManager.createNonRootComposedType(null, productType, "customProduct2", null, null, true);
			assertEquals(productType, customProductType.getSuperType());
			try
			{
				typeManager.getAttributeDescriptor(productType, "successor");
				fail("expected EJBItemNotFoundException");
			}
			catch (final EJBItemNotFoundException e)
			{
				// fine...
			}
			assertEquals("code", typeManager.getAttributeDescriptor(customProductType, "code").getQualifier());
			assertEquals("java.lang.String", typeManager.getAttributeDescriptor(customProductType, "code").getAttributeType()
					.getCode());
		}
		finally
		{
			if (customProductType != null)
			{

				typeManager.removeItem(customProductType);
			}
		}
	}

	@Test
	public void testCreateRelation() throws Exception
	{
		// UserType as 'members' ---{userUserGroupTestRelation}--> UserGroupType as 'groups'
		final String relationQualifer = "userUserGroupTestRelation";
		// src userType as 'members' -> ...  
		final String sourceQualifer = "membersTest";
		final ComposedTypeRemote sourceType = typeManager.getRootComposedType(Constants.TC.User);
		// tgt                   ... -> UserGroupType as 'groups'
		final String targetQualifer = "groupsTest";
		final ComposedTypeRemote targetType = typeManager.getRootComposedType(Constants.TC.UserGroup);
		// mod
		final int modifier = AttributeDescriptor.READ_FLAG | AttributeDescriptor.WRITE_FLAG | AttributeDescriptor.OPTIONAL_FLAG;
		final boolean localized = false;

		final ComposedTypeRemote relationMetaType = typeManager.getComposedType("RelationMetaType");
		assertNotNull(relationMetaType);

		final AttributeDescriptorRemote sourceFD, targetFD;
		final ComposedTypeRemote relationType;
		assertNotNull(relationType = typeManager.createRelationType(
				null,
				relationQualifer,
				localized,
				sourceFD = typeManager.createRelationDescriptor(null, relationQualifer, targetQualifer, sourceType, targetType,
						modifier, localized, false, CollectionType.COLLECTION),
				targetFD = typeManager.createRelationDescriptor(null, relationQualifer, sourceQualifer, targetType, sourceType,
						modifier, localized, false, CollectionType.COLLECTION)));
		typeManager.connectRelation(relationQualifer, false, relationType, sourceFD, targetFD, null, null);
		/*
		 * check relation type
		 */
		assertNotNull(relationType);
		assertEquals(relationQualifer, relationType.getCode());
		assertEquals(relationMetaType, relationType.getComposedType());
		assertEquals(Boolean.FALSE, relationType.getProperty(RelationType.LOCALIZED));
		/*
		 * check source FD user -> userGroup
		 */
		assertNotNull(sourceFD);
		final CollectionTypeRemote sourceCollType = (CollectionTypeRemote) sourceFD.getAttributeType();
		assertEquals(targetQualifer, sourceFD.getQualifier()); // qualifier 'groups'
		assertEquals(sourceType, sourceFD.getEnclosingType()); // enclodes by 'user'
		assertEquals(targetType, sourceCollType.getElementType()); // points to 'userGroup'
		assertFalse(sourceFD.isLocalized()); // not localized
		assertTrue(((Boolean) sourceFD.getProperty(RelationDescriptor.IS_SOURCE)).booleanValue()); // is source
		assertEquals(new ItemPropertyValue(relationType.getPK()), sourceFD.getProperty(RelationDescriptor.RELATION_TYPE));
		assertEquals(relationQualifer, sourceFD.getProperty(RelationDescriptor.RELATION_NAME));

		/*
		 * check target FD userGroup <- user
		 */
		assertNotNull(targetFD);
		final CollectionTypeRemote targetCollType = (CollectionTypeRemote) targetFD.getAttributeType();
		assertEquals(sourceQualifer, targetFD.getQualifier()); // qualifier 'members'
		assertEquals(targetType, targetFD.getEnclosingType()); // enclodes by 'userGroup'
		assertEquals(sourceType, targetCollType.getElementType()); // points to 'user'
		assertFalse(targetFD.isLocalized()); // not localized
		assertFalse(((Boolean) targetFD.getProperty(RelationDescriptor.IS_SOURCE)).booleanValue()); // not source
		assertEquals(new ItemPropertyValue(relationType.getPK()), targetFD.getProperty(RelationDescriptor.RELATION_TYPE));
		assertEquals(relationQualifer, targetFD.getProperty(RelationDescriptor.RELATION_NAME));
	}

	protected AttributeDescriptorRemote getAttributeDescriptor(final ComposedTypeRemote type, final String qualifer)
			throws Exception
	{
		final Collection fds = typeManager.getAttributeDescriptors(type, false);
		assertNotNull(fds);
		for (final Iterator it = fds.iterator(); it.hasNext();)
		{
			final AttributeDescriptorRemote fd = (AttributeDescriptorRemote) it.next();
			if (fd.getQualifier().equals(qualifer))
			{
				return fd;
			}
		}
		return null;
	}

	public static void assertEqualsTest(final ItemRemote expected, final ItemRemote actual)
	{
		Assert.assertTrue("expected <" + EJBTools.getPK(expected) + "> but was:<" + EJBTools.getPK(actual) + ">",
				expected == actual || expected != null && expected.equals(actual));
	}
}
