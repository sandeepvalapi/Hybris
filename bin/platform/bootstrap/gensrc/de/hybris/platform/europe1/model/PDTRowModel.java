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
package de.hybris.platform.europe1.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.util.StandardDateRange;
import java.util.Date;

/**
 * Generated model class for type PDTRow first defined at extension europe1.
 */
@SuppressWarnings("all")
public class PDTRowModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PDTRow";
	
	/** <i>Generated constant</i> - Attribute key of <code>PDTRow.product</code> attribute defined at extension <code>europe1</code>. */
	public static final String PRODUCT = "product";
	
	/** <i>Generated constant</i> - Attribute key of <code>PDTRow.pg</code> attribute defined at extension <code>europe1</code>. */
	public static final String PG = "pg";
	
	/** <i>Generated constant</i> - Attribute key of <code>PDTRow.productMatchQualifier</code> attribute defined at extension <code>europe1</code>. */
	public static final String PRODUCTMATCHQUALIFIER = "productMatchQualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>PDTRow.startTime</code> attribute defined at extension <code>europe1</code>. */
	public static final String STARTTIME = "startTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>PDTRow.endTime</code> attribute defined at extension <code>europe1</code>. */
	public static final String ENDTIME = "endTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>PDTRow.dateRange</code> attribute defined at extension <code>europe1</code>. */
	public static final String DATERANGE = "dateRange";
	
	/** <i>Generated constant</i> - Attribute key of <code>PDTRow.user</code> attribute defined at extension <code>europe1</code>. */
	public static final String USER = "user";
	
	/** <i>Generated constant</i> - Attribute key of <code>PDTRow.ug</code> attribute defined at extension <code>europe1</code>. */
	public static final String UG = "ug";
	
	/** <i>Generated constant</i> - Attribute key of <code>PDTRow.userMatchQualifier</code> attribute defined at extension <code>europe1</code>. */
	public static final String USERMATCHQUALIFIER = "userMatchQualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>PDTRow.productId</code> attribute defined at extension <code>europe1</code>. */
	public static final String PRODUCTID = "productId";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PDTRowModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PDTRowModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _pg initial attribute declared by type <code>PDTRow</code> at extension <code>europe1</code>
	 * @param _product initial attribute declared by type <code>PDTRow</code> at extension <code>europe1</code>
	 * @param _productId initial attribute declared by type <code>PDTRow</code> at extension <code>europe1</code>
	 */
	@Deprecated
	public PDTRowModel(final ItemModel _owner, final HybrisEnumValue _pg, final ProductModel _product, final String _productId)
	{
		super();
		setOwner(_owner);
		setPg(_pg);
		setProduct(_product);
		setProductId(_productId);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PDTRow.dateRange</code> dynamic attribute defined at extension <code>europe1</code>. 
	 * @return the dateRange
	 */
	@Accessor(qualifier = "dateRange", type = Accessor.Type.GETTER)
	public StandardDateRange getDateRange()
	{
		return getPersistenceContext().getDynamicValue(this,DATERANGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PDTRow.endTime</code> attribute defined at extension <code>europe1</code>. 
	 * @return the endTime
	 */
	@Accessor(qualifier = "endTime", type = Accessor.Type.GETTER)
	public Date getEndTime()
	{
		return getPersistenceContext().getPropertyValue(ENDTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PDTRow.pg</code> attribute defined at extension <code>europe1</code>. 
	 * @return the pg
	 */
	@Accessor(qualifier = "pg", type = Accessor.Type.GETTER)
	public HybrisEnumValue getPg()
	{
		return getPersistenceContext().getPropertyValue(PG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PDTRow.product</code> attribute defined at extension <code>europe1</code>. 
	 * @return the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.GETTER)
	public ProductModel getProduct()
	{
		return getPersistenceContext().getPropertyValue(PRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PDTRow.productId</code> attribute defined at extension <code>europe1</code>. 
	 * @return the productId
	 */
	@Accessor(qualifier = "productId", type = Accessor.Type.GETTER)
	public String getProductId()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PDTRow.productMatchQualifier</code> attribute defined at extension <code>europe1</code>. 
	 * @return the productMatchQualifier
	 */
	@Accessor(qualifier = "productMatchQualifier", type = Accessor.Type.GETTER)
	public Long getProductMatchQualifier()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTMATCHQUALIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PDTRow.startTime</code> attribute defined at extension <code>europe1</code>. 
	 * @return the startTime
	 */
	@Accessor(qualifier = "startTime", type = Accessor.Type.GETTER)
	public Date getStartTime()
	{
		return getPersistenceContext().getPropertyValue(STARTTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PDTRow.ug</code> attribute defined at extension <code>europe1</code>. 
	 * @return the ug
	 */
	@Accessor(qualifier = "ug", type = Accessor.Type.GETTER)
	public HybrisEnumValue getUg()
	{
		return getPersistenceContext().getPropertyValue(UG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PDTRow.user</code> attribute defined at extension <code>europe1</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PDTRow.userMatchQualifier</code> attribute defined at extension <code>europe1</code>. 
	 * @return the userMatchQualifier
	 */
	@Accessor(qualifier = "userMatchQualifier", type = Accessor.Type.GETTER)
	public Long getUserMatchQualifier()
	{
		return getPersistenceContext().getPropertyValue(USERMATCHQUALIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PDTRow.dateRange</code> dynamic attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the dateRange
	 */
	@Accessor(qualifier = "dateRange", type = Accessor.Type.SETTER)
	public void setDateRange(final StandardDateRange value)
	{
		getPersistenceContext().setDynamicValue(this,DATERANGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PDTRow.endTime</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the endTime
	 */
	@Accessor(qualifier = "endTime", type = Accessor.Type.SETTER)
	public void setEndTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(ENDTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>PDTRow.pg</code> attribute defined at extension <code>europe1</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the pg
	 */
	@Accessor(qualifier = "pg", type = Accessor.Type.SETTER)
	public void setPg(final HybrisEnumValue value)
	{
		getPersistenceContext().setPropertyValue(PG, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>PDTRow.product</code> attribute defined at extension <code>europe1</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.SETTER)
	public void setProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(PRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>PDTRow.productId</code> attribute defined at extension <code>europe1</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the productId
	 */
	@Accessor(qualifier = "productId", type = Accessor.Type.SETTER)
	public void setProductId(final String value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PDTRow.productMatchQualifier</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the productMatchQualifier
	 */
	@Accessor(qualifier = "productMatchQualifier", type = Accessor.Type.SETTER)
	public void setProductMatchQualifier(final Long value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTMATCHQUALIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PDTRow.startTime</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the startTime
	 */
	@Accessor(qualifier = "startTime", type = Accessor.Type.SETTER)
	public void setStartTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(STARTTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PDTRow.ug</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the ug
	 */
	@Accessor(qualifier = "ug", type = Accessor.Type.SETTER)
	public void setUg(final HybrisEnumValue value)
	{
		getPersistenceContext().setPropertyValue(UG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PDTRow.user</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PDTRow.userMatchQualifier</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the userMatchQualifier
	 */
	@Accessor(qualifier = "userMatchQualifier", type = Accessor.Type.SETTER)
	public void setUserMatchQualifier(final Long value)
	{
		getPersistenceContext().setPropertyValue(USERMATCHQUALIFIER, value);
	}
	
}
