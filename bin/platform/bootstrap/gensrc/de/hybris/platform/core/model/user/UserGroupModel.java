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
package de.hybris.platform.core.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cms2.model.restrictions.CMSUserGroupRestrictionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.europe1.enums.UserDiscountGroup;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.enums.UserTaxGroup;
import de.hybris.platform.platformbackoffice.model.BackofficeSavedQueryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type UserGroup first defined at extension core.
 */
@SuppressWarnings("all")
public class UserGroupModel extends PrincipalGroupModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "UserGroup";
	
	/**<i>Generated relation code constant for relation <code>UserGroupsForRestriction</code> defining source attribute <code>restrictions</code> in extension <code>cms2</code>.</i>*/
	public static final String _USERGROUPSFORRESTRICTION = "UserGroupsForRestriction";
	
	/**<i>Generated relation code constant for relation <code>BackofficeSavedQuery2UserGroupRelation</code> defining source attribute <code>savedQueries</code> in extension <code>platformbackoffice</code>.</i>*/
	public static final String _BACKOFFICESAVEDQUERY2USERGROUPRELATION = "BackofficeSavedQuery2UserGroupRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserGroup.writeableLanguages</code> attribute defined at extension <code>core</code>. */
	public static final String WRITEABLELANGUAGES = "writeableLanguages";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserGroup.readableLanguages</code> attribute defined at extension <code>core</code>. */
	public static final String READABLELANGUAGES = "readableLanguages";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserGroup.hmcXML</code> attribute defined at extension <code>core</code>. */
	public static final String HMCXML = "hmcXML";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserGroup.userDiscountGroup</code> attribute defined at extension <code>europe1</code>. */
	public static final String USERDISCOUNTGROUP = "userDiscountGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserGroup.userPriceGroup</code> attribute defined at extension <code>europe1</code>. */
	public static final String USERPRICEGROUP = "userPriceGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserGroup.userTaxGroup</code> attribute defined at extension <code>europe1</code>. */
	public static final String USERTAXGROUP = "userTaxGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserGroup.restrictions</code> attribute defined at extension <code>cms2</code>. */
	public static final String RESTRICTIONS = "restrictions";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserGroup.savedQueries</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String SAVEDQUERIES = "savedQueries";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public UserGroupModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public UserGroupModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public UserGroupModel(final String _uid)
	{
		super();
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public UserGroupModel(final ItemModel _owner, final String _uid)
	{
		super();
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserGroup.hmcXML</code> attribute defined at extension <code>core</code>. 
	 * @return the hmcXML
	 */
	@Accessor(qualifier = "hmcXML", type = Accessor.Type.GETTER)
	public String getHmcXML()
	{
		return getPersistenceContext().getPropertyValue(HMCXML);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserGroup.readableLanguages</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the readableLanguages
	 */
	@Accessor(qualifier = "readableLanguages", type = Accessor.Type.GETTER)
	public Collection<LanguageModel> getReadableLanguages()
	{
		return getPersistenceContext().getPropertyValue(READABLELANGUAGES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserGroup.restrictions</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the restrictions
	 */
	@Accessor(qualifier = "restrictions", type = Accessor.Type.GETTER)
	public Collection<CMSUserGroupRestrictionModel> getRestrictions()
	{
		return getPersistenceContext().getPropertyValue(RESTRICTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserGroup.savedQueries</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the savedQueries
	 */
	@Accessor(qualifier = "savedQueries", type = Accessor.Type.GETTER)
	public Collection<BackofficeSavedQueryModel> getSavedQueries()
	{
		return getPersistenceContext().getPropertyValue(SAVEDQUERIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserGroup.userDiscountGroup</code> attribute defined at extension <code>europe1</code>. 
	 * @return the userDiscountGroup
	 */
	@Accessor(qualifier = "userDiscountGroup", type = Accessor.Type.GETTER)
	public UserDiscountGroup getUserDiscountGroup()
	{
		return getPersistenceContext().getPropertyValue(USERDISCOUNTGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserGroup.userPriceGroup</code> attribute defined at extension <code>europe1</code>. 
	 * @return the userPriceGroup
	 */
	@Accessor(qualifier = "userPriceGroup", type = Accessor.Type.GETTER)
	public UserPriceGroup getUserPriceGroup()
	{
		return getPersistenceContext().getPropertyValue(USERPRICEGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserGroup.userTaxGroup</code> attribute defined at extension <code>europe1</code>. 
	 * @return the userTaxGroup
	 */
	@Accessor(qualifier = "userTaxGroup", type = Accessor.Type.GETTER)
	public UserTaxGroup getUserTaxGroup()
	{
		return getPersistenceContext().getPropertyValue(USERTAXGROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserGroup.writeableLanguages</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the writeableLanguages
	 */
	@Accessor(qualifier = "writeableLanguages", type = Accessor.Type.GETTER)
	public Collection<LanguageModel> getWriteableLanguages()
	{
		return getPersistenceContext().getPropertyValue(WRITEABLELANGUAGES);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserGroup.hmcXML</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the hmcXML
	 */
	@Accessor(qualifier = "hmcXML", type = Accessor.Type.SETTER)
	public void setHmcXML(final String value)
	{
		getPersistenceContext().setPropertyValue(HMCXML, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserGroup.readableLanguages</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the readableLanguages
	 */
	@Accessor(qualifier = "readableLanguages", type = Accessor.Type.SETTER)
	public void setReadableLanguages(final Collection<LanguageModel> value)
	{
		getPersistenceContext().setPropertyValue(READABLELANGUAGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserGroup.restrictions</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the restrictions
	 */
	@Accessor(qualifier = "restrictions", type = Accessor.Type.SETTER)
	public void setRestrictions(final Collection<CMSUserGroupRestrictionModel> value)
	{
		getPersistenceContext().setPropertyValue(RESTRICTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserGroup.savedQueries</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the savedQueries
	 */
	@Accessor(qualifier = "savedQueries", type = Accessor.Type.SETTER)
	public void setSavedQueries(final Collection<BackofficeSavedQueryModel> value)
	{
		getPersistenceContext().setPropertyValue(SAVEDQUERIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserGroup.userDiscountGroup</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the userDiscountGroup
	 */
	@Accessor(qualifier = "userDiscountGroup", type = Accessor.Type.SETTER)
	public void setUserDiscountGroup(final UserDiscountGroup value)
	{
		getPersistenceContext().setPropertyValue(USERDISCOUNTGROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserGroup.userPriceGroup</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the userPriceGroup
	 */
	@Accessor(qualifier = "userPriceGroup", type = Accessor.Type.SETTER)
	public void setUserPriceGroup(final UserPriceGroup value)
	{
		getPersistenceContext().setPropertyValue(USERPRICEGROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserGroup.userTaxGroup</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the userTaxGroup
	 */
	@Accessor(qualifier = "userTaxGroup", type = Accessor.Type.SETTER)
	public void setUserTaxGroup(final UserTaxGroup value)
	{
		getPersistenceContext().setPropertyValue(USERTAXGROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserGroup.writeableLanguages</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the writeableLanguages
	 */
	@Accessor(qualifier = "writeableLanguages", type = Accessor.Type.SETTER)
	public void setWriteableLanguages(final Collection<LanguageModel> value)
	{
		getPersistenceContext().setPropertyValue(WRITEABLELANGUAGES, value);
	}
	
}
