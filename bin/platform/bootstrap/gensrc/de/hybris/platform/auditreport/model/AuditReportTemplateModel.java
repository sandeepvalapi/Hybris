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
import de.hybris.platform.commons.enums.RendererTypeEnum;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AuditReportTemplate first defined at extension auditreportservices.
 */
@SuppressWarnings("all")
public class AuditReportTemplateModel extends RendererTemplateModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AuditReportTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>AuditReportTemplate.includeText</code> attribute defined at extension <code>auditreportservices</code>. */
	public static final String INCLUDETEXT = "includeText";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AuditReportTemplateModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AuditReportTemplateModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>RendererTemplate</code> at extension <code>commons</code>
	 * @param _includeText initial attribute declared by type <code>AuditReportTemplate</code> at extension <code>auditreportservices</code>
	 * @param _rendererType initial attribute declared by type <code>RendererTemplate</code> at extension <code>commons</code>
	 */
	@Deprecated
	public AuditReportTemplateModel(final String _code, final Boolean _includeText, final RendererTypeEnum _rendererType)
	{
		super();
		setCode(_code);
		setIncludeText(_includeText);
		setRendererType(_rendererType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>RendererTemplate</code> at extension <code>commons</code>
	 * @param _includeText initial attribute declared by type <code>AuditReportTemplate</code> at extension <code>auditreportservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _rendererType initial attribute declared by type <code>RendererTemplate</code> at extension <code>commons</code>
	 */
	@Deprecated
	public AuditReportTemplateModel(final String _code, final Boolean _includeText, final ItemModel _owner, final RendererTypeEnum _rendererType)
	{
		super();
		setCode(_code);
		setIncludeText(_includeText);
		setOwner(_owner);
		setRendererType(_rendererType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AuditReportTemplate.includeText</code> attribute defined at extension <code>auditreportservices</code>. 
	 * @return the includeText
	 */
	@Accessor(qualifier = "includeText", type = Accessor.Type.GETTER)
	public Boolean getIncludeText()
	{
		return getPersistenceContext().getPropertyValue(INCLUDETEXT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AuditReportTemplate.includeText</code> attribute defined at extension <code>auditreportservices</code>. 
	 *  
	 * @param value the includeText
	 */
	@Accessor(qualifier = "includeText", type = Accessor.Type.SETTER)
	public void setIncludeText(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(INCLUDETEXT, value);
	}
	
}
