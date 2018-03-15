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
package de.hybris.platform.acceleratorservices.model.export;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorservices.enums.ExportDataStatus;
import de.hybris.platform.acceleratorservices.model.export.ExportDataCronJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type ExportDataHistoryEntry first defined at extension acceleratorservices.
 * <p>
 * Contains a history of executed exports.
 */
@SuppressWarnings("all")
public class ExportDataHistoryEntryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ExportDataHistoryEntry";
	
	/**<i>Generated relation code constant for relation <code>ExportDataCronJob2ExportDataHistoryRel</code> defining source attribute <code>exportDataCronJob</code> in extension <code>acceleratorservices</code>.</i>*/
	public static final String _EXPORTDATACRONJOB2EXPORTDATAHISTORYREL = "ExportDataCronJob2ExportDataHistoryRel";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExportDataHistoryEntry.code</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExportDataHistoryEntry.status</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExportDataHistoryEntry.startTime</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String STARTTIME = "startTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExportDataHistoryEntry.finishTime</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String FINISHTIME = "finishTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExportDataHistoryEntry.processedResultCount</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String PROCESSEDRESULTCOUNT = "processedResultCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExportDataHistoryEntry.failureMessage</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String FAILUREMESSAGE = "failureMessage";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExportDataHistoryEntry.exportDataCronJob</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String EXPORTDATACRONJOB = "exportDataCronJob";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ExportDataHistoryEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ExportDataHistoryEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ExportDataHistoryEntry</code> at extension <code>acceleratorservices</code>
	 */
	@Deprecated
	public ExportDataHistoryEntryModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>ExportDataHistoryEntry</code> at extension <code>acceleratorservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ExportDataHistoryEntryModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportDataHistoryEntry.code</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the code - unique code that identifies the export data history
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportDataHistoryEntry.exportDataCronJob</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the exportDataCronJob
	 */
	@Accessor(qualifier = "exportDataCronJob", type = Accessor.Type.GETTER)
	public ExportDataCronJobModel getExportDataCronJob()
	{
		return getPersistenceContext().getPropertyValue(EXPORTDATACRONJOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportDataHistoryEntry.failureMessage</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the failureMessage - Message if failure occurred
	 */
	@Accessor(qualifier = "failureMessage", type = Accessor.Type.GETTER)
	public String getFailureMessage()
	{
		return getPersistenceContext().getPropertyValue(FAILUREMESSAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportDataHistoryEntry.finishTime</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the finishTime - Start time of this export
	 */
	@Accessor(qualifier = "finishTime", type = Accessor.Type.GETTER)
	public Date getFinishTime()
	{
		return getPersistenceContext().getPropertyValue(FINISHTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportDataHistoryEntry.processedResultCount</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the processedResultCount - The amount of records that was written to the export file
	 */
	@Accessor(qualifier = "processedResultCount", type = Accessor.Type.GETTER)
	public Integer getProcessedResultCount()
	{
		return getPersistenceContext().getPropertyValue(PROCESSEDRESULTCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportDataHistoryEntry.startTime</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the startTime - Start time of this export
	 */
	@Accessor(qualifier = "startTime", type = Accessor.Type.GETTER)
	public Date getStartTime()
	{
		return getPersistenceContext().getPropertyValue(STARTTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExportDataHistoryEntry.status</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the status - The status of this particular export
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public ExportDataStatus getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ExportDataHistoryEntry.code</code> attribute defined at extension <code>acceleratorservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code - unique code that identifies the export data history
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExportDataHistoryEntry.exportDataCronJob</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the exportDataCronJob
	 */
	@Accessor(qualifier = "exportDataCronJob", type = Accessor.Type.SETTER)
	public void setExportDataCronJob(final ExportDataCronJobModel value)
	{
		getPersistenceContext().setPropertyValue(EXPORTDATACRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExportDataHistoryEntry.failureMessage</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the failureMessage - Message if failure occurred
	 */
	@Accessor(qualifier = "failureMessage", type = Accessor.Type.SETTER)
	public void setFailureMessage(final String value)
	{
		getPersistenceContext().setPropertyValue(FAILUREMESSAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExportDataHistoryEntry.finishTime</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the finishTime - Start time of this export
	 */
	@Accessor(qualifier = "finishTime", type = Accessor.Type.SETTER)
	public void setFinishTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(FINISHTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExportDataHistoryEntry.processedResultCount</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the processedResultCount - The amount of records that was written to the export file
	 */
	@Accessor(qualifier = "processedResultCount", type = Accessor.Type.SETTER)
	public void setProcessedResultCount(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PROCESSEDRESULTCOUNT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExportDataHistoryEntry.startTime</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the startTime - Start time of this export
	 */
	@Accessor(qualifier = "startTime", type = Accessor.Type.SETTER)
	public void setStartTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(STARTTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExportDataHistoryEntry.status</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the status - The status of this particular export
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final ExportDataStatus value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
}
