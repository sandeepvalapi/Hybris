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
import de.hybris.platform.adaptivesearch.enums.AsBoostOperator;
import de.hybris.platform.adaptivesearch.enums.AsBoostType;
import de.hybris.platform.adaptivesearch.model.AbstractAsBoostRuleConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AsBoostRule first defined at extension adaptivesearch.
 */
@SuppressWarnings("all")
public class AsBoostRuleModel extends AbstractAsBoostRuleConfigurationModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AsBoostRule";
	
	/**<i>Generated relation code constant for relation <code>AsConfigurableSearchConfiguration2BoostRuleRelation</code> defining source attribute <code>searchConfiguration</code> in extension <code>adaptivesearch</code>.</i>*/
	public static final String _ASCONFIGURABLESEARCHCONFIGURATION2BOOSTRULERELATION = "AsConfigurableSearchConfiguration2BoostRuleRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsBoostRule.indexProperty</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String INDEXPROPERTY = "indexProperty";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsBoostRule.operator</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String OPERATOR = "operator";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsBoostRule.value</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String VALUE = "value";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsBoostRule.boostType</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String BOOSTTYPE = "boostType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsBoostRule.boost</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String BOOST = "boost";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsBoostRule.searchConfigurationPOS</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String SEARCHCONFIGURATIONPOS = "searchConfigurationPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>AsBoostRule.searchConfiguration</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String SEARCHCONFIGURATION = "searchConfiguration";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AsBoostRuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AsBoostRuleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _boost initial attribute declared by type <code>AsBoostRule</code> at extension <code>adaptivesearch</code>
	 * @param _indexProperty initial attribute declared by type <code>AsBoostRule</code> at extension <code>adaptivesearch</code>
	 * @param _searchConfiguration initial attribute declared by type <code>AsBoostRule</code> at extension <code>adaptivesearch</code>
	 * @param _uid initial attribute declared by type <code>AbstractAsConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _value initial attribute declared by type <code>AsBoostRule</code> at extension <code>adaptivesearch</code>
	 */
	@Deprecated
	public AsBoostRuleModel(final Float _boost, final String _indexProperty, final AbstractAsConfigurableSearchConfigurationModel _searchConfiguration, final String _uid, final String _value)
	{
		super();
		setBoost(_boost);
		setIndexProperty(_indexProperty);
		setSearchConfiguration(_searchConfiguration);
		setUid(_uid);
		setValue(_value);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _boost initial attribute declared by type <code>AsBoostRule</code> at extension <code>adaptivesearch</code>
	 * @param _catalogVersion initial attribute declared by type <code>AbstractAsConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _indexProperty initial attribute declared by type <code>AsBoostRule</code> at extension <code>adaptivesearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _searchConfiguration initial attribute declared by type <code>AsBoostRule</code> at extension <code>adaptivesearch</code>
	 * @param _uid initial attribute declared by type <code>AbstractAsConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _value initial attribute declared by type <code>AsBoostRule</code> at extension <code>adaptivesearch</code>
	 */
	@Deprecated
	public AsBoostRuleModel(final Float _boost, final CatalogVersionModel _catalogVersion, final String _indexProperty, final ItemModel _owner, final AbstractAsConfigurableSearchConfigurationModel _searchConfiguration, final String _uid, final String _value)
	{
		super();
		setBoost(_boost);
		setCatalogVersion(_catalogVersion);
		setIndexProperty(_indexProperty);
		setOwner(_owner);
		setSearchConfiguration(_searchConfiguration);
		setUid(_uid);
		setValue(_value);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AsBoostRule.boost</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the boost
	 */
	@Accessor(qualifier = "boost", type = Accessor.Type.GETTER)
	public Float getBoost()
	{
		return getPersistenceContext().getPropertyValue(BOOST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AsBoostRule.boostType</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the boostType
	 */
	@Accessor(qualifier = "boostType", type = Accessor.Type.GETTER)
	public AsBoostType getBoostType()
	{
		return getPersistenceContext().getPropertyValue(BOOSTTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AsBoostRule.indexProperty</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the indexProperty
	 */
	@Accessor(qualifier = "indexProperty", type = Accessor.Type.GETTER)
	public String getIndexProperty()
	{
		return getPersistenceContext().getPropertyValue(INDEXPROPERTY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AsBoostRule.operator</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the operator
	 */
	@Accessor(qualifier = "operator", type = Accessor.Type.GETTER)
	public AsBoostOperator getOperator()
	{
		return getPersistenceContext().getPropertyValue(OPERATOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AsBoostRule.searchConfiguration</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the searchConfiguration
	 */
	@Accessor(qualifier = "searchConfiguration", type = Accessor.Type.GETTER)
	public AbstractAsConfigurableSearchConfigurationModel getSearchConfiguration()
	{
		return getPersistenceContext().getPropertyValue(SEARCHCONFIGURATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AsBoostRule.value</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.GETTER)
	public String getValue()
	{
		return getPersistenceContext().getPropertyValue(VALUE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AsBoostRule.boost</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the boost
	 */
	@Accessor(qualifier = "boost", type = Accessor.Type.SETTER)
	public void setBoost(final Float value)
	{
		getPersistenceContext().setPropertyValue(BOOST, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AsBoostRule.boostType</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the boostType
	 */
	@Accessor(qualifier = "boostType", type = Accessor.Type.SETTER)
	public void setBoostType(final AsBoostType value)
	{
		getPersistenceContext().setPropertyValue(BOOSTTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AsBoostRule.indexProperty</code> attribute defined at extension <code>adaptivesearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the indexProperty
	 */
	@Accessor(qualifier = "indexProperty", type = Accessor.Type.SETTER)
	public void setIndexProperty(final String value)
	{
		getPersistenceContext().setPropertyValue(INDEXPROPERTY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AsBoostRule.operator</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the operator
	 */
	@Accessor(qualifier = "operator", type = Accessor.Type.SETTER)
	public void setOperator(final AsBoostOperator value)
	{
		getPersistenceContext().setPropertyValue(OPERATOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AsBoostRule.searchConfiguration</code> attribute defined at extension <code>adaptivesearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the searchConfiguration
	 */
	@Accessor(qualifier = "searchConfiguration", type = Accessor.Type.SETTER)
	public void setSearchConfiguration(final AbstractAsConfigurableSearchConfigurationModel value)
	{
		getPersistenceContext().setPropertyValue(SEARCHCONFIGURATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AsBoostRule.value</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.SETTER)
	public void setValue(final String value)
	{
		getPersistenceContext().setPropertyValue(VALUE, value);
	}
	
}
