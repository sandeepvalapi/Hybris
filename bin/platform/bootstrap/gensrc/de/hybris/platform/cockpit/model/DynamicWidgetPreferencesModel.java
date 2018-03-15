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
import de.hybris.platform.cockpit.model.WidgetParameterModel;
import de.hybris.platform.cockpit.model.WidgetPreferencesModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type DynamicWidgetPreferences first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class DynamicWidgetPreferencesModel extends WidgetPreferencesModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DynamicWidgetPreferences";
	
	/** <i>Generated constant</i> - Attribute key of <code>DynamicWidgetPreferences.parameters</code> attribute defined at extension <code>cockpit</code>. */
	public static final String PARAMETERS = "parameters";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DynamicWidgetPreferencesModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DynamicWidgetPreferencesModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public DynamicWidgetPreferencesModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DynamicWidgetPreferences.parameters</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the parameters
	 */
	@Accessor(qualifier = "parameters", type = Accessor.Type.GETTER)
	public Collection<WidgetParameterModel> getParameters()
	{
		return getPersistenceContext().getPropertyValue(PARAMETERS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DynamicWidgetPreferences.parameters</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the parameters
	 */
	@Accessor(qualifier = "parameters", type = Accessor.Type.SETTER)
	public void setParameters(final Collection<WidgetParameterModel> value)
	{
		getPersistenceContext().setPropertyValue(PARAMETERS, value);
	}
	
}
