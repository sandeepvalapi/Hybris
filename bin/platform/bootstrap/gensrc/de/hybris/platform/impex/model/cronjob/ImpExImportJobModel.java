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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ImpExImportJob first defined at extension impex.
 */
@SuppressWarnings("all")
public class ImpExImportJobModel extends JobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ImpExImportJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportJob.maxThreads</code> attribute defined at extension <code>impex</code>. */
	public static final String MAXTHREADS = "maxThreads";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ImpExImportJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ImpExImportJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ImpExImportJobModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _nodeID initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ImpExImportJobModel(final String _code, final Integer _nodeID, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setNodeID(_nodeID);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportJob.maxThreads</code> attribute defined at extension <code>impex</code>. 
	 * @return the maxThreads
	 */
	@Accessor(qualifier = "maxThreads", type = Accessor.Type.GETTER)
	public Integer getMaxThreads()
	{
		return getPersistenceContext().getPropertyValue(MAXTHREADS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportJob.maxThreads</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the maxThreads
	 */
	@Accessor(qualifier = "maxThreads", type = Accessor.Type.SETTER)
	public void setMaxThreads(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MAXTHREADS, value);
	}
	
}
