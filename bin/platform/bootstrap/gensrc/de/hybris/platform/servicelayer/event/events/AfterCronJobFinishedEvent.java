/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:41 PM
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

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.event.events.AbstractCronJobPerformEvent;

public  class AfterCronJobFinishedEvent extends AbstractCronJobPerformEvent {


	/** <i>Generated property</i> for <code>AfterCronJobFinishedEvent.result</code> property defined at extension <code>processing</code>. */
		
	private CronJobResult result;

	/** <i>Generated property</i> for <code>AfterCronJobFinishedEvent.status</code> property defined at extension <code>processing</code>. */
		
	private CronJobStatus status;
	
	public AfterCronJobFinishedEvent()
	{
		super();
	}

	public AfterCronJobFinishedEvent(final Serializable source)
	{
		super(source);
	}
	
	
	
	public void setResult(final CronJobResult result)
	{
		this.result = result;
	}
	
	
	
	public CronJobResult getResult() 
	{
		return result;
	}
	
	
	
	public void setStatus(final CronJobStatus status)
	{
		this.status = status;
	}
	
	
	
	public CronJobStatus getStatus() 
	{
		return status;
	}
	


}