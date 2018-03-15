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
import de.hybris.platform.comments.model.AbstractCommentModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

/**
 * Generated model class for type Reply first defined at extension comments.
 */
@SuppressWarnings("all")
public class ReplyModel extends AbstractCommentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Reply";
	
	/**<i>Generated relation code constant for relation <code>ReplyParentRelation</code> defining source attribute <code>replies</code> in extension <code>comments</code>.</i>*/
	public static final String _REPLYPARENTRELATION = "ReplyParentRelation";
	
	/**<i>Generated relation code constant for relation <code>CommentReplyRelation</code> defining source attribute <code>comment</code> in extension <code>comments</code>.</i>*/
	public static final String _COMMENTREPLYRELATION = "CommentReplyRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Reply.replies</code> attribute defined at extension <code>comments</code>. */
	public static final String REPLIES = "replies";
	
	/** <i>Generated constant</i> - Attribute key of <code>Reply.parentPOS</code> attribute defined at extension <code>comments</code>. */
	public static final String PARENTPOS = "parentPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>Reply.parent</code> attribute defined at extension <code>comments</code>. */
	public static final String PARENT = "parent";
	
	/** <i>Generated constant</i> - Attribute key of <code>Reply.commentPOS</code> attribute defined at extension <code>comments</code>. */
	public static final String COMMENTPOS = "commentPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>Reply.comment</code> attribute defined at extension <code>comments</code>. */
	public static final String COMMENT = "comment";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ReplyModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ReplyModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _author initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 * @param _comment initial attribute declared by type <code>Reply</code> at extension <code>comments</code>
	 * @param _text initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 */
	@Deprecated
	public ReplyModel(final UserModel _author, final CommentModel _comment, final String _text)
	{
		super();
		setAuthor(_author);
		setComment(_comment);
		setText(_text);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _author initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 * @param _comment initial attribute declared by type <code>Reply</code> at extension <code>comments</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _parent initial attribute declared by type <code>Reply</code> at extension <code>comments</code>
	 * @param _text initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 */
	@Deprecated
	public ReplyModel(final UserModel _author, final CommentModel _comment, final ItemModel _owner, final ReplyModel _parent, final String _text)
	{
		super();
		setAuthor(_author);
		setComment(_comment);
		setOwner(_owner);
		setParent(_parent);
		setText(_text);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Reply.comment</code> attribute defined at extension <code>comments</code>. 
	 * @return the comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
	public CommentModel getComment()
	{
		return getPersistenceContext().getPropertyValue(COMMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Reply.parent</code> attribute defined at extension <code>comments</code>. 
	 * @return the parent
	 */
	@Accessor(qualifier = "parent", type = Accessor.Type.GETTER)
	public ReplyModel getParent()
	{
		return getPersistenceContext().getPropertyValue(PARENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Reply.replies</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the replies
	 */
	@Accessor(qualifier = "replies", type = Accessor.Type.GETTER)
	public List<ReplyModel> getReplies()
	{
		return getPersistenceContext().getPropertyValue(REPLIES);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Reply.comment</code> attribute defined at extension <code>comments</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
	public void setComment(final CommentModel value)
	{
		getPersistenceContext().setPropertyValue(COMMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Reply.parent</code> attribute defined at extension <code>comments</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the parent
	 */
	@Accessor(qualifier = "parent", type = Accessor.Type.SETTER)
	public void setParent(final ReplyModel value)
	{
		getPersistenceContext().setPropertyValue(PARENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Reply.replies</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the replies
	 */
	@Accessor(qualifier = "replies", type = Accessor.Type.SETTER)
	public void setReplies(final List<ReplyModel> value)
	{
		getPersistenceContext().setPropertyValue(REPLIES, value);
	}
	
}
