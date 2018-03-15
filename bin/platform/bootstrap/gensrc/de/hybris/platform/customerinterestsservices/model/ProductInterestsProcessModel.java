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
package de.hybris.platform.customerinterestsservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.customerinterestsservices.model.ProductInterestModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ProductInterestsProcess first defined at extension customerinterestsservices.
 * <p>
 * Represents The Customer Interests Process.
 */
@SuppressWarnings("all")
public class ProductInterestsProcessModel extends BusinessProcessModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductInterestsProcess";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductInterestsProcess.language</code> attribute defined at extension <code>customerinterestsservices</code>. */
	public static final String LANGUAGE = "language";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductInterestsProcess.productInterest</code> attribute defined at extension <code>customerinterestsservices</code>. */
	public static final String PRODUCTINTEREST = "productInterest";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductInterestsProcessModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductInterestsProcessModel(final ItemModelContext ctx)
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
	public ProductInterestsProcessModel(final String _code, final String _processDefinitionName)
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
	public ProductInterestsProcessModel(final String _code, final ItemModel _owner, final String _processDefinitionName)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setProcessDefinitionName(_processDefinitionName);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterestsProcess.language</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 * @return the language - Attribute contains language that will be used in the process.
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public LanguageModel getLanguage()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductInterestsProcess.productInterest</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 * @return the productInterest - Attribute contains the product back in stock interests
	 */
	@Accessor(qualifier = "productInterest", type = Accessor.Type.GETTER)
	public ProductInterestModel getProductInterest()
	{
		return getPersistenceContext().getPropertyValue(PRODUCTINTEREST);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductInterestsProcess.language</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 *  
	 * @param value the language - Attribute contains language that will be used in the process.
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductInterestsProcess.productInterest</code> attribute defined at extension <code>customerinterestsservices</code>. 
	 *  
	 * @param value the productInterest - Attribute contains the product back in stock interests
	 */
	@Accessor(qualifier = "productInterest", type = Accessor.Type.SETTER)
	public void setProductInterest(final ProductInterestModel value)
	{
		getPersistenceContext().setPropertyValue(PRODUCTINTEREST, value);
	}
	
}
