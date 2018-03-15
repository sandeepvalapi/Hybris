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
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.enums.ClassificationAttributeVisibilityEnum;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type ClassAttributeAssignment first defined at extension catalog.
 */
@SuppressWarnings("all")
public class ClassAttributeAssignmentModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ClassAttributeAssignment";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassAttributeAssignment.classificationClass</code> attribute defined at extension <code>catalog</code>. */
	public static final String CLASSIFICATIONCLASS = "classificationClass";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassAttributeAssignment.classificationAttribute</code> attribute defined at extension <code>catalog</code>. */
	public static final String CLASSIFICATIONATTRIBUTE = "classificationAttribute";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassAttributeAssignment.description</code> attribute defined at extension <code>catalog</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassAttributeAssignment.systemVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYSTEMVERSION = "systemVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassAttributeAssignment.position</code> attribute defined at extension <code>catalog</code>. */
	public static final String POSITION = "position";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassAttributeAssignment.externalID</code> attribute defined at extension <code>catalog</code>. */
	public static final String EXTERNALID = "externalID";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassAttributeAssignment.unit</code> attribute defined at extension <code>catalog</code>. */
	public static final String UNIT = "unit";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassAttributeAssignment.mandatory</code> attribute defined at extension <code>catalog</code>. */
	public static final String MANDATORY = "mandatory";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassAttributeAssignment.localized</code> attribute defined at extension <code>catalog</code>. */
	public static final String LOCALIZED = "localized";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassAttributeAssignment.range</code> attribute defined at extension <code>catalog</code>. */
	public static final String RANGE = "range";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassAttributeAssignment.multiValued</code> attribute defined at extension <code>catalog</code>. */
	public static final String MULTIVALUED = "multiValued";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassAttributeAssignment.searchable</code> attribute defined at extension <code>catalog</code>. */
	public static final String SEARCHABLE = "searchable";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassAttributeAssignment.attributeType</code> attribute defined at extension <code>catalog</code>. */
	public static final String ATTRIBUTETYPE = "attributeType";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassAttributeAssignment.formatDefinition</code> attribute defined at extension <code>catalog</code>. */
	public static final String FORMATDEFINITION = "formatDefinition";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassAttributeAssignment.listable</code> attribute defined at extension <code>catalog</code>. */
	public static final String LISTABLE = "listable";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassAttributeAssignment.comparable</code> attribute defined at extension <code>catalog</code>. */
	public static final String COMPARABLE = "comparable";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassAttributeAssignment.visibility</code> attribute defined at extension <code>catalog</code>. */
	public static final String VISIBILITY = "visibility";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassAttributeAssignment.attributeValues</code> attribute defined at extension <code>catalog</code>. */
	public static final String ATTRIBUTEVALUES = "attributeValues";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassAttributeAssignment.attributeValueDisplayString</code> attribute defined at extension <code>catalog</code>. */
	public static final String ATTRIBUTEVALUEDISPLAYSTRING = "attributeValueDisplayString";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ClassAttributeAssignmentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ClassAttributeAssignmentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _classificationAttribute initial attribute declared by type <code>ClassAttributeAssignment</code> at extension <code>catalog</code>
	 * @param _classificationClass initial attribute declared by type <code>ClassAttributeAssignment</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ClassAttributeAssignmentModel(final ClassificationAttributeModel _classificationAttribute, final ClassificationClassModel _classificationClass)
	{
		super();
		setClassificationAttribute(_classificationAttribute);
		setClassificationClass(_classificationClass);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _classificationAttribute initial attribute declared by type <code>ClassAttributeAssignment</code> at extension <code>catalog</code>
	 * @param _classificationClass initial attribute declared by type <code>ClassAttributeAssignment</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _systemVersion initial attribute declared by type <code>ClassAttributeAssignment</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ClassAttributeAssignmentModel(final ClassificationAttributeModel _classificationAttribute, final ClassificationClassModel _classificationClass, final ItemModel _owner, final ClassificationSystemVersionModel _systemVersion)
	{
		super();
		setClassificationAttribute(_classificationAttribute);
		setClassificationClass(_classificationClass);
		setOwner(_owner);
		setSystemVersion(_systemVersion);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.attributeType</code> attribute defined at extension <code>catalog</code>. 
	 * @return the attributeType - Type of attribute: string, number, boolean or range
	 */
	@Accessor(qualifier = "attributeType", type = Accessor.Type.GETTER)
	public ClassificationAttributeTypeEnum getAttributeType()
	{
		return getPersistenceContext().getPropertyValue(ATTRIBUTETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.attributeValueDisplayString</code> attribute defined at extension <code>catalog</code>. 
	 * @return the attributeValueDisplayString
	 */
	@Accessor(qualifier = "attributeValueDisplayString", type = Accessor.Type.GETTER)
	public String getAttributeValueDisplayString()
	{
		return getAttributeValueDisplayString(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.attributeValueDisplayString</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the attributeValueDisplayString
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "attributeValueDisplayString", type = Accessor.Type.GETTER)
	public String getAttributeValueDisplayString(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(ATTRIBUTEVALUEDISPLAYSTRING, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.attributeValues</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the attributeValues
	 */
	@Accessor(qualifier = "attributeValues", type = Accessor.Type.GETTER)
	public List<ClassificationAttributeValueModel> getAttributeValues()
	{
		return getPersistenceContext().getPropertyValue(ATTRIBUTEVALUES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.classificationAttribute</code> attribute defined at extension <code>catalog</code>. 
	 * @return the classificationAttribute
	 */
	@Accessor(qualifier = "classificationAttribute", type = Accessor.Type.GETTER)
	public ClassificationAttributeModel getClassificationAttribute()
	{
		return getPersistenceContext().getPropertyValue(CLASSIFICATIONATTRIBUTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.classificationClass</code> attribute defined at extension <code>catalog</code>. 
	 * @return the classificationClass
	 */
	@Accessor(qualifier = "classificationClass", type = Accessor.Type.GETTER)
	public ClassificationClassModel getClassificationClass()
	{
		return getPersistenceContext().getPropertyValue(CLASSIFICATIONCLASS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.comparable</code> attribute defined at extension <code>catalog</code>. 
	 * @return the comparable
	 */
	@Accessor(qualifier = "comparable", type = Accessor.Type.GETTER)
	public Boolean getComparable()
	{
		return getPersistenceContext().getPropertyValue(COMPARABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.description</code> attribute defined at extension <code>catalog</code>. 
	 * @return the description - description of attribute usage
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.description</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the description - description of attribute usage
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.externalID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the externalID - external identifier refering to the actual classification system definition
	 */
	@Accessor(qualifier = "externalID", type = Accessor.Type.GETTER)
	public String getExternalID()
	{
		return getPersistenceContext().getPropertyValue(EXTERNALID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.formatDefinition</code> attribute defined at extension <code>catalog</code>. 
	 * @return the formatDefinition - Format definition string: optionally used e.g. as number format
	 */
	@Accessor(qualifier = "formatDefinition", type = Accessor.Type.GETTER)
	public String getFormatDefinition()
	{
		return getPersistenceContext().getPropertyValue(FORMATDEFINITION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.listable</code> attribute defined at extension <code>catalog</code>. 
	 * @return the listable
	 */
	@Accessor(qualifier = "listable", type = Accessor.Type.GETTER)
	public Boolean getListable()
	{
		return getPersistenceContext().getPropertyValue(LISTABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.localized</code> attribute defined at extension <code>catalog</code>. 
	 * @return the localized
	 */
	@Accessor(qualifier = "localized", type = Accessor.Type.GETTER)
	public Boolean getLocalized()
	{
		return getPersistenceContext().getPropertyValue(LOCALIZED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.mandatory</code> attribute defined at extension <code>catalog</code>. 
	 * @return the mandatory
	 */
	@Accessor(qualifier = "mandatory", type = Accessor.Type.GETTER)
	public Boolean getMandatory()
	{
		return getPersistenceContext().getPropertyValue(MANDATORY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.multiValued</code> attribute defined at extension <code>catalog</code>. 
	 * @return the multiValued
	 */
	@Accessor(qualifier = "multiValued", type = Accessor.Type.GETTER)
	public Boolean getMultiValued()
	{
		return getPersistenceContext().getPropertyValue(MULTIVALUED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.position</code> attribute defined at extension <code>catalog</code>. 
	 * @return the position
	 */
	@Accessor(qualifier = "position", type = Accessor.Type.GETTER)
	public Integer getPosition()
	{
		return getPersistenceContext().getPropertyValue(POSITION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.range</code> attribute defined at extension <code>catalog</code>. 
	 * @return the range
	 */
	@Accessor(qualifier = "range", type = Accessor.Type.GETTER)
	public Boolean getRange()
	{
		return getPersistenceContext().getPropertyValue(RANGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.searchable</code> attribute defined at extension <code>catalog</code>. 
	 * @return the searchable
	 */
	@Accessor(qualifier = "searchable", type = Accessor.Type.GETTER)
	public Boolean getSearchable()
	{
		return getPersistenceContext().getPropertyValue(SEARCHABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.systemVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the systemVersion
	 */
	@Accessor(qualifier = "systemVersion", type = Accessor.Type.GETTER)
	public ClassificationSystemVersionModel getSystemVersion()
	{
		return getPersistenceContext().getPropertyValue(SYSTEMVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.unit</code> attribute defined at extension <code>catalog</code>. 
	 * @return the unit
	 */
	@Accessor(qualifier = "unit", type = Accessor.Type.GETTER)
	public ClassificationAttributeUnitModel getUnit()
	{
		return getPersistenceContext().getPropertyValue(UNIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassAttributeAssignment.visibility</code> attribute defined at extension <code>catalog</code>. 
	 * @return the visibility - Visibility of attribute: visible, not_visible, visible_in_base or visible_in_variant
	 */
	@Accessor(qualifier = "visibility", type = Accessor.Type.GETTER)
	public ClassificationAttributeVisibilityEnum getVisibility()
	{
		return getPersistenceContext().getPropertyValue(VISIBILITY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassAttributeAssignment.attributeType</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the attributeType - Type of attribute: string, number, boolean or range
	 */
	@Accessor(qualifier = "attributeType", type = Accessor.Type.SETTER)
	public void setAttributeType(final ClassificationAttributeTypeEnum value)
	{
		getPersistenceContext().setPropertyValue(ATTRIBUTETYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassAttributeAssignment.attributeValues</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the attributeValues
	 */
	@Accessor(qualifier = "attributeValues", type = Accessor.Type.SETTER)
	public void setAttributeValues(final List<ClassificationAttributeValueModel> value)
	{
		getPersistenceContext().setPropertyValue(ATTRIBUTEVALUES, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ClassAttributeAssignment.classificationAttribute</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the classificationAttribute
	 */
	@Accessor(qualifier = "classificationAttribute", type = Accessor.Type.SETTER)
	public void setClassificationAttribute(final ClassificationAttributeModel value)
	{
		getPersistenceContext().setPropertyValue(CLASSIFICATIONATTRIBUTE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassAttributeAssignment.classificationClass</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the classificationClass
	 */
	@Accessor(qualifier = "classificationClass", type = Accessor.Type.SETTER)
	public void setClassificationClass(final ClassificationClassModel value)
	{
		getPersistenceContext().setPropertyValue(CLASSIFICATIONCLASS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassAttributeAssignment.comparable</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the comparable
	 */
	@Accessor(qualifier = "comparable", type = Accessor.Type.SETTER)
	public void setComparable(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(COMPARABLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassAttributeAssignment.description</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the description - description of attribute usage
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ClassAttributeAssignment.description</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the description - description of attribute usage
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassAttributeAssignment.externalID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the externalID - external identifier refering to the actual classification system definition
	 */
	@Accessor(qualifier = "externalID", type = Accessor.Type.SETTER)
	public void setExternalID(final String value)
	{
		getPersistenceContext().setPropertyValue(EXTERNALID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassAttributeAssignment.formatDefinition</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the formatDefinition - Format definition string: optionally used e.g. as number format
	 */
	@Accessor(qualifier = "formatDefinition", type = Accessor.Type.SETTER)
	public void setFormatDefinition(final String value)
	{
		getPersistenceContext().setPropertyValue(FORMATDEFINITION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassAttributeAssignment.listable</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the listable
	 */
	@Accessor(qualifier = "listable", type = Accessor.Type.SETTER)
	public void setListable(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(LISTABLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassAttributeAssignment.localized</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the localized
	 */
	@Accessor(qualifier = "localized", type = Accessor.Type.SETTER)
	public void setLocalized(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(LOCALIZED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassAttributeAssignment.mandatory</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the mandatory
	 */
	@Accessor(qualifier = "mandatory", type = Accessor.Type.SETTER)
	public void setMandatory(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(MANDATORY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassAttributeAssignment.multiValued</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the multiValued
	 */
	@Accessor(qualifier = "multiValued", type = Accessor.Type.SETTER)
	public void setMultiValued(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(MULTIVALUED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassAttributeAssignment.position</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the position
	 */
	@Accessor(qualifier = "position", type = Accessor.Type.SETTER)
	public void setPosition(final Integer value)
	{
		getPersistenceContext().setPropertyValue(POSITION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassAttributeAssignment.range</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the range
	 */
	@Accessor(qualifier = "range", type = Accessor.Type.SETTER)
	public void setRange(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(RANGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassAttributeAssignment.searchable</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the searchable
	 */
	@Accessor(qualifier = "searchable", type = Accessor.Type.SETTER)
	public void setSearchable(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SEARCHABLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ClassAttributeAssignment.systemVersion</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the systemVersion
	 */
	@Accessor(qualifier = "systemVersion", type = Accessor.Type.SETTER)
	public void setSystemVersion(final ClassificationSystemVersionModel value)
	{
		getPersistenceContext().setPropertyValue(SYSTEMVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassAttributeAssignment.unit</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the unit
	 */
	@Accessor(qualifier = "unit", type = Accessor.Type.SETTER)
	public void setUnit(final ClassificationAttributeUnitModel value)
	{
		getPersistenceContext().setPropertyValue(UNIT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassAttributeAssignment.visibility</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the visibility - Visibility of attribute: visible, not_visible, visible_in_base or visible_in_variant
	 */
	@Accessor(qualifier = "visibility", type = Accessor.Type.SETTER)
	public void setVisibility(final ClassificationAttributeVisibilityEnum value)
	{
		getPersistenceContext().setPropertyValue(VISIBILITY, value);
	}
	
}
