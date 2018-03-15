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
package de.hybris.platform.processing.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.model.AbstractRetentionRuleModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Map;

/**
 * Generated model class for type FlexibleSearchRetentionRule first defined at extension processing.
 */
@SuppressWarnings("all")
public class FlexibleSearchRetentionRuleModel extends AbstractRetentionRuleModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "FlexibleSearchRetentionRule";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlexibleSearchRetentionRule.searchQuery</code> attribute defined at extension <code>processing</code>. */
	public static final String SEARCHQUERY = "searchQuery";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlexibleSearchRetentionRule.queryParameters</code> attribute defined at extension <code>processing</code>. */
	public static final String QUERYPARAMETERS = "queryParameters";
	
	/** <i>Generated constant</i> - Attribute key of <code>FlexibleSearchRetentionRule.retentionTimeSeconds</code> attribute defined at extension <code>processing</code>. */
	public static final String RETENTIONTIMESECONDS = "retentionTimeSeconds";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public FlexibleSearchRetentionRuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public FlexibleSearchRetentionRuleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _actionReference initial attribute declared by type <code>AbstractRetentionRule</code> at extension <code>processing</code>
	 * @param _code initial attribute declared by type <code>AbstractRetentionRule</code> at extension <code>processing</code>
	 * @param _searchQuery initial attribute declared by type <code>FlexibleSearchRetentionRule</code> at extension <code>processing</code>
	 */
	@Deprecated
	public FlexibleSearchRetentionRuleModel(final String _actionReference, final String _code, final String _searchQuery)
	{
		super();
		setActionReference(_actionReference);
		setCode(_code);
		setSearchQuery(_searchQuery);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _actionReference initial attribute declared by type <code>AbstractRetentionRule</code> at extension <code>processing</code>
	 * @param _code initial attribute declared by type <code>AbstractRetentionRule</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _searchQuery initial attribute declared by type <code>FlexibleSearchRetentionRule</code> at extension <code>processing</code>
	 */
	@Deprecated
	public FlexibleSearchRetentionRuleModel(final String _actionReference, final String _code, final ItemModel _owner, final String _searchQuery)
	{
		super();
		setActionReference(_actionReference);
		setCode(_code);
		setOwner(_owner);
		setSearchQuery(_searchQuery);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlexibleSearchRetentionRule.queryParameters</code> attribute defined at extension <code>processing</code>. 
	 * @return the queryParameters
	 */
	@Accessor(qualifier = "queryParameters", type = Accessor.Type.GETTER)
	public Map<String,String> getQueryParameters()
	{
		return getPersistenceContext().getPropertyValue(QUERYPARAMETERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlexibleSearchRetentionRule.retentionTimeSeconds</code> attribute defined at extension <code>processing</code>. 
	 * @return the retentionTimeSeconds
	 */
	@Accessor(qualifier = "retentionTimeSeconds", type = Accessor.Type.GETTER)
	public Long getRetentionTimeSeconds()
	{
		return getPersistenceContext().getPropertyValue(RETENTIONTIMESECONDS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FlexibleSearchRetentionRule.searchQuery</code> attribute defined at extension <code>processing</code>. 
	 * @return the searchQuery
	 */
	@Accessor(qualifier = "searchQuery", type = Accessor.Type.GETTER)
	public String getSearchQuery()
	{
		return getPersistenceContext().getPropertyValue(SEARCHQUERY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlexibleSearchRetentionRule.queryParameters</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the queryParameters
	 */
	@Accessor(qualifier = "queryParameters", type = Accessor.Type.SETTER)
	public void setQueryParameters(final Map<String,String> value)
	{
		getPersistenceContext().setPropertyValue(QUERYPARAMETERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlexibleSearchRetentionRule.retentionTimeSeconds</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the retentionTimeSeconds
	 */
	@Accessor(qualifier = "retentionTimeSeconds", type = Accessor.Type.SETTER)
	public void setRetentionTimeSeconds(final Long value)
	{
		getPersistenceContext().setPropertyValue(RETENTIONTIMESECONDS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FlexibleSearchRetentionRule.searchQuery</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the searchQuery
	 */
	@Accessor(qualifier = "searchQuery", type = Accessor.Type.SETTER)
	public void setSearchQuery(final String value)
	{
		getPersistenceContext().setPropertyValue(SEARCHQUERY, value);
	}
	
}
