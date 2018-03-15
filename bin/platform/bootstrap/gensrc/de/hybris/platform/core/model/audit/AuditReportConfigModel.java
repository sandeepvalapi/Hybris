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
package de.hybris.platform.core.model.audit;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.auditreport.model.AuditReportDataModel;
import de.hybris.platform.core.model.AbstractDynamicContentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type AuditReportConfig first defined at extension core.
 */
@SuppressWarnings("all")
public class AuditReportConfigModel extends AbstractDynamicContentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AuditReportConfig";
	
	/**<i>Generated relation code constant for relation <code>AuditReportData2AuditReportConfigRelation</code> defining source attribute <code>auditReportData</code> in extension <code>auditreportservices</code>.</i>*/
	public static final String _AUDITREPORTDATA2AUDITREPORTCONFIGRELATION = "AuditReportData2AuditReportConfigRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>AuditReportConfig.auditReportData</code> attribute defined at extension <code>auditreportservices</code>. */
	public static final String AUDITREPORTDATA = "auditReportData";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AuditReportConfigModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AuditReportConfigModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 * @param _content initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 */
	@Deprecated
	public AuditReportConfigModel(final String _code, final String _content)
	{
		super();
		setCode(_code);
		setContent(_content);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 * @param _content initial attribute declared by type <code>AbstractDynamicContent</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AuditReportConfigModel(final String _code, final String _content, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setContent(_content);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AuditReportConfig.auditReportData</code> attribute defined at extension <code>auditreportservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the auditReportData
	 */
	@Accessor(qualifier = "auditReportData", type = Accessor.Type.GETTER)
	public Collection<AuditReportDataModel> getAuditReportData()
	{
		return getPersistenceContext().getPropertyValue(AUDITREPORTDATA);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AuditReportConfig.auditReportData</code> attribute defined at extension <code>auditreportservices</code>. 
	 *  
	 * @param value the auditReportData
	 */
	@Accessor(qualifier = "auditReportData", type = Accessor.Type.SETTER)
	public void setAuditReportData(final Collection<AuditReportDataModel> value)
	{
		getPersistenceContext().setPropertyValue(AUDITREPORTDATA, value);
	}
	
}
