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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;


/**
 * A strategy for applying modification to target bean and all its child beans according to bean definition.
 */
public class ChildBeanPostProcessorStrategy
{
	private final ConfigurableListableBeanFactory beanFactory;

	public ChildBeanPostProcessorStrategy(final ConfigurableListableBeanFactory beanFactory)
	{
		this.beanFactory = beanFactory;
	}

	/**
	 * Applies operation to beans of type beanClass starting from bean provided by targetProvider and including all child
	 * beans.
	 *
	 * @param beanClass
	 *           class of beans to process
	 * @param targetProvider
	 *           starting bean defining bean hierarchy
	 * @param operation
	 *           operation executed on selected beans
	 *
	 */
	public <T> void process(final Class<T> beanClass, final Supplier<T> targetProvider, final Consumer<T> operation)
	{
		//all beans of given type
		final Map<String, T> targets = beanFactory.getBeansOfType(beanClass);
		//mapping bean -> its name
		final Map<T, String> targetsNames = targets.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
		//mapping bean name -> set of parent names
		final Map<String, Set<String>> parentNames = targets.keySet().stream()
				.collect(Collectors.toMap(e -> e, e -> getParentNames(e)));

		final String targetName = targetsNames.get(targetProvider.get());

		parentNames.entrySet().stream() //
				.filter(e -> e.getValue().contains(targetName)) //only beans where target exists in parent hierarchy
				.map(e -> e.getKey()) // name of bean
				.map(k -> targets.get(k)) // bean itself
				.filter(t -> t != null) // without nulls just in case
				.forEach(operation); //apply modification to them
	}

	/*
	 * Get parents name from bean definition (including given bean)
	 */
	private Set<String> getParentNames(final String beanName)
	{
		final Set<String> result = new HashSet<String>();

		String name = beanName;
		do
		{
			result.add(name);
			final BeanDefinition definition = beanFactory.getBeanDefinition(name);
			name = definition.getParentName();
		}
		while (name != null);
		return result;
	}
}
