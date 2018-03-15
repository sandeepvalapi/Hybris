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
package de.hybris.platform.returns.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type ReturnEntry first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class ReturnEntryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ReturnEntry";
	
	/**<i>Generated relation code constant for relation <code>ReturnRequest2ReturnEntry</code> defining source attribute <code>returnRequest</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _RETURNREQUEST2RETURNENTRY = "ReturnRequest2ReturnEntry";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnEntry.orderEntry</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String ORDERENTRY = "orderEntry";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnEntry.expectedQuantity</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String EXPECTEDQUANTITY = "expectedQuantity";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnEntry.receivedQuantity</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String RECEIVEDQUANTITY = "receivedQuantity";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnEntry.reachedDate</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String REACHEDDATE = "reachedDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnEntry.status</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnEntry.action</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String ACTION = "action";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnEntry.notes</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String NOTES = "notes";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnEntry.returnRequestPOS</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String RETURNREQUESTPOS = "returnRequestPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnEntry.returnRequest</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String RETURNREQUEST = "returnRequest";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ReturnEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ReturnEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _action initial attribute declared by type <code>ReturnEntry</code> at extension <code>basecommerce</code>
	 * @param _orderEntry initial attribute declared by type <code>ReturnEntry</code> at extension <code>basecommerce</code>
	 * @param _status initial attribute declared by type <code>ReturnEntry</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public ReturnEntryModel(final ReturnAction _action, final AbstractOrderEntryModel _orderEntry, final ReturnStatus _status)
	{
		super();
		setAction(_action);
		setOrderEntry(_orderEntry);
		setStatus(_status);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _action initial attribute declared by type <code>ReturnEntry</code> at extension <code>basecommerce</code>
	 * @param _orderEntry initial attribute declared by type <code>ReturnEntry</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _status initial attribute declared by type <code>ReturnEntry</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public ReturnEntryModel(final ReturnAction _action, final AbstractOrderEntryModel _orderEntry, final ItemModel _owner, final ReturnStatus _status)
	{
		super();
		setAction(_action);
		setOrderEntry(_orderEntry);
		setOwner(_owner);
		setStatus(_status);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnEntry.action</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the action
	 */
	@Accessor(qualifier = "action", type = Accessor.Type.GETTER)
	public ReturnAction getAction()
	{
		return getPersistenceContext().getPropertyValue(ACTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnEntry.expectedQuantity</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the expectedQuantity
	 */
	@Accessor(qualifier = "expectedQuantity", type = Accessor.Type.GETTER)
	public Long getExpectedQuantity()
	{
		return getPersistenceContext().getPropertyValue(EXPECTEDQUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnEntry.notes</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the notes
	 */
	@Accessor(qualifier = "notes", type = Accessor.Type.GETTER)
	public String getNotes()
	{
		return getPersistenceContext().getPropertyValue(NOTES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnEntry.orderEntry</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the orderEntry
	 */
	@Accessor(qualifier = "orderEntry", type = Accessor.Type.GETTER)
	public AbstractOrderEntryModel getOrderEntry()
	{
		return getPersistenceContext().getPropertyValue(ORDERENTRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnEntry.reachedDate</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the reachedDate
	 */
	@Accessor(qualifier = "reachedDate", type = Accessor.Type.GETTER)
	public Date getReachedDate()
	{
		return getPersistenceContext().getPropertyValue(REACHEDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnEntry.receivedQuantity</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the receivedQuantity
	 */
	@Accessor(qualifier = "receivedQuantity", type = Accessor.Type.GETTER)
	public Long getReceivedQuantity()
	{
		return getPersistenceContext().getPropertyValue(RECEIVEDQUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnEntry.returnRequest</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the returnRequest
	 */
	@Accessor(qualifier = "returnRequest", type = Accessor.Type.GETTER)
	public ReturnRequestModel getReturnRequest()
	{
		return getPersistenceContext().getPropertyValue(RETURNREQUEST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnEntry.status</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public ReturnStatus getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnEntry.action</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the action
	 */
	@Accessor(qualifier = "action", type = Accessor.Type.SETTER)
	public void setAction(final ReturnAction value)
	{
		getPersistenceContext().setPropertyValue(ACTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnEntry.expectedQuantity</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the expectedQuantity
	 */
	@Accessor(qualifier = "expectedQuantity", type = Accessor.Type.SETTER)
	public void setExpectedQuantity(final Long value)
	{
		getPersistenceContext().setPropertyValue(EXPECTEDQUANTITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnEntry.notes</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the notes
	 */
	@Accessor(qualifier = "notes", type = Accessor.Type.SETTER)
	public void setNotes(final String value)
	{
		getPersistenceContext().setPropertyValue(NOTES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnEntry.orderEntry</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the orderEntry
	 */
	@Accessor(qualifier = "orderEntry", type = Accessor.Type.SETTER)
	public void setOrderEntry(final AbstractOrderEntryModel value)
	{
		getPersistenceContext().setPropertyValue(ORDERENTRY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnEntry.reachedDate</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the reachedDate
	 */
	@Accessor(qualifier = "reachedDate", type = Accessor.Type.SETTER)
	public void setReachedDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(REACHEDDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnEntry.receivedQuantity</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the receivedQuantity
	 */
	@Accessor(qualifier = "receivedQuantity", type = Accessor.Type.SETTER)
	public void setReceivedQuantity(final Long value)
	{
		getPersistenceContext().setPropertyValue(RECEIVEDQUANTITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnEntry.returnRequest</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the returnRequest
	 */
	@Accessor(qualifier = "returnRequest", type = Accessor.Type.SETTER)
	public void setReturnRequest(final ReturnRequestModel value)
	{
		getPersistenceContext().setPropertyValue(RETURNREQUEST, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnEntry.status</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final ReturnStatus value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
}
