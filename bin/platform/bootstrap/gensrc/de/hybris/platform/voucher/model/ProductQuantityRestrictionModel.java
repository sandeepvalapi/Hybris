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
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.voucher.model.ProductRestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

/**
 * Generated model class for type ProductQuantityRestriction first defined at extension voucher.
 */
@SuppressWarnings("all")
public class ProductQuantityRestrictionModel extends ProductRestrictionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductQuantityRestriction";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductQuantityRestriction.quantity</code> attribute defined at extension <code>voucher</code>. */
	public static final String QUANTITY = "quantity";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductQuantityRestriction.unit</code> attribute defined at extension <code>voucher</code>. */
	public static final String UNIT = "unit";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductQuantityRestrictionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductQuantityRestrictionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _quantity initial attribute declared by type <code>ProductQuantityRestriction</code> at extension <code>voucher</code>
	 * @param _unit initial attribute declared by type <code>ProductQuantityRestriction</code> at extension <code>voucher</code>
	 * @param _voucher initial attribute declared by type <code>Restriction</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public ProductQuantityRestrictionModel(final Long _quantity, final UnitModel _unit, final VoucherModel _voucher)
	{
		super();
		setQuantity(_quantity);
		setUnit(_unit);
		setVoucher(_voucher);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _quantity initial attribute declared by type <code>ProductQuantityRestriction</code> at extension <code>voucher</code>
	 * @param _unit initial attribute declared by type <code>ProductQuantityRestriction</code> at extension <code>voucher</code>
	 * @param _voucher initial attribute declared by type <code>Restriction</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public ProductQuantityRestrictionModel(final ItemModel _owner, final Long _quantity, final UnitModel _unit, final VoucherModel _voucher)
	{
		super();
		setOwner(_owner);
		setQuantity(_quantity);
		setUnit(_unit);
		setVoucher(_voucher);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductQuantityRestriction.quantity</code> attribute defined at extension <code>voucher</code>. 
	 * @return the quantity - the quantity of products the given voucher is valid for.
	 */
	@Accessor(qualifier = "quantity", type = Accessor.Type.GETTER)
	public Long getQuantity()
	{
		return getPersistenceContext().getPropertyValue(QUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductQuantityRestriction.unit</code> attribute defined at extension <code>voucher</code>. 
	 * @return the unit - the unit for the quantity of products the given voucher is valid for.
	 */
	@Accessor(qualifier = "unit", type = Accessor.Type.GETTER)
	public UnitModel getUnit()
	{
		return getPersistenceContext().getPropertyValue(UNIT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductQuantityRestriction.quantity</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the quantity - the quantity of products the given voucher is valid for.
	 */
	@Accessor(qualifier = "quantity", type = Accessor.Type.SETTER)
	public void setQuantity(final Long value)
	{
		getPersistenceContext().setPropertyValue(QUANTITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductQuantityRestriction.unit</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the unit - the unit for the quantity of products the given voucher is valid for.
	 */
	@Accessor(qualifier = "unit", type = Accessor.Type.SETTER)
	public void setUnit(final UnitModel value)
	{
		getPersistenceContext().setPropertyValue(UNIT, value);
	}
	
}
