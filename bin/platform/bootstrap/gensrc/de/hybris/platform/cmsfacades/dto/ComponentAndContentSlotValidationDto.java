/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:39 PM
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
package de.hybris.platform.cmsfacades.dto;

import java.io.Serializable;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;

public  class ComponentAndContentSlotValidationDto  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ComponentAndContentSlotValidationDto.component</code> property defined at extension <code>cmsfacades</code>. */
		
	private AbstractCMSComponentModel component;

	/** <i>Generated property</i> for <code>ComponentAndContentSlotValidationDto.contentSlot</code> property defined at extension <code>cmsfacades</code>. */
		
	private ContentSlotModel contentSlot;
	
	public ComponentAndContentSlotValidationDto()
	{
		// default constructor
	}
	
		
	
	public void setComponent(final AbstractCMSComponentModel component)
	{
		this.component = component;
	}

		
	
	public AbstractCMSComponentModel getComponent() 
	{
		return component;
	}
	
		
	
	public void setContentSlot(final ContentSlotModel contentSlot)
	{
		this.contentSlot = contentSlot;
	}

		
	
	public ContentSlotModel getContentSlot() 
	{
		return contentSlot;
	}
	


}
