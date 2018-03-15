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
package de.hybris.platform.commons.model.translator;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commons.model.translator.JaloTranslatorConfigurationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ParserProperty first defined at extension commons.
 */
@SuppressWarnings("all")
public class ParserPropertyModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ParserProperty";
	
	/**<i>Generated relation code constant for relation <code>TranslatorConfig2ParserProperties</code> defining source attribute <code>translatorConfiguration</code> in extension <code>commons</code>.</i>*/
	public static final String _TRANSLATORCONFIG2PARSERPROPERTIES = "TranslatorConfig2ParserProperties";
	
	/** <i>Generated constant</i> - Attribute key of <code>ParserProperty.name</code> attribute defined at extension <code>commons</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>ParserProperty.startExp</code> attribute defined at extension <code>commons</code>. */
	public static final String STARTEXP = "startExp";
	
	/** <i>Generated constant</i> - Attribute key of <code>ParserProperty.endExp</code> attribute defined at extension <code>commons</code>. */
	public static final String ENDEXP = "endExp";
	
	/** <i>Generated constant</i> - Attribute key of <code>ParserProperty.parserClass</code> attribute defined at extension <code>commons</code>. */
	public static final String PARSERCLASS = "parserClass";
	
	/** <i>Generated constant</i> - Attribute key of <code>ParserProperty.translatorConfigurationPOS</code> attribute defined at extension <code>commons</code>. */
	public static final String TRANSLATORCONFIGURATIONPOS = "translatorConfigurationPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>ParserProperty.translatorConfiguration</code> attribute defined at extension <code>commons</code>. */
	public static final String TRANSLATORCONFIGURATION = "translatorConfiguration";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ParserPropertyModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ParserPropertyModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>ParserProperty</code> at extension <code>commons</code>
	 * @param _startExp initial attribute declared by type <code>ParserProperty</code> at extension <code>commons</code>
	 */
	@Deprecated
	public ParserPropertyModel(final String _name, final String _startExp)
	{
		super();
		setName(_name);
		setStartExp(_startExp);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>ParserProperty</code> at extension <code>commons</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _startExp initial attribute declared by type <code>ParserProperty</code> at extension <code>commons</code>
	 */
	@Deprecated
	public ParserPropertyModel(final String _name, final ItemModel _owner, final String _startExp)
	{
		super();
		setName(_name);
		setOwner(_owner);
		setStartExp(_startExp);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ParserProperty.endExp</code> attribute defined at extension <code>commons</code>. 
	 * @return the endExp - End expression of the tag/element
	 */
	@Accessor(qualifier = "endExp", type = Accessor.Type.GETTER)
	public String getEndExp()
	{
		return getPersistenceContext().getPropertyValue(ENDEXP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ParserProperty.name</code> attribute defined at extension <code>commons</code>. 
	 * @return the name - Name of the tag/element
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ParserProperty.parserClass</code> attribute defined at extension <code>commons</code>. 
	 * @return the parserClass - Special java class that parses the related tag/element
	 */
	@Accessor(qualifier = "parserClass", type = Accessor.Type.GETTER)
	public String getParserClass()
	{
		return getPersistenceContext().getPropertyValue(PARSERCLASS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ParserProperty.startExp</code> attribute defined at extension <code>commons</code>. 
	 * @return the startExp - Start expression of the tag/element
	 */
	@Accessor(qualifier = "startExp", type = Accessor.Type.GETTER)
	public String getStartExp()
	{
		return getPersistenceContext().getPropertyValue(STARTEXP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ParserProperty.translatorConfiguration</code> attribute defined at extension <code>commons</code>. 
	 * @return the translatorConfiguration
	 */
	@Accessor(qualifier = "translatorConfiguration", type = Accessor.Type.GETTER)
	public JaloTranslatorConfigurationModel getTranslatorConfiguration()
	{
		return getPersistenceContext().getPropertyValue(TRANSLATORCONFIGURATION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ParserProperty.endExp</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the endExp - End expression of the tag/element
	 */
	@Accessor(qualifier = "endExp", type = Accessor.Type.SETTER)
	public void setEndExp(final String value)
	{
		getPersistenceContext().setPropertyValue(ENDEXP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ParserProperty.name</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the name - Name of the tag/element
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ParserProperty.parserClass</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the parserClass - Special java class that parses the related tag/element
	 */
	@Accessor(qualifier = "parserClass", type = Accessor.Type.SETTER)
	public void setParserClass(final String value)
	{
		getPersistenceContext().setPropertyValue(PARSERCLASS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ParserProperty.startExp</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the startExp - Start expression of the tag/element
	 */
	@Accessor(qualifier = "startExp", type = Accessor.Type.SETTER)
	public void setStartExp(final String value)
	{
		getPersistenceContext().setPropertyValue(STARTEXP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ParserProperty.translatorConfiguration</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the translatorConfiguration
	 */
	@Accessor(qualifier = "translatorConfiguration", type = Accessor.Type.SETTER)
	public void setTranslatorConfiguration(final JaloTranslatorConfigurationModel value)
	{
		getPersistenceContext().setPropertyValue(TRANSLATORCONFIGURATION, value);
	}
	
}
