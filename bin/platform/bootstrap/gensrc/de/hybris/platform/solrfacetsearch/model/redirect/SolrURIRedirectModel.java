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
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrAbstractKeywordRedirectModel;

/**
 * Generated model class for type SolrURIRedirect first defined at extension solrfacetsearch.
 */
@SuppressWarnings("all")
public class SolrURIRedirectModel extends SolrAbstractKeywordRedirectModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SolrURIRedirect";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrURIRedirect.url</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String URL = "url";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrURIRedirectModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrURIRedirectModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _url initial attribute declared by type <code>SolrURIRedirect</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrURIRedirectModel(final String _url)
	{
		super();
		setUrl(_url);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _url initial attribute declared by type <code>SolrURIRedirect</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrURIRedirectModel(final ItemModel _owner, final String _url)
	{
		super();
		setOwner(_owner);
		setUrl(_url);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrURIRedirect.url</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the url
	 */
	@Accessor(qualifier = "url", type = Accessor.Type.GETTER)
	public String getUrl()
	{
		return getPersistenceContext().getPropertyValue(URL);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrURIRedirect.url</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the url
	 */
	@Accessor(qualifier = "url", type = Accessor.Type.SETTER)
	public void setUrl(final String value)
	{
		getPersistenceContext().setPropertyValue(URL, value);
	}
	
}
