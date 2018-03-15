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
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.xyformsservices.enums.YFormDefinitionStatusEnum;
import de.hybris.platform.xyformsservices.model.YFormDataModel;
import java.util.Collection;

/**
 * Generated model class for type YFormDefinition first defined at extension xyformsservices.
 */
@SuppressWarnings("all")
public class YFormDefinitionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "YFormDefinition";
	
	/**<i>Generated relation code constant for relation <code>Category2YFormDefinitionRelation</code> defining source attribute <code>categories</code> in extension <code>xyformsservices</code>.</i>*/
	public static final String _CATEGORY2YFORMDEFINITIONRELATION = "Category2YFormDefinitionRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormDefinition.applicationId</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String APPLICATIONID = "applicationId";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormDefinition.formId</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String FORMID = "formId";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormDefinition.version</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String VERSION = "version";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormDefinition.title</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String TITLE = "title";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormDefinition.description</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormDefinition.documentId</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String DOCUMENTID = "documentId";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormDefinition.system</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String SYSTEM = "system";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormDefinition.latest</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String LATEST = "latest";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormDefinition.status</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String STATUS = "status";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormDefinition.content</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String CONTENT = "content";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormDefinition.data</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String DATA = "data";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormDefinition.categories</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String CATEGORIES = "categories";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public YFormDefinitionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public YFormDefinitionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _applicationId initial attribute declared by type <code>YFormDefinition</code> at extension <code>xyformsservices</code>
	 * @param _formId initial attribute declared by type <code>YFormDefinition</code> at extension <code>xyformsservices</code>
	 */
	@Deprecated
	public YFormDefinitionModel(final String _applicationId, final String _formId)
	{
		super();
		setApplicationId(_applicationId);
		setFormId(_formId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _applicationId initial attribute declared by type <code>YFormDefinition</code> at extension <code>xyformsservices</code>
	 * @param _formId initial attribute declared by type <code>YFormDefinition</code> at extension <code>xyformsservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public YFormDefinitionModel(final String _applicationId, final String _formId, final ItemModel _owner)
	{
		super();
		setApplicationId(_applicationId);
		setFormId(_formId);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.applicationId</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the applicationId
	 */
	@Accessor(qualifier = "applicationId", type = Accessor.Type.GETTER)
	public String getApplicationId()
	{
		return getPersistenceContext().getPropertyValue(APPLICATIONID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.categories</code> attribute defined at extension <code>xyformsservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the categories - Categories
	 */
	@Accessor(qualifier = "categories", type = Accessor.Type.GETTER)
	public Collection<CategoryModel> getCategories()
	{
		return getPersistenceContext().getPropertyValue(CATEGORIES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.content</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the content
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.GETTER)
	public String getContent()
	{
		return getPersistenceContext().getPropertyValue(CONTENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.data</code> attribute defined at extension <code>xyformsservices</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the data
	 */
	@Accessor(qualifier = "data", type = Accessor.Type.GETTER)
	public Collection<YFormDataModel> getData()
	{
		return getPersistenceContext().getPropertyValue(DATA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.description</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getPersistenceContext().getPropertyValue(DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.documentId</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the documentId
	 */
	@Accessor(qualifier = "documentId", type = Accessor.Type.GETTER)
	public String getDocumentId()
	{
		return getPersistenceContext().getPropertyValue(DOCUMENTID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.formId</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the formId
	 */
	@Accessor(qualifier = "formId", type = Accessor.Type.GETTER)
	public String getFormId()
	{
		return getPersistenceContext().getPropertyValue(FORMID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.latest</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the latest
	 */
	@Accessor(qualifier = "latest", type = Accessor.Type.GETTER)
	public Boolean getLatest()
	{
		return getPersistenceContext().getPropertyValue(LATEST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.status</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.GETTER)
	public YFormDefinitionStatusEnum getStatus()
	{
		return getPersistenceContext().getPropertyValue(STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.system</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the system
	 */
	@Accessor(qualifier = "system", type = Accessor.Type.GETTER)
	public Boolean getSystem()
	{
		return getPersistenceContext().getPropertyValue(SYSTEM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.title</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the title
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.GETTER)
	public String getTitle()
	{
		return getPersistenceContext().getPropertyValue(TITLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDefinition.version</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the version
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.GETTER)
	public int getVersion()
	{
		return toPrimitive((Integer)getPersistenceContext().getPropertyValue(VERSION));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormDefinition.applicationId</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the applicationId
	 */
	@Accessor(qualifier = "applicationId", type = Accessor.Type.SETTER)
	public void setApplicationId(final String value)
	{
		getPersistenceContext().setPropertyValue(APPLICATIONID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormDefinition.categories</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the categories - Categories
	 */
	@Accessor(qualifier = "categories", type = Accessor.Type.SETTER)
	public void setCategories(final Collection<CategoryModel> value)
	{
		getPersistenceContext().setPropertyValue(CATEGORIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormDefinition.content</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the content
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.SETTER)
	public void setContent(final String value)
	{
		getPersistenceContext().setPropertyValue(CONTENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormDefinition.data</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the data
	 */
	@Accessor(qualifier = "data", type = Accessor.Type.SETTER)
	public void setData(final Collection<YFormDataModel> value)
	{
		getPersistenceContext().setPropertyValue(DATA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormDefinition.description</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		getPersistenceContext().setPropertyValue(DESCRIPTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormDefinition.documentId</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the documentId
	 */
	@Accessor(qualifier = "documentId", type = Accessor.Type.SETTER)
	public void setDocumentId(final String value)
	{
		getPersistenceContext().setPropertyValue(DOCUMENTID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormDefinition.formId</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the formId
	 */
	@Accessor(qualifier = "formId", type = Accessor.Type.SETTER)
	public void setFormId(final String value)
	{
		getPersistenceContext().setPropertyValue(FORMID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormDefinition.latest</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the latest
	 */
	@Accessor(qualifier = "latest", type = Accessor.Type.SETTER)
	public void setLatest(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(LATEST, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormDefinition.status</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the status
	 */
	@Accessor(qualifier = "status", type = Accessor.Type.SETTER)
	public void setStatus(final YFormDefinitionStatusEnum value)
	{
		getPersistenceContext().setPropertyValue(STATUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormDefinition.system</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the system
	 */
	@Accessor(qualifier = "system", type = Accessor.Type.SETTER)
	public void setSystem(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SYSTEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormDefinition.title</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the title
	 */
	@Accessor(qualifier = "title", type = Accessor.Type.SETTER)
	public void setTitle(final String value)
	{
		getPersistenceContext().setPropertyValue(TITLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormDefinition.version</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the version
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.SETTER)
	public void setVersion(final int value)
	{
		getPersistenceContext().setPropertyValue(VERSION, toObject(value));
	}
	
}
