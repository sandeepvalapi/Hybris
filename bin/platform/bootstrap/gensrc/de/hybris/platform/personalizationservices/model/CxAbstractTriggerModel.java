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
package de.hybris.platform.personalizationservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CxAbstractTrigger first defined at extension personalizationservices.
 */
@SuppressWarnings("all")
public class CxAbstractTriggerModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CxAbstractTrigger";
	
	/**<i>Generated relation code constant for relation <code>CxTriggerToVariation</code> defining source attribute <code>variation</code> in extension <code>personalizationservices</code>.</i>*/
	public static final String _CXTRIGGERTOVARIATION = "CxTriggerToVariation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxAbstractTrigger.code</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxAbstractTrigger.catalogVersion</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxAbstractTrigger.variation</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String VARIATION = "variation";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CxAbstractTriggerModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CxAbstractTriggerModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxAbstractTrigger</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>CxAbstractTrigger</code> at extension <code>personalizationservices</code>
	 * @param _variation initial attribute declared by type <code>CxAbstractTrigger</code> at extension <code>personalizationservices</code>
	 */
	@Deprecated
	public CxAbstractTriggerModel(final CatalogVersionModel _catalogVersion, final String _code, final CxVariationModel _variation)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setVariation(_variation);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxAbstractTrigger</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>CxAbstractTrigger</code> at extension <code>personalizationservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _variation initial attribute declared by type <code>CxAbstractTrigger</code> at extension <code>personalizationservices</code>
	 */
	@Deprecated
	public CxAbstractTriggerModel(final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner, final CxVariationModel _variation)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
		setVariation(_variation);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxAbstractTrigger.catalogVersion</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxAbstractTrigger.code</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the code - Unique identifier
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxAbstractTrigger.variation</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the variation
	 */
	@Accessor(qualifier = "variation", type = Accessor.Type.GETTER)
	public CxVariationModel getVariation()
	{
		return getPersistenceContext().getPropertyValue(VARIATION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxAbstractTrigger.catalogVersion</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CxAbstractTrigger.code</code> attribute defined at extension <code>personalizationservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code - Unique identifier
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxAbstractTrigger.variation</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the variation
	 */
	@Accessor(qualifier = "variation", type = Accessor.Type.SETTER)
	public void setVariation(final CxVariationModel value)
	{
		getPersistenceContext().setPropertyValue(VARIATION, value);
	}
	
}
