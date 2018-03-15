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
package de.hybris.platform.comments.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type Domain first defined at extension comments.
 */
@SuppressWarnings("all")
public class DomainModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Domain";
	
	/** <i>Generated constant</i> - Attribute key of <code>Domain.code</code> attribute defined at extension <code>comments</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Domain.name</code> attribute defined at extension <code>comments</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>Domain.components</code> attribute defined at extension <code>comments</code>. */
	public static final String COMPONENTS = "components";
	
	/** <i>Generated constant</i> - Attribute key of <code>Domain.commentTypes</code> attribute defined at extension <code>comments</code>. */
	public static final String COMMENTTYPES = "commentTypes";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DomainModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DomainModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Domain</code> at extension <code>comments</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public DomainModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Domain.code</code> attribute defined at extension <code>comments</code>. 
	 * @return the code - unique identifier of the domain; will be generated if not set
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Domain.commentTypes</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the commentTypes
	 */
	@Accessor(qualifier = "commentTypes", type = Accessor.Type.GETTER)
	public Collection<CommentTypeModel> getCommentTypes()
	{
		return getPersistenceContext().getPropertyValue(COMMENTTYPES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Domain.components</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the components
	 */
	@Accessor(qualifier = "components", type = Accessor.Type.GETTER)
	public Collection<ComponentModel> getComponents()
	{
		return getPersistenceContext().getPropertyValue(COMPONENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Domain.name</code> attribute defined at extension <code>comments</code>. 
	 * @return the name - Name of the domain
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Domain.code</code> attribute defined at extension <code>comments</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code - unique identifier of the domain; will be generated if not set
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Domain.commentTypes</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the commentTypes
	 */
	@Accessor(qualifier = "commentTypes", type = Accessor.Type.SETTER)
	public void setCommentTypes(final Collection<CommentTypeModel> value)
	{
		getPersistenceContext().setPropertyValue(COMMENTTYPES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Domain.components</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the components
	 */
	@Accessor(qualifier = "components", type = Accessor.Type.SETTER)
	public void setComponents(final Collection<ComponentModel> value)
	{
		getPersistenceContext().setPropertyValue(COMPONENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Domain.name</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the name - Name of the domain
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
}
