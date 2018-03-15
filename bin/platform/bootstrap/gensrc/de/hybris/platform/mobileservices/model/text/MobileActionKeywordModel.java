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
import de.hybris.platform.mobileservices.enums.MobileKeywordType;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type MobileActionKeyword first defined at extension mobileservices.
 */
@SuppressWarnings("all")
public class MobileActionKeywordModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MobileActionKeyword";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileActionKeyword.type</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String TYPE = "type";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileActionKeyword.keyword</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String KEYWORD = "keyword";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileActionKeyword.keywordLowerCase</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String KEYWORDLOWERCASE = "keywordLowerCase";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MobileActionKeywordModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MobileActionKeywordModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _keyword initial attribute declared by type <code>MobileActionKeyword</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public MobileActionKeywordModel(final String _keyword)
	{
		super();
		setKeyword(_keyword);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _keyword initial attribute declared by type <code>MobileActionKeyword</code> at extension <code>mobileservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public MobileActionKeywordModel(final String _keyword, final ItemModel _owner)
	{
		super();
		setKeyword(_keyword);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileActionKeyword.keyword</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the keyword
	 */
	@Accessor(qualifier = "keyword", type = Accessor.Type.GETTER)
	public String getKeyword()
	{
		return getPersistenceContext().getPropertyValue(KEYWORD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileActionKeyword.type</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public MobileKeywordType getType()
	{
		return getPersistenceContext().getPropertyValue(TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileActionKeyword.keyword</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the keyword
	 */
	@Accessor(qualifier = "keyword", type = Accessor.Type.SETTER)
	public void setKeyword(final String value)
	{
		getPersistenceContext().setPropertyValue(KEYWORD, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileActionKeyword.type</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.SETTER)
	public void setType(final MobileKeywordType value)
	{
		getPersistenceContext().setPropertyValue(TYPE, value);
	}
	
}
