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
 * Generated model class for type ExternalImportKey first defined at extension impex.
 */
@SuppressWarnings("all")
public class ExternalImportKeyModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ExternalImportKey";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExternalImportKey.sourceSystemID</code> attribute defined at extension <code>impex</code>. */
	public static final String SOURCESYSTEMID = "sourceSystemID";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExternalImportKey.sourceKey</code> attribute defined at extension <code>impex</code>. */
	public static final String SOURCEKEY = "sourceKey";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExternalImportKey.targetPK</code> attribute defined at extension <code>impex</code>. */
	public static final String TARGETPK = "targetPK";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ExternalImportKeyModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ExternalImportKeyModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _sourceKey initial attribute declared by type <code>ExternalImportKey</code> at extension <code>impex</code>
	 * @param _sourceSystemID initial attribute declared by type <code>ExternalImportKey</code> at extension <code>impex</code>
	 * @param _targetPK initial attribute declared by type <code>ExternalImportKey</code> at extension <code>impex</code>
	 */
	@Deprecated
	public ExternalImportKeyModel(final String _sourceKey, final String _sourceSystemID, final PK _targetPK)
	{
		super();
		setSourceKey(_sourceKey);
		setSourceSystemID(_sourceSystemID);
		setTargetPK(_targetPK);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sourceKey initial attribute declared by type <code>ExternalImportKey</code> at extension <code>impex</code>
	 * @param _sourceSystemID initial attribute declared by type <code>ExternalImportKey</code> at extension <code>impex</code>
	 * @param _targetPK initial attribute declared by type <code>ExternalImportKey</code> at extension <code>impex</code>
	 */
	@Deprecated
	public ExternalImportKeyModel(final ItemModel _owner, final String _sourceKey, final String _sourceSystemID, final PK _targetPK)
	{
		super();
		setOwner(_owner);
		setSourceKey(_sourceKey);
		setSourceSystemID(_sourceSystemID);
		setTargetPK(_targetPK);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExternalImportKey.sourceKey</code> attribute defined at extension <code>impex</code>. 
	 * @return the sourceKey
	 */
	@Accessor(qualifier = "sourceKey", type = Accessor.Type.GETTER)
	public String getSourceKey()
	{
		return getPersistenceContext().getPropertyValue(SOURCEKEY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExternalImportKey.sourceSystemID</code> attribute defined at extension <code>impex</code>. 
	 * @return the sourceSystemID
	 */
	@Accessor(qualifier = "sourceSystemID", type = Accessor.Type.GETTER)
	public String getSourceSystemID()
	{
		return getPersistenceContext().getPropertyValue(SOURCESYSTEMID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExternalImportKey.targetPK</code> attribute defined at extension <code>impex</code>. 
	 * @return the targetPK
	 */
	@Accessor(qualifier = "targetPK", type = Accessor.Type.GETTER)
	public PK getTargetPK()
	{
		return getPersistenceContext().getPropertyValue(TARGETPK);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExternalImportKey.sourceKey</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the sourceKey
	 */
	@Accessor(qualifier = "sourceKey", type = Accessor.Type.SETTER)
	public void setSourceKey(final String value)
	{
		getPersistenceContext().setPropertyValue(SOURCEKEY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExternalImportKey.sourceSystemID</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the sourceSystemID
	 */
	@Accessor(qualifier = "sourceSystemID", type = Accessor.Type.SETTER)
	public void setSourceSystemID(final String value)
	{
		getPersistenceContext().setPropertyValue(SOURCESYSTEMID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExternalImportKey.targetPK</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the targetPK
	 */
	@Accessor(qualifier = "targetPK", type = Accessor.Type.SETTER)
	public void setTargetPK(final PK value)
	{
		getPersistenceContext().setPropertyValue(TARGETPK, value);
	}
	
}
