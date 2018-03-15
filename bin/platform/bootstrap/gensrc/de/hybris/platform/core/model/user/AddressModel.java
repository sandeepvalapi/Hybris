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
import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.orderscheduling.model.CartToOrderCronJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;

/**
 * Generated model class for type Address first defined at extension core.
 */
@SuppressWarnings("all")
public class AddressModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Address";
	
	/**<i>Generated relation code constant for relation <code>User2Addresses</code> defining source attribute <code>owner</code> in extension <code>core</code>.</i>*/
	public static final String _USER2ADDRESSES = "User2Addresses";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.original</code> attribute defined at extension <code>core</code>. */
	public static final String ORIGINAL = "original";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.duplicate</code> attribute defined at extension <code>core</code>. */
	public static final String DUPLICATE = "duplicate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.appartment</code> attribute defined at extension <code>core</code>. */
	public static final String APPARTMENT = "appartment";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.building</code> attribute defined at extension <code>core</code>. */
	public static final String BUILDING = "building";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.cellphone</code> attribute defined at extension <code>core</code>. */
	public static final String CELLPHONE = "cellphone";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.company</code> attribute defined at extension <code>core</code>. */
	public static final String COMPANY = "company";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.country</code> attribute defined at extension <code>core</code>. */
	public static final String COUNTRY = "country";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.department</code> attribute defined at extension <code>core</code>. */
	public static final String DEPARTMENT = "department";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.district</code> attribute defined at extension <code>core</code>. */
	public static final String DISTRICT = "district";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.email</code> attribute defined at extension <code>core</code>. */
	public static final String EMAIL = "email";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.fax</code> attribute defined at extension <code>core</code>. */
	public static final String FAX = "fax";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.firstname</code> attribute defined at extension <code>core</code>. */
	public static final String FIRSTNAME = "firstname";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.lastname</code> attribute defined at extension <code>core</code>. */
	public static final String LASTNAME = "lastname";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.middlename</code> attribute defined at extension <code>core</code>. */
	public static final String MIDDLENAME = "middlename";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.middlename2</code> attribute defined at extension <code>core</code>. */
	public static final String MIDDLENAME2 = "middlename2";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.phone1</code> attribute defined at extension <code>core</code>. */
	public static final String PHONE1 = "phone1";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.phone2</code> attribute defined at extension <code>core</code>. */
	public static final String PHONE2 = "phone2";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.pobox</code> attribute defined at extension <code>core</code>. */
	public static final String POBOX = "pobox";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.postalcode</code> attribute defined at extension <code>core</code>. */
	public static final String POSTALCODE = "postalcode";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.region</code> attribute defined at extension <code>core</code>. */
	public static final String REGION = "region";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.streetname</code> attribute defined at extension <code>core</code>. */
	public static final String STREETNAME = "streetname";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.streetnumber</code> attribute defined at extension <code>core</code>. */
	public static final String STREETNUMBER = "streetnumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.title</code> attribute defined at extension <code>core</code>. */
	public static final String TITLE = "title";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.town</code> attribute defined at extension <code>core</code>. */
	public static final String TOWN = "town";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.gender</code> attribute defined at extension <code>core</code>. */
	public static final String GENDER = "gender";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.dateOfBirth</code> attribute defined at extension <code>core</code>. */
	public static final String DATEOFBIRTH = "dateOfBirth";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.remarks</code> attribute defined at extension <code>catalog</code>. */
	public static final String REMARKS = "remarks";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.publicKey</code> attribute defined at extension <code>catalog</code>. */
	public static final String PUBLICKEY = "publicKey";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.url</code> attribute defined at extension <code>catalog</code>. */
	public static final String URL = "url";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.typeQualifier</code> attribute defined at extension <code>catalog</code>. */
	public static final String TYPEQUALIFIER = "typeQualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.shippingAddress</code> attribute defined at extension <code>catalog</code>. */
	public static final String SHIPPINGADDRESS = "shippingAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.unloadingAddress</code> attribute defined at extension <code>catalog</code>. */
	public static final String UNLOADINGADDRESS = "unloadingAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.billingAddress</code> attribute defined at extension <code>catalog</code>. */
	public static final String BILLINGADDRESS = "billingAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.contactAddress</code> attribute defined at extension <code>catalog</code>. */
	public static final String CONTACTADDRESS = "contactAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.line1</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String LINE1 = "line1";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.line2</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String LINE2 = "line2";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.deliveryAddresss2CartToOrderCronJob</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String DELIVERYADDRESSS2CARTTOORDERCRONJOB = "deliveryAddresss2CartToOrderCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.paymentAddresss2CartToOrderCronJob</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String PAYMENTADDRESSS2CARTTOORDERCRONJOB = "paymentAddresss2CartToOrderCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>Address.visibleInAddressBook</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String VISIBLEINADDRESSBOOK = "visibleInAddressBook";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AddressModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AddressModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _original initial attribute declared by type <code>Address</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Address</code> at extension <code>core</code>
	 */
	@Deprecated
	public AddressModel(final AddressModel _original, final ItemModel _owner)
	{
		super();
		setOriginal(_original);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.appartment</code> attribute defined at extension <code>core</code>. 
	 * @return the appartment
	 */
	@Accessor(qualifier = "appartment", type = Accessor.Type.GETTER)
	public String getAppartment()
	{
		return getPersistenceContext().getPropertyValue(APPARTMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.billingAddress</code> attribute defined at extension <code>catalog</code>. 
	 * @return the billingAddress - billingAddress
	 */
	@Accessor(qualifier = "billingAddress", type = Accessor.Type.GETTER)
	public Boolean getBillingAddress()
	{
		return getPersistenceContext().getPropertyValue(BILLINGADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.building</code> attribute defined at extension <code>core</code>. 
	 * @return the building
	 */
	@Accessor(qualifier = "building", type = Accessor.Type.GETTER)
	public String getBuilding()
	{
		return getPersistenceContext().getPropertyValue(BUILDING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.cellphone</code> attribute defined at extension <code>core</code>. 
	 * @return the cellphone
	 */
	@Accessor(qualifier = "cellphone", type = Accessor.Type.GETTER)
	public String getCellphone()
	{
		return getPersistenceContext().getPropertyValue(CELLPHONE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.company</code> attribute defined at extension <code>core</code>. 
	 * @return the company
	 */
	@Accessor(qualifier = "company", type = Accessor.Type.GETTER)
	public String getCompany()
	{
		return getPersistenceContext().getPropertyValue(COMPANY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.contactAddress</code> attribute defined at extension <code>catalog</code>. 
	 * @return the contactAddress - contactAddress
	 */
	@Accessor(qualifier = "contactAddress", type = Accessor.Type.GETTER)
	public Boolean getContactAddress()
	{
		return getPersistenceContext().getPropertyValue(CONTACTADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.country</code> attribute defined at extension <code>core</code>. 
	 * @return the country
	 */
	@Accessor(qualifier = "country", type = Accessor.Type.GETTER)
	public CountryModel getCountry()
	{
		return getPersistenceContext().getPropertyValue(COUNTRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.dateOfBirth</code> attribute defined at extension <code>core</code>. 
	 * @return the dateOfBirth
	 * @deprecated since ages - use { @link #getDateOfBirth()} instead
	 */
	@Deprecated
	public Date getDateofbirth()
	{
		return this.getDateOfBirth();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.dateOfBirth</code> attribute defined at extension <code>core</code>. 
	 * @return the dateOfBirth
	 */
	@Accessor(qualifier = "dateOfBirth", type = Accessor.Type.GETTER)
	public Date getDateOfBirth()
	{
		return getPersistenceContext().getPropertyValue(DATEOFBIRTH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.deliveryAddresss2CartToOrderCronJob</code> attribute defined at extension <code>basecommerce</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the deliveryAddresss2CartToOrderCronJob
	 */
	@Accessor(qualifier = "deliveryAddresss2CartToOrderCronJob", type = Accessor.Type.GETTER)
	public Collection<CartToOrderCronJobModel> getDeliveryAddresss2CartToOrderCronJob()
	{
		return getPersistenceContext().getPropertyValue(DELIVERYADDRESSS2CARTTOORDERCRONJOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.department</code> attribute defined at extension <code>core</code>. 
	 * @return the department
	 */
	@Accessor(qualifier = "department", type = Accessor.Type.GETTER)
	public String getDepartment()
	{
		return getPersistenceContext().getPropertyValue(DEPARTMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.district</code> attribute defined at extension <code>core</code>. 
	 * @return the district
	 */
	@Accessor(qualifier = "district", type = Accessor.Type.GETTER)
	public String getDistrict()
	{
		return getPersistenceContext().getPropertyValue(DISTRICT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.duplicate</code> attribute defined at extension <code>core</code>. 
	 * @return the duplicate
	 */
	@Accessor(qualifier = "duplicate", type = Accessor.Type.GETTER)
	public Boolean getDuplicate()
	{
		final Boolean value = getPersistenceContext().getPropertyValue(DUPLICATE);
		return value != null ? value : Boolean.valueOf(false);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.email</code> attribute defined at extension <code>core</code>. 
	 * @return the email
	 */
	@Accessor(qualifier = "email", type = Accessor.Type.GETTER)
	public String getEmail()
	{
		return getPersistenceContext().getPropertyValue(EMAIL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.fax</code> attribute defined at extension <code>core</code>. 
	 * @return the fax
	 */
	@Accessor(qualifier = "fax", type = Accessor.Type.GETTER)
	public String getFax()
	{
		return getPersistenceContext().getPropertyValue(FAX);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.firstname</code> attribute defined at extension <code>core</code>. 
	 * @return the firstname
	 */
	@Accessor(qualifier = "firstname", type = Accessor.Type.GETTER)
	public String getFirstname()
	{
		return getPersistenceContext().getPropertyValue(FIRSTNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.gender</code> attribute defined at extension <code>core</code>. 
	 * @return the gender
	 */
	@Accessor(qualifier = "gender", type = Accessor.Type.GETTER)
	public Gender getGender()
	{
		return getPersistenceContext().getPropertyValue(GENDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.lastname</code> attribute defined at extension <code>core</code>. 
	 * @return the lastname
	 */
	@Accessor(qualifier = "lastname", type = Accessor.Type.GETTER)
	public String getLastname()
	{
		return getPersistenceContext().getPropertyValue(LASTNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.line1</code> dynamic attribute defined at extension <code>basecommerce</code>. 
	 * @return the line1 - Address line1 is a dynamic attribute that is stored in the Address.streetname field
	 */
	@Accessor(qualifier = "line1", type = Accessor.Type.GETTER)
	public String getLine1()
	{
		return getPersistenceContext().getDynamicValue(this,LINE1);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.line2</code> dynamic attribute defined at extension <code>basecommerce</code>. 
	 * @return the line2 - Address line2 is a dynamic attribute that is stored in the Address.streetnumber field
	 */
	@Accessor(qualifier = "line2", type = Accessor.Type.GETTER)
	public String getLine2()
	{
		return getPersistenceContext().getDynamicValue(this,LINE2);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.middlename</code> attribute defined at extension <code>core</code>. 
	 * @return the middlename
	 */
	@Accessor(qualifier = "middlename", type = Accessor.Type.GETTER)
	public String getMiddlename()
	{
		return getPersistenceContext().getPropertyValue(MIDDLENAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.middlename2</code> attribute defined at extension <code>core</code>. 
	 * @return the middlename2
	 */
	@Accessor(qualifier = "middlename2", type = Accessor.Type.GETTER)
	public String getMiddlename2()
	{
		return getPersistenceContext().getPropertyValue(MIDDLENAME2);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.original</code> attribute defined at extension <code>core</code>. 
	 * @return the original
	 */
	@Accessor(qualifier = "original", type = Accessor.Type.GETTER)
	public AddressModel getOriginal()
	{
		return getPersistenceContext().getPropertyValue(ORIGINAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.paymentAddresss2CartToOrderCronJob</code> attribute defined at extension <code>basecommerce</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the paymentAddresss2CartToOrderCronJob
	 */
	@Accessor(qualifier = "paymentAddresss2CartToOrderCronJob", type = Accessor.Type.GETTER)
	public Collection<CartToOrderCronJobModel> getPaymentAddresss2CartToOrderCronJob()
	{
		return getPersistenceContext().getPropertyValue(PAYMENTADDRESSS2CARTTOORDERCRONJOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.phone1</code> attribute defined at extension <code>core</code>. 
	 * @return the phone1
	 */
	@Accessor(qualifier = "phone1", type = Accessor.Type.GETTER)
	public String getPhone1()
	{
		return getPersistenceContext().getPropertyValue(PHONE1);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.phone2</code> attribute defined at extension <code>core</code>. 
	 * @return the phone2
	 */
	@Accessor(qualifier = "phone2", type = Accessor.Type.GETTER)
	public String getPhone2()
	{
		return getPersistenceContext().getPropertyValue(PHONE2);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.pobox</code> attribute defined at extension <code>core</code>. 
	 * @return the pobox
	 */
	@Accessor(qualifier = "pobox", type = Accessor.Type.GETTER)
	public String getPobox()
	{
		return getPersistenceContext().getPropertyValue(POBOX);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.postalcode</code> attribute defined at extension <code>core</code>. 
	 * @return the postalcode
	 */
	@Accessor(qualifier = "postalcode", type = Accessor.Type.GETTER)
	public String getPostalcode()
	{
		return getPersistenceContext().getPropertyValue(POSTALCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.publicKey</code> attribute defined at extension <code>catalog</code>. 
	 * @return the publicKey
	 */
	@Accessor(qualifier = "publicKey", type = Accessor.Type.GETTER)
	public String getPublicKey()
	{
		return getPersistenceContext().getPropertyValue(PUBLICKEY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.region</code> attribute defined at extension <code>core</code>. 
	 * @return the region
	 */
	@Accessor(qualifier = "region", type = Accessor.Type.GETTER)
	public RegionModel getRegion()
	{
		return getPersistenceContext().getPropertyValue(REGION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.remarks</code> attribute defined at extension <code>catalog</code>. 
	 * @return the remarks
	 */
	@Accessor(qualifier = "remarks", type = Accessor.Type.GETTER)
	public String getRemarks()
	{
		return getPersistenceContext().getPropertyValue(REMARKS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.shippingAddress</code> attribute defined at extension <code>catalog</code>. 
	 * @return the shippingAddress - shippingAddress
	 */
	@Accessor(qualifier = "shippingAddress", type = Accessor.Type.GETTER)
	public Boolean getShippingAddress()
	{
		return getPersistenceContext().getPropertyValue(SHIPPINGADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.streetname</code> attribute defined at extension <code>core</code>. 
	 * @return the streetname
	 */
	@Accessor(qualifier = "streetname", type = Accessor.Type.GETTER)
	public String getStreetname()
	{
		return getPersistenceContext().getPropertyValue(STREETNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.streetnumber</code> attribute defined at extension <code>core</code>. 
	 * @return the streetnumber
	 */
	@Accessor(qualifier = "streetnumber", type = Accessor.Type.GETTER)
	public String getStreetnumber()
	{
		return getPersistenceContext().getPropertyValue(STREETNUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.title</code> attribute defined at extension <code>core</code>. 
	 * @return the title
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public TitleModel getTitle()
	{
		return getPersistenceContext().getPropertyValue(TITLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.town</code> attribute defined at extension <code>core</code>. 
	 * @return the town
	 */
	@Accessor(qualifier = "town", type = Accessor.Type.GETTER)
	public String getTown()
	{
		return getPersistenceContext().getPropertyValue(TOWN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.typeQualifier</code> dynamic attribute defined at extension <code>catalog</code>. 
	 * @return the typeQualifier - typeQualifier
	 */
	@Accessor(qualifier = "typeQualifier", type = Accessor.Type.GETTER)
	public String getTypeQualifier()
	{
		return getPersistenceContext().getDynamicValue(this,TYPEQUALIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.unloadingAddress</code> attribute defined at extension <code>catalog</code>. 
	 * @return the unloadingAddress - unloadingAddress
	 */
	@Accessor(qualifier = "unloadingAddress", type = Accessor.Type.GETTER)
	public Boolean getUnloadingAddress()
	{
		return getPersistenceContext().getPropertyValue(UNLOADINGADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.url</code> attribute defined at extension <code>catalog</code>. 
	 * @return the url
	 */
	@Accessor(qualifier = "url", type = Accessor.Type.GETTER)
	public String getUrl()
	{
		return getPersistenceContext().getPropertyValue(URL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Address.visibleInAddressBook</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the visibleInAddressBook - Indicates if the address will be displayed to the user in the address book.
	 */
	@Accessor(qualifier = "visibleInAddressBook", type = Accessor.Type.GETTER)
	public Boolean getVisibleInAddressBook()
	{
		return getPersistenceContext().getPropertyValue(VISIBLEINADDRESSBOOK);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.appartment</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the appartment
	 */
	@Accessor(qualifier = "appartment", type = Accessor.Type.SETTER)
	public void setAppartment(final String value)
	{
		getPersistenceContext().setPropertyValue(APPARTMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.billingAddress</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the billingAddress - billingAddress
	 */
	@Accessor(qualifier = "billingAddress", type = Accessor.Type.SETTER)
	public void setBillingAddress(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(BILLINGADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.building</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the building
	 */
	@Accessor(qualifier = "building", type = Accessor.Type.SETTER)
	public void setBuilding(final String value)
	{
		getPersistenceContext().setPropertyValue(BUILDING, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.cellphone</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the cellphone
	 */
	@Accessor(qualifier = "cellphone", type = Accessor.Type.SETTER)
	public void setCellphone(final String value)
	{
		getPersistenceContext().setPropertyValue(CELLPHONE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.company</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the company
	 */
	@Accessor(qualifier = "company", type = Accessor.Type.SETTER)
	public void setCompany(final String value)
	{
		getPersistenceContext().setPropertyValue(COMPANY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.contactAddress</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the contactAddress - contactAddress
	 */
	@Accessor(qualifier = "contactAddress", type = Accessor.Type.SETTER)
	public void setContactAddress(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(CONTACTADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.country</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the country
	 */
	@Accessor(qualifier = "country", type = Accessor.Type.SETTER)
	public void setCountry(final CountryModel value)
	{
		getPersistenceContext().setPropertyValue(COUNTRY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.dateOfBirth</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the dateOfBirth
	 * @deprecated since ages - use { @link #setDateOfBirth(java.util.Date)} instead
	 */
	@Deprecated
	public void setDateofbirth(final Date value)
	{
		this.setDateOfBirth(value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.dateOfBirth</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the dateOfBirth
	 */
	@Accessor(qualifier = "dateOfBirth", type = Accessor.Type.SETTER)
	public void setDateOfBirth(final Date value)
	{
		getPersistenceContext().setPropertyValue(DATEOFBIRTH, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.deliveryAddresss2CartToOrderCronJob</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the deliveryAddresss2CartToOrderCronJob
	 */
	@Accessor(qualifier = "deliveryAddresss2CartToOrderCronJob", type = Accessor.Type.SETTER)
	public void setDeliveryAddresss2CartToOrderCronJob(final Collection<CartToOrderCronJobModel> value)
	{
		getPersistenceContext().setPropertyValue(DELIVERYADDRESSS2CARTTOORDERCRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.department</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the department
	 */
	@Accessor(qualifier = "department", type = Accessor.Type.SETTER)
	public void setDepartment(final String value)
	{
		getPersistenceContext().setPropertyValue(DEPARTMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.district</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the district
	 */
	@Accessor(qualifier = "district", type = Accessor.Type.SETTER)
	public void setDistrict(final String value)
	{
		getPersistenceContext().setPropertyValue(DISTRICT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.duplicate</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the duplicate
	 */
	@Accessor(qualifier = "duplicate", type = Accessor.Type.SETTER)
	public void setDuplicate(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(DUPLICATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.email</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the email
	 */
	@Accessor(qualifier = "email", type = Accessor.Type.SETTER)
	public void setEmail(final String value)
	{
		getPersistenceContext().setPropertyValue(EMAIL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.fax</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the fax
	 */
	@Accessor(qualifier = "fax", type = Accessor.Type.SETTER)
	public void setFax(final String value)
	{
		getPersistenceContext().setPropertyValue(FAX, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.firstname</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the firstname
	 */
	@Accessor(qualifier = "firstname", type = Accessor.Type.SETTER)
	public void setFirstname(final String value)
	{
		getPersistenceContext().setPropertyValue(FIRSTNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.gender</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the gender
	 */
	@Accessor(qualifier = "gender", type = Accessor.Type.SETTER)
	public void setGender(final Gender value)
	{
		getPersistenceContext().setPropertyValue(GENDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.lastname</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the lastname
	 */
	@Accessor(qualifier = "lastname", type = Accessor.Type.SETTER)
	public void setLastname(final String value)
	{
		getPersistenceContext().setPropertyValue(LASTNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.line1</code> dynamic attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the line1 - Address line1 is a dynamic attribute that is stored in the Address.streetname field
	 */
	@Accessor(qualifier = "line1", type = Accessor.Type.SETTER)
	public void setLine1(final String value)
	{
		getPersistenceContext().setDynamicValue(this,LINE1, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.line2</code> dynamic attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the line2 - Address line2 is a dynamic attribute that is stored in the Address.streetnumber field
	 */
	@Accessor(qualifier = "line2", type = Accessor.Type.SETTER)
	public void setLine2(final String value)
	{
		getPersistenceContext().setDynamicValue(this,LINE2, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.middlename</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the middlename
	 */
	@Accessor(qualifier = "middlename", type = Accessor.Type.SETTER)
	public void setMiddlename(final String value)
	{
		getPersistenceContext().setPropertyValue(MIDDLENAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.middlename2</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the middlename2
	 */
	@Accessor(qualifier = "middlename2", type = Accessor.Type.SETTER)
	public void setMiddlename2(final String value)
	{
		getPersistenceContext().setPropertyValue(MIDDLENAME2, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Address.original</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the original
	 */
	@Accessor(qualifier = "original", type = Accessor.Type.SETTER)
	public void setOriginal(final AddressModel value)
	{
		getPersistenceContext().setPropertyValue(ORIGINAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Item.owner</code> attribute defined at extension <code>core</code> and redeclared at extension <code>core</code>. Can only be used at creation of model - before first save. Will only accept values of type {@link de.hybris.platform.core.model.ItemModel}.  
	 *  
	 * @param value the owner
	 */
	@Override
	@Accessor(qualifier = "owner", type = Accessor.Type.SETTER)
	public void setOwner(final ItemModel value)
	{
		super.setOwner(value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.paymentAddresss2CartToOrderCronJob</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the paymentAddresss2CartToOrderCronJob
	 */
	@Accessor(qualifier = "paymentAddresss2CartToOrderCronJob", type = Accessor.Type.SETTER)
	public void setPaymentAddresss2CartToOrderCronJob(final Collection<CartToOrderCronJobModel> value)
	{
		getPersistenceContext().setPropertyValue(PAYMENTADDRESSS2CARTTOORDERCRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.phone1</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the phone1
	 */
	@Accessor(qualifier = "phone1", type = Accessor.Type.SETTER)
	public void setPhone1(final String value)
	{
		getPersistenceContext().setPropertyValue(PHONE1, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.phone2</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the phone2
	 */
	@Accessor(qualifier = "phone2", type = Accessor.Type.SETTER)
	public void setPhone2(final String value)
	{
		getPersistenceContext().setPropertyValue(PHONE2, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.pobox</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the pobox
	 */
	@Accessor(qualifier = "pobox", type = Accessor.Type.SETTER)
	public void setPobox(final String value)
	{
		getPersistenceContext().setPropertyValue(POBOX, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.postalcode</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the postalcode
	 */
	@Accessor(qualifier = "postalcode", type = Accessor.Type.SETTER)
	public void setPostalcode(final String value)
	{
		getPersistenceContext().setPropertyValue(POSTALCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.publicKey</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the publicKey
	 */
	@Accessor(qualifier = "publicKey", type = Accessor.Type.SETTER)
	public void setPublicKey(final String value)
	{
		getPersistenceContext().setPropertyValue(PUBLICKEY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.region</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the region
	 */
	@Accessor(qualifier = "region", type = Accessor.Type.SETTER)
	public void setRegion(final RegionModel value)
	{
		getPersistenceContext().setPropertyValue(REGION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.remarks</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the remarks
	 */
	@Accessor(qualifier = "remarks", type = Accessor.Type.SETTER)
	public void setRemarks(final String value)
	{
		getPersistenceContext().setPropertyValue(REMARKS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.shippingAddress</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the shippingAddress - shippingAddress
	 */
	@Accessor(qualifier = "shippingAddress", type = Accessor.Type.SETTER)
	public void setShippingAddress(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SHIPPINGADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.streetname</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the streetname
	 */
	@Accessor(qualifier = "streetname", type = Accessor.Type.SETTER)
	public void setStreetname(final String value)
	{
		getPersistenceContext().setPropertyValue(STREETNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.streetnumber</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the streetnumber
	 */
	@Accessor(qualifier = "streetnumber", type = Accessor.Type.SETTER)
	public void setStreetnumber(final String value)
	{
		getPersistenceContext().setPropertyValue(STREETNUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.title</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the title
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final TitleModel value)
	{
		getPersistenceContext().setPropertyValue(TITLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.town</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the town
	 */
	@Accessor(qualifier = "town", type = Accessor.Type.SETTER)
	public void setTown(final String value)
	{
		getPersistenceContext().setPropertyValue(TOWN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.unloadingAddress</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the unloadingAddress - unloadingAddress
	 */
	@Accessor(qualifier = "unloadingAddress", type = Accessor.Type.SETTER)
	public void setUnloadingAddress(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(UNLOADINGADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.url</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the url
	 */
	@Accessor(qualifier = "url", type = Accessor.Type.SETTER)
	public void setUrl(final String value)
	{
		getPersistenceContext().setPropertyValue(URL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Address.visibleInAddressBook</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the visibleInAddressBook - Indicates if the address will be displayed to the user in the address book.
	 */
	@Accessor(qualifier = "visibleInAddressBook", type = Accessor.Type.SETTER)
	public void setVisibleInAddressBook(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(VISIBLEINADDRESSBOOK, value);
	}
	
}
