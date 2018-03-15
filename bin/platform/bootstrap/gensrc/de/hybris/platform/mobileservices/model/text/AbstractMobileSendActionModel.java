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
import de.hybris.platform.mobileservices.model.text.PhoneNumberListModel;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import java.util.Collection;

/**
 * Generated model class for type AbstractMobileSendAction first defined at extension mobileservices.
 */
@SuppressWarnings("all")
public class AbstractMobileSendActionModel extends AbstractActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractMobileSendAction";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractMobileSendAction.defaultLanguage</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String DEFAULTLANGUAGE = "defaultLanguage";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractMobileSendAction.filterLists</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String FILTERLISTS = "filterLists";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractMobileSendActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractMobileSendActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractMobileSendAction</code> at extension <code>mobileservices</code>
	 * @param _defaultLanguage initial attribute declared by type <code>AbstractMobileSendAction</code> at extension <code>mobileservices</code>
	 * @param _target initial attribute declared by type <code>AbstractMobileSendAction</code> at extension <code>mobileservices</code>
	 * @param _type initial attribute declared by type <code>AbstractMobileSendAction</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public AbstractMobileSendActionModel(final String _code, final LanguageModel _defaultLanguage, final String _target, final ActionType _type)
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
	 * @param _target initial attribute declared by type <code>AbstractMobileSendAction</code> at extension <code>mobileservices</code>
	 * @param _type initial attribute declared by type <code>AbstractMobileSendAction</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public AbstractMobileSendActionModel(final String _code, final LanguageModel _defaultLanguage, final ItemModel _owner, final String _target, final ActionType _type)
	{
		super();
		setCode(_code);
		setDefaultLanguage(_defaultLanguage);
		setOwner(_owner);
		setTarget(_target);
		setType(_type);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractMobileSendAction.defaultLanguage</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the defaultLanguage
	 */
	@Accessor(qualifier = "defaultLanguage", type = Accessor.Type.GETTER)
	public LanguageModel getDefaultLanguage()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTLANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractMobileSendAction.filterLists</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the filterLists
	 */
	@Accessor(qualifier = "filterLists", type = Accessor.Type.GETTER)
	public Collection<PhoneNumberListModel> getFilterLists()
	{
		return getPersistenceContext().getPropertyValue(FILTERLISTS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractAction.code</code> attribute defined at extension <code>processing</code> and redeclared at extension <code>mobileservices</code>. Will only accept values of type {@link java.lang.String}. 
	 *  
	 * @param value the code
	 */
	@Override
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		super.setCode(value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractMobileSendAction.defaultLanguage</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the defaultLanguage
	 */
	@Accessor(qualifier = "defaultLanguage", type = Accessor.Type.SETTER)
	public void setDefaultLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTLANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractMobileSendAction.filterLists</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the filterLists
	 */
	@Accessor(qualifier = "filterLists", type = Accessor.Type.SETTER)
	public void setFilterLists(final Collection<PhoneNumberListModel> value)
	{
		getPersistenceContext().setPropertyValue(FILTERLISTS, value);
	}
	
}
