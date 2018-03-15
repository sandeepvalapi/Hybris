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
package de.hybris.platform.catalog;


import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.AgreementModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.internal.model.impl.PersistenceTestUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;


@IntegrationTest
public class CatalogVersionSLDTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private SessionService sessionService;

	private final PropertyConfigSwitcher persistenceLegacyModeSwitch = new PropertyConfigSwitcher("persistence.legacy.mode");

	@Before
	public void enableDirectPersistence()
	{
		persistenceLegacyModeSwitch.switchToValue("false");
	}

	@After
	public void resetPersistence()
	{
		persistenceLegacyModeSwitch.switchBackToDefault();
	}

	private static String asUUID()
	{
		return UUID.randomUUID().toString();
	}

	@Test
	@SuppressWarnings("Duplicates")
	public void shouldGetRootCategories()
	{
		final CatalogModel defaultCatalog = modelService.create(CatalogModel.class);
		defaultCatalog.setId(asUUID());
		defaultCatalog.setDefaultCatalog(Boolean.TRUE);

		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setCatalog(defaultCatalog);
		catalogVersion.setVersion(asUUID());
		catalogVersion.setActive(Boolean.TRUE);

		final CategoryModel rootCategory1 = modelService.create(CategoryModel.class);
		rootCategory1.setCode(asUUID());
		rootCategory1.setCatalogVersion(catalogVersion);

		final CategoryModel rootCategory2 = modelService.create(CategoryModel.class);
		rootCategory2.setCode(asUUID());
		rootCategory2.setCatalogVersion(catalogVersion);

		final CategoryModel subCategory1 = modelService.create(CategoryModel.class);
		subCategory1.setCode(asUUID());
		subCategory1.setCatalogVersion(catalogVersion);
		subCategory1.setSupercategories(ImmutableList.of(rootCategory1));

		final CategoryModel subCategory2 = modelService.create(CategoryModel.class);
		subCategory2.setCode(asUUID());
		subCategory2.setCatalogVersion(catalogVersion);
		subCategory2.setSupercategories(ImmutableList.of(rootCategory1));

		final CategoryModel subCategory3 = modelService.create(CategoryModel.class);
		subCategory3.setCode(asUUID());
		subCategory3.setCatalogVersion(catalogVersion);
		subCategory3.setSupercategories(ImmutableList.of(rootCategory2));

		modelService.saveAll();

		final List<CategoryModel> rootCategories = catalogVersion.getRootCategories();
		assertThat(rootCategories).hasSize(2);
	}


	@Test
	public void shouldNotAllowToReassignCatalogVersionToCatalog()
	{
		final CatalogModel defaultCatalog = modelService.create(CatalogModel.class);
		defaultCatalog.setId(asUUID());

		final CatalogModel anotherCatalog = modelService.create(CatalogModel.class);
		anotherCatalog.setId(asUUID());

		modelService.saveAll();

		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setCatalog(defaultCatalog);
		catalogVersion.setVersion(asUUID());
		catalogVersion.setActive(Boolean.TRUE);

		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, catalogVersion);
		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(catalogVersion);

		catalogVersion.setCatalog(anotherCatalog);
		try
		{
			modelService.saveAll();
			fail();
		}
		catch (final Exception ex)
		{
			assertThat(ex).isInstanceOf(ModelSavingException.class);
		}
	}


	@Test(expected = ModelRemovalException.class)
	public void shouldNotAllowToRemoveActive()
	{
		final CatalogModel defaultCatalog = modelService.create(CatalogModel.class);
		defaultCatalog.setId(asUUID());
		modelService.saveAll();

		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setCatalog(defaultCatalog);
		catalogVersion.setVersion(asUUID());
		catalogVersion.setActive(Boolean.TRUE);

		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, catalogVersion);
		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(catalogVersion);

		modelService.remove(catalogVersion);
	}


	@Test
	public void shouldRemoveActiveIfItemCheckIsDisabled()
	{
		sessionService.executeInLocalViewWithParams(ImmutableMap.of(Item.DISABLE_ITEMCHECK_BEFORE_REMOVABLE, Boolean.TRUE),
				new SessionExecutionBody()
				{
					@Override
					public void executeWithoutResult()
					{
						//				super.executeWithoutResult();
						final CatalogModel defaultCatalog = modelService.create(CatalogModel.class);
						defaultCatalog.setId(asUUID());
						modelService.saveAll();

						final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
						catalogVersion.setCatalog(defaultCatalog);
						catalogVersion.setVersion(asUUID());
						catalogVersion.setActive(Boolean.TRUE);

						PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, catalogVersion);
						PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(catalogVersion);

						modelService.remove(catalogVersion);
					}
				});


	}

	@Test
	public void shouldSetAndGetAgreements()
	{
		final CatalogModel defaultCatalog = modelService.create(CatalogModel.class);
		defaultCatalog.setId(asUUID());

		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setCatalog(defaultCatalog);
		catalogVersion.setVersion(asUUID());

		final AgreementModel agreement = modelService.create(AgreementModel.class);
		agreement.setId(asUUID());
		agreement.setEnddate(new Date());

		final AgreementModel otherAgreement = modelService.create(AgreementModel.class);
		otherAgreement.setId(asUUID());
		otherAgreement.setEnddate(new Date());

		catalogVersion.setAgreements(ImmutableList.of(agreement, otherAgreement));

		modelService.saveAll();
		modelService.detach(catalogVersion);

		final CatalogVersionModel readVersion = modelService.get(catalogVersion.getPk());
		final Collection<AgreementModel> agreements = readVersion.getAgreements();

		assertThat(agreements).hasSize(2).containsOnly(agreement, otherAgreement);
	}

	@Test
	public void shouldSetOnlySingleCatalogAsActive()
	{
		final CatalogModel defaultCatalog = modelService.create(CatalogModel.class);
		defaultCatalog.setId(asUUID());
		defaultCatalog.setDefaultCatalog(Boolean.TRUE);

		final CatalogVersionModel version1 = modelService.create(CatalogVersionModel.class);
		version1.setCatalog(defaultCatalog);
		version1.setVersion(asUUID());
		version1.setActive(Boolean.TRUE);

		final CatalogVersionModel version2 = modelService.create(CatalogVersionModel.class);
		version2.setCatalog(defaultCatalog);
		version2.setVersion(asUUID());

		final CatalogVersionModel version3 = modelService.create(CatalogVersionModel.class);
		version3.setCatalog(defaultCatalog);
		version3.setVersion(asUUID());

		modelService.saveAll();

		assertThat(version1.getActive()).isTrue();
		assertThat(version2.getActive()).isFalse();
		assertThat(version3.getActive()).isFalse();

		version3.setActive(Boolean.TRUE);

		modelService.saveAll();

		assertThat(version1.getActive()).isFalse();
		assertThat(version2.getActive()).isFalse();
		assertThat(version3.getActive()).isTrue();

		version1.setActive(Boolean.TRUE);
		version2.setActive(Boolean.TRUE);
		version3.setActive(Boolean.TRUE);

		modelService.saveAll();

		assertSingleVersionActive(version1, version2, version3);
	}

	void assertSingleVersionActive(final CatalogVersionModel... versions)
	{
		int activeCounter = 0;

		for (final CatalogVersionModel version : versions)
		{
			if (BooleanUtils.isTrue(version.getActive()))
			{
				activeCounter++;
			}
		}

		assertThat(activeCounter).isEqualTo(1);
	}


	@Test
	public void shouldAutoAssignReadRightWhenWriteIsSet()
	{
		final CatalogModel defaultCatalog = modelService.create(CatalogModel.class);
		defaultCatalog.setId(asUUID());
		defaultCatalog.setDefaultCatalog(Boolean.TRUE);

		final CatalogVersionModel version1 = modelService.create(CatalogVersionModel.class);
		version1.setCatalog(defaultCatalog);
		version1.setVersion(asUUID());

		final UserModel user = modelService.create(UserModel.class);
		user.setUid(asUUID());

		final UserModel user2 = modelService.create(UserModel.class);
		user2.setUid(asUUID());

		modelService.saveAll();

		version1.setWritePrincipals(ImmutableList.of(user));
		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, version1);
		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(version1);

		assertReadAndWriteUsersForCatalogVersion(version1, user);

		version1.setWritePrincipals(ImmutableList.of(user, user2));
		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, version1);
		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(version1);

		assertReadAndWriteUsersForCatalogVersion(version1, user, user2);
	}

	public void assertReadAndWriteUsersForCatalogVersion(final CatalogVersionModel catalogVersion, final UserModel... users)
	{
		final List<PrincipalModel> readPrincipals = catalogVersion.getReadPrincipals();
		final List<PrincipalModel> writePrincipals = catalogVersion.getWritePrincipals();

		for (final UserModel user : users)
		{
			assertThat(readPrincipals.stream().anyMatch(i -> i.getUid().equals(user.getUid()))).isTrue();
			assertThat(writePrincipals.stream().anyMatch(i -> i.getUid().equals(user.getUid()))).isTrue();
		}
	}

	@Test
	public void shouldRemoveDuplicateLanguages()
	{
		final CatalogModel defaultCatalog = modelService.create(CatalogModel.class);
		defaultCatalog.setId(asUUID());
		defaultCatalog.setDefaultCatalog(Boolean.TRUE);

		final CatalogVersionModel version = modelService.create(CatalogVersionModel.class);
		version.setCatalog(defaultCatalog);
		version.setVersion(asUUID());

		final LanguageModel language = modelService.create(LanguageModel.class);
		language.setIsocode(asUUID());

		final List<LanguageModel> langs = new ArrayList<>();
		langs.add(language);
		langs.add(language);

		version.setLanguages(langs);

		assertThat(version.getLanguages()).hasSize(2);

		modelService.saveAll();
		modelService.refresh(version);

		assertThat(version.getLanguages()).hasSize(1);
	}

	@Test
	public void shouldAddCurrentUserToReadWritePrincipals()
	{
		final UserModel user = modelService.create(UserModel.class);
		final String userUid = asUUID();
		user.setUid(userUid);

		modelService.save(user);

		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				final CatalogModel defaultCatalog = modelService.create(CatalogModel.class);
				defaultCatalog.setId(asUUID());
				defaultCatalog.setDefaultCatalog(Boolean.TRUE);

				final CatalogVersionModel version = modelService.create(CatalogVersionModel.class);
				version.setCatalog(defaultCatalog);
				version.setVersion(asUUID());

				modelService.saveAll(defaultCatalog, version);

				PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, version);
				PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(version);

				final List<PrincipalModel> readPrincipals = version.getReadPrincipals();
				final List<PrincipalModel> writePrincipals = version.getWritePrincipals();

				assertThat(readPrincipals.get(0).getUid()).isEqualTo(userUid);
				assertThat(writePrincipals.get(0).getUid()).isEqualTo(userUid);
			}
		}, user);

	}

	@Test
	public void shouldSaveViaDirectPersistence()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(asUUID());
		catalog.setDefaultCatalog(Boolean.TRUE);

		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, catalog);
		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(catalog);

		final CatalogVersionModel version1 = modelService.create(CatalogVersionModel.class);
		version1.setCatalog(catalog);
		version1.setVersion(asUUID());

		PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, version1);
		PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(version1);
	}

}
