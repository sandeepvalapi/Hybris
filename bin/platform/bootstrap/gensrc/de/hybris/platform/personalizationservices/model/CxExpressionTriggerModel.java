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
import de.hybris.platform.personalizationservices.model.CxAbstractTriggerModel;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type CxExpressionTrigger first defined at extension personalizationservices.
 */
@SuppressWarnings("all")
public class CxExpressionTriggerModel extends CxAbstractTriggerModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CxExpressionTrigger";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxExpressionTrigger.expression</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String EXPRESSION = "expression";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxExpressionTrigger.segments</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String SEGMENTS = "segments";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CxExpressionTriggerModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CxExpressionTriggerModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxAbstractTrigger</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>CxAbstractTrigger</code> at extension <code>personalizationservices</code>
	 * @param _expression initial attribute declared by type <code>CxExpressionTrigger</code> at extension <code>personalizationservices</code>
	 * @param _variation initial attribute declared by type <code>CxAbstractTrigger</code> at extension <code>personalizationservices</code>
	 */
	@Deprecated
	public CxExpressionTriggerModel(final CatalogVersionModel _catalogVersion, final String _code, final String _expression, final CxVariationModel _variation)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setExpression(_expression);
		setVariation(_variation);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxAbstractTrigger</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>CxAbstractTrigger</code> at extension <code>personalizationservices</code>
	 * @param _expression initial attribute declared by type <code>CxExpressionTrigger</code> at extension <code>personalizationservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _variation initial attribute declared by type <code>CxAbstractTrigger</code> at extension <code>personalizationservices</code>
	 */
	@Deprecated
	public CxExpressionTriggerModel(final CatalogVersionModel _catalogVersion, final String _code, final String _expression, final ItemModel _owner, final CxVariationModel _variation)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setExpression(_expression);
		setOwner(_owner);
		setVariation(_variation);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxExpressionTrigger.expression</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the expression - How segments are grouped
	 */
	@Accessor(qualifier = "expression", type = Accessor.Type.GETTER)
	public String getExpression()
	{
		return getPersistenceContext().getPropertyValue(EXPRESSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxExpressionTrigger.segments</code> attribute defined at extension <code>personalizationservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the segments
	 */
	@Accessor(qualifier = "segments", type = Accessor.Type.GETTER)
	public Collection<CxSegmentModel> getSegments()
	{
		return getPersistenceContext().getPropertyValue(SEGMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxExpressionTrigger.expression</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the expression - How segments are grouped
	 */
	@Accessor(qualifier = "expression", type = Accessor.Type.SETTER)
	public void setExpression(final String value)
	{
		getPersistenceContext().setPropertyValue(EXPRESSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxExpressionTrigger.segments</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the segments
	 */
	@Accessor(qualifier = "segments", type = Accessor.Type.SETTER)
	public void setSegments(final Collection<CxSegmentModel> value)
	{
		getPersistenceContext().setPropertyValue(SEGMENTS, value);
	}
	
}
