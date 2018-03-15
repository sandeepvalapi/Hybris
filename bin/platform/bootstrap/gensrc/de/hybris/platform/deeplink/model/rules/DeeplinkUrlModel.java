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
package de.hybris.platform.deeplink.model.rules;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.deeplink.model.media.BarcodeMediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type DeeplinkUrl first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class DeeplinkUrlModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DeeplinkUrl";
	
	/**<i>Generated relation code constant for relation <code>BarcodeMedia2DeeplinkUrl</code> defining source attribute <code>barcodeMedias</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _BARCODEMEDIA2DEEPLINKURL = "BarcodeMedia2DeeplinkUrl";
	
	/** <i>Generated constant</i> - Attribute key of <code>DeeplinkUrl.code</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>DeeplinkUrl.name</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>DeeplinkUrl.baseUrl</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String BASEURL = "baseUrl";
	
	/** <i>Generated constant</i> - Attribute key of <code>DeeplinkUrl.barcodeMedias</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String BARCODEMEDIAS = "barcodeMedias";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DeeplinkUrlModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DeeplinkUrlModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseUrl initial attribute declared by type <code>DeeplinkUrl</code> at extension <code>basecommerce</code>
	 * @param _code initial attribute declared by type <code>DeeplinkUrl</code> at extension <code>basecommerce</code>
	 * @param _name initial attribute declared by type <code>DeeplinkUrl</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public DeeplinkUrlModel(final String _baseUrl, final String _code, final String _name)
	{
		super();
		setBaseUrl(_baseUrl);
		setCode(_code);
		setName(_name);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseUrl initial attribute declared by type <code>DeeplinkUrl</code> at extension <code>basecommerce</code>
	 * @param _code initial attribute declared by type <code>DeeplinkUrl</code> at extension <code>basecommerce</code>
	 * @param _name initial attribute declared by type <code>DeeplinkUrl</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public DeeplinkUrlModel(final String _baseUrl, final String _code, final String _name, final ItemModel _owner)
	{
		super();
		setBaseUrl(_baseUrl);
		setCode(_code);
		setName(_name);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeeplinkUrl.barcodeMedias</code> attribute defined at extension <code>basecommerce</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the barcodeMedias
	 */
	@Accessor(qualifier = "barcodeMedias", type = Accessor.Type.GETTER)
	public Collection<BarcodeMediaModel> getBarcodeMedias()
	{
		return getPersistenceContext().getPropertyValue(BARCODEMEDIAS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeeplinkUrl.baseUrl</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the baseUrl
	 */
	@Accessor(qualifier = "baseUrl", type = Accessor.Type.GETTER)
	public String getBaseUrl()
	{
		return getPersistenceContext().getPropertyValue(BASEURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeeplinkUrl.code</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeeplinkUrl.name</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeeplinkUrl.barcodeMedias</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the barcodeMedias
	 */
	@Accessor(qualifier = "barcodeMedias", type = Accessor.Type.SETTER)
	public void setBarcodeMedias(final Collection<BarcodeMediaModel> value)
	{
		getPersistenceContext().setPropertyValue(BARCODEMEDIAS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeeplinkUrl.baseUrl</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the baseUrl
	 */
	@Accessor(qualifier = "baseUrl", type = Accessor.Type.SETTER)
	public void setBaseUrl(final String value)
	{
		getPersistenceContext().setPropertyValue(BASEURL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeeplinkUrl.code</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeeplinkUrl.name</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
}
