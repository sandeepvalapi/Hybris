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
package de.hybris.platform.processing.distributed.defaultimpl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHelper.EnvFeature;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.interceptor.impl.InterceptorExecutionPolicy;
import de.hybris.platform.servicelayer.internal.model.extractor.impl.DefaultPersistenceTypeService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.persistence.PersistenceUtils;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests the runInEnvironment(...) method
 */
@IntegrationTest
public class RunInEnvironmentTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private SessionService sessionService;

	private boolean legacyModeBefore = false;

	@Before
	public void setUp()
	{
		legacyModeBefore = DefaultPersistenceTypeService.getLegacyPersistenceGlobalSettingFromConfig();
	}

	@After
	public void tearDown()
	{
		Config.setParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE, String.valueOf(legacyModeBefore));
	}

	@Test
	public void shouldRunWithSldModeWhenDisabledGlobally() throws InterruptedException
	{
		final TestDistributedProcessHandler handler = new TestDistributedProcessHandler(modelService, flexibleSearchService);
		final Function<Supplier<DistributedProcessModel>, DistributedProcessModel> functionWithSLD = DistributedProcessHelper
				.runInEnvironment(handler, EnvFeature.SLD);

		forceLegacyMode();
		assertJaloInUse();

		final DistributedProcessModel process = functionWithSLD.apply(() -> {
			assertSldInUse();
			final UserModel userJan = createUser("jan");
			final DistributedProcessModel result = new DistributedProcessModel();
			result.setCode("Process_" + userJan.getUid());
			return result;
		});

		assertJaloInUse();
		assertThat(process).isNotNull();
		assertThat(process.getCode()).isEqualTo("Process_jan");
		assertThat(userService.isUserExisting("jan")).isTrue();
	}

	@Test
	public void shouldRunWithSldModeWhenEnabledGlobally() throws InterruptedException
	{
		final TestDistributedProcessHandler handler = new TestDistributedProcessHandler(modelService, flexibleSearchService);
		final Function<Supplier<DistributedProcessModel>, DistributedProcessModel> functionWithSLD = DistributedProcessHelper
				.runInEnvironment(handler, EnvFeature.SLD);

		forceSldMode();
		assertSldInUse();

		final DistributedProcessModel process = functionWithSLD.apply(() -> {
			assertSldInUse();
			final UserModel userJan = createUser("jan");
			final DistributedProcessModel result = new DistributedProcessModel();
			result.setCode("Process_" + userJan.getUid());
			return result;
		});

		assertSldInUse();
		assertThat(process).isNotNull();
		assertThat(process.getCode()).isEqualTo("Process_jan");
		assertThat(userService.isUserExisting("jan")).isTrue();
	}

	@Test
	public void shouldRunWithDisabledUniquenessCheck() throws InterruptedException
	{
		final TestDistributedProcessHandler handler = new TestDistributedProcessHandler(modelService, flexibleSearchService);
		final Function<Supplier<DistributedProcessModel>, DistributedProcessModel> functionToTest = DistributedProcessHelper
				.runInEnvironment(handler, EnvFeature.SLD);

		final AtomicBoolean invoked = new AtomicBoolean(false);
		functionToTest.apply(() -> {
			invoked.set(true);
			final Set<String> types = sessionService
					.getAttribute(InterceptorExecutionPolicy.DISABLED_UNIQUE_ATTRIBUTE_VALIDATOR_FOR_ITEM_TYPES);
			assertThat(types).isEqualTo(handler.getTypesWithDisabledUniquenessCheck());
			return null;
		});

		assertThat(invoked.get()).isTrue();
		final Set<String> types = sessionService
				.getAttribute(InterceptorExecutionPolicy.DISABLED_UNIQUE_ATTRIBUTE_VALIDATOR_FOR_ITEM_TYPES);
		assertThat(types).isNull();
	}

	private void assertSldInUse()
	{
		assertThat(PersistenceUtils.isPersistenceLegacyModeEnabled()).isFalse();
	}

	private void assertJaloInUse()
	{
		assertThat(PersistenceUtils.isPersistenceLegacyModeEnabled()).isTrue();
	}

	private UserModel createUser(final String uid)
	{
		final UserModel testUser = modelService.create(UserModel.class);
		testUser.setUid(uid);
		modelService.save(testUser);
		return testUser;
	}

	private void forceLegacyMode()
	{
		Config.setParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE, "true");
	}

	private void forceSldMode()
	{
		Config.setParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE, "false");
	}
}
