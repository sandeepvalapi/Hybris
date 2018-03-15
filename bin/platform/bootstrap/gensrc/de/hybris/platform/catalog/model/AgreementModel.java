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
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type Agreement first defined at extension catalog.
 */
@SuppressWarnings("all")
public class AgreementModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Agreement";
	
	/**<i>Generated relation code constant for relation <code>CatalogVersion2Agreements</code> defining source attribute <code>catalogVersion</code> in extension <code>catalog</code>.</i>*/
	public static final String _CATALOGVERSION2AGREEMENTS = "CatalogVersion2Agreements";
	
	/** <i>Generated constant</i> - Attribute key of <code>Agreement.id</code> attribute defined at extension <code>catalog</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>Agreement.startdate</code> attribute defined at extension <code>catalog</code>. */
	public static final String STARTDATE = "startdate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Agreement.enddate</code> attribute defined at extension <code>catalog</code>. */
	public static final String ENDDATE = "enddate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Agreement.Catalog</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOG = "Catalog";
	
	/** <i>Generated constant</i> - Attribute key of <code>Agreement.buyer</code> attribute defined at extension <code>catalog</code>. */
	public static final String BUYER = "buyer";
	
	/** <i>Generated constant</i> - Attribute key of <code>Agreement.supplier</code> attribute defined at extension <code>catalog</code>. */
	public static final String SUPPLIER = "supplier";
	
	/** <i>Generated constant</i> - Attribute key of <code>Agreement.buyerContact</code> attribute defined at extension <code>catalog</code>. */
	public static final String BUYERCONTACT = "buyerContact";
	
	/** <i>Generated constant</i> - Attribute key of <code>Agreement.supplierContact</code> attribute defined at extension <code>catalog</code>. */
	public static final String SUPPLIERCONTACT = "supplierContact";
	
	/** <i>Generated constant</i> - Attribute key of <code>Agreement.currency</code> attribute defined at extension <code>catalog</code>. */
	public static final String CURRENCY = "currency";
	
	/** <i>Generated constant</i> - Attribute key of <code>Agreement.catalogVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AgreementModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AgreementModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _enddate initial attribute declared by type <code>Agreement</code> at extension <code>catalog</code>
	 * @param _id initial attribute declared by type <code>Agreement</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public AgreementModel(final Date _enddate, final String _id)
	{
		super();
		setEnddate(_enddate);
		setId(_id);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _enddate initial attribute declared by type <code>Agreement</code> at extension <code>catalog</code>
	 * @param _id initial attribute declared by type <code>Agreement</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AgreementModel(final Date _enddate, final String _id, final ItemModel _owner)
	{
		super();
		setEnddate(_enddate);
		setId(_id);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Agreement.buyer</code> attribute defined at extension <code>catalog</code>. 
	 * @return the buyer - buyer
	 */
	@Accessor(qualifier = "buyer", type = Accessor.Type.GETTER)
	public CompanyModel getBuyer()
	{
		return getPersistenceContext().getPropertyValue(BUYER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Agreement.buyerContact</code> attribute defined at extension <code>catalog</code>. 
	 * @return the buyerContact - buyerContact
	 */
	@Accessor(qualifier = "buyerContact", type = Accessor.Type.GETTER)
	public UserModel getBuyerContact()
	{
		return getPersistenceContext().getPropertyValue(BUYERCONTACT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Agreement.catalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Agreement.currency</code> attribute defined at extension <code>catalog</code>. 
	 * @return the currency - currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
	public CurrencyModel getCurrency()
	{
		return getPersistenceContext().getPropertyValue(CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Agreement.enddate</code> attribute defined at extension <code>catalog</code>. 
	 * @return the enddate - Agreement ID
	 */
	@Accessor(qualifier = "enddate", type = Accessor.Type.GETTER)
	public Date getEnddate()
	{
		return getPersistenceContext().getPropertyValue(ENDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Agreement.id</code> attribute defined at extension <code>catalog</code>. 
	 * @return the id - Agreement ID
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Agreement.startdate</code> attribute defined at extension <code>catalog</code>. 
	 * @return the startdate - Start Date
	 */
	@Accessor(qualifier = "startdate", type = Accessor.Type.GETTER)
	public Date getStartdate()
	{
		return getPersistenceContext().getPropertyValue(STARTDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Agreement.supplier</code> attribute defined at extension <code>catalog</code>. 
	 * @return the supplier - supplier
	 */
	@Accessor(qualifier = "supplier", type = Accessor.Type.GETTER)
	public CompanyModel getSupplier()
	{
		return getPersistenceContext().getPropertyValue(SUPPLIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Agreement.supplierContact</code> attribute defined at extension <code>catalog</code>. 
	 * @return the supplierContact - supplierContact
	 */
	@Accessor(qualifier = "supplierContact", type = Accessor.Type.GETTER)
	public UserModel getSupplierContact()
	{
		return getPersistenceContext().getPropertyValue(SUPPLIERCONTACT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Agreement.buyer</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the buyer - buyer
	 */
	@Accessor(qualifier = "buyer", type = Accessor.Type.SETTER)
	public void setBuyer(final CompanyModel value)
	{
		getPersistenceContext().setPropertyValue(BUYER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Agreement.buyerContact</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the buyerContact - buyerContact
	 */
	@Accessor(qualifier = "buyerContact", type = Accessor.Type.SETTER)
	public void setBuyerContact(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(BUYERCONTACT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Agreement.catalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Agreement.currency</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the currency - currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
	public void setCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(CURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Agreement.enddate</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the enddate - Agreement ID
	 */
	@Accessor(qualifier = "enddate", type = Accessor.Type.SETTER)
	public void setEnddate(final Date value)
	{
		getPersistenceContext().setPropertyValue(ENDDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Agreement.id</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the id - Agreement ID
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Agreement.startdate</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the startdate - Start Date
	 */
	@Accessor(qualifier = "startdate", type = Accessor.Type.SETTER)
	public void setStartdate(final Date value)
	{
		getPersistenceContext().setPropertyValue(STARTDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Agreement.supplier</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the supplier - supplier
	 */
	@Accessor(qualifier = "supplier", type = Accessor.Type.SETTER)
	public void setSupplier(final CompanyModel value)
	{
		getPersistenceContext().setPropertyValue(SUPPLIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Agreement.supplierContact</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the supplierContact - supplierContact
	 */
	@Accessor(qualifier = "supplierContact", type = Accessor.Type.SETTER)
	public void setSupplierContact(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(SUPPLIERCONTACT, value);
	}
	
}
