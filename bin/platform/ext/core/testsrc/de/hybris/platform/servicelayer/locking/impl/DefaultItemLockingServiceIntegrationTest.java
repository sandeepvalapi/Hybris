/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.servicelayer.locking.impl;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.locking.ItemLockedForProcessingException;
import de.hybris.platform.core.locking.LockingNotAllowedException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.gateway.AuditSearchQuery;
import de.hybris.platform.persistence.audit.gateway.AuditSearchQuery.QueryBuilder;
import de.hybris.platform.persistence.audit.gateway.ReadAuditGateway;
import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import de.hybris.platform.persistence.audit.internal.AuditEnablementService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.locking.ItemLockingService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultItemLockingServiceIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private ItemLockingService itemLockingService;
	@Resource
	private UserService userService;
	@Resource
	private WriteAuditGateway writeAuditGateway;
	@Resource(name = "auditingEnablementService")
	private AuditEnablementService auditEnablementService;
	@Resource
	private ReadAuditGateway readAuditGateway;

	private UserModel user;
	private PrincipalGroupModel itemLockingGroup;

	private Set<PrincipalGroupModel> oldGroups;
	private Set<PrincipalGroupModel> groupsWithItemLockingGroup;

	private final PropertyConfigSwitcher switcherForAudit = new PropertyConfigSwitcher("audit.user.enabled");
	private final PropertyConfigSwitcher switcherForPersistence = new PropertyConfigSwitcher("persistence.legacy.mode");
	private final PropertyConfigSwitcher switcherForOptimisticLocking = new PropertyConfigSwitcher(
			"hjmp.throw.concurrent.modification.exceptions");

	@Before
	public void setUp()
	{
		user = modelService.create(UserModel.class);
		user.setUid(UUID.randomUUID().toString());
		modelService.save(user);

		itemLockingGroup = modelService.create(UserGroupModel.class);
		itemLockingGroup.setUid("itemLockingGroup");
		modelService.save(itemLockingGroup);

		oldGroups = userService.getCurrentUser().getGroups();
		groupsWithItemLockingGroup = new HashSet<>(oldGroups);

		groupsWithItemLockingGroup.add(itemLockingGroup);

		userService.getCurrentUser().setGroups(groupsWithItemLockingGroup);
	}

	private List<ItemModel> prepareUsers()
	{
		final List<ItemModel> users = new ArrayList<>();
		for (int i = 0; i < 5; i++)
		{
			final UserModel userModel = modelService.create(UserModel.class);
			userModel.setUid(UUID.randomUUID().toString());
			modelService.save(userModel);
			users.add(userModel);
		}

		return users;
	}

	@After
	public void cleanUp()
	{
		if (user.getPk() != null && !modelService.isRemoved(user))
		{
			userService.getCurrentUser().setGroups(groupsWithItemLockingGroup);
			itemLockingService.unlock(user);
			modelService.remove(user);
		}

		switcherForPersistence.switchBackToDefault();
		switcherForOptimisticLocking.switchBackToDefault();
		modelService.remove(itemLockingGroup);
	}

	@Test
	public void testLocking()
	{
		//given
		assertThat(itemLockingService.isLocked(user)).isFalse();

		//when
		itemLockingService.lock(user);

		//then
		assertThat(user.isSealed()).isTrue();
		assertThat(itemLockingService.isLocked(user)).isTrue();
	}

	@Test
	public void testUnLocking()
	{
		//given
		itemLockingService.lock(user);
		assertThat(itemLockingService.isLocked(user)).isTrue();

		//when
		itemLockingService.unlock(user);

		//then
		assertThat(itemLockingService.isLocked(user)).isFalse();
		assertThat(user.isSealed()).isFalse();
	}

	@Test
	public void testSavingAfterItemModifiedWithSLD()
	{
		switcherForPersistence.switchToValue("false");
		testSavingAfterItemModified();
		switcherForPersistence.switchBackToDefault();
	}

	@Test
	public void testSavingAfterItemModifiedWithJalo()
	{
		switcherForPersistence.switchToValue("true");
		testSavingAfterItemModified();
		switcherForPersistence.switchBackToDefault();
	}

	public void testSavingAfterItemModified()
	{
		//given
		ItemLockedForProcessingException e = null;
		itemLockingService.lock(user);
		user.setUid(UUID.randomUUID().toString());

		//when
		try
		{
			modelService.save(user);
		}
		catch (final ItemLockedForProcessingException exception)
		{
			e = exception;
		}

		//then
		assertThat(e).isNotNull();
		assertThat(e.getMessage()).contains("is locked for processing and cannot be saved");
	}

	@Test
	public void testSavingAfterItemModifiedWithSLDWithCollection()
	{
		//given
		switcherForPersistence.switchToValue("false");
		testSavingAfterItemModifiedWithCollection();
		switcherForPersistence.switchBackToDefault();
	}

	@Test
	public void testSavingAfterItemModifiedWithJaloWithCollection()
	{

		//given
		switcherForPersistence.switchToValue("true");
		testSavingAfterItemModifiedWithCollection();
		switcherForPersistence.switchBackToDefault();
	}

	@Test
	public void testSavingAfterItemModifiedWithCollection()
	{
		ItemLockedForProcessingException e = null;

		//given
		itemLockingService.lock(user);

		final UserGroupModel pgm = modelService.create(UserGroupModel.class);
		pgm.setUid(UUID.randomUUID().toString());

		final PrincipalGroupModel group = modelService.create(UserGroupModel.class);
		group.setUid(UUID.randomUUID().toString());

		final Set<PrincipalGroupModel> old = user.getGroups();
		final Set<PrincipalGroupModel> newModifable = new HashSet<>(old);
		newModifable.add(group);
		user.setGroups(newModifable);
		modelService.save(pgm);

		//when
		try
		{
			modelService.save(user);
		}
		catch (final ItemLockedForProcessingException exception)
		{
			e = exception;
		}

		//then
		assertThat(e).isNotNull();
		assertThat(e.getMessage()).contains("is locked for processing and cannot be saved");
	}

	@Test
	public void testSavingAfterItemModifiedWithJaloWithOptimisticLocking()
	{
		switcherForPersistence.switchToValue("true");
		testSavingAfterItemModifiedWithOptimisticLocking();
		switcherForPersistence.switchBackToDefault();
	}

	@Test
	public void testSavingAfterItemModifiedWithSLDWithOptimisticLocking()
	{
		//given
		switcherForPersistence.switchToValue("false");
		testSavingAfterItemModifiedWithOptimisticLocking();
		switcherForPersistence.switchBackToDefault();
	}


	public void testSavingAfterItemModifiedWithOptimisticLocking()
	{
		switcherForOptimisticLocking.switchToValue("true");

		//given
		ItemLockedForProcessingException e = null;
		itemLockingService.lock(user);
		user.setUid(UUID.randomUUID().toString());

		//when
		try
		{
			modelService.save(user);
		}
		catch (final ItemLockedForProcessingException exception)
		{
			e = exception;
		}

		//then
		assertThat(e).isNotNull();
		switcherForOptimisticLocking.switchBackToDefault();
	}


	@Test
	public void testRemovingWithSLD()
	{
		//given
		switcherForPersistence.switchToValue("false");
		testRemoving();
		switcherForPersistence.switchBackToDefault();
	}

	@Test
	public void testRemovingWithJalo()
	{
		switcherForPersistence.switchToValue("true");
		testRemoving();
		switcherForPersistence.switchBackToDefault();
	}

	private void testRemoving()
	{
		//given
		Exception e = null;
		itemLockingService.lock(user);

		//when
		try
		{
			modelService.remove(user);
		}
		catch (final ItemLockedForProcessingException exception)
		{
			e = exception;
		}

		//then
		assertThat(e).isNotNull();
		assertThat(e.getMessage()).contains("is locked for processing and cannot be saved or removed");
	}

	@Test
	public void testWhenUserNotAllowedToLock()
	{
		//given
		userService.getCurrentUser().setGroups(oldGroups);
		modelService.save(user);

		LockingNotAllowedException e = null;

		//when
		try
		{
			itemLockingService.lock(user);
		}
		catch (final LockingNotAllowedException exception)
		{
			e = exception;
		}

		//then
		assertThat(e).isNotNull();
		assertThat(e.getMessage()).startsWith("Current user");
	}

	@Test
	public void testWhenUserIsNotSaved()
	{
		//given
		user = modelService.create(UserModel.class);
		user.setUid(UUID.randomUUID().toString());

		LockingNotAllowedException e = null;

		//when
		try
		{
			itemLockingService.lock(user);
		}
		catch (final LockingNotAllowedException exception)
		{
			e = exception;
		}

		//then
		assertThat(e).isNotNull();
		assertThat(e.getMessage()).startsWith("Item without PK cannot be locked/unlocked");
	}

	@Test
	public void testAuditRecordsCreationAndRemovalForTypeWithLockedUser()
	{
		//given
		switcherForAudit.switchToValue("true");
		auditEnablementService.refreshConfiguredAuditTypes();
		writeAuditGateway.removeAuditRecordsForType("User");
		List<AuditRecord> auditRecordsForType = getAuditRecordsForUsers();
		assertThat(auditRecordsForType).isEmpty();

		//when
		itemLockingService.lock(user);

		//then
		auditRecordsForType = getAuditRecordsForUsers();
		assertThat(auditRecordsForType).hasSize(1);
		assertThat(auditRecordsForType.get(0).getAttributeBeforeOperation("sealed")).isNull();
		assertThat(auditRecordsForType.get(0).getAttributeAfterOperation("sealed")).isEqualTo(true);

		ItemLockedForProcessingException exception = null;

		//when
		try
		{
			writeAuditGateway.removeAuditRecordsForType("User");
		}
		catch (final ItemLockedForProcessingException e)
		{
			exception = e;
		}

		//then
		assertThat(auditRecordsForType).hasSize(1);
		assertThat(exception).isNotNull();
		assertThat(exception.getMessage()).contains("is locked for processing and its audit records cannot be removed");
	}

	@Test
	public void testAuditRecordsCreationAndRemovalForTypeAndPKWithLockedUser()
	{
		//given
		switcherForAudit.switchToValue("true");
		auditEnablementService.refreshConfiguredAuditTypes();
		writeAuditGateway.removeAuditRecordsForType("User");
		assertThat(getAuditRecordsForUsers()).isEmpty();

		//when
		itemLockingService.lock(user);

		//then
		final List<AuditRecord> auditRecordsForType = getAuditRecordsForUsers();
		assertThat(auditRecordsForType).hasSize(1);
		assertThat(auditRecordsForType.get(0).getAttributeBeforeOperation("sealed")).isNull();
		assertThat(auditRecordsForType.get(0).getAttributeAfterOperation("sealed")).isEqualTo(true);
		ItemLockedForProcessingException exception = null;

		//when
		try
		{
			writeAuditGateway.removeAuditRecordsForType("User", user.getPk());
		}
		catch (final ItemLockedForProcessingException e)
		{
			exception = e;
		}

		//then
		assertThat(getAuditRecordsForUsers(user.getPk())).hasSize(1);
		assertThat(exception).isNotNull();
		assertThat(exception.getMessage()).contains("is locked for processing and its audit records cannot be removed");
	}

	@Test
	public void testLockAndUnlockCollection()
	{
		//given
		final List<ItemModel> users = prepareUsers();

		//when
		itemLockingService.lockAll(users);

		for (final ItemModel itemModel : users)
		{
			//then
			assertThat(itemModel.isSealed()).isTrue();
			assertThat(itemLockingService.isLocked(itemModel)).isTrue();
		}

		//when
		itemLockingService.unlockAll(users);

		for (final ItemModel itemModel : users)
		{
			//then
			assertThat(itemModel.isSealed()).isFalse();
			assertThat(itemLockingService.isLocked(itemModel)).isFalse();
		}
	}

	private List<AuditRecord> getAuditRecordsForUsers(final PK... pks)
	{
		QueryBuilder queryBuilder = AuditSearchQuery.forType("User");
		if (pks != null && pks.length > 0)
		{
			queryBuilder = queryBuilder.withPkSearchRules(pks);
		}

		return readAuditGateway.search(queryBuilder.build()).collect(Collectors.toList());
	}
}
