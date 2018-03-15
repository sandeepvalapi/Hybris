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
package de.hybris.platform.basecommerce.model.externaltax;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ProductTaxCode first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class ProductTaxCodeModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductTaxCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductTaxCode.productCode</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String PRODUCTCODE = "productCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductTaxCode.taxArea</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String TAXAREA = "taxArea";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductTaxCode.taxCode</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String TAXCODE = "taxCode";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductTaxCodeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductTaxCodeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _productCode initial attribute declared by type <code>ProductTaxCode</code> at extension <code>basecommerce</code>
	 * @param _taxCode initial attribute declared by type <code>ProductTaxCode</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public ProductTaxCodeModel(final String _productCode, final String _taxCode)
	{
		super();
		setProductCode(_productCode);
		setTaxCode(_taxCode);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _productCode initial attribute declared by type <code>ProductTaxCode</code> at extension <code>basecommerce</code>
	 * @param _taxArea initial attribute declared by type <code>ProductTaxCode</code> at extension <code>basecommerce</code>
	 * @param _taxCode initial attribute declared by type <code>ProductTaxCode</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public ProductTaxCodeModel(final ItemModel _owner, final String _productCode, final String _taxArea, final String _taxCode)
	{
		super();
		setOwner(_owner);
		setProductCode(_productCode);
		setTaxArea(_taxArea);
		setTaxCode(_taxCode);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductTaxCode.productCode</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the productCode
	 */
	@Accessor(qualifier = "productCode", type = Accessor.Type.GETTER)
	public String getProductCode()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductTaxCode.taxArea</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the taxArea
	 */
	@Accessor(qualifier = "taxArea", type = Accessor.Type.GETTER)
	public String getTaxArea()
	{
		return getPersistenceContext().getPropertyValue(TAXAREA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductTaxCode.taxCode</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the taxCode
	 */
	@Accessor(qualifier = "taxCode", type = Accessor.Type.GETTER)
	public String getTaxCode()
	{
		return getPersistenceContext().getPropertyValue(TAXCODE);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductTaxCode.productCode</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the productCode
	 */
	@Accessor(qualifier = "productCode", type = Accessor.Type.SETTER)
	public void setProductCode(final String value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductTaxCode.taxArea</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the taxArea
	 */
	@Accessor(qualifier = "taxArea", type = Accessor.Type.SETTER)
	public void setTaxArea(final String value)
	{
		getPersistenceContext().setPropertyValue(TAXAREA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductTaxCode.taxCode</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the taxCode
	 */
	@Accessor(qualifier = "taxCode", type = Accessor.Type.SETTER)
	public void setTaxCode(final String value)
	{
		getPersistenceContext().setPropertyValue(TAXCODE, value);
	}
	
}
