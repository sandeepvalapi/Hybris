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
package de.hybris.platform.order.interceptors;

import de.hybris.platform.core.enums.QuoteState;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.util.localization.Localization;

import org.springframework.beans.factory.annotation.Required;


/**
 * Sets default values for the following attributes of new {@link QuoteModel} instances:
 * <ul>
 * <li>{@link QuoteModel#STATE},</li>
 * <li>{@link QuoteModel#VERSION}</li>
 * </ul>
 */
public class DefaultQuotePrepareInterceptor implements PrepareInterceptor<QuoteModel>
{
	private Integer initialVersion;
	private QuoteState initialState;
	private KeyGenerator keyGenerator;

	@Override
	public void onPrepare(final QuoteModel quote, final InterceptorContext ctx) throws InterceptorException
	{
		if (ctx.isNew(quote))
		{
			if (quote.getCode() == null)
			{
				quote.setCode((String) getKeyGenerator().generate());
			}
			if (quote.getVersion() == null)
			{
				quote.setVersion(getInitialVersion());
			}
			if (quote.getState() == null)
			{
				quote.setState(getInitialState());
			}
			if (quote.getName() == null)
			{
				quote.setName(String.format("%s %s", getLocalizedTypeName(), quote.getCode()));
			}
		}
	}

	protected String getLocalizedTypeName()
	{
		return Localization.getLocalizedString("type.quote.name");
	}

	protected Integer getInitialVersion()
	{
		return initialVersion;
	}

	@Required
	public void setInitialVersion(final Integer initialVersion)
	{
		this.initialVersion = initialVersion;
	}

	protected QuoteState getInitialState()
	{
		return initialState;
	}

	@Required
	public void setInitialState(final QuoteState initialState)
	{
		this.initialState = initialState;
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

}
