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
package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type RemoveItemsCronJob first defined at extension processing.
 */
@SuppressWarnings("all")
public class RemoveItemsCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RemoveItemsCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveItemsCronJob.itemPKs</code> attribute defined at extension <code>processing</code>. */
	public static final String ITEMPKS = "itemPKs";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveItemsCronJob.itemsFound</code> attribute defined at extension <code>processing</code>. */
	public static final String ITEMSFOUND = "itemsFound";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveItemsCronJob.itemsDeleted</code> attribute defined at extension <code>processing</code>. */
	public static final String ITEMSDELETED = "itemsDeleted";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveItemsCronJob.itemsRefused</code> attribute defined at extension <code>processing</code>. */
	public static final String ITEMSREFUSED = "itemsRefused";
	
	/** <i>Generated constant</i> - Attribute key of <code>RemoveItemsCronJob.createSavedValues</code> attribute defined at extension <code>processing</code>. */
	public static final String CREATESAVEDVALUES = "createSavedValues";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RemoveItemsCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RemoveItemsCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _itemPKs initial attribute declared by type <code>RemoveItemsCronJob</code> at extension <code>processing</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public RemoveItemsCronJobModel(final MediaModel _itemPKs, final JobModel _job)
	{
		super();
		setItemPKs(_itemPKs);
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _itemPKs initial attribute declared by type <code>RemoveItemsCronJob</code> at extension <code>processing</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public RemoveItemsCronJobModel(final MediaModel _itemPKs, final JobModel _job, final ItemModel _owner)
	{
		super();
		setItemPKs(_itemPKs);
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveItemsCronJob.createSavedValues</code> attribute defined at extension <code>processing</code>. 
	 * @return the createSavedValues
	 */
	@Accessor(qualifier = "createSavedValues", type = Accessor.Type.GETTER)
	public Boolean getCreateSavedValues()
	{
		return getPersistenceContext().getPropertyValue(CREATESAVEDVALUES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveItemsCronJob.itemPKs</code> attribute defined at extension <code>processing</code>. 
	 * @return the itemPKs
	 */
	@Accessor(qualifier = "itemPKs", type = Accessor.Type.GETTER)
	public MediaModel getItemPKs()
	{
		return getPersistenceContext().getPropertyValue(ITEMPKS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveItemsCronJob.itemsDeleted</code> attribute defined at extension <code>processing</code>. 
	 * @return the itemsDeleted
	 */
	@Accessor(qualifier = "itemsDeleted", type = Accessor.Type.GETTER)
	public Integer getItemsDeleted()
	{
		return getPersistenceContext().getPropertyValue(ITEMSDELETED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveItemsCronJob.itemsFound</code> attribute defined at extension <code>processing</code>. 
	 * @return the itemsFound
	 */
	@Accessor(qualifier = "itemsFound", type = Accessor.Type.GETTER)
	public Integer getItemsFound()
	{
		return getPersistenceContext().getPropertyValue(ITEMSFOUND);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RemoveItemsCronJob.itemsRefused</code> attribute defined at extension <code>processing</code>. 
	 * @return the itemsRefused
	 */
	@Accessor(qualifier = "itemsRefused", type = Accessor.Type.GETTER)
	public Integer getItemsRefused()
	{
		return getPersistenceContext().getPropertyValue(ITEMSREFUSED);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveItemsCronJob.createSavedValues</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the createSavedValues
	 */
	@Accessor(qualifier = "createSavedValues", type = Accessor.Type.SETTER)
	public void setCreateSavedValues(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(CREATESAVEDVALUES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveItemsCronJob.itemPKs</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the itemPKs
	 */
	@Accessor(qualifier = "itemPKs", type = Accessor.Type.SETTER)
	public void setItemPKs(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(ITEMPKS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveItemsCronJob.itemsDeleted</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the itemsDeleted
	 */
	@Accessor(qualifier = "itemsDeleted", type = Accessor.Type.SETTER)
	public void setItemsDeleted(final Integer value)
	{
		getPersistenceContext().setPropertyValue(ITEMSDELETED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveItemsCronJob.itemsFound</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the itemsFound
	 */
	@Accessor(qualifier = "itemsFound", type = Accessor.Type.SETTER)
	public void setItemsFound(final Integer value)
	{
		getPersistenceContext().setPropertyValue(ITEMSFOUND, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RemoveItemsCronJob.itemsRefused</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the itemsRefused
	 */
	@Accessor(qualifier = "itemsRefused", type = Accessor.Type.SETTER)
	public void setItemsRefused(final Integer value)
	{
		getPersistenceContext().setPropertyValue(ITEMSREFUSED, value);
	}
	
}
