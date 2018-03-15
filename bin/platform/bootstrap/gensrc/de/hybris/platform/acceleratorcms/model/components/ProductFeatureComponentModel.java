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
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type ProductFeatureComponent first defined at extension acceleratorcms.
 * <p>
 * Represents product feature component that displays some commercial information about the product.
 */
@SuppressWarnings("all")
public class ProductFeatureComponentModel extends SimpleCMSComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductFeatureComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeatureComponent.title</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String TITLE = "title";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeatureComponent.description</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeatureComponent.media</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String MEDIA = "media";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeatureComponent.product</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String PRODUCT = "product";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductFeatureComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductFeatureComponentModel(final ItemModelContext ctx)
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
	public ProductFeatureComponentModel(final CatalogVersionModel _catalogVersion, final String _uid)
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
	public ProductFeatureComponentModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeatureComponent.description</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the description - Localized description of the component.
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeatureComponent.description</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @param loc the value localization key 
	 * @return the description - Localized description of the component.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeatureComponent.media</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the media - Localized media that is displayed in the component.
	 */
	@Accessor(qualifier = "media", type = Accessor.Type.GETTER)
	public MediaModel getMedia()
	{
		return getMedia(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeatureComponent.media</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @param loc the value localization key 
	 * @return the media - Localized media that is displayed in the component.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "media", type = Accessor.Type.GETTER)
	public MediaModel getMedia(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(MEDIA, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeatureComponent.product</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.GETTER)
	public ProductModel getProduct()
	{
		return getPersistenceContext().getPropertyValue(PRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeatureComponent.title</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the title - Localized title of the component.
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle()
	{
		return getTitle(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeatureComponent.title</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @param loc the value localization key 
	 * @return the title - Localized title of the component.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(TITLE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductFeatureComponent.description</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the description - Localized description of the component.
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ProductFeatureComponent.description</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the description - Localized description of the component.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductFeatureComponent.media</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the media - Localized media that is displayed in the component.
	 */
	@Accessor(qualifier = "media", type = Accessor.Type.SETTER)
	public void setMedia(final MediaModel value)
	{
		setMedia(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ProductFeatureComponent.media</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the media - Localized media that is displayed in the component.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "media", type = Accessor.Type.SETTER)
	public void setMedia(final MediaModel value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(MEDIA, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductFeatureComponent.product</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.SETTER)
	public void setProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(PRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductFeatureComponent.title</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the title - Localized title of the component.
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value)
	{
		setTitle(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ProductFeatureComponent.title</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the title - Localized title of the component.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(TITLE, loc, value);
	}
	
}
