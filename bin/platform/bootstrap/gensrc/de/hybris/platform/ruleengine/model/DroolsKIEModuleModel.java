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
package de.hybris.platform.ruleengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type DroolsKIEModule first defined at extension ruleengine.
 */
@SuppressWarnings("all")
public class DroolsKIEModuleModel extends AbstractRulesModuleModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "DroolsKIEModule";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsKIEModule.mvnGroupId</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String MVNGROUPID = "mvnGroupId";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsKIEModule.mvnArtifactId</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String MVNARTIFACTID = "mvnArtifactId";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsKIEModule.mvnVersion</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String MVNVERSION = "mvnVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsKIEModule.deployedMvnVersion</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String DEPLOYEDMVNVERSION = "deployedMvnVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsKIEModule.defaultKIEBase</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String DEFAULTKIEBASE = "defaultKIEBase";
	
	/** <i>Generated constant</i> - Attribute key of <code>DroolsKIEModule.kieBases</code> attribute defined at extension <code>ruleengine</code>. */
	public static final String KIEBASES = "kieBases";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DroolsKIEModuleModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DroolsKIEModuleModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _mvnArtifactId initial attribute declared by type <code>DroolsKIEModule</code> at extension <code>ruleengine</code>
	 * @param _mvnGroupId initial attribute declared by type <code>DroolsKIEModule</code> at extension <code>ruleengine</code>
	 * @param _mvnVersion initial attribute declared by type <code>DroolsKIEModule</code> at extension <code>ruleengine</code>
	 * @param _name initial attribute declared by type <code>AbstractRulesModule</code> at extension <code>ruleengine</code>
	 * @param _version initial attribute declared by type <code>AbstractRulesModule</code> at extension <code>ruleengine</code>
	 */
	@Deprecated
	public DroolsKIEModuleModel(final String _mvnArtifactId, final String _mvnGroupId, final String _mvnVersion, final String _name, final Long _version)
	{
		super();
		setMvnArtifactId(_mvnArtifactId);
		setMvnGroupId(_mvnGroupId);
		setMvnVersion(_mvnVersion);
		setName(_name);
		setVersion(_version);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _mvnArtifactId initial attribute declared by type <code>DroolsKIEModule</code> at extension <code>ruleengine</code>
	 * @param _mvnGroupId initial attribute declared by type <code>DroolsKIEModule</code> at extension <code>ruleengine</code>
	 * @param _mvnVersion initial attribute declared by type <code>DroolsKIEModule</code> at extension <code>ruleengine</code>
	 * @param _name initial attribute declared by type <code>AbstractRulesModule</code> at extension <code>ruleengine</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _version initial attribute declared by type <code>AbstractRulesModule</code> at extension <code>ruleengine</code>
	 */
	@Deprecated
	public DroolsKIEModuleModel(final String _mvnArtifactId, final String _mvnGroupId, final String _mvnVersion, final String _name, final ItemModel _owner, final Long _version)
	{
		super();
		setMvnArtifactId(_mvnArtifactId);
		setMvnGroupId(_mvnGroupId);
		setMvnVersion(_mvnVersion);
		setName(_name);
		setOwner(_owner);
		setVersion(_version);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsKIEModule.defaultKIEBase</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the defaultKIEBase
	 */
	@Accessor(qualifier = "defaultKIEBase", type = Accessor.Type.GETTER)
	public DroolsKIEBaseModel getDefaultKIEBase()
	{
		return getPersistenceContext().getPropertyValue(DEFAULTKIEBASE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsKIEModule.deployedMvnVersion</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the deployedMvnVersion
	 */
	@Accessor(qualifier = "deployedMvnVersion", type = Accessor.Type.GETTER)
	public String getDeployedMvnVersion()
	{
		return getPersistenceContext().getPropertyValue(DEPLOYEDMVNVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsKIEModule.kieBases</code> attribute defined at extension <code>ruleengine</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the kieBases
	 */
	@Accessor(qualifier = "kieBases", type = Accessor.Type.GETTER)
	public Collection<DroolsKIEBaseModel> getKieBases()
	{
		return getPersistenceContext().getPropertyValue(KIEBASES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsKIEModule.mvnArtifactId</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the mvnArtifactId
	 */
	@Accessor(qualifier = "mvnArtifactId", type = Accessor.Type.GETTER)
	public String getMvnArtifactId()
	{
		return getPersistenceContext().getPropertyValue(MVNARTIFACTID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsKIEModule.mvnGroupId</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the mvnGroupId
	 */
	@Accessor(qualifier = "mvnGroupId", type = Accessor.Type.GETTER)
	public String getMvnGroupId()
	{
		return getPersistenceContext().getPropertyValue(MVNGROUPID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DroolsKIEModule.mvnVersion</code> attribute defined at extension <code>ruleengine</code>. 
	 * @return the mvnVersion
	 */
	@Accessor(qualifier = "mvnVersion", type = Accessor.Type.GETTER)
	public String getMvnVersion()
	{
		return getPersistenceContext().getPropertyValue(MVNVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DroolsKIEModule.defaultKIEBase</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the defaultKIEBase
	 */
	@Accessor(qualifier = "defaultKIEBase", type = Accessor.Type.SETTER)
	public void setDefaultKIEBase(final DroolsKIEBaseModel value)
	{
		getPersistenceContext().setPropertyValue(DEFAULTKIEBASE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DroolsKIEModule.deployedMvnVersion</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the deployedMvnVersion
	 */
	@Accessor(qualifier = "deployedMvnVersion", type = Accessor.Type.SETTER)
	public void setDeployedMvnVersion(final String value)
	{
		getPersistenceContext().setPropertyValue(DEPLOYEDMVNVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DroolsKIEModule.kieBases</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the kieBases
	 */
	@Accessor(qualifier = "kieBases", type = Accessor.Type.SETTER)
	public void setKieBases(final Collection<DroolsKIEBaseModel> value)
	{
		getPersistenceContext().setPropertyValue(KIEBASES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DroolsKIEModule.mvnArtifactId</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the mvnArtifactId
	 */
	@Accessor(qualifier = "mvnArtifactId", type = Accessor.Type.SETTER)
	public void setMvnArtifactId(final String value)
	{
		getPersistenceContext().setPropertyValue(MVNARTIFACTID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DroolsKIEModule.mvnGroupId</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the mvnGroupId
	 */
	@Accessor(qualifier = "mvnGroupId", type = Accessor.Type.SETTER)
	public void setMvnGroupId(final String value)
	{
		getPersistenceContext().setPropertyValue(MVNGROUPID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DroolsKIEModule.mvnVersion</code> attribute defined at extension <code>ruleengine</code>. 
	 *  
	 * @param value the mvnVersion
	 */
	@Accessor(qualifier = "mvnVersion", type = Accessor.Type.SETTER)
	public void setMvnVersion(final String value)
	{
		getPersistenceContext().setPropertyValue(MVNVERSION, value);
	}
	
}
