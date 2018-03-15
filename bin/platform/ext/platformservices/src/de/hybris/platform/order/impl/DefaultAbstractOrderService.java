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
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.order.AbstractOrderEntryService;
import de.hybris.platform.order.AbstractOrderEntryTypeService;
import de.hybris.platform.order.AbstractOrderService;
import de.hybris.platform.order.daos.OrderDao;
import de.hybris.platform.order.strategies.ordercloning.CloneAbstractOrderStrategy;
import de.hybris.platform.order.strategies.saving.SaveAbstractOrderStrategy;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link AbstractOrderService}. Implements its contract on the {@link AbstractOrderModel}
 * type level. Any order related services should extend it (i.e. {@link DefaultOrderService}, {@link DefaultCartService}
 * , or a dedicated service for a custom order type).
 */
public abstract class DefaultAbstractOrderService<O extends AbstractOrderModel, E extends AbstractOrderEntryModel> extends
		AbstractBusinessService implements AbstractOrderService<O, E>
{

	private static final Logger LOG = Logger.getLogger(DefaultAbstractOrderService.class);
	private static final int APPEND_AS_LAST = -1;

	private AbstractOrderEntryService<E> abstractOrderEntryService;

	private AbstractOrderEntryTypeService abstractOrderEntryTypeService;

	private CloneAbstractOrderStrategy cloneAbstractOrderStrategy;

	private SaveAbstractOrderStrategy<O> saveAbstractOrderStrategy;


	private OrderDao orderDao;

	@Override
	public E addNewEntry(final O order, final ProductModel product, final long qty, final UnitModel unit)
	{
		return addNewEntry(order, product, qty, unit, APPEND_AS_LAST, true);
	}

	@Override
	public E addNewEntry(final O order, final ProductModel product, final long qty, final UnitModel unit, final int number,
			final boolean addToPresent)
	{
		validateParameterNotNullStandardMessage("order", order);
		return (E) addNewEntry(abstractOrderEntryTypeService.getAbstractOrderEntryType(order), order, product, qty, unit, number,
				addToPresent);
	}


	@Override
	public AbstractOrderEntryModel addNewEntry(final ComposedTypeModel entryType, final O order, final ProductModel product,
			final long qty, final UnitModel unit, final int number, final boolean addToPresent)
	{
		validateParameterNotNullStandardMessage("entryType", entryType);
		validateParameterNotNullStandardMessage("product", product);
		validateParameterNotNullStandardMessage("order", order);
		if (qty <= 0)
		{
			throw new IllegalArgumentException("Quantity must be a positive non-zero value");
		}
		if (number < APPEND_AS_LAST)
		{
			throw new IllegalArgumentException("Number must be greater or equal -1");
		}
		UnitModel usedUnit = unit;
		if (usedUnit == null)
		{
			LOG.debug("No unit passed, trying to get product unit");
			usedUnit = product.getUnit();
			validateParameterNotNullStandardMessage("usedUnit", usedUnit);
		}

		AbstractOrderEntryModel ret = null;
		// search for present entries for this product if needed
		if (addToPresent)
		{
			for (final E e : getEntriesForProduct(order, product))
			{
				// Ensure that order entry is not a 'give away', and has same units
				if (Boolean.FALSE.equals(e.getGiveAway()) && usedUnit.equals(e.getUnit()))
				{
					e.setQuantity(Long.valueOf(e.getQuantity().longValue() + qty));
					ret = e;
					break;
				}
			}
		}

		if (ret == null)
		{
			ret = abstractOrderEntryService.createEntry(entryType, order);
			ret.setQuantity(Long.valueOf(qty));
			ret.setProduct(product);
			ret.setUnit(usedUnit);
			addEntryAtPosition(order, ret, number);
		}
		order.setCalculated(Boolean.FALSE);
		return ret;
	}

	protected int addEntryAtPosition(final AbstractOrderModel order, final AbstractOrderEntryModel entry, final int requested)
	{
		int ret = requested;
		final List<AbstractOrderEntryModel> all = order.getEntries();
		/*
		 * just append ?
		 */
		final int lastIndex = all.isEmpty() ? 0 : all.size() - 1;
		final int lastIndexEntryNumberValue = all.isEmpty() ? 0 : (all.get(lastIndex)).getEntryNumber().intValue();

		if (requested < 0)
		{

			ret = all.isEmpty() ? 0 : lastIndexEntryNumberValue + 1;
		}
		/*
		 * need shuffling other entries
		 */
		else
		{
			boolean foundEntryWithNumber = false;
			for (int i = 0, s = all.size(); i < s; i++) //NOPMD
			{
				final AbstractOrderEntryModel currentEntry = all.get(i);
				final int enr = currentEntry.getEntryNumber().intValue();
				// other entry got this number -> we need to shift numbers now
				if (foundEntryWithNumber)
				{
					currentEntry.setEntryNumber(Integer.valueOf(enr + 1));
				}
				// found it now?
				else if (enr == requested)
				{
					foundEntryWithNumber = true;
					currentEntry.setEntryNumber(Integer.valueOf(currentEntry.getEntryNumber().intValue() + 1));
				}
				// no other entry got this number -> dont need to shift other entry numbers
				else if (enr > requested)
				{
					break;
				}
			}
		}
		entry.setEntryNumber(Integer.valueOf(ret));

		final List<AbstractOrderEntryModel> newEntries = new ArrayList<AbstractOrderEntryModel>(all);
		newEntries.add(entry);
		Collections.sort(newEntries, new Comparator<AbstractOrderEntryModel>()
		{
			@Override
			public int compare(final AbstractOrderEntryModel order1, final AbstractOrderEntryModel order2)
			{
				return order1.getEntryNumber().compareTo(order2.getEntryNumber());
			}
		});

		order.setEntries(newEntries);
		return ret;
	}

	@Override
	public E getEntryForNumber(final O order, final int number)
	{
		validateParameterNotNullStandardMessage("order", order);
		if (number < 0)
		{
			throw new IllegalArgumentException("number must be greater than 0");
		}
		//TODO needed - refactor dao
		final List<AbstractOrderEntryModel> entries = getOrderDao().findEntriesByNumber(getEntryTypeCode(order), order, number);
		ServicesUtil.validateIfSingleResult(entries, "Cannot find entry at position " + number, "More than one entry at position "
				+ number);
		return (E) entries.get(0);
	}

	@Override
	public List<E> getEntriesForNumber(final O order, final int start, final int end)
	{
		validateParameterNotNullStandardMessage("order", order);
		if (start < 0)
		{
			throw new IllegalArgumentException("First entry number must be greater than 0");
		}
		if (start > end)
		{
			throw new IllegalArgumentException("Wrong range boundaries. Start must be less than or equal to end.");
		}

		//TODO fixme
		final List<E> entries = (List<E>) getOrderDao().findEntriesByNumber(getEntryTypeCode(order), order, start, end);
		if (entries.isEmpty())
		{
			throw new UnknownIdentifierException("Cannot find entries between position " + start + " and " + end);
		}
		return entries;
	}

	@Override
	public List<E> getEntriesForProduct(final O order, final ProductModel product)
	{
		validateParameterNotNullStandardMessage("order", order);
		validateParameterNotNullStandardMessage("product", product);
		//TODO fixme
		return (List<E>) orderDao.findEntriesByProduct(getEntryTypeCode(order), order, product);
	}

	@Override
	public O saveOrder(final O order)
	{
		return saveAbstractOrderStrategy.saveOrder(order);
	}

	@Override
	public void addGlobalDiscountValue(final O order, final DiscountValue discountValue)
	{
		validateParameterNotNullStandardMessage("discountValue", discountValue);
		addAllGlobalDiscountValues(order, Collections.singletonList(discountValue));
	}

	@Override
	public void addTotalTaxValue(final O order, final TaxValue taxValue)
	{
		validateParameterNotNullStandardMessage("taxValue", taxValue);
		addAllTotalTaxValues(order, Collections.singletonList(taxValue));
	}

	@Override
	public void addAllGlobalDiscountValues(final O order, final List<DiscountValue> discountValues)
	{
		validateParameterNotNullStandardMessage("order", order);
		validateParameterNotNullStandardMessage("discountValues", discountValues);
		final List<DiscountValue> currGlobalDiscountVal = order.getGlobalDiscountValues();
		final List<DiscountValue> discounts = currGlobalDiscountVal == null ? new ArrayList<DiscountValue>()
				: new ArrayList<DiscountValue>(currGlobalDiscountVal);
		discounts.addAll(discountValues);
		order.setGlobalDiscountValues(discounts);
	}

	@Override
	public void addAllTotalTaxValues(final O order, final List<TaxValue> taxValues)
	{
		validateParameterNotNullStandardMessage("order", order);
		validateParameterNotNullStandardMessage("taxValues", taxValues);
		final Collection<TaxValue> currentTotalTaxValues = order.getTotalTaxValues();
		final Collection<TaxValue> taxes = currentTotalTaxValues == null ? new ArrayList<TaxValue>() : new ArrayList<TaxValue>(
				currentTotalTaxValues);
		taxes.addAll(taxValues);
		order.setTotalTaxValues(taxes);
	}

	@Override
	public void removeGlobalDiscountValue(final O order, final DiscountValue discountValue)
	{
		validateParameterNotNullStandardMessage("order", order);
		validateParameterNotNullStandardMessage("discountValue", discountValue);
		final List<DiscountValue> discounts = new ArrayList<DiscountValue>(order.getGlobalDiscountValues());
		if (discounts.contains(discountValue))
		{
			discounts.remove(discountValue);
			order.setGlobalDiscountValues(discounts);
		}
		else
		{
			LOG.warn("Discount [" + discountValue.getCode() + "] not present in the order's global discounts");
		}
	}

	@Override
	public void removeTotalTaxValue(final O order, final TaxValue taxValue)
	{
		validateParameterNotNullStandardMessage("order", order);
		validateParameterNotNullStandardMessage("taxValue", taxValue);
		final Collection<TaxValue> taxes = new ArrayList<TaxValue>(order.getTotalTaxValues());
		if (taxes.contains(taxValue))
		{
			taxes.remove(taxValue);
			order.setTotalTaxValues(taxes);
		}
		else
		{
			LOG.warn("Tax [" + taxValue.getCode() + "] not present in the order's taxes");
		}
	}


	//TODO needed?
	protected String getEntryTypeCode(final AbstractOrderModel order)
	{
		return abstractOrderEntryTypeService.getAbstractOrderEntryType(order).getCode();
	}

	protected AbstractOrderEntryModel getCollidingEntry(final int requested, final O order)

	{
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		if (entries != null)
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				if (entry.getEntryNumber().intValue() == requested)
				{
					return entry;
				}
			}
		}
		return null;
	}


	// Injection Setters
	@Required
	public void setOrderDao(final OrderDao orderDao)
	{
		this.orderDao = orderDao;
	}


	@Required
	public void setAbstractOrderEntryTypeService(final AbstractOrderEntryTypeService abstractOrderEntryTypeService)
	{
		this.abstractOrderEntryTypeService = abstractOrderEntryTypeService;
	}

	@Required
	public void setSaveAbstractOrderStrategy(final SaveAbstractOrderStrategy<O> saveAbstractOrderStrategy)
	{
		this.saveAbstractOrderStrategy = saveAbstractOrderStrategy;
	}

	@Required
	public void setCloneAbstractOrderStrategy(final CloneAbstractOrderStrategy cloneAbstractOrderStrategy)
	{
		this.cloneAbstractOrderStrategy = cloneAbstractOrderStrategy;
	}

	@Required
	public void setAbstractOrderEntryService(final AbstractOrderEntryService<E> abstractOrderEntryService)
	{
		this.abstractOrderEntryService = abstractOrderEntryService;
	}


	// Injection Getters
	protected CloneAbstractOrderStrategy getCloneAbstractOrderStrategy()
	{
		return cloneAbstractOrderStrategy;
	}

	protected AbstractOrderEntryTypeService getAbstractOrderEntryTypeService()
	{
		return abstractOrderEntryTypeService;
	}

	protected SaveAbstractOrderStrategy<O> getSaveAbstractOrderStrategy()
	{
		return saveAbstractOrderStrategy;
	}

	protected AbstractOrderEntryService<E> getAbstractOrderEntryService()
	{
		return abstractOrderEntryService;
	}

	protected OrderDao getOrderDao()
	{
		return orderDao;
	}

	@Override
	public DiscountValue getGlobalDiscountValue(final O order, final DiscountValue discountValue)
	{
		validateParameterNotNullStandardMessage("order", order);
		validateParameterNotNullStandardMessage("discountValue", discountValue);
		final List<DiscountValue> discounts = new ArrayList<DiscountValue>(order.getGlobalDiscountValues());
		for (final DiscountValue discount : discounts)
		{
			if (discount.equalsIgnoreAppliedValue(discountValue))
			{
				return discount;
			}
		}
		return null;
	}
}
