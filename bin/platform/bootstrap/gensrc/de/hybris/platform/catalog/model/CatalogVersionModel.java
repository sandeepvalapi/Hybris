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
package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.AgreementModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.personalizationservices.model.process.CxPersonalizationProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type CatalogVersion first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CatalogVersionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CatalogVersion";
	
	/**<i>Generated relation code constant for relation <code>Catalog2VersionsRelation</code> defining source attribute <code>catalog</code> in extension <code>catalog</code>.</i>*/
	public static final String _CATALOG2VERSIONSRELATION = "Catalog2VersionsRelation";
	
	/**<i>Generated relation code constant for relation <code>Principal2WriteableCatalogVersionRelation</code> defining source attribute <code>writePrincipals</code> in extension <code>catalog</code>.</i>*/
	public static final String _PRINCIPAL2WRITEABLECATALOGVERSIONRELATION = "Principal2WriteableCatalogVersionRelation";
	
	/**<i>Generated relation code constant for relation <code>Principal2ReadableCatalogVersionRelation</code> defining source attribute <code>readPrincipals</code> in extension <code>catalog</code>.</i>*/
	public static final String _PRINCIPAL2READABLECATALOGVERSIONRELATION = "Principal2ReadableCatalogVersionRelation";
	
	/**<i>Generated relation code constant for relation <code>SolrFacetSearchConfig2CatalogVersionRelation</code> defining source attribute <code>facetSearchConfigs</code> in extension <code>solrfacetsearch</code>.</i>*/
	public static final String _SOLRFACETSEARCHCONFIG2CATALOGVERSIONRELATION = "SolrFacetSearchConfig2CatalogVersionRelation";
	
	/**<i>Generated relation code constant for relation <code>PreviewDataToCatalogVersion</code> defining source attribute <code>previews</code> in extension <code>cms2</code>.</i>*/
	public static final String _PREVIEWDATATOCATALOGVERSION = "PreviewDataToCatalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.active</code> attribute defined at extension <code>catalog</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.version</code> attribute defined at extension <code>catalog</code>. */
	public static final String VERSION = "version";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.mimeRootDirectory</code> attribute defined at extension <code>catalog</code>. */
	public static final String MIMEROOTDIRECTORY = "mimeRootDirectory";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.generationDate</code> attribute defined at extension <code>catalog</code>. */
	public static final String GENERATIONDATE = "generationDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.defaultCurrency</code> attribute defined at extension <code>catalog</code>. */
	public static final String DEFAULTCURRENCY = "defaultCurrency";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.inclFreight</code> attribute defined at extension <code>catalog</code>. */
	public static final String INCLFREIGHT = "inclFreight";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.inclPacking</code> attribute defined at extension <code>catalog</code>. */
	public static final String INCLPACKING = "inclPacking";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.inclAssurance</code> attribute defined at extension <code>catalog</code>. */
	public static final String INCLASSURANCE = "inclAssurance";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.inclDuty</code> attribute defined at extension <code>catalog</code>. */
	public static final String INCLDUTY = "inclDuty";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.territories</code> attribute defined at extension <code>catalog</code>. */
	public static final String TERRITORIES = "territories";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.languages</code> attribute defined at extension <code>catalog</code>. */
	public static final String LANGUAGES = "languages";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.generatorInfo</code> attribute defined at extension <code>catalog</code>. */
	public static final String GENERATORINFO = "generatorInfo";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.categorySystemID</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATEGORYSYSTEMID = "categorySystemID";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.categorySystemName</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATEGORYSYSTEMNAME = "categorySystemName";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.categorySystemDescription</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATEGORYSYSTEMDESCRIPTION = "categorySystemDescription";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.rootCategories</code> attribute defined at extension <code>catalog</code>. */
	public static final String ROOTCATEGORIES = "rootCategories";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.previousUpdateVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String PREVIOUSUPDATEVERSION = "previousUpdateVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.catalog</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOG = "catalog";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.synchronizations</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYNCHRONIZATIONS = "synchronizations";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.incomingSynchronizations</code> attribute defined at extension <code>catalog</code>. */
	public static final String INCOMINGSYNCHRONIZATIONS = "incomingSynchronizations";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.agreements</code> attribute defined at extension <code>catalog</code>. */
	public static final String AGREEMENTS = "agreements";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.writePrincipals</code> attribute defined at extension <code>catalog</code>. */
	public static final String WRITEPRINCIPALS = "writePrincipals";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.readPrincipals</code> attribute defined at extension <code>catalog</code>. */
	public static final String READPRINCIPALS = "readPrincipals";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.mnemonic</code> attribute defined at extension <code>cockpit</code>. */
	public static final String MNEMONIC = "mnemonic";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.facetSearchConfigs</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FACETSEARCHCONFIGS = "facetSearchConfigs";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.cxPersonalizationProcesses</code> attribute defined at extension <code>personalizationservices</code>. */
	public static final String CXPERSONALIZATIONPROCESSES = "cxPersonalizationProcesses";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.previews</code> attribute defined at extension <code>cms2</code>. */
	public static final String PREVIEWS = "previews";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CatalogVersionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CatalogVersionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalog initial attribute declared by type <code>CatalogVersion</code> at extension <code>catalog</code>
	 * @param _version initial attribute declared by type <code>CatalogVersion</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CatalogVersionModel(final CatalogModel _catalog, final String _version)
	{
		super();
		setCatalog(_catalog);
		setVersion(_version);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalog initial attribute declared by type <code>CatalogVersion</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _version initial attribute declared by type <code>CatalogVersion</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CatalogVersionModel(final CatalogModel _catalog, final ItemModel _owner, final String _version)
	{
		super();
		setCatalog(_catalog);
		setOwner(_owner);
		setVersion(_version);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.active</code> attribute defined at extension <code>catalog</code>. 
	 * @return the active - active flag
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public Boolean getActive()
	{
		return getPersistenceContext().getPropertyValue(ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.agreements</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the agreements
	 */
	@Accessor(qualifier = "agreements", type = Accessor.Type.GETTER)
	public List<AgreementModel> getAgreements()
	{
		return getPersistenceContext().getPropertyValue(AGREEMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.catalog</code> attribute defined at extension <code>catalog</code>. 
	 * @return the catalog
	 */
	@Accessor(qualifier = "catalog", type = Accessor.Type.GETTER)
	public CatalogModel getCatalog()
	{
		return getPersistenceContext().getPropertyValue(CATALOG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.categorySystemDescription</code> attribute defined at extension <code>catalog</code>. 
	 * @return the categorySystemDescription
	 */
	@Accessor(qualifier = "categorySystemDescription", type = Accessor.Type.GETTER)
	public String getCategorySystemDescription()
	{
		return getCategorySystemDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.categorySystemDescription</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the categorySystemDescription
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "categorySystemDescription", type = Accessor.Type.GETTER)
	public String getCategorySystemDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(CATEGORYSYSTEMDESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.categorySystemID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the categorySystemID
	 */
	@Accessor(qualifier = "categorySystemID", type = Accessor.Type.GETTER)
	public String getCategorySystemID()
	{
		return getPersistenceContext().getPropertyValue(CATEGORYSYSTEMID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.categorySystemName</code> attribute defined at extension <code>catalog</code>. 
	 * @return the categorySystemName
	 */
	@Accessor(qualifier = "categorySystemName", type = Accessor.Type.GETTER)
	public String getCategorySystemName()
	{
		return getCategorySystemName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.categorySystemName</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the categorySystemName
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "categorySystemName", type = Accessor.Type.GETTER)
	public String getCategorySystemName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(CATEGORYSYSTEMNAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.cxPersonalizationProcesses</code> attribute defined at extension <code>personalizationservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the cxPersonalizationProcesses
	 */
	@Accessor(qualifier = "cxPersonalizationProcesses", type = Accessor.Type.GETTER)
	public Collection<CxPersonalizationProcessModel> getCxPersonalizationProcesses()
	{
		return getPersistenceContext().getPropertyValue(CXPERSONALIZATIONPROCESSES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.defaultCurrency</code> attribute defined at extension <code>catalog</code>. 
	 * @return the defaultCurrency - Default Currency
	 */
	@Accessor(qualifier = "defaultCurrency", type = Accessor.Type.GETTER)
	public CurrencyModel getDefaultCurrency()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTCURRENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.facetSearchConfigs</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the facetSearchConfigs
	 */
	@Accessor(qualifier = "facetSearchConfigs", type = Accessor.Type.GETTER)
	public List<SolrFacetSearchConfigModel> getFacetSearchConfigs()
	{
		return getPersistenceContext().getPropertyValue(FACETSEARCHCONFIGS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.generationDate</code> attribute defined at extension <code>catalog</code>. 
	 * @return the generationDate - Generation Date
	 */
	@Accessor(qualifier = "generationDate", type = Accessor.Type.GETTER)
	public Date getGenerationDate()
	{
		return getPersistenceContext().getPropertyValue(GENERATIONDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.generatorInfo</code> attribute defined at extension <code>catalog</code>. 
	 * @return the generatorInfo - Generator Info
	 */
	@Accessor(qualifier = "generatorInfo", type = Accessor.Type.GETTER)
	public String getGeneratorInfo()
	{
		return getPersistenceContext().getPropertyValue(GENERATORINFO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.inclAssurance</code> attribute defined at extension <code>catalog</code>. 
	 * @return the inclAssurance - incl.Assurance
	 */
	@Accessor(qualifier = "inclAssurance", type = Accessor.Type.GETTER)
	public Boolean getInclAssurance()
	{
		return getPersistenceContext().getPropertyValue(INCLASSURANCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.inclDuty</code> attribute defined at extension <code>catalog</code>. 
	 * @return the inclDuty - incl.Duty
	 */
	@Accessor(qualifier = "inclDuty", type = Accessor.Type.GETTER)
	public Boolean getInclDuty()
	{
		return getPersistenceContext().getPropertyValue(INCLDUTY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.inclFreight</code> attribute defined at extension <code>catalog</code>. 
	 * @return the inclFreight - incl.Freight
	 */
	@Accessor(qualifier = "inclFreight", type = Accessor.Type.GETTER)
	public Boolean getInclFreight()
	{
		return getPersistenceContext().getPropertyValue(INCLFREIGHT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.inclPacking</code> attribute defined at extension <code>catalog</code>. 
	 * @return the inclPacking - incl.Packing
	 */
	@Accessor(qualifier = "inclPacking", type = Accessor.Type.GETTER)
	public Boolean getInclPacking()
	{
		return getPersistenceContext().getPropertyValue(INCLPACKING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.incomingSynchronizations</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the incomingSynchronizations
	 */
	@Accessor(qualifier = "incomingSynchronizations", type = Accessor.Type.GETTER)
	public List<SyncItemJobModel> getIncomingSynchronizations()
	{
		return getPersistenceContext().getPropertyValue(INCOMINGSYNCHRONIZATIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.languages</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the languages - languages
	 */
	@Accessor(qualifier = "languages", type = Accessor.Type.GETTER)
	public Collection<LanguageModel> getLanguages()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.mimeRootDirectory</code> attribute defined at extension <code>catalog</code>. 
	 * @return the mimeRootDirectory - Mime Root Directory
	 */
	@Accessor(qualifier = "mimeRootDirectory", type = Accessor.Type.GETTER)
	public String getMimeRootDirectory()
	{
		return getPersistenceContext().getPropertyValue(MIMEROOTDIRECTORY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.mnemonic</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the mnemonic
	 */
	@Accessor(qualifier = "mnemonic", type = Accessor.Type.GETTER)
	public String getMnemonic()
	{
		return getPersistenceContext().getPropertyValue(MNEMONIC);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.previews</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the previews
	 */
	@Accessor(qualifier = "previews", type = Accessor.Type.GETTER)
	public Collection<PreviewDataModel> getPreviews()
	{
		return getPersistenceContext().getPropertyValue(PREVIEWS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.previousUpdateVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the previousUpdateVersion
	 */
	@Accessor(qualifier = "previousUpdateVersion", type = Accessor.Type.GETTER)
	public Integer getPreviousUpdateVersion()
	{
		return getPersistenceContext().getPropertyValue(PREVIOUSUPDATEVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.readPrincipals</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the readPrincipals
	 */
	@Accessor(qualifier = "readPrincipals", type = Accessor.Type.GETTER)
	public List<PrincipalModel> getReadPrincipals()
	{
		return getPersistenceContext().getPropertyValue(READPRINCIPALS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.rootCategories</code> dynamic attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the rootCategories
	 */
	@Accessor(qualifier = "rootCategories", type = Accessor.Type.GETTER)
	public List<CategoryModel> getRootCategories()
	{
		return getPersistenceContext().getDynamicValue(this,ROOTCATEGORIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.synchronizations</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the synchronizations
	 */
	@Accessor(qualifier = "synchronizations", type = Accessor.Type.GETTER)
	public List<SyncItemJobModel> getSynchronizations()
	{
		return getPersistenceContext().getPropertyValue(SYNCHRONIZATIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.territories</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the territories - Territory
	 */
	@Accessor(qualifier = "territories", type = Accessor.Type.GETTER)
	public Collection<CountryModel> getTerritories()
	{
		return getPersistenceContext().getPropertyValue(TERRITORIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.version</code> attribute defined at extension <code>catalog</code>. 
	 * @return the version - version
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.GETTER)
	public String getVersion()
	{
		return getPersistenceContext().getPropertyValue(VERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.writePrincipals</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the writePrincipals
	 */
	@Accessor(qualifier = "writePrincipals", type = Accessor.Type.GETTER)
	public List<PrincipalModel> getWritePrincipals()
	{
		return getPersistenceContext().getPropertyValue(WRITEPRINCIPALS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.active</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the active - active flag
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.agreements</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the agreements
	 */
	@Accessor(qualifier = "agreements", type = Accessor.Type.SETTER)
	public void setAgreements(final List<AgreementModel> value)
	{
		getPersistenceContext().setPropertyValue(AGREEMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CatalogVersion.catalog</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the catalog
	 */
	@Accessor(qualifier = "catalog", type = Accessor.Type.SETTER)
	public void setCatalog(final CatalogModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.categorySystemDescription</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the categorySystemDescription
	 */
	@Accessor(qualifier = "categorySystemDescription", type = Accessor.Type.SETTER)
	public void setCategorySystemDescription(final String value)
	{
		setCategorySystemDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.categorySystemDescription</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the categorySystemDescription
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "categorySystemDescription", type = Accessor.Type.SETTER)
	public void setCategorySystemDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(CATEGORYSYSTEMDESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.categorySystemID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the categorySystemID
	 */
	@Accessor(qualifier = "categorySystemID", type = Accessor.Type.SETTER)
	public void setCategorySystemID(final String value)
	{
		getPersistenceContext().setPropertyValue(CATEGORYSYSTEMID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.categorySystemName</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the categorySystemName
	 */
	@Accessor(qualifier = "categorySystemName", type = Accessor.Type.SETTER)
	public void setCategorySystemName(final String value)
	{
		setCategorySystemName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.categorySystemName</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the categorySystemName
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "categorySystemName", type = Accessor.Type.SETTER)
	public void setCategorySystemName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(CATEGORYSYSTEMNAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.cxPersonalizationProcesses</code> attribute defined at extension <code>personalizationservices</code>. 
	 *  
	 * @param value the cxPersonalizationProcesses
	 */
	@Accessor(qualifier = "cxPersonalizationProcesses", type = Accessor.Type.SETTER)
	public void setCxPersonalizationProcesses(final Collection<CxPersonalizationProcessModel> value)
	{
		getPersistenceContext().setPropertyValue(CXPERSONALIZATIONPROCESSES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.defaultCurrency</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the defaultCurrency - Default Currency
	 */
	@Accessor(qualifier = "defaultCurrency", type = Accessor.Type.SETTER)
	public void setDefaultCurrency(final CurrencyModel value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTCURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.facetSearchConfigs</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the facetSearchConfigs
	 */
	@Accessor(qualifier = "facetSearchConfigs", type = Accessor.Type.SETTER)
	public void setFacetSearchConfigs(final List<SolrFacetSearchConfigModel> value)
	{
		getPersistenceContext().setPropertyValue(FACETSEARCHCONFIGS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.generationDate</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the generationDate - Generation Date
	 */
	@Accessor(qualifier = "generationDate", type = Accessor.Type.SETTER)
	public void setGenerationDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(GENERATIONDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.generatorInfo</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the generatorInfo - Generator Info
	 */
	@Accessor(qualifier = "generatorInfo", type = Accessor.Type.SETTER)
	public void setGeneratorInfo(final String value)
	{
		getPersistenceContext().setPropertyValue(GENERATORINFO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.inclAssurance</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the inclAssurance - incl.Assurance
	 */
	@Accessor(qualifier = "inclAssurance", type = Accessor.Type.SETTER)
	public void setInclAssurance(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(INCLASSURANCE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.inclDuty</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the inclDuty - incl.Duty
	 */
	@Accessor(qualifier = "inclDuty", type = Accessor.Type.SETTER)
	public void setInclDuty(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(INCLDUTY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.inclFreight</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the inclFreight - incl.Freight
	 */
	@Accessor(qualifier = "inclFreight", type = Accessor.Type.SETTER)
	public void setInclFreight(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(INCLFREIGHT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.inclPacking</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the inclPacking - incl.Packing
	 */
	@Accessor(qualifier = "inclPacking", type = Accessor.Type.SETTER)
	public void setInclPacking(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(INCLPACKING, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.languages</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the languages - languages
	 */
	@Accessor(qualifier = "languages", type = Accessor.Type.SETTER)
	public void setLanguages(final Collection<LanguageModel> value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.mimeRootDirectory</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the mimeRootDirectory - Mime Root Directory
	 */
	@Accessor(qualifier = "mimeRootDirectory", type = Accessor.Type.SETTER)
	public void setMimeRootDirectory(final String value)
	{
		getPersistenceContext().setPropertyValue(MIMEROOTDIRECTORY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.mnemonic</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the mnemonic
	 */
	@Accessor(qualifier = "mnemonic", type = Accessor.Type.SETTER)
	public void setMnemonic(final String value)
	{
		getPersistenceContext().setPropertyValue(MNEMONIC, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.previews</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the previews
	 */
	@Accessor(qualifier = "previews", type = Accessor.Type.SETTER)
	public void setPreviews(final Collection<PreviewDataModel> value)
	{
		getPersistenceContext().setPropertyValue(PREVIEWS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.previousUpdateVersion</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the previousUpdateVersion
	 */
	@Accessor(qualifier = "previousUpdateVersion", type = Accessor.Type.SETTER)
	public void setPreviousUpdateVersion(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PREVIOUSUPDATEVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.readPrincipals</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the readPrincipals
	 */
	@Accessor(qualifier = "readPrincipals", type = Accessor.Type.SETTER)
	public void setReadPrincipals(final List<PrincipalModel> value)
	{
		getPersistenceContext().setPropertyValue(READPRINCIPALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.rootCategories</code> dynamic attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the rootCategories
	 */
	@Accessor(qualifier = "rootCategories", type = Accessor.Type.SETTER)
	public void setRootCategories(final List<CategoryModel> value)
	{
		getPersistenceContext().setDynamicValue(this,ROOTCATEGORIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.synchronizations</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the synchronizations
	 */
	@Accessor(qualifier = "synchronizations", type = Accessor.Type.SETTER)
	public void setSynchronizations(final List<SyncItemJobModel> value)
	{
		getPersistenceContext().setPropertyValue(SYNCHRONIZATIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.territories</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the territories - Territory
	 */
	@Accessor(qualifier = "territories", type = Accessor.Type.SETTER)
	public void setTerritories(final Collection<CountryModel> value)
	{
		getPersistenceContext().setPropertyValue(TERRITORIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.version</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the version - version
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.SETTER)
	public void setVersion(final String value)
	{
		getPersistenceContext().setPropertyValue(VERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.writePrincipals</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the writePrincipals
	 */
	@Accessor(qualifier = "writePrincipals", type = Accessor.Type.SETTER)
	public void setWritePrincipals(final List<PrincipalModel> value)
	{
		getPersistenceContext().setPropertyValue(WRITEPRINCIPALS, value);
	}
	
}
