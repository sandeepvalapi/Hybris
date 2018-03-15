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
package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type Keyword first defined at extension catalog.
 */
@SuppressWarnings("all")
public class KeywordModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Keyword";
	
	/**<i>Generated relation code constant for relation <code>Category2KeywordRelation</code> defining source attribute <code>categories</code> in extension <code>catalog</code>.</i>*/
	public static final String _CATEGORY2KEYWORDRELATION = "Category2KeywordRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Keyword.keyword</code> attribute defined at extension <code>catalog</code>. */
	public static final String KEYWORD = "keyword";
	
	/** <i>Generated constant</i> - Attribute key of <code>Keyword.language</code> attribute defined at extension <code>catalog</code>. */
	public static final String LANGUAGE = "language";
	
	/** <i>Generated constant</i> - Attribute key of <code>Keyword.catalog</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOG = "catalog";
	
	/** <i>Generated constant</i> - Attribute key of <code>Keyword.catalogVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>Keyword.products</code> attribute defined at extension <code>catalog</code>. */
	public static final String PRODUCTS = "products";
	
	/** <i>Generated constant</i> - Attribute key of <code>Keyword.categories</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATEGORIES = "categories";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public KeywordModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public KeywordModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Keyword</code> at extension <code>catalog</code>
	 * @param _keyword initial attribute declared by type <code>Keyword</code> at extension <code>catalog</code>
	 * @param _language initial attribute declared by type <code>Keyword</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public KeywordModel(final CatalogVersionModel _catalogVersion, final String _keyword, final LanguageModel _language)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setKeyword(_keyword);
		setLanguage(_language);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Keyword</code> at extension <code>catalog</code>
	 * @param _keyword initial attribute declared by type <code>Keyword</code> at extension <code>catalog</code>
	 * @param _language initial attribute declared by type <code>Keyword</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public KeywordModel(final CatalogVersionModel _catalogVersion, final String _keyword, final LanguageModel _language, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setKeyword(_keyword);
		setLanguage(_language);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Keyword.catalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Keyword.categories</code> attribute defined at extension <code>catalog</code>. 
	 * @return the categories - Categories
	 */
	@Accessor(qualifier = "categories", type = Accessor.Type.GETTER)
	public Collection<CategoryModel> getCategories()
	{
		return getCategories(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Keyword.categories</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the categories - Categories
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "categories", type = Accessor.Type.GETTER)
	public Collection<CategoryModel> getCategories(final Locale loc)
	{
		return getPersistenceContext().getLocalizedRelationValue(CATEGORIES, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Keyword.keyword</code> attribute defined at extension <code>catalog</code>. 
	 * @return the keyword
	 */
	@Accessor(qualifier = "keyword", type = Accessor.Type.GETTER)
	public String getKeyword()
	{
		return getPersistenceContext().getPropertyValue(KEYWORD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Keyword.language</code> attribute defined at extension <code>catalog</code>. 
	 * @return the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public LanguageModel getLanguage()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Keyword.products</code> attribute defined at extension <code>catalog</code>. 
	 * @return the products - Products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.GETTER)
	public Collection<ProductModel> getProducts()
	{
		return getProducts(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Keyword.products</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the products - Products
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.GETTER)
	public Collection<ProductModel> getProducts(final Locale loc)
	{
		return getPersistenceContext().getLocalizedRelationValue(PRODUCTS, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Keyword.catalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Keyword.categories</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the categories - Categories
	 */
	@Accessor(qualifier = "categories", type = Accessor.Type.SETTER)
	public void setCategories(final Collection<CategoryModel> value)
	{
		setCategories(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Keyword.categories</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the categories - Categories
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "categories", type = Accessor.Type.SETTER)
	public void setCategories(final Collection<CategoryModel> value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(CATEGORIES, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Keyword.keyword</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the keyword
	 */
	@Accessor(qualifier = "keyword", type = Accessor.Type.SETTER)
	public void setKeyword(final String value)
	{
		getPersistenceContext().setPropertyValue(KEYWORD, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Keyword.language</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Keyword.products</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the products - Products
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.SETTER)
	public void setProducts(final Collection<ProductModel> value)
	{
		setProducts(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Keyword.products</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the products - Products
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "products", type = Accessor.Type.SETTER)
	public void setProducts(final Collection<ProductModel> value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(PRODUCTS, loc, value);
	}
	
}
