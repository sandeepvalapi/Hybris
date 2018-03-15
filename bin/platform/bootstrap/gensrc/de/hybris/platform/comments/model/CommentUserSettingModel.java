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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CommentUserSetting first defined at extension comments.
 */
@SuppressWarnings("all")
public class CommentUserSettingModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CommentUserSetting";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentUserSetting.read</code> attribute defined at extension <code>comments</code>. */
	public static final String READ = "read";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentUserSetting.ignored</code> attribute defined at extension <code>comments</code>. */
	public static final String IGNORED = "ignored";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentUserSetting.priority</code> attribute defined at extension <code>comments</code>. */
	public static final String PRIORITY = "priority";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentUserSetting.comment</code> attribute defined at extension <code>comments</code>. */
	public static final String COMMENT = "comment";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentUserSetting.user</code> attribute defined at extension <code>comments</code>. */
	public static final String USER = "user";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentUserSetting.workStatus</code> attribute defined at extension <code>cockpit</code>. */
	public static final String WORKSTATUS = "workStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentUserSetting.hidden</code> attribute defined at extension <code>cockpit</code>. */
	public static final String HIDDEN = "hidden";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CommentUserSettingModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CommentUserSettingModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _comment initial attribute declared by type <code>CommentUserSetting</code> at extension <code>comments</code>
	 * @param _user initial attribute declared by type <code>CommentUserSetting</code> at extension <code>comments</code>
	 */
	@Deprecated
	public CommentUserSettingModel(final AbstractCommentModel _comment, final UserModel _user)
	{
		super();
		setComment(_comment);
		setUser(_user);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _comment initial attribute declared by type <code>CommentUserSetting</code> at extension <code>comments</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>CommentUserSetting</code> at extension <code>comments</code>
	 */
	@Deprecated
	public CommentUserSettingModel(final AbstractCommentModel _comment, final ItemModel _owner, final UserModel _user)
	{
		super();
		setComment(_comment);
		setOwner(_owner);
		setUser(_user);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentUserSetting.comment</code> attribute defined at extension <code>comments</code>. 
	 * @return the comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
	public AbstractCommentModel getComment()
	{
		return getPersistenceContext().getPropertyValue(COMMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentUserSetting.hidden</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the hidden
	 */
	@Accessor(qualifier = "hidden", type = Accessor.Type.GETTER)
	public Boolean getHidden()
	{
		return getPersistenceContext().getPropertyValue(HIDDEN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentUserSetting.ignored</code> attribute defined at extension <code>comments</code>. 
	 * @return the ignored
	 */
	@Accessor(qualifier = "ignored", type = Accessor.Type.GETTER)
	public Boolean getIgnored()
	{
		return getPersistenceContext().getPropertyValue(IGNORED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentUserSetting.priority</code> attribute defined at extension <code>comments</code>. 
	 * @return the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public Integer getPriority()
	{
		return getPersistenceContext().getPropertyValue(PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentUserSetting.read</code> attribute defined at extension <code>comments</code>. 
	 * @return the read
	 */
	@Accessor(qualifier = "read", type = Accessor.Type.GETTER)
	public Boolean getRead()
	{
		return getPersistenceContext().getPropertyValue(READ);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentUserSetting.user</code> attribute defined at extension <code>comments</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentUserSetting.workStatus</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the workStatus
	 */
	@Accessor(qualifier = "workStatus", type = Accessor.Type.GETTER)
	public Boolean getWorkStatus()
	{
		return getPersistenceContext().getPropertyValue(WORKSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CommentUserSetting.comment</code> attribute defined at extension <code>comments</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
	public void setComment(final AbstractCommentModel value)
	{
		getPersistenceContext().setPropertyValue(COMMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentUserSetting.hidden</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the hidden
	 */
	@Accessor(qualifier = "hidden", type = Accessor.Type.SETTER)
	public void setHidden(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(HIDDEN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentUserSetting.ignored</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the ignored
	 */
	@Accessor(qualifier = "ignored", type = Accessor.Type.SETTER)
	public void setIgnored(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(IGNORED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentUserSetting.priority</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PRIORITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentUserSetting.read</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the read
	 */
	@Accessor(qualifier = "read", type = Accessor.Type.SETTER)
	public void setRead(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(READ, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CommentUserSetting.user</code> attribute defined at extension <code>comments</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentUserSetting.workStatus</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the workStatus
	 */
	@Accessor(qualifier = "workStatus", type = Accessor.Type.SETTER)
	public void setWorkStatus(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(WORKSTATUS, value);
	}
	
}
