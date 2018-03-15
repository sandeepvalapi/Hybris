/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:42 PM
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
 */
package de.hybris.platform.servicelayer.event.events;

import java.io.Serializable;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.event.events.AbstractCronJobEvent;

public abstract  class AbstractCronJobPerformEvent extends AbstractCronJobEvent {


	/** <i>Generated property</i> for <code>AbstractCronJobPerformEvent.scheduled</code> property defined at extension <code>processing</code>. */
		
	private boolean scheduled;

	/** <i>Generated property</i> for <code>AbstractCronJobPerformEvent.scheduledByTriggerPk</code> property defined at extension <code>processing</code>. */
		
	private PK scheduledByTriggerPk;

	/** <i>Generated property</i> for <code>AbstractCronJobPerformEvent.synchronous</code> property defined at extension <code>processing</code>. */
		
	private boolean synchronous;
	
	public AbstractCronJobPerformEvent()
	{
		super();
	}

	public AbstractCronJobPerformEvent(final Serializable source)
	{
		super(source);
	}
	
	
	
	public void setScheduled(final boolean scheduled)
	{
		this.scheduled = scheduled;
	}
	
	
	
	public boolean isScheduled() 
	{
		return scheduled;
	}
	
	
	
	public void setScheduledByTriggerPk(final PK scheduledByTriggerPk)
	{
		this.scheduledByTriggerPk = scheduledByTriggerPk;
	}
	
	
	
	public PK getScheduledByTriggerPk() 
	{
		return scheduledByTriggerPk;
	}
	
	
	
	public void setSynchronous(final boolean synchronous)
	{
		this.synchronous = synchronous;
	}
	
	
	
	public boolean isSynchronous() 
	{
		return synchronous;
	}
	


}