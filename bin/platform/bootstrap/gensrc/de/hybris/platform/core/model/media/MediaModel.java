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
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.AbstractMediaModel;
import de.hybris.platform.core.model.media.DerivedMediaModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type Media first defined at extension core.
 */
@SuppressWarnings("all")
public class MediaModel extends AbstractMediaModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Media";
	
	/**<i>Generated relation code constant for relation <code>MediaContainer2MediaRel</code> defining source attribute <code>mediaContainer</code> in extension <code>core</code>.</i>*/
	public static final String _MEDIACONTAINER2MEDIAREL = "MediaContainer2MediaRel";
	
	/**<i>Generated relation code constant for relation <code>CategoryMediaRelation</code> defining source attribute <code>supercategories</code> in extension <code>catalog</code>.</i>*/
	public static final String _CATEGORYMEDIARELATION = "CategoryMediaRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Media.code</code> attribute defined at extension <code>core</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Media.internalURL</code> attribute defined at extension <code>core</code>. */
	public static final String INTERNALURL = "internalURL";
	
	/** <i>Generated constant</i> - Attribute key of <code>Media.URL</code> attribute defined at extension <code>core</code>. */
	public static final String URL = "URL";
	
	/** <i>Generated constant</i> - Attribute key of <code>Media.URL2</code> attribute defined at extension <code>core</code>. */
	public static final String URL2 = "URL2";
	
	/** <i>Generated constant</i> - Attribute key of <code>Media.downloadURL</code> attribute defined at extension <code>core</code>. */
	public static final String DOWNLOADURL = "downloadURL";
	
	/** <i>Generated constant</i> - Attribute key of <code>Media.description</code> attribute defined at extension <code>core</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>Media.altText</code> attribute defined at extension <code>core</code>. */
	public static final String ALTTEXT = "altText";
	
	/** <i>Generated constant</i> - Attribute key of <code>Media.removable</code> attribute defined at extension <code>core</code>. */
	public static final String REMOVABLE = "removable";
	
	/** <i>Generated constant</i> - Attribute key of <code>Media.mediaFormat</code> attribute defined at extension <code>core</code>. */
	public static final String MEDIAFORMAT = "mediaFormat";
	
	/** <i>Generated constant</i> - Attribute key of <code>Media.folder</code> attribute defined at extension <code>core</code>. */
	public static final String FOLDER = "folder";
	
	/** <i>Generated constant</i> - Attribute key of <code>Media.subFolderPath</code> attribute defined at extension <code>core</code>. */
	public static final String SUBFOLDERPATH = "subFolderPath";
	
	/** <i>Generated constant</i> - Attribute key of <code>Media.foreignDataOwners</code> attribute defined at extension <code>core</code>. */
	public static final String FOREIGNDATAOWNERS = "foreignDataOwners";
	
	/** <i>Generated constant</i> - Attribute key of <code>Media.permittedPrincipals</code> attribute defined at extension <code>core</code>. */
	public static final String PERMITTEDPRINCIPALS = "permittedPrincipals";
	
	/** <i>Generated constant</i> - Attribute key of <code>Media.deniedPrincipals</code> attribute defined at extension <code>core</code>. */
	public static final String DENIEDPRINCIPALS = "deniedPrincipals";
	
	/** <i>Generated constant</i> - Attribute key of <code>Media.mediaContainer</code> attribute defined at extension <code>core</code>. */
	public static final String MEDIACONTAINER = "mediaContainer";
	
	/** <i>Generated constant</i> - Attribute key of <code>Media.derivedMedias</code> attribute defined at extension <code>core</code>. */
	public static final String DERIVEDMEDIAS = "derivedMedias";
	
	/** <i>Generated constant</i> - Attribute key of <code>Media.catalog</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOG = "catalog";
	
	/** <i>Generated constant</i> - Attribute key of <code>Media.catalogVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>Media.supercategories</code> attribute defined at extension <code>catalog</code>. */
	public static final String SUPERCATEGORIES = "supercategories";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MediaModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MediaModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Media</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 */
	@Deprecated
	public MediaModel(final CatalogVersionModel _catalogVersion, final String _code)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Media</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public MediaModel(final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.altText</code> attribute defined at extension <code>core</code>. 
	 * @return the altText
	 * @deprecated since ages - use { @link #getAltText()} instead
	 */
	@Deprecated
	public String getAlttext()
	{
		return this.getAltText();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.altText</code> attribute defined at extension <code>core</code>. 
	 * @return the altText
	 */
	@Accessor(qualifier = "altText", type = Accessor.Type.GETTER)
	public String getAltText()
	{
		return getPersistenceContext().getPropertyValue(ALTTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.catalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.code</code> attribute defined at extension <code>core</code>. 
	 * @return the code - Code of media
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.deniedPrincipals</code> dynamic attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the deniedPrincipals
	 */
	@Accessor(qualifier = "deniedPrincipals", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getDeniedPrincipals()
	{
		return getPersistenceContext().getDynamicValue(this,DENIEDPRINCIPALS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.derivedMedias</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the derivedMedias
	 */
	@Accessor(qualifier = "derivedMedias", type = Accessor.Type.GETTER)
	public Collection<DerivedMediaModel> getDerivedMedias()
	{
		return getPersistenceContext().getPropertyValue(DERIVEDMEDIAS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.description</code> attribute defined at extension <code>core</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getPersistenceContext().getPropertyValue(DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.downloadURL</code> dynamic attribute defined at extension <code>core</code>. 
	 * @return the downloadURL
	 * @deprecated since ages - use { @link #getDownloadURL()} instead
	 */
	@Deprecated
	public String getDownloadurl()
	{
		return this.getDownloadURL();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.downloadURL</code> dynamic attribute defined at extension <code>core</code>. 
	 * @return the downloadURL
	 */
	@Accessor(qualifier = "downloadURL", type = Accessor.Type.GETTER)
	public String getDownloadURL()
	{
		return getPersistenceContext().getDynamicValue(this,DOWNLOADURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.folder</code> attribute defined at extension <code>core</code>. 
	 * @return the folder - Sub folder where this media is stored.
	 */
	@Accessor(qualifier = "folder", type = Accessor.Type.GETTER)
	public MediaFolderModel getFolder()
	{
		return getPersistenceContext().getPropertyValue(FOLDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.foreignDataOwners</code> dynamic attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the foreignDataOwners - List of all medias referencing same data file.
	 */
	@Accessor(qualifier = "foreignDataOwners", type = Accessor.Type.GETTER)
	public Collection<MediaModel> getForeignDataOwners()
	{
		return getPersistenceContext().getDynamicValue(this,FOREIGNDATAOWNERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.internalURL</code> attribute defined at extension <code>core</code>. 
	 * @return the internalURL
	 */
	@Accessor(qualifier = "internalURL", type = Accessor.Type.GETTER)
	public String getInternalURL()
	{
		return getPersistenceContext().getPropertyValue(INTERNALURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.mediaContainer</code> attribute defined at extension <code>core</code>. 
	 * @return the mediaContainer
	 */
	@Accessor(qualifier = "mediaContainer", type = Accessor.Type.GETTER)
	public MediaContainerModel getMediaContainer()
	{
		return getPersistenceContext().getPropertyValue(MEDIACONTAINER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.mediaFormat</code> attribute defined at extension <code>core</code>. 
	 * @return the mediaFormat - Format of this media
	 */
	@Accessor(qualifier = "mediaFormat", type = Accessor.Type.GETTER)
	public MediaFormatModel getMediaFormat()
	{
		return getPersistenceContext().getPropertyValue(MEDIAFORMAT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.permittedPrincipals</code> dynamic attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the permittedPrincipals
	 */
	@Accessor(qualifier = "permittedPrincipals", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getPermittedPrincipals()
	{
		return getPersistenceContext().getDynamicValue(this,PERMITTEDPRINCIPALS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.removable</code> attribute defined at extension <code>core</code>. 
	 * @return the removable
	 */
	@Accessor(qualifier = "removable", type = Accessor.Type.GETTER)
	public Boolean getRemovable()
	{
		final Boolean value = getPersistenceContext().getPropertyValue(REMOVABLE);
		return value != null ? value : Boolean.valueOf(true);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.subFolderPath</code> attribute defined at extension <code>core</code>. 
	 * @return the subFolderPath - Generated location (by one of Storage Strategies) to media within storage.
	 */
	@Accessor(qualifier = "subFolderPath", type = Accessor.Type.GETTER)
	public String getSubFolderPath()
	{
		return getPersistenceContext().getPropertyValue(SUBFOLDERPATH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.supercategories</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the supercategories - Super Categories
	 */
	@Accessor(qualifier = "supercategories", type = Accessor.Type.GETTER)
	public Collection<CategoryModel> getSupercategories()
	{
		return getPersistenceContext().getPropertyValue(SUPERCATEGORIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.URL</code> dynamic attribute defined at extension <code>core</code>. 
	 * @return the URL
	 * @deprecated since ages - use { @link #getURL()} instead
	 */
	@Deprecated
	public String getUrl()
	{
		return this.getURL();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.URL</code> dynamic attribute defined at extension <code>core</code>. 
	 * @return the URL
	 */
	@Accessor(qualifier = "URL", type = Accessor.Type.GETTER)
	public String getURL()
	{
		return getPersistenceContext().getDynamicValue(this,URL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.URL2</code> attribute defined at extension <code>core</code>. 
	 * @return the URL2
	 * @deprecated since ages - use { @link #getURL2()} instead
	 */
	@Deprecated
	public String getUrl2()
	{
		return this.getURL2();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Media.URL2</code> attribute defined at extension <code>core</code>. 
	 * @return the URL2
	 */
	@Accessor(qualifier = "URL2", type = Accessor.Type.GETTER)
	public String getURL2()
	{
		return getPersistenceContext().getPropertyValue(URL2);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Media.altText</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the altText
	 * @deprecated since ages - use { @link #setAltText(java.lang.String)} instead
	 */
	@Deprecated
	public void setAlttext(final String value)
	{
		this.setAltText(value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Media.altText</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the altText
	 */
	@Accessor(qualifier = "altText", type = Accessor.Type.SETTER)
	public void setAltText(final String value)
	{
		getPersistenceContext().setPropertyValue(ALTTEXT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Media.catalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Media.code</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the code - Code of media
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Media.deniedPrincipals</code> dynamic attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the deniedPrincipals
	 */
	@Accessor(qualifier = "deniedPrincipals", type = Accessor.Type.SETTER)
	public void setDeniedPrincipals(final Collection<PrincipalModel> value)
	{
		getPersistenceContext().setDynamicValue(this,DENIEDPRINCIPALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Media.derivedMedias</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the derivedMedias
	 */
	@Accessor(qualifier = "derivedMedias", type = Accessor.Type.SETTER)
	public void setDerivedMedias(final Collection<DerivedMediaModel> value)
	{
		getPersistenceContext().setPropertyValue(DERIVEDMEDIAS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Media.description</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		getPersistenceContext().setPropertyValue(DESCRIPTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Media.folder</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the folder - Sub folder where this media is stored.
	 */
	@Accessor(qualifier = "folder", type = Accessor.Type.SETTER)
	public void setFolder(final MediaFolderModel value)
	{
		getPersistenceContext().setPropertyValue(FOLDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Media.internalURL</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the internalURL
	 */
	@Accessor(qualifier = "internalURL", type = Accessor.Type.SETTER)
	public void setInternalURL(final String value)
	{
		getPersistenceContext().setPropertyValue(INTERNALURL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Media.mediaContainer</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the mediaContainer
	 */
	@Accessor(qualifier = "mediaContainer", type = Accessor.Type.SETTER)
	public void setMediaContainer(final MediaContainerModel value)
	{
		getPersistenceContext().setPropertyValue(MEDIACONTAINER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Media.mediaFormat</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the mediaFormat - Format of this media
	 */
	@Accessor(qualifier = "mediaFormat", type = Accessor.Type.SETTER)
	public void setMediaFormat(final MediaFormatModel value)
	{
		getPersistenceContext().setPropertyValue(MEDIAFORMAT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Media.permittedPrincipals</code> dynamic attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the permittedPrincipals
	 */
	@Accessor(qualifier = "permittedPrincipals", type = Accessor.Type.SETTER)
	public void setPermittedPrincipals(final Collection<PrincipalModel> value)
	{
		getPersistenceContext().setDynamicValue(this,PERMITTEDPRINCIPALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Media.removable</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the removable
	 */
	@Accessor(qualifier = "removable", type = Accessor.Type.SETTER)
	public void setRemovable(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(REMOVABLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Media.subFolderPath</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the subFolderPath - Generated location (by one of Storage Strategies) to media within storage.
	 */
	@Accessor(qualifier = "subFolderPath", type = Accessor.Type.SETTER)
	public void setSubFolderPath(final String value)
	{
		getPersistenceContext().setPropertyValue(SUBFOLDERPATH, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Media.supercategories</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the supercategories - Super Categories
	 */
	@Accessor(qualifier = "supercategories", type = Accessor.Type.SETTER)
	public void setSupercategories(final Collection<CategoryModel> value)
	{
		getPersistenceContext().setPropertyValue(SUPERCATEGORIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Media.URL</code> dynamic attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the URL
	 * @deprecated since ages - use { @link #setURL(java.lang.String)} instead
	 */
	@Deprecated
	public void setUrl(final String value)
	{
		this.setURL(value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Media.URL</code> dynamic attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the URL
	 */
	@Accessor(qualifier = "URL", type = Accessor.Type.SETTER)
	public void setURL(final String value)
	{
		getPersistenceContext().setDynamicValue(this,URL, value);
	}
	
}
