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
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.order.impl.DefaultAbstractOrderService;
import de.hybris.platform.order.strategies.ordercloning.CloneAbstractOrderStrategy;
import de.hybris.platform.order.strategies.saving.SaveAbstractOrderStrategy;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import java.util.List;


/**
 * Abstract interface for services dedicated for handling subtypes of {@link AbstractOrderModel}. </br> Extend this
 * service with a typed interface (like {@link OrderService}, or {@link CartService}) to obtain a set of it's contract
 * methods for your type (like {@link OrderModel}, or {@link CartModel} correspondingly). </br> Default implementation -
 * {@link DefaultAbstractOrderService} - provides shared implementation on the {@link AbstractOrderModel}.
 */
public interface AbstractOrderService<O extends AbstractOrderModel, E extends AbstractOrderEntryModel>
{
	/**
	 * Adds a new entry to the given order. The entry type is recognized basing on {@link AbstractOrderEntryTypeService}
	 * and the spring configuration behind it. When a product already exists, the corresponding entry quantity is
	 * increased. If a null Unit is specified, a unit according to given product will be chosen. The new entry is neither
	 * saved nor calculated.
	 *
	 *
	 * @param order
	 *           - target order
	 *
	 * @param product
	 *           -product to add, must not be null
	 * @param qty
	 *           - quantity
	 * @param unit
	 *           - must not be null
	 *
	 * @return {@link AbstractOrderEntryModel} - newly created order entry
	 *
	 * @throws IllegalArgumentException
	 *            if either order or product is null or quantity is negative
	 * @see AbstractOrderEntryTypeService#getAbstractOrderEntryType(AbstractOrderModel)
	 */
	E addNewEntry(O order, final ProductModel product, final long qty, final UnitModel unit);

	/**
	 * Adds a new entry to the given order on the required entry number. The entry type is recognized basing on
	 * {@link AbstractOrderEntryTypeService} and the spring configuration behind it. The new entry is neither saved nor
	 * calculated. If your new entry has caused entries shuffling you may need to call
	 * {@link #saveOrder(AbstractOrderModel)} method in order to persist the changes.
	 *
	 * @param order
	 *           - target order
	 *
	 * @param product
	 *           -product to add, must not be null
	 * @param qty
	 *           - quantity
	 * @param unit
	 *           - must not be null
	 * @param number
	 *           - entry number of the new entry in the order. Entries are indexed starting from 0. Set number to -1 if
	 *           you want to append the entry as the last one. You can request any non-negative position.
	 * @param addToPresent
	 *           - if true an existing entry with matching product <b>and</b> unit will get its quantity increased;
	 *           otherwise a new entry is created
	 * @return {@link AbstractOrderEntryModel} - newly created order entry
	 *
	 * @throws IllegalArgumentException
	 *            if either order or product is null or quantity is negative.
	 *
	 * @see AbstractOrderEntryTypeService#getAbstractOrderEntryType(AbstractOrderModel)
	 */
	E addNewEntry(final O order, final ProductModel product, final long qty, final UnitModel unit, final int number,
			final boolean addToPresent);

	/**
	 * Adds a new entry of the given {@link ComposedTypeModel} to the given order. The new entry is neither saved nor
	 * calculated. If your new entry has caused entries shuffling you may need to call
	 * {@link #saveOrder(AbstractOrderModel)} method in order to persist the changes.
	 *
	 * @param entryType
	 *           - the requested sub-type AbstractOrderEntry
	 * @param order
	 *           - target order
	 *
	 * @param product
	 *           -product to add, must not be null
	 * @param qty
	 *           - quantity
	 * @param unit
	 *           - must not be null
	 * @param number
	 *           - entry number of the new entry in the order. Entries are indexed starting from 0. Set number to -1 if
	 *           you want to append the entry as the last one. You can request any non-negative position.
	 * @param addToPresent
	 *           - if true an existing entry with matching product <b>and</b> unit will get its quantity increased;
	 *           otherwise a new entry is created
	 * @return {@link AbstractOrderEntryModel} - newly created order entry
	 *
	 * @throws IllegalArgumentException
	 *            if either entryType or order or product is null or quantity is negative.
	 *
	 */
	AbstractOrderEntryModel addNewEntry(final ComposedTypeModel entryType, final O order, final ProductModel product,
			final long qty, final UnitModel unit, final int number, final boolean addToPresent);

	/**
	 * Creates new order from given original and change its type and type for entry. Delegates to the specific
	 * {@link CloneAbstractOrderStrategy} injected strategy. Resulting order remains not persisted.
	 *
	 * @param orderType
	 *           type of result order (OrderModel will be chosen if null is passed)
	 * @param entryType
	 *           type of entry (if null will use service to get proper type)
	 * @param original
	 *           original order
	 * @param code
	 *           code for new order
	 * @return not persisted order model instance.
	 */
	O clone(final ComposedTypeModel orderType, final ComposedTypeModel entryType, final AbstractOrderModel original,
			final String code);

	/**
	 * Returns an order entry with the given {@link AbstractOrderEntryModel#ENTRYNUMBER}. The method delegates to data
	 * access object and checks the actual persisted order in the data base, not the order state represented by the
	 * model.
	 *
	 * @param order
	 * @param number
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no entry with the requested number exists
	 * @throws IllegalArgumentException
	 *            if either order is null or number is negative
	 *
	 * @return {@link AbstractOrderEntryModel}
	 */
	E getEntryForNumber(final O order, final int number);

	/**
	 * Returns order entries with the given {@link AbstractOrderEntryModel#ENTRYNUMBER}. The method delegates to data
	 * access object and checks the actual persisted order in the data base, not the order state represented by the
	 * model.
	 *
	 * @param order
	 * @param start
	 *           start of the range
	 * @param end
	 *           end of the range
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no entry from the requested range number exists
	 * @throws IllegalArgumentException
	 *            if either order is null or start is negative or start is greater than end
	 *
	 * @return {@link AbstractOrderEntryModel}
	 */
	List<E> getEntriesForNumber(final O order, final int start, final int end);

	/**
	 * Returns order entries having the target product. In case is no entry contains the requested product, empty list is
	 * returned. The method delegates to data access object and checks the actual persisted order in the data base, not
	 * the order state represented by the model.
	 *
	 * @param order
	 *           - target order
	 * @param product
	 *           - searched product
	 * @throws IllegalArgumentException
	 *            if either order or product is null
	 *
	 * @return matching order entries
	 */
	List<E> getEntriesForProduct(final O order, final ProductModel product);

	/**
	 * <p>
	 * Persist the model and all it's members and entries within one transaction using {@link SaveAbstractOrderStrategy}.
	 * After this method call the order and it's entries are refreshed.
	 * </p>
	 * This method is useful if you have added an {@link AbstractOrderEntryModel} on the already occupied entryNumber
	 * position, which caused order entries shuffling. In other, trivial cases, the regular call to
	 * {@link ModelService#save(Object)} is sufficient. Please mind that the concrete typed service must allow entries
	 * shuffling (like {@link CartService} does). Tying to put OrderEntryModel on an occupied entryNumber using
	 * {@link OrderService} would throw an exception in the first place.
	 *
	 * The method call is delegated to the spring configurable {@link SaveAbstractOrderStrategy}.
	 *
	 * @param order
	 *           - non persisted {@link OrderModel} that needs to be persisted.
	 * @throws IllegalArgumentException
	 *            if order is null.
	 * @return persisted {@link OrderModel}
	 */
	O saveOrder(O order);

	/**
	 * Convenience method to add a {@link TaxValue} to the given order. <b> please note that the order's tax values are
	 * overwritten each time {@link CalculationService#calculate(AbstractOrderModel)},
	 * {@link CalculationService#calculate(AbstractOrderModel, java.util.Date)},
	 * {@link CalculationService#recalculate(AbstractOrderModel)},
	 * {@link CalculationService#recalculate(AbstractOrderModel, java.util.Date)} is called.</b> Those calls fetch the
	 * tax information from the price factory and overwrites the previous state. The content of the
	 * {@link AbstractOrderModel#TOTALTAXVALUES} represents all tax values introduced by the order entries. After this
	 * method call, the order model remains not persisted.
	 *
	 * @param taxValue
	 * @param order
	 *           target order
	 * @throws IllegalArgumentException
	 *            if either <code>order</code> or <code>taxValue</code> is null.
	 */
	void addTotalTaxValue(O order, TaxValue taxValue);

	/**
	 * Adds a collection of tax values into given order. <b> please note that this field is overwritten each time
	 * {@link CalculationService#calculate(AbstractOrderModel)},
	 * {@link CalculationService#calculate(AbstractOrderModel, java.util.Date)},
	 * {@link CalculationService#recalculate(AbstractOrderModel)},
	 * {@link CalculationService#recalculate(AbstractOrderModel, java.util.Date)} is called.</b> Those calls fetch the
	 * tax information from the price factory and overwrites the previous state. The content of the
	 * {@link AbstractOrderModel#TOTALTAXVALUES} represents all tax values introduced by the order entries. After this
	 * method call, the order model remains not persisted.
	 *
	 * @param order
	 *           target order
	 * @param taxValues
	 *           tax values to add
	 * @throws IllegalArgumentException
	 *            if either <code>order</code> or <code>taxValues</code> is null.
	 */
	void addAllTotalTaxValues(O order, List<TaxValue> taxValues);

	/**
	 * Removes a tax value from the given order. <b> please note that this field will be overwritten after one of the
	 * following method calls: {@link CalculationService#calculate(AbstractOrderModel)},
	 * {@link CalculationService#calculate(AbstractOrderModel, java.util.Date)},
	 * {@link CalculationService#recalculate(AbstractOrderModel)},
	 * {@link CalculationService#recalculate(AbstractOrderModel, java.util.Date)} is called.</b> Those calls fetch the
	 * tax information from the price factory and overwrites the previous state. The content of the
	 * {@link AbstractOrderModel#TOTALTAXVALUES} represents all tax values introduced by the order entries. After this
	 * method call, the order model remains not persisted.
	 *
	 * @param taxValue
	 * @param order
	 *           target order
	 * @throws IllegalArgumentException
	 *            if either <code>order</code> or <code>taxValue</code> is null.
	 */
	void removeTotalTaxValue(O order, TaxValue taxValue);

	/**
	 * Adds a global discount value to the given order. Such discount will be treated globally for the whole order rather
	 * than for any particular entry. After this method call order remains not persisted. User needs to manually persist
	 * and recalculate the order so that the global discount is reflected in the total price.
	 *
	 * @param discountValue
	 *           discount to add
	 * @param order
	 *           target order
	 * @throws IllegalArgumentException
	 *            if either <code>order</code> or <code>discountValue</code> is null.
	 *
	 */
	void addGlobalDiscountValue(O order, DiscountValue discountValue);

	/**
	 * Adds a collection of global discount values to the given order. Such discounts will be treated globally for the
	 * whole order rather than for any particular entry. After this method call order remains not persisted. User needs
	 * to manually persist and recalculate the order so that the added global discounts are reflected in the total price.
	 *
	 * @param discountValues
	 *           discount values to add
	 * @param order
	 *           target order
	 *
	 * @throws IllegalArgumentException
	 *            if either <code>order</code> or <code>discountValues</code> is null.
	 */
	void addAllGlobalDiscountValues(O order, List<DiscountValue> discountValues);

	/**
	 * Removes a global discount value from this order. After this method call order remains not persisted. User needs to
	 * manually persist and recalculate the order so that the added global discounts are reflected in the total price.
	 *
	 * @param discountValue
	 *           discount to remove
	 * @param order
	 *           target order
	 * @throws IllegalArgumentException
	 *            if either <code>order</code> or <code>discountValue</code> is null.
	 */
	void removeGlobalDiscountValue(O order, DiscountValue discountValue);

	/**
	 * Searches for complete discount value (with calculated applied value) object created using given discountValue.
	 * Returns null when discount value for given parameter can not be found.
	 */
	DiscountValue getGlobalDiscountValue(O order, DiscountValue discountValue);

}
