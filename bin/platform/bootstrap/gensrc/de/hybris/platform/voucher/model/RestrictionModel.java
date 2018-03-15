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
package de.hybris.platform.voucher.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.voucher.model.VoucherModel;
import java.util.Locale;

/**
 * Generated model class for type Restriction first defined at extension voucher.
 */
@SuppressWarnings("all")
public class RestrictionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Restriction";
	
	/**<i>Generated relation code constant for relation <code>VoucherRestrictionsRelation</code> defining source attribute <code>voucher</code> in extension <code>voucher</code>.</i>*/
	public static final String _VOUCHERRESTRICTIONSRELATION = "VoucherRestrictionsRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Restriction.positive</code> attribute defined at extension <code>voucher</code>. */
	public static final String POSITIVE = "positive";
	
	/** <i>Generated constant</i> - Attribute key of <code>Restriction.description</code> attribute defined at extension <code>voucher</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>Restriction.violationMessage</code> attribute defined at extension <code>voucher</code>. */
	public static final String VIOLATIONMESSAGE = "violationMessage";
	
	/** <i>Generated constant</i> - Attribute key of <code>Restriction.restrictionType</code> attribute defined at extension <code>voucher</code>. */
	public static final String RESTRICTIONTYPE = "restrictionType";
	
	/** <i>Generated constant</i> - Attribute key of <code>Restriction.voucherPOS</code> attribute defined at extension <code>voucher</code>. */
	public static final String VOUCHERPOS = "voucherPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>Restriction.voucher</code> attribute defined at extension <code>voucher</code>. */
	public static final String VOUCHER = "voucher";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RestrictionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RestrictionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _voucher initial attribute declared by type <code>Restriction</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public RestrictionModel(final VoucherModel _voucher)
	{
		super();
		setVoucher(_voucher);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _voucher initial attribute declared by type <code>Restriction</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public RestrictionModel(final ItemModel _owner, final VoucherModel _voucher)
	{
		super();
		setOwner(_owner);
		setVoucher(_voucher);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Restriction.description</code> attribute defined at extension <code>voucher</code>. 
	 * @return the description - the description of the restriction.
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Restriction.description</code> attribute defined at extension <code>voucher</code>. 
	 * @param loc the value localization key 
	 * @return the description - the description of the restriction.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Restriction.positive</code> attribute defined at extension <code>voucher</code>. 
	 * @return the positive - Specifies if this restriction is a positive (true) or negative 
	 * 						(false) one.
	 */
	@Accessor(qualifier = "positive", type = Accessor.Type.GETTER)
	public Boolean getPositive()
	{
		return getPersistenceContext().getPropertyValue(POSITIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Restriction.restrictionType</code> attribute defined at extension <code>voucher</code>. 
	 * @return the restrictionType - the type of this restriction.
	 */
	@Accessor(qualifier = "restrictionType", type = Accessor.Type.GETTER)
	public String getRestrictionType()
	{
		return getRestrictionType(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Restriction.restrictionType</code> attribute defined at extension <code>voucher</code>. 
	 * @param loc the value localization key 
	 * @return the restrictionType - the type of this restriction.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "restrictionType", type = Accessor.Type.GETTER)
	public String getRestrictionType(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(RESTRICTIONTYPE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Restriction.violationMessage</code> attribute defined at extension <code>voucher</code>. 
	 * @return the violationMessage - the message to return if the restriction is violated (not fulfilled).
	 */
	@Accessor(qualifier = "violationMessage", type = Accessor.Type.GETTER)
	public String getViolationMessage()
	{
		return getViolationMessage(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Restriction.violationMessage</code> attribute defined at extension <code>voucher</code>. 
	 * @param loc the value localization key 
	 * @return the violationMessage - the message to return if the restriction is violated (not fulfilled).
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "violationMessage", type = Accessor.Type.GETTER)
	public String getViolationMessage(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(VIOLATIONMESSAGE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Restriction.voucher</code> attribute defined at extension <code>voucher</code>. 
	 * @return the voucher
	 */
	@Accessor(qualifier = "voucher", type = Accessor.Type.GETTER)
	public VoucherModel getVoucher()
	{
		return getPersistenceContext().getPropertyValue(VOUCHER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Restriction.description</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the description - the description of the restriction.
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Restriction.description</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the description - the description of the restriction.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Restriction.positive</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the positive - Specifies if this restriction is a positive (true) or negative 
	 * 						(false) one.
	 */
	@Accessor(qualifier = "positive", type = Accessor.Type.SETTER)
	public void setPositive(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(POSITIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Restriction.violationMessage</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the violationMessage - the message to return if the restriction is violated (not fulfilled).
	 */
	@Accessor(qualifier = "violationMessage", type = Accessor.Type.SETTER)
	public void setViolationMessage(final String value)
	{
		setViolationMessage(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Restriction.violationMessage</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the violationMessage - the message to return if the restriction is violated (not fulfilled).
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "violationMessage", type = Accessor.Type.SETTER)
	public void setViolationMessage(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(VIOLATIONMESSAGE, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Restriction.voucher</code> attribute defined at extension <code>voucher</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the voucher
	 */
	@Accessor(qualifier = "voucher", type = Accessor.Type.SETTER)
	public void setVoucher(final VoucherModel value)
	{
		getPersistenceContext().setPropertyValue(VOUCHER, value);
	}
	
}
