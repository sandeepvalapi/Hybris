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
package de.hybris.platform.order.strategies.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.strategies.ordercloning.CloneAbstractOrderStrategy;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * Generic strategy for abstract order cloning, taking target order and order entry classes as generic parameters.
 */
public class GenericAbstractOrderCloningStrategy<T extends AbstractOrderModel, E extends AbstractOrderEntryModel, O extends AbstractOrderModel>
{
	private CloneAbstractOrderStrategy cloneAbstractOrderStrategy;
	private KeyGenerator keyGenerator;
	private final Class<T> abstractOrderResultClass;
	private final Class<E> abstractOrderEntryResultClass;
	private final Class<O> originalAbstractOrderClass;

	/**
	 * Constructor taking the target classes for order and order entry type as parameters. Must be called from within
	 * constructors extending this class.
	 */
	public GenericAbstractOrderCloningStrategy(final Class<T> abstractOrderResultClass,
			final Class<E> abstractOrderEntryResultClass, final Class<O> originalAbstractOrderClass)
	{
		this.abstractOrderResultClass = abstractOrderResultClass;
		this.abstractOrderEntryResultClass = abstractOrderEntryResultClass;
		this.originalAbstractOrderClass = originalAbstractOrderClass;
	}

	protected T clone(final O original, final Optional<String> code)
	{
		return getCloneAbstractOrderStrategy().clone(null, null, original, code.isPresent() ? code.get() : generateCode(),
				abstractOrderResultClass, abstractOrderEntryResultClass);
	}

	protected String generateCode()
	{
		final Object generatedValue = getKeyGenerator().generate();
		if (generatedValue instanceof String)
		{
			return (String) generatedValue;
		}
		else
		{
			return String.valueOf(generatedValue);
		}
	}

	@SuppressWarnings("unused")
	protected void postProcess(final O original, final T copy)
	{
		// extension point
	}

	protected CloneAbstractOrderStrategy getCloneAbstractOrderStrategy()
	{
		return cloneAbstractOrderStrategy;
	}

	@Required
	public void setCloneAbstractOrderStrategy(final CloneAbstractOrderStrategy cloneAbstractOrderStrategy)
	{
		this.cloneAbstractOrderStrategy = cloneAbstractOrderStrategy;
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}

	@Required
	public void setKeyGenerator(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	protected Class<T> getAbstractOrderResultClass()
	{
		return abstractOrderResultClass;
	}

	protected Class<E> getAbstractOrderEntryResultClass()
	{
		return abstractOrderEntryResultClass;
	}

	protected Class<O> getOriginalAbstractOrderClass()
	{
		return originalAbstractOrderClass;
	}
}
