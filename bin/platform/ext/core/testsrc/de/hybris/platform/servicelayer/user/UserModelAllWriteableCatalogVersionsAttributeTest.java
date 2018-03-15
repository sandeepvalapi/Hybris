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
package de.hybris.platform.servicelayer.user;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;


@IntegrationTest
public class UserModelAllWriteableCatalogVersionsAttributeTest extends ServicelayerBaseTest
{
	@Resource
	ModelService modelService;

	@Test
	public void shouldReturnEmptyCollectionWhenModelIsNew()
	{
		final UserModel userModel = modelService.create(UserModel.class);

		final Collection<CatalogVersionModel> versions = userModel.getAllWriteableCatalogVersions();

		assertThat(versions).isEmpty();
	}

	@Test
	public void shouldReturnEmptyCollectionWhenModelIsNewAndUserGroupDoesntHaveWritableCatalogVersions()
	{
		final UserModel userModel = modelService.create(UserModel.class);
		final UserGroupModel userGroupModel = modelService.create(UserGroupModel.class);

		userModel.setGroups(ImmutableSet.of(userGroupModel));

		final Collection<CatalogVersionModel> versions = userModel.getAllWriteableCatalogVersions();

		assertThat(versions).isEmpty();
	}

	@Test
	public void shouldReturnEmptyCollectionWhenModelIsNewAndMultipleUserGroupsDontHaveWritableCatalogVersions()
	{
		final UserModel userModel = modelService.create(UserModel.class);
		final UserGroupModel superUserGroupModel = modelService.create(UserGroupModel.class);
		final UserGroupModel userGroupModel = modelService.create(UserGroupModel.class);

		userGroupModel.setGroups(Collections.singleton(superUserGroupModel));
		userModel.setGroups(Collections.singleton(userGroupModel));

		final Collection<CatalogVersionModel> versions = userModel.getAllWriteableCatalogVersions();

		assertThat(versions).isEmpty();
	}

	@Test
	public void shouldReturnWritableCatalogVersionsIfSetForNewModel()
	{
		final UserModel userModel = modelService.create(UserModel.class);
		final CatalogVersionModel cv1 = modelService.create(CatalogVersionModel.class);

		userModel.setWritableCatalogVersions(Collections.singletonList(cv1));

		final Collection<CatalogVersionModel> versions = userModel.getAllWriteableCatalogVersions();

		assertThat(versions).hasSize(1).containsOnly(cv1);
	}

	@Test
	public void shouldReturnVersionsFromUserAndUserGroupIfModelIsNew()
	{
		final UserModel userModel = modelService.create(UserModel.class);
		final UserGroupModel userGroupModel = modelService.create(UserGroupModel.class);
		final CatalogVersionModel cv1 = modelService.create(CatalogVersionModel.class);
		final CatalogVersionModel cv2 = modelService.create(CatalogVersionModel.class);

		userModel.setWritableCatalogVersions(Collections.singletonList(cv1));
		userGroupModel.setWritableCatalogVersions(Collections.singletonList(cv2));
		userModel.setGroups(ImmutableSet.of(userGroupModel));

		final Collection<CatalogVersionModel> versions = userModel.getAllWriteableCatalogVersions();

		assertThat(versions).hasSize(2).containsOnly(cv1, cv2);
	}

	@Test
	public void shouldReturnVersionsFromUserAndMultipleUserGroupsIfModelIsNew()
	{
		final UserModel userModel = modelService.create(UserModel.class);
		final UserGroupModel superUserGroupModel = modelService.create(UserGroupModel.class);
		final UserGroupModel userGroupModel = modelService.create(UserGroupModel.class);
		final CatalogVersionModel cv1 = modelService.create(CatalogVersionModel.class);
		final CatalogVersionModel cv2 = modelService.create(CatalogVersionModel.class);
		final CatalogVersionModel cv3 = modelService.create(CatalogVersionModel.class);

		userModel.setWritableCatalogVersions(Collections.singletonList(cv1));
		userGroupModel.setWritableCatalogVersions(Collections.singletonList(cv2));
		superUserGroupModel.setWritableCatalogVersions(Collections.singletonList(cv3));
		userGroupModel.setGroups(Collections.singleton(superUserGroupModel));
		userModel.setGroups(Collections.singleton(userGroupModel));

		final Collection<CatalogVersionModel> versions = userModel.getAllWriteableCatalogVersions();

		assertThat(versions).hasSize(3).containsOnly(cv1, cv2, cv3);
	}


	@Test
	public void shouldReturnDistinctVersionsFromUserAndUserGroupIfModelIsNew()
	{
		final UserModel userModel = modelService.create(UserModel.class);
		final UserGroupModel userGroupModel = modelService.create(UserGroupModel.class);
		final CatalogVersionModel cv1 = modelService.create(CatalogVersionModel.class);

		userModel.setWritableCatalogVersions(Collections.singletonList(cv1));
		userGroupModel.setWritableCatalogVersions(Collections.singletonList(cv1));
		userModel.setGroups(ImmutableSet.of(userGroupModel));

		final Collection<CatalogVersionModel> versions = userModel.getAllWriteableCatalogVersions();

		assertThat(versions).hasSize(1).containsOnly(cv1);
	}

	@Test
	public void shouldReturnDistinctVersionsFromUserAndMultipleUserGroupsIfModelIsNew()
	{
		final UserModel userModel = modelService.create(UserModel.class);
		final UserGroupModel superUserGroupModel = modelService.create(UserGroupModel.class);
		final UserGroupModel userGroupModel = modelService.create(UserGroupModel.class);
		final CatalogVersionModel cv1 = modelService.create(CatalogVersionModel.class);

		userModel.setWritableCatalogVersions(Collections.singletonList(cv1));
		userGroupModel.setWritableCatalogVersions(Collections.singletonList(cv1));
		superUserGroupModel.setWritableCatalogVersions(Collections.singletonList(cv1));
		userGroupModel.setGroups(Collections.singleton(superUserGroupModel));
		userModel.setGroups(Collections.singleton(userGroupModel));

		final Collection<CatalogVersionModel> versions = userModel.getAllWriteableCatalogVersions();

		assertThat(versions).hasSize(1).containsOnly(cv1);
	}

}
