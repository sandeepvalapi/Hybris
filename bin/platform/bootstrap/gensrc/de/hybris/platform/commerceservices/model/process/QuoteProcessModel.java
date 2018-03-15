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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type QuoteProcess first defined at extension commerceservices.
 */
@SuppressWarnings("all")
public class QuoteProcessModel extends BusinessProcessModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "QuoteProcess";
	
	/** <i>Generated constant</i> - Attribute key of <code>QuoteProcess.quoteCode</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String QUOTECODE = "quoteCode";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public QuoteProcessModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public QuoteProcessModel(final ItemModelContext ctx)
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
	public QuoteProcessModel(final String _code, final String _processDefinitionName)
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
	public QuoteProcessModel(final String _code, final ItemModel _owner, final String _processDefinitionName)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setProcessDefinitionName(_processDefinitionName);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>QuoteProcess.quoteCode</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the quoteCode - Quote model submitted by the buyer/sales rep.
	 */
	@Accessor(qualifier = "quoteCode", type = Accessor.Type.GETTER)
	public String getQuoteCode()
	{
		return getPersistenceContext().getPropertyValue(QUOTECODE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>QuoteProcess.quoteCode</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the quoteCode - Quote model submitted by the buyer/sales rep.
	 */
	@Accessor(qualifier = "quoteCode", type = Accessor.Type.SETTER)
	public void setQuoteCode(final String value)
	{
		getPersistenceContext().setPropertyValue(QUOTECODE, value);
	}
	
}
