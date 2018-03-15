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
import de.hybris.platform.core.model.type.TypeManagerManagedModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type Descriptor first defined at extension core.
 */
@SuppressWarnings("all")
public class DescriptorModel extends TypeManagerManagedModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Descriptor";
	
	/** <i>Generated constant</i> - Attribute key of <code>Descriptor.qualifier</code> attribute defined at extension <code>core</code>. */
	public static final String QUALIFIER = "qualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>Descriptor.attributeType</code> attribute defined at extension <code>core</code>. */
	public static final String ATTRIBUTETYPE = "attributeType";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DescriptorModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DescriptorModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _attributeType initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 */
	@Deprecated
	public DescriptorModel(final TypeModel _attributeType, final Boolean _generate, final String _qualifier)
	{
		super();
		setAttributeType(_attributeType);
		setGenerate(_generate);
		setQualifier(_qualifier);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _attributeType initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 */
	@Deprecated
	public DescriptorModel(final TypeModel _attributeType, final Boolean _generate, final ItemModel _owner, final String _qualifier)
	{
		super();
		setAttributeType(_attributeType);
		setGenerate(_generate);
		setOwner(_owner);
		setQualifier(_qualifier);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Descriptor.attributeType</code> attribute defined at extension <code>core</code>. 
	 * @return the attributeType
	 */
	@Accessor(qualifier = "attributeType", type = Accessor.Type.GETTER)
	public TypeModel getAttributeType()
	{
		return getPersistenceContext().getPropertyValue(ATTRIBUTETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Descriptor.qualifier</code> attribute defined at extension <code>core</code>. 
	 * @return the qualifier
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
	public String getQualifier()
	{
		return getPersistenceContext().getPropertyValue(QUALIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Descriptor.attributeType</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the attributeType
	 */
	@Accessor(qualifier = "attributeType", type = Accessor.Type.SETTER)
	public void setAttributeType(final TypeModel value)
	{
		getPersistenceContext().setPropertyValue(ATTRIBUTETYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Descriptor.qualifier</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the qualifier
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
	public void setQualifier(final String value)
	{
		getPersistenceContext().setPropertyValue(QUALIFIER, value);
	}
	
}
