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
import de.hybris.platform.mobileservices.model.text.AbstractMobileSendActionModel;
import de.hybris.platform.mobileservices.model.text.MobileActionAssignmentModel;
import de.hybris.platform.mobileservices.model.text.MobileShortcodeModel;
import de.hybris.platform.mobileservices.model.text.PhoneNumberModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type PhoneNumberList first defined at extension mobileservices.
 */
@SuppressWarnings("all")
public class PhoneNumberListModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PhoneNumberList";
	
	/**<i>Generated relation code constant for relation <code>ShortcodeListRelation</code> defining source attribute <code>blockedShortcodes</code> in extension <code>mobileservices</code>.</i>*/
	public static final String _SHORTCODELISTRELATION = "ShortcodeListRelation";
	
	/**<i>Generated relation code constant for relation <code>ShortcodeTestListRelation</code> defining source attribute <code>testingShortcodes</code> in extension <code>mobileservices</code>.</i>*/
	public static final String _SHORTCODETESTLISTRELATION = "ShortcodeTestListRelation";
	
	/**<i>Generated relation code constant for relation <code>AssignmentListRelation</code> defining source attribute <code>blockedAssignments</code> in extension <code>mobileservices</code>.</i>*/
	public static final String _ASSIGNMENTLISTRELATION = "AssignmentListRelation";
	
	/**<i>Generated relation code constant for relation <code>AssignmentTestListRelation</code> defining source attribute <code>testingAssignments</code> in extension <code>mobileservices</code>.</i>*/
	public static final String _ASSIGNMENTTESTLISTRELATION = "AssignmentTestListRelation";
	
	/**<i>Generated relation code constant for relation <code>SendActionsPhoneListRelation</code> defining source attribute <code>sendingActions</code> in extension <code>mobileservices</code>.</i>*/
	public static final String _SENDACTIONSPHONELISTRELATION = "SendActionsPhoneListRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>PhoneNumberList.code</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>PhoneNumberList.name</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>PhoneNumberList.memberSize</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String MEMBERSIZE = "memberSize";
	
	/** <i>Generated constant</i> - Attribute key of <code>PhoneNumberList.numbers</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String NUMBERS = "numbers";
	
	/** <i>Generated constant</i> - Attribute key of <code>PhoneNumberList.blockedShortcodes</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String BLOCKEDSHORTCODES = "blockedShortcodes";
	
	/** <i>Generated constant</i> - Attribute key of <code>PhoneNumberList.testingShortcodes</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String TESTINGSHORTCODES = "testingShortcodes";
	
	/** <i>Generated constant</i> - Attribute key of <code>PhoneNumberList.blockedAssignments</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String BLOCKEDASSIGNMENTS = "blockedAssignments";
	
	/** <i>Generated constant</i> - Attribute key of <code>PhoneNumberList.testingAssignments</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String TESTINGASSIGNMENTS = "testingAssignments";
	
	/** <i>Generated constant</i> - Attribute key of <code>PhoneNumberList.sendingActions</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String SENDINGACTIONS = "sendingActions";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PhoneNumberListModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PhoneNumberListModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>PhoneNumberList</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public PhoneNumberListModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>PhoneNumberList</code> at extension <code>mobileservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public PhoneNumberListModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PhoneNumberList.blockedAssignments</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the blockedAssignments
	 */
	@Accessor(qualifier = "blockedAssignments", type = Accessor.Type.GETTER)
	public Collection<MobileActionAssignmentModel> getBlockedAssignments()
	{
		return getPersistenceContext().getPropertyValue(BLOCKEDASSIGNMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PhoneNumberList.blockedShortcodes</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the blockedShortcodes
	 */
	@Accessor(qualifier = "blockedShortcodes", type = Accessor.Type.GETTER)
	public Collection<MobileShortcodeModel> getBlockedShortcodes()
	{
		return getPersistenceContext().getPropertyValue(BLOCKEDSHORTCODES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PhoneNumberList.code</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PhoneNumberList.memberSize</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the memberSize
	 */
	@Accessor(qualifier = "memberSize", type = Accessor.Type.GETTER)
	public Integer getMemberSize()
	{
		return getPersistenceContext().getPropertyValue(MEMBERSIZE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PhoneNumberList.name</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>PhoneNumberList.name</code> attribute defined at extension <code>mobileservices</code>. 
	 * @param loc the value localization key 
	 * @return the name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PhoneNumberList.numbers</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the numbers
	 */
	@Accessor(qualifier = "numbers", type = Accessor.Type.GETTER)
	public Collection<PhoneNumberModel> getNumbers()
	{
		return getPersistenceContext().getPropertyValue(NUMBERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PhoneNumberList.sendingActions</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the sendingActions
	 */
	@Accessor(qualifier = "sendingActions", type = Accessor.Type.GETTER)
	public Collection<AbstractMobileSendActionModel> getSendingActions()
	{
		return getPersistenceContext().getPropertyValue(SENDINGACTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PhoneNumberList.testingAssignments</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the testingAssignments
	 */
	@Accessor(qualifier = "testingAssignments", type = Accessor.Type.GETTER)
	public Collection<MobileActionAssignmentModel> getTestingAssignments()
	{
		return getPersistenceContext().getPropertyValue(TESTINGASSIGNMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PhoneNumberList.testingShortcodes</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the testingShortcodes
	 */
	@Accessor(qualifier = "testingShortcodes", type = Accessor.Type.GETTER)
	public Collection<MobileShortcodeModel> getTestingShortcodes()
	{
		return getPersistenceContext().getPropertyValue(TESTINGSHORTCODES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PhoneNumberList.blockedAssignments</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the blockedAssignments
	 */
	@Accessor(qualifier = "blockedAssignments", type = Accessor.Type.SETTER)
	public void setBlockedAssignments(final Collection<MobileActionAssignmentModel> value)
	{
		getPersistenceContext().setPropertyValue(BLOCKEDASSIGNMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PhoneNumberList.blockedShortcodes</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the blockedShortcodes
	 */
	@Accessor(qualifier = "blockedShortcodes", type = Accessor.Type.SETTER)
	public void setBlockedShortcodes(final Collection<MobileShortcodeModel> value)
	{
		getPersistenceContext().setPropertyValue(BLOCKEDSHORTCODES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PhoneNumberList.code</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PhoneNumberList.name</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>PhoneNumberList.name</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PhoneNumberList.numbers</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the numbers
	 */
	@Accessor(qualifier = "numbers", type = Accessor.Type.SETTER)
	public void setNumbers(final Collection<PhoneNumberModel> value)
	{
		getPersistenceContext().setPropertyValue(NUMBERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PhoneNumberList.sendingActions</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the sendingActions
	 */
	@Accessor(qualifier = "sendingActions", type = Accessor.Type.SETTER)
	public void setSendingActions(final Collection<AbstractMobileSendActionModel> value)
	{
		getPersistenceContext().setPropertyValue(SENDINGACTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PhoneNumberList.testingAssignments</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the testingAssignments
	 */
	@Accessor(qualifier = "testingAssignments", type = Accessor.Type.SETTER)
	public void setTestingAssignments(final Collection<MobileActionAssignmentModel> value)
	{
		getPersistenceContext().setPropertyValue(TESTINGASSIGNMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PhoneNumberList.testingShortcodes</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the testingShortcodes
	 */
	@Accessor(qualifier = "testingShortcodes", type = Accessor.Type.SETTER)
	public void setTestingShortcodes(final Collection<MobileShortcodeModel> value)
	{
		getPersistenceContext().setPropertyValue(TESTINGSHORTCODES, value);
	}
	
}
