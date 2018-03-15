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
package de.hybris.platform.order;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import java.util.List;


/**
 * Generic service that allows creation of order entries of particular type. Concrete implementation should be typed for
 * a particular subtype of {@link AbstractOrderEntryModel}. I.e :</br>
 * <ul>
 * <li> {@link OrderEntryService} should implement the AbstractOrderEntryService for {@link OrderEntryModel},</li>
 * <li>CartEntryService should implement the AbstractOrderEntryService for {@link CartEntryModel},</li>
 * <li>... etc</li>
 * </ul>
 * 
 * The service allows managing taxes and discounts on abstract order entry level.
 */
public interface AbstractOrderEntryService<E extends AbstractOrderEntryModel>
{

	/**
	 * Creates a new instance of order entry for a given abstract order instance. The entry type is chosen to best suit
	 * the order instance. New entry remains not persisted.
	 * 
	 * @see AbstractOrderEntryTypeService#getAbstractOrderEntryType(AbstractOrderModel)
	 * 
	 * @param abstractOrder
	 *           target order
	 * @return new order entry of the suitable runtime type.
	 */
	public E createEntry(final AbstractOrderModel abstractOrder);

	/**
	 * Creates a new instance of abstract order entry of the specific composed type for a given order instance. New entry
	 * remains not persisted.
	 * 
	 * @param abstractOrder
	 *           target order
	 * @param entryType
	 *           create entry of this specific type
	 * @return new abstract order entry of the specific runtime type.
	 */
	public AbstractOrderEntryModel createEntry(final ComposedTypeModel entryType, final AbstractOrderModel abstractOrder);

	/**
	 * Adds a new discount value to the given entry. Entry remains not persisted. By default, after save operation, the
	 * entry will be not calculated. User needs to recalculate order in order to notice the added discount in the total
	 * price.
	 * 
	 * @see CalculationService#calculate(AbstractOrderModel)
	 * @param discountValue
	 *           discount value to add
	 * @param entry
	 *           target {@link AbstractOrderEntryModel}
	 * @throws IllegalArgumentException
	 *            if either <code>order<code> or <code>discountValue<code> is null.
	 */
	void addDiscountValue(E entry, DiscountValue discountValue);


	/**
	 * Adds a collection of discount values into given entry. Entry remains not persisted and not calculated. User needs
	 * to recalculate order in order to notice the added discounts in the total price.
	 * 
	 * @see CalculationService#calculate(AbstractOrderModel)
	 * 
	 * @param discountValues
	 *           discount values to add
	 * @param entry
	 *           target {@link AbstractOrderEntryModel}
	 * @throws IllegalArgumentException
	 *            if either <code>order<code> or <code>discountValues<code> is null.
	 */
	void addAllDiscountValues(E entry, List<DiscountValue> discountValues);

	/**
	 * Removes given discount value from the given entry. Entry remains not persisted and not calculated. User needs to
	 * recalculate order in order to notice the removed discount in the total price.
	 * 
	 * @see CalculationService#calculate(AbstractOrderModel)
	 * 
	 * @param discountValue
	 *           discount value to remove
	 * @param entry
	 *           target {@link AbstractOrderEntryModel}
	 * @throws IllegalArgumentException
	 *            if either <code>order<code> or <code>discountValue<code> is null.
	 */
	void removeDiscountValue(E entry, DiscountValue discountValue);

	/**
	 * Searches for complete discount value (with calculated applied value) object created using given discountValue.
	 * Returns null when discount value for given parameter can not be found.
	 */
	DiscountValue getGlobalDiscountValue(E entry, DiscountValue discountValue);

	/**
	 * Adds a new tax value into given entry. Entry remains not persisted. By default, after save operation, the entry
	 * will be not calculated. User needs to recalculate entry in order to notice the added tax value in the orders total
	 * tax. After recalculation of the order, the new {@link TaxValue} should be present in orders tax values
	 * {@link AbstractOrderModel#TOTALTAXVALUES}.
	 * 
	 * @see CalculationService#calculate(AbstractOrderModel)
	 * 
	 * @param taxValue
	 *           tax value to add
	 * @param entry
	 *           target {@link AbstractOrderEntryModel}
	 * @throws IllegalArgumentException
	 *            if either <code>order<code> or <code>taxValue<code> is null.
	 * 
	 */
	void addTaxValue(E entry, TaxValue taxValue);

	/**
	 * Adds a collection of tax values into given entry. Entry remains not persisted and not calculated. User needs to
	 * recalculate entry in order to notice the added tax values in the orders total tax. After recalculation of the
	 * order, the new {@link TaxValue}s should be present in orders tax values {@link AbstractOrderModel#TOTALTAXVALUES}.
	 * 
	 * @see CalculationService#calculate(AbstractOrderModel)
	 * 
	 * @param taxValues
	 *           tax values to add
	 * @param entry
	 *           target {@link AbstractOrderEntryModel}
	 * @throws IllegalArgumentException
	 *            if either <code>order<code> or <code>taxValues<code> is null.
	 */
	void addAllTaxValues(E entry, List<TaxValue> taxValues);

	/**
	 * Removes given tax value from the given entry. Entry remains not persisted and not calculated. User needs to
	 * recalculate entry in order to see the difference in order's total tax. After recalculation of the order, the
	 * removed {@link TaxValue} should be removed from orders tax values {@link AbstractOrderModel#TOTALTAXVALUES}.
	 * 
	 * @see CalculationService#calculate(AbstractOrderModel)
	 * 
	 * @param taxValue
	 *           tax value to remove
	 * @param entry
	 *           target {@link AbstractOrderEntryModel}
	 * @throws IllegalArgumentException
	 *            if either <code>order<code> or <code>taxValue<code> is null.
	 */
	void removeTaxValue(E entry, TaxValue taxValue);
}
