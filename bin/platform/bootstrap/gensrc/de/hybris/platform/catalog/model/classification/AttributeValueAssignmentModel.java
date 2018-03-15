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
package de.hybris.platform.catalog.model.classification;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AttributeValueAssignment first defined at extension catalog.
 */
@SuppressWarnings("all")
public class AttributeValueAssignmentModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AttributeValueAssignment";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeValueAssignment.value</code> attribute defined at extension <code>catalog</code>. */
	public static final String VALUE = "value";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeValueAssignment.attributeAssignment</code> attribute defined at extension <code>catalog</code>. */
	public static final String ATTRIBUTEASSIGNMENT = "attributeAssignment";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeValueAssignment.attribute</code> attribute defined at extension <code>catalog</code>. */
	public static final String ATTRIBUTE = "attribute";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeValueAssignment.systemVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYSTEMVERSION = "systemVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeValueAssignment.position</code> attribute defined at extension <code>catalog</code>. */
	public static final String POSITION = "position";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeValueAssignment.externalID</code> attribute defined at extension <code>catalog</code>. */
	public static final String EXTERNALID = "externalID";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AttributeValueAssignmentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AttributeValueAssignmentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _value initial attribute declared by type <code>AttributeValueAssignment</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public AttributeValueAssignmentModel(final ClassificationAttributeValueModel _value)
	{
		super();
		setValue(_value);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _attribute initial attribute declared by type <code>AttributeValueAssignment</code> at extension <code>catalog</code>
	 * @param _attributeAssignment initial attribute declared by type <code>AttributeValueAssignment</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _systemVersion initial attribute declared by type <code>AttributeValueAssignment</code> at extension <code>catalog</code>
	 * @param _value initial attribute declared by type <code>AttributeValueAssignment</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public AttributeValueAssignmentModel(final ClassificationAttributeModel _attribute, final ClassAttributeAssignmentModel _attributeAssignment, final ItemModel _owner, final ClassificationSystemVersionModel _systemVersion, final ClassificationAttributeValueModel _value)
	{
		super();
		setAttribute(_attribute);
		setAttributeAssignment(_attributeAssignment);
		setOwner(_owner);
		setSystemVersion(_systemVersion);
		setValue(_value);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeValueAssignment.attribute</code> attribute defined at extension <code>catalog</code>. 
	 * @return the attribute
	 */
	@Accessor(qualifier = "attribute", type = Accessor.Type.GETTER)
	public ClassificationAttributeModel getAttribute()
	{
		return getPersistenceContext().getPropertyValue(ATTRIBUTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeValueAssignment.attributeAssignment</code> attribute defined at extension <code>catalog</code>. 
	 * @return the attributeAssignment
	 */
	@Accessor(qualifier = "attributeAssignment", type = Accessor.Type.GETTER)
	public ClassAttributeAssignmentModel getAttributeAssignment()
	{
		return getPersistenceContext().getPropertyValue(ATTRIBUTEASSIGNMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeValueAssignment.externalID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the externalID - external identifier refering to the actual classification system definition
	 */
	@Accessor(qualifier = "externalID", type = Accessor.Type.GETTER)
	public String getExternalID()
	{
		return getPersistenceContext().getPropertyValue(EXTERNALID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeValueAssignment.position</code> attribute defined at extension <code>catalog</code>. 
	 * @return the position
	 */
	@Accessor(qualifier = "position", type = Accessor.Type.GETTER)
	public Integer getPosition()
	{
		return getPersistenceContext().getPropertyValue(POSITION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeValueAssignment.systemVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the systemVersion
	 */
	@Accessor(qualifier = "systemVersion", type = Accessor.Type.GETTER)
	public ClassificationSystemVersionModel getSystemVersion()
	{
		return getPersistenceContext().getPropertyValue(SYSTEMVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeValueAssignment.value</code> attribute defined at extension <code>catalog</code>. 
	 * @return the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.GETTER)
	public ClassificationAttributeValueModel getValue()
	{
		return getPersistenceContext().getPropertyValue(VALUE);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AttributeValueAssignment.attribute</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the attribute
	 */
	@Accessor(qualifier = "attribute", type = Accessor.Type.SETTER)
	public void setAttribute(final ClassificationAttributeModel value)
	{
		getPersistenceContext().setPropertyValue(ATTRIBUTE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AttributeValueAssignment.attributeAssignment</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the attributeAssignment
	 */
	@Accessor(qualifier = "attributeAssignment", type = Accessor.Type.SETTER)
	public void setAttributeAssignment(final ClassAttributeAssignmentModel value)
	{
		getPersistenceContext().setPropertyValue(ATTRIBUTEASSIGNMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeValueAssignment.externalID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the externalID - external identifier refering to the actual classification system definition
	 */
	@Accessor(qualifier = "externalID", type = Accessor.Type.SETTER)
	public void setExternalID(final String value)
	{
		getPersistenceContext().setPropertyValue(EXTERNALID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeValueAssignment.position</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the position
	 */
	@Accessor(qualifier = "position", type = Accessor.Type.SETTER)
	public void setPosition(final Integer value)
	{
		getPersistenceContext().setPropertyValue(POSITION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AttributeValueAssignment.systemVersion</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the systemVersion
	 */
	@Accessor(qualifier = "systemVersion", type = Accessor.Type.SETTER)
	public void setSystemVersion(final ClassificationSystemVersionModel value)
	{
		getPersistenceContext().setPropertyValue(SYSTEMVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AttributeValueAssignment.value</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.SETTER)
	public void setValue(final ClassificationAttributeValueModel value)
	{
		getPersistenceContext().setPropertyValue(VALUE, value);
	}
	
}
