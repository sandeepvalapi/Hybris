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
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type Batch first defined at extension processing.
 */
@SuppressWarnings("all")
public class BatchModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Batch";
	
	/**<i>Generated relation code constant for relation <code>DistributedProcess2BatchRelation</code> defining source attribute <code>process</code> in extension <code>processing</code>.</i>*/
	public static final String _DISTRIBUTEDPROCESS2BATCHRELATION = "DistributedProcess2BatchRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Batch.id</code> attribute defined at extension <code>processing</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>Batch.executionId</code> attribute defined at extension <code>processing</code>. */
	public static final String EXECUTIONID = "executionId";
	
	/** <i>Generated constant</i> - Attribute key of <code>Batch.type</code> attribute defined at extension <code>processing</code>. */
	public static final String TYPE = "type";
	
	/** <i>Generated constant</i> - Attribute key of <code>Batch.remainingWorkLoad</code> attribute defined at extension <code>processing</code>. */
	public static final String REMAININGWORKLOAD = "remainingWorkLoad";
	
	/** <i>Generated constant</i> - Attribute key of <code>Batch.process</code> attribute defined at extension <code>processing</code>. */
	public static final String PROCESS = "process";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public BatchModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public BatchModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _executionId initial attribute declared by type <code>Batch</code> at extension <code>processing</code>
	 * @param _id initial attribute declared by type <code>Batch</code> at extension <code>processing</code>
	 * @param _process initial attribute declared by type <code>Batch</code> at extension <code>processing</code>
	 * @param _type initial attribute declared by type <code>Batch</code> at extension <code>processing</code>
	 */
	@Deprecated
	public BatchModel(final String _executionId, final String _id, final DistributedProcessModel _process, final BatchType _type)
	{
		super();
		setExecutionId(_executionId);
		setId(_id);
		setProcess(_process);
		setType(_type);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _executionId initial attribute declared by type <code>Batch</code> at extension <code>processing</code>
	 * @param _id initial attribute declared by type <code>Batch</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _process initial attribute declared by type <code>Batch</code> at extension <code>processing</code>
	 * @param _type initial attribute declared by type <code>Batch</code> at extension <code>processing</code>
	 */
	@Deprecated
	public BatchModel(final String _executionId, final String _id, final ItemModel _owner, final DistributedProcessModel _process, final BatchType _type)
	{
		super();
		setExecutionId(_executionId);
		setId(_id);
		setOwner(_owner);
		setProcess(_process);
		setType(_type);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Batch.executionId</code> attribute defined at extension <code>processing</code>. 
	 * @return the executionId
	 */
	@Accessor(qualifier = "executionId", type = Accessor.Type.GETTER)
	public String getExecutionId()
	{
		return getPersistenceContext().getPropertyValue(EXECUTIONID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Batch.id</code> attribute defined at extension <code>processing</code>. 
	 * @return the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Batch.process</code> attribute defined at extension <code>processing</code>. 
	 * @return the process
	 */
	@Accessor(qualifier = "process", type = Accessor.Type.GETTER)
	public DistributedProcessModel getProcess()
	{
		return getPersistenceContext().getPropertyValue(PROCESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Batch.remainingWorkLoad</code> attribute defined at extension <code>processing</code>. 
	 * @return the remainingWorkLoad
	 */
	@Accessor(qualifier = "remainingWorkLoad", type = Accessor.Type.GETTER)
	public long getRemainingWorkLoad()
	{
		return toPrimitive((Long)getPersistenceContext().getPropertyValue(REMAININGWORKLOAD));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Batch.type</code> attribute defined at extension <code>processing</code>. 
	 * @return the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public BatchType getType()
	{
		return getPersistenceContext().getPropertyValue(TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Batch.executionId</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the executionId
	 */
	@Accessor(qualifier = "executionId", type = Accessor.Type.SETTER)
	public void setExecutionId(final String value)
	{
		getPersistenceContext().setPropertyValue(EXECUTIONID, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Batch.id</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Batch.process</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the process
	 */
	@Accessor(qualifier = "process", type = Accessor.Type.SETTER)
	public void setProcess(final DistributedProcessModel value)
	{
		getPersistenceContext().setPropertyValue(PROCESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Batch.remainingWorkLoad</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the remainingWorkLoad
	 */
	@Accessor(qualifier = "remainingWorkLoad", type = Accessor.Type.SETTER)
	public void setRemainingWorkLoad(final long value)
	{
		getPersistenceContext().setPropertyValue(REMAININGWORKLOAD, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Batch.type</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.SETTER)
	public void setType(final BatchType value)
	{
		getPersistenceContext().setPropertyValue(TYPE, value);
	}
	
}
