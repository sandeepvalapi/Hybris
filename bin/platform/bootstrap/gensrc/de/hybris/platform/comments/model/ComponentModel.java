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
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type Component first defined at extension comments.
 */
@SuppressWarnings("all")
public class ComponentModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Component";
	
	/**<i>Generated relation code constant for relation <code>DomainComponentRelation</code> defining source attribute <code>domain</code> in extension <code>comments</code>.</i>*/
	public static final String _DOMAINCOMPONENTRELATION = "DomainComponentRelation";
	
	/**<i>Generated relation code constant for relation <code>CommentComponentRelation</code> defining source attribute <code>comment</code> in extension <code>comments</code>.</i>*/
	public static final String _COMMENTCOMPONENTRELATION = "CommentComponentRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Component.code</code> attribute defined at extension <code>comments</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Component.name</code> attribute defined at extension <code>comments</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>Component.availableCommentTypes</code> attribute defined at extension <code>comments</code>. */
	public static final String AVAILABLECOMMENTTYPES = "availableCommentTypes";
	
	/** <i>Generated constant</i> - Attribute key of <code>Component.domain</code> attribute defined at extension <code>comments</code>. */
	public static final String DOMAIN = "domain";
	
	/** <i>Generated constant</i> - Attribute key of <code>Component.comment</code> attribute defined at extension <code>comments</code>. */
	public static final String COMMENT = "comment";
	
	/** <i>Generated constant</i> - Attribute key of <code>Component.readPermitted</code> attribute defined at extension <code>comments</code>. */
	public static final String READPERMITTED = "readPermitted";
	
	/** <i>Generated constant</i> - Attribute key of <code>Component.writePermitted</code> attribute defined at extension <code>comments</code>. */
	public static final String WRITEPERMITTED = "writePermitted";
	
	/** <i>Generated constant</i> - Attribute key of <code>Component.createPermitted</code> attribute defined at extension <code>comments</code>. */
	public static final String CREATEPERMITTED = "createPermitted";
	
	/** <i>Generated constant</i> - Attribute key of <code>Component.removePermitted</code> attribute defined at extension <code>comments</code>. */
	public static final String REMOVEPERMITTED = "removePermitted";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ComponentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _domain initial attribute declared by type <code>Component</code> at extension <code>comments</code>
	 */
	@Deprecated
	public ComponentModel(final DomainModel _domain)
	{
		super();
		setDomain(_domain);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Component</code> at extension <code>comments</code>
	 * @param _domain initial attribute declared by type <code>Component</code> at extension <code>comments</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ComponentModel(final String _code, final DomainModel _domain, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setDomain(_domain);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Component.availableCommentTypes</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the availableCommentTypes
	 * @deprecated since ages
	 */
	@Deprecated
	@Accessor(qualifier = "availableCommentTypes", type = Accessor.Type.GETTER)
	public Collection<CommentTypeModel> getAvailableCommentTypes()
	{
		return getPersistenceContext().getPropertyValue(AVAILABLECOMMENTTYPES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Component.code</code> attribute defined at extension <code>comments</code>. 
	 * @return the code - unique identifier of the component
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Component.createPermitted</code> attribute defined at extension <code>comments</code>. 
	 * @return the createPermitted
	 */
	@Accessor(qualifier = "createPermitted", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getCreatePermitted()
	{
		return getCreatePermitted(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Component.createPermitted</code> attribute defined at extension <code>comments</code>. 
	 * @param loc the value localization key 
	 * @return the createPermitted
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "createPermitted", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getCreatePermitted(final Locale loc)
	{
		return getPersistenceContext().getLocalizedRelationValue(CREATEPERMITTED, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Component.domain</code> attribute defined at extension <code>comments</code>. 
	 * @return the domain
	 */
	@Accessor(qualifier = "domain", type = Accessor.Type.GETTER)
	public DomainModel getDomain()
	{
		return getPersistenceContext().getPropertyValue(DOMAIN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Component.name</code> attribute defined at extension <code>comments</code>. 
	 * @return the name - Name of the component
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Component.readPermitted</code> attribute defined at extension <code>comments</code>. 
	 * @return the readPermitted
	 */
	@Accessor(qualifier = "readPermitted", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getReadPermitted()
	{
		return getReadPermitted(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Component.readPermitted</code> attribute defined at extension <code>comments</code>. 
	 * @param loc the value localization key 
	 * @return the readPermitted
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "readPermitted", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getReadPermitted(final Locale loc)
	{
		return getPersistenceContext().getLocalizedRelationValue(READPERMITTED, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Component.removePermitted</code> attribute defined at extension <code>comments</code>. 
	 * @return the removePermitted
	 */
	@Accessor(qualifier = "removePermitted", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getRemovePermitted()
	{
		return getRemovePermitted(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Component.removePermitted</code> attribute defined at extension <code>comments</code>. 
	 * @param loc the value localization key 
	 * @return the removePermitted
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "removePermitted", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getRemovePermitted(final Locale loc)
	{
		return getPersistenceContext().getLocalizedRelationValue(REMOVEPERMITTED, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Component.writePermitted</code> attribute defined at extension <code>comments</code>. 
	 * @return the writePermitted
	 */
	@Accessor(qualifier = "writePermitted", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getWritePermitted()
	{
		return getWritePermitted(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Component.writePermitted</code> attribute defined at extension <code>comments</code>. 
	 * @param loc the value localization key 
	 * @return the writePermitted
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "writePermitted", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getWritePermitted(final Locale loc)
	{
		return getPersistenceContext().getLocalizedRelationValue(WRITEPERMITTED, loc);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Component.code</code> attribute defined at extension <code>comments</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code - unique identifier of the component
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Component.createPermitted</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the createPermitted
	 */
	@Accessor(qualifier = "createPermitted", type = Accessor.Type.SETTER)
	public void setCreatePermitted(final Collection<PrincipalModel> value)
	{
		setCreatePermitted(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Component.createPermitted</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the createPermitted
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "createPermitted", type = Accessor.Type.SETTER)
	public void setCreatePermitted(final Collection<PrincipalModel> value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(CREATEPERMITTED, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Component.domain</code> attribute defined at extension <code>comments</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the domain
	 */
	@Accessor(qualifier = "domain", type = Accessor.Type.SETTER)
	public void setDomain(final DomainModel value)
	{
		getPersistenceContext().setPropertyValue(DOMAIN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Component.name</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the name - Name of the component
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Component.readPermitted</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the readPermitted
	 */
	@Accessor(qualifier = "readPermitted", type = Accessor.Type.SETTER)
	public void setReadPermitted(final Collection<PrincipalModel> value)
	{
		setReadPermitted(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Component.readPermitted</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the readPermitted
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "readPermitted", type = Accessor.Type.SETTER)
	public void setReadPermitted(final Collection<PrincipalModel> value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(READPERMITTED, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Component.removePermitted</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the removePermitted
	 */
	@Accessor(qualifier = "removePermitted", type = Accessor.Type.SETTER)
	public void setRemovePermitted(final Collection<PrincipalModel> value)
	{
		setRemovePermitted(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Component.removePermitted</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the removePermitted
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "removePermitted", type = Accessor.Type.SETTER)
	public void setRemovePermitted(final Collection<PrincipalModel> value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(REMOVEPERMITTED, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Component.writePermitted</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the writePermitted
	 */
	@Accessor(qualifier = "writePermitted", type = Accessor.Type.SETTER)
	public void setWritePermitted(final Collection<PrincipalModel> value)
	{
		setWritePermitted(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Component.writePermitted</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the writePermitted
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "writePermitted", type = Accessor.Type.SETTER)
	public void setWritePermitted(final Collection<PrincipalModel> value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(WRITEPERMITTED, loc, value);
	}
	
}
