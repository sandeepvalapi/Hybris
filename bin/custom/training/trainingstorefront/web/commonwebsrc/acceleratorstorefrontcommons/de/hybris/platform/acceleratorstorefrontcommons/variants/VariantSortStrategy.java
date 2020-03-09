/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.variants;

import de.hybris.platform.commercefacades.product.data.VariantOptionData;

import java.util.Comparator;
import java.util.List;
import java.util.Map;


/**
 * Strategy that will provide comparator used for sorting variants for frontend view
 */
public interface VariantSortStrategy extends Comparator<VariantOptionData>
{
	/**
	 * Sets the sorting fields order. String array contains variant attributes that will be used to compare variants.
	 * When the method was not called or empty list was passed no sorting will be performed.
	 * 
	 * @param fields
	 *           the new sorting fields order
	 */
	void setSortingFieldsOrder(List<String> fields);

	/**
	 * Map of attribute - comparator. Where attribute is variant attribute to compare for sorting
	 * 
	 * @param comparators
	 */
	void setComparators(Map<String, Comparator<String>> comparators);

	/**
	 * @param defaultComparator
	 *           for comparing variants' values - used as fallback, provide your own in {@link #setComparators(Map)}
	 */
	void setDefaultComparator(Comparator<String> defaultComparator);
}
