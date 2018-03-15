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
package de.hybris.platform.couponservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.Locale;

/**
 * Generated model class for type AbstractCoupon first defined at extension couponservices.
 */
@SuppressWarnings("all")
public class AbstractCouponModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractCoupon";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractCoupon.couponId</code> attribute defined at extension <code>couponservices</code>. */
	public static final String COUPONID = "couponId";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractCoupon.name</code> attribute defined at extension <code>couponservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractCoupon.startDate</code> attribute defined at extension <code>couponservices</code>. */
	public static final String STARTDATE = "startDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractCoupon.endDate</code> attribute defined at extension <code>couponservices</code>. */
	public static final String ENDDATE = "endDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractCoupon.active</code> attribute defined at extension <code>couponservices</code>. */
	public static final String ACTIVE = "active";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractCouponModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractCouponModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _couponId initial attribute declared by type <code>AbstractCoupon</code> at extension <code>couponservices</code>
	 */
	@Deprecated
	public AbstractCouponModel(final String _couponId)
	{
		super();
		setCouponId(_couponId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _couponId initial attribute declared by type <code>AbstractCoupon</code> at extension <code>couponservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractCouponModel(final String _couponId, final ItemModel _owner)
	{
		super();
		setCouponId(_couponId);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCoupon.active</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public Boolean getActive()
	{
		return getPersistenceContext().getPropertyValue(ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCoupon.couponId</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the couponId
	 */
	@Accessor(qualifier = "couponId", type = Accessor.Type.GETTER)
	public String getCouponId()
	{
		return getPersistenceContext().getPropertyValue(COUPONID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCoupon.endDate</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the endDate
	 */
	@Accessor(qualifier = "endDate", type = Accessor.Type.GETTER)
	public Date getEndDate()
	{
		return getPersistenceContext().getPropertyValue(ENDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCoupon.name</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCoupon.name</code> attribute defined at extension <code>couponservices</code>. 
	 * @param loc the value localization key 
	 * @return the name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCoupon.startDate</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the startDate
	 */
	@Accessor(qualifier = "startDate", type = Accessor.Type.GETTER)
	public Date getStartDate()
	{
		return getPersistenceContext().getPropertyValue(STARTDATE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractCoupon.active</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractCoupon.couponId</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the couponId
	 */
	@Accessor(qualifier = "couponId", type = Accessor.Type.SETTER)
	public void setCouponId(final String value)
	{
		getPersistenceContext().setPropertyValue(COUPONID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractCoupon.endDate</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the endDate
	 */
	@Accessor(qualifier = "endDate", type = Accessor.Type.SETTER)
	public void setEndDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(ENDDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractCoupon.name</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractCoupon.name</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractCoupon.startDate</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the startDate
	 */
	@Accessor(qualifier = "startDate", type = Accessor.Type.SETTER)
	public void setStartDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(STARTDATE, value);
	}
	
}
