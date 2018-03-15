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
package de.hybris.platform.order.strategies.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.strategies.AddToCartStrategy;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.variants.jalo.VariantsManager;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link AddToCartStrategy}.
 */
public class DefaultAddToCartStrategy extends AbstractBusinessService implements AddToCartStrategy
{
	private ProductService productService;

	/**
	 * @deprecated since ages
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public void addToCart(final CartModel cart, final ProductModel product, final long quantity, final UnitModel unit)
			throws InvalidCartException
	{
		//TODO: revome product from cart? how?
		//TODO: null checks?
		final Product productItem = getModelService().getSource(product);
		if (VariantsManager.getInstance().isBaseProduct(productItem))
		{
			throw new InvalidCartException("Choose a variant instead of the base product");
		}
		if (quantity < 1)
		{
			throw new InvalidCartException("Quantity must not be less than one");
		}
		UnitModel orderableUnit = unit;
		if (orderableUnit == null)
		{
			try
			{
				orderableUnit = productService.getOrderableUnit(product);
			}
			catch (final ModelNotFoundException e)
			{
				throw new InvalidCartException(e.getMessage(), e);
			}
		}
		final Cart cartItem = getModelService().getSource(cart);
		cartItem.addNewEntry(productItem, quantity, (Unit) getModelService().getSource(orderableUnit), true);
		getModelService().refresh(cart);
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}
}
