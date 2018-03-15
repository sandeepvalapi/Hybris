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
package de.hybris.platform.classification.filter;

import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collections;
import java.util.List;


/**
 * The result you get when you have applied a product filter. It contains the list of the filtered products and also a
 * list of all possible attribute values for these products.
 */
public class ProductFilterResult
{

	private final List<ProductModel> products;
	private final List<FilterAttribute> filteredAttributes;
	private final int totalCount;


	/**
	 * Initializes a result with a list of products and a list of possible filter attributes.
	 */
	public ProductFilterResult(final List<ProductModel> products, final List<FilterAttribute> filteredAttributes,
			final int totalCount)
	{
		super();
		this.products = products;
		this.filteredAttributes = filteredAttributes;
		this.totalCount = totalCount;
	}

	/**
	 * Returns the list of the filtered products.
	 * 
	 * @return the list of the filtered products
	 */
	public List<ProductModel> getProducts()
	{
		return Collections.unmodifiableList(products);
	}

	/**
	 * A list of possible attribute values for the filtered products. Each item represents a classifcation attribute and
	 * a list of possible values, but only those values that can be found in the filtered products. Values that have been
	 * used in the filter are marked with a flag. You can use this information to construct a new filter with which you
	 * can further drill down the result, e.g. with a funnel search.
	 * 
	 * @see FilterAttribute
	 * @see FilterAttributeValue
	 * @return a list of possible filter attributes
	 */
	public List<FilterAttribute> getFilteredAttributes()
	{
		return Collections.unmodifiableList(filteredAttributes);
	}

	/**
	 * The number of elements in the result list. This may be less that the {@link #getTotalCount() total count} if you
	 * have limited the result list.
	 * 
	 * @return the number of elements in the result list
	 */
	public int getCount()
	{
		return products.size();
	}

	/**
	 * The total number of products for the filter. This may be more than the {@link #getCount() count} if you have
	 * limited the result lost
	 * 
	 * @return the total number of products for the filter
	 */
	public int getTotalCount()
	{
		return totalCount;
	}

}
