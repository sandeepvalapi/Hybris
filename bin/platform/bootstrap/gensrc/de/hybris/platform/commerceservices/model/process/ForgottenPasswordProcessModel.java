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
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ForgottenPasswordProcess first defined at extension commerceservices.
 * <p>
 * Represents process that is used for retrieving forgotten password.
 */
@SuppressWarnings("all")
public class ForgottenPasswordProcessModel extends StoreFrontCustomerProcessModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ForgottenPasswordProcess";
	
	/** <i>Generated constant</i> - Attribute key of <code>ForgottenPasswordProcess.token</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String TOKEN = "token";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ForgottenPasswordProcessModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ForgottenPasswordProcessModel(final ItemModelContext ctx)
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
	public ForgottenPasswordProcessModel(final String _code, final String _processDefinitionName)
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
	public ForgottenPasswordProcessModel(final String _code, final ItemModel _owner, final String _processDefinitionName)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setProcessDefinitionName(_processDefinitionName);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ForgottenPasswordProcess.token</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the token - Attribute contains token that is used in this process.
	 */
	@Accessor(qualifier = "token", type = Accessor.Type.GETTER)
	public String getToken()
	{
		return getPersistenceContext().getPropertyValue(TOKEN);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ForgottenPasswordProcess.token</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the token - Attribute contains token that is used in this process.
	 */
	@Accessor(qualifier = "token", type = Accessor.Type.SETTER)
	public void setToken(final String value)
	{
		getPersistenceContext().setPropertyValue(TOKEN, value);
	}
	
}
