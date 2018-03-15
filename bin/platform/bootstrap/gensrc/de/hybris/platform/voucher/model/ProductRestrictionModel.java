/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Mar 15, 2018 5:02:29 PM                     ---
 * ----------------------------------------------------------------
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *  
 */
package de.hybris.platform.voucher.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;
import java.util.Collection;

/**
 * Generated model class for type ProductRestriction first defined at extension voucher.
 */
@SuppressWarnings("all")
public class ProductRestrictionModel extends RestrictionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductRestriction";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductRestriction.products</code> attribute defined at extension <code>voucher</code>. */
	public static final String PRODUCTS = "products";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductRestrictionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductRestrictionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _voucher initial attribute declared by type <code>Restriction</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public ProductRestrictionModel(final VoucherModel _voucher)
	{
		super();
		setVoucher(_voucher);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _voucher initial attribute declared by type <code>Restriction</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public ProductRestrictionModel(final ItemModel _owner, final VoucherModel _voucher)
	{
		super();
		setOwner(_owner);
		setVoucher(_voucher);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductRestriction.products</code> attribute defined at extension <code>voucher</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the products - the products the given voucher is valid for.
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.GETTER)
	public Collection<ProductModel> getProducts()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductRestriction.products</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the products - the products the given voucher is valid for.
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.SETTER)
	public void setProducts(final Collection<ProductModel> value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTS, value);
	}
	
}
