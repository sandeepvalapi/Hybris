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

/**
 * Generated model class for type DynamicBannerComponent first defined at extension acceleratorcms.
 * <p>
 * Banner component implementation that displays media that code is dynamically evaluated basing on given parameters.
 */
@SuppressWarnings("all")
public class DynamicBannerComponentModel extends AbstractBannerComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DynamicBannerComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>DynamicBannerComponent.mediaCodePattern</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String MEDIACODEPATTERN = "mediaCodePattern";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DynamicBannerComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DynamicBannerComponentModel(final ItemModelContext ctx)
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
	public DynamicBannerComponentModel(final CatalogVersionModel _catalogVersion, final boolean _external, final String _uid)
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
	public DynamicBannerComponentModel(final CatalogVersionModel _catalogVersion, final boolean _external, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setExternal(_external);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DynamicBannerComponent.mediaCodePattern</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the mediaCodePattern - Media code pattern that will be used for evaluation.
	 */
	@Accessor(qualifier = "mediaCodePattern", type = Accessor.Type.GETTER)
	public String getMediaCodePattern()
	{
		return getPersistenceContext().getPropertyValue(MEDIACODEPATTERN);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DynamicBannerComponent.mediaCodePattern</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the mediaCodePattern - Media code pattern that will be used for evaluation.
	 */
	@Accessor(qualifier = "mediaCodePattern", type = Accessor.Type.SETTER)
	public void setMediaCodePattern(final String value)
	{
		getPersistenceContext().setPropertyValue(MEDIACODEPATTERN, value);
	}
	
}
