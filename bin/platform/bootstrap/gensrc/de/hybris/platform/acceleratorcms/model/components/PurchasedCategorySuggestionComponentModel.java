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
package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorcms.model.components.SimpleSuggestionComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type PurchasedCategorySuggestionComponent first defined at extension acceleratorcms.
 * <p>
 * Represents the suggestion component that displays product references that can be filtered by purchased or from specified category.
 */
@SuppressWarnings("all")
public class PurchasedCategorySuggestionComponentModel extends SimpleSuggestionComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PurchasedCategorySuggestionComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>PurchasedCategorySuggestionComponent.category</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String CATEGORY = "category";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PurchasedCategorySuggestionComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PurchasedCategorySuggestionComponentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _category initial attribute declared by type <code>PurchasedCategorySuggestionComponent</code> at extension <code>acceleratorcms</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public PurchasedCategorySuggestionComponentModel(final CatalogVersionModel _catalogVersion, final CategoryModel _category, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCategory(_category);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _category initial attribute declared by type <code>PurchasedCategorySuggestionComponent</code> at extension <code>acceleratorcms</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public PurchasedCategorySuggestionComponentModel(final CatalogVersionModel _catalogVersion, final CategoryModel _category, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCategory(_category);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PurchasedCategorySuggestionComponent.category</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the category - Category to filter displayed references with.
	 */
	@Accessor(qualifier = "category", type = Accessor.Type.GETTER)
	public CategoryModel getCategory()
	{
		return getPersistenceContext().getPropertyValue(CATEGORY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PurchasedCategorySuggestionComponent.category</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the category - Category to filter displayed references with.
	 */
	@Accessor(qualifier = "category", type = Accessor.Type.SETTER)
	public void setCategory(final CategoryModel value)
	{
		getPersistenceContext().setPropertyValue(CATEGORY, value);
	}
	
}
