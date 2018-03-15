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
package de.hybris.platform.commons.test.jalo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commons.constants.CommonsConstants;
import de.hybris.platform.commons.jalo.CommonsManager;
import de.hybris.platform.commons.jalo.CustomOrder2XML;
import de.hybris.platform.commons.jalo.Document;
import de.hybris.platform.commons.jalo.FOPFormatter;
import de.hybris.platform.commons.jalo.Format;
import de.hybris.platform.commons.jalo.MediaFormatter;
import de.hybris.platform.commons.jalo.VelocityFormatter;
import de.hybris.platform.commons.jalo.renderer.RendererTemplate;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.MediaUtil;
import de.hybris.platform.util.localization.Localization;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;


/**
 * UnitTest for extension Commons.
 * 
 */
@IntegrationTest
public class CommonsTest extends HybrisJUnit4TransactionalTest
{
	private Customer testcustomer = null; //customer for an order
	private Country testcountry = null; //country for user/customer
	private Address testaddress = null;
	private Product testproduct = null;
	private Unit testunit = null;
	private Currency testcurrency = null;

	private AbstractOrder testorder = null;
	private AbstractOrderEntry testorderentry = null;

	@Before
	public void setUp() throws Exception
	{
		testcustomer = UserManager.getInstance().createCustomer("testmoo");
		testcustomer.setName("the testcustommer");
		assertNotNull(testcustomer);

		testcurrency = C2LManager.getInstance().createCurrency("testcurrency");
		assertNotNull(testcurrency);

		testorder = OrderManager.getInstance()
				.createOrder("the testorder", testcustomer, testcurrency, new Date(1234567890), false);
		assertNotNull(testorder);

		testcountry = C2LManager.getInstance().createCountry("testcountry");
		assertNotNull(testcountry);

		testaddress = testcustomer.createAddress();
		testaddress.setAttribute(Address.FIRSTNAME, "blah");
		testaddress.setAttribute(Address.LASTNAME, "blubb");
		testaddress.setAttribute(Address.STREETNAME, "weg");
		testaddress.setAttribute(Address.STREETNUMBER, "44");
		testaddress.setCountry(testcountry);
		assertNotNull(testaddress);

		testorder.setDeliveryAddress(testaddress);
		testorder.setPaymentAddress(testaddress);
		testcustomer.setDefaultDeliveryAddress(testaddress);
		testcustomer.setDefaultPaymentAddress(testaddress);
		testorder.setUser(testcustomer);

		testproduct = ProductManager.getInstance().createProduct("testproduct");
		assertNotNull(testproduct);

		testunit = ProductManager.getInstance().createUnit("unittype", "unitcode");
		assertNotNull(testunit);

		testorderentry = testorder.addNewEntry(testproduct, 10, testunit);
		testorderentry.setBasePrice(34.56);
		testorderentry.setTotalPrice(345.60);
		assertNotNull(testorderentry);

	}

	@Test
	public void testCustomOrder2PDF() throws ConsistencyCheckException, JaloBusinessException, IOException
	{
		final CustomOrder2XML co2x = CommonsManager.getInstance().createCustomOrder2XML(
				Collections.singletonMap(CustomOrder2XML.CODE, "test_smallchain_1"));
		assertNotNull(co2x);

		final FOPFormatter fopf = CommonsManager.getInstance().createFOPFormatter(
				Collections.singletonMap(FOPFormatter.CODE, "test_smallchain_2"));
		assertNotNull(fopf);

		fopf.setInputMimeType("text/xml");
		fopf.setOutputMimeType("application/pdf");
		fopf.setData(new DataInputStream(CommonsTest.class.getResourceAsStream("/commons/unittest/quotation.xsl")),
				"quotation.xsl", "text/xml");

		final Format format = CommonsManager.getInstance().createFormat(Collections.singletonMap(Format.CODE, "test_smallchain_3"));
		assertNotNull(format);

		format.setInitial(co2x);
		format.setChained(Collections.singletonList((MediaFormatter) fopf));

		final Document ret = format.format(testorder);

		final String mimetypetest = MediaUtil.guess(ret.getDataFromStream());
		assertEquals(mimetypetest, "application/pdf");

		final InputStreamReader streamReader = new InputStreamReader(ret.getDataFromStream());
		final BufferedReader bufferedReader = new BufferedReader(streamReader);

		try
		{
			assertBufferLineHas(bufferedReader.readLine(), "%PDF-1.4");
			bufferedReader.readLine();
			assertBufferLineHas(bufferedReader.readLine(), "1 0 obj");
		}
		finally
		{
			IOUtils.closeQuietly(bufferedReader);
		}
	}

	@Test
	public void testVelocityTemplate() throws JaloBusinessException, IOException
	{
		final VelocityFormatter formatter = CommonsManager.getInstance().createVelocityFormatter(
				Collections.singletonMap(VelocityFormatter.CODE, "test_velocityFormatter"));
		assertNotNull(formatter);
		formatter.setData(new DataInputStream(CommonsTest.class.getResourceAsStream("/commons/unittest/testtemplate1.templ")),
				"testtemplate1.templ", "text");
		formatter.setOutputMimeType("text");

		final Format format = CommonsManager.getInstance().createFormat(
				Collections.singletonMap(Format.CODE, "test_velocityFormatter_2"));
		assertNotNull(format);

		format.setInitial(formatter);

		final Document ret = format.format(testorder);

		final InputStreamReader streamReader = new InputStreamReader(ret.getDataFromStream());
		final BufferedReader bufferedReader = new BufferedReader(streamReader);
		try
		{
			assertBufferLineHas(bufferedReader.readLine(), "the testorder");
			assertBufferLineHas(bufferedReader.readLine(), "testcurrency");
		}
		finally
		{
			IOUtils.closeQuietly(bufferedReader);
		}
	}

	@Test
	public void testFormatsSearch() throws JaloBusinessException
	{
		final ComposedType ctao = TypeManager.getInstance().getComposedType(AbstractOrder.class);

		final CustomOrder2XML co2x_1 = CommonsManager.getInstance().createCustomOrder2XML(
				Collections.singletonMap(CustomOrder2XML.CODE, "test_smallchain_1"));
		assertNotNull(co2x_1);

		final FOPFormatter fopf_1 = CommonsManager.getInstance().createFOPFormatter(
				Collections.singletonMap(FOPFormatter.CODE, "test_smallchain_2"));
		assertNotNull(fopf_1);
		fopf_1.setInputMimeType("text/xml");
		fopf_1.setOutputMimeType("application/pdf");
		fopf_1.setData(new DataInputStream(CommonsTest.class.getResourceAsStream("/commons/unittest/quotation.xsl")),
				"quotation.xsl", "text/xml");
		final Format format1 = CommonsManager.getInstance().createFormat(Collections.singletonMap(Format.CODE, "xxx1"));
		assertNotNull(format1);
		format1.setInitial(co2x_1);
		format1.setChained(Collections.singletonList((MediaFormatter) fopf_1));

		format1.setValidFor(Collections.singletonList(ctao));
		format1.format(testorder);

		final VelocityFormatter vf_2 = CommonsManager.getInstance().createVelocityFormatter(
				Collections.singletonMap(VelocityFormatter.CODE, "test_velocityFormatter_search"));
		assertNotNull(vf_2);
		vf_2.setData(new DataInputStream(CommonsTest.class.getResourceAsStream("/commons/unittest/testtemplate1.templ")),
				"testtemplate1.templ", "text");
		vf_2.setOutputMimeType("text");

		final Format format2 = CommonsManager.getInstance().createFormat(Collections.singletonMap(Format.CODE, "xxx1"));
		assertNotNull(format2);
		format2.setInitial(vf_2);
		format2.setValidFor(Collections.singletonList(ctao));
		format2.format(testorder);

		final Collection res = CommonsManager.getInstance().getFormatsForItem(testorder);
		assertEquals(2, res.size());
		for (final Iterator iter = res.iterator(); iter.hasNext();)
		{
			final Format element = (Format) iter.next();
			assertEquals("xxx1", element.getCode());
		}

	}

	@Test
	public void testGetAndSetDocument() throws JaloBusinessException
	{
		final VelocityFormatter formatter = CommonsManager.getInstance().createVelocityFormatter(
				Collections.singletonMap(VelocityFormatter.CODE, "test_velocityFormatter_search"));
		assertNotNull(formatter);
		formatter.setData(new DataInputStream(CommonsTest.class.getResourceAsStream("/commons/unittest/testtemplate1.templ")),
				"testtemplate1.templ", "text");
		formatter.setOutputMimeType("text");

		final Format format = CommonsManager.getInstance().createFormat(
				Collections.singletonMap(Format.CODE, "test_velocityFormatter_2_search"));
		assertNotNull(format);
		format.setInitial(formatter);
		final Document ret = format.format(testorder);
		ret.setCode("myDocument");

		final Collection res = CommonsManager.getInstance().getDocuments(testorder, format);

		assertEquals(res.size(), 1);
		assertEquals(((Document) res.iterator().next()).getCode(), "myDocument");

		CommonsManager.getInstance().setDocuments(testorder, format, Collections.EMPTY_LIST);

		final Collection res2 = CommonsManager.getInstance().getDocuments(testorder, format);
		assertEquals(res2.size(), 0);

	}

	@Test
	public void testGetandSetAllDocuments1() throws JaloBusinessException
	{
		final ComposedType orderType = TypeManager.getInstance().getComposedType(Order.class);

		final CustomOrder2XML co2x_1 = CommonsManager.getInstance().createCustomOrder2XML(
				Collections.singletonMap(CustomOrder2XML.CODE, "test_smallchain_1"));
		assertNotNull(co2x_1);

		final FOPFormatter fopf_1 = CommonsManager.getInstance().createFOPFormatter(
				Collections.singletonMap(FOPFormatter.CODE, "test_smallchain_2"));
		assertNotNull(fopf_1);
		fopf_1.setInputMimeType("text/xml");
		fopf_1.setOutputMimeType("application/pdf");
		fopf_1.setData(new DataInputStream(CommonsTest.class.getResourceAsStream("/commons/unittest/quotation.xsl")),
				"quotation.xsl", "text/xml");
		final Format format1 = CommonsManager.getInstance().createFormat(Collections.singletonMap(Format.CODE, "xxx1"));
		assertNotNull(format1);
		format1.setInitial(co2x_1);
		format1.setChained(Collections.singletonList((MediaFormatter) fopf_1));

		format1.setValidFor(Collections.singletonList(orderType));
		final Document ret1 = format1.format(testorder);
		ret1.setCode("ret1");

		final VelocityFormatter vf_2 = CommonsManager.getInstance().createVelocityFormatter(
				Collections.singletonMap(VelocityFormatter.CODE, "test_velocityFormatter_search"));
		assertNotNull(vf_2);
		vf_2.setData(new DataInputStream(CommonsTest.class.getResourceAsStream("/commons/unittest/testtemplate1.templ")),
				"testtemplate1.templ", "text");
		vf_2.setOutputMimeType("text");

		final Format format2 = CommonsManager.getInstance().createFormat(Collections.singletonMap(Format.CODE, "xxx1"));
		assertNotNull(format2);
		format2.setInitial(vf_2);
		format2.setValidFor(Collections.singletonList(orderType));
		final Document ret2 = format2.format(testorder);
		ret2.setCode("ret2");


		final Collection<Document> res = CommonsManager.getInstance().getAllDocuments(testorder);
		assertEquals(res.size(), 2);
		assertEquals( new HashSet<String>(Arrays.asList("ret1","ret2")), res.stream().map(d -> d.getCode()).collect(Collectors.toSet()));

		final Collection newcoll1 = new ArrayList();
		ret2.setCode("dddd");
		newcoll1.add(ret2);
		CommonsManager.getInstance().setAllDocuments(testorder, newcoll1);

		final Collection<Document> res2 = CommonsManager.getInstance().getAllDocuments(testorder);

		assertEquals(res2.size(), 1);
		assertEquals( new HashSet<String>(Arrays.asList("dddd")), res2.stream().map(d -> d.getCode()).collect(Collectors.toSet()));

		//------------------------------------------------------

		final Map params1 = new HashMap();
		params1.put(Format.CODE, "formatblacode3");
		params1.put(Format.NAME, "formatblaname3");
		final Format format3 = CommonsManager.getInstance().createFormat(params1);
		assertNotNull(format3);

		final Map params2 = new HashMap();
		final Collection newcoll2 = new ArrayList();
		params2.put(Document.CODE, "bla1");
		params2.put(Document.FORMAT, format3);
		params2.put(Document.SOURCEITEM, testcurrency);
		params2.put(Document.ITEMTIMESTAMP, testorder.getCreationTime());
		final Document doc3 = CommonsManager.getInstance().createDocument(params2);
		newcoll2.add(doc3);
		assertNotNull(doc3);

		try
		{
			CommonsManager.getInstance().setAllDocuments(testorder, newcoll2);
			fail("A JaloInvalidParameterException was expected");
		}
		catch (final JaloInvalidParameterException e)
		{
			// OK
		}
		catch (final Exception e)
		{
			fail("unexpected exception was thrown");
		}
	}

	@Test
	public void testRefreshAndSo() throws JaloBusinessException, IOException, InterruptedException
	{
		final Product product = ProductManager.getInstance().createProduct("testrefreshproduct");
		assertNotNull(product);


		final VelocityFormatter formatter = CommonsManager.getInstance().createVelocityFormatter(
				Collections.singletonMap(VelocityFormatter.CODE, "test_velocityFormatter"));
		assertNotNull(formatter);
		formatter.setData(new DataInputStream(CommonsTest.class.getResourceAsStream("/commons/unittest/testtemplate2.templ")),
				"testtemplate2.templ", "text");
		formatter.setOutputMimeType("text");

		final Format format = CommonsManager.getInstance().createFormat(
				Collections.singletonMap(Format.CODE, "test_velocityFormatter_2"));
		assertNotNull(format);

		format.setInitial(formatter);

		final Document ret = format.format(product);

		assertFalse(ret.needRefresh());
		final InputStreamReader streamReader = new InputStreamReader(ret.getDataFromStream());
		final BufferedReader bufferedReader = new BufferedReader(streamReader);
		try
		{
			assertBufferLineHas(bufferedReader.readLine(), "testrefreshproduct");
		}
		finally
		{
			IOUtils.closeQuietly(bufferedReader);
		}

		//TODO: problems with mysql
		Thread.sleep(2000);
		product.setCode("new testrefreshproduct name");
		assertTrue(ret.needRefresh());
		ret.setCode("testmoo");
		ret.refresh();

		final InputStreamReader ir1 = new InputStreamReader(ret.getDataFromStream());
		final BufferedReader br1 = new BufferedReader(ir1);
		String line1 = null;

		line1 = br1.readLine();
		assertEquals(line1, "new testrefreshproduct name");
		assertEquals("testmoo", ret.getCode());

	}

	@Test
	public void testSetAndGetScript() throws JaloBusinessException, IOException
	{
		final Product product1 = ProductManager.getInstance().createProduct("testscriptproduct");
		assertNotNull(product1);
		product1.setDescription("mooo-");

		final VelocityFormatter formatter = CommonsManager.getInstance().createVelocityFormatter(
				Collections.singletonMap(VelocityFormatter.CODE, "test_vF"));
		assertNotNull(formatter);
		formatter.setScript("$this.getDescription()|$this.code");
		formatter.setOutputMimeType("text");

		final Format format = CommonsManager.getInstance().createFormat(
				Collections.singletonMap(Format.CODE, "test_velocityFormatter_2"));
		assertNotNull(format);

		format.setInitial(formatter);

		final Document ret = format.format(product1);

		final InputStreamReader streamReader = new InputStreamReader(ret.getDataFromStream());
		final BufferedReader bufferedReader = new BufferedReader(streamReader);

		try
		{
			assertBufferLineHas(bufferedReader.readLine(), "mooo-|testscriptproduct");
		}
		finally
		{
			IOUtils.closeQuietly(bufferedReader);
		}

		assertEquals("\n$this.getDescription()|$this.code", formatter.getScript());
	}

	/**
	 * defines some renderer context class (interface) -
	 * because it is necessary to test RendererTemplate.getContextClassDescription reflection functionality
	 */
	public interface TestRendererContext
	{
		String getParameter1();

		boolean isParameter2();

		String getParameter3();

		void setParameter1(String param1);

		void setParameter2(boolean param2);

		void setParameter3(String param3);
	}

	@Test
	public void testGetRendererContextClassDescription()
	{
		final SessionContext ctx = jaloSession.getSessionContext();

		final EnumerationValue rendererType = EnumerationManager.getInstance().getEnumerationValue(
				CommonsConstants.TC.RENDERERTYPEENUM, CommonsConstants.Enumerations.RendererTypeEnum.VELOCITY);

		assertNotNull(rendererType);

		final Map attributeValues = new HashMap();
		attributeValues.put(RendererTemplate.CODE, "testCode123");
		attributeValues.put(RendererTemplate.DESCRIPTION, "testDescription123");
		attributeValues.put(RendererTemplate.TEMPLATESCRIPT, "testTemplateScript123");
		attributeValues.put(RendererTemplate.CONTEXTCLASS, "de.hybris.platform.commons.test.jalo.CommonsTest$TestRendererContext");
		attributeValues.put(RendererTemplate.RENDERERTYPE, rendererType);

		final RendererTemplate template = CommonsManager.getInstance().createRendererTemplate(ctx, attributeValues);
		assertNotNull(template);

		// as Class.getMethods() is NOT guaranteed to return methods in any order we have to
		// test for correct parameters texts in a rather strange way
		// this requires a TestRendererContext
		final String text = template.getContextClassDescription(ctx);
		assertTrue(text.contains("parameter1  "));
		assertTrue(text.contains("parameter2  "));
		assertTrue(text.contains("parameter3  "));
	}

	@Test
	public void testGetRendererContextDescriptionClassEmpty()
	{
		final SessionContext ctx = jaloSession.getSessionContext();

		final EnumerationValue rendererType = EnumerationManager.getInstance().getEnumerationValue(
				CommonsConstants.TC.RENDERERTYPEENUM, CommonsConstants.Enumerations.RendererTypeEnum.VELOCITY);

		assertNotNull(rendererType);

		final Map attributeValues = new HashMap();
		attributeValues.put(RendererTemplate.CODE, "testCode123");
		attributeValues.put(RendererTemplate.DESCRIPTION, "testDescription123");
		attributeValues.put(RendererTemplate.TEMPLATESCRIPT, "testTemplateScript123");
		attributeValues.put(RendererTemplate.CONTEXTCLASS, "");
		attributeValues.put(RendererTemplate.RENDERERTYPE, rendererType);

		final RendererTemplate template = CommonsManager.getInstance().createRendererTemplate(ctx, attributeValues);
		assertNotNull(template);

		assertEquals("", template.getContextClassDescription(ctx));

	}


	@Test
	public void testGetRendererContextDescriptionClassNotFound()
	{
		final String notExistedContextClass = "NotExistedContextClass";
		final SessionContext ctx = jaloSession.getSessionContext();

		final Language initialLanguage = ctx.getLanguage();

		ctx.setLanguage(getOrCreateLanguage("en"));

		final EnumerationValue rendererType = EnumerationManager.getInstance().getEnumerationValue(
				CommonsConstants.TC.RENDERERTYPEENUM, CommonsConstants.Enumerations.RendererTypeEnum.VELOCITY);

		assertNotNull(rendererType);

		final Map attributeValues = new HashMap();
		attributeValues.put(RendererTemplate.CODE, "testCode123");
		attributeValues.put(RendererTemplate.DESCRIPTION, "testDescription123");
		attributeValues.put(RendererTemplate.TEMPLATESCRIPT, "testTemplateScript123");
		attributeValues.put(RendererTemplate.CONTEXTCLASS, notExistedContextClass);
		attributeValues.put(RendererTemplate.RENDERERTYPE, rendererType);

		final RendererTemplate template = CommonsManager.getInstance().createRendererTemplate(ctx, attributeValues);
		assertNotNull(template);

		assertEquals(Localization.getLocalizedString(RendererTemplate.CONTEXT_CLASS_NOT_FOUND, new String[]
		{ notExistedContextClass }), template.getContextClassDescription(ctx));

		ctx.setLanguage(initialLanguage);
	}

	private void assertBufferLineHas(final String bufferedLine, final String content) throws IOException
	{
		assertEquals(bufferedLine, content);
	}
}
