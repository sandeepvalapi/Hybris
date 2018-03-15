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
package de.hybris.platform.impex.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ImpexDocumentId first defined at extension impex.
 */
@SuppressWarnings("all")
public class ImpexDocumentIdModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ImpexDocumentId";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpexDocumentId.processCode</code> attribute defined at extension <code>impex</code>. */
	public static final String PROCESSCODE = "processCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpexDocumentId.docId</code> attribute defined at extension <code>impex</code>. */
	public static final String DOCID = "docId";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpexDocumentId.itemQualifier</code> attribute defined at extension <code>impex</code>. */
	public static final String ITEMQUALIFIER = "itemQualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpexDocumentId.itemPK</code> attribute defined at extension <code>impex</code>. */
	public static final String ITEMPK = "itemPK";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpexDocumentId.resolved</code> attribute defined at extension <code>impex</code>. */
	public static final String RESOLVED = "resolved";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ImpexDocumentIdModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ImpexDocumentIdModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _docId initial attribute declared by type <code>ImpexDocumentId</code> at extension <code>impex</code>
	 * @param _itemPK initial attribute declared by type <code>ImpexDocumentId</code> at extension <code>impex</code>
	 * @param _itemQualifier initial attribute declared by type <code>ImpexDocumentId</code> at extension <code>impex</code>
	 * @param _resolved initial attribute declared by type <code>ImpexDocumentId</code> at extension <code>impex</code>
	 */
	@Deprecated
	public ImpexDocumentIdModel(final String _docId, final PK _itemPK, final String _itemQualifier, final Boolean _resolved)
	{
		super();
		setDocId(_docId);
		setItemPK(_itemPK);
		setItemQualifier(_itemQualifier);
		setResolved(_resolved);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _docId initial attribute declared by type <code>ImpexDocumentId</code> at extension <code>impex</code>
	 * @param _itemPK initial attribute declared by type <code>ImpexDocumentId</code> at extension <code>impex</code>
	 * @param _itemQualifier initial attribute declared by type <code>ImpexDocumentId</code> at extension <code>impex</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _resolved initial attribute declared by type <code>ImpexDocumentId</code> at extension <code>impex</code>
	 */
	@Deprecated
	public ImpexDocumentIdModel(final String _docId, final PK _itemPK, final String _itemQualifier, final ItemModel _owner, final Boolean _resolved)
	{
		super();
		setDocId(_docId);
		setItemPK(_itemPK);
		setItemQualifier(_itemQualifier);
		setOwner(_owner);
		setResolved(_resolved);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpexDocumentId.docId</code> attribute defined at extension <code>impex</code>. 
	 * @return the docId
	 */
	@Accessor(qualifier = "docId", type = Accessor.Type.GETTER)
	public String getDocId()
	{
		return getPersistenceContext().getPropertyValue(DOCID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpexDocumentId.itemPK</code> attribute defined at extension <code>impex</code>. 
	 * @return the itemPK
	 */
	@Accessor(qualifier = "itemPK", type = Accessor.Type.GETTER)
	public PK getItemPK()
	{
		return getPersistenceContext().getPropertyValue(ITEMPK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpexDocumentId.itemQualifier</code> attribute defined at extension <code>impex</code>. 
	 * @return the itemQualifier
	 */
	@Accessor(qualifier = "itemQualifier", type = Accessor.Type.GETTER)
	public String getItemQualifier()
	{
		return getPersistenceContext().getPropertyValue(ITEMQUALIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpexDocumentId.processCode</code> attribute defined at extension <code>impex</code>. 
	 * @return the processCode
	 */
	@Accessor(qualifier = "processCode", type = Accessor.Type.GETTER)
	public String getProcessCode()
	{
		return getPersistenceContext().getPropertyValue(PROCESSCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpexDocumentId.resolved</code> attribute defined at extension <code>impex</code>. 
	 * @return the resolved
	 */
	@Accessor(qualifier = "resolved", type = Accessor.Type.GETTER)
	public Boolean getResolved()
	{
		return getPersistenceContext().getPropertyValue(RESOLVED);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpexDocumentId.docId</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the docId
	 */
	@Accessor(qualifier = "docId", type = Accessor.Type.SETTER)
	public void setDocId(final String value)
	{
		getPersistenceContext().setPropertyValue(DOCID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpexDocumentId.itemPK</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the itemPK
	 */
	@Accessor(qualifier = "itemPK", type = Accessor.Type.SETTER)
	public void setItemPK(final PK value)
	{
		getPersistenceContext().setPropertyValue(ITEMPK, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpexDocumentId.itemQualifier</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the itemQualifier
	 */
	@Accessor(qualifier = "itemQualifier", type = Accessor.Type.SETTER)
	public void setItemQualifier(final String value)
	{
		getPersistenceContext().setPropertyValue(ITEMQUALIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpexDocumentId.processCode</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the processCode
	 */
	@Accessor(qualifier = "processCode", type = Accessor.Type.SETTER)
	public void setProcessCode(final String value)
	{
		getPersistenceContext().setPropertyValue(PROCESSCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpexDocumentId.resolved</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the resolved
	 */
	@Accessor(qualifier = "resolved", type = Accessor.Type.SETTER)
	public void setResolved(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(RESOLVED, value);
	}
	
}
