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
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.enums.ProductDifferenceMode;
import de.hybris.platform.catalog.model.CatalogVersionDifferenceModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.CompareCatalogVersionsCronJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ProductCatalogVersionDifference first defined at extension catalog.
 */
@SuppressWarnings("all")
public class ProductCatalogVersionDifferenceModel extends CatalogVersionDifferenceModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductCatalogVersionDifference";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductCatalogVersionDifference.sourceProduct</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCEPRODUCT = "sourceProduct";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductCatalogVersionDifference.targetProduct</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETPRODUCT = "targetProduct";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductCatalogVersionDifference.mode</code> attribute defined at extension <code>catalog</code>. */
	public static final String MODE = "mode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductCatalogVersionDifference.sourceProductApprovalStatus</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCEPRODUCTAPPROVALSTATUS = "sourceProductApprovalStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductCatalogVersionDifference.targetProductApprovalStatus</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETPRODUCTAPPROVALSTATUS = "targetProductApprovalStatus";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductCatalogVersionDifferenceModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductCatalogVersionDifferenceModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _cronJob initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _mode initial attribute declared by type <code>ProductCatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ProductCatalogVersionDifferenceModel(final CompareCatalogVersionsCronJobModel _cronJob, final ProductDifferenceMode _mode, final CatalogVersionModel _sourceVersion, final CatalogVersionModel _targetVersion)
	{
		super();
		setCronJob(_cronJob);
		setMode(_mode);
		setSourceVersion(_sourceVersion);
		setTargetVersion(_targetVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _cronJob initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _mode initial attribute declared by type <code>ProductCatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sourceProduct initial attribute declared by type <code>ProductCatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _targetProduct initial attribute declared by type <code>ProductCatalogVersionDifference</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>CatalogVersionDifference</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ProductCatalogVersionDifferenceModel(final CompareCatalogVersionsCronJobModel _cronJob, final ProductDifferenceMode _mode, final ItemModel _owner, final ProductModel _sourceProduct, final CatalogVersionModel _sourceVersion, final ProductModel _targetProduct, final CatalogVersionModel _targetVersion)
	{
		super();
		setCronJob(_cronJob);
		setMode(_mode);
		setOwner(_owner);
		setSourceProduct(_sourceProduct);
		setSourceVersion(_sourceVersion);
		setTargetProduct(_targetProduct);
		setTargetVersion(_targetVersion);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductCatalogVersionDifference.mode</code> attribute defined at extension <code>catalog</code>. 
	 * @return the mode
	 */
	@Accessor(qualifier = "mode", type = Accessor.Type.GETTER)
	public ProductDifferenceMode getMode()
	{
		return getPersistenceContext().getPropertyValue(MODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductCatalogVersionDifference.sourceProduct</code> attribute defined at extension <code>catalog</code>. 
	 * @return the sourceProduct
	 */
	@Accessor(qualifier = "sourceProduct", type = Accessor.Type.GETTER)
	public ProductModel getSourceProduct()
	{
		return getPersistenceContext().getPropertyValue(SOURCEPRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductCatalogVersionDifference.sourceProductApprovalStatus</code> attribute defined at extension <code>catalog</code>. 
	 * @return the sourceProductApprovalStatus
	 */
	@Accessor(qualifier = "sourceProductApprovalStatus", type = Accessor.Type.GETTER)
	public ArticleApprovalStatus getSourceProductApprovalStatus()
	{
		return getPersistenceContext().getPropertyValue(SOURCEPRODUCTAPPROVALSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductCatalogVersionDifference.targetProduct</code> attribute defined at extension <code>catalog</code>. 
	 * @return the targetProduct
	 */
	@Accessor(qualifier = "targetProduct", type = Accessor.Type.GETTER)
	public ProductModel getTargetProduct()
	{
		return getPersistenceContext().getPropertyValue(TARGETPRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductCatalogVersionDifference.targetProductApprovalStatus</code> attribute defined at extension <code>catalog</code>. 
	 * @return the targetProductApprovalStatus
	 */
	@Accessor(qualifier = "targetProductApprovalStatus", type = Accessor.Type.GETTER)
	public ArticleApprovalStatus getTargetProductApprovalStatus()
	{
		return getPersistenceContext().getPropertyValue(TARGETPRODUCTAPPROVALSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductCatalogVersionDifference.mode</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the mode
	 */
	@Accessor(qualifier = "mode", type = Accessor.Type.SETTER)
	public void setMode(final ProductDifferenceMode value)
	{
		getPersistenceContext().setPropertyValue(MODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductCatalogVersionDifference.sourceProduct</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sourceProduct
	 */
	@Accessor(qualifier = "sourceProduct", type = Accessor.Type.SETTER)
	public void setSourceProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(SOURCEPRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductCatalogVersionDifference.targetProduct</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the targetProduct
	 */
	@Accessor(qualifier = "targetProduct", type = Accessor.Type.SETTER)
	public void setTargetProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(TARGETPRODUCT, value);
	}
	
}
