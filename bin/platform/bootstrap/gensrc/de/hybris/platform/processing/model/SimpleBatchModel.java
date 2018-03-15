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
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SimpleBatch first defined at extension processing.
 */
@SuppressWarnings("all")
public class SimpleBatchModel extends BatchModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SimpleBatch";
	
	/** <i>Generated constant</i> - Attribute key of <code>SimpleBatch.resultBatchId</code> attribute defined at extension <code>processing</code>. */
	public static final String RESULTBATCHID = "resultBatchId";
	
	/** <i>Generated constant</i> - Attribute key of <code>SimpleBatch.retries</code> attribute defined at extension <code>processing</code>. */
	public static final String RETRIES = "retries";
	
	/** <i>Generated constant</i> - Attribute key of <code>SimpleBatch.scriptCode</code> attribute defined at extension <code>processing</code>. */
	public static final String SCRIPTCODE = "scriptCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>SimpleBatch.context</code> attribute defined at extension <code>processing</code>. */
	public static final String CONTEXT = "context";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SimpleBatchModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SimpleBatchModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _executionId initial attribute declared by type <code>Batch</code> at extension <code>processing</code>
	 * @param _id initial attribute declared by type <code>Batch</code> at extension <code>processing</code>
	 * @param _process initial attribute declared by type <code>Batch</code> at extension <code>processing</code>
	 * @param _retries initial attribute declared by type <code>SimpleBatch</code> at extension <code>processing</code>
	 * @param _type initial attribute declared by type <code>Batch</code> at extension <code>processing</code>
	 */
	@Deprecated
	public SimpleBatchModel(final String _executionId, final String _id, final DistributedProcessModel _process, final int _retries, final BatchType _type)
	{
		super();
		setExecutionId(_executionId);
		setId(_id);
		setProcess(_process);
		setRetries(_retries);
		setType(_type);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _executionId initial attribute declared by type <code>Batch</code> at extension <code>processing</code>
	 * @param _id initial attribute declared by type <code>Batch</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _process initial attribute declared by type <code>Batch</code> at extension <code>processing</code>
	 * @param _retries initial attribute declared by type <code>SimpleBatch</code> at extension <code>processing</code>
	 * @param _type initial attribute declared by type <code>Batch</code> at extension <code>processing</code>
	 */
	@Deprecated
	public SimpleBatchModel(final String _executionId, final String _id, final ItemModel _owner, final DistributedProcessModel _process, final int _retries, final BatchType _type)
	{
		super();
		setExecutionId(_executionId);
		setId(_id);
		setOwner(_owner);
		setProcess(_process);
		setRetries(_retries);
		setType(_type);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SimpleBatch.context</code> attribute defined at extension <code>processing</code>. 
	 * @return the context
	 */
	@Accessor(qualifier = "context", type = Accessor.Type.GETTER)
	public Object getContext()
	{
		return getPersistenceContext().getPropertyValue(CONTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SimpleBatch.resultBatchId</code> attribute defined at extension <code>processing</code>. 
	 * @return the resultBatchId
	 */
	@Accessor(qualifier = "resultBatchId", type = Accessor.Type.GETTER)
	public String getResultBatchId()
	{
		return getPersistenceContext().getPropertyValue(RESULTBATCHID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SimpleBatch.retries</code> attribute defined at extension <code>processing</code>. 
	 * @return the retries
	 */
	@Accessor(qualifier = "retries", type = Accessor.Type.GETTER)
	public int getRetries()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(RETRIES));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SimpleBatch.scriptCode</code> attribute defined at extension <code>processing</code>. 
	 * @return the scriptCode
	 */
	@Accessor(qualifier = "scriptCode", type = Accessor.Type.GETTER)
	public String getScriptCode()
	{
		return getPersistenceContext().getPropertyValue(SCRIPTCODE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SimpleBatch.context</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the context
	 */
	@Accessor(qualifier = "context", type = Accessor.Type.SETTER)
	public void setContext(final Object value)
	{
		getPersistenceContext().setPropertyValue(CONTEXT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SimpleBatch.resultBatchId</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the resultBatchId
	 */
	@Accessor(qualifier = "resultBatchId", type = Accessor.Type.SETTER)
	public void setResultBatchId(final String value)
	{
		getPersistenceContext().setPropertyValue(RESULTBATCHID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SimpleBatch.retries</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the retries
	 */
	@Accessor(qualifier = "retries", type = Accessor.Type.SETTER)
	public void setRetries(final int value)
	{
		getPersistenceContext().setPropertyValue(RETRIES, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SimpleBatch.scriptCode</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the scriptCode
	 */
	@Accessor(qualifier = "scriptCode", type = Accessor.Type.SETTER)
	public void setScriptCode(final String value)
	{
		getPersistenceContext().setPropertyValue(SCRIPTCODE, value);
	}
	
}
