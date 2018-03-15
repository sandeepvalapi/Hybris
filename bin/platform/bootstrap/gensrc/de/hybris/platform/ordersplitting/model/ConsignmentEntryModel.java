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
package de.hybris.platform.ordersplitting.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ConsignmentEntry first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class ConsignmentEntryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ConsignmentEntry";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConsignmentEntry.quantity</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String QUANTITY = "quantity";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConsignmentEntry.shippedQuantity</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String SHIPPEDQUANTITY = "shippedQuantity";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConsignmentEntry.orderEntry</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String ORDERENTRY = "orderEntry";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConsignmentEntry.consignment</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CONSIGNMENT = "consignment";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ConsignmentEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ConsignmentEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _consignment initial attribute declared by type <code>ConsignmentEntry</code> at extension <code>basecommerce</code>
	 * @param _orderEntry initial attribute declared by type <code>ConsignmentEntry</code> at extension <code>basecommerce</code>
	 * @param _quantity initial attribute declared by type <code>ConsignmentEntry</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public ConsignmentEntryModel(final ConsignmentModel _consignment, final AbstractOrderEntryModel _orderEntry, final Long _quantity)
	{
		super();
		setConsignment(_consignment);
		setOrderEntry(_orderEntry);
		setQuantity(_quantity);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _consignment initial attribute declared by type <code>ConsignmentEntry</code> at extension <code>basecommerce</code>
	 * @param _orderEntry initial attribute declared by type <code>ConsignmentEntry</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _quantity initial attribute declared by type <code>ConsignmentEntry</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public ConsignmentEntryModel(final ConsignmentModel _consignment, final AbstractOrderEntryModel _orderEntry, final ItemModel _owner, final Long _quantity)
	{
		super();
		setConsignment(_consignment);
		setOrderEntry(_orderEntry);
		setOwner(_owner);
		setQuantity(_quantity);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsignmentEntry.consignment</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the consignment
	 */
	@Accessor(qualifier = "consignment", type = Accessor.Type.GETTER)
	public ConsignmentModel getConsignment()
	{
		return getPersistenceContext().getPropertyValue(CONSIGNMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsignmentEntry.orderEntry</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the orderEntry
	 */
	@Accessor(qualifier = "orderEntry", type = Accessor.Type.GETTER)
	public AbstractOrderEntryModel getOrderEntry()
	{
		return getPersistenceContext().getPropertyValue(ORDERENTRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsignmentEntry.quantity</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the quantity
	 */
	@Accessor(qualifier = "quantity", type = Accessor.Type.GETTER)
	public Long getQuantity()
	{
		return getPersistenceContext().getPropertyValue(QUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsignmentEntry.shippedQuantity</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the shippedQuantity
	 */
	@Accessor(qualifier = "shippedQuantity", type = Accessor.Type.GETTER)
	public Long getShippedQuantity()
	{
		return getPersistenceContext().getPropertyValue(SHIPPEDQUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ConsignmentEntry.consignment</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the consignment
	 */
	@Accessor(qualifier = "consignment", type = Accessor.Type.SETTER)
	public void setConsignment(final ConsignmentModel value)
	{
		getPersistenceContext().setPropertyValue(CONSIGNMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ConsignmentEntry.orderEntry</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the orderEntry
	 */
	@Accessor(qualifier = "orderEntry", type = Accessor.Type.SETTER)
	public void setOrderEntry(final AbstractOrderEntryModel value)
	{
		getPersistenceContext().setPropertyValue(ORDERENTRY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConsignmentEntry.quantity</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the quantity
	 */
	@Accessor(qualifier = "quantity", type = Accessor.Type.SETTER)
	public void setQuantity(final Long value)
	{
		getPersistenceContext().setPropertyValue(QUANTITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConsignmentEntry.shippedQuantity</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the shippedQuantity
	 */
	@Accessor(qualifier = "shippedQuantity", type = Accessor.Type.SETTER)
	public void setShippedQuantity(final Long value)
	{
		getPersistenceContext().setPropertyValue(SHIPPEDQUANTITY, value);
	}
	
}
