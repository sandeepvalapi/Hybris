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
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.variants.model.VariantTypeModel;

/**
 * Generated model class for type VariantAttributeDescriptor first defined at extension catalog.
 */
@SuppressWarnings("all")
public class VariantAttributeDescriptorModel extends AttributeDescriptorModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "VariantAttributeDescriptor";
	
	/** <i>Generated constant</i> - Attribute key of <code>VariantAttributeDescriptor.position</code> attribute defined at extension <code>catalog</code>. */
	public static final String POSITION = "position";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public VariantAttributeDescriptorModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public VariantAttributeDescriptorModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _attributeType initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 * @param _enclosingType initial attribute declared by type <code>VariantAttributeDescriptor</code> at extension <code>catalog</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _partOf initial attribute declared by type <code>AttributeDescriptor</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 */
	@Deprecated
	public VariantAttributeDescriptorModel(final TypeModel _attributeType, final VariantTypeModel _enclosingType, final Boolean _generate, final Boolean _partOf, final String _qualifier)
	{
		super();
		setAttributeType(_attributeType);
		setEnclosingType(_enclosingType);
		setGenerate(_generate);
		setPartOf(_partOf);
		setQualifier(_qualifier);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _attributeType initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 * @param _enclosingType initial attribute declared by type <code>VariantAttributeDescriptor</code> at extension <code>catalog</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _partOf initial attribute declared by type <code>AttributeDescriptor</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 */
	@Deprecated
	public VariantAttributeDescriptorModel(final TypeModel _attributeType, final VariantTypeModel _enclosingType, final Boolean _generate, final ItemModel _owner, final Boolean _partOf, final String _qualifier)
	{
		super();
		setAttributeType(_attributeType);
		setEnclosingType(_enclosingType);
		setGenerate(_generate);
		setOwner(_owner);
		setPartOf(_partOf);
		setQualifier(_qualifier);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.enclosingType</code> attribute defined at extension <code>core</code> and redeclared at extension <code>catalog</code>. 
	 * @return the enclosingType
	 */
	@Override
	@Accessor(qualifier = "enclosingType", type = Accessor.Type.GETTER)
	public VariantTypeModel getEnclosingType()
	{
		return (VariantTypeModel) super.getEnclosingType();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>VariantAttributeDescriptor.position</code> attribute defined at extension <code>catalog</code>. 
	 * @return the position
	 */
	@Accessor(qualifier = "position", type = Accessor.Type.GETTER)
	public Integer getPosition()
	{
		return getPersistenceContext().getPropertyValue(POSITION);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AttributeDescriptor.enclosingType</code> attribute defined at extension <code>core</code> and redeclared at extension <code>catalog</code>. Can only be used at creation of model - before first save. Will only accept values of type {@link de.hybris.platform.variants.model.VariantTypeModel}.  
	 *  
	 * @param value the enclosingType
	 */
	@Override
	@Accessor(qualifier = "enclosingType", type = Accessor.Type.SETTER)
	public void setEnclosingType(final ComposedTypeModel value)
	{
		if( value == null || value instanceof VariantTypeModel)
		{
			super.setEnclosingType(value);
		}
		else
		{
			throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.variants.model.VariantTypeModel");
		}
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>VariantAttributeDescriptor.position</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the position
	 */
	@Accessor(qualifier = "position", type = Accessor.Type.SETTER)
	public void setPosition(final Integer value)
	{
		getPersistenceContext().setPropertyValue(POSITION, value);
	}
	
}
