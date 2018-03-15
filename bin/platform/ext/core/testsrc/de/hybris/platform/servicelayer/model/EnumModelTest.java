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
package de.hybris.platform.servicelayer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.enums.EncodingEnum;
import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class EnumModelTest extends ServicelayerTransactionalBaseTest
{
	@Resource
	private UserService userService;
	@Resource
	private TypeService typeService;
	@Resource
	private ModelService modelService;
	@Resource
	private I18NService i18nService;

	@Before
	public void setUp() throws Exception
	{
		getOrCreateLanguage("de");
		getOrCreateLanguage("en");
	}

	@Test
	public void testFixedEnums()
	{
		AddressModel addr = new AddressModel();
		modelService.initDefaults(addr);
		addr.setOwner(userService.getCurrentUser());
		addr.setGender(Gender.MALE);
		modelService.save(addr);

		modelService.get(modelService.<Object>getSource(addr));
		assertEquals(Gender.MALE, addr.getGender());
	}

	@Test
	public void testDynamicEnums()
	{
		final ImpExMediaModel media = new ImpExMediaModel();
		media.setCode("testMedia");
		media.setEncoding(EncodingEnum.valueOf("UTF-8"));
		modelService.save(media);

		media.setEncoding(EncodingEnum.valueOf("TEST"));
		modelService.save(media);
	}

	@Test
	public void testDynamicEnumEquals()
	{
		final OrderStatus st1 = OrderStatus.CREATED;
		assertFalse(st1.equals(null)); //NOPMD yes we WANT to do that !!!
		assertFalse(st1.equals("foo"));//NOPMD yes we WANT to do that !!!
		assertFalse(st1.equals(OrderStatus.CANCELLED));
		assertTrue(st1.equals(OrderStatus.CREATED));
		assertTrue(st1.equals(OrderStatus.valueOf("creaTeD")));
	}

	/**
	 * TPL-1215
	 */
	@Test
	public void testTPL1215() throws IOException, ClassNotFoundException
	{
		i18nService.setCurrentLocale(Locale.UK);
		EnumerationValueModel model = typeService.getEnumerationValue(Gender.MALE);
		modelService.save(model);
		i18nService.setCurrentLocale(Locale.GERMAN);

		final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		final ObjectOutputStream outputStream = new ObjectOutputStream(buffer);
		outputStream.writeObject(model);
		outputStream.close();

		final ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
		model = (EnumerationValueModel) inputStream.readObject();
		inputStream.close();

		model.getName();
	}

	@Test
	// see PLA-9616
	public void testCaseInsensitivity() throws Exception
	{
		final OrderStatus cancelled = OrderStatus.CANCELLED;

		assertSame(cancelled, OrderStatus.valueOf("caNceLleD"));

		// We want to be absolutely sure that even if we'd got duplicates
		// with different case codes the will still considered to be the same!
		// Though we need a trick to do that since constructor is actually private !!!
		final OrderStatus illegalDuplicate = createIllegalOrderStatus("canCELLed");
		assertNotNull(illegalDuplicate);
		assertEquals(cancelled.hashCode(), illegalDuplicate.hashCode());
		assertTrue(cancelled.equals(illegalDuplicate));
	}

	private OrderStatus createIllegalOrderStatus(final String code) throws Exception
	{
		final Constructor<OrderStatus> con = OrderStatus.class.getDeclaredConstructor(String.class);
		con.setAccessible(true);
		return con.newInstance(code);
	}
}
