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
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type MoveMediaCronJob first defined at extension processing.
 */
@SuppressWarnings("all")
public class MoveMediaCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MoveMediaCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>MoveMediaCronJob.medias</code> attribute defined at extension <code>processing</code>. */
	public static final String MEDIAS = "medias";
	
	/** <i>Generated constant</i> - Attribute key of <code>MoveMediaCronJob.targetFolder</code> attribute defined at extension <code>processing</code>. */
	public static final String TARGETFOLDER = "targetFolder";
	
	/** <i>Generated constant</i> - Attribute key of <code>MoveMediaCronJob.movedMediasCount</code> attribute defined at extension <code>processing</code>. */
	public static final String MOVEDMEDIASCOUNT = "movedMediasCount";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MoveMediaCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MoveMediaCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public MoveMediaCronJobModel(final JobModel _job)
	{
		super();
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public MoveMediaCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MoveMediaCronJob.medias</code> attribute defined at extension <code>processing</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the medias - Selected medias which will be moved
	 */
	@Accessor(qualifier = "medias", type = Accessor.Type.GETTER)
	public Collection<MediaModel> getMedias()
	{
		return getPersistenceContext().getPropertyValue(MEDIAS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MoveMediaCronJob.movedMediasCount</code> attribute defined at extension <code>processing</code>. 
	 * @return the movedMediasCount - Successfully moved medias since start of cronjob
	 */
	@Accessor(qualifier = "movedMediasCount", type = Accessor.Type.GETTER)
	public Integer getMovedMediasCount()
	{
		return getPersistenceContext().getPropertyValue(MOVEDMEDIASCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MoveMediaCronJob.targetFolder</code> attribute defined at extension <code>processing</code>. 
	 * @return the targetFolder - Folder where selected medias will be moved to
	 */
	@Accessor(qualifier = "targetFolder", type = Accessor.Type.GETTER)
	public MediaFolderModel getTargetFolder()
	{
		return getPersistenceContext().getPropertyValue(TARGETFOLDER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MoveMediaCronJob.medias</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the medias - Selected medias which will be moved
	 */
	@Accessor(qualifier = "medias", type = Accessor.Type.SETTER)
	public void setMedias(final Collection<MediaModel> value)
	{
		getPersistenceContext().setPropertyValue(MEDIAS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MoveMediaCronJob.movedMediasCount</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the movedMediasCount - Successfully moved medias since start of cronjob
	 */
	@Accessor(qualifier = "movedMediasCount", type = Accessor.Type.SETTER)
	public void setMovedMediasCount(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MOVEDMEDIASCOUNT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MoveMediaCronJob.targetFolder</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the targetFolder - Folder where selected medias will be moved to
	 */
	@Accessor(qualifier = "targetFolder", type = Accessor.Type.SETTER)
	public void setTargetFolder(final MediaFolderModel value)
	{
		getPersistenceContext().setPropertyValue(TARGETFOLDER, value);
	}
	
}
