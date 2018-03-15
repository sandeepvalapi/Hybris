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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type JasperMedia first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class JasperMediaModel extends MediaModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "JasperMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>JasperMedia.title</code> attribute defined at extension <code>cockpit</code>. */
	public static final String TITLE = "title";
	
	/** <i>Generated constant</i> - Attribute key of <code>JasperMedia.reportDescription</code> attribute defined at extension <code>cockpit</code>. */
	public static final String REPORTDESCRIPTION = "reportDescription";
	
	/** <i>Generated constant</i> - Attribute key of <code>JasperMedia.icon</code> attribute defined at extension <code>cockpit</code>. */
	public static final String ICON = "icon";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public JasperMediaModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public JasperMediaModel(final ItemModelContext ctx)
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
	public JasperMediaModel(final CatalogVersionModel _catalogVersion, final String _code)
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
	public JasperMediaModel(final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JasperMedia.icon</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the icon
	 */
	@Accessor(qualifier = "icon", type = Accessor.Type.GETTER)
	public MediaModel getIcon()
	{
		return getPersistenceContext().getPropertyValue(ICON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JasperMedia.reportDescription</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the reportDescription
	 */
	@Accessor(qualifier = "reportDescription", type = Accessor.Type.GETTER)
	public String getReportDescription()
	{
		return getReportDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>JasperMedia.reportDescription</code> attribute defined at extension <code>cockpit</code>. 
	 * @param loc the value localization key 
	 * @return the reportDescription
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "reportDescription", type = Accessor.Type.GETTER)
	public String getReportDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(REPORTDESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JasperMedia.title</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the title
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle()
	{
		return getTitle(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>JasperMedia.title</code> attribute defined at extension <code>cockpit</code>. 
	 * @param loc the value localization key 
	 * @return the title
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(TITLE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JasperMedia.icon</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the icon
	 */
	@Accessor(qualifier = "icon", type = Accessor.Type.SETTER)
	public void setIcon(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(ICON, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JasperMedia.reportDescription</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the reportDescription
	 */
	@Accessor(qualifier = "reportDescription", type = Accessor.Type.SETTER)
	public void setReportDescription(final String value)
	{
		setReportDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>JasperMedia.reportDescription</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the reportDescription
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "reportDescription", type = Accessor.Type.SETTER)
	public void setReportDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(REPORTDESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JasperMedia.title</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the title
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value)
	{
		setTitle(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>JasperMedia.title</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the title
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(TITLE, loc, value);
	}
	
}
