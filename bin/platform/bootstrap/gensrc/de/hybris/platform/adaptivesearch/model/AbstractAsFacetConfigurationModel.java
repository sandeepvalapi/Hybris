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
import de.hybris.platform.adaptivesearch.enums.AsFacetType;
import de.hybris.platform.adaptivesearch.model.AbstractAsItemConfigurationModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AbstractAsFacetConfiguration first defined at extension adaptivesearch.
 */
@SuppressWarnings("all")
public class AbstractAsFacetConfigurationModel extends AbstractAsItemConfigurationModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractAsFacetConfiguration";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsFacetConfiguration.indexProperty</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String INDEXPROPERTY = "indexProperty";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsFacetConfiguration.facetType</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String FACETTYPE = "facetType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsFacetConfiguration.priority</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String PRIORITY = "priority";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsFacetConfiguration.valuesSortProvider</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String VALUESSORTPROVIDER = "valuesSortProvider";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsFacetConfiguration.valuesDisplayNameProvider</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String VALUESDISPLAYNAMEPROVIDER = "valuesDisplayNameProvider";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsFacetConfiguration.topValuesProvider</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String TOPVALUESPROVIDER = "topValuesProvider";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAsFacetConfiguration.uniqueIdx</code> attribute defined at extension <code>adaptivesearch</code>. */
	public static final String UNIQUEIDX = "uniqueIdx";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractAsFacetConfigurationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractAsFacetConfigurationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _indexProperty initial attribute declared by type <code>AbstractAsFacetConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _uid initial attribute declared by type <code>AbstractAsConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _uniqueIdx initial attribute declared by type <code>AbstractAsFacetConfiguration</code> at extension <code>adaptivesearch</code>
	 */
	@Deprecated
	public AbstractAsFacetConfigurationModel(final String _indexProperty, final String _uid, final String _uniqueIdx)
	{
		super();
		setIndexProperty(_indexProperty);
		setUid(_uid);
		setUniqueIdx(_uniqueIdx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>AbstractAsConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _indexProperty initial attribute declared by type <code>AbstractAsFacetConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>AbstractAsConfiguration</code> at extension <code>adaptivesearch</code>
	 * @param _uniqueIdx initial attribute declared by type <code>AbstractAsFacetConfiguration</code> at extension <code>adaptivesearch</code>
	 */
	@Deprecated
	public AbstractAsFacetConfigurationModel(final CatalogVersionModel _catalogVersion, final String _indexProperty, final ItemModel _owner, final String _uid, final String _uniqueIdx)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setIndexProperty(_indexProperty);
		setOwner(_owner);
		setUid(_uid);
		setUniqueIdx(_uniqueIdx);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsFacetConfiguration.facetType</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the facetType
	 */
	@Accessor(qualifier = "facetType", type = Accessor.Type.GETTER)
	public AsFacetType getFacetType()
	{
		return getPersistenceContext().getPropertyValue(FACETTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsFacetConfiguration.indexProperty</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the indexProperty
	 */
	@Accessor(qualifier = "indexProperty", type = Accessor.Type.GETTER)
	public String getIndexProperty()
	{
		return getPersistenceContext().getPropertyValue(INDEXPROPERTY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsFacetConfiguration.priority</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public Integer getPriority()
	{
		return getPersistenceContext().getPropertyValue(PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsFacetConfiguration.topValuesProvider</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the topValuesProvider
	 */
	@Accessor(qualifier = "topValuesProvider", type = Accessor.Type.GETTER)
	public String getTopValuesProvider()
	{
		return getPersistenceContext().getPropertyValue(TOPVALUESPROVIDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsFacetConfiguration.uniqueIdx</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the uniqueIdx
	 */
	@Accessor(qualifier = "uniqueIdx", type = Accessor.Type.GETTER)
	public String getUniqueIdx()
	{
		return getPersistenceContext().getPropertyValue(UNIQUEIDX);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsFacetConfiguration.valuesDisplayNameProvider</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the valuesDisplayNameProvider
	 */
	@Accessor(qualifier = "valuesDisplayNameProvider", type = Accessor.Type.GETTER)
	public String getValuesDisplayNameProvider()
	{
		return getPersistenceContext().getPropertyValue(VALUESDISPLAYNAMEPROVIDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAsFacetConfiguration.valuesSortProvider</code> attribute defined at extension <code>adaptivesearch</code>. 
	 * @return the valuesSortProvider
	 */
	@Accessor(qualifier = "valuesSortProvider", type = Accessor.Type.GETTER)
	public String getValuesSortProvider()
	{
		return getPersistenceContext().getPropertyValue(VALUESSORTPROVIDER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAsFacetConfiguration.facetType</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the facetType
	 */
	@Accessor(qualifier = "facetType", type = Accessor.Type.SETTER)
	public void setFacetType(final AsFacetType value)
	{
		getPersistenceContext().setPropertyValue(FACETTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractAsFacetConfiguration.indexProperty</code> attribute defined at extension <code>adaptivesearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the indexProperty
	 */
	@Accessor(qualifier = "indexProperty", type = Accessor.Type.SETTER)
	public void setIndexProperty(final String value)
	{
		getPersistenceContext().setPropertyValue(INDEXPROPERTY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAsFacetConfiguration.priority</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PRIORITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAsFacetConfiguration.topValuesProvider</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the topValuesProvider
	 */
	@Accessor(qualifier = "topValuesProvider", type = Accessor.Type.SETTER)
	public void setTopValuesProvider(final String value)
	{
		getPersistenceContext().setPropertyValue(TOPVALUESPROVIDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AbstractAsFacetConfiguration.uniqueIdx</code> attribute defined at extension <code>adaptivesearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the uniqueIdx
	 */
	@Accessor(qualifier = "uniqueIdx", type = Accessor.Type.SETTER)
	public void setUniqueIdx(final String value)
	{
		getPersistenceContext().setPropertyValue(UNIQUEIDX, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAsFacetConfiguration.valuesDisplayNameProvider</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the valuesDisplayNameProvider
	 */
	@Accessor(qualifier = "valuesDisplayNameProvider", type = Accessor.Type.SETTER)
	public void setValuesDisplayNameProvider(final String value)
	{
		getPersistenceContext().setPropertyValue(VALUESDISPLAYNAMEPROVIDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAsFacetConfiguration.valuesSortProvider</code> attribute defined at extension <code>adaptivesearch</code>. 
	 *  
	 * @param value the valuesSortProvider
	 */
	@Accessor(qualifier = "valuesSortProvider", type = Accessor.Type.SETTER)
	public void setValuesSortProvider(final String value)
	{
		getPersistenceContext().setPropertyValue(VALUESSORTPROVIDER, value);
	}
	
}
