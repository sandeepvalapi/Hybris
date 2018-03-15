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
package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.enums.LineOfBusiness;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type Company first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CompanyModel extends UserGroupModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Company";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.dunsID</code> attribute defined at extension <code>catalog</code>. */
	public static final String DUNSID = "dunsID";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.ilnID</code> attribute defined at extension <code>catalog</code>. */
	public static final String ILNID = "ilnID";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.buyerSpecificID</code> attribute defined at extension <code>catalog</code>. */
	public static final String BUYERSPECIFICID = "buyerSpecificID";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.Id</code> attribute defined at extension <code>catalog</code>. */
	public static final String ID = "Id";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.supplierSpecificID</code> attribute defined at extension <code>catalog</code>. */
	public static final String SUPPLIERSPECIFICID = "supplierSpecificID";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.addresses</code> attribute defined at extension <code>catalog</code>. */
	public static final String ADDRESSES = "addresses";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.medias</code> attribute defined at extension <code>catalog</code>. */
	public static final String MEDIAS = "medias";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.shippingAddresses</code> attribute defined at extension <code>catalog</code>. */
	public static final String SHIPPINGADDRESSES = "shippingAddresses";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.shippingAddress</code> attribute defined at extension <code>catalog</code>. */
	public static final String SHIPPINGADDRESS = "shippingAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.unloadingAddresses</code> attribute defined at extension <code>catalog</code>. */
	public static final String UNLOADINGADDRESSES = "unloadingAddresses";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.unloadingAddress</code> attribute defined at extension <code>catalog</code>. */
	public static final String UNLOADINGADDRESS = "unloadingAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.billingAddresses</code> attribute defined at extension <code>catalog</code>. */
	public static final String BILLINGADDRESSES = "billingAddresses";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.billingAddress</code> attribute defined at extension <code>catalog</code>. */
	public static final String BILLINGADDRESS = "billingAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.contactAddresses</code> attribute defined at extension <code>catalog</code>. */
	public static final String CONTACTADDRESSES = "contactAddresses";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.contactAddress</code> attribute defined at extension <code>catalog</code>. */
	public static final String CONTACTADDRESS = "contactAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.contact</code> attribute defined at extension <code>catalog</code>. */
	public static final String CONTACT = "contact";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.vatID</code> attribute defined at extension <code>catalog</code>. */
	public static final String VATID = "vatID";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.responsibleCompany</code> attribute defined at extension <code>catalog</code>. */
	public static final String RESPONSIBLECOMPANY = "responsibleCompany";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.country</code> attribute defined at extension <code>catalog</code>. */
	public static final String COUNTRY = "country";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.lineOfBuisness</code> attribute defined at extension <code>catalog</code>. */
	public static final String LINEOFBUISNESS = "lineOfBuisness";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.buyer</code> attribute defined at extension <code>catalog</code>. */
	public static final String BUYER = "buyer";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.supplier</code> attribute defined at extension <code>catalog</code>. */
	public static final String SUPPLIER = "supplier";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.manufacturer</code> attribute defined at extension <code>catalog</code>. */
	public static final String MANUFACTURER = "manufacturer";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.carrier</code> attribute defined at extension <code>catalog</code>. */
	public static final String CARRIER = "carrier";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.providedCatalogs</code> attribute defined at extension <code>catalog</code>. */
	public static final String PROVIDEDCATALOGS = "providedCatalogs";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.purchasedCatalogs</code> attribute defined at extension <code>catalog</code>. */
	public static final String PURCHASEDCATALOGS = "purchasedCatalogs";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CompanyModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CompanyModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public CompanyModel(final String _uid)
	{
		super();
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public CompanyModel(final ItemModel _owner, final String _uid)
	{
		super();
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.addresses</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the addresses
	 */
	@Accessor(qualifier = "addresses", type = Accessor.Type.GETTER)
	public Collection<AddressModel> getAddresses()
	{
		return getPersistenceContext().getPropertyValue(ADDRESSES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.billingAddress</code> attribute defined at extension <code>catalog</code>. 
	 * @return the billingAddress - Billing address of this company
	 */
	@Accessor(qualifier = "billingAddress", type = Accessor.Type.GETTER)
	public AddressModel getBillingAddress()
	{
		return getPersistenceContext().getPropertyValue(BILLINGADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.billingAddresses</code> dynamic attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the billingAddresses
	 */
	@Accessor(qualifier = "billingAddresses", type = Accessor.Type.GETTER)
	public Collection<AddressModel> getBillingAddresses()
	{
		return getPersistenceContext().getDynamicValue(this,BILLINGADDRESSES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.buyer</code> attribute defined at extension <code>catalog</code>. 
	 * @return the buyer - buyer
	 */
	@Accessor(qualifier = "buyer", type = Accessor.Type.GETTER)
	public Boolean getBuyer()
	{
		return getPersistenceContext().getPropertyValue(BUYER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.buyerSpecificID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the buyerSpecificID - Buyer Specific ID
	 */
	@Accessor(qualifier = "buyerSpecificID", type = Accessor.Type.GETTER)
	public String getBuyerSpecificID()
	{
		return getPersistenceContext().getPropertyValue(BUYERSPECIFICID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.carrier</code> attribute defined at extension <code>catalog</code>. 
	 * @return the carrier - carrier
	 */
	@Accessor(qualifier = "carrier", type = Accessor.Type.GETTER)
	public Boolean getCarrier()
	{
		return getPersistenceContext().getPropertyValue(CARRIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.contact</code> attribute defined at extension <code>catalog</code>. 
	 * @return the contact - Contact for this company
	 */
	@Accessor(qualifier = "contact", type = Accessor.Type.GETTER)
	public UserModel getContact()
	{
		return getPersistenceContext().getPropertyValue(CONTACT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.contactAddress</code> attribute defined at extension <code>catalog</code>. 
	 * @return the contactAddress - Contact address of this company
	 */
	@Accessor(qualifier = "contactAddress", type = Accessor.Type.GETTER)
	public AddressModel getContactAddress()
	{
		return getPersistenceContext().getPropertyValue(CONTACTADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.contactAddresses</code> dynamic attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the contactAddresses
	 */
	@Accessor(qualifier = "contactAddresses", type = Accessor.Type.GETTER)
	public Collection<AddressModel> getContactAddresses()
	{
		return getPersistenceContext().getDynamicValue(this,CONTACTADDRESSES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.country</code> attribute defined at extension <code>catalog</code>. 
	 * @return the country - country
	 */
	@Accessor(qualifier = "country", type = Accessor.Type.GETTER)
	public CountryModel getCountry()
	{
		return getPersistenceContext().getPropertyValue(COUNTRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.dunsID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the dunsID - DUNS
	 */
	@Accessor(qualifier = "dunsID", type = Accessor.Type.GETTER)
	public String getDunsID()
	{
		return getPersistenceContext().getPropertyValue(DUNSID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.Id</code> attribute defined at extension <code>catalog</code>. 
	 * @return the Id - id
	 */
	@Accessor(qualifier = "Id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.ilnID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the ilnID - ILN
	 */
	@Accessor(qualifier = "ilnID", type = Accessor.Type.GETTER)
	public String getIlnID()
	{
		return getPersistenceContext().getPropertyValue(ILNID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.lineOfBuisness</code> attribute defined at extension <code>catalog</code>. 
	 * @return the lineOfBuisness - line of business
	 */
	@Accessor(qualifier = "lineOfBuisness", type = Accessor.Type.GETTER)
	public LineOfBusiness getLineOfBuisness()
	{
		return getPersistenceContext().getPropertyValue(LINEOFBUISNESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.manufacturer</code> attribute defined at extension <code>catalog</code>. 
	 * @return the manufacturer - manufacturer
	 */
	@Accessor(qualifier = "manufacturer", type = Accessor.Type.GETTER)
	public Boolean getManufacturer()
	{
		return getPersistenceContext().getPropertyValue(MANUFACTURER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.medias</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the medias - medias
	 */
	@Accessor(qualifier = "medias", type = Accessor.Type.GETTER)
	public Collection<MediaModel> getMedias()
	{
		return getPersistenceContext().getPropertyValue(MEDIAS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.providedCatalogs</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the providedCatalogs
	 */
	@Accessor(qualifier = "providedCatalogs", type = Accessor.Type.GETTER)
	public Collection<CatalogModel> getProvidedCatalogs()
	{
		return getPersistenceContext().getPropertyValue(PROVIDEDCATALOGS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.purchasedCatalogs</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the purchasedCatalogs
	 */
	@Accessor(qualifier = "purchasedCatalogs", type = Accessor.Type.GETTER)
	public Collection<CatalogModel> getPurchasedCatalogs()
	{
		return getPersistenceContext().getPropertyValue(PURCHASEDCATALOGS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.responsibleCompany</code> attribute defined at extension <code>catalog</code>. 
	 * @return the responsibleCompany - responsible company
	 */
	@Accessor(qualifier = "responsibleCompany", type = Accessor.Type.GETTER)
	public CompanyModel getResponsibleCompany()
	{
		return getPersistenceContext().getPropertyValue(RESPONSIBLECOMPANY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.shippingAddress</code> attribute defined at extension <code>catalog</code>. 
	 * @return the shippingAddress - Shipping address of this company
	 */
	@Accessor(qualifier = "shippingAddress", type = Accessor.Type.GETTER)
	public AddressModel getShippingAddress()
	{
		return getPersistenceContext().getPropertyValue(SHIPPINGADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.shippingAddresses</code> dynamic attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the shippingAddresses
	 */
	@Accessor(qualifier = "shippingAddresses", type = Accessor.Type.GETTER)
	public Collection<AddressModel> getShippingAddresses()
	{
		return getPersistenceContext().getDynamicValue(this,SHIPPINGADDRESSES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.supplier</code> attribute defined at extension <code>catalog</code>. 
	 * @return the supplier - supplier
	 */
	@Accessor(qualifier = "supplier", type = Accessor.Type.GETTER)
	public Boolean getSupplier()
	{
		return getPersistenceContext().getPropertyValue(SUPPLIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.supplierSpecificID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the supplierSpecificID - Supplier Specific ID
	 */
	@Accessor(qualifier = "supplierSpecificID", type = Accessor.Type.GETTER)
	public String getSupplierSpecificID()
	{
		return getPersistenceContext().getPropertyValue(SUPPLIERSPECIFICID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.unloadingAddress</code> attribute defined at extension <code>catalog</code>. 
	 * @return the unloadingAddress - Unloading address of this company
	 */
	@Accessor(qualifier = "unloadingAddress", type = Accessor.Type.GETTER)
	public AddressModel getUnloadingAddress()
	{
		return getPersistenceContext().getPropertyValue(UNLOADINGADDRESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.unloadingAddresses</code> dynamic attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the unloadingAddresses
	 */
	@Accessor(qualifier = "unloadingAddresses", type = Accessor.Type.GETTER)
	public Collection<AddressModel> getUnloadingAddresses()
	{
		return getPersistenceContext().getDynamicValue(this,UNLOADINGADDRESSES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.vatID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the vatID - vat id
	 */
	@Accessor(qualifier = "vatID", type = Accessor.Type.GETTER)
	public String getVatID()
	{
		return getPersistenceContext().getPropertyValue(VATID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.addresses</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the addresses
	 */
	@Accessor(qualifier = "addresses", type = Accessor.Type.SETTER)
	public void setAddresses(final Collection<AddressModel> value)
	{
		getPersistenceContext().setPropertyValue(ADDRESSES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.billingAddress</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the billingAddress - Billing address of this company
	 */
	@Accessor(qualifier = "billingAddress", type = Accessor.Type.SETTER)
	public void setBillingAddress(final AddressModel value)
	{
		getPersistenceContext().setPropertyValue(BILLINGADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.buyer</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the buyer - buyer
	 */
	@Accessor(qualifier = "buyer", type = Accessor.Type.SETTER)
	public void setBuyer(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(BUYER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.buyerSpecificID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the buyerSpecificID - Buyer Specific ID
	 */
	@Accessor(qualifier = "buyerSpecificID", type = Accessor.Type.SETTER)
	public void setBuyerSpecificID(final String value)
	{
		getPersistenceContext().setPropertyValue(BUYERSPECIFICID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.carrier</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the carrier - carrier
	 */
	@Accessor(qualifier = "carrier", type = Accessor.Type.SETTER)
	public void setCarrier(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(CARRIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.contact</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the contact - Contact for this company
	 */
	@Accessor(qualifier = "contact", type = Accessor.Type.SETTER)
	public void setContact(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(CONTACT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.contactAddress</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the contactAddress - Contact address of this company
	 */
	@Accessor(qualifier = "contactAddress", type = Accessor.Type.SETTER)
	public void setContactAddress(final AddressModel value)
	{
		getPersistenceContext().setPropertyValue(CONTACTADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.country</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the country - country
	 */
	@Accessor(qualifier = "country", type = Accessor.Type.SETTER)
	public void setCountry(final CountryModel value)
	{
		getPersistenceContext().setPropertyValue(COUNTRY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.dunsID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the dunsID - DUNS
	 */
	@Accessor(qualifier = "dunsID", type = Accessor.Type.SETTER)
	public void setDunsID(final String value)
	{
		getPersistenceContext().setPropertyValue(DUNSID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.Id</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the Id - id
	 */
	@Accessor(qualifier = "Id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.ilnID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the ilnID - ILN
	 */
	@Accessor(qualifier = "ilnID", type = Accessor.Type.SETTER)
	public void setIlnID(final String value)
	{
		getPersistenceContext().setPropertyValue(ILNID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.lineOfBuisness</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the lineOfBuisness - line of business
	 */
	@Accessor(qualifier = "lineOfBuisness", type = Accessor.Type.SETTER)
	public void setLineOfBuisness(final LineOfBusiness value)
	{
		getPersistenceContext().setPropertyValue(LINEOFBUISNESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.manufacturer</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the manufacturer - manufacturer
	 */
	@Accessor(qualifier = "manufacturer", type = Accessor.Type.SETTER)
	public void setManufacturer(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(MANUFACTURER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.medias</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the medias - medias
	 */
	@Accessor(qualifier = "medias", type = Accessor.Type.SETTER)
	public void setMedias(final Collection<MediaModel> value)
	{
		getPersistenceContext().setPropertyValue(MEDIAS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.responsibleCompany</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the responsibleCompany - responsible company
	 */
	@Accessor(qualifier = "responsibleCompany", type = Accessor.Type.SETTER)
	public void setResponsibleCompany(final CompanyModel value)
	{
		getPersistenceContext().setPropertyValue(RESPONSIBLECOMPANY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.shippingAddress</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the shippingAddress - Shipping address of this company
	 */
	@Accessor(qualifier = "shippingAddress", type = Accessor.Type.SETTER)
	public void setShippingAddress(final AddressModel value)
	{
		getPersistenceContext().setPropertyValue(SHIPPINGADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.supplier</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the supplier - supplier
	 */
	@Accessor(qualifier = "supplier", type = Accessor.Type.SETTER)
	public void setSupplier(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SUPPLIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.supplierSpecificID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the supplierSpecificID - Supplier Specific ID
	 */
	@Accessor(qualifier = "supplierSpecificID", type = Accessor.Type.SETTER)
	public void setSupplierSpecificID(final String value)
	{
		getPersistenceContext().setPropertyValue(SUPPLIERSPECIFICID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.unloadingAddress</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the unloadingAddress - Unloading address of this company
	 */
	@Accessor(qualifier = "unloadingAddress", type = Accessor.Type.SETTER)
	public void setUnloadingAddress(final AddressModel value)
	{
		getPersistenceContext().setPropertyValue(UNLOADINGADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.vatID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the vatID - vat id
	 */
	@Accessor(qualifier = "vatID", type = Accessor.Type.SETTER)
	public void setVatID(final String value)
	{
		getPersistenceContext().setPropertyValue(VATID, value);
	}
	
}
