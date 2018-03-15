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
import de.hybris.platform.cms2lib.enums.RotatingImagesComponentEffect;
import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

/**
 * Generated model class for type RotatingImagesComponent first defined at extension cms2lib.
 */
@SuppressWarnings("all")
public class RotatingImagesComponentModel extends SimpleCMSComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RotatingImagesComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>RotatingImagesComponent.timeout</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String TIMEOUT = "timeout";
	
	/** <i>Generated constant</i> - Attribute key of <code>RotatingImagesComponent.effect</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String EFFECT = "effect";
	
	/** <i>Generated constant</i> - Attribute key of <code>RotatingImagesComponent.banners</code> attribute defined at extension <code>cms2lib</code>. */
	public static final String BANNERS = "banners";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RotatingImagesComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RotatingImagesComponentModel(final ItemModelContext ctx)
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
	public RotatingImagesComponentModel(final CatalogVersionModel _catalogVersion, final String _uid)
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
	public RotatingImagesComponentModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RotatingImagesComponent.banners</code> attribute defined at extension <code>cms2lib</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the banners
	 */
	@Accessor(qualifier = "banners", type = Accessor.Type.GETTER)
	public List<BannerComponentModel> getBanners()
	{
		return getPersistenceContext().getPropertyValue(BANNERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RotatingImagesComponent.effect</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the effect
	 */
	@Accessor(qualifier = "effect", type = Accessor.Type.GETTER)
	public RotatingImagesComponentEffect getEffect()
	{
		return getPersistenceContext().getPropertyValue(EFFECT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RotatingImagesComponent.timeout</code> attribute defined at extension <code>cms2lib</code>. 
	 * @return the timeout
	 */
	@Accessor(qualifier = "timeout", type = Accessor.Type.GETTER)
	public Integer getTimeout()
	{
		return getPersistenceContext().getPropertyValue(TIMEOUT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RotatingImagesComponent.banners</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the banners
	 */
	@Accessor(qualifier = "banners", type = Accessor.Type.SETTER)
	public void setBanners(final List<BannerComponentModel> value)
	{
		getPersistenceContext().setPropertyValue(BANNERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RotatingImagesComponent.effect</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the effect
	 */
	@Accessor(qualifier = "effect", type = Accessor.Type.SETTER)
	public void setEffect(final RotatingImagesComponentEffect value)
	{
		getPersistenceContext().setPropertyValue(EFFECT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RotatingImagesComponent.timeout</code> attribute defined at extension <code>cms2lib</code>. 
	 *  
	 * @param value the timeout
	 */
	@Accessor(qualifier = "timeout", type = Accessor.Type.SETTER)
	public void setTimeout(final Integer value)
	{
		getPersistenceContext().setPropertyValue(TIMEOUT, value);
	}
	
}
