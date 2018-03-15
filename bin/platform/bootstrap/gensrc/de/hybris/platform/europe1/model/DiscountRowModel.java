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
package de.hybris.platform.europe1.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.enums.ProductDiscountGroup;
import de.hybris.platform.europe1.model.AbstractDiscountRowModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type DiscountRow first defined at extension europe1.
 */
@SuppressWarnings("all")
public class DiscountRowModel extends AbstractDiscountRowModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DiscountRow";
	
	/**<i>Generated relation code constant for relation <code>Product2OwnEurope1Discounts</code> defining source attribute <code>product</code> in extension <code>europe1</code>.</i>*/
	public static final String _PRODUCT2OWNEUROPE1DISCOUNTS = "Product2OwnEurope1Discounts";
	
	/** <i>Generated constant</i> - Attribute key of <code>DiscountRow.catalogVersion</code> attribute defined at extension <code>europe1</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>DiscountRow.asTargetPrice</code> attribute defined at extension <code>europe1</code>. */
	public static final String ASTARGETPRICE = "asTargetPrice";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DiscountRowModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DiscountRowModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _discount initial attribute declared by type <code>AbstractDiscountRow</code> at extension <code>europe1</code>
	 */
	@Deprecated
	public DiscountRowModel(final DiscountModel _discount)
	{
		super();
		setDiscount(_discount);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _discount initial attribute declared by type <code>AbstractDiscountRow</code> at extension <code>europe1</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _pg initial attribute declared by type <code>AbstractDiscountRow</code> at extension <code>europe1</code>
	 * @param _product initial attribute declared by type <code>DiscountRow</code> at extension <code>europe1</code>
	 * @param _productId initial attribute declared by type <code>PDTRow</code> at extension <code>europe1</code>
	 */
	@Deprecated
	public DiscountRowModel(final DiscountModel _discount, final ItemModel _owner, final ProductDiscountGroup _pg, final ProductModel _product, final String _productId)
	{
		super();
		setDiscount(_discount);
		setOwner(_owner);
		setPg(_pg);
		setProduct(_product);
		setProductId(_productId);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DiscountRow.asTargetPrice</code> attribute defined at extension <code>europe1</code>. 
	 * @return the asTargetPrice
	 */
	@Accessor(qualifier = "asTargetPrice", type = Accessor.Type.GETTER)
	public Boolean getAsTargetPrice()
	{
		return getPersistenceContext().getPropertyValue(ASTARGETPRICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DiscountRow.catalogVersion</code> attribute defined at extension <code>europe1</code>. 
	 * @return the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DiscountRow.asTargetPrice</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the asTargetPrice
	 */
	@Accessor(qualifier = "asTargetPrice", type = Accessor.Type.SETTER)
	public void setAsTargetPrice(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ASTARGETPRICE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DiscountRow.catalogVersion</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>PDTRow.product</code> attribute defined at extension <code>europe1</code> and redeclared at extension <code>europe1</code>. Can only be used at creation of model - before first save. Will only accept values of type {@link de.hybris.platform.core.model.product.ProductModel}.  
	 *  
	 * @param value the product
	 */
	@Override
	@Accessor(qualifier = "product", type = Accessor.Type.SETTER)
	public void setProduct(final ProductModel value)
	{
		super.setProduct(value);
	}
	
}
