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
package de.hybris.platform.core.model.link;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type Link first defined at extension core.
 */
@SuppressWarnings("all")
public class LinkModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Link";
	
	/** <i>Generated constant</i> - Attribute key of <code>Link.language</code> attribute defined at extension <code>core</code>. */
	public static final String LANGUAGE = "language";
	
	/** <i>Generated constant</i> - Attribute key of <code>Link.qualifier</code> attribute defined at extension <code>core</code>. */
	public static final String QUALIFIER = "qualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>Link.source</code> attribute defined at extension <code>core</code>. */
	public static final String SOURCE = "source";
	
	/** <i>Generated constant</i> - Attribute key of <code>Link.target</code> attribute defined at extension <code>core</code>. */
	public static final String TARGET = "target";
	
	/** <i>Generated constant</i> - Attribute key of <code>Link.sequenceNumber</code> attribute defined at extension <code>core</code>. */
	public static final String SEQUENCENUMBER = "sequenceNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>Link.reverseSequenceNumber</code> attribute defined at extension <code>core</code>. */
	public static final String REVERSESEQUENCENUMBER = "reverseSequenceNumber";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public LinkModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public LinkModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _source initial attribute declared by type <code>Link</code> at extension <code>core</code>
	 * @param _target initial attribute declared by type <code>Link</code> at extension <code>core</code>
	 */
	@Deprecated
	public LinkModel(final ItemModel _source, final ItemModel _target)
	{
		super();
		setSource(_source);
		setTarget(_target);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _source initial attribute declared by type <code>Link</code> at extension <code>core</code>
	 * @param _target initial attribute declared by type <code>Link</code> at extension <code>core</code>
	 */
	@Deprecated
	public LinkModel(final ItemModel _owner, final ItemModel _source, final ItemModel _target)
	{
		super();
		setOwner(_owner);
		setSource(_source);
		setTarget(_target);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Link.language</code> attribute defined at extension <code>core</code>. 
	 * @return the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public LanguageModel getLanguage()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Link.qualifier</code> attribute defined at extension <code>core</code>. 
	 * @return the qualifier
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
	public String getQualifier()
	{
		return getPersistenceContext().getPropertyValue(QUALIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Link.reverseSequenceNumber</code> attribute defined at extension <code>core</code>. 
	 * @return the reverseSequenceNumber
	 */
	@Accessor(qualifier = "reverseSequenceNumber", type = Accessor.Type.GETTER)
	public Integer getReverseSequenceNumber()
	{
		return getPersistenceContext().getPropertyValue(REVERSESEQUENCENUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Link.sequenceNumber</code> attribute defined at extension <code>core</code>. 
	 * @return the sequenceNumber
	 */
	@Accessor(qualifier = "sequenceNumber", type = Accessor.Type.GETTER)
	public Integer getSequenceNumber()
	{
		return getPersistenceContext().getPropertyValue(SEQUENCENUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Link.source</code> attribute defined at extension <code>core</code>. 
	 * @return the source
	 */
	@Accessor(qualifier = "source", type = Accessor.Type.GETTER)
	public ItemModel getSource()
	{
		return getPersistenceContext().getPropertyValue(SOURCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Link.target</code> attribute defined at extension <code>core</code>. 
	 * @return the target
	 */
	@Accessor(qualifier = "target", type = Accessor.Type.GETTER)
	public ItemModel getTarget()
	{
		return getPersistenceContext().getPropertyValue(TARGET);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Link.language</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Link.qualifier</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the qualifier
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
	public void setQualifier(final String value)
	{
		getPersistenceContext().setPropertyValue(QUALIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Link.reverseSequenceNumber</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the reverseSequenceNumber
	 */
	@Accessor(qualifier = "reverseSequenceNumber", type = Accessor.Type.SETTER)
	public void setReverseSequenceNumber(final Integer value)
	{
		getPersistenceContext().setPropertyValue(REVERSESEQUENCENUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Link.sequenceNumber</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the sequenceNumber
	 */
	@Accessor(qualifier = "sequenceNumber", type = Accessor.Type.SETTER)
	public void setSequenceNumber(final Integer value)
	{
		getPersistenceContext().setPropertyValue(SEQUENCENUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Link.source</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the source
	 */
	@Accessor(qualifier = "source", type = Accessor.Type.SETTER)
	public void setSource(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(SOURCE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Link.target</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the target
	 */
	@Accessor(qualifier = "target", type = Accessor.Type.SETTER)
	public void setTarget(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(TARGET, value);
	}
	
}
