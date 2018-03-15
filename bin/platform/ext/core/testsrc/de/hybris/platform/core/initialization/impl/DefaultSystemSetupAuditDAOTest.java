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
package de.hybris.platform.core.initialization.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.initialization.SystemSetupAuditDAO;
import de.hybris.platform.core.initialization.SystemSetupCollectorResult;
import de.hybris.platform.core.initialization.testbeans.TestPatchBean;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;

import java.lang.reflect.Method;
import java.util.Arrays;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.util.ReflectionUtils;


@IntegrationTest
public class DefaultSystemSetupAuditDAOTest extends ServicelayerBaseTest
{
	@Resource(name = "systemSetupAuditDAO")
	private SystemSetupAuditDAO dao;

	@Test
	public void shouldReturnTrueForAlreadyAppliedPatch() throws Exception
	{
		// given
		final SystemSetupCollectorResult collectorResult = prepareSystemSetupCollectorResult();
		dao.storeSystemPatchAction(collectorResult);

		// when
		final boolean applied = dao.isPatchApplied(collectorResult.getHash());

		// then
		assertThat(applied).isTrue();
	}

	private SystemSetupCollectorResult prepareSystemSetupCollectorResult()
	{
		final TestPatchBean testPatchBean = new TestPatchBean();
		final Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(TestPatchBean.class);
		final Method method = Arrays.stream(methods).filter(m -> "requiredPatch".equals(m.getName())).findFirst().get();
        return new SystemSetupCollectorResult(null, null, testPatchBean, "testExtension", "testBeanID", method, "testPatch", "test description", true, true);
	}
}
