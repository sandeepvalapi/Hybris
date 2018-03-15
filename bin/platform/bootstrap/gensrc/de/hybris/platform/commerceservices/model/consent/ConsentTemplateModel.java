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
package de.hybris.platform.commerceservices.model.consent;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type ConsentTemplate first defined at extension commerceservices.
 * <p>
 * A type of consent associated with a particular store.
 */
@SuppressWarnings("all")
public class ConsentTemplateModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ConsentTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConsentTemplate.id</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConsentTemplate.version</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String VERSION = "version";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConsentTemplate.baseSite</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String BASESITE = "baseSite";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConsentTemplate.name</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConsentTemplate.exposed</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String EXPOSED = "exposed";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConsentTemplate.description</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String DESCRIPTION = "description";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ConsentTemplateModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ConsentTemplateModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseSite initial attribute declared by type <code>ConsentTemplate</code> at extension <code>commerceservices</code>
	 * @param _id initial attribute declared by type <code>ConsentTemplate</code> at extension <code>commerceservices</code>
	 * @param _version initial attribute declared by type <code>ConsentTemplate</code> at extension <code>commerceservices</code>
	 */
	@Deprecated
	public ConsentTemplateModel(final BaseSiteModel _baseSite, final String _id, final Integer _version)
	{
		super();
		setBaseSite(_baseSite);
		setId(_id);
		setVersion(_version);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseSite initial attribute declared by type <code>ConsentTemplate</code> at extension <code>commerceservices</code>
	 * @param _id initial attribute declared by type <code>ConsentTemplate</code> at extension <code>commerceservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _version initial attribute declared by type <code>ConsentTemplate</code> at extension <code>commerceservices</code>
	 */
	@Deprecated
	public ConsentTemplateModel(final BaseSiteModel _baseSite, final String _id, final ItemModel _owner, final Integer _version)
	{
		super();
		setBaseSite(_baseSite);
		setId(_id);
		setOwner(_owner);
		setVersion(_version);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.baseSite</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the baseSite - BaseSite that this consent template belongs to
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.GETTER)
	public BaseSiteModel getBaseSite()
	{
		return getPersistenceContext().getPropertyValue(BASESITE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.description</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the description - Consent Template Description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.description</code> attribute defined at extension <code>commerceservices</code>. 
	 * @param loc the value localization key 
	 * @return the description - Consent Template Description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.id</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the id - Consent Template ID
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.name</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the name - Consent Template Name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.name</code> attribute defined at extension <code>commerceservices</code>. 
	 * @param loc the value localization key 
	 * @return the name - Consent Template Name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.version</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the version - Consent Template Version
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.GETTER)
	public Integer getVersion()
	{
		return getPersistenceContext().getPropertyValue(VERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsentTemplate.exposed</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the exposed - Indicates whether the consent template should be exposed to integrators in a storefront implementation as part of cookie and session information
	 */
	@Accessor(qualifier = "exposed", type = Accessor.Type.GETTER)
	public boolean isExposed()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(EXPOSED));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConsentTemplate.baseSite</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the baseSite - BaseSite that this consent template belongs to
	 */
	@Accessor(qualifier = "baseSite", type = Accessor.Type.SETTER)
	public void setBaseSite(final BaseSiteModel value)
	{
		getPersistenceContext().setPropertyValue(BASESITE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConsentTemplate.description</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the description - Consent Template Description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ConsentTemplate.description</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the description - Consent Template Description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConsentTemplate.exposed</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the exposed - Indicates whether the consent template should be exposed to integrators in a storefront implementation as part of cookie and session information
	 */
	@Accessor(qualifier = "exposed", type = Accessor.Type.SETTER)
	public void setExposed(final boolean value)
	{
		getPersistenceContext().setPropertyValue(EXPOSED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConsentTemplate.id</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the id - Consent Template ID
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConsentTemplate.name</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the name - Consent Template Name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ConsentTemplate.name</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the name - Consent Template Name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConsentTemplate.version</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the version - Consent Template Version
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.SETTER)
	public void setVersion(final Integer value)
	{
		getPersistenceContext().setPropertyValue(VERSION, value);
	}
	
}
