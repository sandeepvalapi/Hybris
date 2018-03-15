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
package de.hybris.platform.core.model.enumeration;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type EnumerationMetaType first defined at extension core.
 */
@SuppressWarnings("all")
public class EnumerationMetaTypeModel extends ComposedTypeModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "EnumerationMetaType";
	
	/** <i>Generated constant</i> - Attribute key of <code>EnumerationMetaType.comparationAttribute</code> attribute defined at extension <code>core</code>. */
	public static final String COMPARATIONATTRIBUTE = "comparationAttribute";
	
	/** <i>Generated constant</i> - Attribute key of <code>EnumerationMetaType.values</code> attribute defined at extension <code>core</code>. */
	public static final String VALUES = "values";
	
	/** <i>Generated constant</i> - Attribute key of <code>EnumerationMetaType.valueType</code> attribute defined at extension <code>core</code>. */
	public static final String VALUETYPE = "valueType";
	
	/** <i>Generated constant</i> - Attribute key of <code>EnumerationMetaType.isSorted</code> attribute defined at extension <code>core</code>. */
	public static final String ISSORTED = "isSorted";
	
	/** <i>Generated constant</i> - Attribute key of <code>EnumerationMetaType.isResortable</code> attribute defined at extension <code>core</code>. */
	public static final String ISRESORTABLE = "isResortable";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public EnumerationMetaTypeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public EnumerationMetaTypeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogItemType initial attribute declared by type <code>ComposedType</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Type</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _singleton initial attribute declared by type <code>ComposedType</code> at extension <code>core</code>
	 * @param _superType initial attribute declared by type <code>EnumerationMetaType</code> at extension <code>core</code>
	 */
	@Deprecated
	public EnumerationMetaTypeModel(final Boolean _catalogItemType, final String _code, final Boolean _generate, final Boolean _singleton, final ComposedTypeModel _superType)
	{
		super();
		setCatalogItemType(_catalogItemType);
		setCode(_code);
		setGenerate(_generate);
		setSingleton(_singleton);
		setSuperType(_superType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogItemType initial attribute declared by type <code>ComposedType</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Type</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _singleton initial attribute declared by type <code>ComposedType</code> at extension <code>core</code>
	 * @param _superType initial attribute declared by type <code>EnumerationMetaType</code> at extension <code>core</code>
	 * @param _valueType initial attribute declared by type <code>EnumerationMetaType</code> at extension <code>core</code>
	 */
	@Deprecated
	public EnumerationMetaTypeModel(final Boolean _catalogItemType, final String _code, final Boolean _generate, final ItemModel _owner, final Boolean _singleton, final ComposedTypeModel _superType, final ComposedTypeModel _valueType)
	{
		super();
		setCatalogItemType(_catalogItemType);
		setCode(_code);
		setGenerate(_generate);
		setOwner(_owner);
		setSingleton(_singleton);
		setSuperType(_superType);
		setValueType(_valueType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EnumerationMetaType.comparationAttribute</code> attribute defined at extension <code>core</code>. 
	 * @return the comparationAttribute
	 */
	@Accessor(qualifier = "comparationAttribute", type = Accessor.Type.GETTER)
	public AttributeDescriptorModel getComparationAttribute()
	{
		return getPersistenceContext().getPropertyValue(COMPARATIONATTRIBUTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EnumerationMetaType.isResortable</code> attribute defined at extension <code>core</code>. 
	 * @return the isResortable
	 */
	@Accessor(qualifier = "isResortable", type = Accessor.Type.GETTER)
	public Boolean getIsResortable()
	{
		return getPersistenceContext().getPropertyValue(ISRESORTABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EnumerationMetaType.isSorted</code> attribute defined at extension <code>core</code>. 
	 * @return the isSorted
	 */
	@Accessor(qualifier = "isSorted", type = Accessor.Type.GETTER)
	public Boolean getIsSorted()
	{
		return getPersistenceContext().getPropertyValue(ISSORTED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EnumerationMetaType.values</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the values
	 */
	@Accessor(qualifier = "values", type = Accessor.Type.GETTER)
	public Collection<ItemModel> getValues()
	{
		return getPersistenceContext().getPropertyValue(VALUES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EnumerationMetaType.valueType</code> attribute defined at extension <code>core</code>. 
	 * @return the valueType
	 */
	@Accessor(qualifier = "valueType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getValueType()
	{
		return getPersistenceContext().getPropertyValue(VALUETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EnumerationMetaType.comparationAttribute</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the comparationAttribute
	 */
	@Accessor(qualifier = "comparationAttribute", type = Accessor.Type.SETTER)
	public void setComparationAttribute(final AttributeDescriptorModel value)
	{
		getPersistenceContext().setPropertyValue(COMPARATIONATTRIBUTE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EnumerationMetaType.values</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the values
	 */
	@Accessor(qualifier = "values", type = Accessor.Type.SETTER)
	public void setValues(final Collection<ItemModel> value)
	{
		getPersistenceContext().setPropertyValue(VALUES, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>EnumerationMetaType.valueType</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the valueType
	 */
	@Accessor(qualifier = "valueType", type = Accessor.Type.SETTER)
	public void setValueType(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(VALUETYPE, value);
	}
	
}
