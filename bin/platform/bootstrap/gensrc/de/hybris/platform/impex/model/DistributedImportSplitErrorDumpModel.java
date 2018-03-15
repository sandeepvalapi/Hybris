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
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type DistributedImportSplitErrorDump first defined at extension impex.
 */
@SuppressWarnings("all")
public class DistributedImportSplitErrorDumpModel extends ImportBatchContentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DistributedImportSplitErrorDump";
	
	/** <i>Generated constant</i> - Attribute key of <code>DistributedImportSplitErrorDump.processCode</code> attribute defined at extension <code>impex</code>. */
	public static final String PROCESSCODE = "processCode";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DistributedImportSplitErrorDumpModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DistributedImportSplitErrorDumpModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ImportBatchContent</code> at extension <code>impex</code>
	 * @param _content initial attribute declared by type <code>ImportBatchContent</code> at extension <code>impex</code>
	 * @param _processCode initial attribute declared by type <code>DistributedImportSplitErrorDump</code> at extension <code>impex</code>
	 */
	@Deprecated
	public DistributedImportSplitErrorDumpModel(final String _code, final String _content, final String _processCode)
	{
		super();
		setCode(_code);
		setContent(_content);
		setProcessCode(_processCode);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ImportBatchContent</code> at extension <code>impex</code>
	 * @param _content initial attribute declared by type <code>ImportBatchContent</code> at extension <code>impex</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _processCode initial attribute declared by type <code>DistributedImportSplitErrorDump</code> at extension <code>impex</code>
	 */
	@Deprecated
	public DistributedImportSplitErrorDumpModel(final String _code, final String _content, final ItemModel _owner, final String _processCode)
	{
		super();
		setCode(_code);
		setContent(_content);
		setOwner(_owner);
		setProcessCode(_processCode);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DistributedImportSplitErrorDump.processCode</code> attribute defined at extension <code>impex</code>. 
	 * @return the processCode
	 */
	@Accessor(qualifier = "processCode", type = Accessor.Type.GETTER)
	public String getProcessCode()
	{
		return getPersistenceContext().getPropertyValue(PROCESSCODE);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>DistributedImportSplitErrorDump.processCode</code> attribute defined at extension <code>impex</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the processCode
	 */
	@Accessor(qualifier = "processCode", type = Accessor.Type.SETTER)
	public void setProcessCode(final String value)
	{
		getPersistenceContext().setPropertyValue(PROCESSCODE, value);
	}
	
}
