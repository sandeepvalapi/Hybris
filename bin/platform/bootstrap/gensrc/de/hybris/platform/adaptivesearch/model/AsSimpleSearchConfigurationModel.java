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
package de.hybris.platform.adaptivesearch.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsSimpleSearchProfileModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AsSimpleSearchConfiguration first defined at extension adaptivesearch.
 */
@SuppressWarnings("all")
public class AsSimpleSearchConfigurationModel extends AbstractAsConfigurableSearchConfigurationModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AsSimpleSearchConfiguration";
	
	/**<i>Generated relation code constant for relation <code>AsSimpleSearchProfile2SimpleSearchConfiguration</code> defining source attribute <code>searchProfile</code> in extension <code>adaptivesearch</code>.</i>*/
	public static final String _ASSIMPLESEARCHPROFILE2SIMPLESEARCHCONFIGURATION = "AsSimpleSearchProfile2SimpleSearchConfiguration";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsSimpleSearchConfiguration.searchProfile</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String SEARCHPROFILE = "searchProfile";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AsSimpleSearchConfigurationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AsSimpleSearchConfigurationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _searchProfile initial attribute declared by type <code>AsSimpleSearchConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _uid initial attribute declared by type <code>AbstractAsConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _uniqueIdx initial attribute declared by type <code>AbstractAsSearchConfiguration</code> at extension <code>adaptivesearch</code>
	 */
	@Deprecated
	public AsSimpleSearchConfigurationModel(final AsSimpleSearchProfileModel _searchProfile, final String _uid, final String _uniqueIdx)
	{
		super();
		setSearchProfile(_searchProfile);
		setUid(_uid);
		setUniqueIdx(_uniqueIdx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>AbstractAsConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _searchProfile initial attribute declared by type <code>AsSimpleSearchConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _uid initial attribute declared by type <code>AbstractAsConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _uniqueIdx initial attribute declared by type <code>AbstractAsSearchConfiguration</code> at extension <code>adaptivesearch</code>
	 */
	@Deprecated
	public AsSimpleSearchConfigurationModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final AsSimpleSearchProfileModel _searchProfile, final String _uid, final String _uniqueIdx)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setSearchProfile(_searchProfile);
		setUid(_uid);
		setUniqueIdx(_uniqueIdx);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AsSimpleSearchConfiguration.searchProfile</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the searchProfile
	 */
	@Accessor(qualifier = "searchProfile", type = Accessor.Type.GETTER)
	public AsSimpleSearchProfileModel getSearchProfile()
	{
		return getPersistenceContext().getPropertyValue(SEARCHPROFILE);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AsSimpleSearchConfiguration.searchProfile</code> attribute defined at extension <code>adaptivesearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the searchProfile
	 */
	@Accessor(qualifier = "searchProfile", type = Accessor.Type.SETTER)
	public void setSearchProfile(final AsSimpleSearchProfileModel value)
	{
		getPersistenceContext().setPropertyValue(SEARCHPROFILE, value);
	}
	
}
