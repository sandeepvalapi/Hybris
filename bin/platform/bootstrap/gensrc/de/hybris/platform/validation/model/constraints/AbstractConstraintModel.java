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
package de.hybris.platform.validation.model.constraints;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.enums.Severity;
import de.hybris.platform.validation.model.constraints.ConstraintGroupModel;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type AbstractConstraint first defined at extension validation.
 * <p>
 * Abstract constraint base definition for all constraint types.
 */
@SuppressWarnings("all")
public class AbstractConstraintModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractConstraint";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractConstraint.id</code> attribute defined at extension <code>validation</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractConstraint.active</code> attribute defined at extension <code>validation</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractConstraint.needReload</code> attribute defined at extension <code>validation</code>. */
	public static final String NEEDRELOAD = "needReload";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractConstraint.annotation</code> attribute defined at extension <code>validation</code>. */
	public static final String ANNOTATION = "annotation";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractConstraint.severity</code> attribute defined at extension <code>validation</code>. */
	public static final String SEVERITY = "severity";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractConstraint.defaultMessage</code> attribute defined at extension <code>validation</code>. */
	public static final String DEFAULTMESSAGE = "defaultMessage";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractConstraint.message</code> attribute defined at extension <code>validation</code>. */
	public static final String MESSAGE = "message";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractConstraint.target</code> attribute defined at extension <code>validation</code>. */
	public static final String TARGET = "target";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractConstraint.type</code> attribute defined at extension <code>validation</code>. */
	public static final String TYPE = "type";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractConstraint.constraintGroups</code> attribute defined at extension <code>validation</code>. */
	public static final String CONSTRAINTGROUPS = "constraintGroups";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractConstraintModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractConstraintModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _annotation initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 */
	@Deprecated
	public AbstractConstraintModel(final Class _annotation, final String _id)
	{
		super();
		setAnnotation(_annotation);
		setId(_id);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _annotation initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractConstraintModel(final Class _annotation, final String _id, final ItemModel _owner)
	{
		super();
		setAnnotation(_annotation);
		setId(_id);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractConstraint.annotation</code> attribute defined at extension <code>validation</code>. 
	 * @return the annotation - Full class name literal for a corresponding annotation
	 */
	@Accessor(qualifier = "annotation", type = Accessor.Type.GETTER)
	public Class getAnnotation()
	{
		return getPersistenceContext().getPropertyValue(ANNOTATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractConstraint.constraintGroups</code> attribute defined at extension <code>validation</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the constraintGroups
	 */
	@Accessor(qualifier = "constraintGroups", type = Accessor.Type.GETTER)
	public Set<ConstraintGroupModel> getConstraintGroups()
	{
		return getPersistenceContext().getPropertyValue(CONSTRAINTGROUPS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractConstraint.defaultMessage</code> attribute defined at extension <code>validation</code>. 
	 * @return the defaultMessage - Localized resource bundle evaluated error message
	 */
	@Accessor(qualifier = "defaultMessage", type = Accessor.Type.GETTER)
	public String getDefaultMessage()
	{
		return getDefaultMessage(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractConstraint.defaultMessage</code> attribute defined at extension <code>validation</code>. 
	 * @param loc the value localization key 
	 * @return the defaultMessage - Localized resource bundle evaluated error message
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "defaultMessage", type = Accessor.Type.GETTER)
	public String getDefaultMessage(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DEFAULTMESSAGE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractConstraint.id</code> attribute defined at extension <code>validation</code>. 
	 * @return the id - Constraint identifier
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractConstraint.message</code> attribute defined at extension <code>validation</code>. 
	 * @return the message - Localized error message
	 */
	@Accessor(qualifier = "message", type = Accessor.Type.GETTER)
	public String getMessage()
	{
		return getMessage(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractConstraint.message</code> attribute defined at extension <code>validation</code>. 
	 * @param loc the value localization key 
	 * @return the message - Localized error message
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "message", type = Accessor.Type.GETTER)
	public String getMessage(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(MESSAGE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractConstraint.severity</code> attribute defined at extension <code>validation</code>. 
	 * @return the severity - Severity level
	 */
	@Accessor(qualifier = "severity", type = Accessor.Type.GETTER)
	public Severity getSeverity()
	{
		return getPersistenceContext().getPropertyValue(SEVERITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractConstraint.target</code> attribute defined at extension <code>validation</code>. 
	 * @return the target - Target class for covered with  validation logic
	 */
	@Accessor(qualifier = "target", type = Accessor.Type.GETTER)
	public Class getTarget()
	{
		return getPersistenceContext().getPropertyValue(TARGET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractConstraint.type</code> attribute defined at extension <code>validation</code>. 
	 * @return the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public ComposedTypeModel getType()
	{
		return getPersistenceContext().getPropertyValue(TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractConstraint.active</code> attribute defined at extension <code>validation</code>. 
	 * @return the active - Enabled flag for a constraint
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public boolean isActive()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(ACTIVE));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractConstraint.needReload</code> dynamic attribute defined at extension <code>validation</code>. 
	 * @return the needReload - true if the constraint (+values) was never loaded (in the current state) into the framework.
	 */
	@Accessor(qualifier = "needReload", type = Accessor.Type.GETTER)
	public boolean isNeedReload()
	{
		return toPrimitive( (Boolean) getPersistenceContext().getDynamicValue(this,NEEDRELOAD));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractConstraint.active</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the active - Enabled flag for a constraint
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final boolean value)
	{
		getPersistenceContext().setPropertyValue(ACTIVE, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractConstraint.annotation</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the annotation - Full class name literal for a corresponding annotation
	 */
	@Accessor(qualifier = "annotation", type = Accessor.Type.SETTER)
	public void setAnnotation(final Class value)
	{
		getPersistenceContext().setPropertyValue(ANNOTATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractConstraint.constraintGroups</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the constraintGroups
	 */
	@Accessor(qualifier = "constraintGroups", type = Accessor.Type.SETTER)
	public void setConstraintGroups(final Set<ConstraintGroupModel> value)
	{
		getPersistenceContext().setPropertyValue(CONSTRAINTGROUPS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractConstraint.id</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the id - Constraint identifier
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractConstraint.message</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the message - Localized error message
	 */
	@Accessor(qualifier = "message", type = Accessor.Type.SETTER)
	public void setMessage(final String value)
	{
		setMessage(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractConstraint.message</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the message - Localized error message
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "message", type = Accessor.Type.SETTER)
	public void setMessage(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(MESSAGE, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractConstraint.severity</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the severity - Severity level
	 */
	@Accessor(qualifier = "severity", type = Accessor.Type.SETTER)
	public void setSeverity(final Severity value)
	{
		getPersistenceContext().setPropertyValue(SEVERITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractConstraint.target</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the target - Target class for covered with  validation logic
	 */
	@Accessor(qualifier = "target", type = Accessor.Type.SETTER)
	public void setTarget(final Class value)
	{
		getPersistenceContext().setPropertyValue(TARGET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractConstraint.type</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.SETTER)
	public void setType(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(TYPE, value);
	}
	
}
