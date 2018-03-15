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
package de.hybris.platform.commerceservices.search.facetdata;

import java.io.Serializable;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import java.util.List;

/**
 * POJO representing a facet.
 */
public  class FacetData<STATE>  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>FacetData<STATE>.code</code> property defined at extension <code>commerceservices</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>FacetData<STATE>.name</code> property defined at extension <code>commerceservices</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>FacetData<STATE>.priority</code> property defined at extension <code>commerceservices</code>. */
		
	private int priority;

	/** <i>Generated property</i> for <code>FacetData<STATE>.category</code> property defined at extension <code>commerceservices</code>. */
		
	private boolean category;

	/** <i>Generated property</i> for <code>FacetData<STATE>.multiSelect</code> property defined at extension <code>commerceservices</code>. */
		
	private boolean multiSelect;

	/** <i>Generated property</i> for <code>FacetData<STATE>.visible</code> property defined at extension <code>commerceservices</code>. */
		
	private boolean visible;

	/** <i>Generated property</i> for <code>FacetData<STATE>.topValues</code> property defined at extension <code>commerceservices</code>. */
		
	private List<FacetValueData<STATE>> topValues;

	/** <i>Generated property</i> for <code>FacetData<STATE>.values</code> property defined at extension <code>commerceservices</code>. */
		
	private List<FacetValueData<STATE>> values;
	
	public FacetData()
	{
		// default constructor
	}
	
		
	
	public void setCode(final String code)
	{
		this.code = code;
	}

		
	
	public String getCode() 
	{
		return code;
	}
	
		
	
	public void setName(final String name)
	{
		this.name = name;
	}

		
	
	public String getName() 
	{
		return name;
	}
	
		
	
	public void setPriority(final int priority)
	{
		this.priority = priority;
	}

		
	
	public int getPriority() 
	{
		return priority;
	}
	
		
	
	public void setCategory(final boolean category)
	{
		this.category = category;
	}

		
	
	public boolean isCategory() 
	{
		return category;
	}
	
		
	
	public void setMultiSelect(final boolean multiSelect)
	{
		this.multiSelect = multiSelect;
	}

		
	
	public boolean isMultiSelect() 
	{
		return multiSelect;
	}
	
		
	
	public void setVisible(final boolean visible)
	{
		this.visible = visible;
	}

		
	
	public boolean isVisible() 
	{
		return visible;
	}
	
		
	
	public void setTopValues(final List<FacetValueData<STATE>> topValues)
	{
		this.topValues = topValues;
	}

		
	
	public List<FacetValueData<STATE>> getTopValues() 
	{
		return topValues;
	}
	
		
	
	public void setValues(final List<FacetValueData<STATE>> values)
	{
		this.values = values;
	}

		
	
	public List<FacetValueData<STATE>> getValues() 
	{
		return values;
	}
	


}
