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
package de.hybris.platform.commons.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commons.enums.DocumentTypeEnum;
import de.hybris.platform.commons.model.ItemFormatterModel;
import de.hybris.platform.commons.model.MediaFormatterModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type Format first defined at extension commons.
 */
@SuppressWarnings("all")
public class FormatModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Format";
	
	/** <i>Generated constant</i> - Attribute key of <code>Format.code</code> attribute defined at extension <code>commons</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Format.name</code> attribute defined at extension <code>commons</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>Format.initial</code> attribute defined at extension <code>commons</code>. */
	public static final String INITIAL = "initial";
	
	/** <i>Generated constant</i> - Attribute key of <code>Format.documentType</code> attribute defined at extension <code>commons</code>. */
	public static final String DOCUMENTTYPE = "documentType";
	
	/** <i>Generated constant</i> - Attribute key of <code>Format.chained</code> attribute defined at extension <code>commons</code>. */
	public static final String CHAINED = "chained";
	
	/** <i>Generated constant</i> - Attribute key of <code>Format.validFor</code> attribute defined at extension <code>commons</code>. */
	public static final String VALIDFOR = "validFor";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public FormatModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public FormatModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Format</code> at extension <code>commons</code>
	 * @param _documentType initial attribute declared by type <code>Format</code> at extension <code>commons</code>
	 * @param _initial initial attribute declared by type <code>Format</code> at extension <code>commons</code>
	 */
	@Deprecated
	public FormatModel(final String _code, final DocumentTypeEnum _documentType, final ItemFormatterModel _initial)
	{
		super();
		setCode(_code);
		setDocumentType(_documentType);
		setInitial(_initial);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Format</code> at extension <code>commons</code>
	 * @param _documentType initial attribute declared by type <code>Format</code> at extension <code>commons</code>
	 * @param _initial initial attribute declared by type <code>Format</code> at extension <code>commons</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public FormatModel(final String _code, final DocumentTypeEnum _documentType, final ItemFormatterModel _initial, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setDocumentType(_documentType);
		setInitial(_initial);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Format.chained</code> attribute defined at extension <code>commons</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the chained
	 */
	@Accessor(qualifier = "chained", type = Accessor.Type.GETTER)
	public Collection<MediaFormatterModel> getChained()
	{
		return getPersistenceContext().getPropertyValue(CHAINED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Format.code</code> attribute defined at extension <code>commons</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Format.documentType</code> attribute defined at extension <code>commons</code>. 
	 * @return the documentType
	 */
	@Accessor(qualifier = "documentType", type = Accessor.Type.GETTER)
	public DocumentTypeEnum getDocumentType()
	{
		return getPersistenceContext().getPropertyValue(DOCUMENTTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Format.initial</code> attribute defined at extension <code>commons</code>. 
	 * @return the initial
	 */
	@Accessor(qualifier = "initial", type = Accessor.Type.GETTER)
	public ItemFormatterModel getInitial()
	{
		return getPersistenceContext().getPropertyValue(INITIAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Format.name</code> attribute defined at extension <code>commons</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Format.name</code> attribute defined at extension <code>commons</code>. 
	 * @param loc the value localization key 
	 * @return the name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Format.validFor</code> attribute defined at extension <code>commons</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the validFor
	 */
	@Accessor(qualifier = "validFor", type = Accessor.Type.GETTER)
	public Collection<ComposedTypeModel> getValidFor()
	{
		return getPersistenceContext().getPropertyValue(VALIDFOR);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Format.chained</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the chained
	 */
	@Accessor(qualifier = "chained", type = Accessor.Type.SETTER)
	public void setChained(final Collection<MediaFormatterModel> value)
	{
		getPersistenceContext().setPropertyValue(CHAINED, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>Format.code</code> attribute defined at extension <code>commons</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Format.documentType</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the documentType
	 */
	@Accessor(qualifier = "documentType", type = Accessor.Type.SETTER)
	public void setDocumentType(final DocumentTypeEnum value)
	{
		getPersistenceContext().setPropertyValue(DOCUMENTTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Format.initial</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the initial
	 */
	@Accessor(qualifier = "initial", type = Accessor.Type.SETTER)
	public void setInitial(final ItemFormatterModel value)
	{
		getPersistenceContext().setPropertyValue(INITIAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Format.name</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Format.name</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Format.validFor</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the validFor
	 */
	@Accessor(qualifier = "validFor", type = Accessor.Type.SETTER)
	public void setValidFor(final Collection<ComposedTypeModel> value)
	{
		getPersistenceContext().setPropertyValue(VALIDFOR, value);
	}
	
}
