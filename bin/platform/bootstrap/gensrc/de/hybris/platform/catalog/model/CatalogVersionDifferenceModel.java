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
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.CompareCatalogVersionsCronJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CatalogVersionDifference first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CatalogVersionDifferenceModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CatalogVersionDifference";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionDifference.sourceVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCEVERSION = "sourceVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionDifference.targetVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETVERSION = "targetVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionDifference.cronJob</code> attribute defined at extension <code>catalog</code>. */
	public static final String CRONJOB = "cronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionDifference.differenceText</code> attribute defined at extension <code>catalog</code>. */
	public static final String DIFFERENCETEXT = "differenceText";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionDifference.differenceValue</code> attribute defined at extension <code>catalog</code>. */
	public static final String DIFFERENCEVALUE = "differenceValue";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CatalogVersionDifferenceModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CatalogVersionDifferenceModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _cronJob initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CatalogVersionDifferenceModel(final CompareCatalogVersionsCronJobModel _cronJob, final CatalogVersionModel _sourceVersion, final CatalogVersionModel _targetVersion)
	{
		super();
		setCronJob(_cronJob);
		setSourceVersion(_sourceVersion);
		setTargetVersion(_targetVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _cronJob initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sourceVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CatalogVersionDifferenceModel(final CompareCatalogVersionsCronJobModel _cronJob, final ItemModel _owner, final CatalogVersionModel _sourceVersion, final CatalogVersionModel _targetVersion)
	{
		super();
		setCronJob(_cronJob);
		setOwner(_owner);
		setSourceVersion(_sourceVersion);
		setTargetVersion(_targetVersion);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionDifference.cronJob</code> attribute defined at extension <code>catalog</code>. 
	 * @return the cronJob
	 */
	@Accessor(qualifier = "cronJob", type = Accessor.Type.GETTER)
	public CompareCatalogVersionsCronJobModel getCronJob()
	{
		return getPersistenceContext().getPropertyValue(CRONJOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionDifference.differenceText</code> attribute defined at extension <code>catalog</code>. 
	 * @return the differenceText
	 */
	@Accessor(qualifier = "differenceText", type = Accessor.Type.GETTER)
	public String getDifferenceText()
	{
		return getPersistenceContext().getPropertyValue(DIFFERENCETEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionDifference.differenceValue</code> attribute defined at extension <code>catalog</code>. 
	 * @return the differenceValue
	 */
	@Accessor(qualifier = "differenceValue", type = Accessor.Type.GETTER)
	public Double getDifferenceValue()
	{
		return getPersistenceContext().getPropertyValue(DIFFERENCEVALUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionDifference.sourceVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the sourceVersion
	 */
	@Accessor(qualifier = "sourceVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getSourceVersion()
	{
		return getPersistenceContext().getPropertyValue(SOURCEVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionDifference.targetVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the targetVersion
	 */
	@Accessor(qualifier = "targetVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getTargetVersion()
	{
		return getPersistenceContext().getPropertyValue(TARGETVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CatalogVersionDifference.cronJob</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the cronJob
	 */
	@Accessor(qualifier = "cronJob", type = Accessor.Type.SETTER)
	public void setCronJob(final CompareCatalogVersionsCronJobModel value)
	{
		getPersistenceContext().setPropertyValue(CRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionDifference.differenceText</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the differenceText
	 */
	@Accessor(qualifier = "differenceText", type = Accessor.Type.SETTER)
	public void setDifferenceText(final String value)
	{
		getPersistenceContext().setPropertyValue(DIFFERENCETEXT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionDifference.differenceValue</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the differenceValue
	 */
	@Accessor(qualifier = "differenceValue", type = Accessor.Type.SETTER)
	public void setDifferenceValue(final Double value)
	{
		getPersistenceContext().setPropertyValue(DIFFERENCEVALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CatalogVersionDifference.sourceVersion</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sourceVersion
	 */
	@Accessor(qualifier = "sourceVersion", type = Accessor.Type.SETTER)
	public void setSourceVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(SOURCEVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CatalogVersionDifference.targetVersion</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the targetVersion
	 */
	@Accessor(qualifier = "targetVersion", type = Accessor.Type.SETTER)
	public void setTargetVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(TARGETVERSION, value);
	}
	
}
