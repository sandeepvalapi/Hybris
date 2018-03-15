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
package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type SyncItemCronJob first defined at extension catalog.
 */
@SuppressWarnings("all")
public class SyncItemCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SyncItemCronJob";
	
	/**<i>Generated relation code constant for relation <code>JobCronJobRelation</code> defining source attribute <code>job</code> in extension <code>processing</code>.</i>*/
	public static final String _JOBCRONJOBRELATION = "JobCronJobRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemCronJob.forceUpdate</code> attribute defined at extension <code>catalog</code>. */
	public static final String FORCEUPDATE = "forceUpdate";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemCronJob.pendingItems</code> attribute defined at extension <code>catalog</code>. */
	public static final String PENDINGITEMS = "pendingItems";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemCronJob.finishedItems</code> attribute defined at extension <code>catalog</code>. */
	public static final String FINISHEDITEMS = "finishedItems";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemCronJob.createSavedValues</code> attribute defined at extension <code>catalog</code>. */
	public static final String CREATESAVEDVALUES = "createSavedValues";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemCronJob.fullSync</code> attribute defined at extension <code>catalog</code>. */
	public static final String FULLSYNC = "fullSync";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemCronJob.abortOnCollidingSync</code> attribute defined at extension <code>catalog</code>. */
	public static final String ABORTONCOLLIDINGSYNC = "abortOnCollidingSync";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SyncItemCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SyncItemCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>SyncItemCronJob</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public SyncItemCronJobModel(final SyncItemJobModel _job)
	{
		super();
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>SyncItemCronJob</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public SyncItemCronJobModel(final SyncItemJobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemCronJob.abortOnCollidingSync</code> attribute defined at extension <code>catalog</code>. 
	 * @return the abortOnCollidingSync
	 */
	@Accessor(qualifier = "abortOnCollidingSync", type = Accessor.Type.GETTER)
	public Boolean getAbortOnCollidingSync()
	{
		return getPersistenceContext().getPropertyValue(ABORTONCOLLIDINGSYNC);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemCronJob.createSavedValues</code> attribute defined at extension <code>catalog</code>. 
	 * @return the createSavedValues
	 */
	@Accessor(qualifier = "createSavedValues", type = Accessor.Type.GETTER)
	public Boolean getCreateSavedValues()
	{
		return getPersistenceContext().getPropertyValue(CREATESAVEDVALUES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemCronJob.finishedItems</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the finishedItems
	 */
	@Accessor(qualifier = "finishedItems", type = Accessor.Type.GETTER)
	public Collection<ItemModel> getFinishedItems()
	{
		return getPersistenceContext().getPropertyValue(FINISHEDITEMS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemCronJob.forceUpdate</code> attribute defined at extension <code>catalog</code>. 
	 * @return the forceUpdate
	 */
	@Accessor(qualifier = "forceUpdate", type = Accessor.Type.GETTER)
	public Boolean getForceUpdate()
	{
		return getPersistenceContext().getPropertyValue(FORCEUPDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemCronJob.fullSync</code> attribute defined at extension <code>catalog</code>. 
	 * @return the fullSync
	 */
	@Accessor(qualifier = "fullSync", type = Accessor.Type.GETTER)
	public Boolean getFullSync()
	{
		return getPersistenceContext().getPropertyValue(FULLSYNC);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.job</code> attribute defined at extension <code>processing</code> and redeclared at extension <code>catalog</code>. 
	 * @return the job
	 */
	@Override
	@Accessor(qualifier = "job", type = Accessor.Type.GETTER)
	public SyncItemJobModel getJob()
	{
		return (SyncItemJobModel) super.getJob();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemCronJob.pendingItems</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the pendingItems
	 */
	@Accessor(qualifier = "pendingItems", type = Accessor.Type.GETTER)
	public Collection<ItemModel> getPendingItems()
	{
		return getPersistenceContext().getPropertyValue(PENDINGITEMS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemCronJob.abortOnCollidingSync</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the abortOnCollidingSync
	 */
	@Accessor(qualifier = "abortOnCollidingSync", type = Accessor.Type.SETTER)
	public void setAbortOnCollidingSync(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ABORTONCOLLIDINGSYNC, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemCronJob.createSavedValues</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the createSavedValues
	 */
	@Accessor(qualifier = "createSavedValues", type = Accessor.Type.SETTER)
	public void setCreateSavedValues(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(CREATESAVEDVALUES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemCronJob.forceUpdate</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the forceUpdate
	 */
	@Accessor(qualifier = "forceUpdate", type = Accessor.Type.SETTER)
	public void setForceUpdate(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(FORCEUPDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemCronJob.fullSync</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the fullSync
	 */
	@Accessor(qualifier = "fullSync", type = Accessor.Type.SETTER)
	public void setFullSync(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(FULLSYNC, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CronJob.job</code> attribute defined at extension <code>processing</code> and redeclared at extension <code>catalog</code>. Can only be used at creation of model - before first save. Will only accept values of type {@link de.hybris.platform.catalog.model.SyncItemJobModel}.  
	 *  
	 * @param value the job
	 */
	@Override
	@Accessor(qualifier = "job", type = Accessor.Type.SETTER)
	public void setJob(final JobModel value)
	{
		if( value == null || value instanceof SyncItemJobModel)
		{
			super.setJob(value);
		}
		else
		{
			throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.catalog.model.SyncItemJobModel");
		}
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemCronJob.pendingItems</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the pendingItems
	 */
	@Accessor(qualifier = "pendingItems", type = Accessor.Type.SETTER)
	public void setPendingItems(final Collection<ItemModel> value)
	{
		getPersistenceContext().setPropertyValue(PENDINGITEMS, value);
	}
	
}
