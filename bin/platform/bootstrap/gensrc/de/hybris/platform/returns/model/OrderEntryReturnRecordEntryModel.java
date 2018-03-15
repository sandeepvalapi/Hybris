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
package de.hybris.platform.returns.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type OrderEntryReturnRecordEntry first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class OrderEntryReturnRecordEntryModel extends OrderEntryModificationRecordEntryModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "OrderEntryReturnRecordEntry";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderEntryReturnRecordEntry.expectedQuantity</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String EXPECTEDQUANTITY = "expectedQuantity";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderEntryReturnRecordEntry.returnedQuantity</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String RETURNEDQUANTITY = "returnedQuantity";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public OrderEntryReturnRecordEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public OrderEntryReturnRecordEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>OrderEntryModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _modificationRecordEntry initial attribute declared by type <code>OrderEntryModificationRecordEntry</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public OrderEntryReturnRecordEntryModel(final String _code, final OrderModificationRecordEntryModel _modificationRecordEntry)
	{
		super();
		setCode(_code);
		setModificationRecordEntry(_modificationRecordEntry);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>OrderEntryModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _modificationRecordEntry initial attribute declared by type <code>OrderEntryModificationRecordEntry</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public OrderEntryReturnRecordEntryModel(final String _code, final OrderModificationRecordEntryModel _modificationRecordEntry, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setModificationRecordEntry(_modificationRecordEntry);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderEntryReturnRecordEntry.expectedQuantity</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the expectedQuantity
	 */
	@Accessor(qualifier = "expectedQuantity", type = Accessor.Type.GETTER)
	public Long getExpectedQuantity()
	{
		return getPersistenceContext().getPropertyValue(EXPECTEDQUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderEntryReturnRecordEntry.returnedQuantity</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the returnedQuantity
	 */
	@Accessor(qualifier = "returnedQuantity", type = Accessor.Type.GETTER)
	public Long getReturnedQuantity()
	{
		return getPersistenceContext().getPropertyValue(RETURNEDQUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderEntryReturnRecordEntry.expectedQuantity</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the expectedQuantity
	 */
	@Accessor(qualifier = "expectedQuantity", type = Accessor.Type.SETTER)
	public void setExpectedQuantity(final Long value)
	{
		getPersistenceContext().setPropertyValue(EXPECTEDQUANTITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderEntryReturnRecordEntry.returnedQuantity</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the returnedQuantity
	 */
	@Accessor(qualifier = "returnedQuantity", type = Accessor.Type.SETTER)
	public void setReturnedQuantity(final Long value)
	{
		getPersistenceContext().setPropertyValue(RETURNEDQUANTITY, value);
	}
	
}
