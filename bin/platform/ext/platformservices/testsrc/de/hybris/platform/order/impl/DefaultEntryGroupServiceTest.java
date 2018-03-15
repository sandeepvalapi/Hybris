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
 */
package de.hybris.platform.order.impl;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.enums.GroupType;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.order.EntryGroup;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import org.junit.Test;


@UnitTest
public class DefaultEntryGroupServiceTest
{
	final protected DefaultEntryGroupService entryGroupService = new DefaultEntryGroupService();
	
	final protected GroupType groupType = GroupType.valueOf("TEST_TYPE");

	final protected EntryGroup root = entryGroup(1, entryGroup(2), entryGroup(3, entryGroup(10), entryGroup(11)));


	@Test
	public void shouldThrowExceptionIfGroupDoesNotExist()
	{
		assertThatThrownBy(() -> entryGroupService.getGroup(new AbstractOrderModel(), 1)).isInstanceOf(
				IllegalArgumentException.class).hasMessage("Parameter order.entryGroups can not be null");
	}

	@Test
	public void shouldReturnEntryGroupByNumber()
	{
		final AbstractOrderModel order = new AbstractOrderModel();
		order.setEntryGroups(Collections.singletonList(root));
		final EntryGroup group = entryGroupService.getGroup(order, 10);

		assertEquals(10, (int) group.getGroupNumber());
	}

	@Test
	public void shouldFlattenEntryGroups()
	{
		final List<EntryGroup> allGroup = entryGroupService.getNestedGroups(root);
		assertThat(
				allGroup,
				containsInAnyOrder(hasProperty("groupNumber", is(1)), hasProperty("groupNumber", is(2)),
						hasProperty("groupNumber", is(3)), hasProperty("groupNumber", is(10)), hasProperty("groupNumber", is(11))));
	}

	@Test
	public void shouldFlattenDownFromGivenSubroot()
	{
		final AbstractOrderModel order = new AbstractOrderModel();
		order.setEntryGroups(Collections.singletonList(root));
		final EntryGroup group = entryGroupService.getGroup(order, 3);
		final List<EntryGroup> allGroup = entryGroupService.getNestedGroups(group);
		assertThat(
				allGroup,
				containsInAnyOrder(hasProperty("groupNumber", is(3)), hasProperty("groupNumber", is(10)),
						hasProperty("groupNumber", is(11))));
	}

	@Test
	public void shouldGetLeaves()
	{
		final List<EntryGroup> leaves = entryGroupService.getLeaves(root);
		assertThat(
				leaves,
				containsInAnyOrder(hasProperty("groupNumber", is(2)), hasProperty("groupNumber", is(10)),
						hasProperty("groupNumber", is(11))));
	}

	@Test
	public void shouldGetRoot()
	{
		final AbstractOrderModel order = new AbstractOrderModel();
		order.setEntryGroups(Arrays.asList(root, entryGroup(100, entryGroup(101))));
		final EntryGroup r = entryGroupService.getRoot(order, 101);
		assertEquals(100, (int) r.getGroupNumber());
	}

	@Test
	public void shouldStartFlatListFromRoot()
	{
		final List<EntryGroup> allGroups = entryGroupService.getNestedGroups(root);
		assertEquals(1, (int) allGroups.get(0).getGroupNumber());
	}

	@Test
	public void shouldPreventSiblingsOrder()
	{
		final EntryGroup child1 = entryGroup(2, entryGroup(3));
		final EntryGroup child2 = entryGroup(4);
		final EntryGroup tree = entryGroup(1, child1, child2);

		final List<EntryGroup> allGroups = entryGroupService.getNestedGroups(tree);

		assertTrue(allGroups.indexOf(child1) < allGroups.indexOf(child2));
	}

	@Test
	public void shouldPutChildrenAfterParent()
	{
		final EntryGroup parent = entryGroup(2, entryGroup(3), entryGroup(4));
		final EntryGroup tree = entryGroup(1, parent);

		final List<EntryGroup> allGroups = entryGroupService.getNestedGroups(tree);

		assertTrue(allGroups.indexOf(parent) < allGroups.indexOf(parent.getChildren().get(0)));
		assertTrue(allGroups.indexOf(parent) < allGroups.indexOf(parent.getChildren().get(1)));
	}

	@Test
	public void shouldGetParentOfLeaf()
	{
		final AbstractOrderModel order = new AbstractOrderModel();
		order.setEntryGroups(Collections.singletonList(root));

		final EntryGroup parentGroup = entryGroupService.getParent(order, 10);

		assertEquals(3, (int) parentGroup.getGroupNumber());
	}

	@Test
	public void shouldGetParentOfRoot()
	{
		final AbstractOrderModel order = new AbstractOrderModel();
		order.setEntryGroups(Collections.singletonList(root));

		final EntryGroup parentGroup = entryGroupService.getParent(order, 1);

		assertNull(parentGroup);
	}

	@Test
	public void shouldThrowExceptionIfNoGroupCart()
	{
		final AbstractOrderModel order = new AbstractOrderModel();
		order.setEntryGroups(Collections.singletonList(root));

		assertThatThrownBy(() -> entryGroupService.getParent(order, 0)).isInstanceOf(IllegalArgumentException.class).hasMessage(
				"No group with number '0' in the order with code '" + order.getCode() + "'");
	}

	@Test
	public void shouldThrowExceptionOnDuplicateEntryGroup()
	{
		final EntryGroup duplicateChild = entryGroup(3);
		final EntryGroup treeRoot = entryGroup(1, duplicateChild, entryGroup(2, duplicateChild));

		assertThatThrownBy(() -> entryGroupService.getNestedGroups(treeRoot)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Found duplicate entry group in subtree of entry group #" + treeRoot.getGroupNumber());
	}

	@Test
	public void shouldThrowExceptionOnCircularDependency()
	{
		final EntryGroup circularDependencyChild = entryGroup(3);
		final EntryGroup treeRoot = entryGroup(1, entryGroup(2, circularDependencyChild));
		circularDependencyChild.setChildren(Collections.singletonList(treeRoot));

		assertThatThrownBy(() -> entryGroupService.getNestedGroups(treeRoot)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Found duplicate entry group in subtree of entry group #" + treeRoot.getGroupNumber());
	}

	@Test
	public void shouldReturnMaxGroupNumber()
	{
		//when
		final int max = entryGroupService
				.findMaxGroupNumber(Arrays.asList(entryGroup(1, entryGroup(100), entryGroup(2)), entryGroup(99)));

		//then
		assertThat(max).isEqualTo(100);
	}

	@Test
	public void findMaxShouldAcceptNullRootList()
	{
		assertThat(entryGroupService.findMaxGroupNumber(null)).isZero();
	}

	@Test
	public void findMaxShouldAcceptEmptyRootList()
	{
		assertThat(entryGroupService.findMaxGroupNumber(Collections.emptyList())).isZero();
	}

	@Test
	public void getGroupOfTypeShouldThrowExceptionIfNoGroupsInOrder()
	{
		final AbstractOrderModel order = new AbstractOrderModel();
		order.setEntryGroups(Collections.emptyList());
		
		assertThatThrownBy(() -> entryGroupService.getGroupOfType(order, Collections.singletonList(1), groupType)).isInstanceOf(
				IllegalArgumentException.class).hasMessage(
				"No group with number '1' in the order with code '" + order.getCode() + "'");
	}

	@Test
	public void getGroupOfTypeShouldReturnNullIfNoGroupsInList()
	{
		final AbstractOrderModel order = new AbstractOrderModel();
		order.setEntryGroups(Collections.singletonList(root));

		assertNull(entryGroupService.getGroupOfType(order, Collections.emptyList(), groupType));
	}

	@Test
	public void getGroupOfTypeShouldThrowExceptionIfNoSuchGroupOrder()
	{
		final AbstractOrderModel order = new AbstractOrderModel();
		order.setEntryGroups(Collections.singletonList(root));

		assertThatThrownBy(() -> entryGroupService.getGroupOfType(order, Collections.singletonList(5), groupType)).isInstanceOf(
				IllegalArgumentException.class).hasMessage("No group with number '5' in the order with code '" + order.getCode() + "'");
	}

	@Test
	public void getGroupOfTypeShouldReturnNullIfNoSuchTypeInOrder()
	{
		final AbstractOrderModel order = new AbstractOrderModel();
		order.setEntryGroups(Collections.singletonList(root));

		assertNull(entryGroupService.getGroupOfType(order, Collections.singletonList(1), GroupType.valueOf("ANOTHER_TYPE")));
	}

	@Test
	public void getGroupOfTypeShouldReturnCorrectEntryGroup()
	{
		final AbstractOrderModel order = new AbstractOrderModel();
		order.setEntryGroups(Collections.singletonList(root));

		final EntryGroup entryGroup = entryGroupService.getGroupOfType(order, Collections.singletonList(2), groupType);

		assertEquals(2, (int) entryGroup.getGroupNumber());
	}
	
	@Test
	public void getGroupOfTypeShouldThrowExceptionIfMoreThanOneGroupOfType()
	{
		final EntryGroup rootGroup= entryGroup(1, entryGroup(2), entryGroup(3));
		final AbstractOrderModel order = new AbstractOrderModel();
		order.setEntryGroups(Collections.singletonList(rootGroup));

		assertThatThrownBy(() -> entryGroupService.getGroupOfType(order, Arrays.asList(2, 3), groupType)).isInstanceOf(
				AmbiguousIdentifierException.class).hasMessage("More than one entry group of type TEST_TYPE found in order " + order.getCode()
						+ " with numbers [2, 3]");
	}

	protected EntryGroup entryGroup(final Integer number, final EntryGroup... children)
	{
		final EntryGroup result = new EntryGroup();
		result.setGroupNumber(number);
		result.setGroupType(groupType);
		result.setChildren(Stream.of(children).collect(Collectors.toList()));
		return result;
	}

}
