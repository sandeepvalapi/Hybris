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
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SearchBoxComponent first defined at extension acceleratorcms.
 * <p>
 * Represents the search box component.
 */
@SuppressWarnings("all")
public class SearchBoxComponentModel extends SimpleCMSComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SearchBoxComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>SearchBoxComponent.displaySuggestions</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String DISPLAYSUGGESTIONS = "displaySuggestions";
	
	/** <i>Generated constant</i> - Attribute key of <code>SearchBoxComponent.displayProducts</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String DISPLAYPRODUCTS = "displayProducts";
	
	/** <i>Generated constant</i> - Attribute key of <code>SearchBoxComponent.displayProductImages</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String DISPLAYPRODUCTIMAGES = "displayProductImages";
	
	/** <i>Generated constant</i> - Attribute key of <code>SearchBoxComponent.maxSuggestions</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String MAXSUGGESTIONS = "maxSuggestions";
	
	/** <i>Generated constant</i> - Attribute key of <code>SearchBoxComponent.maxProducts</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String MAXPRODUCTS = "maxProducts";
	
	/** <i>Generated constant</i> - Attribute key of <code>SearchBoxComponent.minCharactersBeforeRequest</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String MINCHARACTERSBEFOREREQUEST = "minCharactersBeforeRequest";
	
	/** <i>Generated constant</i> - Attribute key of <code>SearchBoxComponent.waitTimeBeforeRequest</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String WAITTIMEBEFOREREQUEST = "waitTimeBeforeRequest";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SearchBoxComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SearchBoxComponentModel(final ItemModelContext ctx)
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
	public SearchBoxComponentModel(final CatalogVersionModel _catalogVersion, final String _uid)
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
	public SearchBoxComponentModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SearchBoxComponent.maxProducts</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the maxProducts - Determines the max number of products to display in the component.
	 */
	@Accessor(qualifier = "maxProducts", type = Accessor.Type.GETTER)
	public Integer getMaxProducts()
	{
		return getPersistenceContext().getPropertyValue(MAXPRODUCTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SearchBoxComponent.maxSuggestions</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the maxSuggestions - Determines the max number of suggestions to display in the component.
	 */
	@Accessor(qualifier = "maxSuggestions", type = Accessor.Type.GETTER)
	public Integer getMaxSuggestions()
	{
		return getPersistenceContext().getPropertyValue(MAXSUGGESTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SearchBoxComponent.minCharactersBeforeRequest</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the minCharactersBeforeRequest - Determines the min number of characters to enter before submitting a search request.
	 */
	@Accessor(qualifier = "minCharactersBeforeRequest", type = Accessor.Type.GETTER)
	public Integer getMinCharactersBeforeRequest()
	{
		return getPersistenceContext().getPropertyValue(MINCHARACTERSBEFOREREQUEST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SearchBoxComponent.waitTimeBeforeRequest</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the waitTimeBeforeRequest - Determines the wait time after the search term has been altered before resubmitting the search request.
	 */
	@Accessor(qualifier = "waitTimeBeforeRequest", type = Accessor.Type.GETTER)
	public Integer getWaitTimeBeforeRequest()
	{
		return getPersistenceContext().getPropertyValue(WAITTIMEBEFOREREQUEST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SearchBoxComponent.displayProductImages</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the displayProductImages - Determines if product images are shown.
	 */
	@Accessor(qualifier = "displayProductImages", type = Accessor.Type.GETTER)
	public boolean isDisplayProductImages()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(DISPLAYPRODUCTIMAGES));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SearchBoxComponent.displayProducts</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the displayProducts - Determines if product results are displayed in the component.
	 */
	@Accessor(qualifier = "displayProducts", type = Accessor.Type.GETTER)
	public boolean isDisplayProducts()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(DISPLAYPRODUCTS));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SearchBoxComponent.displaySuggestions</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the displaySuggestions - Determines if suggested terms are displayed in the component.
	 */
	@Accessor(qualifier = "displaySuggestions", type = Accessor.Type.GETTER)
	public boolean isDisplaySuggestions()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(DISPLAYSUGGESTIONS));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SearchBoxComponent.displayProductImages</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the displayProductImages - Determines if product images are shown.
	 */
	@Accessor(qualifier = "displayProductImages", type = Accessor.Type.SETTER)
	public void setDisplayProductImages(final boolean value)
	{
		getPersistenceContext().setPropertyValue(DISPLAYPRODUCTIMAGES, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SearchBoxComponent.displayProducts</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the displayProducts - Determines if product results are displayed in the component.
	 */
	@Accessor(qualifier = "displayProducts", type = Accessor.Type.SETTER)
	public void setDisplayProducts(final boolean value)
	{
		getPersistenceContext().setPropertyValue(DISPLAYPRODUCTS, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SearchBoxComponent.displaySuggestions</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the displaySuggestions - Determines if suggested terms are displayed in the component.
	 */
	@Accessor(qualifier = "displaySuggestions", type = Accessor.Type.SETTER)
	public void setDisplaySuggestions(final boolean value)
	{
		getPersistenceContext().setPropertyValue(DISPLAYSUGGESTIONS, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SearchBoxComponent.maxProducts</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the maxProducts - Determines the max number of products to display in the component.
	 */
	@Accessor(qualifier = "maxProducts", type = Accessor.Type.SETTER)
	public void setMaxProducts(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MAXPRODUCTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SearchBoxComponent.maxSuggestions</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the maxSuggestions - Determines the max number of suggestions to display in the component.
	 */
	@Accessor(qualifier = "maxSuggestions", type = Accessor.Type.SETTER)
	public void setMaxSuggestions(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MAXSUGGESTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SearchBoxComponent.minCharactersBeforeRequest</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the minCharactersBeforeRequest - Determines the min number of characters to enter before submitting a search request.
	 */
	@Accessor(qualifier = "minCharactersBeforeRequest", type = Accessor.Type.SETTER)
	public void setMinCharactersBeforeRequest(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MINCHARACTERSBEFOREREQUEST, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SearchBoxComponent.waitTimeBeforeRequest</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the waitTimeBeforeRequest - Determines the wait time after the search term has been altered before resubmitting the search request.
	 */
	@Accessor(qualifier = "waitTimeBeforeRequest", type = Accessor.Type.SETTER)
	public void setWaitTimeBeforeRequest(final Integer value)
	{
		getPersistenceContext().setPropertyValue(WAITTIMEBEFOREREQUEST, value);
	}
	
}
