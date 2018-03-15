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
package de.hybris.platform.cms2.model.restrictions;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.restrictions.CMSTimeRestrictionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Collection;

/**
 * Generated model class for type CMSBaseStoreTimeRestriction first defined at extension cms2.
 * <p>
 * Extension of the CMSTimeRestriction to restrict for a specific set of BaseStores.
 */
@SuppressWarnings("all")
public class CMSBaseStoreTimeRestrictionModel extends CMSTimeRestrictionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CMSBaseStoreTimeRestriction";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSBaseStoreTimeRestriction.passIfStoreDoesntMatch</code> attribute defined at extension <code>cms2</code>. */
	public static final String PASSIFSTOREDOESNTMATCH = "passIfStoreDoesntMatch";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSBaseStoreTimeRestriction.baseStores</code> attribute defined at extension <code>cms2</code>. */
	public static final String BASESTORES = "baseStores";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CMSBaseStoreTimeRestrictionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CMSBaseStoreTimeRestrictionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseStores initial attribute declared by type <code>CMSBaseStoreTimeRestriction</code> at extension <code>cms2</code>
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public CMSBaseStoreTimeRestrictionModel(final Collection<BaseStoreModel> _baseStores, final CatalogVersionModel _catalogVersion, final String _uid)
	{
		super();
		setBaseStores(_baseStores);
		setCatalogVersion(_catalogVersion);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseStores initial attribute declared by type <code>CMSBaseStoreTimeRestriction</code> at extension <code>cms2</code>
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public CMSBaseStoreTimeRestrictionModel(final Collection<BaseStoreModel> _baseStores, final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setBaseStores(_baseStores);
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSBaseStoreTimeRestriction.baseStores</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the baseStores
	 */
	@Accessor(qualifier = "baseStores", type = Accessor.Type.GETTER)
	public Collection<BaseStoreModel> getBaseStores()
	{
		return getPersistenceContext().getPropertyValue(BASESTORES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSBaseStoreTimeRestriction.passIfStoreDoesntMatch</code> attribute defined at extension <code>cms2</code>. 
	 * @return the passIfStoreDoesntMatch - If session store doesn't match store of the restriction then allow a pass
	 */
	@Accessor(qualifier = "passIfStoreDoesntMatch", type = Accessor.Type.GETTER)
	public Boolean getPassIfStoreDoesntMatch()
	{
		return getPersistenceContext().getPropertyValue(PASSIFSTOREDOESNTMATCH);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSBaseStoreTimeRestriction.baseStores</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the baseStores
	 */
	@Accessor(qualifier = "baseStores", type = Accessor.Type.SETTER)
	public void setBaseStores(final Collection<BaseStoreModel> value)
	{
		getPersistenceContext().setPropertyValue(BASESTORES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSBaseStoreTimeRestriction.passIfStoreDoesntMatch</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the passIfStoreDoesntMatch - If session store doesn't match store of the restriction then allow a pass
	 */
	@Accessor(qualifier = "passIfStoreDoesntMatch", type = Accessor.Type.SETTER)
	public void setPassIfStoreDoesntMatch(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(PASSIFSTOREDOESNTMATCH, value);
	}
	
}
