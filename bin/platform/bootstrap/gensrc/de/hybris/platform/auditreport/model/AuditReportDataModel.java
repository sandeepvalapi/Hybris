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
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.audit.AuditReportConfigModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AuditReportData first defined at extension auditreportservices.
 */
@SuppressWarnings("all")
public class AuditReportDataModel extends CatalogUnawareMediaModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AuditReportData";
	
	/** <i>Generated constant</i> - Attribute key of <code>AuditReportData.auditRootItem</code> attribute defined at extension <code>auditreportservices</code>. */
	public static final String AUDITROOTITEM = "auditRootItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>AuditReportData.auditReportConfig</code> attribute defined at extension <code>auditreportservices</code>. */
	public static final String AUDITREPORTCONFIG = "auditReportConfig";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AuditReportDataModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AuditReportDataModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _auditRootItem initial attribute declared by type <code>AuditReportData</code> at extension <code>auditreportservices</code>
	 * @param _catalogVersion initial attribute declared by type <code>CatalogUnawareMedia</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 */
	@Deprecated
	public AuditReportDataModel(final ItemModel _auditRootItem, final CatalogVersionModel _catalogVersion, final String _code)
	{
		super();
		setAuditRootItem(_auditRootItem);
		setCatalogVersion(_catalogVersion);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _auditRootItem initial attribute declared by type <code>AuditReportData</code> at extension <code>auditreportservices</code>
	 * @param _catalogVersion initial attribute declared by type <code>CatalogUnawareMedia</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AuditReportDataModel(final ItemModel _auditRootItem, final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner)
	{
		super();
		setAuditRootItem(_auditRootItem);
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AuditReportData.auditReportConfig</code> attribute defined at extension <code>auditreportservices</code>. 
	 * @return the auditReportConfig
	 */
	@Accessor(qualifier = "auditReportConfig", type = Accessor.Type.GETTER)
	public AuditReportConfigModel getAuditReportConfig()
	{
		return getPersistenceContext().getPropertyValue(AUDITREPORTCONFIG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AuditReportData.auditRootItem</code> attribute defined at extension <code>auditreportservices</code>. 
	 * @return the auditRootItem
	 */
	@Accessor(qualifier = "auditRootItem", type = Accessor.Type.GETTER)
	public ItemModel getAuditRootItem()
	{
		return getPersistenceContext().getPropertyValue(AUDITROOTITEM);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AuditReportData.auditReportConfig</code> attribute defined at extension <code>auditreportservices</code>. 
	 *  
	 * @param value the auditReportConfig
	 */
	@Accessor(qualifier = "auditReportConfig", type = Accessor.Type.SETTER)
	public void setAuditReportConfig(final AuditReportConfigModel value)
	{
		getPersistenceContext().setPropertyValue(AUDITREPORTCONFIG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AuditReportData.auditRootItem</code> attribute defined at extension <code>auditreportservices</code>. 
	 *  
	 * @param value the auditRootItem
	 */
	@Accessor(qualifier = "auditRootItem", type = Accessor.Type.SETTER)
	public void setAuditRootItem(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(AUDITROOTITEM, value);
	}
	
}
