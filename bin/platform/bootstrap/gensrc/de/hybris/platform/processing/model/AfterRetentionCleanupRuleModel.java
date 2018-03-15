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
package de.hybris.platform.processing.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.processing.model.AbstractRetentionRuleModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AfterRetentionCleanupRule first defined at extension processing.
 */
@SuppressWarnings("all")
public class AfterRetentionCleanupRuleModel extends AbstractRetentionRuleModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AfterRetentionCleanupRule";
	
	/** <i>Generated constant</i> - Attribute key of <code>AfterRetentionCleanupRule.retirementItemType</code> attribute defined at extension <code>processing</code>. */
	public static final String RETIREMENTITEMTYPE = "retirementItemType";
	
	/** <i>Generated constant</i> - Attribute key of <code>AfterRetentionCleanupRule.retirementDateAttribute</code> attribute defined at extension <code>processing</code>. */
	public static final String RETIREMENTDATEATTRIBUTE = "retirementDateAttribute";
	
	/** <i>Generated constant</i> - Attribute key of <code>AfterRetentionCleanupRule.retentionTimeSeconds</code> attribute defined at extension <code>processing</code>. */
	public static final String RETENTIONTIMESECONDS = "retentionTimeSeconds";
	
	/** <i>Generated constant</i> - Attribute key of <code>AfterRetentionCleanupRule.itemFilterExpression</code> attribute defined at extension <code>processing</code>. */
	public static final String ITEMFILTEREXPRESSION = "itemFilterExpression";
	
	/** <i>Generated constant</i> - Attribute key of <code>AfterRetentionCleanupRule.retirementDateExpression</code> attribute defined at extension <code>processing</code>. */
	public static final String RETIREMENTDATEEXPRESSION = "retirementDateExpression";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AfterRetentionCleanupRuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AfterRetentionCleanupRuleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _actionReference initial attribute declared by type <code>AbstractRetentionRule</code> at extension <code>processing</code>
	 * @param _code initial attribute declared by type <code>AbstractRetentionRule</code> at extension <code>processing</code>
	 * @param _retirementItemType initial attribute declared by type <code>AfterRetentionCleanupRule</code> at extension <code>processing</code>
	 */
	@Deprecated
	public AfterRetentionCleanupRuleModel(final String _actionReference, final String _code, final ComposedTypeModel _retirementItemType)
	{
		super();
		setActionReference(_actionReference);
		setCode(_code);
		setRetirementItemType(_retirementItemType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _actionReference initial attribute declared by type <code>AbstractRetentionRule</code> at extension <code>processing</code>
	 * @param _code initial attribute declared by type <code>AbstractRetentionRule</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _retirementItemType initial attribute declared by type <code>AfterRetentionCleanupRule</code> at extension <code>processing</code>
	 */
	@Deprecated
	public AfterRetentionCleanupRuleModel(final String _actionReference, final String _code, final ItemModel _owner, final ComposedTypeModel _retirementItemType)
	{
		super();
		setActionReference(_actionReference);
		setCode(_code);
		setOwner(_owner);
		setRetirementItemType(_retirementItemType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AfterRetentionCleanupRule.itemFilterExpression</code> attribute defined at extension <code>processing</code>. 
	 * @return the itemFilterExpression
	 */
	@Accessor(qualifier = "itemFilterExpression", type = Accessor.Type.GETTER)
	public String getItemFilterExpression()
	{
		return getPersistenceContext().getPropertyValue(ITEMFILTEREXPRESSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AfterRetentionCleanupRule.retentionTimeSeconds</code> attribute defined at extension <code>processing</code>. 
	 * @return the retentionTimeSeconds
	 */
	@Accessor(qualifier = "retentionTimeSeconds", type = Accessor.Type.GETTER)
	public Long getRetentionTimeSeconds()
	{
		return getPersistenceContext().getPropertyValue(RETENTIONTIMESECONDS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AfterRetentionCleanupRule.retirementDateAttribute</code> attribute defined at extension <code>processing</code>. 
	 * @return the retirementDateAttribute
	 */
	@Accessor(qualifier = "retirementDateAttribute", type = Accessor.Type.GETTER)
	public AttributeDescriptorModel getRetirementDateAttribute()
	{
		return getPersistenceContext().getPropertyValue(RETIREMENTDATEATTRIBUTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AfterRetentionCleanupRule.retirementDateExpression</code> attribute defined at extension <code>processing</code>. 
	 * @return the retirementDateExpression
	 */
	@Accessor(qualifier = "retirementDateExpression", type = Accessor.Type.GETTER)
	public String getRetirementDateExpression()
	{
		return getPersistenceContext().getPropertyValue(RETIREMENTDATEEXPRESSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AfterRetentionCleanupRule.retirementItemType</code> attribute defined at extension <code>processing</code>. 
	 * @return the retirementItemType
	 */
	@Accessor(qualifier = "retirementItemType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getRetirementItemType()
	{
		return getPersistenceContext().getPropertyValue(RETIREMENTITEMTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AfterRetentionCleanupRule.itemFilterExpression</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the itemFilterExpression
	 */
	@Accessor(qualifier = "itemFilterExpression", type = Accessor.Type.SETTER)
	public void setItemFilterExpression(final String value)
	{
		getPersistenceContext().setPropertyValue(ITEMFILTEREXPRESSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AfterRetentionCleanupRule.retentionTimeSeconds</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the retentionTimeSeconds
	 */
	@Accessor(qualifier = "retentionTimeSeconds", type = Accessor.Type.SETTER)
	public void setRetentionTimeSeconds(final Long value)
	{
		getPersistenceContext().setPropertyValue(RETENTIONTIMESECONDS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AfterRetentionCleanupRule.retirementDateAttribute</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the retirementDateAttribute
	 */
	@Accessor(qualifier = "retirementDateAttribute", type = Accessor.Type.SETTER)
	public void setRetirementDateAttribute(final AttributeDescriptorModel value)
	{
		getPersistenceContext().setPropertyValue(RETIREMENTDATEATTRIBUTE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AfterRetentionCleanupRule.retirementDateExpression</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the retirementDateExpression
	 */
	@Accessor(qualifier = "retirementDateExpression", type = Accessor.Type.SETTER)
	public void setRetirementDateExpression(final String value)
	{
		getPersistenceContext().setPropertyValue(RETIREMENTDATEEXPRESSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AfterRetentionCleanupRule.retirementItemType</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the retirementItemType
	 */
	@Accessor(qualifier = "retirementItemType", type = Accessor.Type.SETTER)
	public void setRetirementItemType(final ComposedTypeModel value)
	{
		getPersistenceContext().setPropertyValue(RETIREMENTITEMTYPE, value);
	}
	
}
