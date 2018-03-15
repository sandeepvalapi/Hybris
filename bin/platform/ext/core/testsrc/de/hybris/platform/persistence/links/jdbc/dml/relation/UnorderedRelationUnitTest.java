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
package de.hybris.platform.persistence.links.jdbc.dml.relation;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.persistence.links.jdbc.dml.RelationModification;

import org.junit.Test;

import com.google.common.collect.ImmutableList;


@UnitTest
public class UnorderedRelationUnitTest
{
	@Test
	public void shouldNoGenerateOperationsForRemovalOfEmptyItems()
	{
		final TestableUnorderedRelation r = givenRelation(false, //
				link(1, 'A'), //
				link(2, 'B'), //
				link(3, 'C'));

		final Iterable<RelationModification> operations = r.getModificationsForRemoval(requested());

		assertThat(operations).isNotNull().isEmpty();
	}

	@Test
	public void shouldNoGenerateOperationsForRemovalWhenRelationHasNoChildren()
	{
		final TestableUnorderedRelation r = givenRelation(false);

		final Iterable<RelationModification> operations = r.getModificationsForRemoval(requested('A', 'B'));

		assertThat(operations).isNotNull().isEmpty();
	}

	@Test
	public void shouldGenerateRemoveOperation()
	{
		final TestableUnorderedRelation r = givenRelation(false, //
				link(1, 'A'), //
				link(2, 'B'), //
				link(3, 'C'));

		final Iterable<RelationModification> operations = r.getModificationsForRemoval(requested('B'));

		assertThat(operations).isNotNull().hasSize(1);
		assertThat(operations).containsOnly(r.remove(2));
	}

	@Test
	public void shouldGenerateRemoveOperationsForAllChildren()
	{
		final TestableUnorderedRelation r = givenRelation(false, //
				link(1, 'A'), //
				link(2, 'B'), //
				link(3, 'C'));

		final Iterable<RelationModification> operations = r.getModificationsForRemoval(requested('B', 'A', 'C'));

		assertThat(operations).isNotNull().hasSize(3);
		assertThat(operations).containsOnly(r.remove(2), r.remove(3), r.remove(1));
	}

	@Test
	public void shouldInsertWhenThereIsNoExistingLink()
	{
		final TestableUnorderedRelation r = givenRelation(false);

		final Iterable<RelationModification> operations = r.getModificationsForInsertion(requested('D', 'E'), -4);

		assertThat(operations).isNotNull().hasSize(2);
		assertThat(operations).containsOnly(r.add('D'), r.add('E'));
	}

	@Test
	public void shouldInsertChildren()
	{
		final TestableUnorderedRelation r = givenRelation(false, //
				link(1, 'A'), //
				link(2, 'B'), //
				link(3, 'C'));

		final Iterable<RelationModification> operations = r.getModificationsForInsertion(requested('D', 'C'), -1);

		assertThat(operations).isNotNull().hasSize(2);
		assertThat(operations).containsOnly(r.add('D'), r.add('C'));
	}

	@Test
	public void shouldSetChildrenForEmptyRelation()
	{
		final TestableUnorderedRelation r = givenRelation(false);

		final Iterable<RelationModification> operations = r.getModificationsForSetting(requested('D', 'E'));

		assertThat(operations).isNotNull().hasSize(2);
		assertThat(operations).containsOnly(r.add('D'), r.add('E'));
	}

	@Test
	public void shouldRemoveAllExistingLinksWhenSettingToEmptyCollection()
	{
		final TestableUnorderedRelation r = givenRelation(false, //
				link(1, 'A'), //
				link(2, 'B'), //
				link(3, 'C'));

		final Iterable<RelationModification> operations = r.getModificationsForSetting(requested());

		assertThat(operations).isNotNull().hasSize(3);
		assertThat(operations).containsOnly(r.remove(1), r.remove(2), r.remove(3));
	}

	@Test
	public void shouldReuseAllExistingLinksWhenSettingLinks()
	{
		final TestableUnorderedRelation r = givenRelation(false, //
				link(1, 'A'), //
				link(2, 'B'), //
				link(3, 'C'));

		final Iterable<RelationModification> operations = r.getModificationsForSetting(requested('C', 'B', 'A'));

		assertThat(operations).isNotNull().hasSize(0);
	}

	@Test
	public void shouldReusePartOfExistingLinks()
	{
		final TestableUnorderedRelation r = givenRelation(false, //
				link(1, 'A'), //
				link(2, 'B'), //
				link(3, 'C'), //
				link(4, 'D'));

		final Iterable<RelationModification> operations = r.getModificationsForSetting(requested('D', 'E', 'B'));

		assertThat(operations).isNotNull().hasSize(3);
		assertThat(operations).containsOnly(r.remove(1), r.remove(3), r.add('E'));
	}

	@Test
	public void shouldReuseMultipleLinksToTheSameChild()
	{
		final TestableUnorderedRelation r = givenRelation(false, //
				link(1, 'A'), //
				link(2, 'B'), //
				link(3, 'A'));

		final Iterable<RelationModification> operations = r.getModificationsForSetting(requested('A', 'A'));

		assertThat(operations).isNotNull().hasSize(1);
		assertThat(operations).containsOnly(r.remove(2));
	}

	@Test
	public void shouldBeSmart()
	{
		final TestableUnorderedRelation r = givenRelation(false, //
				link(1, 'A'), //
				link(2, 'B'), //
				link(3, 'B'), //
				link(4, 'A'));

		final Iterable<RelationModification> operations = r.getModificationsForSetting(requested('B', 'A', 'A', 'B'));

		assertThat(operations).isNotNull().isEmpty();
	}

	@Test
	public void shouldTouchParrentAndChildWhenRemovingExistingLink()
	{
		final TestableUnorderedRelation r = givenRelation(true, //
				link(1, 'A'), //
				link(2, 'B'));

		final Iterable<RelationModification> operations = r.getModificationsForRemoval(requested('B'));

		assertThat(operations).isNotNull().isNotEmpty();
		assertThat(operations).contains(r.touchedParent(), r.touchedChild('B'));
	}

	@Test
	public void shouldNotTouchParrentAndChildWhenRemovingNonExistingLink()
	{
		final TestableUnorderedRelation r = givenRelation(true, //
				link(1, 'A'), //
				link(2, 'B'));

		final Iterable<RelationModification> operations = r.getModificationsForRemoval(requested('G'));

		assertThat(operations).isNotNull().isEmpty();
	}

	@Test
	public void shouldTouchParrentAndChildWhenInsertingNewLink()
	{
		final TestableUnorderedRelation r = givenRelation(true, //
				link(1, 'A'));

		final Iterable<RelationModification> operations = r.getModificationsForInsertion(requested('B'), 1);

		assertThat(operations).isNotNull().isNotEmpty();
		assertThat(operations).contains(r.touchedParent(), r.touchedChild('B'));
	}

	@Test
	public void shouldNotTouchParrentAndChildWhenSettingToTheSameLinks()
	{
		final TestableUnorderedRelation r = givenRelation(true, //
				link(1, 'A'), //
				link(2, 'B'));

		final Iterable<RelationModification> operations = r.getModificationsForSetting(requested('A', 'B'));

		assertThat(operations).isNotNull().isEmpty();
	}


	private TestableUnorderedRelation givenRelation(final boolean markAsModified, final ExistingLinkToChild... links)
	{
		final RelationId id = new RelationId(123, 321);
		return new TestableUnorderedRelation(id, ImmutableList.copyOf(links), markAsModified);
	}

	private static class TestableUnorderedRelation extends UnorderedRelation
	{
		public TestableUnorderedRelation(final RelationId relationId, final Iterable<ExistingLinkToChild> existingLinks,
				final boolean markAsModified)
		{
			super(relationId, existingLinks, markAsModified);
		}


		public RelationModification touchedChild(final char ch)
		{
			return new Touch(ch);
		}

		public RelationModification touchedParent()
		{
			return new Touch(getParentPK());
		}

		private RelationModification add(final char ch)
		{
			return new Add(ch, POSITION_FOR_UNORDERED_LINKS);
		}

		private RelationModification remove(final long id)
		{
			return new Remove(id, 0);
		}
	}

	private Iterable<Long> requested(final char... chars)
	{
		final ImmutableList.Builder<Long> resultBuilder = ImmutableList.builder();

		for (final char ch : chars)
		{
			resultBuilder.add(Long.valueOf(ch));
		}

		return resultBuilder.build();
	}

	private ExistingLinkToChild link(final long id, final char ch)
	{
		return new ExistingLinkToChild(id, ch, TestableUnorderedRelation.POSITION_FOR_UNORDERED_LINKS, 0);
	}
}
