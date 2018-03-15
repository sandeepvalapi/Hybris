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
import de.hybris.platform.xyformsservices.model.YFormDataModel;

/**
 * Generated model class for type YFormDataHistory first defined at extension xyformsservices.
 */
@SuppressWarnings("all")
public class YFormDataHistoryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "YFormDataHistory";
	
	/**<i>Generated relation code constant for relation <code>YFormData2YFormDataHistoryRelation</code> defining source attribute <code>formData</code> in extension <code>xyformsservices</code>.</i>*/
	public static final String _YFORMDATA2YFORMDATAHISTORYRELATION = "YFormData2YFormDataHistoryRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormDataHistory.formDataId</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String FORMDATAID = "formDataId";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormDataHistory.content</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String CONTENT = "content";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormDataHistory.formDataPOS</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String FORMDATAPOS = "formDataPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>YFormDataHistory.formData</code> attribute defined at extension <code>xyformsservices</code>. */
	public static final String FORMDATA = "formData";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public YFormDataHistoryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public YFormDataHistoryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _formDataId initial attribute declared by type <code>YFormDataHistory</code> at extension <code>xyformsservices</code>
	 */
	@Deprecated
	public YFormDataHistoryModel(final String _formDataId)
	{
		super();
		setFormDataId(_formDataId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _formDataId initial attribute declared by type <code>YFormDataHistory</code> at extension <code>xyformsservices</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public YFormDataHistoryModel(final String _formDataId, final ItemModel _owner)
	{
		super();
		setFormDataId(_formDataId);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDataHistory.content</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the content
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.GETTER)
	public String getContent()
	{
		return getPersistenceContext().getPropertyValue(CONTENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDataHistory.formData</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the formData
	 */
	@Accessor(qualifier = "formData", type = Accessor.Type.GETTER)
	public YFormDataModel getFormData()
	{
		return getPersistenceContext().getPropertyValue(FORMDATA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YFormDataHistory.formDataId</code> attribute defined at extension <code>xyformsservices</code>. 
	 * @return the formDataId
	 */
	@Accessor(qualifier = "formDataId", type = Accessor.Type.GETTER)
	public String getFormDataId()
	{
		return getPersistenceContext().getPropertyValue(FORMDATAID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormDataHistory.content</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the content
	 */
	@Accessor(qualifier = "content", type = Accessor.Type.SETTER)
	public void setContent(final String value)
	{
		getPersistenceContext().setPropertyValue(CONTENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormDataHistory.formData</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the formData
	 */
	@Accessor(qualifier = "formData", type = Accessor.Type.SETTER)
	public void setFormData(final YFormDataModel value)
	{
		getPersistenceContext().setPropertyValue(FORMDATA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>YFormDataHistory.formDataId</code> attribute defined at extension <code>xyformsservices</code>. 
	 *  
	 * @param value the formDataId
	 */
	@Accessor(qualifier = "formDataId", type = Accessor.Type.SETTER)
	public void setFormDataId(final String value)
	{
		getPersistenceContext().setPropertyValue(FORMDATAID, value);
	}
	
}
