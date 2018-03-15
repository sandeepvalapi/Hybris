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
package de.hybris.platform.personalizationyprofile.yaas;

import java.io.Serializable;
import java.math.BigDecimal;

public  class Affinity  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>Affinity.score</code> property defined at extension <code>personalizationyprofile</code>. */
		
	private BigDecimal score;

	/** <i>Generated property</i> for <code>Affinity.recentViewCount</code> property defined at extension <code>personalizationyprofile</code>. */
		
	private Integer recentViewCount;

	/** <i>Generated property</i> for <code>Affinity.recentScore</code> property defined at extension <code>personalizationyprofile</code>. */
		
	private BigDecimal recentScore;
	
	public Affinity()
	{
		// default constructor
	}
	
		
	
	public void setScore(final BigDecimal score)
	{
		this.score = score;
	}

		
	
	public BigDecimal getScore() 
	{
		return score;
	}
	
		
	
	public void setRecentViewCount(final Integer recentViewCount)
	{
		this.recentViewCount = recentViewCount;
	}

		
	
	public Integer getRecentViewCount() 
	{
		return recentViewCount;
	}
	
		
	
	public void setRecentScore(final BigDecimal recentScore)
	{
		this.recentScore = recentScore;
	}

		
	
	public BigDecimal getRecentScore() 
	{
		return recentScore;
	}
	


}
