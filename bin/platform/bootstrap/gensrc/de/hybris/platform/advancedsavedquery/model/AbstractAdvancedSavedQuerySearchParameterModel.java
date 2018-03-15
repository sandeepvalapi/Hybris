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
package de.hybris.platform.advancedsavedquery.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.advancedsavedquery.enums.AdvancedQueryComparatorEnum;
import de.hybris.platform.advancedsavedquery.enums.EmptyParamEnum;
import de.hybris.platform.advancedsavedquery.model.WherePartModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type AbstractAdvancedSavedQuerySearchParameter first defined at extension advancedsavedquery.
 */
@SuppressWarnings("all")
public class AbstractAdvancedSavedQuerySearchParameterModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractAdvancedSavedQuerySearchParameter";
	
	/**<i>Generated relation code constant for relation <code>WherePart2SearchParameterRelation</code> defining source attribute <code>wherePart</code> in extension <code>advancedsavedquery</code>.</i>*/
	public static final String _WHEREPART2SEARCHPARAMETERRELATION = "WherePart2SearchParameterRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAdvancedSavedQuerySearchParameter.comparator</code> attribute defined at extension <code>advancedsavedquery</code>. */
	public static final String COMPARATOR = "comparator";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAdvancedSavedQuerySearchParameter.emptyHandling</code> attribute defined at extension <code>advancedsavedquery</code>. */
	public static final String EMPTYHANDLING = "emptyHandling";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAdvancedSavedQuerySearchParameter.valueType</code> attribute defined at extension <code>advancedsavedquery</code>. */
	public static final String VALUETYPE = "valueType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAdvancedSavedQuerySearchParameter.searchParameterName</code> attribute defined at extension <code>advancedsavedquery</code>. */
	public static final String SEARCHPARAMETERNAME = "searchParameterName";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAdvancedSavedQuerySearchParameter.joinAlias</code> attribute defined at extension <code>advancedsavedquery</code>. */
	public static final String JOINALIAS = "joinAlias";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAdvancedSavedQuerySearchParameter.name</code> attribute defined at extension <code>advancedsavedquery</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAdvancedSavedQuerySearchParameter.lower</code> attribute defined at extension <code>advancedsavedquery</code>. */
	public static final String LOWER = "lower";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractAdvancedSavedQuerySearchParameter.wherePart</code> attribute defined at extension <code>advancedsavedquery</code>. */
	public static final String WHEREPART = "wherePart";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractAdvancedSavedQuerySearchParameterModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractAdvancedSavedQuerySearchParameterModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _searchParameterName initial attribute declared by type <code>AbstractAdvancedSavedQuerySearchParameter</code> at extension <code>advancedsavedquery</code>
	 * @param _wherePart initial attribute declared by type <code>AbstractAdvancedSavedQuerySearchParameter</code> at extension <code>advancedsavedquery</code>
	 */
	@Deprecated
	public AbstractAdvancedSavedQuerySearchParameterModel(final String _searchParameterName, final WherePartModel _wherePart)
	{
		super();
		setSearchParameterName(_searchParameterName);
		setWherePart(_wherePart);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _searchParameterName initial attribute declared by type <code>AbstractAdvancedSavedQuerySearchParameter</code> at extension <code>advancedsavedquery</code>
	 * @param _wherePart initial attribute declared by type <code>AbstractAdvancedSavedQuerySearchParameter</code> at extension <code>advancedsavedquery</code>
	 */
	@Deprecated
	public AbstractAdvancedSavedQuerySearchParameterModel(final ItemModel _owner, final String _searchParameterName, final WherePartModel _wherePart)
	{
		super();
		setOwner(_owner);
		setSearchParameterName(_searchParameterName);
		setWherePart(_wherePart);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAdvancedSavedQuerySearchParameter.comparator</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 * @return the comparator
	 */
	@Accessor(qualifier = "comparator", type = Accessor.Type.GETTER)
	public AdvancedQueryComparatorEnum getComparator()
	{
		return getPersistenceContext().getPropertyValue(COMPARATOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAdvancedSavedQuerySearchParameter.emptyHandling</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 * @return the emptyHandling
	 */
	@Accessor(qualifier = "emptyHandling", type = Accessor.Type.GETTER)
	public EmptyParamEnum getEmptyHandling()
	{
		return getPersistenceContext().getPropertyValue(EMPTYHANDLING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAdvancedSavedQuerySearchParameter.joinAlias</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 * @return the joinAlias
	 */
	@Accessor(qualifier = "joinAlias", type = Accessor.Type.GETTER)
	public String getJoinAlias()
	{
		return getPersistenceContext().getPropertyValue(JOINALIAS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAdvancedSavedQuerySearchParameter.lower</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 * @return the lower
	 */
	@Accessor(qualifier = "lower", type = Accessor.Type.GETTER)
	public Boolean getLower()
	{
		return getPersistenceContext().getPropertyValue(LOWER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAdvancedSavedQuerySearchParameter.name</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAdvancedSavedQuerySearchParameter.name</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 * @param loc the value localization key 
	 * @return the name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAdvancedSavedQuerySearchParameter.searchParameterName</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 * @return the searchParameterName
	 */
	@Accessor(qualifier = "searchParameterName", type = Accessor.Type.GETTER)
	public String getSearchParameterName()
	{
		return getPersistenceContext().getPropertyValue(SEARCHPARAMETERNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAdvancedSavedQuerySearchParameter.valueType</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 * @return the valueType
	 */
	@Accessor(qualifier = "valueType", type = Accessor.Type.GETTER)
	public TypeModel getValueType()
	{
		return getPersistenceContext().getPropertyValue(VALUETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractAdvancedSavedQuerySearchParameter.wherePart</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 * @return the wherePart
	 */
	@Accessor(qualifier = "wherePart", type = Accessor.Type.GETTER)
	public WherePartModel getWherePart()
	{
		return getPersistenceContext().getPropertyValue(WHEREPART);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAdvancedSavedQuerySearchParameter.comparator</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 *  
	 * @param value the comparator
	 */
	@Accessor(qualifier = "comparator", type = Accessor.Type.SETTER)
	public void setComparator(final AdvancedQueryComparatorEnum value)
	{
		getPersistenceContext().setPropertyValue(COMPARATOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAdvancedSavedQuerySearchParameter.emptyHandling</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 *  
	 * @param value the emptyHandling
	 */
	@Accessor(qualifier = "emptyHandling", type = Accessor.Type.SETTER)
	public void setEmptyHandling(final EmptyParamEnum value)
	{
		getPersistenceContext().setPropertyValue(EMPTYHANDLING, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAdvancedSavedQuerySearchParameter.joinAlias</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 *  
	 * @param value the joinAlias
	 */
	@Accessor(qualifier = "joinAlias", type = Accessor.Type.SETTER)
	public void setJoinAlias(final String value)
	{
		getPersistenceContext().setPropertyValue(JOINALIAS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAdvancedSavedQuerySearchParameter.lower</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 *  
	 * @param value the lower
	 */
	@Accessor(qualifier = "lower", type = Accessor.Type.SETTER)
	public void setLower(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(LOWER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAdvancedSavedQuerySearchParameter.name</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAdvancedSavedQuerySearchParameter.name</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 *  
	 * @param value the name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAdvancedSavedQuerySearchParameter.searchParameterName</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 *  
	 * @param value the searchParameterName
	 */
	@Accessor(qualifier = "searchParameterName", type = Accessor.Type.SETTER)
	public void setSearchParameterName(final String value)
	{
		getPersistenceContext().setPropertyValue(SEARCHPARAMETERNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAdvancedSavedQuerySearchParameter.valueType</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 *  
	 * @param value the valueType
	 */
	@Accessor(qualifier = "valueType", type = Accessor.Type.SETTER)
	public void setValueType(final TypeModel value)
	{
		getPersistenceContext().setPropertyValue(VALUETYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAdvancedSavedQuerySearchParameter.wherePart</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 *  
	 * @param value the wherePart
	 */
	@Accessor(qualifier = "wherePart", type = Accessor.Type.SETTER)
	public void setWherePart(final WherePartModel value)
	{
		getPersistenceContext().setPropertyValue(WHEREPART, value);
	}
	
}
