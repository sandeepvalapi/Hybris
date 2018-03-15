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
package de.hybris.platform.core.model.c2l;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrStopWordModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrSynonymConfigModel;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Collection;
import java.util.List;

/**
 * Generated model class for type Language first defined at extension core.
 */
@SuppressWarnings("all")
public class LanguageModel extends C2LItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Language";
	
	/**<i>Generated relation code constant for relation <code>SyncJob2LangRel</code> defining source attribute <code>syncJobs</code> in extension <code>catalog</code>.</i>*/
	public static final String _SYNCJOB2LANGREL = "SyncJob2LangRel";
	
	/**<i>Generated relation code constant for relation <code>SolrFacetSearchConfig2LanguageRelation</code> defining source attribute <code>facetSearchConfigs</code> in extension <code>solrfacetsearch</code>.</i>*/
	public static final String _SOLRFACETSEARCHCONFIG2LANGUAGERELATION = "SolrFacetSearchConfig2LanguageRelation";
	
	/**<i>Generated relation code constant for relation <code>SolrSynonymConfig2Language</code> defining source attribute <code>synonyms</code> in extension <code>solrfacetsearch</code>.</i>*/
	public static final String _SOLRSYNONYMCONFIG2LANGUAGE = "SolrSynonymConfig2Language";
	
	/**<i>Generated relation code constant for relation <code>SolrStopWord2Language</code> defining source attribute <code>StopWords</code> in extension <code>solrfacetsearch</code>.</i>*/
	public static final String _SOLRSTOPWORD2LANGUAGE = "SolrStopWord2Language";
	
	/**<i>Generated relation code constant for relation <code>BaseStore2LanguageRel</code> defining source attribute <code>baseStores</code> in extension <code>commerceservices</code>.</i>*/
	public static final String _BASESTORE2LANGUAGEREL = "BaseStore2LanguageRel";
	
	/** <i>Generated constant</i> - Attribute key of <code>Language.fallbackLanguages</code> attribute defined at extension <code>core</code>. */
	public static final String FALLBACKLANGUAGES = "fallbackLanguages";
	
	/** <i>Generated constant</i> - Attribute key of <code>Language.syncJobs</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYNCJOBS = "syncJobs";
	
	/** <i>Generated constant</i> - Attribute key of <code>Language.facetSearchConfigs</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FACETSEARCHCONFIGS = "facetSearchConfigs";
	
	/** <i>Generated constant</i> - Attribute key of <code>Language.synonyms</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String SYNONYMS = "synonyms";
	
	/** <i>Generated constant</i> - Attribute key of <code>Language.StopWords</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String STOPWORDS = "StopWords";
	
	/** <i>Generated constant</i> - Attribute key of <code>Language.baseStores</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String BASESTORES = "baseStores";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public LanguageModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public LanguageModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _isocode initial attribute declared by type <code>Language</code> at extension <code>core</code>
	 */
	@Deprecated
	public LanguageModel(final String _isocode)
	{
		super();
		setIsocode(_isocode);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _isocode initial attribute declared by type <code>Language</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public LanguageModel(final String _isocode, final ItemModel _owner)
	{
		super();
		setIsocode(_isocode);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Language.baseStores</code> attribute defined at extension <code>commerceservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the baseStores
	 */
	@Accessor(qualifier = "baseStores", type = Accessor.Type.GETTER)
	public Collection<BaseStoreModel> getBaseStores()
	{
		return getPersistenceContext().getPropertyValue(BASESTORES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Language.facetSearchConfigs</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the facetSearchConfigs
	 */
	@Accessor(qualifier = "facetSearchConfigs", type = Accessor.Type.GETTER)
	public List<SolrFacetSearchConfigModel> getFacetSearchConfigs()
	{
		return getPersistenceContext().getPropertyValue(FACETSEARCHCONFIGS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Language.fallbackLanguages</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the fallbackLanguages
	 */
	@Accessor(qualifier = "fallbackLanguages", type = Accessor.Type.GETTER)
	public List<LanguageModel> getFallbackLanguages()
	{
		final List<LanguageModel> value = getPersistenceContext().getPropertyValue(FALLBACKLANGUAGES);
		return value != null ? value : java.util.Collections.emptyList();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Language.StopWords</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the StopWords
	 */
	@Accessor(qualifier = "StopWords", type = Accessor.Type.GETTER)
	public List<SolrStopWordModel> getStopWords()
	{
		return getPersistenceContext().getPropertyValue(STOPWORDS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Language.synonyms</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the synonyms
	 */
	@Accessor(qualifier = "synonyms", type = Accessor.Type.GETTER)
	public List<SolrSynonymConfigModel> getSynonyms()
	{
		return getPersistenceContext().getPropertyValue(SYNONYMS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Language.baseStores</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the baseStores
	 */
	@Accessor(qualifier = "baseStores", type = Accessor.Type.SETTER)
	public void setBaseStores(final Collection<BaseStoreModel> value)
	{
		getPersistenceContext().setPropertyValue(BASESTORES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Language.facetSearchConfigs</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the facetSearchConfigs
	 */
	@Accessor(qualifier = "facetSearchConfigs", type = Accessor.Type.SETTER)
	public void setFacetSearchConfigs(final List<SolrFacetSearchConfigModel> value)
	{
		getPersistenceContext().setPropertyValue(FACETSEARCHCONFIGS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Language.fallbackLanguages</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the fallbackLanguages
	 */
	@Accessor(qualifier = "fallbackLanguages", type = Accessor.Type.SETTER)
	public void setFallbackLanguages(final List<LanguageModel> value)
	{
		getPersistenceContext().setPropertyValue(FALLBACKLANGUAGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Language.StopWords</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the StopWords
	 */
	@Accessor(qualifier = "StopWords", type = Accessor.Type.SETTER)
	public void setStopWords(final List<SolrStopWordModel> value)
	{
		getPersistenceContext().setPropertyValue(STOPWORDS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Language.synonyms</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the synonyms
	 */
	@Accessor(qualifier = "synonyms", type = Accessor.Type.SETTER)
	public void setSynonyms(final List<SolrSynonymConfigModel> value)
	{
		getPersistenceContext().setPropertyValue(SYNONYMS, value);
	}
	
}
