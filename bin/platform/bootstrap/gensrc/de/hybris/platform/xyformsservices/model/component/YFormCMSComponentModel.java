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
package de.hybris.platform.xyformsservices.model.component;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.xyformsservices.enums.YFormDataActionEnum;

/**
 * Generated model class for type YFormCMSComponent first defined at extension xyformsservices.
 * <p>
 * This is used to renderer the Orbeon forms by the given parameters.
 */
@SuppressWarnings("all")
public class YFormCMSComponentModel extends SimpleCMSComponentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "YFormCMSComponent";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormCMSComponent.applicationId</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String APPLICATIONID = "applicationId";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormCMSComponent.formId</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String FORMID = "formId";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormCMSComponent.formDataId</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String FORMDATAID = "formDataId";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormCMSComponent.action</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String ACTION = "action";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public YFormCMSComponentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public YFormCMSComponentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _applicationId initial attribute declared by type <code>YFormCMSComponent</code> at extension <code>xyformsservices</code>
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _formId initial attribute declared by type <code>YFormCMSComponent</code> at extension <code>xyformsservices</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public YFormCMSComponentModel(final String _applicationId, final CatalogVersionModel _catalogVersion, final String _formId, final String _uid)
	{
		super();
		setApplicationId(_applicationId);
		setCatalogVersion(_catalogVersion);
		setFormId(_formId);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _applicationId initial attribute declared by type <code>YFormCMSComponent</code> at extension <code>xyformsservices</code>
	 * @param _catalogVersion initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 * @param _formId initial attribute declared by type <code>YFormCMSComponent</code> at extension <code>xyformsservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>CMSItem</code> at extension <code>cms2</code>
	 */
	@Deprecated
	public YFormCMSComponentModel(final String _applicationId, final CatalogVersionModel _catalogVersion, final String _formId, final ItemModel _owner, final String _uid)
	{
		super();
		setApplicationId(_applicationId);
		setCatalogVersion(_catalogVersion);
		setFormId(_formId);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormCMSComponent.action</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the action
	 */
	@Accessor(qualifier = "action", type = Accessor.Type.GETTER)
	public YFormDataActionEnum getAction()
	{
		return getPersistenceContext().getPropertyValue(ACTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormCMSComponent.applicationId</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the applicationId
	 */
	@Accessor(qualifier = "applicationId", type = Accessor.Type.GETTER)
	public String getApplicationId()
	{
		return getPersistenceContext().getPropertyValue(APPLICATIONID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormCMSComponent.formDataId</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the formDataId
	 */
	@Accessor(qualifier = "formDataId", type = Accessor.Type.GETTER)
	public String getFormDataId()
	{
		return getPersistenceContext().getPropertyValue(FORMDATAID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormCMSComponent.formId</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the formId
	 */
	@Accessor(qualifier = "formId", type = Accessor.Type.GETTER)
	public String getFormId()
	{
		return getPersistenceContext().getPropertyValue(FORMID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormCMSComponent.action</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the action
	 */
	@Accessor(qualifier = "action", type = Accessor.Type.SETTER)
	public void setAction(final YFormDataActionEnum value)
	{
		getPersistenceContext().setPropertyValue(ACTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormCMSComponent.applicationId</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the applicationId
	 */
	@Accessor(qualifier = "applicationId", type = Accessor.Type.SETTER)
	public void setApplicationId(final String value)
	{
		getPersistenceContext().setPropertyValue(APPLICATIONID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormCMSComponent.formDataId</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the formDataId
	 */
	@Accessor(qualifier = "formDataId", type = Accessor.Type.SETTER)
	public void setFormDataId(final String value)
	{
		getPersistenceContext().setPropertyValue(FORMDATAID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormCMSComponent.formId</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the formId
	 */
	@Accessor(qualifier = "formId", type = Accessor.Type.SETTER)
	public void setFormId(final String value)
	{
		getPersistenceContext().setPropertyValue(FORMID, value);
	}
	
}
