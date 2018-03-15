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
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CommentType first defined at extension comments.
 */
@SuppressWarnings("all")
public class CommentTypeModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CommentType";
	
	/**<i>Generated relation code constant for relation <code>DomainCommentTypeRelation</code> defining source attribute <code>domain</code> in extension <code>comments</code>.</i>*/
	public static final String _DOMAINCOMMENTTYPERELATION = "DomainCommentTypeRelation";
	
	/**<i>Generated relation code constant for relation <code>CommentCommentTypeRelation</code> defining source attribute <code>comment</code> in extension <code>comments</code>.</i>*/
	public static final String _COMMENTCOMMENTTYPERELATION = "CommentCommentTypeRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentType.code</code> attribute defined at extension <code>comments</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentType.name</code> attribute defined at extension <code>comments</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentType.metaType</code> attribute defined at extension <code>comments</code>. */
	public static final String METATYPE = "metaType";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentType.domain</code> attribute defined at extension <code>comments</code>. */
	public static final String DOMAIN = "domain";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentType.comment</code> attribute defined at extension <code>comments</code>. */
	public static final String COMMENT = "comment";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CommentTypeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CommentTypeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _domain initial attribute declared by type <code>CommentType</code> at extension <code>comments</code>
	 */
	@Deprecated
	public CommentTypeModel(final DomainModel _domain)
	{
		super();
		setDomain(_domain);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CommentType</code> at extension <code>comments</code>
	 * @param _domain initial attribute declared by type <code>CommentType</code> at extension <code>comments</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CommentTypeModel(final String _code, final DomainModel _domain, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setDomain(_domain);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentType.code</code> attribute defined at extension <code>comments</code>. 
	 * @return the code - unique identifier of the comment type within domain
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentType.domain</code> attribute defined at extension <code>comments</code>. 
	 * @return the domain
	 */
	@Accessor(qualifier = "domain", type = Accessor.Type.GETTER)
	public DomainModel getDomain()
	{
		return getPersistenceContext().getPropertyValue(DOMAIN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentType.metaType</code> attribute defined at extension <code>comments</code>. 
	 * @return the metaType
	 */
	@Accessor(qualifier = "metaType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getMetaType()
	{
		return getPersistenceContext().getPropertyValue(METATYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentType.name</code> attribute defined at extension <code>comments</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CommentType.code</code> attribute defined at extension <code>comments</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code - unique identifier of the comment type within domain
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CommentType.domain</code> attribute defined at extension <code>comments</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the domain
	 */
	@Accessor(qualifier = "domain", type = Accessor.Type.SETTER)
	public void setDomain(final DomainModel value)
	{
		getPersistenceContext().setPropertyValue(DOMAIN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentType.metaType</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the metaType
	 */
	@Accessor(qualifier = "metaType", type = Accessor.Type.SETTER)
	public void setMetaType(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(METATYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentType.name</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
}
