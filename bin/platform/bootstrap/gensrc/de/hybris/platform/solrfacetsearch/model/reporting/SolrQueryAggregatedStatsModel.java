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
package de.hybris.platform.solrfacetsearch.model.reporting;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import java.util.Date;

/**
 * Generated model class for type SolrQueryAggregatedStats first defined at extension solrfacetsearch.
 */
@SuppressWarnings("all")
public class SolrQueryAggregatedStatsModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SolrQueryAggregatedStats";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrQueryAggregatedStats.time</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String TIME = "time";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrQueryAggregatedStats.indexConfig</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String INDEXCONFIG = "indexConfig";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrQueryAggregatedStats.language</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String LANGUAGE = "language";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrQueryAggregatedStats.query</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String QUERY = "query";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrQueryAggregatedStats.count</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String COUNT = "count";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrQueryAggregatedStats.avgNumberOfResults</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String AVGNUMBEROFRESULTS = "avgNumberOfResults";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrQueryAggregatedStatsModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrQueryAggregatedStatsModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _avgNumberOfResults initial attribute declared by type <code>SolrQueryAggregatedStats</code> at extension <code>solrfacetsearch</code>
	 * @param _count initial attribute declared by type <code>SolrQueryAggregatedStats</code> at extension <code>solrfacetsearch</code>
	 * @param _indexConfig initial attribute declared by type <code>SolrQueryAggregatedStats</code> at extension <code>solrfacetsearch</code>
	 * @param _language initial attribute declared by type <code>SolrQueryAggregatedStats</code> at extension <code>solrfacetsearch</code>
	 * @param _query initial attribute declared by type <code>SolrQueryAggregatedStats</code> at extension <code>solrfacetsearch</code>
	 * @param _time initial attribute declared by type <code>SolrQueryAggregatedStats</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrQueryAggregatedStatsModel(final double _avgNumberOfResults, final long _count, final SolrFacetSearchConfigModel _indexConfig, final LanguageModel _language, final String _query, final Date _time)
	{
		super();
		setAvgNumberOfResults(_avgNumberOfResults);
		setCount(_count);
		setIndexConfig(_indexConfig);
		setLanguage(_language);
		setQuery(_query);
		setTime(_time);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _avgNumberOfResults initial attribute declared by type <code>SolrQueryAggregatedStats</code> at extension <code>solrfacetsearch</code>
	 * @param _count initial attribute declared by type <code>SolrQueryAggregatedStats</code> at extension <code>solrfacetsearch</code>
	 * @param _indexConfig initial attribute declared by type <code>SolrQueryAggregatedStats</code> at extension <code>solrfacetsearch</code>
	 * @param _language initial attribute declared by type <code>SolrQueryAggregatedStats</code> at extension <code>solrfacetsearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _query initial attribute declared by type <code>SolrQueryAggregatedStats</code> at extension <code>solrfacetsearch</code>
	 * @param _time initial attribute declared by type <code>SolrQueryAggregatedStats</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrQueryAggregatedStatsModel(final double _avgNumberOfResults, final long _count, final SolrFacetSearchConfigModel _indexConfig, final LanguageModel _language, final ItemModel _owner, final String _query, final Date _time)
	{
		super();
		setAvgNumberOfResults(_avgNumberOfResults);
		setCount(_count);
		setIndexConfig(_indexConfig);
		setLanguage(_language);
		setOwner(_owner);
		setQuery(_query);
		setTime(_time);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrQueryAggregatedStats.avgNumberOfResults</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the avgNumberOfResults
	 */
	@Accessor(qualifier = "avgNumberOfResults", type = Accessor.Type.GETTER)
	public double getAvgNumberOfResults()
	{
		return toPrimitive((Double)getPersistenceContext().getPropertyValue(AVGNUMBEROFRESULTS));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrQueryAggregatedStats.count</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the count
	 */
	@Accessor(qualifier = "count", type = Accessor.Type.GETTER)
	public long getCount()
	{
		return toPrimitive((Long)getPersistenceContext().getPropertyValue(COUNT));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrQueryAggregatedStats.indexConfig</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the indexConfig
	 */
	@Accessor(qualifier = "indexConfig", type = Accessor.Type.GETTER)
	public SolrFacetSearchConfigModel getIndexConfig()
	{
		return getPersistenceContext().getPropertyValue(INDEXCONFIG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrQueryAggregatedStats.language</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public LanguageModel getLanguage()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrQueryAggregatedStats.query</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the query
	 */
	@Accessor(qualifier = "query", type = Accessor.Type.GETTER)
	public String getQuery()
	{
		return getPersistenceContext().getPropertyValue(QUERY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrQueryAggregatedStats.time</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the time
	 */
	@Accessor(qualifier = "time", type = Accessor.Type.GETTER)
	public Date getTime()
	{
		return getPersistenceContext().getPropertyValue(TIME);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrQueryAggregatedStats.avgNumberOfResults</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the avgNumberOfResults
	 */
	@Accessor(qualifier = "avgNumberOfResults", type = Accessor.Type.SETTER)
	public void setAvgNumberOfResults(final double value)
	{
		getPersistenceContext().setPropertyValue(AVGNUMBEROFRESULTS, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrQueryAggregatedStats.count</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the count
	 */
	@Accessor(qualifier = "count", type = Accessor.Type.SETTER)
	public void setCount(final long value)
	{
		getPersistenceContext().setPropertyValue(COUNT, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrQueryAggregatedStats.indexConfig</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the indexConfig
	 */
	@Accessor(qualifier = "indexConfig", type = Accessor.Type.SETTER)
	public void setIndexConfig(final SolrFacetSearchConfigModel value)
	{
		getPersistenceContext().setPropertyValue(INDEXCONFIG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrQueryAggregatedStats.language</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrQueryAggregatedStats.query</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the query
	 */
	@Accessor(qualifier = "query", type = Accessor.Type.SETTER)
	public void setQuery(final String value)
	{
		getPersistenceContext().setPropertyValue(QUERY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrQueryAggregatedStats.time</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the time
	 */
	@Accessor(qualifier = "time", type = Accessor.Type.SETTER)
	public void setTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(TIME, value);
	}
	
}
