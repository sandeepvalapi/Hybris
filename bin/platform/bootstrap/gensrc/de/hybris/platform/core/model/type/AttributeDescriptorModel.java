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
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.DescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type AttributeDescriptor first defined at extension core.
 */
@SuppressWarnings("all")
public class AttributeDescriptorModel extends DescriptorModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AttributeDescriptor";
	
	/**<i>Generated relation code constant for relation <code>AttributeConstraintAttributeDescRelation</code> defining source attribute <code>constraints</code> in extension <code>validation</code>.</i>*/
	public static final String _ATTRIBUTECONSTRAINTATTRIBUTEDESCRELATION = "AttributeConstraintAttributeDescRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.databaseColumn</code> attribute defined at extension <code>core</code>. */
	public static final String DATABASECOLUMN = "databaseColumn";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.defaultValue</code> attribute defined at extension <code>core</code>. */
	public static final String DEFAULTVALUE = "defaultValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.defaultValueDefinitionString</code> attribute defined at extension <code>core</code>. */
	public static final String DEFAULTVALUEDEFINITIONSTRING = "defaultValueDefinitionString";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.enclosingType</code> attribute defined at extension <code>core</code>. */
	public static final String ENCLOSINGTYPE = "enclosingType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.declaringEnclosingType</code> attribute defined at extension <code>core</code>. */
	public static final String DECLARINGENCLOSINGTYPE = "declaringEnclosingType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.description</code> attribute defined at extension <code>core</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.persistenceClass</code> attribute defined at extension <code>core</code>. */
	public static final String PERSISTENCECLASS = "persistenceClass";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.persistenceQualifier</code> attribute defined at extension <code>core</code>. */
	public static final String PERSISTENCEQUALIFIER = "persistenceQualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.persistenceType</code> attribute defined at extension <code>core</code>. */
	public static final String PERSISTENCETYPE = "persistenceType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.attributeHandler</code> attribute defined at extension <code>core</code>. */
	public static final String ATTRIBUTEHANDLER = "attributeHandler";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.selectionOf</code> attribute defined at extension <code>core</code>. */
	public static final String SELECTIONOF = "selectionOf";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.proposedDatabaseColumn</code> attribute defined at extension <code>core</code>. */
	public static final String PROPOSEDDATABASECOLUMN = "proposedDatabaseColumn";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.modifiers</code> attribute defined at extension <code>core</code>. */
	public static final String MODIFIERS = "modifiers";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.initial</code> attribute defined at extension <code>core</code>. */
	public static final String INITIAL = "initial";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.localized</code> attribute defined at extension <code>core</code>. */
	public static final String LOCALIZED = "localized";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.optional</code> attribute defined at extension <code>core</code>. */
	public static final String OPTIONAL = "optional";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.partOf</code> attribute defined at extension <code>core</code>. */
	public static final String PARTOF = "partOf";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.unique</code> attribute defined at extension <code>core</code>. */
	public static final String UNIQUE = "unique";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.private</code> attribute defined at extension <code>core</code>. */
	public static final String PRIVATE = "private";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.property</code> attribute defined at extension <code>core</code>. */
	public static final String PROPERTY = "property";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.readable</code> attribute defined at extension <code>core</code>. */
	public static final String READABLE = "readable";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.removable</code> attribute defined at extension <code>core</code>. */
	public static final String REMOVABLE = "removable";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.search</code> attribute defined at extension <code>core</code>. */
	public static final String SEARCH = "search";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.writable</code> attribute defined at extension <code>core</code>. */
	public static final String WRITABLE = "writable";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.encrypted</code> attribute defined at extension <code>core</code>. */
	public static final String ENCRYPTED = "encrypted";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.primitive</code> attribute defined at extension <code>core</code>. */
	public static final String PRIMITIVE = "primitive";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.hiddenForUI</code> attribute defined at extension <code>core</code>. */
	public static final String HIDDENFORUI = "hiddenForUI";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.readOnlyForUI</code> attribute defined at extension <code>core</code>. */
	public static final String READONLYFORUI = "readOnlyForUI";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.constraints</code> attribute defined at extension <code>validation</code>. */
	public static final String CONSTRAINTS = "constraints";
	
	/** <i>Generated constant</i> - Attribute key of <code>AttributeDescriptor.dontCopy</code> attribute defined at extension <code>catalog</code>. */
	public static final String DONTCOPY = "dontCopy";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AttributeDescriptorModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AttributeDescriptorModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _attributeType initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 * @param _enclosingType initial attribute declared by type <code>AttributeDescriptor</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _partOf initial attribute declared by type <code>AttributeDescriptor</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 */
	@Deprecated
	public AttributeDescriptorModel(final TypeModel _attributeType, final ComposedTypeModel _enclosingType, final Boolean _generate, final Boolean _partOf, final String _qualifier)
	{
		super();
		setAttributeType(_attributeType);
		setEnclosingType(_enclosingType);
		setGenerate(_generate);
		setPartOf(_partOf);
		setQualifier(_qualifier);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _attributeType initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 * @param _enclosingType initial attribute declared by type <code>AttributeDescriptor</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _partOf initial attribute declared by type <code>AttributeDescriptor</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>Descriptor</code> at extension <code>core</code>
	 */
	@Deprecated
	public AttributeDescriptorModel(final TypeModel _attributeType, final ComposedTypeModel _enclosingType, final Boolean _generate, final ItemModel _owner, final Boolean _partOf, final String _qualifier)
	{
		super();
		setAttributeType(_attributeType);
		setEnclosingType(_enclosingType);
		setGenerate(_generate);
		setOwner(_owner);
		setPartOf(_partOf);
		setQualifier(_qualifier);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.attributeHandler</code> attribute defined at extension <code>core</code>. 
	 * @return the attributeHandler
	 */
	@Accessor(qualifier = "attributeHandler", type = Accessor.Type.GETTER)
	public String getAttributeHandler()
	{
		return getPersistenceContext().getPropertyValue(ATTRIBUTEHANDLER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.constraints</code> attribute defined at extension <code>validation</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the constraints
	 */
	@Accessor(qualifier = "constraints", type = Accessor.Type.GETTER)
	public Set<AttributeConstraintModel> getConstraints()
	{
		return getPersistenceContext().getPropertyValue(CONSTRAINTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.databaseColumn</code> attribute defined at extension <code>core</code>. 
	 * @return the databaseColumn
	 */
	@Accessor(qualifier = "databaseColumn", type = Accessor.Type.GETTER)
	public String getDatabaseColumn()
	{
		return getPersistenceContext().getPropertyValue(DATABASECOLUMN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.declaringEnclosingType</code> attribute defined at extension <code>core</code>. 
	 * @return the declaringEnclosingType
	 */
	@Accessor(qualifier = "declaringEnclosingType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getDeclaringEnclosingType()
	{
		return getPersistenceContext().getPropertyValue(DECLARINGENCLOSINGTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.defaultValue</code> attribute defined at extension <code>core</code>. 
	 * @return the defaultValue
	 */
	@Accessor(qualifier = "defaultValue", type = Accessor.Type.GETTER)
	public Object getDefaultValue()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTVALUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.description</code> attribute defined at extension <code>core</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.description</code> attribute defined at extension <code>core</code>. 
	 * @param loc the value localization key 
	 * @return the description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.dontCopy</code> attribute defined at extension <code>catalog</code>. 
	 * @return the dontCopy
	 */
	@Accessor(qualifier = "dontCopy", type = Accessor.Type.GETTER)
	public Boolean getDontCopy()
	{
		return getPersistenceContext().getPropertyValue(DONTCOPY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.enclosingType</code> attribute defined at extension <code>core</code>. 
	 * @return the enclosingType
	 */
	@Accessor(qualifier = "enclosingType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getEnclosingType()
	{
		return getPersistenceContext().getPropertyValue(ENCLOSINGTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.encrypted</code> attribute defined at extension <code>core</code>. 
	 * @return the encrypted
	 */
	@Accessor(qualifier = "encrypted", type = Accessor.Type.GETTER)
	public Boolean getEncrypted()
	{
		return getPersistenceContext().getPropertyValue(ENCRYPTED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.hiddenForUI</code> attribute defined at extension <code>core</code>. 
	 * @return the hiddenForUI
	 */
	@Accessor(qualifier = "hiddenForUI", type = Accessor.Type.GETTER)
	public Boolean getHiddenForUI()
	{
		return getPersistenceContext().getPropertyValue(HIDDENFORUI);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.initial</code> attribute defined at extension <code>core</code>. 
	 * @return the initial
	 */
	@Accessor(qualifier = "initial", type = Accessor.Type.GETTER)
	public Boolean getInitial()
	{
		return getPersistenceContext().getPropertyValue(INITIAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.localized</code> attribute defined at extension <code>core</code>. 
	 * @return the localized
	 */
	@Accessor(qualifier = "localized", type = Accessor.Type.GETTER)
	public Boolean getLocalized()
	{
		return getPersistenceContext().getPropertyValue(LOCALIZED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.modifiers</code> attribute defined at extension <code>core</code>. 
	 * @return the modifiers
	 */
	@Accessor(qualifier = "modifiers", type = Accessor.Type.GETTER)
	public Integer getModifiers()
	{
		return getPersistenceContext().getPropertyValue(MODIFIERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.optional</code> attribute defined at extension <code>core</code>. 
	 * @return the optional
	 */
	@Accessor(qualifier = "optional", type = Accessor.Type.GETTER)
	public Boolean getOptional()
	{
		return getPersistenceContext().getPropertyValue(OPTIONAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.partOf</code> attribute defined at extension <code>core</code>. 
	 * @return the partOf
	 */
	@Accessor(qualifier = "partOf", type = Accessor.Type.GETTER)
	public Boolean getPartOf()
	{
		return getPersistenceContext().getPropertyValue(PARTOF);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.persistenceClass</code> attribute defined at extension <code>core</code>. 
	 * @return the persistenceClass
	 */
	@Accessor(qualifier = "persistenceClass", type = Accessor.Type.GETTER)
	public Class getPersistenceClass()
	{
		return getPersistenceContext().getPropertyValue(PERSISTENCECLASS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.primitive</code> attribute defined at extension <code>core</code>. 
	 * @return the primitive
	 */
	@Accessor(qualifier = "primitive", type = Accessor.Type.GETTER)
	public Boolean getPrimitive()
	{
		return getPersistenceContext().getPropertyValue(PRIMITIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.private</code> attribute defined at extension <code>core</code>. 
	 * @return the private
	 */
	@Accessor(qualifier = "private", type = Accessor.Type.GETTER)
	public Boolean getPrivate()
	{
		return getPersistenceContext().getPropertyValue(PRIVATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.property</code> attribute defined at extension <code>core</code>. 
	 * @return the property
	 */
	@Accessor(qualifier = "property", type = Accessor.Type.GETTER)
	public Boolean getProperty()
	{
		return getPersistenceContext().getPropertyValue(PROPERTY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.proposedDatabaseColumn</code> attribute defined at extension <code>core</code>. 
	 * @return the proposedDatabaseColumn
	 */
	@Accessor(qualifier = "proposedDatabaseColumn", type = Accessor.Type.GETTER)
	public String getProposedDatabaseColumn()
	{
		return getPersistenceContext().getPropertyValue(PROPOSEDDATABASECOLUMN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.readable</code> attribute defined at extension <code>core</code>. 
	 * @return the readable
	 */
	@Accessor(qualifier = "readable", type = Accessor.Type.GETTER)
	public Boolean getReadable()
	{
		return getPersistenceContext().getPropertyValue(READABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.readOnlyForUI</code> attribute defined at extension <code>core</code>. 
	 * @return the readOnlyForUI
	 */
	@Accessor(qualifier = "readOnlyForUI", type = Accessor.Type.GETTER)
	public Boolean getReadOnlyForUI()
	{
		return getPersistenceContext().getPropertyValue(READONLYFORUI);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.removable</code> attribute defined at extension <code>core</code>. 
	 * @return the removable
	 */
	@Accessor(qualifier = "removable", type = Accessor.Type.GETTER)
	public Boolean getRemovable()
	{
		return getPersistenceContext().getPropertyValue(REMOVABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.search</code> attribute defined at extension <code>core</code>. 
	 * @return the search
	 */
	@Accessor(qualifier = "search", type = Accessor.Type.GETTER)
	public Boolean getSearch()
	{
		return getPersistenceContext().getPropertyValue(SEARCH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.selectionOf</code> attribute defined at extension <code>core</code>. 
	 * @return the selectionOf
	 */
	@Accessor(qualifier = "selectionOf", type = Accessor.Type.GETTER)
	public AttributeDescriptorModel getSelectionOf()
	{
		return getPersistenceContext().getPropertyValue(SELECTIONOF);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.unique</code> attribute defined at extension <code>core</code>. 
	 * @return the unique
	 */
	@Accessor(qualifier = "unique", type = Accessor.Type.GETTER)
	public Boolean getUnique()
	{
		return getPersistenceContext().getPropertyValue(UNIQUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AttributeDescriptor.writable</code> attribute defined at extension <code>core</code>. 
	 * @return the writable
	 */
	@Accessor(qualifier = "writable", type = Accessor.Type.GETTER)
	public Boolean getWritable()
	{
		return getPersistenceContext().getPropertyValue(WRITABLE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.attributeHandler</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the attributeHandler
	 */
	@Accessor(qualifier = "attributeHandler", type = Accessor.Type.SETTER)
	public void setAttributeHandler(final String value)
	{
		getPersistenceContext().setPropertyValue(ATTRIBUTEHANDLER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.constraints</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the constraints
	 */
	@Accessor(qualifier = "constraints", type = Accessor.Type.SETTER)
	public void setConstraints(final Set<AttributeConstraintModel> value)
	{
		getPersistenceContext().setPropertyValue(CONSTRAINTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.databaseColumn</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the databaseColumn
	 */
	@Accessor(qualifier = "databaseColumn", type = Accessor.Type.SETTER)
	public void setDatabaseColumn(final String value)
	{
		getPersistenceContext().setPropertyValue(DATABASECOLUMN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.defaultValue</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the defaultValue
	 */
	@Accessor(qualifier = "defaultValue", type = Accessor.Type.SETTER)
	public void setDefaultValue(final Object value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTVALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.description</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.description</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.dontCopy</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the dontCopy
	 */
	@Accessor(qualifier = "dontCopy", type = Accessor.Type.SETTER)
	public void setDontCopy(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(DONTCOPY, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AttributeDescriptor.enclosingType</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the enclosingType
	 */
	@Accessor(qualifier = "enclosingType", type = Accessor.Type.SETTER)
	public void setEnclosingType(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(ENCLOSINGTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.encrypted</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the encrypted
	 */
	@Accessor(qualifier = "encrypted", type = Accessor.Type.SETTER)
	public void setEncrypted(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(ENCRYPTED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.hiddenForUI</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the hiddenForUI
	 */
	@Accessor(qualifier = "hiddenForUI", type = Accessor.Type.SETTER)
	public void setHiddenForUI(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(HIDDENFORUI, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.initial</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the initial
	 */
	@Accessor(qualifier = "initial", type = Accessor.Type.SETTER)
	public void setInitial(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(INITIAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.modifiers</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the modifiers
	 */
	@Accessor(qualifier = "modifiers", type = Accessor.Type.SETTER)
	public void setModifiers(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MODIFIERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.optional</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the optional
	 */
	@Accessor(qualifier = "optional", type = Accessor.Type.SETTER)
	public void setOptional(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(OPTIONAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.partOf</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the partOf
	 */
	@Accessor(qualifier = "partOf", type = Accessor.Type.SETTER)
	public void setPartOf(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(PARTOF, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.primitive</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the primitive
	 */
	@Accessor(qualifier = "primitive", type = Accessor.Type.SETTER)
	public void setPrimitive(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(PRIMITIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.private</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the private
	 */
	@Accessor(qualifier = "private", type = Accessor.Type.SETTER)
	public void setPrivate(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(PRIVATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>AttributeDescriptor.property</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the property
	 */
	@Accessor(qualifier = "property", type = Accessor.Type.SETTER)
	public void setProperty(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(PROPERTY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.readable</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the readable
	 */
	@Accessor(qualifier = "readable", type = Accessor.Type.SETTER)
	public void setReadable(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(READABLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.readOnlyForUI</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the readOnlyForUI
	 */
	@Accessor(qualifier = "readOnlyForUI", type = Accessor.Type.SETTER)
	public void setReadOnlyForUI(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(READONLYFORUI, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.removable</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the removable
	 */
	@Accessor(qualifier = "removable", type = Accessor.Type.SETTER)
	public void setRemovable(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(REMOVABLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.search</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the search
	 */
	@Accessor(qualifier = "search", type = Accessor.Type.SETTER)
	public void setSearch(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SEARCH, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.unique</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the unique
	 */
	@Accessor(qualifier = "unique", type = Accessor.Type.SETTER)
	public void setUnique(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(UNIQUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AttributeDescriptor.writable</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the writable
	 */
	@Accessor(qualifier = "writable", type = Accessor.Type.SETTER)
	public void setWritable(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(WRITABLE, value);
	}
	
}
