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
package de.hybris.platform.core.initialization.testbeans;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;

import java.util.ArrayList;
import java.util.List;


/**
 * This bean acts like a real system setup bean but is not part of the normal application context
 */

@SystemSetup(extension = SystemSetupParameterTestBean.SYSTEM_SETUP_PARAMETER_TEST_EXTENSION)
public class SystemSetupParameterTestBean
{
	public static final String SYSTEM_SETUP_PARAMETER_TEST_EXTENSION = "SystemSetupParameterTestExtension";

	public static final String TEST_PARAMETER_KEY = "key";
	public static final String TEST_PARAMETER_VALUE1 = "something";
	public static final String TEST_PARAMETER_VALUE2 = "default!!!";
	public static final String TEST_PARAMETER_VALUE3 = "else";


	@SystemSetup(type = Type.ALL, process = Process.ALL)
	public void parameterTest(final SystemSetupContext context) throws Exception
	{
		throw new Exception(context.getParameter(TEST_PARAMETER_KEY));
	}


	@SystemSetupParameterMethod(extension = SystemSetupParameterTestBean.SYSTEM_SETUP_PARAMETER_TEST_EXTENSION)
	public List<SystemSetupParameter> parameterMethod()
	{
		final List<SystemSetupParameter> parameters = new ArrayList<SystemSetupParameter>();

		final SystemSetupParameter parameter = new SystemSetupParameter(TEST_PARAMETER_KEY);
		parameter.addValue(TEST_PARAMETER_VALUE1);
		parameter.addValue(TEST_PARAMETER_VALUE2, true);
		parameter.addValue(TEST_PARAMETER_VALUE3, true);

		parameters.add(parameter);
		return parameters;
	}
}
