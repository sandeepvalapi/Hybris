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
import de.hybris.platform.acceleratorcms.model.components.NavigationComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type FooterNavigationComponent first defined at extension acceleratorcms.
 * <p>
 * CMS component for footer navigation in the accelerator.
 */
@SuppressWarnings("all")
public class FooterNavigationComponentModel extends NavigationComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "FooterNavigationComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>FooterNavigationComponent.showLanguageCurrency</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String SHOWLANGUAGECURRENCY = "showLanguageCurrency";
	
	/** <i>Generated constant</i> - Attribute key of <code>FooterNavigationComponent.notice</code> attribute defined at extension <code>acceleratorcms</code>. */
	public static final String NOTICE = "notice";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public FooterNavigationComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public FooterNavigationComponentModel(final ItemModelContext ctx)
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
	public FooterNavigationComponentModel(final CatalogVersionModel _catalogVersion, final String _uid)
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
	public FooterNavigationComponentModel(final CatalogVersionModel _catalogVersion, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FooterNavigationComponent.notice</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the notice - Intended to store a copyright notice or other text to be displayed in the footer
	 */
	@Accessor(qualifier = "notice", type = Accessor.Type.GETTER)
	public String getNotice()
	{
		return getNotice(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>FooterNavigationComponent.notice</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @param loc the value localization key 
	 * @return the notice - Intended to store a copyright notice or other text to be displayed in the footer
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "notice", type = Accessor.Type.GETTER)
	public String getNotice(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NOTICE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FooterNavigationComponent.showLanguageCurrency</code> attribute defined at extension <code>acceleratorcms</code>. 
	 * @return the showLanguageCurrency - Determines whether or not to show the language currency selection.
	 */
	@Accessor(qualifier = "showLanguageCurrency", type = Accessor.Type.GETTER)
	public boolean isShowLanguageCurrency()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(SHOWLANGUAGECURRENCY));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FooterNavigationComponent.notice</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the notice - Intended to store a copyright notice or other text to be displayed in the footer
	 */
	@Accessor(qualifier = "notice", type = Accessor.Type.SETTER)
	public void setNotice(final String value)
	{
		setNotice(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>FooterNavigationComponent.notice</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the notice - Intended to store a copyright notice or other text to be displayed in the footer
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "notice", type = Accessor.Type.SETTER)
	public void setNotice(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NOTICE, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>FooterNavigationComponent.showLanguageCurrency</code> attribute defined at extension <code>acceleratorcms</code>. 
	 *  
	 * @param value the showLanguageCurrency - Determines whether or not to show the language currency selection.
	 */
	@Accessor(qualifier = "showLanguageCurrency", type = Accessor.Type.SETTER)
	public void setShowLanguageCurrency(final boolean value)
	{
		getPersistenceContext().setPropertyValue(SHOWLANGUAGECURRENCY, toObject(value));
	}
	
}
