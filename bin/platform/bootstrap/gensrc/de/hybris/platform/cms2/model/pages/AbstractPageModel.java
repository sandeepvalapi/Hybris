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
package de.hybris.platform.cms2.model.pages;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.enums.CmsPageStatus;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type AbstractPage first defined at extension cms2.
 */
@SuppressWarnings("all")
public class AbstractPageModel extends CMSItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "AbstractPage";
	
	/**<i>Generated relation code constant for relation <code>AbstractPage2UserRelation</code> defining source attribute <code>lockedBy</code> in extension <code>cms2</code>.</i>*/
	public static final String _ABSTRACTPAGE2USERRELATION = "AbstractPage2UserRelation";
	
	/**<i>Generated relation code constant for relation <code>LocalizedPageRelation</code> defining source attribute <code>originalPage</code> in extension <code>cms2</code>.</i>*/
	public static final String _LOCALIZEDPAGERELATION = "LocalizedPageRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPage.approvalStatus</code> attribute defined at extension <code>cms2</code>. */
	public static final String APPROVALSTATUS = "approvalStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPage.pageStatus</code> attribute defined at extension <code>cms2</code>. */
	public static final String PAGESTATUS = "pageStatus";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPage.title</code> attribute defined at extension <code>cms2</code>. */
	public static final String TITLE = "title";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPage.masterTemplate</code> attribute defined at extension <code>cms2</code>. */
	public static final String MASTERTEMPLATE = "masterTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPage.defaultPage</code> attribute defined at extension <code>cms2</code>. */
	public static final String DEFAULTPAGE = "defaultPage";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPage.onlyOneRestrictionMustApply</code> attribute defined at extension <code>cms2</code>. */
	public static final String ONLYONERESTRICTIONMUSTAPPLY = "onlyOneRestrictionMustApply";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPage.previewImage</code> attribute defined at extension <code>cms2</code>. */
	public static final String PREVIEWIMAGE = "previewImage";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPage.contentSlots</code> attribute defined at extension <code>cms2</code>. */
	public static final String CONTENTSLOTS = "contentSlots";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPage.type</code> attribute defined at extension <code>cms2</code>. */
	public static final String TYPE = "type";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPage.typeCode</code> attribute defined at extension <code>cms2</code>. */
	public static final String TYPECODE = "typeCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPage.missingContentSlots</code> attribute defined at extension <code>cms2</code>. */
	public static final String MISSINGCONTENTSLOTS = "missingContentSlots";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPage.availableContentSlots</code> attribute defined at extension <code>cms2</code>. */
	public static final String AVAILABLECONTENTSLOTS = "availableContentSlots";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPage.view</code> attribute defined at extension <code>cms2</code>. */
	public static final String VIEW = "view";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPage.navigationNodeList</code> attribute defined at extension <code>cms2</code>. */
	public static final String NAVIGATIONNODELIST = "navigationNodeList";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPage.copyToCatalogsDisabled</code> attribute defined at extension <code>cms2</code>. */
	public static final String COPYTOCATALOGSDISABLED = "copyToCatalogsDisabled";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPage.restrictions</code> attribute defined at extension <code>cms2</code>. */
	public static final String RESTRICTIONS = "restrictions";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPage.lockedBy</code> attribute defined at extension <code>cms2</code>. */
	public static final String LOCKEDBY = "lockedBy";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPage.originalPage</code> attribute defined at extension <code>cms2</code>. */
	public static final String ORIGINALPAGE = "originalPage";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractPage.localizedPages</code> attribute defined at extension <code>cms2</code>. */
	public static final String LOCALIZEDPAGES = "localizedPages";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractPageModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractPageModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _masterTemplate initial attribute declared by type <code>AbstractPage</code> at extension <code>cms2</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public AbstractPageModel(final CatalogVersionModel _catalogVersion, final PageTemplateModel _masterTemplate, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setMasterTemplate(_masterTemplate);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _masterTemplate initial attribute declared by type <code>AbstractPage</code> at extension <code>cms2</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public AbstractPageModel(final CatalogVersionModel _catalogVersion, final PageTemplateModel _masterTemplate, final ItemModel _owner, final String _uid)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setMasterTemplate(_masterTemplate);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.approvalStatus</code> attribute defined at extension <code>cms2</code>. 
	 * @return the approvalStatus
	 */
	@Accessor(qualifier = "approvalStatus", type = Accessor.Type.GETTER)
	public CmsApprovalStatus getApprovalStatus()
	{
		return getPersistenceContext().getPropertyValue(APPROVALSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.availableContentSlots</code> attribute defined at extension <code>cms2</code>. 
	 * @return the availableContentSlots
	 * @deprecated since 4.3
	 */
	@Deprecated
	@Accessor(qualifier = "availableContentSlots", type = Accessor.Type.GETTER)
	public String getAvailableContentSlots()
	{
		return getPersistenceContext().getPropertyValue(AVAILABLECONTENTSLOTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.contentSlots</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the contentSlots
	 * @deprecated since 4.3
	 */
	@Deprecated
	@Accessor(qualifier = "contentSlots", type = Accessor.Type.GETTER)
	public List<ContentSlotForPageModel> getContentSlots()
	{
		return getPersistenceContext().getPropertyValue(CONTENTSLOTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.defaultPage</code> attribute defined at extension <code>cms2</code>. 
	 * @return the defaultPage
	 */
	@Accessor(qualifier = "defaultPage", type = Accessor.Type.GETTER)
	public Boolean getDefaultPage()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTPAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.localizedPages</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the localizedPages
	 */
	@Accessor(qualifier = "localizedPages", type = Accessor.Type.GETTER)
	public Collection<AbstractPageModel> getLocalizedPages()
	{
		return getPersistenceContext().getPropertyValue(LOCALIZEDPAGES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.lockedBy</code> attribute defined at extension <code>cms2</code>. 
	 * @return the lockedBy
	 */
	@Accessor(qualifier = "lockedBy", type = Accessor.Type.GETTER)
	public UserModel getLockedBy()
	{
		return getPersistenceContext().getPropertyValue(LOCKEDBY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.masterTemplate</code> attribute defined at extension <code>cms2</code>. 
	 * @return the masterTemplate
	 */
	@Accessor(qualifier = "masterTemplate", type = Accessor.Type.GETTER)
	public PageTemplateModel getMasterTemplate()
	{
		return getPersistenceContext().getPropertyValue(MASTERTEMPLATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.missingContentSlots</code> attribute defined at extension <code>cms2</code>. 
	 * @return the missingContentSlots
	 * @deprecated since 4.3
	 */
	@Deprecated
	@Accessor(qualifier = "missingContentSlots", type = Accessor.Type.GETTER)
	public String getMissingContentSlots()
	{
		return getPersistenceContext().getPropertyValue(MISSINGCONTENTSLOTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.navigationNodeList</code> dynamic attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the navigationNodeList
	 */
	@Accessor(qualifier = "navigationNodeList", type = Accessor.Type.GETTER)
	public List<CMSNavigationNodeModel> getNavigationNodeList()
	{
		return getPersistenceContext().getDynamicValue(this,NAVIGATIONNODELIST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.originalPage</code> attribute defined at extension <code>cms2</code>. 
	 * @return the originalPage
	 */
	@Accessor(qualifier = "originalPage", type = Accessor.Type.GETTER)
	public AbstractPageModel getOriginalPage()
	{
		return getPersistenceContext().getPropertyValue(ORIGINALPAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.pageStatus</code> attribute defined at extension <code>cms2</code>. 
	 * @return the pageStatus
	 */
	@Accessor(qualifier = "pageStatus", type = Accessor.Type.GETTER)
	public CmsPageStatus getPageStatus()
	{
		return getPersistenceContext().getPropertyValue(PAGESTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.previewImage</code> attribute defined at extension <code>cms2</code>. 
	 * @return the previewImage
	 */
	@Accessor(qualifier = "previewImage", type = Accessor.Type.GETTER)
	public MediaModel getPreviewImage()
	{
		return getPersistenceContext().getPropertyValue(PREVIEWIMAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.restrictions</code> attribute defined at extension <code>cms2</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the restrictions
	 */
	@Accessor(qualifier = "restrictions", type = Accessor.Type.GETTER)
	public List<AbstractRestrictionModel> getRestrictions()
	{
		return getPersistenceContext().getPropertyValue(RESTRICTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.title</code> attribute defined at extension <code>cms2</code>. 
	 * @return the title
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle()
	{
		return getTitle(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.title</code> attribute defined at extension <code>cms2</code>. 
	 * @param loc the value localization key 
	 * @return the title
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(TITLE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.type</code> attribute defined at extension <code>cms2</code>. 
	 * @return the type
	 * @deprecated since 4.3
	 */
	@Deprecated
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public String getType()
	{
		return getType(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.type</code> attribute defined at extension <code>cms2</code>. 
	 * @param loc the value localization key 
	 * @return the type
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Deprecated
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public String getType(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(TYPE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.typeCode</code> attribute defined at extension <code>cms2</code>. 
	 * @return the typeCode
	 * @deprecated since 4.3
	 */
	@Deprecated
	@Accessor(qualifier = "typeCode", type = Accessor.Type.GETTER)
	public String getTypeCode()
	{
		return getPersistenceContext().getPropertyValue(TYPECODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.view</code> attribute defined at extension <code>cms2</code>. 
	 * @return the view
	 * @deprecated since 4.3
	 */
	@Deprecated
	@Accessor(qualifier = "view", type = Accessor.Type.GETTER)
	public String getView()
	{
		return getPersistenceContext().getPropertyValue(VIEW);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.copyToCatalogsDisabled</code> attribute defined at extension <code>cms2</code>. 
	 * @return the copyToCatalogsDisabled - Determines whether a local version of the page can be copied to another content catalog.
	 */
	@Accessor(qualifier = "copyToCatalogsDisabled", type = Accessor.Type.GETTER)
	public boolean isCopyToCatalogsDisabled()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(COPYTOCATALOGSDISABLED));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractPage.onlyOneRestrictionMustApply</code> attribute defined at extension <code>cms2</code>. 
	 * @return the onlyOneRestrictionMustApply
	 */
	@Accessor(qualifier = "onlyOneRestrictionMustApply", type = Accessor.Type.GETTER)
	public boolean isOnlyOneRestrictionMustApply()
	{
		return toPrimitive((Boolean)getPersistenceContext().getPropertyValue(ONLYONERESTRICTIONMUSTAPPLY));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPage.approvalStatus</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the approvalStatus
	 */
	@Accessor(qualifier = "approvalStatus", type = Accessor.Type.SETTER)
	public void setApprovalStatus(final CmsApprovalStatus value)
	{
		getPersistenceContext().setPropertyValue(APPROVALSTATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPage.copyToCatalogsDisabled</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the copyToCatalogsDisabled - Determines whether a local version of the page can be copied to another content catalog.
	 */
	@Accessor(qualifier = "copyToCatalogsDisabled", type = Accessor.Type.SETTER)
	public void setCopyToCatalogsDisabled(final boolean value)
	{
		getPersistenceContext().setPropertyValue(COPYTOCATALOGSDISABLED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPage.defaultPage</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the defaultPage
	 */
	@Accessor(qualifier = "defaultPage", type = Accessor.Type.SETTER)
	public void setDefaultPage(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTPAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPage.localizedPages</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the localizedPages
	 */
	@Accessor(qualifier = "localizedPages", type = Accessor.Type.SETTER)
	public void setLocalizedPages(final Collection<AbstractPageModel> value)
	{
		getPersistenceContext().setPropertyValue(LOCALIZEDPAGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPage.lockedBy</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the lockedBy
	 */
	@Accessor(qualifier = "lockedBy", type = Accessor.Type.SETTER)
	public void setLockedBy(final UserModel value)
	{
		getPersistenceContext().setPropertyValue(LOCKEDBY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPage.masterTemplate</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the masterTemplate
	 */
	@Accessor(qualifier = "masterTemplate", type = Accessor.Type.SETTER)
	public void setMasterTemplate(final PageTemplateModel value)
	{
		getPersistenceContext().setPropertyValue(MASTERTEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPage.navigationNodeList</code> dynamic attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the navigationNodeList
	 */
	@Accessor(qualifier = "navigationNodeList", type = Accessor.Type.SETTER)
	public void setNavigationNodeList(final List<CMSNavigationNodeModel> value)
	{
		getPersistenceContext().setDynamicValue(this,NAVIGATIONNODELIST, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPage.onlyOneRestrictionMustApply</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the onlyOneRestrictionMustApply
	 */
	@Accessor(qualifier = "onlyOneRestrictionMustApply", type = Accessor.Type.SETTER)
	public void setOnlyOneRestrictionMustApply(final boolean value)
	{
		getPersistenceContext().setPropertyValue(ONLYONERESTRICTIONMUSTAPPLY, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPage.originalPage</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the originalPage
	 */
	@Accessor(qualifier = "originalPage", type = Accessor.Type.SETTER)
	public void setOriginalPage(final AbstractPageModel value)
	{
		getPersistenceContext().setPropertyValue(ORIGINALPAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPage.pageStatus</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the pageStatus
	 */
	@Accessor(qualifier = "pageStatus", type = Accessor.Type.SETTER)
	public void setPageStatus(final CmsPageStatus value)
	{
		getPersistenceContext().setPropertyValue(PAGESTATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPage.previewImage</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the previewImage
	 */
	@Accessor(qualifier = "previewImage", type = Accessor.Type.SETTER)
	public void setPreviewImage(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(PREVIEWIMAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPage.restrictions</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the restrictions
	 */
	@Accessor(qualifier = "restrictions", type = Accessor.Type.SETTER)
	public void setRestrictions(final List<AbstractRestrictionModel> value)
	{
		getPersistenceContext().setPropertyValue(RESTRICTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPage.title</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the title
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value)
	{
		setTitle(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractPage.title</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the title
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(TITLE, loc, value);
	}
	
}
