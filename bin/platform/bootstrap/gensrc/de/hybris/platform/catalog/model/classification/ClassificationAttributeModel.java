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
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type ClassificationAttribute first defined at extension catalog.
 */
@SuppressWarnings("all")
public class ClassificationAttributeModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ClassificationAttribute";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttribute.systemVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYSTEMVERSION = "systemVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttribute.code</code> attribute defined at extension <code>catalog</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttribute.externalID</code> attribute defined at extension <code>catalog</code>. */
	public static final String EXTERNALID = "externalID";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttribute.name</code> attribute defined at extension <code>catalog</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttribute.classes</code> attribute defined at extension <code>catalog</code>. */
	public static final String CLASSES = "classes";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttribute.defaultAttributeValues</code> attribute defined at extension <code>catalog</code>. */
	public static final String DEFAULTATTRIBUTEVALUES = "defaultAttributeValues";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ClassificationAttributeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ClassificationAttributeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ClassificationAttribute</code> at extension <code>catalog</code>
	 * @param _systemVersion initial attribute declared by type <code>ClassificationAttribute</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ClassificationAttributeModel(final String _code, final ClassificationSystemVersionModel _systemVersion)
	{
		super();
		setCode(_code);
		setSystemVersion(_systemVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ClassificationAttribute</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _systemVersion initial attribute declared by type <code>ClassificationAttribute</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ClassificationAttributeModel(final String _code, final ItemModel _owner, final ClassificationSystemVersionModel _systemVersion)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setSystemVersion(_systemVersion);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttribute.classes</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the classes - list of classes this attribute was assigned to
	 */
	@Accessor(qualifier = "classes", type = Accessor.Type.GETTER)
	public List<ClassificationClassModel> getClasses()
	{
		return getPersistenceContext().getPropertyValue(CLASSES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttribute.code</code> attribute defined at extension <code>catalog</code>. 
	 * @return the code - Code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttribute.defaultAttributeValues</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the defaultAttributeValues
	 */
	@Accessor(qualifier = "defaultAttributeValues", type = Accessor.Type.GETTER)
	public List<ClassificationAttributeValueModel> getDefaultAttributeValues()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTATTRIBUTEVALUES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttribute.externalID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the externalID - external identifier refering to the actual classification system definition
	 */
	@Accessor(qualifier = "externalID", type = Accessor.Type.GETTER)
	public String getExternalID()
	{
		return getPersistenceContext().getPropertyValue(EXTERNALID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttribute.name</code> attribute defined at extension <code>catalog</code>. 
	 * @return the name - Name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttribute.name</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the name - Name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttribute.systemVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the systemVersion
	 */
	@Accessor(qualifier = "systemVersion", type = Accessor.Type.GETTER)
	public ClassificationSystemVersionModel getSystemVersion()
	{
		return getPersistenceContext().getPropertyValue(SYSTEMVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ClassificationAttribute.code</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code - Code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttribute.defaultAttributeValues</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the defaultAttributeValues
	 */
	@Accessor(qualifier = "defaultAttributeValues", type = Accessor.Type.SETTER)
	public void setDefaultAttributeValues(final List<ClassificationAttributeValueModel> value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTATTRIBUTEVALUES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttribute.externalID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the externalID - external identifier refering to the actual classification system definition
	 */
	@Accessor(qualifier = "externalID", type = Accessor.Type.SETTER)
	public void setExternalID(final String value)
	{
		getPersistenceContext().setPropertyValue(EXTERNALID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttribute.name</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the name - Name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttribute.name</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the name - Name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttribute.systemVersion</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the systemVersion
	 */
	@Accessor(qualifier = "systemVersion", type = Accessor.Type.SETTER)
	public void setSystemVersion(final ClassificationSystemVersionModel value)
	{
		getPersistenceContext().setPropertyValue(SYSTEMVERSION, value);
	}
	
}
