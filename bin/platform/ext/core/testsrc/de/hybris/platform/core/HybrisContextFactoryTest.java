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
package de.hybris.platform.core;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.HybrisContextFactory.ApplicationContextFactory;
import de.hybris.platform.core.HybrisContextFactory.GlobalContextFactory;
import de.hybris.platform.spring.ctx.CloseAwareApplicationContext;
import de.hybris.platform.spring.ctx.ScopeTenantIgnoreDocReader;
import de.hybris.platform.spring.ctx.TestBean;
import de.hybris.platform.spring.ctx.TestSubBean;
import de.hybris.platform.spring.ctx.annot.TestComponent;
import de.hybris.platform.util.CoreUtilities;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test/dummy-test-spring.xml")
//just for sake to run an autowiring
public class HybrisContextFactoryTest
{
	private static final String FOO_CTX_ID = "foo_id";
	private static final String EXT_ONE = "one";
	private static final String EXT_TWO = "two";

	private static final String GLOBAL_BEAN = "globalBean";
	private static final String BEAN_ONE = "beanOne";
	private static final String BEAN_TWO = "beanTwo";
	private static final String COMPONENT_SCAN_BEAN = "testComponent";
	private static final String PROFILE_ONE = "test";
	private static final String PROFILE_TWO = "develop";
	private static final String GLOBALLY_CONFIGURED_PROFILES = PROFILE_ONE + ',' + PROFILE_TWO;

	@Value("classpath:test/global-test-spring.xml")
	private org.springframework.core.io.Resource globalContextResource;

	@Value("classpath:test/app-one-test-spring.xml")
	private org.springframework.core.io.Resource appOneContextResource;

	@Value("classpath:test/app-two-test-spring.xml")
	private org.springframework.core.io.Resource appTwoContextResource;

	@Value("classpath:test/app-annot-test-spring.xml")
	private org.springframework.core.io.Resource appAnnotContextResource;

	@Mock
	private Tenant tenantMock;

	@Mock
	private CoreUtilities coreUtilities;

	@Before
	public void inject()
	{
		MockitoAnnotations.initMocks(this);

		Mockito.doReturn(FOO_CTX_ID).when(tenantMock).getTenantID();

		given(coreUtilities.getConfigProperty("one.global-context", "/one-global-context.xml")).willReturn(
				"/one-global-context.xml");
		given(coreUtilities.getConfigProperty("one.application-context", "/one-application-context.xml")).willReturn(
				"/one-application-context.xml");
		given(coreUtilities.getConfigProperty("two.application-context", "/two-application-context.xml")).willReturn(
				"/two-application-context.xml");
		given(coreUtilities.getConfigProperty("spring.profiles.active")).willReturn(GLOBALLY_CONFIGURED_PROFILES);
	}

	@Test
	public void applicationContextShouldHaveOnlyProfileNamedFromTenantIDIfThereAreNotProfilesConfiguredGlobally() throws Exception
	{
		// given
		given(coreUtilities.getConfigProperty("spring.profiles.active")).willReturn(null);
		final GenericApplicationContext superContext = new TestGlobalContextFactory().build();
		final GenericApplicationContext ctx = new TestApplicationContextFactory(superContext).build();

		// when
		final String[] profilesFromSuperCtx = superContext.getEnvironment().getActiveProfiles();
		final String[] profilesFromAppCtx = ctx.getEnvironment().getActiveProfiles();

		// then
		assertThat(profilesFromSuperCtx).isEmpty();
		assertThat(profilesFromAppCtx).containsOnly("tenant_" + FOO_CTX_ID);
	}

	@Test
	public void applicationContextShouldHaveAnyConfiguredGloballyProfilePlusProfileNamedFromTenantID() throws Exception
	{
		// given
		final GenericApplicationContext superContext = new TestGlobalContextFactory().build();
		final GenericApplicationContext ctx = new TestApplicationContextFactory(superContext).build();

		// when
		final String[] profilesFromSuperCtx = superContext.getEnvironment().getActiveProfiles();
		final String[] profilesFromAppCtx = ctx.getEnvironment().getActiveProfiles();

		// then
		assertThat(profilesFromSuperCtx).containsOnly(PROFILE_ONE, PROFILE_TWO);
		assertThat(profilesFromAppCtx).containsOnly("tenant_" + FOO_CTX_ID, PROFILE_ONE, PROFILE_TWO);
	}

	@Test
	public void applicationContextShouldReturnBeanConfiguredInTenantAwareProfile() throws Exception
	{
		// given
		final GenericApplicationContext superContext = new TestGlobalContextFactory().build();
		final GenericApplicationContext ctx = new TestApplicationContextFactory(superContext).build();

		// when
		final TestSubBean bean = (TestSubBean) ctx.getBean("subBeanOneProfiled");

		// then
		assertThat(bean).isNotNull();
		assertThat(bean.getSimpleProperty()).isEqualTo("valueTENANT_FOO_ID");
	}

	@Test
	public void globalApplicationContextShouldReturnBeanConfiguredInPredefinedInPropertiesProfile() throws Exception
	{
		// given
		final GenericApplicationContext superContext = new TestGlobalContextFactory().build();

		// when
		final TestSubBean bean = (TestSubBean) superContext.getBean("subBeanOneGlobal");

		// then
		assertThat(bean).isNotNull();
		assertThat(bean.getSimpleProperty()).isEqualTo("valueTEST");
	}

	@Test
	public void testHasAGlobalBean() throws Exception
	{
		final GenericApplicationContext mooContext = new TestGlobalContextFactory().build();

		assertThat(mooContext).isNotNull();
		assertThat(mooContext.getBean(GLOBAL_BEAN)).isNotNull().isInstanceOf(TestBean.class);
	}


	@Test
	public void testApplicationContextHasGlobalContextAsAParent() throws Exception
	{
		final CloseAwareApplicationContext superContext = Mockito.mock(CloseAwareApplicationContext.class);


		final GenericApplicationContext mooContext = new TestApplicationContextFactory(superContext)
		{
			@Override
			protected void refreshContext(final GenericApplicationContext ctx)
			{
				//nop
			}


		}.build();

		assertThat(mooContext.getParent()).isEqualTo(superContext);
	}

	@Test
	public void testHasALocallBean() throws Exception
	{
		final GenericApplicationContext mooContext = new TestApplicationContextFactory(new TestGlobalContextFactory().build())
				.build();

		assertThat(mooContext.getBean(BEAN_ONE)).isNotNull().isInstanceOf(TestBean.class);
		assertThat(mooContext.getBean(BEAN_TWO)).isNotNull().isInstanceOf(TestBean.class);

		final TestBean beanOne = mooContext.getBean(BEAN_ONE, TestBean.class);
		assertThat(beanOne.getMapping()).hasSize(4);
		assertThat(beanOne.getMapping().keySet()).contains("subBeanOne", "subBeanTwo");

		final TestBean beanTwo = mooContext.getBean(BEAN_TWO, TestBean.class);
		assertThat(beanTwo.getMapping()).hasSize(2);
		assertThat(beanTwo.getMapping().keySet()).contains("keyOne", "keyTwo");
	}

	@Test
	public void testAssureHasAGlobalBean() throws Exception
	{
		final GenericApplicationContext mooContext = new TestGlobalContextFactory().build();

		assertThat(mooContext).isNotNull();
		assertThat(mooContext.getBean(GLOBAL_BEAN)).isNotNull().isInstanceOf(TestBean.class);
	}

	@Test
	public void testAssureComponentScanIgnoresTenantAnnotation() throws Exception
	{
		final GenericApplicationContext annotContext = new TestApplicationContextFactory(new TestGlobalContextFactory().build())
		{
			@Override
			protected Resource getResource(final String extension, final String location)
			{
				return appAnnotContextResource;
			}
		}.build();

		assertThat(annotContext.getBean(COMPONENT_SCAN_BEAN)).isNotNull().isInstanceOf(TestComponent.class);

		final TestComponent component = annotContext.getBean(COMPONENT_SCAN_BEAN, TestComponent.class);
		assertThat(component.getTestBean()).isNotNull();
	}


	class TestGlobalContextFactory extends GlobalContextFactory
	{

		@Override
		protected Collection<String> getPlatformExtensions()
		{
			return Arrays.asList(EXT_ONE);
		}

		@Override
		protected Resource getResource(final String extension, final String location)
		{
			return globalContextResource;
		}

		@Override
		CoreUtilities getCoreUtils()
		{
			return coreUtilities;
		}

	}


	class TestApplicationContextFactory extends ApplicationContextFactory
	{

		TestApplicationContextFactory(final GenericApplicationContext globalContext)
		{
			super(FOO_CTX_ID, null, globalContext);
		}


		@Override
		protected Collection<String> getPlatformExtensions()
		{
			return Arrays.asList(EXT_ONE, EXT_TWO);
		}

		@Override
		protected Resource getResource(final String extension, final String location)
		{
			if (EXT_ONE.equalsIgnoreCase(extension))
			{
				return appOneContextResource;
			}
			else if (EXT_TWO.equalsIgnoreCase(extension))
			{
				return appTwoContextResource;
			}
			return super.getResource(extension, location);
		}

		@Override
		protected Class<? extends ScopeTenantIgnoreDocReader> getDocumentReaderClass()
		{
			return ScopeTenantIgnoreDocReader.class;
		}

		@Override
		CoreUtilities getCoreUtils()
		{
			return coreUtilities;
		}

	}
}
