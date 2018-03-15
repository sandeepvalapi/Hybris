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
package de.hybris.platform.couponservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CodeGenerationConfiguration first defined at extension couponservices.
 */
@SuppressWarnings("all")
public class CodeGenerationConfigurationModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CodeGenerationConfiguration";
	
	/** <i>Generated constant</i> - Attribute key of <code>CodeGenerationConfiguration.name</code> attribute defined at extension <code>couponservices</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>CodeGenerationConfiguration.codeSeparator</code> attribute defined at extension <code>couponservices</code>. */
	public static final String CODESEPARATOR = "codeSeparator";
	
	/** <i>Generated constant</i> - Attribute key of <code>CodeGenerationConfiguration.couponPartCount</code> attribute defined at extension <code>couponservices</code>. */
	public static final String COUPONPARTCOUNT = "couponPartCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>CodeGenerationConfiguration.couponPartLength</code> attribute defined at extension <code>couponservices</code>. */
	public static final String COUPONPARTLENGTH = "couponPartLength";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CodeGenerationConfigurationModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CodeGenerationConfigurationModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>CodeGenerationConfiguration</code> at extension <code>couponservices</code>
	 */
	@Deprecated
	public CodeGenerationConfigurationModel(final String _name)
	{
		super();
		setName(_name);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _name initial attribute declared by type <code>CodeGenerationConfiguration</code> at extension <code>couponservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CodeGenerationConfigurationModel(final String _name, final ItemModel _owner)
	{
		super();
		setName(_name);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CodeGenerationConfiguration.codeSeparator</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the codeSeparator
	 */
	@Accessor(qualifier = "codeSeparator", type = Accessor.Type.GETTER)
	public String getCodeSeparator()
	{
		return getPersistenceContext().getPropertyValue(CODESEPARATOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CodeGenerationConfiguration.couponPartCount</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the couponPartCount
	 */
	@Accessor(qualifier = "couponPartCount", type = Accessor.Type.GETTER)
	public int getCouponPartCount()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(COUPONPARTCOUNT));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CodeGenerationConfiguration.couponPartLength</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the couponPartLength
	 */
	@Accessor(qualifier = "couponPartLength", type = Accessor.Type.GETTER)
	public int getCouponPartLength()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(COUPONPARTLENGTH));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CodeGenerationConfiguration.name</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CodeGenerationConfiguration.codeSeparator</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the codeSeparator
	 */
	@Accessor(qualifier = "codeSeparator", type = Accessor.Type.SETTER)
	public void setCodeSeparator(final String value)
	{
		getPersistenceContext().setPropertyValue(CODESEPARATOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CodeGenerationConfiguration.couponPartCount</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the couponPartCount
	 */
	@Accessor(qualifier = "couponPartCount", type = Accessor.Type.SETTER)
	public void setCouponPartCount(final int value)
	{
		getPersistenceContext().setPropertyValue(COUPONPARTCOUNT, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CodeGenerationConfiguration.couponPartLength</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the couponPartLength
	 */
	@Accessor(qualifier = "couponPartLength", type = Accessor.Type.SETTER)
	public void setCouponPartLength(final int value)
	{
		getPersistenceContext().setPropertyValue(COUPONPARTLENGTH, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CodeGenerationConfiguration.name</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
}
