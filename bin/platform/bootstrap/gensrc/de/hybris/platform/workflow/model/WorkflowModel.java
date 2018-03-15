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
package de.hybris.platform.workflow.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type Workflow first defined at extension workflow.
 */
@SuppressWarnings("all")
public class WorkflowModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Workflow";
	
	/**<i>Generated relation code constant for relation <code>JobCronJobRelation</code> defining source attribute <code>job</code> in extension <code>processing</code>.</i>*/
	public static final String _JOBCRONJOBRELATION = "JobCronJobRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Workflow.name</code> attribute defined at extension <code>workflow</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>Workflow.description</code> attribute defined at extension <code>workflow</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>Workflow.actions</code> attribute defined at extension <code>workflow</code>. */
	public static final String ACTIONS = "actions";
	
	/** <i>Generated constant</i> - Attribute key of <code>Workflow.attachments</code> attribute defined at extension <code>workflow</code>. */
	public static final String ATTACHMENTS = "attachments";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public WorkflowModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public WorkflowModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>Workflow</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowModel(final WorkflowTemplateModel _job)
	{
		super();
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>Workflow</code> at extension <code>workflow</code>
	 * @param _owner initial attribute declared by type <code>Workflow</code> at extension <code>workflow</code>
	 */
	@Deprecated
	public WorkflowModel(final WorkflowTemplateModel _job, final UserModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Workflow.actions</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the actions
	 */
	@Accessor(qualifier = "actions", type = Accessor.Type.GETTER)
	public List<WorkflowActionModel> getActions()
	{
		return getPersistenceContext().getPropertyValue(ACTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Workflow.attachments</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the attachments - n-part of the WorkflowItemAttachmentRelation; holds a set of attachments (item references) belonging to the topic of workflow
	 */
	@Accessor(qualifier = "attachments", type = Accessor.Type.GETTER)
	public List<WorkflowItemAttachmentModel> getAttachments()
	{
		return getPersistenceContext().getPropertyValue(ATTACHMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Workflow.description</code> attribute defined at extension <code>workflow</code>. 
	 * @return the description - global description of the workflow while each action has its own description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Workflow.description</code> attribute defined at extension <code>workflow</code>. 
	 * @param loc the value localization key 
	 * @return the description - global description of the workflow while each action has its own description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.job</code> attribute defined at extension <code>processing</code> and redeclared at extension <code>workflow</code>. 
	 * @return the job - related workflow template this workflow is created from (never changable)
	 */
	@Override
	@Accessor(qualifier = "job", type = Accessor.Type.GETTER)
	public WorkflowTemplateModel getJob()
	{
		return (WorkflowTemplateModel) super.getJob();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Workflow.name</code> attribute defined at extension <code>workflow</code>. 
	 * @return the name - name of the workflow
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Workflow.name</code> attribute defined at extension <code>workflow</code>. 
	 * @param loc the value localization key 
	 * @return the name - name of the workflow
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Item.owner</code> attribute defined at extension <code>core</code> and redeclared at extension <code>workflow</code>. 
	 * @return the owner - user responsible for this workflow
	 */
	@Override
	@Accessor(qualifier = "owner", type = Accessor.Type.GETTER)
	public UserModel getOwner()
	{
		return (UserModel) super.getOwner();
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Workflow.actions</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the actions
	 */
	@Accessor(qualifier = "actions", type = Accessor.Type.SETTER)
	public void setActions(final List<WorkflowActionModel> value)
	{
		getPersistenceContext().setPropertyValue(ACTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Workflow.attachments</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the attachments - n-part of the WorkflowItemAttachmentRelation; holds a set of attachments (item references) belonging to the topic of workflow
	 */
	@Accessor(qualifier = "attachments", type = Accessor.Type.SETTER)
	public void setAttachments(final List<WorkflowItemAttachmentModel> value)
	{
		getPersistenceContext().setPropertyValue(ATTACHMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Workflow.description</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the description - global description of the workflow while each action has its own description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Workflow.description</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the description - global description of the workflow while each action has its own description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CronJob.job</code> attribute defined at extension <code>processing</code> and redeclared at extension <code>workflow</code>. Can only be used at creation of model - before first save. Will only accept values of type {@link de.hybris.platform.workflow.model.WorkflowTemplateModel}.  
	 *  
	 * @param value the job - related workflow template this workflow is created from (never changable)
	 */
	@Override
	@Accessor(qualifier = "job", type = Accessor.Type.SETTER)
	public void setJob(final JobModel value)
	{
		if( value == null || value instanceof WorkflowTemplateModel)
		{
			super.setJob(value);
		}
		else
		{
			throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.workflow.model.WorkflowTemplateModel");
		}
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Workflow.name</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the name - name of the workflow
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Workflow.name</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the name - name of the workflow
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Item.owner</code> attribute defined at extension <code>core</code> and redeclared at extension <code>workflow</code>. Will only accept values of type {@link de.hybris.platform.core.model.user.UserModel}. 
	 *  
	 * @param value the owner - user responsible for this workflow
	 */
	@Override
	@Accessor(qualifier = "owner", type = Accessor.Type.SETTER)
	public void setOwner(final ItemModel value)
	{
		if( value == null || value instanceof UserModel)
		{
			super.setOwner(value);
		}
		else
		{
			throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.core.model.user.UserModel");
		}
	}
	
}
