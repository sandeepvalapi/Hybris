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
package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AbstractPromotionAction first defined at extension promotions.
 */
@SuppressWarnings("all")
public class AbstractPromotionActionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractPromotionAction";
	
	/**<i>Generated relation code constant for relation <code>PromotionResult2PromotionActionsRelation</code> defining source attribute <code>promotionResult</code> in extension <code>promotions</code>.</i>*/
	public static final String _PROMOTIONRESULT2PROMOTIONACTIONSRELATION = "PromotionResult2PromotionActionsRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotionAction.markedApplied</code> attribute defined at extension <code>promotions</code>. */
	public static final String MARKEDAPPLIED = "markedApplied";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotionAction.guid</code> attribute defined at extension <code>promotions</code>. */
	public static final String GUID = "guid";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPromotionAction.promotionResult</code> attribute defined at extension <code>promotions</code>. */
	public static final String PROMOTIONRESULT = "promotionResult";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractPromotionActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractPromotionActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractPromotionActionModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotionAction.guid</code> attribute defined at extension <code>promotions</code>. 
	 * @return the guid - The unique identifier for this action.
	 */
	@Accessor(qualifier = "guid", type = Accessor.Type.GETTER)
	public String getGuid()
	{
		return getPersistenceContext().getPropertyValue(GUID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotionAction.markedApplied</code> attribute defined at extension <code>promotions</code>. 
	 * @return the markedApplied - Flag to indicate that this promotion is applied.
	 */
	@Accessor(qualifier = "markedApplied", type = Accessor.Type.GETTER)
	public Boolean getMarkedApplied()
	{
		return getPersistenceContext().getPropertyValue(MARKEDAPPLIED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPromotionAction.promotionResult</code> attribute defined at extension <code>promotions</code>. 
	 * @return the promotionResult
	 */
	@Accessor(qualifier = "promotionResult", type = Accessor.Type.GETTER)
	public PromotionResultModel getPromotionResult()
	{
		return getPersistenceContext().getPropertyValue(PROMOTIONRESULT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotionAction.guid</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the guid - The unique identifier for this action.
	 */
	@Accessor(qualifier = "guid", type = Accessor.Type.SETTER)
	public void setGuid(final String value)
	{
		getPersistenceContext().setPropertyValue(GUID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotionAction.markedApplied</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the markedApplied - Flag to indicate that this promotion is applied.
	 */
	@Accessor(qualifier = "markedApplied", type = Accessor.Type.SETTER)
	public void setMarkedApplied(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(MARKEDAPPLIED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPromotionAction.promotionResult</code> attribute defined at extension <code>promotions</code>. 
	 *  
	 * @param value the promotionResult
	 */
	@Accessor(qualifier = "promotionResult", type = Accessor.Type.SETTER)
	public void setPromotionResult(final PromotionResultModel value)
	{
		getPersistenceContext().setPropertyValue(PROMOTIONRESULT, value);
	}
	
}
