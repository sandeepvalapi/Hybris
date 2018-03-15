/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Mar 15, 2018 5:02:29 PM                     ---
 * ----------------------------------------------------------------
 */
package com.hybris.backoffice.solrsearch.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;

/**
 * Generated model class for type BackofficeIndexedTypeToSolrFacetSearchConfig first defined at extension backofficesolrsearch.
 */
@SuppressWarnings("all")
public class BackofficeIndexedTypeToSolrFacetSearchConfigModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "BackofficeIndexedTypeToSolrFacetSearchConfig";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeIndexedTypeToSolrFacetSearchConfig.indexedType</code> attribute defined at extension <code>backofficesolrsearch</code>. */
	public static final String INDEXEDTYPE = "indexedType";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeIndexedTypeToSolrFacetSearchConfig.solrFacetSearchConfig</code> attribute defined at extension <code>backofficesolrsearch</code>. */
	public static final String SOLRFACETSEARCHCONFIG = "solrFacetSearchConfig";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public BackofficeIndexedTypeToSolrFacetSearchConfigModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public BackofficeIndexedTypeToSolrFacetSearchConfigModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _indexedType initial attribute declared by type <code>BackofficeIndexedTypeToSolrFacetSearchConfig</code> at extension <code>backofficesolrsearch</code>
	 * @param _solrFacetSearchConfig initial attribute declared by type <code>BackofficeIndexedTypeToSolrFacetSearchConfig</code> at extension <code>backofficesolrsearch</code>
	 */
	@Deprecated
	public BackofficeIndexedTypeToSolrFacetSearchConfigModel(final ComposedTypeModel _indexedType, final SolrFacetSearchConfigModel _solrFacetSearchConfig)
	{
		super();
		setIndexedType(_indexedType);
		setSolrFacetSearchConfig(_solrFacetSearchConfig);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _indexedType initial attribute declared by type <code>BackofficeIndexedTypeToSolrFacetSearchConfig</code> at extension <code>backofficesolrsearch</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _solrFacetSearchConfig initial attribute declared by type <code>BackofficeIndexedTypeToSolrFacetSearchConfig</code> at extension <code>backofficesolrsearch</code>
	 */
	@Deprecated
	public BackofficeIndexedTypeToSolrFacetSearchConfigModel(final ComposedTypeModel _indexedType, final ItemModel _owner, final SolrFacetSearchConfigModel _solrFacetSearchConfig)
	{
		super();
		setIndexedType(_indexedType);
		setOwner(_owner);
		setSolrFacetSearchConfig(_solrFacetSearchConfig);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeIndexedTypeToSolrFacetSearchConfig.indexedType</code> attribute defined at extension <code>backofficesolrsearch</code>. 
	 * @return the indexedType
	 */
	@Accessor(qualifier = "indexedType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getIndexedType()
	{
		return getPersistenceContext().getPropertyValue(INDEXEDTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeIndexedTypeToSolrFacetSearchConfig.solrFacetSearchConfig</code> attribute defined at extension <code>backofficesolrsearch</code>. 
	 * @return the solrFacetSearchConfig
	 */
	@Accessor(qualifier = "solrFacetSearchConfig", type = Accessor.Type.GETTER)
	public SolrFacetSearchConfigModel getSolrFacetSearchConfig()
	{
		return getPersistenceContext().getPropertyValue(SOLRFACETSEARCHCONFIG);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeIndexedTypeToSolrFacetSearchConfig.indexedType</code> attribute defined at extension <code>backofficesolrsearch</code>. 
	 *  
	 * @param value the indexedType
	 */
	@Accessor(qualifier = "indexedType", type = Accessor.Type.SETTER)
	public void setIndexedType(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(INDEXEDTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeIndexedTypeToSolrFacetSearchConfig.solrFacetSearchConfig</code> attribute defined at extension <code>backofficesolrsearch</code>. 
	 *  
	 * @param value the solrFacetSearchConfig
	 */
	@Accessor(qualifier = "solrFacetSearchConfig", type = Accessor.Type.SETTER)
	public void setSolrFacetSearchConfig(final SolrFacetSearchConfigModel value)
	{
		getPersistenceContext().setPropertyValue(SOLRFACETSEARCHCONFIG, value);
	}
	
}
