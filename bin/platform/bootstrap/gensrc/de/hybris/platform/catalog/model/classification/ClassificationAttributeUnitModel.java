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
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type ClassificationAttributeUnit first defined at extension catalog.
 */
@SuppressWarnings("all")
public class ClassificationAttributeUnitModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ClassificationAttributeUnit";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttributeUnit.systemVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYSTEMVERSION = "systemVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttributeUnit.code</code> attribute defined at extension <code>catalog</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttributeUnit.name</code> attribute defined at extension <code>catalog</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttributeUnit.externalID</code> attribute defined at extension <code>catalog</code>. */
	public static final String EXTERNALID = "externalID";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttributeUnit.symbol</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYMBOL = "symbol";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttributeUnit.unitType</code> attribute defined at extension <code>catalog</code>. */
	public static final String UNITTYPE = "unitType";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttributeUnit.conversionFactor</code> attribute defined at extension <code>catalog</code>. */
	public static final String CONVERSIONFACTOR = "conversionFactor";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationAttributeUnit.convertibleUnits</code> attribute defined at extension <code>catalog</code>. */
	public static final String CONVERTIBLEUNITS = "convertibleUnits";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ClassificationAttributeUnitModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ClassificationAttributeUnitModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ClassificationAttributeUnit</code> at extension <code>catalog</code>
	 * @param _symbol initial attribute declared by type <code>ClassificationAttributeUnit</code> at extension <code>catalog</code>
	 * @param _systemVersion initial attribute declared by type <code>ClassificationAttributeUnit</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ClassificationAttributeUnitModel(final String _code, final String _symbol, final ClassificationSystemVersionModel _systemVersion)
	{
		super();
		setCode(_code);
		setSymbol(_symbol);
		setSystemVersion(_systemVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ClassificationAttributeUnit</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _symbol initial attribute declared by type <code>ClassificationAttributeUnit</code> at extension <code>catalog</code>
	 * @param _systemVersion initial attribute declared by type <code>ClassificationAttributeUnit</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ClassificationAttributeUnitModel(final String _code, final ItemModel _owner, final String _symbol, final ClassificationSystemVersionModel _systemVersion)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setSymbol(_symbol);
		setSystemVersion(_systemVersion);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeUnit.code</code> attribute defined at extension <code>catalog</code>. 
	 * @return the code - external identifier refering to the actual classification system definition
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeUnit.conversionFactor</code> attribute defined at extension <code>catalog</code>. 
	 * @return the conversionFactor - Conversion factor
	 */
	@Accessor(qualifier = "conversionFactor", type = Accessor.Type.GETTER)
	public Double getConversionFactor()
	{
		return getPersistenceContext().getPropertyValue(CONVERSIONFACTOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeUnit.convertibleUnits</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the convertibleUnits - Convertible units
	 */
	@Accessor(qualifier = "convertibleUnits", type = Accessor.Type.GETTER)
	public Set<ClassificationAttributeUnitModel> getConvertibleUnits()
	{
		return getPersistenceContext().getPropertyValue(CONVERTIBLEUNITS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeUnit.externalID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the externalID - external identifier refering to the actual classification system definition
	 */
	@Accessor(qualifier = "externalID", type = Accessor.Type.GETTER)
	public String getExternalID()
	{
		return getPersistenceContext().getPropertyValue(EXTERNALID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeUnit.name</code> attribute defined at extension <code>catalog</code>. 
	 * @return the name - optional localized name of this class
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeUnit.name</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the name - optional localized name of this class
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeUnit.symbol</code> attribute defined at extension <code>catalog</code>. 
	 * @return the symbol - Symbol
	 */
	@Accessor(qualifier = "symbol", type = Accessor.Type.GETTER)
	public String getSymbol()
	{
		return getPersistenceContext().getPropertyValue(SYMBOL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeUnit.systemVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the systemVersion
	 */
	@Accessor(qualifier = "systemVersion", type = Accessor.Type.GETTER)
	public ClassificationSystemVersionModel getSystemVersion()
	{
		return getPersistenceContext().getPropertyValue(SYSTEMVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationAttributeUnit.unitType</code> attribute defined at extension <code>catalog</code>. 
	 * @return the unitType - Unit type
	 */
	@Accessor(qualifier = "unitType", type = Accessor.Type.GETTER)
	public String getUnitType()
	{
		return getPersistenceContext().getPropertyValue(UNITTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttributeUnit.code</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the code - external identifier refering to the actual classification system definition
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttributeUnit.conversionFactor</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the conversionFactor - Conversion factor
	 */
	@Accessor(qualifier = "conversionFactor", type = Accessor.Type.SETTER)
	public void setConversionFactor(final Double value)
	{
		getPersistenceContext().setPropertyValue(CONVERSIONFACTOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttributeUnit.externalID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the externalID - external identifier refering to the actual classification system definition
	 */
	@Accessor(qualifier = "externalID", type = Accessor.Type.SETTER)
	public void setExternalID(final String value)
	{
		getPersistenceContext().setPropertyValue(EXTERNALID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttributeUnit.name</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the name - optional localized name of this class
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttributeUnit.name</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the name - optional localized name of this class
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttributeUnit.symbol</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the symbol - Symbol
	 */
	@Accessor(qualifier = "symbol", type = Accessor.Type.SETTER)
	public void setSymbol(final String value)
	{
		getPersistenceContext().setPropertyValue(SYMBOL, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ClassificationAttributeUnit.systemVersion</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the systemVersion
	 */
	@Accessor(qualifier = "systemVersion", type = Accessor.Type.SETTER)
	public void setSystemVersion(final ClassificationSystemVersionModel value)
	{
		getPersistenceContext().setPropertyValue(SYSTEMVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationAttributeUnit.unitType</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the unitType - Unit type
	 */
	@Accessor(qualifier = "unitType", type = Accessor.Type.SETTER)
	public void setUnitType(final String value)
	{
		getPersistenceContext().setPropertyValue(UNITTYPE, value);
	}
	
}
