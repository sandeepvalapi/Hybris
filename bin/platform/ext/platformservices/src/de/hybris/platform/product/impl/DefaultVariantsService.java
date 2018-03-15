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
package de.hybris.platform.product.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.product.VariantsService;
import de.hybris.platform.product.daos.VariantTypeDao;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.jalo.VariantProduct;
import de.hybris.platform.variants.model.VariantAttributeDescriptorModel;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link VariantsService}.
 */
public class DefaultVariantsService extends AbstractBusinessService implements VariantsService
{

	private TypeService typeService;

	private VariantTypeDao variantTypeDao;

	private static final Logger LOG = Logger.getLogger(DefaultVariantsService.class);

	private static final Comparator VARIANT_ATTRIBUTES_COMPARATOR = new Comparator()
	{
		@Override
		public int compare(final Object object1, final Object object2)
		{
			Integer tmpValue = ((VariantAttributeDescriptorModel) object1).getPosition();
			final int posPrimitive1 = tmpValue != null ? tmpValue.intValue() : 0;

			tmpValue = ((VariantAttributeDescriptorModel) object2).getPosition();
			final int posPrimitive2 = tmpValue != null ? tmpValue.intValue() : 0;

			return posPrimitive1 - posPrimitive2;
		}
	};

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public VariantTypeModel getVariantTypeForCode(final String code)
	{
		validateParameterNotNullStandardMessage("code", code);

		final ComposedTypeModel composedTypeModel = typeService.getComposedTypeForCode(code);
		if (!(composedTypeModel instanceof VariantTypeModel))
		{
			throw new UnknownIdentifierException("There is no variant type '" + code + "' (found composed type instead)");
		}

		return (VariantTypeModel) composedTypeModel;

	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Set<String> getVariantAttributes(final String variantProductType)
	{
		final ComposedTypeModel composedTypeModel = typeService.getComposedTypeForCode(variantProductType);
		if (composedTypeModel instanceof VariantTypeModel)
		{
			final VariantTypeModel variantTypeModel = (VariantTypeModel) composedTypeModel;
			final Collection<VariantAttributeDescriptorModel> variantAttributtes = getVariantAttributesForVariantType(variantTypeModel);
			if (CollectionUtils.isEmpty(variantAttributtes))
			{
				return Collections.EMPTY_SET;
			}
			else
			{
				final Set<String> ret = new HashSet<String>(variantAttributtes.size());
				for (final VariantAttributeDescriptorModel vad : variantAttributtes)
				{
					ret.add(vad.getQualifier());
				}
				return ret;
			}
		}
		else
		{
			throw new IllegalArgumentException("there is no variant type '" + variantProductType + "'"
					+ (composedTypeModel == null ? "" : " - found composed type (" + composedTypeModel + ") instead"));
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * TODO: to be changed, remove the jalo part. At this moment the jalo-way has to be used because there is no chance
	 * to read the attribute value in SL, because it's not a part of the model.
	 */
	@Override
	public Object getVariantAttributeValue(final VariantProductModel variant, final String qualifier)
	{
		try
		{
			return getModelService().getAttributeValue(variant, qualifier);
		}
		catch (final AttributeNotSupportedException e)
		{
			final VariantProduct variantproduct = getModelService().getSource(variant);
			try
			{
				return getModelService().toModelLayer(variantproduct.getAttribute(qualifier));
			}
			catch (final Exception ex)
			{
				throw new SystemException("cannot read variant attribute value for '" + qualifier + "' due to : " + ex.getMessage(),
						ex);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * TODO: to be changed, remove the jalo part. At this moment the jalo-way has to be used because there is no chance
	 * to read the attribute value in SL, because it's not a part of the model.
	 */
	@Override
	public void setVariantAttributeValue(final VariantProductModel variant, final String qualifier, final Object value)
	{
		try
		{
			getModelService().setAttributeValue(variant, qualifier, value);
		}
		catch (final AttributeNotSupportedException e)
		{
			final VariantProduct variantproduct = getModelService().getSource(variant);
			try
			{
				variantproduct.setAttribute(qualifier, getModelService().toPersistenceLayer(value));
			}
			catch (final Exception ex)
			{
				throw new SystemException("cannot write variant attribute value for '" + qualifier + "' due to : " + ex.getMessage(),
						ex);
			}
		}
	}


	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}


	@Required
	public void setVariantTypeDao(final VariantTypeDao variantTypeDao)
	{
		this.variantTypeDao = variantTypeDao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<VariantTypeModel> getAllVariantTypes()
	{
		return variantTypeDao.findAllVariantTypes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Collection<Object>> getAssignedVariantAttributes(final ProductModel baseProduct)
	{
		validateParameterNotNullStandardMessage("baseProduct", baseProduct);

		final Map<String, Collection<Object>> result = new HashMap<String, Collection<Object>>();
		final Collection<VariantProductModel> variantModelList = baseProduct.getVariants();
		final List<VariantAttributeDescriptorModel> vadList = getVariantAttributesForVariantType(baseProduct.getVariantType());

		//iterate trough all variants to get the qualifier with appropriate values		
		for (final VariantProductModel variant : variantModelList)
		{
			for (final VariantAttributeDescriptorModel item : vadList)
			{

				Collection values = result.get(item.getQualifier());
				if (values == null)
				{
					values = new LinkedHashSet();
					result.put(item.getQualifier(), values);
				}

				//there is no chance to read the attribute value in SL, because it"s not a part of the model.
				//	final Object value = variant.getAttributeProvider().getAttribute(item.getQualifier());
				//Currently the jalo-solution has to be used

				values.add(getVariantAttributeValue(variant, item.getQualifier()));

			}
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(result.size() + " variant attributes with assigned values found");

		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<VariantProductModel> getVariantProductForAttributeValues(final ProductModel baseProduct,
			final Map<String, Object> filterValues)
	{
		validateParameterNotNullStandardMessage("baseProduct", baseProduct);

		final Collection<VariantProductModel> result = new ArrayList<VariantProductModel>();
		final List<VariantAttributeDescriptorModel> vadList = getVariantAttributesForVariantType(baseProduct.getVariantType());
		final Collection<VariantProductModel> allBaseProductVariants = baseProduct.getVariants();

		if (filterValues == null || filterValues.isEmpty())
		{
			//no filter defined - no results.
			if (LOG.isDebugEnabled())
			{
				LOG.debug("The filter values haven't been set, no matching variants in cases like this.");

			}
			return result;
		}

		//iterate through all variants and filter those with matching qualifier and values
		for (final VariantProductModel variant : allBaseProductVariants)
		{
			boolean add = true;
			for (final Iterator<Map.Entry<String, Object>> iterator = filterValues.entrySet().iterator(); iterator.hasNext();)
			{
				if (!add)
				{
					break; //the filter element, which has been checked doesn't match, skip checking of addtional filter elements, go to next variant.
				}
				final Map.Entry entry = iterator.next();
				final String filterKey = (String) entry.getKey();
				final Object filterValue = entry.getValue();
				//	compare the filterKey and filterValue with all attributes (qualifier-value) of the variantType.
				for (final VariantAttributeDescriptorModel attrDesc : vadList)
				{
					final String qualifier = attrDesc.getQualifier();
					final Object variantAttributeValue = getVariantAttributeValue(variant, qualifier);

					if (!(filterKey.equals(qualifier) && (filterValue == variantAttributeValue || (filterValue != null && filterValue
							.equals(variantAttributeValue)))))
					{
						add = false;
					}
					else
					{
						add = true; // the current filter key and values matches for the current variant, go to next filter-element to check it.
						break;
					}
				}
			}
			if (add)
			{
				result.add(variant);
			}
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug(result.size() + " matching variants have been found.");

		}
		return result;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<VariantAttributeDescriptorModel> getVariantAttributesForVariantType(final VariantTypeModel variantType)
	{
		//Currently a deprecated typeService - method is in use, the dedicated replacement (getAttributeDescriptorsForType(...) doesn't return the updated AttributeDescriptor List.
		final List<AttributeDescriptorModel> attributes = new ArrayList(typeService.getAttributeDescriptors(variantType));
		final List<VariantAttributeDescriptorModel> attributesResult = new ArrayList<VariantAttributeDescriptorModel>(
				attributes.size());
		for (final Iterator<AttributeDescriptorModel> it = attributes.iterator(); it.hasNext();)
		{
			final AttributeDescriptorModel attributeDescriptorModel = it.next();
			if ((attributeDescriptorModel instanceof VariantAttributeDescriptorModel))
			{
				attributesResult.add((VariantAttributeDescriptorModel) attributeDescriptorModel);
			}
		}

		Collections.sort(attributesResult, VARIANT_ATTRIBUTES_COMPARATOR);
		return attributesResult;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVariantAttributesForVariantType(final VariantTypeModel variantType,
			final List<VariantAttributeDescriptorModel> newAttributes)
	{
		if (getModelService().isNew(variantType))
		{
			throw new IllegalArgumentException("Given variantType " + variantType + " is new and hasn't been persisted yet");
		}
		final List<VariantAttributeDescriptorModel> oldAttributes = getVariantAttributesForVariantType(variantType);
		if (newAttributes != null && !newAttributes.isEmpty())
		{
			//prepare to remove List: existing Attributes which don't include the new (given attributes)
			oldAttributes.removeAll(newAttributes);
		}
		if (!oldAttributes.isEmpty())
		{
			//if any of the attributes is an inherited attribute - it cannot be removed
			for (final VariantAttributeDescriptorModel attr : oldAttributes)
			{
				if (!variantType.equals(attr.getDeclaringEnclosingType()))
				{
					throw new SystemException(
							"attribute "
									+ attr.getQualifier()
									+ " is an inherited attribute of the variantType "
									+ variantType.getCode()
									+ " and can't be removed this way. Setting new Variant Attributes List don't forget to include the inherited variant attributes as well.");
				}
			}
			if (LOG.isDebugEnabled())
			{
				LOG.debug(oldAttributes.size() + " old attributes will be removed from model...");
			}
			for (final VariantAttributeDescriptorModel attr : oldAttributes)
			{
				getModelService().remove(attr);
			}
		}

		//for all new Attributes check the enclosing type and set position for attribute order
		if (newAttributes != null)
		{
			int index = 0;
			for (final VariantAttributeDescriptorModel attr : newAttributes)
			{
				// check enclosing type
				if (!variantType.equals(attr.getEnclosingType()))
				{
					final String attrCode = attr.getEnclosingType() == null ? null : attr.getEnclosingType().getCode();
					throw new SystemException("attribute descriptor " + attr.getQualifier()
							+ " has different enclosing type (expected " + variantType.getCode() + " but got " + attrCode + ")");
				}

				// set position for attribute order
				attr.setPosition(Integer.valueOf(index++));
			}
			//save all attributes
			getModelService().saveAll(newAttributes);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Attributes have been assigned to the variantType=" + variantType.getCode() + ". This type contains now "
						+ newAttributes.size() + " attributes");

			}
		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("No new attributes have been assigned to the variantType=" + variantType.getCode()
						+ ". The old attributes have been removed and the type does not contain any attributes at this moment");
			}
		}
	}

}
