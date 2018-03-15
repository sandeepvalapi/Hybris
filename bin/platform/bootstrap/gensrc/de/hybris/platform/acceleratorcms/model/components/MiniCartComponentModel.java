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
import de.hybris.platform.acceleratorcms.enums.CartTotalDisplayType;
import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type MiniCartComponent first defined at extension acceleratorcms.
 * <p>
 * It represents mini cart component that can be configured to show some information about cart content.
 */
@SuppressWarnings("all")
public class MiniCartComponentModel extends SimpleCMSComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MiniCartComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>MiniCartComponent.title</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String TITLE = "title";
	
	/** <i>Generated constant</i> - Attribute key of <code>MiniCartComponent.totalDisplay</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String TOTALDISPLAY = "totalDisplay";
	
	/** <i>Generated constant</i> - Attribute key of <code>MiniCartComponent.shownProductCount</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String SHOWNPRODUCTCOUNT = "shownProductCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>MiniCartComponent.lightboxBannerComponent</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String LIGHTBOXBANNERCOMPONENT = "lightboxBannerComponent";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MiniCartComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MiniCartComponentModel(final ItemModelContext ctx)
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
	public MiniCartComponentModel(final CatalogVersionModel _catalogVersion, final String _uid)
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
	public MiniCartComponentModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MiniCartComponent.lightboxBannerComponent</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the lightboxBannerComponent - It is a banner component that can be displayed in the mini cart.
	 */
	@Accessor(qualifier = "lightboxBannerComponent", type = Accessor.Type.GETTER)
	public SimpleBannerComponentModel getLightboxBannerComponent()
	{
		return getPersistenceContext().getPropertyValue(LIGHTBOXBANNERCOMPONENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MiniCartComponent.shownProductCount</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the shownProductCount - It controls the maximum number of products to display in the component.
	 */
	@Accessor(qualifier = "shownProductCount", type = Accessor.Type.GETTER)
	public int getShownProductCount()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(SHOWNPRODUCTCOUNT));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MiniCartComponent.title</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the title - It is localized title of the component.
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle()
	{
		return getTitle(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>MiniCartComponent.title</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @param loc the value localization key 
	 * @return the title - It is localized title of the component.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(TITLE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MiniCartComponent.totalDisplay</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the totalDisplay - This attribute determines the type of displayed total cart value.
	 */
	@Accessor(qualifier = "totalDisplay", type = Accessor.Type.GETTER)
	public CartTotalDisplayType getTotalDisplay()
	{
		return getPersistenceContext().getPropertyValue(TOTALDISPLAY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MiniCartComponent.lightboxBannerComponent</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the lightboxBannerComponent - It is a banner component that can be displayed in the mini cart.
	 */
	@Accessor(qualifier = "lightboxBannerComponent", type = Accessor.Type.SETTER)
	public void setLightboxBannerComponent(final SimpleBannerComponentModel value)
	{
		getPersistenceContext().setPropertyValue(LIGHTBOXBANNERCOMPONENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MiniCartComponent.shownProductCount</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the shownProductCount - It controls the maximum number of products to display in the component.
	 */
	@Accessor(qualifier = "shownProductCount", type = Accessor.Type.SETTER)
	public void setShownProductCount(final int value)
	{
		getPersistenceContext().setPropertyValue(SHOWNPRODUCTCOUNT, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MiniCartComponent.title</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the title - It is localized title of the component.
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value)
	{
		setTitle(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>MiniCartComponent.title</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the title - It is localized title of the component.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(TITLE, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MiniCartComponent.totalDisplay</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the totalDisplay - This attribute determines the type of displayed total cart value.
	 */
	@Accessor(qualifier = "totalDisplay", type = Accessor.Type.SETTER)
	public void setTotalDisplay(final CartTotalDisplayType value)
	{
		getPersistenceContext().setPropertyValue(TOTALDISPLAY, value);
	}
	
}
