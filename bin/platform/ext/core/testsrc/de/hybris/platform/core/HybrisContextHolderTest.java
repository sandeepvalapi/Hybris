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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.AbstractTenant.ShutDownMode;
import de.hybris.platform.core.HybrisContextFactory.ApplicationContextFactory;
import de.hybris.platform.core.HybrisContextFactory.GlobalContextFactory;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.spring.ctx.ScopeTenantIgnoreDocReader;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.util.config.ConfigIntf;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 *
 */
@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test/dummy-test-spring.xml")
public class HybrisContextHolderTest
{

	private static final String EXPECTED_MESSAGE = "some expected message here";

	private static final String FOO_CTX_ID = "foo id";
	private static final String EXT_ONE = "one";
	private static final String EXT_TWO = "two";

	private static final String GLOBAL_BEAN = "globalBean";
	private static final String BEAN_ONE = "beanOne";
	private static final String BEAN_TWO = "beanTwo";

	@Value("classpath:test/global-test-spring.xml")
	private org.springframework.core.io.Resource globalContextResource;

	@Value("classpath:test/app-one-test-spring.xml")
	private org.springframework.core.io.Resource appOneContextResource;

	@Value("classpath:test/app-two-test-spring.xml")
	private org.springframework.core.io.Resource appTwoContextResource;

	private HybrisContextHolder holder;


	private final Tenant tenantMock = Mockito.mock(Tenant.class);

	public GenericApplicationContext ctxGlobalMock = Mockito.spy(new GenericApplicationContext());

	public GenericApplicationContext ctxAppMock = Mockito.spy(new GenericApplicationContext());




	@Before
	public void inject()
	{
		Mockito.doReturn("ctxGlobalMock").when(ctxGlobalMock).toString();
		Mockito.doReturn("ctxAppMock").when(ctxAppMock).toString();


		//global tenant mock
		Mockito.doReturn(FOO_CTX_ID).when(tenantMock).getTenantID();
	}

	@After
	public void cleanUp()
	{
		TestApplicationContextFactory.testFailure = null;
		TestGlobalContextFactory.testFailure = null;
		if (holder != null)
		{
			holder.destroy();
		}
	}

	@Test
	public void testAssureCloseCalledWhileContextsDestroyed() throws Exception
	{




		final TestGlobalContextFactory globalCtxFactory = new TestGlobalContextFactory(globalContextResource)
		{
			@Override
			protected GenericApplicationContext createNewContext()
			{
				return ctxGlobalMock;
			}

			@Override
			protected Resource getResource(final String extension, final String location)
			{
				return globalContextResource;
			}
		};


		holder = new TestHybrisContextHolder()
		{
			@Override
			HybrisContextFactory getGlobalCtxFactory()
			{
				return globalCtxFactory;
			}

			@Override
			HybrisContextFactory getAppCtxFactory(final Tenant tenant) throws BeansException
			{
				return new TestApplicationContextFactory(ctxGlobalMock, appOneContextResource, appTwoContextResource)
				{
					@Override
					protected GenericApplicationContext createNewContext()
					{

						return ctxAppMock;
					}
				};
			}
		};

		final AbstractTenant tenant = new TenantStub()
		{

			@Override
			HybrisContextHolder getContextHolder()
			{
				return holder;
			}

			@Override
			GenericApplicationContext getApplicationContext()
			{
				return ctxAppMock;
			}

		};

		Assert.assertNotNull(holder.getGlobalInstance());
		Assert.assertNotNull(holder.getApplicationInstance(tenantMock));

		holder.destroy();

		Mockito.verify(ctxGlobalMock, Mockito.times(1)).close();

		tenant.shutDownApplicationContext(ShutDownMode.INITIALIZATION);
		Mockito.verify(ctxAppMock, Mockito.times(0)).close();

		tenant.shutDownApplicationContext(ShutDownMode.SYSTEM_SHUTDOWN);
		Mockito.verify(ctxAppMock, Mockito.times(1)).close();

	}

	@Test
	public void testAssureGlobalContextInvalidatedWhenAfterDestroyed() throws Exception
	{
		holder = new TestHybrisContextHolder()
		{
			@Override
			HybrisContextFactory getGlobalCtxFactory()
			{
				return new TestGlobalContextFactory(globalContextResource)
				{
					@Override
					protected GenericApplicationContext createNewContext()
					{

						return new GenericApplicationContext();
					}
				};
			}
		};


		GenericApplicationContext globalBefore = null;

		Assert.assertNotNull(globalBefore = holder.getGlobalInstance());


		//every call returns the same 
		Assert.assertSame(globalBefore, holder.getGlobalInstance());


		Assert.assertSame(globalBefore, holder.getGlobalInstance());


		holder.destroy();//


		Assert.assertNotSame(globalBefore, holder.getGlobalInstance());

	}

	@Test
	public void testAssureAppContextForTheSameTenantsThrowsException() throws Exception
	{
		holder = new TestHybrisContextHolder()
		{

			@Override
			protected GenericApplicationContext getGlobalInstanceCached() throws FatalBeanException
			{
				return ctxGlobalMock;
			}


			@Override
			HybrisContextFactory getAppCtxFactory(final Tenant tenant) throws BeansException
			{
				return new TestApplicationContextFactory(ctxGlobalMock, appOneContextResource, appTwoContextResource)
				{

					@Override
					protected void refreshContext(final GenericApplicationContext ctx)
					{
						//nop
					}
				};
			}
		};

		ApplicationContext appBefore = null;


		Assert.assertNotNull(appBefore = holder.getApplicationInstance(tenantMock));
		try
		{
			holder.getApplicationInstance(tenantMock);
			Assert.fail("Holder should not allow to call more than once for the same tenant");
		}
		catch (final ApplicationContextException e)
		{
			Assert.assertTrue(e.getCause() instanceof IllegalArgumentException);
			Assert.assertEquals(e.getCause().getMessage(), "There is already registered context for tenant " + tenantMock);

		}


		try
		{
			holder.getApplicationInstance(tenantMock);
			Assert.fail("Holder should not allow to call more than once for the same tenant");
		}
		catch (final ApplicationContextException e)
		{
			Assert.assertTrue(e.getCause() instanceof IllegalArgumentException);
			Assert.assertEquals(e.getCause().getMessage(), "There is already registered context for tenant " + tenantMock);

		}

		holder.destroy();//

		Assert.assertNotSame(appBefore, holder.getApplicationInstance(tenantMock));

	}

	@Test
	public void testAssureApplicationContextDestroyedWhenErrorOccursWhenCreatingGlobalContext() throws Exception
	{
		//
		final RuntimeException fatalExceptionWhileStartingGlobalContext = new FatalBeanException(EXPECTED_MESSAGE);
		//
		Mockito.doThrow(fatalExceptionWhileStartingGlobalContext).when(ctxGlobalMock).refresh();


		holder = new TestHybrisContextHolder()
		{
			@Override
			HybrisContextFactory getGlobalCtxFactory()
			{
				return new TestGlobalContextFactory(globalContextResource)
				{
					@Override
					protected GenericApplicationContext createNewContext()
					{
						return ctxGlobalMock;
					}
				};
			}
		};


		try
		{
			TestUtils.disableFileAnalyzer("Expect to have here an error message");
			holder.getGlobalInstance();
			Assert.fail("Should not be able to create instance ");
		}
		catch (final RuntimeException e)
		{
			Assert.assertEquals(EXPECTED_MESSAGE, e.getMessage());
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}

		try
		{
			TestUtils.disableFileAnalyzer("Expect to have here an error message");
			holder.getApplicationInstance(tenantMock);
			Assert.fail("Should not be able to create instance ");
		}
		catch (final RuntimeException e)
		{
			Assert.assertEquals(FatalBeanException.class, e.getClass());
			Assert.assertTrue(e.getMessage().endsWith(EXPECTED_MESSAGE));
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}

		Mockito.verify(ctxGlobalMock, Mockito.times(1)).destroy();

		Mockito.verifyZeroInteractions(ctxAppMock);
	}


	@Test
	public void testAssureGlobalContextDestroyedWhenErrorOccursWhenCreatingGlobalContext() throws Exception
	{
		final RuntimeException fatalExceptionWhileStartingGlobalContext = new FatalBeanException(EXPECTED_MESSAGE);
		//
		Mockito.doThrow(fatalExceptionWhileStartingGlobalContext).when(ctxGlobalMock).refresh();


		holder = new TestHybrisContextHolder()
		{
			@Override
			HybrisContextFactory getGlobalCtxFactory()
			{
				return new TestGlobalContextFactory(globalContextResource)
				{
					@Override
					protected GenericApplicationContext createNewContext()
					{
						return ctxGlobalMock;
					}
				};
			}

		};


		try
		{
			TestUtils.disableFileAnalyzer("Expect to have here an error message");
			holder.getGlobalInstance();
			Assert.fail("Should not be able to create instance ");
		}
		catch (final RuntimeException e)
		{
			Assert.assertEquals(EXPECTED_MESSAGE, e.getMessage());
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}

		Mockito.verify(ctxGlobalMock, Mockito.times(1)).destroy();

		Mockito.verifyZeroInteractions(ctxAppMock);
	}

	@Test
	public void testAssureGlobalContextDestroyedWhenErrorOccursWhenCreatingApplicationContext() throws Exception
	{
		//
		final RuntimeException fatalExceptionWhileStartingAppContext = new FatalBeanException(EXPECTED_MESSAGE);
		//
		Mockito.doThrow(fatalExceptionWhileStartingAppContext).when(ctxAppMock).refresh();
		//
		//
		holder = new TestHybrisContextHolder()
		{
			@Override
			HybrisContextFactory getGlobalCtxFactory()
			{
				return new TestGlobalContextFactory(globalContextResource)
				{
					@Override
					protected GenericApplicationContext createNewContext()
					{
						return ctxGlobalMock;
					}
				};
			}

			@Override
			HybrisContextFactory getAppCtxFactory(final Tenant tenant) throws BeansException
			{
				return new TestApplicationContextFactory(ctxGlobalMock, appOneContextResource, appTwoContextResource)
				{
					@Override
					protected GenericApplicationContext createNewContext()
					{

						return ctxAppMock;
					}
				};
			}
		};


		holder.getGlobalInstance();

		try
		{
			TestUtils.disableFileAnalyzer("Expect to have here an error message");
			holder.getApplicationInstance(tenantMock);
			Assert.fail("Should not be able to create instance ");
		}
		catch (final RuntimeException e)
		{
			Assert.assertEquals(EXPECTED_MESSAGE, e.getMessage());
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}

		Mockito.verify(ctxAppMock, Mockito.times(1)).destroy();
	}


	@Test
	public void testCheckSpecificExtensionsForTenant() throws Exception
	{
		final GenericApplicationContext globalCtx = new TestGlobalContextFactory(globalContextResource).build();

		holder = new TestHybrisContextHolder()
		{
			@Override
			protected GenericApplicationContext getGlobalInstanceCached() throws FatalBeanException
			{
				return globalCtx;
			}

			@Override
			HybrisContextFactory getAppCtxFactory(final Tenant tenant) throws BeansException
			{
				return new TestApplicationContextFactory(globalCtx, appOneContextResource, appTwoContextResource)
				{
					@Override
					protected Collection<String> getPlatformExtensions()
					{
						// 
						return Arrays.asList(EXT_ONE);
					}
				};

			}
		};

		final ApplicationContext appCtx = holder.getApplicationInstance(tenantMock);


		Assert.assertNotNull("Should be able to fetch global bean ", appCtx.getBean(GLOBAL_BEAN));

		Assert.assertNotNull("Should be able to fetch bean from extension one ", appCtx.getBean(BEAN_ONE));
		Assert.assertFalse("Should not be able to fetch bean from extension two ", appCtx.containsBean(BEAN_TWO));


	}

	@Test
	public void testAssureFailFastWhenGlobalContextRefreshFails() throws Exception
	{

		final RuntimeException fatalExceptionWhileStartingAppContext = new BeanCreationException(EXPECTED_MESSAGE);
		//
		Mockito.doThrow(fatalExceptionWhileStartingAppContext).when(ctxGlobalMock).refresh();

		holder = new TestHybrisContextHolder()
		{
			@Override
			HybrisContextFactory getGlobalCtxFactory()
			{
				return new TestGlobalContextFactory(globalContextResource)
				{
					@Override
					protected GenericApplicationContext createNewContext()
					{
						return ctxGlobalMock;
					}
				};
			}
		};

		try
		{
			TestUtils.disableFileAnalyzer("Expect to have here an error message");
			holder.getGlobalInstance();
			Assert.fail("This should throw an exception on refresh ");
		}
		catch (final RuntimeException e)
		{
			Assert.assertEquals(BeanCreationException.class, e.getClass());
			Assert.assertEquals(EXPECTED_MESSAGE, e.getMessage());
			//fine here 
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}


		try
		{
			TestUtils.disableFileAnalyzer("Expect to have here an error message");
			holder.getApplicationInstance(tenantMock);//fail fast} 
			Assert.fail("This should throw while creating since the global context is not constructed ");
		}
		catch (final RuntimeException e)
		{
			Assert.assertEquals(FatalBeanException.class, e.getClass());
			Assert.assertTrue(e.getMessage().endsWith(EXPECTED_MESSAGE));
			//fine here 
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}


		Mockito.verify(ctxAppMock, Mockito.times(0)).refresh();

		try
		{
			TestUtils.disableFileAnalyzer("Expect to have here an error message");
			holder.getApplicationInstance(tenantMock);//fail fast}
			Assert.fail("This should throw while creating since the global context is not constructed ");
		}
		catch (final RuntimeException e)
		{
			Assert.assertEquals(FatalBeanException.class, e.getClass());
			Assert.assertTrue(e.getMessage().endsWith(EXPECTED_MESSAGE));
			//fine here 
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}


		Mockito.verify(ctxAppMock, Mockito.times(0)).refresh();

	}

	@Test
	public void testAssureFailFastWhenApplicationContextRefreshFails() throws Exception
	{

		final String TEST_CTX_NAME = "TestApplicationContextFactory";


		BDDMockito.doThrow(new BeanCreationException("Error while refreshing application context")).when(ctxAppMock).refresh();

		holder = new TestHybrisContextHolder()
		{
			@Override
			HybrisContextFactory getGlobalCtxFactory()
			{
				return new TestGlobalContextFactory(globalContextResource)
				{
					@Override
					protected GenericApplicationContext createNewContext()
					{
						return ctxGlobalMock;
					}
				};
			}

			@Override
			HybrisContextFactory getAppCtxFactory(final Tenant tenant) throws BeansException
			{
				return new TestApplicationContextFactory(TEST_CTX_NAME, ctxGlobalMock, appOneContextResource, appTwoContextResource)
				{

					@Override
					protected GenericApplicationContext createNewContext()
					{
						return ctxAppMock;
					}
				};
			}
		};


		holder.getGlobalInstance();


		try
		{
			TestUtils.disableFileAnalyzer("Expect to have here an error message");
			holder.getApplicationInstance(tenantMock);//fail fast}
			Assert.fail("This should throw while creating since the application context is not constructed ");
		}
		catch (final RuntimeException e)
		{
			Assert.assertEquals(BeanCreationException.class, e.getClass());
			Assert.assertEquals("Error while refreshing application context", e.getMessage());
			//fine here 
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}


		Mockito.reset(ctxAppMock);

		try
		{
			holder.getApplicationInstance(tenantMock);//fail fast}
			Assert.fail("This should throw while creating since the application context is not constructed ");
		}
		catch (final RuntimeException e)
		{
			Assert.assertEquals(FatalBeanException.class, e.getClass());
			Assert.assertEquals("Context hybris application Context Factory - " + TEST_CTX_NAME
					+ " couldn't  be created correctly due to, Error while refreshing application context", e.getMessage());
			//fine here			
		}

		Mockito.verify(ctxAppMock, Mockito.times(0)).refresh();


		try
		{
			holder.getApplicationInstance(tenantMock);//fail fast}
			Assert.fail("This should throw while creating since the application context is not constructed ");
		}
		catch (final RuntimeException e)
		{
			Assert.assertEquals(FatalBeanException.class, e.getClass());
			Assert.assertEquals("Context hybris application Context Factory - " + TEST_CTX_NAME
					+ " couldn't  be created correctly due to, Error while refreshing application context", e.getMessage());
			//fine here 
		}

		Mockito.verify(ctxAppMock, Mockito.times(0)).refresh();
	}


	static class TestGlobalContextFactory extends GlobalContextFactory
	{

		private final org.springframework.core.io.Resource globalContextResource;

		private static Exception testFailure;

		TestGlobalContextFactory(final org.springframework.core.io.Resource globalContextResource)
		{
			this.globalContextResource = globalContextResource;
		}

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
		void setLoadingFailure(final Exception loadingFailed)
		{
			testFailure = loadingFailed;
		}

		@Override
		Exception getLoadingFailure()
		{
			return testFailure;
		}


		@Override
		XmlBeanDefinitionReader createXmlBeanReader(final GenericApplicationContext context)
		{
			final XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);
			xmlReader.setDocumentReaderClass(ScopeTenantIgnoreDocReader.class);
			return xmlReader;
		}
	}


	static class TestApplicationContextFactory extends ApplicationContextFactory
	{
		private static Exception testFailure;

		private final org.springframework.core.io.Resource appOneContextResource;
		private final org.springframework.core.io.Resource appTwoContextResource;

		@Override
		void setLoadingFailure(final Exception loadingFailed)
		{
			testFailure = loadingFailed;
		}

		@Override
		Exception getLoadingFailure()
		{
			return testFailure;
		}

		TestApplicationContextFactory(final GenericApplicationContext globalContext, final Resource appOneContextResource,
				final Resource appTwoContextResource)
		{
			this(FOO_CTX_ID, globalContext, appOneContextResource, appTwoContextResource);

		}

		TestApplicationContextFactory(final String id, final GenericApplicationContext globalContext,
				final Resource appOneContextResource, final Resource appTwoContextResource)
		{
			super(id, null, globalContext);
			this.appOneContextResource = appOneContextResource;
			this.appTwoContextResource = appTwoContextResource;
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
		XmlBeanDefinitionReader createXmlBeanReader(final GenericApplicationContext context)
		{
			final XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);
			xmlReader.setDocumentReaderClass(ScopeTenantIgnoreDocReader.class);
			return xmlReader;
		}

	}

	static class TestHybrisContextHolder extends HybrisContextHolder
	{

		//some dummy cache here
		@Override
		protected GenericApplicationContext getGlobalInstanceCached() throws FatalBeanException
		{
			if (globalContextRef == null)
			{
				globalContextRef = getGlobalCtxFactory().build();

			}
			return globalContextRef;
		}

		@Override
		HybrisContextFactory getGlobalCtxFactory()
		{
			// 
			return new GlobalContextFactory()
			{
				@Override
				XmlBeanDefinitionReader createXmlBeanReader(final GenericApplicationContext context)
				{
					final XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);
					xmlReader.setDocumentReaderClass(ScopeTenantIgnoreDocReader.class);
					return xmlReader;
				}
			};
		}

	}


	class TenantStub extends AbstractTenant
	{
		TenantStub()
		{
			super(FOO_CTX_ID);
		}

		@Override
		public Locale getTenantSpecificLocale()
		{
			// YTODO Auto-generated method stub
			return null;
		}

		@Override
		public TimeZone getTenantSpecificTimeZone()
		{
			// YTODO Auto-generated method stub
			return null;
		}

		@Override
		public List<String> getTenantSpecificExtensionNames()
		{
			// YTODO Auto-generated method stub
			return null;
		}

		@Override
		public ConfigIntf getConfig()
		{
			// YTODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUp() throws ConsistencyCheckException
		{
			// YTODO Auto-generated method stub

		}

		@Override
		public void shutDown()
		{
			// YTODO Auto-generated method stub

		}

		@Override
		void shutDown(final ShutDownMode mode)
		{
			// YTODO Auto-generated method stub			
		}

	}

}
