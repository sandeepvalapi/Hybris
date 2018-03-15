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
package de.hybris.platform.processing.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SimpleDistributedProcess first defined at extension processing.
 */
@SuppressWarnings("all")
public class SimpleDistributedProcessModel extends DistributedProcessModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SimpleDistributedProcess";
	
	/** <i>Generated constant</i> - Attribute key of <code>SimpleDistributedProcess.batchSize</code> attribute defined at extension <code>processing</code>. */
	public static final String BATCHSIZE = "batchSize";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SimpleDistributedProcessModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SimpleDistributedProcessModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _currentExecutionId initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _state initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 */
	@Deprecated
	public SimpleDistributedProcessModel(final String _code, final String _currentExecutionId, final DistributedProcessState _state)
	{
		super();
		setCode(_code);
		setCurrentExecutionId(_currentExecutionId);
		setState(_state);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _batchSize initial attribute declared by type <code>SimpleDistributedProcess</code> at extension <code>processing</code>
	 * @param _code initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _currentExecutionId initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _handlerBeanId initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _nodeGroup initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _state initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 */
	@Deprecated
	public SimpleDistributedProcessModel(final int _batchSize, final String _code, final String _currentExecutionId, final String _handlerBeanId, final String _nodeGroup, final ItemModel _owner, final DistributedProcessState _state)
	{
		super();
		setBatchSize(_batchSize);
		setCode(_code);
		setCurrentExecutionId(_currentExecutionId);
		setHandlerBeanId(_handlerBeanId);
		setNodeGroup(_nodeGroup);
		setOwner(_owner);
		setState(_state);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SimpleDistributedProcess.batchSize</code> attribute defined at extension <code>processing</code>. 
	 * @return the batchSize
	 */
	@Accessor(qualifier = "batchSize", type = Accessor.Type.GETTER)
	public int getBatchSize()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(BATCHSIZE));
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SimpleDistributedProcess.batchSize</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the batchSize
	 */
	@Accessor(qualifier = "batchSize", type = Accessor.Type.SETTER)
	public void setBatchSize(final int value)
	{
		getPersistenceContext().setPropertyValue(BATCHSIZE, toObject(value));
	}
	
}
