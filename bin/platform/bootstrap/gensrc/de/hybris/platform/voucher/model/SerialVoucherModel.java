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
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.voucher.model.VoucherModel;
import java.util.Collection;

/**
 * Generated model class for type SerialVoucher first defined at extension voucher.
 */
@SuppressWarnings("all")
public class SerialVoucherModel extends VoucherModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SerialVoucher";
	
	/** <i>Generated constant</i> - Attribute key of <code>SerialVoucher.codes</code> attribute defined at extension <code>voucher</code>. */
	public static final String CODES = "codes";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SerialVoucherModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SerialVoucherModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Voucher</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public SerialVoucherModel(final String _code)
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
	public SerialVoucherModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SerialVoucher.codes</code> attribute defined at extension <code>voucher</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the codes - the generated codes of this voucher.
	 */
	@Accessor(qualifier = "codes", type = Accessor.Type.GETTER)
	public Collection<MediaModel> getCodes()
	{
		return getPersistenceContext().getPropertyValue(CODES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SerialVoucher.codes</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the codes - the generated codes of this voucher.
	 */
	@Accessor(qualifier = "codes", type = Accessor.Type.SETTER)
	public void setCodes(final Collection<MediaModel> value)
	{
		getPersistenceContext().setPropertyValue(CODES, value);
	}
	
}
