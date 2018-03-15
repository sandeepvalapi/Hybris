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
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type MediaFolderStructureMigrationCronJob first defined at extension processing.
 */
@SuppressWarnings("all")
public class MediaFolderStructureMigrationCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MediaFolderStructureMigrationCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaFolderStructureMigrationCronJob.mediaFolder</code> attribute defined at extension <code>processing</code>. */
	public static final String MEDIAFOLDER = "mediaFolder";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MediaFolderStructureMigrationCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MediaFolderStructureMigrationCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _mediaFolder initial attribute declared by type <code>MediaFolderStructureMigrationCronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public MediaFolderStructureMigrationCronJobModel(final JobModel _job, final MediaFolderModel _mediaFolder)
	{
		super();
		setJob(_job);
		setMediaFolder(_mediaFolder);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _mediaFolder initial attribute declared by type <code>MediaFolderStructureMigrationCronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public MediaFolderStructureMigrationCronJobModel(final JobModel _job, final MediaFolderModel _mediaFolder, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setMediaFolder(_mediaFolder);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MediaFolderStructureMigrationCronJob.mediaFolder</code> attribute defined at extension <code>processing</code>. 
	 * @return the mediaFolder - Folder for which migration job has to be performed
	 */
	@Accessor(qualifier = "mediaFolder", type = Accessor.Type.GETTER)
	public MediaFolderModel getMediaFolder()
	{
		return getPersistenceContext().getPropertyValue(MEDIAFOLDER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MediaFolderStructureMigrationCronJob.mediaFolder</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the mediaFolder - Folder for which migration job has to be performed
	 */
	@Accessor(qualifier = "mediaFolder", type = Accessor.Type.SETTER)
	public void setMediaFolder(final MediaFolderModel value)
	{
		getPersistenceContext().setPropertyValue(MEDIAFOLDER, value);
	}
	
}
