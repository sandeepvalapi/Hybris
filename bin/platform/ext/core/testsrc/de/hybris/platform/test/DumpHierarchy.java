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

import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.type.AttributeDescriptorRemote;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.persistence.type.TypeManagerEJB;
import de.hybris.platform.persistence.type.TypeTools;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.junit.Test;


@IntegrationTest
public class DumpHierarchy extends HybrisJUnit4TransactionalTest
{
	private static final Logger LOG = Logger.getLogger(DumpHierarchy.class.getName());

	@Test
	public void testDump()
	{
		try
		{
			dumpTypeHierarchie(SystemEJB.getInstance().getTypeManager().getRootComposedTypeForJaloClass(Item.class.getName()), 0);
		}
		catch (final EJBItemNotFoundException e)
		{
			fail(e.getMessage());
		}
	}

	private void dumpTypeHierarchie(final ComposedTypeRemote type, final int level)
	{
		final char[] spacesArray = new char[2 * level];
		Arrays.fill(spacesArray, ' ');
		final String spaces = new String(spacesArray);
		LOG.info(spaces + TypeTools.asString(type));
		final TypeManagerEJB typeManagerEJB = SystemEJB.getInstance().getTypeManager();
		for (final Iterator iter = typeManagerEJB.getDeclaredAttributeDescriptors(type).iterator(); iter.hasNext();)
		{
			final AttributeDescriptorRemote attributeDescriptorRemote = (AttributeDescriptorRemote) iter.next();

			LOG.info(spaces + " " + attributeDescriptorRemote.getQualifier() + "("
					+ modifiersToString(attributeDescriptorRemote.getModifiers()) + "):"
					+ TypeTools.asString(attributeDescriptorRemote.getComposedType()));
		}

		for (final Iterator iter = typeManagerEJB.getSubTypes(type).iterator(); iter.hasNext();)
		{
			dumpTypeHierarchie((ComposedTypeRemote) iter.next(), level + 1);
		}
	}

	private static String modifiersToString(final int mod)
	{
		final StringBuilder stringBuilder = new StringBuilder();

		if ((mod & AttributeDescriptor.READ_FLAG) != 0)
		{
			stringBuilder.append('r');
		}

		if ((mod & AttributeDescriptor.WRITE_FLAG) != 0)
		{
			stringBuilder.append('w');
		}

		if ((mod & AttributeDescriptor.REMOVE_FLAG) != 0)
		{
			stringBuilder.append('R');
		}

		if ((mod & AttributeDescriptor.OPTIONAL_FLAG) != 0)
		{
			stringBuilder.append('O');
		}

		if ((mod & AttributeDescriptor.SEARCH_FLAG) != 0)
		{
			stringBuilder.append('S');
		}

		if ((mod & AttributeDescriptor.PARTOF_FLAG) != 0)
		{
			stringBuilder.append('P');
		}

		if ((mod & AttributeDescriptor.PROPERTY_FLAG) != 0)
		{
			stringBuilder.append('P');
		}

		if ((mod & AttributeDescriptor.LOCALIZED_FLAG) != 0)
		{
			stringBuilder.append('L');
		}

		return stringBuilder.toString();
	}

}
