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
package de.hybris.platform.jmx;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jmx.mbeans.impl.AbstractJMXMBean;

import java.util.HashMap;
import java.util.Map;

import javax.management.JMException;
import javax.management.MBeanInfo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler;


@IntegrationTest
public class HybrisJmxStructureTest extends AbstractHybrisJmxTest
{

	private MBeanRegisterUtilities jmxRegistry;
	private Map<String, AbstractJMXMBean> registeredBefore, unregisteredBefore;

	@Before
	public void setUp()
	{
		// get reg and unreg in system
		jmxRegistry = getJMXRegistry();
		registeredBefore = jmxRegistry.getRegisteredBeans();
		unregisteredBefore = jmxRegistry.getUnRegisteredBeans();
	}

	@After
	public void tearDown()
	{
		// restore reg and unreg in system
		jmxRegistry.registerMBeans(registeredBefore);
		jmxRegistry.unregisterMBeans(unregisteredBefore);
	}

	@Test
	public void testAutoRegistration() throws JMException
	{
		final Map<String, AbstractJMXMBean> definedBeans = getAllSpringJMXBeans();
		final MBeanRegisterUtilities registerUtility = this.jmxRegistry;
		final Map<String, AbstractJMXMBean> registeredBeans = registerUtility.getRegisteredBeans();

		final int registeredBeansSize = registeredBeans.size();
		final int definedBeansSize = definedBeans.size();

		Assert.assertEquals("Number of registered " + registeredBeansSize + " and defined " + definedBeansSize
				+ " beans should the same ", definedBeansSize, registeredBeansSize);

		for (final AbstractJMXMBean bean : definedBeans.values())
		{
			Assert.assertTrue("Bean " + bean + " has been defined  but is not instantiated ", registeredBeans.containsValue(bean));
		}
	}

	@Test
	public void testUnregistrationAutoRegistration() throws JMException
	{
		final MBeanRegisterUtilities registerUtility = this.jmxRegistry;

		Assert.assertTrue("Register list shouldn't be empty ", registerUtility.getRegisteredBeans().entrySet().iterator().hasNext());

		final Map.Entry<String, AbstractJMXMBean> firstOneEntry = registerUtility.getRegisteredBeans().entrySet().iterator().next();

		final Map<String, AbstractJMXMBean> toRemoveMap = new HashMap<String, AbstractJMXMBean>(1);
		toRemoveMap.put(firstOneEntry.getKey(), firstOneEntry.getValue());

		registerUtility.unregisterMBeans(toRemoveMap);

		Assert.assertFalse("Bean " + firstOneEntry.getKey() + " was removed ",
				registerUtility.getRegisteredBeans().containsKey(firstOneEntry.getKey()));

		Assert.assertFalse("Bean " + firstOneEntry.getKey() + " was removed ",
				registerUtility.getRegisteredBeans().containsValue(firstOneEntry.getValue()));
	}

	@Test
	public void testRegistrationAutoRegistration() throws JMException
	{
		final MBeanRegisterUtilities registerUtility = this.jmxRegistry;

		registerUtility.unregisterMBeans(registerUtility.getRegisteredBeans()); //remove all		

		Assert.assertFalse("Register list should be empty ", registerUtility.getRegisteredBeans().entrySet().iterator().hasNext());

		Assert.assertTrue("Unregister list shouldn't be empty ", registerUtility.getUnRegisteredBeans().entrySet().iterator()
				.hasNext());

		final Map.Entry<String, AbstractJMXMBean> firstOneEntry = registerUtility.getUnRegisteredBeans().entrySet().iterator()
				.next();

		final Map<String, AbstractJMXMBean> toAddMap = new HashMap<String, AbstractJMXMBean>(1);
		toAddMap.put(firstOneEntry.getKey(), firstOneEntry.getValue());

		registerUtility.registerMBeans(toAddMap);

		Assert.assertTrue("Bean " + firstOneEntry.getKey() + " was added ",
				registerUtility.getRegisteredBeans().containsKey(firstOneEntry.getKey()));

		Assert.assertTrue("Bean " + firstOneEntry.getKey() + " was added ",
				registerUtility.getRegisteredBeans().containsValue(firstOneEntry.getValue()));

		//try one more time 

		registerUtility.registerMBeans(toAddMap);

		Assert.assertTrue("Bean " + firstOneEntry.getKey() + " was added ",
				registerUtility.getRegisteredBeans().containsKey(firstOneEntry.getKey()));

		Assert.assertTrue("Bean " + firstOneEntry.getKey() + " was added ",
				registerUtility.getRegisteredBeans().containsValue(firstOneEntry.getValue()));
	}

	@Test
	public void testCustomAssemberApiExposure() throws JMException
	{

		final MetadataMBeanInfoAssembler assembler = getJMXAssembler(); //defined assembler 

		final Map<String, AbstractJMXMBean> beans = getAllSpringJMXBeans();

		for (final AbstractJMXMBean bean : beans.values())
		{
			final MBeanInfo infoToVerify = (MBeanInfo) assembler.getMBeanInfo(bean, bean.getBeanInterface().getName());

			checkAllAttributtes(bean, infoToVerify);

			checkAllOperations(bean, infoToVerify);

			checkDuplicatedOperations(bean, infoToVerify);
		}
	}

}
