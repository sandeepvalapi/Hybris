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
package de.hybris.platform.core.model.web;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type StoredHttpSession first defined at extension core.
 */
@SuppressWarnings("all")
public class StoredHttpSessionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "StoredHttpSession";
	
	/** <i>Generated constant</i> - Attribute key of <code>StoredHttpSession.sessionId</code> attribute defined at extension <code>core</code>. */
	public static final String SESSIONID = "sessionId";
	
	/** <i>Generated constant</i> - Attribute key of <code>StoredHttpSession.clusterId</code> attribute defined at extension <code>core</code>. */
	public static final String CLUSTERID = "clusterId";
	
	/** <i>Generated constant</i> - Attribute key of <code>StoredHttpSession.extension</code> attribute defined at extension <code>core</code>. */
	public static final String EXTENSION = "extension";
	
	/** <i>Generated constant</i> - Attribute key of <code>StoredHttpSession.contextRoot</code> attribute defined at extension <code>core</code>. */
	public static final String CONTEXTROOT = "contextRoot";
	
	/** <i>Generated constant</i> - Attribute key of <code>StoredHttpSession.serializedSession</code> attribute defined at extension <code>core</code>. */
	public static final String SERIALIZEDSESSION = "serializedSession";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public StoredHttpSessionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public StoredHttpSessionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _clusterId initial attribute declared by type <code>StoredHttpSession</code> at extension <code>core</code>
	 * @param _sessionId initial attribute declared by type <code>StoredHttpSession</code> at extension <code>core</code>
	 */
	@Deprecated
	public StoredHttpSessionModel(final Integer _clusterId, final String _sessionId)
	{
		super();
		setClusterId(_clusterId);
		setSessionId(_sessionId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _clusterId initial attribute declared by type <code>StoredHttpSession</code> at extension <code>core</code>
	 * @param _contextRoot initial attribute declared by type <code>StoredHttpSession</code> at extension <code>core</code>
	 * @param _extension initial attribute declared by type <code>StoredHttpSession</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sessionId initial attribute declared by type <code>StoredHttpSession</code> at extension <code>core</code>
	 */
	@Deprecated
	public StoredHttpSessionModel(final Integer _clusterId, final String _contextRoot, final String _extension, final ItemModel _owner, final String _sessionId)
	{
		super();
		setClusterId(_clusterId);
		setContextRoot(_contextRoot);
		setExtension(_extension);
		setOwner(_owner);
		setSessionId(_sessionId);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoredHttpSession.clusterId</code> attribute defined at extension <code>core</code>. 
	 * @return the clusterId
	 */
	@Accessor(qualifier = "clusterId", type = Accessor.Type.GETTER)
	public Integer getClusterId()
	{
		return getPersistenceContext().getPropertyValue(CLUSTERID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoredHttpSession.contextRoot</code> attribute defined at extension <code>core</code>. 
	 * @return the contextRoot
	 */
	@Accessor(qualifier = "contextRoot", type = Accessor.Type.GETTER)
	public String getContextRoot()
	{
		return getPersistenceContext().getPropertyValue(CONTEXTROOT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoredHttpSession.extension</code> attribute defined at extension <code>core</code>. 
	 * @return the extension
	 */
	@Accessor(qualifier = "extension", type = Accessor.Type.GETTER)
	public String getExtension()
	{
		return getPersistenceContext().getPropertyValue(EXTENSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoredHttpSession.serializedSession</code> attribute defined at extension <code>core</code>. 
	 * @return the serializedSession
	 */
	@Accessor(qualifier = "serializedSession", type = Accessor.Type.GETTER)
	public Object getSerializedSession()
	{
		return getPersistenceContext().getPropertyValue(SERIALIZEDSESSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoredHttpSession.sessionId</code> attribute defined at extension <code>core</code>. 
	 * @return the sessionId
	 */
	@Accessor(qualifier = "sessionId", type = Accessor.Type.GETTER)
	public String getSessionId()
	{
		return getPersistenceContext().getPropertyValue(SESSIONID);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>StoredHttpSession.clusterId</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the clusterId
	 */
	@Accessor(qualifier = "clusterId", type = Accessor.Type.SETTER)
	public void setClusterId(final Integer value)
	{
		getPersistenceContext().setPropertyValue(CLUSTERID, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>StoredHttpSession.contextRoot</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the contextRoot
	 */
	@Accessor(qualifier = "contextRoot", type = Accessor.Type.SETTER)
	public void setContextRoot(final String value)
	{
		getPersistenceContext().setPropertyValue(CONTEXTROOT, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>StoredHttpSession.extension</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the extension
	 */
	@Accessor(qualifier = "extension", type = Accessor.Type.SETTER)
	public void setExtension(final String value)
	{
		getPersistenceContext().setPropertyValue(EXTENSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StoredHttpSession.serializedSession</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the serializedSession
	 */
	@Accessor(qualifier = "serializedSession", type = Accessor.Type.SETTER)
	public void setSerializedSession(final Object value)
	{
		getPersistenceContext().setPropertyValue(SERIALIZEDSESSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>StoredHttpSession.sessionId</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sessionId
	 */
	@Accessor(qualifier = "sessionId", type = Accessor.Type.SETTER)
	public void setSessionId(final String value)
	{
		getPersistenceContext().setPropertyValue(SESSIONID, value);
	}
	
}
