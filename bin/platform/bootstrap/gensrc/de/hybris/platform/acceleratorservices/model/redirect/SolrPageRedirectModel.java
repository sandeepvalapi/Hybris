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
package de.hybris.platform.acceleratorservices.model.redirect;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrAbstractKeywordRedirectModel;

/**
 * Generated model class for type SolrPageRedirect first defined at extension acceleratorservices.
 */
@SuppressWarnings("all")
public class SolrPageRedirectModel extends SolrAbstractKeywordRedirectModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SolrPageRedirect";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrPageRedirect.redirectItem</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String REDIRECTITEM = "redirectItem";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrPageRedirectModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrPageRedirectModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _redirectItem initial attribute declared by type <code>SolrPageRedirect</code> at extension <code>acceleratorservices</code>
	 */
	@Deprecated
	public SolrPageRedirectModel(final AbstractPageModel _redirectItem)
	{
		super();
		setRedirectItem(_redirectItem);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _redirectItem initial attribute declared by type <code>SolrPageRedirect</code> at extension <code>acceleratorservices</code>
	 */
	@Deprecated
	public SolrPageRedirectModel(final ItemModel _owner, final AbstractPageModel _redirectItem)
	{
		super();
		setOwner(_owner);
		setRedirectItem(_redirectItem);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrPageRedirect.redirectItem</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the redirectItem
	 */
	@Accessor(qualifier = "redirectItem", type = Accessor.Type.GETTER)
	public AbstractPageModel getRedirectItem()
	{
		return getPersistenceContext().getPropertyValue(REDIRECTITEM);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SolrPageRedirect.redirectItem</code> attribute defined at extension <code>acceleratorservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the redirectItem
	 */
	@Accessor(qualifier = "redirectItem", type = Accessor.Type.SETTER)
	public void setRedirectItem(final AbstractPageModel value)
	{
		getPersistenceContext().setPropertyValue(REDIRECTITEM, value);
	}
	
}
