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
package de.hybris.platform.commerceservices.model.storelocator;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type StoreLocatorFeature first defined at extension commerceservices.
 * <p>
 * Store locator feature describes single feature that store point of service supports.
 */
@SuppressWarnings("all")
public class StoreLocatorFeatureModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "StoreLocatorFeature";
	
	/**<i>Generated relation code constant for relation <code>StoreLocation2StoreLocatorFeature</code> defining source attribute <code>pointOfServices</code> in extension <code>commerceservices</code>.</i>*/
	public static final String _STORELOCATION2STORELOCATORFEATURE = "StoreLocation2StoreLocatorFeature";
	
	/** <i>Generated constant</i> - Attribute key of <code>StoreLocatorFeature.code</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>StoreLocatorFeature.name</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>StoreLocatorFeature.icon</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String ICON = "icon";
	
	/** <i>Generated constant</i> - Attribute key of <code>StoreLocatorFeature.pointOfServices</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String POINTOFSERVICES = "pointOfServices";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public StoreLocatorFeatureModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public StoreLocatorFeatureModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public StoreLocatorFeatureModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreLocatorFeature.code</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the code - Unique code of store locator feature.
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreLocatorFeature.icon</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the icon - Icon that can represent the feature
	 */
	@Accessor(qualifier = "icon", type = Accessor.Type.GETTER)
	public MediaModel getIcon()
	{
		return getPersistenceContext().getPropertyValue(ICON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreLocatorFeature.name</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the name - Description of the store locator feature
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreLocatorFeature.name</code> attribute defined at extension <code>commerceservices</code>. 
	 * @param loc the value localization key 
	 * @return the name - Description of the store locator feature
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreLocatorFeature.pointOfServices</code> attribute defined at extension <code>commerceservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the pointOfServices
	 */
	@Accessor(qualifier = "pointOfServices", type = Accessor.Type.GETTER)
	public Collection<PointOfServiceModel> getPointOfServices()
	{
		return getPersistenceContext().getPropertyValue(POINTOFSERVICES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StoreLocatorFeature.code</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the code - Unique code of store locator feature.
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StoreLocatorFeature.icon</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the icon - Icon that can represent the feature
	 */
	@Accessor(qualifier = "icon", type = Accessor.Type.SETTER)
	public void setIcon(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(ICON, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StoreLocatorFeature.name</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the name - Description of the store locator feature
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>StoreLocatorFeature.name</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the name - Description of the store locator feature
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StoreLocatorFeature.pointOfServices</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the pointOfServices
	 */
	@Accessor(qualifier = "pointOfServices", type = Accessor.Type.SETTER)
	public void setPointOfServices(final Collection<PointOfServiceModel> value)
	{
		getPersistenceContext().setPropertyValue(POINTOFSERVICES, value);
	}
	
}
