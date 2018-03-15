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
package de.hybris.platform.cockpit.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cockpit.model.CockpitSavedFacetValueModel;
import de.hybris.platform.cockpit.model.CockpitSavedParameterValueModel;
import de.hybris.platform.cockpit.model.CockpitSavedSortCriterionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type CockpitSavedQuery first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class CockpitSavedQueryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CockpitSavedQuery";
	
	/**<i>Generated relation code constant for relation <code>User2CockpitSavedQueryRelation</code> defining source attribute <code>user</code> in extension <code>cockpit</code>.</i>*/
	public static final String _USER2COCKPITSAVEDQUERYRELATION = "User2CockpitSavedQueryRelation";
	
	/**<i>Generated relation code constant for relation <code>ReadPrincipal2CockpitSavedQueryRelation</code> defining source attribute <code>readSavedQueryPrincipals</code> in extension <code>cockpit</code>.</i>*/
	public static final String _READPRINCIPAL2COCKPITSAVEDQUERYRELATION = "ReadPrincipal2CockpitSavedQueryRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedQuery.label</code> attribute defined at extension <code>cockpit</code>. */
	public static final String LABEL = "label";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedQuery.description</code> attribute defined at extension <code>cockpit</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedQuery.simpleText</code> attribute defined at extension <code>cockpit</code>. */
	public static final String SIMPLETEXT = "simpleText";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedQuery.selectedTypeCode</code> attribute defined at extension <code>cockpit</code>. */
	public static final String SELECTEDTYPECODE = "selectedTypeCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedQuery.selectedTemplateCode</code> attribute defined at extension <code>cockpit</code>. */
	public static final String SELECTEDTEMPLATECODE = "selectedTemplateCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedQuery.code</code> attribute defined at extension <code>cockpit</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedQuery.defaultViewMode</code> attribute defined at extension <code>cockpit</code>. */
	public static final String DEFAULTVIEWMODE = "defaultViewMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedQuery.user</code> attribute defined at extension <code>cockpit</code>. */
	public static final String USER = "user";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedQuery.cockpitSavedFacetValues</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COCKPITSAVEDFACETVALUES = "cockpitSavedFacetValues";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedQuery.cockpitSavedSortCriteria</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COCKPITSAVEDSORTCRITERIA = "cockpitSavedSortCriteria";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedQuery.cockpitSavedParameterValues</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COCKPITSAVEDPARAMETERVALUES = "cockpitSavedParameterValues";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedQuery.readSavedQueryPrincipals</code> attribute defined at extension <code>cockpit</code>. */
	public static final String READSAVEDQUERYPRINCIPALS = "readSavedQueryPrincipals";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CockpitSavedQueryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CockpitSavedQueryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CockpitSavedQuery</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CockpitSavedQueryModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CockpitSavedQuery</code> at extension <code>cockpit</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CockpitSavedQueryModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedQuery.cockpitSavedFacetValues</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the cockpitSavedFacetValues
	 */
	@Accessor(qualifier = "cockpitSavedFacetValues", type = Accessor.Type.GETTER)
	public Collection<CockpitSavedFacetValueModel> getCockpitSavedFacetValues()
	{
		return getPersistenceContext().getPropertyValue(COCKPITSAVEDFACETVALUES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedQuery.cockpitSavedParameterValues</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the cockpitSavedParameterValues
	 */
	@Accessor(qualifier = "cockpitSavedParameterValues", type = Accessor.Type.GETTER)
	public Collection<CockpitSavedParameterValueModel> getCockpitSavedParameterValues()
	{
		return getPersistenceContext().getPropertyValue(COCKPITSAVEDPARAMETERVALUES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedQuery.cockpitSavedSortCriteria</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the cockpitSavedSortCriteria
	 */
	@Accessor(qualifier = "cockpitSavedSortCriteria", type = Accessor.Type.GETTER)
	public Collection<CockpitSavedSortCriterionModel> getCockpitSavedSortCriteria()
	{
		return getPersistenceContext().getPropertyValue(COCKPITSAVEDSORTCRITERIA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedQuery.code</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the code - unique identifier of the Cockpit Saved Query; will be generated if not set
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedQuery.defaultViewMode</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the defaultViewMode
	 */
	@Accessor(qualifier = "defaultViewMode", type = Accessor.Type.GETTER)
	public String getDefaultViewMode()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTVIEWMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedQuery.description</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedQuery.description</code> attribute defined at extension <code>cockpit</code>. 
	 * @param loc the value localization key 
	 * @return the description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedQuery.label</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the label
	 */
	@Accessor(qualifier = "label", type = Accessor.Type.GETTER)
	public String getLabel()
	{
		return getPersistenceContext().getPropertyValue(LABEL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedQuery.readSavedQueryPrincipals</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the readSavedQueryPrincipals
	 */
	@Accessor(qualifier = "readSavedQueryPrincipals", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getReadSavedQueryPrincipals()
	{
		return getPersistenceContext().getPropertyValue(READSAVEDQUERYPRINCIPALS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedQuery.selectedTemplateCode</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the selectedTemplateCode
	 */
	@Accessor(qualifier = "selectedTemplateCode", type = Accessor.Type.GETTER)
	public String getSelectedTemplateCode()
	{
		return getPersistenceContext().getPropertyValue(SELECTEDTEMPLATECODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedQuery.selectedTypeCode</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the selectedTypeCode
	 */
	@Accessor(qualifier = "selectedTypeCode", type = Accessor.Type.GETTER)
	public String getSelectedTypeCode()
	{
		return getPersistenceContext().getPropertyValue(SELECTEDTYPECODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedQuery.simpleText</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the simpleText
	 */
	@Accessor(qualifier = "simpleText", type = Accessor.Type.GETTER)
	public String getSimpleText()
	{
		return getPersistenceContext().getPropertyValue(SIMPLETEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedQuery.user</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedQuery.cockpitSavedFacetValues</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the cockpitSavedFacetValues
	 */
	@Accessor(qualifier = "cockpitSavedFacetValues", type = Accessor.Type.SETTER)
	public void setCockpitSavedFacetValues(final Collection<CockpitSavedFacetValueModel> value)
	{
		getPersistenceContext().setPropertyValue(COCKPITSAVEDFACETVALUES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedQuery.cockpitSavedParameterValues</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the cockpitSavedParameterValues
	 */
	@Accessor(qualifier = "cockpitSavedParameterValues", type = Accessor.Type.SETTER)
	public void setCockpitSavedParameterValues(final Collection<CockpitSavedParameterValueModel> value)
	{
		getPersistenceContext().setPropertyValue(COCKPITSAVEDPARAMETERVALUES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedQuery.cockpitSavedSortCriteria</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the cockpitSavedSortCriteria
	 */
	@Accessor(qualifier = "cockpitSavedSortCriteria", type = Accessor.Type.SETTER)
	public void setCockpitSavedSortCriteria(final Collection<CockpitSavedSortCriterionModel> value)
	{
		getPersistenceContext().setPropertyValue(COCKPITSAVEDSORTCRITERIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedQuery.code</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the code - unique identifier of the Cockpit Saved Query; will be generated if not set
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedQuery.defaultViewMode</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the defaultViewMode
	 */
	@Accessor(qualifier = "defaultViewMode", type = Accessor.Type.SETTER)
	public void setDefaultViewMode(final String value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTVIEWMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedQuery.description</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedQuery.description</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedQuery.label</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the label
	 */
	@Accessor(qualifier = "label", type = Accessor.Type.SETTER)
	public void setLabel(final String value)
	{
		getPersistenceContext().setPropertyValue(LABEL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedQuery.readSavedQueryPrincipals</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the readSavedQueryPrincipals
	 */
	@Accessor(qualifier = "readSavedQueryPrincipals", type = Accessor.Type.SETTER)
	public void setReadSavedQueryPrincipals(final Collection<PrincipalModel> value)
	{
		getPersistenceContext().setPropertyValue(READSAVEDQUERYPRINCIPALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedQuery.selectedTemplateCode</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the selectedTemplateCode
	 */
	@Accessor(qualifier = "selectedTemplateCode", type = Accessor.Type.SETTER)
	public void setSelectedTemplateCode(final String value)
	{
		getPersistenceContext().setPropertyValue(SELECTEDTEMPLATECODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedQuery.selectedTypeCode</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the selectedTypeCode
	 */
	@Accessor(qualifier = "selectedTypeCode", type = Accessor.Type.SETTER)
	public void setSelectedTypeCode(final String value)
	{
		getPersistenceContext().setPropertyValue(SELECTEDTYPECODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedQuery.simpleText</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the simpleText
	 */
	@Accessor(qualifier = "simpleText", type = Accessor.Type.SETTER)
	public void setSimpleText(final String value)
	{
		getPersistenceContext().setPropertyValue(SIMPLETEXT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedQuery.user</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
}
