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
package de.hybris.platform.personalizationyprofile.yaas;

import java.io.Serializable;
import de.hybris.platform.personalizationyprofile.yaas.Affinity;
import java.util.Map;

public  class Affinities  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>Affinities.categories</code> property defined at extension <code>personalizationyprofile</code>. */
		
	private Map<String,Affinity> categories;

	/** <i>Generated property</i> for <code>Affinities.products</code> property defined at extension <code>personalizationyprofile</code>. */
		
	private Map<String,Affinity> products;
	
	public Affinities()
	{
		// default constructor
	}
	
		
	
	public void setCategories(final Map<String,Affinity> categories)
	{
		this.categories = categories;
	}

		
	
	public Map<String,Affinity> getCategories() 
	{
		return categories;
	}
	
		
	
	public void setProducts(final Map<String,Affinity> products)
	{
		this.products = products;
	}

		
	
	public Map<String,Affinity> getProducts() 
	{
		return products;
	}
	


}
