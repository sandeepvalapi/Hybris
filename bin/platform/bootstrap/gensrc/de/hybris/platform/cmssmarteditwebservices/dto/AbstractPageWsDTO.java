/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:38 PM
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
package de.hybris.platform.cmssmarteditwebservices.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public  class AbstractPageWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>AbstractPageWsDTO.pk</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private String pk;

	/** <i>Generated property</i> for <code>AbstractPageWsDTO.creationtime</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private Date creationtime;

	/** <i>Generated property</i> for <code>AbstractPageWsDTO.modifiedtime</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private Date modifiedtime;

	/** <i>Generated property</i> for <code>AbstractPageWsDTO.uid</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private String uid;

	/** <i>Generated property</i> for <code>AbstractPageWsDTO.name</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>AbstractPageWsDTO.title</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private Map<String,String> title;

	/** <i>Generated property</i> for <code>AbstractPageWsDTO.typeCode</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private String typeCode;

	/** <i>Generated property</i> for <code>AbstractPageWsDTO.template</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private String template;

	/** <i>Generated property</i> for <code>AbstractPageWsDTO.defaultPage</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private boolean defaultPage;

	/** <i>Generated property</i> for <code>AbstractPageWsDTO.onlyOneRestrictionMustApply</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private boolean onlyOneRestrictionMustApply;

	/** <i>Generated property</i> for <code>AbstractPageWsDTO.cloneComponents</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private boolean cloneComponents;

	/** <i>Generated property</i> for <code>AbstractPageWsDTO.restrictions</code> property defined at extension <code>cmssmarteditwebservices</code>. */
		
	private List<String> restrictions;
	
	public AbstractPageWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setPk(final String pk)
	{
		this.pk = pk;
	}

		
	
	public String getPk() 
	{
		return pk;
	}
	
		
	
	public void setCreationtime(final Date creationtime)
	{
		this.creationtime = creationtime;
	}

		
	
	public Date getCreationtime() 
	{
		return creationtime;
	}
	
		
	
	public void setModifiedtime(final Date modifiedtime)
	{
		this.modifiedtime = modifiedtime;
	}

		
	
	public Date getModifiedtime() 
	{
		return modifiedtime;
	}
	
		
	
	public void setUid(final String uid)
	{
		this.uid = uid;
	}

		
	
	public String getUid() 
	{
		return uid;
	}
	
		
	
	public void setName(final String name)
	{
		this.name = name;
	}

		
	
	public String getName() 
	{
		return name;
	}
	
		
	
	public void setTitle(final Map<String,String> title)
	{
		this.title = title;
	}

		
	
	public Map<String,String> getTitle() 
	{
		return title;
	}
	
		
	
	public void setTypeCode(final String typeCode)
	{
		this.typeCode = typeCode;
	}

		
	
	public String getTypeCode() 
	{
		return typeCode;
	}
	
		
	
	public void setTemplate(final String template)
	{
		this.template = template;
	}

		
	
	public String getTemplate() 
	{
		return template;
	}
	
		
	
	public void setDefaultPage(final boolean defaultPage)
	{
		this.defaultPage = defaultPage;
	}

		
	
	public boolean isDefaultPage() 
	{
		return defaultPage;
	}
	
		
	
	public void setOnlyOneRestrictionMustApply(final boolean onlyOneRestrictionMustApply)
	{
		this.onlyOneRestrictionMustApply = onlyOneRestrictionMustApply;
	}

		
	
	public boolean isOnlyOneRestrictionMustApply() 
	{
		return onlyOneRestrictionMustApply;
	}
	
		
	
	public void setCloneComponents(final boolean cloneComponents)
	{
		this.cloneComponents = cloneComponents;
	}

		
	
	public boolean isCloneComponents() 
	{
		return cloneComponents;
	}
	
		
	
	public void setRestrictions(final List<String> restrictions)
	{
		this.restrictions = restrictions;
	}

		
	
	public List<String> getRestrictions() 
	{
		return restrictions;
	}
	


}
