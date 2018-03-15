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
import de.hybris.platform.mobileservices.enums.MobileActionAssignmentStateType;
import de.hybris.platform.mobileservices.model.text.MobileActionKeywordModel;
import de.hybris.platform.mobileservices.model.text.MobileReceiveGenericActionModel;
import de.hybris.platform.mobileservices.model.text.MobileShortcodeModel;
import de.hybris.platform.mobileservices.model.text.PhoneNumberListModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;

/**
 * Generated model class for type MobileActionAssignment first defined at extension mobileservices.
 */
@SuppressWarnings("all")
public class MobileActionAssignmentModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MobileActionAssignment";
	
	/**<i>Generated relation code constant for relation <code>ShortcodesActionAssignmentsRelation</code> defining source attribute <code>shortcode</code> in extension <code>mobileservices</code>.</i>*/
	public static final String _SHORTCODESACTIONASSIGNMENTSRELATION = "ShortcodesActionAssignmentsRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileActionAssignment.action</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String ACTION = "action";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileActionAssignment.keyword</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String KEYWORD = "keyword";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileActionAssignment.state</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String STATE = "state";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileActionAssignment.language</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String LANGUAGE = "language";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileActionAssignment.startDate</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String STARTDATE = "startDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileActionAssignment.endDate</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String ENDDATE = "endDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileActionAssignment.shortcode</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String SHORTCODE = "shortcode";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileActionAssignment.phoneNumberFilters</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String PHONENUMBERFILTERS = "phoneNumberFilters";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileActionAssignment.testingPhoneNumbers</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String TESTINGPHONENUMBERS = "testingPhoneNumbers";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MobileActionAssignmentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MobileActionAssignmentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _action initial attribute declared by type <code>MobileActionAssignment</code> at extension <code>mobileservices</code>
	 * @param _keyword initial attribute declared by type <code>MobileActionAssignment</code> at extension <code>mobileservices</code>
	 * @param _shortcode initial attribute declared by type <code>MobileActionAssignment</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public MobileActionAssignmentModel(final MobileReceiveGenericActionModel _action, final MobileActionKeywordModel _keyword, final MobileShortcodeModel _shortcode)
	{
		super();
		setAction(_action);
		setKeyword(_keyword);
		setShortcode(_shortcode);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _action initial attribute declared by type <code>MobileActionAssignment</code> at extension <code>mobileservices</code>
	 * @param _keyword initial attribute declared by type <code>MobileActionAssignment</code> at extension <code>mobileservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _shortcode initial attribute declared by type <code>MobileActionAssignment</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public MobileActionAssignmentModel(final MobileReceiveGenericActionModel _action, final MobileActionKeywordModel _keyword, final ItemModel _owner, final MobileShortcodeModel _shortcode)
	{
		super();
		setAction(_action);
		setKeyword(_keyword);
		setOwner(_owner);
		setShortcode(_shortcode);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileActionAssignment.action</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the action
	 */
	@Accessor(qualifier = "action", type = Accessor.Type.GETTER)
	public MobileReceiveGenericActionModel getAction()
	{
		return getPersistenceContext().getPropertyValue(ACTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileActionAssignment.endDate</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the endDate
	 */
	@Accessor(qualifier = "endDate", type = Accessor.Type.GETTER)
	public Date getEndDate()
	{
		return getPersistenceContext().getPropertyValue(ENDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileActionAssignment.keyword</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the keyword
	 */
	@Accessor(qualifier = "keyword", type = Accessor.Type.GETTER)
	public MobileActionKeywordModel getKeyword()
	{
		return getPersistenceContext().getPropertyValue(KEYWORD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileActionAssignment.language</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public LanguageModel getLanguage()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileActionAssignment.phoneNumberFilters</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the phoneNumberFilters
	 */
	@Accessor(qualifier = "phoneNumberFilters", type = Accessor.Type.GETTER)
	public Collection<PhoneNumberListModel> getPhoneNumberFilters()
	{
		return getPersistenceContext().getPropertyValue(PHONENUMBERFILTERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileActionAssignment.shortcode</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the shortcode
	 */
	@Accessor(qualifier = "shortcode", type = Accessor.Type.GETTER)
	public MobileShortcodeModel getShortcode()
	{
		return getPersistenceContext().getPropertyValue(SHORTCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileActionAssignment.startDate</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the startDate
	 */
	@Accessor(qualifier = "startDate", type = Accessor.Type.GETTER)
	public Date getStartDate()
	{
		return getPersistenceContext().getPropertyValue(STARTDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileActionAssignment.state</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the state
	 */
	@Accessor(qualifier = "state", type = Accessor.Type.GETTER)
	public MobileActionAssignmentStateType getState()
	{
		return getPersistenceContext().getPropertyValue(STATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileActionAssignment.testingPhoneNumbers</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the testingPhoneNumbers
	 */
	@Accessor(qualifier = "testingPhoneNumbers", type = Accessor.Type.GETTER)
	public Collection<PhoneNumberListModel> getTestingPhoneNumbers()
	{
		return getPersistenceContext().getPropertyValue(TESTINGPHONENUMBERS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileActionAssignment.action</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the action
	 */
	@Accessor(qualifier = "action", type = Accessor.Type.SETTER)
	public void setAction(final MobileReceiveGenericActionModel value)
	{
		getPersistenceContext().setPropertyValue(ACTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileActionAssignment.endDate</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the endDate
	 */
	@Accessor(qualifier = "endDate", type = Accessor.Type.SETTER)
	public void setEndDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(ENDDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileActionAssignment.keyword</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the keyword
	 */
	@Accessor(qualifier = "keyword", type = Accessor.Type.SETTER)
	public void setKeyword(final MobileActionKeywordModel value)
	{
		getPersistenceContext().setPropertyValue(KEYWORD, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileActionAssignment.language</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileActionAssignment.phoneNumberFilters</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the phoneNumberFilters
	 */
	@Accessor(qualifier = "phoneNumberFilters", type = Accessor.Type.SETTER)
	public void setPhoneNumberFilters(final Collection<PhoneNumberListModel> value)
	{
		getPersistenceContext().setPropertyValue(PHONENUMBERFILTERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileActionAssignment.shortcode</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the shortcode
	 */
	@Accessor(qualifier = "shortcode", type = Accessor.Type.SETTER)
	public void setShortcode(final MobileShortcodeModel value)
	{
		getPersistenceContext().setPropertyValue(SHORTCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileActionAssignment.startDate</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the startDate
	 */
	@Accessor(qualifier = "startDate", type = Accessor.Type.SETTER)
	public void setStartDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(STARTDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileActionAssignment.state</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the state
	 */
	@Accessor(qualifier = "state", type = Accessor.Type.SETTER)
	public void setState(final MobileActionAssignmentStateType value)
	{
		getPersistenceContext().setPropertyValue(STATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileActionAssignment.testingPhoneNumbers</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the testingPhoneNumbers
	 */
	@Accessor(qualifier = "testingPhoneNumbers", type = Accessor.Type.SETTER)
	public void setTestingPhoneNumbers(final Collection<PhoneNumberListModel> value)
	{
		getPersistenceContext().setPropertyValue(TESTINGPHONENUMBERS, value);
	}
	
}
