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
package de.hybris.platform.product.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.order.price.PriceFactory;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.product.PriceCriteria;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Default implementation of the {@link PriceService}.
 */
public class DefaultPriceService extends AbstractBusinessService implements PriceService, ApplicationContextAware
{
	private ApplicationContext ctx;
	private TimeService timeService;
	private UserService userService;
	
	@Override
	public List<PriceInformation> getPriceInformationsForProduct(final ProductModel product)
	{
		return getPriceInformations(PriceCriteria.DefaultPriceCriteria.forProduct(product));
	}

	@Override
	public List<PriceInformation> getPriceInformations(final PriceCriteria priceCriteria)
	{
		final Product productItem = (Product) getModelService().getSource(priceCriteria.getProduct());
		Date forDate = priceCriteria.getDate();
		if (forDate == null)
		{
			forDate = timeService.getCurrentTime();
		}
		Boolean net = priceCriteria.isNet();
		if (net == null)
		{
			final UserModel currentUser = userService.getCurrentUser();
			final User userItem = getModelService().getSource(currentUser);
			// TODO: Re-Implement with PriceFactoryService
			final PriceFactory pricefactory = OrderManager.getInstance().getPriceFactory();
			net = Boolean.valueOf(pricefactory.isNetUser(userItem));
		}
		try
		{
			return productItem.getPriceInformations(forDate, net.booleanValue());
		}
		catch (final JaloPriceFactoryException e)
		{
			throw new SystemException(e.getMessage(), e);
		}
	}

	// actually this would be @Required but we don't know how many partners did 
	// override it. so we're looking it up in afterPropertiesSet() in case it's missing
	public void setTimeService(TimeService timeService)
	{
		this.timeService = timeService;
	}

	// actually this would be @Required but we don't know how many partners did 
	// override it. so we're looking it up in afterPropertiesSet() in case it's missing
	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException
	{
		this.ctx = ctx;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception
	{
		super.afterPropertiesSet();
		
		if( timeService == null )
		{
			this.timeService = ctx.getBean("timeService", TimeService.class);
		}
		if( userService == null )
		{
			this.userService = ctx.getBean("userService", UserService.class);
		}
	}
}
