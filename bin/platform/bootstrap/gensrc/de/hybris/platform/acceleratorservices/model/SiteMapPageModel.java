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
package de.hybris.platform.acceleratorservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorservices.enums.SiteMapChangeFrequencyEnum;
import de.hybris.platform.acceleratorservices.enums.SiteMapPageEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SiteMapPage first defined at extension acceleratorservices.
 * <p>
 * Creating a Site Map type to group attributes per page type.
 */
@SuppressWarnings("all")
public class SiteMapPageModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SiteMapPage";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMapPage.code</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMapPage.frequency</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String FREQUENCY = "frequency";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMapPage.priority</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String PRIORITY = "priority";
	
	/** <i>Generated constant</i> - Attribute key of <code>SiteMapPage.active</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String ACTIVE = "active";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SiteMapPageModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SiteMapPageModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public SiteMapPageModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMapPage.active</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the active - Is sitemap page enabled or disabled
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public Boolean getActive()
	{
		return getPersistenceContext().getPropertyValue(ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMapPage.code</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the code - The type of page to be qualified
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public SiteMapPageEnum getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMapPage.frequency</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the frequency - How often the page should be updated.
	 */
	@Accessor(qualifier = "frequency", type = Accessor.Type.GETTER)
	public SiteMapChangeFrequencyEnum getFrequency()
	{
		return getPersistenceContext().getPropertyValue(FREQUENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SiteMapPage.priority</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the priority - The weight the page should have in the sitemap
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public Double getPriority()
	{
		return getPersistenceContext().getPropertyValue(PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMapPage.active</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the active - Is sitemap page enabled or disabled
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMapPage.code</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the code - The type of page to be qualified
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final SiteMapPageEnum value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMapPage.frequency</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the frequency - How often the page should be updated.
	 */
	@Accessor(qualifier = "frequency", type = Accessor.Type.SETTER)
	public void setFrequency(final SiteMapChangeFrequencyEnum value)
	{
		getPersistenceContext().setPropertyValue(FREQUENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SiteMapPage.priority</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the priority - The weight the page should have in the sitemap
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Double value)
	{
		getPersistenceContext().setPropertyValue(PRIORITY, value);
	}
	
}
