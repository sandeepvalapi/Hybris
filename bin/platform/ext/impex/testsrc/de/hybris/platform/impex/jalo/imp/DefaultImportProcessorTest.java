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
package de.hybris.platform.impex.jalo.imp;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.interceptor.InterceptorRegistry;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.testframework.TestUtils;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Test for HORST-1477.
 */
@IntegrationTest
public class DefaultImportProcessorTest extends ServicelayerTest
{

	public static final String SELECT_PK_FROM_TITLE_WHERE_NAME_EN_NAME = "SELECT {PK} FROM {Title} WHERE {name[en]}=?name";
	public static final String SELECT_PK_FROM_ADDRESS_WHERE_STREETNAME_STREETNAME = "SELECT {PK} FROM {Address} WHERE {streetname}=?streetname";

	@Resource
	private ImportService importService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private ModelService modelService;

	@Resource
	private UserService userService;

	@Resource
	private InterceptorRegistry interceptorRegistry;

	private TestAddressModelValidateInterceptor interceptor;

	private CustomerModel customer;
	private String uid;

	@Before
	public void setUp()
	{
		interceptor = assertInterceptorInstalled();

		customer = getOrCreateCustomer();
		// We MUST get the uid from customer since other modules install interceptors that *mess it up*!
		uid = customer.getUid();

		interceptor.setUpForTest(uid);

		SearchResult<Object> search = getTitleSearchResult();
		search.getResult().forEach(title -> modelService.remove(title));

		search = getAddressSearchResult();
		search.getResult().forEach(address -> modelService.remove(address));
	}

	@After
	public void tearDown()
	{
		interceptor.deactivate();
	}

	@Test
	public void testSinglethreaded()
	{
		assertThat(getTitleSearchResult().getCount()).isEqualTo(0);
		assertThat(getAddressSearchResult().getCount()).isEqualTo(0);
		TestUtils.disableFileAnalyzer("expecting InterceptorException", 1000);
		final ImportResult importResult = importService.importData(createImportConfig(false));
		TestUtils.enableFileAnalyzer();
		assertThat(interceptor.wasExceptionThrown()).isTrue();
		assertThat(importResult).isNotNull();
		assertThat(importResult.isSuccessful()).isEqualTo(true);
		assertThat(importResult.hasUnresolvedLines()).isEqualTo(false);
	}

	@Test
	public void testMultithreaded()
	{
		assertThat(getTitleSearchResult().getCount()).isEqualTo(0);
		assertThat(getAddressSearchResult().getCount()).isEqualTo(0);
		TestUtils.disableFileAnalyzer("expecting InterceptorException", 1000);
		final ImportResult importResult = importService.importData(createImportConfig(true));
		TestUtils.enableFileAnalyzer();
		assertThat(interceptor.wasExceptionThrown()).isTrue();
		assertThat(importResult).isNotNull();
		assertThat(importResult.isSuccessful()).isEqualTo(true);
		assertThat(importResult.hasUnresolvedLines()).isEqualTo(false);
	}

	protected TestAddressModelValidateInterceptor assertInterceptorInstalled()
	{
		for (final ValidateInterceptor i : interceptorRegistry.getValidateInterceptors(AddressModel._TYPECODE))
		{
			if (i instanceof TestAddressModelValidateInterceptor)
			{
				return (TestAddressModelValidateInterceptor) i;
			}
		}
		fail("TestAddressModelValidateInterceptor not installed! - got "
				+ interceptorRegistry.getValidateInterceptors(AddressModel._TYPECODE) + " instead");
		return null;
	}

	protected CustomerModel getOrCreateCustomer()
	{
		final String UID = "testCustomer00";
		try
		{
			return (CustomerModel) userService.getUserForUID(UID);
		}
		catch (final UnknownIdentifierException e)
		{
			final CustomerModel customer = modelService.create(CustomerModel.class);
			customer.setUid(UID);
			modelService.save(customer);
			return customer;
		}
	}

	protected SearchResult<Object> getAddressSearchResult()
	{
		return flexibleSearchService.search(SELECT_PK_FROM_ADDRESS_WHERE_STREETNAME_STREETNAME,
				Collections.singletonMap("streetname", "testStreet00"));
	}

	protected SearchResult<Object> getTitleSearchResult()
	{
		return flexibleSearchService.search(SELECT_PK_FROM_TITLE_WHERE_NAME_EN_NAME,
				Collections.singletonMap("name", "testTitle00"));
	}

	private ImportConfig createImportConfig(final boolean multithreaded)
	{
		final ImportConfig importConfig = new ImportConfig();
		importConfig.setMaxThreads(multithreaded ? 10 : 1);
		importConfig.setLegacyMode(false);
		importConfig.setFailOnError(true);
		importConfig.setValidationMode(ImportConfig.ValidationMode.STRICT);
		importConfig
				.setScript("INSERT_UPDATE Address;owner(Customer.uid);streetname;streetnumber[unique=true];title(code)\n" + ";" + uid
						+ ";testStreet00;00;title00\n" + "INSERT_UPDATE Title;code[unique=true];name[lang=en];\n"
						+ ";title00;testTitle00;\n"
						+ "INSERT_UPDATE Address;owner(Customer.uid);streetname;streetnumber[unique=true];title(code)\n" + ";" + uid
						+ ";testStreet00;01;title01\n" + "INSERT_UPDATE Title;code[unique=true];name[lang=en];\n"
						+ ";title01;testTitle00;\n"
						+ "INSERT_UPDATE Address;owner(Customer.uid);streetname;streetnumber[unique=true];title(code)\n" + ";" + uid
						+ ";testStreet00;02;title02\n" + "INSERT_UPDATE Title;code[unique=true];name[lang=en];\n"
						+ ";title02;testTitle00;\n"
						+ "INSERT_UPDATE Address;owner(Customer.uid);streetname;streetnumber[unique=true];title(code)\n" + ";" + uid
						+ ";testStreet00;03;title03\n" + "INSERT_UPDATE Title;code[unique=true];name[lang=en];\n"
						+ ";title03;testTitle00;\n"
						+ "INSERT_UPDATE Address;owner(Customer.uid);streetname;streetnumber[unique=true];title(code)\n" + ";" + uid
						+ ";testStreet00;04;title04\n" + "INSERT_UPDATE Title;code[unique=true];name[lang=en];\n"
						+ ";title04;testTitle00;\n"
						+ "INSERT_UPDATE Address;owner(Customer.uid);streetname;streetnumber[unique=true];title(code)\n" + ";" + uid
						+ ";testStreet00;05;title05\n" + "INSERT_UPDATE Title;code[unique=true];name[lang=en];\n"
						+ ";title05;testTitle00;\n"
						+ "INSERT_UPDATE Address;owner(Customer.uid);streetname;streetnumber[unique=true];title(code)\n" + ";" + uid
						+ ";testStreet00;06;title06\n" + "INSERT_UPDATE Title;code[unique=true];name[lang=en];\n"
						+ ";title06;testTitle00;\n"
						+ "INSERT_UPDATE Address;owner(Customer.uid);streetname;streetnumber[unique=true];title(code)\n" + ";" + uid
						+ ";testStreet00;07;title07\n" + "INSERT_UPDATE Title;code[unique=true];name[lang=en];\n"
						+ ";title07;testTitle00;\n"
						+ "INSERT_UPDATE Address;owner(Customer.uid);streetname;streetnumber[unique=true];title(code)\n" + ";" + uid
						+ ";testStreet00;08;title08\n" + "INSERT_UPDATE Title;code[unique=true];name[lang=en];\n"
						+ ";title08;testTitle00;\n"
						+ "INSERT_UPDATE Address;owner(Customer.uid);streetname;streetnumber[unique=true];title(code)\n" + ";" + uid
						+ ";testStreet00;09;title09\n" + "INSERT_UPDATE Title;code[unique=true];name[lang=en];\n"
						+ ";title09;testTitle00;\n");
		return importConfig;
	}
}
