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
package de.hybris.platform.commons.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commons.model.FormatModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type Document first defined at extension commons.
 */
@SuppressWarnings("all")
public class DocumentModel extends MediaModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Document";
	
	/**<i>Generated relation code constant for relation <code>ItemDocrRelation</code> defining source attribute <code>sourceItem</code> in extension <code>commons</code>.</i>*/
	public static final String _ITEMDOCRRELATION = "ItemDocrRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Document.itemTimestamp</code> attribute defined at extension <code>commons</code>. */
	public static final String ITEMTIMESTAMP = "itemTimestamp";
	
	/** <i>Generated constant</i> - Attribute key of <code>Document.format</code> attribute defined at extension <code>commons</code>. */
	public static final String FORMAT = "format";
	
	/** <i>Generated constant</i> - Attribute key of <code>Document.sourceItem</code> attribute defined at extension <code>commons</code>. */
	public static final String SOURCEITEM = "sourceItem";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DocumentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DocumentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Document</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _format initial attribute declared by type <code>Document</code> at extension <code>commons</code>
	 */
	@Deprecated
	public DocumentModel(final CatalogVersionModel _catalogVersion, final String _code, final FormatModel _format)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setFormat(_format);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Document</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _format initial attribute declared by type <code>Document</code> at extension <code>commons</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sourceItem initial attribute declared by type <code>Document</code> at extension <code>commons</code>
	 */
	@Deprecated
	public DocumentModel(final CatalogVersionModel _catalogVersion, final String _code, final FormatModel _format, final ItemModel _owner, final ItemModel _sourceItem)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setFormat(_format);
		setOwner(_owner);
		setSourceItem(_sourceItem);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Document.format</code> attribute defined at extension <code>commons</code>. 
	 * @return the format - The format of the document
	 */
	@Accessor(qualifier = "format", type = Accessor.Type.GETTER)
	public FormatModel getFormat()
	{
		return getPersistenceContext().getPropertyValue(FORMAT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Document.itemTimestamp</code> attribute defined at extension <code>commons</code>. 
	 * @return the itemTimestamp - The modified time of the attached item. If the time of the item is
	 * 					younger than the value of this document it could be outdated.
	 */
	@Accessor(qualifier = "itemTimestamp", type = Accessor.Type.GETTER)
	public Date getItemTimestamp()
	{
		return getPersistenceContext().getPropertyValue(ITEMTIMESTAMP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Document.sourceItem</code> attribute defined at extension <code>commons</code>. 
	 * @return the sourceItem
	 */
	@Accessor(qualifier = "sourceItem", type = Accessor.Type.GETTER)
	public ItemModel getSourceItem()
	{
		return getPersistenceContext().getPropertyValue(SOURCEITEM);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Document.format</code> attribute defined at extension <code>commons</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the format - The format of the document
	 */
	@Accessor(qualifier = "format", type = Accessor.Type.SETTER)
	public void setFormat(final FormatModel value)
	{
		getPersistenceContext().setPropertyValue(FORMAT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Document.itemTimestamp</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the itemTimestamp - The modified time of the attached item. If the time of the item is
	 * 					younger than the value of this document it could be outdated.
	 */
	@Accessor(qualifier = "itemTimestamp", type = Accessor.Type.SETTER)
	public void setItemTimestamp(final Date value)
	{
		getPersistenceContext().setPropertyValue(ITEMTIMESTAMP, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Document.sourceItem</code> attribute defined at extension <code>commons</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sourceItem
	 */
	@Accessor(qualifier = "sourceItem", type = Accessor.Type.SETTER)
	public void setSourceItem(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(SOURCEITEM, value);
	}
	
}
