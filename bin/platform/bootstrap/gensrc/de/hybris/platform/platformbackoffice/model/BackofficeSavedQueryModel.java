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
package de.hybris.platform.platformbackoffice.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.platformbackoffice.model.BackofficeSearchConditionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type BackofficeSavedQuery first defined at extension platformbackoffice.
 */
@SuppressWarnings("all")
public class BackofficeSavedQueryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "BackofficeSavedQuery";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSavedQuery.name</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSavedQuery.queryOwner</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String QUERYOWNER = "queryOwner";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSavedQuery.typeCode</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String TYPECODE = "typeCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSavedQuery.includeSubtypes</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String INCLUDESUBTYPES = "includeSubtypes";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSavedQuery.globalOperatorCode</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String GLOBALOPERATORCODE = "globalOperatorCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSavedQuery.sortAttribute</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String SORTATTRIBUTE = "sortAttribute";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSavedQuery.sortAsc</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String SORTASC = "sortAsc";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSavedQuery.tokenizable</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String TOKENIZABLE = "tokenizable";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSavedQuery.searchMode</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String SEARCHMODE = "searchMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSavedQuery.conditions</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String CONDITIONS = "conditions";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSavedQuery.userGroups</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String USERGROUPS = "userGroups";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public BackofficeSavedQueryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public BackofficeSavedQueryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _queryOwner initial attribute declared by type <code>BackofficeSavedQuery</code> at extension <code>platformbackoffice</code>
	 * @param _typeCode initial attribute declared by type <code>BackofficeSavedQuery</code> at extension <code>platformbackoffice</code>
	 */
	@Deprecated
	public BackofficeSavedQueryModel(final UserModel _queryOwner, final String _typeCode)
	{
		super();
		setQueryOwner(_queryOwner);
		setTypeCode(_typeCode);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _queryOwner initial attribute declared by type <code>BackofficeSavedQuery</code> at extension <code>platformbackoffice</code>
	 * @param _typeCode initial attribute declared by type <code>BackofficeSavedQuery</code> at extension <code>platformbackoffice</code>
	 */
	@Deprecated
	public BackofficeSavedQueryModel(final ItemModel _owner, final UserModel _queryOwner, final String _typeCode)
	{
		super();
		setOwner(_owner);
		setQueryOwner(_queryOwner);
		setTypeCode(_typeCode);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSavedQuery.conditions</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the conditions
	 */
	@Accessor(qualifier = "conditions", type = Accessor.Type.GETTER)
	public Collection<BackofficeSearchConditionModel> getConditions()
	{
		return getPersistenceContext().getPropertyValue(CONDITIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSavedQuery.globalOperatorCode</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the globalOperatorCode
	 */
	@Accessor(qualifier = "globalOperatorCode", type = Accessor.Type.GETTER)
	public String getGlobalOperatorCode()
	{
		return getPersistenceContext().getPropertyValue(GLOBALOPERATORCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSavedQuery.includeSubtypes</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the includeSubtypes
	 */
	@Accessor(qualifier = "includeSubtypes", type = Accessor.Type.GETTER)
	public Boolean getIncludeSubtypes()
	{
		return getPersistenceContext().getPropertyValue(INCLUDESUBTYPES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSavedQuery.name</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSavedQuery.name</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @param loc the value localization key 
	 * @return the name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSavedQuery.queryOwner</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the queryOwner
	 */
	@Accessor(qualifier = "queryOwner", type = Accessor.Type.GETTER)
	public UserModel getQueryOwner()
	{
		return getPersistenceContext().getPropertyValue(QUERYOWNER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSavedQuery.searchMode</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the searchMode
	 */
	@Accessor(qualifier = "searchMode", type = Accessor.Type.GETTER)
	public String getSearchMode()
	{
		return getPersistenceContext().getPropertyValue(SEARCHMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSavedQuery.sortAsc</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the sortAsc
	 */
	@Accessor(qualifier = "sortAsc", type = Accessor.Type.GETTER)
	public Boolean getSortAsc()
	{
		return getPersistenceContext().getPropertyValue(SORTASC);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSavedQuery.sortAttribute</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the sortAttribute
	 */
	@Accessor(qualifier = "sortAttribute", type = Accessor.Type.GETTER)
	public String getSortAttribute()
	{
		return getPersistenceContext().getPropertyValue(SORTATTRIBUTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSavedQuery.tokenizable</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the tokenizable
	 */
	@Accessor(qualifier = "tokenizable", type = Accessor.Type.GETTER)
	public Boolean getTokenizable()
	{
		return getPersistenceContext().getPropertyValue(TOKENIZABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSavedQuery.typeCode</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the typeCode
	 */
	@Accessor(qualifier = "typeCode", type = Accessor.Type.GETTER)
	public String getTypeCode()
	{
		return getPersistenceContext().getPropertyValue(TYPECODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSavedQuery.userGroups</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the userGroups
	 */
	@Accessor(qualifier = "userGroups", type = Accessor.Type.GETTER)
	public Collection<UserGroupModel> getUserGroups()
	{
		return getPersistenceContext().getPropertyValue(USERGROUPS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSavedQuery.conditions</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the conditions
	 */
	@Accessor(qualifier = "conditions", type = Accessor.Type.SETTER)
	public void setConditions(final Collection<BackofficeSearchConditionModel> value)
	{
		getPersistenceContext().setPropertyValue(CONDITIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSavedQuery.globalOperatorCode</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the globalOperatorCode
	 */
	@Accessor(qualifier = "globalOperatorCode", type = Accessor.Type.SETTER)
	public void setGlobalOperatorCode(final String value)
	{
		getPersistenceContext().setPropertyValue(GLOBALOPERATORCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSavedQuery.includeSubtypes</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the includeSubtypes
	 */
	@Accessor(qualifier = "includeSubtypes", type = Accessor.Type.SETTER)
	public void setIncludeSubtypes(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(INCLUDESUBTYPES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSavedQuery.name</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSavedQuery.name</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSavedQuery.queryOwner</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the queryOwner
	 */
	@Accessor(qualifier = "queryOwner", type = Accessor.Type.SETTER)
	public void setQueryOwner(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(QUERYOWNER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSavedQuery.searchMode</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the searchMode
	 */
	@Accessor(qualifier = "searchMode", type = Accessor.Type.SETTER)
	public void setSearchMode(final String value)
	{
		getPersistenceContext().setPropertyValue(SEARCHMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSavedQuery.sortAsc</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the sortAsc
	 */
	@Accessor(qualifier = "sortAsc", type = Accessor.Type.SETTER)
	public void setSortAsc(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SORTASC, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSavedQuery.sortAttribute</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the sortAttribute
	 */
	@Accessor(qualifier = "sortAttribute", type = Accessor.Type.SETTER)
	public void setSortAttribute(final String value)
	{
		getPersistenceContext().setPropertyValue(SORTATTRIBUTE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSavedQuery.tokenizable</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the tokenizable
	 */
	@Accessor(qualifier = "tokenizable", type = Accessor.Type.SETTER)
	public void setTokenizable(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(TOKENIZABLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSavedQuery.typeCode</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the typeCode
	 */
	@Accessor(qualifier = "typeCode", type = Accessor.Type.SETTER)
	public void setTypeCode(final String value)
	{
		getPersistenceContext().setPropertyValue(TYPECODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSavedQuery.userGroups</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the userGroups
	 */
	@Accessor(qualifier = "userGroups", type = Accessor.Type.SETTER)
	public void setUserGroups(final Collection<UserGroupModel> value)
	{
		getPersistenceContext().setPropertyValue(USERGROUPS, value);
	}
	
}
