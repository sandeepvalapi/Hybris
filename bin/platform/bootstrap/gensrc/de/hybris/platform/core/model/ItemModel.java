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
package de.hybris.platform.core.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.List;

/**
 * Generated model class for type Item first defined at extension core.
 */
@SuppressWarnings("all")
public class ItemModel extends AbstractItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Item";
	
	/**<i>Generated relation code constant for relation <code>CommentItemRelation</code> defining source attribute <code>comments</code> in extension <code>comments</code>.</i>*/
	public static final String _COMMENTITEMRELATION = "CommentItemRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Item.creationtime</code> attribute defined at extension <code>core</code>. */
	public static final String CREATIONTIME = "creationtime";
	
	/** <i>Generated constant</i> - Attribute key of <code>Item.modifiedtime</code> attribute defined at extension <code>core</code>. */
	public static final String MODIFIEDTIME = "modifiedtime";
	
	/** <i>Generated constant</i> - Attribute key of <code>Item.itemtype</code> attribute defined at extension <code>core</code>. */
	public static final String ITEMTYPE = "itemtype";
	
	/** <i>Generated constant</i> - Attribute key of <code>Item.owner</code> attribute defined at extension <code>core</code>. */
	public static final String OWNER = "owner";
	
	/** <i>Generated constant</i> - Attribute key of <code>Item.pk</code> attribute defined at extension <code>core</code>. */
	public static final String PK = "pk";
	
	/** <i>Generated constant</i> - Attribute key of <code>Item.sealed</code> attribute defined at extension <code>core</code>. */
	public static final String SEALED = "sealed";
	
	/** <i>Generated constant</i> - Attribute key of <code>Item.comments</code> attribute defined at extension <code>comments</code>. */
	public static final String COMMENTS = "comments";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ItemModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ItemModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ItemModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Item.comments</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the comments
	 */
	@Accessor(qualifier = "comments", type = Accessor.Type.GETTER)
	public List<CommentModel> getComments()
	{
		return getPersistenceContext().getPropertyValue(COMMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Item.creationtime</code> attribute defined at extension <code>core</code>. 
	 * @return the creationtime
	 */
	@Accessor(qualifier = "creationtime", type = Accessor.Type.GETTER)
	public Date getCreationtime()
	{
		return getPersistenceContext().getPropertyValue(CREATIONTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Item.modifiedtime</code> attribute defined at extension <code>core</code>. 
	 * @return the modifiedtime
	 */
	@Accessor(qualifier = "modifiedtime", type = Accessor.Type.GETTER)
	public Date getModifiedtime()
	{
		return getPersistenceContext().getPropertyValue(MODIFIEDTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Item.owner</code> attribute defined at extension <code>core</code>. 
	 * @return the owner
	 */
	@Accessor(qualifier = "owner", type = Accessor.Type.GETTER)
	public ItemModel getOwner()
	{
		return getPersistenceContext().getPropertyValue(OWNER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Item.sealed</code> attribute defined at extension <code>core</code>. 
	 * @return the sealed
	 */
	@Accessor(qualifier = "sealed", type = Accessor.Type.GETTER)
	public boolean isSealed()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(SEALED));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Item.comments</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the comments
	 */
	@Accessor(qualifier = "comments", type = Accessor.Type.SETTER)
	public void setComments(final List<CommentModel> value)
	{
		getPersistenceContext().setPropertyValue(COMMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Item.creationtime</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the creationtime
	 */
	@Accessor(qualifier = "creationtime", type = Accessor.Type.SETTER)
	public void setCreationtime(final Date value)
	{
		getPersistenceContext().setPropertyValue(CREATIONTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Item.modifiedtime</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the modifiedtime
	 */
	@Accessor(qualifier = "modifiedtime", type = Accessor.Type.SETTER)
	public void setModifiedtime(final Date value)
	{
		getPersistenceContext().setPropertyValue(MODIFIEDTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Item.owner</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the owner
	 */
	@Accessor(qualifier = "owner", type = Accessor.Type.SETTER)
	public void setOwner(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(OWNER, value);
	}
	
}
