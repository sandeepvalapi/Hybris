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
package de.hybris.platform.auditreport.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.auditreport.model.AuditReportTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type CreateAuditReportCronJob first defined at extension auditreportservices.
 */
@SuppressWarnings("all")
public class CreateAuditReportCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CreateAuditReportCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreateAuditReportCronJob.rootItem</code> attribute defined at extension <code>auditreportservices</code>. */
	public static final String ROOTITEM = "rootItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreateAuditReportCronJob.configName</code> attribute defined at extension <code>auditreportservices</code>. */
	public static final String CONFIGNAME = "configName";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreateAuditReportCronJob.reportId</code> attribute defined at extension <code>auditreportservices</code>. */
	public static final String REPORTID = "reportId";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreateAuditReportCronJob.audit</code> attribute defined at extension <code>auditreportservices</code>. */
	public static final String AUDIT = "audit";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreateAuditReportCronJob.includedLanguages</code> attribute defined at extension <code>auditreportservices</code>. */
	public static final String INCLUDEDLANGUAGES = "includedLanguages";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreateAuditReportCronJob.auditReportTemplate</code> attribute defined at extension <code>auditreportservices</code>. */
	public static final String AUDITREPORTTEMPLATE = "auditReportTemplate";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CreateAuditReportCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CreateAuditReportCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _auditReportTemplate initial attribute declared by type <code>CreateAuditReportCronJob</code> at extension <code>auditreportservices</code>
	 * @param _configName initial attribute declared by type <code>CreateAuditReportCronJob</code> at extension <code>auditreportservices</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _reportId initial attribute declared by type <code>CreateAuditReportCronJob</code> at extension <code>auditreportservices</code>
	 * @param _rootItem initial attribute declared by type <code>CreateAuditReportCronJob</code> at extension <code>auditreportservices</code>
	 */
	@Deprecated
	public CreateAuditReportCronJobModel(final AuditReportTemplateModel _auditReportTemplate, final String _configName, final JobModel _job, final String _reportId, final ItemModel _rootItem)
	{
		super();
		setAuditReportTemplate(_auditReportTemplate);
		setConfigName(_configName);
		setJob(_job);
		setReportId(_reportId);
		setRootItem(_rootItem);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _auditReportTemplate initial attribute declared by type <code>CreateAuditReportCronJob</code> at extension <code>auditreportservices</code>
	 * @param _configName initial attribute declared by type <code>CreateAuditReportCronJob</code> at extension <code>auditreportservices</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _reportId initial attribute declared by type <code>CreateAuditReportCronJob</code> at extension <code>auditreportservices</code>
	 * @param _rootItem initial attribute declared by type <code>CreateAuditReportCronJob</code> at extension <code>auditreportservices</code>
	 */
	@Deprecated
	public CreateAuditReportCronJobModel(final AuditReportTemplateModel _auditReportTemplate, final String _configName, final JobModel _job, final ItemModel _owner, final String _reportId, final ItemModel _rootItem)
	{
		super();
		setAuditReportTemplate(_auditReportTemplate);
		setConfigName(_configName);
		setJob(_job);
		setOwner(_owner);
		setReportId(_reportId);
		setRootItem(_rootItem);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreateAuditReportCronJob.audit</code> attribute defined at extension <code>auditreportservices</code>. 
	 * @return the audit
	 */
	@Accessor(qualifier = "audit", type = Accessor.Type.GETTER)
	public Boolean getAudit()
	{
		return getPersistenceContext().getPropertyValue(AUDIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreateAuditReportCronJob.auditReportTemplate</code> attribute defined at extension <code>auditreportservices</code>. 
	 * @return the auditReportTemplate
	 */
	@Accessor(qualifier = "auditReportTemplate", type = Accessor.Type.GETTER)
	public AuditReportTemplateModel getAuditReportTemplate()
	{
		return getPersistenceContext().getPropertyValue(AUDITREPORTTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreateAuditReportCronJob.configName</code> attribute defined at extension <code>auditreportservices</code>. 
	 * @return the configName
	 */
	@Accessor(qualifier = "configName", type = Accessor.Type.GETTER)
	public String getConfigName()
	{
		return getPersistenceContext().getPropertyValue(CONFIGNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreateAuditReportCronJob.includedLanguages</code> attribute defined at extension <code>auditreportservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the includedLanguages
	 */
	@Accessor(qualifier = "includedLanguages", type = Accessor.Type.GETTER)
	public Collection<LanguageModel> getIncludedLanguages()
	{
		return getPersistenceContext().getPropertyValue(INCLUDEDLANGUAGES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreateAuditReportCronJob.reportId</code> attribute defined at extension <code>auditreportservices</code>. 
	 * @return the reportId
	 */
	@Accessor(qualifier = "reportId", type = Accessor.Type.GETTER)
	public String getReportId()
	{
		return getPersistenceContext().getPropertyValue(REPORTID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreateAuditReportCronJob.rootItem</code> attribute defined at extension <code>auditreportservices</code>. 
	 * @return the rootItem
	 */
	@Accessor(qualifier = "rootItem", type = Accessor.Type.GETTER)
	public ItemModel getRootItem()
	{
		return getPersistenceContext().getPropertyValue(ROOTITEM);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreateAuditReportCronJob.audit</code> attribute defined at extension <code>auditreportservices</code>. 
	 *  
	 * @param value the audit
	 */
	@Accessor(qualifier = "audit", type = Accessor.Type.SETTER)
	public void setAudit(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(AUDIT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreateAuditReportCronJob.auditReportTemplate</code> attribute defined at extension <code>auditreportservices</code>. 
	 *  
	 * @param value the auditReportTemplate
	 */
	@Accessor(qualifier = "auditReportTemplate", type = Accessor.Type.SETTER)
	public void setAuditReportTemplate(final AuditReportTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(AUDITREPORTTEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreateAuditReportCronJob.configName</code> attribute defined at extension <code>auditreportservices</code>. 
	 *  
	 * @param value the configName
	 */
	@Accessor(qualifier = "configName", type = Accessor.Type.SETTER)
	public void setConfigName(final String value)
	{
		getPersistenceContext().setPropertyValue(CONFIGNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreateAuditReportCronJob.includedLanguages</code> attribute defined at extension <code>auditreportservices</code>. 
	 *  
	 * @param value the includedLanguages
	 */
	@Accessor(qualifier = "includedLanguages", type = Accessor.Type.SETTER)
	public void setIncludedLanguages(final Collection<LanguageModel> value)
	{
		getPersistenceContext().setPropertyValue(INCLUDEDLANGUAGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreateAuditReportCronJob.reportId</code> attribute defined at extension <code>auditreportservices</code>. 
	 *  
	 * @param value the reportId
	 */
	@Accessor(qualifier = "reportId", type = Accessor.Type.SETTER)
	public void setReportId(final String value)
	{
		getPersistenceContext().setPropertyValue(REPORTID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreateAuditReportCronJob.rootItem</code> attribute defined at extension <code>auditreportservices</code>. 
	 *  
	 * @param value the rootItem
	 */
	@Accessor(qualifier = "rootItem", type = Accessor.Type.SETTER)
	public void setRootItem(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(ROOTITEM, value);
	}
	
}
