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


/**
 * This bean acts like a real system setup bean but is not part of the normal application context
 */

@SystemSetup(extension = SystemSetupTypeProcessTestBean.SYSTEM_SETUP_TYPE_PROCESS_TEST_EXTENSION)
public class SystemSetupTypeProcessTestBean
{
	public static final String SYSTEM_SETUP_TYPE_PROCESS_TEST_EXTENSION = "SystemSetupTypeBeanTestExtension";

	public static final String ESSENTIAL_INIT = "essentialinit";
	public static final String ESSENTIAL_UPDATE = "essentialupdate";
	public static final String PROJECT_INIT = "projectinit";
	public static final String PROJECT_UPDATE = "projectupdate";

	@SystemSetup(type = Type.ESSENTIAL, process = Process.INIT)
	public void essentialInit() throws Exception //NOPMD
	{
		throw new Exception(ESSENTIAL_INIT); //NOPMD
	}

	@SystemSetup(type = Type.ESSENTIAL, process = Process.UPDATE)
	public void essentialUpdate() throws Exception //NOPMD
	{
		throw new Exception(ESSENTIAL_UPDATE); //NOPMD
	}

	@SystemSetup(type = Type.PROJECT, process = Process.INIT)
	public void projectInit() throws Exception //NOPMD
	{
		throw new Exception(PROJECT_INIT); //NOPMD
	}

	@SystemSetup(type = Type.PROJECT, process = Process.UPDATE)
	public void projectUpdate() throws Exception //NOPMD
	{
		throw new Exception(PROJECT_UPDATE); //NOPMD
	}
}
