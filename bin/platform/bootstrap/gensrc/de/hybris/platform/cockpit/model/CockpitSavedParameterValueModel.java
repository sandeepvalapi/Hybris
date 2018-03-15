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
import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CockpitSavedParameterValue first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class CockpitSavedParameterValueModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CockpitSavedParameterValue";
	
	/**<i>Generated relation code constant for relation <code>CockpitSavedQuery2CockpitSavedParameterValueRelation</code> defining source attribute <code>cockpitSavedQuery</code> in extension <code>cockpit</code>.</i>*/
	public static final String _COCKPITSAVEDQUERY2COCKPITSAVEDPARAMETERVALUERELATION = "CockpitSavedQuery2CockpitSavedParameterValueRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedParameterValue.rawValue</code> attribute defined at extension <code>cockpit</code>. */
	public static final String RAWVALUE = "rawValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedParameterValue.operatorQualifier</code> attribute defined at extension <code>cockpit</code>. */
	public static final String OPERATORQUALIFIER = "operatorQualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedParameterValue.languageIso</code> attribute defined at extension <code>cockpit</code>. */
	public static final String LANGUAGEISO = "languageIso";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedParameterValue.parameterQualifier</code> attribute defined at extension <code>cockpit</code>. */
	public static final String PARAMETERQUALIFIER = "parameterQualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedParameterValue.caseSensitive</code> attribute defined at extension <code>cockpit</code>. */
	public static final String CASESENSITIVE = "caseSensitive";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedParameterValue.reference</code> attribute defined at extension <code>cockpit</code>. */
	public static final String REFERENCE = "reference";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedParameterValue.cockpitSavedQuery</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COCKPITSAVEDQUERY = "cockpitSavedQuery";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CockpitSavedParameterValueModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CockpitSavedParameterValueModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _operatorQualifier initial attribute declared by type <code>CockpitSavedParameterValue</code> at extension <code>cockpit</code>
	 * @param _parameterQualifier initial attribute declared by type <code>CockpitSavedParameterValue</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CockpitSavedParameterValueModel(final String _operatorQualifier, final String _parameterQualifier)
	{
		super();
		setOperatorQualifier(_operatorQualifier);
		setParameterQualifier(_parameterQualifier);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _operatorQualifier initial attribute declared by type <code>CockpitSavedParameterValue</code> at extension <code>cockpit</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _parameterQualifier initial attribute declared by type <code>CockpitSavedParameterValue</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CockpitSavedParameterValueModel(final String _operatorQualifier, final ItemModel _owner, final String _parameterQualifier)
	{
		super();
		setOperatorQualifier(_operatorQualifier);
		setOwner(_owner);
		setParameterQualifier(_parameterQualifier);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedParameterValue.caseSensitive</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the caseSensitive
	 */
	@Accessor(qualifier = "caseSensitive", type = Accessor.Type.GETTER)
	public Boolean getCaseSensitive()
	{
		return getPersistenceContext().getPropertyValue(CASESENSITIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedParameterValue.cockpitSavedQuery</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the cockpitSavedQuery
	 */
	@Accessor(qualifier = "cockpitSavedQuery", type = Accessor.Type.GETTER)
	public CockpitSavedQueryModel getCockpitSavedQuery()
	{
		return getPersistenceContext().getPropertyValue(COCKPITSAVEDQUERY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedParameterValue.languageIso</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the languageIso
	 */
	@Accessor(qualifier = "languageIso", type = Accessor.Type.GETTER)
	public String getLanguageIso()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGEISO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedParameterValue.operatorQualifier</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the operatorQualifier
	 */
	@Accessor(qualifier = "operatorQualifier", type = Accessor.Type.GETTER)
	public String getOperatorQualifier()
	{
		return getPersistenceContext().getPropertyValue(OPERATORQUALIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedParameterValue.parameterQualifier</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the parameterQualifier
	 */
	@Accessor(qualifier = "parameterQualifier", type = Accessor.Type.GETTER)
	public String getParameterQualifier()
	{
		return getPersistenceContext().getPropertyValue(PARAMETERQUALIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedParameterValue.rawValue</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the rawValue
	 */
	@Accessor(qualifier = "rawValue", type = Accessor.Type.GETTER)
	public String getRawValue()
	{
		return getPersistenceContext().getPropertyValue(RAWVALUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedParameterValue.reference</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the reference
	 */
	@Accessor(qualifier = "reference", type = Accessor.Type.GETTER)
	public Boolean getReference()
	{
		return getPersistenceContext().getPropertyValue(REFERENCE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedParameterValue.caseSensitive</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the caseSensitive
	 */
	@Accessor(qualifier = "caseSensitive", type = Accessor.Type.SETTER)
	public void setCaseSensitive(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(CASESENSITIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedParameterValue.cockpitSavedQuery</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the cockpitSavedQuery
	 */
	@Accessor(qualifier = "cockpitSavedQuery", type = Accessor.Type.SETTER)
	public void setCockpitSavedQuery(final CockpitSavedQueryModel value)
	{
		getPersistenceContext().setPropertyValue(COCKPITSAVEDQUERY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedParameterValue.languageIso</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the languageIso
	 */
	@Accessor(qualifier = "languageIso", type = Accessor.Type.SETTER)
	public void setLanguageIso(final String value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGEISO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedParameterValue.operatorQualifier</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the operatorQualifier
	 */
	@Accessor(qualifier = "operatorQualifier", type = Accessor.Type.SETTER)
	public void setOperatorQualifier(final String value)
	{
		getPersistenceContext().setPropertyValue(OPERATORQUALIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedParameterValue.parameterQualifier</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the parameterQualifier
	 */
	@Accessor(qualifier = "parameterQualifier", type = Accessor.Type.SETTER)
	public void setParameterQualifier(final String value)
	{
		getPersistenceContext().setPropertyValue(PARAMETERQUALIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedParameterValue.rawValue</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the rawValue
	 */
	@Accessor(qualifier = "rawValue", type = Accessor.Type.SETTER)
	public void setRawValue(final String value)
	{
		getPersistenceContext().setPropertyValue(RAWVALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedParameterValue.reference</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the reference
	 */
	@Accessor(qualifier = "reference", type = Accessor.Type.SETTER)
	public void setReference(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(REFERENCE, value);
	}
	
}
