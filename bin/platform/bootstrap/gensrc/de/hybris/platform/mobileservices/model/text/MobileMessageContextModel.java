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
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.mobileservices.enums.MessageType;
import de.hybris.platform.mobileservices.enums.MobileAggregatorError;
import de.hybris.platform.mobileservices.enums.MobileMessageError;
import de.hybris.platform.mobileservices.enums.MobileMessageStatus;
import de.hybris.platform.mobileservices.model.text.MobileActionAssignmentModel;
import de.hybris.platform.mobileservices.model.text.MobileShortcodeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import java.math.BigDecimal;

/**
 * Generated model class for type MobileMessageContext first defined at extension mobileservices.
 */
@SuppressWarnings("all")
public class MobileMessageContextModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MobileMessageContext";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.incomingMessageId</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String INCOMINGMESSAGEID = "incomingMessageId";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.incomingText</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String INCOMINGTEXT = "incomingText";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.outgoingMessageId</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String OUTGOINGMESSAGEID = "outgoingMessageId";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.outgoingText</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String OUTGOINGTEXT = "outgoingText";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.outgoingSubject</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String OUTGOINGSUBJECT = "outgoingSubject";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.phoneNumber</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String PHONENUMBER = "phoneNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.normalizedPhoneNumber</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String NORMALIZEDPHONENUMBER = "normalizedPhoneNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.countryIsoCode</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String COUNTRYISOCODE = "countryIsoCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.phoneCountryIsoCode</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String PHONECOUNTRYISOCODE = "phoneCountryIsoCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.type</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String TYPE = "type";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.user</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String USER = "user";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.status</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.usingDefaultAction</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String USINGDEFAULTACTION = "usingDefaultAction";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.messageError</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String MESSAGEERROR = "messageError";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.messageErrorDescription</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String MESSAGEERRORDESCRIPTION = "messageErrorDescription";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.aggregatorError</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String AGGREGATORERROR = "aggregatorError";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.aggregatorErrorDescription</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String AGGREGATORERRORDESCRIPTION = "aggregatorErrorDescription";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.isLink</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String ISLINK = "isLink";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.shortcode</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String SHORTCODE = "shortcode";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.normalizedShortcode</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String NORMALIZEDSHORTCODE = "normalizedShortcode";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.matchedActionAssignment</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String MATCHEDACTIONASSIGNMENT = "matchedActionAssignment";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.matchedAction</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String MATCHEDACTION = "matchedAction";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.matchedShortcode</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String MATCHEDSHORTCODE = "matchedShortcode";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.outgoingShortcode</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String OUTGOINGSHORTCODE = "outgoingShortcode";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.failures</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String FAILURES = "failures";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.price</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String PRICE = "price";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.priceCurrency</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String PRICECURRENCY = "priceCurrency";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.revenue</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String REVENUE = "revenue";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.revenueCurrency</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String REVENUECURRENCY = "revenueCurrency";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.optOperator</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String OPTOPERATOR = "optOperator";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.optTariff</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String OPTTARIFF = "optTariff";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.optSessionId</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String OPTSESSIONID = "optSessionId";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.rawMessageDetails</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String RAWMESSAGEDETAILS = "rawMessageDetails";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.incomingEngineId</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String INCOMINGENGINEID = "incomingEngineId";
	
	/** <i>Generated constant</i> - Attribute key of <code>MobileMessageContext.outgoingEngineId</code> attribute defined at extension <code>mobileservices</code>. */
	public static final String OUTGOINGENGINEID = "outgoingEngineId";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MobileMessageContextModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MobileMessageContextModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _phoneNumber initial attribute declared by type <code>MobileMessageContext</code> at extension <code>mobileservices</code>
	 * @param _status initial attribute declared by type <code>MobileMessageContext</code> at extension <code>mobileservices</code>
	 * @param _type initial attribute declared by type <code>MobileMessageContext</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public MobileMessageContextModel(final String _phoneNumber, final MobileMessageStatus _status, final MessageType _type)
	{
		super();
		setPhoneNumber(_phoneNumber);
		setStatus(_status);
		setType(_type);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _incomingEngineId initial attribute declared by type <code>MobileMessageContext</code> at extension <code>mobileservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _phoneNumber initial attribute declared by type <code>MobileMessageContext</code> at extension <code>mobileservices</code>
	 * @param _rawMessageDetails initial attribute declared by type <code>MobileMessageContext</code> at extension <code>mobileservices</code>
	 * @param _status initial attribute declared by type <code>MobileMessageContext</code> at extension <code>mobileservices</code>
	 * @param _type initial attribute declared by type <code>MobileMessageContext</code> at extension <code>mobileservices</code>
	 */
	@Deprecated
	public MobileMessageContextModel(final String _incomingEngineId, final ItemModel _owner, final String _phoneNumber, final String _rawMessageDetails, final MobileMessageStatus _status, final MessageType _type)
	{
		super();
		setIncomingEngineId(_incomingEngineId);
		setOwner(_owner);
		setPhoneNumber(_phoneNumber);
		setRawMessageDetails(_rawMessageDetails);
		setStatus(_status);
		setType(_type);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.aggregatorError</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the aggregatorError
	 */
	@Accessor(qualifier = "aggregatorError", type = Accessor.Type.GETTER)
	public MobileAggregatorError getAggregatorError()
	{
		return getPersistenceContext().getPropertyValue(AGGREGATORERROR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.aggregatorErrorDescription</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the aggregatorErrorDescription
	 */
	@Accessor(qualifier = "aggregatorErrorDescription", type = Accessor.Type.GETTER)
	public String getAggregatorErrorDescription()
	{
		return getPersistenceContext().getPropertyValue(AGGREGATORERRORDESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.countryIsoCode</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the countryIsoCode
	 */
	@Accessor(qualifier = "countryIsoCode", type = Accessor.Type.GETTER)
	public String getCountryIsoCode()
	{
		return getPersistenceContext().getPropertyValue(COUNTRYISOCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.failures</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the failures
	 */
	@Accessor(qualifier = "failures", type = Accessor.Type.GETTER)
	public Integer getFailures()
	{
		return getPersistenceContext().getPropertyValue(FAILURES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.incomingEngineId</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the incomingEngineId
	 */
	@Accessor(qualifier = "incomingEngineId", type = Accessor.Type.GETTER)
	public String getIncomingEngineId()
	{
		return getPersistenceContext().getPropertyValue(INCOMINGENGINEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.incomingMessageId</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the incomingMessageId
	 */
	@Accessor(qualifier = "incomingMessageId", type = Accessor.Type.GETTER)
	public String getIncomingMessageId()
	{
		return getPersistenceContext().getPropertyValue(INCOMINGMESSAGEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.incomingText</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the incomingText
	 */
	@Accessor(qualifier = "incomingText", type = Accessor.Type.GETTER)
	public String getIncomingText()
	{
		return getPersistenceContext().getPropertyValue(INCOMINGTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.isLink</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the isLink
	 */
	@Accessor(qualifier = "isLink", type = Accessor.Type.GETTER)
	public Boolean getIsLink()
	{
		return getPersistenceContext().getPropertyValue(ISLINK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.matchedAction</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the matchedAction
	 */
	@Accessor(qualifier = "matchedAction", type = Accessor.Type.GETTER)
	public AbstractActionModel getMatchedAction()
	{
		return getPersistenceContext().getPropertyValue(MATCHEDACTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.matchedActionAssignment</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the matchedActionAssignment
	 */
	@Accessor(qualifier = "matchedActionAssignment", type = Accessor.Type.GETTER)
	public MobileActionAssignmentModel getMatchedActionAssignment()
	{
		return getPersistenceContext().getPropertyValue(MATCHEDACTIONASSIGNMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.matchedShortcode</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the matchedShortcode
	 */
	@Accessor(qualifier = "matchedShortcode", type = Accessor.Type.GETTER)
	public MobileShortcodeModel getMatchedShortcode()
	{
		return getPersistenceContext().getPropertyValue(MATCHEDSHORTCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.messageError</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the messageError
	 */
	@Accessor(qualifier = "messageError", type = Accessor.Type.GETTER)
	public MobileMessageError getMessageError()
	{
		return getPersistenceContext().getPropertyValue(MESSAGEERROR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.messageErrorDescription</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the messageErrorDescription
	 */
	@Accessor(qualifier = "messageErrorDescription", type = Accessor.Type.GETTER)
	public String getMessageErrorDescription()
	{
		return getPersistenceContext().getPropertyValue(MESSAGEERRORDESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.normalizedPhoneNumber</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the normalizedPhoneNumber
	 */
	@Accessor(qualifier = "normalizedPhoneNumber", type = Accessor.Type.GETTER)
	public String getNormalizedPhoneNumber()
	{
		return getPersistenceContext().getPropertyValue(NORMALIZEDPHONENUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.normalizedShortcode</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the normalizedShortcode
	 */
	@Accessor(qualifier = "normalizedShortcode", type = Accessor.Type.GETTER)
	public String getNormalizedShortcode()
	{
		return getPersistenceContext().getPropertyValue(NORMALIZEDSHORTCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.optOperator</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the optOperator
	 */
	@Accessor(qualifier = "optOperator", type = Accessor.Type.GETTER)
	public String getOptOperator()
	{
		return getPersistenceContext().getPropertyValue(OPTOPERATOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.optSessionId</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the optSessionId
	 */
	@Accessor(qualifier = "optSessionId", type = Accessor.Type.GETTER)
	public String getOptSessionId()
	{
		return getPersistenceContext().getPropertyValue(OPTSESSIONID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.optTariff</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the optTariff
	 */
	@Accessor(qualifier = "optTariff", type = Accessor.Type.GETTER)
	public String getOptTariff()
	{
		return getPersistenceContext().getPropertyValue(OPTTARIFF);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.outgoingEngineId</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the outgoingEngineId
	 */
	@Accessor(qualifier = "outgoingEngineId", type = Accessor.Type.GETTER)
	public String getOutgoingEngineId()
	{
		return getPersistenceContext().getPropertyValue(OUTGOINGENGINEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.outgoingMessageId</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the outgoingMessageId
	 */
	@Accessor(qualifier = "outgoingMessageId", type = Accessor.Type.GETTER)
	public String getOutgoingMessageId()
	{
		return getPersistenceContext().getPropertyValue(OUTGOINGMESSAGEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.outgoingShortcode</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the outgoingShortcode
	 */
	@Accessor(qualifier = "outgoingShortcode", type = Accessor.Type.GETTER)
	public MobileShortcodeModel getOutgoingShortcode()
	{
		return getPersistenceContext().getPropertyValue(OUTGOINGSHORTCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.outgoingSubject</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the outgoingSubject
	 */
	@Accessor(qualifier = "outgoingSubject", type = Accessor.Type.GETTER)
	public String getOutgoingSubject()
	{
		return getPersistenceContext().getPropertyValue(OUTGOINGSUBJECT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.outgoingText</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the outgoingText
	 */
	@Accessor(qualifier = "outgoingText", type = Accessor.Type.GETTER)
	public String getOutgoingText()
	{
		return getPersistenceContext().getPropertyValue(OUTGOINGTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.phoneCountryIsoCode</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the phoneCountryIsoCode
	 */
	@Accessor(qualifier = "phoneCountryIsoCode", type = Accessor.Type.GETTER)
	public String getPhoneCountryIsoCode()
	{
		return getPersistenceContext().getPropertyValue(PHONECOUNTRYISOCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.phoneNumber</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the phoneNumber
	 */
	@Accessor(qualifier = "phoneNumber", type = Accessor.Type.GETTER)
	public String getPhoneNumber()
	{
		return getPersistenceContext().getPropertyValue(PHONENUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.price</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the price
	 */
	@Accessor(qualifier = "price", type = Accessor.Type.GETTER)
	public BigDecimal getPrice()
	{
		return getPersistenceContext().getPropertyValue(PRICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.priceCurrency</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the priceCurrency
	 */
	@Accessor(qualifier = "priceCurrency", type = Accessor.Type.GETTER)
	public CurrencyModel getPriceCurrency()
	{
		return getPersistenceContext().getPropertyValue(PRICECURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.rawMessageDetails</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the rawMessageDetails
	 */
	@Accessor(qualifier = "rawMessageDetails", type = Accessor.Type.GETTER)
	public String getRawMessageDetails()
	{
		return getPersistenceContext().getPropertyValue(RAWMESSAGEDETAILS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.revenue</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the revenue
	 */
	@Accessor(qualifier = "revenue", type = Accessor.Type.GETTER)
	public BigDecimal getRevenue()
	{
		return getPersistenceContext().getPropertyValue(REVENUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.revenueCurrency</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the revenueCurrency
	 */
	@Accessor(qualifier = "revenueCurrency", type = Accessor.Type.GETTER)
	public CurrencyModel getRevenueCurrency()
	{
		return getPersistenceContext().getPropertyValue(REVENUECURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.shortcode</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the shortcode
	 */
	@Accessor(qualifier = "shortcode", type = Accessor.Type.GETTER)
	public String getShortcode()
	{
		return getPersistenceContext().getPropertyValue(SHORTCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.status</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public MobileMessageStatus getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.type</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public MessageType getType()
	{
		return getPersistenceContext().getPropertyValue(TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.user</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MobileMessageContext.usingDefaultAction</code> attribute defined at extension <code>mobileservices</code>. 
	 * @return the usingDefaultAction
	 */
	@Accessor(qualifier = "usingDefaultAction", type = Accessor.Type.GETTER)
	public Boolean getUsingDefaultAction()
	{
		return getPersistenceContext().getPropertyValue(USINGDEFAULTACTION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.aggregatorError</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the aggregatorError
	 */
	@Accessor(qualifier = "aggregatorError", type = Accessor.Type.SETTER)
	public void setAggregatorError(final MobileAggregatorError value)
	{
		getPersistenceContext().setPropertyValue(AGGREGATORERROR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.aggregatorErrorDescription</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the aggregatorErrorDescription
	 */
	@Accessor(qualifier = "aggregatorErrorDescription", type = Accessor.Type.SETTER)
	public void setAggregatorErrorDescription(final String value)
	{
		getPersistenceContext().setPropertyValue(AGGREGATORERRORDESCRIPTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.countryIsoCode</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the countryIsoCode
	 */
	@Accessor(qualifier = "countryIsoCode", type = Accessor.Type.SETTER)
	public void setCountryIsoCode(final String value)
	{
		getPersistenceContext().setPropertyValue(COUNTRYISOCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.failures</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the failures
	 */
	@Accessor(qualifier = "failures", type = Accessor.Type.SETTER)
	public void setFailures(final Integer value)
	{
		getPersistenceContext().setPropertyValue(FAILURES, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>MobileMessageContext.incomingEngineId</code> attribute defined at extension <code>mobileservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the incomingEngineId
	 */
	@Accessor(qualifier = "incomingEngineId", type = Accessor.Type.SETTER)
	public void setIncomingEngineId(final String value)
	{
		getPersistenceContext().setPropertyValue(INCOMINGENGINEID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.incomingMessageId</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the incomingMessageId
	 */
	@Accessor(qualifier = "incomingMessageId", type = Accessor.Type.SETTER)
	public void setIncomingMessageId(final String value)
	{
		getPersistenceContext().setPropertyValue(INCOMINGMESSAGEID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.incomingText</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the incomingText
	 */
	@Accessor(qualifier = "incomingText", type = Accessor.Type.SETTER)
	public void setIncomingText(final String value)
	{
		getPersistenceContext().setPropertyValue(INCOMINGTEXT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.isLink</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the isLink
	 */
	@Accessor(qualifier = "isLink", type = Accessor.Type.SETTER)
	public void setIsLink(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ISLINK, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.matchedAction</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the matchedAction
	 */
	@Accessor(qualifier = "matchedAction", type = Accessor.Type.SETTER)
	public void setMatchedAction(final AbstractActionModel value)
	{
		getPersistenceContext().setPropertyValue(MATCHEDACTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.matchedActionAssignment</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the matchedActionAssignment
	 */
	@Accessor(qualifier = "matchedActionAssignment", type = Accessor.Type.SETTER)
	public void setMatchedActionAssignment(final MobileActionAssignmentModel value)
	{
		getPersistenceContext().setPropertyValue(MATCHEDACTIONASSIGNMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.matchedShortcode</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the matchedShortcode
	 */
	@Accessor(qualifier = "matchedShortcode", type = Accessor.Type.SETTER)
	public void setMatchedShortcode(final MobileShortcodeModel value)
	{
		getPersistenceContext().setPropertyValue(MATCHEDSHORTCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.messageError</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the messageError
	 */
	@Accessor(qualifier = "messageError", type = Accessor.Type.SETTER)
	public void setMessageError(final MobileMessageError value)
	{
		getPersistenceContext().setPropertyValue(MESSAGEERROR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.messageErrorDescription</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the messageErrorDescription
	 */
	@Accessor(qualifier = "messageErrorDescription", type = Accessor.Type.SETTER)
	public void setMessageErrorDescription(final String value)
	{
		getPersistenceContext().setPropertyValue(MESSAGEERRORDESCRIPTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.normalizedPhoneNumber</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the normalizedPhoneNumber
	 */
	@Accessor(qualifier = "normalizedPhoneNumber", type = Accessor.Type.SETTER)
	public void setNormalizedPhoneNumber(final String value)
	{
		getPersistenceContext().setPropertyValue(NORMALIZEDPHONENUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.normalizedShortcode</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the normalizedShortcode
	 */
	@Accessor(qualifier = "normalizedShortcode", type = Accessor.Type.SETTER)
	public void setNormalizedShortcode(final String value)
	{
		getPersistenceContext().setPropertyValue(NORMALIZEDSHORTCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.optOperator</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the optOperator
	 */
	@Accessor(qualifier = "optOperator", type = Accessor.Type.SETTER)
	public void setOptOperator(final String value)
	{
		getPersistenceContext().setPropertyValue(OPTOPERATOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.optSessionId</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the optSessionId
	 */
	@Accessor(qualifier = "optSessionId", type = Accessor.Type.SETTER)
	public void setOptSessionId(final String value)
	{
		getPersistenceContext().setPropertyValue(OPTSESSIONID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.optTariff</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the optTariff
	 */
	@Accessor(qualifier = "optTariff", type = Accessor.Type.SETTER)
	public void setOptTariff(final String value)
	{
		getPersistenceContext().setPropertyValue(OPTTARIFF, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.outgoingEngineId</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the outgoingEngineId
	 */
	@Accessor(qualifier = "outgoingEngineId", type = Accessor.Type.SETTER)
	public void setOutgoingEngineId(final String value)
	{
		getPersistenceContext().setPropertyValue(OUTGOINGENGINEID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.outgoingMessageId</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the outgoingMessageId
	 */
	@Accessor(qualifier = "outgoingMessageId", type = Accessor.Type.SETTER)
	public void setOutgoingMessageId(final String value)
	{
		getPersistenceContext().setPropertyValue(OUTGOINGMESSAGEID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.outgoingShortcode</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the outgoingShortcode
	 */
	@Accessor(qualifier = "outgoingShortcode", type = Accessor.Type.SETTER)
	public void setOutgoingShortcode(final MobileShortcodeModel value)
	{
		getPersistenceContext().setPropertyValue(OUTGOINGSHORTCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.outgoingSubject</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the outgoingSubject
	 */
	@Accessor(qualifier = "outgoingSubject", type = Accessor.Type.SETTER)
	public void setOutgoingSubject(final String value)
	{
		getPersistenceContext().setPropertyValue(OUTGOINGSUBJECT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.outgoingText</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the outgoingText
	 */
	@Accessor(qualifier = "outgoingText", type = Accessor.Type.SETTER)
	public void setOutgoingText(final String value)
	{
		getPersistenceContext().setPropertyValue(OUTGOINGTEXT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.phoneCountryIsoCode</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the phoneCountryIsoCode
	 */
	@Accessor(qualifier = "phoneCountryIsoCode", type = Accessor.Type.SETTER)
	public void setPhoneCountryIsoCode(final String value)
	{
		getPersistenceContext().setPropertyValue(PHONECOUNTRYISOCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.phoneNumber</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the phoneNumber
	 */
	@Accessor(qualifier = "phoneNumber", type = Accessor.Type.SETTER)
	public void setPhoneNumber(final String value)
	{
		getPersistenceContext().setPropertyValue(PHONENUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.price</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the price
	 */
	@Accessor(qualifier = "price", type = Accessor.Type.SETTER)
	public void setPrice(final BigDecimal value)
	{
		getPersistenceContext().setPropertyValue(PRICE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.priceCurrency</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the priceCurrency
	 */
	@Accessor(qualifier = "priceCurrency", type = Accessor.Type.SETTER)
	public void setPriceCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(PRICECURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>MobileMessageContext.rawMessageDetails</code> attribute defined at extension <code>mobileservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the rawMessageDetails
	 */
	@Accessor(qualifier = "rawMessageDetails", type = Accessor.Type.SETTER)
	public void setRawMessageDetails(final String value)
	{
		getPersistenceContext().setPropertyValue(RAWMESSAGEDETAILS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.revenue</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the revenue
	 */
	@Accessor(qualifier = "revenue", type = Accessor.Type.SETTER)
	public void setRevenue(final BigDecimal value)
	{
		getPersistenceContext().setPropertyValue(REVENUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.revenueCurrency</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the revenueCurrency
	 */
	@Accessor(qualifier = "revenueCurrency", type = Accessor.Type.SETTER)
	public void setRevenueCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(REVENUECURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.shortcode</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the shortcode
	 */
	@Accessor(qualifier = "shortcode", type = Accessor.Type.SETTER)
	public void setShortcode(final String value)
	{
		getPersistenceContext().setPropertyValue(SHORTCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.status</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final MobileMessageStatus value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.type</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.SETTER)
	public void setType(final MessageType value)
	{
		getPersistenceContext().setPropertyValue(TYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.user</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MobileMessageContext.usingDefaultAction</code> attribute defined at extension <code>mobileservices</code>. 
	 *  
	 * @param value the usingDefaultAction
	 */
	@Accessor(qualifier = "usingDefaultAction", type = Accessor.Type.SETTER)
	public void setUsingDefaultAction(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(USINGDEFAULTACTION, value);
	}
	
}
