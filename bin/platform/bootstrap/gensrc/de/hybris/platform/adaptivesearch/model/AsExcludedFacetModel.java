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
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetConfigurationModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AsExcludedFacet first defined at extension adaptivesearch.
 */
@SuppressWarnings("all")
public class AsExcludedFacetModel extends AbstractAsFacetConfigurationModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AsExcludedFacet";
	
	/**<i>Generated relation code constant for relation <code>AsConfigurableSearchConfiguration2ExcludedFacetRelation</code> defining source attribute <code>searchConfiguration</code> in extension <code>adaptivesearch</code>.</i>*/
	public static final String _ASCONFIGURABLESEARCHCONFIGURATION2EXCLUDEDFACETRELATION = "AsConfigurableSearchConfiguration2ExcludedFacetRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsExcludedFacet.searchConfigurationPOS</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String SEARCHCONFIGURATIONPOS = "searchConfigurationPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsExcludedFacet.searchConfiguration</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String SEARCHCONFIGURATION = "searchConfiguration";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AsExcludedFacetModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AsExcludedFacetModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _indexProperty initial attribute declared by type <code>AbstractAsFacetConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _searchConfiguration initial attribute declared by type <code>AsExcludedFacet</code> at extension <code>adaptivesearch</code>
	 * @param _uid initial attribute declared by type <code>AbstractAsConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _uniqueIdx initial attribute declared by type <code>AbstractAsFacetConfiguration</code> at extension <code>adaptivesearch</code>
	 */
	@Deprecated
	public AsExcludedFacetModel(final String _indexProperty, final AbstractAsConfigurableSearchConfigurationModel _searchConfiguration, final String _uid, final String _uniqueIdx)
	{
		super();
		setIndexProperty(_indexProperty);
		setSearchConfiguration(_searchConfiguration);
		setUid(_uid);
		setUniqueIdx(_uniqueIdx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>AbstractAsConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _indexProperty initial attribute declared by type <code>AbstractAsFacetConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _searchConfiguration initial attribute declared by type <code>AsExcludedFacet</code> at extension <code>adaptivesearch</code>
	 * @param _uid initial attribute declared by type <code>AbstractAsConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _uniqueIdx initial attribute declared by type <code>AbstractAsFacetConfiguration</code> at extension <code>adaptivesearch</code>
	 */
	@Deprecated
	public AsExcludedFacetModel(final CatalogVersionModel _catalogVersion, final String _indexProperty, final ItemModel _owner, final AbstractAsConfigurableSearchConfigurationModel _searchConfiguration, final String _uid, final String _uniqueIdx)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setIndexProperty(_indexProperty);
		setOwner(_owner);
		setSearchConfiguration(_searchConfiguration);
		setUid(_uid);
		setUniqueIdx(_uniqueIdx);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AsExcludedFacet.searchConfiguration</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the searchConfiguration
	 */
	@Accessor(qualifier = "searchConfiguration", type = Accessor.Type.GETTER)
	public AbstractAsConfigurableSearchConfigurationModel getSearchConfiguration()
	{
		return getPersistenceContext().getPropertyValue(SEARCHCONFIGURATION);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AsExcludedFacet.searchConfiguration</code> attribute defined at extension <code>adaptivesearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the searchConfiguration
	 */
	@Accessor(qualifier = "searchConfiguration", type = Accessor.Type.SETTER)
	public void setSearchConfiguration(final AbstractAsConfigurableSearchConfigurationModel value)
	{
		getPersistenceContext().setPropertyValue(SEARCHCONFIGURATION, value);
	}
	
}
