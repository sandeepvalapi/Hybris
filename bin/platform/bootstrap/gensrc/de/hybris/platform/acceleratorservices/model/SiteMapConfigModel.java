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
package de.hybris.platform.acceleratorservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorservices.model.SiteMapLanguageCurrencyModel;
import de.hybris.platform.acceleratorservices.model.SiteMapPageModel;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type SiteMapConfig first defined at extension acceleratorservices.
 * <p>
 * Holds the site map configurations.
 */
@SuppressWarnings("all")
public class SiteMapConfigModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SiteMapConfig";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMapConfig.configId</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String CONFIGID = "configId";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMapConfig.siteMapLanguageCurrencies</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String SITEMAPLANGUAGECURRENCIES = "siteMapLanguageCurrencies";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMapConfig.siteMapPages</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String SITEMAPPAGES = "siteMapPages";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMapConfig.siteMapTemplate</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String SITEMAPTEMPLATE = "siteMapTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMapConfig.customUrls</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String CUSTOMURLS = "customUrls";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SiteMapConfigModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SiteMapConfigModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public SiteMapConfigModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMapConfig.configId</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the configId
	 */
	@Accessor(qualifier = "configId", type = Accessor.Type.GETTER)
	public String getConfigId()
	{
		return getPersistenceContext().getPropertyValue(CONFIGID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMapConfig.customUrls</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the customUrls
	 */
	@Accessor(qualifier = "customUrls", type = Accessor.Type.GETTER)
	public Collection<String> getCustomUrls()
	{
		return getPersistenceContext().getPropertyValue(CUSTOMURLS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMapConfig.siteMapLanguageCurrencies</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the siteMapLanguageCurrencies
	 */
	@Accessor(qualifier = "siteMapLanguageCurrencies", type = Accessor.Type.GETTER)
	public Collection<SiteMapLanguageCurrencyModel> getSiteMapLanguageCurrencies()
	{
		return getPersistenceContext().getPropertyValue(SITEMAPLANGUAGECURRENCIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMapConfig.siteMapPages</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the siteMapPages
	 */
	@Accessor(qualifier = "siteMapPages", type = Accessor.Type.GETTER)
	public Collection<SiteMapPageModel> getSiteMapPages()
	{
		return getPersistenceContext().getPropertyValue(SITEMAPPAGES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMapConfig.siteMapTemplate</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the siteMapTemplate
	 */
	@Accessor(qualifier = "siteMapTemplate", type = Accessor.Type.GETTER)
	public RendererTemplateModel getSiteMapTemplate()
	{
		return getPersistenceContext().getPropertyValue(SITEMAPTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMapConfig.configId</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the configId
	 */
	@Accessor(qualifier = "configId", type = Accessor.Type.SETTER)
	public void setConfigId(final String value)
	{
		getPersistenceContext().setPropertyValue(CONFIGID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMapConfig.customUrls</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the customUrls
	 */
	@Accessor(qualifier = "customUrls", type = Accessor.Type.SETTER)
	public void setCustomUrls(final Collection<String> value)
	{
		getPersistenceContext().setPropertyValue(CUSTOMURLS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMapConfig.siteMapLanguageCurrencies</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the siteMapLanguageCurrencies
	 */
	@Accessor(qualifier = "siteMapLanguageCurrencies", type = Accessor.Type.SETTER)
	public void setSiteMapLanguageCurrencies(final Collection<SiteMapLanguageCurrencyModel> value)
	{
		getPersistenceContext().setPropertyValue(SITEMAPLANGUAGECURRENCIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMapConfig.siteMapPages</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the siteMapPages
	 */
	@Accessor(qualifier = "siteMapPages", type = Accessor.Type.SETTER)
	public void setSiteMapPages(final Collection<SiteMapPageModel> value)
	{
		getPersistenceContext().setPropertyValue(SITEMAPPAGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMapConfig.siteMapTemplate</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the siteMapTemplate
	 */
	@Accessor(qualifier = "siteMapTemplate", type = Accessor.Type.SETTER)
	public void setSiteMapTemplate(final RendererTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(SITEMAPTEMPLATE, value);
	}
	
}
