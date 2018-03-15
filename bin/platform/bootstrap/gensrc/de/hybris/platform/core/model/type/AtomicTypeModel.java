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
package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type AtomicType first defined at extension core.
 */
@SuppressWarnings("all")
public class AtomicTypeModel extends TypeModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AtomicType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AtomicType.inheritancePathString</code> attribute defined at extension <code>core</code>. */
	public static final String INHERITANCEPATHSTRING = "inheritancePathString";
	
	/** <i>Generated constant</i> - Attribute key of <code>AtomicType.javaClass</code> attribute defined at extension <code>core</code>. */
	public static final String JAVACLASS = "javaClass";
	
	/** <i>Generated constant</i> - Attribute key of <code>AtomicType.subtypes</code> attribute defined at extension <code>core</code>. */
	public static final String SUBTYPES = "subtypes";
	
	/** <i>Generated constant</i> - Attribute key of <code>AtomicType.superType</code> attribute defined at extension <code>core</code>. */
	public static final String SUPERTYPE = "superType";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AtomicTypeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AtomicTypeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AtomicType</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _javaClass initial attribute declared by type <code>AtomicType</code> at extension <code>core</code>
	 */
	@Deprecated
	public AtomicTypeModel(final String _code, final Boolean _generate, final Class _javaClass)
	{
		super();
		setCode(_code);
		setGenerate(_generate);
		setJavaClass(_javaClass);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AtomicType</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _javaClass initial attribute declared by type <code>AtomicType</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _superType initial attribute declared by type <code>AtomicType</code> at extension <code>core</code>
	 */
	@Deprecated
	public AtomicTypeModel(final String _code, final Boolean _generate, final Class _javaClass, final ItemModel _owner, final AtomicTypeModel _superType)
	{
		super();
		setCode(_code);
		setGenerate(_generate);
		setJavaClass(_javaClass);
		setOwner(_owner);
		setSuperType(_superType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AtomicType.javaClass</code> attribute defined at extension <code>core</code>. 
	 * @return the javaClass
	 */
	@Accessor(qualifier = "javaClass", type = Accessor.Type.GETTER)
	public Class getJavaClass()
	{
		return getPersistenceContext().getPropertyValue(JAVACLASS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AtomicType.subtypes</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the subtypes
	 */
	@Accessor(qualifier = "subtypes", type = Accessor.Type.GETTER)
	public Collection<AtomicTypeModel> getSubtypes()
	{
		return getPersistenceContext().getPropertyValue(SUBTYPES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AtomicType.superType</code> attribute defined at extension <code>core</code>. 
	 * @return the superType
	 */
	@Accessor(qualifier = "superType", type = Accessor.Type.GETTER)
	public AtomicTypeModel getSuperType()
	{
		return getPersistenceContext().getPropertyValue(SUPERTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AtomicType.javaClass</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the javaClass
	 */
	@Accessor(qualifier = "javaClass", type = Accessor.Type.SETTER)
	public void setJavaClass(final Class value)
	{
		getPersistenceContext().setPropertyValue(JAVACLASS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AtomicType.superType</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the superType
	 */
	@Accessor(qualifier = "superType", type = Accessor.Type.SETTER)
	public void setSuperType(final AtomicTypeModel value)
	{
		getPersistenceContext().setPropertyValue(SUPERTYPE, value);
	}
	
}
