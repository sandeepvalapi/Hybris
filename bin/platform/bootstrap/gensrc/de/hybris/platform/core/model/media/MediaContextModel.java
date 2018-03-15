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
package de.hybris.platform.core.model.media;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaFormatMappingModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type MediaContext first defined at extension core.
 */
@SuppressWarnings("all")
public class MediaContextModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MediaContext";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaContext.qualifier</code> attribute defined at extension <code>core</code>. */
	public static final String QUALIFIER = "qualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaContext.name</code> attribute defined at extension <code>core</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaContext.mappings</code> attribute defined at extension <code>core</code>. */
	public static final String MAPPINGS = "mappings";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MediaContextModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MediaContextModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _qualifier initial attribute declared by type <code>MediaContext</code> at extension <code>core</code>
	 */
	@Deprecated
	public MediaContextModel(final String _qualifier)
	{
		super();
		setQualifier(_qualifier);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>MediaContext</code> at extension <code>core</code>
	 */
	@Deprecated
	public MediaContextModel(final ItemModel _owner, final String _qualifier)
	{
		super();
		setOwner(_owner);
		setQualifier(_qualifier);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MediaContext.mappings</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the mappings
	 */
	@Accessor(qualifier = "mappings", type = Accessor.Type.GETTER)
	public Collection<MediaFormatMappingModel> getMappings()
	{
		return getPersistenceContext().getPropertyValue(MAPPINGS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MediaContext.name</code> attribute defined at extension <code>core</code>. 
	 * @return the name - Name of this context
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>MediaContext.name</code> attribute defined at extension <code>core</code>. 
	 * @param loc the value localization key 
	 * @return the name - Name of this context
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MediaContext.qualifier</code> attribute defined at extension <code>core</code>. 
	 * @return the qualifier - Qualifying name of this context
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
	public String getQualifier()
	{
		return getPersistenceContext().getPropertyValue(QUALIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MediaContext.mappings</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the mappings
	 */
	@Accessor(qualifier = "mappings", type = Accessor.Type.SETTER)
	public void setMappings(final Collection<MediaFormatMappingModel> value)
	{
		getPersistenceContext().setPropertyValue(MAPPINGS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MediaContext.name</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the name - Name of this context
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>MediaContext.name</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the name - Name of this context
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MediaContext.qualifier</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the qualifier - Qualifying name of this context
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
	public void setQualifier(final String value)
	{
		getPersistenceContext().setPropertyValue(QUALIFIER, value);
	}
	
}
