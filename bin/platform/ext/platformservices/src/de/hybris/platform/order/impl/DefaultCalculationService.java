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

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.calculation.FindDeliveryCostStrategy;
import de.hybris.platform.order.strategies.calculation.FindDiscountValuesStrategy;
import de.hybris.platform.order.strategies.calculation.FindPaymentCostStrategy;
import de.hybris.platform.order.strategies.calculation.FindPriceStrategy;
import de.hybris.platform.order.strategies.calculation.FindTaxValuesStrategy;
import de.hybris.platform.order.strategies.calculation.OrderRequiresCalculationStrategy;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.TaxValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 *
 */
public class DefaultCalculationService extends AbstractBusinessService implements CalculationService
{

	private static final Logger LOG = Logger.getLogger(DefaultCalculationService.class);
	private List<FindTaxValuesStrategy> findTaxesStrategies;
	private List<FindDiscountValuesStrategy> findDiscountsStrategies;
	private FindPriceStrategy findPriceStrategy;
	private FindDeliveryCostStrategy findDeliveryCostStrategy;
	private FindPaymentCostStrategy findPaymentCostStrategy;
	private OrderRequiresCalculationStrategy orderRequiresCalculationStrategy;

	private CommonI18NService commonI18NService;

	//see PLA-11851
	private boolean taxFreeEntrySupport = false;

	@Override
	public void calculate(final AbstractOrderModel order) throws CalculationException
	{
		if (orderRequiresCalculationStrategy.requiresCalculation(order))
		{
			// -----------------------------
			// first calc all entries
			calculateEntries(order, false);
			// -----------------------------
			// reset own values
			final Map taxValueMap = resetAllValues(order);
			// -----------------------------
			// now calculate all totals
			calculateTotals(order, false, taxValueMap);
			// notify manual discouns - needed?
			//notifyDiscountsAboutCalculation();
		}
	}

	@Override
	public boolean requiresCalculation(final AbstractOrderModel order)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("order", order);
		return orderRequiresCalculationStrategy.requiresCalculation(order);
	}

	protected void setCalculatedStatus(final AbstractOrderModel order)
	{
		order.setCalculated(Boolean.TRUE);
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		if (entries != null)
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				entry.setCalculated(Boolean.TRUE);
			}
		}
	}

	protected void setCalculatedStatus(final AbstractOrderEntryModel entry)
	{
		entry.setCalculated(Boolean.TRUE);
	}


	@Override
	public void calculate(final AbstractOrderModel order, final Date date) throws CalculationException
	{
		final Date old = order.getDate();
		order.setDate(date);
		try
		{
			calculate(order);
		}
		finally
		{
			order.setDate(old);
			getModelService().save(order);
		}
	}


	@Override
	public void calculateTotals(final AbstractOrderModel order, final boolean recalculate) throws CalculationException
	{
		calculateTotals(order, recalculate, calculateSubtotal(order, recalculate));
	}

	/**
	 * calculates all totals. this does not trigger price, tax and discount calculation but takes all currently set
	 * price, tax and discount values as base. this method requires the correct subtotal to be set before and the correct
	 * tax value map.
	 *
	 * @param recalculate
	 *           if false calculation is done only if the calculated flag is not set
	 * @param taxValueMap
	 *           the map { tax value -> Double( sum of all entry totals for this tax ) } obtainable via
	 *           {@link #calculateSubtotal(AbstractOrderModel, boolean)}
	 * @throws CalculationException
	 */
	protected void calculateTotals(final AbstractOrderModel order, final boolean recalculate,
			final Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap) throws CalculationException
	{
		if (recalculate || orderRequiresCalculationStrategy.requiresCalculation(order))
		{
			final CurrencyModel curr = order.getCurrency();
			final int digits = curr.getDigits().intValue();
			// subtotal
			final double subtotal = order.getSubtotal().doubleValue();
			// discounts

			final double totalDiscounts = calculateDiscountValues(order, recalculate);
			final double roundedTotalDiscounts = commonI18NService.roundCurrency(totalDiscounts, digits);
			order.setTotalDiscounts(Double.valueOf(roundedTotalDiscounts));
			// set total
			final double total = subtotal + order.getPaymentCost().doubleValue() + order.getDeliveryCost().doubleValue()
					- roundedTotalDiscounts;
			final double totalRounded = commonI18NService.roundCurrency(total, digits);
			order.setTotalPrice(Double.valueOf(totalRounded));
			// taxes
			final double totalTaxes = calculateTotalTaxValues(//
					order, recalculate, //
					digits, //
					getTaxCorrectionFactor(taxValueMap, subtotal, total, order), //
					taxValueMap);//
			final double totalRoundedTaxes = commonI18NService.roundCurrency(totalTaxes, digits);
			order.setTotalTax(Double.valueOf(totalRoundedTaxes));
			setCalculatedStatus(order);
			saveOrder(order);
		}
	}
	
	protected void saveOrder(AbstractOrderModel order)
	{
		// since order *and* entries are changed we need to save them all
		List<AbstractOrderEntryModel> entries = order.getEntries();
		if( CollectionUtils.isNotEmpty(entries))
		{
			final Collection<ItemModel> all = new ArrayList<>(entries);
			all.add(order);
			getModelService().saveAll(all);
		}
		else
		{
			getModelService().save(order);
		}
	}

	protected double getTaxCorrectionFactor(final Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap, final double subtotal,
			final double total, final AbstractOrderModel order) throws CalculationException
	{
		// default: adjust taxes relative to total-subtotal ratio
		double factor = subtotal != 0.0 ? ( total / subtotal ) : 1.0;

		if (mustHandleTaxFreeEntries(taxValueMap, subtotal, order))
		{
			final double taxFreeSubTotal = getTaxFreeSubTotal(order);

			final double taxedTotal = total - taxFreeSubTotal;
			final double taxedSubTotal = subtotal - taxFreeSubTotal;

			// illegal state: taxed subtotal is <= 0 -> cannot calculate with that
			if (taxedSubTotal <= 0)
			{
				throw new CalculationException("illegal taxed subtotal " + taxedSubTotal + ", must be > 0");
			}
			// illegal state: taxed total is <= 0 -> no sense in having negative taxes (factor would become negative!)
			if (taxedTotal <= 0)
			{
				throw new CalculationException("illegal taxed total " + taxedTotal + ", must be > 0");
			}
			factor = taxedSubTotal != 0.0 ? ( taxedTotal / taxedSubTotal ) : 1.0;
		}
		return factor;
	}

	//see PLA-11851: we must take special actions in case some entries DO NOT HAVE TAXES on them
	protected boolean mustHandleTaxFreeEntries(final Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap, final double subtotal,
			final AbstractOrderModel order)
	{
		return MapUtils.isNotEmpty(taxValueMap) // got taxes at all
				&& taxFreeEntrySupport // mode is enabled
				&& !isAllEntriesTaxed(taxValueMap, subtotal, order); // check sums whether some entries are contributing to tax map
	}

	/**
	 * Calculates the sub total of all order entries with NO tax values.
	 */
	protected double getTaxFreeSubTotal(final AbstractOrderModel order)
	{
		double sum = 0;
		for (final AbstractOrderEntryModel e : order.getEntries())
		{
			if (CollectionUtils.isEmpty(e.getTaxValues()))
			{
				sum += e.getTotalPrice().doubleValue();
			}
		}
		return sum;
	}

	protected boolean isAllEntriesTaxed(final Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap, final double subtotal,
			final AbstractOrderModel order)
	{
		double sum = 0.0;
		final Set<Set<TaxValue>> consumedTaxGroups = new HashSet<Set<TaxValue>>();
		for (final Map.Entry<TaxValue, Map<Set<TaxValue>, Double>> taxEntry : taxValueMap.entrySet())
		{
			for (final Map.Entry<Set<TaxValue>, Double> taxGroupEntry : taxEntry.getValue().entrySet())
			{
				if (consumedTaxGroups.add(taxGroupEntry.getKey())) // avoid duplicate occurrences of the same tax group
				{
					sum += taxGroupEntry.getValue().doubleValue();
				}
			}
		}
		// delta ( 2 digits ) == 10 ^-3 == 0.001
		final double allowedDelta = Math.pow(10, -1 * (order.getCurrency().getDigits().intValue() + 1));
		return Math.abs(subtotal - sum) <= allowedDelta;
	}

	@Override
	public void recalculate(final AbstractOrderModel order) throws CalculationException
	{
		// -----------------------------
		// first force calc entries
		calculateEntries(order, true);
		// -----------------------------
		// reset own values
		final Map taxValueMap = resetAllValues(order);
		// -----------------------------
		// now recalculate totals
		calculateTotals(order, true, taxValueMap);
		// notify discounts -- needed?
		//			notifyDiscountsAboutCalculation();

	}


	//phase 1 : delegate to Jalo
	@Override
	public void recalculate(final AbstractOrderModel order, final Date date) throws CalculationException
	{
		final AbstractOrder orderItem = getModelService().getSource(order);
		try
		{
			orderItem.recalculate(date);
		}
		catch (final JaloPriceFactoryException e)
		{
			throw new CalculationException(e);
		}
		refreshOrder(order);
	}

	public void calculateEntries(final AbstractOrderModel order, final boolean forceRecalculate) throws CalculationException
	{
		double subtotal = 0.0;
		for (final AbstractOrderEntryModel e : order.getEntries())
		{
			recalculateOrderEntryIfNeeded(e, forceRecalculate);
			subtotal += e.getTotalPrice().doubleValue();
		}
		order.setTotalPrice(Double.valueOf(subtotal));

	}

	@Override
	public void calculateTotals(final AbstractOrderEntryModel entry, final boolean recalculate)
	{
		if (recalculate || orderRequiresCalculationStrategy.requiresCalculation(entry))
		{
			final AbstractOrderModel order = entry.getOrder();
			final CurrencyModel curr = order.getCurrency();
			final int digits = curr.getDigits().intValue();
			final double totalPriceWithoutDiscount = commonI18NService.roundCurrency(entry.getBasePrice().doubleValue()
					* entry.getQuantity().longValue(), digits);
			final double quantity = entry.getQuantity().doubleValue();
			/*
			 * apply discounts (will be rounded each) convert absolute discount values in case their currency doesn't match
			 * the order currency
			 */
			//YTODO : use CalculatinService methods to apply discounts
			final List appliedDiscounts = DiscountValue.apply(quantity, totalPriceWithoutDiscount, digits,
					convertDiscountValues(order, entry.getDiscountValues()), curr.getIsocode());
			entry.setDiscountValues(appliedDiscounts);
			double totalPrice = totalPriceWithoutDiscount;
			for (final Iterator it = appliedDiscounts.iterator(); it.hasNext();)
			{
				totalPrice -= ((DiscountValue) it.next()).getAppliedValue();
			}
			// set total price
			entry.setTotalPrice(Double.valueOf(totalPrice));
			// apply tax values too
			//YTODO : use CalculatinService methods to apply taxes
			calculateTotalTaxValues(entry);
			setCalculatedStatus(entry);
			getModelService().save(entry);

		}
	}

	protected void calculateTotalTaxValues(final AbstractOrderEntryModel entry)
	{
		final AbstractOrderModel order = entry.getOrder();
		final double quantity = entry.getQuantity().doubleValue();
		final double totalPrice = entry.getTotalPrice().doubleValue();
		final CurrencyModel curr = order.getCurrency();
		final int digits = curr.getDigits().intValue();

		entry.setTaxValues(TaxValue.apply(quantity, totalPrice, digits, entry.getTaxValues(), order.getNet().booleanValue(),
				curr.getIsocode()));
	}

	protected void recalculateOrderEntryIfNeeded(final AbstractOrderEntryModel entry, final boolean forceRecalculation)
			throws CalculationException
	{
		if (forceRecalculation || orderRequiresCalculationStrategy.requiresCalculation(entry))
		{
			resetAllValues(entry);
			calculateTotals(entry, true);
		}
	}

	@Override
	public void recalculate(final AbstractOrderEntryModel entry) throws CalculationException
	{
		recalculateOrderEntryIfNeeded(entry, true);
	}

	//YTODO - should not be necessary
	protected void refreshOrder(final AbstractOrderModel order)
	{
		getModelService().refresh(order);
		for (final AbstractOrderEntryModel entry : order.getEntries())
		{
			getModelService().refresh(entry);
		}
	}

	protected void resetAllValues(final AbstractOrderEntryModel entry) throws CalculationException
	{
		// taxes
		final Collection<TaxValue> entryTaxes = findTaxValues(entry);
		entry.setTaxValues(entryTaxes);
		final PriceValue pv = findBasePrice(entry);
		final AbstractOrderModel order = entry.getOrder();
		final PriceValue basePrice = convertPriceIfNecessary(pv, order.getNet().booleanValue(), order.getCurrency(), entryTaxes);
		entry.setBasePrice(Double.valueOf(basePrice.getValue()));
		final List<DiscountValue> entryDiscounts = findDiscountValues(entry);
		entry.setDiscountValues(entryDiscounts);
	}

	protected Map resetAllValues(final AbstractOrderModel order) throws CalculationException
	{
		// -----------------------------
		// set subtotal and get tax value map
		final Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap = calculateSubtotal(order, false);
		/*
		 * filter just relative tax values - payment and delivery prices might require conversion using taxes -> absolute
		 * taxes do not apply here TODO: ask someone for absolute taxes and how they apply to delivery cost etc. - this
		 * implementation might be wrong now
		 */
		final Collection<TaxValue> relativeTaxValues = new LinkedList<TaxValue>();
		for (final Map.Entry<TaxValue, ?> e : taxValueMap.entrySet())
		{
			final TaxValue taxValue = e.getKey();
			if (!taxValue.isAbsolute())
			{
				relativeTaxValues.add(taxValue);
			}
		}

		//PLA-10914
		final boolean setAdditionalCostsBeforeDiscounts = Config.getBoolean(
				"ordercalculation.reset.additionalcosts.before.discounts", true);
		if (setAdditionalCostsBeforeDiscounts)
		{
			resetAdditionalCosts(order, relativeTaxValues);
		}
		// -----------------------------
		// set discount values ( not applied yet ) - dont needed in model domain (?)
		//removeAllGlobalDiscountValues();
		order.setGlobalDiscountValues(findGlobalDiscounts(order));
		// -----------------------------
		// set delivery costs - convert if net or currency is different

		if (!setAdditionalCostsBeforeDiscounts)
		{
			resetAdditionalCosts(order, relativeTaxValues);
		}

		return taxValueMap;

	}


	protected void resetAdditionalCosts(final AbstractOrderModel order, final Collection<TaxValue> relativeTaxValues)
	{
		final PriceValue deliCost = findDeliveryCostStrategy.getDeliveryCost(order);
		double deliveryCostValue = 0.0;
		if (deliCost != null)
		{
			deliveryCostValue = convertPriceIfNecessary(deliCost, order.getNet().booleanValue(), order.getCurrency(),
					relativeTaxValues).getValue();
		}
		order.setDeliveryCost(Double.valueOf(deliveryCostValue));
		// -----------------------------
		// set payment cost - convert if net or currency is different
		final PriceValue payCost = findPaymentCostStrategy.getPaymentCost(order);
		double paymentCostValue = 0.0;
		if (payCost != null)
		{
			paymentCostValue = convertPriceIfNecessary(payCost, order.getNet().booleanValue(), order.getCurrency(),
					relativeTaxValues).getValue();
		}
		order.setPaymentCost(Double.valueOf(paymentCostValue));
	}


	/**
	 * converts a PriceValue object into a double matching the target currency and net/gross - state if necessary. this
	 * is the case when the given price value has a different net/gross flag or different currency.
	 *
	 * @param pv
	 *           the base price to convert
	 * @param toNet
	 *           the target net/gross state
	 * @param toCurrency
	 *           the target currency
	 * @param taxValues
	 *           the collection of tax values which apply to this price
	 *
	 * @return a new PriceValue containing the converted price or pv in case no conversion was necessary
	 */
	//YTODO: refactor to some helper class
	public PriceValue convertPriceIfNecessary(final PriceValue pv, final boolean toNet, final CurrencyModel toCurrency,
			final Collection taxValues)
	{
		// net - gross - conversion
		double convertedPrice = pv.getValue();
		if (pv.isNet() != toNet)
		{
			convertedPrice = pv.getOtherPrice(taxValues).getValue();
			convertedPrice = commonI18NService.roundCurrency(convertedPrice, toCurrency.getDigits().intValue());
		}
		// currency conversion
		final String iso = pv.getCurrencyIso();
		if (iso != null && !iso.equals(toCurrency.getIsocode()))
		{
			try
			{
				final CurrencyModel basePriceCurrency = commonI18NService.getCurrency(iso);
				convertedPrice = commonI18NService.convertAndRoundCurrency(basePriceCurrency.getConversion().doubleValue(),
						toCurrency.getConversion().doubleValue(), toCurrency.getDigits().intValue(), convertedPrice);
			}
			catch (final UnknownIdentifierException e)
			{
				LOG.warn("Cannot convert from currency '" + iso + "' to currency '" + toCurrency.getIsocode() + "' since '" + iso
						+ "' doesn't exist any more - ignored");
			}
		}
		return new PriceValue(toCurrency.getIsocode(), convertedPrice, toNet);
	}

	protected List convertDiscountValues(final AbstractOrderModel order, final List dvs)
	{
		if (dvs == null)
		{
			return null;
		}
		if (dvs.isEmpty())
		{
			return dvs;
		}
		//

		final CurrencyModel curr = order.getCurrency();
		final String iso = curr.getIsocode();
		final List tmp = new ArrayList(dvs);
		/*
		 * convert absolute discount values to order currency is needed
		 */
		final Map<String, CurrencyModel> currencyMap = new HashMap<String, CurrencyModel>(); // just don't search twice for an isocode
		for (int i = 0; i < tmp.size(); i++)
		{
			final DiscountValue discountValue = (DiscountValue) tmp.get(i);
			if (discountValue.isAbsolute() && !iso.equals(discountValue.getCurrencyIsoCode()))
			{
				// get currency
				CurrencyModel dCurr = currencyMap.get(discountValue.getCurrencyIsoCode());
				if (dCurr == null)
				{
					currencyMap.put(discountValue.getCurrencyIsoCode(),
							dCurr = commonI18NService.getCurrency(discountValue.getCurrencyIsoCode()));
				}
				// replace old value in temp list
				tmp.set(
						i,
						new DiscountValue(discountValue.getCode(),
								commonI18NService.convertAndRoundCurrency(dCurr.getConversion().doubleValue(), curr.getConversion()
										.doubleValue(), curr.getDigits().intValue(), discountValue.getValue()), true, iso));
			}
		}
		return tmp;
	}

	protected Map<TaxValue, Map<Set<TaxValue>, Double>> calculateSubtotal(final AbstractOrderModel order, final boolean recalculate)
	{
		if (recalculate || orderRequiresCalculationStrategy.requiresCalculation(order))
		{
			double subtotal = 0.0;
			// entry grouping via map { tax code -> Double }
			final List<AbstractOrderEntryModel> entries = order.getEntries();
			final Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap = new LinkedHashMap<TaxValue, Map<Set<TaxValue>, Double>>(
					entries.size() * 2);

			for (final AbstractOrderEntryModel entry : entries)
			{
				calculateTotals(entry, recalculate);
				final double entryTotal = entry.getTotalPrice().doubleValue();
				subtotal += entryTotal;
				// use un-applied version of tax values!!!
				final Collection<TaxValue> allTaxValues = entry.getTaxValues();
				final Set<TaxValue> relativeTaxGroupKey = getUnappliedRelativeTaxValues(allTaxValues);
				for (final TaxValue taxValue : allTaxValues)
				{
					if (taxValue.isAbsolute())
					{
						addAbsoluteEntryTaxValue(entry.getQuantity().longValue(), taxValue.unapply(), taxValueMap);
					}
					else
					{
						addRelativeEntryTaxValue(entryTotal, taxValue.unapply(), relativeTaxGroupKey, taxValueMap);
					}
				}
			}
			// store subtotal
			subtotal = commonI18NService.roundCurrency(subtotal, order.getCurrency().getDigits().intValue());
			order.setSubtotal(Double.valueOf(subtotal));
			return taxValueMap;
		}
		return Collections.EMPTY_MAP;
	}

	/**
	 * Returns the total discount for this abstract order.
	 *
	 * @param recalculate
	 *           <code>true</code> forces a recalculation
	 * @return totalDiscounts
	 */
	protected double calculateDiscountValues(final AbstractOrderModel order, final boolean recalculate)
	{
		if (recalculate || orderRequiresCalculationStrategy.requiresCalculation(order))
		{
			final List<DiscountValue> discountValues = order.getGlobalDiscountValues();
			if (discountValues != null && !discountValues.isEmpty())
			{
				// clean discount value list -- do we still need it?
				//				removeAllGlobalDiscountValues();
				//
				final CurrencyModel curr = order.getCurrency();
				final String iso = curr.getIsocode();

				final int digits = curr.getDigits().intValue();
				final double discountablePrice = order.getSubtotal().doubleValue()
						+ (order.isDiscountsIncludeDeliveryCost() ? order.getDeliveryCost().doubleValue() : 0.0)
						+ (order.isDiscountsIncludePaymentCost() ? order.getPaymentCost().doubleValue() : 0.0);
				/*
				 * apply discounts to this order's total
				 */
				final List appliedDiscounts = DiscountValue.apply(1.0, discountablePrice, digits,
						convertDiscountValues(order, discountValues), iso);
				// store discount values
				order.setGlobalDiscountValues(appliedDiscounts);
				return DiscountValue.sumAppliedValues(appliedDiscounts);
			}
			return 0.0;
		}
		else
		{
			return DiscountValue.sumAppliedValues(order.getGlobalDiscountValues());
		}
	}

	/**
	 * @param recalculate
	 * @param digits
	 * @param taxAdjustmentFactor
	 * @param taxValueMap
	 * @return total taxes
	 */
	protected double calculateTotalTaxValues(final AbstractOrderModel order, final boolean recalculate, final int digits,
			final double taxAdjustmentFactor, final Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap)
	{
		if (recalculate || orderRequiresCalculationStrategy.requiresCalculation(order))
		{
			final CurrencyModel curr = order.getCurrency();
			final String iso = curr.getIsocode();
			//Do we still need it?
			//removeAllTotalTaxValues();
			final boolean net = order.getNet().booleanValue();
			double totalTaxes = 0.0;
			// compute absolute taxes if necessary
			if (MapUtils.isNotEmpty(taxValueMap))
			{
				// adjust total taxes if additional costs or discounts are present
				//	create tax values which contains applied values too
				final Collection orderTaxValues = new ArrayList<TaxValue>(taxValueMap.size());
				for (final Map.Entry<TaxValue, Map<Set<TaxValue>, Double>> taxValueEntry : taxValueMap.entrySet())
				{
					// e.g. VAT_FULL 19%
					final TaxValue unappliedTaxValue = taxValueEntry.getKey();
					// e.g. { (VAT_FULL 19%, CUSTOM 2%) -> 10EUR, (VAT_FULL) -> 20EUR }
					// or, in case of absolute taxes one single entry
					// { (ABS_1 4,50EUR) -> 2 (pieces) }
					final Map<Set<TaxValue>, Double> taxGroups = taxValueEntry.getValue();

					final TaxValue appliedTaxValue;

					if (unappliedTaxValue.isAbsolute())
					{
						// absolute tax entries ALWAYS map to a single-entry map -> we'll use a shortcut here:
						final double quantitySum = taxGroups.entrySet().iterator().next().getValue().doubleValue();
						appliedTaxValue = calculateAbsoluteTotalTaxValue(curr, iso, digits, net, unappliedTaxValue, quantitySum);
					}
					else if (net)
					{
						appliedTaxValue = applyNetMixedRate(unappliedTaxValue, taxGroups, digits, taxAdjustmentFactor);
					}
					else
					{
						appliedTaxValue = applyGrossMixedRate(unappliedTaxValue, taxGroups, digits, taxAdjustmentFactor);
					}
					totalTaxes += appliedTaxValue.getAppliedValue();
					orderTaxValues.add(appliedTaxValue);
				}
				order.setTotalTaxValues(orderTaxValues);
			}
			return totalTaxes;
		}
		else
		{
			return order.getTotalTax().doubleValue();
		}
	}

	protected void addRelativeEntryTaxValue(final double entryTotal, final TaxValue taxValue,
			final Set<TaxValue> relativeEntryTaxValues, final Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap)
	{
		Double relativeTaxTotalSum = null;
		// A. is tax value already registered ?
		Map<Set<TaxValue>, Double> taxTotalsMap = taxValueMap.get(taxValue);
		if (taxTotalsMap != null) // tax value exists
		{
			// A.1 is set of tax un-applied values already registered by set of all relative tax values ?
			relativeTaxTotalSum = taxTotalsMap.get(relativeEntryTaxValues);
		}
		// B tax value did not exist before
		else
		{
			taxTotalsMap = new LinkedHashMap<Set<TaxValue>, Double>();
			taxValueMap.put(taxValue, taxTotalsMap);
		}
		taxTotalsMap.put(relativeEntryTaxValues,
				Double.valueOf((relativeTaxTotalSum != null ? relativeTaxTotalSum.doubleValue() : 0d) + entryTotal));

	}

	protected void addAbsoluteEntryTaxValue(final long entryQuantity, final TaxValue taxValue,
			final Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap)
	{
		Map<Set<TaxValue>, Double> taxGroupMap = taxValueMap.get(taxValue);
		Double quantitySum = null;
		final Set<TaxValue> absoluteTaxGroupKey = Collections.singleton(taxValue);
		if (taxGroupMap == null)
		{
			taxGroupMap = new LinkedHashMap<Set<TaxValue>, Double>(4);
			taxValueMap.put(taxValue, taxGroupMap);
		}
		else
		{
			quantitySum = taxGroupMap.get(absoluteTaxGroupKey);
		}
		taxGroupMap.put(absoluteTaxGroupKey, Double.valueOf((quantitySum != null ? quantitySum.doubleValue() : 0) + entryQuantity));

	}

	protected Set<TaxValue> getUnappliedRelativeTaxValues(final Collection<TaxValue> allTaxValues)
	{
		if (CollectionUtils.isNotEmpty(allTaxValues))
		{
			final Set<TaxValue> ret = new LinkedHashSet<TaxValue>(allTaxValues.size());
			for (final TaxValue appliedTv : allTaxValues)
			{
				if (!appliedTv.isAbsolute())
				{
					ret.add(appliedTv.unapply());
				}
			}
			return ret;
		}
		else
		{
			return Collections.EMPTY_SET;
		}
	}

	protected TaxValue calculateAbsoluteTotalTaxValue(final CurrencyModel curr, final String currencyIso, final int digits,
			final boolean net, TaxValue taxValue, final double cumulatedEntryQuantities)
	{
		final String taxValueIsoCode = taxValue.getCurrencyIsoCode();
		// convert absolute tax values into order currency if necessary

		if (taxValueIsoCode != null && !currencyIso.equalsIgnoreCase(taxValueIsoCode))
		{
			final CurrencyModel taxCurrency = commonI18NService.getCurrency(taxValueIsoCode);
			final double taxConvertedValue = commonI18NService.convertAndRoundCurrency(taxCurrency.getConversion().doubleValue(),
					curr.getConversion().doubleValue(), digits, taxValue.getValue());
			taxValue = new TaxValue(taxValue.getCode(), taxConvertedValue, true, 0, currencyIso);
		}
		return taxValue.apply(cumulatedEntryQuantities, 0.0, digits, net, currencyIso);
	}

	protected TaxValue applyGrossMixedRate(final TaxValue unappliedTaxValue, final Map<Set<TaxValue>, Double> taxGroups,
			final int digits, final double taxAdjustmentFactor)
	{
		if (unappliedTaxValue.isAbsolute())
		{
			throw new IllegalStateException("AbstractOrder.applyGrossMixedRate(..) cannot be called for absolute tax value!");
		}
		final double singleTaxRate = unappliedTaxValue.getValue();
		double appliedTaxValueNotRounded = 0.0;
		for (final Map.Entry<Set<TaxValue>, Double> taxGroupEntry : taxGroups.entrySet())
		{
			final double groupTaxesRate = TaxValue.sumRelativeTaxValues(taxGroupEntry.getKey());
			final double taxGroupPrice = taxGroupEntry.getValue().doubleValue();

			appliedTaxValueNotRounded += (taxGroupPrice * singleTaxRate) / (100.0 + groupTaxesRate);
		}

		//adjust taxes according to global discounts / costs
		appliedTaxValueNotRounded = appliedTaxValueNotRounded * taxAdjustmentFactor;

		return new TaxValue(//
				unappliedTaxValue.getCode(), //
				unappliedTaxValue.getValue(), //
				false,//
				// always round (even if digits are 0) since relative taxes result in unwanted precision !!!
				commonI18NService.roundCurrency(appliedTaxValueNotRounded, Math.max(digits, 0)), //
				null //
		);
	}

	protected TaxValue applyNetMixedRate(final TaxValue unappliedTaxValue, final Map<Set<TaxValue>, Double> taxGroups,
			final int digits, final double taxAdjustmentFactor)
	{
		if (unappliedTaxValue.isAbsolute())
		{
			throw new IllegalStateException("cannot applyGrossMixedRate(..) cannot be called on absolute tax value!");
		}

		// In NET mode we don't care for tax groups since they're only needed to calculated *incldued* taxes!
		// Here we just sum up all group totals...
		double entriesTotalPrice = 0.0;
		for (final Map.Entry<Set<TaxValue>, Double> taxGroupEntry : taxGroups.entrySet())
		{
			entriesTotalPrice += taxGroupEntry.getValue().doubleValue();
		}
		// and apply them in one go:
		return unappliedTaxValue.apply(1.0, entriesTotalPrice * taxAdjustmentFactor, digits, true, null);
	}


	protected Collection<TaxValue> findTaxValues(final AbstractOrderEntryModel entry) throws CalculationException
	{
		if (findTaxesStrategies.isEmpty())
		{
			LOG.warn("No strategies for finding tax values could be found!");
			return Collections.<TaxValue> emptyList();
		}
		else
		{
			final List<TaxValue> result = new ArrayList<TaxValue>();
			for (final FindTaxValuesStrategy findStrategy : findTaxesStrategies)
			{
				result.addAll(findStrategy.findTaxValues(entry));
			}
			return result;
		}
	}

	protected List<DiscountValue> findDiscountValues(final AbstractOrderEntryModel entry) throws CalculationException
	{
		if (findDiscountsStrategies.isEmpty())
		{
			LOG.warn("No strategies for finding discount values could be found!");
			return Collections.<DiscountValue> emptyList();
		}
		else
		{
			final List<DiscountValue> result = new ArrayList<DiscountValue>();
			for (final FindDiscountValuesStrategy findStrategy : findDiscountsStrategies)
			{
				result.addAll(findStrategy.findDiscountValues(entry));
			}
			return result;
		}
	}

	protected List<DiscountValue> findGlobalDiscounts(final AbstractOrderModel order) throws CalculationException
	{
		if (findDiscountsStrategies.isEmpty())
		{
			LOG.warn("No strategies for finding discount values could be found!");
			return Collections.<DiscountValue> emptyList();
		}
		else
		{
			final List<DiscountValue> result = new ArrayList<DiscountValue>();
			for (final FindDiscountValuesStrategy findStrategy : findDiscountsStrategies)
			{
				result.addAll(findStrategy.findDiscountValues(order));
			}
			return result;
		}
	}


	protected PriceValue findBasePrice(final AbstractOrderEntryModel entry) throws CalculationException
	{
		return findPriceStrategy.findBasePrice(entry);
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}


	@Required
	public void setFindTaxesStrategies(final List<FindTaxValuesStrategy> findTaxesStrategies)
	{
		this.findTaxesStrategies = findTaxesStrategies;
	}


	@Required
	public void setFindDiscountsStrategies(final List<FindDiscountValuesStrategy> findDiscountsStrategies)
	{
		this.findDiscountsStrategies = findDiscountsStrategies;
	}


	@Required
	public void setFindPriceStrategy(final FindPriceStrategy findPriceStrategy)
	{
		this.findPriceStrategy = findPriceStrategy;
	}


	@Required
	public void setFindDeliveryCostStrategy(final FindDeliveryCostStrategy findDeliveryCostStrategy)
	{
		this.findDeliveryCostStrategy = findDeliveryCostStrategy;
	}


	@Required
	public void setFindPaymentCostStrategy(final FindPaymentCostStrategy findPaymentCostStrategy)
	{
		this.findPaymentCostStrategy = findPaymentCostStrategy;
	}

	@Required
	public void setOrderRequiresCalculationStrategy(final OrderRequiresCalculationStrategy orderRequiresCalculationStrategy)
	{
		this.orderRequiresCalculationStrategy = orderRequiresCalculationStrategy;
	}

	public void setTaxFreeEntrySupport(final boolean taxFreeEntrySupport)
	{
		this.taxFreeEntrySupport = taxFreeEntrySupport;
	}

	/**
	 * @deprecated since ages - use{@link #isTaxFreeEntrySupport()}
	 */
	@Deprecated
	public boolean getTaxFreeEntrySupport() //NOPMD
	{
		return isTaxFreeEntrySupport();
	}

	public boolean isTaxFreeEntrySupport()
	{
		return this.taxFreeEntrySupport;
	}

}
