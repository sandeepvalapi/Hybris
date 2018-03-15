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
package de.hybris.platform.cms2.model.contents;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type ContentCatalog first defined at extension cms2.
 */
@SuppressWarnings("all")
public class ContentCatalogModel extends CatalogModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ContentCatalog";
	
	/**<i>Generated relation code constant for relation <code>CatalogsForCMSSite</code> defining source attribute <code>cmsSites</code> in extension <code>cms2</code>.</i>*/
	public static final String _CATALOGSFORCMSSITE = "CatalogsForCMSSite";
	
	/**<i>Generated relation code constant for relation <code>Catalog2CatalogRelation</code> defining source attribute <code>superCatalog</code> in extension <code>cms2</code>.</i>*/
	public static final String _CATALOG2CATALOGRELATION = "Catalog2CatalogRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>ContentCatalog.catalogLevelName</code> attribute defined at extension <code>cms2</code>. */
	public static final String CATALOGLEVELNAME = "catalogLevelName";
	
	/** <i>Generated constant</i> - Attribute key of <code>ContentCatalog.cmsSites</code> attribute defined at extension <code>cms2</code>. */
	public static final String CMSSITES = "cmsSites";
	
	/** <i>Generated constant</i> - Attribute key of <code>ContentCatalog.superCatalog</code> attribute defined at extension <code>cms2</code>. */
	public static final String SUPERCATALOG = "superCatalog";
	
	/** <i>Generated constant</i> - Attribute key of <code>ContentCatalog.subCatalogs</code> attribute defined at extension <code>cms2</code>. */
	public static final String SUBCATALOGS = "subCatalogs";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ContentCatalogModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ContentCatalogModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>Catalog</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ContentCatalogModel(final String _id)
	{
		super();
		setId(_id);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>Catalog</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ContentCatalogModel(final String _id, final ItemModel _owner)
	{
		super();
		setId(_id);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentCatalog.catalogLevelName</code> attribute defined at extension <code>cms2</code>. 
	 * @return the catalogLevelName
	 */
	@Accessor(qualifier = "catalogLevelName", type = Accessor.Type.GETTER)
	public String getCatalogLevelName()
	{
		return getCatalogLevelName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentCatalog.catalogLevelName</code> attribute defined at extension <code>cms2</code>. 
	 * @param loc the value localization key 
	 * @return the catalogLevelName
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "catalogLevelName", type = Accessor.Type.GETTER)
	public String getCatalogLevelName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(CATALOGLEVELNAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentCatalog.cmsSites</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the cmsSites
	 */
	@Accessor(qualifier = "cmsSites", type = Accessor.Type.GETTER)
	public Collection<CMSSiteModel> getCmsSites()
	{
		return getPersistenceContext().getPropertyValue(CMSSITES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentCatalog.subCatalogs</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the subCatalogs - Sub Catalogs
	 */
	@Accessor(qualifier = "subCatalogs", type = Accessor.Type.GETTER)
	public Set<ContentCatalogModel> getSubCatalogs()
	{
		return getPersistenceContext().getPropertyValue(SUBCATALOGS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentCatalog.superCatalog</code> attribute defined at extension <code>cms2</code>. 
	 * @return the superCatalog - Super Catalog
	 */
	@Accessor(qualifier = "superCatalog", type = Accessor.Type.GETTER)
	public ContentCatalogModel getSuperCatalog()
	{
		return getPersistenceContext().getPropertyValue(SUPERCATALOG);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ContentCatalog.catalogLevelName</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the catalogLevelName
	 */
	@Accessor(qualifier = "catalogLevelName", type = Accessor.Type.SETTER)
	public void setCatalogLevelName(final String value)
	{
		setCatalogLevelName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ContentCatalog.catalogLevelName</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the catalogLevelName
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "catalogLevelName", type = Accessor.Type.SETTER)
	public void setCatalogLevelName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(CATALOGLEVELNAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ContentCatalog.cmsSites</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the cmsSites
	 */
	@Accessor(qualifier = "cmsSites", type = Accessor.Type.SETTER)
	public void setCmsSites(final Collection<CMSSiteModel> value)
	{
		getPersistenceContext().setPropertyValue(CMSSITES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ContentCatalog.subCatalogs</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the subCatalogs - Sub Catalogs
	 */
	@Accessor(qualifier = "subCatalogs", type = Accessor.Type.SETTER)
	public void setSubCatalogs(final Set<ContentCatalogModel> value)
	{
		getPersistenceContext().setPropertyValue(SUBCATALOGS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ContentCatalog.superCatalog</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the superCatalog - Super Catalog
	 */
	@Accessor(qualifier = "superCatalog", type = Accessor.Type.SETTER)
	public void setSuperCatalog(final ContentCatalogModel value)
	{
		getPersistenceContext().setPropertyValue(SUPERCATALOG, value);
	}
	
}
