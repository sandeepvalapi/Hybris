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
package de.hybris.platform.servicelayer.model.collector;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.visitor.ItemVisitor;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;


public class GenericItemVisitorForTest implements ItemVisitor<ItemModel>
{

	private TypeService typeService;
	private Collection<String> qualifiers;

	public GenericItemVisitorForTest(final TypeService typeService, final Collection<String> qualifiers)
	{
		this.typeService = typeService;
		this.qualifiers = qualifiers;
	}


	@Override
	public List<ItemModel> visit(ItemModel theSource, List<ItemModel> path, Map<String, Object> ctx)
	{

		final List<ItemModel> collectedItems = Lists.newArrayList();
		final String givenType = theSource.getItemtype();
		final ComposedTypeModel composedTypeModel = typeService.getComposedTypeForCode(givenType);
		final List<AttributeDescriptorModel> descriptors = qualifiers.stream()
				.filter(each -> typeService.hasAttribute(composedTypeModel, each))
				.map(each -> typeService.getAttributeDescriptor(givenType, each)).collect(Collectors.toList());

		for (final AttributeDescriptorModel attributeDescriptor : descriptors)
		{
			if (!((attributeDescriptor.getAttributeType() instanceof AtomicTypeModel)
					&& !(attributeDescriptor.getAttributeType() instanceof EnumerationMetaTypeModel)))
			{

				final Object value = theSource.getProperty(attributeDescriptor.getQualifier());

				if (value instanceof Collection)
				{

					final Collection<Object> collection = (Collection<Object>) value;
					collectedItems.addAll(collection.stream().filter(ItemModel.class::isInstance).map(ItemModel.class::cast)
							.collect(Collectors.toList()));

				}
				else if (value instanceof ItemModel)
				{
					collectedItems.add((ItemModel) value);
				}
			}


		}
		return collectedItems;
	}
}
