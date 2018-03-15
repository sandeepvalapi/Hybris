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
package de.hybris.platform.cms2.model.contents.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.enums.LinkTargets;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type CMSLinkComponent first defined at extension cms2.
 */
@SuppressWarnings("all")
public class CMSLinkComponentModel extends SimpleCMSComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CMSLinkComponent";
	
	/**<i>Generated relation code constant for relation <code>CmsLinkComponentsForContentPage</code> defining source attribute <code>contentPage</code> in extension <code>cms2</code>.</i>*/
	public static final String _CMSLINKCOMPONENTSFORCONTENTPAGE = "CmsLinkComponentsForContentPage";
	
	/**<i>Generated relation code constant for relation <code>CmsLinkComponentsForProduct</code> defining source attribute <code>product</code> in extension <code>cms2</code>.</i>*/
	public static final String _CMSLINKCOMPONENTSFORPRODUCT = "CmsLinkComponentsForProduct";
	
	/**<i>Generated relation code constant for relation <code>CmsLinkComponentsForCategory</code> defining source attribute <code>category</code> in extension <code>cms2</code>.</i>*/
	public static final String _CMSLINKCOMPONENTSFORCATEGORY = "CmsLinkComponentsForCategory";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSLinkComponent.linkName</code> attribute defined at extension <code>cms2</code>. */
	public static final String LINKNAME = "linkName";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSLinkComponent.external</code> attribute defined at extension <code>cms2</code>. */
	public static final String EXTERNAL = "external";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSLinkComponent.url</code> attribute defined at extension <code>cms2</code>. */
	public static final String URL = "url";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSLinkComponent.contentPageLabelOrId</code> attribute defined at extension <code>cms2</code>. */
	public static final String CONTENTPAGELABELORID = "contentPageLabelOrId";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSLinkComponent.productCode</code> attribute defined at extension <code>cms2</code>. */
	public static final String PRODUCTCODE = "productCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSLinkComponent.categoryCode</code> attribute defined at extension <code>cms2</code>. */
	public static final String CATEGORYCODE = "categoryCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSLinkComponent.target</code> attribute defined at extension <code>cms2</code>. */
	public static final String TARGET = "target";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSLinkComponent.navigationNodes</code> attribute defined at extension <code>cms2</code>. */
	public static final String NAVIGATIONNODES = "navigationNodes";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSLinkComponent.contentPagePOS</code> attribute defined at extension <code>cms2</code>. */
	public static final String CONTENTPAGEPOS = "contentPagePOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSLinkComponent.contentPage</code> attribute defined at extension <code>cms2</code>. */
	public static final String CONTENTPAGE = "contentPage";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSLinkComponent.productPOS</code> attribute defined at extension <code>cms2</code>. */
	public static final String PRODUCTPOS = "productPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSLinkComponent.product</code> attribute defined at extension <code>cms2</code>. */
	public static final String PRODUCT = "product";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSLinkComponent.categoryPOS</code> attribute defined at extension <code>cms2</code>. */
	public static final String CATEGORYPOS = "categoryPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSLinkComponent.category</code> attribute defined at extension <code>cms2</code>. */
	public static final String CATEGORY = "category";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSLinkComponent.styleAttributes</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String STYLEATTRIBUTES = "styleAttributes";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CMSLinkComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CMSLinkComponentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public CMSLinkComponentModel(final CatalogVersionModel _catalogVersion, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public CMSLinkComponentModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.category</code> attribute defined at extension <code>cms2</code>. 
	 * @return the category
	 */
	@Accessor(qualifier = "category", type = Accessor.Type.GETTER)
	public CategoryModel getCategory()
	{
		return getPersistenceContext().getPropertyValue(CATEGORY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.categoryCode</code> attribute defined at extension <code>cms2</code>. 
	 * @return the categoryCode
	 * @deprecated since 4.3
	 */
	@Deprecated
	@Accessor(qualifier = "categoryCode", type = Accessor.Type.GETTER)
	public String getCategoryCode()
	{
		return getPersistenceContext().getPropertyValue(CATEGORYCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.contentPage</code> attribute defined at extension <code>cms2</code>. 
	 * @return the contentPage
	 */
	@Accessor(qualifier = "contentPage", type = Accessor.Type.GETTER)
	public ContentPageModel getContentPage()
	{
		return getPersistenceContext().getPropertyValue(CONTENTPAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.contentPageLabelOrId</code> attribute defined at extension <code>cms2</code>. 
	 * @return the contentPageLabelOrId
	 * @deprecated since 4.3
	 */
	@Deprecated
	@Accessor(qualifier = "contentPageLabelOrId", type = Accessor.Type.GETTER)
	public String getContentPageLabelOrId()
	{
		return getPersistenceContext().getPropertyValue(CONTENTPAGELABELORID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.linkName</code> attribute defined at extension <code>cms2</code>. 
	 * @return the linkName
	 */
	@Accessor(qualifier = "linkName", type = Accessor.Type.GETTER)
	public String getLinkName()
	{
		return getLinkName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.linkName</code> attribute defined at extension <code>cms2</code>. 
	 * @param loc the value localization key 
	 * @return the linkName
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "linkName", type = Accessor.Type.GETTER)
	public String getLinkName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(LINKNAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.navigationNodes</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the navigationNodes
	 * @deprecated since 4.4
	 */
	@Deprecated
	@Accessor(qualifier = "navigationNodes", type = Accessor.Type.GETTER)
	public List<CMSNavigationNodeModel> getNavigationNodes()
	{
		return getPersistenceContext().getPropertyValue(NAVIGATIONNODES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.product</code> attribute defined at extension <code>cms2</code>. 
	 * @return the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.GETTER)
	public ProductModel getProduct()
	{
		return getPersistenceContext().getPropertyValue(PRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.productCode</code> attribute defined at extension <code>cms2</code>. 
	 * @return the productCode
	 * @deprecated since 4.3
	 */
	@Deprecated
	@Accessor(qualifier = "productCode", type = Accessor.Type.GETTER)
	public String getProductCode()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.styleAttributes</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the styleAttributes - Can store custom styling attributes for additional styling
	 */
	@Accessor(qualifier = "styleAttributes", type = Accessor.Type.GETTER)
	public String getStyleAttributes()
	{
		return getPersistenceContext().getPropertyValue(STYLEATTRIBUTES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.target</code> attribute defined at extension <code>cms2</code>. 
	 * @return the target
	 */
	@Accessor(qualifier = "target", type = Accessor.Type.GETTER)
	public LinkTargets getTarget()
	{
		return getPersistenceContext().getPropertyValue(TARGET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.url</code> attribute defined at extension <code>cms2</code>. 
	 * @return the url
	 */
	@Accessor(qualifier = "url", type = Accessor.Type.GETTER)
	public String getUrl()
	{
		return getPersistenceContext().getPropertyValue(URL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.external</code> attribute defined at extension <code>cms2</code>. 
	 * @return the external
	 */
	@Accessor(qualifier = "external", type = Accessor.Type.GETTER)
	public boolean isExternal()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(EXTERNAL));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSLinkComponent.category</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the category
	 */
	@Accessor(qualifier = "category", type = Accessor.Type.SETTER)
	public void setCategory(final CategoryModel value)
	{
		getPersistenceContext().setPropertyValue(CATEGORY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSLinkComponent.contentPage</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the contentPage
	 */
	@Accessor(qualifier = "contentPage", type = Accessor.Type.SETTER)
	public void setContentPage(final ContentPageModel value)
	{
		getPersistenceContext().setPropertyValue(CONTENTPAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSLinkComponent.external</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the external
	 */
	@Accessor(qualifier = "external", type = Accessor.Type.SETTER)
	public void setExternal(final boolean value)
	{
		getPersistenceContext().setPropertyValue(EXTERNAL, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSLinkComponent.linkName</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the linkName
	 */
	@Accessor(qualifier = "linkName", type = Accessor.Type.SETTER)
	public void setLinkName(final String value)
	{
		setLinkName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>CMSLinkComponent.linkName</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the linkName
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "linkName", type = Accessor.Type.SETTER)
	public void setLinkName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(LINKNAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSLinkComponent.navigationNodes</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the navigationNodes
	 * @deprecated since 4.4
	 */
	@Deprecated
	@Accessor(qualifier = "navigationNodes", type = Accessor.Type.SETTER)
	public void setNavigationNodes(final List<CMSNavigationNodeModel> value)
	{
		getPersistenceContext().setPropertyValue(NAVIGATIONNODES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSLinkComponent.product</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.SETTER)
	public void setProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(PRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSLinkComponent.styleAttributes</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the styleAttributes - Can store custom styling attributes for additional styling
	 */
	@Accessor(qualifier = "styleAttributes", type = Accessor.Type.SETTER)
	public void setStyleAttributes(final String value)
	{
		getPersistenceContext().setPropertyValue(STYLEATTRIBUTES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSLinkComponent.target</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the target
	 */
	@Accessor(qualifier = "target", type = Accessor.Type.SETTER)
	public void setTarget(final LinkTargets value)
	{
		getPersistenceContext().setPropertyValue(TARGET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSLinkComponent.url</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the url
	 */
	@Accessor(qualifier = "url", type = Accessor.Type.SETTER)
	public void setUrl(final String value)
	{
		getPersistenceContext().setPropertyValue(URL, value);
	}
	
}
