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

import de.hybris.platform.core.Registry;
import de.hybris.platform.jmx.mbeans.impl.AbstractJMXMBean;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler;


/**
 * Base class for JMX tests
 */
@Ignore
public abstract class AbstractHybrisJmxTest extends HybrisJUnit4Test
{

	private ApplicationContext ctx = null;

	@Before
	public void initContext() throws JMException
	{
		ctx = Registry.getApplicationContext();
	}

	protected MBeanRegisterUtilities getJMXRegistry()
	{
		return ctx.getBean("mbeanRegisterUtility", MBeanRegisterUtilities.class);
	}


	protected MetadataMBeanInfoAssembler getJMXAssembler()
	{
		return ctx.getBean("assembler", MetadataMBeanInfoAssembler.class);
	}

	/**
	 * gets all spring beans registered as JMX beans as map where key = jmx bean name (), value = is jmx bean instance
	 */
	protected Map<String, AbstractJMXMBean> getAllSpringJMXBeans()
	{
		final Map<String, AbstractJMXMBean> definedBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors( //
				ctx, //
				AbstractJMXMBean.class, true, true); //base class for all JMXBeans

		final Map<String, AbstractJMXMBean> definedBeansByBeanName = new HashMap<String, AbstractJMXMBean>(definedBeans.size());

		for (final String beanName : definedBeans.keySet())
		{

			definedBeansByBeanName.put(definedBeans.get(beanName).getObjectNameString(), definedBeans.get(beanName));
		}

		return definedBeansByBeanName;
	}



	/**
	 * checks if for a bean meta information retrieved by {@link MetadataMBeanInfoAssembler} implementation no duplicate
	 * operation is retrieved for a specific attribute (see : HOR-683)
	 */
	protected void checkDuplicatedOperations(final AbstractJMXMBean bean, final MBeanInfo infoToVerify)
	{

		final Set<String> allAttrs = new HashSet<String>();
		for (final MBeanAttributeInfo attrs : infoToVerify.getAttributes())
		{
			allAttrs.add(attrs.getName());
		}

		for (final MBeanOperationInfo oper : infoToVerify.getOperations())
		{

			final boolean isBooleanType = "java.lang.boolean".equals(oper.getReturnType())
					|| "java.lang.Boolean".equals(oper.getReturnType());

			Assert.assertFalse("Already defined attribute for method " + oper.getName() + " in bean " + bean + " ", allAttrs
					.contains((isBooleanType ? oper.getName().substring("is".length()) : oper.getName().substring("get".length()))));

		}

	}

	/**
	 * checks if all methods exposed in interface for jmx bean and annotated as {@link ManagedAttribute} are in its
	 * implementation is assembled correctly by JMX assembler
	 */
	protected void checkAllAttributtes(final AbstractJMXMBean bean, final MBeanInfo infoToVerify)
	{
		final Map<String, Method> attributableMethods = new HashMap<String, Method>();

		for (final Method m : bean.getClass().getMethods())
		{
			if (m.isAnnotationPresent(ManagedAttribute.class))
			{
				attributableMethods.put(m.getName(), m);
			}
		}

		for (final MBeanAttributeInfo mbai : infoToVerify.getAttributes())
		{
			verifyMethodSignature("", attributableMethods, mbai);

			verifyMethodSignature("get", attributableMethods, mbai);

			verifyMethodSignature("is", attributableMethods, mbai);

		}
	}


	/**
	 * checks if all methods exposed in interface for jmx bean and annotated as {@link ManagedOperation} are in its
	 * implementation is assembled correctly by JMX assembler
	 */
	protected void checkAllOperations(final AbstractJMXMBean bean, final MBeanInfo infoToVerify)
	{
		final Map<String, Method> attributableMethods = new HashMap<String, Method>();

		for (final Method m : bean.getClass().getMethods())
		{
			if (m.isAnnotationPresent(ManagedOperation.class))
			{
				attributableMethods.put(m.getName(), m);
			}
		}

		for (final MBeanAttributeInfo mbai : infoToVerify.getAttributes())
		{
			verifyMethodSignature("", attributableMethods, mbai);

			verifyMethodSignature("get", attributableMethods, mbai);

			verifyMethodSignature("is", attributableMethods, mbai);

		}
	}

	/**
	 * verify method signature
	 */
	protected void verifyMethodSignature(final String prefix, final Map<String, Method> attributableMethods,
			final MBeanAttributeInfo mbai)
	{
		if (attributableMethods.containsKey(prefix + mbai.getName()))
		{
			final Method method = attributableMethods.get(prefix + mbai.getName());
			Assert.assertEquals("Method name in mbean info has changed", prefix + mbai.getName(), method.getName());
			Assert.assertEquals("Method type in mbean info has changed", mbai.getType(), method.getReturnType().getName());
		}
	}
}
