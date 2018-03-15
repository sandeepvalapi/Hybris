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
package de.hybris.platform.cms2.model.site;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorservices.model.SiteMapConfigModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Generated model class for type CMSSite first defined at extension cms2.
 */
@SuppressWarnings("all")
public class CMSSiteModel extends BaseSiteModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CMSSite";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.urlPatterns</code> attribute defined at extension <code>cms2</code>. */
	public static final String URLPATTERNS = "urlPatterns";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.active</code> attribute defined at extension <code>cms2</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.activeFrom</code> attribute defined at extension <code>cms2</code>. */
	public static final String ACTIVEFROM = "activeFrom";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.activeUntil</code> attribute defined at extension <code>cms2</code>. */
	public static final String ACTIVEUNTIL = "activeUntil";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.defaultCatalog</code> attribute defined at extension <code>cms2</code>. */
	public static final String DEFAULTCATALOG = "defaultCatalog";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.startingPage</code> attribute defined at extension <code>cms2</code>. */
	public static final String STARTINGPAGE = "startingPage";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.redirectURL</code> attribute defined at extension <code>cms2</code>. */
	public static final String REDIRECTURL = "redirectURL";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.previewURL</code> attribute defined at extension <code>cms2</code>. */
	public static final String PREVIEWURL = "previewURL";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.classificationCatalogs</code> attribute defined at extension <code>cms2</code>. */
	public static final String CLASSIFICATIONCATALOGS = "classificationCatalogs";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.productCatalogs</code> attribute defined at extension <code>cms2</code>. */
	public static final String PRODUCTCATALOGS = "productCatalogs";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.startPageLabel</code> attribute defined at extension <code>cms2</code>. */
	public static final String STARTPAGELABEL = "startPageLabel";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.openPreviewInNewTab</code> attribute defined at extension <code>cms2</code>. */
	public static final String OPENPREVIEWINNEWTAB = "openPreviewInNewTab";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.defaultPreviewCategory</code> attribute defined at extension <code>cms2</code>. */
	public static final String DEFAULTPREVIEWCATEGORY = "defaultPreviewCategory";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.defaultPreviewProduct</code> attribute defined at extension <code>cms2</code>. */
	public static final String DEFAULTPREVIEWPRODUCT = "defaultPreviewProduct";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.defaultPreviewCatalog</code> attribute defined at extension <code>cms2</code>. */
	public static final String DEFAULTPREVIEWCATALOG = "defaultPreviewCatalog";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.contentCatalogs</code> attribute defined at extension <code>cms2</code>. */
	public static final String CONTENTCATALOGS = "contentCatalogs";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.validComponentTypes</code> attribute defined at extension <code>cms2</code>. */
	public static final String VALIDCOMPONENTTYPES = "validComponentTypes";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.urlEncodingAttributes</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String URLENCODINGATTRIBUTES = "urlEncodingAttributes";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.siteMaps</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String SITEMAPS = "siteMaps";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSSite.siteMapConfig</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String SITEMAPCONFIG = "siteMapConfig";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CMSSiteModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CMSSiteModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _uid initial attribute declared by type <code>BaseSite</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public CMSSiteModel(final String _uid)
	{
		super();
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>BaseSite</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public CMSSiteModel(final ItemModel _owner, final String _uid)
	{
		super();
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.active</code> attribute defined at extension <code>cms2</code>. 
	 * @return the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public Boolean getActive()
	{
		return getPersistenceContext().getPropertyValue(ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.activeFrom</code> attribute defined at extension <code>cms2</code>. 
	 * @return the activeFrom
	 */
	@Accessor(qualifier = "activeFrom", type = Accessor.Type.GETTER)
	public Date getActiveFrom()
	{
		return getPersistenceContext().getPropertyValue(ACTIVEFROM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.activeUntil</code> attribute defined at extension <code>cms2</code>. 
	 * @return the activeUntil
	 */
	@Accessor(qualifier = "activeUntil", type = Accessor.Type.GETTER)
	public Date getActiveUntil()
	{
		return getPersistenceContext().getPropertyValue(ACTIVEUNTIL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.classificationCatalogs</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the classificationCatalogs
	 * @deprecated since 4.3
	 */
	@Deprecated
	@Accessor(qualifier = "classificationCatalogs", type = Accessor.Type.GETTER)
	public List<CatalogModel> getClassificationCatalogs()
	{
		return getPersistenceContext().getPropertyValue(CLASSIFICATIONCATALOGS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.contentCatalogs</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the contentCatalogs
	 */
	@Accessor(qualifier = "contentCatalogs", type = Accessor.Type.GETTER)
	public List<ContentCatalogModel> getContentCatalogs()
	{
		return getPersistenceContext().getPropertyValue(CONTENTCATALOGS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.defaultCatalog</code> attribute defined at extension <code>cms2</code>. 
	 * @return the defaultCatalog
	 */
	@Accessor(qualifier = "defaultCatalog", type = Accessor.Type.GETTER)
	public CatalogModel getDefaultCatalog()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTCATALOG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.defaultPreviewCatalog</code> attribute defined at extension <code>cms2</code>. 
	 * @return the defaultPreviewCatalog
	 */
	@Accessor(qualifier = "defaultPreviewCatalog", type = Accessor.Type.GETTER)
	public CatalogModel getDefaultPreviewCatalog()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTPREVIEWCATALOG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.defaultPreviewCategory</code> attribute defined at extension <code>cms2</code>. 
	 * @return the defaultPreviewCategory
	 */
	@Accessor(qualifier = "defaultPreviewCategory", type = Accessor.Type.GETTER)
	public CategoryModel getDefaultPreviewCategory()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTPREVIEWCATEGORY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.defaultPreviewProduct</code> attribute defined at extension <code>cms2</code>. 
	 * @return the defaultPreviewProduct
	 */
	@Accessor(qualifier = "defaultPreviewProduct", type = Accessor.Type.GETTER)
	public ProductModel getDefaultPreviewProduct()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTPREVIEWPRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.previewURL</code> attribute defined at extension <code>cms2</code>. 
	 * @return the previewURL
	 */
	@Accessor(qualifier = "previewURL", type = Accessor.Type.GETTER)
	public String getPreviewURL()
	{
		return getPersistenceContext().getPropertyValue(PREVIEWURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.productCatalogs</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the productCatalogs
	 * @deprecated since 4.3
	 */
	@Deprecated
	@Accessor(qualifier = "productCatalogs", type = Accessor.Type.GETTER)
	public List<CatalogModel> getProductCatalogs()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTCATALOGS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.redirectURL</code> attribute defined at extension <code>cms2</code>. 
	 * @return the redirectURL
	 */
	@Accessor(qualifier = "redirectURL", type = Accessor.Type.GETTER)
	public String getRedirectURL()
	{
		return getPersistenceContext().getPropertyValue(REDIRECTURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.siteMapConfig</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the siteMapConfig
	 */
	@Accessor(qualifier = "siteMapConfig", type = Accessor.Type.GETTER)
	public SiteMapConfigModel getSiteMapConfig()
	{
		return getPersistenceContext().getPropertyValue(SITEMAPCONFIG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.siteMaps</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the siteMaps
	 */
	@Accessor(qualifier = "siteMaps", type = Accessor.Type.GETTER)
	public Collection<MediaModel> getSiteMaps()
	{
		return getPersistenceContext().getPropertyValue(SITEMAPS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.startingPage</code> attribute defined at extension <code>cms2</code>. 
	 * @return the startingPage
	 */
	@Accessor(qualifier = "startingPage", type = Accessor.Type.GETTER)
	public ContentPageModel getStartingPage()
	{
		return getPersistenceContext().getPropertyValue(STARTINGPAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.startPageLabel</code> attribute defined at extension <code>cms2</code>. 
	 * @return the startPageLabel
	 * @deprecated since 4.3
	 */
	@Deprecated
	@Accessor(qualifier = "startPageLabel", type = Accessor.Type.GETTER)
	public String getStartPageLabel()
	{
		return getPersistenceContext().getPropertyValue(STARTPAGELABEL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.urlEncodingAttributes</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the urlEncodingAttributes
	 */
	@Accessor(qualifier = "urlEncodingAttributes", type = Accessor.Type.GETTER)
	public Collection<String> getUrlEncodingAttributes()
	{
		return getPersistenceContext().getPropertyValue(URLENCODINGATTRIBUTES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.urlPatterns</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the urlPatterns
	 */
	@Accessor(qualifier = "urlPatterns", type = Accessor.Type.GETTER)
	public Collection<String> getUrlPatterns()
	{
		return getPersistenceContext().getPropertyValue(URLPATTERNS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.validComponentTypes</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the validComponentTypes
	 */
	@Accessor(qualifier = "validComponentTypes", type = Accessor.Type.GETTER)
	public Set<CMSComponentTypeModel> getValidComponentTypes()
	{
		return getPersistenceContext().getPropertyValue(VALIDCOMPONENTTYPES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSSite.openPreviewInNewTab</code> attribute defined at extension <code>cms2</code>. 
	 * @return the openPreviewInNewTab
	 */
	@Accessor(qualifier = "openPreviewInNewTab", type = Accessor.Type.GETTER)
	public boolean isOpenPreviewInNewTab()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(OPENPREVIEWINNEWTAB));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSSite.active</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSSite.activeFrom</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the activeFrom
	 */
	@Accessor(qualifier = "activeFrom", type = Accessor.Type.SETTER)
	public void setActiveFrom(final Date value)
	{
		getPersistenceContext().setPropertyValue(ACTIVEFROM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSSite.activeUntil</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the activeUntil
	 */
	@Accessor(qualifier = "activeUntil", type = Accessor.Type.SETTER)
	public void setActiveUntil(final Date value)
	{
		getPersistenceContext().setPropertyValue(ACTIVEUNTIL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSSite.contentCatalogs</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the contentCatalogs
	 */
	@Accessor(qualifier = "contentCatalogs", type = Accessor.Type.SETTER)
	public void setContentCatalogs(final List<ContentCatalogModel> value)
	{
		getPersistenceContext().setPropertyValue(CONTENTCATALOGS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSSite.defaultCatalog</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the defaultCatalog
	 */
	@Accessor(qualifier = "defaultCatalog", type = Accessor.Type.SETTER)
	public void setDefaultCatalog(final CatalogModel value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTCATALOG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSSite.defaultPreviewCatalog</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the defaultPreviewCatalog
	 */
	@Accessor(qualifier = "defaultPreviewCatalog", type = Accessor.Type.SETTER)
	public void setDefaultPreviewCatalog(final CatalogModel value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTPREVIEWCATALOG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSSite.defaultPreviewCategory</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the defaultPreviewCategory
	 */
	@Accessor(qualifier = "defaultPreviewCategory", type = Accessor.Type.SETTER)
	public void setDefaultPreviewCategory(final CategoryModel value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTPREVIEWCATEGORY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSSite.defaultPreviewProduct</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the defaultPreviewProduct
	 */
	@Accessor(qualifier = "defaultPreviewProduct", type = Accessor.Type.SETTER)
	public void setDefaultPreviewProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTPREVIEWPRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSSite.openPreviewInNewTab</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the openPreviewInNewTab
	 */
	@Accessor(qualifier = "openPreviewInNewTab", type = Accessor.Type.SETTER)
	public void setOpenPreviewInNewTab(final boolean value)
	{
		getPersistenceContext().setPropertyValue(OPENPREVIEWINNEWTAB, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSSite.previewURL</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the previewURL
	 */
	@Accessor(qualifier = "previewURL", type = Accessor.Type.SETTER)
	public void setPreviewURL(final String value)
	{
		getPersistenceContext().setPropertyValue(PREVIEWURL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSSite.redirectURL</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the redirectURL
	 */
	@Accessor(qualifier = "redirectURL", type = Accessor.Type.SETTER)
	public void setRedirectURL(final String value)
	{
		getPersistenceContext().setPropertyValue(REDIRECTURL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSSite.siteMapConfig</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the siteMapConfig
	 */
	@Accessor(qualifier = "siteMapConfig", type = Accessor.Type.SETTER)
	public void setSiteMapConfig(final SiteMapConfigModel value)
	{
		getPersistenceContext().setPropertyValue(SITEMAPCONFIG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSSite.siteMaps</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the siteMaps
	 */
	@Accessor(qualifier = "siteMaps", type = Accessor.Type.SETTER)
	public void setSiteMaps(final Collection<MediaModel> value)
	{
		getPersistenceContext().setPropertyValue(SITEMAPS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSSite.startingPage</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the startingPage
	 */
	@Accessor(qualifier = "startingPage", type = Accessor.Type.SETTER)
	public void setStartingPage(final ContentPageModel value)
	{
		getPersistenceContext().setPropertyValue(STARTINGPAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSSite.urlEncodingAttributes</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the urlEncodingAttributes
	 */
	@Accessor(qualifier = "urlEncodingAttributes", type = Accessor.Type.SETTER)
	public void setUrlEncodingAttributes(final Collection<String> value)
	{
		getPersistenceContext().setPropertyValue(URLENCODINGATTRIBUTES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSSite.urlPatterns</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the urlPatterns
	 */
	@Accessor(qualifier = "urlPatterns", type = Accessor.Type.SETTER)
	public void setUrlPatterns(final Collection<String> value)
	{
		getPersistenceContext().setPropertyValue(URLPATTERNS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSSite.validComponentTypes</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the validComponentTypes
	 */
	@Accessor(qualifier = "validComponentTypes", type = Accessor.Type.SETTER)
	public void setValidComponentTypes(final Set<CMSComponentTypeModel> value)
	{
		getPersistenceContext().setPropertyValue(VALIDCOMPONENTTYPES, value);
	}
	
}
