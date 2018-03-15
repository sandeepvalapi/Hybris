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
package de.hybris.platform.catalog.model.classification;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ClassificationSystemVersion first defined at extension catalog.
 */
@SuppressWarnings("all")
public class ClassificationSystemVersionModel extends CatalogVersionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ClassificationSystemVersion";
	
	/**<i>Generated relation code constant for relation <code>Catalog2VersionsRelation</code> defining source attribute <code>catalog</code> in extension <code>catalog</code>.</i>*/
	public static final String _CATALOG2VERSIONSRELATION = "Catalog2VersionsRelation";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ClassificationSystemVersionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ClassificationSystemVersionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalog initial attribute declared by type <code>ClassificationSystemVersion</code> at extension <code>catalog</code>
	 * @param _version initial attribute declared by type <code>CatalogVersion</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ClassificationSystemVersionModel(final ClassificationSystemModel _catalog, final String _version)
	{
		super();
		setCatalog(_catalog);
		setVersion(_version);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalog initial attribute declared by type <code>ClassificationSystemVersion</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _version initial attribute declared by type <code>CatalogVersion</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ClassificationSystemVersionModel(final ClassificationSystemModel _catalog, final ItemModel _owner, final String _version)
	{
		super();
		setCatalog(_catalog);
		setOwner(_owner);
		setVersion(_version);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.catalog</code> attribute defined at extension <code>catalog</code> and redeclared at extension <code>catalog</code>. 
	 * @return the catalog - the classification system
	 */
	@Override
	@Accessor(qualifier = "catalog", type = Accessor.Type.GETTER)
	public ClassificationSystemModel getCatalog()
	{
		return (ClassificationSystemModel) super.getCatalog();
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CatalogVersion.catalog</code> attribute defined at extension <code>catalog</code> and redeclared at extension <code>catalog</code>. Can only be used at creation of model - before first save. Will only accept values of type {@link de.hybris.platform.catalog.model.classification.ClassificationSystemModel}.  
	 *  
	 * @param value the catalog - the classification system
	 */
	@Override
	@Accessor(qualifier = "catalog", type = Accessor.Type.SETTER)
	public void setCatalog(final CatalogModel value)
	{
		if( value == null || value instanceof ClassificationSystemModel)
		{
			super.setCatalog(value);
		}
		else
		{
			throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.catalog.model.classification.ClassificationSystemModel");
		}
	}
	
}
