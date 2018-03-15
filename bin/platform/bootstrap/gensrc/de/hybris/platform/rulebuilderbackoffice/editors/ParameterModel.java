/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:40 PM
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
 */
package de.hybris.platform.rulebuilderbackoffice.editors;

import java.io.Serializable;
import de.hybris.platform.rulebuilderbackoffice.editors.ValidationInfoModel;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang.math.NumberUtils;

/**
 * Represents Parameter of a Rule in rule builder
 */
public  class ParameterModel  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ParameterModel.uuid</code> property defined at extension <code>rulebuilderbackoffice</code>. */
		
	private String uuid;

	/** <i>Generated property</i> for <code>ParameterModel.id</code> property defined at extension <code>rulebuilderbackoffice</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>ParameterModel.name</code> property defined at extension <code>rulebuilderbackoffice</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>ParameterModel.description</code> property defined at extension <code>rulebuilderbackoffice</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>ParameterModel.priority</code> property defined at extension <code>rulebuilderbackoffice</code>. */
		
	private Integer priority;

	/** <i>Generated property</i> for <code>ParameterModel.type</code> property defined at extension <code>rulebuilderbackoffice</code>. */
		
	private String type;

	/** <i>Generated property</i> for <code>ParameterModel.value</code> property defined at extension <code>rulebuilderbackoffice</code>. */
		
	private Serializable value;

	/** <i>Generated property</i> for <code>ParameterModel.required</code> property defined at extension <code>rulebuilderbackoffice</code>. */
		
	private boolean required;

	/** <i>Generated property</i> for <code>ParameterModel.valid</code> property defined at extension <code>rulebuilderbackoffice</code>. */
		
	private boolean valid;

	/** <i>Generated property</i> for <code>ParameterModel.validationIconStyleClass</code> property defined at extension <code>rulebuilderbackoffice</code>. */
		
	private String validationIconStyleClass;

	/** <i>Generated property</i> for <code>ParameterModel.validationStyleClass</code> property defined at extension <code>rulebuilderbackoffice</code>. */
		
	private String validationStyleClass;

	/** <i>Generated property</i> for <code>ParameterModel.validationInfos</code> property defined at extension <code>rulebuilderbackoffice</code>. */
		
	private List<ValidationInfoModel> validationInfos;

	/** <i>Generated property</i> for <code>ParameterModel.readOnly</code> property defined at extension <code>rulebuilderbackoffice</code>. */
		
	private boolean readOnly;

	/** <i>Generated property</i> for <code>ParameterModel.customAttributes</code> property defined at extension <code>rulebuilderbackoffice</code>. */
		
	private Map<String,Serializable> customAttributes;

	/** <i>Generated property</i> for <code>ParameterModel.filters</code> property defined at extension <code>rulebuilderbackoffice</code>. */
		
	private Map<String,String> filters;

	/** <i>Generated property</i> for <code>ParameterModel.defaultEditor</code> property defined at extension <code>rulebuilderbackoffice</code>. */
		
	private String defaultEditor;
	
	public ParameterModel()
	{
		// default constructor
	}
	
		
	
	public void setUuid(final String uuid)
	{
		this.uuid = uuid;
	}

		
	
	public String getUuid() 
	{
		return uuid;
	}
	
		
	
	public void setId(final String id)
	{
		this.id = id;
	}

		
	
	public String getId() 
	{
		return id;
	}
	
		
	
	public void setName(final String name)
	{
		this.name = name;
	}

		
	
	public String getName() 
	{
		return name;
	}
	
		
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

		
	
	public String getDescription() 
	{
		return description;
	}
	
		
	
	public void setPriority(final Integer priority)
	{
		this.priority = priority;
	}

		
	
	public Integer getPriority() 
	{
		return priority;
	}
	
		
	
	public void setType(final String type)
	{
		this.type = type;
	}

		
	
	public String getType() 
	{
		return type;
	}
	
		
	
	public void setValue(final Serializable value)
	{
		this.value = value;
	}

		
	
	public Serializable getValue() 
	{
		return value;
	}
	
		
	
	public void setRequired(final boolean required)
	{
		this.required = required;
	}

		
	
	public boolean isRequired() 
	{
		return required;
	}
	
		
	
	public void setValid(final boolean valid)
	{
		this.valid = valid;
	}

		
	
	public boolean isValid() 
	{
		return valid;
	}
	
		
	
	public void setValidationIconStyleClass(final String validationIconStyleClass)
	{
		this.validationIconStyleClass = validationIconStyleClass;
	}

		
	
	public String getValidationIconStyleClass() 
	{
		return validationIconStyleClass;
	}
	
		
	
	public void setValidationStyleClass(final String validationStyleClass)
	{
		this.validationStyleClass = validationStyleClass;
	}

		
	
	public String getValidationStyleClass() 
	{
		return validationStyleClass;
	}
	
		
	
	public void setValidationInfos(final List<ValidationInfoModel> validationInfos)
	{
		this.validationInfos = validationInfos;
	}

		
	
	public List<ValidationInfoModel> getValidationInfos() 
	{
		return validationInfos;
	}
	
		
	
	public void setReadOnly(final boolean readOnly)
	{
		this.readOnly = readOnly;
	}

		
	
	public boolean isReadOnly() 
	{
		return readOnly;
	}
	
		
	
	public void setCustomAttributes(final Map<String,Serializable> customAttributes)
	{
		this.customAttributes = customAttributes;
	}

		
	
	public Map<String,Serializable> getCustomAttributes() 
	{
		return customAttributes;
	}
	
		
	
	public void setFilters(final Map<String,String> filters)
	{
		this.filters = filters;
	}

		
	
	public Map<String,String> getFilters() 
	{
		return filters;
	}
	
		
	
	public void setDefaultEditor(final String defaultEditor)
	{
		this.defaultEditor = defaultEditor;
	}

		
	
	public String getDefaultEditor() 
	{
		return defaultEditor;
	}
	

	@Override
	public boolean equals(final Object o)
	{
	
		if (o == null) return false;
		if (o == this) return true;

		try
		{
			final ParameterModel other = (ParameterModel) o;
			return new org.apache.commons.lang.builder.EqualsBuilder()
			.append(getUuid(), other.getUuid()) 
			.append(getId(), other.getId()) 
			.append(getName(), other.getName()) 
			.append(getType(), other.getType()) 
			.isEquals();
		} 
		catch (ClassCastException c)
		{
			return false;
		}
	}
	
	@Override
	public int hashCode()
	{
		return new org.apache.commons.lang.builder.HashCodeBuilder()
		.append(getUuid()) 
		.append(getId()) 
		.append(getName()) 
		.append(getType()) 
		.toHashCode();
	}

}
