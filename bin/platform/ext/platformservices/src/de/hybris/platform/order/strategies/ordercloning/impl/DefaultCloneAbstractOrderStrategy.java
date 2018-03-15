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
package de.hybris.platform.order.strategies.ordercloning.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.order.AbstractOrderEntryTypeService;
import de.hybris.platform.order.strategies.ordercloning.CloneAbstractOrderStrategy;
import de.hybris.platform.servicelayer.internal.model.impl.ItemModelCloneCreator;
import de.hybris.platform.servicelayer.internal.model.impl.ItemModelCloneCreator.CopyContext;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Use {@link ItemModelCloneCreator} to clone one of subtype of {@link AbstractOrderModel} to other (or the same)
 * subtype.
 */
public class DefaultCloneAbstractOrderStrategy implements CloneAbstractOrderStrategy
{

	private final TypeService typeService;
	private final ItemModelCloneCreator itemModelCloneCreator;
	private final AbstractOrderEntryTypeService abstractOrderEntryTypeService;


	public DefaultCloneAbstractOrderStrategy(final TypeService typeService, final ItemModelCloneCreator itemModelCloneCreator,
			final AbstractOrderEntryTypeService abstractOrderEntryTypeService)
	{
		super();
		this.typeService = typeService;
		this.itemModelCloneCreator = itemModelCloneCreator;
		this.abstractOrderEntryTypeService = abstractOrderEntryTypeService;
	}

	/**
	 * Create clone of original and change the type of order and orderEntries
	 * 
	 * @param _orderType
	 *           clone order type
	 * @param _entryType
	 *           clone order entry type
	 * @param original
	 *           original order
	 * @param code
	 *           clone code
	 * @param abstractOrderClassResult
	 *           super class of clone (usually OrderModel or CartModel)
	 */
	@Override
	public <T extends AbstractOrderModel> T clone(final ComposedTypeModel _orderType, final ComposedTypeModel _entryType,
			final AbstractOrderModel original, final String code, final Class abstractOrderClassResult,
			final Class abstractOrderEntryClassResult)
	{
		validateParameterNotNull(original, "original must not be null!");
		validateParameterNotNull(abstractOrderClassResult, "abstractOrderClassResult must not be null!");
		validateParameterNotNull(abstractOrderEntryClassResult, "abstractOrderEntryClassResult must not be null!");

		final ComposedTypeModel orderType = getOrderType(_orderType, original, abstractOrderClassResult);
		final ComposedTypeModel entryType = getOrderEntryType(_entryType, original, abstractOrderClassResult,
				abstractOrderEntryClassResult);

		final CopyContext copyContext = new CopyContext()
		{
			@Override
			public ComposedTypeModel getTargetType(final ItemModel originalModel)
			{
				if (originalModel instanceof AbstractOrderEntryModel)
				{
					return entryType;
				}
				return super.getTargetType(originalModel);
			}
		};

		final T orderClone = (T) itemModelCloneCreator.copy(orderType, original, copyContext);
		if (code != null)
		{
			orderClone.setCode(code);
		}
		postProcess(original, orderClone);
		return orderClone;
	}

	/**
	 * Clone the entries of the <b>original</b> and change the type according to the <b>entryType</b> given.
	 * 
	 * @param _entryType
	 *           desited {@link ComposedTypeModel} of the cloned cart entries .
	 * @param original
	 *           original {@link AbstractOrderModel}
	 * @return collection of cloned entries
	 */
	@Override
	public <T extends AbstractOrderEntryModel> List<T> cloneEntries(final ComposedTypeModel _entryType,
			final AbstractOrderModel original)
	{
		validateParameterNotNull(original, "original must not be null!");
		ComposedTypeModel entryType = null;
		if (_entryType != null)
		{
			entryType = _entryType;
		}
		else
		{
			entryType = abstractOrderEntryTypeService.getAbstractOrderEntryType(original);
		}

		final CopyContext copyContext = new CopyContext()
		{
			@Override
			protected boolean skipAttribute(final Object original, final String qualifier)
			{
				if (AbstractOrderEntryModel.ORDER.equals(qualifier))
				{
					return true;
				}
				else
				{
					return super.skipAttribute(original, qualifier);
				}
			}
		};
		final List<ItemModel> entriesItems = new ArrayList<ItemModel>(original.getEntries());

		final List<ItemModel> itemClones = itemModelCloneCreator.copyAll(entryType, entriesItems, copyContext);
		if (itemClones != null)
		{
			final List<T> entriesClones = new ArrayList<T>(itemClones.size());
			for (final ItemModel itemClone : itemClones)
			{
				entriesClones.add((T) itemClone);
			}
			postProcessEntries(original.getEntries(), entriesClones);
			return entriesClones;
		}
		else
		{
			return Collections.<T> emptyList();
		}


	}

	private <T extends AbstractOrderModel> ComposedTypeModel getOrderType(final ComposedTypeModel orderType,
			final AbstractOrderModel original, final Class<T> clazz)
	{
		if (orderType != null)
		{
			return orderType;
		}

		if (clazz.isAssignableFrom(original.getClass()))
		{
			return typeService.getComposedTypeForClass(original.getClass());

		}

		return typeService.getComposedTypeForClass(clazz);
	}

	private <E extends AbstractOrderEntryModel, T extends AbstractOrderModel> ComposedTypeModel getOrderEntryType(
			final ComposedTypeModel entryType, final AbstractOrderModel original, final Class<T> orderClazz, final Class<E> clazz)
	{
		if (entryType != null)
		{
			return entryType;
		}

		if (orderClazz.isAssignableFrom(original.getClass()))
		{
			return abstractOrderEntryTypeService.getAbstractOrderEntryType(original);
		}

		return typeService.getComposedTypeForClass(clazz);
	}


	private static <T extends AbstractOrderEntryModel> void postProcessEntries(
			final List<AbstractOrderEntryModel> originalEntries, final List<T> entriesClones)
	{
		for (final AbstractOrderEntryModel entry : entriesClones)
		{
			entry.setCalculated(Boolean.FALSE);
		}
	}

	protected void postProcess(final AbstractOrderModel original, final AbstractOrderModel copy)
	{
		copyTotalTaxValues(original, copy);
		copyCalculatedFlag(original, copy);
	}

	protected void copyTotalTaxValues(final AbstractOrderModel original, final AbstractOrderModel copy)
	{
		copy.setTotalTaxValues(original.getTotalTaxValues());

	}


	protected void copyCalculatedFlag(final AbstractOrderModel original, final AbstractOrderModel copy)
	{
		copy.setCalculated(original.getCalculated());

		final List<AbstractOrderEntryModel> originalEntries = original.getEntries();
		final List<AbstractOrderEntryModel> copyEntries = copy.getEntries();

		final int copyEntriesSize = copyEntries == null ? 0 : copyEntries.size();

		if (originalEntries.size() != copyEntriesSize)
		{
			throw new IllegalStateException("different entry numbers in original and copied order ( " + originalEntries.size()
					+ "<>" + copyEntries.size() + ")");
		}

		normalizeEntriesNumbers(originalEntries);

		for (int i = 0; i < originalEntries.size(); i++)
		{
			final AbstractOrderEntryModel originalEntry = originalEntries.get(i);
			final AbstractOrderEntryModel copyEntry = copyEntries.get(i);
			copyEntry.setCalculated(originalEntry.getCalculated());
		}
	}

	private static void normalizeEntriesNumbers(final List<AbstractOrderEntryModel> allEntries)
	{
		for (int i = 0; i < allEntries.size(); i++)
		{
			final AbstractOrderEntryModel oEntry = allEntries.get(i);
			oEntry.setEntryNumber(Integer.valueOf(i));
		}
	}



}
