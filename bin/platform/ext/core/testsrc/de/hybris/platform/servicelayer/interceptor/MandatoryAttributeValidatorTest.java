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
package de.hybris.platform.servicelayer.interceptor;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.payment.DebitPaymentInfoModel;
import de.hybris.platform.core.model.test.TestItemType2Model;
import de.hybris.platform.core.model.test.TestItemType3Model;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.systemsetup.datacreator.impl.LangPackDataCreator;
import de.hybris.platform.core.systemsetup.datacreator.internal.CoreDataCreator;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Collections;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import junit.framework.Assert;


@IntegrationTest
public class MandatoryAttributeValidatorTest extends ServicelayerTransactionalBaseTest
{

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(MandatoryAttributeValidatorTest.class.getName());

	@Resource
	private I18NService i18nService;

	@Resource
	private ModelService modelService;

	@Resource
	private SessionService sessionService;

	@Resource
	private CoreDataCreator c2lDataCreator;

	@Before
	public void prepare()
	{
		c2lDataCreator.populateDatabase();
	}


	@Test
	public void testCreate1()
	{
		final UserModel user = modelService.create(UserModel.class);
		try
		{
			modelService.save(user);
			fail("expected fail with InterceptorException");
		}
		catch (final ModelSavingException e)
		{
			assertTrue(e.getCause() instanceof InterceptorException);
			assertTrue(e.getMessage().contains("[uid]"));
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}
	}


	@Test
	public void testCreate2()
	{
		final DebitPaymentInfoModel model = new DebitPaymentInfoModel();
		//those mandatory set to null
		model.setBank(null);
		model.setBankIDNumber(null);

		//those mandatory set to not null
		model.setCode("codenameEagle");
		model.setAccountNumber("100-111-2344");
		model.setBaOwner("owner");


		try
		{
			modelService.save(model);
			fail("expected fail with InterceptorException");
		}
		catch (final ModelSavingException e)
		{
			assertTrue(e.getCause() instanceof InterceptorException);
			assertTrue(e.getMessage().contains(DebitPaymentInfoModel.BANK));
			assertTrue(e.getMessage().contains(DebitPaymentInfoModel.BANKIDNUMBER));
			assertTrue(e.getMessage().contains(DebitPaymentInfoModel.USER));
		}
		catch (final Exception e)
		{
			fail("caught unexpected exception: " + e);
		}
	}

	@Test
	public void testCreate3()
	{
		final UserModel user = new UserModel();
		user.setUid("yyy");
		modelService.initDefaults(user);
		modelService.save(user);
	}


	@Test
	public void testMissingMandatoryAttributeGermanMessage()
	{
		getOrCreateLanguage(Locale.GERMAN.getLanguage());

		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{

				i18nService.setCurrentLocale(Locale.GERMAN);

				final TitleModel model = modelService.create(TitleModel.class);

				try
				{
					modelService.save(model);
				}
				catch (final ModelSavingException mse)
				{
					Assert.assertTrue(mse.getCause() instanceof InterceptorException);
					//Assert.assertEquals("missing values for [code] in model TitleModel (<unsaved>) to create a new Title", mse
					//		.getCause().getMessage());
					Assert.assertTrue(
							  "message should end with 'fehlende Werte für [code] in Modell TitleModel (<unsaved>) zum Erstellen einer " +
										 "neuen Title' but actually it was :"
										 + mse.getCause().getMessage(),
							  mse.getCause()
										 .getMessage()
										 .endsWith("fehlende Werte für [code] in Modell TitleModel (<unsaved>) zum Erstellen einer neuen " +
													"Title"));
				}
			}
		});
	}

	@Test
	public void testMissingMandatoryAttributeEnglishMessage()
	{

		sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{

				i18nService.setCurrentLocale(Locale.ENGLISH);

				final TitleModel model = modelService.create(TitleModel.class);

				try
				{
					modelService.save(model);
				}
				catch (final ModelSavingException mse)
				{
					Assert.assertTrue(mse.getCause() instanceof InterceptorException);
					//Assert.assertEquals("missing values for [code] in model TitleModel (<unsaved>) to create a new Title", mse
					//		.getCause().getMessage());
					Assert.assertTrue(
							  "message should end with 'missing values for [code] in model TitleModel (<unsaved>) to create a new " +
										 "Title' but actually it was :"
										 + mse.getCause().getMessage(),
							  mse.getCause().getMessage()
										 .endsWith("missing values for [code] in model TitleModel (<unsaved>) to create a new Title"));
				}
			}
		});
	}

	@Test
	public void shouldProperlySaveModelWhenMandatoryReferenceIsAlive() throws Exception
	{
		// given
		final TestItemType2Model itemTypeTwo = createAndSaveItemTypeTwo("foo");

		// when
		final TestItemType3Model itemType3 = modelService.create(TestItemType3Model.class);
		itemType3.setItemTypeTwo(itemTypeTwo);
		itemType3.setItemsTypeTwo(Collections.EMPTY_LIST);
		modelService.save(itemType3);

		// then
		checkModelState(itemTypeTwo);
		checkModelState(itemType3);
		assertThat(itemType3.getItemTypeTwo()).isEqualTo(itemTypeTwo);
	}

	private void checkModelState(final AbstractItemModel model)
	{
		assertThat(modelService.isUpToDate(model)).isTrue();
		assertThat(modelService.isNew(model)).isFalse();
		assertThat(modelService.isRemoved(model)).isFalse();
	}

	@Test
	public void shouldThrowExceptionWhenMandatoryReferencedModelIsAlreadyRemoved() throws Exception
	{
		// given
		final TestItemType2Model itemTypeTwo = createAndSaveItemTypeTwo("foo");
		removeItemTypeTwo(itemTypeTwo);

		try
		{
			// when
			final TestItemType3Model itemType3 = modelService.create(TestItemType3Model.class);
			itemType3.setItemTypeTwo(itemTypeTwo);
			itemType3.setItemsTypeTwo(Collections.EMPTY_LIST);
			modelService.save(itemType3);
			fail("Should throw ModelSavingException");
		}
		catch (final ModelSavingException e)
		{
			// then
			assertThat(e.getCause()).isInstanceOf(InterceptorException.class);
			assertThat(e.getMessage()).contains("missing values for [itemTypeTwo]");
		}
	}

	@Test
	public void shouldThrowExceptionWhenMandatoryModelAsPartOfReferencedCollectionIsAlreadyRemoved() throws Exception
	{
		// given
		final TestItemType2Model itemTypeTwo1 = createAndSaveItemTypeTwo("foo1");
		final TestItemType2Model itemTypeTwo2 = createAndSaveItemTypeTwo("foo2");
		final TestItemType2Model itemTypeTwo3 = createAndSaveItemTypeTwo("foo3");
		removeItemTypeTwo(itemTypeTwo2);
		removeItemTypeTwo(itemTypeTwo3);

		try
		{
			// when
			final TestItemType3Model itemType3 = modelService.create(TestItemType3Model.class);
			itemType3.setItemTypeTwo(itemTypeTwo1);
			itemType3.setItemsTypeTwo(Lists.newArrayList(itemTypeTwo2, itemTypeTwo3));
			modelService.save(itemType3);
			fail("Should throw ModelSavingException");
		}
		catch (final ModelSavingException e)
		{
			// then
			assertThat(e.getCause()).isInstanceOf(InterceptorException.class);
			assertThat(e.getMessage()).contains("missing values for [itemsTypeTwo]");
		}
	}

	private TestItemType2Model createAndSaveItemTypeTwo(final String fooValue)
	{
		final TestItemType2Model itemType2 = modelService.create(TestItemType2Model.class);
		itemType2.setFoo(fooValue);
		modelService.save(itemType2);

		return itemType2;
	}

	private void removeItemTypeTwo(final TestItemType2Model itemType2)
	{
		modelService.remove(itemType2);
		assertThat(modelService.isRemoved(itemType2)).isTrue();
	}
}
