/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Mar 15, 2018 5:02:29 PM                     ---
 * ----------------------------------------------------------------
 */
package com.hybris.backoffice.solrsearch.model;

import com.hybris.backoffice.solrsearch.enums.SolrItemModificationType;
import com.hybris.backoffice.solrsearch.model.BackofficeSolrIndexerCronJobModel;
import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SolrModifiedItem first defined at extension backofficesolrsearch.
 */
@SuppressWarnings("all")
public class SolrModifiedItemModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SolrModifiedItem";
	
	/**<i>Generated relation code constant for relation <code>BackofficeIndexerCronJob2RemovedItemRelation</code> defining source attribute <code>parent</code> in extension <code>backofficesolrsearch</code>.</i>*/
	public static final String _BACKOFFICEINDEXERCRONJOB2REMOVEDITEMRELATION = "BackofficeIndexerCronJob2RemovedItemRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrModifiedItem.modifiedTypeCode</code> attribute defined at extension <code>backofficesolrsearch</code>. */
	public static final String MODIFIEDTYPECODE = "modifiedTypeCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrModifiedItem.modifiedPk</code> attribute defined at extension <code>backofficesolrsearch</code>. */
	public static final String MODIFIEDPK = "modifiedPk";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrModifiedItem.modificationType</code> attribute defined at extension <code>backofficesolrsearch</code>. */
	public static final String MODIFICATIONTYPE = "modificationType";
	
	/** <i>Generated constant</i> - Attribute key of <code>SolrModifiedItem.parent</code> attribute defined at extension <code>backofficesolrsearch</code>. */
	public static final String PARENT = "parent";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SolrModifiedItemModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SolrModifiedItemModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public SolrModifiedItemModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrModifiedItem.modificationType</code> attribute defined at extension <code>backofficesolrsearch</code>. 
	 * @return the modificationType
	 */
	@Accessor(qualifier = "modificationType", type = Accessor.Type.GETTER)
	public SolrItemModificationType getModificationType()
	{
		return getPersistenceContext().getPropertyValue(MODIFICATIONTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrModifiedItem.modifiedPk</code> attribute defined at extension <code>backofficesolrsearch</code>. 
	 * @return the modifiedPk
	 */
	@Accessor(qualifier = "modifiedPk", type = Accessor.Type.GETTER)
	public Long getModifiedPk()
	{
		return getPersistenceContext().getPropertyValue(MODIFIEDPK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrModifiedItem.modifiedTypeCode</code> attribute defined at extension <code>backofficesolrsearch</code>. 
	 * @return the modifiedTypeCode
	 */
	@Accessor(qualifier = "modifiedTypeCode", type = Accessor.Type.GETTER)
	public String getModifiedTypeCode()
	{
		return getPersistenceContext().getPropertyValue(MODIFIEDTYPECODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrModifiedItem.parent</code> attribute defined at extension <code>backofficesolrsearch</code>. 
	 * @return the parent
	 */
	@Accessor(qualifier = "parent", type = Accessor.Type.GETTER)
	public BackofficeSolrIndexerCronJobModel getParent()
	{
		return getPersistenceContext().getPropertyValue(PARENT);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrModifiedItem.modificationType</code> attribute defined at extension <code>backofficesolrsearch</code>. 
	 *  
	 * @param value the modificationType
	 */
	@Accessor(qualifier = "modificationType", type = Accessor.Type.SETTER)
	public void setModificationType(final SolrItemModificationType value)
	{
		getPersistenceContext().setPropertyValue(MODIFICATIONTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrModifiedItem.modifiedPk</code> attribute defined at extension <code>backofficesolrsearch</code>. 
	 *  
	 * @param value the modifiedPk
	 */
	@Accessor(qualifier = "modifiedPk", type = Accessor.Type.SETTER)
	public void setModifiedPk(final Long value)
	{
		getPersistenceContext().setPropertyValue(MODIFIEDPK, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrModifiedItem.modifiedTypeCode</code> attribute defined at extension <code>backofficesolrsearch</code>. 
	 *  
	 * @param value the modifiedTypeCode
	 */
	@Accessor(qualifier = "modifiedTypeCode", type = Accessor.Type.SETTER)
	public void setModifiedTypeCode(final String value)
	{
		getPersistenceContext().setPropertyValue(MODIFIEDTYPECODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SolrModifiedItem.parent</code> attribute defined at extension <code>backofficesolrsearch</code>. 
	 *  
	 * @param value the parent
	 */
	@Accessor(qualifier = "parent", type = Accessor.Type.SETTER)
	public void setParent(final BackofficeSolrIndexerCronJobModel value)
	{
		getPersistenceContext().setPropertyValue(PARENT, value);
	}
	
}
