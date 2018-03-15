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
package de.hybris.platform.cmswebservices.dto;

import java.io.Serializable;
import java.util.List;

public  class PageContentSlotContainerWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>PageContentSlotContainerWsDTO.pageId</code> property defined at extension <code>cmswebservices</code>. */
		
	private String pageId;

	/** <i>Generated property</i> for <code>PageContentSlotContainerWsDTO.slotId</code> property defined at extension <code>cmswebservices</code>. */
		
	private String slotId;

	/** <i>Generated property</i> for <code>PageContentSlotContainerWsDTO.containerId</code> property defined at extension <code>cmswebservices</code>. */
		
	private String containerId;

	/** <i>Generated property</i> for <code>PageContentSlotContainerWsDTO.containerType</code> property defined at extension <code>cmswebservices</code>. */
		
	private String containerType;

	/** <i>Generated property</i> for <code>PageContentSlotContainerWsDTO.components</code> property defined at extension <code>cmswebservices</code>. */
		
	private List<String> components;
	
	public PageContentSlotContainerWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setPageId(final String pageId)
	{
		this.pageId = pageId;
	}

		
	
	public String getPageId() 
	{
		return pageId;
	}
	
		
	
	public void setSlotId(final String slotId)
	{
		this.slotId = slotId;
	}

		
	
	public String getSlotId() 
	{
		return slotId;
	}
	
		
	
	public void setContainerId(final String containerId)
	{
		this.containerId = containerId;
	}

		
	
	public String getContainerId() 
	{
		return containerId;
	}
	
		
	
	public void setContainerType(final String containerType)
	{
		this.containerType = containerType;
	}

		
	
	public String getContainerType() 
	{
		return containerType;
	}
	
		
	
	public void setComponents(final List<String> components)
	{
		this.components = components;
	}

		
	
	public List<String> getComponents() 
	{
		return components;
	}
	


}
