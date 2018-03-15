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
package de.hybris.platform.yaasconfiguration.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.yaasconfiguration.model.YaasProjectModel;
import java.util.Set;

/**
 * Generated model class for type YaasOrganisation first defined at extension yaasconfiguration.
 */
@SuppressWarnings("all")
public class YaasOrganisationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "YaasOrganisation";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasOrganisation.identifier</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String IDENTIFIER = "identifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasOrganisation.basePath</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String BASEPATH = "basePath";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasOrganisation.yaasProjects</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String YAASPROJECTS = "yaasProjects";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public YaasOrganisationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public YaasOrganisationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public YaasOrganisationModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YaasOrganisation.basePath</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * @return the basePath
	 */
	@Accessor(qualifier = "basePath", type = Accessor.Type.GETTER)
	public String getBasePath()
	{
		return getPersistenceContext().getPropertyValue(BASEPATH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YaasOrganisation.identifier</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * @return the identifier
	 */
	@Accessor(qualifier = "identifier", type = Accessor.Type.GETTER)
	public String getIdentifier()
	{
		return getPersistenceContext().getPropertyValue(IDENTIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YaasOrganisation.yaasProjects</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the yaasProjects
	 */
	@Accessor(qualifier = "yaasProjects", type = Accessor.Type.GETTER)
	public Set<YaasProjectModel> getYaasProjects()
	{
		return getPersistenceContext().getPropertyValue(YAASPROJECTS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YaasOrganisation.basePath</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the basePath
	 */
	@Accessor(qualifier = "basePath", type = Accessor.Type.SETTER)
	public void setBasePath(final String value)
	{
		getPersistenceContext().setPropertyValue(BASEPATH, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YaasOrganisation.identifier</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the identifier
	 */
	@Accessor(qualifier = "identifier", type = Accessor.Type.SETTER)
	public void setIdentifier(final String value)
	{
		getPersistenceContext().setPropertyValue(IDENTIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YaasOrganisation.yaasProjects</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the yaasProjects
	 */
	@Accessor(qualifier = "yaasProjects", type = Accessor.Type.SETTER)
	public void setYaasProjects(final Set<YaasProjectModel> value)
	{
		getPersistenceContext().setPropertyValue(YAASPROJECTS, value);
	}
	
}
