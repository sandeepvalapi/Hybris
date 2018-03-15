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
import de.hybris.platform.storelocator.model.OpeningScheduleModel;
import java.util.Date;

/**
 * Generated model class for type OpeningDay first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class OpeningDayModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OpeningDay";
	
	/** <i>Generated constant</i> - Attribute key of <code>OpeningDay.openingTime</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String OPENINGTIME = "openingTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>OpeningDay.closingTime</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CLOSINGTIME = "closingTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>OpeningDay.openingSchedule</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String OPENINGSCHEDULE = "openingSchedule";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OpeningDayModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OpeningDayModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public OpeningDayModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OpeningDay.closingTime</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the closingTime
	 */
	@Accessor(qualifier = "closingTime", type = Accessor.Type.GETTER)
	public Date getClosingTime()
	{
		return getPersistenceContext().getPropertyValue(CLOSINGTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OpeningDay.openingSchedule</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the openingSchedule
	 */
	@Accessor(qualifier = "openingSchedule", type = Accessor.Type.GETTER)
	public OpeningScheduleModel getOpeningSchedule()
	{
		return getPersistenceContext().getPropertyValue(OPENINGSCHEDULE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OpeningDay.openingTime</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the openingTime
	 */
	@Accessor(qualifier = "openingTime", type = Accessor.Type.GETTER)
	public Date getOpeningTime()
	{
		return getPersistenceContext().getPropertyValue(OPENINGTIME);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OpeningDay.closingTime</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the closingTime
	 */
	@Accessor(qualifier = "closingTime", type = Accessor.Type.SETTER)
	public void setClosingTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(CLOSINGTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OpeningDay.openingSchedule</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the openingSchedule
	 */
	@Accessor(qualifier = "openingSchedule", type = Accessor.Type.SETTER)
	public void setOpeningSchedule(final OpeningScheduleModel value)
	{
		getPersistenceContext().setPropertyValue(OPENINGSCHEDULE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OpeningDay.openingTime</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the openingTime
	 */
	@Accessor(qualifier = "openingTime", type = Accessor.Type.SETTER)
	public void setOpeningTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(OPENINGTIME, value);
	}
	
}
