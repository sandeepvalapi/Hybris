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
package de.hybris.platform.ticket.events.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;

/**
 * Generated model class for type CsTicketChangeEventEntry first defined at extension ticketsystem.
 */
@SuppressWarnings("all")
public class CsTicketChangeEventEntryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CsTicketChangeEventEntry";
	
	/**<i>Generated relation code constant for relation <code>CsTicketEvent2CsTicketChangeEntry</code> defining source attribute <code>event</code> in extension <code>ticketsystem</code>.</i>*/
	public static final String _CSTICKETEVENT2CSTICKETCHANGEENTRY = "CsTicketEvent2CsTicketChangeEntry";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketChangeEventEntry.alteredAttribute</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String ALTEREDATTRIBUTE = "alteredAttribute";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketChangeEventEntry.oldStringValue</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String OLDSTRINGVALUE = "oldStringValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketChangeEventEntry.newStringValue</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String NEWSTRINGVALUE = "newStringValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketChangeEventEntry.oldBinaryValue</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String OLDBINARYVALUE = "oldBinaryValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketChangeEventEntry.newBinaryValue</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String NEWBINARYVALUE = "newBinaryValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>CsTicketChangeEventEntry.event</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String EVENT = "event";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CsTicketChangeEventEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CsTicketChangeEventEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CsTicketChangeEventEntryModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketChangeEventEntry.alteredAttribute</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the alteredAttribute
	 */
	@Accessor(qualifier = "alteredAttribute", type = Accessor.Type.GETTER)
	public AttributeDescriptorModel getAlteredAttribute()
	{
		return getPersistenceContext().getPropertyValue(ALTEREDATTRIBUTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketChangeEventEntry.event</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the event
	 */
	@Accessor(qualifier = "event", type = Accessor.Type.GETTER)
	public CsTicketEventModel getEvent()
	{
		return getPersistenceContext().getPropertyValue(EVENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketChangeEventEntry.newBinaryValue</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the newBinaryValue
	 */
	@Accessor(qualifier = "newBinaryValue", type = Accessor.Type.GETTER)
	public Object getNewBinaryValue()
	{
		return getPersistenceContext().getPropertyValue(NEWBINARYVALUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketChangeEventEntry.newStringValue</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the newStringValue
	 */
	@Accessor(qualifier = "newStringValue", type = Accessor.Type.GETTER)
	public String getNewStringValue()
	{
		return getPersistenceContext().getPropertyValue(NEWSTRINGVALUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketChangeEventEntry.oldBinaryValue</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the oldBinaryValue
	 */
	@Accessor(qualifier = "oldBinaryValue", type = Accessor.Type.GETTER)
	public Object getOldBinaryValue()
	{
		return getPersistenceContext().getPropertyValue(OLDBINARYVALUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CsTicketChangeEventEntry.oldStringValue</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the oldStringValue
	 */
	@Accessor(qualifier = "oldStringValue", type = Accessor.Type.GETTER)
	public String getOldStringValue()
	{
		return getPersistenceContext().getPropertyValue(OLDSTRINGVALUE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketChangeEventEntry.alteredAttribute</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the alteredAttribute
	 */
	@Accessor(qualifier = "alteredAttribute", type = Accessor.Type.SETTER)
	public void setAlteredAttribute(final AttributeDescriptorModel value)
	{
		getPersistenceContext().setPropertyValue(ALTEREDATTRIBUTE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketChangeEventEntry.event</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the event
	 */
	@Accessor(qualifier = "event", type = Accessor.Type.SETTER)
	public void setEvent(final CsTicketEventModel value)
	{
		getPersistenceContext().setPropertyValue(EVENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketChangeEventEntry.newBinaryValue</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the newBinaryValue
	 */
	@Accessor(qualifier = "newBinaryValue", type = Accessor.Type.SETTER)
	public void setNewBinaryValue(final Object value)
	{
		getPersistenceContext().setPropertyValue(NEWBINARYVALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketChangeEventEntry.newStringValue</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the newStringValue
	 */
	@Accessor(qualifier = "newStringValue", type = Accessor.Type.SETTER)
	public void setNewStringValue(final String value)
	{
		getPersistenceContext().setPropertyValue(NEWSTRINGVALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketChangeEventEntry.oldBinaryValue</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the oldBinaryValue
	 */
	@Accessor(qualifier = "oldBinaryValue", type = Accessor.Type.SETTER)
	public void setOldBinaryValue(final Object value)
	{
		getPersistenceContext().setPropertyValue(OLDBINARYVALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CsTicketChangeEventEntry.oldStringValue</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the oldStringValue
	 */
	@Accessor(qualifier = "oldStringValue", type = Accessor.Type.SETTER)
	public void setOldStringValue(final String value)
	{
		getPersistenceContext().setPropertyValue(OLDSTRINGVALUE, value);
	}
	
}
