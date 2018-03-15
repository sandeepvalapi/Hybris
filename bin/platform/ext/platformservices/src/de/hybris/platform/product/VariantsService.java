/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.product;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantAttributeDescriptorModel;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Service to provide generic access to variant product attributes. This is necessary because variant product types may
 * be created during runtime and therefore no actual model class is available providing appropriate attribute accessor
 * methods.
 * 
 * @spring.bean variantsService
 */
public interface VariantsService
{
	/**
	 * Returns a {@link VariantTypeModel} instance for the given <code>code</code>.
	 * 
	 * @param code
	 *           the variant type code. Must exists in the system and must be not null.
	 * @return a VariantTypeModel for the given code.
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if there is no VariantTypeModel for given code
	 * @throws IllegalArgumentException
	 *            if given <code>code</code> is null
	 */
	VariantTypeModel getVariantTypeForCode(String code);

	/**
	 * Returns a set of variant attribute qualifiers for the given <code>variantProductType</code>.
	 * 
	 * @param variantProductType
	 *           the variant type code. Must exists in the system and must be a variant type.
	 * @return an empty set if for the given <code>variantProductType</code> no variant attributes were found.
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if the given <code>variantProductType</code> does not exists or is not a variant type
	 */
	Set<String> getVariantAttributes(String variantProductType);

	/**
	 * Returns for the given {@link VariantProductModel} and given attribute <code>qualifier</code> the value of this
	 * attribute.
	 * 
	 * @param variant
	 *           the variant product
	 * @param qualifier
	 *           the attribute qualifier
	 * @return what is stored in this attribute.
	 * @throws de.hybris.platform.servicelayer.exceptions.SystemException
	 *            if the attribute couldn't be read
	 */
	Object getVariantAttributeValue(VariantProductModel variant, String qualifier);

	/**
	 * Sets in the given {@link VariantProductModel} the given <code>value</code> to the given attribute
	 * <code>qualifier</code>.
	 * 
	 * @param variant
	 *           the variant product
	 * @param qualifier
	 *           the attribute qualifier
	 * @param value
	 *           the value to be set
	 * @throws de.hybris.platform.servicelayer.exceptions.SystemException
	 *            if the attribute couldn't be written
	 */
	void setVariantAttributeValue(VariantProductModel variant, String qualifier, Object value);

	/**
	 * Searches all existing variant types.
	 * 
	 * @return the variant types.
	 */
	Collection<VariantTypeModel> getAllVariantTypes();

	/**
	 * Searches variant attributes with all assigned values for given base product.
	 * 
	 * @param baseProduct
	 *           the base product for all variants
	 * 
	 * @return map "name of attribute (qualifier) ->distinct values" over all variants
	 */
	Map<String, Collection<Object>> getAssignedVariantAttributes(final ProductModel baseProduct);


	/**
	 * Filters a range of variants from a given base product according to the given variant attribute qualifier-value
	 * map. If not all available attributes are specified the method is likely to return more than one variant - if all
	 * attributes are specified there should be only one variant which matches them.
	 * 
	 * @param baseProduct
	 *           the base product to get variants for
	 * @param filterValues
	 *           the variant attribute values (attribute qualifier->value map) to match. if <code>null or empty</code> an
	 *           empty List will be returned.
	 * @return collection of matching variants.
	 */
	Collection<VariantProductModel> getVariantProductForAttributeValues(final ProductModel baseProduct,
			final Map<String, Object> filterValues);

	/**
	 * Returns the attribute descriptors of the given <code>VariantType</code>.
	 * 
	 * @param variantType
	 *           the variant type
	 * @return an attributes collection
	 */
	List<VariantAttributeDescriptorModel> getVariantAttributesForVariantType(final VariantTypeModel variantType);

	/**
	 * Tries to assign new variant attribute descriptors for the given variant type. Removes old attributes if necessary
	 * and checks if enclosing type is set correctly. This is achieved by internal save of the given variantType with new
	 * set of the {@link VariantAttributeDescriptorModel}s.
	 * 
	 * @param variantType
	 *           the variant type
	 * @param newAttributes
	 *           the attributes which should be assigned
	 * @throws IllegalArgumentException
	 *            if given variantType is new
	 */
	void setVariantAttributesForVariantType(final VariantTypeModel variantType,
			final List<VariantAttributeDescriptorModel> newAttributes);

}
