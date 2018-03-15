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
package de.hybris.platform.assistedservicestorefront.customer360;

import java.io.Serializable;
import java.util.Date;

public  class ReviewData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ReviewData.productName</code> property defined at extension <code>assistedservicestorefront</code>. */
		
	private String productName;

	/** <i>Generated property</i> for <code>ReviewData.SKUNumber</code> property defined at extension <code>assistedservicestorefront</code>. */
		
	private String SKUNumber;

	/** <i>Generated property</i> for <code>ReviewData.created</code> property defined at extension <code>assistedservicestorefront</code>. */
		
	private Date created;

	/** <i>Generated property</i> for <code>ReviewData.updated</code> property defined at extension <code>assistedservicestorefront</code>. */
		
	private Date updated;

	/** <i>Generated property</i> for <code>ReviewData.rating</code> property defined at extension <code>assistedservicestorefront</code>. */
		
	private Double rating;

	/** <i>Generated property</i> for <code>ReviewData.reviewStatus</code> property defined at extension <code>assistedservicestorefront</code>. */
		
	private String reviewStatus;

	/** <i>Generated property</i> for <code>ReviewData.reviewText</code> property defined at extension <code>assistedservicestorefront</code>. */
		
	private String reviewText;

	/** <i>Generated property</i> for <code>ReviewData.productUrl</code> property defined at extension <code>assistedservicestorefront</code>. */
		
	private String productUrl;
	
	public ReviewData()
	{
		// default constructor
	}
	
		
	
	public void setProductName(final String productName)
	{
		this.productName = productName;
	}

		
	
	public String getProductName() 
	{
		return productName;
	}
	
		
	
	public void setSKUNumber(final String SKUNumber)
	{
		this.SKUNumber = SKUNumber;
	}

		
	
	public String getSKUNumber() 
	{
		return SKUNumber;
	}
	
		
	
	public void setCreated(final Date created)
	{
		this.created = created;
	}

		
	
	public Date getCreated() 
	{
		return created;
	}
	
		
	
	public void setUpdated(final Date updated)
	{
		this.updated = updated;
	}

		
	
	public Date getUpdated() 
	{
		return updated;
	}
	
		
	
	public void setRating(final Double rating)
	{
		this.rating = rating;
	}

		
	
	public Double getRating() 
	{
		return rating;
	}
	
		
	
	public void setReviewStatus(final String reviewStatus)
	{
		this.reviewStatus = reviewStatus;
	}

		
	
	public String getReviewStatus() 
	{
		return reviewStatus;
	}
	
		
	
	public void setReviewText(final String reviewText)
	{
		this.reviewText = reviewText;
	}

		
	
	public String getReviewText() 
	{
		return reviewText;
	}
	
		
	
	public void setProductUrl(final String productUrl)
	{
		this.productUrl = productUrl;
	}

		
	
	public String getProductUrl() 
	{
		return productUrl;
	}
	


}
