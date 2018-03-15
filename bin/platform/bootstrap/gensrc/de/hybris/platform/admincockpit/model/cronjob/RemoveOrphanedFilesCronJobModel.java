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
package de.hybris.platform.admincockpit.model.cronjob;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type RemoveOrphanedFilesCronJob first defined at extension admincockpit.
 */
@SuppressWarnings("all")
public class RemoveOrphanedFilesCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RemoveOrphanedFilesCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveOrphanedFilesCronJob.paging</code> attribute defined at extension <code>admincockpit</code>. */
	public static final String PAGING = "paging";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RemoveOrphanedFilesCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RemoveOrphanedFilesCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public RemoveOrphanedFilesCronJobModel(final JobModel _job)
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
	public RemoveOrphanedFilesCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveOrphanedFilesCronJob.paging</code> attribute defined at extension <code>admincockpit</code>. 
	 * @return the paging - Defines how many files will be checked during each 'analyzing round'.
	 */
	@Accessor(qualifier = "paging", type = Accessor.Type.GETTER)
	public Integer getPaging()
	{
		return getPersistenceContext().getPropertyValue(PAGING);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveOrphanedFilesCronJob.paging</code> attribute defined at extension <code>admincockpit</code>. 
	 *  
	 * @param value the paging - Defines how many files will be checked during each 'analyzing round'.
	 */
	@Accessor(qualifier = "paging", type = Accessor.Type.SETTER)
	public void setPaging(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PAGING, value);
	}
	
}
