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
package de.hybris.platform.commerceservices.model.process;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commerceservices.model.process.StoreFrontProcessModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type StoreFrontCustomerProcess first defined at extension commerceservices.
 * <p>
 * Represents customer related store front business process.
 */
@SuppressWarnings("all")
public class StoreFrontCustomerProcessModel extends StoreFrontProcessModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "StoreFrontCustomerProcess";
	
	/** <i>Generated constant</i> - Attribute key of <code>StoreFrontCustomerProcess.customer</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String CUSTOMER = "customer";
	
	/** <i>Generated constant</i> - Attribute key of <code>StoreFrontCustomerProcess.language</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String LANGUAGE = "language";
	
	/** <i>Generated constant</i> - Attribute key of <code>StoreFrontCustomerProcess.currency</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String CURRENCY = "currency";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public StoreFrontCustomerProcessModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public StoreFrontCustomerProcessModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 * @param _processDefinitionName initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 */
	@Deprecated
	public StoreFrontCustomerProcessModel(final String _code, final String _processDefinitionName)
	{
		super();
		setCode(_code);
		setProcessDefinitionName(_processDefinitionName);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _processDefinitionName initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 */
	@Deprecated
	public StoreFrontCustomerProcessModel(final String _code, final ItemModel _owner, final String _processDefinitionName)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setProcessDefinitionName(_processDefinitionName);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreFrontCustomerProcess.currency</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the currency - Storefront currency.
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
	public CurrencyModel getCurrency()
	{
		return getPersistenceContext().getPropertyValue(CURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreFrontCustomerProcess.customer</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the customer - Attribute contains customer object that will be used in the process.
	 */
	@Accessor(qualifier = "customer", type = Accessor.Type.GETTER)
	public CustomerModel getCustomer()
	{
		return getPersistenceContext().getPropertyValue(CUSTOMER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreFrontCustomerProcess.language</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the language - Attribute contains language that will be used in the process.
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public LanguageModel getLanguage()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StoreFrontCustomerProcess.currency</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the currency - Storefront currency.
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
	public void setCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(CURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StoreFrontCustomerProcess.customer</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the customer - Attribute contains customer object that will be used in the process.
	 */
	@Accessor(qualifier = "customer", type = Accessor.Type.SETTER)
	public void setCustomer(final CustomerModel value)
	{
		getPersistenceContext().setPropertyValue(CUSTOMER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StoreFrontCustomerProcess.language</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the language - Attribute contains language that will be used in the process.
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGE, value);
	}
	
}
