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
package de.hybris.platform.test;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.testframework.DefaultAnnotationFilter;

import java.lang.annotation.Annotation;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;


@UnitTest
public class AnnotationFilterTest
{
	@Test
	public void testIncludedWithDefaultFalse() throws SecurityException, NoSuchMethodException
	{
		final Filter filter = new DefaultAnnotationFilter("PerformanceTest,UnitTest,DemoTest", "", false);
		final Description performanceMethod = Description.createTestDescription(FirstTest.class, "performanceTest",
				createAnnotation(PerformanceTest.class));
		final Description firstTest = Description.createSuiteDescription(FirstTest.class);
		final Description secondTest = Description.createSuiteDescription(SecondTest.class);
		assertThat(filter.shouldRun(performanceMethod)).isTrue();
		assertThat(filter.shouldRun(firstTest)).isTrue();
		assertThat(filter.shouldRun(secondTest)).isFalse();

	}

	@Test
	public void testIncludedWithDefaultTrue() throws SecurityException, NoSuchMethodException
	{
		final Filter filter = new DefaultAnnotationFilter("PerformanceTest,UnitTest,DemoTest", "", true);
		final Description testMethod = Description.createTestDescription(FirstTest.class, "test");
		assertThat(filter.shouldRun(testMethod)).isTrue();
	}

	@Test
	public void testExludedWithDefaultTrue() throws SecurityException, NoSuchMethodException
	{
		final Filter filter = new DefaultAnnotationFilter("", "PerformanceTest,UnitTest,DemoTest", true);
		assertThat(filter.describe()).isEqualTo(
				"DefaultAnnotationFilter(included=[], " + "excluded=[interface " + PerformanceTest.class.getName() + ", "
						+ "interface " + UnitTest.class.getName() + ", " + "interface " + DemoTest.class.getName()
						+ "], defaultIncluded=true)");

		final Description performanceMethod = Description.createTestDescription(FirstTest.class, "performanceTest",
				createAnnotation(PerformanceTest.class));
		final Description testMethod = Description.createTestDescription(FirstTest.class, "test");
		final Description testClass = Description.createSuiteDescription(FirstTest.class);
		assertThat(filter.shouldRun(performanceMethod)).isFalse();
		assertThat(filter.shouldRun(testMethod)).isTrue();
		assertThat(filter.shouldRun(testClass)).isFalse();
	}

	@Test
	public void testEmtyWithDefaultTrue() throws SecurityException, NoSuchMethodException
	{
		final Filter filter = new DefaultAnnotationFilter("", "", true);
		final Description performanceMethod = Description.createTestDescription(FirstTest.class, "performanceTest",
				createAnnotation(PerformanceTest.class));
		final Description testMethod = Description.createTestDescription(FirstTest.class, "test");
		final Description testClass = Description.createSuiteDescription(FirstTest.class);

		assertThat(filter.shouldRun(performanceMethod)).isTrue();
		assertThat(filter.shouldRun(testMethod)).isTrue();
		assertThat(filter.shouldRun(testClass)).isTrue();

	}

	private Annotation createAnnotation(final Class type)
	{
		return new Annotation()
		{

			@Override
			public Class<? extends Annotation> annotationType()
			{

				return type;
			}
		};
	}

	@DemoTest
	@UnitTest
	class FirstTest
	{
		//
	}

	class SecondTest
	{
		//
	}
}
