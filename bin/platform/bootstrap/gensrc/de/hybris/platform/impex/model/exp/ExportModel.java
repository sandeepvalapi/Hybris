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
package de.hybris.platform.impex.model.exp;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.impex.model.exp.ImpExExportMediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type Export first defined at extension impex.
 */
@SuppressWarnings("all")
public class ExportModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Export";
	
	/** <i>Generated constant</i> - Attribute key of <code>Export.code</code> attribute defined at extension <code>impex</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Export.exportedMedias</code> attribute defined at extension <code>impex</code>. */
	public static final String EXPORTEDMEDIAS = "exportedMedias";
	
	/** <i>Generated constant</i> - Attribute key of <code>Export.exportedData</code> attribute defined at extension <code>impex</code>. */
	public static final String EXPORTEDDATA = "exportedData";
	
	/** <i>Generated constant</i> - Attribute key of <code>Export.exportScript</code> attribute defined at extension <code>impex</code>. */
	public static final String EXPORTSCRIPT = "exportScript";
	
	/** <i>Generated constant</i> - Attribute key of <code>Export.description</code> attribute defined at extension <code>impex</code>. */
	public static final String DESCRIPTION = "description";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ExportModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ExportModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Export</code> at extension <code>impex</code>
	 */
	@Deprecated
	public ExportModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Export</code> at extension <code>impex</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ExportModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Export.code</code> attribute defined at extension <code>impex</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Export.description</code> attribute defined at extension <code>impex</code>. 
	 * @return the description - description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Export.description</code> attribute defined at extension <code>impex</code>. 
	 * @param loc the value localization key 
	 * @return the description - description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Export.exportedData</code> attribute defined at extension <code>impex</code>. 
	 * @return the exportedData - contains the exported data
	 */
	@Accessor(qualifier = "exportedData", type = Accessor.Type.GETTER)
	public ImpExExportMediaModel getExportedData()
	{
		return getPersistenceContext().getPropertyValue(EXPORTEDDATA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Export.exportedMedias</code> attribute defined at extension <code>impex</code>. 
	 * @return the exportedMedias - contains the exported medias
	 */
	@Accessor(qualifier = "exportedMedias", type = Accessor.Type.GETTER)
	public ImpExExportMediaModel getExportedMedias()
	{
		return getPersistenceContext().getPropertyValue(EXPORTEDMEDIAS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Export.exportScript</code> attribute defined at extension <code>impex</code>. 
	 * @return the exportScript - contains the export script
	 */
	@Accessor(qualifier = "exportScript", type = Accessor.Type.GETTER)
	public ImpExMediaModel getExportScript()
	{
		return getPersistenceContext().getPropertyValue(EXPORTSCRIPT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Export.code</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Export.description</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the description - description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Export.description</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the description - description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Export.exportedData</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the exportedData - contains the exported data
	 */
	@Accessor(qualifier = "exportedData", type = Accessor.Type.SETTER)
	public void setExportedData(final ImpExExportMediaModel value)
	{
		getPersistenceContext().setPropertyValue(EXPORTEDDATA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Export.exportedMedias</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the exportedMedias - contains the exported medias
	 */
	@Accessor(qualifier = "exportedMedias", type = Accessor.Type.SETTER)
	public void setExportedMedias(final ImpExExportMediaModel value)
	{
		getPersistenceContext().setPropertyValue(EXPORTEDMEDIAS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Export.exportScript</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the exportScript - contains the export script
	 */
	@Accessor(qualifier = "exportScript", type = Accessor.Type.SETTER)
	public void setExportScript(final ImpExMediaModel value)
	{
		getPersistenceContext().setPropertyValue(EXPORTSCRIPT, value);
	}
	
}
