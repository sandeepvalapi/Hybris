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
import de.hybris.platform.cockpit.model.CommentMetadataModel;
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.ReplyModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;

/**
 * Generated model class for type Comment first defined at extension comments.
 */
@SuppressWarnings("all")
public class CommentModel extends AbstractCommentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Comment";
	
	/** <i>Generated constant</i> - Attribute key of <code>Comment.code</code> attribute defined at extension <code>comments</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Comment.priority</code> attribute defined at extension <code>comments</code>. */
	public static final String PRIORITY = "priority";
	
	/** <i>Generated constant</i> - Attribute key of <code>Comment.relatedItems</code> attribute defined at extension <code>comments</code>. */
	public static final String RELATEDITEMS = "relatedItems";
	
	/** <i>Generated constant</i> - Attribute key of <code>Comment.replies</code> attribute defined at extension <code>comments</code>. */
	public static final String REPLIES = "replies";
	
	/** <i>Generated constant</i> - Attribute key of <code>Comment.assignedTo</code> attribute defined at extension <code>comments</code>. */
	public static final String ASSIGNEDTO = "assignedTo";
	
	/** <i>Generated constant</i> - Attribute key of <code>Comment.watchers</code> attribute defined at extension <code>comments</code>. */
	public static final String WATCHERS = "watchers";
	
	/** <i>Generated constant</i> - Attribute key of <code>Comment.component</code> attribute defined at extension <code>comments</code>. */
	public static final String COMPONENT = "component";
	
	/** <i>Generated constant</i> - Attribute key of <code>Comment.commentType</code> attribute defined at extension <code>comments</code>. */
	public static final String COMMENTTYPE = "commentType";
	
	/** <i>Generated constant</i> - Attribute key of <code>Comment.commentMetadata</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COMMENTMETADATA = "commentMetadata";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CommentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CommentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _author initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 * @param _commentType initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _component initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _text initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 */
	@Deprecated
	public CommentModel(final UserModel _author, final CommentTypeModel _commentType, final ComponentModel _component, final String _text)
	{
		super();
		setAuthor(_author);
		setCommentType(_commentType);
		setComponent(_component);
		setText(_text);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _author initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 * @param _commentType initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _component initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _text initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 */
	@Deprecated
	public CommentModel(final UserModel _author, final CommentTypeModel _commentType, final ComponentModel _component, final ItemModel _owner, final String _text)
	{
		super();
		setAuthor(_author);
		setCommentType(_commentType);
		setComponent(_component);
		setOwner(_owner);
		setText(_text);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Comment.assignedTo</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the assignedTo
	 */
	@Accessor(qualifier = "assignedTo", type = Accessor.Type.GETTER)
	public Collection<UserModel> getAssignedTo()
	{
		return getPersistenceContext().getPropertyValue(ASSIGNEDTO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Comment.code</code> attribute defined at extension <code>comments</code>. 
	 * @return the code - unique identifier of the comment; will be generated if not set
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Comment.commentMetadata</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the commentMetadata
	 */
	@Accessor(qualifier = "commentMetadata", type = Accessor.Type.GETTER)
	public Collection<CommentMetadataModel> getCommentMetadata()
	{
		return getPersistenceContext().getPropertyValue(COMMENTMETADATA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Comment.commentType</code> attribute defined at extension <code>comments</code>. 
	 * @return the commentType
	 */
	@Accessor(qualifier = "commentType", type = Accessor.Type.GETTER)
	public CommentTypeModel getCommentType()
	{
		return getPersistenceContext().getPropertyValue(COMMENTTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Comment.component</code> attribute defined at extension <code>comments</code>. 
	 * @return the component
	 */
	@Accessor(qualifier = "component", type = Accessor.Type.GETTER)
	public ComponentModel getComponent()
	{
		return getPersistenceContext().getPropertyValue(COMPONENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Comment.priority</code> attribute defined at extension <code>comments</code>. 
	 * @return the priority - Priority of a comment
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public Integer getPriority()
	{
		return getPersistenceContext().getPropertyValue(PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Comment.relatedItems</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the relatedItems - Related items of this comment
	 * @deprecated since ages
	 */
	@Deprecated
	@Accessor(qualifier = "relatedItems", type = Accessor.Type.GETTER)
	public Collection<ItemModel> getRelatedItems()
	{
		return getPersistenceContext().getPropertyValue(RELATEDITEMS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Comment.replies</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the replies
	 */
	@Accessor(qualifier = "replies", type = Accessor.Type.GETTER)
	public List<ReplyModel> getReplies()
	{
		return getPersistenceContext().getPropertyValue(REPLIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Comment.watchers</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the watchers
	 */
	@Accessor(qualifier = "watchers", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getWatchers()
	{
		return getPersistenceContext().getPropertyValue(WATCHERS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Comment.assignedTo</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the assignedTo
	 */
	@Accessor(qualifier = "assignedTo", type = Accessor.Type.SETTER)
	public void setAssignedTo(final Collection<UserModel> value)
	{
		getPersistenceContext().setPropertyValue(ASSIGNEDTO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Comment.code</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the code - unique identifier of the comment; will be generated if not set
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Comment.commentMetadata</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the commentMetadata
	 */
	@Accessor(qualifier = "commentMetadata", type = Accessor.Type.SETTER)
	public void setCommentMetadata(final Collection<CommentMetadataModel> value)
	{
		getPersistenceContext().setPropertyValue(COMMENTMETADATA, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Comment.commentType</code> attribute defined at extension <code>comments</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the commentType
	 */
	@Accessor(qualifier = "commentType", type = Accessor.Type.SETTER)
	public void setCommentType(final CommentTypeModel value)
	{
		getPersistenceContext().setPropertyValue(COMMENTTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Comment.component</code> attribute defined at extension <code>comments</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the component
	 */
	@Accessor(qualifier = "component", type = Accessor.Type.SETTER)
	public void setComponent(final ComponentModel value)
	{
		getPersistenceContext().setPropertyValue(COMPONENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Comment.priority</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the priority - Priority of a comment
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PRIORITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Comment.relatedItems</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the relatedItems - Related items of this comment
	 * @deprecated since ages
	 */
	@Deprecated
	@Accessor(qualifier = "relatedItems", type = Accessor.Type.SETTER)
	public void setRelatedItems(final Collection<ItemModel> value)
	{
		getPersistenceContext().setPropertyValue(RELATEDITEMS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Comment.replies</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the replies
	 */
	@Accessor(qualifier = "replies", type = Accessor.Type.SETTER)
	public void setReplies(final List<ReplyModel> value)
	{
		getPersistenceContext().setPropertyValue(REPLIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Comment.watchers</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the watchers
	 */
	@Accessor(qualifier = "watchers", type = Accessor.Type.SETTER)
	public void setWatchers(final Collection<PrincipalModel> value)
	{
		getPersistenceContext().setPropertyValue(WATCHERS, value);
	}
	
}
