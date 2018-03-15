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
import de.hybris.platform.catalog.enums.CategoryDifferenceMode;
import de.hybris.platform.catalog.model.CatalogVersionDifferenceModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.CompareCatalogVersionsCronJobModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CategoryCatalogVersionDifference first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CategoryCatalogVersionDifferenceModel extends CatalogVersionDifferenceModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CategoryCatalogVersionDifference";
	
	/** <i>Generated constant</i> - Attribute key of <code>CategoryCatalogVersionDifference.sourceCategory</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCECATEGORY = "sourceCategory";
	
	/** <i>Generated constant</i> - Attribute key of <code>CategoryCatalogVersionDifference.targetCategory</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETCATEGORY = "targetCategory";
	
	/** <i>Generated constant</i> - Attribute key of <code>CategoryCatalogVersionDifference.mode</code> attribute defined at extension <code>catalog</code>. */
	public static final String MODE = "mode";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CategoryCatalogVersionDifferenceModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CategoryCatalogVersionDifferenceModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _cronJob initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _mode initial attribute declared by type <code>CategoryCatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CategoryCatalogVersionDifferenceModel(final CompareCatalogVersionsCronJobModel _cronJob, final CategoryDifferenceMode _mode, final CatalogVersionModel _sourceVersion, final CatalogVersionModel _targetVersion)
	{
		super();
		setCronJob(_cronJob);
		setMode(_mode);
		setSourceVersion(_sourceVersion);
		setTargetVersion(_targetVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _cronJob initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _mode initial attribute declared by type <code>CategoryCatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sourceCategory initial attribute declared by type <code>CategoryCatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _targetCategory initial attribute declared by type <code>CategoryCatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CategoryCatalogVersionDifferenceModel(final CompareCatalogVersionsCronJobModel _cronJob, final CategoryDifferenceMode _mode, final ItemModel _owner, final CategoryModel _sourceCategory, final CatalogVersionModel _sourceVersion, final CategoryModel _targetCategory, final CatalogVersionModel _targetVersion)
	{
		super();
		setCronJob(_cronJob);
		setMode(_mode);
		setOwner(_owner);
		setSourceCategory(_sourceCategory);
		setSourceVersion(_sourceVersion);
		setTargetCategory(_targetCategory);
		setTargetVersion(_targetVersion);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CategoryCatalogVersionDifference.mode</code> attribute defined at extension <code>catalog</code>. 
	 * @return the mode
	 */
	@Accessor(qualifier = "mode", type = Accessor.Type.GETTER)
	public CategoryDifferenceMode getMode()
	{
		return getPersistenceContext().getPropertyValue(MODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CategoryCatalogVersionDifference.sourceCategory</code> attribute defined at extension <code>catalog</code>. 
	 * @return the sourceCategory
	 */
	@Accessor(qualifier = "sourceCategory", type = Accessor.Type.GETTER)
	public CategoryModel getSourceCategory()
	{
		return getPersistenceContext().getPropertyValue(SOURCECATEGORY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CategoryCatalogVersionDifference.targetCategory</code> attribute defined at extension <code>catalog</code>. 
	 * @return the targetCategory
	 */
	@Accessor(qualifier = "targetCategory", type = Accessor.Type.GETTER)
	public CategoryModel getTargetCategory()
	{
		return getPersistenceContext().getPropertyValue(TARGETCATEGORY);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CategoryCatalogVersionDifference.mode</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the mode
	 */
	@Accessor(qualifier = "mode", type = Accessor.Type.SETTER)
	public void setMode(final CategoryDifferenceMode value)
	{
		getPersistenceContext().setPropertyValue(MODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CategoryCatalogVersionDifference.sourceCategory</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sourceCategory
	 */
	@Accessor(qualifier = "sourceCategory", type = Accessor.Type.SETTER)
	public void setSourceCategory(final CategoryModel value)
	{
		getPersistenceContext().setPropertyValue(SOURCECATEGORY, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CategoryCatalogVersionDifference.targetCategory</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the targetCategory
	 */
	@Accessor(qualifier = "targetCategory", type = Accessor.Type.SETTER)
	public void setTargetCategory(final CategoryModel value)
	{
		getPersistenceContext().setPropertyValue(TARGETCATEGORY, value);
	}
	
}
