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
package de.hybris.platform.impex.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.impex.model.ImportBatchContentModel;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.BatchModel;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ImportBatch first defined at extension impex.
 */
@SuppressWarnings("all")
public class ImportBatchModel extends BatchModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ImportBatch";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImportBatch.group</code> attribute defined at extension <code>impex</code>. */
	public static final String GROUP = "group";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImportBatch.metadata</code> attribute defined at extension <code>impex</code>. */
	public static final String METADATA = "metadata";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImportBatch.importContentCode</code> attribute defined at extension <code>impex</code>. */
	public static final String IMPORTCONTENTCODE = "importContentCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImportBatch.importBatchContent</code> attribute defined at extension <code>impex</code>. */
	public static final String IMPORTBATCHCONTENT = "importBatchContent";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ImportBatchModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ImportBatchModel(final ItemModelContext ctx)
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
	public ImportBatchModel(final String _executionId, final String _id, final DistributedProcessModel _process, final BatchType _type)
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
	 * @param _importContentCode initial attribute declared by type <code>ImportBatch</code> at extension <code>impex</code>
	 * @param _metadata initial attribute declared by type <code>ImportBatch</code> at extension <code>impex</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _process initial attribute declared by type <code>Batch</code> at extension <code>processing</code>
	 * @param _type initial attribute declared by type <code>Batch</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ImportBatchModel(final String _executionId, final String _id, final String _importContentCode, final String _metadata, final ItemModel _owner, final DistributedProcessModel _process, final BatchType _type)
	{
		super();
		setExecutionId(_executionId);
		setId(_id);
		setImportContentCode(_importContentCode);
		setMetadata(_metadata);
		setOwner(_owner);
		setProcess(_process);
		setType(_type);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImportBatch.group</code> attribute defined at extension <code>impex</code>. 
	 * @return the group
	 */
	@Accessor(qualifier = "group", type = Accessor.Type.GETTER)
	public int getGroup()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(GROUP));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImportBatch.importBatchContent</code> dynamic attribute defined at extension <code>impex</code>. 
	 * @return the importBatchContent
	 */
	@Accessor(qualifier = "importBatchContent", type = Accessor.Type.GETTER)
	public ImportBatchContentModel getImportBatchContent()
	{
		return getPersistenceContext().getDynamicValue(this,IMPORTBATCHCONTENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImportBatch.importContentCode</code> attribute defined at extension <code>impex</code>. 
	 * @return the importContentCode
	 */
	@Accessor(qualifier = "importContentCode", type = Accessor.Type.GETTER)
	public String getImportContentCode()
	{
		return getPersistenceContext().getPropertyValue(IMPORTCONTENTCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImportBatch.metadata</code> attribute defined at extension <code>impex</code>. 
	 * @return the metadata
	 */
	@Accessor(qualifier = "metadata", type = Accessor.Type.GETTER)
	public String getMetadata()
	{
		return getPersistenceContext().getPropertyValue(METADATA);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ImportBatch.group</code> attribute defined at extension <code>impex</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the group
	 */
	@Accessor(qualifier = "group", type = Accessor.Type.SETTER)
	public void setGroup(final int value)
	{
		getPersistenceContext().setPropertyValue(GROUP, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ImportBatch.importContentCode</code> attribute defined at extension <code>impex</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the importContentCode
	 */
	@Accessor(qualifier = "importContentCode", type = Accessor.Type.SETTER)
	public void setImportContentCode(final String value)
	{
		getPersistenceContext().setPropertyValue(IMPORTCONTENTCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ImportBatch.metadata</code> attribute defined at extension <code>impex</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the metadata
	 */
	@Accessor(qualifier = "metadata", type = Accessor.Type.SETTER)
	public void setMetadata(final String value)
	{
		getPersistenceContext().setPropertyValue(METADATA, value);
	}
	
}
