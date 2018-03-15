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
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.calculation.FindDeliveryCostStrategy;
import de.hybris.platform.order.strategies.calculation.FindDiscountValuesStrategy;
import de.hybris.platform.order.strategies.calculation.FindPaymentCostStrategy;
import de.hybris.platform.order.strategies.calculation.FindPriceStrategy;
import de.hybris.platform.order.strategies.calculation.FindTaxValuesStrategy;
import de.hybris.platform.order.strategies.calculation.OrderRequiresCalculationStrategy;

import java.util.Date;


/**
 * Service allows calculation or recalculation of the order. This includes calculation of all the entries, taxes and
 * discount values. Information about price, taxes and discounts are fetched using dedicated strategies :
 * {@link FindDiscountValuesStrategy}, {@link FindTaxValuesStrategy}, {@link FindPriceStrategy}. Also payment and
 * delivery costs are resolved using strategies {@link FindDeliveryCostStrategy} and {@link FindPaymentCostStrategy}.
 * 
 * Whether order needs to be calculated or not is up to the implementation of {@link OrderRequiresCalculationStrategy}.
 */
public interface CalculationService
{
	/**
	 * Calculates the given order and all its entries unless they're already calculated and the order has not been
	 * modified.
	 * <p>
	 * So this method will fetch prices, taxes and discounts (using spring configurable strategies) for uncalculated or
	 * modified entries only - all other will remain unchanged. If at least one entry has been calculated or the order
	 * itself has been modified the totals will be calculated as well.
	 * <p>
	 * if you merely like to adjust totals while keeping prices, taxes and discounts you should call
	 * {@link #calculateTotals(AbstractOrderModel, boolean)} instead (e.g. if just a quantity has changed or prices have
	 * been imported and cannot be found via price factory ).
	 * 
	 * @param order
	 *           target {@link AbstractOrderModel}
	 * @throws CalculationException
	 *            if a pricefactory error occurred.
	 */
	void calculate(AbstractOrderModel order) throws CalculationException;

	/**
	 * Return {@link Boolean#TRUE} in case if order needs to be calculated. Default implementation use
	 * calculationStrategy to get this information.
	 */
	boolean requiresCalculation(AbstractOrderModel order);

	/**
	 * Calculates the given order and all its entries unless they're already calculated and the order has not been
	 * modified.
	 * <p>
	 * So this method will fetch prices, taxes and discounts (using spring configured strategies) for uncalculated or
	 * modified entries only - all other will remain unchanged. If at least one entry has been calculated or the order
	 * itself has been modified the totals will be calculated as well.
	 * <p>
	 * if you merely like to adjust totals while keeping prices, taxes and discounts you should call
	 * {@link #calculateTotals(AbstractOrderModel, boolean)} instead (e.g. if just a quantity has changed or prices have
	 * been imported and cannot be found via price factory ).
	 * 
	 * @param order
	 *           target {@link AbstractOrderModel}
	 * 
	 * @param date
	 *           assumes a given date to perform calculations for - be careful with that since this method will calculate
	 *           modified or uncalculated entries only, so the given date may not apply to all entries!
	 * @throws CalculationException
	 *            if a pricefactory error occurred
	 */
	void calculate(AbstractOrderModel order, final Date date) throws CalculationException;

	/**
	 * recalculates the whole order and all its entries. this includes finding prices, taxes, discounts, payment and
	 * delivery costs by calling the currently installed price factory.
	 * <p>
	 * if you merely like to adjust totals while keeping prices, taxes and discounts you should call
	 * {@link #calculateTotals(AbstractOrderModel, boolean)} instead (e.g. if just a quantity has changed or prices have
	 * been imported and cannot be found via price factory ).
	 * 
	 * @param order
	 *           target {@link AbstractOrderModel}
	 * @throws CalculationException
	 *            if a pricefactory error occurred.
	 */
	void recalculate(AbstractOrderModel order) throws CalculationException;

	/**
	 * recalculates the whole order and all its entries. this includes finding prices, taxes, discounts, payment and
	 * delivery costs by calling the currently installed dedicated strategies ( {@link FindPriceStrategy},
	 * {@link FindDeliveryCostStrategy}, {@link FindTaxValuesStrategy}, etc... ).
	 * <p>
	 * if you merely like to adjust totals while keeping prices, taxes and discounts you should call
	 * {@link #calculateTotals(AbstractOrderModel, boolean)} instead (e.g. if just a quantity has changed or prices have
	 * been imported and cannot be found via price factory ).
	 * 
	 * @param order
	 *           target {@link AbstractOrderModel}
	 * @param date
	 *           the date to calculate prices for
	 * @throws CalculationException
	 *            if a pricefactory error occurred
	 */
	void recalculate(AbstractOrderModel order, final Date date) throws CalculationException;

	/**
	 * (re)calculates all totals for the given order. This does not trigger price, tax and discount calculation but takes
	 * all currently set price, tax and discount values as base.
	 * 
	 * @param order
	 *           target {@link AbstractOrderModel}
	 * @param recalculate
	 *           forces setting total even if order is marked as calculated
	 */
	void calculateTotals(AbstractOrderModel order, final boolean recalculate) throws CalculationException;


	/**
	 * Recalculates given entries total prices and re-applies tax and discount values. This does not include finding
	 * price, taxes or discount but uses all currently set values.
	 * 
	 * @param entry
	 *           target {@link AbstractOrderEntryModel}
	 * @param recalculate
	 *           forces the recalculation of the abstract order entry.
	 */
	void calculateTotals(AbstractOrderEntryModel entry, final boolean recalculate);

	/**
	 * Calculates this entry even if the entry was calculated before. This includes finding the correct base price, taxes
	 * and discount using the currently installed price factory.
	 * <p>
	 * If prices should be left as currently set but the entry totals have to be consolidated (e.g. when the quantity has
	 * changed) call {@link #calculateTotals(AbstractOrderEntryModel,boolean)} instead !
	 * 
	 * @param entry
	 *           target {@link AbstractOrderEntryModel}
	 * @throws CalculationException
	 *            if a pricefactory error occurred
	 */
	void recalculate(AbstractOrderEntryModel entry) throws CalculationException;
}
