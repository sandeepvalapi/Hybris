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
package de.hybris.platform.personalizationservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CxResultsCleaningCronJob first defined at extension personalizationservices.
 */
@SuppressWarnings("all")
public class CxResultsCleaningCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CxResultsCleaningCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxResultsCleaningCronJob.maxResultsAge</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String MAXRESULTSAGE = "maxResultsAge";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxResultsCleaningCronJob.anonymous</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String ANONYMOUS = "anonymous";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CxResultsCleaningCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CxResultsCleaningCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CxResultsCleaningCronJobModel(final JobModel _job)
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
	public CxResultsCleaningCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxResultsCleaningCronJob.maxResultsAge</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the maxResultsAge - After specified number of seconds cx results will be cleaned up. Default is 2 days.
	 */
	@Accessor(qualifier = "maxResultsAge", type = Accessor.Type.GETTER)
	public int getMaxResultsAge()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(MAXRESULTSAGE));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxResultsCleaningCronJob.anonymous</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the anonymous
	 */
	@Accessor(qualifier = "anonymous", type = Accessor.Type.GETTER)
	public boolean isAnonymous()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(ANONYMOUS));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxResultsCleaningCronJob.anonymous</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the anonymous
	 */
	@Accessor(qualifier = "anonymous", type = Accessor.Type.SETTER)
	public void setAnonymous(final boolean value)
	{
		getPersistenceContext().setPropertyValue(ANONYMOUS, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxResultsCleaningCronJob.maxResultsAge</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the maxResultsAge - After specified number of seconds cx results will be cleaned up. Default is 2 days.
	 */
	@Accessor(qualifier = "maxResultsAge", type = Accessor.Type.SETTER)
	public void setMaxResultsAge(final int value)
	{
		getPersistenceContext().setPropertyValue(MAXRESULTSAGE, toObject(value));
	}
	
}
