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
package de.hybris.platform.platformbackoffice.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.platformbackoffice.model.BackofficeSavedQueryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Map;

/**
 * Generated model class for type BackofficeSearchCondition first defined at extension platformbackoffice.
 */
@SuppressWarnings("all")
public class BackofficeSearchConditionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "BackofficeSearchCondition";
	
	/**<i>Generated relation code constant for relation <code>BackofficeSavedQuery2SearchConditionRelation</code> defining source attribute <code>savedQuery</code> in extension <code>platformbackoffice</code>.</i>*/
	public static final String _BACKOFFICESAVEDQUERY2SEARCHCONDITIONRELATION = "BackofficeSavedQuery2SearchConditionRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSearchCondition.attribute</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String ATTRIBUTE = "attribute";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSearchCondition.value</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String VALUE = "value";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSearchCondition.valueReference</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String VALUEREFERENCE = "valueReference";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSearchCondition.languageCode</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String LANGUAGECODE = "languageCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSearchCondition.operatorCode</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String OPERATORCODE = "operatorCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSearchCondition.selected</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String SELECTED = "selected";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSearchCondition.editor</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String EDITOR = "editor";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSearchCondition.editorParameters</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String EDITORPARAMETERS = "editorParameters";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSearchCondition.sortable</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String SORTABLE = "sortable";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSearchCondition.disabled</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String DISABLED = "disabled";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSearchCondition.mandatory</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String MANDATORY = "mandatory";
	
	/** <i>Generated constant</i> - Attribute key of <code>BackofficeSearchCondition.savedQuery</code> attribute defined at extension <code>platformbackoffice</code>. */
	public static final String SAVEDQUERY = "savedQuery";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public BackofficeSearchConditionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public BackofficeSearchConditionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _attribute initial attribute declared by type <code>BackofficeSearchCondition</code> at extension <code>platformbackoffice</code>
	 * @param _operatorCode initial attribute declared by type <code>BackofficeSearchCondition</code> at extension <code>platformbackoffice</code>
	 */
	@Deprecated
	public BackofficeSearchConditionModel(final String _attribute, final String _operatorCode)
	{
		super();
		setAttribute(_attribute);
		setOperatorCode(_operatorCode);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _attribute initial attribute declared by type <code>BackofficeSearchCondition</code> at extension <code>platformbackoffice</code>
	 * @param _operatorCode initial attribute declared by type <code>BackofficeSearchCondition</code> at extension <code>platformbackoffice</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public BackofficeSearchConditionModel(final String _attribute, final String _operatorCode, final ItemModel _owner)
	{
		super();
		setAttribute(_attribute);
		setOperatorCode(_operatorCode);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSearchCondition.attribute</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the attribute
	 */
	@Accessor(qualifier = "attribute", type = Accessor.Type.GETTER)
	public String getAttribute()
	{
		return getPersistenceContext().getPropertyValue(ATTRIBUTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSearchCondition.disabled</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the disabled
	 */
	@Accessor(qualifier = "disabled", type = Accessor.Type.GETTER)
	public Boolean getDisabled()
	{
		return getPersistenceContext().getPropertyValue(DISABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSearchCondition.editor</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the editor
	 */
	@Accessor(qualifier = "editor", type = Accessor.Type.GETTER)
	public String getEditor()
	{
		return getPersistenceContext().getPropertyValue(EDITOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSearchCondition.editorParameters</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the editorParameters
	 */
	@Accessor(qualifier = "editorParameters", type = Accessor.Type.GETTER)
	public Map<String,String> getEditorParameters()
	{
		return getPersistenceContext().getPropertyValue(EDITORPARAMETERS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSearchCondition.languageCode</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the languageCode
	 */
	@Accessor(qualifier = "languageCode", type = Accessor.Type.GETTER)
	public String getLanguageCode()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGECODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSearchCondition.mandatory</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the mandatory
	 */
	@Accessor(qualifier = "mandatory", type = Accessor.Type.GETTER)
	public Boolean getMandatory()
	{
		return getPersistenceContext().getPropertyValue(MANDATORY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSearchCondition.operatorCode</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the operatorCode
	 */
	@Accessor(qualifier = "operatorCode", type = Accessor.Type.GETTER)
	public String getOperatorCode()
	{
		return getPersistenceContext().getPropertyValue(OPERATORCODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSearchCondition.savedQuery</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the savedQuery
	 */
	@Accessor(qualifier = "savedQuery", type = Accessor.Type.GETTER)
	public BackofficeSavedQueryModel getSavedQuery()
	{
		return getPersistenceContext().getPropertyValue(SAVEDQUERY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSearchCondition.selected</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the selected
	 */
	@Accessor(qualifier = "selected", type = Accessor.Type.GETTER)
	public Boolean getSelected()
	{
		return getPersistenceContext().getPropertyValue(SELECTED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSearchCondition.sortable</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the sortable
	 */
	@Accessor(qualifier = "sortable", type = Accessor.Type.GETTER)
	public Boolean getSortable()
	{
		return getPersistenceContext().getPropertyValue(SORTABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSearchCondition.value</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.GETTER)
	public String getValue()
	{
		return getPersistenceContext().getPropertyValue(VALUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BackofficeSearchCondition.valueReference</code> attribute defined at extension <code>platformbackoffice</code>. 
	 * @return the valueReference
	 */
	@Accessor(qualifier = "valueReference", type = Accessor.Type.GETTER)
	public ItemModel getValueReference()
	{
		return getPersistenceContext().getPropertyValue(VALUEREFERENCE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSearchCondition.attribute</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the attribute
	 */
	@Accessor(qualifier = "attribute", type = Accessor.Type.SETTER)
	public void setAttribute(final String value)
	{
		getPersistenceContext().setPropertyValue(ATTRIBUTE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSearchCondition.disabled</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the disabled
	 */
	@Accessor(qualifier = "disabled", type = Accessor.Type.SETTER)
	public void setDisabled(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(DISABLED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSearchCondition.editor</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the editor
	 */
	@Accessor(qualifier = "editor", type = Accessor.Type.SETTER)
	public void setEditor(final String value)
	{
		getPersistenceContext().setPropertyValue(EDITOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSearchCondition.editorParameters</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the editorParameters
	 */
	@Accessor(qualifier = "editorParameters", type = Accessor.Type.SETTER)
	public void setEditorParameters(final Map<String,String> value)
	{
		getPersistenceContext().setPropertyValue(EDITORPARAMETERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSearchCondition.languageCode</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the languageCode
	 */
	@Accessor(qualifier = "languageCode", type = Accessor.Type.SETTER)
	public void setLanguageCode(final String value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGECODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSearchCondition.mandatory</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the mandatory
	 */
	@Accessor(qualifier = "mandatory", type = Accessor.Type.SETTER)
	public void setMandatory(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(MANDATORY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSearchCondition.operatorCode</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the operatorCode
	 */
	@Accessor(qualifier = "operatorCode", type = Accessor.Type.SETTER)
	public void setOperatorCode(final String value)
	{
		getPersistenceContext().setPropertyValue(OPERATORCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSearchCondition.savedQuery</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the savedQuery
	 */
	@Accessor(qualifier = "savedQuery", type = Accessor.Type.SETTER)
	public void setSavedQuery(final BackofficeSavedQueryModel value)
	{
		getPersistenceContext().setPropertyValue(SAVEDQUERY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSearchCondition.selected</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the selected
	 */
	@Accessor(qualifier = "selected", type = Accessor.Type.SETTER)
	public void setSelected(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SELECTED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSearchCondition.sortable</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the sortable
	 */
	@Accessor(qualifier = "sortable", type = Accessor.Type.SETTER)
	public void setSortable(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(SORTABLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSearchCondition.value</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the value
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.SETTER)
	public void setValue(final String value)
	{
		getPersistenceContext().setPropertyValue(VALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>BackofficeSearchCondition.valueReference</code> attribute defined at extension <code>platformbackoffice</code>. 
	 *  
	 * @param value the valueReference
	 */
	@Accessor(qualifier = "valueReference", type = Accessor.Type.SETTER)
	public void setValueReference(final ItemModel value)
	{
		getPersistenceContext().setPropertyValue(VALUEREFERENCE, value);
	}
	
}
