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
package de.hybris.platform.ticketsystem.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CSTicketStagnationCronJob first defined at extension ticketsystem.
 * <p>
 * Cron Job for cleaning CSTickets
 * 			@deprecated Since 6.6: replaced with ticketRetentionCronJob from ycoreaccelerator module.
 */
@SuppressWarnings("all")
public class CSTicketStagnationCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CSTicketStagnationCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>CSTicketStagnationCronJob.stagnationPeriod</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String STAGNATIONPERIOD = "stagnationPeriod";
	
	/** <i>Generated constant</i> - Attribute key of <code>CSTicketStagnationCronJob.eligibleStates</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String ELIGIBLESTATES = "eligibleStates";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CSTicketStagnationCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CSTicketStagnationCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CSTicketStagnationCronJobModel(final JobModel _job)
	{
		super();
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CSTicketStagnationCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CSTicketStagnationCronJob.eligibleStates</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the eligibleStates - Comma separated states to be closed after specific amount of days and send notification to the customer through email.
	 */
	@Accessor(qualifier = "eligibleStates", type = Accessor.Type.GETTER)
	public String getEligibleStates()
	{
		return getPersistenceContext().getPropertyValue(ELIGIBLESTATES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CSTicketStagnationCronJob.stagnationPeriod</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the stagnationPeriod - After specified number of days tickets with specific state will be set to 'CLOSE'. Default is 90 days.
	 */
	@Accessor(qualifier = "stagnationPeriod", type = Accessor.Type.GETTER)
	public int getStagnationPeriod()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(STAGNATIONPERIOD));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CSTicketStagnationCronJob.eligibleStates</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the eligibleStates - Comma separated states to be closed after specific amount of days and send notification to the customer through email.
	 */
	@Accessor(qualifier = "eligibleStates", type = Accessor.Type.SETTER)
	public void setEligibleStates(final String value)
	{
		getPersistenceContext().setPropertyValue(ELIGIBLESTATES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CSTicketStagnationCronJob.stagnationPeriod</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the stagnationPeriod - After specified number of days tickets with specific state will be set to 'CLOSE'. Default is 90 days.
	 */
	@Accessor(qualifier = "stagnationPeriod", type = Accessor.Type.SETTER)
	public void setStagnationPeriod(final int value)
	{
		getPersistenceContext().setPropertyValue(STAGNATIONPERIOD, toObject(value));
	}
	
}
