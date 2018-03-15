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
package de.hybris.platform.variants.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.variants.model.VariantAttributeDescriptorModel;
import java.util.List;

/**
 * Generated model class for type VariantType first defined at extension catalog.
 */
@SuppressWarnings("all")
public class VariantTypeModel extends ComposedTypeModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "VariantType";
	
	/** <i>Generated constant</i> - Attribute key of <code>VariantType.variantAttributes</code> attribute defined at extension <code>catalog</code>. */
	public static final String VARIANTATTRIBUTES = "variantAttributes";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public VariantTypeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public VariantTypeModel(final ItemModelContext ctx)
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
	 * @param _superType initial attribute declared by type <code>VariantType</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public VariantTypeModel(final Boolean _catalogItemType, final String _code, final Boolean _generate, final Boolean _singleton, final ComposedTypeModel _superType)
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
	 * @param _superType initial attribute declared by type <code>VariantType</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public VariantTypeModel(final Boolean _catalogItemType, final String _code, final Boolean _generate, final ItemModel _owner, final Boolean _singleton, final ComposedTypeModel _superType)
	{
		super();
		setCatalogItemType(_catalogItemType);
		setCode(_code);
		setGenerate(_generate);
		setOwner(_owner);
		setSingleton(_singleton);
		setSuperType(_superType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>VariantType.variantAttributes</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the variantAttributes
	 * @deprecated since ages
	 */
	@Deprecated
	@Accessor(qualifier = "variantAttributes", type = Accessor.Type.GETTER)
	public List<VariantAttributeDescriptorModel> getVariantAttributes()
	{
		return getPersistenceContext().getPropertyValue(VARIANTATTRIBUTES);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ComposedType.superType</code> attribute defined at extension <code>core</code> and redeclared at extension <code>catalog</code>. Can only be used at creation of model - before first save. Will only accept values of type {@link de.hybris.platform.core.model.type.ComposedTypeModel}.  
	 *  
	 * @param value the superType
	 */
	@Override
	@Accessor(qualifier = "superType", type = Accessor.Type.SETTER)
	public void setSuperType(final ComposedTypeModel value)
	{
		super.setSuperType(value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>VariantType.variantAttributes</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the variantAttributes
	 * @deprecated since ages
	 */
	@Deprecated
	@Accessor(qualifier = "variantAttributes", type = Accessor.Type.SETTER)
	public void setVariantAttributes(final List<VariantAttributeDescriptorModel> value)
	{
		getPersistenceContext().setPropertyValue(VARIANTATTRIBUTES, value);
	}
	
}
