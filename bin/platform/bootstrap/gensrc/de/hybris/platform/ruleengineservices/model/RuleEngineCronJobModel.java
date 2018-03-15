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
package de.hybris.platform.ruleengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

/**
 * Generated model class for type RuleEngineCronJob first defined at extension ruleengineservices.
 */
@SuppressWarnings("all")
public class RuleEngineCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RuleEngineCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleEngineCronJob.sourceRules</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String SOURCERULES = "sourceRules";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleEngineCronJob.srcModuleName</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String SRCMODULENAME = "srcModuleName";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleEngineCronJob.targetModuleName</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String TARGETMODULENAME = "targetModuleName";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleEngineCronJob.enableIncrementalUpdate</code> attribute defined at extension <code>ruleengineservices</code>. */
	public static final String ENABLEINCREMENTALUPDATE = "enableIncrementalUpdate";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RuleEngineCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RuleEngineCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public RuleEngineCronJobModel(final JobModel _job)
	{
		super();
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public RuleEngineCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleEngineCronJob.enableIncrementalUpdate</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the enableIncrementalUpdate
	 */
	@Accessor(qualifier = "enableIncrementalUpdate", type = Accessor.Type.GETTER)
	public Boolean getEnableIncrementalUpdate()
	{
		return getPersistenceContext().getPropertyValue(ENABLEINCREMENTALUPDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleEngineCronJob.sourceRules</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the sourceRules
	 */
	@Accessor(qualifier = "sourceRules", type = Accessor.Type.GETTER)
	public List<SourceRuleModel> getSourceRules()
	{
		return getPersistenceContext().getPropertyValue(SOURCERULES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleEngineCronJob.srcModuleName</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the srcModuleName
	 */
	@Accessor(qualifier = "srcModuleName", type = Accessor.Type.GETTER)
	public String getSrcModuleName()
	{
		return getPersistenceContext().getPropertyValue(SRCMODULENAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleEngineCronJob.targetModuleName</code> attribute defined at extension <code>ruleengineservices</code>. 
	 * @return the targetModuleName
	 */
	@Accessor(qualifier = "targetModuleName", type = Accessor.Type.GETTER)
	public String getTargetModuleName()
	{
		return getPersistenceContext().getPropertyValue(TARGETMODULENAME);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleEngineCronJob.enableIncrementalUpdate</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the enableIncrementalUpdate
	 */
	@Accessor(qualifier = "enableIncrementalUpdate", type = Accessor.Type.SETTER)
	public void setEnableIncrementalUpdate(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ENABLEINCREMENTALUPDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleEngineCronJob.sourceRules</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the sourceRules
	 */
	@Accessor(qualifier = "sourceRules", type = Accessor.Type.SETTER)
	public void setSourceRules(final List<SourceRuleModel> value)
	{
		getPersistenceContext().setPropertyValue(SOURCERULES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleEngineCronJob.srcModuleName</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the srcModuleName
	 */
	@Accessor(qualifier = "srcModuleName", type = Accessor.Type.SETTER)
	public void setSrcModuleName(final String value)
	{
		getPersistenceContext().setPropertyValue(SRCMODULENAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleEngineCronJob.targetModuleName</code> attribute defined at extension <code>ruleengineservices</code>. 
	 *  
	 * @param value the targetModuleName
	 */
	@Accessor(qualifier = "targetModuleName", type = Accessor.Type.SETTER)
	public void setTargetModuleName(final String value)
	{
		getPersistenceContext().setPropertyValue(TARGETMODULENAME, value);
	}
	
}
