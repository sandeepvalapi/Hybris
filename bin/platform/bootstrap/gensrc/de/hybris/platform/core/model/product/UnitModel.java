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
package de.hybris.platform.core.model.product;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type Unit first defined at extension core.
 */
@SuppressWarnings("all")
public class UnitModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Unit";
	
	/** <i>Generated constant</i> - Attribute key of <code>Unit.code</code> attribute defined at extension <code>core</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Unit.conversion</code> attribute defined at extension <code>core</code>. */
	public static final String CONVERSION = "conversion";
	
	/** <i>Generated constant</i> - Attribute key of <code>Unit.name</code> attribute defined at extension <code>core</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>Unit.unitType</code> attribute defined at extension <code>core</code>. */
	public static final String UNITTYPE = "unitType";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public UnitModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public UnitModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Unit</code> at extension <code>core</code>
	 * @param _unitType initial attribute declared by type <code>Unit</code> at extension <code>core</code>
	 */
	@Deprecated
	public UnitModel(final String _code, final String _unitType)
	{
		super();
		setCode(_code);
		setUnitType(_unitType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Unit</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _unitType initial attribute declared by type <code>Unit</code> at extension <code>core</code>
	 */
	@Deprecated
	public UnitModel(final String _code, final ItemModel _owner, final String _unitType)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setUnitType(_unitType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Unit.code</code> attribute defined at extension <code>core</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Unit.conversion</code> attribute defined at extension <code>core</code>. 
	 * @return the conversion
	 */
	@Accessor(qualifier = "conversion", type = Accessor.Type.GETTER)
	public Double getConversion()
	{
		return getPersistenceContext().getPropertyValue(CONVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Unit.name</code> attribute defined at extension <code>core</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Unit.name</code> attribute defined at extension <code>core</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>Unit.unitType</code> attribute defined at extension <code>core</code>. 
	 * @return the unitType
	 */
	@Accessor(qualifier = "unitType", type = Accessor.Type.GETTER)
	public String getUnitType()
	{
		return getPersistenceContext().getPropertyValue(UNITTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Unit.code</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Unit.conversion</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the conversion
	 */
	@Accessor(qualifier = "conversion", type = Accessor.Type.SETTER)
	public void setConversion(final Double value)
	{
		getPersistenceContext().setPropertyValue(CONVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Unit.name</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Unit.name</code> attribute defined at extension <code>core</code>. 
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
	 * <i>Generated method</i> - Setter of <code>Unit.unitType</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the unitType
	 */
	@Accessor(qualifier = "unitType", type = Accessor.Type.SETTER)
	public void setUnitType(final String value)
	{
		getPersistenceContext().setPropertyValue(UNITTYPE, value);
	}
	
}
