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
package de.hybris.platform.core.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerinterestsservices.model.ProductInterestModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.ticket.model.CsTicketModel;
import java.util.Collection;
import java.util.List;

/**
 * Generated model class for type Customer first defined at extension core.
 */
@SuppressWarnings("all")
public class CustomerModel extends UserModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Customer";
	
	/** <i>Generated constant</i> - Attribute key of <code>Customer.customerID</code> attribute defined at extension <code>core</code>. */
	public static final String CUSTOMERID = "customerID";
	
	/** <i>Generated constant</i> - Attribute key of <code>Customer.previewCatalogVersions</code> attribute defined at extension <code>catalog</code>. */
	public static final String PREVIEWCATALOGVERSIONS = "previewCatalogVersions";
	
	/** <i>Generated constant</i> - Attribute key of <code>Customer.title</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String TITLE = "title";
	
	/** <i>Generated constant</i> - Attribute key of <code>Customer.defaultPaymentInfo</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String DEFAULTPAYMENTINFO = "defaultPaymentInfo";
	
	/** <i>Generated constant</i> - Attribute key of <code>Customer.token</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String TOKEN = "token";
	
	/** <i>Generated constant</i> - Attribute key of <code>Customer.contactEmail</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String CONTACTEMAIL = "contactEmail";
	
	/** <i>Generated constant</i> - Attribute key of <code>Customer.originalUid</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String ORIGINALUID = "originalUid";
	
	/** <i>Generated constant</i> - Attribute key of <code>Customer.type</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String TYPE = "type";
	
	/** <i>Generated constant</i> - Attribute key of <code>Customer.emailPreference</code> attribute defined at extension <code>notificationservices</code>. */
	public static final String EMAILPREFERENCE = "emailPreference";
	
	/** <i>Generated constant</i> - Attribute key of <code>Customer.smsPreference</code> attribute defined at extension <code>notificationservices</code>. */
	public static final String SMSPREFERENCE = "smsPreference";
	
	/** <i>Generated constant</i> - Attribute key of <code>Customer.tickets</code> attribute defined at extension <code>ticketsystem</code>. */
	public static final String TICKETS = "tickets";
	
	/** <i>Generated constant</i> - Attribute key of <code>Customer.productInterests</code> attribute defined at extension <code>customerinterestsservices</code>. */
	public static final String PRODUCTINTERESTS = "productInterests";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CustomerModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CustomerModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _loginDisabled initial attribute declared by type <code>User</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public CustomerModel(final boolean _loginDisabled, final String _uid)
	{
		super();
		setLoginDisabled(_loginDisabled);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _customerReviews initial attribute declared by type <code>User</code> at extension <code>customerreview</code>
	 * @param _loginDisabled initial attribute declared by type <code>User</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public CustomerModel(final Collection<CustomerReviewModel> _customerReviews, final boolean _loginDisabled, final ItemModel _owner, final String _uid)
	{
		super();
		setCustomerReviews(_customerReviews);
		setLoginDisabled(_loginDisabled);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.contactEmail</code> dynamic attribute defined at extension <code>commerceservices</code>. 
	 * @return the contactEmail - Contact email is a dynamic attribute that is used to determine contact email
	 * 							address.
	 */
	@Accessor(qualifier = "contactEmail", type = Accessor.Type.GETTER)
	public String getContactEmail()
	{
		return getPersistenceContext().getDynamicValue(this,CONTACTEMAIL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.customerID</code> attribute defined at extension <code>core</code>. 
	 * @return the customerID
	 */
	@Accessor(qualifier = "customerID", type = Accessor.Type.GETTER)
	public String getCustomerID()
	{
		return getPersistenceContext().getPropertyValue(CUSTOMERID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.defaultPaymentInfo</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the defaultPaymentInfo - It holds information about default payment that is used by the customer.
	 */
	@Accessor(qualifier = "defaultPaymentInfo", type = Accessor.Type.GETTER)
	public PaymentInfoModel getDefaultPaymentInfo()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTPAYMENTINFO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.emailPreference</code> attribute defined at extension <code>notificationservices</code>. 
	 * @return the emailPreference
	 */
	@Accessor(qualifier = "emailPreference", type = Accessor.Type.GETTER)
	public Boolean getEmailPreference()
	{
		return getPersistenceContext().getPropertyValue(EMAILPREFERENCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.originalUid</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the originalUid
	 */
	@Accessor(qualifier = "originalUid", type = Accessor.Type.GETTER)
	public String getOriginalUid()
	{
		return getPersistenceContext().getPropertyValue(ORIGINALUID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.previewCatalogVersions</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the previewCatalogVersions
	 */
	@Accessor(qualifier = "previewCatalogVersions", type = Accessor.Type.GETTER)
	public Collection<CatalogVersionModel> getPreviewCatalogVersions()
	{
		return getPersistenceContext().getPropertyValue(PREVIEWCATALOGVERSIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.productInterests</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the productInterests
	 */
	@Accessor(qualifier = "productInterests", type = Accessor.Type.GETTER)
	public Collection<ProductInterestModel> getProductInterests()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTINTERESTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.smsPreference</code> attribute defined at extension <code>notificationservices</code>. 
	 * @return the smsPreference
	 */
	@Accessor(qualifier = "smsPreference", type = Accessor.Type.GETTER)
	public Boolean getSmsPreference()
	{
		return getPersistenceContext().getPropertyValue(SMSPREFERENCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.tickets</code> dynamic attribute defined at extension <code>ticketsystem</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the tickets - Tickets is a dynamic attribute.
	 */
	@Accessor(qualifier = "tickets", type = Accessor.Type.GETTER)
	public List<CsTicketModel> getTickets()
	{
		return getPersistenceContext().getDynamicValue(this,TICKETS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.title</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the title - It holds information about customer title (i.e. Mr, Dr, etc.)
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public TitleModel getTitle()
	{
		return getPersistenceContext().getPropertyValue(TITLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.token</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the token - Attribute is used during forgotten password to ensure that the link can be used
	 * 							only once.
	 */
	@Accessor(qualifier = "token", type = Accessor.Type.GETTER)
	public String getToken()
	{
		return getPersistenceContext().getPropertyValue(TOKEN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.type</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the type - Customer type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public CustomerType getType()
	{
		return getPersistenceContext().getPropertyValue(TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Customer.customerID</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the customerID
	 */
	@Accessor(qualifier = "customerID", type = Accessor.Type.SETTER)
	public void setCustomerID(final String value)
	{
		getPersistenceContext().setPropertyValue(CUSTOMERID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Customer.defaultPaymentInfo</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the defaultPaymentInfo - It holds information about default payment that is used by the customer.
	 */
	@Accessor(qualifier = "defaultPaymentInfo", type = Accessor.Type.SETTER)
	public void setDefaultPaymentInfo(final PaymentInfoModel value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTPAYMENTINFO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Customer.emailPreference</code> attribute defined at extension <code>notificationservices</code>. 
	 *  
	 * @param value the emailPreference
	 */
	@Accessor(qualifier = "emailPreference", type = Accessor.Type.SETTER)
	public void setEmailPreference(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(EMAILPREFERENCE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Customer.originalUid</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the originalUid
	 */
	@Accessor(qualifier = "originalUid", type = Accessor.Type.SETTER)
	public void setOriginalUid(final String value)
	{
		getPersistenceContext().setPropertyValue(ORIGINALUID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Customer.previewCatalogVersions</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the previewCatalogVersions
	 */
	@Accessor(qualifier = "previewCatalogVersions", type = Accessor.Type.SETTER)
	public void setPreviewCatalogVersions(final Collection<CatalogVersionModel> value)
	{
		getPersistenceContext().setPropertyValue(PREVIEWCATALOGVERSIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Customer.productInterests</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 *  
	 * @param value the productInterests
	 */
	@Accessor(qualifier = "productInterests", type = Accessor.Type.SETTER)
	public void setProductInterests(final Collection<ProductInterestModel> value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTINTERESTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Customer.smsPreference</code> attribute defined at extension <code>notificationservices</code>. 
	 *  
	 * @param value the smsPreference
	 */
	@Accessor(qualifier = "smsPreference", type = Accessor.Type.SETTER)
	public void setSmsPreference(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SMSPREFERENCE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Customer.tickets</code> dynamic attribute defined at extension <code>ticketsystem</code>. 
	 *  
	 * @param value the tickets - Tickets is a dynamic attribute.
	 */
	@Accessor(qualifier = "tickets", type = Accessor.Type.SETTER)
	public void setTickets(final List<CsTicketModel> value)
	{
		getPersistenceContext().setDynamicValue(this,TICKETS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Customer.title</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the title - It holds information about customer title (i.e. Mr, Dr, etc.)
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final TitleModel value)
	{
		getPersistenceContext().setPropertyValue(TITLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Customer.token</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the token - Attribute is used during forgotten password to ensure that the link can be used
	 * 							only once.
	 */
	@Accessor(qualifier = "token", type = Accessor.Type.SETTER)
	public void setToken(final String value)
	{
		getPersistenceContext().setPropertyValue(TOKEN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Customer.type</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the type - Customer type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.SETTER)
	public void setType(final CustomerType value)
	{
		getPersistenceContext().setPropertyValue(TYPE, value);
	}
	
}
