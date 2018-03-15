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
import de.hybris.platform.cronjob.model.CompositeCronJobModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CompositeEntry first defined at extension processing.
 */
@SuppressWarnings("all")
public class CompositeEntryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CompositeEntry";
	
	/**<i>Generated relation code constant for relation <code>CompositeCronJobEntriesRelation</code> defining source attribute <code>compositeCronJob</code> in extension <code>processing</code>.</i>*/
	public static final String _COMPOSITECRONJOBENTRIESRELATION = "CompositeCronJobEntriesRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompositeEntry.code</code> attribute defined at extension <code>processing</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompositeEntry.executableCronJob</code> attribute defined at extension <code>processing</code>. */
	public static final String EXECUTABLECRONJOB = "executableCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompositeEntry.triggerableJob</code> attribute defined at extension <code>processing</code>. */
	public static final String TRIGGERABLEJOB = "triggerableJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompositeEntry.compositeCronJobPOS</code> attribute defined at extension <code>processing</code>. */
	public static final String COMPOSITECRONJOBPOS = "compositeCronJobPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompositeEntry.compositeCronJob</code> attribute defined at extension <code>processing</code>. */
	public static final String COMPOSITECRONJOB = "compositeCronJob";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CompositeEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CompositeEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CompositeEntry</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CompositeEntryModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CompositeEntry</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CompositeEntryModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompositeEntry.code</code> attribute defined at extension <code>processing</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompositeEntry.compositeCronJob</code> attribute defined at extension <code>processing</code>. 
	 * @return the compositeCronJob - assigned CronJob
	 */
	@Accessor(qualifier = "compositeCronJob", type = Accessor.Type.GETTER)
	public CompositeCronJobModel getCompositeCronJob()
	{
		return getPersistenceContext().getPropertyValue(COMPOSITECRONJOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompositeEntry.executableCronJob</code> attribute defined at extension <code>processing</code>. 
	 * @return the executableCronJob
	 */
	@Accessor(qualifier = "executableCronJob", type = Accessor.Type.GETTER)
	public CronJobModel getExecutableCronJob()
	{
		return getPersistenceContext().getPropertyValue(EXECUTABLECRONJOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompositeEntry.triggerableJob</code> attribute defined at extension <code>processing</code>. 
	 * @return the triggerableJob
	 */
	@Accessor(qualifier = "triggerableJob", type = Accessor.Type.GETTER)
	public JobModel getTriggerableJob()
	{
		return getPersistenceContext().getPropertyValue(TRIGGERABLEJOB);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompositeEntry.code</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompositeEntry.compositeCronJob</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the compositeCronJob - assigned CronJob
	 */
	@Accessor(qualifier = "compositeCronJob", type = Accessor.Type.SETTER)
	public void setCompositeCronJob(final CompositeCronJobModel value)
	{
		getPersistenceContext().setPropertyValue(COMPOSITECRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompositeEntry.executableCronJob</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the executableCronJob
	 */
	@Accessor(qualifier = "executableCronJob", type = Accessor.Type.SETTER)
	public void setExecutableCronJob(final CronJobModel value)
	{
		getPersistenceContext().setPropertyValue(EXECUTABLECRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompositeEntry.triggerableJob</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the triggerableJob
	 */
	@Accessor(qualifier = "triggerableJob", type = Accessor.Type.SETTER)
	public void setTriggerableJob(final JobModel value)
	{
		getPersistenceContext().setPropertyValue(TRIGGERABLEJOB, value);
	}
	
}
