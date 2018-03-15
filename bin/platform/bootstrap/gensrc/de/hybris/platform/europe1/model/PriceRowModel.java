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
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.europe1.enums.ProductPriceGroup;
import de.hybris.platform.europe1.model.PDTRowModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type PriceRow first defined at extension europe1.
 */
@SuppressWarnings("all")
public class PriceRowModel extends PDTRowModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PriceRow";
	
	/**<i>Generated relation code constant for relation <code>Product2OwnEurope1Prices</code> defining source attribute <code>product</code> in extension <code>europe1</code>.</i>*/
	public static final String _PRODUCT2OWNEUROPE1PRICES = "Product2OwnEurope1Prices";
	
	/** <i>Generated constant</i> - Attribute key of <code>PriceRow.catalogVersion</code> attribute defined at extension <code>europe1</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>PriceRow.matchValue</code> attribute defined at extension <code>europe1</code>. */
	public static final String MATCHVALUE = "matchValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>PriceRow.currency</code> attribute defined at extension <code>europe1</code>. */
	public static final String CURRENCY = "currency";
	
	/** <i>Generated constant</i> - Attribute key of <code>PriceRow.minqtd</code> attribute defined at extension <code>europe1</code>. */
	public static final String MINQTD = "minqtd";
	
	/** <i>Generated constant</i> - Attribute key of <code>PriceRow.net</code> attribute defined at extension <code>europe1</code>. */
	public static final String NET = "net";
	
	/** <i>Generated constant</i> - Attribute key of <code>PriceRow.price</code> attribute defined at extension <code>europe1</code>. */
	public static final String PRICE = "price";
	
	/** <i>Generated constant</i> - Attribute key of <code>PriceRow.unit</code> attribute defined at extension <code>europe1</code>. */
	public static final String UNIT = "unit";
	
	/** <i>Generated constant</i> - Attribute key of <code>PriceRow.unitFactor</code> attribute defined at extension <code>europe1</code>. */
	public static final String UNITFACTOR = "unitFactor";
	
	/** <i>Generated constant</i> - Attribute key of <code>PriceRow.giveAwayPrice</code> attribute defined at extension <code>europe1</code>. */
	public static final String GIVEAWAYPRICE = "giveAwayPrice";
	
	/** <i>Generated constant</i> - Attribute key of <code>PriceRow.channel</code> attribute defined at extension <code>europe1</code>. */
	public static final String CHANNEL = "channel";
	
	/** <i>Generated constant</i> - Attribute key of <code>PriceRow.sequenceId</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String SEQUENCEID = "sequenceId";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PriceRowModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PriceRowModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _currency initial attribute declared by type <code>PriceRow</code> at extension <code>europe1</code>
	 * @param _price initial attribute declared by type <code>PriceRow</code> at extension <code>europe1</code>
	 * @param _unit initial attribute declared by type <code>PriceRow</code> at extension <code>europe1</code>
	 */
	@Deprecated
	public PriceRowModel(final CurrencyModel _currency, final Double _price, final UnitModel _unit)
	{
		super();
		setCurrency(_currency);
		setPrice(_price);
		setUnit(_unit);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _currency initial attribute declared by type <code>PriceRow</code> at extension <code>europe1</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _pg initial attribute declared by type <code>PriceRow</code> at extension <code>europe1</code>
	 * @param _price initial attribute declared by type <code>PriceRow</code> at extension <code>europe1</code>
	 * @param _product initial attribute declared by type <code>PriceRow</code> at extension <code>europe1</code>
	 * @param _productId initial attribute declared by type <code>PDTRow</code> at extension <code>europe1</code>
	 * @param _unit initial attribute declared by type <code>PriceRow</code> at extension <code>europe1</code>
	 */
	@Deprecated
	public PriceRowModel(final CurrencyModel _currency, final ItemModel _owner, final ProductPriceGroup _pg, final Double _price, final ProductModel _product, final String _productId, final UnitModel _unit)
	{
		super();
		setCurrency(_currency);
		setOwner(_owner);
		setPg(_pg);
		setPrice(_price);
		setProduct(_product);
		setProductId(_productId);
		setUnit(_unit);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceRow.catalogVersion</code> attribute defined at extension <code>europe1</code>. 
	 * @return the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceRow.channel</code> attribute defined at extension <code>europe1</code>. 
	 * @return the channel
	 */
	@Accessor(qualifier = "channel", type = Accessor.Type.GETTER)
	public PriceRowChannel getChannel()
	{
		return getPersistenceContext().getPropertyValue(CHANNEL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceRow.currency</code> attribute defined at extension <code>europe1</code>. 
	 * @return the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
	public CurrencyModel getCurrency()
	{
		return getPersistenceContext().getPropertyValue(CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceRow.giveAwayPrice</code> attribute defined at extension <code>europe1</code>. 
	 * @return the giveAwayPrice
	 */
	@Accessor(qualifier = "giveAwayPrice", type = Accessor.Type.GETTER)
	public Boolean getGiveAwayPrice()
	{
		return getPersistenceContext().getPropertyValue(GIVEAWAYPRICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceRow.matchValue</code> attribute defined at extension <code>europe1</code>. 
	 * @return the matchValue
	 */
	@Accessor(qualifier = "matchValue", type = Accessor.Type.GETTER)
	public Integer getMatchValue()
	{
		return getPersistenceContext().getPropertyValue(MATCHVALUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceRow.minqtd</code> attribute defined at extension <code>europe1</code>. 
	 * @return the minqtd
	 */
	@Accessor(qualifier = "minqtd", type = Accessor.Type.GETTER)
	public Long getMinqtd()
	{
		return getPersistenceContext().getPropertyValue(MINQTD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceRow.net</code> attribute defined at extension <code>europe1</code>. 
	 * @return the net
	 */
	@Accessor(qualifier = "net", type = Accessor.Type.GETTER)
	public Boolean getNet()
	{
		return getPersistenceContext().getPropertyValue(NET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceRow.price</code> attribute defined at extension <code>europe1</code>. 
	 * @return the price
	 */
	@Accessor(qualifier = "price", type = Accessor.Type.GETTER)
	public Double getPrice()
	{
		return getPersistenceContext().getPropertyValue(PRICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceRow.sequenceId</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the sequenceId - Attribute is used in batch import process for avoiding multiple imports.
	 */
	@Accessor(qualifier = "sequenceId", type = Accessor.Type.GETTER)
	public Long getSequenceId()
	{
		return getPersistenceContext().getPropertyValue(SEQUENCEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceRow.unit</code> attribute defined at extension <code>europe1</code>. 
	 * @return the unit
	 */
	@Accessor(qualifier = "unit", type = Accessor.Type.GETTER)
	public UnitModel getUnit()
	{
		return getPersistenceContext().getPropertyValue(UNIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceRow.unitFactor</code> attribute defined at extension <code>europe1</code>. 
	 * @return the unitFactor
	 */
	@Accessor(qualifier = "unitFactor", type = Accessor.Type.GETTER)
	public Integer getUnitFactor()
	{
		return getPersistenceContext().getPropertyValue(UNITFACTOR);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PriceRow.catalogVersion</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PriceRow.channel</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the channel
	 */
	@Accessor(qualifier = "channel", type = Accessor.Type.SETTER)
	public void setChannel(final PriceRowChannel value)
	{
		getPersistenceContext().setPropertyValue(CHANNEL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PriceRow.currency</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
	public void setCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(CURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PriceRow.giveAwayPrice</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the giveAwayPrice
	 */
	@Accessor(qualifier = "giveAwayPrice", type = Accessor.Type.SETTER)
	public void setGiveAwayPrice(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(GIVEAWAYPRICE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PriceRow.matchValue</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the matchValue
	 */
	@Accessor(qualifier = "matchValue", type = Accessor.Type.SETTER)
	public void setMatchValue(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MATCHVALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PriceRow.minqtd</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the minqtd
	 */
	@Accessor(qualifier = "minqtd", type = Accessor.Type.SETTER)
	public void setMinqtd(final Long value)
	{
		getPersistenceContext().setPropertyValue(MINQTD, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PriceRow.net</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the net
	 */
	@Accessor(qualifier = "net", type = Accessor.Type.SETTER)
	public void setNet(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(NET, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>PDTRow.pg</code> attribute defined at extension <code>europe1</code> and redeclared at extension <code>europe1</code>. Can only be used at creation of model - before first save. Will only accept values of type {@link de.hybris.platform.europe1.enums.ProductPriceGroup}.  
	 *  
	 * @param value the pg
	 */
	@Override
	@Accessor(qualifier = "pg", type = Accessor.Type.SETTER)
	public void setPg(final HybrisEnumValue value)
	{
		if( value == null || value instanceof ProductPriceGroup)
		{
			super.setPg(value);
		}
		else
		{
			throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.europe1.enums.ProductPriceGroup");
		}
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PriceRow.price</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the price
	 */
	@Accessor(qualifier = "price", type = Accessor.Type.SETTER)
	public void setPrice(final Double value)
	{
		getPersistenceContext().setPropertyValue(PRICE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>PDTRow.product</code> attribute defined at extension <code>europe1</code> and redeclared at extension <code>europe1</code>. Can only be used at creation of model - before first save. Will only accept values of type {@link de.hybris.platform.core.model.product.ProductModel}.  
	 *  
	 * @param value the product
	 */
	@Override
	@Accessor(qualifier = "product", type = Accessor.Type.SETTER)
	public void setProduct(final ProductModel value)
	{
		super.setProduct(value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PriceRow.sequenceId</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the sequenceId - Attribute is used in batch import process for avoiding multiple imports.
	 */
	@Accessor(qualifier = "sequenceId", type = Accessor.Type.SETTER)
	public void setSequenceId(final Long value)
	{
		getPersistenceContext().setPropertyValue(SEQUENCEID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PriceRow.unit</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the unit
	 */
	@Accessor(qualifier = "unit", type = Accessor.Type.SETTER)
	public void setUnit(final UnitModel value)
	{
		getPersistenceContext().setPropertyValue(UNIT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PriceRow.unitFactor</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the unitFactor
	 */
	@Accessor(qualifier = "unitFactor", type = Accessor.Type.SETTER)
	public void setUnitFactor(final Integer value)
	{
		getPersistenceContext().setPropertyValue(UNITFACTOR, value);
	}
	
}
