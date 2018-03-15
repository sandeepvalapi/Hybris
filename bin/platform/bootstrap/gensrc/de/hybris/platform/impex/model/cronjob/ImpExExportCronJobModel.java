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
package de.hybris.platform.impex.model.cronjob;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.enums.EncodingEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.impex.enums.ExportConverterEnum;
import de.hybris.platform.impex.enums.ImpExValidationModeEnum;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.impex.model.exp.ExportModel;
import de.hybris.platform.impex.model.exp.HeaderLibraryModel;
import de.hybris.platform.impex.model.exp.ImpExExportMediaModel;
import de.hybris.platform.impex.model.exp.ReportModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ImpExExportCronJob first defined at extension impex.
 */
@SuppressWarnings("all")
public class ImpExExportCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ImpExExportCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.encoding</code> attribute defined at extension <code>impex</code>. */
	public static final String ENCODING = "encoding";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.mode</code> attribute defined at extension <code>impex</code>. */
	public static final String MODE = "mode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.dataExportTarget</code> attribute defined at extension <code>impex</code>. */
	public static final String DATAEXPORTTARGET = "dataExportTarget";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.mediasExportTarget</code> attribute defined at extension <code>impex</code>. */
	public static final String MEDIASEXPORTTARGET = "mediasExportTarget";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.exportTemplate</code> attribute defined at extension <code>impex</code>. */
	public static final String EXPORTTEMPLATE = "exportTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.export</code> attribute defined at extension <code>impex</code>. */
	public static final String EXPORT = "export";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.itemsExported</code> attribute defined at extension <code>impex</code>. */
	public static final String ITEMSEXPORTED = "itemsExported";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.itemsMaxCount</code> attribute defined at extension <code>impex</code>. */
	public static final String ITEMSMAXCOUNT = "itemsMaxCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.itemsSkipped</code> attribute defined at extension <code>impex</code>. */
	public static final String ITEMSSKIPPED = "itemsSkipped";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.jobMedia</code> attribute defined at extension <code>impex</code>. */
	public static final String JOBMEDIA = "jobMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.fieldSeparator</code> attribute defined at extension <code>impex</code>. */
	public static final String FIELDSEPARATOR = "fieldSeparator";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.quoteCharacter</code> attribute defined at extension <code>impex</code>. */
	public static final String QUOTECHARACTER = "quoteCharacter";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.commentCharacter</code> attribute defined at extension <code>impex</code>. */
	public static final String COMMENTCHARACTER = "commentCharacter";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.dataExportMediaCode</code> attribute defined at extension <code>impex</code>. */
	public static final String DATAEXPORTMEDIACODE = "dataExportMediaCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.mediasExportMediaCode</code> attribute defined at extension <code>impex</code>. */
	public static final String MEDIASEXPORTMEDIACODE = "mediasExportMediaCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.report</code> attribute defined at extension <code>impex</code>. */
	public static final String REPORT = "report";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.converterClass</code> attribute defined at extension <code>impex</code>. */
	public static final String CONVERTERCLASS = "converterClass";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.singleFile</code> attribute defined at extension <code>impex</code>. */
	public static final String SINGLEFILE = "singleFile";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ImpExExportCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ImpExExportCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ImpExExportCronJobModel(final JobModel _job)
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
	public ImpExExportCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.commentCharacter</code> attribute defined at extension <code>impex</code>. 
	 * @return the commentCharacter - Character used for indicating a comment
	 */
	@Accessor(qualifier = "commentCharacter", type = Accessor.Type.GETTER)
	public Character getCommentCharacter()
	{
		final Character value = getPersistenceContext().getPropertyValue(COMMENTCHARACTER);
		return value != null ? value : Character.valueOf('#');
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.converterClass</code> attribute defined at extension <code>impex</code>. 
	 * @return the converterClass - class, which will be used for report generation
	 */
	@Accessor(qualifier = "converterClass", type = Accessor.Type.GETTER)
	public ExportConverterEnum getConverterClass()
	{
		return getPersistenceContext().getPropertyValue(CONVERTERCLASS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.dataExportMediaCode</code> attribute defined at extension <code>impex</code>. 
	 * @return the dataExportMediaCode - Code of the generated export media, containing the exported data
	 */
	@Accessor(qualifier = "dataExportMediaCode", type = Accessor.Type.GETTER)
	public String getDataExportMediaCode()
	{
		final String value = getPersistenceContext().getPropertyValue(DATAEXPORTMEDIACODE);
		return value != null ? value : ("dataexport_" + this.getCode());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.dataExportTarget</code> attribute defined at extension <code>impex</code>. 
	 * @return the dataExportTarget
	 */
	@Accessor(qualifier = "dataExportTarget", type = Accessor.Type.GETTER)
	public ImpExExportMediaModel getDataExportTarget()
	{
		return getPersistenceContext().getPropertyValue(DATAEXPORTTARGET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.encoding</code> attribute defined at extension <code>impex</code>. 
	 * @return the encoding
	 */
	@Accessor(qualifier = "encoding", type = Accessor.Type.GETTER)
	public EncodingEnum getEncoding()
	{
		return getPersistenceContext().getPropertyValue(ENCODING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.export</code> attribute defined at extension <code>impex</code>. 
	 * @return the export
	 */
	@Accessor(qualifier = "export", type = Accessor.Type.GETTER)
	public ExportModel getExport()
	{
		return getPersistenceContext().getPropertyValue(EXPORT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.exportTemplate</code> attribute defined at extension <code>impex</code>. 
	 * @return the exportTemplate
	 */
	@Accessor(qualifier = "exportTemplate", type = Accessor.Type.GETTER)
	public HeaderLibraryModel getExportTemplate()
	{
		return getPersistenceContext().getPropertyValue(EXPORTTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.fieldSeparator</code> attribute defined at extension <code>impex</code>. 
	 * @return the fieldSeparator - Character used for separating columns in CSV-lines
	 */
	@Accessor(qualifier = "fieldSeparator", type = Accessor.Type.GETTER)
	public Character getFieldSeparator()
	{
		final Character value = getPersistenceContext().getPropertyValue(FIELDSEPARATOR);
		return value != null ? value : Character.valueOf(';');
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.itemsExported</code> attribute defined at extension <code>impex</code>. 
	 * @return the itemsExported
	 */
	@Accessor(qualifier = "itemsExported", type = Accessor.Type.GETTER)
	public Integer getItemsExported()
	{
		return getPersistenceContext().getPropertyValue(ITEMSEXPORTED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.itemsMaxCount</code> attribute defined at extension <code>impex</code>. 
	 * @return the itemsMaxCount
	 */
	@Accessor(qualifier = "itemsMaxCount", type = Accessor.Type.GETTER)
	public Integer getItemsMaxCount()
	{
		return getPersistenceContext().getPropertyValue(ITEMSMAXCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.itemsSkipped</code> attribute defined at extension <code>impex</code>. 
	 * @return the itemsSkipped
	 */
	@Accessor(qualifier = "itemsSkipped", type = Accessor.Type.GETTER)
	public Integer getItemsSkipped()
	{
		return getPersistenceContext().getPropertyValue(ITEMSSKIPPED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.jobMedia</code> attribute defined at extension <code>impex</code>. 
	 * @return the jobMedia
	 */
	@Accessor(qualifier = "jobMedia", type = Accessor.Type.GETTER)
	public ImpExMediaModel getJobMedia()
	{
		return getPersistenceContext().getPropertyValue(JOBMEDIA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.mediasExportMediaCode</code> attribute defined at extension <code>impex</code>. 
	 * @return the mediasExportMediaCode - Code of the generated export media, containing the exported medias
	 */
	@Accessor(qualifier = "mediasExportMediaCode", type = Accessor.Type.GETTER)
	public String getMediasExportMediaCode()
	{
		final String value = getPersistenceContext().getPropertyValue(MEDIASEXPORTMEDIACODE);
		return value != null ? value : ("mediasexport_" + this.getCode());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.mediasExportTarget</code> attribute defined at extension <code>impex</code>. 
	 * @return the mediasExportTarget
	 */
	@Accessor(qualifier = "mediasExportTarget", type = Accessor.Type.GETTER)
	public ImpExExportMediaModel getMediasExportTarget()
	{
		return getPersistenceContext().getPropertyValue(MEDIASEXPORTTARGET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.mode</code> attribute defined at extension <code>impex</code>. 
	 * @return the mode
	 */
	@Accessor(qualifier = "mode", type = Accessor.Type.GETTER)
	public ImpExValidationModeEnum getMode()
	{
		return getPersistenceContext().getPropertyValue(MODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.quoteCharacter</code> attribute defined at extension <code>impex</code>. 
	 * @return the quoteCharacter - Character used for escaping columns in CSV-lines
	 */
	@Accessor(qualifier = "quoteCharacter", type = Accessor.Type.GETTER)
	public Character getQuoteCharacter()
	{
		final Character value = getPersistenceContext().getPropertyValue(QUOTECHARACTER);
		return value != null ? value : Character.valueOf('"');
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.report</code> attribute defined at extension <code>impex</code>. 
	 * @return the report
	 */
	@Accessor(qualifier = "report", type = Accessor.Type.GETTER)
	public ReportModel getReport()
	{
		return getPersistenceContext().getPropertyValue(REPORT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.singleFile</code> attribute defined at extension <code>impex</code>. 
	 * @return the singleFile - Export result as a single file instead of Zip archive
	 */
	@Accessor(qualifier = "singleFile", type = Accessor.Type.GETTER)
	public Boolean getSingleFile()
	{
		return getPersistenceContext().getPropertyValue(SINGLEFILE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.commentCharacter</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the commentCharacter - Character used for indicating a comment
	 */
	@Accessor(qualifier = "commentCharacter", type = Accessor.Type.SETTER)
	public void setCommentCharacter(final Character value)
	{
		getPersistenceContext().setPropertyValue(COMMENTCHARACTER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.converterClass</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the converterClass - class, which will be used for report generation
	 */
	@Accessor(qualifier = "converterClass", type = Accessor.Type.SETTER)
	public void setConverterClass(final ExportConverterEnum value)
	{
		getPersistenceContext().setPropertyValue(CONVERTERCLASS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.dataExportMediaCode</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the dataExportMediaCode - Code of the generated export media, containing the exported data
	 */
	@Accessor(qualifier = "dataExportMediaCode", type = Accessor.Type.SETTER)
	public void setDataExportMediaCode(final String value)
	{
		getPersistenceContext().setPropertyValue(DATAEXPORTMEDIACODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.dataExportTarget</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the dataExportTarget
	 */
	@Accessor(qualifier = "dataExportTarget", type = Accessor.Type.SETTER)
	public void setDataExportTarget(final ImpExExportMediaModel value)
	{
		getPersistenceContext().setPropertyValue(DATAEXPORTTARGET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.encoding</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the encoding
	 */
	@Accessor(qualifier = "encoding", type = Accessor.Type.SETTER)
	public void setEncoding(final EncodingEnum value)
	{
		getPersistenceContext().setPropertyValue(ENCODING, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.export</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the export
	 */
	@Accessor(qualifier = "export", type = Accessor.Type.SETTER)
	public void setExport(final ExportModel value)
	{
		getPersistenceContext().setPropertyValue(EXPORT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.exportTemplate</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the exportTemplate
	 */
	@Accessor(qualifier = "exportTemplate", type = Accessor.Type.SETTER)
	public void setExportTemplate(final HeaderLibraryModel value)
	{
		getPersistenceContext().setPropertyValue(EXPORTTEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.fieldSeparator</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the fieldSeparator - Character used for separating columns in CSV-lines
	 */
	@Accessor(qualifier = "fieldSeparator", type = Accessor.Type.SETTER)
	public void setFieldSeparator(final Character value)
	{
		getPersistenceContext().setPropertyValue(FIELDSEPARATOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.jobMedia</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the jobMedia
	 */
	@Accessor(qualifier = "jobMedia", type = Accessor.Type.SETTER)
	public void setJobMedia(final ImpExMediaModel value)
	{
		getPersistenceContext().setPropertyValue(JOBMEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.mediasExportMediaCode</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the mediasExportMediaCode - Code of the generated export media, containing the exported medias
	 */
	@Accessor(qualifier = "mediasExportMediaCode", type = Accessor.Type.SETTER)
	public void setMediasExportMediaCode(final String value)
	{
		getPersistenceContext().setPropertyValue(MEDIASEXPORTMEDIACODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.mediasExportTarget</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the mediasExportTarget
	 */
	@Accessor(qualifier = "mediasExportTarget", type = Accessor.Type.SETTER)
	public void setMediasExportTarget(final ImpExExportMediaModel value)
	{
		getPersistenceContext().setPropertyValue(MEDIASEXPORTTARGET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.mode</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the mode
	 */
	@Accessor(qualifier = "mode", type = Accessor.Type.SETTER)
	public void setMode(final ImpExValidationModeEnum value)
	{
		getPersistenceContext().setPropertyValue(MODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.quoteCharacter</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the quoteCharacter - Character used for escaping columns in CSV-lines
	 */
	@Accessor(qualifier = "quoteCharacter", type = Accessor.Type.SETTER)
	public void setQuoteCharacter(final Character value)
	{
		getPersistenceContext().setPropertyValue(QUOTECHARACTER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.report</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the report
	 */
	@Accessor(qualifier = "report", type = Accessor.Type.SETTER)
	public void setReport(final ReportModel value)
	{
		getPersistenceContext().setPropertyValue(REPORT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.singleFile</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the singleFile - Export result as a single file instead of Zip archive
	 */
	@Accessor(qualifier = "singleFile", type = Accessor.Type.SETTER)
	public void setSingleFile(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SINGLEFILE, value);
	}
	
}
