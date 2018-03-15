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

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.price.DiscountModel;

import java.util.Collection;


/**
 * Service around the {@link DiscountModel}.
 * <p>
 * There are different kinds of discounts: absolute or relative, and global and non-global. Absolute discounts contain a
 * real price value (price and currency), whereas relative discounts are discounts in percent. Global discounts are
 * applied on order level, which means that they can not be applied to a single entry. Non-global discounts are only
 * applicable on entry - level.
 * <p/>
 * <i> Since all price calculation is done be the currently installed pricefactory refer to it for maybe differing
 * behaviour. </i>
 * 
 * @spring.bean discountService
 */
public interface DiscountService
{

	/**
	 * Gets the {@link DiscountModel} with the specified code.
	 * 
	 * @param code
	 *           the discount code
	 * @return the found {@link DiscountModel} with the specified code
	 */
	DiscountModel getDiscountForCode(String code);

	/**
	 * Gets all {@link DiscountModel}s which match the specified code.
	 * 
	 * @param matchedCode
	 *           an SQL-Like statement as String, such as <b>%discountCode%</b>
	 * @return the <code>Collection</code> of all {@link DiscountModel}s which match the specified code
	 */
	Collection<DiscountModel> getDiscountsForCode(String matchedCode);

	/**
	 * Gets all {@link DiscountModel}s which match the specified currency.
	 * 
	 * @param currency
	 *           target {@link CurrencyModel}
	 * @return the <code>Collection</code> of all {@link DiscountModel}s which matching currency. If no matching
	 *         discounts were found, an empty collection is returned.
	 * 
	 * @throws IllegalArgumentException
	 *            if currency is null
	 * 
	 */
	Collection<DiscountModel> getDiscountsForCurrency(CurrencyModel currency);

}
