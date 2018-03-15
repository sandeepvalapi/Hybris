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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.extension.ExtensionEJB;
import de.hybris.platform.persistence.extension.ExtensionManagerEJB;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class TypecodeTest extends HybrisJUnit4TransactionalTest
{
	static final Logger log = Logger.getLogger(TypecodeTest.class.getName());
	private ExtensionManagerEJB em;

	@Before
	public void setUp() throws Exception
	{
		em = SystemEJB.getInstance().getExtensionManager();
	}

	@Test
	public void testTypecodes() throws Exception
	{
		// --- get core typecodes per reflection -----------------------------
		final Class constants = Class.forName(de.hybris.platform.core.Constants.class.getName());
		final Class[] innerClasses = constants.getDeclaredClasses();
		Class tc = null;
		for (int i = 0; i < innerClasses.length; i++)
		{
			if (innerClasses[i].getName().endsWith("TC"))
			{
				tc = innerClasses[i];
				break;
			}
		}
		assertNotNull("inner class TC of EJBConstants not found", tc);
		final Field[] tcFields = tc.getDeclaredFields();
		final Map coreTCs = new HashMap();
		for (int i = 0; i < tcFields.length; i++)
		{
			final int modifiers = tcFields[i].getModifiers();
			// only public static final fields
			if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers))
			{
				final Integer tcValue = Integer.valueOf(tcFields[i].getInt(null));
				assertFalse(
						"core typecode " + tcValue + " of field " + tcFields[i].getName() + " clashes with "
								+ (String) coreTCs.get(tcValue), coreTCs.containsKey(tcValue));
				coreTCs.put(tcValue, "core." + tcFields[i].getName());
				/* conv-log */log.debug("added " + tcValue + " for core." + tcFields[i].getName());
			}
		}
		assertFalse("no core typecodes found", coreTCs.isEmpty());
		// -- get extension typecodes ---
		for (final Iterator it = em.getAllExtensionNames().iterator(); it.hasNext();)
		{
			final String name = (String) it.next();
			final ExtensionEJB ext = em.getExtension(name);
			assertEquals("info name \"" + ext.getName() + "\" does not match extension name \"" + name + "\"", name, ext.getName());
			//	      for( Iterator tcs = info.getSupportedTypecodes().iterator(); tcs.hasNext();)
			//	      {
			//	         Integer tcValue = (Integer)tcs.next();
			//   	      assertTrue( "extension typecode "+ tcValue +
			//   	              " clashes with " + (String)coreTCs.get( tcValue ) ,
			//	              !coreTCs.containsKey( tcValue ) );
			//            coreTCs.put( tcValue, "extension:"+name );
			//   	      /*conv-log*/ log.debug( "added " + tcValue + " for extension." + name );
			//	      }
		}
	}

}
