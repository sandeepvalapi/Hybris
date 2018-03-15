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
package de.hybris.platform.commerceservices.search.resultdata;

import java.io.Serializable;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import java.util.Collection;
import java.util.Map;

public  class SearchResultValueData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>SearchResultValueData.values</code> property defined at extension <code>commerceservices</code>. */
		
	private Map<String, Object> values;

	/** <i>Generated property</i> for <code>SearchResultValueData.featureValues</code> property defined at extension <code>commerceservices</code>. */
		
	private Map<ClassAttributeAssignmentModel, Object> featureValues;

	/** <i>Generated property</i> for <code>SearchResultValueData.variants</code> property defined at extension <code>commerceservices</code>. */
		
	private Collection<SearchResultValueData> variants;
	
	public SearchResultValueData()
	{
		// default constructor
	}
	
		
	
	public void setValues(final Map<String, Object> values)
	{
		this.values = values;
	}

		
	
	public Map<String, Object> getValues() 
	{
		return values;
	}
	
		
	
	public void setFeatureValues(final Map<ClassAttributeAssignmentModel, Object> featureValues)
	{
		this.featureValues = featureValues;
	}

		
	
	public Map<ClassAttributeAssignmentModel, Object> getFeatureValues() 
	{
		return featureValues;
	}
	
		
	
	public void setVariants(final Collection<SearchResultValueData> variants)
	{
		this.variants = variants;
	}

		
	
	public Collection<SearchResultValueData> getVariants() 
	{
		return variants;
	}
	


}
