/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:40 PM
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
package de.hybris.platform.ruleengineservices.rao;

import de.hybris.platform.ruleengineservices.rao.AbstractActionedRAO;
import de.hybris.platform.ruleengineservices.rao.CategoryRAO;
import java.util.Set;

public  class ProductRAO extends AbstractActionedRAO 
{

 

	/** <i>Generated property</i> for <code>ProductRAO.code</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>ProductRAO.categories</code> property defined at extension <code>ruleengineservices</code>. */
		
	private Set<CategoryRAO> categories;
	
	public ProductRAO()
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
	
		
	
	public void setCategories(final Set<CategoryRAO> categories)
	{
		this.categories = categories;
	}

		
	
	public Set<CategoryRAO> getCategories() 
	{
		return categories;
	}
	

	@Override
	public boolean equals(final Object o)
	{
	
		if (o == null) return false;
		if (o == this) return true;

		try
		{
			final ProductRAO other = (ProductRAO) o;
			return new org.apache.commons.lang.builder.EqualsBuilder()
			.append(getCode(), other.getCode()) 
			.isEquals();
		} 
		catch (ClassCastException c)
		{
			return false;
		}
	}
	
	@Override
	public int hashCode()
	{
		return new org.apache.commons.lang.builder.HashCodeBuilder()
		.append(getCode()) 
		.toHashCode();
	}

}
