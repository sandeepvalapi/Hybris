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
package de.hybris.platform.personalizationintegration.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type CxMapperScript first defined at extension personalizationintegration.
 */
@SuppressWarnings("all")
public class CxMapperScriptModel extends ScriptModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CxMapperScript";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxMapperScript.group</code> attribute defined at extension <code>personalizationintegration</code>. */
	public static final String GROUP = "group";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxMapperScript.requiredFields</code> attribute defined at extension <code>personalizationyprofile</code>. */
	public static final String REQUIREDFIELDS = "requiredFields";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CxMapperScriptModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CxMapperScriptModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 * @param _content initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 * @param _group initial attribute declared by type <code>CxMapperScript</code> at extension <code>personalizationintegration</code>
	 */
	@Deprecated
	public CxMapperScriptModel(final String _code, final String _content, final String _group)
	{
		super();
		setCode(_code);
		setContent(_content);
		setGroup(_group);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 * @param _content initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 * @param _group initial attribute declared by type <code>CxMapperScript</code> at extension <code>personalizationintegration</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CxMapperScriptModel(final String _code, final String _content, final String _group, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setContent(_content);
		setGroup(_group);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxMapperScript.group</code> attribute defined at extension <code>personalizationintegration</code>. 
	 * @return the group
	 */
	@Accessor(qualifier = "group", type = Accessor.Type.GETTER)
	public String getGroup()
	{
		return getPersistenceContext().getPropertyValue(GROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxMapperScript.requiredFields</code> attribute defined at extension <code>personalizationyprofile</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the requiredFields - Fields required for mapping data to segments
	 */
	@Accessor(qualifier = "requiredFields", type = Accessor.Type.GETTER)
	public Collection<String> getRequiredFields()
	{
		return getPersistenceContext().getPropertyValue(REQUIREDFIELDS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxMapperScript.group</code> attribute defined at extension <code>personalizationintegration</code>. 
	 *  
	 * @param value the group
	 */
	@Accessor(qualifier = "group", type = Accessor.Type.SETTER)
	public void setGroup(final String value)
	{
		getPersistenceContext().setPropertyValue(GROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxMapperScript.requiredFields</code> attribute defined at extension <code>personalizationyprofile</code>. 
	 *  
	 * @param value the requiredFields - Fields required for mapping data to segments
	 */
	@Accessor(qualifier = "requiredFields", type = Accessor.Type.SETTER)
	public void setRequiredFields(final Collection<String> value)
	{
		getPersistenceContext().setPropertyValue(REQUIREDFIELDS, value);
	}
	
}
