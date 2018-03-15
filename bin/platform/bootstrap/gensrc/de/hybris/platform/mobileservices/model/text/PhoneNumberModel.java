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
package de.hybris.platform.mobileservices.model.text;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.mobileservices.enums.PhoneNumberFormat;
import de.hybris.platform.mobileservices.model.text.PhoneNumberListModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type PhoneNumber first defined at extension mobileservices.
 */
@SuppressWarnings("all")
public class PhoneNumberModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PhoneNumber";
	
	/**<i>Generated relation code constant for relation <code>ListNumberRelation</code> defining source attribute <code>lists</code> in extension <code>mobileservices</code>.</i>*/
	public static final String _LISTNUMBERRELATION = "ListNumberRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>PhoneNumber.format</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String FORMAT = "format";
	
	/** <i>Generated constant</i> - Attribute key of <code>PhoneNumber.number</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String NUMBER = "number";
	
	/** <i>Generated constant</i> - Attribute key of <code>PhoneNumber.country</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String COUNTRY = "country";
	
	/** <i>Generated constant</i> - Attribute key of <code>PhoneNumber.normalizedNumber</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String NORMALIZEDNUMBER = "normalizedNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>PhoneNumber.lists</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String LISTS = "lists";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PhoneNumberModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PhoneNumberModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _number initial attribute declared by type <code>PhoneNumber</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public PhoneNumberModel(final String _number)
	{
		super();
		setNumber(_number);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _number initial attribute declared by type <code>PhoneNumber</code> at extension <code>mobileservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public PhoneNumberModel(final String _number, final ItemModel _owner)
	{
		super();
		setNumber(_number);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PhoneNumber.country</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the country
	 */
	@Accessor(qualifier = "country", type = Accessor.Type.GETTER)
	public CountryModel getCountry()
	{
		return getPersistenceContext().getPropertyValue(COUNTRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PhoneNumber.format</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the format
	 */
	@Accessor(qualifier = "format", type = Accessor.Type.GETTER)
	public PhoneNumberFormat getFormat()
	{
		return getPersistenceContext().getPropertyValue(FORMAT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PhoneNumber.lists</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the lists
	 */
	@Accessor(qualifier = "lists", type = Accessor.Type.GETTER)
	public Collection<PhoneNumberListModel> getLists()
	{
		return getPersistenceContext().getPropertyValue(LISTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PhoneNumber.normalizedNumber</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the normalizedNumber
	 */
	@Accessor(qualifier = "normalizedNumber", type = Accessor.Type.GETTER)
	public String getNormalizedNumber()
	{
		return getPersistenceContext().getPropertyValue(NORMALIZEDNUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PhoneNumber.number</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the number
	 */
	@Accessor(qualifier = "number", type = Accessor.Type.GETTER)
	public String getNumber()
	{
		return getPersistenceContext().getPropertyValue(NUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PhoneNumber.country</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the country
	 */
	@Accessor(qualifier = "country", type = Accessor.Type.SETTER)
	public void setCountry(final CountryModel value)
	{
		getPersistenceContext().setPropertyValue(COUNTRY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PhoneNumber.format</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the format
	 */
	@Accessor(qualifier = "format", type = Accessor.Type.SETTER)
	public void setFormat(final PhoneNumberFormat value)
	{
		getPersistenceContext().setPropertyValue(FORMAT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PhoneNumber.lists</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the lists
	 */
	@Accessor(qualifier = "lists", type = Accessor.Type.SETTER)
	public void setLists(final Collection<PhoneNumberListModel> value)
	{
		getPersistenceContext().setPropertyValue(LISTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PhoneNumber.number</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the number
	 */
	@Accessor(qualifier = "number", type = Accessor.Type.SETTER)
	public void setNumber(final String value)
	{
		getPersistenceContext().setPropertyValue(NUMBER, value);
	}
	
}
