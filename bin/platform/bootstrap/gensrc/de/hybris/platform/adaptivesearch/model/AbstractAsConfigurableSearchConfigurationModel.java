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
import de.hybris.platform.adaptivesearch.enums.AsBoostItemsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsBoostRulesMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsFacetsMergeMode;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsBoostRuleModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedFacetModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedItemModel;
import de.hybris.platform.adaptivesearch.model.AsFacetModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedFacetModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedItemModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

/**
 * Generated model class for type AbstractAsConfigurableSearchConfiguration first defined at extension adaptivesearch.
 */
@SuppressWarnings("all")
public class AbstractAsConfigurableSearchConfigurationModel extends AbstractAsSearchConfigurationModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractAsConfigurableSearchConfiguration";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsConfigurableSearchConfiguration.facetsMergeMode</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String FACETSMERGEMODE = "facetsMergeMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsConfigurableSearchConfiguration.boostItemsMergeMode</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String BOOSTITEMSMERGEMODE = "boostItemsMergeMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsConfigurableSearchConfiguration.boostRulesMergeMode</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String BOOSTRULESMERGEMODE = "boostRulesMergeMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsConfigurableSearchConfiguration.promotedFacets</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String PROMOTEDFACETS = "promotedFacets";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsConfigurableSearchConfiguration.facets</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String FACETS = "facets";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsConfigurableSearchConfiguration.excludedFacets</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String EXCLUDEDFACETS = "excludedFacets";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsConfigurableSearchConfiguration.promotedItems</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String PROMOTEDITEMS = "promotedItems";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsConfigurableSearchConfiguration.excludedItems</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String EXCLUDEDITEMS = "excludedItems";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsConfigurableSearchConfiguration.boostRules</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String BOOSTRULES = "boostRules";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractAsConfigurableSearchConfigurationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractAsConfigurableSearchConfigurationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _uid initial attribute declared by type <code>AbstractAsConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _uniqueIdx initial attribute declared by type <code>AbstractAsSearchConfiguration</code> at extension <code>adaptivesearch</code>
	 */
	@Deprecated
	public AbstractAsConfigurableSearchConfigurationModel(final String _uid, final String _uniqueIdx)
	{
		super();
		setUid(_uid);
		setUniqueIdx(_uniqueIdx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>AbstractAsConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>AbstractAsConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _uniqueIdx initial attribute declared by type <code>AbstractAsSearchConfiguration</code> at extension <code>adaptivesearch</code>
	 */
	@Deprecated
	public AbstractAsConfigurableSearchConfigurationModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid, final String _uniqueIdx)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
		setUniqueIdx(_uniqueIdx);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsConfigurableSearchConfiguration.boostItemsMergeMode</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the boostItemsMergeMode
	 */
	@Accessor(qualifier = "boostItemsMergeMode", type = Accessor.Type.GETTER)
	public AsBoostItemsMergeMode getBoostItemsMergeMode()
	{
		return getPersistenceContext().getPropertyValue(BOOSTITEMSMERGEMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsConfigurableSearchConfiguration.boostRules</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the boostRules
	 */
	@Accessor(qualifier = "boostRules", type = Accessor.Type.GETTER)
	public List<AsBoostRuleModel> getBoostRules()
	{
		return getPersistenceContext().getPropertyValue(BOOSTRULES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsConfigurableSearchConfiguration.boostRulesMergeMode</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the boostRulesMergeMode
	 */
	@Accessor(qualifier = "boostRulesMergeMode", type = Accessor.Type.GETTER)
	public AsBoostRulesMergeMode getBoostRulesMergeMode()
	{
		return getPersistenceContext().getPropertyValue(BOOSTRULESMERGEMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsConfigurableSearchConfiguration.excludedFacets</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the excludedFacets
	 */
	@Accessor(qualifier = "excludedFacets", type = Accessor.Type.GETTER)
	public List<AsExcludedFacetModel> getExcludedFacets()
	{
		return getPersistenceContext().getPropertyValue(EXCLUDEDFACETS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsConfigurableSearchConfiguration.excludedItems</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the excludedItems
	 */
	@Accessor(qualifier = "excludedItems", type = Accessor.Type.GETTER)
	public List<AsExcludedItemModel> getExcludedItems()
	{
		return getPersistenceContext().getPropertyValue(EXCLUDEDITEMS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsConfigurableSearchConfiguration.facets</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the facets
	 */
	@Accessor(qualifier = "facets", type = Accessor.Type.GETTER)
	public List<AsFacetModel> getFacets()
	{
		return getPersistenceContext().getPropertyValue(FACETS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsConfigurableSearchConfiguration.facetsMergeMode</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the facetsMergeMode
	 */
	@Accessor(qualifier = "facetsMergeMode", type = Accessor.Type.GETTER)
	public AsFacetsMergeMode getFacetsMergeMode()
	{
		return getPersistenceContext().getPropertyValue(FACETSMERGEMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsConfigurableSearchConfiguration.promotedFacets</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the promotedFacets
	 */
	@Accessor(qualifier = "promotedFacets", type = Accessor.Type.GETTER)
	public List<AsPromotedFacetModel> getPromotedFacets()
	{
		return getPersistenceContext().getPropertyValue(PROMOTEDFACETS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsConfigurableSearchConfiguration.promotedItems</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the promotedItems
	 */
	@Accessor(qualifier = "promotedItems", type = Accessor.Type.GETTER)
	public List<AsPromotedItemModel> getPromotedItems()
	{
		return getPersistenceContext().getPropertyValue(PROMOTEDITEMS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAsConfigurableSearchConfiguration.boostItemsMergeMode</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the boostItemsMergeMode
	 */
	@Accessor(qualifier = "boostItemsMergeMode", type = Accessor.Type.SETTER)
	public void setBoostItemsMergeMode(final AsBoostItemsMergeMode value)
	{
		getPersistenceContext().setPropertyValue(BOOSTITEMSMERGEMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAsConfigurableSearchConfiguration.boostRules</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the boostRules
	 */
	@Accessor(qualifier = "boostRules", type = Accessor.Type.SETTER)
	public void setBoostRules(final List<AsBoostRuleModel> value)
	{
		getPersistenceContext().setPropertyValue(BOOSTRULES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAsConfigurableSearchConfiguration.boostRulesMergeMode</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the boostRulesMergeMode
	 */
	@Accessor(qualifier = "boostRulesMergeMode", type = Accessor.Type.SETTER)
	public void setBoostRulesMergeMode(final AsBoostRulesMergeMode value)
	{
		getPersistenceContext().setPropertyValue(BOOSTRULESMERGEMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAsConfigurableSearchConfiguration.excludedFacets</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the excludedFacets
	 */
	@Accessor(qualifier = "excludedFacets", type = Accessor.Type.SETTER)
	public void setExcludedFacets(final List<AsExcludedFacetModel> value)
	{
		getPersistenceContext().setPropertyValue(EXCLUDEDFACETS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAsConfigurableSearchConfiguration.excludedItems</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the excludedItems
	 */
	@Accessor(qualifier = "excludedItems", type = Accessor.Type.SETTER)
	public void setExcludedItems(final List<AsExcludedItemModel> value)
	{
		getPersistenceContext().setPropertyValue(EXCLUDEDITEMS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAsConfigurableSearchConfiguration.facets</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the facets
	 */
	@Accessor(qualifier = "facets", type = Accessor.Type.SETTER)
	public void setFacets(final List<AsFacetModel> value)
	{
		getPersistenceContext().setPropertyValue(FACETS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAsConfigurableSearchConfiguration.facetsMergeMode</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the facetsMergeMode
	 */
	@Accessor(qualifier = "facetsMergeMode", type = Accessor.Type.SETTER)
	public void setFacetsMergeMode(final AsFacetsMergeMode value)
	{
		getPersistenceContext().setPropertyValue(FACETSMERGEMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAsConfigurableSearchConfiguration.promotedFacets</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the promotedFacets
	 */
	@Accessor(qualifier = "promotedFacets", type = Accessor.Type.SETTER)
	public void setPromotedFacets(final List<AsPromotedFacetModel> value)
	{
		getPersistenceContext().setPropertyValue(PROMOTEDFACETS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAsConfigurableSearchConfiguration.promotedItems</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the promotedItems
	 */
	@Accessor(qualifier = "promotedItems", type = Accessor.Type.SETTER)
	public void setPromotedItems(final List<AsPromotedItemModel> value)
	{
		getPersistenceContext().setPropertyValue(PROMOTEDITEMS, value);
	}
	
}
