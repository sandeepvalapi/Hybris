/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:39 PM
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
 */
package de.hybris.platform.commercewebservicescommons.dto.search.facetdata;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.SearchStateWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.facetdata.BreadcrumbWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.facetdata.FacetWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.facetdata.SpellingSuggestionWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.SortWsDTO;
import java.util.List;

/**
 * POJO containing the result page for product search.
 */
public  class ProductSearchPageWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ProductSearchPageWsDTO.freeTextSearch</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String freeTextSearch;

	/** <i>Generated property</i> for <code>ProductSearchPageWsDTO.categoryCode</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String categoryCode;

	/** <i>Generated property</i> for <code>ProductSearchPageWsDTO.keywordRedirectUrl</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String keywordRedirectUrl;

	/** <i>Generated property</i> for <code>ProductSearchPageWsDTO.spellingSuggestion</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private SpellingSuggestionWsDTO spellingSuggestion;

	/** <i>Generated property</i> for <code>ProductSearchPageWsDTO.products</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<ProductWsDTO> products;

	/** <i>Generated property</i> for <code>ProductSearchPageWsDTO.sorts</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<SortWsDTO> sorts;

	/** <i>Generated property</i> for <code>ProductSearchPageWsDTO.pagination</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PaginationWsDTO pagination;

	/** <i>Generated property</i> for <code>ProductSearchPageWsDTO.currentQuery</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private SearchStateWsDTO currentQuery;

	/** <i>Generated property</i> for <code>ProductSearchPageWsDTO.breadcrumbs</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<BreadcrumbWsDTO> breadcrumbs;

	/** <i>Generated property</i> for <code>ProductSearchPageWsDTO.facets</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<FacetWsDTO> facets;
	
	public ProductSearchPageWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setFreeTextSearch(final String freeTextSearch)
	{
		this.freeTextSearch = freeTextSearch;
	}

		
	
	public String getFreeTextSearch() 
	{
		return freeTextSearch;
	}
	
		
	
	public void setCategoryCode(final String categoryCode)
	{
		this.categoryCode = categoryCode;
	}

		
	
	public String getCategoryCode() 
	{
		return categoryCode;
	}
	
		
	
	public void setKeywordRedirectUrl(final String keywordRedirectUrl)
	{
		this.keywordRedirectUrl = keywordRedirectUrl;
	}

		
	
	public String getKeywordRedirectUrl() 
	{
		return keywordRedirectUrl;
	}
	
		
	
	public void setSpellingSuggestion(final SpellingSuggestionWsDTO spellingSuggestion)
	{
		this.spellingSuggestion = spellingSuggestion;
	}

		
	
	public SpellingSuggestionWsDTO getSpellingSuggestion() 
	{
		return spellingSuggestion;
	}
	
		
	
	public void setProducts(final List<ProductWsDTO> products)
	{
		this.products = products;
	}

		
	
	public List<ProductWsDTO> getProducts() 
	{
		return products;
	}
	
		
	
	public void setSorts(final List<SortWsDTO> sorts)
	{
		this.sorts = sorts;
	}

		
	
	public List<SortWsDTO> getSorts() 
	{
		return sorts;
	}
	
		
	
	public void setPagination(final PaginationWsDTO pagination)
	{
		this.pagination = pagination;
	}

		
	
	public PaginationWsDTO getPagination() 
	{
		return pagination;
	}
	
		
	
	public void setCurrentQuery(final SearchStateWsDTO currentQuery)
	{
		this.currentQuery = currentQuery;
	}

		
	
	public SearchStateWsDTO getCurrentQuery() 
	{
		return currentQuery;
	}
	
		
	
	public void setBreadcrumbs(final List<BreadcrumbWsDTO> breadcrumbs)
	{
		this.breadcrumbs = breadcrumbs;
	}

		
	
	public List<BreadcrumbWsDTO> getBreadcrumbs() 
	{
		return breadcrumbs;
	}
	
		
	
	public void setFacets(final List<FacetWsDTO> facets)
	{
		this.facets = facets;
	}

		
	
	public List<FacetWsDTO> getFacets() 
	{
		return facets;
	}
	


}
