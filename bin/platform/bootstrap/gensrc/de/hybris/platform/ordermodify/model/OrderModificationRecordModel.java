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
package de.hybris.platform.ordermodify.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type OrderModificationRecord first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class OrderModificationRecordModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OrderModificationRecord";
	
	/**<i>Generated relation code constant for relation <code>Order2OrderModificationRecords</code> defining source attribute <code>order</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _ORDER2ORDERMODIFICATIONRECORDS = "Order2OrderModificationRecords";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderModificationRecord.inProgress</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String INPROGRESS = "inProgress";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderModificationRecord.identifier</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String IDENTIFIER = "identifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderModificationRecord.order</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String ORDER = "order";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderModificationRecord.modificationRecordEntries</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String MODIFICATIONRECORDENTRIES = "modificationRecordEntries";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OrderModificationRecordModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OrderModificationRecordModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _inProgress initial attribute declared by type <code>OrderModificationRecord</code> at extension <code>basecommerce</code>
	 * @param _order initial attribute declared by type <code>OrderModificationRecord</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public OrderModificationRecordModel(final boolean _inProgress, final OrderModel _order)
	{
		super();
		setInProgress(_inProgress);
		setOrder(_order);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _inProgress initial attribute declared by type <code>OrderModificationRecord</code> at extension <code>basecommerce</code>
	 * @param _order initial attribute declared by type <code>OrderModificationRecord</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public OrderModificationRecordModel(final boolean _inProgress, final OrderModel _order, final ItemModel _owner)
	{
		super();
		setInProgress(_inProgress);
		setOrder(_order);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderModificationRecord.identifier</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the identifier
	 */
	@Accessor(qualifier = "identifier", type = Accessor.Type.GETTER)
	public String getIdentifier()
	{
		return getPersistenceContext().getPropertyValue(IDENTIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderModificationRecord.modificationRecordEntries</code> attribute defined at extension <code>basecommerce</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the modificationRecordEntries
	 */
	@Accessor(qualifier = "modificationRecordEntries", type = Accessor.Type.GETTER)
	public Collection<OrderModificationRecordEntryModel> getModificationRecordEntries()
	{
		return getPersistenceContext().getPropertyValue(MODIFICATIONRECORDENTRIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderModificationRecord.order</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.GETTER)
	public OrderModel getOrder()
	{
		return getPersistenceContext().getPropertyValue(ORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderModificationRecord.inProgress</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the inProgress - Determines if the partial type of orderModification is currently in progress
	 */
	@Accessor(qualifier = "inProgress", type = Accessor.Type.GETTER)
	public boolean isInProgress()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(INPROGRESS));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderModificationRecord.identifier</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the identifier
	 */
	@Accessor(qualifier = "identifier", type = Accessor.Type.SETTER)
	public void setIdentifier(final String value)
	{
		getPersistenceContext().setPropertyValue(IDENTIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderModificationRecord.inProgress</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the inProgress - Determines if the partial type of orderModification is currently in progress
	 */
	@Accessor(qualifier = "inProgress", type = Accessor.Type.SETTER)
	public void setInProgress(final boolean value)
	{
		getPersistenceContext().setPropertyValue(INPROGRESS, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderModificationRecord.modificationRecordEntries</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the modificationRecordEntries
	 */
	@Accessor(qualifier = "modificationRecordEntries", type = Accessor.Type.SETTER)
	public void setModificationRecordEntries(final Collection<OrderModificationRecordEntryModel> value)
	{
		getPersistenceContext().setPropertyValue(MODIFICATIONRECORDENTRIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderModificationRecord.order</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.SETTER)
	public void setOrder(final OrderModel value)
	{
		getPersistenceContext().setPropertyValue(ORDER, value);
	}
	
}
