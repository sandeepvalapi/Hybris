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
package de.hybris.platform.solrfacetsearch.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.SolrSearchQueryTemplateModel;

/**
 * Generated model class for type SolrSearchQuerySort first defined at extension solrfacetsearch.
 */
@SuppressWarnings("all")
public class SolrSearchQuerySortModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SolrSearchQuerySort";
	
	/**<i>Generated relation code constant for relation <code>SolrSearchQueryTemplate2SolrSearchQuerySort</code> defining source attribute <code>searchQueryTemplate</code> in extension <code>solrfacetsearch</code>.</i>*/
	public static final String _SOLRSEARCHQUERYTEMPLATE2SOLRSEARCHQUERYSORT = "SolrSearchQueryTemplate2SolrSearchQuerySort";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQuerySort.field</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FIELD = "field";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQuerySort.ascending</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String ASCENDING = "ascending";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQuerySort.searchQueryTemplatePOS</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String SEARCHQUERYTEMPLATEPOS = "searchQueryTemplatePOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQuerySort.searchQueryTemplate</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String SEARCHQUERYTEMPLATE = "searchQueryTemplate";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrSearchQuerySortModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrSearchQuerySortModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _searchQueryTemplate initial attribute declared by type <code>SolrSearchQuerySort</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrSearchQuerySortModel(final SolrSearchQueryTemplateModel _searchQueryTemplate)
	{
		super();
		setSearchQueryTemplate(_searchQueryTemplate);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _searchQueryTemplate initial attribute declared by type <code>SolrSearchQuerySort</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrSearchQuerySortModel(final ItemModel _owner, final SolrSearchQueryTemplateModel _searchQueryTemplate)
	{
		super();
		setOwner(_owner);
		setSearchQueryTemplate(_searchQueryTemplate);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQuerySort.field</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the field
	 */
	@Accessor(qualifier = "field", type = Accessor.Type.GETTER)
	public String getField()
	{
		return getPersistenceContext().getPropertyValue(FIELD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQuerySort.searchQueryTemplate</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the searchQueryTemplate
	 */
	@Accessor(qualifier = "searchQueryTemplate", type = Accessor.Type.GETTER)
	public SolrSearchQueryTemplateModel getSearchQueryTemplate()
	{
		return getPersistenceContext().getPropertyValue(SEARCHQUERYTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQuerySort.ascending</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the ascending
	 */
	@Accessor(qualifier = "ascending", type = Accessor.Type.GETTER)
	public boolean isAscending()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(ASCENDING));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQuerySort.ascending</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the ascending
	 */
	@Accessor(qualifier = "ascending", type = Accessor.Type.SETTER)
	public void setAscending(final boolean value)
	{
		getPersistenceContext().setPropertyValue(ASCENDING, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQuerySort.field</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the field
	 */
	@Accessor(qualifier = "field", type = Accessor.Type.SETTER)
	public void setField(final String value)
	{
		getPersistenceContext().setPropertyValue(FIELD, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SolrSearchQuerySort.searchQueryTemplate</code> attribute defined at extension <code>solrfacetsearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the searchQueryTemplate
	 */
	@Accessor(qualifier = "searchQueryTemplate", type = Accessor.Type.SETTER)
	public void setSearchQueryTemplate(final SolrSearchQueryTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(SEARCHQUERYTEMPLATE, value);
	}
	
}
