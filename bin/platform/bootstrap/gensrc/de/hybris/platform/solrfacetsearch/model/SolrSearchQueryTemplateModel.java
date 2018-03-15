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
import de.hybris.platform.solrfacetsearch.model.SolrSearchQueryPropertyModel;
import de.hybris.platform.solrfacetsearch.model.SolrSearchQuerySortModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import java.util.Collection;
import java.util.Map;

/**
 * Generated model class for type SolrSearchQueryTemplate first defined at extension solrfacetsearch.
 */
@SuppressWarnings("all")
public class SolrSearchQueryTemplateModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SolrSearchQueryTemplate";
	
	/**<i>Generated relation code constant for relation <code>SolrIndexedType2SolrSearchQueryTemplate</code> defining source attribute <code>indexedType</code> in extension <code>solrfacetsearch</code>.</i>*/
	public static final String _SOLRINDEXEDTYPE2SOLRSEARCHQUERYTEMPLATE = "SolrIndexedType2SolrSearchQueryTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryTemplate.name</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryTemplate.showFacets</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String SHOWFACETS = "showFacets";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryTemplate.restrictFieldsInResponse</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String RESTRICTFIELDSINRESPONSE = "restrictFieldsInResponse";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryTemplate.enableHighlighting</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String ENABLEHIGHLIGHTING = "enableHighlighting";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryTemplate.group</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String GROUP = "group";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryTemplate.groupProperty</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String GROUPPROPERTY = "groupProperty";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryTemplate.groupLimit</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String GROUPLIMIT = "groupLimit";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryTemplate.groupFacets</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String GROUPFACETS = "groupFacets";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryTemplate.pageSize</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String PAGESIZE = "pageSize";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryTemplate.ftsQueryBuilder</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FTSQUERYBUILDER = "ftsQueryBuilder";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryTemplate.ftsQueryBuilderParameters</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String FTSQUERYBUILDERPARAMETERS = "ftsQueryBuilderParameters";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryTemplate.indexedTypePOS</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String INDEXEDTYPEPOS = "indexedTypePOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryTemplate.indexedType</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String INDEXEDTYPE = "indexedType";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryTemplate.searchQueryProperties</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String SEARCHQUERYPROPERTIES = "searchQueryProperties";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrSearchQueryTemplate.searchQuerySorts</code> attribute defined at extension <code>solrfacetsearch</code>. */
	public static final String SEARCHQUERYSORTS = "searchQuerySorts";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrSearchQueryTemplateModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrSearchQueryTemplateModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _indexedType initial attribute declared by type <code>SolrSearchQueryTemplate</code> at extension <code>solrfacetsearch</code>
	 * @param _name initial attribute declared by type <code>SolrSearchQueryTemplate</code> at extension <code>solrfacetsearch</code>
	 */
	@Deprecated
	public SolrSearchQueryTemplateModel(final SolrIndexedTypeModel _indexedType, final String _name)
	{
		super();
		setIndexedType(_indexedType);
		setName(_name);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _indexedType initial attribute declared by type <code>SolrSearchQueryTemplate</code> at extension <code>solrfacetsearch</code>
	 * @param _name initial attribute declared by type <code>SolrSearchQueryTemplate</code> at extension <code>solrfacetsearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public SolrSearchQueryTemplateModel(final SolrIndexedTypeModel _indexedType, final String _name, final ItemModel _owner)
	{
		super();
		setIndexedType(_indexedType);
		setName(_name);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryTemplate.ftsQueryBuilder</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the ftsQueryBuilder
	 */
	@Accessor(qualifier = "ftsQueryBuilder", type = Accessor.Type.GETTER)
	public String getFtsQueryBuilder()
	{
		return getPersistenceContext().getPropertyValue(FTSQUERYBUILDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryTemplate.ftsQueryBuilderParameters</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the ftsQueryBuilderParameters
	 */
	@Accessor(qualifier = "ftsQueryBuilderParameters", type = Accessor.Type.GETTER)
	public Map<String,String> getFtsQueryBuilderParameters()
	{
		return getPersistenceContext().getPropertyValue(FTSQUERYBUILDERPARAMETERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryTemplate.groupLimit</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the groupLimit
	 */
	@Accessor(qualifier = "groupLimit", type = Accessor.Type.GETTER)
	public Integer getGroupLimit()
	{
		return getPersistenceContext().getPropertyValue(GROUPLIMIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryTemplate.groupProperty</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the groupProperty
	 */
	@Accessor(qualifier = "groupProperty", type = Accessor.Type.GETTER)
	public SolrIndexedPropertyModel getGroupProperty()
	{
		return getPersistenceContext().getPropertyValue(GROUPPROPERTY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryTemplate.indexedType</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the indexedType
	 */
	@Accessor(qualifier = "indexedType", type = Accessor.Type.GETTER)
	public SolrIndexedTypeModel getIndexedType()
	{
		return getPersistenceContext().getPropertyValue(INDEXEDTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryTemplate.name</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryTemplate.pageSize</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the pageSize
	 */
	@Accessor(qualifier = "pageSize", type = Accessor.Type.GETTER)
	public Integer getPageSize()
	{
		return getPersistenceContext().getPropertyValue(PAGESIZE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryTemplate.searchQueryProperties</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the searchQueryProperties
	 */
	@Accessor(qualifier = "searchQueryProperties", type = Accessor.Type.GETTER)
	public Collection<SolrSearchQueryPropertyModel> getSearchQueryProperties()
	{
		return getPersistenceContext().getPropertyValue(SEARCHQUERYPROPERTIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryTemplate.searchQuerySorts</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the searchQuerySorts
	 */
	@Accessor(qualifier = "searchQuerySorts", type = Accessor.Type.GETTER)
	public Collection<SolrSearchQuerySortModel> getSearchQuerySorts()
	{
		return getPersistenceContext().getPropertyValue(SEARCHQUERYSORTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryTemplate.enableHighlighting</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the enableHighlighting
	 */
	@Accessor(qualifier = "enableHighlighting", type = Accessor.Type.GETTER)
	public boolean isEnableHighlighting()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(ENABLEHIGHLIGHTING));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryTemplate.group</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the group
	 */
	@Accessor(qualifier = "group", type = Accessor.Type.GETTER)
	public boolean isGroup()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(GROUP));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryTemplate.groupFacets</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the groupFacets
	 */
	@Accessor(qualifier = "groupFacets", type = Accessor.Type.GETTER)
	public boolean isGroupFacets()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(GROUPFACETS));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryTemplate.restrictFieldsInResponse</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the restrictFieldsInResponse
	 */
	@Accessor(qualifier = "restrictFieldsInResponse", type = Accessor.Type.GETTER)
	public boolean isRestrictFieldsInResponse()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(RESTRICTFIELDSINRESPONSE));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrSearchQueryTemplate.showFacets</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 * @return the showFacets
	 */
	@Accessor(qualifier = "showFacets", type = Accessor.Type.GETTER)
	public boolean isShowFacets()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(SHOWFACETS));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryTemplate.enableHighlighting</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the enableHighlighting
	 */
	@Accessor(qualifier = "enableHighlighting", type = Accessor.Type.SETTER)
	public void setEnableHighlighting(final boolean value)
	{
		getPersistenceContext().setPropertyValue(ENABLEHIGHLIGHTING, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryTemplate.ftsQueryBuilder</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the ftsQueryBuilder
	 */
	@Accessor(qualifier = "ftsQueryBuilder", type = Accessor.Type.SETTER)
	public void setFtsQueryBuilder(final String value)
	{
		getPersistenceContext().setPropertyValue(FTSQUERYBUILDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryTemplate.ftsQueryBuilderParameters</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the ftsQueryBuilderParameters
	 */
	@Accessor(qualifier = "ftsQueryBuilderParameters", type = Accessor.Type.SETTER)
	public void setFtsQueryBuilderParameters(final Map<String,String> value)
	{
		getPersistenceContext().setPropertyValue(FTSQUERYBUILDERPARAMETERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryTemplate.group</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the group
	 */
	@Accessor(qualifier = "group", type = Accessor.Type.SETTER)
	public void setGroup(final boolean value)
	{
		getPersistenceContext().setPropertyValue(GROUP, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryTemplate.groupFacets</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the groupFacets
	 */
	@Accessor(qualifier = "groupFacets", type = Accessor.Type.SETTER)
	public void setGroupFacets(final boolean value)
	{
		getPersistenceContext().setPropertyValue(GROUPFACETS, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryTemplate.groupLimit</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the groupLimit
	 */
	@Accessor(qualifier = "groupLimit", type = Accessor.Type.SETTER)
	public void setGroupLimit(final Integer value)
	{
		getPersistenceContext().setPropertyValue(GROUPLIMIT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryTemplate.groupProperty</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the groupProperty
	 */
	@Accessor(qualifier = "groupProperty", type = Accessor.Type.SETTER)
	public void setGroupProperty(final SolrIndexedPropertyModel value)
	{
		getPersistenceContext().setPropertyValue(GROUPPROPERTY, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SolrSearchQueryTemplate.indexedType</code> attribute defined at extension <code>solrfacetsearch</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the indexedType
	 */
	@Accessor(qualifier = "indexedType", type = Accessor.Type.SETTER)
	public void setIndexedType(final SolrIndexedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(INDEXEDTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryTemplate.name</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryTemplate.pageSize</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the pageSize
	 */
	@Accessor(qualifier = "pageSize", type = Accessor.Type.SETTER)
	public void setPageSize(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PAGESIZE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryTemplate.restrictFieldsInResponse</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the restrictFieldsInResponse
	 */
	@Accessor(qualifier = "restrictFieldsInResponse", type = Accessor.Type.SETTER)
	public void setRestrictFieldsInResponse(final boolean value)
	{
		getPersistenceContext().setPropertyValue(RESTRICTFIELDSINRESPONSE, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryTemplate.searchQueryProperties</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the searchQueryProperties
	 */
	@Accessor(qualifier = "searchQueryProperties", type = Accessor.Type.SETTER)
	public void setSearchQueryProperties(final Collection<SolrSearchQueryPropertyModel> value)
	{
		getPersistenceContext().setPropertyValue(SEARCHQUERYPROPERTIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryTemplate.searchQuerySorts</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the searchQuerySorts
	 */
	@Accessor(qualifier = "searchQuerySorts", type = Accessor.Type.SETTER)
	public void setSearchQuerySorts(final Collection<SolrSearchQuerySortModel> value)
	{
		getPersistenceContext().setPropertyValue(SEARCHQUERYSORTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrSearchQueryTemplate.showFacets</code> attribute defined at extension <code>solrfacetsearch</code>. 
	 *  
	 * @param value the showFacets
	 */
	@Accessor(qualifier = "showFacets", type = Accessor.Type.SETTER)
	public void setShowFacets(final boolean value)
	{
		getPersistenceContext().setPropertyValue(SHOWFACETS, toObject(value));
	}
	
}
