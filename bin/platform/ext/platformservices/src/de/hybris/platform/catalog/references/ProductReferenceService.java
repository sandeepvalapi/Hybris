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
package de.hybris.platform.catalog.references;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;


/**
 * Service providing product references oriented functionality.
 */
public interface ProductReferenceService
{

	/**
	 * Returns all {@link ProductReferenceModel}s for the given (persisted) <b>source</b> {@link ProductModel} and for
	 * the specific {@link ProductReferenceTypeEnum}.
	 * <p/>
	 * Example: Using {@link ProductReferenceTypeEnum#ACCESSORIES} returns the related (accessories)
	 * {@link ProductReferenceModel}s, <b>NOT</b> the actual products (accessories), use
	 * {@link ProductReferenceModel#getTarget()} to get the actual accessory product.
	 * 
	 * @param source
	 *           the source {@link ProductModel}
	 * @param referenceType
	 *           the type of the reference - {@link ProductReferenceTypeEnum}. If this argument is
	 *           <code>null<code>, the productReferences of all types will be returned.
	 * @param activeOnly
	 *           - if <code>true<code>, only active product references will be returned.
	 * @throws IllegalArgumentException
	 *            if <code>source<code> product is null.
	 * @return a collection of {@link ProductReferenceModel}s or an empty list if there are no source references for the
	 *         given product
	 */
	public Collection<ProductReferenceModel> getProductReferencesForSourceProduct(ProductModel source,
			ProductReferenceTypeEnum referenceType, boolean activeOnly);

	/**
	 * Returns all {@link ProductReferenceModel}s for the given (persisted) <b>target</b> {@link ProductModel} and for
	 * the specific {@link ProductReferenceTypeEnum}.
	 * <p/>
	 * Example: Using {@link ProductReferenceTypeEnum#CROSSELLING} returns the related (for cross selling)
	 * {@link ProductReferenceModel}s, <b>NOT</b> the actual cross selled (source) products, use
	 * {@link ProductReferenceModel#getSource()} to get the source product of the cross selling.
	 * 
	 * @param target
	 *           the target {@link ProductModel}
	 * @param referenceType
	 *           the type of the reference - {@link ProductReferenceTypeEnum}. If this argument is
	 *           <code>null<code>, the productReferences of all types will be returned.
	 * @param activeOnly
	 *           - if <code>true<code>, only active product references will be returned.
	 * @throws IllegalArgumentException
	 *            if <code>target<code> product is null.
	 * @return a collection of {@link ProductReferenceModel}s or empty list if there are no target references for the
	 *         given product.
	 */
	public Collection<ProductReferenceModel> getProductReferencesForTargetProduct(ProductModel target,
			ProductReferenceTypeEnum referenceType, boolean activeOnly);

	/**
	 * Returns all {@link ProductReferenceModel}s that join the two given products in the given (persisted) source/target
	 * configuration.
	 * <p/>
	 * One can get multiple {@link ProductReferenceModel}s as one product may reference another in different way
	 * (different {@link ProductReferenceModel#REFERENCETYPE})
	 * <p/>
	 * Example: two products can be joined by a reference of {@link ProductReferenceTypeEnum#ACCESSORIES} and
	 * {@link ProductReferenceTypeEnum#CROSSELLING} types, which means that one is an accessory of the other and - at the
	 * same time - they are configured in the cross-selling strategy.
	 * <p>
	 * 
	 * @param source
	 *           the source {@link ProductModel}
	 * @param target
	 *           the target {@link ProductModel}
	 * @param activeOnly
	 *           - if <code>true<code>, only active Product references will be returned.
	 * @throws IllegalArgumentException
	 *            if either <code>source<code> or <code>target<code> product is null.
	 * @return Collection of the {@link ProductReferenceModel}s or empty list.
	 */
	public Collection<ProductReferenceModel> getProductReferencesForSourceAndTarget(ProductModel source, ProductModel target,
			boolean activeOnly);

	/**
	 * Returns {@link ProductReferenceModel}s that join the two given products in the given source/target configuration
	 * having the given {@link ProductReferenceModel#QUALIFIER} value.
	 * <p/>
	 * 
	 * @param qualifier
	 *           the desired {@link ProductReferenceModel#QUALIFIER}. If this argument is
	 *           <code>null<code> or empty string, it will be ignored as a search criterion.
	 * @param source
	 *           the source {@link ProductModel}
	 * @param target
	 *           the target {@link ProductModel}
	 * @param activeOnly
	 *           - if <code>true<code>, only active Product references will be returned.
	 * @throws IllegalArgumentException
	 *            if either <code>source<code> or <code>target<code> product is null.
	 * 
	 * @return Collection of the {@link ProductReferenceModel}s or empty list.
	 */
	public Collection<ProductReferenceModel> getProductReferences(final String qualifier, final ProductModel source,
			final ProductModel target, boolean activeOnly);
}
