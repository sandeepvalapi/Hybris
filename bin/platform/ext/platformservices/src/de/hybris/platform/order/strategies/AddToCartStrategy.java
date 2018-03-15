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
package de.hybris.platform.order.strategies;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.product.ProductService;


/**
 * The product add to cart strategy.
 * 
 * @spring.bean addToCartStrategy
 * @deprecated since ages
 */
@Deprecated
public interface AddToCartStrategy
{
	/**
	 * Adds to the (existing) {@link CartModel} the (existing) {@link ProductModel} in the given {@link UnitModel} and
	 * with the given <code>quantity</code>. If in the cart already an entry with the given product and given unit exists
	 * the given <code>quantity</code> is added to the the quantity of this cart entry.
	 * 
	 * @param cart
	 *           the cart, must exist
	 * @param product
	 *           the product which is added to the cart
	 * @param quantity
	 *           the quantity of the product
	 * @param unit
	 *           if <code>null</code> {@link ProductService#getOrderableUnit(ProductModel)} is used to determine the unit
	 * @throws InvalidCartException
	 *            if the <code>product</code> is a base product OR the quantity is less 1 or no usable unit was found
	 *            (only when given <code>unit</code> is also <code>null</code>)
	 */
	void addToCart(CartModel cart, ProductModel product, long quantity, UnitModel unit) throws InvalidCartException;
}
