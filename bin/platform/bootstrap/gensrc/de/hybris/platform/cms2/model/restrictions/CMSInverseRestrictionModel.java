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
package de.hybris.platform.cms2.model.restrictions;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CMSInverseRestriction first defined at extension cms2.
 */
@SuppressWarnings("all")
public class CMSInverseRestrictionModel extends AbstractRestrictionModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CMSInverseRestriction";
	
	/**<i>Generated relation code constant for relation <code>AbstractRestriction2CMSInverseRestriction</code> defining source attribute <code>originalRestriction</code> in extension <code>cms2</code>.</i>*/
	public static final String _ABSTRACTRESTRICTION2CMSINVERSERESTRICTION = "AbstractRestriction2CMSInverseRestriction";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSInverseRestriction.originalRestriction</code> attribute defined at extension <code>cms2</code>. */
	public static final String ORIGINALRESTRICTION = "originalRestriction";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CMSInverseRestrictionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CMSInverseRestrictionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _originalRestriction initial attribute declared by type <code>CMSInverseRestriction</code> at extension <code>cms2</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public CMSInverseRestrictionModel(final CatalogVersionModel _catalogVersion, final AbstractRestrictionModel _originalRestriction, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOriginalRestriction(_originalRestriction);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _originalRestriction initial attribute declared by type <code>CMSInverseRestriction</code> at extension <code>cms2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public CMSInverseRestrictionModel(final CatalogVersionModel _catalogVersion, final AbstractRestrictionModel _originalRestriction, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setOriginalRestriction(_originalRestriction);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSInverseRestriction.originalRestriction</code> attribute defined at extension <code>cms2</code>. 
	 * @return the originalRestriction
	 */
	@Accessor(qualifier = "originalRestriction", type = Accessor.Type.GETTER)
	public AbstractRestrictionModel getOriginalRestriction()
	{
		return getPersistenceContext().getPropertyValue(ORIGINALRESTRICTION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSInverseRestriction.originalRestriction</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the originalRestriction
	 */
	@Accessor(qualifier = "originalRestriction", type = Accessor.Type.SETTER)
	public void setOriginalRestriction(final AbstractRestrictionModel value)
	{
		getPersistenceContext().setPropertyValue(ORIGINALRESTRICTION, value);
	}
	
}
