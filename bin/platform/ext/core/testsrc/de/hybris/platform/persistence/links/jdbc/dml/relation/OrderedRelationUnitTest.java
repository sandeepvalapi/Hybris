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
public class OrderedRelationUnitTest
{
	@Test
	public void shouldNoGenerateOperationsForRemovalOfEmptyItems()
	{
		final TestableOrderedRelation r = givenRelation(false, //
				link(1, 'A', 0), //
				link(2, 'B', 1), //
				link(3, 'C', 2));

		final Iterable<RelationModification> operations = r.getModificationsForRemoval(requested());

		assertThat(operations).isNotNull().isEmpty();
	}

	@Test
	public void shouldNoGenerateOperationsForRemovalWhenRelationHasNoChildren()
	{
		final TestableOrderedRelation r = givenRelation(false);

		final Iterable<RelationModification> operations = r.getModificationsForRemoval(requested('A', 'B'));

		assertThat(operations).isNotNull().isEmpty();
	}

	@Test
	public void shouldGenerateRemoveOperation()
	{
		final TestableOrderedRelation r = givenRelation(false, //
				link(1, 'A', 0), //
				link(2, 'B', 1), //
				link(3, 'C', 2));

		final Iterable<RelationModification> operations = r.getModificationsForRemoval(requested('B'));

		assertThat(operations).isNotNull().hasSize(1);
		assertThat(operations).containsOnly(r.remove(2));
	}

	@Test
	public void shouldGenerateRemoveOperationsForAllChildren()
	{
		final TestableOrderedRelation r = givenRelation(false, //
				link(1, 'A', 0), //
				link(2, 'B', 1), //
				link(3, 'C', 2));

		final Iterable<RelationModification> operations = r.getModificationsForRemoval(requested('B', 'A', 'C'));

		assertThat(operations).isNotNull().hasSize(3);
		assertThat(operations).containsOnly(r.remove(2), r.remove(3), r.remove(1));
	}

	@Test
	public void shouldInsertWhenThereIsNoExistingLink()
	{
		final TestableOrderedRelation r = givenRelation(false);

		final Iterable<RelationModification> operations = r.getModificationsForInsertion(requested('D', 'E'), -4);

		assertThat(operations).isNotNull().hasSize(2);
		assertThat(operations).containsOnly(r.add('D', 0), r.add('E', 1));
	}

	@Test
	public void shouldInsertChildrenAtTheEndWhenPositionIsNegative()
	{
		final TestableOrderedRelation r = givenRelation(false, //
				link(1, 'A', 0), //
				link(2, 'B', 1), //
				link(3, 'C', 2));

		final Iterable<RelationModification> operations = r.getModificationsForInsertion(requested('D', 'E'), -1);

		assertThat(operations).isNotNull().hasSize(2);
		assertThat(operations).containsOnly(r.add('D', 3), r.add('E', 4));
	}

	@Test
	public void shouldInsertChildrenAtTheBeginningAndShiftExistingLinks()
	{
		final TestableOrderedRelation r = givenRelation(false, //
				link(1, 'A', 0), //
				link(2, 'B', 1), //
				link(3, 'C', 2));

		final Iterable<RelationModification> operations = r.getModificationsForInsertion(requested('D', 'E'), 0);

		assertThat(operations).isNotNull().hasSize(5);
		assertThat(operations).containsOnly(r.add('D', 0), r.add('E', 1), r.shift(1, 2), r.shift(2, 3), r.shift(3, 4));
	}

	@Test
	public void shouldInsertChildrenAtTheBeginningAndNotShiftExistingLinks()
	{
		final TestableOrderedRelation r = givenRelation(false, //
				link(1, 'A', 2), //
				link(2, 'B', 3), //
				link(3, 'C', 4));

		final Iterable<RelationModification> operations = r.getModificationsForInsertion(requested('D', 'E'), 0);

		assertThat(operations).isNotNull().hasSize(2);
		assertThat(operations).containsOnly(r.add('D', 0), r.add('E', 1));
	}

	@Test
	public void shouldInsertChildrenInTheMiddleAndNotShiftExistingLinks()
	{
		final TestableOrderedRelation r = givenRelation(false, //
				link(1, 'A', 0), //
				link(2, 'B', 1), //
				link(3, 'C', 5));

		final Iterable<RelationModification> operations = r.getModificationsForInsertion(requested('D', 'E'), 2);

		assertThat(operations).isNotNull().hasSize(2);
		assertThat(operations).containsOnly(r.add('D', 2), r.add('E', 3));
	}

	@Test
	public void shouldInsertChildrenInTheMiddleAndShiftExistingLinks()
	{
		final TestableOrderedRelation r = givenRelation(false, //
				link(1, 'A', 0), //
				link(2, 'B', 1), //
				link(3, 'C', 2));

		final Iterable<RelationModification> operations = r.getModificationsForInsertion(requested('D', 'E'), 1);

		assertThat(operations).isNotNull().hasSize(4);
		assertThat(operations).containsOnly(r.add('D', 1), r.add('E', 2), r.shift(2, 3), r.shift(3, 4));
	}

	@Test
	public void shouldSetChildrenForEmptyRelation()
	{
		final TestableOrderedRelation r = givenRelation(false);

		final Iterable<RelationModification> operations = r.getModificationsForSetting(requested('D', 'E'));

		assertThat(operations).isNotNull().hasSize(2);
		assertThat(operations).containsOnly(r.add('D', 0), r.add('E', 1));
	}

	@Test
	public void shouldRemoveAllExistingLinksWhenSettingToEmptyCollection()
	{
		final TestableOrderedRelation r = givenRelation(false, //
				link(1, 'A', 0), //
				link(2, 'B', 1), //
				link(3, 'C', 2));

		final Iterable<RelationModification> operations = r.getModificationsForSetting(requested());

		assertThat(operations).isNotNull().hasSize(3);
		assertThat(operations).containsOnly(r.remove(1), r.remove(2), r.remove(3));
	}

	@Test
	public void shouldReuseAllExistingLinksWithoutGaps()
	{
		final TestableOrderedRelation r = givenRelation(false, //
				link(1, 'A', 0), //
				link(2, 'B', 1), //
				link(3, 'C', 2));

		final Iterable<RelationModification> operations = r.getModificationsForSetting(requested('A', 'B', 'C'));

		assertThat(operations).isNotNull().hasSize(0);
	}

	@Test
	public void shouldReuseAllExistingLinksWithGaps()
	{
		final TestableOrderedRelation relation = givenRelation(false, //
				link(1, 'A', 7), //
				link(2, 'B', 16), //
				link(3, 'C', 1234));

		final Iterable<RelationModification> operations = relation.getModificationsForSetting(requested('A', 'B', 'C'));

		assertThat(operations).isNotNull().hasSize(0);
	}

	@Test
	public void shouldReusePartOfExistingLinks()
	{
		final TestableOrderedRelation r = givenRelation(false, //
				link(1, 'A', 7), //
				link(2, 'B', 16), //
				link(3, 'C', 1234), //
				link(4, 'D', 9876));

		final Iterable<RelationModification> operations = r.getModificationsForSetting(requested('B', 'D'));

		assertThat(operations).isNotNull().hasSize(2);
		assertThat(operations).containsOnly(r.remove(1), r.remove(3));
	}

	@Test
	public void shouldNotShiftWhenLinkCanBeReused()
	{
		final TestableOrderedRelation r = givenRelation(false, //
				link(1, 'A', 7), //
				link(2, 'B', 16), //
				link(3, 'C', 1234), //
				link(4, 'D', 9876));

		final Iterable<RelationModification> operations = r.getModificationsForSetting(requested('E', 'A', 'F', 'B', 'G', 'C', 'H',
				'D', 'I'));

		assertThat(operations).isNotNull().hasSize(5);
		assertThat(operations).containsOnly(r.add('E', 0), r.add('F', 8), r.add('G', 17), r.add('H', 1235), r.add('I', 9877));
	}

	@Test
	public void shouldShiftWhenPositionOfExistingLinkMustBeChanged()
	{
		final TestableOrderedRelation r = givenRelation(false, //
				link(1, 'A', 7), //
				link(2, 'B', 8), //
				link(3, 'C', 10), //
				link(4, 'D', 100));

		final Iterable<RelationModification> operations = r.getModificationsForSetting(requested('A', 'F', 'B', 'C', 'D'));

		assertThat(operations).isNotNull().hasSize(2);
		assertThat(operations).containsOnly(r.add('F', 8), r.shift(2, 9));
	}

	@Test
	public void shouldReuseMultipleLinksToTheSameChild()
	{
		final TestableOrderedRelation r = givenRelation(false, //
				link(1, 'A', 0), //
				link(2, 'B', 1), //
				link(3, 'A', 2));

		final Iterable<RelationModification> operations = r.getModificationsForSetting(requested('A', 'A'));

		assertThat(operations).isNotNull().hasSize(1);
		assertThat(operations).containsOnly(r.remove(2));
	}

	@Test
	public void shouldBeSmart()
	{
		final TestableOrderedRelation r = givenRelation(false, //
				link(1, 'A', 0), //
				link(2, 'B', 2), //
				link(3, 'B', 7), //
				link(4, 'A', 10));

		final Iterable<RelationModification> operations = r.getModificationsForSetting(requested('B', 'A', 'A', 'B'));

		assertThat(operations).isNotNull().hasSize(2);
		assertThat(operations).containsOnly(r.shift(1, 11), r.shift(3, 12));
	}

	@Test
	public void shouldTouchParrentAndChildWhenRemovingExistingLink()
	{
		final TestableOrderedRelation r = givenRelation(true, //
				link(1, 'A', 0), //
				link(2, 'B', 2));

		final Iterable<RelationModification> operations = r.getModificationsForRemoval(requested('B'));

		assertThat(operations).isNotNull().isNotEmpty();
		assertThat(operations).contains(r.touchedParent(), r.touchedChild('B'));
	}

	@Test
	public void shouldNotTouchParrentAndChildWhenRemovingNonExistingLink()
	{
		final TestableOrderedRelation r = givenRelation(true, //
				link(1, 'A', 0), //
				link(2, 'B', 2));

		final Iterable<RelationModification> operations = r.getModificationsForRemoval(requested('G'));

		assertThat(operations).isNotNull().isEmpty();
	}

	@Test
	public void shouldTouchParrentAndChildWhenInsertingNewLink()
	{
		final TestableOrderedRelation r = givenRelation(true, //
				link(1, 'A', 0));

		final Iterable<RelationModification> operations = r.getModificationsForInsertion(requested('B'), 1);

		assertThat(operations).isNotNull().isNotEmpty();
		assertThat(operations).contains(r.touchedParent(), r.touchedChild('B'));
	}

	@Test
	public void shouldNotTouchNotShiftedChildWhenInsertingNewLink()
	{
		final TestableOrderedRelation r = givenRelation(true, //
				link(1, 'A', 0), //
				link(2, 'C', 10));

		final Iterable<RelationModification> operations = r.getModificationsForInsertion(requested('B'), 1);

		assertThat(operations).isNotNull().isNotEmpty();
		assertThat(operations).containsOnly(r.add('B', 1), r.touchedParent(), r.touchedChild('B'));
	}

	@Test
	public void shouldNotTouchShiftedChildWhenInsertingNewLink()
	{
		final TestableOrderedRelation r = givenRelation(true, //
				link(1, 'A', 0), //
				link(2, 'C', 1));

		final Iterable<RelationModification> operations = r.getModificationsForInsertion(requested('B'), 1);

		assertThat(operations).isNotNull().isNotEmpty();
		assertThat(operations).containsOnly(r.add('B', 1), r.touchedParent(), r.touchedChild('B'), r.shift(2, 2));
	}

	@Test
	public void shouldNotTouchParrentAndChildWhenSettingToTheSameLinks()
	{
		final TestableOrderedRelation r = givenRelation(true, //
				link(1, 'A', 0), //
				link(2, 'B', 10));

		final Iterable<RelationModification> operations = r.getModificationsForSetting(requested('A', 'B'));

		assertThat(operations).isNotNull().isEmpty();
	}

	private TestableOrderedRelation givenRelation(final boolean markAsModified, final ExistingLinkToChild... links)
	{
		final RelationId id = new RelationId(123, 321);
		return new TestableOrderedRelation(id, ImmutableList.copyOf(links), markAsModified);
	}

	private static class TestableOrderedRelation extends OrderedRelation
	{

		public TestableOrderedRelation(final RelationId relationId, final Iterable<ExistingLinkToChild> existingLinks,
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

		public RelationModification shift(final long id, final int toPosition)
		{
			return new Shift(id, 0, toPosition);
		}

		private RelationModification add(final char ch, final int position)
		{
			return new Add(ch, position);
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

	private ExistingLinkToChild link(final long id, final char ch, final int position)
	{
		return new ExistingLinkToChild(id, ch, position, 0);
	}
}
