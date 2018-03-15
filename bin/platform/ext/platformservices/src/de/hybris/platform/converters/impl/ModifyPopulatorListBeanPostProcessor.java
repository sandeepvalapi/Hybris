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

import de.hybris.platform.converters.PopulatorList;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;


/**
 * This is BeanPostProcessor for adding populators into existing populatorList or any parent in spring declaration.
 * <p>
 * It process all instances of {@link ModifyPopulatorList} beans and applies modification stored in those beans to given
 * objects. If for some reasons (lazyInit) {@link ModifyPopulatorList} is not instanciated then this post processor will
 * not get triggered which may lead to some nasty bugs.
 * <p>
 * Forcing this BeanPostProcessor to load all {@link ModifyPopulatorList} instances during its initialization (via
 * dependency injection or by extracting them form ApplicationContext) will lead to change in order in which beans are
 * intialized. Especially some beans will be instanciated before other BeanPostProcessors which may highly affect
 * stability of the system, so its a bad idea.
 */
public class ModifyPopulatorListBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware
{
	private static final Logger LOG = Logger.getLogger(ModifyPopulatorListBeanPostProcessor.class);

	private ConfigurableListableBeanFactory beanFactory;
	private ChildBeanPostProcessorStrategy strategy;

	@Override
	public void setBeanFactory(final BeanFactory beanFactory) throws BeansException
	{
		if (!(beanFactory instanceof ConfigurableListableBeanFactory))
		{
			throw new IllegalStateException(
					"ModifyPopulatorListBeanPostProcessor doesn't work with a BeanFactory which does not implement ConfigurableListableBeanFactory: "
							+ beanFactory.getClass());
		}
		this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
	}

	@PostConstruct
	public void initialize()
	{
		strategy = new ChildBeanPostProcessorStrategy(beanFactory);
	}

	@Override
	public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException
	{
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException
	{
		if (bean instanceof ModifyPopulatorList)
		{
			LOG.debug("Processing " + beanName);
			process((ModifyPopulatorList) bean);
		}
		return bean;
	}

	private void process(final ModifyPopulatorList bean)
	{
		strategy.process(PopulatorList.class, () -> bean.getList(), target -> processOnTarget(bean, target));
	}

	private void processOnTarget(final ModifyPopulatorList bean, final PopulatorList target)
	{
		target.getPopulators().remove(bean.getRemove());
		target.getPopulators().add(bean.getAdd());
	}
}
