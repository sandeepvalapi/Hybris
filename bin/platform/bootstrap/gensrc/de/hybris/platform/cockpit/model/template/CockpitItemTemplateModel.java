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
package de.hybris.platform.cockpit.model.template;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type CockpitItemTemplate first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class CockpitItemTemplateModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CockpitItemTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitItemTemplate.code</code> attribute defined at extension <code>cockpit</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitItemTemplate.name</code> attribute defined at extension <code>cockpit</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitItemTemplate.description</code> attribute defined at extension <code>cockpit</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitItemTemplate.relatedType</code> attribute defined at extension <code>cockpit</code>. */
	public static final String RELATEDTYPE = "relatedType";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitItemTemplate.classificationClasses</code> attribute defined at extension <code>cockpit</code>. */
	public static final String CLASSIFICATIONCLASSES = "classificationClasses";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CockpitItemTemplateModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CockpitItemTemplateModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CockpitItemTemplate</code> at extension <code>cockpit</code>
	 * @param _relatedType initial attribute declared by type <code>CockpitItemTemplate</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CockpitItemTemplateModel(final String _code, final ComposedTypeModel _relatedType)
	{
		super();
		setCode(_code);
		setRelatedType(_relatedType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CockpitItemTemplate</code> at extension <code>cockpit</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _relatedType initial attribute declared by type <code>CockpitItemTemplate</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CockpitItemTemplateModel(final String _code, final ItemModel _owner, final ComposedTypeModel _relatedType)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setRelatedType(_relatedType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitItemTemplate.classificationClasses</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the classificationClasses
	 */
	@Accessor(qualifier = "classificationClasses", type = Accessor.Type.GETTER)
	public Collection<ClassificationClassModel> getClassificationClasses()
	{
		return getPersistenceContext().getPropertyValue(CLASSIFICATIONCLASSES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitItemTemplate.code</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitItemTemplate.description</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitItemTemplate.description</code> attribute defined at extension <code>cockpit</code>. 
	 * @param loc the value localization key 
	 * @return the description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitItemTemplate.name</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitItemTemplate.name</code> attribute defined at extension <code>cockpit</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>CockpitItemTemplate.relatedType</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the relatedType
	 */
	@Accessor(qualifier = "relatedType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getRelatedType()
	{
		return getPersistenceContext().getPropertyValue(RELATEDTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitItemTemplate.classificationClasses</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the classificationClasses
	 */
	@Accessor(qualifier = "classificationClasses", type = Accessor.Type.SETTER)
	public void setClassificationClasses(final Collection<ClassificationClassModel> value)
	{
		getPersistenceContext().setPropertyValue(CLASSIFICATIONCLASSES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitItemTemplate.code</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitItemTemplate.description</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitItemTemplate.description</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitItemTemplate.name</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitItemTemplate.name</code> attribute defined at extension <code>cockpit</code>. 
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
	 * <i>Generated method</i> - Setter of <code>CockpitItemTemplate.relatedType</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the relatedType
	 */
	@Accessor(qualifier = "relatedType", type = Accessor.Type.SETTER)
	public void setRelatedType(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(RELATEDTYPE, value);
	}
	
}
