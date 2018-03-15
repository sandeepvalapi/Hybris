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
package de.hybris.platform.storelocator.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.storelocator.model.OpeningDayModel;
import java.util.Date;
import java.util.Locale;

/**
 * Generated model class for type SpecialOpeningDay first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class SpecialOpeningDayModel extends OpeningDayModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SpecialOpeningDay";
	
	/** <i>Generated constant</i> - Attribute key of <code>SpecialOpeningDay.date</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String DATE = "date";
	
	/** <i>Generated constant</i> - Attribute key of <code>SpecialOpeningDay.closed</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CLOSED = "closed";
	
	/** <i>Generated constant</i> - Attribute key of <code>SpecialOpeningDay.name</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>SpecialOpeningDay.message</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String MESSAGE = "message";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SpecialOpeningDayModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SpecialOpeningDayModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _date initial attribute declared by type <code>SpecialOpeningDay</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public SpecialOpeningDayModel(final Date _date)
	{
		super();
		setDate(_date);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _date initial attribute declared by type <code>SpecialOpeningDay</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public SpecialOpeningDayModel(final Date _date, final ItemModel _owner)
	{
		super();
		setDate(_date);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SpecialOpeningDay.date</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the date
	 */
	@Accessor(qualifier = "date", type = Accessor.Type.GETTER)
	public Date getDate()
	{
		return getPersistenceContext().getPropertyValue(DATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SpecialOpeningDay.message</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the message
	 */
	@Accessor(qualifier = "message", type = Accessor.Type.GETTER)
	public String getMessage()
	{
		return getMessage(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>SpecialOpeningDay.message</code> attribute defined at extension <code>basecommerce</code>. 
	 * @param loc the value localization key 
	 * @return the message
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "message", type = Accessor.Type.GETTER)
	public String getMessage(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(MESSAGE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SpecialOpeningDay.name</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>SpecialOpeningDay.name</code> attribute defined at extension <code>basecommerce</code>. 
	 * @param loc the value localization key 
	 * @return the name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SpecialOpeningDay.closed</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the closed
	 */
	@Accessor(qualifier = "closed", type = Accessor.Type.GETTER)
	public boolean isClosed()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(CLOSED));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SpecialOpeningDay.closed</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the closed
	 */
	@Accessor(qualifier = "closed", type = Accessor.Type.SETTER)
	public void setClosed(final boolean value)
	{
		getPersistenceContext().setPropertyValue(CLOSED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SpecialOpeningDay.date</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the date
	 */
	@Accessor(qualifier = "date", type = Accessor.Type.SETTER)
	public void setDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(DATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SpecialOpeningDay.message</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the message
	 */
	@Accessor(qualifier = "message", type = Accessor.Type.SETTER)
	public void setMessage(final String value)
	{
		setMessage(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>SpecialOpeningDay.message</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the message
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "message", type = Accessor.Type.SETTER)
	public void setMessage(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(MESSAGE, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SpecialOpeningDay.name</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>SpecialOpeningDay.name</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
}
