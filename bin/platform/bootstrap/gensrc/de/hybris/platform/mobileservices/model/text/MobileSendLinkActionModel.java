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
 * Generated model class for type MobileSendLinkAction first defined at extension mobileservices.
 */
@SuppressWarnings("all")
public class MobileSendLinkActionModel extends AbstractMobileSendActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MobileSendLinkAction";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileSendLinkAction.link</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String LINK = "link";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileSendLinkAction.subject</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String SUBJECT = "subject";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MobileSendLinkActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MobileSendLinkActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractMobileSendAction</code> at extension <code>mobileservices</code>
	 * @param _defaultLanguage initial attribute declared by type <code>AbstractMobileSendAction</code> at extension <code>mobileservices</code>
	 * @param _target initial attribute declared by type <code>MobileSendLinkAction</code> at extension <code>mobileservices</code>
	 * @param _type initial attribute declared by type <code>AbstractMobileSendAction</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public MobileSendLinkActionModel(final String _code, final LanguageModel _defaultLanguage, final String _target, final ActionType _type)
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
	 * @param _target initial attribute declared by type <code>MobileSendLinkAction</code> at extension <code>mobileservices</code>
	 * @param _type initial attribute declared by type <code>AbstractMobileSendAction</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public MobileSendLinkActionModel(final String _code, final LanguageModel _defaultLanguage, final ItemModel _owner, final String _target, final ActionType _type)
	{
		super();
		setCode(_code);
		setDefaultLanguage(_defaultLanguage);
		setOwner(_owner);
		setTarget(_target);
		setType(_type);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileSendLinkAction.link</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the link
	 */
	@Accessor(qualifier = "link", type = Accessor.Type.GETTER)
	public String getLink()
	{
		return getLink(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileSendLinkAction.link</code> attribute defined at extension <code>mobileservices</code>. 
	 * @param loc the value localization key 
	 * @return the link
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "link", type = Accessor.Type.GETTER)
	public String getLink(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(LINK, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileSendLinkAction.subject</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the subject
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.GETTER)
	public String getSubject()
	{
		return getSubject(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileSendLinkAction.subject</code> attribute defined at extension <code>mobileservices</code>. 
	 * @param loc the value localization key 
	 * @return the subject
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.GETTER)
	public String getSubject(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(SUBJECT, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileSendLinkAction.link</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the link
	 */
	@Accessor(qualifier = "link", type = Accessor.Type.SETTER)
	public void setLink(final String value)
	{
		setLink(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>MobileSendLinkAction.link</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the link
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "link", type = Accessor.Type.SETTER)
	public void setLink(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(LINK, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileSendLinkAction.subject</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the subject
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.SETTER)
	public void setSubject(final String value)
	{
		setSubject(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>MobileSendLinkAction.subject</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the subject
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "subject", type = Accessor.Type.SETTER)
	public void setSubject(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(SUBJECT, loc, value);
	}
	
}
