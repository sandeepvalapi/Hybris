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
package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.SyncItemCronJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.ChangeDescriptorModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.StepModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ItemSyncDescriptor first defined at extension catalog.
 */
@SuppressWarnings("all")
public class ItemSyncDescriptorModel extends ChangeDescriptorModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ItemSyncDescriptor";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncDescriptor.targetItem</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETITEM = "targetItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncDescriptor.done</code> attribute defined at extension <code>catalog</code>. */
	public static final String DONE = "done";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncDescriptor.copiedImplicitely</code> attribute defined at extension <code>catalog</code>. */
	public static final String COPIEDIMPLICITELY = "copiedImplicitely";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ItemSyncDescriptorModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ItemSyncDescriptorModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _changeType initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 * @param _cronJob initial attribute declared by type <code>ItemSyncDescriptor</code> at extension <code>catalog</code>
	 * @param _sequenceNumber initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 * @param _step initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ItemSyncDescriptorModel(final String _changeType, final SyncItemCronJobModel _cronJob, final Integer _sequenceNumber, final StepModel _step)
	{
		super();
		setChangeType(_changeType);
		setCronJob(_cronJob);
		setSequenceNumber(_sequenceNumber);
		setStep(_step);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _changeType initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 * @param _cronJob initial attribute declared by type <code>ItemSyncDescriptor</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sequenceNumber initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 * @param _step initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ItemSyncDescriptorModel(final String _changeType, final SyncItemCronJobModel _cronJob, final ItemModel _owner, final Integer _sequenceNumber, final StepModel _step)
	{
		super();
		setChangeType(_changeType);
		setCronJob(_cronJob);
		setOwner(_owner);
		setSequenceNumber(_sequenceNumber);
		setStep(_step);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncDescriptor.copiedImplicitely</code> attribute defined at extension <code>catalog</code>. 
	 * @return the copiedImplicitely
	 */
	@Accessor(qualifier = "copiedImplicitely", type = Accessor.Type.GETTER)
	public Boolean getCopiedImplicitely()
	{
		return getPersistenceContext().getPropertyValue(COPIEDIMPLICITELY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDescriptor.cronJob</code> attribute defined at extension <code>processing</code> and redeclared at extension <code>catalog</code>. 
	 * @return the cronJob
	 */
	@Override
	@Accessor(qualifier = "cronJob", type = Accessor.Type.GETTER)
	public SyncItemCronJobModel getCronJob()
	{
		return (SyncItemCronJobModel) super.getCronJob();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncDescriptor.done</code> attribute defined at extension <code>catalog</code>. 
	 * @return the done
	 */
	@Accessor(qualifier = "done", type = Accessor.Type.GETTER)
	public Boolean getDone()
	{
		return getPersistenceContext().getPropertyValue(DONE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncDescriptor.targetItem</code> attribute defined at extension <code>catalog</code>. 
	 * @return the targetItem
	 */
	@Accessor(qualifier = "targetItem", type = Accessor.Type.GETTER)
	public ItemModel getTargetItem()
	{
		return getPersistenceContext().getPropertyValue(TARGETITEM);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemSyncDescriptor.copiedImplicitely</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the copiedImplicitely
	 */
	@Accessor(qualifier = "copiedImplicitely", type = Accessor.Type.SETTER)
	public void setCopiedImplicitely(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(COPIEDIMPLICITELY, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ChangeDescriptor.cronJob</code> attribute defined at extension <code>processing</code> and redeclared at extension <code>catalog</code>. Can only be used at creation of model - before first save. Will only accept values of type {@link de.hybris.platform.catalog.model.SyncItemCronJobModel}.  
	 *  
	 * @param value the cronJob
	 */
	@Override
	@Accessor(qualifier = "cronJob", type = Accessor.Type.SETTER)
	public void setCronJob(final CronJobModel value)
	{
		if( value == null || value instanceof SyncItemCronJobModel)
		{
			super.setCronJob(value);
		}
		else
		{
			throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.catalog.model.SyncItemCronJobModel");
		}
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemSyncDescriptor.done</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the done
	 */
	@Accessor(qualifier = "done", type = Accessor.Type.SETTER)
	public void setDone(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(DONE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemSyncDescriptor.targetItem</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the targetItem
	 */
	@Accessor(qualifier = "targetItem", type = Accessor.Type.SETTER)
	public void setTargetItem(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(TARGETITEM, value);
	}
	
}
