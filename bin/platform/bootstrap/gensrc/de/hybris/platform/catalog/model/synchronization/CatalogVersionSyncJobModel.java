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
package de.hybris.platform.catalog.model.synchronization;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

/**
 * Generated model class for type CatalogVersionSyncJob first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CatalogVersionSyncJobModel extends SyncItemJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CatalogVersionSyncJob";
	
	/**<i>Generated relation code constant for relation <code>DependentCatalogVersionSyncJobRelation</code> defining source attribute <code>dependentSyncJobs</code> in extension <code>catalog</code>.</i>*/
	public static final String _DEPENDENTCATALOGVERSIONSYNCJOBRELATION = "DependentCatalogVersionSyncJobRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncJob.copyCacheSize</code> attribute defined at extension <code>catalog</code>. */
	public static final String COPYCACHESIZE = "copyCacheSize";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncJob.enableTransactions</code> attribute defined at extension <code>catalog</code>. */
	public static final String ENABLETRANSACTIONS = "enableTransactions";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncJob.maxThreads</code> attribute defined at extension <code>catalog</code>. */
	public static final String MAXTHREADS = "maxThreads";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncJob.maxSchedulerThreads</code> attribute defined at extension <code>catalog</code>. */
	public static final String MAXSCHEDULERTHREADS = "maxSchedulerThreads";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncJob.dependentSyncJobs</code> attribute defined at extension <code>catalog</code>. */
	public static final String DEPENDENTSYNCJOBS = "dependentSyncJobs";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncJob.dependsOnSyncJobs</code> attribute defined at extension <code>catalog</code>. */
	public static final String DEPENDSONSYNCJOBS = "dependsOnSyncJobs";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CatalogVersionSyncJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CatalogVersionSyncJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CatalogVersionSyncJobModel(final String _code, final CatalogVersionModel _sourceVersion, final CatalogVersionModel _targetVersion)
	{
		super();
		setCode(_code);
		setSourceVersion(_sourceVersion);
		setTargetVersion(_targetVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 * @param _nodeID initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sourceVersion initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CatalogVersionSyncJobModel(final String _code, final Integer _nodeID, final ItemModel _owner, final CatalogVersionModel _sourceVersion, final CatalogVersionModel _targetVersion)
	{
		super();
		setCode(_code);
		setNodeID(_nodeID);
		setOwner(_owner);
		setSourceVersion(_sourceVersion);
		setTargetVersion(_targetVersion);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncJob.copyCacheSize</code> attribute defined at extension <code>catalog</code>. 
	 * @return the copyCacheSize
	 */
	@Accessor(qualifier = "copyCacheSize", type = Accessor.Type.GETTER)
	public Integer getCopyCacheSize()
	{
		return getPersistenceContext().getPropertyValue(COPYCACHESIZE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncJob.dependentSyncJobs</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the dependentSyncJobs
	 */
	@Accessor(qualifier = "dependentSyncJobs", type = Accessor.Type.GETTER)
	public Set<CatalogVersionSyncJobModel> getDependentSyncJobs()
	{
		return getPersistenceContext().getPropertyValue(DEPENDENTSYNCJOBS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncJob.dependsOnSyncJobs</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the dependsOnSyncJobs
	 */
	@Accessor(qualifier = "dependsOnSyncJobs", type = Accessor.Type.GETTER)
	public Set<CatalogVersionSyncJobModel> getDependsOnSyncJobs()
	{
		return getPersistenceContext().getPropertyValue(DEPENDSONSYNCJOBS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncJob.enableTransactions</code> attribute defined at extension <code>catalog</code>. 
	 * @return the enableTransactions
	 */
	@Accessor(qualifier = "enableTransactions", type = Accessor.Type.GETTER)
	public Boolean getEnableTransactions()
	{
		return getPersistenceContext().getPropertyValue(ENABLETRANSACTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncJob.maxSchedulerThreads</code> attribute defined at extension <code>catalog</code>. 
	 * @return the maxSchedulerThreads
	 */
	@Accessor(qualifier = "maxSchedulerThreads", type = Accessor.Type.GETTER)
	public Integer getMaxSchedulerThreads()
	{
		return getPersistenceContext().getPropertyValue(MAXSCHEDULERTHREADS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncJob.maxThreads</code> attribute defined at extension <code>catalog</code>. 
	 * @return the maxThreads
	 */
	@Accessor(qualifier = "maxThreads", type = Accessor.Type.GETTER)
	public Integer getMaxThreads()
	{
		return getPersistenceContext().getPropertyValue(MAXTHREADS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncJob.copyCacheSize</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the copyCacheSize
	 */
	@Accessor(qualifier = "copyCacheSize", type = Accessor.Type.SETTER)
	public void setCopyCacheSize(final Integer value)
	{
		getPersistenceContext().setPropertyValue(COPYCACHESIZE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncJob.dependentSyncJobs</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the dependentSyncJobs
	 */
	@Accessor(qualifier = "dependentSyncJobs", type = Accessor.Type.SETTER)
	public void setDependentSyncJobs(final Set<CatalogVersionSyncJobModel> value)
	{
		getPersistenceContext().setPropertyValue(DEPENDENTSYNCJOBS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncJob.dependsOnSyncJobs</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the dependsOnSyncJobs
	 */
	@Accessor(qualifier = "dependsOnSyncJobs", type = Accessor.Type.SETTER)
	public void setDependsOnSyncJobs(final Set<CatalogVersionSyncJobModel> value)
	{
		getPersistenceContext().setPropertyValue(DEPENDSONSYNCJOBS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncJob.enableTransactions</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the enableTransactions
	 */
	@Accessor(qualifier = "enableTransactions", type = Accessor.Type.SETTER)
	public void setEnableTransactions(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ENABLETRANSACTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncJob.maxSchedulerThreads</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the maxSchedulerThreads
	 */
	@Accessor(qualifier = "maxSchedulerThreads", type = Accessor.Type.SETTER)
	public void setMaxSchedulerThreads(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MAXSCHEDULERTHREADS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncJob.maxThreads</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the maxThreads
	 */
	@Accessor(qualifier = "maxThreads", type = Accessor.Type.SETTER)
	public void setMaxThreads(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MAXTHREADS, value);
	}
	
}
