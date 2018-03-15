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
import de.hybris.platform.basecommerce.enums.WeekDay;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.storelocator.model.OpeningDayModel;

/**
 * Generated model class for type WeekdayOpeningDay first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class WeekdayOpeningDayModel extends OpeningDayModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "WeekdayOpeningDay";
	
	/** <i>Generated constant</i> - Attribute key of <code>WeekdayOpeningDay.dayOfWeek</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String DAYOFWEEK = "dayOfWeek";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public WeekdayOpeningDayModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public WeekdayOpeningDayModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _dayOfWeek initial attribute declared by type <code>WeekdayOpeningDay</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public WeekdayOpeningDayModel(final WeekDay _dayOfWeek)
	{
		super();
		setDayOfWeek(_dayOfWeek);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _dayOfWeek initial attribute declared by type <code>WeekdayOpeningDay</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public WeekdayOpeningDayModel(final WeekDay _dayOfWeek, final ItemModel _owner)
	{
		super();
		setDayOfWeek(_dayOfWeek);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WeekdayOpeningDay.dayOfWeek</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the dayOfWeek
	 */
	@Accessor(qualifier = "dayOfWeek", type = Accessor.Type.GETTER)
	public WeekDay getDayOfWeek()
	{
		return getPersistenceContext().getPropertyValue(DAYOFWEEK);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WeekdayOpeningDay.dayOfWeek</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the dayOfWeek
	 */
	@Accessor(qualifier = "dayOfWeek", type = Accessor.Type.SETTER)
	public void setDayOfWeek(final WeekDay value)
	{
		getPersistenceContext().setPropertyValue(DAYOFWEEK, value);
	}
	
}
