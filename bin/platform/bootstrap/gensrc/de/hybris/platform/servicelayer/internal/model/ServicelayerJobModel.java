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
package de.hybris.platform.servicelayer.internal.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ServicelayerJob first defined at extension processing.
 */
@SuppressWarnings("all")
public class ServicelayerJobModel extends JobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ServicelayerJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>ServicelayerJob.springId</code> attribute defined at extension <code>processing</code>. */
	public static final String SPRINGID = "springId";
	
	/** <i>Generated constant</i> - Attribute key of <code>ServicelayerJob.springIdCronJobFactory</code> attribute defined at extension <code>processing</code>. */
	public static final String SPRINGIDCRONJOBFACTORY = "springIdCronJobFactory";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ServicelayerJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ServicelayerJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _springId initial attribute declared by type <code>ServicelayerJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ServicelayerJobModel(final String _code, final String _springId)
	{
		super();
		setCode(_code);
		setSpringId(_springId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _nodeID initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _springId initial attribute declared by type <code>ServicelayerJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ServicelayerJobModel(final String _code, final Integer _nodeID, final ItemModel _owner, final String _springId)
	{
		super();
		setCode(_code);
		setNodeID(_nodeID);
		setOwner(_owner);
		setSpringId(_springId);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ServicelayerJob.springId</code> attribute defined at extension <code>processing</code>. 
	 * @return the springId
	 */
	@Accessor(qualifier = "springId", type = Accessor.Type.GETTER)
	public String getSpringId()
	{
		return getPersistenceContext().getPropertyValue(SPRINGID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ServicelayerJob.springIdCronJobFactory</code> attribute defined at extension <code>processing</code>. 
	 * @return the springIdCronJobFactory
	 */
	@Accessor(qualifier = "springIdCronJobFactory", type = Accessor.Type.GETTER)
	public String getSpringIdCronJobFactory()
	{
		return getPersistenceContext().getPropertyValue(SPRINGIDCRONJOBFACTORY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ServicelayerJob.springId</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the springId
	 */
	@Accessor(qualifier = "springId", type = Accessor.Type.SETTER)
	public void setSpringId(final String value)
	{
		getPersistenceContext().setPropertyValue(SPRINGID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ServicelayerJob.springIdCronJobFactory</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the springIdCronJobFactory
	 */
	@Accessor(qualifier = "springIdCronJobFactory", type = Accessor.Type.SETTER)
	public void setSpringIdCronJobFactory(final String value)
	{
		getPersistenceContext().setPropertyValue(SPRINGIDCRONJOBFACTORY, value);
	}
	
}
