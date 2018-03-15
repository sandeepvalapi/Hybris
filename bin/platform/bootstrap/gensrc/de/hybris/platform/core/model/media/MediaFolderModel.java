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
package de.hybris.platform.core.model.media;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type MediaFolder first defined at extension core.
 */
@SuppressWarnings("all")
public class MediaFolderModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MediaFolder";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaFolder.qualifier</code> attribute defined at extension <code>core</code>. */
	public static final String QUALIFIER = "qualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaFolder.path</code> attribute defined at extension <code>core</code>. */
	public static final String PATH = "path";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MediaFolderModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MediaFolderModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _qualifier initial attribute declared by type <code>MediaFolder</code> at extension <code>core</code>
	 */
	@Deprecated
	public MediaFolderModel(final String _qualifier)
	{
		super();
		setQualifier(_qualifier);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _path initial attribute declared by type <code>MediaFolder</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>MediaFolder</code> at extension <code>core</code>
	 */
	@Deprecated
	public MediaFolderModel(final ItemModel _owner, final String _path, final String _qualifier)
	{
		super();
		setOwner(_owner);
		setPath(_path);
		setQualifier(_qualifier);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MediaFolder.path</code> attribute defined at extension <code>core</code>. 
	 * @return the path - The physical path of the folder relative the the media webroot
	 */
	@Accessor(qualifier = "path", type = Accessor.Type.GETTER)
	public String getPath()
	{
		return getPersistenceContext().getPropertyValue(PATH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MediaFolder.qualifier</code> attribute defined at extension <code>core</code>. 
	 * @return the qualifier - Identifies the folder by a qualifier
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
	public String getQualifier()
	{
		return getPersistenceContext().getPropertyValue(QUALIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>MediaFolder.path</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the path - The physical path of the folder relative the the media webroot
	 */
	@Accessor(qualifier = "path", type = Accessor.Type.SETTER)
	public void setPath(final String value)
	{
		getPersistenceContext().setPropertyValue(PATH, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MediaFolder.qualifier</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the qualifier - Identifies the folder by a qualifier
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
	public void setQualifier(final String value)
	{
		getPersistenceContext().setPropertyValue(QUALIFIER, value);
	}
	
}
