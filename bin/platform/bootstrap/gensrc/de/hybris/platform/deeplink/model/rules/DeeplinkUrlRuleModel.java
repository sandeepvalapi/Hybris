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
package de.hybris.platform.deeplink.model.rules;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type DeeplinkUrlRule first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class DeeplinkUrlRuleModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DeeplinkUrlRule";
	
	/** <i>Generated constant</i> - Attribute key of <code>DeeplinkUrlRule.baseUrlPattern</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String BASEURLPATTERN = "baseUrlPattern";
	
	/** <i>Generated constant</i> - Attribute key of <code>DeeplinkUrlRule.destUrlTemplate</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String DESTURLTEMPLATE = "destUrlTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>DeeplinkUrlRule.applicableType</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String APPLICABLETYPE = "applicableType";
	
	/** <i>Generated constant</i> - Attribute key of <code>DeeplinkUrlRule.useForward</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String USEFORWARD = "useForward";
	
	/** <i>Generated constant</i> - Attribute key of <code>DeeplinkUrlRule.priority</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String PRIORITY = "priority";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DeeplinkUrlRuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DeeplinkUrlRuleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _applicableType initial attribute declared by type <code>DeeplinkUrlRule</code> at extension <code>basecommerce</code>
	 * @param _baseUrlPattern initial attribute declared by type <code>DeeplinkUrlRule</code> at extension <code>basecommerce</code>
	 * @param _destUrlTemplate initial attribute declared by type <code>DeeplinkUrlRule</code> at extension <code>basecommerce</code>
	 * @param _priority initial attribute declared by type <code>DeeplinkUrlRule</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public DeeplinkUrlRuleModel(final ComposedTypeModel _applicableType, final String _baseUrlPattern, final String _destUrlTemplate, final Integer _priority)
	{
		super();
		setApplicableType(_applicableType);
		setBaseUrlPattern(_baseUrlPattern);
		setDestUrlTemplate(_destUrlTemplate);
		setPriority(_priority);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _applicableType initial attribute declared by type <code>DeeplinkUrlRule</code> at extension <code>basecommerce</code>
	 * @param _baseUrlPattern initial attribute declared by type <code>DeeplinkUrlRule</code> at extension <code>basecommerce</code>
	 * @param _destUrlTemplate initial attribute declared by type <code>DeeplinkUrlRule</code> at extension <code>basecommerce</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _priority initial attribute declared by type <code>DeeplinkUrlRule</code> at extension <code>basecommerce</code>
	 */
	@Deprecated
	public DeeplinkUrlRuleModel(final ComposedTypeModel _applicableType, final String _baseUrlPattern, final String _destUrlTemplate, final ItemModel _owner, final Integer _priority)
	{
		super();
		setApplicableType(_applicableType);
		setBaseUrlPattern(_baseUrlPattern);
		setDestUrlTemplate(_destUrlTemplate);
		setOwner(_owner);
		setPriority(_priority);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeeplinkUrlRule.applicableType</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the applicableType
	 */
	@Accessor(qualifier = "applicableType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getApplicableType()
	{
		return getPersistenceContext().getPropertyValue(APPLICABLETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeeplinkUrlRule.baseUrlPattern</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the baseUrlPattern
	 */
	@Accessor(qualifier = "baseUrlPattern", type = Accessor.Type.GETTER)
	public String getBaseUrlPattern()
	{
		return getPersistenceContext().getPropertyValue(BASEURLPATTERN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeeplinkUrlRule.destUrlTemplate</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the destUrlTemplate
	 */
	@Accessor(qualifier = "destUrlTemplate", type = Accessor.Type.GETTER)
	public String getDestUrlTemplate()
	{
		return getPersistenceContext().getPropertyValue(DESTURLTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeeplinkUrlRule.priority</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public Integer getPriority()
	{
		return getPersistenceContext().getPropertyValue(PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeeplinkUrlRule.useForward</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the useForward
	 */
	@Accessor(qualifier = "useForward", type = Accessor.Type.GETTER)
	public Boolean getUseForward()
	{
		return getPersistenceContext().getPropertyValue(USEFORWARD);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeeplinkUrlRule.applicableType</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the applicableType
	 */
	@Accessor(qualifier = "applicableType", type = Accessor.Type.SETTER)
	public void setApplicableType(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(APPLICABLETYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeeplinkUrlRule.baseUrlPattern</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the baseUrlPattern
	 */
	@Accessor(qualifier = "baseUrlPattern", type = Accessor.Type.SETTER)
	public void setBaseUrlPattern(final String value)
	{
		getPersistenceContext().setPropertyValue(BASEURLPATTERN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeeplinkUrlRule.destUrlTemplate</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the destUrlTemplate
	 */
	@Accessor(qualifier = "destUrlTemplate", type = Accessor.Type.SETTER)
	public void setDestUrlTemplate(final String value)
	{
		getPersistenceContext().setPropertyValue(DESTURLTEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeeplinkUrlRule.priority</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the priority
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PRIORITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeeplinkUrlRule.useForward</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the useForward
	 */
	@Accessor(qualifier = "useForward", type = Accessor.Type.SETTER)
	public void setUseForward(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(USEFORWARD, value);
	}
	
}
