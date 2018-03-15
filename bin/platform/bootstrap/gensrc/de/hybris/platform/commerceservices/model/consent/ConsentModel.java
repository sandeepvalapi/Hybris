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
package de.hybris.platform.commerceservices.model.consent;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commerceservices.model.consent.ConsentTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type Consent first defined at extension commerceservices.
 * <p>
 * A consent instance associated with a customer.
 */
@SuppressWarnings("all")
public class ConsentModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Consent";
	
	/** <i>Generated constant</i> - Attribute key of <code>Consent.code</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Consent.customer</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String CUSTOMER = "customer";
	
	/** <i>Generated constant</i> - Attribute key of <code>Consent.consentTemplate</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String CONSENTTEMPLATE = "consentTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Consent.consentGivenDate</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String CONSENTGIVENDATE = "consentGivenDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Consent.consentWithdrawnDate</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String CONSENTWITHDRAWNDATE = "consentWithdrawnDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Consent.active</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String ACTIVE = "active";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ConsentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ConsentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Consent</code> at extension <code>commerceservices</code>
	 * @param _consentTemplate initial attribute declared by type <code>Consent</code> at extension <code>commerceservices</code>
	 * @param _customer initial attribute declared by type <code>Consent</code> at extension <code>commerceservices</code>
	 */
	@Deprecated
	public ConsentModel(final String _code, final ConsentTemplateModel _consentTemplate, final CustomerModel _customer)
	{
		super();
		setCode(_code);
		setConsentTemplate(_consentTemplate);
		setCustomer(_customer);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Consent</code> at extension <code>commerceservices</code>
	 * @param _consentTemplate initial attribute declared by type <code>Consent</code> at extension <code>commerceservices</code>
	 * @param _customer initial attribute declared by type <code>Consent</code> at extension <code>commerceservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ConsentModel(final String _code, final ConsentTemplateModel _consentTemplate, final CustomerModel _customer, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setConsentTemplate(_consentTemplate);
		setCustomer(_customer);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consent.code</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consent.consentGivenDate</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the consentGivenDate - The timestamp when consent was given by the customer
	 */
	@Accessor(qualifier = "consentGivenDate", type = Accessor.Type.GETTER)
	public Date getConsentGivenDate()
	{
		return getPersistenceContext().getPropertyValue(CONSENTGIVENDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consent.consentTemplate</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the consentTemplate - The Consent Reference
	 */
	@Accessor(qualifier = "consentTemplate", type = Accessor.Type.GETTER)
	public ConsentTemplateModel getConsentTemplate()
	{
		return getPersistenceContext().getPropertyValue(CONSENTTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consent.consentWithdrawnDate</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the consentWithdrawnDate - The timestamp when consent was withdrawn by the customer
	 */
	@Accessor(qualifier = "consentWithdrawnDate", type = Accessor.Type.GETTER)
	public Date getConsentWithdrawnDate()
	{
		return getPersistenceContext().getPropertyValue(CONSENTWITHDRAWNDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consent.customer</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the customer - The customer for which the consent is recorded
	 */
	@Accessor(qualifier = "customer", type = Accessor.Type.GETTER)
	public CustomerModel getCustomer()
	{
		return getPersistenceContext().getPropertyValue(CUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consent.active</code> dynamic attribute defined at extension <code>commerceservices</code>. 
	 * @return the active - Indicates if the consent has not yet been withdrawn by the customer
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public boolean isActive()
	{
		return toPrimitive( (Boolean) getPersistenceContext().getDynamicValue(this,ACTIVE));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Consent.code</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Consent.consentGivenDate</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the consentGivenDate - The timestamp when consent was given by the customer
	 */
	@Accessor(qualifier = "consentGivenDate", type = Accessor.Type.SETTER)
	public void setConsentGivenDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(CONSENTGIVENDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Consent.consentTemplate</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the consentTemplate - The Consent Reference
	 */
	@Accessor(qualifier = "consentTemplate", type = Accessor.Type.SETTER)
	public void setConsentTemplate(final ConsentTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(CONSENTTEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Consent.consentWithdrawnDate</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the consentWithdrawnDate - The timestamp when consent was withdrawn by the customer
	 */
	@Accessor(qualifier = "consentWithdrawnDate", type = Accessor.Type.SETTER)
	public void setConsentWithdrawnDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(CONSENTWITHDRAWNDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Consent.customer</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the customer - The customer for which the consent is recorded
	 */
	@Accessor(qualifier = "customer", type = Accessor.Type.SETTER)
	public void setCustomer(final CustomerModel value)
	{
		getPersistenceContext().setPropertyValue(CUSTOMER, value);
	}
	
}
