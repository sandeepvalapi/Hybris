/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:41 PM
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
package de.hybris.platform.commerceservices.impersonation;

import java.io.Serializable;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.enums.UserTaxGroup;
import java.util.Collection;

public  class ImpersonationContext  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ImpersonationContext.catalogVersions</code> property defined at extension <code>commerceservices</code>. */
		
	private Collection<CatalogVersionModel> catalogVersions;

	/** <i>Generated property</i> for <code>ImpersonationContext.user</code> property defined at extension <code>commerceservices</code>. */
		
	private UserModel user;

	/** <i>Generated property</i> for <code>ImpersonationContext.language</code> property defined at extension <code>commerceservices</code>. */
		
	private LanguageModel language;

	/** <i>Generated property</i> for <code>ImpersonationContext.currency</code> property defined at extension <code>commerceservices</code>. */
		
	private CurrencyModel currency;

	/** <i>Generated property</i> for <code>ImpersonationContext.taxGroup</code> property defined at extension <code>commerceservices</code>. */
		
	private UserTaxGroup taxGroup;

	/** <i>Generated property</i> for <code>ImpersonationContext.site</code> property defined at extension <code>commerceservices</code>. */
		
	private BaseSiteModel site;

	/** <i>Generated property</i> for <code>ImpersonationContext.order</code> property defined at extension <code>commerceservices</code>. */
		
	private AbstractOrderModel order;
	
	public ImpersonationContext()
	{
		// default constructor
	}
	
		
	
	public void setCatalogVersions(final Collection<CatalogVersionModel> catalogVersions)
	{
		this.catalogVersions = catalogVersions;
	}

		
	
	public Collection<CatalogVersionModel> getCatalogVersions() 
	{
		return catalogVersions;
	}
	
		
	
	public void setUser(final UserModel user)
	{
		this.user = user;
	}

		
	
	public UserModel getUser() 
	{
		return user;
	}
	
		
	
	public void setLanguage(final LanguageModel language)
	{
		this.language = language;
	}

		
	
	public LanguageModel getLanguage() 
	{
		return language;
	}
	
		
	
	public void setCurrency(final CurrencyModel currency)
	{
		this.currency = currency;
	}

		
	
	public CurrencyModel getCurrency() 
	{
		return currency;
	}
	
		
	
	public void setTaxGroup(final UserTaxGroup taxGroup)
	{
		this.taxGroup = taxGroup;
	}

		
	
	public UserTaxGroup getTaxGroup() 
	{
		return taxGroup;
	}
	
		
	
	public void setSite(final BaseSiteModel site)
	{
		this.site = site;
	}

		
	
	public BaseSiteModel getSite() 
	{
		return site;
	}
	
		
	
	public void setOrder(final AbstractOrderModel order)
	{
		this.order = order;
	}

		
	
	public AbstractOrderModel getOrder() 
	{
		return order;
	}
	


}
