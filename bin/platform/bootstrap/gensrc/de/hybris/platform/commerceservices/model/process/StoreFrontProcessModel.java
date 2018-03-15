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
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;

/**
 * Generated model class for type StoreFrontProcess first defined at extension commerceservices.
 * <p>
 * It represents storefront business process.
 */
@SuppressWarnings("all")
public class StoreFrontProcessModel extends BusinessProcessModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "StoreFrontProcess";
	
	/** <i>Generated constant</i> - Attribute key of <code>StoreFrontProcess.site</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String SITE = "site";
	
	/** <i>Generated constant</i> - Attribute key of <code>StoreFrontProcess.store</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String STORE = "store";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public StoreFrontProcessModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public StoreFrontProcessModel(final ItemModelContext ctx)
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
	public StoreFrontProcessModel(final String _code, final String _processDefinitionName)
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
	public StoreFrontProcessModel(final String _code, final ItemModel _owner, final String _processDefinitionName)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setProcessDefinitionName(_processDefinitionName);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreFrontProcess.site</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the site - Attribute contains base site object that will be used in the process.
	 */
	@Accessor(qualifier = "site", type = Accessor.Type.GETTER)
	public BaseSiteModel getSite()
	{
		return getPersistenceContext().getPropertyValue(SITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StoreFrontProcess.store</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the store - Attribute contains base store object that will be used in the process.
	 */
	@Accessor(qualifier = "store", type = Accessor.Type.GETTER)
	public BaseStoreModel getStore()
	{
		return getPersistenceContext().getPropertyValue(STORE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StoreFrontProcess.site</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the site - Attribute contains base site object that will be used in the process.
	 */
	@Accessor(qualifier = "site", type = Accessor.Type.SETTER)
	public void setSite(final BaseSiteModel value)
	{
		getPersistenceContext().setPropertyValue(SITE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>StoreFrontProcess.store</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the store - Attribute contains base store object that will be used in the process.
	 */
	@Accessor(qualifier = "store", type = Accessor.Type.SETTER)
	public void setStore(final BaseStoreModel value)
	{
		getPersistenceContext().setPropertyValue(STORE, value);
	}
	
}
