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
import de.hybris.platform.mobileservices.enums.MessageType;
import de.hybris.platform.mobileservices.enums.MobileActiveStateType;
import de.hybris.platform.mobileservices.enums.NetworkType;
import de.hybris.platform.mobileservices.model.text.MobileActionAssignmentModel;
import de.hybris.platform.mobileservices.model.text.MobileAggregatorModel;
import de.hybris.platform.mobileservices.model.text.MobileCostPlanModel;
import de.hybris.platform.mobileservices.model.text.MobileReceiveGenericActionModel;
import de.hybris.platform.mobileservices.model.text.PhoneNumberListModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type MobileShortcode first defined at extension mobileservices.
 */
@SuppressWarnings("all")
public class MobileShortcodeModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MobileShortcode";
	
	/**<i>Generated relation code constant for relation <code>AggregatorShortCodeRelation</code> defining source attribute <code>aggregator</code> in extension <code>mobileservices</code>.</i>*/
	public static final String _AGGREGATORSHORTCODERELATION = "AggregatorShortCodeRelation";
	
	/**<i>Generated relation code constant for relation <code>ShortcodeAlternativeRouteRelation</code> defining source attribute <code>connectedShortcodes</code> in extension <code>mobileservices</code>.</i>*/
	public static final String _SHORTCODEALTERNATIVEROUTERELATION = "ShortcodeAlternativeRouteRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileShortcode.code</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileShortcode.keywordPrefix</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String KEYWORDPREFIX = "keywordPrefix";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileShortcode.supportedMessageType</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String SUPPORTEDMESSAGETYPE = "supportedMessageType";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileShortcode.country</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String COUNTRY = "country";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileShortcode.defaultAction</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String DEFAULTACTION = "defaultAction";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileShortcode.networkType</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String NETWORKTYPE = "networkType";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileShortcode.messageMaxSize</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String MESSAGEMAXSIZE = "messageMaxSize";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileShortcode.state</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String STATE = "state";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileShortcode.aggregator</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String AGGREGATOR = "aggregator";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileShortcode.assignments</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String ASSIGNMENTS = "assignments";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileShortcode.connectedShortcodes</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String CONNECTEDSHORTCODES = "connectedShortcodes";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileShortcode.alternativeReplyRoute</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String ALTERNATIVEREPLYROUTE = "alternativeReplyRoute";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileShortcode.revenuePlans</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String REVENUEPLANS = "revenuePlans";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileShortcode.costPlans</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String COSTPLANS = "costPlans";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileShortcode.phoneNumberFilters</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String PHONENUMBERFILTERS = "phoneNumberFilters";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileShortcode.testingPhoneNumbers</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String TESTINGPHONENUMBERS = "testingPhoneNumbers";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MobileShortcodeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MobileShortcodeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _aggregator initial attribute declared by type <code>MobileShortcode</code> at extension <code>mobileservices</code>
	 * @param _code initial attribute declared by type <code>MobileShortcode</code> at extension <code>mobileservices</code>
	 * @param _country initial attribute declared by type <code>MobileShortcode</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public MobileShortcodeModel(final MobileAggregatorModel _aggregator, final String _code, final CountryModel _country)
	{
		super();
		setAggregator(_aggregator);
		setCode(_code);
		setCountry(_country);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _aggregator initial attribute declared by type <code>MobileShortcode</code> at extension <code>mobileservices</code>
	 * @param _code initial attribute declared by type <code>MobileShortcode</code> at extension <code>mobileservices</code>
	 * @param _country initial attribute declared by type <code>MobileShortcode</code> at extension <code>mobileservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public MobileShortcodeModel(final MobileAggregatorModel _aggregator, final String _code, final CountryModel _country, final ItemModel _owner)
	{
		super();
		setAggregator(_aggregator);
		setCode(_code);
		setCountry(_country);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileShortcode.aggregator</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the aggregator
	 */
	@Accessor(qualifier = "aggregator", type = Accessor.Type.GETTER)
	public MobileAggregatorModel getAggregator()
	{
		return getPersistenceContext().getPropertyValue(AGGREGATOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileShortcode.alternativeReplyRoute</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the alternativeReplyRoute
	 */
	@Accessor(qualifier = "alternativeReplyRoute", type = Accessor.Type.GETTER)
	public MobileShortcodeModel getAlternativeReplyRoute()
	{
		return getPersistenceContext().getPropertyValue(ALTERNATIVEREPLYROUTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileShortcode.assignments</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the assignments
	 */
	@Accessor(qualifier = "assignments", type = Accessor.Type.GETTER)
	public Collection<MobileActionAssignmentModel> getAssignments()
	{
		return getPersistenceContext().getPropertyValue(ASSIGNMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileShortcode.code</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileShortcode.connectedShortcodes</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the connectedShortcodes
	 */
	@Accessor(qualifier = "connectedShortcodes", type = Accessor.Type.GETTER)
	public Collection<MobileShortcodeModel> getConnectedShortcodes()
	{
		return getPersistenceContext().getPropertyValue(CONNECTEDSHORTCODES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileShortcode.costPlans</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the costPlans
	 */
	@Accessor(qualifier = "costPlans", type = Accessor.Type.GETTER)
	public Collection<MobileCostPlanModel> getCostPlans()
	{
		return getPersistenceContext().getPropertyValue(COSTPLANS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileShortcode.country</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the country
	 */
	@Accessor(qualifier = "country", type = Accessor.Type.GETTER)
	public CountryModel getCountry()
	{
		return getPersistenceContext().getPropertyValue(COUNTRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileShortcode.defaultAction</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the defaultAction
	 */
	@Accessor(qualifier = "defaultAction", type = Accessor.Type.GETTER)
	public MobileReceiveGenericActionModel getDefaultAction()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTACTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileShortcode.keywordPrefix</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the keywordPrefix
	 */
	@Accessor(qualifier = "keywordPrefix", type = Accessor.Type.GETTER)
	public String getKeywordPrefix()
	{
		return getPersistenceContext().getPropertyValue(KEYWORDPREFIX);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileShortcode.messageMaxSize</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the messageMaxSize
	 */
	@Accessor(qualifier = "messageMaxSize", type = Accessor.Type.GETTER)
	public Integer getMessageMaxSize()
	{
		return getPersistenceContext().getPropertyValue(MESSAGEMAXSIZE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileShortcode.networkType</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the networkType
	 */
	@Accessor(qualifier = "networkType", type = Accessor.Type.GETTER)
	public NetworkType getNetworkType()
	{
		return getPersistenceContext().getPropertyValue(NETWORKTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileShortcode.phoneNumberFilters</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the phoneNumberFilters
	 */
	@Accessor(qualifier = "phoneNumberFilters", type = Accessor.Type.GETTER)
	public Collection<PhoneNumberListModel> getPhoneNumberFilters()
	{
		return getPersistenceContext().getPropertyValue(PHONENUMBERFILTERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileShortcode.revenuePlans</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the revenuePlans
	 */
	@Accessor(qualifier = "revenuePlans", type = Accessor.Type.GETTER)
	public Collection<MobileCostPlanModel> getRevenuePlans()
	{
		return getPersistenceContext().getPropertyValue(REVENUEPLANS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileShortcode.state</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the state
	 */
	@Accessor(qualifier = "state", type = Accessor.Type.GETTER)
	public MobileActiveStateType getState()
	{
		return getPersistenceContext().getPropertyValue(STATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileShortcode.supportedMessageType</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the supportedMessageType
	 */
	@Accessor(qualifier = "supportedMessageType", type = Accessor.Type.GETTER)
	public MessageType getSupportedMessageType()
	{
		return getPersistenceContext().getPropertyValue(SUPPORTEDMESSAGETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileShortcode.testingPhoneNumbers</code> attribute defined at extension <code>mobileservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the testingPhoneNumbers
	 */
	@Accessor(qualifier = "testingPhoneNumbers", type = Accessor.Type.GETTER)
	public Collection<PhoneNumberListModel> getTestingPhoneNumbers()
	{
		return getPersistenceContext().getPropertyValue(TESTINGPHONENUMBERS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileShortcode.aggregator</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the aggregator
	 */
	@Accessor(qualifier = "aggregator", type = Accessor.Type.SETTER)
	public void setAggregator(final MobileAggregatorModel value)
	{
		getPersistenceContext().setPropertyValue(AGGREGATOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileShortcode.alternativeReplyRoute</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the alternativeReplyRoute
	 */
	@Accessor(qualifier = "alternativeReplyRoute", type = Accessor.Type.SETTER)
	public void setAlternativeReplyRoute(final MobileShortcodeModel value)
	{
		getPersistenceContext().setPropertyValue(ALTERNATIVEREPLYROUTE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileShortcode.assignments</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the assignments
	 */
	@Accessor(qualifier = "assignments", type = Accessor.Type.SETTER)
	public void setAssignments(final Collection<MobileActionAssignmentModel> value)
	{
		getPersistenceContext().setPropertyValue(ASSIGNMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileShortcode.code</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileShortcode.connectedShortcodes</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the connectedShortcodes
	 */
	@Accessor(qualifier = "connectedShortcodes", type = Accessor.Type.SETTER)
	public void setConnectedShortcodes(final Collection<MobileShortcodeModel> value)
	{
		getPersistenceContext().setPropertyValue(CONNECTEDSHORTCODES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileShortcode.costPlans</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the costPlans
	 */
	@Accessor(qualifier = "costPlans", type = Accessor.Type.SETTER)
	public void setCostPlans(final Collection<MobileCostPlanModel> value)
	{
		getPersistenceContext().setPropertyValue(COSTPLANS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileShortcode.country</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the country
	 */
	@Accessor(qualifier = "country", type = Accessor.Type.SETTER)
	public void setCountry(final CountryModel value)
	{
		getPersistenceContext().setPropertyValue(COUNTRY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileShortcode.defaultAction</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the defaultAction
	 */
	@Accessor(qualifier = "defaultAction", type = Accessor.Type.SETTER)
	public void setDefaultAction(final MobileReceiveGenericActionModel value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTACTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileShortcode.keywordPrefix</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the keywordPrefix
	 */
	@Accessor(qualifier = "keywordPrefix", type = Accessor.Type.SETTER)
	public void setKeywordPrefix(final String value)
	{
		getPersistenceContext().setPropertyValue(KEYWORDPREFIX, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileShortcode.messageMaxSize</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the messageMaxSize
	 */
	@Accessor(qualifier = "messageMaxSize", type = Accessor.Type.SETTER)
	public void setMessageMaxSize(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MESSAGEMAXSIZE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileShortcode.networkType</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the networkType
	 */
	@Accessor(qualifier = "networkType", type = Accessor.Type.SETTER)
	public void setNetworkType(final NetworkType value)
	{
		getPersistenceContext().setPropertyValue(NETWORKTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileShortcode.phoneNumberFilters</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the phoneNumberFilters
	 */
	@Accessor(qualifier = "phoneNumberFilters", type = Accessor.Type.SETTER)
	public void setPhoneNumberFilters(final Collection<PhoneNumberListModel> value)
	{
		getPersistenceContext().setPropertyValue(PHONENUMBERFILTERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileShortcode.revenuePlans</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the revenuePlans
	 */
	@Accessor(qualifier = "revenuePlans", type = Accessor.Type.SETTER)
	public void setRevenuePlans(final Collection<MobileCostPlanModel> value)
	{
		getPersistenceContext().setPropertyValue(REVENUEPLANS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileShortcode.state</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the state
	 */
	@Accessor(qualifier = "state", type = Accessor.Type.SETTER)
	public void setState(final MobileActiveStateType value)
	{
		getPersistenceContext().setPropertyValue(STATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileShortcode.supportedMessageType</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the supportedMessageType
	 */
	@Accessor(qualifier = "supportedMessageType", type = Accessor.Type.SETTER)
	public void setSupportedMessageType(final MessageType value)
	{
		getPersistenceContext().setPropertyValue(SUPPORTEDMESSAGETYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileShortcode.testingPhoneNumbers</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the testingPhoneNumbers
	 */
	@Accessor(qualifier = "testingPhoneNumbers", type = Accessor.Type.SETTER)
	public void setTestingPhoneNumbers(final Collection<PhoneNumberListModel> value)
	{
		getPersistenceContext().setPropertyValue(TESTINGPHONENUMBERS, value);
	}
	
}
