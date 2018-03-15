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
package de.hybris.platform.orderhistory.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.Set;

/**
 * Generated model class for type OrderHistoryEntry first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class OrderHistoryEntryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OrderHistoryEntry";
	
	/**<i>Generated relation code constant for relation <code>OrderHistoryRelation</code> defining source attribute <code>order</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _ORDERHISTORYRELATION = "OrderHistoryRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderHistoryEntry.timestamp</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String TIMESTAMP = "timestamp";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderHistoryEntry.employee</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String EMPLOYEE = "employee";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderHistoryEntry.description</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderHistoryEntry.previousOrderVersion</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String PREVIOUSORDERVERSION = "previousOrderVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderHistoryEntry.orderPOS</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String ORDERPOS = "orderPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderHistoryEntry.order</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String ORDER = "order";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderHistoryEntry.documents</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String DOCUMENTS = "documents";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OrderHistoryEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OrderHistoryEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _order initial attribute declared by type <code>OrderHistoryEntry</code> at extension <code>basecommerce</code>
	 * @param _timestamp initial attribute declared by type <code>OrderHistoryEntry</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public OrderHistoryEntryModel(final OrderModel _order, final Date _timestamp)
	{
		super();
		setOrder(_order);
		setTimestamp(_timestamp);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _order initial attribute declared by type <code>OrderHistoryEntry</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _timestamp initial attribute declared by type <code>OrderHistoryEntry</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public OrderHistoryEntryModel(final OrderModel _order, final ItemModel _owner, final Date _timestamp)
	{
		super();
		setOrder(_order);
		setOwner(_owner);
		setTimestamp(_timestamp);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderHistoryEntry.description</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getPersistenceContext().getPropertyValue(DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderHistoryEntry.documents</code> attribute defined at extension <code>basecommerce</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the documents
	 */
	@Accessor(qualifier = "documents", type = Accessor.Type.GETTER)
	public Set<MediaModel> getDocuments()
	{
		return getPersistenceContext().getPropertyValue(DOCUMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderHistoryEntry.employee</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the employee
	 */
	@Accessor(qualifier = "employee", type = Accessor.Type.GETTER)
	public EmployeeModel getEmployee()
	{
		return getPersistenceContext().getPropertyValue(EMPLOYEE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderHistoryEntry.order</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.GETTER)
	public OrderModel getOrder()
	{
		return getPersistenceContext().getPropertyValue(ORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderHistoryEntry.previousOrderVersion</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the previousOrderVersion
	 */
	@Accessor(qualifier = "previousOrderVersion", type = Accessor.Type.GETTER)
	public OrderModel getPreviousOrderVersion()
	{
		return getPersistenceContext().getPropertyValue(PREVIOUSORDERVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderHistoryEntry.timestamp</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the timestamp
	 */
	@Accessor(qualifier = "timestamp", type = Accessor.Type.GETTER)
	public Date getTimestamp()
	{
		return getPersistenceContext().getPropertyValue(TIMESTAMP);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderHistoryEntry.description</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		getPersistenceContext().setPropertyValue(DESCRIPTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderHistoryEntry.documents</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the documents
	 */
	@Accessor(qualifier = "documents", type = Accessor.Type.SETTER)
	public void setDocuments(final Set<MediaModel> value)
	{
		getPersistenceContext().setPropertyValue(DOCUMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderHistoryEntry.employee</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the employee
	 */
	@Accessor(qualifier = "employee", type = Accessor.Type.SETTER)
	public void setEmployee(final EmployeeModel value)
	{
		getPersistenceContext().setPropertyValue(EMPLOYEE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>OrderHistoryEntry.order</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.SETTER)
	public void setOrder(final OrderModel value)
	{
		getPersistenceContext().setPropertyValue(ORDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderHistoryEntry.previousOrderVersion</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the previousOrderVersion
	 */
	@Accessor(qualifier = "previousOrderVersion", type = Accessor.Type.SETTER)
	public void setPreviousOrderVersion(final OrderModel value)
	{
		getPersistenceContext().setPropertyValue(PREVIOUSORDERVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>OrderHistoryEntry.timestamp</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the timestamp
	 */
	@Accessor(qualifier = "timestamp", type = Accessor.Type.SETTER)
	public void setTimestamp(final Date value)
	{
		getPersistenceContext().setPropertyValue(TIMESTAMP, value);
	}
	
}
