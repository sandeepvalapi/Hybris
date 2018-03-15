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
import de.hybris.platform.comments.model.CommentAttachmentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type AbstractComment first defined at extension comments.
 */
@SuppressWarnings("all")
public class AbstractCommentModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractComment";
	
	/**<i>Generated relation code constant for relation <code>CommentUserSettingAbstractCommentRelation</code> defining source attribute <code>commentUserSettings</code> in extension <code>comments</code>.</i>*/
	public static final String _COMMENTUSERSETTINGABSTRACTCOMMENTRELATION = "CommentUserSettingAbstractCommentRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractComment.subject</code> attribute defined at extension <code>comments</code>. */
	public static final String SUBJECT = "subject";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractComment.text</code> attribute defined at extension <code>comments</code>. */
	public static final String TEXT = "text";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractComment.attachments</code> attribute defined at extension <code>comments</code>. */
	public static final String ATTACHMENTS = "attachments";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractComment.author</code> attribute defined at extension <code>comments</code>. */
	public static final String AUTHOR = "author";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractComment.commentUserSettings</code> attribute defined at extension <code>comments</code>. */
	public static final String COMMENTUSERSETTINGS = "commentUserSettings";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractCommentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractCommentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _author initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 * @param _text initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 */
	@Deprecated
	public AbstractCommentModel(final UserModel _author, final String _text)
	{
		super();
		setAuthor(_author);
		setText(_text);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _author initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _text initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 */
	@Deprecated
	public AbstractCommentModel(final UserModel _author, final ItemModel _owner, final String _text)
	{
		super();
		setAuthor(_author);
		setOwner(_owner);
		setText(_text);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractComment.attachments</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the attachments
	 */
	@Accessor(qualifier = "attachments", type = Accessor.Type.GETTER)
	public Collection<CommentAttachmentModel> getAttachments()
	{
		return getPersistenceContext().getPropertyValue(ATTACHMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractComment.author</code> attribute defined at extension <code>comments</code>. 
	 * @return the author
	 */
	@Accessor(qualifier = "author", type = Accessor.Type.GETTER)
	public UserModel getAuthor()
	{
		return getPersistenceContext().getPropertyValue(AUTHOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractComment.subject</code> attribute defined at extension <code>comments</code>. 
	 * @return the subject - Subject of a comment
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.GETTER)
	public String getSubject()
	{
		return getPersistenceContext().getPropertyValue(SUBJECT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractComment.text</code> attribute defined at extension <code>comments</code>. 
	 * @return the text - Content of a comment
	 */
	@Accessor(qualifier = "text", type = Accessor.Type.GETTER)
	public String getText()
	{
		return getPersistenceContext().getPropertyValue(TEXT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractComment.attachments</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the attachments
	 */
	@Accessor(qualifier = "attachments", type = Accessor.Type.SETTER)
	public void setAttachments(final Collection<CommentAttachmentModel> value)
	{
		getPersistenceContext().setPropertyValue(ATTACHMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractComment.author</code> attribute defined at extension <code>comments</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the author
	 */
	@Accessor(qualifier = "author", type = Accessor.Type.SETTER)
	public void setAuthor(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(AUTHOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractComment.subject</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the subject - Subject of a comment
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.SETTER)
	public void setSubject(final String value)
	{
		getPersistenceContext().setPropertyValue(SUBJECT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractComment.text</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the text - Content of a comment
	 */
	@Accessor(qualifier = "text", type = Accessor.Type.SETTER)
	public void setText(final String value)
	{
		getPersistenceContext().setPropertyValue(TEXT, value);
	}
	
}
