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
package de.hybris.platform.ordercancel.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type OrderEntryCancelRecordEntry first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class OrderEntryCancelRecordEntryModel extends OrderEntryModificationRecordEntryModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OrderEntryCancelRecordEntry";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderEntryCancelRecordEntry.cancelRequestQuantity</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CANCELREQUESTQUANTITY = "cancelRequestQuantity";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderEntryCancelRecordEntry.cancelledQuantity</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CANCELLEDQUANTITY = "cancelledQuantity";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderEntryCancelRecordEntry.cancelReason</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CANCELREASON = "cancelReason";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OrderEntryCancelRecordEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OrderEntryCancelRecordEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>OrderEntryModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _modificationRecordEntry initial attribute declared by type <code>OrderEntryModificationRecordEntry</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public OrderEntryCancelRecordEntryModel(final String _code, final OrderModificationRecordEntryModel _modificationRecordEntry)
	{
		super();
		setCode(_code);
		setModificationRecordEntry(_modificationRecordEntry);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>OrderEntryModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _modificationRecordEntry initial attribute declared by type <code>OrderEntryModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public OrderEntryCancelRecordEntryModel(final String _code, final OrderModificationRecordEntryModel _modificationRecordEntry, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setModificationRecordEntry(_modificationRecordEntry);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderEntryCancelRecordEntry.cancelledQuantity</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the cancelledQuantity
	 */
	@Accessor(qualifier = "cancelledQuantity", type = Accessor.Type.GETTER)
	public Integer getCancelledQuantity()
	{
		return getPersistenceContext().getPropertyValue(CANCELLEDQUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderEntryCancelRecordEntry.cancelReason</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the cancelReason
	 */
	@Accessor(qualifier = "cancelReason", type = Accessor.Type.GETTER)
	public CancelReason getCancelReason()
	{
		return getPersistenceContext().getPropertyValue(CANCELREASON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderEntryCancelRecordEntry.cancelRequestQuantity</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the cancelRequestQuantity
	 */
	@Accessor(qualifier = "cancelRequestQuantity", type = Accessor.Type.GETTER)
	public Integer getCancelRequestQuantity()
	{
		return getPersistenceContext().getPropertyValue(CANCELREQUESTQUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderEntryCancelRecordEntry.cancelledQuantity</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the cancelledQuantity
	 */
	@Accessor(qualifier = "cancelledQuantity", type = Accessor.Type.SETTER)
	public void setCancelledQuantity(final Integer value)
	{
		getPersistenceContext().setPropertyValue(CANCELLEDQUANTITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderEntryCancelRecordEntry.cancelReason</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the cancelReason
	 */
	@Accessor(qualifier = "cancelReason", type = Accessor.Type.SETTER)
	public void setCancelReason(final CancelReason value)
	{
		getPersistenceContext().setPropertyValue(CANCELREASON, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderEntryCancelRecordEntry.cancelRequestQuantity</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the cancelRequestQuantity
	 */
	@Accessor(qualifier = "cancelRequestQuantity", type = Accessor.Type.SETTER)
	public void setCancelRequestQuantity(final Integer value)
	{
		getPersistenceContext().setPropertyValue(CANCELREQUESTQUANTITY, value);
	}
	
}
