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
package de.hybris.platform.xyformsservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
import de.hybris.platform.xyformsservices.model.YFormDataHistoryModel;
import de.hybris.platform.xyformsservices.model.YFormDefinitionModel;
import java.util.List;

/**
 * Generated model class for type YFormData first defined at extension xyformsservices.
 */
@SuppressWarnings("all")
public class YFormDataModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "YFormData";
	
	/**<i>Generated relation code constant for relation <code>YFormDefinition2YFormDataRelation</code> defining source attribute <code>formDefinition</code> in extension <code>xyformsservices</code>.</i>*/
	public static final String _YFORMDEFINITION2YFORMDATARELATION = "YFormDefinition2YFormDataRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormData.id</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormData.applicationId</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String APPLICATIONID = "applicationId";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormData.formId</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String FORMID = "formId";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormData.refId</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String REFID = "refId";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormData.system</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String SYSTEM = "system";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormData.type</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String TYPE = "type";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormData.content</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String CONTENT = "content";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormData.formDefinition</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String FORMDEFINITION = "formDefinition";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormData.history</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String HISTORY = "history";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public YFormDataModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public YFormDataModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _applicationId initial attribute declared by type <code>YFormData</code> at extension <code>xyformsservices</code>
	 * @param _formId initial attribute declared by type <code>YFormData</code> at extension <code>xyformsservices</code>
	 * @param _id initial attribute declared by type <code>YFormData</code> at extension <code>xyformsservices</code>
	 */
	@Deprecated
	public YFormDataModel(final String _applicationId, final String _formId, final String _id)
	{
		super();
		setApplicationId(_applicationId);
		setFormId(_formId);
		setId(_id);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _applicationId initial attribute declared by type <code>YFormData</code> at extension <code>xyformsservices</code>
	 * @param _formId initial attribute declared by type <code>YFormData</code> at extension <code>xyformsservices</code>
	 * @param _id initial attribute declared by type <code>YFormData</code> at extension <code>xyformsservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public YFormDataModel(final String _applicationId, final String _formId, final String _id, final ItemModel _owner)
	{
		super();
		setApplicationId(_applicationId);
		setFormId(_formId);
		setId(_id);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.applicationId</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the applicationId
	 */
	@Accessor(qualifier = "applicationId", type = Accessor.Type.GETTER)
	public String getApplicationId()
	{
		return getPersistenceContext().getPropertyValue(APPLICATIONID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.content</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the content
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.GETTER)
	public String getContent()
	{
		return getPersistenceContext().getPropertyValue(CONTENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.formDefinition</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the formDefinition
	 */
	@Accessor(qualifier = "formDefinition", type = Accessor.Type.GETTER)
	public YFormDefinitionModel getFormDefinition()
	{
		return getPersistenceContext().getPropertyValue(FORMDEFINITION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.formId</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the formId
	 */
	@Accessor(qualifier = "formId", type = Accessor.Type.GETTER)
	public String getFormId()
	{
		return getPersistenceContext().getPropertyValue(FORMID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.history</code> attribute defined at extension <code>xyformsservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the history
	 */
	@Accessor(qualifier = "history", type = Accessor.Type.GETTER)
	public List<YFormDataHistoryModel> getHistory()
	{
		return getPersistenceContext().getPropertyValue(HISTORY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.id</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		return getPersistenceContext().getPropertyValue(ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.refId</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the refId
	 */
	@Accessor(qualifier = "refId", type = Accessor.Type.GETTER)
	public String getRefId()
	{
		return getPersistenceContext().getPropertyValue(REFID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.system</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the system
	 */
	@Accessor(qualifier = "system", type = Accessor.Type.GETTER)
	public Boolean getSystem()
	{
		return getPersistenceContext().getPropertyValue(SYSTEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormData.type</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public YFormDataTypeEnum getType()
	{
		return getPersistenceContext().getPropertyValue(TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormData.applicationId</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the applicationId
	 */
	@Accessor(qualifier = "applicationId", type = Accessor.Type.SETTER)
	public void setApplicationId(final String value)
	{
		getPersistenceContext().setPropertyValue(APPLICATIONID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormData.content</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the content
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.SETTER)
	public void setContent(final String value)
	{
		getPersistenceContext().setPropertyValue(CONTENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormData.formDefinition</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the formDefinition
	 */
	@Accessor(qualifier = "formDefinition", type = Accessor.Type.SETTER)
	public void setFormDefinition(final YFormDefinitionModel value)
	{
		getPersistenceContext().setPropertyValue(FORMDEFINITION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormData.formId</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the formId
	 */
	@Accessor(qualifier = "formId", type = Accessor.Type.SETTER)
	public void setFormId(final String value)
	{
		getPersistenceContext().setPropertyValue(FORMID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormData.history</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the history
	 */
	@Accessor(qualifier = "history", type = Accessor.Type.SETTER)
	public void setHistory(final List<YFormDataHistoryModel> value)
	{
		getPersistenceContext().setPropertyValue(HISTORY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormData.id</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the id
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		getPersistenceContext().setPropertyValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormData.refId</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the refId
	 */
	@Accessor(qualifier = "refId", type = Accessor.Type.SETTER)
	public void setRefId(final String value)
	{
		getPersistenceContext().setPropertyValue(REFID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormData.system</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the system
	 */
	@Accessor(qualifier = "system", type = Accessor.Type.SETTER)
	public void setSystem(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SYSTEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormData.type</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.SETTER)
	public void setType(final YFormDataTypeEnum value)
	{
		getPersistenceContext().setPropertyValue(TYPE, value);
	}
	
}
