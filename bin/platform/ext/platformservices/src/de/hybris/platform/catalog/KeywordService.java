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
package de.hybris.platform.catalog;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;


/**
 * Provides access to Keyword.
 * 
 * A keyword can be used to allow convenient searches on catalog elements. Keywords are bound to the catalog elements
 * {@link CategoryModel} and {@link ProductModel}.
 * 
 * @spring.bean keywordService
 */
public interface KeywordService
{

	/**
	 * Returns the Keyword for the specified keyword value and <code>CatalogVersion</code>.
	 * 
	 * @param catalogVersion
	 *           The <code>CatalogVersion</code> the <code>Keyword</code> belongs to.
	 * @param keywordValue
	 *           The value of the searched <code>Keyword</code>.
	 * @return The matching <code>Keyword</code>.
	 * 
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            when keyword not found.
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            when more then one found.
	 */
	KeywordModel getKeyword(final CatalogVersionModel catalogVersion, final String keywordValue);


	/**
	 * Returns the Keyword for the specified keyword value and <code>CatalogVersion</code>.
	 * 
	 * @param typeCode
	 *           The code of type (or subtype) of keyword to allow to search for subclasses
	 * @param catalogVersion
	 *           The <code>CatalogVersion</code> the <code>Keyword</code> belongs to.
	 * @param keywordValue
	 *           The value of the searched <code>Keyword</code>.
	 * @return The matching <code>Keyword</code>.
	 * 
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            when keyword not found.
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            when more then one found.
	 */
	KeywordModel getKeyword(String typeCode, CatalogVersionModel catalogVersion, String keywordValue);
}
