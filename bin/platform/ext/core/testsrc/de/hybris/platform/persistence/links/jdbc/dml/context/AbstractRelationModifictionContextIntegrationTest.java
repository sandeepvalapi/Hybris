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
package de.hybris.platform.persistence.links.jdbc.dml.context;

import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.fail;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.WritePersistenceGateway;
import de.hybris.platform.directpersistence.exception.ConcurrentModificationException;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.links.jdbc.JdbcLinksSupport;
import de.hybris.platform.persistence.links.jdbc.dml.RelationModifictionContext;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.tx.Transaction;

import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;


@Ignore
public abstract class AbstractRelationModifictionContextIntegrationTest extends ServicelayerBaseTest
{
	protected static final String RELATION_CODE = "testRelation";
	private static final long LANGUAGE_PK = JdbcLinksSupport.NONE_LANGUAGE_PK_VALUE.longValue();
	private static final Language LANGUAGE = null;

	private final PropertyConfigSwitcher optimisticLockingSwitch = new PropertyConfigSwitcher(
			"hjmp.throw.concurrent.modification.exceptions");

	@Resource
	protected WritePersistenceGateway writePersistenceGateway;

	private LinkManager linkManager;

	private RelationModifictionContext ctx;

	private Title parentItem;
	private Title linkedChildItem;
	private Title notLinkedChildtem;
	private Link linkToLinkedChild;

	@Before
	public void setUp() throws ConsistencyCheckException
	{
		linkManager = LinkManager.getInstance();
		final UserManager userManager = UserManager.getInstance();
		parentItem = userManager.createTitle("PARENT");
		linkedChildItem = userManager.createTitle("LINKED_CHILD");
		notLinkedChildtem = userManager.createTitle("NOT_LINKED_CHILD");
		linkManager.addLinkedItems(parentItem, true, RELATION_CODE, LANGUAGE, ImmutableList.of(linkedChildItem));
		final Collection<Link> allLinks = linkManager.getLinks(RELATION_CODE, parentItem, Link.ANYITEM);
		assertThat(allLinks).isNotNull().hasSize(1);
		linkToLinkedChild = Iterables.getFirst(allLinks, null);
		assertThat(linkToLinkedChild).isNotNull();

		// !!! context is having a *fixed* 'now' date - make sure that it's different from the create items modification time
		final Date nowLater = new Date(parentItem.getModificationTime().getTime() + 2000);
		ctx = instantiateContext(nowLater);
	}

	protected abstract RelationModifictionContext instantiateContext(Date now);

	@Test
	public void shouldTouchItemWhenOptimisticLockingIsDisabled() throws Exception
	{
		doWithOptimisticLockingDisabled(new RunnableWithException()
		{
			@Override
			public void run() throws Exception
			{
				final Date modificationTimeBefore = parentItem.getModificationTime();
				final long versionBefore = parentItem.getPersistenceVersion();
				final long pkValue = parentItem.getPK().getLongValue();

				ctx.touchExistingItem(pkValue);
				ctx.flush();

				assertThat(parentItem.getModificationTime()).isNotNull();
				assertThat(parentItem.getModificationTime().after(modificationTimeBefore)).isTrue();

				// with delayed store = on *inside* a running transaction modified time isn't flushed to database yet
				// and therefore we must see the previous persistence version
				if (isFlushSupposedToHappenInCurrentTx(pkValue))
				{
					assertThat(parentItem.getPersistenceVersion()).isEqualTo(versionBefore + 1);
				}
				else
				{
					assertThat(parentItem.getPersistenceVersion()).isEqualTo(versionBefore);
				}
			}
		});
	}

	protected boolean isFlushSupposedToHappenInCurrentTx(final long pkValue)
	{
		if (ctx instanceof RunningTransactionContext
				&& ((RunningTransactionContext) ctx).shouldUseDirectPersistenceForTouching(PK.fromLong(pkValue)))
		{
			return true;
		}
		return !Transaction.current().isRunning() || !Transaction.current().isDelayedStoreEnabled();
	}

	@Test
	public void shouldCreateNewLinkWhenOptimisticLockingIsDisabled() throws Exception
	{
		doWithOptimisticLockingDisabled(new RunnableWithException()
		{
			@Override
			public void run() throws Exception
			{
				final long parentPK = parentItem.getPK().getLongValue();
				final long childPK = notLinkedChildtem.getPK().getLongValue();

				ctx.addNewLinkToChild(parentPK, LANGUAGE_PK, childPK, 123, 321);
				ctx.flush();

				final Collection<Link> allLinks = linkManager.getLinks(RELATION_CODE, parentItem, Link.ANYITEM);
				assertThat(allLinks).isNotNull().hasSize(2).contains(linkToLinkedChild);
				final Link newLink = FluentIterable.from(allLinks).filter(not(equalTo(linkToLinkedChild))).first().get();
				assertThat(newLink.getLanguage()).isNull();
				assertThat(newLink.getSource()).isEqualTo(parentItem);
				assertThat(newLink.getTarget()).isEqualTo(notLinkedChildtem);
				assertThat(newLink.getSequenceNumber()).isEqualTo(123);
				assertThat(newLink.getReverseSequenceNumber()).isEqualTo(321);
			}
		});
	}

	@Test
	public void shouldShiftExistingLinkWhenOptimisticLockingIsDisabled() throws Exception
	{
		doWithOptimisticLockingDisabled(new RunnableWithException()
		{
			@Override
			public void run() throws Exception
			{
				final long versionBefore = linkToLinkedChild.getPersistenceVersion();
				final long existingLinkPK = linkToLinkedChild.getPK().getLongValue();

				ctx.shiftExistingLink(existingLinkPK, 9876, versionBefore);
				ctx.flush();

				assertThat(linkToLinkedChild.getLanguage()).isNull();
				assertThat(linkToLinkedChild.getSource()).isEqualTo(parentItem);
				assertThat(linkToLinkedChild.getSequenceNumber()).isEqualTo(9876);
				assertThat(linkToLinkedChild.getPersistenceVersion()).isEqualTo(versionBefore + 1);
			}
		});
	}

	@Test
	public void shouldRemoveExistingLinkWhenOptimisticLockingIsDisabled() throws Exception
	{
		doWithOptimisticLockingDisabled(new RunnableWithException()
		{
			@Override
			public void run() throws Exception
			{
				final long versionBefore = linkToLinkedChild.getPersistenceVersion();
				final long existingLinkPK = linkToLinkedChild.getPK().getLongValue();

				ctx.removeExistingLink(existingLinkPK, versionBefore);
				ctx.flush();

				final Collection<Link> allLinks = linkManager.getLinks(RELATION_CODE, parentItem, Link.ANYITEM);
				assertThat(allLinks).isNotNull().isEmpty();
			}
		});
	}


	@Test
	public void shouldFailOnShiftingExistingLinkWhenOptimisticLockingIsEnabledAndVersionDoesntMatch() throws Exception
	{
		doWithOptimisticLockingEnabled(new RunnableWithException()
		{
			@Override
			public void run() throws Exception
			{
				final long versionBefore = linkToLinkedChild.getPersistenceVersion();
				final long existingLinkPK = linkToLinkedChild.getPK().getLongValue();

				try
				{
					ctx.shiftExistingLink(existingLinkPK, 9876, versionBefore + 7);
					ctx.flush();
				}
				catch (@SuppressWarnings("unused") final ConcurrentModificationException ex)
				{
					return;
				}
				fail("ConcurrentModificationException was expected");
			}
		});
	}

	@Test
	public void shouldFailOnRemovingExistingLinkWhenOptimisticLockingIsEnabledAndVersionDoesntMatch() throws Exception
	{
		doWithOptimisticLockingEnabled(new RunnableWithException()
		{
			@Override
			public void run() throws Exception
			{
				final long versionBefore = linkToLinkedChild.getPersistenceVersion();
				final long existingLinkPK = linkToLinkedChild.getPK().getLongValue();

				assertThatExceptionOfType(ConcurrentModificationException.class).isThrownBy(() -> {
					ctx.removeExistingLink(existingLinkPK, versionBefore + 7);
					ctx.flush();
				});
			}
		});

	}

	@Test
	public void shouldNotFailOnShiftingExistingLinkWhenOptimisticLockingIsDisabledAndVersionDoesntMatch() throws Exception
	{
		doWithOptimisticLockingDisabled(new RunnableWithException()
		{
			@Override
			public void run() throws Exception
			{
				final long versionBefore = linkToLinkedChild.getPersistenceVersion();
				final long existingLinkPK = linkToLinkedChild.getPK().getLongValue();

				ctx.shiftExistingLink(existingLinkPK, 9876, versionBefore + 7);
				ctx.flush();

				assertThat(linkToLinkedChild.getLanguage()).isNull();
				assertThat(linkToLinkedChild.getSource()).isEqualTo(parentItem);
				assertThat(linkToLinkedChild.getSequenceNumber()).isEqualTo(9876);
				assertThat(linkToLinkedChild.getPersistenceVersion()).isEqualTo(versionBefore + 8);
			}
		});
	}

	@Test
	public void shouldFailOnRemovingExistingLinkWhenOptimisticLockingIsDisabledAndVersionDoesntMatch() throws Exception
	{
		doWithOptimisticLockingEnabled(new RunnableWithException()
		{
			@Override
			public void run() throws Exception
			{
				final long versionBefore = linkToLinkedChild.getPersistenceVersion();
				final long existingLinkPK = linkToLinkedChild.getPK().getLongValue();

				assertThatExceptionOfType(ConcurrentModificationException.class).isThrownBy(() -> {
					ctx.removeExistingLink(existingLinkPK, versionBefore + 7);
					ctx.flush();
				});
			}
		});
	}

	private void doWithOptimisticLockingEnabled(final RunnableWithException r) throws Exception
	{
		doWithOptimisticLockingSetTo(true, r);
	}

	private void doWithOptimisticLockingDisabled(final RunnableWithException r) throws Exception
	{
		doWithOptimisticLockingSetTo(false, r);
	}

	private void doWithOptimisticLockingSetTo(final boolean value, final RunnableWithException r) throws Exception
	{
		optimisticLockingSwitch.switchToValue(Boolean.valueOf(value).toString());
		try
		{
			r.run();
		}
		finally
		{
			optimisticLockingSwitch.switchBackToDefault();
		}
	}

	private interface RunnableWithException
	{
		void run() throws Exception;
	}
}
