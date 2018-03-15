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
package de.hybris.platform.cms2.model.contents.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type CMSImageComponent first defined at extension cms2.
 */
@SuppressWarnings("all")
public class CMSImageComponentModel extends SimpleCMSComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CMSImageComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSImageComponent.media</code> attribute defined at extension <code>cms2</code>. */
	public static final String MEDIA = "media";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CMSImageComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CMSImageComponentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public CMSImageComponentModel(final CatalogVersionModel _catalogVersion, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public CMSImageComponentModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSImageComponent.media</code> attribute defined at extension <code>cms2</code>. 
	 * @return the media
	 */
	@Accessor(qualifier = "media", type = Accessor.Type.GETTER)
	public MediaModel getMedia()
	{
		return getMedia(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSImageComponent.media</code> attribute defined at extension <code>cms2</code>. 
	 * @param loc the value localization key 
	 * @return the media
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "media", type = Accessor.Type.GETTER)
	public MediaModel getMedia(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(MEDIA, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSImageComponent.media</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the media
	 */
	@Accessor(qualifier = "media", type = Accessor.Type.SETTER)
	public void setMedia(final MediaModel value)
	{
		setMedia(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>CMSImageComponent.media</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the media
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "media", type = Accessor.Type.SETTER)
	public void setMedia(final MediaModel value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(MEDIA, loc, value);
	}
	
}
