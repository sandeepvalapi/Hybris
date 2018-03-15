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
package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncAttributeDescriptorConfigModel;
import de.hybris.platform.catalog.model.SyncItemCronJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generated model class for type SyncItemJob first defined at extension catalog.
 */
@SuppressWarnings("all")
public class SyncItemJobModel extends JobModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "SyncItemJob";
	
	/**<i>Generated relation code constant for relation <code>CatalogVersion2Synchronizations</code> defining source attribute <code>sourceVersion</code> in extension <code>catalog</code>.</i>*/
	public static final String _CATALOGVERSION2SYNCHRONIZATIONS = "CatalogVersion2Synchronizations";
	
	/**<i>Generated relation code constant for relation <code>CatalogVersion2IncomingSynchronizations</code> defining source attribute <code>targetVersion</code> in extension <code>catalog</code>.</i>*/
	public static final String _CATALOGVERSION2INCOMINGSYNCHRONIZATIONS = "CatalogVersion2IncomingSynchronizations";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.exclusiveMode</code> attribute defined at extension <code>catalog</code>. */
	public static final String EXCLUSIVEMODE = "exclusiveMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.syncPrincipalsOnly</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYNCPRINCIPALSONLY = "syncPrincipalsOnly";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.createNewItems</code> attribute defined at extension <code>catalog</code>. */
	public static final String CREATENEWITEMS = "createNewItems";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.removeMissingItems</code> attribute defined at extension <code>catalog</code>. */
	public static final String REMOVEMISSINGITEMS = "removeMissingItems";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.executions</code> attribute defined at extension <code>catalog</code>. */
	public static final String EXECUTIONS = "executions";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.syncOrder</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYNCORDER = "syncOrder";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.exportAttributeDescriptors</code> attribute defined at extension <code>catalog</code>. */
	public static final String EXPORTATTRIBUTEDESCRIPTORS = "exportAttributeDescriptors";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.syncAttributeConfigurations</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYNCATTRIBUTECONFIGURATIONS = "syncAttributeConfigurations";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.effectiveSyncLanguages</code> attribute defined at extension <code>catalog</code>. */
	public static final String EFFECTIVESYNCLANGUAGES = "effectiveSyncLanguages";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.sourceVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCEVERSION = "sourceVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.targetVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETVERSION = "targetVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.rootTypes</code> attribute defined at extension <code>catalog</code>. */
	public static final String ROOTTYPES = "rootTypes";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.syncLanguages</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYNCLANGUAGES = "syncLanguages";
	
	/** <i>Generated constant</i> - Attribute key of <code>SyncItemJob.syncPrincipals</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYNCPRINCIPALS = "syncPrincipals";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SyncItemJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SyncItemJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public SyncItemJobModel(final String _code, final CatalogVersionModel _sourceVersion, final CatalogVersionModel _targetVersion)
	{
		super();
		setCode(_code);
		setSourceVersion(_sourceVersion);
		setTargetVersion(_targetVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 * @param _nodeID initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sourceVersion initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public SyncItemJobModel(final String _code, final Integer _nodeID, final ItemModel _owner, final CatalogVersionModel _sourceVersion, final CatalogVersionModel _targetVersion)
	{
		super();
		setCode(_code);
		setNodeID(_nodeID);
		setOwner(_owner);
		setSourceVersion(_sourceVersion);
		setTargetVersion(_targetVersion);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.createNewItems</code> attribute defined at extension <code>catalog</code>. 
	 * @return the createNewItems
	 */
	@Accessor(qualifier = "createNewItems", type = Accessor.Type.GETTER)
	public Boolean getCreateNewItems()
	{
		return getPersistenceContext().getPropertyValue(CREATENEWITEMS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.effectiveSyncLanguages</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the effectiveSyncLanguages
	 */
	@Accessor(qualifier = "effectiveSyncLanguages", type = Accessor.Type.GETTER)
	public Collection<LanguageModel> getEffectiveSyncLanguages()
	{
		return getPersistenceContext().getPropertyValue(EFFECTIVESYNCLANGUAGES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.exclusiveMode</code> attribute defined at extension <code>catalog</code>. 
	 * @return the exclusiveMode
	 */
	@Accessor(qualifier = "exclusiveMode", type = Accessor.Type.GETTER)
	public Boolean getExclusiveMode()
	{
		return getPersistenceContext().getPropertyValue(EXCLUSIVEMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.executions</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the executions
	 */
	@Accessor(qualifier = "executions", type = Accessor.Type.GETTER)
	public Collection<SyncItemCronJobModel> getExecutions()
	{
		return getPersistenceContext().getPropertyValue(EXECUTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.exportAttributeDescriptors</code> attribute defined at extension <code>catalog</code>. 
	 * @return the exportAttributeDescriptors
	 */
	@Accessor(qualifier = "exportAttributeDescriptors", type = Accessor.Type.GETTER)
	public Map<AttributeDescriptorModel,Boolean> getExportAttributeDescriptors()
	{
		return getPersistenceContext().getPropertyValue(EXPORTATTRIBUTEDESCRIPTORS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.removeMissingItems</code> attribute defined at extension <code>catalog</code>. 
	 * @return the removeMissingItems
	 */
	@Accessor(qualifier = "removeMissingItems", type = Accessor.Type.GETTER)
	public Boolean getRemoveMissingItems()
	{
		return getPersistenceContext().getPropertyValue(REMOVEMISSINGITEMS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.rootTypes</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the rootTypes
	 */
	@Accessor(qualifier = "rootTypes", type = Accessor.Type.GETTER)
	public List<ComposedTypeModel> getRootTypes()
	{
		return getPersistenceContext().getPropertyValue(ROOTTYPES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.sourceVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the sourceVersion
	 */
	@Accessor(qualifier = "sourceVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getSourceVersion()
	{
		return getPersistenceContext().getPropertyValue(SOURCEVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.syncAttributeConfigurations</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the syncAttributeConfigurations
	 */
	@Accessor(qualifier = "syncAttributeConfigurations", type = Accessor.Type.GETTER)
	public Collection<SyncAttributeDescriptorConfigModel> getSyncAttributeConfigurations()
	{
		return getPersistenceContext().getPropertyValue(SYNCATTRIBUTECONFIGURATIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.syncLanguages</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the syncLanguages
	 */
	@Accessor(qualifier = "syncLanguages", type = Accessor.Type.GETTER)
	public Set<LanguageModel> getSyncLanguages()
	{
		return getPersistenceContext().getPropertyValue(SYNCLANGUAGES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.syncOrder</code> attribute defined at extension <code>catalog</code>. 
	 * @return the syncOrder
	 */
	@Accessor(qualifier = "syncOrder", type = Accessor.Type.GETTER)
	public Integer getSyncOrder()
	{
		return getPersistenceContext().getPropertyValue(SYNCORDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.syncPrincipals</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the syncPrincipals
	 */
	@Accessor(qualifier = "syncPrincipals", type = Accessor.Type.GETTER)
	public List<PrincipalModel> getSyncPrincipals()
	{
		return getPersistenceContext().getPropertyValue(SYNCPRINCIPALS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.syncPrincipalsOnly</code> attribute defined at extension <code>catalog</code>. 
	 * @return the syncPrincipalsOnly
	 */
	@Accessor(qualifier = "syncPrincipalsOnly", type = Accessor.Type.GETTER)
	public Boolean getSyncPrincipalsOnly()
	{
		return getPersistenceContext().getPropertyValue(SYNCPRINCIPALSONLY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SyncItemJob.targetVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the targetVersion
	 */
	@Accessor(qualifier = "targetVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getTargetVersion()
	{
		return getPersistenceContext().getPropertyValue(TARGETVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemJob.createNewItems</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the createNewItems
	 */
	@Accessor(qualifier = "createNewItems", type = Accessor.Type.SETTER)
	public void setCreateNewItems(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(CREATENEWITEMS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SyncItemJob.exclusiveMode</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the exclusiveMode
	 */
	@Accessor(qualifier = "exclusiveMode", type = Accessor.Type.SETTER)
	public void setExclusiveMode(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(EXCLUSIVEMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemJob.exportAttributeDescriptors</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the exportAttributeDescriptors
	 */
	@Accessor(qualifier = "exportAttributeDescriptors", type = Accessor.Type.SETTER)
	public void setExportAttributeDescriptors(final Map<AttributeDescriptorModel,Boolean> value)
	{
		getPersistenceContext().setPropertyValue(EXPORTATTRIBUTEDESCRIPTORS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemJob.removeMissingItems</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the removeMissingItems
	 */
	@Accessor(qualifier = "removeMissingItems", type = Accessor.Type.SETTER)
	public void setRemoveMissingItems(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(REMOVEMISSINGITEMS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemJob.rootTypes</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the rootTypes
	 */
	@Accessor(qualifier = "rootTypes", type = Accessor.Type.SETTER)
	public void setRootTypes(final List<ComposedTypeModel> value)
	{
		getPersistenceContext().setPropertyValue(ROOTTYPES, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SyncItemJob.sourceVersion</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sourceVersion
	 */
	@Accessor(qualifier = "sourceVersion", type = Accessor.Type.SETTER)
	public void setSourceVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(SOURCEVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemJob.syncAttributeConfigurations</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the syncAttributeConfigurations
	 */
	@Accessor(qualifier = "syncAttributeConfigurations", type = Accessor.Type.SETTER)
	public void setSyncAttributeConfigurations(final Collection<SyncAttributeDescriptorConfigModel> value)
	{
		getPersistenceContext().setPropertyValue(SYNCATTRIBUTECONFIGURATIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemJob.syncLanguages</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the syncLanguages
	 */
	@Accessor(qualifier = "syncLanguages", type = Accessor.Type.SETTER)
	public void setSyncLanguages(final Set<LanguageModel> value)
	{
		getPersistenceContext().setPropertyValue(SYNCLANGUAGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemJob.syncOrder</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the syncOrder
	 */
	@Accessor(qualifier = "syncOrder", type = Accessor.Type.SETTER)
	public void setSyncOrder(final Integer value)
	{
		getPersistenceContext().setPropertyValue(SYNCORDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemJob.syncPrincipals</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the syncPrincipals
	 */
	@Accessor(qualifier = "syncPrincipals", type = Accessor.Type.SETTER)
	public void setSyncPrincipals(final List<PrincipalModel> value)
	{
		getPersistenceContext().setPropertyValue(SYNCPRINCIPALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SyncItemJob.syncPrincipalsOnly</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the syncPrincipalsOnly
	 */
	@Accessor(qualifier = "syncPrincipalsOnly", type = Accessor.Type.SETTER)
	public void setSyncPrincipalsOnly(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SYNCPRINCIPALSONLY, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>SyncItemJob.targetVersion</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the targetVersion
	 */
	@Accessor(qualifier = "targetVersion", type = Accessor.Type.SETTER)
	public void setTargetVersion(final CatalogVersionModel value)
	{
		getPersistenceContext().setPropertyValue(TARGETVERSION, value);
	}
	
}
