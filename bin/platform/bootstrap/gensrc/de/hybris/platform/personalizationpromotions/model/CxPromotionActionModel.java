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
package de.hybris.platform.personalizationpromotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CxPromotionAction first defined at extension personalizationpromotions.
 */
@SuppressWarnings("all")
public class CxPromotionActionModel extends CxAbstractActionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CxPromotionAction";
	
	/** <i>Generated constant</i> - Attribute key of <code>CxPromotionAction.promotionId</code> attribute defined at extension <code>personalizationpromotions</code>. */
	public static final String PROMOTIONID = "promotionId";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CxPromotionActionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CxPromotionActionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxAbstractAction</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 * @param _promotionId initial attribute declared by type <code>CxPromotionAction</code> at extension <code>personalizationpromotions</code>
	 * @param _target initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 * @param _type initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CxPromotionActionModel(final CatalogVersionModel _catalogVersion, final String _code, final String _promotionId, final String _target, final ActionType _type)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setPromotionId(_promotionId);
		setTarget(_target);
		setType(_type);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CxAbstractAction</code> at extension <code>personalizationservices</code>
	 * @param _code initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _promotionId initial attribute declared by type <code>CxPromotionAction</code> at extension <code>personalizationpromotions</code>
	 * @param _target initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 * @param _type initial attribute declared by type <code>AbstractAction</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CxPromotionActionModel(final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner, final String _promotionId, final String _target, final ActionType _type)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
		setPromotionId(_promotionId);
		setTarget(_target);
		setType(_type);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CxPromotionAction.promotionId</code> attribute defined at extension <code>personalizationpromotions</code>. 
	 * @return the promotionId - Target promotion
	 */
	@Accessor(qualifier = "promotionId", type = Accessor.Type.GETTER)
	public String getPromotionId()
	{
		return getPersistenceContext().getPropertyValue(PROMOTIONID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CxPromotionAction.promotionId</code> attribute defined at extension <code>personalizationpromotions</code>. 
	 *  
	 * @param value the promotionId - Target promotion
	 */
	@Accessor(qualifier = "promotionId", type = Accessor.Type.SETTER)
	public void setPromotionId(final String value)
	{
		getPersistenceContext().setPropertyValue(PROMOTIONID, value);
	}
	
}
