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
package de.hybris.platform.personalizationservices.trigger.expression;

import java.io.Serializable;
import java.util.Collection;

public  class CxExpressionContext  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CxExpressionContext.segments</code> property defined at extension <code>personalizationservices</code>. */
		
	private Collection<String> segments;
	
	public CxExpressionContext()
	{
		// default constructor
	}
	
		
	
	public void setSegments(final Collection<String> segments)
	{
		this.segments = segments;
	}

		
	
	public Collection<String> getSegments() 
	{
		return segments;
	}
	


}
