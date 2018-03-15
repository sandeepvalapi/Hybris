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
package de.hybris.platform.validation.model.constraints.jsr303;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.enums.RegexpFlag;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;
import java.util.Set;

/**
 * Generated model class for type PatternConstraint first defined at extension validation.
 * <p>
 * Pattern JSR 303 compatible constraint class.
 */
@SuppressWarnings("all")
public class PatternConstraintModel extends AttributeConstraintModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "PatternConstraint";
	
	/** <i>Generated constant</i> - Attribute key of <code>PatternConstraint.regexp</code> attribute defined at extension <code>validation</code>. */
	public static final String REGEXP = "regexp";
	
	/** <i>Generated constant</i> - Attribute key of <code>PatternConstraint.flags</code> attribute defined at extension <code>validation</code>. */
	public static final String FLAGS = "flags";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PatternConstraintModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PatternConstraintModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _annotation initial attribute declared by type <code>PatternConstraint</code> at extension <code>validation</code>
	 * @param _flags initial attribute declared by type <code>PatternConstraint</code> at extension <code>validation</code>
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _regexp initial attribute declared by type <code>PatternConstraint</code> at extension <code>validation</code>
	 */
	@Deprecated
	public PatternConstraintModel(final Class _annotation, final Set<RegexpFlag> _flags, final String _id, final String _regexp)
	{
		super();
		setAnnotation(_annotation);
		setFlags(_flags);
		setId(_id);
		setRegexp(_regexp);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _annotation initial attribute declared by type <code>PatternConstraint</code> at extension <code>validation</code>
	 * @param _flags initial attribute declared by type <code>PatternConstraint</code> at extension <code>validation</code>
	 * @param _id initial attribute declared by type <code>AbstractConstraint</code> at extension <code>validation</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _regexp initial attribute declared by type <code>PatternConstraint</code> at extension <code>validation</code>
	 */
	@Deprecated
	public PatternConstraintModel(final Class _annotation, final Set<RegexpFlag> _flags, final String _id, final ItemModel _owner, final String _regexp)
	{
		super();
		setAnnotation(_annotation);
		setFlags(_flags);
		setId(_id);
		setOwner(_owner);
		setRegexp(_regexp);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PatternConstraint.flags</code> attribute defined at extension <code>validation</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the flags - Regular expression for constraint
	 */
	@Accessor(qualifier = "flags", type = Accessor.Type.GETTER)
	public Set<RegexpFlag> getFlags()
	{
		return getPersistenceContext().getPropertyValue(FLAGS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PatternConstraint.regexp</code> attribute defined at extension <code>validation</code>. 
	 * @return the regexp - Regular expression for constraint
	 */
	@Accessor(qualifier = "regexp", type = Accessor.Type.GETTER)
	public String getRegexp()
	{
		return getPersistenceContext().getPropertyValue(REGEXP);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PatternConstraint.flags</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the flags - Regular expression for constraint
	 */
	@Accessor(qualifier = "flags", type = Accessor.Type.SETTER)
	public void setFlags(final Set<RegexpFlag> value)
	{
		getPersistenceContext().setPropertyValue(FLAGS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PatternConstraint.regexp</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the regexp - Regular expression for constraint
	 */
	@Accessor(qualifier = "regexp", type = Accessor.Type.SETTER)
	public void setRegexp(final String value)
	{
		getPersistenceContext().setPropertyValue(REGEXP, value);
	}
	
}
