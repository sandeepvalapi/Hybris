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
package de.hybris.platform.jmx;

import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.jmx.mbeans.impl.AbstractJMXMBean;
import de.hybris.platform.testframework.TestUtils;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.JMX;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.jmx.support.JmxUtils;
import org.springframework.util.ReflectionUtils;


@ManualTest
public class HybrisJmxIntegrationTest extends AbstractHybrisJmxTest
{

	private static final Logger LOG = Logger.getLogger(HybrisJmxIntegrationTest.class.getName());


	@Before
	public void checkRemoteAccess()
	{
		if (Registry.isStandaloneMode())
		{
			LOG.warn("Integration test will be ignored since it is in standalone mode");
			Assume.assumeTrue(!Registry.isStandaloneMode());
			return;
		}
		final String jmxURL = String.format("service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi", "localhost",
				Integer.valueOf(Registry.getMasterTenant().getConfig().getInt("tomcat.jmx.port", 9001)));
		JMXConnector connector = null;
		try
		{
			testConnection("localhost", Registry.getMasterTenant().getConfig().getInt("tomcat.jmx.port", 9001));
			LOG.info("Checking remote access via JMX ... to " + jmxURL);
			final JMXServiceURL target = new JMXServiceURL(jmxURL);

			connector = JMXConnectorFactory.connect(target);
			LOG.info("Integration test will be fired since a JMX interface  available at  " + jmxURL);
		}
		catch (final Exception e)
		{
			TestUtils.disableFileAnalyzer(5);//three lines might contain a exception
			LOG.info("Unable to perform integration tests, remote access via JMX ... to " + jmxURL + " impossible ,"
					+ e.getMessage());
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage(), e);
			}
			Assume.assumeNoException(e);
		}
		finally
		{
			if (connector != null)
			{

				try
				{
					connector.close();
				}
				catch (final IOException e)
				{
					//NOOP
				}

			}
		}
	}

	@Test
	public void testJMXAPI() throws IOException, MalformedObjectNameException, NullPointerException, InstanceNotFoundException,
			IntrospectionException, ReflectionException, ClassNotFoundException, SecurityException, NoSuchMethodException
	{

		final String jmxURL = String.format("service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi", "localhost",
				Integer.valueOf(Registry.getMasterTenant().getConfig().getInt("tomcat.jmx.port", 9001)));
		LOG.info("Trying remotely access via JMX :" + jmxURL);
		// Get target's URL
		final JMXServiceURL target = new JMXServiceURL(jmxURL);

		// Connect to target (assuming no security)
		JMXConnector connector = null;
		try
		{
			connector = JMXConnectorFactory.connect(target);

			// Get an MBeanServerConnection on the remote VM.
			final MBeanServerConnection remote = connector.getMBeanServerConnection();

			LOG.info("Reloading all JMX beans for hybris ");
			Registry.getApplicationContext().getBean("mbeanRegisterUtility", MBeanRegisterUtilities.class).refreshMBeans();
			LOG.info("Available JMX beans for hybris "
					+ Registry.getApplicationContext().getBean("mbeanRegisterUtility", MBeanRegisterUtilities.class).getRegisteredBeans().keySet());

			LOG.info("All available MBeans: " + remote.getMBeanCount());
			LOG.info("Default domain MBeans: " + remote.getDefaultDomain());
			if (remote.getDomains() != null)
			{
				LOG.info("Default domain MBeans: " + Arrays.asList(remote.getDomains()));
			}

			LOG.info("All hybris JMX spring MBeans: " + getAllSpringJMXBeans());

			for (final Map.Entry<String, AbstractJMXMBean> entry : getAllSpringJMXBeans().entrySet())
			{

				LOG.info("Checking JMX bean " + entry.getKey());

				ObjectName name = new ObjectName(entry.getKey());

				for (final ObjectInstance instance : remote.queryMBeans(new ObjectName("hybris:*"), new ObjectName("hybris:*")))//could be [Master|junit] [T|t]enant
				{
					LOG.info("[ Domain <" + instance.getObjectName().getDomain() + ">, name <"
							+ instance.getObjectName().getCanonicalName() + ">, class <" + instance.getClassName() + ">, instance <"
							+ instance + ">] ");
					LOG.info("Object name <" + instance.getObjectName() + ">");
				}
				//check metainfo
				MBeanInfo info = null;

				try
				{
					LOG.info("Checking availability of bean  [" + name + "] :" + remote.isRegistered(name));
					info = remote.getMBeanInfo(name);
				}
				catch (final javax.management.InstanceNotFoundException e)
				{
					LOG.info("Couldn't fetch bean " + entry.getKey() + " falling back to "
							+ de.hybris.platform.jmx.JmxUtils.MASTER_TENANT);
					//try to switch
					String masterKey = entry.getKey();
					if (masterKey.indexOf(Registry.getCurrentTenant().getTenantID() + " Tenant") > 0)
					{
						masterKey = masterKey.replace(Registry.getCurrentTenant().getTenantID()
								+ de.hybris.platform.jmx.JmxUtils.TENANT_SUBFIX, de.hybris.platform.jmx.JmxUtils.MASTER_TENANT);
						LOG.info("(Re)Checking JMX bean " + masterKey);
						info = remote.getMBeanInfo(name = new ObjectName(masterKey));
					}
					else
					{
						throw e;
					}
				}

				Assert.assertTrue("Metainfo for object" + name + " should be registered  ", info != null);

				checkAllAttributtes(entry.getValue(), info);
				checkAllOperations(entry.getValue(), info);
				checkDuplicatedOperations(entry.getValue(), info);

				//check instance
				final ObjectInstance instance = remote.getObjectInstance(name);
				Assert.assertTrue("Instance  for object" + name + " should be registered  ", instance != null);

				checkInstance(remote, instance);

			}
		}
		finally
		{
			if (connector != null)
			{
				connector.close();
			}
		}
	}

	/**
	 * tries to invoke all not parameterized methods from bean instance
	 * 
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private void checkInstance(final MBeanServerConnection remote, final ObjectInstance newMBeanProxy)
			throws ClassNotFoundException, SecurityException, NoSuchMethodException
	{

		final Class beanClass = Class.forName(newMBeanProxy.getClassName());
		//XXX it is very fragile for a custom class/interface  hierarchy
		final Object proxyInstance = JMX.newMBeanProxy(remote, newMBeanProxy.getObjectName(), beanClass.getInterfaces()[0], false);

		final Method[] methods = JmxUtils.getClassToExpose(proxyInstance).getMethods();

		//support only simple methods without parameters
		for (final Method method : methods)
		{
			if (shouldExcludeMethod(method))
			{
				continue;
			}

			//ModelMBeanOperationInfo info = null;
			final PropertyDescriptor propertyDescriptor = BeanUtils.findPropertyForMethod(method);
			LOG.debug("method<" + method.getName() + "> property <" + propertyDescriptor + ">");

			if (propertyDescriptor != null)
			{
				if (propertyDescriptor.getReadMethod() != null)
				{
					if (!shouldExcludeMethod(propertyDescriptor.getReadMethod()))
					{
						checkInvokeMethod(proxyInstance, propertyDescriptor.getReadMethod());
					}
				}

			}
			else
			{
				checkInvokeMethod(proxyInstance, method);
			}

			//invoke directly via RMI
			try
			{
				//ReflectionUtils.
				//support only simple methods without parameters
				final Method reflectedMethod = Class.forName(newMBeanProxy.getClassName()).getMethod(method.getName(),
						(Class<?>[]) null);
				if (reflectedMethod.isAnnotationPresent(org.springframework.jmx.export.annotation.ManagedOperation.class))
				{
					final Object result = remote.invoke(newMBeanProxy.getObjectName(), method.getName(), null, null);
					if (LOG.isDebugEnabled())
					{
						LOG.debug("operation <" + method.getName() + "> result " + result);
					}
				}
				else if (reflectedMethod.isAnnotationPresent(org.springframework.jmx.export.annotation.ManagedAttribute.class))
				{
					final Object result = remote.getAttribute(newMBeanProxy.getObjectName(),
							method.getName().startsWith("is") ? method.getName().substring("is".length()) : (method.getName()
									.startsWith("get") ? method.getName().substring("get".length()) : method.getName()));
					if (LOG.isDebugEnabled())
					{
						LOG.debug("attributte  <" + method.getName() + "> value " + result);
					}
				}
			}
			catch (final InstanceNotFoundException e)
			{
				Assert.fail(e.getMessage());
			}
			catch (final MBeanException e)
			{
				Assert.fail(e.getMessage());
			}
			catch (final ReflectionException e)
			{
				Assert.fail(e.getMessage());
			}
			catch (final IOException e)
			{
				Assert.fail(e.getMessage());
			}
			catch (final AttributeNotFoundException e)
			{
				Assert.fail(e.getMessage());
			}
		}


	}


	private void checkInvokeMethod(final Object proxyInstance, final Method method)
	{
		try
		{
			final Object result = ReflectionUtils.invokeMethod(method, proxyInstance);

			if (!method.getReturnType().equals(Void.class))
			{
				LOG.debug("result<" + result + ">");
			}
		}
		/*
		 * catch (final SessionClosedException sce) { //ignore this }
		 */
		catch (final Exception e)
		{
			Assert.fail("Invoking method " + method.getName() + " on bean " + proxyInstance + " failed :" + e.getMessage());
			LOG.error(e);
		}
	}

	/**
	 * if method should be checked
	 */
	private boolean shouldExcludeMethod(final Method method)
	{
		if (method.isSynthetic())
		{
			return true;
		}
		if (method.getDeclaringClass().equals(Object.class))
		{
			return true;
		}

		if (method.getName().equals("toString") || method.getName().equals("hashCode"))
		{
			return true;
		}

		if (method.getAnnotations().equals(Object.class))
		{
			return true;
		}

		if (method.getParameterTypes() != null && method.getParameterTypes().length > 0)
		{
			return true;
		}

		return false;
	}

	private void testConnection(final String url, final int port) throws Exception
	{
		Socket socket = null;
		try
		{
			LOG.info("Checking JMX socket availibility ...");
			socket = new Socket(url, port);
			socket.isConnected();
			LOG.info("Checked JMX socket availibility - ok.");
		}
		finally
		{
			if (socket != null)
			{
				try
				{
					socket.close();
				}
				catch (final Exception e)
				{
					LOG.error(e.getMessage());
				}

			}
		}
	}



	/**
	 * we close all session in our test so default finish fails
	 */
	@Override
	public void finish() throws JaloSecurityException
	{
		// XXX: hack: switch back session user in case a unit test changed it
		if (jaloSession.isClosed())
		{
			jaloSession = JaloSession.getCurrentSession();
		}
		jaloSession.setUser(UserManager.getInstance().getAnonymousCustomer());
		//
		jaloSession = null;
	}



}
