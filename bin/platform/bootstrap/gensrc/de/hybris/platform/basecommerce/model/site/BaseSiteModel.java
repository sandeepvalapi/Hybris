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
package de.hybris.platform.basecommerce.model.site;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorservices.model.CartRemovalCronJobModel;
import de.hybris.platform.acceleratorservices.model.UncollectedOrdersCronJobModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.enums.SiteTheme;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.personalizationservices.model.config.CxConfigModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.yaasconfiguration.model.YaasProjectModel;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type BaseSite first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class BaseSiteModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "BaseSite";
	
	/** <i>Generated constant</i> - Attribute key of <code>BaseSite.uid</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String UID = "uid";
	
	/** <i>Generated constant</i> - Attribute key of <code>BaseSite.name</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>BaseSite.stores</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String STORES = "stores";
	
	/** <i>Generated constant</i> - Attribute key of <code>BaseSite.yaasProjects</code> attribute defined at extension <code>yaasconfiguration</code>. */
	public static final String YAASPROJECTS = "yaasProjects";
	
	/** <i>Generated constant</i> - Attribute key of <code>BaseSite.theme</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String THEME = "theme";
	
	/** <i>Generated constant</i> - Attribute key of <code>BaseSite.defaultLanguage</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String DEFAULTLANGUAGE = "defaultLanguage";
	
	/** <i>Generated constant</i> - Attribute key of <code>BaseSite.locale</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String LOCALE = "locale";
	
	/** <i>Generated constant</i> - Attribute key of <code>BaseSite.channel</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String CHANNEL = "channel";
	
	/** <i>Generated constant</i> - Attribute key of <code>BaseSite.defaultPromotionGroup</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String DEFAULTPROMOTIONGROUP = "defaultPromotionGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>BaseSite.solrFacetSearchConfiguration</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String SOLRFACETSEARCHCONFIGURATION = "solrFacetSearchConfiguration";
	
	/** <i>Generated constant</i> - Attribute key of <code>BaseSite.cxConfig</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String CXCONFIG = "cxConfig";
	
	/** <i>Generated constant</i> - Attribute key of <code>BaseSite.cartRemovalAge</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String CARTREMOVALAGE = "cartRemovalAge";
	
	/** <i>Generated constant</i> - Attribute key of <code>BaseSite.anonymousCartRemovalAge</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String ANONYMOUSCARTREMOVALAGE = "anonymousCartRemovalAge";
	
	/** <i>Generated constant</i> - Attribute key of <code>BaseSite.cartRemovalCronJob</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String CARTREMOVALCRONJOB = "cartRemovalCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>BaseSite.uncollectedOrdersCronJob</code> attribute defined at extension <code>acceleratorservices</code>. */
	public static final String UNCOLLECTEDORDERSCRONJOB = "uncollectedOrdersCronJob";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public BaseSiteModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public BaseSiteModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _uid initial attribute declared by type <code>BaseSite</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public BaseSiteModel(final String _uid)
	{
		super();
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>BaseSite</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public BaseSiteModel(final ItemModel _owner, final String _uid)
	{
		super();
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.anonymousCartRemovalAge</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the anonymousCartRemovalAge - After specified number of seconds carts will be cleaned up. Default is 14 days.
	 */
	@Accessor(qualifier = "anonymousCartRemovalAge", type = Accessor.Type.GETTER)
	public Integer getAnonymousCartRemovalAge()
	{
		return getPersistenceContext().getPropertyValue(ANONYMOUSCARTREMOVALAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.cartRemovalAge</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the cartRemovalAge - After specified number of seconds carts will be cleaned up. Default is 28 days.
	 */
	@Accessor(qualifier = "cartRemovalAge", type = Accessor.Type.GETTER)
	public Integer getCartRemovalAge()
	{
		return getPersistenceContext().getPropertyValue(CARTREMOVALAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.cartRemovalCronJob</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the cartRemovalCronJob
	 */
	@Accessor(qualifier = "cartRemovalCronJob", type = Accessor.Type.GETTER)
	public CartRemovalCronJobModel getCartRemovalCronJob()
	{
		return getPersistenceContext().getPropertyValue(CARTREMOVALCRONJOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.channel</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the channel - The channel for this site.
	 */
	@Accessor(qualifier = "channel", type = Accessor.Type.GETTER)
	public SiteChannel getChannel()
	{
		return getPersistenceContext().getPropertyValue(CHANNEL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.cxConfig</code> attribute defined at extension <code>personalizationservices</code>. 
	 * @return the cxConfig
	 */
	@Accessor(qualifier = "cxConfig", type = Accessor.Type.GETTER)
	public CxConfigModel getCxConfig()
	{
		return getPersistenceContext().getPropertyValue(CXCONFIG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.defaultLanguage</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the defaultLanguage - The default language for the site.
	 */
	@Accessor(qualifier = "defaultLanguage", type = Accessor.Type.GETTER)
	public LanguageModel getDefaultLanguage()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTLANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.defaultPromotionGroup</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the defaultPromotionGroup - The default promotion group for the site.
	 */
	@Accessor(qualifier = "defaultPromotionGroup", type = Accessor.Type.GETTER)
	public PromotionGroupModel getDefaultPromotionGroup()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTPROMOTIONGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.locale</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the locale - The locale to use for each language.
	 */
	@Accessor(qualifier = "locale", type = Accessor.Type.GETTER)
	public String getLocale()
	{
		return getLocale(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.locale</code> attribute defined at extension <code>commerceservices</code>. 
	 * @param loc the value localization key 
	 * @return the locale - The locale to use for each language.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "locale", type = Accessor.Type.GETTER)
	public String getLocale(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(LOCALE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.name</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.name</code> attribute defined at extension <code>basecommerce</code>. 
	 * @param loc the value localization key 
	 * @return the name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.solrFacetSearchConfiguration</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the solrFacetSearchConfiguration - Solr search configuration for this site.
	 */
	@Accessor(qualifier = "solrFacetSearchConfiguration", type = Accessor.Type.GETTER)
	public SolrFacetSearchConfigModel getSolrFacetSearchConfiguration()
	{
		return getPersistenceContext().getPropertyValue(SOLRFACETSEARCHCONFIGURATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.stores</code> attribute defined at extension <code>basecommerce</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the stores
	 */
	@Accessor(qualifier = "stores", type = Accessor.Type.GETTER)
	public List<BaseStoreModel> getStores()
	{
		return getPersistenceContext().getPropertyValue(STORES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.theme</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the theme - The site theme that is used in this site.
	 */
	@Accessor(qualifier = "theme", type = Accessor.Type.GETTER)
	public SiteTheme getTheme()
	{
		return getPersistenceContext().getPropertyValue(THEME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.uid</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the uid
	 */
	@Accessor(qualifier = "uid", type = Accessor.Type.GETTER)
	public String getUid()
	{
		return getPersistenceContext().getPropertyValue(UID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.uncollectedOrdersCronJob</code> attribute defined at extension <code>acceleratorservices</code>. 
	 * @return the uncollectedOrdersCronJob
	 */
	@Accessor(qualifier = "uncollectedOrdersCronJob", type = Accessor.Type.GETTER)
	public UncollectedOrdersCronJobModel getUncollectedOrdersCronJob()
	{
		return getPersistenceContext().getPropertyValue(UNCOLLECTEDORDERSCRONJOB);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseSite.yaasProjects</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the yaasProjects
	 */
	@Accessor(qualifier = "yaasProjects", type = Accessor.Type.GETTER)
	public Set<YaasProjectModel> getYaasProjects()
	{
		return getPersistenceContext().getPropertyValue(YAASPROJECTS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseSite.anonymousCartRemovalAge</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the anonymousCartRemovalAge - After specified number of seconds carts will be cleaned up. Default is 14 days.
	 */
	@Accessor(qualifier = "anonymousCartRemovalAge", type = Accessor.Type.SETTER)
	public void setAnonymousCartRemovalAge(final Integer value)
	{
		getPersistenceContext().setPropertyValue(ANONYMOUSCARTREMOVALAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseSite.cartRemovalAge</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the cartRemovalAge - After specified number of seconds carts will be cleaned up. Default is 28 days.
	 */
	@Accessor(qualifier = "cartRemovalAge", type = Accessor.Type.SETTER)
	public void setCartRemovalAge(final Integer value)
	{
		getPersistenceContext().setPropertyValue(CARTREMOVALAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseSite.cartRemovalCronJob</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the cartRemovalCronJob
	 */
	@Accessor(qualifier = "cartRemovalCronJob", type = Accessor.Type.SETTER)
	public void setCartRemovalCronJob(final CartRemovalCronJobModel value)
	{
		getPersistenceContext().setPropertyValue(CARTREMOVALCRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseSite.channel</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the channel - The channel for this site.
	 */
	@Accessor(qualifier = "channel", type = Accessor.Type.SETTER)
	public void setChannel(final SiteChannel value)
	{
		getPersistenceContext().setPropertyValue(CHANNEL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseSite.cxConfig</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the cxConfig
	 */
	@Accessor(qualifier = "cxConfig", type = Accessor.Type.SETTER)
	public void setCxConfig(final CxConfigModel value)
	{
		getPersistenceContext().setPropertyValue(CXCONFIG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseSite.defaultLanguage</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the defaultLanguage - The default language for the site.
	 */
	@Accessor(qualifier = "defaultLanguage", type = Accessor.Type.SETTER)
	public void setDefaultLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTLANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseSite.defaultPromotionGroup</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the defaultPromotionGroup - The default promotion group for the site.
	 */
	@Accessor(qualifier = "defaultPromotionGroup", type = Accessor.Type.SETTER)
	public void setDefaultPromotionGroup(final PromotionGroupModel value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTPROMOTIONGROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseSite.locale</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the locale - The locale to use for each language.
	 */
	@Accessor(qualifier = "locale", type = Accessor.Type.SETTER)
	public void setLocale(final String value)
	{
		setLocale(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>BaseSite.locale</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the locale - The locale to use for each language.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "locale", type = Accessor.Type.SETTER)
	public void setLocale(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(LOCALE, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseSite.name</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>BaseSite.name</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseSite.solrFacetSearchConfiguration</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the solrFacetSearchConfiguration - Solr search configuration for this site.
	 */
	@Accessor(qualifier = "solrFacetSearchConfiguration", type = Accessor.Type.SETTER)
	public void setSolrFacetSearchConfiguration(final SolrFacetSearchConfigModel value)
	{
		getPersistenceContext().setPropertyValue(SOLRFACETSEARCHCONFIGURATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseSite.stores</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the stores
	 */
	@Accessor(qualifier = "stores", type = Accessor.Type.SETTER)
	public void setStores(final List<BaseStoreModel> value)
	{
		getPersistenceContext().setPropertyValue(STORES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseSite.theme</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the theme - The site theme that is used in this site.
	 */
	@Accessor(qualifier = "theme", type = Accessor.Type.SETTER)
	public void setTheme(final SiteTheme value)
	{
		getPersistenceContext().setPropertyValue(THEME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseSite.uid</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the uid
	 */
	@Accessor(qualifier = "uid", type = Accessor.Type.SETTER)
	public void setUid(final String value)
	{
		getPersistenceContext().setPropertyValue(UID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseSite.uncollectedOrdersCronJob</code> attribute defined at extension <code>acceleratorservices</code>. 
	 *  
	 * @param value the uncollectedOrdersCronJob
	 */
	@Accessor(qualifier = "uncollectedOrdersCronJob", type = Accessor.Type.SETTER)
	public void setUncollectedOrdersCronJob(final UncollectedOrdersCronJobModel value)
	{
		getPersistenceContext().setPropertyValue(UNCOLLECTEDORDERSCRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseSite.yaasProjects</code> attribute defined at extension <code>yaasconfiguration</code>. 
	 *  
	 * @param value the yaasProjects
	 */
	@Accessor(qualifier = "yaasProjects", type = Accessor.Type.SETTER)
	public void setYaasProjects(final Set<YaasProjectModel> value)
	{
		getPersistenceContext().setPropertyValue(YAASPROJECTS, value);
	}
	
}
