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
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SiteMapMediaCronJob first defined at extension acceleratorservices.
 * <p>
 * Cronjob to generate the sitemap media per site.
 */
@SuppressWarnings("all")
public class SiteMapMediaCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SiteMapMediaCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMapMediaCronJob.contentSite</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String CONTENTSITE = "contentSite";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMapMediaCronJob.siteMapUrlLimitPerFile</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String SITEMAPURLLIMITPERFILE = "siteMapUrlLimitPerFile";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SiteMapMediaCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SiteMapMediaCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public SiteMapMediaCronJobModel(final JobModel _job)
	{
		super();
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public SiteMapMediaCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMapMediaCronJob.contentSite</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the contentSite
	 */
	@Accessor(qualifier = "contentSite", type = Accessor.Type.GETTER)
	public CMSSiteModel getContentSite()
	{
		return getPersistenceContext().getPropertyValue(CONTENTSITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMapMediaCronJob.siteMapUrlLimitPerFile</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the siteMapUrlLimitPerFile
	 */
	@Accessor(qualifier = "siteMapUrlLimitPerFile", type = Accessor.Type.GETTER)
	public Integer getSiteMapUrlLimitPerFile()
	{
		return getPersistenceContext().getPropertyValue(SITEMAPURLLIMITPERFILE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMapMediaCronJob.contentSite</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the contentSite
	 */
	@Accessor(qualifier = "contentSite", type = Accessor.Type.SETTER)
	public void setContentSite(final CMSSiteModel value)
	{
		getPersistenceContext().setPropertyValue(CONTENTSITE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMapMediaCronJob.siteMapUrlLimitPerFile</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the siteMapUrlLimitPerFile
	 */
	@Accessor(qualifier = "siteMapUrlLimitPerFile", type = Accessor.Type.SETTER)
	public void setSiteMapUrlLimitPerFile(final Integer value)
	{
		getPersistenceContext().setPropertyValue(SITEMAPURLLIMITPERFILE, value);
	}
	
}
