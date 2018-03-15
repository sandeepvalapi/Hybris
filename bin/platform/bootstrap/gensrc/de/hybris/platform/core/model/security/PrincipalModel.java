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
package de.hybris.platform.core.model.security;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.model.CockpitObjectAbstractCollectionModel;
import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.cockpit.model.CockpitUIComponentAccessRightModel;
import de.hybris.platform.cockpit.model.CockpitUIComponentConfigurationModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.mcc.model.AbstractLinkEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type Principal first defined at extension core.
 */
@SuppressWarnings("all")
public class PrincipalModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "Principal";
	
	/**<i>Generated relation code constant for relation <code>Category2PrincipalRelation</code> defining source attribute <code>accessibleCategories</code> in extension <code>catalog</code>.</i>*/
	public static final String _CATEGORY2PRINCIPALRELATION = "Category2PrincipalRelation";
	
	/**<i>Generated relation code constant for relation <code>SyncItemJob2Principal</code> defining source attribute <code>syncJobs</code> in extension <code>catalog</code>.</i>*/
	public static final String _SYNCITEMJOB2PRINCIPAL = "SyncItemJob2Principal";
	
	/**<i>Generated relation code constant for relation <code>WorkflowTemplate2PrincipalRelation</code> defining source attribute <code>visibleTemplates</code> in extension <code>workflow</code>.</i>*/
	public static final String _WORKFLOWTEMPLATE2PRINCIPALRELATION = "WorkflowTemplate2PrincipalRelation";
	
	/**<i>Generated relation code constant for relation <code>CommentWatcherRelation</code> defining source attribute <code>watchedComments</code> in extension <code>comments</code>.</i>*/
	public static final String _COMMENTWATCHERRELATION = "CommentWatcherRelation";
	
	/**<i>Generated relation code constant for relation <code>Principal2CockpitUIComponentReadAccessRelation</code> defining source attribute <code>readableCockpitUIComponents</code> in extension <code>cockpit</code>.</i>*/
	public static final String _PRINCIPAL2COCKPITUICOMPONENTREADACCESSRELATION = "Principal2CockpitUIComponentReadAccessRelation";
	
	/**<i>Generated relation code constant for relation <code>Principal2CockpitUIComponentWriteAccessRelation</code> defining source attribute <code>writableCockpitUIComponents</code> in extension <code>cockpit</code>.</i>*/
	public static final String _PRINCIPAL2COCKPITUICOMPONENTWRITEACCESSRELATION = "Principal2CockpitUIComponentWriteAccessRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.description</code> attribute defined at extension <code>core</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.name</code> attribute defined at extension <code>core</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.displayName</code> attribute defined at extension <code>core</code>. */
	public static final String DISPLAYNAME = "displayName";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.uid</code> attribute defined at extension <code>core</code>. */
	public static final String UID = "uid";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.allSearchRestrictions</code> attribute defined at extension <code>core</code>. */
	public static final String ALLSEARCHRESTRICTIONS = "allSearchRestrictions";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.allGroups</code> attribute defined at extension <code>core</code>. */
	public static final String ALLGROUPS = "allGroups";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.groups</code> attribute defined at extension <code>core</code>. */
	public static final String GROUPS = "groups";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.searchRestrictions</code> attribute defined at extension <code>core</code>. */
	public static final String SEARCHRESTRICTIONS = "searchRestrictions";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.accessibleCategories</code> attribute defined at extension <code>catalog</code>. */
	public static final String ACCESSIBLECATEGORIES = "accessibleCategories";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.writableCatalogVersions</code> attribute defined at extension <code>catalog</code>. */
	public static final String WRITABLECATALOGVERSIONS = "writableCatalogVersions";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.readableCatalogVersions</code> attribute defined at extension <code>catalog</code>. */
	public static final String READABLECATALOGVERSIONS = "readableCatalogVersions";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.syncJobs</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYNCJOBS = "syncJobs";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.visibleTemplates</code> attribute defined at extension <code>workflow</code>. */
	public static final String VISIBLETEMPLATES = "visibleTemplates";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.profilePicture</code> attribute defined at extension <code>comments</code>. */
	public static final String PROFILEPICTURE = "profilePicture";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.watchedComments</code> attribute defined at extension <code>comments</code>. */
	public static final String WATCHEDCOMMENTS = "watchedComments";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.backOfficeLoginDisabled</code> attribute defined at extension <code>backoffice</code>. */
	public static final String BACKOFFICELOGINDISABLED = "backOfficeLoginDisabled";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.readCollections</code> attribute defined at extension <code>cockpit</code>. */
	public static final String READCOLLECTIONS = "readCollections";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.writeCollections</code> attribute defined at extension <code>cockpit</code>. */
	public static final String WRITECOLLECTIONS = "writeCollections";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.cockpitUIConfigurations</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COCKPITUICONFIGURATIONS = "cockpitUIConfigurations";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.readableCockpitUIComponents</code> attribute defined at extension <code>cockpit</code>. */
	public static final String READABLECOCKPITUICOMPONENTS = "readableCockpitUIComponents";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.writableCockpitUIComponents</code> attribute defined at extension <code>cockpit</code>. */
	public static final String WRITABLECOCKPITUICOMPONENTS = "writableCockpitUIComponents";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.readSavedQueries</code> attribute defined at extension <code>cockpit</code>. */
	public static final String READSAVEDQUERIES = "readSavedQueries";
	
	/** <i>Generated constant</i> - Attribute key of <code>Principal.readAbstractLinkEntry</code> attribute defined at extension <code>mcc</code>. */
	public static final String READABSTRACTLINKENTRY = "readAbstractLinkEntry";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PrincipalModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PrincipalModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public PrincipalModel(final String _uid)
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
	public PrincipalModel(final ItemModel _owner, final String _uid)
	{
		super();
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.accessibleCategories</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the accessibleCategories - catalog categories which are accessible for this principal
	 */
	@Accessor(qualifier = "accessibleCategories", type = Accessor.Type.GETTER)
	public Collection<CategoryModel> getAccessibleCategories()
	{
		return getPersistenceContext().getPropertyValue(ACCESSIBLECATEGORIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.allGroups</code> dynamic attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allGroups
	 * @deprecated since ages - use { @link #getAllGroups()} instead
	 */
	@Deprecated
	public Set<PrincipalGroupModel> getAllgroups()
	{
		return this.getAllGroups();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.allGroups</code> dynamic attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allGroups
	 */
	@Accessor(qualifier = "allGroups", type = Accessor.Type.GETTER)
	public Set<PrincipalGroupModel> getAllGroups()
	{
		return getPersistenceContext().getDynamicValue(this,ALLGROUPS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.allSearchRestrictions</code> dynamic attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allSearchRestrictions
	 * @deprecated since ages - use { @link #getAllSearchRestrictions()} instead
	 */
	@Deprecated
	public Collection<SearchRestrictionModel> getAllsearchrestrictions()
	{
		return this.getAllSearchRestrictions();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.allSearchRestrictions</code> dynamic attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the allSearchRestrictions
	 */
	@Accessor(qualifier = "allSearchRestrictions", type = Accessor.Type.GETTER)
	public Collection<SearchRestrictionModel> getAllSearchRestrictions()
	{
		return getPersistenceContext().getDynamicValue(this,ALLSEARCHRESTRICTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.backOfficeLoginDisabled</code> attribute defined at extension <code>backoffice</code>. 
	 * @return the backOfficeLoginDisabled
	 */
	@Accessor(qualifier = "backOfficeLoginDisabled", type = Accessor.Type.GETTER)
	public Boolean getBackOfficeLoginDisabled()
	{
		return getPersistenceContext().getPropertyValue(BACKOFFICELOGINDISABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.cockpitUIConfigurations</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the cockpitUIConfigurations
	 */
	@Accessor(qualifier = "cockpitUIConfigurations", type = Accessor.Type.GETTER)
	public Collection<CockpitUIComponentConfigurationModel> getCockpitUIConfigurations()
	{
		return getPersistenceContext().getPropertyValue(COCKPITUICONFIGURATIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.description</code> attribute defined at extension <code>core</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getPersistenceContext().getPropertyValue(DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.displayName</code> dynamic attribute defined at extension <code>core</code>. 
	 * @return the displayName
	 */
	@Accessor(qualifier = "displayName", type = Accessor.Type.GETTER)
	public String getDisplayName()
	{
		return getDisplayName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.displayName</code> dynamic attribute defined at extension <code>core</code>. 
	 * @param loc the value localization key 
	 * @return the displayName
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "displayName", type = Accessor.Type.GETTER)
	public String getDisplayName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedDynamicValue(this,DISPLAYNAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.groups</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the groups
	 */
	@Accessor(qualifier = "groups", type = Accessor.Type.GETTER)
	public Set<PrincipalGroupModel> getGroups()
	{
		return getPersistenceContext().getPropertyValue(GROUPS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.name</code> attribute defined at extension <code>core</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getPersistenceContext().getPropertyValue(NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.profilePicture</code> attribute defined at extension <code>comments</code>. 
	 * @return the profilePicture
	 */
	@Accessor(qualifier = "profilePicture", type = Accessor.Type.GETTER)
	public MediaModel getProfilePicture()
	{
		return getPersistenceContext().getPropertyValue(PROFILEPICTURE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.readableCatalogVersions</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the readableCatalogVersions
	 */
	@Accessor(qualifier = "readableCatalogVersions", type = Accessor.Type.GETTER)
	public List<CatalogVersionModel> getReadableCatalogVersions()
	{
		return getPersistenceContext().getPropertyValue(READABLECATALOGVERSIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.readableCockpitUIComponents</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the readableCockpitUIComponents
	 */
	@Accessor(qualifier = "readableCockpitUIComponents", type = Accessor.Type.GETTER)
	public Collection<CockpitUIComponentAccessRightModel> getReadableCockpitUIComponents()
	{
		return getPersistenceContext().getPropertyValue(READABLECOCKPITUICOMPONENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.readAbstractLinkEntry</code> attribute defined at extension <code>mcc</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the readAbstractLinkEntry
	 */
	@Accessor(qualifier = "readAbstractLinkEntry", type = Accessor.Type.GETTER)
	public List<AbstractLinkEntryModel> getReadAbstractLinkEntry()
	{
		return getPersistenceContext().getPropertyValue(READABSTRACTLINKENTRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.readCollections</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the readCollections
	 */
	@Accessor(qualifier = "readCollections", type = Accessor.Type.GETTER)
	public Collection<CockpitObjectAbstractCollectionModel> getReadCollections()
	{
		return getPersistenceContext().getPropertyValue(READCOLLECTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.readSavedQueries</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the readSavedQueries
	 */
	@Accessor(qualifier = "readSavedQueries", type = Accessor.Type.GETTER)
	public Collection<CockpitSavedQueryModel> getReadSavedQueries()
	{
		return getPersistenceContext().getPropertyValue(READSAVEDQUERIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.searchRestrictions</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the searchRestrictions
	 * @deprecated since ages - use { @link #getSearchRestrictions()} instead
	 */
	@Deprecated
	public Collection<SearchRestrictionModel> getSearchrestrictions()
	{
		return this.getSearchRestrictions();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.searchRestrictions</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the searchRestrictions
	 */
	@Accessor(qualifier = "searchRestrictions", type = Accessor.Type.GETTER)
	public Collection<SearchRestrictionModel> getSearchRestrictions()
	{
		return getPersistenceContext().getPropertyValue(SEARCHRESTRICTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.syncJobs</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the syncJobs
	 */
	@Accessor(qualifier = "syncJobs", type = Accessor.Type.GETTER)
	public Collection<SyncItemJobModel> getSyncJobs()
	{
		return getPersistenceContext().getPropertyValue(SYNCJOBS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.uid</code> attribute defined at extension <code>core</code>. 
	 * @return the uid
	 */
	@Accessor(qualifier = "uid", type = Accessor.Type.GETTER)
	public String getUid()
	{
		return getPersistenceContext().getPropertyValue(UID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.visibleTemplates</code> attribute defined at extension <code>workflow</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the visibleTemplates
	 */
	@Accessor(qualifier = "visibleTemplates", type = Accessor.Type.GETTER)
	public Collection<WorkflowTemplateModel> getVisibleTemplates()
	{
		return getPersistenceContext().getPropertyValue(VISIBLETEMPLATES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.watchedComments</code> attribute defined at extension <code>comments</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the watchedComments
	 */
	@Accessor(qualifier = "watchedComments", type = Accessor.Type.GETTER)
	public List<CommentModel> getWatchedComments()
	{
		return getPersistenceContext().getPropertyValue(WATCHEDCOMMENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.writableCatalogVersions</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the writableCatalogVersions
	 */
	@Accessor(qualifier = "writableCatalogVersions", type = Accessor.Type.GETTER)
	public List<CatalogVersionModel> getWritableCatalogVersions()
	{
		return getPersistenceContext().getPropertyValue(WRITABLECATALOGVERSIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.writableCockpitUIComponents</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the writableCockpitUIComponents
	 */
	@Accessor(qualifier = "writableCockpitUIComponents", type = Accessor.Type.GETTER)
	public Collection<CockpitUIComponentAccessRightModel> getWritableCockpitUIComponents()
	{
		return getPersistenceContext().getPropertyValue(WRITABLECOCKPITUICOMPONENTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Principal.writeCollections</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the writeCollections
	 */
	@Accessor(qualifier = "writeCollections", type = Accessor.Type.GETTER)
	public Collection<CockpitObjectAbstractCollectionModel> getWriteCollections()
	{
		return getPersistenceContext().getPropertyValue(WRITECOLLECTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.backOfficeLoginDisabled</code> attribute defined at extension <code>backoffice</code>. 
	 *  
	 * @param value the backOfficeLoginDisabled
	 */
	@Accessor(qualifier = "backOfficeLoginDisabled", type = Accessor.Type.SETTER)
	public void setBackOfficeLoginDisabled(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(BACKOFFICELOGINDISABLED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.cockpitUIConfigurations</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the cockpitUIConfigurations
	 */
	@Accessor(qualifier = "cockpitUIConfigurations", type = Accessor.Type.SETTER)
	public void setCockpitUIConfigurations(final Collection<CockpitUIComponentConfigurationModel> value)
	{
		getPersistenceContext().setPropertyValue(COCKPITUICONFIGURATIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.description</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		getPersistenceContext().setPropertyValue(DESCRIPTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.groups</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the groups
	 */
	@Accessor(qualifier = "groups", type = Accessor.Type.SETTER)
	public void setGroups(final Set<PrincipalGroupModel> value)
	{
		getPersistenceContext().setPropertyValue(GROUPS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.name</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		getPersistenceContext().setPropertyValue(NAME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.profilePicture</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the profilePicture
	 */
	@Accessor(qualifier = "profilePicture", type = Accessor.Type.SETTER)
	public void setProfilePicture(final MediaModel value)
	{
		getPersistenceContext().setPropertyValue(PROFILEPICTURE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.readableCatalogVersions</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the readableCatalogVersions
	 */
	@Accessor(qualifier = "readableCatalogVersions", type = Accessor.Type.SETTER)
	public void setReadableCatalogVersions(final List<CatalogVersionModel> value)
	{
		getPersistenceContext().setPropertyValue(READABLECATALOGVERSIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.readableCockpitUIComponents</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the readableCockpitUIComponents
	 */
	@Accessor(qualifier = "readableCockpitUIComponents", type = Accessor.Type.SETTER)
	public void setReadableCockpitUIComponents(final Collection<CockpitUIComponentAccessRightModel> value)
	{
		getPersistenceContext().setPropertyValue(READABLECOCKPITUICOMPONENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.readAbstractLinkEntry</code> attribute defined at extension <code>mcc</code>. 
	 *  
	 * @param value the readAbstractLinkEntry
	 */
	@Accessor(qualifier = "readAbstractLinkEntry", type = Accessor.Type.SETTER)
	public void setReadAbstractLinkEntry(final List<AbstractLinkEntryModel> value)
	{
		getPersistenceContext().setPropertyValue(READABSTRACTLINKENTRY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.readCollections</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the readCollections
	 */
	@Accessor(qualifier = "readCollections", type = Accessor.Type.SETTER)
	public void setReadCollections(final Collection<CockpitObjectAbstractCollectionModel> value)
	{
		getPersistenceContext().setPropertyValue(READCOLLECTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.readSavedQueries</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the readSavedQueries
	 */
	@Accessor(qualifier = "readSavedQueries", type = Accessor.Type.SETTER)
	public void setReadSavedQueries(final Collection<CockpitSavedQueryModel> value)
	{
		getPersistenceContext().setPropertyValue(READSAVEDQUERIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.syncJobs</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the syncJobs
	 */
	@Accessor(qualifier = "syncJobs", type = Accessor.Type.SETTER)
	public void setSyncJobs(final Collection<SyncItemJobModel> value)
	{
		getPersistenceContext().setPropertyValue(SYNCJOBS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.uid</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the uid
	 */
	@Accessor(qualifier = "uid", type = Accessor.Type.SETTER)
	public void setUid(final String value)
	{
		getPersistenceContext().setPropertyValue(UID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.visibleTemplates</code> attribute defined at extension <code>workflow</code>. 
	 *  
	 * @param value the visibleTemplates
	 */
	@Accessor(qualifier = "visibleTemplates", type = Accessor.Type.SETTER)
	public void setVisibleTemplates(final Collection<WorkflowTemplateModel> value)
	{
		getPersistenceContext().setPropertyValue(VISIBLETEMPLATES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.watchedComments</code> attribute defined at extension <code>comments</code>. 
	 *  
	 * @param value the watchedComments
	 */
	@Accessor(qualifier = "watchedComments", type = Accessor.Type.SETTER)
	public void setWatchedComments(final List<CommentModel> value)
	{
		getPersistenceContext().setPropertyValue(WATCHEDCOMMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.writableCatalogVersions</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the writableCatalogVersions
	 */
	@Accessor(qualifier = "writableCatalogVersions", type = Accessor.Type.SETTER)
	public void setWritableCatalogVersions(final List<CatalogVersionModel> value)
	{
		getPersistenceContext().setPropertyValue(WRITABLECATALOGVERSIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.writableCockpitUIComponents</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the writableCockpitUIComponents
	 */
	@Accessor(qualifier = "writableCockpitUIComponents", type = Accessor.Type.SETTER)
	public void setWritableCockpitUIComponents(final Collection<CockpitUIComponentAccessRightModel> value)
	{
		getPersistenceContext().setPropertyValue(WRITABLECOCKPITUICOMPONENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Principal.writeCollections</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the writeCollections
	 */
	@Accessor(qualifier = "writeCollections", type = Accessor.Type.SETTER)
	public void setWriteCollections(final Collection<CockpitObjectAbstractCollectionModel> value)
	{
		getPersistenceContext().setPropertyValue(WRITECOLLECTIONS, value);
	}
	
}
