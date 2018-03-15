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
package de.hybris.platform.core.model.order;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commerceservices.enums.QuoteNotificationType;
import de.hybris.platform.core.enums.QuoteState;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.Set;

/**
 * Generated model class for type Quote first defined at extension core.
 */
@SuppressWarnings("all")
public class QuoteModel extends AbstractOrderModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Quote";
	
	/**<i>Generated relation code constant for relation <code>Assignee2Quotes</code> defining source attribute <code>assignee</code> in extension <code>commerceservices</code>.</i>*/
	public static final String _ASSIGNEE2QUOTES = "Assignee2Quotes";
	
	/** <i>Generated constant</i> - Attribute key of <code>Quote.version</code> attribute defined at extension <code>core</code>. */
	public static final String VERSION = "version";
	
	/** <i>Generated constant</i> - Attribute key of <code>Quote.state</code> attribute defined at extension <code>core</code>. */
	public static final String STATE = "state";
	
	/** <i>Generated constant</i> - Attribute key of <code>Quote.cartReference</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String CARTREFERENCE = "cartReference";
	
	/** <i>Generated constant</i> - Attribute key of <code>Quote.previousEstimatedTotal</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String PREVIOUSESTIMATEDTOTAL = "previousEstimatedTotal";
	
	/** <i>Generated constant</i> - Attribute key of <code>Quote.assignee</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String ASSIGNEE = "assignee";
	
	/** <i>Generated constant</i> - Attribute key of <code>Quote.generatedNotifications</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String GENERATEDNOTIFICATIONS = "generatedNotifications";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public QuoteModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public QuoteModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _currency initial attribute declared by type <code>AbstractOrder</code> at extension <code>core</code>
	 * @param _date initial attribute declared by type <code>AbstractOrder</code> at extension <code>core</code>
	 * @param _state initial attribute declared by type <code>Quote</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>Quote</code> at extension <code>core</code>
	 * @param _version initial attribute declared by type <code>Quote</code> at extension <code>core</code>
	 */
	@Deprecated
	public QuoteModel(final CurrencyModel _currency, final Date _date, final QuoteState _state, final UserModel _user, final Integer _version)
	{
		super();
		setCurrency(_currency);
		setDate(_date);
		setState(_state);
		setUser(_user);
		setVersion(_version);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _currency initial attribute declared by type <code>AbstractOrder</code> at extension <code>core</code>
	 * @param _date initial attribute declared by type <code>AbstractOrder</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _state initial attribute declared by type <code>Quote</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>Quote</code> at extension <code>core</code>
	 * @param _version initial attribute declared by type <code>Quote</code> at extension <code>core</code>
	 */
	@Deprecated
	public QuoteModel(final CurrencyModel _currency, final Date _date, final ItemModel _owner, final QuoteState _state, final UserModel _user, final Integer _version)
	{
		super();
		setCurrency(_currency);
		setDate(_date);
		setOwner(_owner);
		setState(_state);
		setUser(_user);
		setVersion(_version);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Quote.assignee</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the assignee
	 */
	@Accessor(qualifier = "assignee", type = Accessor.Type.GETTER)
	public UserModel getAssignee()
	{
		return getPersistenceContext().getPropertyValue(ASSIGNEE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Quote.cartReference</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the cartReference - The reference to cart used to manipulate the quote.
	 */
	@Accessor(qualifier = "cartReference", type = Accessor.Type.GETTER)
	public CartModel getCartReference()
	{
		return getPersistenceContext().getPropertyValue(CARTREFERENCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Quote.generatedNotifications</code> attribute defined at extension <code>commerceservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the generatedNotifications
	 */
	@Accessor(qualifier = "generatedNotifications", type = Accessor.Type.GETTER)
	public Set<QuoteNotificationType> getGeneratedNotifications()
	{
		return getPersistenceContext().getPropertyValue(GENERATEDNOTIFICATIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Quote.previousEstimatedTotal</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the previousEstimatedTotal - Previously estimated total
	 */
	@Accessor(qualifier = "previousEstimatedTotal", type = Accessor.Type.GETTER)
	public Double getPreviousEstimatedTotal()
	{
		return getPersistenceContext().getPropertyValue(PREVIOUSESTIMATEDTOTAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Quote.state</code> attribute defined at extension <code>core</code>. 
	 * @return the state - Current state of the quote
	 */
	@Accessor(qualifier = "state", type = Accessor.Type.GETTER)
	public QuoteState getState()
	{
		return getPersistenceContext().getPropertyValue(STATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Quote.version</code> attribute defined at extension <code>core</code>. 
	 * @return the version - The version of the quote. Along with code it makes a quote instance unique.
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.GETTER)
	public Integer getVersion()
	{
		return getPersistenceContext().getPropertyValue(VERSION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Quote.assignee</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the assignee
	 */
	@Accessor(qualifier = "assignee", type = Accessor.Type.SETTER)
	public void setAssignee(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(ASSIGNEE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Quote.cartReference</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the cartReference - The reference to cart used to manipulate the quote.
	 */
	@Accessor(qualifier = "cartReference", type = Accessor.Type.SETTER)
	public void setCartReference(final CartModel value)
	{
		getPersistenceContext().setPropertyValue(CARTREFERENCE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Quote.generatedNotifications</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the generatedNotifications
	 */
	@Accessor(qualifier = "generatedNotifications", type = Accessor.Type.SETTER)
	public void setGeneratedNotifications(final Set<QuoteNotificationType> value)
	{
		getPersistenceContext().setPropertyValue(GENERATEDNOTIFICATIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Quote.previousEstimatedTotal</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the previousEstimatedTotal - Previously estimated total
	 */
	@Accessor(qualifier = "previousEstimatedTotal", type = Accessor.Type.SETTER)
	public void setPreviousEstimatedTotal(final Double value)
	{
		getPersistenceContext().setPropertyValue(PREVIOUSESTIMATEDTOTAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Quote.state</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the state - Current state of the quote
	 */
	@Accessor(qualifier = "state", type = Accessor.Type.SETTER)
	public void setState(final QuoteState value)
	{
		getPersistenceContext().setPropertyValue(STATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Quote.version</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the version - The version of the quote. Along with code it makes a quote instance unique.
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.SETTER)
	public void setVersion(final Integer value)
	{
		getPersistenceContext().setPropertyValue(VERSION, value);
	}
	
}
