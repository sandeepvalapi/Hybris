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
import de.hybris.platform.processing.model.AbstractRetentionRuleModel;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type RetentionJob first defined at extension processing.
 */
@SuppressWarnings("all")
public class RetentionJobModel extends ServicelayerJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RetentionJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>RetentionJob.retentionRule</code> attribute defined at extension <code>processing</code>. */
	public static final String RETENTIONRULE = "retentionRule";
	
	/** <i>Generated constant</i> - Attribute key of <code>RetentionJob.batchSize</code> attribute defined at extension <code>processing</code>. */
	public static final String BATCHSIZE = "batchSize";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RetentionJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RetentionJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _batchSize initial attribute declared by type <code>RetentionJob</code> at extension <code>processing</code>
	 * @param _code initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _retentionRule initial attribute declared by type <code>RetentionJob</code> at extension <code>processing</code>
	 * @param _springId initial attribute declared by type <code>RetentionJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public RetentionJobModel(final Integer _batchSize, final String _code, final AbstractRetentionRuleModel _retentionRule, final String _springId)
	{
		super();
		setBatchSize(_batchSize);
		setCode(_code);
		setRetentionRule(_retentionRule);
		setSpringId(_springId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _batchSize initial attribute declared by type <code>RetentionJob</code> at extension <code>processing</code>
	 * @param _code initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _nodeID initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _retentionRule initial attribute declared by type <code>RetentionJob</code> at extension <code>processing</code>
	 * @param _springId initial attribute declared by type <code>RetentionJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public RetentionJobModel(final Integer _batchSize, final String _code, final Integer _nodeID, final ItemModel _owner, final AbstractRetentionRuleModel _retentionRule, final String _springId)
	{
		super();
		setBatchSize(_batchSize);
		setCode(_code);
		setNodeID(_nodeID);
		setOwner(_owner);
		setRetentionRule(_retentionRule);
		setSpringId(_springId);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RetentionJob.batchSize</code> attribute defined at extension <code>processing</code>. 
	 * @return the batchSize
	 */
	@Accessor(qualifier = "batchSize", type = Accessor.Type.GETTER)
	public Integer getBatchSize()
	{
		return getPersistenceContext().getPropertyValue(BATCHSIZE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RetentionJob.retentionRule</code> attribute defined at extension <code>processing</code>. 
	 * @return the retentionRule
	 */
	@Accessor(qualifier = "retentionRule", type = Accessor.Type.GETTER)
	public AbstractRetentionRuleModel getRetentionRule()
	{
		return getPersistenceContext().getPropertyValue(RETENTIONRULE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RetentionJob.batchSize</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the batchSize
	 */
	@Accessor(qualifier = "batchSize", type = Accessor.Type.SETTER)
	public void setBatchSize(final Integer value)
	{
		getPersistenceContext().setPropertyValue(BATCHSIZE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RetentionJob.retentionRule</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the retentionRule
	 */
	@Accessor(qualifier = "retentionRule", type = Accessor.Type.SETTER)
	public void setRetentionRule(final AbstractRetentionRuleModel value)
	{
		getPersistenceContext().setPropertyValue(RETENTIONRULE, value);
	}
	
}
