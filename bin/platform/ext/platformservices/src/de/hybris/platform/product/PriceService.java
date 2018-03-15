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
package de.hybris.platform.product;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;

import java.util.List;


/**
 * Service for the product prices of a product.
 *
 * @spring.bean priceService
 */
public interface PriceService
{
	/**
	 * Returns all available {@link PriceInformation} for the given {@link ProductModel} and the current session user.
	 *
	 * @param model
	 *           the product
	 * @return an empty list if no price information exists for the product and current session user.
	 */
	List<PriceInformation> getPriceInformationsForProduct(ProductModel model);

	/**
	 * Calculates all applicable prices for given criteria.
	 */
	default List<PriceInformation> getPriceInformations(final PriceCriteria priceCriteria)
	{
		throw new UnsupportedOperationException("Not implemented");
	}
}
