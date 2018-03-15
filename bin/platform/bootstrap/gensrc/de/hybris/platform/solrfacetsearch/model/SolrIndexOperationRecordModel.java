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

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationStatus;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import de.hybris.platform.solrfacetsearch.model.indexer.SolrIndexedCoresRecordModel;
import java.util.Date;

/**
 * Generated model class for type SolrIndexOperationRecord first defined at extension solrfacetsearch.
 * <p>
 * Deprecated since 6.2, please use SolrIndexOperation instead.
 */
@SuppressWarnings("all")
public class SolrIndexOperationRecordModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SolrIndexOperationRecord";
	
	/**<i>Generated relation code constant for relation <code>IndexCore2IndexOperationRecords</code> defining source attribute <code>solrIndexCoreRecord</code> in extension <code>solrfacetsearch</code>.</i>*/
	public static final String _INDEXCORE2INDEXOPERATIONRECORDS = "IndexCore2IndexOperationRecords";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexOperationRecord.startTime</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String STARTTIME = "startTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexOperationRecord.finishTime</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FINISHTIME = "finishTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexOperationRecord.mode</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String MODE = "mode";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexOperationRecord.status</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexOperationRecord.threadId</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String THREADID = "threadId";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexOperationRecord.clusterId</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String CLUSTERID = "clusterId";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexOperationRecord.failedReason</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FAILEDREASON = "failedReason";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexOperationRecord.solrIndexCoreRecordPOS</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String SOLRINDEXCORERECORDPOS = "solrIndexCoreRecordPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexOperationRecord.solrIndexCoreRecord</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String SOLRINDEXCORERECORD = "solrIndexCoreRecord";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrIndexOperationRecordModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrIndexOperationRecordModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _clusterId initial attribute declared by type <code>SolrIndexOperationRecord</code> at extension <code>solrfacetsearch</code>
	 * @param _mode initial attribute declared by type <code>SolrIndexOperationRecord</code> at extension <code>solrfacetsearch</code>
	 * @param _solrIndexCoreRecord initial attribute declared by type <code>SolrIndexOperationRecord</code> at extension <code>solrfacetsearch</code>
	 * @param _status initial attribute declared by type <code>SolrIndexOperationRecord</code> at extension <code>solrfacetsearch</code>
	 * @param _threadId initial attribute declared by type <code>SolrIndexOperationRecord</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrIndexOperationRecordModel(final int _clusterId, final IndexerOperationValues _mode, final SolrIndexedCoresRecordModel _solrIndexCoreRecord, final IndexerOperationStatus _status, final String _threadId)
	{
		super();
		setClusterId(_clusterId);
		setMode(_mode);
		setSolrIndexCoreRecord(_solrIndexCoreRecord);
		setStatus(_status);
		setThreadId(_threadId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _clusterId initial attribute declared by type <code>SolrIndexOperationRecord</code> at extension <code>solrfacetsearch</code>
	 * @param _mode initial attribute declared by type <code>SolrIndexOperationRecord</code> at extension <code>solrfacetsearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _solrIndexCoreRecord initial attribute declared by type <code>SolrIndexOperationRecord</code> at extension <code>solrfacetsearch</code>
	 * @param _status initial attribute declared by type <code>SolrIndexOperationRecord</code> at extension <code>solrfacetsearch</code>
	 * @param _threadId initial attribute declared by type <code>SolrIndexOperationRecord</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrIndexOperationRecordModel(final int _clusterId, final IndexerOperationValues _mode, final ItemModel _owner, final SolrIndexedCoresRecordModel _solrIndexCoreRecord, final IndexerOperationStatus _status, final String _threadId)
	{
		super();
		setClusterId(_clusterId);
		setMode(_mode);
		setOwner(_owner);
		setSolrIndexCoreRecord(_solrIndexCoreRecord);
		setStatus(_status);
		setThreadId(_threadId);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexOperationRecord.clusterId</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the clusterId
	 */
	@Accessor(qualifier = "clusterId", type = Accessor.Type.GETTER)
	public int getClusterId()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(CLUSTERID));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexOperationRecord.failedReason</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the failedReason
	 */
	@Accessor(qualifier = "failedReason", type = Accessor.Type.GETTER)
	public String getFailedReason()
	{
		return getPersistenceContext().getPropertyValue(FAILEDREASON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexOperationRecord.finishTime</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the finishTime
	 */
	@Accessor(qualifier = "finishTime", type = Accessor.Type.GETTER)
	public Date getFinishTime()
	{
		return getPersistenceContext().getPropertyValue(FINISHTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexOperationRecord.mode</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the mode
	 */
	@Accessor(qualifier = "mode", type = Accessor.Type.GETTER)
	public IndexerOperationValues getMode()
	{
		return getPersistenceContext().getPropertyValue(MODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexOperationRecord.solrIndexCoreRecord</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the solrIndexCoreRecord
	 */
	@Accessor(qualifier = "solrIndexCoreRecord", type = Accessor.Type.GETTER)
	public SolrIndexedCoresRecordModel getSolrIndexCoreRecord()
	{
		return getPersistenceContext().getPropertyValue(SOLRINDEXCORERECORD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexOperationRecord.startTime</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the startTime
	 */
	@Accessor(qualifier = "startTime", type = Accessor.Type.GETTER)
	public Date getStartTime()
	{
		return getPersistenceContext().getPropertyValue(STARTTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexOperationRecord.status</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public IndexerOperationStatus getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexOperationRecord.threadId</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the threadId
	 */
	@Accessor(qualifier = "threadId", type = Accessor.Type.GETTER)
	public String getThreadId()
	{
		return getPersistenceContext().getPropertyValue(THREADID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexOperationRecord.clusterId</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the clusterId
	 */
	@Accessor(qualifier = "clusterId", type = Accessor.Type.SETTER)
	public void setClusterId(final int value)
	{
		getPersistenceContext().setPropertyValue(CLUSTERID, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexOperationRecord.failedReason</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the failedReason
	 */
	@Accessor(qualifier = "failedReason", type = Accessor.Type.SETTER)
	public void setFailedReason(final String value)
	{
		getPersistenceContext().setPropertyValue(FAILEDREASON, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexOperationRecord.finishTime</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the finishTime
	 */
	@Accessor(qualifier = "finishTime", type = Accessor.Type.SETTER)
	public void setFinishTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(FINISHTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexOperationRecord.mode</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the mode
	 */
	@Accessor(qualifier = "mode", type = Accessor.Type.SETTER)
	public void setMode(final IndexerOperationValues value)
	{
		getPersistenceContext().setPropertyValue(MODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexOperationRecord.solrIndexCoreRecord</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the solrIndexCoreRecord
	 */
	@Accessor(qualifier = "solrIndexCoreRecord", type = Accessor.Type.SETTER)
	public void setSolrIndexCoreRecord(final SolrIndexedCoresRecordModel value)
	{
		getPersistenceContext().setPropertyValue(SOLRINDEXCORERECORD, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexOperationRecord.startTime</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the startTime
	 */
	@Accessor(qualifier = "startTime", type = Accessor.Type.SETTER)
	public void setStartTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(STARTTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexOperationRecord.status</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final IndexerOperationStatus value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexOperationRecord.threadId</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the threadId
	 */
	@Accessor(qualifier = "threadId", type = Accessor.Type.SETTER)
	public void setThreadId(final String value)
	{
		getPersistenceContext().setPropertyValue(THREADID, value);
	}
	
}
