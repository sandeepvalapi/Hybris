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
package de.hybris.platform.processengine.definition;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.processengine.model.DynamicProcessDefinitionModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.utils.NeedsTaskEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;


@IntegrationTest
@NeedsTaskEngine
public class ScriptedProcessDefinitionTest extends ServicelayerBaseTest
{
	private static final String TEST_DEFINITION_CODE = "testProcessDefinition";
	private static final String TEST_PROCESS_CODE = "testProcessCode";

	@Resource
	private ModelService modelService;
	@Resource
	private BusinessProcessService businessProcessService;
	@Resource
	private ProcessDefinitionsCache processDefinitionsCache;

	@After
	public void tearDown()
	{
		processDefinitionsCache.clear();
	}


	// see HORST-1913
	@Test
	public void shouldSurviveDeadlocks() throws InterruptedException, TimeoutException
	{
		givenProcessDefinition(TEST_DEFINITION_CODE, //
				"<?xml version='1.0' encoding='utf-8'?>", //
				"<process xmlns='http://www.hybris.de/xsd/processdefinition' start='action0' name='testProcessDefinition'>", //
				"	<scriptAction id='action0'>", //
				"		<script type='groovy'>", //
				"			businessProcessService.triggerEvent('foo')", //
				"			return 'itworks'", //
				"    </script>", //
				"		<transition name='itworks' to='success'/>", //
				"	</scriptAction>", //
				"	<end id='success' state='SUCCEEDED'>Everything was fine</end>", //
				"</process>");

		final List<BusinessProcessModel> processes = new ArrayList<>();
		for (int i = 0; i < 20; i++)
		{
			processes.add(businessProcessService.createProcess("TriggerFoo-" + i, TEST_DEFINITION_CODE));
		}

		for (final BusinessProcessModel p : processes)
		{
			businessProcessService.startProcess(p);
		}

		for (final BusinessProcessModel p : processes)
		{
			waitFor(p);
		}

		for (final BusinessProcessModel p : processes)
		{
			assertThat(p.getState()).isEqualTo(ProcessState.SUCCEEDED);
		}
	}

	@Test
	public void shouldBeAbleToRunBusinessProcessWithEmbeddedScript() throws InterruptedException, TimeoutException
	{
		givenProcessDefinition(TEST_DEFINITION_CODE, //
				"<?xml version='1.0' encoding='utf-8'?>", //
				"<process xmlns='http://www.hybris.de/xsd/processdefinition' start='action0' name='testProcessDefinition'>", //
				"	<scriptAction id='action0'>", //
				"		<script type='javascript'>(function() { return 'itworks' })()</script>", //
				"		<transition name='itworks' to='success'/>", //
				"	</scriptAction>", //
				"	<end id='success' state='SUCCEEDED'>Everything was fine</end>", //
				"</process>");

		final BusinessProcessModel process = businessProcessService.startProcess(TEST_PROCESS_CODE, TEST_DEFINITION_CODE);

		waitFor(process);
		assertThat(process.getState()).isEqualTo(ProcessState.SUCCEEDED);
	}

	@Test
	public void shouldBeAbleToSetContextParameterFromScript() throws InterruptedException, TimeoutException
	{
		givenProcessDefinition(TEST_DEFINITION_CODE, //
				"<?xml version='1.0' encoding='utf-8'?>", //
				"<process xmlns='http://www.hybris.de/xsd/processdefinition' start='action0' name='testProcessDefinition'>", //
				"<contextParameter name='testParameter' use='required' type='java.lang.String'/>", //
				"	<scriptAction id='action0'>", //
				"		<script type='javascript'>", //
				"			var parameter = process.contextParameters.get(0);", //
				"			parameter.setValue('changedFromScript');", //
				"			modelService.save(parameter);", //
				"			'itworks'", //
				"		</script>", //
				"		<transition name='itworks' to='success'/>", //
				"	</scriptAction>", //
				"	<end id='success' state='SUCCEEDED'>Everything was fine</end>", //
				"</process>");

		final BusinessProcessModel process = businessProcessService.startProcess(TEST_PROCESS_CODE, TEST_DEFINITION_CODE,
				ImmutableMap.<String, Object> of("testParameter", "initialValue"));

		waitFor(process);
		assertThat(process.getState()).isEqualTo(ProcessState.SUCCEEDED);
		assertThat(process.getContextParameters()).hasSize(1);
		final BusinessProcessParameterModel testParameter = process.getContextParameters().iterator().next();
		assertThat(testParameter).isNotNull();
		assertThat(testParameter.getName()).isNotNull().isEqualTo("testParameter");
		assertThat(testParameter.getValue()).isNotNull().isEqualTo("changedFromScript");
	}

	@Test
	public void processShouldPickUpLatestDefinition() throws InterruptedException, TimeoutException
	{
		final String definitionTemplate = "<?xml version='1.0' encoding='utf-8'?>" + //
				"<process xmlns='http://www.hybris.de/xsd/processdefinition' start='waitSomeTime' name='testProcessDefinition'>" + //
				"<contextParameter name='testParameter' use='required' type='java.lang.String'/>" + //
				"	<scriptAction id='waitSomeTime'>" + //
				"		<script type='javascript'>" + //
				"			java.lang.Thread.sleep(2000);" + //
				"			'OK'" + //
				"		</script>" + //
				"		<transition name='OK' to='updateParameter'/>" + //
				"	</scriptAction>" + //
				"	<scriptAction id='updateParameter'>" + //
				"		<script type='javascript'>" + //
				"			var parameter = process.contextParameters.get(0);" + //
				"			parameter.setValue('changedFromScriptVersion%d');" + //
				"			modelService.save(parameter);" + //
				"			'OK'" + //
				"		</script>" + //
				"		<transition name='OK' to='success'/>" + //
				"	</scriptAction>" + //
				"	<end id='success' state='SUCCEEDED'>Everything was fine</end>" + //
				"</process>";

		final int numberOfProcesses = 100;
		final DynamicProcessDefinitionModel processDefinition = givenProcessDefinition(TEST_DEFINITION_CODE,
				String.format(definitionTemplate, Integer.valueOf(0)));


		for (int i = 0; i < numberOfProcesses; i++)
		{
			processDefinition.setContent(String.format(definitionTemplate, Integer.valueOf(i)));
			modelService.save(processDefinition);
			businessProcessService.startProcess(TEST_PROCESS_CODE + i, TEST_DEFINITION_CODE,
					ImmutableMap.<String, Object> of("testParameter", "initialValue"));
		}

		for (int i = 0; i < numberOfProcesses; i++)
		{
			final BusinessProcessModel process = businessProcessService.getProcess(TEST_PROCESS_CODE + i);
			waitFor(process);
			assertThat(process).isNotNull();
			assertThat(process.getState()).isEqualTo(ProcessState.SUCCEEDED);
			assertThat(process.getContextParameters()).hasSize(1);
			final BusinessProcessParameterModel testParameter = process.getContextParameters().iterator().next();
			assertThat(testParameter).isNotNull();
			assertThat(testParameter.getName()).isNotNull().isEqualTo("testParameter");
			assertThat(testParameter.getValue()).isNotNull()
					.isEqualTo(String.format("changedFromScriptVersion%s", Integer.valueOf(i)));
		}
	}

	private DynamicProcessDefinitionModel givenProcessDefinition(final String definitionCode, final String... lines)
	{
		final DynamicProcessDefinitionModel result = modelService.create(DynamicProcessDefinitionModel.class);
		result.setContent(Joiner.on("\n").join(lines));
		result.setCode(definitionCode);
		modelService.save(result);
		return result;
	}

	private void waitFor(final BusinessProcessModel model) throws InterruptedException, TimeoutException
	{
		final long maxWaitTime = System.currentTimeMillis() + 2 * 60 * 1000;
		while (model.getState() == ProcessState.RUNNING)
		{
			if (System.currentTimeMillis() > maxWaitTime)
			{
				throw new TimeoutException("Wait time exceeded for " + model.getCode());
			}
			modelService.refresh(model);
		}
		assertThat(model.getState()).isEqualTo(ProcessState.SUCCEEDED);
	}
}
