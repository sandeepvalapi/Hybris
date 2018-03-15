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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CompareCatalogVersionsCronJob first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CompareCatalogVersionsCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CompareCatalogVersionsCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.processedItemsCount</code> attribute defined at extension <code>catalog</code>. */
	public static final String PROCESSEDITEMSCOUNT = "processedItemsCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.sourceVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCEVERSION = "sourceVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.targetVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETVERSION = "targetVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.missingProducts</code> attribute defined at extension <code>catalog</code>. */
	public static final String MISSINGPRODUCTS = "missingProducts";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.newProducts</code> attribute defined at extension <code>catalog</code>. */
	public static final String NEWPRODUCTS = "newProducts";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.maxPriceTolerance</code> attribute defined at extension <code>catalog</code>. */
	public static final String MAXPRICETOLERANCE = "maxPriceTolerance";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.searchMissingProducts</code> attribute defined at extension <code>catalog</code>. */
	public static final String SEARCHMISSINGPRODUCTS = "searchMissingProducts";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.searchMissingCategories</code> attribute defined at extension <code>catalog</code>. */
	public static final String SEARCHMISSINGCATEGORIES = "searchMissingCategories";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.searchNewProducts</code> attribute defined at extension <code>catalog</code>. */
	public static final String SEARCHNEWPRODUCTS = "searchNewProducts";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.searchNewCategories</code> attribute defined at extension <code>catalog</code>. */
	public static final String SEARCHNEWCATEGORIES = "searchNewCategories";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.searchPriceDifferences</code> attribute defined at extension <code>catalog</code>. */
	public static final String SEARCHPRICEDIFFERENCES = "searchPriceDifferences";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.overwriteProductApprovalStatus</code> attribute defined at extension <code>catalog</code>. */
	public static final String OVERWRITEPRODUCTAPPROVALSTATUS = "overwriteProductApprovalStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompareCatalogVersionsCronJob.priceCompareCustomer</code> attribute defined at extension <code>catalog</code>. */
	public static final String PRICECOMPARECUSTOMER = "priceCompareCustomer";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CompareCatalogVersionsCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CompareCatalogVersionsCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _missingProducts initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 * @param _newProducts initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 * @param _processedItemsCount initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CompareCatalogVersionsCronJobModel(final JobModel _job, final int _missingProducts, final int _newProducts, final int _processedItemsCount, final CatalogVersionModel _sourceVersion, final CatalogVersionModel _targetVersion)
	{
		super();
		setJob(_job);
		setMissingProducts(_missingProducts);
		setNewProducts(_newProducts);
		setProcessedItemsCount(_processedItemsCount);
		setSourceVersion(_sourceVersion);
		setTargetVersion(_targetVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _missingProducts initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 * @param _newProducts initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _processedItemsCount initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>CompareCatalogVersionsCronJob</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CompareCatalogVersionsCronJobModel(final JobModel _job, final int _missingProducts, final int _newProducts, final ItemModel _owner, final int _processedItemsCount, final CatalogVersionModel _sourceVersion, final CatalogVersionModel _targetVersion)
	{
		super();
		setJob(_job);
		setMissingProducts(_missingProducts);
		setNewProducts(_newProducts);
		setOwner(_owner);
		setProcessedItemsCount(_processedItemsCount);
		setSourceVersion(_sourceVersion);
		setTargetVersion(_targetVersion);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.maxPriceTolerance</code> attribute defined at extension <code>catalog</code>. 
	 * @return the maxPriceTolerance
	 */
	@Accessor(qualifier = "maxPriceTolerance", type = Accessor.Type.GETTER)
	public Double getMaxPriceTolerance()
	{
		return getPersistenceContext().getPropertyValue(MAXPRICETOLERANCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.missingProducts</code> attribute defined at extension <code>catalog</code>. 
	 * @return the missingProducts
	 */
	@Accessor(qualifier = "missingProducts", type = Accessor.Type.GETTER)
	public int getMissingProducts()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(MISSINGPRODUCTS));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.newProducts</code> attribute defined at extension <code>catalog</code>. 
	 * @return the newProducts
	 */
	@Accessor(qualifier = "newProducts", type = Accessor.Type.GETTER)
	public int getNewProducts()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(NEWPRODUCTS));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.overwriteProductApprovalStatus</code> attribute defined at extension <code>catalog</code>. 
	 * @return the overwriteProductApprovalStatus
	 */
	@Accessor(qualifier = "overwriteProductApprovalStatus", type = Accessor.Type.GETTER)
	public Boolean getOverwriteProductApprovalStatus()
	{
		return getPersistenceContext().getPropertyValue(OVERWRITEPRODUCTAPPROVALSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.priceCompareCustomer</code> attribute defined at extension <code>catalog</code>. 
	 * @return the priceCompareCustomer
	 */
	@Accessor(qualifier = "priceCompareCustomer", type = Accessor.Type.GETTER)
	public UserModel getPriceCompareCustomer()
	{
		return getPersistenceContext().getPropertyValue(PRICECOMPARECUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.processedItemsCount</code> attribute defined at extension <code>catalog</code>. 
	 * @return the processedItemsCount
	 */
	@Accessor(qualifier = "processedItemsCount", type = Accessor.Type.GETTER)
	public int getProcessedItemsCount()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(PROCESSEDITEMSCOUNT));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.searchMissingCategories</code> attribute defined at extension <code>catalog</code>. 
	 * @return the searchMissingCategories
	 */
	@Accessor(qualifier = "searchMissingCategories", type = Accessor.Type.GETTER)
	public Boolean getSearchMissingCategories()
	{
		return getPersistenceContext().getPropertyValue(SEARCHMISSINGCATEGORIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.searchMissingProducts</code> attribute defined at extension <code>catalog</code>. 
	 * @return the searchMissingProducts
	 */
	@Accessor(qualifier = "searchMissingProducts", type = Accessor.Type.GETTER)
	public Boolean getSearchMissingProducts()
	{
		return getPersistenceContext().getPropertyValue(SEARCHMISSINGPRODUCTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.searchNewCategories</code> attribute defined at extension <code>catalog</code>. 
	 * @return the searchNewCategories
	 */
	@Accessor(qualifier = "searchNewCategories", type = Accessor.Type.GETTER)
	public Boolean getSearchNewCategories()
	{
		return getPersistenceContext().getPropertyValue(SEARCHNEWCATEGORIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.searchNewProducts</code> attribute defined at extension <code>catalog</code>. 
	 * @return the searchNewProducts
	 */
	@Accessor(qualifier = "searchNewProducts", type = Accessor.Type.GETTER)
	public Boolean getSearchNewProducts()
	{
		return getPersistenceContext().getPropertyValue(SEARCHNEWPRODUCTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.searchPriceDifferences</code> attribute defined at extension <code>catalog</code>. 
	 * @return the searchPriceDifferences
	 */
	@Accessor(qualifier = "searchPriceDifferences", type = Accessor.Type.GETTER)
	public Boolean getSearchPriceDifferences()
	{
		return getPersistenceContext().getPropertyValue(SEARCHPRICEDIFFERENCES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.sourceVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the sourceVersion
	 */
	@Accessor(qualifier = "sourceVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getSourceVersion()
	{
		return getPersistenceContext().getPropertyValue(SOURCEVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompareCatalogVersionsCronJob.targetVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the targetVersion
	 */
	@Accessor(qualifier = "targetVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getTargetVersion()
	{
		return getPersistenceContext().getPropertyValue(TARGETVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.maxPriceTolerance</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the maxPriceTolerance
	 */
	@Accessor(qualifier = "maxPriceTolerance", type = Accessor.Type.SETTER)
	public void setMaxPriceTolerance(final Double value)
	{
		getPersistenceContext().setPropertyValue(MAXPRICETOLERANCE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.missingProducts</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the missingProducts
	 */
	@Accessor(qualifier = "missingProducts", type = Accessor.Type.SETTER)
	public void setMissingProducts(final int value)
	{
		getPersistenceContext().setPropertyValue(MISSINGPRODUCTS, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.newProducts</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the newProducts
	 */
	@Accessor(qualifier = "newProducts", type = Accessor.Type.SETTER)
	public void setNewProducts(final int value)
	{
		getPersistenceContext().setPropertyValue(NEWPRODUCTS, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.overwriteProductApprovalStatus</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the overwriteProductApprovalStatus
	 */
	@Accessor(qualifier = "overwriteProductApprovalStatus", type = Accessor.Type.SETTER)
	public void setOverwriteProductApprovalStatus(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(OVERWRITEPRODUCTAPPROVALSTATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.priceCompareCustomer</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the priceCompareCustomer
	 */
	@Accessor(qualifier = "priceCompareCustomer", type = Accessor.Type.SETTER)
	public void setPriceCompareCustomer(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(PRICECOMPARECUSTOMER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.processedItemsCount</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the processedItemsCount
	 */
	@Accessor(qualifier = "processedItemsCount", type = Accessor.Type.SETTER)
	public void setProcessedItemsCount(final int value)
	{
		getPersistenceContext().setPropertyValue(PROCESSEDITEMSCOUNT, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.searchMissingCategories</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the searchMissingCategories
	 */
	@Accessor(qualifier = "searchMissingCategories", type = Accessor.Type.SETTER)
	public void setSearchMissingCategories(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SEARCHMISSINGCATEGORIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.searchMissingProducts</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the searchMissingProducts
	 */
	@Accessor(qualifier = "searchMissingProducts", type = Accessor.Type.SETTER)
	public void setSearchMissingProducts(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SEARCHMISSINGPRODUCTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.searchNewCategories</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the searchNewCategories
	 */
	@Accessor(qualifier = "searchNewCategories", type = Accessor.Type.SETTER)
	public void setSearchNewCategories(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SEARCHNEWCATEGORIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.searchNewProducts</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the searchNewProducts
	 */
	@Accessor(qualifier = "searchNewProducts", type = Accessor.Type.SETTER)
	public void setSearchNewProducts(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SEARCHNEWPRODUCTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.searchPriceDifferences</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the searchPriceDifferences
	 */
	@Accessor(qualifier = "searchPriceDifferences", type = Accessor.Type.SETTER)
	public void setSearchPriceDifferences(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SEARCHPRICEDIFFERENCES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.sourceVersion</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the sourceVersion
	 */
	@Accessor(qualifier = "sourceVersion", type = Accessor.Type.SETTER)
	public void setSourceVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(SOURCEVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompareCatalogVersionsCronJob.targetVersion</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the targetVersion
	 */
	@Accessor(qualifier = "targetVersion", type = Accessor.Type.SETTER)
	public void setTargetVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(TARGETVERSION, value);
	}
	
}
