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
package de.hybris.platform.customerreview.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.enums.CustomerReviewApprovalType;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CustomerReview first defined at extension customerreview.
 */
@SuppressWarnings("all")
public class CustomerReviewModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CustomerReview";
	
	/**<i>Generated relation code constant for relation <code>ReviewToUserRel</code> defining source attribute <code>user</code> in extension <code>customerreview</code>.</i>*/
	public static final String _REVIEWTOUSERREL = "ReviewToUserRel";
	
	/**<i>Generated relation code constant for relation <code>ReviewToProductRel</code> defining source attribute <code>product</code> in extension <code>customerreview</code>.</i>*/
	public static final String _REVIEWTOPRODUCTREL = "ReviewToProductRel";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerReview.headline</code> attribute defined at extension <code>customerreview</code>. */
	public static final String HEADLINE = "headline";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerReview.comment</code> attribute defined at extension <code>customerreview</code>. */
	public static final String COMMENT = "comment";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerReview.rating</code> attribute defined at extension <code>customerreview</code>. */
	public static final String RATING = "rating";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerReview.blocked</code> attribute defined at extension <code>customerreview</code>. */
	public static final String BLOCKED = "blocked";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerReview.alias</code> attribute defined at extension <code>customerreview</code>. */
	public static final String ALIAS = "alias";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerReview.approvalStatus</code> attribute defined at extension <code>customerreview</code>. */
	public static final String APPROVALSTATUS = "approvalStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerReview.language</code> attribute defined at extension <code>customerreview</code>. */
	public static final String LANGUAGE = "language";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerReview.user</code> attribute defined at extension <code>customerreview</code>. */
	public static final String USER = "user";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerReview.product</code> attribute defined at extension <code>customerreview</code>. */
	public static final String PRODUCT = "product";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CustomerReviewModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CustomerReviewModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _product initial attribute declared by type <code>CustomerReview</code> at extension <code>customerreview</code>
	 * @param _rating initial attribute declared by type <code>CustomerReview</code> at extension <code>customerreview</code>
	 * @param _user initial attribute declared by type <code>CustomerReview</code> at extension <code>customerreview</code>
	 */
	@Deprecated
	public CustomerReviewModel(final ProductModel _product, final Double _rating, final UserModel _user)
	{
		super();
		setProduct(_product);
		setRating(_rating);
		setUser(_user);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _product initial attribute declared by type <code>CustomerReview</code> at extension <code>customerreview</code>
	 * @param _rating initial attribute declared by type <code>CustomerReview</code> at extension <code>customerreview</code>
	 * @param _user initial attribute declared by type <code>CustomerReview</code> at extension <code>customerreview</code>
	 */
	@Deprecated
	public CustomerReviewModel(final ItemModel _owner, final ProductModel _product, final Double _rating, final UserModel _user)
	{
		super();
		setOwner(_owner);
		setProduct(_product);
		setRating(_rating);
		setUser(_user);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerReview.alias</code> attribute defined at extension <code>customerreview</code>. 
	 * @return the alias - It holds the alias of the customer who wrote the review.
	 */
	@Accessor(qualifier = "alias", type = Accessor.Type.GETTER)
	public String getAlias()
	{
		return getPersistenceContext().getPropertyValue(ALIAS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerReview.approvalStatus</code> attribute defined at extension <code>customerreview</code>. 
	 * @return the approvalStatus - Its a review status type - when review is approved it is visible in the frontend.
	 */
	@Accessor(qualifier = "approvalStatus", type = Accessor.Type.GETTER)
	public CustomerReviewApprovalType getApprovalStatus()
	{
		return getPersistenceContext().getPropertyValue(APPROVALSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerReview.blocked</code> attribute defined at extension <code>customerreview</code>. 
	 * @return the blocked - This attribute can be set to true to indicate,
	 * 						that the review should be blocked, eg when it is
	 * 						offensive.
	 */
	@Accessor(qualifier = "blocked", type = Accessor.Type.GETTER)
	public Boolean getBlocked()
	{
		return getPersistenceContext().getPropertyValue(BLOCKED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerReview.comment</code> attribute defined at extension <code>customerreview</code>. 
	 * @return the comment - Comment for the customer review
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
	public String getComment()
	{
		return getPersistenceContext().getPropertyValue(COMMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerReview.headline</code> attribute defined at extension <code>customerreview</code>. 
	 * @return the headline - Headline for the customer review
	 */
	@Accessor(qualifier = "headline", type = Accessor.Type.GETTER)
	public String getHeadline()
	{
		return getPersistenceContext().getPropertyValue(HEADLINE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerReview.language</code> attribute defined at extension <code>customerreview</code>. 
	 * @return the language - It holds the information about language that review was written with.
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public LanguageModel getLanguage()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerReview.product</code> attribute defined at extension <code>customerreview</code>. 
	 * @return the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.GETTER)
	public ProductModel getProduct()
	{
		return getPersistenceContext().getPropertyValue(PRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerReview.rating</code> attribute defined at extension <code>customerreview</code>. 
	 * @return the rating - The rating, between customerreview.minimalrating (lowest rating) and
	 * 						customerreview.maximalrating (highest rating)
	 */
	@Accessor(qualifier = "rating", type = Accessor.Type.GETTER)
	public Double getRating()
	{
		return getPersistenceContext().getPropertyValue(RATING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerReview.user</code> attribute defined at extension <code>customerreview</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		return getPersistenceContext().getPropertyValue(USER);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CustomerReview.alias</code> attribute defined at extension <code>customerreview</code>. 
	 *  
	 * @param value the alias - It holds the alias of the customer who wrote the review.
	 */
	@Accessor(qualifier = "alias", type = Accessor.Type.SETTER)
	public void setAlias(final String value)
	{
		getPersistenceContext().setPropertyValue(ALIAS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CustomerReview.approvalStatus</code> attribute defined at extension <code>customerreview</code>. 
	 *  
	 * @param value the approvalStatus - Its a review status type - when review is approved it is visible in the frontend.
	 */
	@Accessor(qualifier = "approvalStatus", type = Accessor.Type.SETTER)
	public void setApprovalStatus(final CustomerReviewApprovalType value)
	{
		getPersistenceContext().setPropertyValue(APPROVALSTATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CustomerReview.blocked</code> attribute defined at extension <code>customerreview</code>. 
	 *  
	 * @param value the blocked - This attribute can be set to true to indicate,
	 * 						that the review should be blocked, eg when it is
	 * 						offensive.
	 */
	@Accessor(qualifier = "blocked", type = Accessor.Type.SETTER)
	public void setBlocked(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(BLOCKED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CustomerReview.comment</code> attribute defined at extension <code>customerreview</code>. 
	 *  
	 * @param value the comment - Comment for the customer review
	 */
	@Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
	public void setComment(final String value)
	{
		getPersistenceContext().setPropertyValue(COMMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CustomerReview.headline</code> attribute defined at extension <code>customerreview</code>. 
	 *  
	 * @param value the headline - Headline for the customer review
	 */
	@Accessor(qualifier = "headline", type = Accessor.Type.SETTER)
	public void setHeadline(final String value)
	{
		getPersistenceContext().setPropertyValue(HEADLINE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CustomerReview.language</code> attribute defined at extension <code>customerreview</code>. 
	 *  
	 * @param value the language - It holds the information about language that review was written with.
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CustomerReview.product</code> attribute defined at extension <code>customerreview</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.SETTER)
	public void setProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(PRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CustomerReview.rating</code> attribute defined at extension <code>customerreview</code>. 
	 *  
	 * @param value the rating - The rating, between customerreview.minimalrating (lowest rating) and
	 * 						customerreview.maximalrating (highest rating)
	 */
	@Accessor(qualifier = "rating", type = Accessor.Type.SETTER)
	public void setRating(final Double value)
	{
		getPersistenceContext().setPropertyValue(RATING, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CustomerReview.user</code> attribute defined at extension <code>customerreview</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(USER, value);
	}
	
}
