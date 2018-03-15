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
package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.StepModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.Map;

/**
 * Generated model class for type ChangeDescriptor first defined at extension processing.
 */
@SuppressWarnings("all")
public class ChangeDescriptorModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ChangeDescriptor";
	
	/** <i>Generated constant</i> - Attribute key of <code>ChangeDescriptor.cronJob</code> attribute defined at extension <code>processing</code>. */
	public static final String CRONJOB = "cronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>ChangeDescriptor.step</code> attribute defined at extension <code>processing</code>. */
	public static final String STEP = "step";
	
	/** <i>Generated constant</i> - Attribute key of <code>ChangeDescriptor.changedItem</code> attribute defined at extension <code>processing</code>. */
	public static final String CHANGEDITEM = "changedItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>ChangeDescriptor.sequenceNumber</code> attribute defined at extension <code>processing</code>. */
	public static final String SEQUENCENUMBER = "sequenceNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>ChangeDescriptor.saveTimestamp</code> attribute defined at extension <code>processing</code>. */
	public static final String SAVETIMESTAMP = "saveTimestamp";
	
	/** <i>Generated constant</i> - Attribute key of <code>ChangeDescriptor.previousItemState</code> attribute defined at extension <code>processing</code>. */
	public static final String PREVIOUSITEMSTATE = "previousItemState";
	
	/** <i>Generated constant</i> - Attribute key of <code>ChangeDescriptor.changeType</code> attribute defined at extension <code>processing</code>. */
	public static final String CHANGETYPE = "changeType";
	
	/** <i>Generated constant</i> - Attribute key of <code>ChangeDescriptor.description</code> attribute defined at extension <code>processing</code>. */
	public static final String DESCRIPTION = "description";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ChangeDescriptorModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ChangeDescriptorModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _changeType initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 * @param _cronJob initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 * @param _sequenceNumber initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 * @param _step initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ChangeDescriptorModel(final String _changeType, final CronJobModel _cronJob, final Integer _sequenceNumber, final StepModel _step)
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
	 * @param _cronJob initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sequenceNumber initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 * @param _step initial attribute declared by type <code>ChangeDescriptor</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ChangeDescriptorModel(final String _changeType, final CronJobModel _cronJob, final ItemModel _owner, final Integer _sequenceNumber, final StepModel _step)
	{
		super();
		setChangeType(_changeType);
		setCronJob(_cronJob);
		setOwner(_owner);
		setSequenceNumber(_sequenceNumber);
		setStep(_step);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDescriptor.changedItem</code> attribute defined at extension <code>processing</code>. 
	 * @return the changedItem
	 */
	@Accessor(qualifier = "changedItem", type = Accessor.Type.GETTER)
	public ItemModel getChangedItem()
	{
		return getPersistenceContext().getPropertyValue(CHANGEDITEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDescriptor.changeType</code> attribute defined at extension <code>processing</code>. 
	 * @return the changeType
	 */
	@Accessor(qualifier = "changeType", type = Accessor.Type.GETTER)
	public String getChangeType()
	{
		return getPersistenceContext().getPropertyValue(CHANGETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDescriptor.cronJob</code> attribute defined at extension <code>processing</code>. 
	 * @return the cronJob
	 */
	@Accessor(qualifier = "cronJob", type = Accessor.Type.GETTER)
	public CronJobModel getCronJob()
	{
		return getPersistenceContext().getPropertyValue(CRONJOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDescriptor.description</code> attribute defined at extension <code>processing</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getPersistenceContext().getPropertyValue(DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDescriptor.previousItemState</code> attribute defined at extension <code>processing</code>. 
	 * @return the previousItemState
	 */
	@Accessor(qualifier = "previousItemState", type = Accessor.Type.GETTER)
	public Map getPreviousItemState()
	{
		return getPersistenceContext().getPropertyValue(PREVIOUSITEMSTATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDescriptor.saveTimestamp</code> attribute defined at extension <code>processing</code>. 
	 * @return the saveTimestamp
	 */
	@Accessor(qualifier = "saveTimestamp", type = Accessor.Type.GETTER)
	public Date getSaveTimestamp()
	{
		return getPersistenceContext().getPropertyValue(SAVETIMESTAMP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDescriptor.sequenceNumber</code> attribute defined at extension <code>processing</code>. 
	 * @return the sequenceNumber
	 */
	@Accessor(qualifier = "sequenceNumber", type = Accessor.Type.GETTER)
	public Integer getSequenceNumber()
	{
		return getPersistenceContext().getPropertyValue(SEQUENCENUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ChangeDescriptor.step</code> attribute defined at extension <code>processing</code>. 
	 * @return the step
	 */
	@Accessor(qualifier = "step", type = Accessor.Type.GETTER)
	public StepModel getStep()
	{
		return getPersistenceContext().getPropertyValue(STEP);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ChangeDescriptor.changedItem</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the changedItem
	 */
	@Accessor(qualifier = "changedItem", type = Accessor.Type.SETTER)
	public void setChangedItem(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(CHANGEDITEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ChangeDescriptor.changeType</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the changeType
	 */
	@Accessor(qualifier = "changeType", type = Accessor.Type.SETTER)
	public void setChangeType(final String value)
	{
		getPersistenceContext().setPropertyValue(CHANGETYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ChangeDescriptor.cronJob</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the cronJob
	 */
	@Accessor(qualifier = "cronJob", type = Accessor.Type.SETTER)
	public void setCronJob(final CronJobModel value)
	{
		getPersistenceContext().setPropertyValue(CRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ChangeDescriptor.description</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		getPersistenceContext().setPropertyValue(DESCRIPTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ChangeDescriptor.previousItemState</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the previousItemState
	 */
	@Accessor(qualifier = "previousItemState", type = Accessor.Type.SETTER)
	public void setPreviousItemState(final Map value)
	{
		getPersistenceContext().setPropertyValue(PREVIOUSITEMSTATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ChangeDescriptor.saveTimestamp</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the saveTimestamp
	 */
	@Accessor(qualifier = "saveTimestamp", type = Accessor.Type.SETTER)
	public void setSaveTimestamp(final Date value)
	{
		getPersistenceContext().setPropertyValue(SAVETIMESTAMP, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ChangeDescriptor.sequenceNumber</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sequenceNumber
	 */
	@Accessor(qualifier = "sequenceNumber", type = Accessor.Type.SETTER)
	public void setSequenceNumber(final Integer value)
	{
		getPersistenceContext().setPropertyValue(SEQUENCENUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ChangeDescriptor.step</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the step
	 */
	@Accessor(qualifier = "step", type = Accessor.Type.SETTER)
	public void setStep(final StepModel value)
	{
		getPersistenceContext().setPropertyValue(STEP, value);
	}
	
}
