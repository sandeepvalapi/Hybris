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
package de.hybris.platform.catalog.references.daos;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;


/**
 * {@link ProductReferenceModel} oriented Data Access Object.
 */
public interface ProductReferencesDao
{
	/**
	 * Searches for all product (related on source or target) references for given product.
	 * 
	 * @param product
	 *           product for which we are searching references
	 * 
	 */
	List<ProductReferenceModel> findAllReferences(ProductModel product);

	/**
	 * Searches for product references by qualifier, source and target products, reference type and active flag.
	 * 
	 * @param qualifier
	 *           reference's {@link ProductReferenceModel#QUALIFIER}. If <code>null</code>, qualifier will be ignored as
	 *           a search criterion
	 * @param sourceProduct
	 *           references's {@link ProductReferenceModel#SOURCE}. If <code>null</code>, sourceProduct will be ignored
	 *           as a search criterion
	 * @param targetProduct
	 *           references's {@link ProductReferenceModel#TARGET}. If <code>null</code>, targetProduct will be ignored
	 *           as a search criterion
	 * 
	 * @param type
	 *           references's {@link ProductReferenceModel#REFERENCETYPE}. If <code>null</code>, type will be ignored as
	 *           a search criterion
	 * 
	 * @param active
	 *           references's {@link ProductReferenceModel#ACTIVE} flag. If <code>null</code>, active will be ignored as
	 *           a search criterion
	 * 
	 * @throws IllegalArgumentException
	 *            if qualifier <b>AND</b> source <b>AND</b> target <b>AND</b> type arguments are
	 *            <code>null<code>. The active flag with a <code>null</code> value is ignored here.
	 * 
	 * @return List of {@link ProductReferenceModel}s or empty list if no matching product reference was found.
	 */
	List<ProductReferenceModel> findProductReferences(String qualifier, ProductModel sourceProduct, ProductModel targetProduct,
			ProductReferenceTypeEnum type, Boolean active);
}
