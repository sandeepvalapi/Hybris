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
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ProductFeature first defined at extension catalog.
 */
@SuppressWarnings("all")
public class ProductFeatureModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ProductFeature";
	
	/**<i>Generated relation code constant for relation <code>Product2FeatureRelation</code> defining source attribute <code>product</code> in extension <code>catalog</code>.</i>*/
	public static final String _PRODUCT2FEATURERELATION = "Product2FeatureRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.qualifier</code> attribute defined at extension <code>catalog</code>. */
	public static final String QUALIFIER = "qualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.classificationAttributeAssignment</code> attribute defined at extension <code>catalog</code>. */
	public static final String CLASSIFICATIONATTRIBUTEASSIGNMENT = "classificationAttributeAssignment";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.language</code> attribute defined at extension <code>catalog</code>. */
	public static final String LANGUAGE = "language";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.valuePosition</code> attribute defined at extension <code>catalog</code>. */
	public static final String VALUEPOSITION = "valuePosition";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.featurePosition</code> attribute defined at extension <code>catalog</code>. */
	public static final String FEATUREPOSITION = "featurePosition";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.valueType</code> attribute defined at extension <code>catalog</code>. */
	public static final String VALUETYPE = "valueType";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.stringValue</code> attribute defined at extension <code>catalog</code>. */
	public static final String STRINGVALUE = "stringValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.booleanValue</code> attribute defined at extension <code>catalog</code>. */
	public static final String BOOLEANVALUE = "booleanValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.numberValue</code> attribute defined at extension <code>catalog</code>. */
	public static final String NUMBERVALUE = "numberValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.rawValue</code> attribute defined at extension <code>catalog</code>. */
	public static final String RAWVALUE = "rawValue";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.value</code> attribute defined at extension <code>catalog</code>. */
	public static final String VALUE = "value";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.unit</code> attribute defined at extension <code>catalog</code>. */
	public static final String UNIT = "unit";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.valueDetails</code> attribute defined at extension <code>catalog</code>. */
	public static final String VALUEDETAILS = "valueDetails";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.description</code> attribute defined at extension <code>catalog</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.productPOS</code> attribute defined at extension <code>catalog</code>. */
	public static final String PRODUCTPOS = "productPOS";
	
	/** <i>Generated constant</i> - Attribute key of <code>ProductFeature.product</code> attribute defined at extension <code>catalog</code>. */
	public static final String PRODUCT = "product";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ProductFeatureModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ProductFeatureModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _product initial attribute declared by type <code>ProductFeature</code> at extension <code>catalog</code>
	 * @param _qualifier initial attribute declared by type <code>ProductFeature</code> at extension <code>catalog</code>
	 * @param _value initial attribute declared by type <code>ProductFeature</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ProductFeatureModel(final ProductModel _product, final String _qualifier, final Object _value)
	{
		super();
		setProduct(_product);
		setQualifier(_qualifier);
		setValue(_value);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _classificationAttributeAssignment initial attribute declared by type <code>ProductFeature</code> at extension <code>catalog</code>
	 * @param _language initial attribute declared by type <code>ProductFeature</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _product initial attribute declared by type <code>ProductFeature</code> at extension <code>catalog</code>
	 * @param _qualifier initial attribute declared by type <code>ProductFeature</code> at extension <code>catalog</code>
	 * @param _value initial attribute declared by type <code>ProductFeature</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ProductFeatureModel(final ClassAttributeAssignmentModel _classificationAttributeAssignment, final LanguageModel _language, final ItemModel _owner, final ProductModel _product, final String _qualifier, final Object _value)
	{
		super();
		setClassificationAttributeAssignment(_classificationAttributeAssignment);
		setLanguage(_language);
		setOwner(_owner);
		setProduct(_product);
		setQualifier(_qualifier);
		setValue(_value);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.classificationAttributeAssignment</code> attribute defined at extension <code>catalog</code>. 
	 * @return the classificationAttributeAssignment - Classification attribute assignment which this value belongs to
	 */
	@Accessor(qualifier = "classificationAttributeAssignment", type = Accessor.Type.GETTER)
	public ClassAttributeAssignmentModel getClassificationAttributeAssignment()
	{
		return getPersistenceContext().getPropertyValue(CLASSIFICATIONATTRIBUTEASSIGNMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.description</code> attribute defined at extension <code>catalog</code>. 
	 * @return the description - description text
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getPersistenceContext().getPropertyValue(DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.featurePosition</code> attribute defined at extension <code>catalog</code>. 
	 * @return the featurePosition - position of the feature which this value belongs to
	 */
	@Accessor(qualifier = "featurePosition", type = Accessor.Type.GETTER)
	public Integer getFeaturePosition()
	{
		return getPersistenceContext().getPropertyValue(FEATUREPOSITION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.language</code> attribute defined at extension <code>catalog</code>. 
	 * @return the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.GETTER)
	public LanguageModel getLanguage()
	{
		return getPersistenceContext().getPropertyValue(LANGUAGE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.product</code> attribute defined at extension <code>catalog</code>. 
	 * @return the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.GETTER)
	public ProductModel getProduct()
	{
		return getPersistenceContext().getPropertyValue(PRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.qualifier</code> attribute defined at extension <code>catalog</code>. 
	 * @return the qualifier - Qualifier
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
	public String getQualifier()
	{
		return getPersistenceContext().getPropertyValue(QUALIFIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.unit</code> attribute defined at extension <code>catalog</code>. 
	 * @return the unit - Classification attribute unit
	 */
	@Accessor(qualifier = "unit", type = Accessor.Type.GETTER)
	public ClassificationAttributeUnitModel getUnit()
	{
		return getPersistenceContext().getPropertyValue(UNIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.value</code> dynamic attribute defined at extension <code>catalog</code>. 
	 * @return the value - the actual value of this feature
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.GETTER)
	public Object getValue()
	{
		return getPersistenceContext().getDynamicValue(this,VALUE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.valueDetails</code> attribute defined at extension <code>catalog</code>. 
	 * @return the valueDetails - value details text
	 */
	@Accessor(qualifier = "valueDetails", type = Accessor.Type.GETTER)
	public String getValueDetails()
	{
		return getPersistenceContext().getPropertyValue(VALUEDETAILS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ProductFeature.valuePosition</code> attribute defined at extension <code>catalog</code>. 
	 * @return the valuePosition - position mark for multi value features
	 */
	@Accessor(qualifier = "valuePosition", type = Accessor.Type.GETTER)
	public Integer getValuePosition()
	{
		return getPersistenceContext().getPropertyValue(VALUEPOSITION);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductFeature.classificationAttributeAssignment</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the classificationAttributeAssignment - Classification attribute assignment which this value belongs to
	 */
	@Accessor(qualifier = "classificationAttributeAssignment", type = Accessor.Type.SETTER)
	public void setClassificationAttributeAssignment(final ClassAttributeAssignmentModel value)
	{
		getPersistenceContext().setPropertyValue(CLASSIFICATIONATTRIBUTEASSIGNMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductFeature.description</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the description - description text
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		getPersistenceContext().setPropertyValue(DESCRIPTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductFeature.featurePosition</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the featurePosition - position of the feature which this value belongs to
	 */
	@Accessor(qualifier = "featurePosition", type = Accessor.Type.SETTER)
	public void setFeaturePosition(final Integer value)
	{
		getPersistenceContext().setPropertyValue(FEATUREPOSITION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductFeature.language</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the language
	 */
	@Accessor(qualifier = "language", type = Accessor.Type.SETTER)
	public void setLanguage(final LanguageModel value)
	{
		getPersistenceContext().setPropertyValue(LANGUAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductFeature.product</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.SETTER)
	public void setProduct(final ProductModel value)
	{
		getPersistenceContext().setPropertyValue(PRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ProductFeature.qualifier</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the qualifier - Qualifier
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
	public void setQualifier(final String value)
	{
		getPersistenceContext().setPropertyValue(QUALIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductFeature.unit</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the unit - Classification attribute unit
	 */
	@Accessor(qualifier = "unit", type = Accessor.Type.SETTER)
	public void setUnit(final ClassificationAttributeUnitModel value)
	{
		getPersistenceContext().setPropertyValue(UNIT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductFeature.value</code> dynamic attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the value - the actual value of this feature
	 */
	@Accessor(qualifier = "value", type = Accessor.Type.SETTER)
	public void setValue(final Object value)
	{
		getPersistenceContext().setDynamicValue(this,VALUE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductFeature.valueDetails</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the valueDetails - value details text
	 */
	@Accessor(qualifier = "valueDetails", type = Accessor.Type.SETTER)
	public void setValueDetails(final String value)
	{
		getPersistenceContext().setPropertyValue(VALUEDETAILS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ProductFeature.valuePosition</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the valuePosition - position mark for multi value features
	 */
	@Accessor(qualifier = "valuePosition", type = Accessor.Type.SETTER)
	public void setValuePosition(final Integer value)
	{
		getPersistenceContext().setPropertyValue(VALUEPOSITION, value);
	}
	
}
