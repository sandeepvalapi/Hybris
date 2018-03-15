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

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * For a new {@link AbstractOrderModel} the interceptor adjusts the {@link AbstractOrderModel#CODE} according to the
 * configured {@link KeyGenerator}. It also sets a default value of {@link AbstractOrderModel#CALCULATED} flag.
 */
public class DefaultAbstractOrderPrepareInterceptor extends AbstractAttributesModificationAwareInterceptor implements
		PrepareInterceptor
{

	private KeyGenerator keyGenerator;
	private Collection<String> attributesForOrderRecalculation;
	private Collection<String> attributesForOrderEntriesRecalculation;
	private static final Logger LOG = Logger.getLogger(DefaultAbstractOrderPrepareInterceptor.class);

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof AbstractOrderModel)
		{
			final AbstractOrderModel abstractOrder = (AbstractOrderModel) model;
			if (abstractOrder.getCode() == null)
			{
				abstractOrder.setCode((String) keyGenerator.generate());
				abstractOrder.setCalculated(Boolean.FALSE);
			}
			if( isCalculatedFlagUnchangedOrFalse(abstractOrder,ctx) )
			{
   			//does order and orderEntries need to be un-calculated?
   			if (isOneOfAttributesModified(abstractOrder, getAttributesForOrderEntriesRecalculation(), ctx) )
   			{
   				abstractOrder.setCalculated(Boolean.FALSE);
   				if (abstractOrder.getEntries() != null)
   				{
   					for (final AbstractOrderEntryModel entry : abstractOrder.getEntries())
   					{
   						entry.setCalculated(Boolean.FALSE);
   					}
   				}
   			}
   			//..or perhaps only order..?
   			else if (isOneOfAttributesModified(abstractOrder, getAttributesForOrderRecalculation(), ctx))
   			{
   				abstractOrder.setCalculated(Boolean.FALSE);
   			}
			}

			if (abstractOrder.getGlobalDiscountValues() == null)
			{
				abstractOrder.setGlobalDiscountValues(Collections.EMPTY_LIST);
			}

			if (abstractOrder.getTotalTaxValues() == null)
			{
				abstractOrder.setTotalTaxValues(Collections.EMPTY_LIST);
			}
		}
	}

	/**
	 * returns qualifiers of order attributes, which, when modified, the order and orderEntries need to be recalculated.
	 */
	protected Collection<String> getAttributesForOrderEntriesRecalculation()
	{
		if (attributesForOrderEntriesRecalculation != null)
		{
			return attributesForOrderEntriesRecalculation;
		}
		else
		{
			return Arrays.asList(AbstractOrderModel.DATE, AbstractOrderModel.USER, AbstractOrderModel.CURRENCY,
					AbstractOrderModel.NET);
		}
	}

	/**
	 * returns qualifiers of order attributes, which, when modified, the order needs to be recalculated.
	 */
	protected Collection<String> getAttributesForOrderRecalculation()
	{
		if (attributesForOrderRecalculation != null)
		{
			return attributesForOrderRecalculation;
		}
		else
		{
			return Arrays.asList(AbstractOrderModel.DELIVERYMODE, AbstractOrderModel.DELIVERYCOST, AbstractOrderModel.PAYMENTMODE,
					AbstractOrderModel.PAYMENTCOST, AbstractOrderModel.TOTALTAXVALUES, AbstractOrderModel.DISCOUNTS,
					AbstractOrderModel.DISCOUNTSINCLUDEDELIVERYCOST, AbstractOrderModel.DISCOUNTSINCLUDEPAYMENTCOST,
					AbstractOrderModel.DELIVERYADDRESS, AbstractOrderModel.PAYMENTADDRESS);
		}
	}

	/**
	 * As all of the order calculation is moved to the SL we can do the calculation and set the calculated flag in a
	 * single database update. Therefore if the order has the calculated flag to to true we trust that the order is
	 * already calculated and therefore let the flag be persisted
	 */
	protected boolean isCalculatedFlagUnchangedOrFalse(final AbstractOrderModel order, final InterceptorContext ctx)
	{
		return !ctx.isModified(order, AbstractOrderModel.CALCULATED) || !Boolean.TRUE.equals(order.getCalculated());
	}

	@Required
	public void setKeyGenerator(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	public void setAttributesForOrderRecalculation(final Collection<String> attributesForOrderRecalculation)
	{
		this.attributesForOrderRecalculation = attributesForOrderRecalculation;
	}

	public void setAttributesForOrderEntriesRecalculation(final Collection<String> attributesForOrderEntriesRecalculation)
	{
		this.attributesForOrderEntriesRecalculation = attributesForOrderEntriesRecalculation;
	}



	@Override
	public Logger getLogger()
	{
		return LOG;
	}

}
