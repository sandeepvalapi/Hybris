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
package de.hybris.platform.cms2lib.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type AbstractBannerComponent first defined at extension cms2lib.
 */
@SuppressWarnings("all")
public class AbstractBannerComponentModel extends SimpleCMSComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractBannerComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractBannerComponent.media</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String MEDIA = "media";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractBannerComponent.urlLink</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String URLLINK = "urlLink";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractBannerComponent.external</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String EXTERNAL = "external";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractBannerComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractBannerComponentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _external initial attribute declared by type <code>AbstractBannerComponent</code> at extension <code>cms2lib</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public AbstractBannerComponentModel(final CatalogVersionModel _catalogVersion, final boolean _external, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setExternal(_external);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _external initial attribute declared by type <code>AbstractBannerComponent</code> at extension <code>cms2lib</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public AbstractBannerComponentModel(final CatalogVersionModel _catalogVersion, final boolean _external, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setExternal(_external);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBannerComponent.media</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the media
	 */
	@Accessor(qualifier = "media", type = Accessor.Type.GETTER)
	public MediaModel getMedia()
	{
		return getMedia(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBannerComponent.media</code> attribute defined at extension <code>cms2lib</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>AbstractBannerComponent.urlLink</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the urlLink
	 */
	@Accessor(qualifier = "urlLink", type = Accessor.Type.GETTER)
	public String getUrlLink()
	{
		return getPersistenceContext().getPropertyValue(URLLINK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractBannerComponent.external</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the external
	 */
	@Accessor(qualifier = "external", type = Accessor.Type.GETTER)
	public boolean isExternal()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(EXTERNAL));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractBannerComponent.external</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the external
	 */
	@Accessor(qualifier = "external", type = Accessor.Type.SETTER)
	public void setExternal(final boolean value)
	{
		getPersistenceContext().setPropertyValue(EXTERNAL, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractBannerComponent.media</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the media
	 */
	@Accessor(qualifier = "media", type = Accessor.Type.SETTER)
	public void setMedia(final MediaModel value)
	{
		setMedia(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractBannerComponent.media</code> attribute defined at extension <code>cms2lib</code>. 
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
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractBannerComponent.urlLink</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the urlLink
	 */
	@Accessor(qualifier = "urlLink", type = Accessor.Type.SETTER)
	public void setUrlLink(final String value)
	{
		getPersistenceContext().setPropertyValue(URLLINK, value);
	}
	
}
