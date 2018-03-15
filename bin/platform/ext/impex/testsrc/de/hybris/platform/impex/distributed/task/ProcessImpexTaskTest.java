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
package de.hybris.platform.impex.distributed.task;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.distributed.batch.ImportBatchHandler;
import de.hybris.platform.impex.distributed.batch.impl.TestImportBatchHandler;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.security.auth.AuthenticationService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;


@IntegrationTest
public class ProcessImpexTaskTest extends ServicelayerBaseTest
{
	@Resource
	private ProcessImpexTask processImpexTask;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;
	@Resource
	private AuthenticationService authenticationService;


	@Before
	public void setUp() throws Exception
	{
		final LanguageModel de = modelService.create(LanguageModel.class);
		de.setIsocode("DE");
		de.setName("German");
		modelService.save(de);
	}

	@Test
	public void shouldImportItemsUsing_INSERT_header_With_NullValueEntry() throws Exception
	{
		// given
		assertThat(userService.isUserExisting("jan")).isFalse();
		assertThat(userService.isUserExisting("piotr")).isFalse();

		final String importData = "INSERT_UPDATE Customer; uid[unique=true]; description;\n;jan;janHasDescription;\n;piotr;";
		final TestImportBatchHandler handler = new TestImportBatchHandler(importData);

		// when
		processImpexTask.execute(handler);

		// then
		assertThat(handler.getRemainingWorkLoad()).isEqualTo(0);
		assertThat(handler.getOutputData()).isNull();

		assertThat(userService.isUserExisting("jan")).isTrue();
		final CustomerModel storedJan = userService.getUserForUID("jan", CustomerModel.class);
		assertThat(storedJan.getDescription()).isEqualTo("janHasDescription");

		assertThat(userService.isUserExisting("piotr")).isTrue();
		final CustomerModel storedPiotr = userService.getUserForUID("piotr", CustomerModel.class);
		assertThat(storedPiotr.getDescription()).isNull();
	}

	@Test
	public void shouldImportItemsUsing_INSERT_header_With_LessValueEntriesThanColumnsInHeader() throws Exception
	{
		// given
		assertThat(userService.isUserExisting("jan")).isFalse();
		assertThat(userService.isUserExisting("piotr")).isFalse();

		final String importData = "INSERT_UPDATE Customer; uid[unique=true]; description;name;\n;jan;janHasDescription;janHasName\n;piotr";
		final TestImportBatchHandler handler = new TestImportBatchHandler(importData);

		// when
		processImpexTask.execute(handler);

		// then
		assertThat(handler.getRemainingWorkLoad()).isEqualTo(0);
		assertThat(handler.getOutputData()).isNull();

		assertThat(userService.isUserExisting("jan")).isTrue();
		final CustomerModel storedJan = userService.getUserForUID("jan", CustomerModel.class);
		assertThat(storedJan.getDescription()).isEqualTo("janHasDescription");
		assertThat(storedJan.getName()).isEqualTo("janHasName");

		assertThat(userService.isUserExisting("piotr")).isTrue();
		final CustomerModel storedPiotr = userService.getUserForUID("piotr", CustomerModel.class);
		assertThat(storedPiotr.getDescription()).isNull();
		assertThat(storedPiotr.getName()).isNull();

	}

	@Test
	public void shouldImportItemsUsing_INSERT_header() throws Exception
	{
		// given
		final String importData = "INSERT Title;code[unique=true];name[lang=en];name[lang=de]\n;test1;test1EN;test1DE\n;test2;test2EN;test2DE\n;test3;test3EN;test3DE";
		final TestImportBatchHandler handler = new TestImportBatchHandler(importData);

		// when
		processImpexTask.execute(handler);

		// then
		assertThatTitleModelIsSuccessfullyImported("test1", ImmutableMap.of(Locale.ENGLISH, "test1EN", Locale.GERMAN, "test1DE"));
		assertThatTitleModelIsSuccessfullyImported("test2", ImmutableMap.of(Locale.ENGLISH, "test2EN", Locale.GERMAN, "test2DE"));
		assertThatTitleModelIsSuccessfullyImported("test3", ImmutableMap.of(Locale.ENGLISH, "test3EN", Locale.GERMAN, "test3DE"));
		assertThat(handler.getRemainingWorkLoad()).isEqualTo(0);
		assertThat(handler.getOutputData()).isNull();
	}

	@Test
	public void shouldImportItemsUsing_UPDATE_header() throws Exception
	{
		// given
		createModel("test1", "test1EN", "test1DE");
		createModel("test2", "test2EN", "test2DE");
		createModel("test3", "test3EN", "test3DE");
		final String importData = "UPDATE Title;code[unique=true];name[lang=en];name[lang=de]\n;test1;test1EN--UPD;test1DE--UPD\n;test2;test2EN--UPD;test2DE--UPD\n;test3;test3EN--UPD;test3DE--UPD";
		final TestImportBatchHandler handler = new TestImportBatchHandler(importData);

		// when
		processImpexTask.execute(handler);

		// then

		assertThatTitleModelIsSuccessfullyImported("test1",
				ImmutableMap.of(Locale.ENGLISH, "test1EN--UPD", Locale.GERMAN, "test1DE--UPD"));
		assertThatTitleModelIsSuccessfullyImported("test2",
				ImmutableMap.of(Locale.ENGLISH, "test2EN--UPD", Locale.GERMAN, "test2DE--UPD"));
		assertThatTitleModelIsSuccessfullyImported("test3",
				ImmutableMap.of(Locale.ENGLISH, "test3EN--UPD", Locale.GERMAN, "test3DE--UPD"));
		assertThat(handler.getRemainingWorkLoad()).isEqualTo(0);
		assertThat(handler.getOutputData()).isNull();
	}

	@Test
	public void shouldImportItemsUsing_REMOVE_header() throws Exception
	{
		// given
		createModel("test1", "test1EN", "test1DE");
		createModel("test2", "test2EN", "test2DE");
		createModel("test3", "test3EN", "test3DE");
		final String importData = "REMOVE Title;code[unique=true]\n;test1\n;test2\n;test3";
		final TestImportBatchHandler handler = new TestImportBatchHandler(importData);

		// when
		processImpexTask.execute(handler);

		// then
		final Optional<TitleModel> title1 = findTitleForCode("test1");
		assertThat(title1.isPresent()).isFalse();
		final Optional<TitleModel> title2 = findTitleForCode("test2");
		assertThat(title2.isPresent()).isFalse();
		final Optional<TitleModel> title3 = findTitleForCode("test3");
		assertThat(title3.isPresent()).isFalse();
	}

	@Test
	public void shouldImportItemsUsing_INSERT_UPDATE_header() throws Exception
	{
		// given
		createModel("test1", "test1EN", "test1DE");
		createModel("test2", "test2EN", "test2DE");
		createModel("test3", "test3EN", "test3DE");
		final String importData = "INSERT_UPDATE Title;code[unique=true];name[lang=en];name[lang=de]\n;test1;test1EN--UPD;test1DE--UPD\n;test2;test2EN--UPD;test2DE--UPD\n;test3;test3EN;test3DE\n;test4;test4EN;test4DE\n";
		final TestImportBatchHandler handler = new TestImportBatchHandler(importData);

		// when
		processImpexTask.execute(handler);

		// then
		assertThatTitleModelIsSuccessfullyImported("test1",
				ImmutableMap.of(Locale.ENGLISH, "test1EN--UPD", Locale.GERMAN, "test1DE--UPD"));
		assertThatTitleModelIsSuccessfullyImported("test2",
				ImmutableMap.of(Locale.ENGLISH, "test2EN--UPD", Locale.GERMAN, "test2DE--UPD"));
		assertThatTitleModelIsSuccessfullyImported("test3", ImmutableMap.of(Locale.ENGLISH, "test3EN", Locale.GERMAN, "test3DE"));
		assertThatTitleModelIsSuccessfullyImported("test4", ImmutableMap.of(Locale.ENGLISH, "test4EN", Locale.GERMAN, "test4DE"));
		assertThat(handler.getRemainingWorkLoad()).isEqualTo(0);
		assertThat(handler.getOutputData()).isNull();
	}

	@Test
	public void shouldImportItemsFromValidLinesAndPutInLogInvalidOnes() throws Exception
	{
		// given (hint: second value line doesn't start with an ; so actually cell with value "test2" is treated as a code of an Type, which is wrong)
		final String importData = "INSERT Title;code[unique=true];name[lang=en];name[lang=de]\n;test1;test1EN;test1DE\ntest2;test2EN;test2DE\n;test3;test3EN;test3DE";
		final TestImportBatchHandler handler = new TestImportBatchHandler(importData);

		// when
		processImpexTask.execute(handler);

		// then
		assertThatTitleModelIsSuccessfullyImported("test1", ImmutableMap.of(Locale.ENGLISH, "test1EN", Locale.GERMAN, "test1DE"));
		assertThatTitleModelIsSuccessfullyImported("test3", ImmutableMap.of(Locale.ENGLISH, "test3EN", Locale.GERMAN, "test3DE"));
		final Optional<TitleModel> title2 = findTitleForCode("test2");
		assertThat(title2.isPresent()).isFalse();
		assertThat(handler.getRemainingWorkLoad()).isEqualTo(0);
	}

	@Test
	public void shouldDumpWholeImportAndEnableImportByLineModeForNextTurn() throws Exception
	{
		// given
		final String importData = "INSERT Title;code[unique=true];name[lang=en];name[lang=de]\n;test1;test1EN;test1DE\n;test1;test2EN;test2DE\n;test2;test3EN;test3DE";
		final TestImportBatchHandler handler = new TestImportBatchHandler(importData);

		// when
		processImpexTask.execute(handler);

		// then
		final Optional<TitleModel> title1 = findTitleForCode("test1");
		assertThat(title1.isPresent()).isFalse();
		final Optional<TitleModel> title2 = findTitleForCode("test2");
		assertThat(title2.isPresent()).isFalse();
		assertThat(handler.getRemainingWorkLoad()).isEqualTo(60);
		assertThat(handler.getProperty(ImportBatchHandler.IMPORT_BY_LINE_FLAG)).isEqualTo("true");
	}

	@Test
	public void shouldDumpInvalidLineAndImportCorrectLinesInByLineMode() throws Exception
	{
		// given
		final String importData = "INSERT Title;code[unique=true];name[lang=en];name[lang=de]\n;test1;test1EN;test1DE\n;test1;test2EN;test2DE\n;test3;test3EN;test3DE";
		final TestImportBatchHandler handler = new TestImportBatchHandler(importData);
		handler.setProperty(ImportBatchHandler.IMPORT_BY_LINE_FLAG, "true");

		// when
		processImpexTask.execute(handler);

		// then
		assertThatTitleModelIsSuccessfullyImported("test1", ImmutableMap.of(Locale.ENGLISH, "test1EN", Locale.GERMAN, "test1DE"));
		assertThatTitleModelIsSuccessfullyImported("test3", ImmutableMap.of(Locale.ENGLISH, "test3EN", Locale.GERMAN, "test3DE"));
		assertThat(handler.getRemainingWorkLoad()).isEqualTo(20);
		assertThat(handler.getOutputData()).isNotEmpty();
	}

	@Test
	public void shouldPreventImportAnyLineWhenCodeExecutionIsEnabledAnd_clear_IsCalledOnAValieLineInstanceViaScripting()
			throws Exception
	{
		// given
		final String importData = "INSERT Title;code[unique=true];name[lang=en];name[lang=de]\n#% beforeEach: line.clear();\n;test1;test1EN;test1DE\n;test2;test2EN;test2DE\n;test3;test3EN;test3DE";
		final TestImportBatchHandler handler = new TestImportBatchHandler(importData);
		handler.setProperty(ImportBatchHandler.CODE_EXECUTION_FLAG, "true");

		// when
		processImpexTask.execute(handler);

		// then
		final Optional<TitleModel> title1 = findTitleForCode("test1");
		assertThat(title1.isPresent()).isFalse();
		final Optional<TitleModel> title2 = findTitleForCode("test2");
		assertThat(title2.isPresent()).isFalse();
		final Optional<TitleModel> title3 = findTitleForCode("test3");
		assertThat(title3.isPresent()).isFalse();
	}

	@Test
	public void shouldDumpPartiallyCreatedItemForSecondPassExecution() throws Exception
	{
		// given
		final String importData1 = "INSERT_UPDATE Customer; uid[unique=true]; defaultPaymentAddress( &payAddress );\n;jan;payAddress0\n;piotr;payAddress1";
		final String importData2 = "INSERT Address; &payAddress; owner( Customer.uid );department;\n;payAddress0;jan;a1\n;payAddress1;piotr;a2";
		final TestImportBatchHandler handler1 = new TestImportBatchHandler(importData1);
		final TestImportBatchHandler handler2 = new TestImportBatchHandler(importData2);

		// when
		processImpexTask.execute(handler1);
		processImpexTask.execute(handler2);

		// then
		assertThat(userService.isUserExisting("jan")).isTrue();
		final CustomerModel storedJan = userService.getUserForUID("jan", CustomerModel.class);
		assertThat(storedJan.getDefaultPaymentAddress()).isNull();

		assertThat(userService.isUserExisting("piotr")).isTrue();
		final CustomerModel storedPiotr = userService.getUserForUID("piotr", CustomerModel.class);
		assertThat(storedPiotr.getDefaultPaymentAddress()).isNull();

		assertThat(handler1.getRemainingWorkLoad()).isEqualTo(20);
		final String expectedDump = "INSERT_UPDATE Customer;uid[unique=true];defaultPaymentAddress( &payAddress )\n" + "Customer,"
				+ storedJan.getPk().getLongValue()
				+ ",,,column 2: cannot resolve value 'payAddress0' for attribute 'defaultPaymentAddress';<ignore>jan;payAddress0\n"
				+ "Customer," + storedPiotr.getPk().getLongValue()
				+ ",,,column 2: cannot resolve value 'payAddress1' for attribute 'defaultPaymentAddress';<ignore>piotr;payAddress1\n";
		assertThat(handler1.getOutputData()).isEqualTo(expectedDump);

		assertThat(handler2.getRemainingWorkLoad()).isEqualTo(0);
		assertThat(handler2.getOutputData()).isNull();
	}

	@Test
	public void shouldImportUserWithPasswordAttribute() throws Exception
	{
		// given
		final String importData = "INSERT_UPDATE Customer;uid[unique=true];customerID;name;password\n" //
				+ ";ahertz;K2006-C0005;Anja Hertz;1234\n" //
				+ ";abrode;K2006-C0006;Arin Brode;5678\n";
		final TestImportBatchHandler handler = new TestImportBatchHandler(importData);

		// when
		processImpexTask.execute(handler);

		// then
		final UserModel ahertz = userService.getUserForUID("ahertz");
		final UserModel abrode = userService.getUserForUID("abrode");
		assertEquals(ahertz, authenticationService.checkCredentials("ahertz", "1234"));
		assertEquals(abrode, authenticationService.checkCredentials("abrode", "5678"));
	}

	@Test
	public void shouldImportScriptContainingRecurrentSameLocalizedValue() throws Exception
	{
		// given
		final String importData = "INSERT Title;code[unique=true];name[lang=en];name[lang=de]\n" //
				+ ";test1;test1EN;test1DE\n" //
				+ ";test2;test1EN;test1DE\n" //
				+ ";test3;test1EN;test1DE\n";
		final TestImportBatchHandler handler = new TestImportBatchHandler(importData);

		// when
		processImpexTask.execute(handler);

		// then
		assertThatTitleModelIsSuccessfullyImported("test1", ImmutableMap.of(Locale.ENGLISH, "test1EN", Locale.GERMAN, "test1DE"));
		assertThatTitleModelIsSuccessfullyImported("test2", ImmutableMap.of(Locale.ENGLISH, "test1EN", Locale.GERMAN, "test1DE"));
		assertThatTitleModelIsSuccessfullyImported("test3", ImmutableMap.of(Locale.ENGLISH, "test1EN", Locale.GERMAN, "test1DE"));
		assertThat(handler.getRemainingWorkLoad()).isEqualTo(0);
		assertThat(handler.getOutputData()).isNull();
	}

	@Test
	public void shouldImportScriptContainingLocalizedValueWithoudExplicitIsoCode() throws Exception
	{
		// given
		final String importData = "INSERT Title;code[unique=true];name\n" //
				+ ";test1;test1EN\n" //
				+ ";test2;test2EN\n" //
				+ ";test3;test3EN\n";
		final TestImportBatchHandler handler = new TestImportBatchHandler(importData);

		// when
		processImpexTask.execute(handler);

		// then
		assertThatTitleModelIsSuccessfullyImported("test1", ImmutableMap.of(Locale.ENGLISH, "test1EN"));
		assertThatTitleModelIsSuccessfullyImported("test2", ImmutableMap.of(Locale.ENGLISH, "test2EN"));
		assertThatTitleModelIsSuccessfullyImported("test3", ImmutableMap.of(Locale.ENGLISH, "test3EN"));
		assertThat(handler.getRemainingWorkLoad()).isEqualTo(0);
		assertThat(handler.getOutputData()).isNull();
	}

	@Test
	public void shouldImportScriptContainingLocalizedValueWithoutExplicitIsoCodeAndLocalizedValueWitExplicitIsoCode()
			throws Exception
	{
		// given
		final String importData = "INSERT Title;code[unique=true];name;name[lang=de]\n" //
				+ ";test1;test1EN;test1DE\n" //
				+ ";test2;test2EN;test2DE\n" //
				+ ";test3;test3EN;test3DE\n";
		final TestImportBatchHandler handler = new TestImportBatchHandler(importData);

		// when
		processImpexTask.execute(handler);

		// then
		assertThatTitleModelIsSuccessfullyImported("test1", ImmutableMap.of(Locale.ENGLISH, "test1EN", Locale.GERMAN, "test1DE"));
		assertThatTitleModelIsSuccessfullyImported("test2", ImmutableMap.of(Locale.ENGLISH, "test2EN", Locale.GERMAN, "test2DE"));
		assertThatTitleModelIsSuccessfullyImported("test3", ImmutableMap.of(Locale.ENGLISH, "test3EN", Locale.GERMAN, "test3DE"));
		assertThat(handler.getRemainingWorkLoad()).isEqualTo(0);
		assertThat(handler.getOutputData()).isNull();
	}

	@Test
	public void shouldNotImportLineWhenSuchAnItemAlreadyExistUsing_INSERT_header() throws Exception
	{
		// given
		final TitleModel existingTitle = modelService.create(TitleModel.class);
		existingTitle.setCode("existing");
		modelService.save(existingTitle);
		final String importData = "INSERT Title;code[unique=true];name;name[lang=de]\n" //
				+ ";existing;existingEN;existingDE\n";
		final TestImportBatchHandler handler = new TestImportBatchHandler(importData);

		// when
		processImpexTask.execute(handler);
		final String dump = handler.getOutputData();
		final TestImportBatchHandler secondPassHandler = new TestImportBatchHandler(dump);
		secondPassHandler.setProperty(ImportBatchHandler.IMPORT_BY_LINE_FLAG, "true");
		processImpexTask.execute(secondPassHandler);

		// then
		assertThat(secondPassHandler.getRemainingWorkLoad()).isEqualTo(10);
		final Optional<TitleModel> existing = findTitleForCode("existing");
		assertThat(existing.isPresent()).isTrue();
		assertThat(existing.get().getName()).isNull();
		assertThat(existing.get().getName(Locale.GERMAN)).isNull();
	}

	@Test
	public void shouldNotImportLinesWhichRefersToExistingItemsAndImportWhatsPossibleUsing_INSERT_header() throws Exception
	{
		// given
		final TitleModel existingTitle = modelService.create(TitleModel.class);
		existingTitle.setCode("existing");
		modelService.save(existingTitle);
		final String importData = "INSERT Title;code[unique=true];name;name[lang=de]\n" //
				+ ";nonExisting1;nonExisting1EN;nonExisting1DE\n" //
				+ ";existing;existingEN;existingDE\n" //
				+ ";nonExisting2;nonExisting2EN;nonExisting2DE\n"; //
		final TestImportBatchHandler handler = new TestImportBatchHandler(importData);

		// when
		processImpexTask.execute(handler);
		final String dump = handler.getOutputData();
		final TestImportBatchHandler secondPassHandler = new TestImportBatchHandler(dump);
		secondPassHandler.setProperty(ImportBatchHandler.IMPORT_BY_LINE_FLAG, "true");
		processImpexTask.execute(secondPassHandler);

		// then
		assertThat(secondPassHandler.getRemainingWorkLoad()).isEqualTo(10);
		final Optional<TitleModel> existing = findTitleForCode("existing");
		assertThat(existing.isPresent()).isTrue();
		assertThat(existing.get().getName()).isNull();
		assertThat(existing.get().getName(Locale.GERMAN)).isNull();
		final Optional<TitleModel> nonExisting1 = findTitleForCode("nonExisting1");
		assertThat(nonExisting1.isPresent()).isTrue();
		assertThat(nonExisting1.get().getName()).isEqualTo("nonExisting1EN");
		assertThat(nonExisting1.get().getName(Locale.GERMAN)).isEqualTo("nonExisting1DE");
		final Optional<TitleModel> nonExisting2 = findTitleForCode("nonExisting2");
		assertThat(nonExisting2.isPresent()).isTrue();
		assertThat(nonExisting2.get().getName()).isEqualTo("nonExisting2EN");
		assertThat(nonExisting2.get().getName(Locale.GERMAN)).isEqualTo("nonExisting2DE");
	}

	@Test
	public void shouldDumpLineWhenTryingToUpdateNonExistentItemUsing_UPDATE_header() throws Exception
	{
		// given
        final String importData = "UPDATE Title;code[unique=true];name\n" +
                ";nonExistent;someName\n";
		final TestImportBatchHandler handler = new TestImportBatchHandler(importData);

		// when
		processImpexTask.execute(handler);

		// then
		assertThat(handler.getRemainingWorkLoad()).isEqualTo(20);
        assertThat(handler.getOutputData()).isEqualTo("UPDATE Title;code[unique=true];name\n" +
                ",,,,Existing item not found;nonExistent;someName\n");
	}

	private void assertThatTitleModelIsSuccessfullyImported(final String code, final Map<Locale, String> localizedNames)
	{
		final Optional<TitleModel> model = findTitleForCode(code);
		assertThat(model.isPresent()).isTrue();

		for (final Map.Entry<Locale, String> entry : localizedNames.entrySet())
		{
			final Locale locale = entry.getKey();
			final String value = entry.getValue();

			assertThat(model.get().getName(locale)).isEqualTo(value);

		}
	}

	private Optional<TitleModel> findTitleForCode(final String code)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Title} WHERE {code}=?code");
		fQuery.addQueryParameter("code", code);

		final SearchResult<TitleModel> searchResult = flexibleSearchService.search(fQuery);

		if (searchResult.getCount() == 0)
		{
			return Optional.empty();
		}

		if (searchResult.getCount() > 1)
		{
			fail("Found more than one TitleModel with code: " + code);
		}

		final List<TitleModel> result = searchResult.getResult();
		return Optional.of(result.get(0));
	}

	private TitleModel createModel(final String code, final String nameEn, final String nameDe)
	{
		final TitleModel title = modelService.create(TitleModel.class);
		title.setCode(code);
		title.setName(nameEn, Locale.ENGLISH);
		title.setName(nameDe, Locale.GERMAN);

		modelService.save(title);

		return title;
	}
}
