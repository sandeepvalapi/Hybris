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
package de.hybris.platform.personalizationservices.model.process;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type CxPersonalizationProcess first defined at extension personalizationservices.
 * <p>
 * Personalization calculation process.
 */
@SuppressWarnings("all")
public class CxPersonalizationProcessModel extends BusinessProcessModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CxPersonalizationProcess";
	
	/**<i>Generated relation code constant for relation <code>CxPersProcToCatVer</code> defining source attribute <code>catalogVersions</code> in extension <code>personalizationservices</code>.</i>*/
	public static final String _CXPERSPROCTOCATVER = "CxPersProcToCatVer";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxPersonalizationProcess.key</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String KEY = "key";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxPersonalizationProcess.catalogVersions</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String CATALOGVERSIONS = "catalogVersions";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CxPersonalizationProcessModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CxPersonalizationProcessModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 * @param _processDefinitionName initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CxPersonalizationProcessModel(final String _code, final String _processDefinitionName)
	{
		super();
		setCode(_code);
		setProcessDefinitionName(_processDefinitionName);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _processDefinitionName initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CxPersonalizationProcessModel(final String _code, final ItemModel _owner, final String _processDefinitionName)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setProcessDefinitionName(_processDefinitionName);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxPersonalizationProcess.catalogVersions</code> attribute defined at extension <code>personalizationservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the catalogVersions
	 */
	@Accessor(qualifier = "catalogVersions", type = Accessor.Type.GETTER)
	public Collection<CatalogVersionModel> getCatalogVersions()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxPersonalizationProcess.key</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the key
	 */
	@Accessor(qualifier = "key", type = Accessor.Type.GETTER)
	public String getKey()
	{
		return getPersistenceContext().getPropertyValue(KEY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxPersonalizationProcess.catalogVersions</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the catalogVersions
	 */
	@Accessor(qualifier = "catalogVersions", type = Accessor.Type.SETTER)
	public void setCatalogVersions(final Collection<CatalogVersionModel> value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxPersonalizationProcess.key</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the key
	 */
	@Accessor(qualifier = "key", type = Accessor.Type.SETTER)
	public void setKey(final String value)
	{
		getPersistenceContext().setPropertyValue(KEY, value);
	}
	
}
