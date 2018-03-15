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
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type DistributedImportProcess first defined at extension impex.
 */
@SuppressWarnings("all")
public class DistributedImportProcessModel extends DistributedProcessModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DistributedImportProcess";
	
	/** <i>Generated constant</i> - Attribute key of <code>DistributedImportProcess.impExImportCronJob</code> attribute defined at extension <code>impex</code>. */
	public static final String IMPEXIMPORTCRONJOB = "impExImportCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>DistributedImportProcess.metadata</code> attribute defined at extension <code>impex</code>. */
	public static final String METADATA = "metadata";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DistributedImportProcessModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DistributedImportProcessModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _currentExecutionId initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _impExImportCronJob initial attribute declared by type <code>DistributedImportProcess</code> at extension <code>impex</code>
	 * @param _state initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 */
	@Deprecated
	public DistributedImportProcessModel(final String _code, final String _currentExecutionId, final ImpExImportCronJobModel _impExImportCronJob, final DistributedProcessState _state)
	{
		super();
		setCode(_code);
		setCurrentExecutionId(_currentExecutionId);
		setImpExImportCronJob(_impExImportCronJob);
		setState(_state);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _currentExecutionId initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _handlerBeanId initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _impExImportCronJob initial attribute declared by type <code>DistributedImportProcess</code> at extension <code>impex</code>
	 * @param _metadata initial attribute declared by type <code>DistributedImportProcess</code> at extension <code>impex</code>
	 * @param _nodeGroup initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _state initial attribute declared by type <code>DistributedProcess</code> at extension <code>processing</code>
	 */
	@Deprecated
	public DistributedImportProcessModel(final String _code, final String _currentExecutionId, final String _handlerBeanId, final ImpExImportCronJobModel _impExImportCronJob, final String _metadata, final String _nodeGroup, final ItemModel _owner, final DistributedProcessState _state)
	{
		super();
		setCode(_code);
		setCurrentExecutionId(_currentExecutionId);
		setHandlerBeanId(_handlerBeanId);
		setImpExImportCronJob(_impExImportCronJob);
		setMetadata(_metadata);
		setNodeGroup(_nodeGroup);
		setOwner(_owner);
		setState(_state);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DistributedImportProcess.impExImportCronJob</code> attribute defined at extension <code>impex</code>. 
	 * @return the impExImportCronJob
	 */
	@Accessor(qualifier = "impExImportCronJob", type = Accessor.Type.GETTER)
	public ImpExImportCronJobModel getImpExImportCronJob()
	{
		return getPersistenceContext().getPropertyValue(IMPEXIMPORTCRONJOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DistributedImportProcess.metadata</code> attribute defined at extension <code>impex</code>. 
	 * @return the metadata
	 */
	@Accessor(qualifier = "metadata", type = Accessor.Type.GETTER)
	public String getMetadata()
	{
		return getPersistenceContext().getPropertyValue(METADATA);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DistributedImportProcess.impExImportCronJob</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the impExImportCronJob
	 */
	@Accessor(qualifier = "impExImportCronJob", type = Accessor.Type.SETTER)
	public void setImpExImportCronJob(final ImpExImportCronJobModel value)
	{
		getPersistenceContext().setPropertyValue(IMPEXIMPORTCRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>DistributedImportProcess.metadata</code> attribute defined at extension <code>impex</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the metadata
	 */
	@Accessor(qualifier = "metadata", type = Accessor.Type.SETTER)
	public void setMetadata(final String value)
	{
		getPersistenceContext().setPropertyValue(METADATA, value);
	}
	
}
