/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.assistedservicestorefront.util;

import java.lang.reflect.Method;
import java.util.Map;

import de.hybris.platform.assistedserviceservices.exception.AssistedServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;


/**
 * The class is a wrapper around subscriptionFacade bean and delegates calls to it by reflection if subscriptionFacade
 * bean is present in Spring context. Or does nothing if subscriptionFacade is not found.
 */
public class SubscriptionFacadeReflectionWrapper
{
	private static final String SUBSCRIPTION_FACADE_CLASS_NAME = "de.hybris.platform.subscriptionfacades.SubscriptionFacade";
	private static final String SUBSCRIPTION_FACADE_BEAN_NAME = "subscriptionFacade";
	private static final String SUBSCRIPTION_FACADE_UPDATEPROFILE_METHOD = "updateProfile";

	private static final Logger LOG = Logger.getLogger(SubscriptionFacadeReflectionWrapper.class);

	@Autowired
	private ApplicationContext applicationContext;

	public void updateProfile(final Map<String, String> paramMap) throws AssistedServiceException
	{
		try
		{
			Object subscriptionFacadeObject = null;

			subscriptionFacadeObject = applicationContext.getBean(SUBSCRIPTION_FACADE_BEAN_NAME);
			if (subscriptionFacadeObject != null)
			{
				final Class<?> c = Class.forName(SUBSCRIPTION_FACADE_CLASS_NAME);
				final Method updateProfileMethod = c.getDeclaredMethod(SUBSCRIPTION_FACADE_UPDATEPROFILE_METHOD, Map.class);
				updateProfileMethod.invoke(subscriptionFacadeObject, paramMap);
			}
		}
		catch (final BeansException e)
		{
			LOG.info(String.format(
					"Bean with name '%s' not defined. Calling of it methods is impossible. Calling of '%s' is ignored.",
					SUBSCRIPTION_FACADE_BEAN_NAME, SUBSCRIPTION_FACADE_UPDATEPROFILE_METHOD));
			LOG.debug(e.getMessage(), e);
		}
		catch (final Exception ex)
		{
			LOG.error("Subscription profile updating failed", ex);
			throw new AssistedServiceException("Subscription profile updating failed", ex);
		}
	}
}
