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
package de.hybris.platform.cockpit.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CommentMetadata first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class CommentMetadataModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CommentMetadata";
	
	/**<i>Generated relation code constant for relation <code>CommentToCommentMetadata</code> defining source attribute <code>comment</code> in extension <code>cockpit</code>.</i>*/
	public static final String _COMMENTTOCOMMENTMETADATA = "CommentToCommentMetadata";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentMetadata.x</code> attribute defined at extension <code>cockpit</code>. */
	public static final String X = "x";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentMetadata.y</code> attribute defined at extension <code>cockpit</code>. */
	public static final String Y = "y";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentMetadata.pageIndex</code> attribute defined at extension <code>cockpit</code>. */
	public static final String PAGEINDEX = "pageIndex";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentMetadata.item</code> attribute defined at extension <code>cockpit</code>. */
	public static final String ITEM = "item";
	
	/** <i>Generated constant</i> - Attribute key of <code>CommentMetadata.comment</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COMMENT = "comment";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CommentMetadataModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CommentMetadataModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _comment initial attribute declared by type <code>CommentMetadata</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CommentMetadataModel(final CommentModel _comment)
	{
		super();
		setComment(_comment);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _comment initial attribute declared by type <code>CommentMetadata</code> at extension <code>cockpit</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CommentMetadataModel(final CommentModel _comment, final ItemModel _owner)
	{
		super();
		setComment(_comment);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentMetadata.comment</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
	public CommentModel getComment()
	{
		return getPersistenceContext().getPropertyValue(COMMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentMetadata.item</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the item
	 */
	@Accessor(qualifier = "item", type = Accessor.Type.GETTER)
	public ItemModel getItem()
	{
		return getPersistenceContext().getPropertyValue(ITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentMetadata.pageIndex</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the pageIndex
	 */
	@Accessor(qualifier = "pageIndex", type = Accessor.Type.GETTER)
	public Integer getPageIndex()
	{
		return getPersistenceContext().getPropertyValue(PAGEINDEX);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentMetadata.x</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the x
	 */
	@Accessor(qualifier = "x", type = Accessor.Type.GETTER)
	public Integer getX()
	{
		return getPersistenceContext().getPropertyValue(X);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CommentMetadata.y</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the y
	 */
	@Accessor(qualifier = "y", type = Accessor.Type.GETTER)
	public Integer getY()
	{
		return getPersistenceContext().getPropertyValue(Y);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CommentMetadata.comment</code> attribute defined at extension <code>cockpit</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
	public void setComment(final CommentModel value)
	{
		getPersistenceContext().setPropertyValue(COMMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentMetadata.item</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the item
	 */
	@Accessor(qualifier = "item", type = Accessor.Type.SETTER)
	public void setItem(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(ITEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentMetadata.pageIndex</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the pageIndex
	 */
	@Accessor(qualifier = "pageIndex", type = Accessor.Type.SETTER)
	public void setPageIndex(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PAGEINDEX, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentMetadata.x</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the x
	 */
	@Accessor(qualifier = "x", type = Accessor.Type.SETTER)
	public void setX(final Integer value)
	{
		getPersistenceContext().setPropertyValue(X, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CommentMetadata.y</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the y
	 */
	@Accessor(qualifier = "y", type = Accessor.Type.SETTER)
	public void setY(final Integer value)
	{
		getPersistenceContext().setPropertyValue(Y, value);
	}
	
}
