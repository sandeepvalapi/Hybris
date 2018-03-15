/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Mar 15, 2018 5:02:29 PM                     ---
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
 *  
 */
package de.hybris.platform.ticketsystem.events.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.List;

/**
 * Generated model class for type SessionEvent first defined at extension ticketsystem.
 */
@SuppressWarnings("all")
public class SessionEventModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SessionEvent";
	
	/** <i>Generated constant</i> - Attribute key of <code>SessionEvent.eventTime</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String EVENTTIME = "eventTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>SessionEvent.agent</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String AGENT = "agent";
	
	/** <i>Generated constant</i> - Attribute key of <code>SessionEvent.sessionID</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String SESSIONID = "sessionID";
	
	/** <i>Generated constant</i> - Attribute key of <code>SessionEvent.baseSite</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String BASESITE = "baseSite";
	
	/** <i>Generated constant</i> - Attribute key of <code>SessionEvent.groups</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String GROUPS = "groups";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SessionEventModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SessionEventModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public SessionEventModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SessionEvent.agent</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the agent - Any employee.
	 */
	@Accessor(qualifier = "agent", type = Accessor.Type.GETTER)
	public EmployeeModel getAgent()
	{
		return getPersistenceContext().getPropertyValue(AGENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SessionEvent.baseSite</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the baseSite - Site model where event was created.
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.GETTER)
	public BaseSiteModel getBaseSite()
	{
		return getPersistenceContext().getPropertyValue(BASESITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SessionEvent.eventTime</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the eventTime - Event creation time.
	 */
	@Accessor(qualifier = "eventTime", type = Accessor.Type.GETTER)
	public Date getEventTime()
	{
		return getPersistenceContext().getPropertyValue(EVENTTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SessionEvent.groups</code> attribute defined at extension <code>ticketsystem</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the groups
	 */
	@Accessor(qualifier = "groups", type = Accessor.Type.GETTER)
	public List<PrincipalGroupModel> getGroups()
	{
		return getPersistenceContext().getPropertyValue(GROUPS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SessionEvent.sessionID</code> attribute defined at extension <code>ticketsystem</code>. 
	 * @return the sessionID - Unique identificator.
	 */
	@Accessor(qualifier = "sessionID", type = Accessor.Type.GETTER)
	public String getSessionID()
	{
		return getPersistenceContext().getPropertyValue(SESSIONID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SessionEvent.agent</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the agent - Any employee.
	 */
	@Accessor(qualifier = "agent", type = Accessor.Type.SETTER)
	public void setAgent(final EmployeeModel value)
	{
		getPersistenceContext().setPropertyValue(AGENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SessionEvent.baseSite</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the baseSite - Site model where event was created.
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.SETTER)
	public void setBaseSite(final BaseSiteModel value)
	{
		getPersistenceContext().setPropertyValue(BASESITE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SessionEvent.eventTime</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the eventTime - Event creation time.
	 */
	@Accessor(qualifier = "eventTime", type = Accessor.Type.SETTER)
	public void setEventTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(EVENTTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SessionEvent.groups</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the groups
	 */
	@Accessor(qualifier = "groups", type = Accessor.Type.SETTER)
	public void setGroups(final List<PrincipalGroupModel> value)
	{
		getPersistenceContext().setPropertyValue(GROUPS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SessionEvent.sessionID</code> attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the sessionID - Unique identificator.
	 */
	@Accessor(qualifier = "sessionID", type = Accessor.Type.SETTER)
	public void setSessionID(final String value)
	{
		getPersistenceContext().setPropertyValue(SESSIONID, value);
	}
	
}
