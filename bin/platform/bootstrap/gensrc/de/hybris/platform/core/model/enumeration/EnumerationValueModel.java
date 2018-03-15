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
package de.hybris.platform.core.model.enumeration;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type EnumerationValue first defined at extension core.
 */
@SuppressWarnings("all")
public class EnumerationValueModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "EnumerationValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>EnumerationValue.code</code> attribute defined at extension <code>core</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>EnumerationValue.codeLowerCase</code> attribute defined at extension <code>core</code>. */
	public static final String CODELOWERCASE = "codeLowerCase";
	
	/** <i>Generated constant</i> - Attribute key of <code>EnumerationValue.name</code> attribute defined at extension <code>core</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>EnumerationValue.sequenceNumber</code> attribute defined at extension <code>core</code>. */
	public static final String SEQUENCENUMBER = "sequenceNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>EnumerationValue.deprecated</code> attribute defined at extension <code>core</code>. */
	public static final String DEPRECATED = "deprecated";
	
	/** <i>Generated constant</i> - Attribute key of <code>EnumerationValue.extensionName</code> attribute defined at extension <code>core</code>. */
	public static final String EXTENSIONNAME = "extensionName";
	
	/** <i>Generated constant</i> - Attribute key of <code>EnumerationValue.icon</code> attribute defined at extension <code>core</code>. */
	public static final String ICON = "icon";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public EnumerationValueModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public EnumerationValueModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>EnumerationValue</code> at extension <code>core</code>
	 */
	@Deprecated
	public EnumerationValueModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>EnumerationValue</code> at extension <code>core</code>
	 * @param _codeLowerCase initial attribute declared by type <code>EnumerationValue</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public EnumerationValueModel(final String _code, final String _codeLowerCase, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setCodeLowerCase(_codeLowerCase);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EnumerationValue.code</code> attribute defined at extension <code>core</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		return getPersistenceContext().getPropertyValue(CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EnumerationValue.codeLowerCase</code> attribute defined at extension <code>core</code>. 
	 * @return the codeLowerCase
	 */
	@Accessor(qualifier = "codeLowerCase", type = Accessor.Type.GETTER)
	public String getCodeLowerCase()
	{
		return getPersistenceContext().getPropertyValue(CODELOWERCASE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EnumerationValue.deprecated</code> attribute defined at extension <code>core</code>. 
	 * @return the deprecated
	 */
	@Accessor(qualifier = "deprecated", type = Accessor.Type.GETTER)
	public Boolean getDeprecated()
	{
		return getPersistenceContext().getPropertyValue(DEPRECATED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EnumerationValue.extensionName</code> attribute defined at extension <code>core</code>. 
	 * @return the extensionName
	 */
	@Accessor(qualifier = "extensionName", type = Accessor.Type.GETTER)
	public String getExtensionName()
	{
		return getPersistenceContext().getPropertyValue(EXTENSIONNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EnumerationValue.icon</code> attribute defined at extension <code>core</code>. 
	 * @return the icon
	 */
	@Accessor(qualifier = "icon", type = Accessor.Type.GETTER)
	public MediaModel getIcon()
	{
		return getPersistenceContext().getPropertyValue(ICON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>EnumerationValue.name</code> attribute defined at extension <code>core</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>EnumerationValue.name</code> attribute defined at extension <code>core</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>EnumerationValue.sequenceNumber</code> attribute defined at extension <code>core</code>. 
	 * @return the sequenceNumber
	 */
	@Accessor(qualifier = "sequenceNumber", type = Accessor.Type.GETTER)
	public Integer getSequenceNumber()
	{
		return getPersistenceContext().getPropertyValue(SEQUENCENUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>EnumerationValue.code</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		getPersistenceContext().setPropertyValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>EnumerationValue.codeLowerCase</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the codeLowerCase
	 */
	@Accessor(qualifier = "codeLowerCase", type = Accessor.Type.SETTER)
	public void setCodeLowerCase(final String value)
	{
		getPersistenceContext().setPropertyValue(CODELOWERCASE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EnumerationValue.extensionName</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the extensionName
	 */
	@Accessor(qualifier = "extensionName", type = Accessor.Type.SETTER)
	public void setExtensionName(final String value)
	{
		getPersistenceContext().setPropertyValue(EXTENSIONNAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EnumerationValue.icon</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the icon
	 */
	@Accessor(qualifier = "icon", type = Accessor.Type.SETTER)
	public void setIcon(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(ICON, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>EnumerationValue.name</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>EnumerationValue.name</code> attribute defined at extension <code>core</code>. 
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
	 * <i>Generated method</i> - Setter of <code>EnumerationValue.sequenceNumber</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the sequenceNumber
	 */
	@Accessor(qualifier = "sequenceNumber", type = Accessor.Type.SETTER)
	public void setSequenceNumber(final Integer value)
	{
		getPersistenceContext().setPropertyValue(SEQUENCENUMBER, value);
	}
	
}
