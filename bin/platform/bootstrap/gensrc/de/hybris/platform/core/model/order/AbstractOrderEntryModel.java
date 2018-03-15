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
package de.hybris.platform.core.model.order;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.europe1.enums.ProductDiscountGroup;
import de.hybris.platform.europe1.enums.ProductPriceGroup;
import de.hybris.platform.europe1.enums.ProductTaxGroup;
import de.hybris.platform.order.model.AbstractOrderEntryProductInfoModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.VendorModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Generated model class for type AbstractOrderEntry first defined at extension core.
 */
@SuppressWarnings("all")
public class AbstractOrderEntryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractOrderEntry";
	
	/**<i>Generated relation code constant for relation <code>AbstractOrder2AbstractOrderEntry</code> defining source attribute <code>order</code> in extension <code>core</code>.</i>*/
	public static final String _ABSTRACTORDER2ABSTRACTORDERENTRY = "AbstractOrder2AbstractOrderEntry";
	
	/**<i>Generated relation code constant for relation <code>ConsignmentEntryOrderEntryRelation</code> defining source attribute <code>consignmentEntries</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _CONSIGNMENTENTRYORDERENTRYRELATION = "ConsignmentEntryOrderEntryRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.basePrice</code> attribute defined at extension <code>core</code>. */
	public static final String BASEPRICE = "basePrice";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.calculated</code> attribute defined at extension <code>core</code>. */
	public static final String CALCULATED = "calculated";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.discountValuesInternal</code> attribute defined at extension <code>core</code>. */
	public static final String DISCOUNTVALUESINTERNAL = "discountValuesInternal";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.discountValues</code> attribute defined at extension <code>core</code>. */
	public static final String DISCOUNTVALUES = "discountValues";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.entryNumber</code> attribute defined at extension <code>core</code>. */
	public static final String ENTRYNUMBER = "entryNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.info</code> attribute defined at extension <code>core</code>. */
	public static final String INFO = "info";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.product</code> attribute defined at extension <code>core</code>. */
	public static final String PRODUCT = "product";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.quantity</code> attribute defined at extension <code>core</code>. */
	public static final String QUANTITY = "quantity";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.taxValues</code> attribute defined at extension <code>core</code>. */
	public static final String TAXVALUES = "taxValues";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.taxValuesInternal</code> attribute defined at extension <code>core</code>. */
	public static final String TAXVALUESINTERNAL = "taxValuesInternal";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.totalPrice</code> attribute defined at extension <code>core</code>. */
	public static final String TOTALPRICE = "totalPrice";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.unit</code> attribute defined at extension <code>core</code>. */
	public static final String UNIT = "unit";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.giveAway</code> attribute defined at extension <code>core</code>. */
	public static final String GIVEAWAY = "giveAway";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.rejected</code> attribute defined at extension <code>core</code>. */
	public static final String REJECTED = "rejected";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.entryGroupNumbers</code> attribute defined at extension <code>core</code>. */
	public static final String ENTRYGROUPNUMBERS = "entryGroupNumbers";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.order</code> attribute defined at extension <code>core</code>. */
	public static final String ORDER = "order";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.productInfos</code> attribute defined at extension <code>catalog</code>. */
	public static final String PRODUCTINFOS = "productInfos";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.Europe1PriceFactory_PPG</code> attribute defined at extension <code>europe1</code>. */
	public static final String EUROPE1PRICEFACTORY_PPG = "Europe1PriceFactory_PPG";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.Europe1PriceFactory_PTG</code> attribute defined at extension <code>europe1</code>. */
	public static final String EUROPE1PRICEFACTORY_PTG = "Europe1PriceFactory_PTG";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.Europe1PriceFactory_PDG</code> attribute defined at extension <code>europe1</code>. */
	public static final String EUROPE1PRICEFACTORY_PDG = "Europe1PriceFactory_PDG";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.chosenVendor</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CHOSENVENDOR = "chosenVendor";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.deliveryAddress</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String DELIVERYADDRESS = "deliveryAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.deliveryMode</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String DELIVERYMODE = "deliveryMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.namedDeliveryDate</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String NAMEDDELIVERYDATE = "namedDeliveryDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.quantityStatus</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String QUANTITYSTATUS = "quantityStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.consignmentEntries</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CONSIGNMENTENTRIES = "consignmentEntries";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.deliveryPointOfService</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String DELIVERYPOINTOFSERVICE = "deliveryPointOfService";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.bundleNo</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String BUNDLENO = "bundleNo";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.bundleTemplate</code> attribute defined at extension <code>configurablebundleservices</code>. */
	public static final String BUNDLETEMPLATE = "bundleTemplate";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractOrderEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractOrderEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _product initial attribute declared by type <code>AbstractOrderEntry</code> at extension <code>core</code>
	 * @param _quantity initial attribute declared by type <code>AbstractOrderEntry</code> at extension <code>core</code>
	 * @param _unit initial attribute declared by type <code>AbstractOrderEntry</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractOrderEntryModel(final ProductModel _product, final Long _quantity, final UnitModel _unit)
	{
		super();
		setProduct(_product);
		setQuantity(_quantity);
		setUnit(_unit);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _product initial attribute declared by type <code>AbstractOrderEntry</code> at extension <code>core</code>
	 * @param _quantity initial attribute declared by type <code>AbstractOrderEntry</code> at extension <code>core</code>
	 * @param _unit initial attribute declared by type <code>AbstractOrderEntry</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractOrderEntryModel(final ItemModel _owner, final ProductModel _product, final Long _quantity, final UnitModel _unit)
	{
		super();
		setOwner(_owner);
		setProduct(_product);
		setQuantity(_quantity);
		setUnit(_unit);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.basePrice</code> attribute defined at extension <code>core</code>. 
	 * @return the basePrice
	 */
	@Accessor(qualifier = "basePrice", type = Accessor.Type.GETTER)
	public Double getBasePrice()
	{
		return getPersistenceContext().getPropertyValue(BASEPRICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.bundleNo</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the bundleNo - Bundle number, greater than 0 if product is part of a bundle.
	 *                         @deprecated Since 6.4: replaced with entryGroupNumbers
	 * @deprecated
	 */
	@Deprecated
	@Accessor(qualifier = "bundleNo", type = Accessor.Type.GETTER)
	public Integer getBundleNo()
	{
		return getPersistenceContext().getPropertyValue(BUNDLENO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.bundleTemplate</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 * @return the bundleTemplate - Component, the product is assigned to within a bundle. Null, if product is standalone.
	 *                         @deprecated Since 6.4: replaced with EntryGroup#externalReferenceId.
	 * @deprecated
	 */
	@Deprecated
	@Accessor(qualifier = "bundleTemplate", type = Accessor.Type.GETTER)
	public BundleTemplateModel getBundleTemplate()
	{
		return getPersistenceContext().getPropertyValue(BUNDLETEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.calculated</code> attribute defined at extension <code>core</code>. 
	 * @return the calculated
	 */
	@Accessor(qualifier = "calculated", type = Accessor.Type.GETTER)
	public Boolean getCalculated()
	{
		final Boolean value = getPersistenceContext().getPropertyValue(CALCULATED);
		return value != null ? value : Boolean.valueOf(false);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.chosenVendor</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the chosenVendor
	 */
	@Accessor(qualifier = "chosenVendor", type = Accessor.Type.GETTER)
	public VendorModel getChosenVendor()
	{
		return getPersistenceContext().getPropertyValue(CHOSENVENDOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.consignmentEntries</code> attribute defined at extension <code>basecommerce</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the consignmentEntries
	 */
	@Accessor(qualifier = "consignmentEntries", type = Accessor.Type.GETTER)
	public Set<ConsignmentEntryModel> getConsignmentEntries()
	{
		return getPersistenceContext().getPropertyValue(CONSIGNMENTENTRIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.deliveryAddress</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the deliveryAddress
	 */
	@Accessor(qualifier = "deliveryAddress", type = Accessor.Type.GETTER)
	public AddressModel getDeliveryAddress()
	{
		return getPersistenceContext().getPropertyValue(DELIVERYADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.deliveryMode</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the deliveryMode
	 */
	@Accessor(qualifier = "deliveryMode", type = Accessor.Type.GETTER)
	public DeliveryModeModel getDeliveryMode()
	{
		return getPersistenceContext().getPropertyValue(DELIVERYMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.deliveryPointOfService</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the deliveryPointOfService - The point of service to deliver to/collect from.
	 */
	@Accessor(qualifier = "deliveryPointOfService", type = Accessor.Type.GETTER)
	public PointOfServiceModel getDeliveryPointOfService()
	{
		return getPersistenceContext().getPropertyValue(DELIVERYPOINTOFSERVICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.discountValues</code> dynamic attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the discountValues
	 */
	@Accessor(qualifier = "discountValues", type = Accessor.Type.GETTER)
	public List<DiscountValue> getDiscountValues()
	{
		return getPersistenceContext().getDynamicValue(this,DISCOUNTVALUES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.discountValuesInternal</code> attribute defined at extension <code>core</code>. 
	 * @return the discountValuesInternal
	 */
	@Accessor(qualifier = "discountValuesInternal", type = Accessor.Type.GETTER)
	public String getDiscountValuesInternal()
	{
		return getPersistenceContext().getPropertyValue(DISCOUNTVALUESINTERNAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.entryGroupNumbers</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the entryGroupNumbers - List of EntryGroup numbers that this order entry belongs to.
	 */
	@Accessor(qualifier = "entryGroupNumbers", type = Accessor.Type.GETTER)
	public Set<Integer> getEntryGroupNumbers()
	{
		return getPersistenceContext().getPropertyValue(ENTRYGROUPNUMBERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.entryNumber</code> attribute defined at extension <code>core</code>. 
	 * @return the entryNumber
	 */
	@Accessor(qualifier = "entryNumber", type = Accessor.Type.GETTER)
	public Integer getEntryNumber()
	{
		return getPersistenceContext().getPropertyValue(ENTRYNUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.Europe1PriceFactory_PDG</code> attribute defined at extension <code>europe1</code>. 
	 * @return the Europe1PriceFactory_PDG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_PDG", type = Accessor.Type.GETTER)
	public ProductDiscountGroup getEurope1PriceFactory_PDG()
	{
		return getPersistenceContext().getPropertyValue(EUROPE1PRICEFACTORY_PDG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.Europe1PriceFactory_PPG</code> attribute defined at extension <code>europe1</code>. 
	 * @return the Europe1PriceFactory_PPG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_PPG", type = Accessor.Type.GETTER)
	public ProductPriceGroup getEurope1PriceFactory_PPG()
	{
		return getPersistenceContext().getPropertyValue(EUROPE1PRICEFACTORY_PPG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.Europe1PriceFactory_PTG</code> attribute defined at extension <code>europe1</code>. 
	 * @return the Europe1PriceFactory_PTG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_PTG", type = Accessor.Type.GETTER)
	public ProductTaxGroup getEurope1PriceFactory_PTG()
	{
		return getPersistenceContext().getPropertyValue(EUROPE1PRICEFACTORY_PTG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.giveAway</code> attribute defined at extension <code>core</code>. 
	 * @return the giveAway
	 */
	@Accessor(qualifier = "giveAway", type = Accessor.Type.GETTER)
	public Boolean getGiveAway()
	{
		final Boolean value = getPersistenceContext().getPropertyValue(GIVEAWAY);
		return value != null ? value : Boolean.valueOf(false);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.info</code> attribute defined at extension <code>core</code>. 
	 * @return the info
	 */
	@Accessor(qualifier = "info", type = Accessor.Type.GETTER)
	public String getInfo()
	{
		return getPersistenceContext().getPropertyValue(INFO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.namedDeliveryDate</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the namedDeliveryDate
	 */
	@Accessor(qualifier = "namedDeliveryDate", type = Accessor.Type.GETTER)
	public Date getNamedDeliveryDate()
	{
		return getPersistenceContext().getPropertyValue(NAMEDDELIVERYDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.order</code> attribute defined at extension <code>core</code>. 
	 * @return the order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.GETTER)
	public AbstractOrderModel getOrder()
	{
		return getPersistenceContext().getPropertyValue(ORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.product</code> attribute defined at extension <code>core</code>. 
	 * @return the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.GETTER)
	public ProductModel getProduct()
	{
		return getPersistenceContext().getPropertyValue(PRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.productInfos</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the productInfos
	 */
	@Accessor(qualifier = "productInfos", type = Accessor.Type.GETTER)
	public List<AbstractOrderEntryProductInfoModel> getProductInfos()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTINFOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.quantity</code> attribute defined at extension <code>core</code>. 
	 * @return the quantity
	 */
	@Accessor(qualifier = "quantity", type = Accessor.Type.GETTER)
	public Long getQuantity()
	{
		return getPersistenceContext().getPropertyValue(QUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.quantityStatus</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the quantityStatus
	 */
	@Accessor(qualifier = "quantityStatus", type = Accessor.Type.GETTER)
	public OrderEntryStatus getQuantityStatus()
	{
		return getPersistenceContext().getPropertyValue(QUANTITYSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.rejected</code> attribute defined at extension <code>core</code>. 
	 * @return the rejected
	 */
	@Accessor(qualifier = "rejected", type = Accessor.Type.GETTER)
	public Boolean getRejected()
	{
		final Boolean value = getPersistenceContext().getPropertyValue(REJECTED);
		return value != null ? value : Boolean.valueOf(false);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.taxValues</code> dynamic attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the taxValues
	 */
	@Accessor(qualifier = "taxValues", type = Accessor.Type.GETTER)
	public Collection<TaxValue> getTaxValues()
	{
		return getPersistenceContext().getDynamicValue(this,TAXVALUES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.taxValuesInternal</code> attribute defined at extension <code>core</code>. 
	 * @return the taxValuesInternal
	 */
	@Accessor(qualifier = "taxValuesInternal", type = Accessor.Type.GETTER)
	public String getTaxValuesInternal()
	{
		return getPersistenceContext().getPropertyValue(TAXVALUESINTERNAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.totalPrice</code> attribute defined at extension <code>core</code>. 
	 * @return the totalPrice
	 */
	@Accessor(qualifier = "totalPrice", type = Accessor.Type.GETTER)
	public Double getTotalPrice()
	{
		return getPersistenceContext().getPropertyValue(TOTALPRICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.unit</code> attribute defined at extension <code>core</code>. 
	 * @return the unit
	 */
	@Accessor(qualifier = "unit", type = Accessor.Type.GETTER)
	public UnitModel getUnit()
	{
		return getPersistenceContext().getPropertyValue(UNIT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.basePrice</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the basePrice
	 */
	@Accessor(qualifier = "basePrice", type = Accessor.Type.SETTER)
	public void setBasePrice(final Double value)
	{
		getPersistenceContext().setPropertyValue(BASEPRICE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.bundleNo</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the bundleNo - Bundle number, greater than 0 if product is part of a bundle.
	 *                         @deprecated Since 6.4: replaced with entryGroupNumbers
	 * @deprecated
	 */
	@Deprecated
	@Accessor(qualifier = "bundleNo", type = Accessor.Type.SETTER)
	public void setBundleNo(final Integer value)
	{
		getPersistenceContext().setPropertyValue(BUNDLENO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.bundleTemplate</code> attribute defined at extension <code>configurablebundleservices</code>. 
	 *  
	 * @param value the bundleTemplate - Component, the product is assigned to within a bundle. Null, if product is standalone.
	 *                         @deprecated Since 6.4: replaced with EntryGroup#externalReferenceId.
	 * @deprecated
	 */
	@Deprecated
	@Accessor(qualifier = "bundleTemplate", type = Accessor.Type.SETTER)
	public void setBundleTemplate(final BundleTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(BUNDLETEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.calculated</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the calculated
	 */
	@Accessor(qualifier = "calculated", type = Accessor.Type.SETTER)
	public void setCalculated(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(CALCULATED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.chosenVendor</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the chosenVendor
	 */
	@Accessor(qualifier = "chosenVendor", type = Accessor.Type.SETTER)
	public void setChosenVendor(final VendorModel value)
	{
		getPersistenceContext().setPropertyValue(CHOSENVENDOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.consignmentEntries</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the consignmentEntries
	 */
	@Accessor(qualifier = "consignmentEntries", type = Accessor.Type.SETTER)
	public void setConsignmentEntries(final Set<ConsignmentEntryModel> value)
	{
		getPersistenceContext().setPropertyValue(CONSIGNMENTENTRIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.deliveryAddress</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the deliveryAddress
	 */
	@Accessor(qualifier = "deliveryAddress", type = Accessor.Type.SETTER)
	public void setDeliveryAddress(final AddressModel value)
	{
		getPersistenceContext().setPropertyValue(DELIVERYADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.deliveryMode</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the deliveryMode
	 */
	@Accessor(qualifier = "deliveryMode", type = Accessor.Type.SETTER)
	public void setDeliveryMode(final DeliveryModeModel value)
	{
		getPersistenceContext().setPropertyValue(DELIVERYMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.deliveryPointOfService</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the deliveryPointOfService - The point of service to deliver to/collect from.
	 */
	@Accessor(qualifier = "deliveryPointOfService", type = Accessor.Type.SETTER)
	public void setDeliveryPointOfService(final PointOfServiceModel value)
	{
		getPersistenceContext().setPropertyValue(DELIVERYPOINTOFSERVICE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.discountValues</code> dynamic attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the discountValues
	 */
	@Accessor(qualifier = "discountValues", type = Accessor.Type.SETTER)
	public void setDiscountValues(final List<DiscountValue> value)
	{
		getPersistenceContext().setDynamicValue(this,DISCOUNTVALUES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.discountValuesInternal</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the discountValuesInternal
	 */
	@Accessor(qualifier = "discountValuesInternal", type = Accessor.Type.SETTER)
	public void setDiscountValuesInternal(final String value)
	{
		getPersistenceContext().setPropertyValue(DISCOUNTVALUESINTERNAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.entryGroupNumbers</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the entryGroupNumbers - List of EntryGroup numbers that this order entry belongs to.
	 */
	@Accessor(qualifier = "entryGroupNumbers", type = Accessor.Type.SETTER)
	public void setEntryGroupNumbers(final Set<Integer> value)
	{
		getPersistenceContext().setPropertyValue(ENTRYGROUPNUMBERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.entryNumber</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the entryNumber
	 */
	@Accessor(qualifier = "entryNumber", type = Accessor.Type.SETTER)
	public void setEntryNumber(final Integer value)
	{
		getPersistenceContext().setPropertyValue(ENTRYNUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.Europe1PriceFactory_PDG</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the Europe1PriceFactory_PDG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_PDG", type = Accessor.Type.SETTER)
	public void setEurope1PriceFactory_PDG(final ProductDiscountGroup value)
	{
		getPersistenceContext().setPropertyValue(EUROPE1PRICEFACTORY_PDG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.Europe1PriceFactory_PPG</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the Europe1PriceFactory_PPG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_PPG", type = Accessor.Type.SETTER)
	public void setEurope1PriceFactory_PPG(final ProductPriceGroup value)
	{
		getPersistenceContext().setPropertyValue(EUROPE1PRICEFACTORY_PPG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.Europe1PriceFactory_PTG</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the Europe1PriceFactory_PTG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_PTG", type = Accessor.Type.SETTER)
	public void setEurope1PriceFactory_PTG(final ProductTaxGroup value)
	{
		getPersistenceContext().setPropertyValue(EUROPE1PRICEFACTORY_PTG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.giveAway</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the giveAway
	 */
	@Accessor(qualifier = "giveAway", type = Accessor.Type.SETTER)
	public void setGiveAway(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(GIVEAWAY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.info</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the info
	 */
	@Accessor(qualifier = "info", type = Accessor.Type.SETTER)
	public void setInfo(final String value)
	{
		getPersistenceContext().setPropertyValue(INFO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.namedDeliveryDate</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the namedDeliveryDate
	 */
	@Accessor(qualifier = "namedDeliveryDate", type = Accessor.Type.SETTER)
	public void setNamedDeliveryDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(NAMEDDELIVERYDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.order</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.SETTER)
	public void setOrder(final AbstractOrderModel value)
	{
		getPersistenceContext().setPropertyValue(ORDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.product</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.SETTER)
	public void setProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(PRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.productInfos</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the productInfos
	 */
	@Accessor(qualifier = "productInfos", type = Accessor.Type.SETTER)
	public void setProductInfos(final List<AbstractOrderEntryProductInfoModel> value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTINFOS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.quantity</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the quantity
	 */
	@Accessor(qualifier = "quantity", type = Accessor.Type.SETTER)
	public void setQuantity(final Long value)
	{
		getPersistenceContext().setPropertyValue(QUANTITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.quantityStatus</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the quantityStatus
	 */
	@Accessor(qualifier = "quantityStatus", type = Accessor.Type.SETTER)
	public void setQuantityStatus(final OrderEntryStatus value)
	{
		getPersistenceContext().setPropertyValue(QUANTITYSTATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.rejected</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the rejected
	 */
	@Accessor(qualifier = "rejected", type = Accessor.Type.SETTER)
	public void setRejected(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(REJECTED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.taxValues</code> dynamic attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the taxValues
	 */
	@Accessor(qualifier = "taxValues", type = Accessor.Type.SETTER)
	public void setTaxValues(final Collection<TaxValue> value)
	{
		getPersistenceContext().setDynamicValue(this,TAXVALUES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.taxValuesInternal</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the taxValuesInternal
	 */
	@Accessor(qualifier = "taxValuesInternal", type = Accessor.Type.SETTER)
	public void setTaxValuesInternal(final String value)
	{
		getPersistenceContext().setPropertyValue(TAXVALUESINTERNAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.totalPrice</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the totalPrice
	 */
	@Accessor(qualifier = "totalPrice", type = Accessor.Type.SETTER)
	public void setTotalPrice(final Double value)
	{
		getPersistenceContext().setPropertyValue(TOTALPRICE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.unit</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the unit
	 */
	@Accessor(qualifier = "unit", type = Accessor.Type.SETTER)
	public void setUnit(final UnitModel value)
	{
		getPersistenceContext().setPropertyValue(UNIT, value);
	}
	
}
