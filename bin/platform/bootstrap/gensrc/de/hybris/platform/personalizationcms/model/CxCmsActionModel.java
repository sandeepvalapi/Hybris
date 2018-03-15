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
package de.hybris.platform.personalizationcms.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CxCmsAction first defined at extension personalizationcms.
 */
@SuppressWarnings("all")
public class CxCmsActionModel extends CxAbstractActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CxCmsAction";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCmsAction.componentId</code> attribute defined at extension <code>personalizationcms</code>. */
	public static final String COMPONENTID = "componentId";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCmsAction.componentCatalog</code> attribute defined at extension <code>personalizationcms</code>. */
	public static final String COMPONENTCATALOG = "componentCatalog";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxCmsAction.containerId</code> attribute defined at extension <code>personalizationcms</code>. */
	public static final String CONTAINERID = "containerId";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CxCmsActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CxCmsActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxAbstractAction</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 * @param _componentId initial attribute declared by type <code>CxCmsAction</code> at extension <code>personalizationcms</code>
	 * @param _containerId initial attribute declared by type <code>CxCmsAction</code> at extension <code>personalizationcms</code>
	 * @param _target initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 * @param _type initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CxCmsActionModel(final CatalogVersionModel _catalogVersion, final String _code, final String _componentId, final String _containerId, final String _target, final ActionType _type)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setComponentId(_componentId);
		setContainerId(_containerId);
		setTarget(_target);
		setType(_type);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxAbstractAction</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 * @param _componentId initial attribute declared by type <code>CxCmsAction</code> at extension <code>personalizationcms</code>
	 * @param _containerId initial attribute declared by type <code>CxCmsAction</code> at extension <code>personalizationcms</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _target initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 * @param _type initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CxCmsActionModel(final CatalogVersionModel _catalogVersion, final String _code, final String _componentId, final String _containerId, final ItemModel _owner, final String _target, final ActionType _type)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setComponentId(_componentId);
		setContainerId(_containerId);
		setOwner(_owner);
		setTarget(_target);
		setType(_type);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxCmsAction.componentCatalog</code> attribute defined at extension <code>personalizationcms</code>. 
	 * @return the componentCatalog - Targeted component catalog
	 */
	@Accessor(qualifier = "componentCatalog", type = Accessor.Type.GETTER)
	public String getComponentCatalog()
	{
		return getPersistenceContext().getPropertyValue(COMPONENTCATALOG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxCmsAction.componentId</code> attribute defined at extension <code>personalizationcms</code>. 
	 * @return the componentId - Targeted component id
	 */
	@Accessor(qualifier = "componentId", type = Accessor.Type.GETTER)
	public String getComponentId()
	{
		return getPersistenceContext().getPropertyValue(COMPONENTID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxCmsAction.containerId</code> attribute defined at extension <code>personalizationcms</code>. 
	 * @return the containerId - Targeted container id
	 */
	@Accessor(qualifier = "containerId", type = Accessor.Type.GETTER)
	public String getContainerId()
	{
		return getPersistenceContext().getPropertyValue(CONTAINERID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxCmsAction.componentCatalog</code> attribute defined at extension <code>personalizationcms</code>. 
	 *  
	 * @param value the componentCatalog - Targeted component catalog
	 */
	@Accessor(qualifier = "componentCatalog", type = Accessor.Type.SETTER)
	public void setComponentCatalog(final String value)
	{
		getPersistenceContext().setPropertyValue(COMPONENTCATALOG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxCmsAction.componentId</code> attribute defined at extension <code>personalizationcms</code>. 
	 *  
	 * @param value the componentId - Targeted component id
	 */
	@Accessor(qualifier = "componentId", type = Accessor.Type.SETTER)
	public void setComponentId(final String value)
	{
		getPersistenceContext().setPropertyValue(COMPONENTID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxCmsAction.containerId</code> attribute defined at extension <code>personalizationcms</code>. 
	 *  
	 * @param value the containerId - Targeted container id
	 */
	@Accessor(qualifier = "containerId", type = Accessor.Type.SETTER)
	public void setContainerId(final String value)
	{
		getPersistenceContext().setPropertyValue(CONTAINERID, value);
	}
	
}
