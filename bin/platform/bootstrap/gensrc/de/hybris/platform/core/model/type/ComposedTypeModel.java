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
import de.hybris.platform.cockpit.model.template.CockpitItemTemplateModel;
import de.hybris.platform.commons.model.FormatModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.AbstractConstraintModel;
import java.util.Collection;
import java.util.Set;

/**
 * Generated model class for type ComposedType first defined at extension core.
 */
@SuppressWarnings("all")
public class ComposedTypeModel extends TypeModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ComposedType";
	
	/**<i>Generated relation code constant for relation <code>Format2ComTypRel</code> defining source attribute <code>formats</code> in extension <code>commons</code>.</i>*/
	public static final String _FORMAT2COMTYPREL = "Format2ComTypRel";
	
	/**<i>Generated relation code constant for relation <code>ConstraintCompositeTypeRelation</code> defining source attribute <code>constraints</code> in extension <code>validation</code>.</i>*/
	public static final String _CONSTRAINTCOMPOSITETYPERELATION = "ConstraintCompositeTypeRelation";
	
	/**<i>Generated relation code constant for relation <code>SyncJob2TypeRel</code> defining source attribute <code>syncJobs</code> in extension <code>catalog</code>.</i>*/
	public static final String _SYNCJOB2TYPEREL = "SyncJob2TypeRel";
	
	/**<i>Generated relation code constant for relation <code>CockpitItemTemplate2ComposedTypeRelation</code> defining source attribute <code>cockpitItemTemplates</code> in extension <code>cockpit</code>.</i>*/
	public static final String _COCKPITITEMTEMPLATE2COMPOSEDTYPERELATION = "CockpitItemTemplate2ComposedTypeRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.abstract</code> attribute defined at extension <code>core</code>. */
	public static final String ABSTRACT = "abstract";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.declaredattributedescriptors</code> attribute defined at extension <code>core</code>. */
	public static final String DECLAREDATTRIBUTEDESCRIPTORS = "declaredattributedescriptors";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.dumpPropertyTable</code> attribute defined at extension <code>core</code>. */
	public static final String DUMPPROPERTYTABLE = "dumpPropertyTable";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.attributedescriptors</code> attribute defined at extension <code>core</code>. */
	public static final String ATTRIBUTEDESCRIPTORS = "attributedescriptors";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.inheritancePathString</code> attribute defined at extension <code>core</code>. */
	public static final String INHERITANCEPATHSTRING = "inheritancePathString";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.inheritedattributedescriptors</code> attribute defined at extension <code>core</code>. */
	public static final String INHERITEDATTRIBUTEDESCRIPTORS = "inheritedattributedescriptors";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.jaloclass</code> attribute defined at extension <code>core</code>. */
	public static final String JALOCLASS = "jaloclass";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.jndiName</code> attribute defined at extension <code>core</code>. */
	public static final String JNDINAME = "jndiName";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.singleton</code> attribute defined at extension <code>core</code>. */
	public static final String SINGLETON = "singleton";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.jaloonly</code> attribute defined at extension <code>core</code>. */
	public static final String JALOONLY = "jaloonly";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.dynamic</code> attribute defined at extension <code>core</code>. */
	public static final String DYNAMIC = "dynamic";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.subtypes</code> attribute defined at extension <code>core</code>. */
	public static final String SUBTYPES = "subtypes";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.superType</code> attribute defined at extension <code>core</code>. */
	public static final String SUPERTYPE = "superType";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.table</code> attribute defined at extension <code>core</code>. */
	public static final String TABLE = "table";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.allSuperTypes</code> attribute defined at extension <code>core</code>. */
	public static final String ALLSUPERTYPES = "allSuperTypes";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.allSubTypes</code> attribute defined at extension <code>core</code>. */
	public static final String ALLSUBTYPES = "allSubTypes";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.legacyPersistence</code> attribute defined at extension <code>core</code>. */
	public static final String LEGACYPERSISTENCE = "legacyPersistence";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.formats</code> attribute defined at extension <code>commons</code>. */
	public static final String FORMATS = "formats";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.systemType</code> attribute defined at extension <code>impex</code>. */
	public static final String SYSTEMTYPE = "systemType";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.constraints</code> attribute defined at extension <code>validation</code>. */
	public static final String CONSTRAINTS = "constraints";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.catalogItemType</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOGITEMTYPE = "catalogItemType";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.catalogVersionAttribute</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOGVERSIONATTRIBUTE = "catalogVersionAttribute";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.catalogVersionAttributeQualifier</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOGVERSIONATTRIBUTEQUALIFIER = "catalogVersionAttributeQualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.uniqueKeyAttributes</code> attribute defined at extension <code>catalog</code>. */
	public static final String UNIQUEKEYATTRIBUTES = "uniqueKeyAttributes";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.uniqueKeyAttributeQualifier</code> attribute defined at extension <code>catalog</code>. */
	public static final String UNIQUEKEYATTRIBUTEQUALIFIER = "uniqueKeyAttributeQualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.syncJobs</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYNCJOBS = "syncJobs";
	
	/** <i>Generated constant</i> - Attribute key of <code>ComposedType.cockpitItemTemplates</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COCKPITITEMTEMPLATES = "cockpitItemTemplates";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ComposedTypeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ComposedTypeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogItemType initial attribute declared by type <code>ComposedType</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Type</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _singleton initial attribute declared by type <code>ComposedType</code> at extension <code>core</code>
	 * @param _superType initial attribute declared by type <code>ComposedType</code> at extension <code>core</code>
	 */
	@Deprecated
	public ComposedTypeModel(final Boolean _catalogItemType, final String _code, final Boolean _generate, final Boolean _singleton, final ComposedTypeModel _superType)
	{
		super();
		setCatalogItemType(_catalogItemType);
		setCode(_code);
		setGenerate(_generate);
		setSingleton(_singleton);
		setSuperType(_superType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogItemType initial attribute declared by type <code>ComposedType</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Type</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _singleton initial attribute declared by type <code>ComposedType</code> at extension <code>core</code>
	 * @param _superType initial attribute declared by type <code>ComposedType</code> at extension <code>core</code>
	 */
	@Deprecated
	public ComposedTypeModel(final Boolean _catalogItemType, final String _code, final Boolean _generate, final ItemModel _owner, final Boolean _singleton, final ComposedTypeModel _superType)
	{
		super();
		setCatalogItemType(_catalogItemType);
		setCode(_code);
		setGenerate(_generate);
		setOwner(_owner);
		setSingleton(_singleton);
		setSuperType(_superType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.abstract</code> attribute defined at extension <code>core</code>. 
	 * @return the abstract
	 */
	@Accessor(qualifier = "abstract", type = Accessor.Type.GETTER)
	public Boolean getAbstract()
	{
		return getPersistenceContext().getPropertyValue(ABSTRACT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.allSubTypes</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allSubTypes
	 */
	@Accessor(qualifier = "allSubTypes", type = Accessor.Type.GETTER)
	public Collection<ComposedTypeModel> getAllSubTypes()
	{
		return getPersistenceContext().getPropertyValue(ALLSUBTYPES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.allSuperTypes</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allSuperTypes
	 */
	@Accessor(qualifier = "allSuperTypes", type = Accessor.Type.GETTER)
	public Collection<ComposedTypeModel> getAllSuperTypes()
	{
		return getPersistenceContext().getPropertyValue(ALLSUPERTYPES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.catalogItemType</code> attribute defined at extension <code>catalog</code>. 
	 * @return the catalogItemType
	 */
	@Accessor(qualifier = "catalogItemType", type = Accessor.Type.GETTER)
	public Boolean getCatalogItemType()
	{
		return getPersistenceContext().getPropertyValue(CATALOGITEMTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.catalogVersionAttribute</code> attribute defined at extension <code>catalog</code>. 
	 * @return the catalogVersionAttribute
	 */
	@Accessor(qualifier = "catalogVersionAttribute", type = Accessor.Type.GETTER)
	public AttributeDescriptorModel getCatalogVersionAttribute()
	{
		return getPersistenceContext().getPropertyValue(CATALOGVERSIONATTRIBUTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.cockpitItemTemplates</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the cockpitItemTemplates
	 */
	@Accessor(qualifier = "cockpitItemTemplates", type = Accessor.Type.GETTER)
	public Set<CockpitItemTemplateModel> getCockpitItemTemplates()
	{
		return getPersistenceContext().getPropertyValue(COCKPITITEMTEMPLATES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.constraints</code> attribute defined at extension <code>validation</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the constraints
	 */
	@Accessor(qualifier = "constraints", type = Accessor.Type.GETTER)
	public Set<AbstractConstraintModel> getConstraints()
	{
		return getPersistenceContext().getPropertyValue(CONSTRAINTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.declaredattributedescriptors</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the declaredattributedescriptors
	 */
	@Accessor(qualifier = "declaredattributedescriptors", type = Accessor.Type.GETTER)
	public Collection<AttributeDescriptorModel> getDeclaredattributedescriptors()
	{
		return getPersistenceContext().getPropertyValue(DECLAREDATTRIBUTEDESCRIPTORS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.dumpPropertyTable</code> attribute defined at extension <code>core</code>. 
	 * @return the dumpPropertyTable
	 */
	@Accessor(qualifier = "dumpPropertyTable", type = Accessor.Type.GETTER)
	public String getDumpPropertyTable()
	{
		return getPersistenceContext().getPropertyValue(DUMPPROPERTYTABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.dynamic</code> attribute defined at extension <code>core</code>. 
	 * @return the dynamic
	 */
	@Accessor(qualifier = "dynamic", type = Accessor.Type.GETTER)
	public Boolean getDynamic()
	{
		return getPersistenceContext().getPropertyValue(DYNAMIC);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.formats</code> attribute defined at extension <code>commons</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the formats
	 */
	@Accessor(qualifier = "formats", type = Accessor.Type.GETTER)
	public Collection<FormatModel> getFormats()
	{
		return getPersistenceContext().getPropertyValue(FORMATS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.inheritedattributedescriptors</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the inheritedattributedescriptors
	 */
	@Accessor(qualifier = "inheritedattributedescriptors", type = Accessor.Type.GETTER)
	public Collection<AttributeDescriptorModel> getInheritedattributedescriptors()
	{
		return getPersistenceContext().getPropertyValue(INHERITEDATTRIBUTEDESCRIPTORS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.jaloclass</code> attribute defined at extension <code>core</code>. 
	 * @return the jaloclass
	 */
	@Accessor(qualifier = "jaloclass", type = Accessor.Type.GETTER)
	public Class getJaloclass()
	{
		return getPersistenceContext().getPropertyValue(JALOCLASS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.jaloonly</code> attribute defined at extension <code>core</code>. 
	 * @return the jaloonly
	 */
	@Accessor(qualifier = "jaloonly", type = Accessor.Type.GETTER)
	public Boolean getJaloonly()
	{
		return getPersistenceContext().getPropertyValue(JALOONLY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.jndiName</code> attribute defined at extension <code>core</code>. 
	 * @return the jndiName
	 */
	@Accessor(qualifier = "jndiName", type = Accessor.Type.GETTER)
	public String getJndiName()
	{
		return getPersistenceContext().getPropertyValue(JNDINAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.legacyPersistence</code> attribute defined at extension <code>core</code>. 
	 * @return the legacyPersistence
	 */
	@Accessor(qualifier = "legacyPersistence", type = Accessor.Type.GETTER)
	public Boolean getLegacyPersistence()
	{
		return getPersistenceContext().getPropertyValue(LEGACYPERSISTENCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.singleton</code> attribute defined at extension <code>core</code>. 
	 * @return the singleton
	 */
	@Accessor(qualifier = "singleton", type = Accessor.Type.GETTER)
	public Boolean getSingleton()
	{
		return getPersistenceContext().getPropertyValue(SINGLETON);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.subtypes</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the subtypes
	 */
	@Accessor(qualifier = "subtypes", type = Accessor.Type.GETTER)
	public Collection<ComposedTypeModel> getSubtypes()
	{
		return getPersistenceContext().getPropertyValue(SUBTYPES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.superType</code> attribute defined at extension <code>core</code>. 
	 * @return the superType
	 */
	@Accessor(qualifier = "superType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getSuperType()
	{
		return getPersistenceContext().getPropertyValue(SUPERTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.systemType</code> attribute defined at extension <code>impex</code>. 
	 * @return the systemType
	 */
	@Accessor(qualifier = "systemType", type = Accessor.Type.GETTER)
	public Boolean getSystemType()
	{
		return getPersistenceContext().getPropertyValue(SYSTEMTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.table</code> attribute defined at extension <code>core</code>. 
	 * @return the table
	 */
	@Accessor(qualifier = "table", type = Accessor.Type.GETTER)
	public String getTable()
	{
		return getPersistenceContext().getPropertyValue(TABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ComposedType.uniqueKeyAttributes</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the uniqueKeyAttributes
	 */
	@Accessor(qualifier = "uniqueKeyAttributes", type = Accessor.Type.GETTER)
	public Collection<AttributeDescriptorModel> getUniqueKeyAttributes()
	{
		return getPersistenceContext().getPropertyValue(UNIQUEKEYATTRIBUTES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.catalogItemType</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the catalogItemType
	 */
	@Accessor(qualifier = "catalogItemType", type = Accessor.Type.SETTER)
	public void setCatalogItemType(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(CATALOGITEMTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.catalogVersionAttribute</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the catalogVersionAttribute
	 */
	@Accessor(qualifier = "catalogVersionAttribute", type = Accessor.Type.SETTER)
	public void setCatalogVersionAttribute(final AttributeDescriptorModel value)
	{
		getPersistenceContext().setPropertyValue(CATALOGVERSIONATTRIBUTE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.cockpitItemTemplates</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the cockpitItemTemplates
	 */
	@Accessor(qualifier = "cockpitItemTemplates", type = Accessor.Type.SETTER)
	public void setCockpitItemTemplates(final Set<CockpitItemTemplateModel> value)
	{
		getPersistenceContext().setPropertyValue(COCKPITITEMTEMPLATES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.constraints</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the constraints
	 */
	@Accessor(qualifier = "constraints", type = Accessor.Type.SETTER)
	public void setConstraints(final Set<AbstractConstraintModel> value)
	{
		getPersistenceContext().setPropertyValue(CONSTRAINTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.declaredattributedescriptors</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the declaredattributedescriptors
	 */
	@Accessor(qualifier = "declaredattributedescriptors", type = Accessor.Type.SETTER)
	public void setDeclaredattributedescriptors(final Collection<AttributeDescriptorModel> value)
	{
		getPersistenceContext().setPropertyValue(DECLAREDATTRIBUTEDESCRIPTORS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.formats</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the formats
	 */
	@Accessor(qualifier = "formats", type = Accessor.Type.SETTER)
	public void setFormats(final Collection<FormatModel> value)
	{
		getPersistenceContext().setPropertyValue(FORMATS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.jaloclass</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the jaloclass
	 */
	@Accessor(qualifier = "jaloclass", type = Accessor.Type.SETTER)
	public void setJaloclass(final Class value)
	{
		getPersistenceContext().setPropertyValue(JALOCLASS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.jaloonly</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the jaloonly
	 */
	@Accessor(qualifier = "jaloonly", type = Accessor.Type.SETTER)
	public void setJaloonly(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(JALOONLY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.legacyPersistence</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the legacyPersistence
	 */
	@Accessor(qualifier = "legacyPersistence", type = Accessor.Type.SETTER)
	public void setLegacyPersistence(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(LEGACYPERSISTENCE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.singleton</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the singleton
	 */
	@Accessor(qualifier = "singleton", type = Accessor.Type.SETTER)
	public void setSingleton(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SINGLETON, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ComposedType.superType</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the superType
	 */
	@Accessor(qualifier = "superType", type = Accessor.Type.SETTER)
	public void setSuperType(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(SUPERTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.systemType</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the systemType
	 */
	@Accessor(qualifier = "systemType", type = Accessor.Type.SETTER)
	public void setSystemType(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SYSTEMTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ComposedType.uniqueKeyAttributes</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the uniqueKeyAttributes
	 */
	@Accessor(qualifier = "uniqueKeyAttributes", type = Accessor.Type.SETTER)
	public void setUniqueKeyAttributes(final Collection<AttributeDescriptorModel> value)
	{
		getPersistenceContext().setPropertyValue(UNIQUEKEYATTRIBUTES, value);
	}
	
}
