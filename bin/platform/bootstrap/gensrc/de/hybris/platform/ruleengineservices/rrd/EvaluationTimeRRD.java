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
package de.hybris.platform.ruleengineservices.rrd;

import java.io.Serializable;

/**
 * Represents rule evaluation time (gets inserted as one fact per rule). Encapsulates milliseconds, between the evaluation time and midnight, January 1, 1970 UTC
 */
public  class EvaluationTimeRRD  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>EvaluationTimeRRD.evaluationTime</code> property defined at extension <code>ruleengineservices</code>. */
		
	private Long evaluationTime;
	
	public EvaluationTimeRRD()
	{
		// default constructor
	}
	
		
	
	public void setEvaluationTime(final Long evaluationTime)
	{
		this.evaluationTime = evaluationTime;
	}

		
	
	public Long getEvaluationTime() 
	{
		return evaluationTime;
	}
	


}
