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
package de.hybris.platform.personalizationyprofile.yaas;

import java.io.Serializable;
import de.hybris.platform.personalizationyprofile.yaas.Insights;

public  class Profile  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>Profile.insights</code> property defined at extension <code>personalizationyprofile</code>. */
		
	private Insights insights;
	
	public Profile()
	{
		// default constructor
	}
	
		
	
	public void setInsights(final Insights insights)
	{
		this.insights = insights;
	}

		
	
	public Insights getInsights() 
	{
		return insights;
	}
	


}
