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
package de.hybris.platform.servicelayer.internal.model.order;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.internal.model.order.InMemoryCartModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type InMemoryCartEntry first defined at extension core.
 */
@SuppressWarnings("all")
public class InMemoryCartEntryModel extends CartEntryModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "InMemoryCartEntry";
	
	/**<i>Generated relation code constant for relation <code>AbstractOrder2AbstractOrderEntry</code> defining source attribute <code>order</code> in extension <code>core</code>.</i>*/
	public static final String _ABSTRACTORDER2ABSTRACTORDERENTRY = "AbstractOrder2AbstractOrderEntry";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public InMemoryCartEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public InMemoryCartEntryModel(final ItemModelContext ctx)
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
	public InMemoryCartEntryModel(final ProductModel _product, final Long _quantity, final UnitModel _unit)
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
	public InMemoryCartEntryModel(final ItemModel _owner, final ProductModel _product, final Long _quantity, final UnitModel _unit)
	{
		super();
		setOwner(_owner);
		setProduct(_product);
		setQuantity(_quantity);
		setUnit(_unit);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.order</code> attribute defined at extension <code>core</code> and redeclared at extension <code>core</code>. 
	 * @return the order
	 */
	@Override
	@Accessor(qualifier = "order", type = Accessor.Type.GETTER)
	public InMemoryCartModel getOrder()
	{
		return (InMemoryCartModel) super.getOrder();
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractOrderEntry.order</code> attribute defined at extension <code>core</code> and redeclared at extension <code>core</code>. Can only be used at creation of model - before first save. Will only accept values of type {@link de.hybris.platform.servicelayer.internal.model.order.InMemoryCartModel}.  
	 *  
	 * @param value the order
	 */
	@Override
	@Accessor(qualifier = "order", type = Accessor.Type.SETTER)
	public void setOrder(final AbstractOrderModel value)
	{
		if( value == null || value instanceof InMemoryCartModel)
		{
			super.setOrder(value);
		}
		else
		{
			throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.servicelayer.internal.model.order.InMemoryCartModel");
		}
	}
	
}
