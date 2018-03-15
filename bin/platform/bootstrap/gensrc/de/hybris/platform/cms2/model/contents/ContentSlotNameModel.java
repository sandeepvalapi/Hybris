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
package de.hybris.platform.cms2.model.contents;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.model.ComponentTypeGroupModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

/**
 * Generated model class for type ContentSlotName first defined at extension cms2.
 */
@SuppressWarnings("all")
public class ContentSlotNameModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ContentSlotName";
	
	/**<i>Generated relation code constant for relation <code>AvailableSlotsForTemplate</code> defining source attribute <code>template</code> in extension <code>cms2</code>.</i>*/
	public static final String _AVAILABLESLOTSFORTEMPLATE = "AvailableSlotsForTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>ContentSlotName.name</code> attribute defined at extension <code>cms2</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>ContentSlotName.compTypeGroup</code> attribute defined at extension <code>cms2</code>. */
	public static final String COMPTYPEGROUP = "compTypeGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>ContentSlotName.templatePOS</code> attribute defined at extension <code>cms2</code>. */
	public static final String TEMPLATEPOS = "templatePOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>ContentSlotName.template</code> attribute defined at extension <code>cms2</code>. */
	public static final String TEMPLATE = "template";
	
	/** <i>Generated constant</i> - Attribute key of <code>ContentSlotName.validComponentTypes</code> attribute defined at extension <code>cms2</code>. */
	public static final String VALIDCOMPONENTTYPES = "validComponentTypes";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ContentSlotNameModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ContentSlotNameModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>ContentSlotName</code> at extension <code>cms2</code>
	 * @param _template initial attribute declared by type <code>ContentSlotName</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public ContentSlotNameModel(final String _name, final PageTemplateModel _template)
	{
		super();
		setName(_name);
		setTemplate(_template);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>ContentSlotName</code> at extension <code>cms2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _template initial attribute declared by type <code>ContentSlotName</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public ContentSlotNameModel(final String _name, final ItemModel _owner, final PageTemplateModel _template)
	{
		super();
		setName(_name);
		setOwner(_owner);
		setTemplate(_template);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSlotName.compTypeGroup</code> attribute defined at extension <code>cms2</code>. 
	 * @return the compTypeGroup
	 */
	@Accessor(qualifier = "compTypeGroup", type = Accessor.Type.GETTER)
	public ComponentTypeGroupModel getCompTypeGroup()
	{
		return getPersistenceContext().getPropertyValue(COMPTYPEGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSlotName.name</code> attribute defined at extension <code>cms2</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSlotName.template</code> attribute defined at extension <code>cms2</code>. 
	 * @return the template
	 */
	@Accessor(qualifier = "template", type = Accessor.Type.GETTER)
	public PageTemplateModel getTemplate()
	{
		return getPersistenceContext().getPropertyValue(TEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSlotName.validComponentTypes</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the validComponentTypes
	 * @deprecated since 5.0
	 */
	@Deprecated
	@Accessor(qualifier = "validComponentTypes", type = Accessor.Type.GETTER)
	public Set<CMSComponentTypeModel> getValidComponentTypes()
	{
		return getPersistenceContext().getPropertyValue(VALIDCOMPONENTTYPES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ContentSlotName.compTypeGroup</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the compTypeGroup
	 */
	@Accessor(qualifier = "compTypeGroup", type = Accessor.Type.SETTER)
	public void setCompTypeGroup(final ComponentTypeGroupModel value)
	{
		getPersistenceContext().setPropertyValue(COMPTYPEGROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ContentSlotName.name</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ContentSlotName.template</code> attribute defined at extension <code>cms2</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the template
	 */
	@Accessor(qualifier = "template", type = Accessor.Type.SETTER)
	public void setTemplate(final PageTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(TEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ContentSlotName.validComponentTypes</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the validComponentTypes
	 * @deprecated since 5.0
	 */
	@Deprecated
	@Accessor(qualifier = "validComponentTypes", type = Accessor.Type.SETTER)
	public void setValidComponentTypes(final Set<CMSComponentTypeModel> value)
	{
		getPersistenceContext().setPropertyValue(VALIDCOMPONENTTYPES, value);
	}
	
}
