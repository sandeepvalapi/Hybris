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
package de.hybris.platform.order;

import de.hybris.platform.core.enums.GroupType;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;


/**
 * Service that exposes methods to deal with {@link EntryGroup} operations.
 */
public interface EntryGroupService
{
	/**
	 * Gets all nested {@link EntryGroup}s of given one, including the given one.
	 *
	 * @param entryGroup
	 *           root {@link EntryGroup}
	 *
	 * @return all {@link EntryGroup}s for subtree where the given {@link EntryGroup} is root, including the root
	 */
	@Nonnull
	List<EntryGroup> getNestedGroups(@Nonnull final EntryGroup entryGroup);

	/**
	 * Returns all leaf {@link EntryGroup}s of given one. If that one is already leaf it will return itself.
	 *
	 * @param entryGroup
	 *           root {@link EntryGroup}
	 *
	 * @return all leaf {@link EntryGroup}s for subtree where the given {@link EntryGroup} is root or given
	 *         {@link EntryGroup} if it is a leaf.
	 */
	@Nonnull
	List<EntryGroup> getLeaves(@Nonnull final EntryGroup entryGroup);

	/**
	 * Returns {@link EntryGroup} by groupNumber
	 *
	 * @param groupNumber
	 *           number of the group to search for
	 * @param order
	 *           order containing entry group trees
	 *
	 * @return {@link EntryGroup} with given groupNumber from the order
	 * @throws IllegalArgumentException
	 *            if no group with given groupNumber in the order
	 */
	@Nonnull
	EntryGroup getGroup(@Nonnull final AbstractOrderModel order, @Nonnull final Integer groupNumber);

	/**
	 * Returns root of the tree within the order's entry group trees which has given group.
	 *
	 * @param order
	 *           order to take trees from
	 * @param groupNumber
	 *           number of group to search for
	 * @return root of the tree that contains {@code groupNumber}
	 * @throws IllegalArgumentException
	 *            if group is not found
	 */
	@Nonnull
	EntryGroup getRoot(@Nonnull final AbstractOrderModel order, @Nonnull final Integer groupNumber);


	/**
	 * Returns parent entry group of the tree within the order's entry group trees which has given group.
	 *
	 * @param order
	 *           order to take trees from
	 * @param groupNumber
	 *           number of group to search for
	 * @return parent of the {@code groupNumber} or null if it is root
	 * @throws IllegalArgumentException
	 *            if group is not found in the cart
	 */
	EntryGroup getParent(@Nonnull final AbstractOrderModel order, @Nonnull final Integer groupNumber);


	/**
	 *
	 * As {@link EntryGroup} is not a DB entity, changing it does not cause parent order to save. This method updates the
	 * whole {@code entryGroups} field, so the order is marked as changes and correctly saved to DB.
	 *
	 * @param order
	 *           order to save
	 */
	void forceOrderSaving(@Nonnull final AbstractOrderModel order);

	/**
	 * Searches for maximum value of {@link EntryGroup#getGroupNumber()}.
	 * Is useful for assigning unique values to entry group numbers.
	 *
	 * @param roots list of root groups
	 * @return maximum value of groupNumber field
	 */
	int findMaxGroupNumber(final List<EntryGroup> roots);

	/**
	 * Find entry group in the cart by number and type.
	 *
	 * @param order user's cart
	 * @param  groupNumbers  group numbers of the entry
	 * @param  type  group type
	 *
	 * @return {@link EntryGroup} object or null if no groups of given type in collection
	 * @throws AmbiguousIdentifierException if more than one {@link EntryGroup} with provided group type was found
	 */
	@Nonnull
	EntryGroup getGroupOfType(@Nonnull final AbstractOrderModel order, @Nonnull final Collection<Integer> groupNumbers, @Nonnull final GroupType type);
}
