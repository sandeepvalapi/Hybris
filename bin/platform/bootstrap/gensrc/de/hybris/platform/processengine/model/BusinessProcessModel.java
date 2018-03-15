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
package de.hybris.platform.processengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.processengine.model.ProcessTaskLogModel;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;

/**
 * Generated model class for type BusinessProcess first defined at extension processing.
 */
@SuppressWarnings("all")
public class BusinessProcessModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "BusinessProcess";
	
	/** <i>Generated constant</i> - Attribute key of <code>BusinessProcess.code</code> attribute defined at extension <code>processing</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>BusinessProcess.processDefinitionName</code> attribute defined at extension <code>processing</code>. */
	public static final String PROCESSDEFINITIONNAME = "processDefinitionName";
	
	/** <i>Generated constant</i> - Attribute key of <code>BusinessProcess.processDefinitionVersion</code> attribute defined at extension <code>processing</code>. */
	public static final String PROCESSDEFINITIONVERSION = "processDefinitionVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>BusinessProcess.state</code> attribute defined at extension <code>processing</code>. */
	public static final String STATE = "state";
	
	/** <i>Generated constant</i> - Attribute key of <code>BusinessProcess.processState</code> attribute defined at extension <code>processing</code>. */
	public static final String PROCESSSTATE = "processState";
	
	/** <i>Generated constant</i> - Attribute key of <code>BusinessProcess.endMessage</code> attribute defined at extension <code>processing</code>. */
	public static final String ENDMESSAGE = "endMessage";
	
	/** <i>Generated constant</i> - Attribute key of <code>BusinessProcess.user</code> attribute defined at extension <code>processing</code>. */
	public static final String USER = "user";
	
	/** <i>Generated constant</i> - Attribute key of <code>BusinessProcess.currentTasks</code> attribute defined at extension <code>processing</code>. */
	public static final String CURRENTTASKS = "currentTasks";
	
	/** <i>Generated constant</i> - Attribute key of <code>BusinessProcess.contextParameters</code> attribute defined at extension <code>processing</code>. */
	public static final String CONTEXTPARAMETERS = "contextParameters";
	
	/** <i>Generated constant</i> - Attribute key of <code>BusinessProcess.taskLogs</code> attribute defined at extension <code>processing</code>. */
	public static final String TASKLOGS = "taskLogs";
	
	/** <i>Generated constant</i> - Attribute key of <code>BusinessProcess.emails</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String EMAILS = "emails";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public BusinessProcessModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public BusinessProcessModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 * @param _processDefinitionName initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 */
	@Deprecated
	public BusinessProcessModel(final String _code, final String _processDefinitionName)
	{
		super();
		setCode(_code);
		setProcessDefinitionName(_processDefinitionName);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _processDefinitionName initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 */
	@Deprecated
	public BusinessProcessModel(final String _code, final ItemModel _owner, final String _processDefinitionName)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setProcessDefinitionName(_processDefinitionName);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BusinessProcess.code</code> attribute defined at extension <code>processing</code>. 
	 * @return the code - Unique identifier of this process
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BusinessProcess.contextParameters</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the contextParameters
	 */
	@Accessor(qualifier = "contextParameters", type = Accessor.Type.GETTER)
	public Collection<BusinessProcessParameterModel> getContextParameters()
	{
		return getPersistenceContext().getPropertyValue(CONTEXTPARAMETERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BusinessProcess.currentTasks</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the currentTasks
	 */
	@Accessor(qualifier = "currentTasks", type = Accessor.Type.GETTER)
	public Collection<ProcessTaskModel> getCurrentTasks()
	{
		return getPersistenceContext().getPropertyValue(CURRENTTASKS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BusinessProcess.emails</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the emails
	 */
	@Accessor(qualifier = "emails", type = Accessor.Type.GETTER)
	public List<EmailMessageModel> getEmails()
	{
		return getPersistenceContext().getPropertyValue(EMAILS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BusinessProcess.endMessage</code> attribute defined at extension <code>processing</code>. 
	 * @return the endMessage - Message given in the end state of the process.
	 */
	@Accessor(qualifier = "endMessage", type = Accessor.Type.GETTER)
	public String getEndMessage()
	{
		return getPersistenceContext().getPropertyValue(ENDMESSAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BusinessProcess.processDefinitionName</code> attribute defined at extension <code>processing</code>. 
	 * @return the processDefinitionName - Name of the process definition to use.
	 */
	@Accessor(qualifier = "processDefinitionName", type = Accessor.Type.GETTER)
	public String getProcessDefinitionName()
	{
		return getPersistenceContext().getPropertyValue(PROCESSDEFINITIONNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BusinessProcess.processDefinitionVersion</code> attribute defined at extension <code>processing</code>. 
	 * @return the processDefinitionVersion - Version of the process definition used by this process.
	 */
	@Accessor(qualifier = "processDefinitionVersion", type = Accessor.Type.GETTER)
	public String getProcessDefinitionVersion()
	{
		return getPersistenceContext().getPropertyValue(PROCESSDEFINITIONVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BusinessProcess.processState</code> dynamic attribute defined at extension <code>processing</code>. 
	 * @return the processState - Current (accessible) state of this process.
	 */
	@Accessor(qualifier = "processState", type = Accessor.Type.GETTER)
	public ProcessState getProcessState()
	{
		return getPersistenceContext().getDynamicValue(this,PROCESSSTATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BusinessProcess.state</code> attribute defined at extension <code>processing</code>. 
	 * @return the state - Current (persisted) state of this process.
	 */
	@Accessor(qualifier = "state", type = Accessor.Type.GETTER)
	public ProcessState getState()
	{
		return getPersistenceContext().getPropertyValue(STATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BusinessProcess.taskLogs</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the taskLogs
	 */
	@Accessor(qualifier = "taskLogs", type = Accessor.Type.GETTER)
	public Collection<ProcessTaskLogModel> getTaskLogs()
	{
		return getPersistenceContext().getPropertyValue(TASKLOGS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BusinessProcess.user</code> attribute defined at extension <code>processing</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>BusinessProcess.code</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code - Unique identifier of this process
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BusinessProcess.contextParameters</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the contextParameters
	 */
	@Accessor(qualifier = "contextParameters", type = Accessor.Type.SETTER)
	public void setContextParameters(final Collection<BusinessProcessParameterModel> value)
	{
		getPersistenceContext().setPropertyValue(CONTEXTPARAMETERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BusinessProcess.currentTasks</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the currentTasks
	 */
	@Accessor(qualifier = "currentTasks", type = Accessor.Type.SETTER)
	public void setCurrentTasks(final Collection<ProcessTaskModel> value)
	{
		getPersistenceContext().setPropertyValue(CURRENTTASKS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BusinessProcess.emails</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the emails
	 */
	@Accessor(qualifier = "emails", type = Accessor.Type.SETTER)
	public void setEmails(final List<EmailMessageModel> value)
	{
		getPersistenceContext().setPropertyValue(EMAILS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BusinessProcess.endMessage</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the endMessage - Message given in the end state of the process.
	 */
	@Accessor(qualifier = "endMessage", type = Accessor.Type.SETTER)
	public void setEndMessage(final String value)
	{
		getPersistenceContext().setPropertyValue(ENDMESSAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>BusinessProcess.processDefinitionName</code> attribute defined at extension <code>processing</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the processDefinitionName - Name of the process definition to use.
	 */
	@Accessor(qualifier = "processDefinitionName", type = Accessor.Type.SETTER)
	public void setProcessDefinitionName(final String value)
	{
		getPersistenceContext().setPropertyValue(PROCESSDEFINITIONNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BusinessProcess.processDefinitionVersion</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the processDefinitionVersion - Version of the process definition used by this process.
	 */
	@Accessor(qualifier = "processDefinitionVersion", type = Accessor.Type.SETTER)
	public void setProcessDefinitionVersion(final String value)
	{
		getPersistenceContext().setPropertyValue(PROCESSDEFINITIONVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BusinessProcess.state</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the state - Current (persisted) state of this process.
	 */
	@Accessor(qualifier = "state", type = Accessor.Type.SETTER)
	public void setState(final ProcessState value)
	{
		getPersistenceContext().setPropertyValue(STATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BusinessProcess.taskLogs</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the taskLogs
	 */
	@Accessor(qualifier = "taskLogs", type = Accessor.Type.SETTER)
	public void setTaskLogs(final Collection<ProcessTaskLogModel> value)
	{
		getPersistenceContext().setPropertyValue(TASKLOGS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BusinessProcess.user</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
}
