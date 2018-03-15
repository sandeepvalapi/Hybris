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
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.model.CodeGenerationConfigurationModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type MultiCodeCoupon first defined at extension couponservices.
 * <p>
 * A MultiCodeCoupon uses generated coupon codes for coupon redemption. The (inherited) couponId attribute functions as a prefix for the generated coupon codes.
 */
@SuppressWarnings("all")
public class MultiCodeCouponModel extends AbstractCouponModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "MultiCodeCoupon";
	
	/** <i>Generated constant</i> - Attribute key of <code>MultiCodeCoupon.codeGenerationConfiguration</code> attribute defined at extension <code>couponservices</code>. */
	public static final String CODEGENERATIONCONFIGURATION = "codeGenerationConfiguration";
	
	/** <i>Generated constant</i> - Attribute key of <code>MultiCodeCoupon.generatedCodes</code> attribute defined at extension <code>couponservices</code>. */
	public static final String GENERATEDCODES = "generatedCodes";
	
	/** <i>Generated constant</i> - Attribute key of <code>MultiCodeCoupon.alphabet</code> attribute defined at extension <code>couponservices</code>. */
	public static final String ALPHABET = "alphabet";
	
	/** <i>Generated constant</i> - Attribute key of <code>MultiCodeCoupon.signature</code> attribute defined at extension <code>couponservices</code>. */
	public static final String SIGNATURE = "signature";
	
	/** <i>Generated constant</i> - Attribute key of <code>MultiCodeCoupon.couponCodeNumber</code> attribute defined at extension <code>couponservices</code>. */
	public static final String COUPONCODENUMBER = "couponCodeNumber";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MultiCodeCouponModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MultiCodeCouponModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _codeGenerationConfiguration initial attribute declared by type <code>MultiCodeCoupon</code> at extension <code>couponservices</code>
	 * @param _couponId initial attribute declared by type <code>AbstractCoupon</code> at extension <code>couponservices</code>
	 */
	@Deprecated
	public MultiCodeCouponModel(final CodeGenerationConfigurationModel _codeGenerationConfiguration, final String _couponId)
	{
		super();
		setCodeGenerationConfiguration(_codeGenerationConfiguration);
		setCouponId(_couponId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _alphabet initial attribute declared by type <code>MultiCodeCoupon</code> at extension <code>couponservices</code>
	 * @param _codeGenerationConfiguration initial attribute declared by type <code>MultiCodeCoupon</code> at extension <code>couponservices</code>
	 * @param _couponId initial attribute declared by type <code>AbstractCoupon</code> at extension <code>couponservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _signature initial attribute declared by type <code>MultiCodeCoupon</code> at extension <code>couponservices</code>
	 */
	@Deprecated
	public MultiCodeCouponModel(final String _alphabet, final CodeGenerationConfigurationModel _codeGenerationConfiguration, final String _couponId, final ItemModel _owner, final String _signature)
	{
		super();
		setAlphabet(_alphabet);
		setCodeGenerationConfiguration(_codeGenerationConfiguration);
		setCouponId(_couponId);
		setOwner(_owner);
		setSignature(_signature);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MultiCodeCoupon.alphabet</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the alphabet - used for coupon code generation. The alphabet is a 16 character long distinct set of characters.
	 */
	@Accessor(qualifier = "alphabet", type = Accessor.Type.GETTER)
	public String getAlphabet()
	{
		return getPersistenceContext().getPropertyValue(ALPHABET);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MultiCodeCoupon.codeGenerationConfiguration</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the codeGenerationConfiguration
	 */
	@Accessor(qualifier = "codeGenerationConfiguration", type = Accessor.Type.GETTER)
	public CodeGenerationConfigurationModel getCodeGenerationConfiguration()
	{
		return getPersistenceContext().getPropertyValue(CODEGENERATIONCONFIGURATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MultiCodeCoupon.couponCodeNumber</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the couponCodeNumber - used for coupon code generation. The coupon code number functions as a seed for coupon codes.
	 */
	@Accessor(qualifier = "couponCodeNumber", type = Accessor.Type.GETTER)
	public Long getCouponCodeNumber()
	{
		return getPersistenceContext().getPropertyValue(COUPONCODENUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MultiCodeCoupon.generatedCodes</code> attribute defined at extension <code>couponservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the generatedCodes
	 */
	@Accessor(qualifier = "generatedCodes", type = Accessor.Type.GETTER)
	public Collection<MediaModel> getGeneratedCodes()
	{
		return getPersistenceContext().getPropertyValue(GENERATEDCODES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MultiCodeCoupon.signature</code> attribute defined at extension <code>couponservices</code>. 
	 * @return the signature - used for coupon code generation. The signature is a base64 encoded signature used for the ciphertext part of the coupon code.
	 */
	@Accessor(qualifier = "signature", type = Accessor.Type.GETTER)
	public String getSignature()
	{
		return getPersistenceContext().getPropertyValue(SIGNATURE);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>MultiCodeCoupon.alphabet</code> attribute defined at extension <code>couponservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the alphabet - used for coupon code generation. The alphabet is a 16 character long distinct set of characters.
	 */
	@Accessor(qualifier = "alphabet", type = Accessor.Type.SETTER)
	public void setAlphabet(final String value)
	{
		getPersistenceContext().setPropertyValue(ALPHABET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MultiCodeCoupon.codeGenerationConfiguration</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the codeGenerationConfiguration
	 */
	@Accessor(qualifier = "codeGenerationConfiguration", type = Accessor.Type.SETTER)
	public void setCodeGenerationConfiguration(final CodeGenerationConfigurationModel value)
	{
		getPersistenceContext().setPropertyValue(CODEGENERATIONCONFIGURATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MultiCodeCoupon.couponCodeNumber</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the couponCodeNumber - used for coupon code generation. The coupon code number functions as a seed for coupon codes.
	 */
	@Accessor(qualifier = "couponCodeNumber", type = Accessor.Type.SETTER)
	public void setCouponCodeNumber(final Long value)
	{
		getPersistenceContext().setPropertyValue(COUPONCODENUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MultiCodeCoupon.generatedCodes</code> attribute defined at extension <code>couponservices</code>. 
	 *  
	 * @param value the generatedCodes
	 */
	@Accessor(qualifier = "generatedCodes", type = Accessor.Type.SETTER)
	public void setGeneratedCodes(final Collection<MediaModel> value)
	{
		getPersistenceContext().setPropertyValue(GENERATEDCODES, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>MultiCodeCoupon.signature</code> attribute defined at extension <code>couponservices</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the signature - used for coupon code generation. The signature is a base64 encoded signature used for the ciphertext part of the coupon code.
	 */
	@Accessor(qualifier = "signature", type = Accessor.Type.SETTER)
	public void setSignature(final String value)
	{
		getPersistenceContext().setPropertyValue(SIGNATURE, value);
	}
	
}
