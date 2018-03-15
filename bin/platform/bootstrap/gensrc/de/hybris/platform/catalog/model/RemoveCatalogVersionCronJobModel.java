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
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type RemoveCatalogVersionCronJob first defined at extension catalog.
 */
@SuppressWarnings("all")
public class RemoveCatalogVersionCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RemoveCatalogVersionCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveCatalogVersionCronJob.catalog</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOG = "catalog";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveCatalogVersionCronJob.catalogVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveCatalogVersionCronJob.dontRemoveObjects</code> attribute defined at extension <code>catalog</code>. */
	public static final String DONTREMOVEOBJECTS = "dontRemoveObjects";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveCatalogVersionCronJob.notRemovedItems</code> attribute defined at extension <code>catalog</code>. */
	public static final String NOTREMOVEDITEMS = "notRemovedItems";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveCatalogVersionCronJob.totalDeleteItemCount</code> attribute defined at extension <code>catalog</code>. */
	public static final String TOTALDELETEITEMCOUNT = "totalDeleteItemCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveCatalogVersionCronJob.currentProcessingItemCount</code> attribute defined at extension <code>catalog</code>. */
	public static final String CURRENTPROCESSINGITEMCOUNT = "currentProcessingItemCount";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RemoveCatalogVersionCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RemoveCatalogVersionCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalog initial attribute declared by type <code>RemoveCatalogVersionCronJob</code> at extension <code>catalog</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public RemoveCatalogVersionCronJobModel(final CatalogModel _catalog, final JobModel _job)
	{
		super();
		setCatalog(_catalog);
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalog initial attribute declared by type <code>RemoveCatalogVersionCronJob</code> at extension <code>catalog</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public RemoveCatalogVersionCronJobModel(final CatalogModel _catalog, final JobModel _job, final ItemModel _owner)
	{
		super();
		setCatalog(_catalog);
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveCatalogVersionCronJob.catalog</code> attribute defined at extension <code>catalog</code>. 
	 * @return the catalog
	 */
	@Accessor(qualifier = "catalog", type = Accessor.Type.GETTER)
	public CatalogModel getCatalog()
	{
		return getPersistenceContext().getPropertyValue(CATALOG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveCatalogVersionCronJob.catalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveCatalogVersionCronJob.currentProcessingItemCount</code> attribute defined at extension <code>catalog</code>. 
	 * @return the currentProcessingItemCount - Current processed(removed) item instance
	 */
	@Accessor(qualifier = "currentProcessingItemCount", type = Accessor.Type.GETTER)
	public Integer getCurrentProcessingItemCount()
	{
		return getPersistenceContext().getPropertyValue(CURRENTPROCESSINGITEMCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveCatalogVersionCronJob.dontRemoveObjects</code> attribute defined at extension <code>catalog</code>. 
	 * @return the dontRemoveObjects
	 */
	@Accessor(qualifier = "dontRemoveObjects", type = Accessor.Type.GETTER)
	public Boolean getDontRemoveObjects()
	{
		return getPersistenceContext().getPropertyValue(DONTREMOVEOBJECTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveCatalogVersionCronJob.notRemovedItems</code> attribute defined at extension <code>catalog</code>. 
	 * @return the notRemovedItems
	 */
	@Accessor(qualifier = "notRemovedItems", type = Accessor.Type.GETTER)
	public ImpExMediaModel getNotRemovedItems()
	{
		return getPersistenceContext().getPropertyValue(NOTREMOVEDITEMS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveCatalogVersionCronJob.totalDeleteItemCount</code> attribute defined at extension <code>catalog</code>. 
	 * @return the totalDeleteItemCount - Predicted count of items to remove
	 */
	@Accessor(qualifier = "totalDeleteItemCount", type = Accessor.Type.GETTER)
	public Integer getTotalDeleteItemCount()
	{
		return getPersistenceContext().getPropertyValue(TOTALDELETEITEMCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveCatalogVersionCronJob.catalog</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the catalog
	 */
	@Accessor(qualifier = "catalog", type = Accessor.Type.SETTER)
	public void setCatalog(final CatalogModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveCatalogVersionCronJob.catalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveCatalogVersionCronJob.currentProcessingItemCount</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the currentProcessingItemCount - Current processed(removed) item instance
	 */
	@Accessor(qualifier = "currentProcessingItemCount", type = Accessor.Type.SETTER)
	public void setCurrentProcessingItemCount(final Integer value)
	{
		getPersistenceContext().setPropertyValue(CURRENTPROCESSINGITEMCOUNT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveCatalogVersionCronJob.dontRemoveObjects</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the dontRemoveObjects
	 */
	@Accessor(qualifier = "dontRemoveObjects", type = Accessor.Type.SETTER)
	public void setDontRemoveObjects(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(DONTREMOVEOBJECTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveCatalogVersionCronJob.notRemovedItems</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the notRemovedItems
	 */
	@Accessor(qualifier = "notRemovedItems", type = Accessor.Type.SETTER)
	public void setNotRemovedItems(final ImpExMediaModel value)
	{
		getPersistenceContext().setPropertyValue(NOTREMOVEDITEMS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveCatalogVersionCronJob.totalDeleteItemCount</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the totalDeleteItemCount - Predicted count of items to remove
	 */
	@Accessor(qualifier = "totalDeleteItemCount", type = Accessor.Type.SETTER)
	public void setTotalDeleteItemCount(final Integer value)
	{
		getPersistenceContext().setPropertyValue(TOTALDELETEITEMCOUNT, value);
	}
	
}
