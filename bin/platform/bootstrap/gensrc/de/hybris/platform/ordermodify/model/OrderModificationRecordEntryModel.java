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
import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;

/**
 * Generated model class for type OrderModificationRecordEntry first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class OrderModificationRecordEntryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OrderModificationRecordEntry";
	
	/**<i>Generated relation code constant for relation <code>OrderModificationRecord2OrderModificationRecordEntries</code> defining source attribute <code>modificationRecord</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _ORDERMODIFICATIONRECORD2ORDERMODIFICATIONRECORDENTRIES = "OrderModificationRecord2OrderModificationRecordEntries";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderModificationRecordEntry.code</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderModificationRecordEntry.timestamp</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String TIMESTAMP = "timestamp";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderModificationRecordEntry.status</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderModificationRecordEntry.originalVersion</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String ORIGINALVERSION = "originalVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderModificationRecordEntry.principal</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String PRINCIPAL = "principal";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderModificationRecordEntry.failedMessage</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String FAILEDMESSAGE = "failedMessage";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderModificationRecordEntry.notes</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String NOTES = "notes";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderModificationRecordEntry.modificationRecord</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String MODIFICATIONRECORD = "modificationRecord";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderModificationRecordEntry.orderEntriesModificationEntries</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String ORDERENTRIESMODIFICATIONENTRIES = "orderEntriesModificationEntries";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OrderModificationRecordEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OrderModificationRecordEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _modificationRecord initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _originalVersion initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _status initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _timestamp initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public OrderModificationRecordEntryModel(final String _code, final OrderModificationRecordModel _modificationRecord, final OrderHistoryEntryModel _originalVersion, final OrderModificationEntryStatus _status, final Date _timestamp)
	{
		super();
		setCode(_code);
		setModificationRecord(_modificationRecord);
		setOriginalVersion(_originalVersion);
		setStatus(_status);
		setTimestamp(_timestamp);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _modificationRecord initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _originalVersion initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _status initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _timestamp initial attribute declared by type <code>OrderModificationRecordEntry</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public OrderModificationRecordEntryModel(final String _code, final OrderModificationRecordModel _modificationRecord, final OrderHistoryEntryModel _originalVersion, final ItemModel _owner, final OrderModificationEntryStatus _status, final Date _timestamp)
	{
		super();
		setCode(_code);
		setModificationRecord(_modificationRecord);
		setOriginalVersion(_originalVersion);
		setOwner(_owner);
		setStatus(_status);
		setTimestamp(_timestamp);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderModificationRecordEntry.code</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderModificationRecordEntry.failedMessage</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the failedMessage
	 */
	@Accessor(qualifier = "failedMessage", type = Accessor.Type.GETTER)
	public String getFailedMessage()
	{
		return getPersistenceContext().getPropertyValue(FAILEDMESSAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderModificationRecordEntry.modificationRecord</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the modificationRecord
	 */
	@Accessor(qualifier = "modificationRecord", type = Accessor.Type.GETTER)
	public OrderModificationRecordModel getModificationRecord()
	{
		return getPersistenceContext().getPropertyValue(MODIFICATIONRECORD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderModificationRecordEntry.notes</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the notes
	 */
	@Accessor(qualifier = "notes", type = Accessor.Type.GETTER)
	public String getNotes()
	{
		return getPersistenceContext().getPropertyValue(NOTES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderModificationRecordEntry.orderEntriesModificationEntries</code> attribute defined at extension <code>basecommerce</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the orderEntriesModificationEntries
	 */
	@Accessor(qualifier = "orderEntriesModificationEntries", type = Accessor.Type.GETTER)
	public Collection<OrderEntryModificationRecordEntryModel> getOrderEntriesModificationEntries()
	{
		return getPersistenceContext().getPropertyValue(ORDERENTRIESMODIFICATIONENTRIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderModificationRecordEntry.originalVersion</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the originalVersion
	 */
	@Accessor(qualifier = "originalVersion", type = Accessor.Type.GETTER)
	public OrderHistoryEntryModel getOriginalVersion()
	{
		return getPersistenceContext().getPropertyValue(ORIGINALVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderModificationRecordEntry.principal</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the principal
	 */
	@Accessor(qualifier = "principal", type = Accessor.Type.GETTER)
	public PrincipalModel getPrincipal()
	{
		return getPersistenceContext().getPropertyValue(PRINCIPAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderModificationRecordEntry.status</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public OrderModificationEntryStatus getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderModificationRecordEntry.timestamp</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the timestamp
	 */
	@Accessor(qualifier = "timestamp", type = Accessor.Type.GETTER)
	public Date getTimestamp()
	{
		return getPersistenceContext().getPropertyValue(TIMESTAMP);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderModificationRecordEntry.code</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderModificationRecordEntry.failedMessage</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the failedMessage
	 */
	@Accessor(qualifier = "failedMessage", type = Accessor.Type.SETTER)
	public void setFailedMessage(final String value)
	{
		getPersistenceContext().setPropertyValue(FAILEDMESSAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderModificationRecordEntry.modificationRecord</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the modificationRecord
	 */
	@Accessor(qualifier = "modificationRecord", type = Accessor.Type.SETTER)
	public void setModificationRecord(final OrderModificationRecordModel value)
	{
		getPersistenceContext().setPropertyValue(MODIFICATIONRECORD, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderModificationRecordEntry.notes</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the notes
	 */
	@Accessor(qualifier = "notes", type = Accessor.Type.SETTER)
	public void setNotes(final String value)
	{
		getPersistenceContext().setPropertyValue(NOTES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderModificationRecordEntry.orderEntriesModificationEntries</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the orderEntriesModificationEntries
	 */
	@Accessor(qualifier = "orderEntriesModificationEntries", type = Accessor.Type.SETTER)
	public void setOrderEntriesModificationEntries(final Collection<OrderEntryModificationRecordEntryModel> value)
	{
		getPersistenceContext().setPropertyValue(ORDERENTRIESMODIFICATIONENTRIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>OrderModificationRecordEntry.originalVersion</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the originalVersion
	 */
	@Accessor(qualifier = "originalVersion", type = Accessor.Type.SETTER)
	public void setOriginalVersion(final OrderHistoryEntryModel value)
	{
		getPersistenceContext().setPropertyValue(ORIGINALVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderModificationRecordEntry.principal</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the principal
	 */
	@Accessor(qualifier = "principal", type = Accessor.Type.SETTER)
	public void setPrincipal(final PrincipalModel value)
	{
		getPersistenceContext().setPropertyValue(PRINCIPAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderModificationRecordEntry.status</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final OrderModificationEntryStatus value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>OrderModificationRecordEntry.timestamp</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the timestamp
	 */
	@Accessor(qualifier = "timestamp", type = Accessor.Type.SETTER)
	public void setTimestamp(final Date value)
	{
		getPersistenceContext().setPropertyValue(TIMESTAMP, value);
	}
	
}
