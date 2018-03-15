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
package de.hybris.platform.mobileservices.model.text;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.mobileservices.model.text.MobileShortcodeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Generated model class for type MobileCostPlan first defined at extension mobileservices.
 */
@SuppressWarnings("all")
public class MobileCostPlanModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MobileCostPlan";
	
	/**<i>Generated relation code constant for relation <code>ShortcodeRevenueRelation</code> defining source attribute <code>receivingShortcode</code> in extension <code>mobileservices</code>.</i>*/
	public static final String _SHORTCODEREVENUERELATION = "ShortcodeRevenueRelation";
	
	/**<i>Generated relation code constant for relation <code>ShortcodeCostRelation</code> defining source attribute <code>sendingShortcode</code> in extension <code>mobileservices</code>.</i>*/
	public static final String _SHORTCODECOSTRELATION = "ShortcodeCostRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileCostPlan.price</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String PRICE = "price";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileCostPlan.priceCurrency</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String PRICECURRENCY = "priceCurrency";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileCostPlan.startDate</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String STARTDATE = "startDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileCostPlan.endDate</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String ENDDATE = "endDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileCostPlan.receivingShortcode</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String RECEIVINGSHORTCODE = "receivingShortcode";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileCostPlan.sendingShortcode</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String SENDINGSHORTCODE = "sendingShortcode";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MobileCostPlanModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MobileCostPlanModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public MobileCostPlanModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileCostPlan.endDate</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the endDate
	 */
	@Accessor(qualifier = "endDate", type = Accessor.Type.GETTER)
	public Date getEndDate()
	{
		return getPersistenceContext().getPropertyValue(ENDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileCostPlan.price</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the price
	 */
	@Accessor(qualifier = "price", type = Accessor.Type.GETTER)
	public BigDecimal getPrice()
	{
		return getPersistenceContext().getPropertyValue(PRICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileCostPlan.priceCurrency</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the priceCurrency
	 */
	@Accessor(qualifier = "priceCurrency", type = Accessor.Type.GETTER)
	public CurrencyModel getPriceCurrency()
	{
		return getPersistenceContext().getPropertyValue(PRICECURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileCostPlan.receivingShortcode</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the receivingShortcode
	 */
	@Accessor(qualifier = "receivingShortcode", type = Accessor.Type.GETTER)
	public MobileShortcodeModel getReceivingShortcode()
	{
		return getPersistenceContext().getPropertyValue(RECEIVINGSHORTCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileCostPlan.sendingShortcode</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the sendingShortcode
	 */
	@Accessor(qualifier = "sendingShortcode", type = Accessor.Type.GETTER)
	public MobileShortcodeModel getSendingShortcode()
	{
		return getPersistenceContext().getPropertyValue(SENDINGSHORTCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileCostPlan.startDate</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the startDate
	 */
	@Accessor(qualifier = "startDate", type = Accessor.Type.GETTER)
	public Date getStartDate()
	{
		return getPersistenceContext().getPropertyValue(STARTDATE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileCostPlan.endDate</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the endDate
	 */
	@Accessor(qualifier = "endDate", type = Accessor.Type.SETTER)
	public void setEndDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(ENDDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileCostPlan.price</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the price
	 */
	@Accessor(qualifier = "price", type = Accessor.Type.SETTER)
	public void setPrice(final BigDecimal value)
	{
		getPersistenceContext().setPropertyValue(PRICE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileCostPlan.priceCurrency</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the priceCurrency
	 */
	@Accessor(qualifier = "priceCurrency", type = Accessor.Type.SETTER)
	public void setPriceCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(PRICECURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileCostPlan.receivingShortcode</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the receivingShortcode
	 */
	@Accessor(qualifier = "receivingShortcode", type = Accessor.Type.SETTER)
	public void setReceivingShortcode(final MobileShortcodeModel value)
	{
		getPersistenceContext().setPropertyValue(RECEIVINGSHORTCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileCostPlan.sendingShortcode</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the sendingShortcode
	 */
	@Accessor(qualifier = "sendingShortcode", type = Accessor.Type.SETTER)
	public void setSendingShortcode(final MobileShortcodeModel value)
	{
		getPersistenceContext().setPropertyValue(SENDINGSHORTCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileCostPlan.startDate</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the startDate
	 */
	@Accessor(qualifier = "startDate", type = Accessor.Type.SETTER)
	public void setStartDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(STARTDATE, value);
	}
	
}
