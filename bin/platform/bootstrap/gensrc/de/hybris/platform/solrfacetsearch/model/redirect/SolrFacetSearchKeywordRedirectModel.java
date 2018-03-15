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
package de.hybris.platform.solrfacetsearch.model.redirect;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.enums.KeywordRedirectMatchType;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrAbstractKeywordRedirectModel;

/**
 * Generated model class for type SolrFacetSearchKeywordRedirect first defined at extension solrfacetsearch.
 */
@SuppressWarnings("all")
public class SolrFacetSearchKeywordRedirectModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SolrFacetSearchKeywordRedirect";
	
	/**<i>Generated relation code constant for relation <code>SolrFacetSearchConfig2SolrFacetSearchKeywordRedirect</code> defining source attribute <code>facetSearchConfig</code> in extension <code>solrfacetsearch</code>.</i>*/
	public static final String _SOLRFACETSEARCHCONFIG2SOLRFACETSEARCHKEYWORDREDIRECT = "SolrFacetSearchConfig2SolrFacetSearchKeywordRedirect";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrFacetSearchKeywordRedirect.language</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String LANGUAGE = "language";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrFacetSearchKeywordRedirect.keyword</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String KEYWORD = "keyword";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrFacetSearchKeywordRedirect.matchType</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String MATCHTYPE = "matchType";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrFacetSearchKeywordRedirect.ignoreCase</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String IGNORECASE = "ignoreCase";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrFacetSearchKeywordRedirect.redirect</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String REDIRECT = "redirect";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrFacetSearchKeywordRedirect.facetSearchConfig</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FACETSEARCHCONFIG = "facetSearchConfig";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrFacetSearchKeywordRedirectModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrFacetSearchKeywordRedirectModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _keyword initial attribute declared by type <code>SolrFacetSearchKeywordRedirect</code> at extension <code>solrfacetsearch</code>
	 * @param _language initial attribute declared by type <code>SolrFacetSearchKeywordRedirect</code> at extension <code>solrfacetsearch</code>
	 * @param _redirect initial attribute declared by type <code>SolrFacetSearchKeywordRedirect</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrFacetSearchKeywordRedirectModel(final String _keyword, final LanguageModel _language, final SolrAbstractKeywordRedirectModel _redirect)
	{
		super();
		setKeyword(_keyword);
		setLanguage(_language);
		setRedirect(_redirect);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _keyword initial attribute declared by type <code>SolrFacetSearchKeywordRedirect</code> at extension <code>solrfacetsearch</code>
	 * @param _language initial attribute declared by type <code>SolrFacetSearchKeywordRedirect</code> at extension <code>solrfacetsearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _redirect initial attribute declared by type <code>SolrFacetSearchKeywordRedirect</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrFacetSearchKeywordRedirectModel(final String _keyword, final LanguageModel _language, final ItemModel _owner, final SolrAbstractKeywordRedirectModel _redirect)
	{
		super();
		setKeyword(_keyword);
		setLanguage(_language);
		setOwner(_owner);
		setRedirect(_redirect);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrFacetSearchKeywordRedirect.facetSearchConfig</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the facetSearchConfig
	 */
	@Accessor(qualifier = "facetSearchConfig", type = Accessor.Type.GETTER)
	public SolrFacetSearchConfigModel getFacetSearchConfig()
	{
		return getPersistenceContext().getPropertyValue(FACETSEARCHCONFIG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrFacetSearchKeywordRedirect.ignoreCase</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the ignoreCase
	 */
	@Accessor(qualifier = "ignoreCase", type = Accessor.Type.GETTER)
	public Boolean getIgnoreCase()
	{
		return getPersistenceContext().getPropertyValue(IGNORECASE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrFacetSearchKeywordRedirect.keyword</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the keyword
	 */
	@Accessor(qualifier = "keyword", type = Accessor.Type.GETTER)
	public String getKeyword()
	{
		return getPersistenceContext().getPropertyValue(KEYWORD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrFacetSearchKeywordRedirect.language</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public LanguageModel getLanguage()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrFacetSearchKeywordRedirect.matchType</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the matchType
	 */
	@Accessor(qualifier = "matchType", type = Accessor.Type.GETTER)
	public KeywordRedirectMatchType getMatchType()
	{
		return getPersistenceContext().getPropertyValue(MATCHTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrFacetSearchKeywordRedirect.redirect</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the redirect
	 */
	@Accessor(qualifier = "redirect", type = Accessor.Type.GETTER)
	public SolrAbstractKeywordRedirectModel getRedirect()
	{
		return getPersistenceContext().getPropertyValue(REDIRECT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrFacetSearchKeywordRedirect.facetSearchConfig</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the facetSearchConfig
	 */
	@Accessor(qualifier = "facetSearchConfig", type = Accessor.Type.SETTER)
	public void setFacetSearchConfig(final SolrFacetSearchConfigModel value)
	{
		getPersistenceContext().setPropertyValue(FACETSEARCHCONFIG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrFacetSearchKeywordRedirect.ignoreCase</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the ignoreCase
	 */
	@Accessor(qualifier = "ignoreCase", type = Accessor.Type.SETTER)
	public void setIgnoreCase(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(IGNORECASE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrFacetSearchKeywordRedirect.keyword</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the keyword
	 */
	@Accessor(qualifier = "keyword", type = Accessor.Type.SETTER)
	public void setKeyword(final String value)
	{
		getPersistenceContext().setPropertyValue(KEYWORD, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrFacetSearchKeywordRedirect.language</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrFacetSearchKeywordRedirect.matchType</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the matchType
	 */
	@Accessor(qualifier = "matchType", type = Accessor.Type.SETTER)
	public void setMatchType(final KeywordRedirectMatchType value)
	{
		getPersistenceContext().setPropertyValue(MATCHTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrFacetSearchKeywordRedirect.redirect</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the redirect
	 */
	@Accessor(qualifier = "redirect", type = Accessor.Type.SETTER)
	public void setRedirect(final SolrAbstractKeywordRedirectModel value)
	{
		getPersistenceContext().setPropertyValue(REDIRECT, value);
	}
	
}
