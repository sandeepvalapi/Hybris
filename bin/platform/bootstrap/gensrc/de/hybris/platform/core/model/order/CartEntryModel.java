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
package de.hybris.platform.core.model.order;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CartEntry first defined at extension core.
 */
@SuppressWarnings("all")
public class CartEntryModel extends AbstractOrderEntryModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CartEntry";
	
	/**<i>Generated relation code constant for relation <code>AbstractOrder2AbstractOrderEntry</code> defining source attribute <code>order</code> in extension <code>core</code>.</i>*/
	public static final String _ABSTRACTORDER2ABSTRACTORDERENTRY = "AbstractOrder2AbstractOrderEntry";
	
	/**<i>Generated relation code constant for relation <code>LastModifiedEntriesRelation</code> defining source attribute <code>lastModifiedMasterCart</code> in extension <code>configurablebundleservices</code>.</i>*/
	public static final String _LASTMODIFIEDENTRIESRELATION = "LastModifiedEntriesRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CartEntry.lastModifiedMasterCart</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String LASTMODIFIEDMASTERCART = "lastModifiedMasterCart";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CartEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CartEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _product initial attribute declared by type <code>AbstractOrderEntry</code> at extension <code>core</code>
	 * @param _quantity initial attribute declared by type <code>AbstractOrderEntry</code> at extension <code>core</code>
	 * @param _unit initial attribute declared by type <code>AbstractOrderEntry</code> at extension <code>core</code>
	 */
	@Deprecated
	public CartEntryModel(final ProductModel _product, final Long _quantity, final UnitModel _unit)
	{
		super();
		setProduct(_product);
		setQuantity(_quantity);
		setUnit(_unit);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _product initial attribute declared by type <code>AbstractOrderEntry</code> at extension <code>core</code>
	 * @param _quantity initial attribute declared by type <code>AbstractOrderEntry</code> at extension <code>core</code>
	 * @param _unit initial attribute declared by type <code>AbstractOrderEntry</code> at extension <code>core</code>
	 */
	@Deprecated
	public CartEntryModel(final ItemModel _owner, final ProductModel _product, final Long _quantity, final UnitModel _unit)
	{
		super();
		setOwner(_owner);
		setProduct(_product);
		setQuantity(_quantity);
		setUnit(_unit);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CartEntry.lastModifiedMasterCart</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the lastModifiedMasterCart
	 */
	@Accessor(qualifier = "lastModifiedMasterCart", type = Accessor.Type.GETTER)
	public CartModel getLastModifiedMasterCart()
	{
		return getPersistenceContext().getPropertyValue(LASTMODIFIEDMASTERCART);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.order</code> attribute defined at extension <code>core</code> and redeclared at extension <code>core</code>. 
	 * @return the order
	 */
	@Override
	@Accessor(qualifier = "order", type = Accessor.Type.GETTER)
	public CartModel getOrder()
	{
		return (CartModel) super.getOrder();
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CartEntry.lastModifiedMasterCart</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the lastModifiedMasterCart
	 */
	@Accessor(qualifier = "lastModifiedMasterCart", type = Accessor.Type.SETTER)
	public void setLastModifiedMasterCart(final CartModel value)
	{
		getPersistenceContext().setPropertyValue(LASTMODIFIEDMASTERCART, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractOrderEntry.order</code> attribute defined at extension <code>core</code> and redeclared at extension <code>core</code>. Can only be used at creation of model - before first save. Will only accept values of type {@link de.hybris.platform.core.model.order.CartModel}.  
	 *  
	 * @param value the order
	 */
	@Override
	@Accessor(qualifier = "order", type = Accessor.Type.SETTER)
	public void setOrder(final AbstractOrderModel value)
	{
		if( value == null || value instanceof CartModel)
		{
			super.setOrder(value);
		}
		else
		{
			throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.core.model.order.CartModel");
		}
	}
	
}
