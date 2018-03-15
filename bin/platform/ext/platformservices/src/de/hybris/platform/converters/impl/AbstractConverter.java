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
package de.hybris.platform.converters.impl;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.lang.reflect.Method;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ReflectionUtils;


/**
 * Abstract implementation of the Converter interface that also supports the Populator interface. Implementations of
 * this base type can either be used as a converter or as a populator. When used as a converter the
 * {@link #createTarget()} method is called to create the target instance and then the {@link #populate(Object, Object)}
 * method is called to populate the target with values from the source instance. The {@link #createTarget()} method can
 * be implemented via a spring <tt>lookup-method</tt> rather than being overridden in code.
 */
public abstract class AbstractConverter<SOURCE, TARGET>
		implements Converter<SOURCE, TARGET>, Populator<SOURCE, TARGET>, InitializingBean, BeanNameAware
{
	private Class<TARGET> targetClass;

	@Override
	public TARGET convert(final SOURCE source) throws ConversionException
	{
		final TARGET target = targetClass == null ? createTarget() : createFromClass();
		populate(source, target);
		return target;
	}

	/**
	 * @deprecated since 6.0.0 - Do not call this method - only call the single argument{@link #convert(Object)} method.
	 */
	@Override
	@Deprecated
	public TARGET convert(final SOURCE source, final TARGET prototype) throws ConversionException
	{
		populate(source, prototype);
		return prototype;
	}

	/**
	 * Override this method to populate the target from the source
	 * 
	 * @param source
	 *           the source instance
	 * @param target
	 *           the target instance to fill
	 * @see #setTargetClass(Class)
	 */
	@Override
	public abstract void populate(final SOURCE source, final TARGET target);

	/**
	 * Allows to specify the target object class directly. Please use that instead of the deprecated
	 * <lookup-method name="createTarget" ref="bean"> approach, as it's way faster.
	 */
	public void setTargetClass(final Class<TARGET> targetClass)
	{
		this.targetClass = targetClass;

		// sanity check - can we instantiate that class ?
		if (targetClass != null)
		{
			createFromClass();
		}
	}

	protected TARGET createFromClass()
	{
		try
		{
			return targetClass.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * @deprecated please inject the target class directly, since it's way faster than the Spring
	 *             <lookup-method> approach!
	 */
	@Deprecated
	protected TARGET createTarget()
	{
		// optional - no longer requiring sub classes to implement this method
		throw new NotImplementedException();
	}

	// -------------------------------------------------------------------
	// --- Sanity check for the two different converter setups
	// -------------------------------------------------------------------

	private String myBeanName;

	/*
	 * for sanity checks only
	 */
	@Override
	public void setBeanName(final String name)
	{
		this.myBeanName = name;
	}

	public String getMyBeanName()
	{
		return myBeanName;
	}

	/*
	 * Ensures that either a class has been set or createTarget() has been overridden
	 */
	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (targetClass == null)
		{
			final Class<? extends AbstractConverter> cl = this.getClass();
			final Method createTargetMethod = ReflectionUtils.findMethod(cl, "createTarget");
			if (AbstractConverter.class.equals(createTargetMethod.getDeclaringClass()))
			{
				throw new IllegalStateException(
						"Converter '" + myBeanName + "' doesn't have a targetClass property nor does it override createTarget()!");
			}
		}
	}
}
