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
package de.hybris.platform.solrfacetsearch.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.processing.model.SimpleBatchModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SolrIndexerBatch first defined at extension solrfacetsearch.
 */
@SuppressWarnings("all")
public class SolrIndexerBatchModel extends SimpleBatchModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SolrIndexerBatch";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrIndexerBatchModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrIndexerBatchModel(final ItemModelContext ctx)
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
	public SolrIndexerBatchModel(final String _executionId, final String _id, final DistributedProcessModel _process, final int _retries, final BatchType _type)
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
	public SolrIndexerBatchModel(final String _executionId, final String _id, final ItemModel _owner, final DistributedProcessModel _process, final int _retries, final BatchType _type)
	{
		super();
		setExecutionId(_executionId);
		setId(_id);
		setOwner(_owner);
		setProcess(_process);
		setRetries(_retries);
		setType(_type);
	}
	
	
}
