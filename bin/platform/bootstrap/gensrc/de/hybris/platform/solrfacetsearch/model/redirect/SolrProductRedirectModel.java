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
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrAbstractKeywordRedirectModel;

/**
 * Generated model class for type SolrProductRedirect first defined at extension solrfacetsearch.
 */
@SuppressWarnings("all")
public class SolrProductRedirectModel extends SolrAbstractKeywordRedirectModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SolrProductRedirect";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrProductRedirect.redirectItem</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String REDIRECTITEM = "redirectItem";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrProductRedirectModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrProductRedirectModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _redirectItem initial attribute declared by type <code>SolrProductRedirect</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrProductRedirectModel(final ProductModel _redirectItem)
	{
		super();
		setRedirectItem(_redirectItem);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _redirectItem initial attribute declared by type <code>SolrProductRedirect</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrProductRedirectModel(final ItemModel _owner, final ProductModel _redirectItem)
	{
		super();
		setOwner(_owner);
		setRedirectItem(_redirectItem);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrProductRedirect.redirectItem</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the redirectItem
	 */
	@Accessor(qualifier = "redirectItem", type = Accessor.Type.GETTER)
	public ProductModel getRedirectItem()
	{
		return getPersistenceContext().getPropertyValue(REDIRECTITEM);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrProductRedirect.redirectItem</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the redirectItem
	 */
	@Accessor(qualifier = "redirectItem", type = Accessor.Type.SETTER)
	public void setRedirectItem(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(REDIRECTITEM, value);
	}
	
}
