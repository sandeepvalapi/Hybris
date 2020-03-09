/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.fulfilmentprocess.test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.DebitPaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.impl.DefaultCartService;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.payment.commands.factory.CommandFactory;
import de.hybris.platform.payment.commands.factory.impl.DefaultCommandFactoryRegistryImpl;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.definition.ProcessDefinitionFactory;
import de.hybris.platform.processengine.impl.DefaultBusinessProcessService;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.product.impl.DefaultProductService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.spring.ctx.ScopeTenantIgnoreDocReader;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.impl.DefaultTaskService;
import de.hybris.platform.util.Utilities;
import com.hybris.training.fulfilmentprocess.test.actions.TestActionTemp;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;


@IntegrationTest
public class PaymentIntegrationTest extends ServicelayerTest
{
	private static final Logger LOG = Logger.getLogger(PaymentIntegrationTest.class);

	@Resource
	private CommerceCheckoutService commerceCheckoutService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private PaymentService paymentService;
	@Resource
	private CalculationService calculationService;
	@Resource
	private BaseSiteService baseSiteService;
	@Resource
	private DeliveryService deliveryService;

	private static DefaultBusinessProcessService processService;
	private static ProcessDefinitionFactory definitonFactory;

	private static ProductService productService;
	private static CartService cartService;
	private static ModelService modelService;
	private static UserService userService;

	private static TaskServiceStub taskServiceStub;

	@BeforeClass
	public static void prepare() throws Exception
	{
		Registry.activateStandaloneMode();
		Utilities.setJUnitTenant();
		LOG.debug("Preparing...");

		final ApplicationContext appCtx = Registry.getGlobalApplicationContext();

		assertTrue(
				"Application context of type " + appCtx.getClass() + " is not a subclass of " + ConfigurableApplicationContext.class,
				appCtx instanceof ConfigurableApplicationContext);

		final ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) appCtx;
		final ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
		assertTrue("Bean Factory of type " + beanFactory.getClass() + " is not of type " + BeanDefinitionRegistry.class,
				beanFactory instanceof BeanDefinitionRegistry);
		final XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry) beanFactory);
		xmlReader.setDocumentReaderClass(ScopeTenantIgnoreDocReader.class);
		xmlReader.loadBeanDefinitions(
				new ClassPathResource("/trainingfulfilmentprocess/test/trainingfulfilmentprocess-spring-test.xml"));
		xmlReader.loadBeanDefinitions(new ClassPathResource(
				"/trainingfulfilmentprocess/test/trainingfulfilmentprocess-spring-test-fraudcheck.xml"));
		xmlReader
				.loadBeanDefinitions(new ClassPathResource("/trainingfulfilmentprocess/test/process/order-process-spring.xml"));
		modelService = (ModelService) getBean("modelService");
		processService = (DefaultBusinessProcessService) getBean("businessProcessService");
		definitonFactory = processService.getProcessDefinitionFactory();

		LOG.warn("Prepare Process Definition factory...");
		definitonFactory.add("classpath:/trainingfulfilmentprocess/test/process/payment-process.xml");

		//setup command factory to mock
		final DefaultCommandFactoryRegistryImpl commandFactoryReg = appCtx.getBean("commandFactoryRegistry",
				DefaultCommandFactoryRegistryImpl.class);
		commandFactoryReg.setCommandFactoryList(Arrays.asList((CommandFactory) appCtx.getBean("mockupCommandFactory")));

		taskServiceStub = appCtx.getBean("taskServiceStub", TaskServiceStub.class);
		productService = appCtx.getBean("defaultProductService", DefaultProductService.class);
		cartService = appCtx.getBean("defaultCartService", DefaultCartService.class);
		userService = appCtx.getBean("defaultUserService", DefaultUserService.class);
	}

	@Before
	public void setUpProductCatalogue()
	{
		try
		{
			new CoreBasicDataCreator().createEssentialData(Collections.EMPTY_MAP, null);
			importCsv("/trainingfulfilmentprocess/test/testBasics.csv", "windows-1252");
			importCsv("/trainingfulfilmentprocess/test/testCatalog.csv", "windows-1252");
			baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID("testSite"), false);
			LOG.warn("Catalogue has been imported");
		}
		catch (final ImpExException e)
		{
			LOG.warn("Catalogue import has failed");
			LOG.error("Exception : ", e);
		}
		catch (final Exception e)
		{
			LOG.warn("createEssentialData(...) has failed");
			LOG.error("Exception : ", e);
		}
	}

	@AfterClass
	public static void removeProcessDefinitions()
	{
		LOG.debug("cleanup...");

		final ApplicationContext appCtx = Registry.getGlobalApplicationContext();

		assertTrue(
				"Application context of type " + appCtx.getClass() + " is not a subclass of " + ConfigurableApplicationContext.class,
				appCtx instanceof ConfigurableApplicationContext);

		final ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) appCtx;
		final ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
		assertTrue("Bean Factory of type " + beanFactory.getClass() + " is not of type " + BeanDefinitionRegistry.class,
				beanFactory instanceof BeanDefinitionRegistry);
		final XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry) beanFactory);
		xmlReader.loadBeanDefinitions(
				new ClassPathResource("/trainingfulfilmentprocess/test/trainingfulfilmentprocess-spring-testcleanup.xml"));

		//cleanup command factory
		final Map<String, CommandFactory> commandFactoryList = applicationContext.getBeansOfType(CommandFactory.class);
		commandFactoryList.remove("mockupCommandFactory");
		final DefaultCommandFactoryRegistryImpl commandFactoryReg = appCtx.getBean(DefaultCommandFactoryRegistryImpl.class);
		commandFactoryReg.setCommandFactoryList(commandFactoryList.values());

		//		if (definitonFactory != null)
		//		{
		// TODO this test seems to let processes run after method completion - therefore we cannot
		// remove definitions !!!
		//			definitonFactory.remove("testPlaceorder");
		//			definitonFactory.remove("testConsignmentFulfilmentSubprocess");
		//		}
		processService.setTaskService(appCtx.getBean("taskService", DefaultTaskService.class));
		definitonFactory = null;
		processService = null;
	}

	@After
	public void resetServices()
	{
		final List<TaskModel> tasks = taskServiceStub.cleanup();
		final StringBuffer msg = new StringBuffer();
		for (final TaskModel task : tasks)
		{
			final ProcessTaskModel processTask = (ProcessTaskModel) task;

			msg.append(processTask.getAction()).append(", ");
		}

		assertTrue("tasks should be empty after test execution. Left: " + msg, tasks.isEmpty());
	}

	@Test
	public void testPaymentIntegrationAccepted() throws InterruptedException, CalculationException
	{
		try
		{
			final OrderProcessModel orderProcess = createProcess("payment-process-test");
			orderProcess.setOrder(placeTestOrder(true));
			modelService.save(orderProcess);
			assertStep(orderProcess, "checkOrder");
			assertStep(orderProcess, "checkAuthorizeOrderPayment");
			assertStep(orderProcess, "cancelOrder");
			assertStep(orderProcess, "notifyCustomer");
		}
		catch (final InvalidCartException e)
		{
			LOG.info("Problems with the cart detected");
		}
	}

	@Test
	public void testPaymentIntegrationDeclined() throws InterruptedException, CalculationException
	{
		setResultForAction("test.checkAuthorizeOrderPaymentAction", "NOK");

		try
		{
			final OrderProcessModel orderProcess = createProcess("payment-process-test");
			orderProcess.setOrder(placeTestOrder(false));
			modelService.save(orderProcess);
			assertStep(orderProcess, "checkOrder");
			assertStep(orderProcess, "checkAuthorizeOrderPayment");
			assertStep(orderProcess, "notifyCustomer");
		}
		catch (final InvalidCartException e)
		{
			LOG.info("Problems with the cart detected");
		}
	}

	private static int codeNo = 1;

	protected OrderModel placeTestOrder(final boolean valid) throws InvalidCartException, CalculationException
	{
		final CartModel cart = cartService.getSessionCart();
		final UserModel user = userService.getCurrentUser();
		cartService.addNewEntry(cart, productService.getProductForCode("testProduct1"), 1, null);
		cartService.addNewEntry(cart, productService.getProductForCode("testProduct2"), 2, null);
		cartService.addNewEntry(cart, productService.getProductForCode("testProduct3"), 3, null);

		final AddressModel deliveryAddress = new AddressModel();
		deliveryAddress.setOwner(user);
		deliveryAddress.setFirstname("Der");
		deliveryAddress.setLastname("Buck");
		deliveryAddress.setTown("Muenchen");
		deliveryAddress.setCountry(commonI18NService.getCountry("DE"));
		modelService.save(deliveryAddress);

		final DebitPaymentInfoModel paymentInfo = new DebitPaymentInfoModel();
		paymentInfo.setOwner(cart);
		paymentInfo.setBank("MeineBank");
		paymentInfo.setUser(user);
		paymentInfo.setAccountNumber("34434");
		paymentInfo.setBankIDNumber("1111112");
		paymentInfo.setBaOwner("Ich");
		paymentInfo.setCode("testPaymentInfo1");
		modelService.save(paymentInfo);

		cart.setDeliveryMode(deliveryService.getDeliveryModeForCode("free"));
		cart.setDeliveryAddress(deliveryAddress);
		cart.setPaymentInfo(paymentInfo);

		final CardInfo card = new CardInfo();
		card.setCardType(CreditCardType.VISA);
		card.setCardNumber("4111111111111111");
		card.setExpirationMonth(Integer.valueOf(12));
		if (valid)
		{
			card.setExpirationYear(Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR) + 2));
		}
		else
		{
			card.setExpirationYear(Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR) - 2));
		}

		final PaymentTransactionModel paymentTransaction = paymentService
				.authorize("code4" + codeNo++, BigDecimal.ONE, Currency.getInstance("EUR"), deliveryAddress, deliveryAddress, card)
				.getPaymentTransaction();

		cart.setPaymentTransactions(Collections.singletonList(paymentTransaction));
		modelService.save(cart);
		calculationService.calculate(cart);

		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cart);
		parameter.setSalesApplication(SalesApplication.WEB);

		return commerceCheckoutService.placeOrder(parameter).getOrder();
	}

	protected OrderProcessModel createProcess(final String processName)
	{
		final String id = "Test" + (new Date()).getTime();
		return processService.startProcess(id, processName);
	}

	protected void assertStep(final BusinessProcessModel process, final String bean) throws InterruptedException
	{
		LOG.info("assertStep action = " + bean);

		try
		{
			final ProcessTaskModel processTaskModel = taskServiceStub.runProcessTask(bean);

			if (bean == null)
			{
				final StringBuffer found = new StringBuffer();

				for (final TaskModel task : taskServiceStub.getTasks())
				{
					if (task instanceof ProcessTaskModel)
					{
						found.append(((ProcessTaskModel) task).getAction()).append("; ");
					}
				}

				assertNotNull("No tasks found for bean " + bean + ", actions: " + found.toString(), processTaskModel);
			}


		}
		catch (final RetryLaterException e)
		{
			fail(e.toString());
		}
	}

	protected void setResultForAction(final String action, final String result)
	{
		final TestActionTemp tmp = (TestActionTemp) getBean(action);
		tmp.setResult(result);
	}

	protected static Object getBean(final String name)
	{
		return Registry.getApplicationContext().getBean(name);
	}
}
