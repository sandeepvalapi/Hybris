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
package de.hybris.platform.webservicescommons.dto.error;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Error message
 */
@ApiModel(value="error", description="Error message")
public  class ErrorWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** Type of the error e.g. 'LowStockError'.<br/><br/><i>Generated property</i> for <code>ErrorWsDTO.type</code> property defined at extension <code>webservicescommons</code>. */
	@ApiModelProperty(name="type", value="Type of the error e.g. 'LowStockError'.") 	
	private String type;

	/** Additional classification specific for each error type e.g. 'noStock'.<br/><br/><i>Generated property</i> for <code>ErrorWsDTO.reason</code> property defined at extension <code>webservicescommons</code>. */
	@ApiModelProperty(name="reason", value="Additional classification specific for each error type e.g. 'noStock'.") 	
	private String reason;

	/** Descriptive, human readable error message.<br/><br/><i>Generated property</i> for <code>ErrorWsDTO.message</code> property defined at extension <code>webservicescommons</code>. */
	@ApiModelProperty(name="message", value="Descriptive, human readable error message.") 	
	private String message;

	/** Type of the object related to the error e.g. 'entry'.<br/><br/><i>Generated property</i> for <code>ErrorWsDTO.subjectType</code> property defined at extension <code>webservicescommons</code>. */
	@ApiModelProperty(name="subjectType", value="Type of the object related to the error e.g. 'entry'.") 	
	private String subjectType;

	/** Identifier of the related object e.g. '1'.<br/><br/><i>Generated property</i> for <code>ErrorWsDTO.subject</code> property defined at extension <code>webservicescommons</code>. */
	@ApiModelProperty(name="subject", value="Identifier of the related object e.g. '1'.") 	
	private String subject;

	/** <i>Generated property</i> for <code>ErrorWsDTO.language</code> property defined at extension <code>cmswebservices</code>. */
	@ApiModelProperty(name="language") 	
	private String language;

	/** <i>Generated property</i> for <code>ErrorWsDTO.position</code> property defined at extension <code>cmswebservices</code>. */
	@ApiModelProperty(name="position") 	
	private Integer position;

	/** <i>Generated property</i> for <code>ErrorWsDTO.exceptionMessage</code> property defined at extension <code>cmswebservices</code>. */
	@ApiModelProperty(name="exceptionMessage") 	
	private String exceptionMessage;
	
	public ErrorWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setType(final String type)
	{
		this.type = type;
	}

		
	
	public String getType() 
	{
		return type;
	}
	
		
	
	public void setReason(final String reason)
	{
		this.reason = reason;
	}

		
	
	public String getReason() 
	{
		return reason;
	}
	
		
	
	public void setMessage(final String message)
	{
		this.message = message;
	}

		
	
	public String getMessage() 
	{
		return message;
	}
	
		
	
	public void setSubjectType(final String subjectType)
	{
		this.subjectType = subjectType;
	}

		
	
	public String getSubjectType() 
	{
		return subjectType;
	}
	
		
	
	public void setSubject(final String subject)
	{
		this.subject = subject;
	}

		
	
	public String getSubject() 
	{
		return subject;
	}
	
		
	
	public void setLanguage(final String language)
	{
		this.language = language;
	}

		
	
	public String getLanguage() 
	{
		return language;
	}
	
		
	
	public void setPosition(final Integer position)
	{
		this.position = position;
	}

		
	
	public Integer getPosition() 
	{
		return position;
	}
	
		
	
	public void setExceptionMessage(final String exceptionMessage)
	{
		this.exceptionMessage = exceptionMessage;
	}

		
	
	public String getExceptionMessage() 
	{
		return exceptionMessage;
	}
	


}
