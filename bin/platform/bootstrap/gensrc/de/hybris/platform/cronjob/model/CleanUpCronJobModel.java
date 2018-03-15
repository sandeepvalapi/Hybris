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
package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;

/**
 * Generated model class for type CleanUpCronJob first defined at extension processing.
 */
@SuppressWarnings("all")
public class CleanUpCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CleanUpCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>CleanUpCronJob.xDaysOld</code> attribute defined at extension <code>processing</code>. */
	public static final String XDAYSOLD = "xDaysOld";
	
	/** <i>Generated constant</i> - Attribute key of <code>CleanUpCronJob.excludeCronJobs</code> attribute defined at extension <code>processing</code>. */
	public static final String EXCLUDECRONJOBS = "excludeCronJobs";
	
	/** <i>Generated constant</i> - Attribute key of <code>CleanUpCronJob.resultcoll</code> attribute defined at extension <code>processing</code>. */
	public static final String RESULTCOLL = "resultcoll";
	
	/** <i>Generated constant</i> - Attribute key of <code>CleanUpCronJob.statuscoll</code> attribute defined at extension <code>processing</code>. */
	public static final String STATUSCOLL = "statuscoll";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CleanUpCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CleanUpCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CleanUpCronJobModel(final JobModel _job)
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
	public CleanUpCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CleanUpCronJob.excludeCronJobs</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the excludeCronJobs - A List of CronJobs which should never be deleted by this clean up job
	 */
	@Accessor(qualifier = "excludeCronJobs", type = Accessor.Type.GETTER)
	public List<CronJobModel> getExcludeCronJobs()
	{
		return getPersistenceContext().getPropertyValue(EXCLUDECRONJOBS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CleanUpCronJob.resultcoll</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the resultcoll - Filter: only cronjobs with this Result will be deleted
	 */
	@Accessor(qualifier = "resultcoll", type = Accessor.Type.GETTER)
	public Collection<CronJobResult> getResultcoll()
	{
		return getPersistenceContext().getPropertyValue(RESULTCOLL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CleanUpCronJob.statuscoll</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the statuscoll - Filter: only cronjobs with this status will be deleted
	 */
	@Accessor(qualifier = "statuscoll", type = Accessor.Type.GETTER)
	public Collection<CronJobStatus> getStatuscoll()
	{
		return getPersistenceContext().getPropertyValue(STATUSCOLL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CleanUpCronJob.xDaysOld</code> attribute defined at extension <code>processing</code>. 
	 * @return the xDaysOld - All CronJobs (CronJobModels) older than this value in days will be removed
	 */
	@Accessor(qualifier = "xDaysOld", type = Accessor.Type.GETTER)
	public int getXDaysOld()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(XDAYSOLD));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CleanUpCronJob.excludeCronJobs</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the excludeCronJobs - A List of CronJobs which should never be deleted by this clean up job
	 */
	@Accessor(qualifier = "excludeCronJobs", type = Accessor.Type.SETTER)
	public void setExcludeCronJobs(final List<CronJobModel> value)
	{
		getPersistenceContext().setPropertyValue(EXCLUDECRONJOBS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CleanUpCronJob.resultcoll</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the resultcoll - Filter: only cronjobs with this Result will be deleted
	 */
	@Accessor(qualifier = "resultcoll", type = Accessor.Type.SETTER)
	public void setResultcoll(final Collection<CronJobResult> value)
	{
		getPersistenceContext().setPropertyValue(RESULTCOLL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CleanUpCronJob.statuscoll</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the statuscoll - Filter: only cronjobs with this status will be deleted
	 */
	@Accessor(qualifier = "statuscoll", type = Accessor.Type.SETTER)
	public void setStatuscoll(final Collection<CronJobStatus> value)
	{
		getPersistenceContext().setPropertyValue(STATUSCOLL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CleanUpCronJob.xDaysOld</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the xDaysOld - All CronJobs (CronJobModels) older than this value in days will be removed
	 */
	@Accessor(qualifier = "xDaysOld", type = Accessor.Type.SETTER)
	public void setXDaysOld(final int value)
	{
		getPersistenceContext().setPropertyValue(XDAYSOLD, toObject(value));
	}
	
}
