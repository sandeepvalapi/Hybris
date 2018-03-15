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
package de.hybris.platform.deeplink.model.media;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.BarcodeType;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.deeplink.model.rules.DeeplinkUrlModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type BarcodeMedia first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class BarcodeMediaModel extends MediaModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "BarcodeMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>BarcodeMedia.barcodeText</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String BARCODETEXT = "barcodeText";
	
	/** <i>Generated constant</i> - Attribute key of <code>BarcodeMedia.barcodeType</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String BARCODETYPE = "barcodeType";
	
	/** <i>Generated constant</i> - Attribute key of <code>BarcodeMedia.contextItem</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CONTEXTITEM = "contextItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>BarcodeMedia.deeplinkUrl</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String DEEPLINKURL = "deeplinkUrl";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public BarcodeMediaModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public BarcodeMediaModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _barcodeText initial attribute declared by type <code>BarcodeMedia</code> at extension <code>basecommerce</code>
	 * @param _barcodeType initial attribute declared by type <code>BarcodeMedia</code> at extension <code>basecommerce</code>
	 * @param _catalogVersion initial attribute declared by type <code>Media</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 */
	@Deprecated
	public BarcodeMediaModel(final String _barcodeText, final BarcodeType _barcodeType, final CatalogVersionModel _catalogVersion, final String _code)
	{
		super();
		setBarcodeText(_barcodeText);
		setBarcodeType(_barcodeType);
		setCatalogVersion(_catalogVersion);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _barcodeText initial attribute declared by type <code>BarcodeMedia</code> at extension <code>basecommerce</code>
	 * @param _barcodeType initial attribute declared by type <code>BarcodeMedia</code> at extension <code>basecommerce</code>
	 * @param _catalogVersion initial attribute declared by type <code>Media</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public BarcodeMediaModel(final String _barcodeText, final BarcodeType _barcodeType, final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner)
	{
		super();
		setBarcodeText(_barcodeText);
		setBarcodeType(_barcodeType);
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BarcodeMedia.barcodeText</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the barcodeText
	 */
	@Accessor(qualifier = "barcodeText", type = Accessor.Type.GETTER)
	public String getBarcodeText()
	{
		return getPersistenceContext().getPropertyValue(BARCODETEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BarcodeMedia.barcodeType</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the barcodeType
	 */
	@Accessor(qualifier = "barcodeType", type = Accessor.Type.GETTER)
	public BarcodeType getBarcodeType()
	{
		return getPersistenceContext().getPropertyValue(BARCODETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BarcodeMedia.contextItem</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the contextItem
	 */
	@Accessor(qualifier = "contextItem", type = Accessor.Type.GETTER)
	public ItemModel getContextItem()
	{
		return getPersistenceContext().getPropertyValue(CONTEXTITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BarcodeMedia.deeplinkUrl</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the deeplinkUrl
	 */
	@Accessor(qualifier = "deeplinkUrl", type = Accessor.Type.GETTER)
	public DeeplinkUrlModel getDeeplinkUrl()
	{
		return getPersistenceContext().getPropertyValue(DEEPLINKURL);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BarcodeMedia.barcodeText</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the barcodeText
	 */
	@Accessor(qualifier = "barcodeText", type = Accessor.Type.SETTER)
	public void setBarcodeText(final String value)
	{
		getPersistenceContext().setPropertyValue(BARCODETEXT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BarcodeMedia.barcodeType</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the barcodeType
	 */
	@Accessor(qualifier = "barcodeType", type = Accessor.Type.SETTER)
	public void setBarcodeType(final BarcodeType value)
	{
		getPersistenceContext().setPropertyValue(BARCODETYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BarcodeMedia.contextItem</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the contextItem
	 */
	@Accessor(qualifier = "contextItem", type = Accessor.Type.SETTER)
	public void setContextItem(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(CONTEXTITEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BarcodeMedia.deeplinkUrl</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the deeplinkUrl
	 */
	@Accessor(qualifier = "deeplinkUrl", type = Accessor.Type.SETTER)
	public void setDeeplinkUrl(final DeeplinkUrlModel value)
	{
		getPersistenceContext().setPropertyValue(DEEPLINKURL, value);
	}
	
}
