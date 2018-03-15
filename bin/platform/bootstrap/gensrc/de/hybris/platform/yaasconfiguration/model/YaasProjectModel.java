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
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.yaasconfiguration.model.YaasClientCredentialModel;
import de.hybris.platform.yaasconfiguration.model.YaasOrganisationModel;
import java.util.Set;

/**
 * Generated model class for type YaasProject first defined at extension yaasconfiguration.
 */
@SuppressWarnings("all")
public class YaasProjectModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "YaasProject";
	
	/**<i>Generated relation code constant for relation <code>YaasOrganisationProjectRelation</code> defining source attribute <code>yaasOrganisation</code> in extension <code>yaasconfiguration</code>.</i>*/
	public static final String _YAASORGANISATIONPROJECTRELATION = "YaasOrganisationProjectRelation";
	
	/**<i>Generated relation code constant for relation <code>BaseSiteYaasProjectRelation</code> defining source attribute <code>baseSite</code> in extension <code>yaasconfiguration</code>.</i>*/
	public static final String _BASESITEYAASPROJECTRELATION = "BaseSiteYaasProjectRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasProject.identifier</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String IDENTIFIER = "identifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasProject.yaasOrganisation</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String YAASORGANISATION = "yaasOrganisation";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasProject.baseSitePOS</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String BASESITEPOS = "baseSitePOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasProject.baseSite</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String BASESITE = "baseSite";
	
	/** <i>Generated constant</i> - Attribute key of <code>YaasProject.yaasClientCredentials</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String YAASCLIENTCREDENTIALS = "yaasClientCredentials";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public YaasProjectModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public YaasProjectModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public YaasProjectModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YaasProject.baseSite</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * @return the baseSite
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.GETTER)
	public BaseSiteModel getBaseSite()
	{
		return getPersistenceContext().getPropertyValue(BASESITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YaasProject.identifier</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * @return the identifier
	 */
	@Accessor(qualifier = "identifier", type = Accessor.Type.GETTER)
	public String getIdentifier()
	{
		return getPersistenceContext().getPropertyValue(IDENTIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YaasProject.yaasClientCredentials</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the yaasClientCredentials
	 */
	@Accessor(qualifier = "yaasClientCredentials", type = Accessor.Type.GETTER)
	public Set<YaasClientCredentialModel> getYaasClientCredentials()
	{
		return getPersistenceContext().getPropertyValue(YAASCLIENTCREDENTIALS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YaasProject.yaasOrganisation</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * @return the yaasOrganisation
	 */
	@Accessor(qualifier = "yaasOrganisation", type = Accessor.Type.GETTER)
	public YaasOrganisationModel getYaasOrganisation()
	{
		return getPersistenceContext().getPropertyValue(YAASORGANISATION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YaasProject.baseSite</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the baseSite
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.SETTER)
	public void setBaseSite(final BaseSiteModel value)
	{
		getPersistenceContext().setPropertyValue(BASESITE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YaasProject.identifier</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the identifier
	 */
	@Accessor(qualifier = "identifier", type = Accessor.Type.SETTER)
	public void setIdentifier(final String value)
	{
		getPersistenceContext().setPropertyValue(IDENTIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YaasProject.yaasClientCredentials</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the yaasClientCredentials
	 */
	@Accessor(qualifier = "yaasClientCredentials", type = Accessor.Type.SETTER)
	public void setYaasClientCredentials(final Set<YaasClientCredentialModel> value)
	{
		getPersistenceContext().setPropertyValue(YAASCLIENTCREDENTIALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YaasProject.yaasOrganisation</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the yaasOrganisation
	 */
	@Accessor(qualifier = "yaasOrganisation", type = Accessor.Type.SETTER)
	public void setYaasOrganisation(final YaasOrganisationModel value)
	{
		getPersistenceContext().setPropertyValue(YAASORGANISATION, value);
	}
	
}
