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
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CommentAttachment first defined at extension comments.
 */
@SuppressWarnings("all")
public class CommentAttachmentModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CommentAttachment";
	
	/**<i>Generated relation code constant for relation <code>AbstractCommentAttachmentRelation</code> defining source attribute <code>abstractComment</code> in extension <code>comments</code>.</i>*/
	public static final String _ABSTRACTCOMMENTATTACHMENTRELATION = "AbstractCommentAttachmentRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentAttachment.item</code> attribute defined at extension <code>comments</code>. */
	public static final String ITEM = "item";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentAttachment.abstractComment</code> attribute defined at extension <code>comments</code>. */
	public static final String ABSTRACTCOMMENT = "abstractComment";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CommentAttachmentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CommentAttachmentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _item initial attribute declared by type <code>CommentAttachment</code> at extension <code>comments</code>
	 */
	@Deprecated
	public CommentAttachmentModel(final ItemModel _item)
	{
		super();
		setItem(_item);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _item initial attribute declared by type <code>CommentAttachment</code> at extension <code>comments</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CommentAttachmentModel(final ItemModel _item, final ItemModel _owner)
	{
		super();
		setItem(_item);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentAttachment.abstractComment</code> attribute defined at extension <code>comments</code>. 
	 * @return the abstractComment
	 */
	@Accessor(qualifier = "abstractComment", type = Accessor.Type.GETTER)
	public AbstractCommentModel getAbstractComment()
	{
		return getPersistenceContext().getPropertyValue(ABSTRACTCOMMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentAttachment.item</code> attribute defined at extension <code>comments</code>. 
	 * @return the item
	 */
	@Accessor(qualifier = "item", type = Accessor.Type.GETTER)
	public ItemModel getItem()
	{
		return getPersistenceContext().getPropertyValue(ITEM);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentAttachment.abstractComment</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the abstractComment
	 */
	@Accessor(qualifier = "abstractComment", type = Accessor.Type.SETTER)
	public void setAbstractComment(final AbstractCommentModel value)
	{
		getPersistenceContext().setPropertyValue(ABSTRACTCOMMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentAttachment.item</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the item
	 */
	@Accessor(qualifier = "item", type = Accessor.Type.SETTER)
	public void setItem(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(ITEM, value);
	}
	
}
