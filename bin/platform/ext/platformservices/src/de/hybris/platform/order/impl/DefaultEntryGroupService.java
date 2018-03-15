/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 *
 */
package de.hybris.platform.order.impl;

import de.hybris.platform.core.enums.GroupType;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.order.EntryGroupService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link EntryGroupService}
 */
public class DefaultEntryGroupService implements EntryGroupService
{
	private ModelService modelService;

	@Nonnull
	@Override
	public List<EntryGroup> getNestedGroups(@Nonnull final EntryGroup entryGroup)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("entryGroup", entryGroup);

		final List<EntryGroup> result = new ArrayList<>();
		result.add(entryGroup);
		for (int i = 0; i < result.size(); i++)
		{
			final List<EntryGroup> children = result.get(i).getChildren();
			if (CollectionUtils.isNotEmpty(children))
			{
				if (CollectionUtils.containsAny(children, result))
				{
					throw new IllegalArgumentException("Found duplicate entry group in subtree of entry group #"
							+ entryGroup.getGroupNumber());
				}
				result.addAll(children);
			}
		}
		return result;
	}

	@Nonnull
	@Override
	public List<EntryGroup> getLeaves(@Nonnull final EntryGroup entryGroup)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("entryGroup", entryGroup);
		return getNestedGroups(entryGroup).stream().filter(e -> CollectionUtils.isEmpty(e.getChildren()))
				.collect(Collectors.toList());
	}

	@Nonnull
	@Override
	public EntryGroup getGroup(@Nonnull final AbstractOrderModel order, @Nonnull final Integer groupNumber)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("order", order);
		ServicesUtil.validateParameterNotNullStandardMessage("groupNumber", groupNumber);
		ServicesUtil.validateParameterNotNullStandardMessage("order.entryGroups", order.getEntryGroups());

		return order
				.getEntryGroups()
				.stream()
				.map(this::getNestedGroups)
				.flatMap(Collection::stream)
				.filter(e -> groupNumber.equals(e.getGroupNumber()))
				.findAny()
				.orElseThrow(
						() -> new IllegalArgumentException("No group with number '" + groupNumber + "' in the order with code '"
								+ order.getCode() + "'"));
	}

	@Nonnull
	@Override
	public EntryGroup getRoot(@Nonnull final AbstractOrderModel order, @Nonnull final Integer groupNumber)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("order", order);
		ServicesUtil.validateParameterNotNullStandardMessage("groupNumber", groupNumber);
		ServicesUtil.validateParameterNotNullStandardMessage("order.entryGroups", order.getEntryGroups());

		return order
				.getEntryGroups()
				.stream()
				.filter(root -> getNestedGroups(root).stream().map(EntryGroup::getGroupNumber).anyMatch(groupNumber::equals))
				.findAny()
				.orElseThrow(
						() -> new IllegalArgumentException("No group with number '" + groupNumber + "' in the order with code '"
								+ order.getCode() + "'"));
	}

	@Override
	public EntryGroup getParent(@Nonnull final AbstractOrderModel order, @Nonnull final Integer groupNumber)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("order", order);
		ServicesUtil.validateParameterNotNullStandardMessage("groupNumber", groupNumber);
		ServicesUtil.validateParameterNotNullStandardMessage("order.entryGroups", order.getEntryGroups());

		final EntryGroup group = getGroup(order, groupNumber);

		return order.getEntryGroups().stream().map(this::getNestedGroups).flatMap(Collection::stream)
				.filter(e -> e.getChildren().contains(group)).findAny().orElse(null);
	}

	@Override
	public void forceOrderSaving(@Nonnull final AbstractOrderModel order)
	{
		//we need these 3 lines since model service doesn't see any changes in EntryGroup POJO's without them,
		//and as a result does not save updated entry groups to order
		final List<EntryGroup> groupsTobeSaved = new ArrayList<>(order.getEntryGroups());
		order.setEntryGroups(Collections.emptyList());
		getModelService().save(order);
		order.setEntryGroups(groupsTobeSaved);

		getModelService().save(order);
	}

	@Override
	public int findMaxGroupNumber(final List<EntryGroup> roots)
	{
		if (roots == null || roots.isEmpty())
		{
			return 0;
		}
		return roots.stream()
				.map(this::getNestedGroups)
				.flatMap(Collection::stream)
				.map(EntryGroup::getGroupNumber)
				.max(Integer::compareTo)
				.orElse(0);
	}

	@Override
	public EntryGroup getGroupOfType(@Nonnull final AbstractOrderModel order, @Nonnull final Collection<Integer> groupNumbers, @Nonnull final GroupType type)
	{
		final List<EntryGroup> groups = groupNumbers.
				stream()
				.map(n -> getGroup(order, n))
				.filter(g -> type.equals(g.getGroupType()))
				.collect(Collectors.<EntryGroup>toList());
		
		if (groups.size() > 1)
		{
			throw new AmbiguousIdentifierException("More than one entry group of type "+ type + " found in order " + order.getCode()
					+ " with numbers " + groups.stream().map(g -> g.getGroupNumber()).collect(Collectors.toList()));
		}
		

		return groups.isEmpty() ? null :groups.get(0);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
