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
package de.hybris.platform.promotionengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.math.BigDecimal;

/**
 * Generated model class for type RuleBasedOrderChangeDeliveryModeAction first defined at extension promotionengineservices.
 */
@SuppressWarnings("all")
public class RuleBasedOrderChangeDeliveryModeActionModel extends AbstractRuleBasedPromotionActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RuleBasedOrderChangeDeliveryModeAction";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleBasedOrderChangeDeliveryModeAction.deliveryMode</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String DELIVERYMODE = "deliveryMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleBasedOrderChangeDeliveryModeAction.deliveryCost</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String DELIVERYCOST = "deliveryCost";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleBasedOrderChangeDeliveryModeAction.replacedDeliveryMode</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String REPLACEDDELIVERYMODE = "replacedDeliveryMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>RuleBasedOrderChangeDeliveryModeAction.replacedDeliveryCost</code> attribute defined at extension <code>promotionengineservices</code>. */
	public static final String REPLACEDDELIVERYCOST = "replacedDeliveryCost";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RuleBasedOrderChangeDeliveryModeActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RuleBasedOrderChangeDeliveryModeActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _deliveryCost initial attribute declared by type <code>RuleBasedOrderChangeDeliveryModeAction</code> at extension <code>promotionengineservices</code>
	 * @param _deliveryMode initial attribute declared by type <code>RuleBasedOrderChangeDeliveryModeAction</code> at extension <code>promotionengineservices</code>
	 */
	@Deprecated
	public RuleBasedOrderChangeDeliveryModeActionModel(final BigDecimal _deliveryCost, final DeliveryModeModel _deliveryMode)
	{
		super();
		setDeliveryCost(_deliveryCost);
		setDeliveryMode(_deliveryMode);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _deliveryCost initial attribute declared by type <code>RuleBasedOrderChangeDeliveryModeAction</code> at extension <code>promotionengineservices</code>
	 * @param _deliveryMode initial attribute declared by type <code>RuleBasedOrderChangeDeliveryModeAction</code> at extension <code>promotionengineservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public RuleBasedOrderChangeDeliveryModeActionModel(final BigDecimal _deliveryCost, final DeliveryModeModel _deliveryMode, final ItemModel _owner)
	{
		super();
		setDeliveryCost(_deliveryCost);
		setDeliveryMode(_deliveryMode);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleBasedOrderChangeDeliveryModeAction.deliveryCost</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the deliveryCost - The delivery cost to apply to the order
	 */
	@Accessor(qualifier = "deliveryCost", type = Accessor.Type.GETTER)
	public BigDecimal getDeliveryCost()
	{
		return getPersistenceContext().getPropertyValue(DELIVERYCOST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleBasedOrderChangeDeliveryModeAction.deliveryMode</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the deliveryMode - The delivery mode to apply to the order
	 */
	@Accessor(qualifier = "deliveryMode", type = Accessor.Type.GETTER)
	public DeliveryModeModel getDeliveryMode()
	{
		return getPersistenceContext().getPropertyValue(DELIVERYMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleBasedOrderChangeDeliveryModeAction.replacedDeliveryCost</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the replacedDeliveryCost - The delivery cost that was set to order before a promotion was applied
	 */
	@Accessor(qualifier = "replacedDeliveryCost", type = Accessor.Type.GETTER)
	public BigDecimal getReplacedDeliveryCost()
	{
		return getPersistenceContext().getPropertyValue(REPLACEDDELIVERYCOST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RuleBasedOrderChangeDeliveryModeAction.replacedDeliveryMode</code> attribute defined at extension <code>promotionengineservices</code>. 
	 * @return the replacedDeliveryMode - The delivery mode that was set to order before a promotion was applied
	 */
	@Accessor(qualifier = "replacedDeliveryMode", type = Accessor.Type.GETTER)
	public DeliveryModeModel getReplacedDeliveryMode()
	{
		return getPersistenceContext().getPropertyValue(REPLACEDDELIVERYMODE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleBasedOrderChangeDeliveryModeAction.deliveryCost</code> attribute defined at extension <code>promotionengineservices</code>. 
	 *  
	 * @param value the deliveryCost - The delivery cost to apply to the order
	 */
	@Accessor(qualifier = "deliveryCost", type = Accessor.Type.SETTER)
	public void setDeliveryCost(final BigDecimal value)
	{
		getPersistenceContext().setPropertyValue(DELIVERYCOST, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleBasedOrderChangeDeliveryModeAction.deliveryMode</code> attribute defined at extension <code>promotionengineservices</code>. 
	 *  
	 * @param value the deliveryMode - The delivery mode to apply to the order
	 */
	@Accessor(qualifier = "deliveryMode", type = Accessor.Type.SETTER)
	public void setDeliveryMode(final DeliveryModeModel value)
	{
		getPersistenceContext().setPropertyValue(DELIVERYMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleBasedOrderChangeDeliveryModeAction.replacedDeliveryCost</code> attribute defined at extension <code>promotionengineservices</code>. 
	 *  
	 * @param value the replacedDeliveryCost - The delivery cost that was set to order before a promotion was applied
	 */
	@Accessor(qualifier = "replacedDeliveryCost", type = Accessor.Type.SETTER)
	public void setReplacedDeliveryCost(final BigDecimal value)
	{
		getPersistenceContext().setPropertyValue(REPLACEDDELIVERYCOST, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>RuleBasedOrderChangeDeliveryModeAction.replacedDeliveryMode</code> attribute defined at extension <code>promotionengineservices</code>. 
	 *  
	 * @param value the replacedDeliveryMode - The delivery mode that was set to order before a promotion was applied
	 */
	@Accessor(qualifier = "replacedDeliveryMode", type = Accessor.Type.SETTER)
	public void setReplacedDeliveryMode(final DeliveryModeModel value)
	{
		getPersistenceContext().setPropertyValue(REPLACEDDELIVERYMODE, value);
	}
	
}
