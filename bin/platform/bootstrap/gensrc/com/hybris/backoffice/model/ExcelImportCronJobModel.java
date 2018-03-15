/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Mar 15, 2018 5:02:29 PM                     ---
 * ----------------------------------------------------------------
 */
package com.hybris.backoffice.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ExcelImportCronJob first defined at extension backoffice.
 */
@SuppressWarnings("all")
public class ExcelImportCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ExcelImportCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExcelImportCronJob.excelFile</code> attribute defined at extension <code>backoffice</code>. */
	public static final String EXCELFILE = "excelFile";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExcelImportCronJob.referencedContent</code> attribute defined at extension <code>backoffice</code>. */
	public static final String REFERENCEDCONTENT = "referencedContent";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ExcelImportCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ExcelImportCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _excelFile initial attribute declared by type <code>ExcelImportCronJob</code> at extension <code>backoffice</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ExcelImportCronJobModel(final MediaModel _excelFile, final JobModel _job)
	{
		super();
		setExcelFile(_excelFile);
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _excelFile initial attribute declared by type <code>ExcelImportCronJob</code> at extension <code>backoffice</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ExcelImportCronJobModel(final MediaModel _excelFile, final JobModel _job, final ItemModel _owner)
	{
		super();
		setExcelFile(_excelFile);
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExcelImportCronJob.excelFile</code> attribute defined at extension <code>backoffice</code>. 
	 * @return the excelFile
	 */
	@Accessor(qualifier = "excelFile", type = Accessor.Type.GETTER)
	public MediaModel getExcelFile()
	{
		return getPersistenceContext().getPropertyValue(EXCELFILE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExcelImportCronJob.referencedContent</code> attribute defined at extension <code>backoffice</code>. 
	 * @return the referencedContent
	 */
	@Accessor(qualifier = "referencedContent", type = Accessor.Type.GETTER)
	public MediaModel getReferencedContent()
	{
		return getPersistenceContext().getPropertyValue(REFERENCEDCONTENT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExcelImportCronJob.excelFile</code> attribute defined at extension <code>backoffice</code>. 
	 *  
	 * @param value the excelFile
	 */
	@Accessor(qualifier = "excelFile", type = Accessor.Type.SETTER)
	public void setExcelFile(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(EXCELFILE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExcelImportCronJob.referencedContent</code> attribute defined at extension <code>backoffice</code>. 
	 *  
	 * @param value the referencedContent
	 */
	@Accessor(qualifier = "referencedContent", type = Accessor.Type.SETTER)
	public void setReferencedContent(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(REFERENCEDCONTENT, value);
	}
	
}
