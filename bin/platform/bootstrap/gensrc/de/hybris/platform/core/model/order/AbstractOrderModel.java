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
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.model.promotions.PromotionOrderRestrictionModel;
import de.hybris.platform.core.enums.DeliveryStatus;
import de.hybris.platform.core.enums.ExportStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.enums.PaymentStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.europe1.enums.UserDiscountGroup;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.enums.UserTaxGroup;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Generated model class for type AbstractOrder first defined at extension core.
 */
@SuppressWarnings("all")
public class AbstractOrderModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractOrder";
	
	/**<i>Generated relation code constant for relation <code>ConsignmentOrderRelation</code> defining source attribute <code>consignments</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _CONSIGNMENTORDERRELATION = "ConsignmentOrderRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.calculated</code> attribute defined at extension <code>core</code>. */
	public static final String CALCULATED = "calculated";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.code</code> attribute defined at extension <code>core</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.currency</code> attribute defined at extension <code>core</code>. */
	public static final String CURRENCY = "currency";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.date</code> attribute defined at extension <code>core</code>. */
	public static final String DATE = "date";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.deliveryAddress</code> attribute defined at extension <code>core</code>. */
	public static final String DELIVERYADDRESS = "deliveryAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.deliveryCost</code> attribute defined at extension <code>core</code>. */
	public static final String DELIVERYCOST = "deliveryCost";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.deliveryMode</code> attribute defined at extension <code>core</code>. */
	public static final String DELIVERYMODE = "deliveryMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.deliveryStatus</code> attribute defined at extension <code>core</code>. */
	public static final String DELIVERYSTATUS = "deliveryStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.description</code> attribute defined at extension <code>core</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.expirationTime</code> attribute defined at extension <code>core</code>. */
	public static final String EXPIRATIONTIME = "expirationTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.globalDiscountValuesInternal</code> attribute defined at extension <code>core</code>. */
	public static final String GLOBALDISCOUNTVALUESINTERNAL = "globalDiscountValuesInternal";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.globalDiscountValues</code> attribute defined at extension <code>core</code>. */
	public static final String GLOBALDISCOUNTVALUES = "globalDiscountValues";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.name</code> attribute defined at extension <code>core</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.net</code> attribute defined at extension <code>core</code>. */
	public static final String NET = "net";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.paymentAddress</code> attribute defined at extension <code>core</code>. */
	public static final String PAYMENTADDRESS = "paymentAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.paymentCost</code> attribute defined at extension <code>core</code>. */
	public static final String PAYMENTCOST = "paymentCost";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.paymentInfo</code> attribute defined at extension <code>core</code>. */
	public static final String PAYMENTINFO = "paymentInfo";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.paymentMode</code> attribute defined at extension <code>core</code>. */
	public static final String PAYMENTMODE = "paymentMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.paymentStatus</code> attribute defined at extension <code>core</code>. */
	public static final String PAYMENTSTATUS = "paymentStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.status</code> attribute defined at extension <code>core</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.exportStatus</code> attribute defined at extension <code>core</code>. */
	public static final String EXPORTSTATUS = "exportStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.statusInfo</code> attribute defined at extension <code>core</code>. */
	public static final String STATUSINFO = "statusInfo";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.totalPrice</code> attribute defined at extension <code>core</code>. */
	public static final String TOTALPRICE = "totalPrice";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.totalDiscounts</code> attribute defined at extension <code>core</code>. */
	public static final String TOTALDISCOUNTS = "totalDiscounts";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.totalTax</code> attribute defined at extension <code>core</code>. */
	public static final String TOTALTAX = "totalTax";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.totalTaxValuesInternal</code> attribute defined at extension <code>core</code>. */
	public static final String TOTALTAXVALUESINTERNAL = "totalTaxValuesInternal";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.totalTaxValues</code> attribute defined at extension <code>core</code>. */
	public static final String TOTALTAXVALUES = "totalTaxValues";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.user</code> attribute defined at extension <code>core</code>. */
	public static final String USER = "user";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.subtotal</code> attribute defined at extension <code>core</code>. */
	public static final String SUBTOTAL = "subtotal";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.discountsIncludeDeliveryCost</code> attribute defined at extension <code>core</code>. */
	public static final String DISCOUNTSINCLUDEDELIVERYCOST = "discountsIncludeDeliveryCost";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.discountsIncludePaymentCost</code> attribute defined at extension <code>core</code>. */
	public static final String DISCOUNTSINCLUDEPAYMENTCOST = "discountsIncludePaymentCost";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.entryGroups</code> attribute defined at extension <code>core</code>. */
	public static final String ENTRYGROUPS = "entryGroups";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.entries</code> attribute defined at extension <code>core</code>. */
	public static final String ENTRIES = "entries";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.discounts</code> attribute defined at extension <code>core</code>. */
	public static final String DISCOUNTS = "discounts";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.Europe1PriceFactory_UDG</code> attribute defined at extension <code>europe1</code>. */
	public static final String EUROPE1PRICEFACTORY_UDG = "Europe1PriceFactory_UDG";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.Europe1PriceFactory_UPG</code> attribute defined at extension <code>europe1</code>. */
	public static final String EUROPE1PRICEFACTORY_UPG = "Europe1PriceFactory_UPG";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.Europe1PriceFactory_UTG</code> attribute defined at extension <code>europe1</code>. */
	public static final String EUROPE1PRICEFACTORY_UTG = "Europe1PriceFactory_UTG";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.appliedVoucherCodes</code> attribute defined at extension <code>voucher</code>. */
	public static final String APPLIEDVOUCHERCODES = "appliedVoucherCodes";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.previousDeliveryMode</code> attribute defined at extension <code>promotions</code>. */
	public static final String PREVIOUSDELIVERYMODE = "previousDeliveryMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.allPromotionResults</code> attribute defined at extension <code>promotions</code>. */
	public static final String ALLPROMOTIONRESULTS = "allPromotionResults";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.consignments</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CONSIGNMENTS = "consignments";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.paymentTransactions</code> attribute defined at extension <code>payment</code>. */
	public static final String PAYMENTTRANSACTIONS = "paymentTransactions";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.site</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String SITE = "site";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.store</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String STORE = "store";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.guid</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String GUID = "guid";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.quoteDiscountValuesInternal</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String QUOTEDISCOUNTVALUESINTERNAL = "quoteDiscountValuesInternal";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.promotionOrderRestrictions</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String PROMOTIONORDERRESTRICTIONS = "promotionOrderRestrictions";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.consentReference</code> attribute defined at extension <code>yprofileprocessadapter</code>. */
	public static final String CONSENTREFERENCE = "consentReference";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.cartIdReference</code> attribute defined at extension <code>yprofileprocessadapter</code>. */
	public static final String CARTIDREFERENCE = "cartIdReference";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrder.appliedCouponCodes</code> attribute defined at extension <code>couponservices</code>. */
	public static final String APPLIEDCOUPONCODES = "appliedCouponCodes";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractOrderModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractOrderModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _currency initial attribute declared by type <code>AbstractOrder</code> at extension <code>core</code>
	 * @param _date initial attribute declared by type <code>AbstractOrder</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>AbstractOrder</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractOrderModel(final CurrencyModel _currency, final Date _date, final UserModel _user)
	{
		super();
		setCurrency(_currency);
		setDate(_date);
		setUser(_user);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _currency initial attribute declared by type <code>AbstractOrder</code> at extension <code>core</code>
	 * @param _date initial attribute declared by type <code>AbstractOrder</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>AbstractOrder</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractOrderModel(final CurrencyModel _currency, final Date _date, final ItemModel _owner, final UserModel _user)
	{
		super();
		setCurrency(_currency);
		setDate(_date);
		setOwner(_owner);
		setUser(_user);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.allPromotionResults</code> attribute defined at extension <code>promotions</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allPromotionResults
	 */
	@Accessor(qualifier = "allPromotionResults", type = Accessor.Type.GETTER)
	public Set<PromotionResultModel> getAllPromotionResults()
	{
		return getPersistenceContext().getPropertyValue(ALLPROMOTIONRESULTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.appliedCouponCodes</code> attribute defined at extension <code>couponservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the appliedCouponCodes
	 */
	@Accessor(qualifier = "appliedCouponCodes", type = Accessor.Type.GETTER)
	public Collection<String> getAppliedCouponCodes()
	{
		return getPersistenceContext().getPropertyValue(APPLIEDCOUPONCODES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.calculated</code> attribute defined at extension <code>core</code>. 
	 * @return the calculated
	 */
	@Accessor(qualifier = "calculated", type = Accessor.Type.GETTER)
	public Boolean getCalculated()
	{
		final Boolean value = getPersistenceContext().getPropertyValue(CALCULATED);
		return value != null ? value : Boolean.valueOf(false);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.cartIdReference</code> attribute defined at extension <code>yprofileprocessadapter</code>. 
	 * @return the cartIdReference
	 */
	@Accessor(qualifier = "cartIdReference", type = Accessor.Type.GETTER)
	public String getCartIdReference()
	{
		return getPersistenceContext().getPropertyValue(CARTIDREFERENCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.code</code> attribute defined at extension <code>core</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.consentReference</code> attribute defined at extension <code>yprofileprocessadapter</code>. 
	 * @return the consentReference
	 */
	@Accessor(qualifier = "consentReference", type = Accessor.Type.GETTER)
	public String getConsentReference()
	{
		return getPersistenceContext().getPropertyValue(CONSENTREFERENCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.consignments</code> attribute defined at extension <code>basecommerce</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the consignments
	 */
	@Accessor(qualifier = "consignments", type = Accessor.Type.GETTER)
	public Set<ConsignmentModel> getConsignments()
	{
		return getPersistenceContext().getPropertyValue(CONSIGNMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.currency</code> attribute defined at extension <code>core</code>. 
	 * @return the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
	public CurrencyModel getCurrency()
	{
		return getPersistenceContext().getPropertyValue(CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.date</code> attribute defined at extension <code>core</code>. 
	 * @return the date
	 */
	@Accessor(qualifier = "date", type = Accessor.Type.GETTER)
	public Date getDate()
	{
		return getPersistenceContext().getPropertyValue(DATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.deliveryAddress</code> attribute defined at extension <code>core</code>. 
	 * @return the deliveryAddress
	 */
	@Accessor(qualifier = "deliveryAddress", type = Accessor.Type.GETTER)
	public AddressModel getDeliveryAddress()
	{
		return getPersistenceContext().getPropertyValue(DELIVERYADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.deliveryCost</code> attribute defined at extension <code>core</code>. 
	 * @return the deliveryCost
	 */
	@Accessor(qualifier = "deliveryCost", type = Accessor.Type.GETTER)
	public Double getDeliveryCost()
	{
		return getPersistenceContext().getPropertyValue(DELIVERYCOST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.deliveryMode</code> attribute defined at extension <code>core</code>. 
	 * @return the deliveryMode
	 */
	@Accessor(qualifier = "deliveryMode", type = Accessor.Type.GETTER)
	public DeliveryModeModel getDeliveryMode()
	{
		return getPersistenceContext().getPropertyValue(DELIVERYMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.deliveryStatus</code> attribute defined at extension <code>core</code>. 
	 * @return the deliveryStatus
	 */
	@Accessor(qualifier = "deliveryStatus", type = Accessor.Type.GETTER)
	public DeliveryStatus getDeliveryStatus()
	{
		return getPersistenceContext().getPropertyValue(DELIVERYSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.description</code> attribute defined at extension <code>core</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getPersistenceContext().getPropertyValue(DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.discounts</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the discounts
	 */
	@Accessor(qualifier = "discounts", type = Accessor.Type.GETTER)
	public List<DiscountModel> getDiscounts()
	{
		return getPersistenceContext().getPropertyValue(DISCOUNTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.entries</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the entries
	 */
	@Accessor(qualifier = "entries", type = Accessor.Type.GETTER)
	public List<AbstractOrderEntryModel> getEntries()
	{
		return getPersistenceContext().getPropertyValue(ENTRIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.entryGroups</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the entryGroups - List of entry groups for this order.
	 */
	@Accessor(qualifier = "entryGroups", type = Accessor.Type.GETTER)
	public List<EntryGroup> getEntryGroups()
	{
		return getPersistenceContext().getPropertyValue(ENTRYGROUPS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.Europe1PriceFactory_UDG</code> attribute defined at extension <code>europe1</code>. 
	 * @return the Europe1PriceFactory_UDG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_UDG", type = Accessor.Type.GETTER)
	public UserDiscountGroup getEurope1PriceFactory_UDG()
	{
		return getPersistenceContext().getPropertyValue(EUROPE1PRICEFACTORY_UDG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.Europe1PriceFactory_UPG</code> attribute defined at extension <code>europe1</code>. 
	 * @return the Europe1PriceFactory_UPG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_UPG", type = Accessor.Type.GETTER)
	public UserPriceGroup getEurope1PriceFactory_UPG()
	{
		return getPersistenceContext().getPropertyValue(EUROPE1PRICEFACTORY_UPG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.Europe1PriceFactory_UTG</code> attribute defined at extension <code>europe1</code>. 
	 * @return the Europe1PriceFactory_UTG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_UTG", type = Accessor.Type.GETTER)
	public UserTaxGroup getEurope1PriceFactory_UTG()
	{
		return getPersistenceContext().getPropertyValue(EUROPE1PRICEFACTORY_UTG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.expirationTime</code> attribute defined at extension <code>core</code>. 
	 * @return the expirationTime - The date/time when the order will expire
	 */
	@Accessor(qualifier = "expirationTime", type = Accessor.Type.GETTER)
	public Date getExpirationTime()
	{
		return getPersistenceContext().getPropertyValue(EXPIRATIONTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.exportStatus</code> attribute defined at extension <code>core</code>. 
	 * @return the exportStatus
	 */
	@Accessor(qualifier = "exportStatus", type = Accessor.Type.GETTER)
	public ExportStatus getExportStatus()
	{
		return getPersistenceContext().getPropertyValue(EXPORTSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.globalDiscountValues</code> dynamic attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the globalDiscountValues
	 */
	@Accessor(qualifier = "globalDiscountValues", type = Accessor.Type.GETTER)
	public List<DiscountValue> getGlobalDiscountValues()
	{
		return getPersistenceContext().getDynamicValue(this,GLOBALDISCOUNTVALUES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.globalDiscountValuesInternal</code> attribute defined at extension <code>core</code>. 
	 * @return the globalDiscountValuesInternal
	 */
	@Accessor(qualifier = "globalDiscountValuesInternal", type = Accessor.Type.GETTER)
	public String getGlobalDiscountValuesInternal()
	{
		return getPersistenceContext().getPropertyValue(GLOBALDISCOUNTVALUESINTERNAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.guid</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the guid - The guid for the anonymous cart used to lookup stored carts.
	 * 							The order guid is used as a non-authenticated deep link to the order history page.
	 */
	@Accessor(qualifier = "guid", type = Accessor.Type.GETTER)
	public String getGuid()
	{
		return getPersistenceContext().getPropertyValue(GUID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.name</code> attribute defined at extension <code>core</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.net</code> attribute defined at extension <code>core</code>. 
	 * @return the net
	 */
	@Accessor(qualifier = "net", type = Accessor.Type.GETTER)
	public Boolean getNet()
	{
		return getPersistenceContext().getPropertyValue(NET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.paymentAddress</code> attribute defined at extension <code>core</code>. 
	 * @return the paymentAddress
	 */
	@Accessor(qualifier = "paymentAddress", type = Accessor.Type.GETTER)
	public AddressModel getPaymentAddress()
	{
		return getPersistenceContext().getPropertyValue(PAYMENTADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.paymentCost</code> attribute defined at extension <code>core</code>. 
	 * @return the paymentCost
	 */
	@Accessor(qualifier = "paymentCost", type = Accessor.Type.GETTER)
	public Double getPaymentCost()
	{
		return getPersistenceContext().getPropertyValue(PAYMENTCOST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.paymentInfo</code> attribute defined at extension <code>core</code>. 
	 * @return the paymentInfo
	 */
	@Accessor(qualifier = "paymentInfo", type = Accessor.Type.GETTER)
	public PaymentInfoModel getPaymentInfo()
	{
		return getPersistenceContext().getPropertyValue(PAYMENTINFO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.paymentMode</code> attribute defined at extension <code>core</code>. 
	 * @return the paymentMode
	 */
	@Accessor(qualifier = "paymentMode", type = Accessor.Type.GETTER)
	public PaymentModeModel getPaymentMode()
	{
		return getPersistenceContext().getPropertyValue(PAYMENTMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.paymentStatus</code> attribute defined at extension <code>core</code>. 
	 * @return the paymentStatus
	 */
	@Accessor(qualifier = "paymentStatus", type = Accessor.Type.GETTER)
	public PaymentStatus getPaymentStatus()
	{
		return getPersistenceContext().getPropertyValue(PAYMENTSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.paymentTransactions</code> attribute defined at extension <code>payment</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the paymentTransactions
	 */
	@Accessor(qualifier = "paymentTransactions", type = Accessor.Type.GETTER)
	public List<PaymentTransactionModel> getPaymentTransactions()
	{
		return getPersistenceContext().getPropertyValue(PAYMENTTRANSACTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.previousDeliveryMode</code> attribute defined at extension <code>promotions</code>. 
	 * @return the previousDeliveryMode - The old delivery mode stored by the PromotionOrderChangeDeliveryModeAction.
	 */
	@Accessor(qualifier = "previousDeliveryMode", type = Accessor.Type.GETTER)
	public DeliveryModeModel getPreviousDeliveryMode()
	{
		return getPersistenceContext().getPropertyValue(PREVIOUSDELIVERYMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.promotionOrderRestrictions</code> attribute defined at extension <code>commerceservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the promotionOrderRestrictions - Promotion restrictions for order
	 */
	@Accessor(qualifier = "promotionOrderRestrictions", type = Accessor.Type.GETTER)
	public Collection<PromotionOrderRestrictionModel> getPromotionOrderRestrictions()
	{
		return getPersistenceContext().getPropertyValue(PROMOTIONORDERRESTRICTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.quoteDiscountValuesInternal</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the quoteDiscountValuesInternal
	 */
	@Accessor(qualifier = "quoteDiscountValuesInternal", type = Accessor.Type.GETTER)
	public String getQuoteDiscountValuesInternal()
	{
		return getPersistenceContext().getPropertyValue(QUOTEDISCOUNTVALUESINTERNAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.site</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the site - The site on which the cart was created and the order was placed.
	 */
	@Accessor(qualifier = "site", type = Accessor.Type.GETTER)
	public BaseSiteModel getSite()
	{
		return getPersistenceContext().getPropertyValue(SITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.status</code> attribute defined at extension <code>core</code>. 
	 * @return the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public OrderStatus getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.statusInfo</code> attribute defined at extension <code>core</code>. 
	 * @return the statusInfo
	 */
	@Accessor(qualifier = "statusInfo", type = Accessor.Type.GETTER)
	public String getStatusInfo()
	{
		return getPersistenceContext().getPropertyValue(STATUSINFO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.store</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the store - The store for which the cart was created and the order was placed.
	 */
	@Accessor(qualifier = "store", type = Accessor.Type.GETTER)
	public BaseStoreModel getStore()
	{
		return getPersistenceContext().getPropertyValue(STORE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.subtotal</code> attribute defined at extension <code>core</code>. 
	 * @return the subtotal
	 */
	@Accessor(qualifier = "subtotal", type = Accessor.Type.GETTER)
	public Double getSubtotal()
	{
		return getPersistenceContext().getPropertyValue(SUBTOTAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.totalDiscounts</code> attribute defined at extension <code>core</code>. 
	 * @return the totalDiscounts
	 */
	@Accessor(qualifier = "totalDiscounts", type = Accessor.Type.GETTER)
	public Double getTotalDiscounts()
	{
		return getPersistenceContext().getPropertyValue(TOTALDISCOUNTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.totalPrice</code> attribute defined at extension <code>core</code>. 
	 * @return the totalPrice
	 */
	@Accessor(qualifier = "totalPrice", type = Accessor.Type.GETTER)
	public Double getTotalPrice()
	{
		return getPersistenceContext().getPropertyValue(TOTALPRICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.totalTax</code> attribute defined at extension <code>core</code>. 
	 * @return the totalTax
	 */
	@Accessor(qualifier = "totalTax", type = Accessor.Type.GETTER)
	public Double getTotalTax()
	{
		return getPersistenceContext().getPropertyValue(TOTALTAX);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.totalTaxValues</code> dynamic attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the totalTaxValues
	 */
	@Accessor(qualifier = "totalTaxValues", type = Accessor.Type.GETTER)
	public Collection<TaxValue> getTotalTaxValues()
	{
		return getPersistenceContext().getDynamicValue(this,TOTALTAXVALUES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.totalTaxValuesInternal</code> attribute defined at extension <code>core</code>. 
	 * @return the totalTaxValuesInternal
	 */
	@Accessor(qualifier = "totalTaxValuesInternal", type = Accessor.Type.GETTER)
	public String getTotalTaxValuesInternal()
	{
		return getPersistenceContext().getPropertyValue(TOTALTAXVALUESINTERNAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.user</code> attribute defined at extension <code>core</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.discountsIncludeDeliveryCost</code> attribute defined at extension <code>core</code>. 
	 * @return the discountsIncludeDeliveryCost - Tells whether delivery costs should be included in discount calculation or not. If this
	 *                         field is true
	 *                         delivery costs are changed the same way as product costs if discount values are set at this
	 *                         order.
	 */
	@Accessor(qualifier = "discountsIncludeDeliveryCost", type = Accessor.Type.GETTER)
	public boolean isDiscountsIncludeDeliveryCost()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(DISCOUNTSINCLUDEDELIVERYCOST));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.discountsIncludePaymentCost</code> attribute defined at extension <code>core</code>. 
	 * @return the discountsIncludePaymentCost - Tells whether payment costs should be included in discount calculation or not. If this
	 *                         field is true
	 *                         payment costs are changed the same way as product costs if discount values are set at this
	 *                         order.
	 */
	@Accessor(qualifier = "discountsIncludePaymentCost", type = Accessor.Type.GETTER)
	public boolean isDiscountsIncludePaymentCost()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(DISCOUNTSINCLUDEPAYMENTCOST));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.allPromotionResults</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the allPromotionResults
	 */
	@Accessor(qualifier = "allPromotionResults", type = Accessor.Type.SETTER)
	public void setAllPromotionResults(final Set<PromotionResultModel> value)
	{
		getPersistenceContext().setPropertyValue(ALLPROMOTIONRESULTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.appliedCouponCodes</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the appliedCouponCodes
	 */
	@Accessor(qualifier = "appliedCouponCodes", type = Accessor.Type.SETTER)
	public void setAppliedCouponCodes(final Collection<String> value)
	{
		getPersistenceContext().setPropertyValue(APPLIEDCOUPONCODES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.calculated</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the calculated
	 */
	@Accessor(qualifier = "calculated", type = Accessor.Type.SETTER)
	public void setCalculated(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(CALCULATED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.cartIdReference</code> attribute defined at extension <code>yprofileprocessadapter</code>. 
	 *  
	 * @param value the cartIdReference
	 */
	@Accessor(qualifier = "cartIdReference", type = Accessor.Type.SETTER)
	public void setCartIdReference(final String value)
	{
		getPersistenceContext().setPropertyValue(CARTIDREFERENCE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.code</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.consentReference</code> attribute defined at extension <code>yprofileprocessadapter</code>. 
	 *  
	 * @param value the consentReference
	 */
	@Accessor(qualifier = "consentReference", type = Accessor.Type.SETTER)
	public void setConsentReference(final String value)
	{
		getPersistenceContext().setPropertyValue(CONSENTREFERENCE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.consignments</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the consignments
	 */
	@Accessor(qualifier = "consignments", type = Accessor.Type.SETTER)
	public void setConsignments(final Set<ConsignmentModel> value)
	{
		getPersistenceContext().setPropertyValue(CONSIGNMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.currency</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
	public void setCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(CURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.date</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the date
	 */
	@Accessor(qualifier = "date", type = Accessor.Type.SETTER)
	public void setDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(DATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.deliveryAddress</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the deliveryAddress
	 */
	@Accessor(qualifier = "deliveryAddress", type = Accessor.Type.SETTER)
	public void setDeliveryAddress(final AddressModel value)
	{
		getPersistenceContext().setPropertyValue(DELIVERYADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.deliveryCost</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the deliveryCost
	 */
	@Accessor(qualifier = "deliveryCost", type = Accessor.Type.SETTER)
	public void setDeliveryCost(final Double value)
	{
		getPersistenceContext().setPropertyValue(DELIVERYCOST, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.deliveryMode</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the deliveryMode
	 */
	@Accessor(qualifier = "deliveryMode", type = Accessor.Type.SETTER)
	public void setDeliveryMode(final DeliveryModeModel value)
	{
		getPersistenceContext().setPropertyValue(DELIVERYMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.deliveryStatus</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the deliveryStatus
	 */
	@Accessor(qualifier = "deliveryStatus", type = Accessor.Type.SETTER)
	public void setDeliveryStatus(final DeliveryStatus value)
	{
		getPersistenceContext().setPropertyValue(DELIVERYSTATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.description</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		getPersistenceContext().setPropertyValue(DESCRIPTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.discounts</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the discounts
	 */
	@Accessor(qualifier = "discounts", type = Accessor.Type.SETTER)
	public void setDiscounts(final List<DiscountModel> value)
	{
		getPersistenceContext().setPropertyValue(DISCOUNTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.discountsIncludeDeliveryCost</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the discountsIncludeDeliveryCost - Tells whether delivery costs should be included in discount calculation or not. If this
	 *                         field is true
	 *                         delivery costs are changed the same way as product costs if discount values are set at this
	 *                         order.
	 */
	@Accessor(qualifier = "discountsIncludeDeliveryCost", type = Accessor.Type.SETTER)
	public void setDiscountsIncludeDeliveryCost(final boolean value)
	{
		getPersistenceContext().setPropertyValue(DISCOUNTSINCLUDEDELIVERYCOST, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.discountsIncludePaymentCost</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the discountsIncludePaymentCost - Tells whether payment costs should be included in discount calculation or not. If this
	 *                         field is true
	 *                         payment costs are changed the same way as product costs if discount values are set at this
	 *                         order.
	 */
	@Accessor(qualifier = "discountsIncludePaymentCost", type = Accessor.Type.SETTER)
	public void setDiscountsIncludePaymentCost(final boolean value)
	{
		getPersistenceContext().setPropertyValue(DISCOUNTSINCLUDEPAYMENTCOST, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.entries</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the entries
	 */
	@Accessor(qualifier = "entries", type = Accessor.Type.SETTER)
	public void setEntries(final List<AbstractOrderEntryModel> value)
	{
		getPersistenceContext().setPropertyValue(ENTRIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.entryGroups</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the entryGroups - List of entry groups for this order.
	 */
	@Accessor(qualifier = "entryGroups", type = Accessor.Type.SETTER)
	public void setEntryGroups(final List<EntryGroup> value)
	{
		getPersistenceContext().setPropertyValue(ENTRYGROUPS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.Europe1PriceFactory_UDG</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the Europe1PriceFactory_UDG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_UDG", type = Accessor.Type.SETTER)
	public void setEurope1PriceFactory_UDG(final UserDiscountGroup value)
	{
		getPersistenceContext().setPropertyValue(EUROPE1PRICEFACTORY_UDG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.Europe1PriceFactory_UPG</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the Europe1PriceFactory_UPG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_UPG", type = Accessor.Type.SETTER)
	public void setEurope1PriceFactory_UPG(final UserPriceGroup value)
	{
		getPersistenceContext().setPropertyValue(EUROPE1PRICEFACTORY_UPG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.Europe1PriceFactory_UTG</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the Europe1PriceFactory_UTG
	 */
	@Accessor(qualifier = "Europe1PriceFactory_UTG", type = Accessor.Type.SETTER)
	public void setEurope1PriceFactory_UTG(final UserTaxGroup value)
	{
		getPersistenceContext().setPropertyValue(EUROPE1PRICEFACTORY_UTG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.expirationTime</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the expirationTime - The date/time when the order will expire
	 */
	@Accessor(qualifier = "expirationTime", type = Accessor.Type.SETTER)
	public void setExpirationTime(final Date value)
	{
		getPersistenceContext().setPropertyValue(EXPIRATIONTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.exportStatus</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the exportStatus
	 */
	@Accessor(qualifier = "exportStatus", type = Accessor.Type.SETTER)
	public void setExportStatus(final ExportStatus value)
	{
		getPersistenceContext().setPropertyValue(EXPORTSTATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.globalDiscountValues</code> dynamic attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the globalDiscountValues
	 */
	@Accessor(qualifier = "globalDiscountValues", type = Accessor.Type.SETTER)
	public void setGlobalDiscountValues(final List<DiscountValue> value)
	{
		getPersistenceContext().setDynamicValue(this,GLOBALDISCOUNTVALUES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.globalDiscountValuesInternal</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the globalDiscountValuesInternal
	 */
	@Accessor(qualifier = "globalDiscountValuesInternal", type = Accessor.Type.SETTER)
	public void setGlobalDiscountValuesInternal(final String value)
	{
		getPersistenceContext().setPropertyValue(GLOBALDISCOUNTVALUESINTERNAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.guid</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the guid - The guid for the anonymous cart used to lookup stored carts.
	 * 							The order guid is used as a non-authenticated deep link to the order history page.
	 */
	@Accessor(qualifier = "guid", type = Accessor.Type.SETTER)
	public void setGuid(final String value)
	{
		getPersistenceContext().setPropertyValue(GUID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.name</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.net</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the net
	 */
	@Accessor(qualifier = "net", type = Accessor.Type.SETTER)
	public void setNet(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(NET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.paymentAddress</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the paymentAddress
	 */
	@Accessor(qualifier = "paymentAddress", type = Accessor.Type.SETTER)
	public void setPaymentAddress(final AddressModel value)
	{
		getPersistenceContext().setPropertyValue(PAYMENTADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.paymentCost</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the paymentCost
	 */
	@Accessor(qualifier = "paymentCost", type = Accessor.Type.SETTER)
	public void setPaymentCost(final Double value)
	{
		getPersistenceContext().setPropertyValue(PAYMENTCOST, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.paymentInfo</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the paymentInfo
	 */
	@Accessor(qualifier = "paymentInfo", type = Accessor.Type.SETTER)
	public void setPaymentInfo(final PaymentInfoModel value)
	{
		getPersistenceContext().setPropertyValue(PAYMENTINFO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.paymentMode</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the paymentMode
	 */
	@Accessor(qualifier = "paymentMode", type = Accessor.Type.SETTER)
	public void setPaymentMode(final PaymentModeModel value)
	{
		getPersistenceContext().setPropertyValue(PAYMENTMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.paymentStatus</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the paymentStatus
	 */
	@Accessor(qualifier = "paymentStatus", type = Accessor.Type.SETTER)
	public void setPaymentStatus(final PaymentStatus value)
	{
		getPersistenceContext().setPropertyValue(PAYMENTSTATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.paymentTransactions</code> attribute defined at extension <code>payment</code>. 
	 *  
	 * @param value the paymentTransactions
	 */
	@Accessor(qualifier = "paymentTransactions", type = Accessor.Type.SETTER)
	public void setPaymentTransactions(final List<PaymentTransactionModel> value)
	{
		getPersistenceContext().setPropertyValue(PAYMENTTRANSACTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.previousDeliveryMode</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the previousDeliveryMode - The old delivery mode stored by the PromotionOrderChangeDeliveryModeAction.
	 */
	@Accessor(qualifier = "previousDeliveryMode", type = Accessor.Type.SETTER)
	public void setPreviousDeliveryMode(final DeliveryModeModel value)
	{
		getPersistenceContext().setPropertyValue(PREVIOUSDELIVERYMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.promotionOrderRestrictions</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the promotionOrderRestrictions - Promotion restrictions for order
	 */
	@Accessor(qualifier = "promotionOrderRestrictions", type = Accessor.Type.SETTER)
	public void setPromotionOrderRestrictions(final Collection<PromotionOrderRestrictionModel> value)
	{
		getPersistenceContext().setPropertyValue(PROMOTIONORDERRESTRICTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.quoteDiscountValuesInternal</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the quoteDiscountValuesInternal
	 */
	@Accessor(qualifier = "quoteDiscountValuesInternal", type = Accessor.Type.SETTER)
	public void setQuoteDiscountValuesInternal(final String value)
	{
		getPersistenceContext().setPropertyValue(QUOTEDISCOUNTVALUESINTERNAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.site</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the site - The site on which the cart was created and the order was placed.
	 */
	@Accessor(qualifier = "site", type = Accessor.Type.SETTER)
	public void setSite(final BaseSiteModel value)
	{
		getPersistenceContext().setPropertyValue(SITE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.status</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final OrderStatus value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.statusInfo</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the statusInfo
	 */
	@Accessor(qualifier = "statusInfo", type = Accessor.Type.SETTER)
	public void setStatusInfo(final String value)
	{
		getPersistenceContext().setPropertyValue(STATUSINFO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.store</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the store - The store for which the cart was created and the order was placed.
	 */
	@Accessor(qualifier = "store", type = Accessor.Type.SETTER)
	public void setStore(final BaseStoreModel value)
	{
		getPersistenceContext().setPropertyValue(STORE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.subtotal</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the subtotal
	 */
	@Accessor(qualifier = "subtotal", type = Accessor.Type.SETTER)
	public void setSubtotal(final Double value)
	{
		getPersistenceContext().setPropertyValue(SUBTOTAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.totalDiscounts</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the totalDiscounts
	 */
	@Accessor(qualifier = "totalDiscounts", type = Accessor.Type.SETTER)
	public void setTotalDiscounts(final Double value)
	{
		getPersistenceContext().setPropertyValue(TOTALDISCOUNTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.totalPrice</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the totalPrice
	 */
	@Accessor(qualifier = "totalPrice", type = Accessor.Type.SETTER)
	public void setTotalPrice(final Double value)
	{
		getPersistenceContext().setPropertyValue(TOTALPRICE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.totalTax</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the totalTax
	 */
	@Accessor(qualifier = "totalTax", type = Accessor.Type.SETTER)
	public void setTotalTax(final Double value)
	{
		getPersistenceContext().setPropertyValue(TOTALTAX, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.totalTaxValues</code> dynamic attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the totalTaxValues
	 */
	@Accessor(qualifier = "totalTaxValues", type = Accessor.Type.SETTER)
	public void setTotalTaxValues(final Collection<TaxValue> value)
	{
		getPersistenceContext().setDynamicValue(this,TOTALTAXVALUES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.totalTaxValuesInternal</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the totalTaxValuesInternal
	 */
	@Accessor(qualifier = "totalTaxValuesInternal", type = Accessor.Type.SETTER)
	public void setTotalTaxValuesInternal(final String value)
	{
		getPersistenceContext().setPropertyValue(TOTALTAXVALUESINTERNAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrder.user</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
}
