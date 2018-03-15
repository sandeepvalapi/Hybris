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
package de.hybris.platform.storelocator.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type GeocodeAddressesCronJob first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class GeocodeAddressesCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "GeocodeAddressesCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>GeocodeAddressesCronJob.batchSize</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String BATCHSIZE = "batchSize";
	
	/** <i>Generated constant</i> - Attribute key of <code>GeocodeAddressesCronJob.internalDelay</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String INTERNALDELAY = "internalDelay";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public GeocodeAddressesCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public GeocodeAddressesCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public GeocodeAddressesCronJobModel(final JobModel _job)
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
	public GeocodeAddressesCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GeocodeAddressesCronJob.batchSize</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the batchSize
	 */
	@Accessor(qualifier = "batchSize", type = Accessor.Type.GETTER)
	public int getBatchSize()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(BATCHSIZE));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GeocodeAddressesCronJob.internalDelay</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the internalDelay
	 */
	@Accessor(qualifier = "internalDelay", type = Accessor.Type.GETTER)
	public int getInternalDelay()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(INTERNALDELAY));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>GeocodeAddressesCronJob.batchSize</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the batchSize
	 */
	@Accessor(qualifier = "batchSize", type = Accessor.Type.SETTER)
	public void setBatchSize(final int value)
	{
		getPersistenceContext().setPropertyValue(BATCHSIZE, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>GeocodeAddressesCronJob.internalDelay</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the internalDelay
	 */
	@Accessor(qualifier = "internalDelay", type = Accessor.Type.SETTER)
	public void setInternalDelay(final int value)
	{
		getPersistenceContext().setPropertyValue(INTERNALDELAY, toObject(value));
	}
	
}
