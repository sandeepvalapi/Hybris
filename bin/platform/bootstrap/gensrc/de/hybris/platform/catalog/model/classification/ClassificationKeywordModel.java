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
package de.hybris.platform.catalog.model.classification;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ClassificationKeyword first defined at extension catalog.
 */
@SuppressWarnings("all")
public class ClassificationKeywordModel extends KeywordModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ClassificationKeyword";
	
	/** <i>Generated constant</i> - Attribute key of <code>ClassificationKeyword.externalID</code> attribute defined at extension <code>catalog</code>. */
	public static final String EXTERNALID = "externalID";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ClassificationKeywordModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ClassificationKeywordModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>ClassificationKeyword</code> at extension <code>catalog</code>
	 * @param _keyword initial attribute declared by type <code>Keyword</code> at extension <code>catalog</code>
	 * @param _language initial attribute declared by type <code>Keyword</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ClassificationKeywordModel(final ClassificationSystemVersionModel _catalogVersion, final String _keyword, final LanguageModel _language)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setKeyword(_keyword);
		setLanguage(_language);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>ClassificationKeyword</code> at extension <code>catalog</code>
	 * @param _keyword initial attribute declared by type <code>Keyword</code> at extension <code>catalog</code>
	 * @param _language initial attribute declared by type <code>Keyword</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ClassificationKeywordModel(final ClassificationSystemVersionModel _catalogVersion, final String _keyword, final LanguageModel _language, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setKeyword(_keyword);
		setLanguage(_language);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Keyword.catalogVersion</code> attribute defined at extension <code>catalog</code> and redeclared at extension <code>catalog</code>. 
	 * @return the catalogVersion
	 */
	@Override
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public ClassificationSystemVersionModel getCatalogVersion()
	{
		return (ClassificationSystemVersionModel) super.getCatalogVersion();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ClassificationKeyword.externalID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the externalID
	 */
	@Accessor(qualifier = "externalID", type = Accessor.Type.GETTER)
	public String getExternalID()
	{
		return getPersistenceContext().getPropertyValue(EXTERNALID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Keyword.catalogVersion</code> attribute defined at extension <code>catalog</code> and redeclared at extension <code>catalog</code>. Will only accept values of type {@link de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel}. 
	 *  
	 * @param value the catalogVersion
	 */
	@Override
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		if( value == null || value instanceof ClassificationSystemVersionModel)
		{
			super.setCatalogVersion(value);
		}
		else
		{
			throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel");
		}
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ClassificationKeyword.externalID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the externalID
	 */
	@Accessor(qualifier = "externalID", type = Accessor.Type.SETTER)
	public void setExternalID(final String value)
	{
		getPersistenceContext().setPropertyValue(EXTERNALID, value);
	}
	
}
