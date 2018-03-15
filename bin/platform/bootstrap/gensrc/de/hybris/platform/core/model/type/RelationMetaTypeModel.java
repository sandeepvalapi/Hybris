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
import de.hybris.platform.core.enums.RelationEndCardinalityEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.RelationDescriptorModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type RelationMetaType first defined at extension core.
 */
@SuppressWarnings("all")
public class RelationMetaTypeModel extends ComposedTypeModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "RelationMetaType";
	
	/** <i>Generated constant</i> - Attribute key of <code>RelationMetaType.localized</code> attribute defined at extension <code>core</code>. */
	public static final String LOCALIZED = "localized";
	
	/** <i>Generated constant</i> - Attribute key of <code>RelationMetaType.sourceAttribute</code> attribute defined at extension <code>core</code>. */
	public static final String SOURCEATTRIBUTE = "sourceAttribute";
	
	/** <i>Generated constant</i> - Attribute key of <code>RelationMetaType.targetAttribute</code> attribute defined at extension <code>core</code>. */
	public static final String TARGETATTRIBUTE = "targetAttribute";
	
	/** <i>Generated constant</i> - Attribute key of <code>RelationMetaType.sourceType</code> attribute defined at extension <code>core</code>. */
	public static final String SOURCETYPE = "sourceType";
	
	/** <i>Generated constant</i> - Attribute key of <code>RelationMetaType.targetType</code> attribute defined at extension <code>core</code>. */
	public static final String TARGETTYPE = "targetType";
	
	/** <i>Generated constant</i> - Attribute key of <code>RelationMetaType.sourceTypeRole</code> attribute defined at extension <code>core</code>. */
	public static final String SOURCETYPEROLE = "sourceTypeRole";
	
	/** <i>Generated constant</i> - Attribute key of <code>RelationMetaType.targetTypeRole</code> attribute defined at extension <code>core</code>. */
	public static final String TARGETTYPEROLE = "targetTypeRole";
	
	/** <i>Generated constant</i> - Attribute key of <code>RelationMetaType.sourceNavigable</code> attribute defined at extension <code>core</code>. */
	public static final String SOURCENAVIGABLE = "sourceNavigable";
	
	/** <i>Generated constant</i> - Attribute key of <code>RelationMetaType.targetNavigable</code> attribute defined at extension <code>core</code>. */
	public static final String TARGETNAVIGABLE = "targetNavigable";
	
	/** <i>Generated constant</i> - Attribute key of <code>RelationMetaType.sourceTypeCardinality</code> attribute defined at extension <code>core</code>. */
	public static final String SOURCETYPECARDINALITY = "sourceTypeCardinality";
	
	/** <i>Generated constant</i> - Attribute key of <code>RelationMetaType.targetTypeCardinality</code> attribute defined at extension <code>core</code>. */
	public static final String TARGETTYPECARDINALITY = "targetTypeCardinality";
	
	/** <i>Generated constant</i> - Attribute key of <code>RelationMetaType.orderingAttribute</code> attribute defined at extension <code>core</code>. */
	public static final String ORDERINGATTRIBUTE = "orderingAttribute";
	
	/** <i>Generated constant</i> - Attribute key of <code>RelationMetaType.localizationAttribute</code> attribute defined at extension <code>core</code>. */
	public static final String LOCALIZATIONATTRIBUTE = "localizationAttribute";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RelationMetaTypeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RelationMetaTypeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogItemType initial attribute declared by type <code>ComposedType</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Type</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _localized initial attribute declared by type <code>RelationMetaType</code> at extension <code>core</code>
	 * @param _singleton initial attribute declared by type <code>ComposedType</code> at extension <code>core</code>
	 * @param _superType initial attribute declared by type <code>RelationMetaType</code> at extension <code>core</code>
	 */
	@Deprecated
	public RelationMetaTypeModel(final Boolean _catalogItemType, final String _code, final Boolean _generate, final Boolean _localized, final Boolean _singleton, final ComposedTypeModel _superType)
	{
		super();
		setCatalogItemType(_catalogItemType);
		setCode(_code);
		setGenerate(_generate);
		setLocalized(_localized);
		setSingleton(_singleton);
		setSuperType(_superType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogItemType initial attribute declared by type <code>ComposedType</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Type</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _localizationAttribute initial attribute declared by type <code>RelationMetaType</code> at extension <code>core</code>
	 * @param _localized initial attribute declared by type <code>RelationMetaType</code> at extension <code>core</code>
	 * @param _orderingAttribute initial attribute declared by type <code>RelationMetaType</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _singleton initial attribute declared by type <code>ComposedType</code> at extension <code>core</code>
	 * @param _sourceAttribute initial attribute declared by type <code>RelationMetaType</code> at extension <code>core</code>
	 * @param _sourceNavigable initial attribute declared by type <code>RelationMetaType</code> at extension <code>core</code>
	 * @param _sourceType initial attribute declared by type <code>RelationMetaType</code> at extension <code>core</code>
	 * @param _sourceTypeCardinality initial attribute declared by type <code>RelationMetaType</code> at extension <code>core</code>
	 * @param _sourceTypeRole initial attribute declared by type <code>RelationMetaType</code> at extension <code>core</code>
	 * @param _superType initial attribute declared by type <code>RelationMetaType</code> at extension <code>core</code>
	 * @param _targetAttribute initial attribute declared by type <code>RelationMetaType</code> at extension <code>core</code>
	 * @param _targetNavigable initial attribute declared by type <code>RelationMetaType</code> at extension <code>core</code>
	 * @param _targetType initial attribute declared by type <code>RelationMetaType</code> at extension <code>core</code>
	 * @param _targetTypeCardinality initial attribute declared by type <code>RelationMetaType</code> at extension <code>core</code>
	 * @param _targetTypeRole initial attribute declared by type <code>RelationMetaType</code> at extension <code>core</code>
	 */
	@Deprecated
	public RelationMetaTypeModel(final Boolean _catalogItemType, final String _code, final Boolean _generate, final AttributeDescriptorModel _localizationAttribute, final Boolean _localized, final AttributeDescriptorModel _orderingAttribute, final ItemModel _owner, final Boolean _singleton, final RelationDescriptorModel _sourceAttribute, final Boolean _sourceNavigable, final ComposedTypeModel _sourceType, final RelationEndCardinalityEnum _sourceTypeCardinality, final String _sourceTypeRole, final ComposedTypeModel _superType, final RelationDescriptorModel _targetAttribute, final Boolean _targetNavigable, final ComposedTypeModel _targetType, final RelationEndCardinalityEnum _targetTypeCardinality, final String _targetTypeRole)
	{
		super();
		setCatalogItemType(_catalogItemType);
		setCode(_code);
		setGenerate(_generate);
		setLocalizationAttribute(_localizationAttribute);
		setLocalized(_localized);
		setOrderingAttribute(_orderingAttribute);
		setOwner(_owner);
		setSingleton(_singleton);
		setSourceAttribute(_sourceAttribute);
		setSourceNavigable(_sourceNavigable);
		setSourceType(_sourceType);
		setSourceTypeCardinality(_sourceTypeCardinality);
		setSourceTypeRole(_sourceTypeRole);
		setSuperType(_superType);
		setTargetAttribute(_targetAttribute);
		setTargetNavigable(_targetNavigable);
		setTargetType(_targetType);
		setTargetTypeCardinality(_targetTypeCardinality);
		setTargetTypeRole(_targetTypeRole);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RelationMetaType.localizationAttribute</code> attribute defined at extension <code>core</code>. 
	 * @return the localizationAttribute
	 */
	@Accessor(qualifier = "localizationAttribute", type = Accessor.Type.GETTER)
	public AttributeDescriptorModel getLocalizationAttribute()
	{
		return getPersistenceContext().getPropertyValue(LOCALIZATIONATTRIBUTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RelationMetaType.localized</code> attribute defined at extension <code>core</code>. 
	 * @return the localized
	 */
	@Accessor(qualifier = "localized", type = Accessor.Type.GETTER)
	public Boolean getLocalized()
	{
		return getPersistenceContext().getPropertyValue(LOCALIZED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RelationMetaType.orderingAttribute</code> attribute defined at extension <code>core</code>. 
	 * @return the orderingAttribute
	 */
	@Accessor(qualifier = "orderingAttribute", type = Accessor.Type.GETTER)
	public AttributeDescriptorModel getOrderingAttribute()
	{
		return getPersistenceContext().getPropertyValue(ORDERINGATTRIBUTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RelationMetaType.sourceAttribute</code> attribute defined at extension <code>core</code>. 
	 * @return the sourceAttribute
	 */
	@Accessor(qualifier = "sourceAttribute", type = Accessor.Type.GETTER)
	public RelationDescriptorModel getSourceAttribute()
	{
		return getPersistenceContext().getPropertyValue(SOURCEATTRIBUTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RelationMetaType.sourceNavigable</code> attribute defined at extension <code>core</code>. 
	 * @return the sourceNavigable
	 */
	@Accessor(qualifier = "sourceNavigable", type = Accessor.Type.GETTER)
	public Boolean getSourceNavigable()
	{
		return getPersistenceContext().getPropertyValue(SOURCENAVIGABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RelationMetaType.sourceType</code> attribute defined at extension <code>core</code>. 
	 * @return the sourceType
	 */
	@Accessor(qualifier = "sourceType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getSourceType()
	{
		return getPersistenceContext().getPropertyValue(SOURCETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RelationMetaType.sourceTypeCardinality</code> attribute defined at extension <code>core</code>. 
	 * @return the sourceTypeCardinality
	 */
	@Accessor(qualifier = "sourceTypeCardinality", type = Accessor.Type.GETTER)
	public RelationEndCardinalityEnum getSourceTypeCardinality()
	{
		return getPersistenceContext().getPropertyValue(SOURCETYPECARDINALITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RelationMetaType.sourceTypeRole</code> attribute defined at extension <code>core</code>. 
	 * @return the sourceTypeRole
	 */
	@Accessor(qualifier = "sourceTypeRole", type = Accessor.Type.GETTER)
	public String getSourceTypeRole()
	{
		return getPersistenceContext().getPropertyValue(SOURCETYPEROLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RelationMetaType.targetAttribute</code> attribute defined at extension <code>core</code>. 
	 * @return the targetAttribute
	 */
	@Accessor(qualifier = "targetAttribute", type = Accessor.Type.GETTER)
	public RelationDescriptorModel getTargetAttribute()
	{
		return getPersistenceContext().getPropertyValue(TARGETATTRIBUTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RelationMetaType.targetNavigable</code> attribute defined at extension <code>core</code>. 
	 * @return the targetNavigable
	 */
	@Accessor(qualifier = "targetNavigable", type = Accessor.Type.GETTER)
	public Boolean getTargetNavigable()
	{
		return getPersistenceContext().getPropertyValue(TARGETNAVIGABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RelationMetaType.targetType</code> attribute defined at extension <code>core</code>. 
	 * @return the targetType
	 */
	@Accessor(qualifier = "targetType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getTargetType()
	{
		return getPersistenceContext().getPropertyValue(TARGETTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RelationMetaType.targetTypeCardinality</code> attribute defined at extension <code>core</code>. 
	 * @return the targetTypeCardinality
	 */
	@Accessor(qualifier = "targetTypeCardinality", type = Accessor.Type.GETTER)
	public RelationEndCardinalityEnum getTargetTypeCardinality()
	{
		return getPersistenceContext().getPropertyValue(TARGETTYPECARDINALITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RelationMetaType.targetTypeRole</code> attribute defined at extension <code>core</code>. 
	 * @return the targetTypeRole
	 */
	@Accessor(qualifier = "targetTypeRole", type = Accessor.Type.GETTER)
	public String getTargetTypeRole()
	{
		return getPersistenceContext().getPropertyValue(TARGETTYPEROLE);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>RelationMetaType.localizationAttribute</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the localizationAttribute
	 */
	@Accessor(qualifier = "localizationAttribute", type = Accessor.Type.SETTER)
	public void setLocalizationAttribute(final AttributeDescriptorModel value)
	{
		getPersistenceContext().setPropertyValue(LOCALIZATIONATTRIBUTE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>RelationMetaType.localized</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the localized
	 */
	@Accessor(qualifier = "localized", type = Accessor.Type.SETTER)
	public void setLocalized(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(LOCALIZED, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>RelationMetaType.orderingAttribute</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the orderingAttribute
	 */
	@Accessor(qualifier = "orderingAttribute", type = Accessor.Type.SETTER)
	public void setOrderingAttribute(final AttributeDescriptorModel value)
	{
		getPersistenceContext().setPropertyValue(ORDERINGATTRIBUTE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>RelationMetaType.sourceAttribute</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sourceAttribute
	 */
	@Accessor(qualifier = "sourceAttribute", type = Accessor.Type.SETTER)
	public void setSourceAttribute(final RelationDescriptorModel value)
	{
		getPersistenceContext().setPropertyValue(SOURCEATTRIBUTE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>RelationMetaType.sourceNavigable</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sourceNavigable
	 */
	@Accessor(qualifier = "sourceNavigable", type = Accessor.Type.SETTER)
	public void setSourceNavigable(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SOURCENAVIGABLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>RelationMetaType.sourceType</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sourceType
	 */
	@Accessor(qualifier = "sourceType", type = Accessor.Type.SETTER)
	public void setSourceType(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(SOURCETYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>RelationMetaType.sourceTypeCardinality</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sourceTypeCardinality
	 */
	@Accessor(qualifier = "sourceTypeCardinality", type = Accessor.Type.SETTER)
	public void setSourceTypeCardinality(final RelationEndCardinalityEnum value)
	{
		getPersistenceContext().setPropertyValue(SOURCETYPECARDINALITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>RelationMetaType.sourceTypeRole</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sourceTypeRole
	 */
	@Accessor(qualifier = "sourceTypeRole", type = Accessor.Type.SETTER)
	public void setSourceTypeRole(final String value)
	{
		getPersistenceContext().setPropertyValue(SOURCETYPEROLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>RelationMetaType.targetAttribute</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the targetAttribute
	 */
	@Accessor(qualifier = "targetAttribute", type = Accessor.Type.SETTER)
	public void setTargetAttribute(final RelationDescriptorModel value)
	{
		getPersistenceContext().setPropertyValue(TARGETATTRIBUTE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>RelationMetaType.targetNavigable</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the targetNavigable
	 */
	@Accessor(qualifier = "targetNavigable", type = Accessor.Type.SETTER)
	public void setTargetNavigable(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(TARGETNAVIGABLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>RelationMetaType.targetType</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the targetType
	 */
	@Accessor(qualifier = "targetType", type = Accessor.Type.SETTER)
	public void setTargetType(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(TARGETTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>RelationMetaType.targetTypeCardinality</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the targetTypeCardinality
	 */
	@Accessor(qualifier = "targetTypeCardinality", type = Accessor.Type.SETTER)
	public void setTargetTypeCardinality(final RelationEndCardinalityEnum value)
	{
		getPersistenceContext().setPropertyValue(TARGETTYPECARDINALITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>RelationMetaType.targetTypeRole</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the targetTypeRole
	 */
	@Accessor(qualifier = "targetTypeRole", type = Accessor.Type.SETTER)
	public void setTargetTypeRole(final String value)
	{
		getPersistenceContext().setPropertyValue(TARGETTYPEROLE, value);
	}
	
}
