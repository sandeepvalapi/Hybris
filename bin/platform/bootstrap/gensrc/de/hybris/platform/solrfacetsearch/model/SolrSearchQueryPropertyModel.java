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
package de.hybris.platform.solrfacetsearch.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.enums.SolrIndexedPropertyFacetType;
import de.hybris.platform.solrfacetsearch.enums.SolrWildcardType;
import de.hybris.platform.solrfacetsearch.model.SolrSearchQueryTemplateModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;

/**
 * Generated model class for type SolrSearchQueryProperty first defined at extension solrfacetsearch.
 */
@SuppressWarnings("all")
public class SolrSearchQueryPropertyModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SolrSearchQueryProperty";
	
	/**<i>Generated relation code constant for relation <code>SolrIndexedProperty2SolrSearchQueryProperty</code> defining source attribute <code>indexedProperty</code> in extension <code>solrfacetsearch</code>.</i>*/
	public static final String _SOLRINDEXEDPROPERTY2SOLRSEARCHQUERYPROPERTY = "SolrIndexedProperty2SolrSearchQueryProperty";
	
	/**<i>Generated relation code constant for relation <code>SolrSearchQueryTemplate2SolrSearchQueryProperty</code> defining source attribute <code>searchQueryTemplate</code> in extension <code>solrfacetsearch</code>.</i>*/
	public static final String _SOLRSEARCHQUERYTEMPLATE2SOLRSEARCHQUERYPROPERTY = "SolrSearchQueryTemplate2SolrSearchQueryProperty";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.priority</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String PRIORITY = "priority";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.includeInResponse</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String INCLUDEINRESPONSE = "includeInResponse";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.useForHighlighting</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String USEFORHIGHLIGHTING = "useForHighlighting";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.facet</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FACET = "facet";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.facetType</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FACETTYPE = "facetType";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.facetDisplayNameProvider</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FACETDISPLAYNAMEPROVIDER = "facetDisplayNameProvider";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.facetSortProvider</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FACETSORTPROVIDER = "facetSortProvider";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.facetTopValuesProvider</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FACETTOPVALUESPROVIDER = "facetTopValuesProvider";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.ftsQuery</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FTSQUERY = "ftsQuery";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.ftsQueryMinTermLength</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FTSQUERYMINTERMLENGTH = "ftsQueryMinTermLength";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.ftsQueryBoost</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FTSQUERYBOOST = "ftsQueryBoost";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.ftsFuzzyQuery</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FTSFUZZYQUERY = "ftsFuzzyQuery";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.ftsFuzzyQueryMinTermLength</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FTSFUZZYQUERYMINTERMLENGTH = "ftsFuzzyQueryMinTermLength";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.ftsFuzzyQueryFuzziness</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FTSFUZZYQUERYFUZZINESS = "ftsFuzzyQueryFuzziness";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.ftsFuzzyQueryBoost</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FTSFUZZYQUERYBOOST = "ftsFuzzyQueryBoost";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.ftsWildcardQuery</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FTSWILDCARDQUERY = "ftsWildcardQuery";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.ftsWildcardQueryMinTermLength</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FTSWILDCARDQUERYMINTERMLENGTH = "ftsWildcardQueryMinTermLength";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.ftsWildcardQueryType</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FTSWILDCARDQUERYTYPE = "ftsWildcardQueryType";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.ftsWildcardQueryBoost</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FTSWILDCARDQUERYBOOST = "ftsWildcardQueryBoost";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.ftsPhraseQuery</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FTSPHRASEQUERY = "ftsPhraseQuery";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.ftsPhraseQuerySlop</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FTSPHRASEQUERYSLOP = "ftsPhraseQuerySlop";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.ftsPhraseQueryBoost</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FTSPHRASEQUERYBOOST = "ftsPhraseQueryBoost";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.indexedPropertyPOS</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String INDEXEDPROPERTYPOS = "indexedPropertyPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.indexedProperty</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String INDEXEDPROPERTY = "indexedProperty";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.searchQueryTemplatePOS</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String SEARCHQUERYTEMPLATEPOS = "searchQueryTemplatePOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryProperty.searchQueryTemplate</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String SEARCHQUERYTEMPLATE = "searchQueryTemplate";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrSearchQueryPropertyModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrSearchQueryPropertyModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _indexedProperty initial attribute declared by type <code>SolrSearchQueryProperty</code> at extension <code>solrfacetsearch</code>
	 * @param _searchQueryTemplate initial attribute declared by type <code>SolrSearchQueryProperty</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrSearchQueryPropertyModel(final SolrIndexedPropertyModel _indexedProperty, final SolrSearchQueryTemplateModel _searchQueryTemplate)
	{
		super();
		setIndexedProperty(_indexedProperty);
		setSearchQueryTemplate(_searchQueryTemplate);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _indexedProperty initial attribute declared by type <code>SolrSearchQueryProperty</code> at extension <code>solrfacetsearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _searchQueryTemplate initial attribute declared by type <code>SolrSearchQueryProperty</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrSearchQueryPropertyModel(final SolrIndexedPropertyModel _indexedProperty, final ItemModel _owner, final SolrSearchQueryTemplateModel _searchQueryTemplate)
	{
		super();
		setIndexedProperty(_indexedProperty);
		setOwner(_owner);
		setSearchQueryTemplate(_searchQueryTemplate);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.facetDisplayNameProvider</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the facetDisplayNameProvider
	 */
	@Accessor(qualifier = "facetDisplayNameProvider", type = Accessor.Type.GETTER)
	public String getFacetDisplayNameProvider()
	{
		return getPersistenceContext().getPropertyValue(FACETDISPLAYNAMEPROVIDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.facetSortProvider</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the facetSortProvider
	 */
	@Accessor(qualifier = "facetSortProvider", type = Accessor.Type.GETTER)
	public String getFacetSortProvider()
	{
		return getPersistenceContext().getPropertyValue(FACETSORTPROVIDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.facetTopValuesProvider</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the facetTopValuesProvider
	 */
	@Accessor(qualifier = "facetTopValuesProvider", type = Accessor.Type.GETTER)
	public String getFacetTopValuesProvider()
	{
		return getPersistenceContext().getPropertyValue(FACETTOPVALUESPROVIDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.facetType</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the facetType
	 */
	@Accessor(qualifier = "facetType", type = Accessor.Type.GETTER)
	public SolrIndexedPropertyFacetType getFacetType()
	{
		return getPersistenceContext().getPropertyValue(FACETTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.ftsFuzzyQueryBoost</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the ftsFuzzyQueryBoost
	 */
	@Accessor(qualifier = "ftsFuzzyQueryBoost", type = Accessor.Type.GETTER)
	public Float getFtsFuzzyQueryBoost()
	{
		return getPersistenceContext().getPropertyValue(FTSFUZZYQUERYBOOST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.ftsFuzzyQueryFuzziness</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the ftsFuzzyQueryFuzziness
	 */
	@Accessor(qualifier = "ftsFuzzyQueryFuzziness", type = Accessor.Type.GETTER)
	public Integer getFtsFuzzyQueryFuzziness()
	{
		return getPersistenceContext().getPropertyValue(FTSFUZZYQUERYFUZZINESS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.ftsFuzzyQueryMinTermLength</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the ftsFuzzyQueryMinTermLength
	 */
	@Accessor(qualifier = "ftsFuzzyQueryMinTermLength", type = Accessor.Type.GETTER)
	public Integer getFtsFuzzyQueryMinTermLength()
	{
		return getPersistenceContext().getPropertyValue(FTSFUZZYQUERYMINTERMLENGTH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.ftsPhraseQueryBoost</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the ftsPhraseQueryBoost
	 */
	@Accessor(qualifier = "ftsPhraseQueryBoost", type = Accessor.Type.GETTER)
	public Float getFtsPhraseQueryBoost()
	{
		return getPersistenceContext().getPropertyValue(FTSPHRASEQUERYBOOST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.ftsPhraseQuerySlop</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the ftsPhraseQuerySlop
	 */
	@Accessor(qualifier = "ftsPhraseQuerySlop", type = Accessor.Type.GETTER)
	public Float getFtsPhraseQuerySlop()
	{
		return getPersistenceContext().getPropertyValue(FTSPHRASEQUERYSLOP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.ftsQueryBoost</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the ftsQueryBoost
	 */
	@Accessor(qualifier = "ftsQueryBoost", type = Accessor.Type.GETTER)
	public Float getFtsQueryBoost()
	{
		return getPersistenceContext().getPropertyValue(FTSQUERYBOOST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.ftsQueryMinTermLength</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the ftsQueryMinTermLength
	 */
	@Accessor(qualifier = "ftsQueryMinTermLength", type = Accessor.Type.GETTER)
	public Integer getFtsQueryMinTermLength()
	{
		return getPersistenceContext().getPropertyValue(FTSQUERYMINTERMLENGTH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.ftsWildcardQueryBoost</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the ftsWildcardQueryBoost
	 */
	@Accessor(qualifier = "ftsWildcardQueryBoost", type = Accessor.Type.GETTER)
	public Float getFtsWildcardQueryBoost()
	{
		return getPersistenceContext().getPropertyValue(FTSWILDCARDQUERYBOOST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.ftsWildcardQueryMinTermLength</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the ftsWildcardQueryMinTermLength
	 */
	@Accessor(qualifier = "ftsWildcardQueryMinTermLength", type = Accessor.Type.GETTER)
	public Integer getFtsWildcardQueryMinTermLength()
	{
		return getPersistenceContext().getPropertyValue(FTSWILDCARDQUERYMINTERMLENGTH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.ftsWildcardQueryType</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the ftsWildcardQueryType
	 */
	@Accessor(qualifier = "ftsWildcardQueryType", type = Accessor.Type.GETTER)
	public SolrWildcardType getFtsWildcardQueryType()
	{
		return getPersistenceContext().getPropertyValue(FTSWILDCARDQUERYTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.indexedProperty</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the indexedProperty
	 */
	@Accessor(qualifier = "indexedProperty", type = Accessor.Type.GETTER)
	public SolrIndexedPropertyModel getIndexedProperty()
	{
		return getPersistenceContext().getPropertyValue(INDEXEDPROPERTY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.priority</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public Integer getPriority()
	{
		return getPersistenceContext().getPropertyValue(PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.searchQueryTemplate</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the searchQueryTemplate
	 */
	@Accessor(qualifier = "searchQueryTemplate", type = Accessor.Type.GETTER)
	public SolrSearchQueryTemplateModel getSearchQueryTemplate()
	{
		return getPersistenceContext().getPropertyValue(SEARCHQUERYTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.useForHighlighting</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the useForHighlighting - Determines if this property will be used for highlighting search term
	 */
	@Accessor(qualifier = "useForHighlighting", type = Accessor.Type.GETTER)
	public Boolean getUseForHighlighting()
	{
		return getPersistenceContext().getPropertyValue(USEFORHIGHLIGHTING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.facet</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the facet
	 */
	@Accessor(qualifier = "facet", type = Accessor.Type.GETTER)
	public boolean isFacet()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(FACET));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.ftsFuzzyQuery</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the ftsFuzzyQuery
	 */
	@Accessor(qualifier = "ftsFuzzyQuery", type = Accessor.Type.GETTER)
	public boolean isFtsFuzzyQuery()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(FTSFUZZYQUERY));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.ftsPhraseQuery</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the ftsPhraseQuery
	 */
	@Accessor(qualifier = "ftsPhraseQuery", type = Accessor.Type.GETTER)
	public boolean isFtsPhraseQuery()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(FTSPHRASEQUERY));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.ftsQuery</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the ftsQuery
	 */
	@Accessor(qualifier = "ftsQuery", type = Accessor.Type.GETTER)
	public boolean isFtsQuery()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(FTSQUERY));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.ftsWildcardQuery</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the ftsWildcardQuery
	 */
	@Accessor(qualifier = "ftsWildcardQuery", type = Accessor.Type.GETTER)
	public boolean isFtsWildcardQuery()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(FTSWILDCARDQUERY));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryProperty.includeInResponse</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the includeInResponse
	 */
	@Accessor(qualifier = "includeInResponse", type = Accessor.Type.GETTER)
	public boolean isIncludeInResponse()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(INCLUDEINRESPONSE));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.facet</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the facet
	 */
	@Accessor(qualifier = "facet", type = Accessor.Type.SETTER)
	public void setFacet(final boolean value)
	{
		getPersistenceContext().setPropertyValue(FACET, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.facetDisplayNameProvider</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the facetDisplayNameProvider
	 */
	@Accessor(qualifier = "facetDisplayNameProvider", type = Accessor.Type.SETTER)
	public void setFacetDisplayNameProvider(final String value)
	{
		getPersistenceContext().setPropertyValue(FACETDISPLAYNAMEPROVIDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.facetSortProvider</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the facetSortProvider
	 */
	@Accessor(qualifier = "facetSortProvider", type = Accessor.Type.SETTER)
	public void setFacetSortProvider(final String value)
	{
		getPersistenceContext().setPropertyValue(FACETSORTPROVIDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.facetTopValuesProvider</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the facetTopValuesProvider
	 */
	@Accessor(qualifier = "facetTopValuesProvider", type = Accessor.Type.SETTER)
	public void setFacetTopValuesProvider(final String value)
	{
		getPersistenceContext().setPropertyValue(FACETTOPVALUESPROVIDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.facetType</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the facetType
	 */
	@Accessor(qualifier = "facetType", type = Accessor.Type.SETTER)
	public void setFacetType(final SolrIndexedPropertyFacetType value)
	{
		getPersistenceContext().setPropertyValue(FACETTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.ftsFuzzyQuery</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the ftsFuzzyQuery
	 */
	@Accessor(qualifier = "ftsFuzzyQuery", type = Accessor.Type.SETTER)
	public void setFtsFuzzyQuery(final boolean value)
	{
		getPersistenceContext().setPropertyValue(FTSFUZZYQUERY, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.ftsFuzzyQueryBoost</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the ftsFuzzyQueryBoost
	 */
	@Accessor(qualifier = "ftsFuzzyQueryBoost", type = Accessor.Type.SETTER)
	public void setFtsFuzzyQueryBoost(final Float value)
	{
		getPersistenceContext().setPropertyValue(FTSFUZZYQUERYBOOST, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.ftsFuzzyQueryFuzziness</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the ftsFuzzyQueryFuzziness
	 */
	@Accessor(qualifier = "ftsFuzzyQueryFuzziness", type = Accessor.Type.SETTER)
	public void setFtsFuzzyQueryFuzziness(final Integer value)
	{
		getPersistenceContext().setPropertyValue(FTSFUZZYQUERYFUZZINESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.ftsFuzzyQueryMinTermLength</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the ftsFuzzyQueryMinTermLength
	 */
	@Accessor(qualifier = "ftsFuzzyQueryMinTermLength", type = Accessor.Type.SETTER)
	public void setFtsFuzzyQueryMinTermLength(final Integer value)
	{
		getPersistenceContext().setPropertyValue(FTSFUZZYQUERYMINTERMLENGTH, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.ftsPhraseQuery</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the ftsPhraseQuery
	 */
	@Accessor(qualifier = "ftsPhraseQuery", type = Accessor.Type.SETTER)
	public void setFtsPhraseQuery(final boolean value)
	{
		getPersistenceContext().setPropertyValue(FTSPHRASEQUERY, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.ftsPhraseQueryBoost</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the ftsPhraseQueryBoost
	 */
	@Accessor(qualifier = "ftsPhraseQueryBoost", type = Accessor.Type.SETTER)
	public void setFtsPhraseQueryBoost(final Float value)
	{
		getPersistenceContext().setPropertyValue(FTSPHRASEQUERYBOOST, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.ftsPhraseQuerySlop</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the ftsPhraseQuerySlop
	 */
	@Accessor(qualifier = "ftsPhraseQuerySlop", type = Accessor.Type.SETTER)
	public void setFtsPhraseQuerySlop(final Float value)
	{
		getPersistenceContext().setPropertyValue(FTSPHRASEQUERYSLOP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.ftsQuery</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the ftsQuery
	 */
	@Accessor(qualifier = "ftsQuery", type = Accessor.Type.SETTER)
	public void setFtsQuery(final boolean value)
	{
		getPersistenceContext().setPropertyValue(FTSQUERY, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.ftsQueryBoost</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the ftsQueryBoost
	 */
	@Accessor(qualifier = "ftsQueryBoost", type = Accessor.Type.SETTER)
	public void setFtsQueryBoost(final Float value)
	{
		getPersistenceContext().setPropertyValue(FTSQUERYBOOST, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.ftsQueryMinTermLength</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the ftsQueryMinTermLength
	 */
	@Accessor(qualifier = "ftsQueryMinTermLength", type = Accessor.Type.SETTER)
	public void setFtsQueryMinTermLength(final Integer value)
	{
		getPersistenceContext().setPropertyValue(FTSQUERYMINTERMLENGTH, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.ftsWildcardQuery</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the ftsWildcardQuery
	 */
	@Accessor(qualifier = "ftsWildcardQuery", type = Accessor.Type.SETTER)
	public void setFtsWildcardQuery(final boolean value)
	{
		getPersistenceContext().setPropertyValue(FTSWILDCARDQUERY, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.ftsWildcardQueryBoost</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the ftsWildcardQueryBoost
	 */
	@Accessor(qualifier = "ftsWildcardQueryBoost", type = Accessor.Type.SETTER)
	public void setFtsWildcardQueryBoost(final Float value)
	{
		getPersistenceContext().setPropertyValue(FTSWILDCARDQUERYBOOST, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.ftsWildcardQueryMinTermLength</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the ftsWildcardQueryMinTermLength
	 */
	@Accessor(qualifier = "ftsWildcardQueryMinTermLength", type = Accessor.Type.SETTER)
	public void setFtsWildcardQueryMinTermLength(final Integer value)
	{
		getPersistenceContext().setPropertyValue(FTSWILDCARDQUERYMINTERMLENGTH, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.ftsWildcardQueryType</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the ftsWildcardQueryType
	 */
	@Accessor(qualifier = "ftsWildcardQueryType", type = Accessor.Type.SETTER)
	public void setFtsWildcardQueryType(final SolrWildcardType value)
	{
		getPersistenceContext().setPropertyValue(FTSWILDCARDQUERYTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.includeInResponse</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the includeInResponse
	 */
	@Accessor(qualifier = "includeInResponse", type = Accessor.Type.SETTER)
	public void setIncludeInResponse(final boolean value)
	{
		getPersistenceContext().setPropertyValue(INCLUDEINRESPONSE, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SolrSearchQueryProperty.indexedProperty</code> attribute defined at extension <code>solrfacetsearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the indexedProperty
	 */
	@Accessor(qualifier = "indexedProperty", type = Accessor.Type.SETTER)
	public void setIndexedProperty(final SolrIndexedPropertyModel value)
	{
		getPersistenceContext().setPropertyValue(INDEXEDPROPERTY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.priority</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PRIORITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SolrSearchQueryProperty.searchQueryTemplate</code> attribute defined at extension <code>solrfacetsearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the searchQueryTemplate
	 */
	@Accessor(qualifier = "searchQueryTemplate", type = Accessor.Type.SETTER)
	public void setSearchQueryTemplate(final SolrSearchQueryTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(SEARCHQUERYTEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryProperty.useForHighlighting</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the useForHighlighting - Determines if this property will be used for highlighting search term
	 */
	@Accessor(qualifier = "useForHighlighting", type = Accessor.Type.SETTER)
	public void setUseForHighlighting(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(USEFORHIGHLIGHTING, value);
	}
	
}
