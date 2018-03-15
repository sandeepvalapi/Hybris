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
package de.hybris.platform.promotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type PromotionActionParameter first defined at extension promotionengineservices.
 */
@SuppressWarnings("all")
public class PromotionActionParameterModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PromotionActionParameter";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionActionParameter.uuid</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String UUID = "uuid";
	
	/** <i>Generated constant</i> - Attribute key of <code>PromotionActionParameter.value</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String VALUE = "value";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PromotionActionParameterModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PromotionActionParameterModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public PromotionActionParameterModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionActionParameter.uuid</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the uuid - Action parameter uuid
	 */
	@Accessor(qualifier = "uuid", type = Accessor.Type.GETTER)
	public String getUuid()
	{
		return getPersistenceContext().getPropertyValue(UUID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionActionParameter.value</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the value - Action parameter value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.GETTER)
	public Object getValue()
	{
		return getPersistenceContext().getPropertyValue(VALUE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionActionParameter.uuid</code> attribute defined at extension <code>promotionengineservices</code>. 
	 *  
	 * @param value the uuid - Action parameter uuid
	 */
	@Accessor(qualifier = "uuid", type = Accessor.Type.SETTER)
	public void setUuid(final String value)
	{
		getPersistenceContext().setPropertyValue(UUID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PromotionActionParameter.value</code> attribute defined at extension <code>promotionengineservices</code>. 
	 *  
	 * @param value the value - Action parameter value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.SETTER)
	public void setValue(final Object value)
	{
		getPersistenceContext().setPropertyValue(VALUE, value);
	}
	
}
