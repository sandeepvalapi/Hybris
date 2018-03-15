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
package de.hybris.platform.stock.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.StockLevelUpdateType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type StockLevelHistoryEntry first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class StockLevelHistoryEntryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "StockLevelHistoryEntry";
	
	/**<i>Generated relation code constant for relation <code>StockLevelStockLevelHistoryEntryRelation</code> defining source attribute <code>stockLevel</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _STOCKLEVELSTOCKLEVELHISTORYENTRYRELATION = "StockLevelStockLevelHistoryEntryRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevelHistoryEntry.updateDate</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String UPDATEDATE = "updateDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevelHistoryEntry.actual</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String ACTUAL = "actual";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevelHistoryEntry.reserved</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String RESERVED = "reserved";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevelHistoryEntry.updateType</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String UPDATETYPE = "updateType";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevelHistoryEntry.comment</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String COMMENT = "comment";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevelHistoryEntry.stockLevelPOS</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String STOCKLEVELPOS = "stockLevelPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>StockLevelHistoryEntry.stockLevel</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String STOCKLEVEL = "stockLevel";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public StockLevelHistoryEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public StockLevelHistoryEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _actual initial attribute declared by type <code>StockLevelHistoryEntry</code> at extension <code>basecommerce</code>
	 * @param _reserved initial attribute declared by type <code>StockLevelHistoryEntry</code> at extension <code>basecommerce</code>
	 * @param _stockLevel initial attribute declared by type <code>StockLevelHistoryEntry</code> at extension <code>basecommerce</code>
	 * @param _updateDate initial attribute declared by type <code>StockLevelHistoryEntry</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public StockLevelHistoryEntryModel(final int _actual, final int _reserved, final StockLevelModel _stockLevel, final Date _updateDate)
	{
		super();
		setActual(_actual);
		setReserved(_reserved);
		setStockLevel(_stockLevel);
		setUpdateDate(_updateDate);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _actual initial attribute declared by type <code>StockLevelHistoryEntry</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _reserved initial attribute declared by type <code>StockLevelHistoryEntry</code> at extension <code>basecommerce</code>
	 * @param _stockLevel initial attribute declared by type <code>StockLevelHistoryEntry</code> at extension <code>basecommerce</code>
	 * @param _updateDate initial attribute declared by type <code>StockLevelHistoryEntry</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public StockLevelHistoryEntryModel(final int _actual, final ItemModel _owner, final int _reserved, final StockLevelModel _stockLevel, final Date _updateDate)
	{
		super();
		setActual(_actual);
		setOwner(_owner);
		setReserved(_reserved);
		setStockLevel(_stockLevel);
		setUpdateDate(_updateDate);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevelHistoryEntry.actual</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the actual
	 */
	@Accessor(qualifier = "actual", type = Accessor.Type.GETTER)
	public int getActual()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(ACTUAL));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevelHistoryEntry.comment</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
	public String getComment()
	{
		return getPersistenceContext().getPropertyValue(COMMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevelHistoryEntry.reserved</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the reserved
	 */
	@Accessor(qualifier = "reserved", type = Accessor.Type.GETTER)
	public int getReserved()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(RESERVED));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevelHistoryEntry.stockLevel</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the stockLevel
	 */
	@Accessor(qualifier = "stockLevel", type = Accessor.Type.GETTER)
	public StockLevelModel getStockLevel()
	{
		return getPersistenceContext().getPropertyValue(STOCKLEVEL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevelHistoryEntry.updateDate</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the updateDate
	 */
	@Accessor(qualifier = "updateDate", type = Accessor.Type.GETTER)
	public Date getUpdateDate()
	{
		return getPersistenceContext().getPropertyValue(UPDATEDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevelHistoryEntry.updateType</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the updateType
	 */
	@Accessor(qualifier = "updateType", type = Accessor.Type.GETTER)
	public StockLevelUpdateType getUpdateType()
	{
		return getPersistenceContext().getPropertyValue(UPDATETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockLevelHistoryEntry.actual</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the actual
	 */
	@Accessor(qualifier = "actual", type = Accessor.Type.SETTER)
	public void setActual(final int value)
	{
		getPersistenceContext().setPropertyValue(ACTUAL, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockLevelHistoryEntry.comment</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the comment
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
	public void setComment(final String value)
	{
		getPersistenceContext().setPropertyValue(COMMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockLevelHistoryEntry.reserved</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the reserved
	 */
	@Accessor(qualifier = "reserved", type = Accessor.Type.SETTER)
	public void setReserved(final int value)
	{
		getPersistenceContext().setPropertyValue(RESERVED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>StockLevelHistoryEntry.stockLevel</code> attribute defined at extension <code>basecommerce</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the stockLevel
	 */
	@Accessor(qualifier = "stockLevel", type = Accessor.Type.SETTER)
	public void setStockLevel(final StockLevelModel value)
	{
		getPersistenceContext().setPropertyValue(STOCKLEVEL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockLevelHistoryEntry.updateDate</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the updateDate
	 */
	@Accessor(qualifier = "updateDate", type = Accessor.Type.SETTER)
	public void setUpdateDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(UPDATEDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StockLevelHistoryEntry.updateType</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the updateType
	 */
	@Accessor(qualifier = "updateType", type = Accessor.Type.SETTER)
	public void setUpdateType(final StockLevelUpdateType value)
	{
		getPersistenceContext().setPropertyValue(UPDATETYPE, value);
	}
	
}
