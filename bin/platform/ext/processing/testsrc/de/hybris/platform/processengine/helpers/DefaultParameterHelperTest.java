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
package de.hybris.platform.processengine.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.processengine.definition.ProcessDefinitionFactory;
import de.hybris.platform.processengine.helpers.impl.DefaultProcessFactory;
import de.hybris.platform.processengine.helpers.impl.DefaultProcessParameterHelper;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


@IntegrationTest
public class DefaultParameterHelperTest
{
	private String process;
	private String processDefinition;
	private String param1Name;
	private String param1Value;

	private String param2Name;
	private String param2Value;

	private String param3Name;
	private String param3Value;

	private String paramNullName;

	private DefaultProcessParameterHelper test;
	private DefaultProcessFactory factory;
	private BusinessProcessModel testmodel;
	private Map<String, Object> param;

	@Before
	public void setUp()
	{
		final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"/processengine/test/processDefinitionFactory-test-spring.xml", getClass());

		test = new DefaultProcessParameterHelper();
		factory = new DefaultProcessFactory();
		factory.setProcessDefinitionFactory(applicationContext.getBean("processDefinitionFactory", ProcessDefinitionFactory.class));

		param = new HashMap<String, Object>();

		process = "process1";
		processDefinition = "process1";

		param1Name = "name1";
		param1Value = "value1";
		param.put(param1Name, param1Value);

		param2Name = "name2";
		param2Value = "value2";
		param.put(param2Name, param2Value);

		param3Name = "name3";
		param3Value = "value3";
		param.put(param3Name, param3Value);

		paramNullName = "nullName";

		test.setModelService(EasyMock.createMock(ModelService.class));
	}

	@Test
	public void getProcessParameterByNameTest()
	{
		BusinessProcessParameterModel param1Model;
		BusinessProcessParameterModel param2Model;
		BusinessProcessParameterModel param3Model;

		testmodel = factory.createProcessModel(process, processDefinition, param);
		assertNotNull("testmodel is null", testmodel);

		param1Model = test.getProcessParameterByName(param1Name, testmodel.getContextParameters());
		assertNotNull("param1Model is null", param1Model);
		assertEquals("param1Model name is incorrect", param1Model.getName(), param1Name);
		assertEquals("param1Model.getValue is incorrect", param1Model.getValue(), param1Value);

		param2Model = test.getProcessParameterByName(param2Name, testmodel.getContextParameters());
		assertNotNull("param2Model", param2Model);
		assertEquals("param2Model.getName", param2Model.getName(), param2Name);
		assertEquals("param2Model.getValue", param2Model.getValue(), param2Value);

		param3Model = test.getProcessParameterByName(param3Name, testmodel.getContextParameters());
		assertNotNull("param3Model", param3Model);
		assertEquals("param3Model.getName", param3Model.getName(), param3Name);
		assertEquals("param3Model.getValue", param3Model.getValue(), param3Value);

		assertNull("test.getProcessParameterByName(paramNullName,..",
				test.getProcessParameterByName(paramNullName, testmodel.getContextParameters()));
	}

	@Test
	public void containsProcessParameterByNameTest()
	{
		BusinessProcessParameterModel param1Model;

		testmodel = factory.createProcessModel(process, processDefinition, param);
		assertNotNull("testmodel is null", testmodel);

		param1Model = test.getProcessParameterByName(param1Name, testmodel.getContextParameters());
		assertNotNull("param1Model is null", param1Model);
		assertEquals("param1Model name is incorrect", param1Model.getName(), param1Name);
		assertEquals("param1Model.getValue is incorrect", param1Model.getValue(), param1Value);
		assertTrue("param1Name should exist", test.containsParameter(testmodel, param1Name));

		assertFalse("param100Name should not exist", test.containsParameter(testmodel, "param100Name"));
	}

	@Test
	public void setProcessParameterValeTest()
	{
		BusinessProcessParameterModel param1Model;

		testmodel = factory.createProcessModel(process, processDefinition, param);
		assertNotNull("testmodel is null", testmodel);

		param1Model = test.getProcessParameterByName(param1Name, testmodel.getContextParameters());
		assertNotNull("param1Model is null", param1Model);
		assertEquals("param1Model name is incorrect", param1Model.getName(), param1Name);
		assertEquals("param1Model.getValue is incorrect", param1Model.getValue(), param1Value);

		test.setProcessParameter(testmodel, param1Name, param2Value);
		param1Model = test.getProcessParameterByName(param1Name, testmodel.getContextParameters());
		assertEquals("param1Model.getValue is incorrect", param1Model.getValue(), param2Value);
	}

	@Test
	public void getAllParameterNamesTest()
	{
		Set<String> params;

		testmodel = factory.createProcessModel(process, processDefinition, param);
		assertNotNull("testmodel", testmodel);

		params = test.getAllParameterNames(testmodel);
		assertNotNull("params", params);

		assertTrue("param1Name", params.contains(param1Name));
		assertTrue("param2Name", params.contains(param2Name));
		assertTrue("param3Name", params.contains(param3Name));

		assertFalse("paramNullName", params.contains(paramNullName));
	}
}
