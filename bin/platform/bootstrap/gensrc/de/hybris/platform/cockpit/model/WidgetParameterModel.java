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
package de.hybris.platform.cockpit.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cockpit.model.DynamicWidgetPreferencesModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type WidgetParameter first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class WidgetParameterModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "WidgetParameter";
	
	/**<i>Generated relation code constant for relation <code>DynamicWidgetPreferencesToWidgetParameterRelation</code> defining source attribute <code>widgetPreferences</code> in extension <code>cockpit</code>.</i>*/
	public static final String _DYNAMICWIDGETPREFERENCESTOWIDGETPARAMETERRELATION = "DynamicWidgetPreferencesToWidgetParameterRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>WidgetParameter.name</code> attribute defined at extension <code>cockpit</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>WidgetParameter.type</code> attribute defined at extension <code>cockpit</code>. */
	public static final String TYPE = "type";
	
	/** <i>Generated constant</i> - Attribute key of <code>WidgetParameter.description</code> attribute defined at extension <code>cockpit</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>WidgetParameter.defaultValueExpression</code> attribute defined at extension <code>cockpit</code>. */
	public static final String DEFAULTVALUEEXPRESSION = "defaultValueExpression";
	
	/** <i>Generated constant</i> - Attribute key of <code>WidgetParameter.targetType</code> attribute defined at extension <code>cockpit</code>. */
	public static final String TARGETTYPE = "targetType";
	
	/** <i>Generated constant</i> - Attribute key of <code>WidgetParameter.value</code> attribute defined at extension <code>cockpit</code>. */
	public static final String VALUE = "value";
	
	/** <i>Generated constant</i> - Attribute key of <code>WidgetParameter.widgetPreferences</code> attribute defined at extension <code>cockpit</code>. */
	public static final String WIDGETPREFERENCES = "widgetPreferences";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public WidgetParameterModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public WidgetParameterModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>WidgetParameter</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public WidgetParameterModel(final String _name)
	{
		super();
		setName(_name);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>WidgetParameter</code> at extension <code>cockpit</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public WidgetParameterModel(final String _name, final ItemModel _owner)
	{
		super();
		setName(_name);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WidgetParameter.defaultValueExpression</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the defaultValueExpression
	 */
	@Accessor(qualifier = "defaultValueExpression", type = Accessor.Type.GETTER)
	public String getDefaultValueExpression()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTVALUEEXPRESSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WidgetParameter.description</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getPersistenceContext().getPropertyValue(DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WidgetParameter.name</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WidgetParameter.targetType</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the targetType
	 */
	@Accessor(qualifier = "targetType", type = Accessor.Type.GETTER)
	public String getTargetType()
	{
		return getPersistenceContext().getPropertyValue(TARGETTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WidgetParameter.type</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public TypeModel getType()
	{
		return getPersistenceContext().getPropertyValue(TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WidgetParameter.value</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.GETTER)
	public Object getValue()
	{
		return getPersistenceContext().getPropertyValue(VALUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WidgetParameter.widgetPreferences</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the widgetPreferences
	 */
	@Accessor(qualifier = "widgetPreferences", type = Accessor.Type.GETTER)
	public DynamicWidgetPreferencesModel getWidgetPreferences()
	{
		return getPersistenceContext().getPropertyValue(WIDGETPREFERENCES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WidgetParameter.defaultValueExpression</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the defaultValueExpression
	 */
	@Accessor(qualifier = "defaultValueExpression", type = Accessor.Type.SETTER)
	public void setDefaultValueExpression(final String value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTVALUEEXPRESSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WidgetParameter.description</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		getPersistenceContext().setPropertyValue(DESCRIPTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WidgetParameter.name</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WidgetParameter.targetType</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the targetType
	 */
	@Accessor(qualifier = "targetType", type = Accessor.Type.SETTER)
	public void setTargetType(final String value)
	{
		getPersistenceContext().setPropertyValue(TARGETTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WidgetParameter.type</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.SETTER)
	public void setType(final TypeModel value)
	{
		getPersistenceContext().setPropertyValue(TYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WidgetParameter.value</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.SETTER)
	public void setValue(final Object value)
	{
		getPersistenceContext().setPropertyValue(VALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>WidgetParameter.widgetPreferences</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the widgetPreferences
	 */
	@Accessor(qualifier = "widgetPreferences", type = Accessor.Type.SETTER)
	public void setWidgetPreferences(final DynamicWidgetPreferencesModel value)
	{
		getPersistenceContext().setPropertyValue(WIDGETPREFERENCES, value);
	}
	
}
