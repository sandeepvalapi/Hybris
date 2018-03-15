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
package de.hybris.platform.spring.ctx;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;


/**
 *
 */
public class TestRegistrableSingletonBean implements InitializingBean, BeanNameAware
{

	private static final Logger LOG = Logger.getLogger(TestRegistrableSingletonBean.class.getName());

	private String beanId;

	private Map creationCalls;

	@Required
	public void setCreationCalls(final Map registeredCalls)
	{
		LOG.info("set calls" + this + ", " + registeredCalls);
		this.creationCalls = registeredCalls;
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		LOG.info("after props " + this + ", " + beanId);
		creationCalls.put(beanId, this);
	}

	@Override
	public void setBeanName(final String name)
	{
		beanId = name;
	}

}
