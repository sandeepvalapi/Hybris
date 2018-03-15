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
package de.hybris.platform.core.model.c2l;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Collection;
import java.util.Set;

/**
 * Generated model class for type Country first defined at extension core.
 */
@SuppressWarnings("all")
public class CountryModel extends C2LItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Country";
	
	/**<i>Generated relation code constant for relation <code>ZoneCountryRelation</code> defining source attribute <code>zones</code> in extension <code>deliveryzone</code>.</i>*/
	public static final String _ZONECOUNTRYRELATION = "ZoneCountryRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Country.regions</code> attribute defined at extension <code>core</code>. */
	public static final String REGIONS = "regions";
	
	/** <i>Generated constant</i> - Attribute key of <code>Country.zones</code> attribute defined at extension <code>deliveryzone</code>. */
	public static final String ZONES = "zones";
	
	/** <i>Generated constant</i> - Attribute key of <code>Country.baseStores</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String BASESTORES = "baseStores";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CountryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CountryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _isocode initial attribute declared by type <code>Country</code> at extension <code>core</code>
	 */
	@Deprecated
	public CountryModel(final String _isocode)
	{
		super();
		setIsocode(_isocode);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _isocode initial attribute declared by type <code>Country</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CountryModel(final String _isocode, final ItemModel _owner)
	{
		super();
		setIsocode(_isocode);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Country.baseStores</code> attribute defined at extension <code>commerceservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the baseStores
	 */
	@Accessor(qualifier = "baseStores", type = Accessor.Type.GETTER)
	public Set<BaseStoreModel> getBaseStores()
	{
		return getPersistenceContext().getPropertyValue(BASESTORES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Country.regions</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the regions
	 */
	@Accessor(qualifier = "regions", type = Accessor.Type.GETTER)
	public Collection<RegionModel> getRegions()
	{
		return getPersistenceContext().getPropertyValue(REGIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Country.zones</code> attribute defined at extension <code>deliveryzone</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the zones
	 */
	@Accessor(qualifier = "zones", type = Accessor.Type.GETTER)
	public Set<ZoneModel> getZones()
	{
		return getPersistenceContext().getPropertyValue(ZONES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Country.baseStores</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the baseStores
	 */
	@Accessor(qualifier = "baseStores", type = Accessor.Type.SETTER)
	public void setBaseStores(final Set<BaseStoreModel> value)
	{
		getPersistenceContext().setPropertyValue(BASESTORES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Country.regions</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the regions
	 */
	@Accessor(qualifier = "regions", type = Accessor.Type.SETTER)
	public void setRegions(final Collection<RegionModel> value)
	{
		getPersistenceContext().setPropertyValue(REGIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Country.zones</code> attribute defined at extension <code>deliveryzone</code>. 
	 *  
	 * @param value the zones
	 */
	@Accessor(qualifier = "zones", type = Accessor.Type.SETTER)
	public void setZones(final Set<ZoneModel> value)
	{
		getPersistenceContext().setPropertyValue(ZONES, value);
	}
	
}
