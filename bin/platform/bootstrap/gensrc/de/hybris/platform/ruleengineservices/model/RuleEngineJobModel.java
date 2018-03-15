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
package de.hybris.platform.ruleengineservices.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type RuleEngineJob first defined at extension ruleengineservices.
 */
@SuppressWarnings("all")
public class RuleEngineJobModel extends ServicelayerJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RuleEngineJob";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RuleEngineJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RuleEngineJobModel(final ItemModelContext ctx)
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
	public RuleEngineJobModel(final String _code, final String _springId)
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
	public RuleEngineJobModel(final String _code, final Integer _nodeID, final ItemModel _owner, final String _springId)
	{
		super();
		setCode(_code);
		setNodeID(_nodeID);
		setOwner(_owner);
		setSpringId(_springId);
	}
	
	
}
