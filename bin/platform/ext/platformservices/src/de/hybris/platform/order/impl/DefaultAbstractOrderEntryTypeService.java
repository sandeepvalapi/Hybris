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
package de.hybris.platform.order.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.order.AbstractOrderEntryTypeService;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


public class DefaultAbstractOrderEntryTypeService extends AbstractBusinessService implements AbstractOrderEntryTypeService
{

	//Resources
	private Map<String, String> orderType2orderEntryTypeMapping;
	private TypeService typeService;

	//constants
	private static final String ENTRIES = "entries";

	@Override
	public ComposedTypeModel getAbstractOrderEntryType(final AbstractOrderModel order)
	{
		ComposedTypeModel ret = null;
		final String orderTypeCode = getModelService().getModelType(order);
		ret = getOrderEntryTypeFromMapping(orderTypeCode);
		if (ret == null)
		{
			ret = getFallbackEntryComposedType(order);
		}
		return ret;
	}

	@Override
	public Class getAbstractOrderEntryClassForType(final ComposedTypeModel entryType)
	{
		return typeService.getModelClass(entryType);
	}

	private ComposedTypeModel getOrderEntryTypeFromMapping(final String orderTypeCode)
	{
		if (orderType2orderEntryTypeMapping == null || orderType2orderEntryTypeMapping.isEmpty())
		{
			return null;
		}

		String currentTypeCode = orderTypeCode;
		final ComposedTypeModel abstractOrderType = typeService.getComposedTypeForCode(AbstractOrderModel._TYPECODE);
		//handle inheritance
		while (!orderType2orderEntryTypeMapping.containsKey(currentTypeCode))
		{
			final ComposedTypeModel currentType = typeService.getComposedTypeForCode(currentTypeCode);
			if (!typeService.isAssignableFrom(abstractOrderType, currentType))
			{
				//check only up to abstract order
				return null;
			}
			currentTypeCode = currentType.getSuperType().getCode();
		}

		return typeService.getComposedTypeForCode(orderType2orderEntryTypeMapping.get(currentTypeCode));
	}

	private ComposedTypeModel getFallbackEntryComposedType(final AbstractOrderModel order)
	{
		final String orderType = getModelService().getModelType(order);
		final AttributeDescriptorModel entriesDescriptor = typeService.getAttributeDescriptor(orderType, ENTRIES);
		final TypeModel atomicType = ((CollectionTypeModel) entriesDescriptor.getAttributeType()).getElementType();
		return typeService.getComposedTypeForCode(atomicType.getCode());
	}

	public void setOrderType2orderEntryTypeMapping(final Map<String, String> orderType2orderEntryTypeMapping)
	{
		this.orderType2orderEntryTypeMapping = orderType2orderEntryTypeMapping;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

}
