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
package de.hybris.platform.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.order.AbstractOrderEntryService;
import de.hybris.platform.order.AbstractOrderEntryTypeService;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link AbstractOrderEntryService}. Provides implementation on
 * {@link AbstractOrderEntryModel} type level.
 */
public class DefaultAbstractOrderEntryService<T extends AbstractOrderEntryModel> extends AbstractBusinessService implements
		AbstractOrderEntryService<T>
{

	private static final Logger LOG = Logger.getLogger(DefaultAbstractOrderEntryService.class);

	//Resources
	protected AbstractOrderEntryTypeService abstractOrderEntryTypeService;

	@Override
	public T createEntry(final AbstractOrderModel order)
	{
		validateParameterNotNullStandardMessage("order", order);
		final ComposedTypeModel entryComposedType = abstractOrderEntryTypeService.getAbstractOrderEntryType(order);
		return (T) createEntry(entryComposedType, order);
	}

	@Override
	public AbstractOrderEntryModel createEntry(final ComposedTypeModel entryType, final AbstractOrderModel order)
	{
		validateParameterNotNullStandardMessage("entryType", entryType);
		validateParameterNotNullStandardMessage("order", order);
		final Class orderEntryClass = abstractOrderEntryTypeService.getAbstractOrderEntryClassForType(entryType);
		final AbstractOrderEntryModel entryModel = getModelService().create(orderEntryClass);
		entryModel.setOrder(order);
		return entryModel;
	}


	@Override
	public void addDiscountValue(final T entry, final DiscountValue discountValue)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("discountValue", discountValue);
		addAllDiscountValues(entry, Collections.singletonList(discountValue));
	}

	@Override
	public void addTaxValue(final T entry, final TaxValue taxValue)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("taxValue", taxValue);
		addAllTaxValues(entry, Collections.singletonList(taxValue));
	}

	@Override
	public void addAllDiscountValues(final T entry, final List<DiscountValue> discountValues)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("entry", entry);
		ServicesUtil.validateParameterNotNullStandardMessage("discountValues", discountValues);
		final List<DiscountValue> currentDiscountValues = entry.getDiscountValues();
		final List<DiscountValue> entryDiscounts = currentDiscountValues == null ? new ArrayList<DiscountValue>()
				: new ArrayList<DiscountValue>(currentDiscountValues);
		entryDiscounts.addAll(discountValues);
		entry.setDiscountValues(entryDiscounts);
	}

	@Override
	public void addAllTaxValues(final T entry, final List<TaxValue> taxValues)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("entry", entry);
		ServicesUtil.validateParameterNotNullStandardMessage("taxValues", taxValues);
		final Collection<TaxValue> currentTaxValues = entry.getTaxValues();
		final List<TaxValue> entryTaxes = currentTaxValues == null ? new ArrayList<TaxValue>() : new ArrayList<TaxValue>(
				currentTaxValues);
		entryTaxes.addAll(taxValues);
		entry.setTaxValues(entryTaxes);
	}

	@Override
	public void removeDiscountValue(final T entry, final DiscountValue discountValue)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("entry", entry);
		ServicesUtil.validateParameterNotNullStandardMessage("discountValue", discountValue);
		final List<DiscountValue> entryDiscounts = new ArrayList<DiscountValue>(entry.getDiscountValues());
		if (!entryDiscounts.contains(discountValue))
		{
			LOG.warn("Entry no " + entry.getEntryNumber() + " from order [" + entry.getOrder().getCode()
					+ "] doesn't contain discount value (" + discountValue.getCode() + ").. skipping!");
			return;
		}
		entryDiscounts.remove(discountValue);
		entry.setDiscountValues(entryDiscounts);
	}

	@Override
	public void removeTaxValue(final T entry, final TaxValue taxValue)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("entry", entry);
		ServicesUtil.validateParameterNotNullStandardMessage("taxValue", taxValue);
		final List<TaxValue> entryTaxes = new ArrayList<TaxValue>(entry.getTaxValues());
		if (!entryTaxes.contains(taxValue))
		{
			LOG.warn("Entry no " + entry.getEntryNumber() + " from order [" + entry.getOrder().getCode()
					+ "] doesn't contain tax value (" + taxValue.getCode() + ").. skipping!");
			return;
		}
		entryTaxes.remove(taxValue);
		entry.setTaxValues(entryTaxes);

	}

	@Required
	public void setAbstractOrderEntryTypeService(final AbstractOrderEntryTypeService abstractOrderEntryTypeService)
	{
		this.abstractOrderEntryTypeService = abstractOrderEntryTypeService;
	}

	@Override
	public DiscountValue getGlobalDiscountValue(final T entry, final DiscountValue discountValue)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("entry", entry);
		ServicesUtil.validateParameterNotNullStandardMessage("discountValue", discountValue);
		final List<DiscountValue> entryDiscounts = new ArrayList<DiscountValue>(entry.getDiscountValues());
		for (final DiscountValue discount : entryDiscounts)
		{
			if (discount.equalsIgnoreAppliedValue(discountValue))
			{
				return discount;
			}
		}
		LOG.warn("Entry no " + entry.getEntryNumber() + " from order [" + entry.getOrder().getCode()
				+ "] doesn't contain discount value (" + discountValue.getCode() + ").. skipping!");
		return null;

	}

}
