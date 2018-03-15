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
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.mobileservices.model.text.AbstractMobileSendActionModel;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type MobileSendTextAction first defined at extension mobileservices.
 */
@SuppressWarnings("all")
public class MobileSendTextActionModel extends AbstractMobileSendActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MobileSendTextAction";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileSendTextAction.template</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String TEMPLATE = "template";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MobileSendTextActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MobileSendTextActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractMobileSendAction</code> at extension <code>mobileservices</code>
	 * @param _defaultLanguage initial attribute declared by type <code>AbstractMobileSendAction</code> at extension <code>mobileservices</code>
	 * @param _target initial attribute declared by type <code>MobileSendTextAction</code> at extension <code>mobileservices</code>
	 * @param _type initial attribute declared by type <code>AbstractMobileSendAction</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public MobileSendTextActionModel(final String _code, final LanguageModel _defaultLanguage, final String _target, final ActionType _type)
	{
		super();
		setCode(_code);
		setDefaultLanguage(_defaultLanguage);
		setTarget(_target);
		setType(_type);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractMobileSendAction</code> at extension <code>mobileservices</code>
	 * @param _defaultLanguage initial attribute declared by type <code>AbstractMobileSendAction</code> at extension <code>mobileservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _target initial attribute declared by type <code>MobileSendTextAction</code> at extension <code>mobileservices</code>
	 * @param _type initial attribute declared by type <code>AbstractMobileSendAction</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public MobileSendTextActionModel(final String _code, final LanguageModel _defaultLanguage, final ItemModel _owner, final String _target, final ActionType _type)
	{
		super();
		setCode(_code);
		setDefaultLanguage(_defaultLanguage);
		setOwner(_owner);
		setTarget(_target);
		setType(_type);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileSendTextAction.template</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the template
	 */
	@Accessor(qualifier = "template", type = Accessor.Type.GETTER)
	public String getTemplate()
	{
		return getTemplate(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileSendTextAction.template</code> attribute defined at extension <code>mobileservices</code>. 
	 * @param loc the value localization key 
	 * @return the template
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "template", type = Accessor.Type.GETTER)
	public String getTemplate(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(TEMPLATE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileSendTextAction.template</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the template
	 */
	@Accessor(qualifier = "template", type = Accessor.Type.SETTER)
	public void setTemplate(final String value)
	{
		setTemplate(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>MobileSendTextAction.template</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the template
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "template", type = Accessor.Type.SETTER)
	public void setTemplate(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(TEMPLATE, loc, value);
	}
	
}
