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
package de.hybris.eventtracking.model.events;

import java.io.Serializable;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

public  class AbstractTrackingEvent  extends AbstractEvent {


	/** <i>Generated property</i> for <code>AbstractTrackingEvent.eventType</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String eventType;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.pageUrl</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String pageUrl;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.interactionTimestamp</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String interactionTimestamp;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.userId</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String userId;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.userEmail</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String userEmail;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.sessionId</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String sessionId;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.piwikId</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String piwikId;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.refUrl</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String refUrl;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.idsite</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String idsite;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.cvar</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String cvar;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.res</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String res;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.data</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String data;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.search_cat</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String search_cat;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.search_count</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String search_count;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.ec_id</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String ec_id;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.ec_st</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String ec_st;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.ec_tx</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String ec_tx;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.ec_dt</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String ec_dt;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.ec_items</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String ec_items;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.baseSiteId</code> property defined at extension <code>eventtrackingmodel</code>. */
		
	private String baseSiteId;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.consentReference</code> property defined at extension <code>yprofileeventtrackingws</code>. */
		
	private String consentReference;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.userAgent</code> property defined at extension <code>yprofileeventtrackingws</code>. */
		
	private String userAgent;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.accept</code> property defined at extension <code>yprofileeventtrackingws</code>. */
		
	private String accept;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.acceptLanguage</code> property defined at extension <code>yprofileeventtrackingws</code>. */
		
	private String acceptLanguage;

	/** <i>Generated property</i> for <code>AbstractTrackingEvent.referer</code> property defined at extension <code>yprofileeventtrackingws</code>. */
		
	private String referer;
	
	public AbstractTrackingEvent()
	{
		super();
	}

	public AbstractTrackingEvent(final Serializable source)
	{
		super(source);
	}
	
	
	
	public void setEventType(final String eventType)
	{
		this.eventType = eventType;
	}
	
	
	
	public String getEventType() 
	{
		return eventType;
	}
	
	
	
	public void setPageUrl(final String pageUrl)
	{
		this.pageUrl = pageUrl;
	}
	
	
	
	public String getPageUrl() 
	{
		return pageUrl;
	}
	
	
	
	public void setInteractionTimestamp(final String interactionTimestamp)
	{
		this.interactionTimestamp = interactionTimestamp;
	}
	
	
	
	public String getInteractionTimestamp() 
	{
		return interactionTimestamp;
	}
	
	
	
	public void setUserId(final String userId)
	{
		this.userId = userId;
	}
	
	
	
	public String getUserId() 
	{
		return userId;
	}
	
	
	
	public void setUserEmail(final String userEmail)
	{
		this.userEmail = userEmail;
	}
	
	
	
	public String getUserEmail() 
	{
		return userEmail;
	}
	
	
	
	public void setSessionId(final String sessionId)
	{
		this.sessionId = sessionId;
	}
	
	
	
	public String getSessionId() 
	{
		return sessionId;
	}
	
	
	
	public void setPiwikId(final String piwikId)
	{
		this.piwikId = piwikId;
	}
	
	
	
	public String getPiwikId() 
	{
		return piwikId;
	}
	
	
	
	public void setRefUrl(final String refUrl)
	{
		this.refUrl = refUrl;
	}
	
	
	
	public String getRefUrl() 
	{
		return refUrl;
	}
	
	
	
	public void setIdsite(final String idsite)
	{
		this.idsite = idsite;
	}
	
	
	
	public String getIdsite() 
	{
		return idsite;
	}
	
	
	
	public void setCvar(final String cvar)
	{
		this.cvar = cvar;
	}
	
	
	
	public String getCvar() 
	{
		return cvar;
	}
	
	
	
	public void setRes(final String res)
	{
		this.res = res;
	}
	
	
	
	public String getRes() 
	{
		return res;
	}
	
	
	
	public void setData(final String data)
	{
		this.data = data;
	}
	
	
	
	public String getData() 
	{
		return data;
	}
	
	
	
	public void setSearch_cat(final String search_cat)
	{
		this.search_cat = search_cat;
	}
	
	
	
	public String getSearch_cat() 
	{
		return search_cat;
	}
	
	
	
	public void setSearch_count(final String search_count)
	{
		this.search_count = search_count;
	}
	
	
	
	public String getSearch_count() 
	{
		return search_count;
	}
	
	
	
	public void setEc_id(final String ec_id)
	{
		this.ec_id = ec_id;
	}
	
	
	
	public String getEc_id() 
	{
		return ec_id;
	}
	
	
	
	public void setEc_st(final String ec_st)
	{
		this.ec_st = ec_st;
	}
	
	
	
	public String getEc_st() 
	{
		return ec_st;
	}
	
	
	
	public void setEc_tx(final String ec_tx)
	{
		this.ec_tx = ec_tx;
	}
	
	
	
	public String getEc_tx() 
	{
		return ec_tx;
	}
	
	
	
	public void setEc_dt(final String ec_dt)
	{
		this.ec_dt = ec_dt;
	}
	
	
	
	public String getEc_dt() 
	{
		return ec_dt;
	}
	
	
	
	public void setEc_items(final String ec_items)
	{
		this.ec_items = ec_items;
	}
	
	
	
	public String getEc_items() 
	{
		return ec_items;
	}
	
	
	
	public void setBaseSiteId(final String baseSiteId)
	{
		this.baseSiteId = baseSiteId;
	}
	
	
	
	public String getBaseSiteId() 
	{
		return baseSiteId;
	}
	
	
	
	public void setConsentReference(final String consentReference)
	{
		this.consentReference = consentReference;
	}
	
	
	
	public String getConsentReference() 
	{
		return consentReference;
	}
	
	
	
	public void setUserAgent(final String userAgent)
	{
		this.userAgent = userAgent;
	}
	
	
	
	public String getUserAgent() 
	{
		return userAgent;
	}
	
	
	
	public void setAccept(final String accept)
	{
		this.accept = accept;
	}
	
	
	
	public String getAccept() 
	{
		return accept;
	}
	
	
	
	public void setAcceptLanguage(final String acceptLanguage)
	{
		this.acceptLanguage = acceptLanguage;
	}
	
	
	
	public String getAcceptLanguage() 
	{
		return acceptLanguage;
	}
	
	
	
	public void setReferer(final String referer)
	{
		this.referer = referer;
	}
	
	
	
	public String getReferer() 
	{
		return referer;
	}
	


}