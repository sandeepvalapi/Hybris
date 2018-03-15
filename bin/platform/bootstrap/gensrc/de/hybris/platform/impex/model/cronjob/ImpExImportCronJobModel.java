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
package de.hybris.platform.impex.model.cronjob;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.enums.EncodingEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.impex.enums.ImpExValidationModeEnum;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type ImpExImportCronJob first defined at extension impex.
 */
@SuppressWarnings("all")
public class ImpExImportCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ImpExImportCronJob";
	
	/**<i>Generated relation code constant for relation <code>JobCronJobRelation</code> defining source attribute <code>job</code> in extension <code>processing</code>.</i>*/
	public static final String _JOBCRONJOBRELATION = "JobCronJobRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.jobMedia</code> attribute defined at extension <code>impex</code>. */
	public static final String JOBMEDIA = "jobMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.workMedia</code> attribute defined at extension <code>impex</code>. */
	public static final String WORKMEDIA = "workMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.lastSuccessfulLine</code> attribute defined at extension <code>impex</code>. */
	public static final String LASTSUCCESSFULLINE = "lastSuccessfulLine";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.mediasMedia</code> attribute defined at extension <code>impex</code>. */
	public static final String MEDIASMEDIA = "mediasMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.externalDataCollection</code> attribute defined at extension <code>impex</code>. */
	public static final String EXTERNALDATACOLLECTION = "externalDataCollection";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.locale</code> attribute defined at extension <code>impex</code>. */
	public static final String LOCALE = "locale";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.dumpFileEncoding</code> attribute defined at extension <code>impex</code>. */
	public static final String DUMPFILEENCODING = "dumpFileEncoding";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.enableCodeExecution</code> attribute defined at extension <code>impex</code>. */
	public static final String ENABLECODEEXECUTION = "enableCodeExecution";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.enableExternalCodeExecution</code> attribute defined at extension <code>impex</code>. */
	public static final String ENABLEEXTERNALCODEEXECUTION = "enableExternalCodeExecution";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.enableExternalSyntaxParsing</code> attribute defined at extension <code>impex</code>. */
	public static final String ENABLEEXTERNALSYNTAXPARSING = "enableExternalSyntaxParsing";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.enableHmcSavedValues</code> attribute defined at extension <code>impex</code>. */
	public static final String ENABLEHMCSAVEDVALUES = "enableHmcSavedValues";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.mediasTarget</code> attribute defined at extension <code>impex</code>. */
	public static final String MEDIASTARGET = "mediasTarget";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.valueCount</code> attribute defined at extension <code>impex</code>. */
	public static final String VALUECOUNT = "valueCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.unresolvedDataStore</code> attribute defined at extension <code>impex</code>. */
	public static final String UNRESOLVEDDATASTORE = "unresolvedDataStore";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.zipentry</code> attribute defined at extension <code>impex</code>. */
	public static final String ZIPENTRY = "zipentry";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.mode</code> attribute defined at extension <code>impex</code>. */
	public static final String MODE = "mode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.dumpingAllowed</code> attribute defined at extension <code>impex</code>. */
	public static final String DUMPINGALLOWED = "dumpingAllowed";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.unzipMediasMedia</code> attribute defined at extension <code>impex</code>. */
	public static final String UNZIPMEDIASMEDIA = "unzipMediasMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.maxThreads</code> attribute defined at extension <code>impex</code>. */
	public static final String MAXTHREADS = "maxThreads";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.legacyMode</code> attribute defined at extension <code>impex</code>. */
	public static final String LEGACYMODE = "legacyMode";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ImpExImportCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ImpExImportCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>ImpExImportCronJob</code> at extension <code>impex</code>
	 */
	@Deprecated
	public ImpExImportCronJobModel(final ImpExImportJobModel _job)
	{
		super();
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>ImpExImportCronJob</code> at extension <code>impex</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ImpExImportCronJobModel(final ImpExImportJobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.dumpFileEncoding</code> attribute defined at extension <code>impex</code>. 
	 * @return the dumpFileEncoding
	 */
	@Accessor(qualifier = "dumpFileEncoding", type = Accessor.Type.GETTER)
	public EncodingEnum getDumpFileEncoding()
	{
		return getPersistenceContext().getPropertyValue(DUMPFILEENCODING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.dumpingAllowed</code> attribute defined at extension <code>impex</code>. 
	 * @return the dumpingAllowed
	 */
	@Accessor(qualifier = "dumpingAllowed", type = Accessor.Type.GETTER)
	public Boolean getDumpingAllowed()
	{
		return getPersistenceContext().getPropertyValue(DUMPINGALLOWED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.enableCodeExecution</code> attribute defined at extension <code>impex</code>. 
	 * @return the enableCodeExecution
	 */
	@Accessor(qualifier = "enableCodeExecution", type = Accessor.Type.GETTER)
	public Boolean getEnableCodeExecution()
	{
		return getPersistenceContext().getPropertyValue(ENABLECODEEXECUTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.enableExternalCodeExecution</code> attribute defined at extension <code>impex</code>. 
	 * @return the enableExternalCodeExecution
	 */
	@Accessor(qualifier = "enableExternalCodeExecution", type = Accessor.Type.GETTER)
	public Boolean getEnableExternalCodeExecution()
	{
		return getPersistenceContext().getPropertyValue(ENABLEEXTERNALCODEEXECUTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.enableExternalSyntaxParsing</code> attribute defined at extension <code>impex</code>. 
	 * @return the enableExternalSyntaxParsing
	 */
	@Accessor(qualifier = "enableExternalSyntaxParsing", type = Accessor.Type.GETTER)
	public Boolean getEnableExternalSyntaxParsing()
	{
		return getPersistenceContext().getPropertyValue(ENABLEEXTERNALSYNTAXPARSING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.enableHmcSavedValues</code> attribute defined at extension <code>impex</code>. 
	 * @return the enableHmcSavedValues
	 */
	@Accessor(qualifier = "enableHmcSavedValues", type = Accessor.Type.GETTER)
	public Boolean getEnableHmcSavedValues()
	{
		return getPersistenceContext().getPropertyValue(ENABLEHMCSAVEDVALUES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.externalDataCollection</code> attribute defined at extension <code>impex</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the externalDataCollection
	 */
	@Accessor(qualifier = "externalDataCollection", type = Accessor.Type.GETTER)
	public Collection<ImpExMediaModel> getExternalDataCollection()
	{
		return getPersistenceContext().getPropertyValue(EXTERNALDATACOLLECTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.job</code> attribute defined at extension <code>processing</code> and redeclared at extension <code>impex</code>. 
	 * @return the job
	 */
	@Override
	@Accessor(qualifier = "job", type = Accessor.Type.GETTER)
	public ImpExImportJobModel getJob()
	{
		return (ImpExImportJobModel) super.getJob();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.jobMedia</code> attribute defined at extension <code>impex</code>. 
	 * @return the jobMedia
	 */
	@Accessor(qualifier = "jobMedia", type = Accessor.Type.GETTER)
	public ImpExMediaModel getJobMedia()
	{
		return getPersistenceContext().getPropertyValue(JOBMEDIA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.lastSuccessfulLine</code> attribute defined at extension <code>impex</code>. 
	 * @return the lastSuccessfulLine
	 */
	@Accessor(qualifier = "lastSuccessfulLine", type = Accessor.Type.GETTER)
	public Integer getLastSuccessfulLine()
	{
		return getPersistenceContext().getPropertyValue(LASTSUCCESSFULLINE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.legacyMode</code> attribute defined at extension <code>impex</code>. 
	 * @return the legacyMode
	 */
	@Accessor(qualifier = "legacyMode", type = Accessor.Type.GETTER)
	public Boolean getLegacyMode()
	{
		return getPersistenceContext().getPropertyValue(LEGACYMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.locale</code> attribute defined at extension <code>impex</code>. 
	 * @return the locale
	 */
	@Accessor(qualifier = "locale", type = Accessor.Type.GETTER)
	public String getLocale()
	{
		return getPersistenceContext().getPropertyValue(LOCALE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.maxThreads</code> attribute defined at extension <code>impex</code>. 
	 * @return the maxThreads
	 */
	@Accessor(qualifier = "maxThreads", type = Accessor.Type.GETTER)
	public Integer getMaxThreads()
	{
		return getPersistenceContext().getPropertyValue(MAXTHREADS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.mediasMedia</code> attribute defined at extension <code>impex</code>. 
	 * @return the mediasMedia
	 */
	@Accessor(qualifier = "mediasMedia", type = Accessor.Type.GETTER)
	public MediaModel getMediasMedia()
	{
		return getPersistenceContext().getPropertyValue(MEDIASMEDIA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.mediasTarget</code> attribute defined at extension <code>impex</code>. 
	 * @return the mediasTarget
	 */
	@Accessor(qualifier = "mediasTarget", type = Accessor.Type.GETTER)
	public String getMediasTarget()
	{
		return getPersistenceContext().getPropertyValue(MEDIASTARGET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.mode</code> attribute defined at extension <code>impex</code>. 
	 * @return the mode
	 */
	@Accessor(qualifier = "mode", type = Accessor.Type.GETTER)
	public ImpExValidationModeEnum getMode()
	{
		return getPersistenceContext().getPropertyValue(MODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.unresolvedDataStore</code> attribute defined at extension <code>impex</code>. 
	 * @return the unresolvedDataStore
	 */
	@Accessor(qualifier = "unresolvedDataStore", type = Accessor.Type.GETTER)
	public ImpExMediaModel getUnresolvedDataStore()
	{
		return getPersistenceContext().getPropertyValue(UNRESOLVEDDATASTORE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.unzipMediasMedia</code> attribute defined at extension <code>impex</code>. 
	 * @return the unzipMediasMedia
	 */
	@Accessor(qualifier = "unzipMediasMedia", type = Accessor.Type.GETTER)
	public Boolean getUnzipMediasMedia()
	{
		return getPersistenceContext().getPropertyValue(UNZIPMEDIASMEDIA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.valueCount</code> attribute defined at extension <code>impex</code>. 
	 * @return the valueCount
	 */
	@Accessor(qualifier = "valueCount", type = Accessor.Type.GETTER)
	public Integer getValueCount()
	{
		return getPersistenceContext().getPropertyValue(VALUECOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.workMedia</code> attribute defined at extension <code>impex</code>. 
	 * @return the workMedia
	 */
	@Accessor(qualifier = "workMedia", type = Accessor.Type.GETTER)
	public ImpExMediaModel getWorkMedia()
	{
		return getPersistenceContext().getPropertyValue(WORKMEDIA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.zipentry</code> attribute defined at extension <code>impex</code>. 
	 * @return the zipentry
	 */
	@Accessor(qualifier = "zipentry", type = Accessor.Type.GETTER)
	public String getZipentry()
	{
		return getPersistenceContext().getPropertyValue(ZIPENTRY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.dumpFileEncoding</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the dumpFileEncoding
	 */
	@Accessor(qualifier = "dumpFileEncoding", type = Accessor.Type.SETTER)
	public void setDumpFileEncoding(final EncodingEnum value)
	{
		getPersistenceContext().setPropertyValue(DUMPFILEENCODING, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.dumpingAllowed</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the dumpingAllowed
	 */
	@Accessor(qualifier = "dumpingAllowed", type = Accessor.Type.SETTER)
	public void setDumpingAllowed(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(DUMPINGALLOWED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.enableCodeExecution</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the enableCodeExecution
	 */
	@Accessor(qualifier = "enableCodeExecution", type = Accessor.Type.SETTER)
	public void setEnableCodeExecution(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ENABLECODEEXECUTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.enableExternalCodeExecution</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the enableExternalCodeExecution
	 */
	@Accessor(qualifier = "enableExternalCodeExecution", type = Accessor.Type.SETTER)
	public void setEnableExternalCodeExecution(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ENABLEEXTERNALCODEEXECUTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.enableExternalSyntaxParsing</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the enableExternalSyntaxParsing
	 */
	@Accessor(qualifier = "enableExternalSyntaxParsing", type = Accessor.Type.SETTER)
	public void setEnableExternalSyntaxParsing(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ENABLEEXTERNALSYNTAXPARSING, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.enableHmcSavedValues</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the enableHmcSavedValues
	 */
	@Accessor(qualifier = "enableHmcSavedValues", type = Accessor.Type.SETTER)
	public void setEnableHmcSavedValues(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ENABLEHMCSAVEDVALUES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.externalDataCollection</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the externalDataCollection
	 */
	@Accessor(qualifier = "externalDataCollection", type = Accessor.Type.SETTER)
	public void setExternalDataCollection(final Collection<ImpExMediaModel> value)
	{
		getPersistenceContext().setPropertyValue(EXTERNALDATACOLLECTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CronJob.job</code> attribute defined at extension <code>processing</code> and redeclared at extension <code>impex</code>. Can only be used at creation of model - before first save. Will only accept values of type {@link de.hybris.platform.impex.model.cronjob.ImpExImportJobModel}.  
	 *  
	 * @param value the job
	 */
	@Override
	@Accessor(qualifier = "job", type = Accessor.Type.SETTER)
	public void setJob(final JobModel value)
	{
		if( value == null || value instanceof ImpExImportJobModel)
		{
			super.setJob(value);
		}
		else
		{
			throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.impex.model.cronjob.ImpExImportJobModel");
		}
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.jobMedia</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the jobMedia
	 */
	@Accessor(qualifier = "jobMedia", type = Accessor.Type.SETTER)
	public void setJobMedia(final ImpExMediaModel value)
	{
		getPersistenceContext().setPropertyValue(JOBMEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.lastSuccessfulLine</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the lastSuccessfulLine
	 */
	@Accessor(qualifier = "lastSuccessfulLine", type = Accessor.Type.SETTER)
	public void setLastSuccessfulLine(final Integer value)
	{
		getPersistenceContext().setPropertyValue(LASTSUCCESSFULLINE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.legacyMode</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the legacyMode
	 */
	@Accessor(qualifier = "legacyMode", type = Accessor.Type.SETTER)
	public void setLegacyMode(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(LEGACYMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.locale</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the locale
	 */
	@Accessor(qualifier = "locale", type = Accessor.Type.SETTER)
	public void setLocale(final String value)
	{
		getPersistenceContext().setPropertyValue(LOCALE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.maxThreads</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the maxThreads
	 */
	@Accessor(qualifier = "maxThreads", type = Accessor.Type.SETTER)
	public void setMaxThreads(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MAXTHREADS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.mediasMedia</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the mediasMedia
	 */
	@Accessor(qualifier = "mediasMedia", type = Accessor.Type.SETTER)
	public void setMediasMedia(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(MEDIASMEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.mediasTarget</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the mediasTarget
	 */
	@Accessor(qualifier = "mediasTarget", type = Accessor.Type.SETTER)
	public void setMediasTarget(final String value)
	{
		getPersistenceContext().setPropertyValue(MEDIASTARGET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.mode</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the mode
	 */
	@Accessor(qualifier = "mode", type = Accessor.Type.SETTER)
	public void setMode(final ImpExValidationModeEnum value)
	{
		getPersistenceContext().setPropertyValue(MODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.unresolvedDataStore</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the unresolvedDataStore
	 */
	@Accessor(qualifier = "unresolvedDataStore", type = Accessor.Type.SETTER)
	public void setUnresolvedDataStore(final ImpExMediaModel value)
	{
		getPersistenceContext().setPropertyValue(UNRESOLVEDDATASTORE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.unzipMediasMedia</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the unzipMediasMedia
	 */
	@Accessor(qualifier = "unzipMediasMedia", type = Accessor.Type.SETTER)
	public void setUnzipMediasMedia(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(UNZIPMEDIASMEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.valueCount</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the valueCount
	 */
	@Accessor(qualifier = "valueCount", type = Accessor.Type.SETTER)
	public void setValueCount(final Integer value)
	{
		getPersistenceContext().setPropertyValue(VALUECOUNT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.workMedia</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the workMedia
	 */
	@Accessor(qualifier = "workMedia", type = Accessor.Type.SETTER)
	public void setWorkMedia(final ImpExMediaModel value)
	{
		getPersistenceContext().setPropertyValue(WORKMEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.zipentry</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the zipentry
	 */
	@Accessor(qualifier = "zipentry", type = Accessor.Type.SETTER)
	public void setZipentry(final String value)
	{
		getPersistenceContext().setPropertyValue(ZIPENTRY, value);
	}
	
}
