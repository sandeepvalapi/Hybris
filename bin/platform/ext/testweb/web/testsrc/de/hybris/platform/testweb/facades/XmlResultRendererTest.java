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
package de.hybris.platform.testweb.facades;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import de.hybris.platform.testframework.SampleTest;
import de.hybris.platform.testframework.model.HybrisTestsResult;
import de.hybris.platform.testframework.model.SingleTestResultData;
import de.hybris.platform.testframework.model.TestSuiteData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.notification.Failure;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class XmlResultRendererTest
{
	private XmlResultRenderer renderer;
	@Mock
	private HybrisTestsResult result;
	@Mock
	private SingleTestResultData st1, st2, st3, st4;
	@Mock
	private TestSuiteData testSuiteData1;
	@Mock
	private Failure failure;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		final Map<Class, TestSuiteData> testSuites = new HashMap<>();
		testSuites.put(SampleTest.class, testSuiteData1);
		given(result.getTestResults()).willReturn(testSuites);
		given(failure.getException()).willReturn(new IllegalArgumentException("some sample message"));

		renderer = new XmlResultRenderer();
	}

	@Test
	public void shouldRenderXmlForNonFailingTests() throws Exception
	{
		// given
		given(Long.valueOf(testSuiteData1.getElapsedTime())).willReturn(Long.valueOf(10));
		given(testSuiteData1.getResults()).willReturn(Arrays.asList(st1, st2, st3, st4));
		given(Long.valueOf(st1.getElapsedTime())).willReturn(Long.valueOf(2));
		given(Long.valueOf(st2.getElapsedTime())).willReturn(Long.valueOf(2));
		given(Long.valueOf(st3.getElapsedTime())).willReturn(Long.valueOf(2));
		given(Long.valueOf(st4.getElapsedTime())).willReturn(Long.valueOf(2));
		given(st1.getTestCaseMethod()).willReturn("st1");
		given(st2.getTestCaseMethod()).willReturn("st2");
		given(st3.getTestCaseMethod()).willReturn("st3");
		given(st4.getTestCaseMethod()).willReturn("st4");

		// when
		final String xml = renderer.render(result);

		// then
		assertThat(xml).isNotNull();
		assertThat(xml).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><testsuites><testsuite name=\"SampleTest\" " +
				  "package=\"de.hybris.platform.testframework\" tests=\"4\" time=\"10\" failures=\"0\" errors=\"0\" ><testcase " +
				  "name=\"st1\" time=\"2\" ></testcase><testcase name=\"st2\" time=\"2\" ></testcase><testcase name=\"st3\" " +
				  "time=\"2\" ></testcase><testcase name=\"st4\" time=\"2\" ></testcase></testsuite></testsuites>");
	}

	@Test
	public void shouldRenderXmlForFailingTests() throws Exception
	{
		// given
		given(Long.valueOf(testSuiteData1.getElapsedTime())).willReturn(Long.valueOf(10));
		given(testSuiteData1.getResults()).willReturn(Arrays.asList(st1, st2, st3, st4));
		given(Integer.valueOf(testSuiteData1.getNumErrors())).willReturn(Integer.valueOf(1));
		given(Integer.valueOf(testSuiteData1.getNumFailures())).willReturn(Integer.valueOf(1));
		given(Long.valueOf(st1.getElapsedTime())).willReturn(Long.valueOf(2));
		given(Long.valueOf(st2.getElapsedTime())).willReturn(Long.valueOf(2));
		given(Long.valueOf(st3.getElapsedTime())).willReturn(Long.valueOf(2));
		given(Long.valueOf(st4.getElapsedTime())).willReturn(Long.valueOf(2));
		given(st1.getTestCaseMethod()).willReturn("st1");
		given(st2.getTestCaseMethod()).willReturn("st2");
		given(st3.getTestCaseMethod()).willReturn("st3");
		given(st4.getTestCaseMethod()).willReturn("st4");
		given(Boolean.valueOf(st1.hasFailure())).willReturn(Boolean.TRUE);
		given(st1.getFailure()).willReturn(failure);

		// when
		final String xml = renderer.render(result);

		// then
		assertThat(xml).isNotNull();
		assertThat(xml).contains("<testsuite name=\"SampleTest\" " +
				  "package=\"de.hybris.platform.testframework\" tests=\"4\" time=\"10\" failures=\"1\" errors=\"1\" ><testcase " +
				  "name=\"st1\" time=\"2\" ><failure message");
	}
}
