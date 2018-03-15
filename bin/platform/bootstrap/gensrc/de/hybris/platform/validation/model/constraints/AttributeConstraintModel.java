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
package de.hybris.platform.validation.model.constraints;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.AbstractConstraintModel;
import java.util.Set;

/**
 * Generated model class for type AttributeConstraint first defined at extension validation.
 * <p>
 * Attribute constraint definition.
 */
@SuppressWarnings("all")
public class AttributeConstraintModel extends AbstractConstraintModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AttributeConstraint";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeConstraint.qualifier</code> attribute defined at extension <code>validation</code>. */
	public static final String QUALIFIER = "qualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeConstraint.languages</code> attribute defined at extension <code>validation</code>. */
	public static final String LANGUAGES = "languages";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeConstraint.descriptor</code> attribute defined at extension <code>validation</code>. */
	public static final String DESCRIPTOR = "descriptor";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AttributeConstraintModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AttributeConstraintModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _annotation initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 */
	@Deprecated
	public AttributeConstraintModel(final Class _annotation, final String _id)
	{
		super();
		setAnnotation(_annotation);
		setId(_id);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _annotation initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AttributeConstraintModel(final Class _annotation, final String _id, final ItemModel _owner)
	{
		super();
		setAnnotation(_annotation);
		setId(_id);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeConstraint.descriptor</code> attribute defined at extension <code>validation</code>. 
	 * @return the descriptor
	 */
	@Accessor(qualifier = "descriptor", type = Accessor.Type.GETTER)
	public AttributeDescriptorModel getDescriptor()
	{
		return getPersistenceContext().getPropertyValue(DESCRIPTOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeConstraint.languages</code> attribute defined at extension <code>validation</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the languages - Languages for which constraint will be enforced
	 */
	@Accessor(qualifier = "languages", type = Accessor.Type.GETTER)
	public Set<LanguageModel> getLanguages()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeConstraint.qualifier</code> attribute defined at extension <code>validation</code>. 
	 * @return the qualifier - Qualifier name for attribute descriptor
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
	public String getQualifier()
	{
		return getPersistenceContext().getPropertyValue(QUALIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeConstraint.descriptor</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the descriptor
	 */
	@Accessor(qualifier = "descriptor", type = Accessor.Type.SETTER)
	public void setDescriptor(final AttributeDescriptorModel value)
	{
		getPersistenceContext().setPropertyValue(DESCRIPTOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeConstraint.languages</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the languages - Languages for which constraint will be enforced
	 */
	@Accessor(qualifier = "languages", type = Accessor.Type.SETTER)
	public void setLanguages(final Set<LanguageModel> value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeConstraint.qualifier</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the qualifier - Qualifier name for attribute descriptor
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
	public void setQualifier(final String value)
	{
		getPersistenceContext().setPropertyValue(QUALIFIER, value);
	}
	
}
