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
package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorcms.model.components.ProductReferencesComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SimpleSuggestionComponent first defined at extension acceleratorcms.
 * <p>
 * Represents the suggestion component that displays suggestions to the user.
 */
@SuppressWarnings("all")
public class SimpleSuggestionComponentModel extends ProductReferencesComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SimpleSuggestionComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>SimpleSuggestionComponent.filterPurchased</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String FILTERPURCHASED = "filterPurchased";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SimpleSuggestionComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SimpleSuggestionComponentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public SimpleSuggestionComponentModel(final CatalogVersionModel _catalogVersion, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public SimpleSuggestionComponentModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SimpleSuggestionComponent.filterPurchased</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the filterPurchased - Determines if only purchased products are displayed in the component.
	 */
	@Accessor(qualifier = "filterPurchased", type = Accessor.Type.GETTER)
	public boolean isFilterPurchased()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(FILTERPURCHASED));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SimpleSuggestionComponent.filterPurchased</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the filterPurchased - Determines if only purchased products are displayed in the component.
	 */
	@Accessor(qualifier = "filterPurchased", type = Accessor.Type.SETTER)
	public void setFilterPurchased(final boolean value)
	{
		getPersistenceContext().setPropertyValue(FILTERPURCHASED, toObject(value));
	}
	
}
