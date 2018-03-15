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
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type Voucher first defined at extension voucher.
 */
@SuppressWarnings("all")
public class VoucherModel extends DiscountModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Voucher";
	
	/**<i>Generated relation code constant for relation <code>OrderDiscountRelation</code> defining source attribute <code>orders</code> in extension <code>core</code>.</i>*/
	public static final String _ORDERDISCOUNTRELATION = "OrderDiscountRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Voucher.description</code> attribute defined at extension <code>voucher</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>Voucher.freeShipping</code> attribute defined at extension <code>voucher</code>. */
	public static final String FREESHIPPING = "freeShipping";
	
	/** <i>Generated constant</i> - Attribute key of <code>Voucher.valueString</code> attribute defined at extension <code>voucher</code>. */
	public static final String VALUESTRING = "valueString";
	
	/** <i>Generated constant</i> - Attribute key of <code>Voucher.restrictions</code> attribute defined at extension <code>voucher</code>. */
	public static final String RESTRICTIONS = "restrictions";
	
	/** <i>Generated constant</i> - Attribute key of <code>Voucher.invalidations</code> attribute defined at extension <code>voucher</code>. */
	public static final String INVALIDATIONS = "invalidations";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public VoucherModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public VoucherModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Voucher</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public VoucherModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Voucher</code> at extension <code>voucher</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public VoucherModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Voucher.description</code> attribute defined at extension <code>voucher</code>. 
	 * @return the description - the description of the voucher.
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Voucher.description</code> attribute defined at extension <code>voucher</code>. 
	 * @param loc the value localization key 
	 * @return the description - the description of the voucher.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Voucher.freeShipping</code> attribute defined at extension <code>voucher</code>. 
	 * @return the freeShipping - Specifies if the order this voucher is applied to is shipped 
	 * 						for free (true) or not (false). Default is not (false).
	 */
	@Accessor(qualifier = "freeShipping", type = Accessor.Type.GETTER)
	public Boolean getFreeShipping()
	{
		return getPersistenceContext().getPropertyValue(FREESHIPPING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Voucher.invalidations</code> attribute defined at extension <code>voucher</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the invalidations
	 */
	@Accessor(qualifier = "invalidations", type = Accessor.Type.GETTER)
	public Collection<VoucherInvalidationModel> getInvalidations()
	{
		return getPersistenceContext().getPropertyValue(INVALIDATIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Voucher.restrictions</code> attribute defined at extension <code>voucher</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the restrictions
	 */
	@Accessor(qualifier = "restrictions", type = Accessor.Type.GETTER)
	public Set<RestrictionModel> getRestrictions()
	{
		return getPersistenceContext().getPropertyValue(RESTRICTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Voucher.valueString</code> attribute defined at extension <code>voucher</code>. 
	 * @return the valueString - the value of this voucher to display.
	 */
	@Accessor(qualifier = "valueString", type = Accessor.Type.GETTER)
	public String getValueString()
	{
		return getPersistenceContext().getPropertyValue(VALUESTRING);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Voucher.description</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the description - the description of the voucher.
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Voucher.description</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the description - the description of the voucher.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Voucher.freeShipping</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the freeShipping - Specifies if the order this voucher is applied to is shipped 
	 * 						for free (true) or not (false). Default is not (false).
	 */
	@Accessor(qualifier = "freeShipping", type = Accessor.Type.SETTER)
	public void setFreeShipping(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(FREESHIPPING, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Voucher.invalidations</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the invalidations
	 */
	@Accessor(qualifier = "invalidations", type = Accessor.Type.SETTER)
	public void setInvalidations(final Collection<VoucherInvalidationModel> value)
	{
		getPersistenceContext().setPropertyValue(INVALIDATIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Voucher.restrictions</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the restrictions
	 */
	@Accessor(qualifier = "restrictions", type = Accessor.Type.SETTER)
	public void setRestrictions(final Set<RestrictionModel> value)
	{
		getPersistenceContext().setPropertyValue(RESTRICTIONS, value);
	}
	
}
