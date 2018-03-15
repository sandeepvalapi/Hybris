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
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import java.util.Date;

/**
 * Generated model class for type SolrIndexOperation first defined at extension solrfacetsearch.
 */
@SuppressWarnings("all")
public class SolrIndexOperationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SolrIndexOperation";
	
	/**<i>Generated relation code constant for relation <code>SolrIndex2SolrIndexOperation</code> defining source attribute <code>index</code> in extension <code>solrfacetsearch</code>.</i>*/
	public static final String _SOLRINDEX2SOLRINDEXOPERATION = "SolrIndex2SolrIndexOperation";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexOperation.id</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexOperation.operation</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String OPERATION = "operation";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexOperation.external</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String EXTERNAL = "external";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexOperation.status</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexOperation.startTime</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String STARTTIME = "startTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexOperation.endTime</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String ENDTIME = "endTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrIndexOperation.index</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String INDEX = "index";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrIndexOperationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrIndexOperationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>SolrIndexOperation</code> at extension <code>solrfacetsearch</code>
	 * @param _index initial attribute declared by type <code>SolrIndexOperation</code> at extension <code>solrfacetsearch</code>
	 * @param _operation initial attribute declared by type <code>SolrIndexOperation</code> at extension <code>solrfacetsearch</code>
	 * @param _status initial attribute declared by type <code>SolrIndexOperation</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrIndexOperationModel(final long _id, final SolrIndexModel _index, final IndexerOperationValues _operation, final IndexerOperationStatus _status)
	{
		super();
		setId(_id);
		setIndex(_index);
		setOperation(_operation);
		setStatus(_status);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>SolrIndexOperation</code> at extension <code>solrfacetsearch</code>
	 * @param _index initial attribute declared by type <code>SolrIndexOperation</code> at extension <code>solrfacetsearch</code>
	 * @param _operation initial attribute declared by type <code>SolrIndexOperation</code> at extension <code>solrfacetsearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _status initial attribute declared by type <code>SolrIndexOperation</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrIndexOperationModel(final long _id, final SolrIndexModel _index, final IndexerOperationValues _operation, final ItemModel _owner, final IndexerOperationStatus _status)
	{
		super();
		setId(_id);
		setIndex(_index);
		setOperation(_operation);
		setOwner(_owner);
		setStatus(_status);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexOperation.endTime</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the endTime
	 */
	@Accessor(qualifier = "endTime", type = Accessor.Type.GETTER)
	public Date getEndTime()
	{
		return getPersistenceContext().getPropertyValue(ENDTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexOperation.id</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public long getId()
	{
		return toPrimitive((Long)getPersistenceContext().getPropertyValue(ID));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexOperation.index</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the index
	 */
	@Accessor(qualifier = "index", type = Accessor.Type.GETTER)
	public SolrIndexModel getIndex()
	{
		return getPersistenceContext().getPropertyValue(INDEX);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexOperation.operation</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the operation
	 */
	@Accessor(qualifier = "operation", type = Accessor.Type.GETTER)
	public IndexerOperationValues getOperation()
	{
		return getPersistenceContext().getPropertyValue(OPERATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexOperation.startTime</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the startTime
	 */
	@Accessor(qualifier = "startTime", type = Accessor.Type.GETTER)
	public Date getStartTime()
	{
		return getPersistenceContext().getPropertyValue(STARTTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexOperation.status</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public IndexerOperationStatus getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexOperation.external</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the external
	 */
	@Accessor(qualifier = "external", type = Accessor.Type.GETTER)
	public boolean isExternal()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(EXTERNAL));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexOperation.endTime</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the endTime
	 */
	@Accessor(qualifier = "endTime", type = Accessor.Type.SETTER)
	public void setEndTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(ENDTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexOperation.external</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the external
	 */
	@Accessor(qualifier = "external", type = Accessor.Type.SETTER)
	public void setExternal(final boolean value)
	{
		getPersistenceContext().setPropertyValue(EXTERNAL, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SolrIndexOperation.id</code> attribute defined at extension <code>solrfacetsearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final long value)
	{
		getPersistenceContext().setPropertyValue(ID, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SolrIndexOperation.index</code> attribute defined at extension <code>solrfacetsearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the index
	 */
	@Accessor(qualifier = "index", type = Accessor.Type.SETTER)
	public void setIndex(final SolrIndexModel value)
	{
		getPersistenceContext().setPropertyValue(INDEX, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SolrIndexOperation.operation</code> attribute defined at extension <code>solrfacetsearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the operation
	 */
	@Accessor(qualifier = "operation", type = Accessor.Type.SETTER)
	public void setOperation(final IndexerOperationValues value)
	{
		getPersistenceContext().setPropertyValue(OPERATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexOperation.startTime</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the startTime
	 */
	@Accessor(qualifier = "startTime", type = Accessor.Type.SETTER)
	public void setStartTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(STARTTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrIndexOperation.status</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final IndexerOperationStatus value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
}
