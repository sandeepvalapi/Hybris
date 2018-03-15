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
package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2lib.model.components.AbstractBannerComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type ImageMapComponent first defined at extension acceleratorcms.
 * <p>
 * It represents image map component that extends AbstractBannerComponent and contains one more attribute.
 */
@SuppressWarnings("all")
public class ImageMapComponentModel extends AbstractBannerComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ImageMapComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImageMapComponent.imageMapHTML</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String IMAGEMAPHTML = "imageMapHTML";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ImageMapComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ImageMapComponentModel(final ItemModelContext ctx)
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
	public ImageMapComponentModel(final CatalogVersionModel _catalogVersion, final boolean _external, final String _uid)
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
	public ImageMapComponentModel(final CatalogVersionModel _catalogVersion, final boolean _external, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setExternal(_external);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImageMapComponent.imageMapHTML</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the imageMapHTML - It is localized HTML string that is displayed under the banner.
	 */
	@Accessor(qualifier = "imageMapHTML", type = Accessor.Type.GETTER)
	public String getImageMapHTML()
	{
		return getImageMapHTML(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ImageMapComponent.imageMapHTML</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @param loc the value localization key 
	 * @return the imageMapHTML - It is localized HTML string that is displayed under the banner.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "imageMapHTML", type = Accessor.Type.GETTER)
	public String getImageMapHTML(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(IMAGEMAPHTML, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImageMapComponent.imageMapHTML</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the imageMapHTML - It is localized HTML string that is displayed under the banner.
	 */
	@Accessor(qualifier = "imageMapHTML", type = Accessor.Type.SETTER)
	public void setImageMapHTML(final String value)
	{
		setImageMapHTML(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ImageMapComponent.imageMapHTML</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the imageMapHTML - It is localized HTML string that is displayed under the banner.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "imageMapHTML", type = Accessor.Type.SETTER)
	public void setImageMapHTML(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(IMAGEMAPHTML, loc, value);
	}
	
}
