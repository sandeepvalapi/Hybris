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
import de.hybris.platform.core.model.media.AbstractMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type DerivedMedia first defined at extension core.
 */
@SuppressWarnings("all")
public class DerivedMediaModel extends AbstractMediaModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DerivedMedia";
	
	/**<i>Generated relation code constant for relation <code>Media2DerivedMediaRel</code> defining source attribute <code>media</code> in extension <code>core</code>.</i>*/
	public static final String _MEDIA2DERIVEDMEDIAREL = "Media2DerivedMediaRel";
	
	/** <i>Generated constant</i> - Attribute key of <code>DerivedMedia.version</code> attribute defined at extension <code>core</code>. */
	public static final String VERSION = "version";
	
	/** <i>Generated constant</i> - Attribute key of <code>DerivedMedia.media</code> attribute defined at extension <code>core</code>. */
	public static final String MEDIA = "media";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DerivedMediaModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DerivedMediaModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _media initial attribute declared by type <code>DerivedMedia</code> at extension <code>core</code>
	 * @param _version initial attribute declared by type <code>DerivedMedia</code> at extension <code>core</code>
	 */
	@Deprecated
	public DerivedMediaModel(final MediaModel _media, final String _version)
	{
		super();
		setMedia(_media);
		setVersion(_version);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _media initial attribute declared by type <code>DerivedMedia</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _version initial attribute declared by type <code>DerivedMedia</code> at extension <code>core</code>
	 */
	@Deprecated
	public DerivedMediaModel(final MediaModel _media, final ItemModel _owner, final String _version)
	{
		super();
		setMedia(_media);
		setOwner(_owner);
		setVersion(_version);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DerivedMedia.media</code> attribute defined at extension <code>core</code>. 
	 * @return the media
	 */
	@Accessor(qualifier = "media", type = Accessor.Type.GETTER)
	public MediaModel getMedia()
	{
		return getPersistenceContext().getPropertyValue(MEDIA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DerivedMedia.version</code> attribute defined at extension <code>core</code>. 
	 * @return the version - Version of DerivedMedia (mostly name of format)
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.GETTER)
	public String getVersion()
	{
		return getPersistenceContext().getPropertyValue(VERSION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DerivedMedia.media</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the media
	 */
	@Accessor(qualifier = "media", type = Accessor.Type.SETTER)
	public void setMedia(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(MEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DerivedMedia.version</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the version - Version of DerivedMedia (mostly name of format)
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.SETTER)
	public void setVersion(final String value)
	{
		getPersistenceContext().setPropertyValue(VERSION, value);
	}
	
}
