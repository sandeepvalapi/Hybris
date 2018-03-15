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
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;
import java.util.Date;

/**
 * Generated model class for type DateRestriction first defined at extension voucher.
 */
@SuppressWarnings("all")
public class DateRestrictionModel extends RestrictionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DateRestriction";
	
	/** <i>Generated constant</i> - Attribute key of <code>DateRestriction.startDate</code> attribute defined at extension <code>voucher</code>. */
	public static final String STARTDATE = "startDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>DateRestriction.endDate</code> attribute defined at extension <code>voucher</code>. */
	public static final String ENDDATE = "endDate";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DateRestrictionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DateRestrictionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _endDate initial attribute declared by type <code>DateRestriction</code> at extension <code>voucher</code>
	 * @param _startDate initial attribute declared by type <code>DateRestriction</code> at extension <code>voucher</code>
	 * @param _voucher initial attribute declared by type <code>Restriction</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public DateRestrictionModel(final Date _endDate, final Date _startDate, final VoucherModel _voucher)
	{
		super();
		setEndDate(_endDate);
		setStartDate(_startDate);
		setVoucher(_voucher);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _endDate initial attribute declared by type <code>DateRestriction</code> at extension <code>voucher</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _startDate initial attribute declared by type <code>DateRestriction</code> at extension <code>voucher</code>
	 * @param _voucher initial attribute declared by type <code>Restriction</code> at extension <code>voucher</code>
	 */
	@Deprecated
	public DateRestrictionModel(final Date _endDate, final ItemModel _owner, final Date _startDate, final VoucherModel _voucher)
	{
		super();
		setEndDate(_endDate);
		setOwner(_owner);
		setStartDate(_startDate);
		setVoucher(_voucher);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DateRestriction.endDate</code> attribute defined at extension <code>voucher</code>. 
	 * @return the endDate - the date until the given voucher is valid.
	 */
	@Accessor(qualifier = "endDate", type = Accessor.Type.GETTER)
	public Date getEndDate()
	{
		return getPersistenceContext().getPropertyValue(ENDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DateRestriction.startDate</code> attribute defined at extension <code>voucher</code>. 
	 * @return the startDate - the date past which the given voucher is valid.
	 */
	@Accessor(qualifier = "startDate", type = Accessor.Type.GETTER)
	public Date getStartDate()
	{
		return getPersistenceContext().getPropertyValue(STARTDATE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DateRestriction.endDate</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the endDate - the date until the given voucher is valid.
	 */
	@Accessor(qualifier = "endDate", type = Accessor.Type.SETTER)
	public void setEndDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(ENDDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DateRestriction.startDate</code> attribute defined at extension <code>voucher</code>. 
	 *  
	 * @param value the startDate - the date past which the given voucher is valid.
	 */
	@Accessor(qualifier = "startDate", type = Accessor.Type.SETTER)
	public void setStartDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(STARTDATE, value);
	}
	
}
