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
package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ViewAttributeDescriptor first defined at extension core.
 */
@SuppressWarnings("all")
public class ViewAttributeDescriptorModel extends AttributeDescriptorModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ViewAttributeDescriptor";
	
	/** <i>Generated constant</i> - Attribute key of <code>ViewAttributeDescriptor.param</code> attribute defined at extension <code>core</code>. */
	public static final String PARAM = "param";
	
	/** <i>Generated constant</i> - Attribute key of <code>ViewAttributeDescriptor.position</code> attribute defined at extension <code>core</code>. */
	public static final String POSITION = "position";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ViewAttributeDescriptorModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ViewAttributeDescriptorModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _attributeType initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 * @param _enclosingType initial attribute declared by type <code>AttributeDescriptor</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _partOf initial attribute declared by type <code>AttributeDescriptor</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 */
	@Deprecated
	public ViewAttributeDescriptorModel(final TypeModel _attributeType, final ComposedTypeModel _enclosingType, final Boolean _generate, final Boolean _partOf, final String _qualifier)
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
	 * @param _enclosingType initial attribute declared by type <code>AttributeDescriptor</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _partOf initial attribute declared by type <code>AttributeDescriptor</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 */
	@Deprecated
	public ViewAttributeDescriptorModel(final TypeModel _attributeType, final ComposedTypeModel _enclosingType, final Boolean _generate, final ItemModel _owner, final Boolean _partOf, final String _qualifier)
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
	 * <i>Generated method</i> - Getter of the <code>ViewAttributeDescriptor.param</code> attribute defined at extension <code>core</code>. 
	 * @return the param
	 */
	@Accessor(qualifier = "param", type = Accessor.Type.GETTER)
	public Boolean getParam()
	{
		return getPersistenceContext().getPropertyValue(PARAM);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ViewAttributeDescriptor.param</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the param
	 */
	@Accessor(qualifier = "param", type = Accessor.Type.SETTER)
	public void setParam(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(PARAM, value);
	}
	
}
