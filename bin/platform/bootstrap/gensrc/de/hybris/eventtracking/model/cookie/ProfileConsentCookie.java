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
package de.hybris.eventtracking.model.cookie;

import java.io.Serializable;

public  class ProfileConsentCookie  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ProfileConsentCookie.templateCode</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String templateCode;

	/** <i>Generated property</i> for <code>ProfileConsentCookie.templateVersion</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String templateVersion;

	/** <i>Generated property</i> for <code>ProfileConsentCookie.consentState</code> property defined at extension <code>yprofilecommons</code>. */
		
	private String consentState;
	
	public ProfileConsentCookie()
	{
		// default constructor
	}
	
		
	
	public void setTemplateCode(final String templateCode)
	{
		this.templateCode = templateCode;
	}

		
	
	public String getTemplateCode() 
	{
		return templateCode;
	}
	
		
	
	public void setTemplateVersion(final String templateVersion)
	{
		this.templateVersion = templateVersion;
	}

		
	
	public String getTemplateVersion() 
	{
		return templateVersion;
	}
	
		
	
	public void setConsentState(final String consentState)
	{
		this.consentState = consentState;
	}

		
	
	public String getConsentState() 
	{
		return consentState;
	}
	


}
