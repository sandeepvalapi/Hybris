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
package de.hybris.platform.personalizationservices.model.process;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type CxPersonalizationProcessCleanupCronJob first defined at extension personalizationservices.
 */
@SuppressWarnings("all")
public class CxPersonalizationProcessCleanupCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CxPersonalizationProcessCleanupCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxPersonalizationProcessCleanupCronJob.processStates</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String PROCESSSTATES = "processStates";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxPersonalizationProcessCleanupCronJob.maxProcessAge</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String MAXPROCESSAGE = "maxProcessAge";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CxPersonalizationProcessCleanupCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CxPersonalizationProcessCleanupCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _maxProcessAge initial attribute declared by type <code>CxPersonalizationProcessCleanupCronJob</code> at extension <code>personalizationservices</code>
	 * @param _processStates initial attribute declared by type <code>CxPersonalizationProcessCleanupCronJob</code> at extension <code>personalizationservices</code>
	 */
	@Deprecated
	public CxPersonalizationProcessCleanupCronJobModel(final JobModel _job, final String _maxProcessAge, final Collection<ProcessState> _processStates)
	{
		super();
		setJob(_job);
		setMaxProcessAge(_maxProcessAge);
		setProcessStates(_processStates);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _maxProcessAge initial attribute declared by type <code>CxPersonalizationProcessCleanupCronJob</code> at extension <code>personalizationservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _processStates initial attribute declared by type <code>CxPersonalizationProcessCleanupCronJob</code> at extension <code>personalizationservices</code>
	 */
	@Deprecated
	public CxPersonalizationProcessCleanupCronJobModel(final JobModel _job, final String _maxProcessAge, final ItemModel _owner, final Collection<ProcessState> _processStates)
	{
		super();
		setJob(_job);
		setMaxProcessAge(_maxProcessAge);
		setOwner(_owner);
		setProcessStates(_processStates);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxPersonalizationProcessCleanupCronJob.maxProcessAge</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the maxProcessAge - After this time (counting from process creation date), CxPersonalizationProcesses with processStates will be deleted
	 */
	@Accessor(qualifier = "maxProcessAge", type = Accessor.Type.GETTER)
	public String getMaxProcessAge()
	{
		return getPersistenceContext().getPropertyValue(MAXPROCESSAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxPersonalizationProcessCleanupCronJob.processStates</code> attribute defined at extension <code>personalizationservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the processStates - Filter: Process states that will be taken into account during cleanup process
	 */
	@Accessor(qualifier = "processStates", type = Accessor.Type.GETTER)
	public Collection<ProcessState> getProcessStates()
	{
		return getPersistenceContext().getPropertyValue(PROCESSSTATES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxPersonalizationProcessCleanupCronJob.maxProcessAge</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the maxProcessAge - After this time (counting from process creation date), CxPersonalizationProcesses with processStates will be deleted
	 */
	@Accessor(qualifier = "maxProcessAge", type = Accessor.Type.SETTER)
	public void setMaxProcessAge(final String value)
	{
		getPersistenceContext().setPropertyValue(MAXPROCESSAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxPersonalizationProcessCleanupCronJob.processStates</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the processStates - Filter: Process states that will be taken into account during cleanup process
	 */
	@Accessor(qualifier = "processStates", type = Accessor.Type.SETTER)
	public void setProcessStates(final Collection<ProcessState> value)
	{
		getPersistenceContext().setPropertyValue(PROCESSSTATES, value);
	}
	
}
