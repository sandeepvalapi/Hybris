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
package de.hybris.platform.acceleratorservices.model.export;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorservices.model.export.ExportDataHistoryEntryModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import java.util.List;

/**
 * Generated model class for type ExportDataCronJob first defined at extension acceleratorservices.
 * <p>
 * CronJob holding information to export to a third party.
 */
@SuppressWarnings("all")
public class ExportDataCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ExportDataCronJob";
	
	/**<i>Generated relation code constant for relation <code>JobCronJobRelation</code> defining source attribute <code>job</code> in extension <code>processing</code>.</i>*/
	public static final String _JOBCRONJOBRELATION = "JobCronJobRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExportDataCronJob.baseStore</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String BASESTORE = "baseStore";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExportDataCronJob.cmsSite</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String CMSSITE = "cmsSite";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExportDataCronJob.language</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String LANGUAGE = "language";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExportDataCronJob.currency</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String CURRENCY = "currency";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExportDataCronJob.user</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String USER = "user";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExportDataCronJob.thirdPartyHost</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String THIRDPARTYHOST = "thirdPartyHost";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExportDataCronJob.thirdPartyUsername</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String THIRDPARTYUSERNAME = "thirdPartyUsername";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExportDataCronJob.thirdPartyPassword</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String THIRDPARTYPASSWORD = "thirdPartyPassword";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExportDataCronJob.dataGenerationPipeline</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String DATAGENERATIONPIPELINE = "dataGenerationPipeline";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExportDataCronJob.historyEntries</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String HISTORYENTRIES = "historyEntries";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ExportDataCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ExportDataCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _dataGenerationPipeline initial attribute declared by type <code>ExportDataCronJob</code> at extension <code>acceleratorservices</code>
	 * @param _job initial attribute declared by type <code>ExportDataCronJob</code> at extension <code>acceleratorservices</code>
	 */
	@Deprecated
	public ExportDataCronJobModel(final String _dataGenerationPipeline, final ServicelayerJobModel _job)
	{
		super();
		setDataGenerationPipeline(_dataGenerationPipeline);
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _dataGenerationPipeline initial attribute declared by type <code>ExportDataCronJob</code> at extension <code>acceleratorservices</code>
	 * @param _job initial attribute declared by type <code>ExportDataCronJob</code> at extension <code>acceleratorservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ExportDataCronJobModel(final String _dataGenerationPipeline, final ServicelayerJobModel _job, final ItemModel _owner)
	{
		super();
		setDataGenerationPipeline(_dataGenerationPipeline);
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportDataCronJob.baseStore</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the baseStore - The base store to run against
	 */
	@Accessor(qualifier = "baseStore", type = Accessor.Type.GETTER)
	public BaseStoreModel getBaseStore()
	{
		return getPersistenceContext().getPropertyValue(BASESTORE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportDataCronJob.cmsSite</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the cmsSite - The cms site to run against
	 */
	@Accessor(qualifier = "cmsSite", type = Accessor.Type.GETTER)
	public CMSSiteModel getCmsSite()
	{
		return getPersistenceContext().getPropertyValue(CMSSITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportDataCronJob.currency</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the currency - The currency context
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
	public CurrencyModel getCurrency()
	{
		return getPersistenceContext().getPropertyValue(CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportDataCronJob.dataGenerationPipeline</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the dataGenerationPipeline - The name of the pipeline that generates the data
	 */
	@Accessor(qualifier = "dataGenerationPipeline", type = Accessor.Type.GETTER)
	public String getDataGenerationPipeline()
	{
		return getPersistenceContext().getPropertyValue(DATAGENERATIONPIPELINE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportDataCronJob.historyEntries</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the historyEntries
	 */
	@Accessor(qualifier = "historyEntries", type = Accessor.Type.GETTER)
	public List<ExportDataHistoryEntryModel> getHistoryEntries()
	{
		return getPersistenceContext().getPropertyValue(HISTORYENTRIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.job</code> attribute defined at extension <code>processing</code> and redeclared at extension <code>acceleratorservices</code>. 
	 * @return the job - Redeclare job type as ServicelayerJob
	 */
	@Override
	@Accessor(qualifier = "job", type = Accessor.Type.GETTER)
	public ServicelayerJobModel getJob()
	{
		return (ServicelayerJobModel) super.getJob();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportDataCronJob.language</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the language - The language context
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public LanguageModel getLanguage()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportDataCronJob.thirdPartyHost</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the thirdPartyHost - The location of the third party account
	 */
	@Accessor(qualifier = "thirdPartyHost", type = Accessor.Type.GETTER)
	public String getThirdPartyHost()
	{
		return getPersistenceContext().getPropertyValue(THIRDPARTYHOST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportDataCronJob.thirdPartyPassword</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the thirdPartyPassword - The password of the third party account
	 */
	@Accessor(qualifier = "thirdPartyPassword", type = Accessor.Type.GETTER)
	public String getThirdPartyPassword()
	{
		return getPersistenceContext().getPropertyValue(THIRDPARTYPASSWORD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportDataCronJob.thirdPartyUsername</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the thirdPartyUsername - The username of the third party account
	 */
	@Accessor(qualifier = "thirdPartyUsername", type = Accessor.Type.GETTER)
	public String getThirdPartyUsername()
	{
		return getPersistenceContext().getPropertyValue(THIRDPARTYUSERNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportDataCronJob.user</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the user - The user to set the export context to
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExportDataCronJob.baseStore</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the baseStore - The base store to run against
	 */
	@Accessor(qualifier = "baseStore", type = Accessor.Type.SETTER)
	public void setBaseStore(final BaseStoreModel value)
	{
		getPersistenceContext().setPropertyValue(BASESTORE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExportDataCronJob.cmsSite</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the cmsSite - The cms site to run against
	 */
	@Accessor(qualifier = "cmsSite", type = Accessor.Type.SETTER)
	public void setCmsSite(final CMSSiteModel value)
	{
		getPersistenceContext().setPropertyValue(CMSSITE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExportDataCronJob.currency</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the currency - The currency context
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
	public void setCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(CURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ExportDataCronJob.dataGenerationPipeline</code> attribute defined at extension <code>acceleratorservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the dataGenerationPipeline - The name of the pipeline that generates the data
	 */
	@Accessor(qualifier = "dataGenerationPipeline", type = Accessor.Type.SETTER)
	public void setDataGenerationPipeline(final String value)
	{
		getPersistenceContext().setPropertyValue(DATAGENERATIONPIPELINE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExportDataCronJob.historyEntries</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the historyEntries
	 */
	@Accessor(qualifier = "historyEntries", type = Accessor.Type.SETTER)
	public void setHistoryEntries(final List<ExportDataHistoryEntryModel> value)
	{
		getPersistenceContext().setPropertyValue(HISTORYENTRIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CronJob.job</code> attribute defined at extension <code>processing</code> and redeclared at extension <code>acceleratorservices</code>. Can only be used at creation of model - before first save. Will only accept values of type {@link de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel}.  
	 *  
	 * @param value the job - Redeclare job type as ServicelayerJob
	 */
	@Override
	@Accessor(qualifier = "job", type = Accessor.Type.SETTER)
	public void setJob(final JobModel value)
	{
		if( value == null || value instanceof ServicelayerJobModel)
		{
			super.setJob(value);
		}
		else
		{
			throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel");
		}
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExportDataCronJob.language</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the language - The language context
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExportDataCronJob.thirdPartyHost</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the thirdPartyHost - The location of the third party account
	 */
	@Accessor(qualifier = "thirdPartyHost", type = Accessor.Type.SETTER)
	public void setThirdPartyHost(final String value)
	{
		getPersistenceContext().setPropertyValue(THIRDPARTYHOST, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExportDataCronJob.thirdPartyPassword</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the thirdPartyPassword - The password of the third party account
	 */
	@Accessor(qualifier = "thirdPartyPassword", type = Accessor.Type.SETTER)
	public void setThirdPartyPassword(final String value)
	{
		getPersistenceContext().setPropertyValue(THIRDPARTYPASSWORD, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExportDataCronJob.thirdPartyUsername</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the thirdPartyUsername - The username of the third party account
	 */
	@Accessor(qualifier = "thirdPartyUsername", type = Accessor.Type.SETTER)
	public void setThirdPartyUsername(final String value)
	{
		getPersistenceContext().setPropertyValue(THIRDPARTYUSERNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExportDataCronJob.user</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the user - The user to set the export context to
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
}
