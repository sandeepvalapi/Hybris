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
package de.hybris.platform.cockpit.reports.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.reports.model.JasperMediaModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CompiledJasperMedia first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class CompiledJasperMediaModel extends JasperMediaModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CompiledJasperMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompiledJasperMedia.compiledReport</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COMPILEDREPORT = "compiledReport";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CompiledJasperMediaModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CompiledJasperMediaModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>JasperMedia</code> at extension <code>cockpit</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 */
	@Deprecated
	public CompiledJasperMediaModel(final CatalogVersionModel _catalogVersion, final String _code)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>JasperMedia</code> at extension <code>cockpit</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CompiledJasperMediaModel(final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompiledJasperMedia.compiledReport</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the compiledReport
	 */
	@Accessor(qualifier = "compiledReport", type = Accessor.Type.GETTER)
	public MediaModel getCompiledReport()
	{
		return getPersistenceContext().getPropertyValue(COMPILEDREPORT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompiledJasperMedia.compiledReport</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the compiledReport
	 */
	@Accessor(qualifier = "compiledReport", type = Accessor.Type.SETTER)
	public void setCompiledReport(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(COMPILEDREPORT, value);
	}
	
}
