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
package de.hybris.platform.servicelayer.internal.model.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.UniqueAttributesInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.Transactional;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


@IntegrationTest
public class DefaultModelServiceIntegrationTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private TypeService typeService;

	@Before
	public void setUp() throws Exception
	{
		getOrCreateLanguage("de");
		getOrCreateLanguage("en");
	}


	@Test
	@Transactional
	public void testSaveAllInTx() throws JaloInvalidParameterException, JaloSecurityException
	{
		testSaveAll();
	}

	@Test
	public void testSaveAll() throws JaloInvalidParameterException, JaloSecurityException
	{
		final UnitModel u1 = new UnitModel();
		u1.setCode("foo1");
		u1.setUnitType("bar");

		final UnitModel u2 = new UnitModel();
		u2.setCode("foo2");
		u2.setUnitType("bar");

		final UnitModel u3 = new UnitModel();
		u3.setCode("foo3");
		u3.setUnitType("bar");

		modelService.saveAll(Arrays.asList(u1, u2, u3));

		Unit src1 = modelService.getSource(u1);

		assertSame(u1, modelService.get(src1));

		assertNotNull(src1);
		assertEquals(u1.getPk(), src1.getPK());
		assertEquals(u1.getCode(), src1.getCode());
		assertEquals("foo1", src1.getCode());
		assertEquals(u1.getUnitType(), src1.getUnitType());

		Unit src2 = modelService.getSource(u2);
		assertNotNull(src2);
		assertEquals(u2.getCode(), src2.getCode());
		assertEquals("foo2", src2.getCode());
		assertEquals(u2.getUnitType(), src2.getUnitType());

		Unit src3 = modelService.getSource(u3);
		assertNotNull(src3);
		assertEquals(u3.getCode(), src3.getCode());
		assertEquals("foo3", src3.getCode());
		assertEquals(u3.getUnitType(), src3.getUnitType());

		// now test saving via context

		final UnitModel u4 = modelService.create(UnitModel.class);
		u4.setCode("foo4");
		u4.setUnitType("bar");

		final UnitModel u5 = new UnitModel();
		u5.setCode("foo5");
		u5.setUnitType("bar");

		modelService.attach(u5);

		u2.setCode("foo2-2");
		u3.setUnitType("bar-3");

		modelService.saveAll();

		src1 = modelService.getSource(u1);
		assertNotNull(src1);
		assertEquals(u1.getCode(), src1.getCode());
		assertEquals("foo1", src1.getCode());
		assertEquals(u1.getUnitType(), src1.getUnitType());

		src2 = modelService.getSource(u2);
		assertNotNull(src2);
		assertEquals(u2.getCode(), src2.getCode());
		assertEquals("foo2-2", src2.getCode());
		assertEquals(u2.getUnitType(), src2.getUnitType());

		src3 = modelService.getSource(u3);
		assertNotNull(src3);
		assertEquals(u3.getCode(), src3.getCode());
		assertEquals("foo3", src3.getCode());
		assertEquals(u3.getUnitType(), src3.getUnitType());
		assertEquals("bar-3", src3.getUnitType());

		final Unit src4 = modelService.getSource(u4);
		assertNotNull(src4);
		assertEquals(u4.getCode(), src4.getCode());
		assertEquals("foo4", src4.getCode());
		assertEquals(u4.getUnitType(), src4.getUnitType());

		final Unit src5 = modelService.getSource(u5);
		assertNotNull(src5);
		assertEquals(u5.getCode(), src5.getCode());
		assertEquals("foo5", src5.getCode());
		assertEquals(u5.getUnitType(), src5.getUnitType());

		for (int i = 0; i < 5; i++)
		{
			assertSame(u1, modelService.get(src1));
		}

		// test cycles

		final CustomerModel c = modelService.create(CustomerModel.class);
		c.setUid("max");
		c.setDescription("hallo");

		final AddressModel ad1 = modelService.create(AddressModel.class);
		ad1.setOwner(c);
		ad1.setStreetname("1111");

		final AddressModel ad2 = modelService.create(AddressModel.class);
		ad2.setOwner(c);
		ad2.setStreetname("2222");

		final AddressModel ad3 = modelService.create(AddressModel.class);
		ad3.setOwner(c);
		ad3.setStreetname("3333");

		c.setAddresses(Arrays.asList(ad1, ad2, ad3));
		c.setDefaultPaymentAddress(ad1);
		c.setDefaultShipmentAddress(ad2);

		modelService.save(c);

		final Customer cSrc = modelService.getSource(c);
		assertNotNull(cSrc);
		assertEquals(c.getUid(), cSrc.getUID());
		assertEquals("max", cSrc.getUID());
		assertEquals(c.getDescription(), cSrc.getDescription());
		assertEquals("hallo", cSrc.getDescription());

		final Address a1Src = modelService.getSource(ad1);
		assertNotNull(a1Src);
		assertEquals(cSrc, a1Src.getUser());
		assertEquals(cSrc, a1Src.getOwner());
		assertEquals(ad1.getStreetname(), a1Src.getAttribute(Address.STREETNAME));
		assertEquals("1111", a1Src.getAttribute(Address.STREETNAME));

		final Address a2Src = modelService.getSource(ad2);
		assertNotNull(a2Src);
		assertEquals(cSrc, a2Src.getUser());
		assertEquals(cSrc, a2Src.getOwner());
		assertEquals(ad2.getStreetname(), a2Src.getAttribute(Address.STREETNAME));
		assertEquals("2222", a2Src.getAttribute(Address.STREETNAME));

		final Address a3Src = modelService.getSource(ad3);
		assertNotNull(a3Src);
		assertEquals(cSrc, a3Src.getUser());
		assertEquals(cSrc, a3Src.getOwner());
		assertEquals(ad3.getStreetname(), a3Src.getAttribute(Address.STREETNAME));
		assertEquals("3333", a3Src.getAttribute(Address.STREETNAME));

		assertEquals(new HashSet(Arrays.asList(a1Src, a2Src, a3Src)), new HashSet(cSrc.getAllAddresses()));
		assertEquals(a1Src, cSrc.getDefaultPaymentAddress());
		assertEquals(a2Src, cSrc.getDefaultDeliveryAddress());

		final ProductModel base = modelService.create(ProductModel.class);
		base.setCode("base");

	}

	@Ignore("TODO: PLA-7687")
	@Test
	// YTODO PLA-7687
	public void testReferenceSaving() throws JaloInvalidParameterException, JaloSecurityException
	{
		final CustomerModel u = new CustomerModel();
		u.setUid("horst");
		final AddressModel adr = new AddressModel();
		adr.setOwner(u);
		adr.setTown("munich");
		u.setAddresses(Collections.singletonList(adr));
		u.setDefaultPaymentAddress(adr);

		modelService.save(u);

		final Customer uItem = modelService.getSource(u);
		assertNotNull(uItem);
		assertEquals("horst", uItem.getUID());

		final Address aItem = modelService.getSource(adr);
		assertNotNull(aItem);
		assertEquals("munich", aItem.getAttribute(Address.TOWN));
		assertEquals(new HashSet(Collections.singleton(aItem)), new HashSet(uItem.getAllAddresses()));
		assertEquals(aItem, uItem.getDefaultPaymentAddress());
		assertNull(aItem.getAttribute(Address.STREETNAME));

		u.getDefaultPaymentAddress().setStreetname("test");

		modelService.save(u);

		assertEquals("test", aItem.getAttribute(Address.STREETNAME));
	}


	@Test
	@Transactional
	public void testPLA7495InTx()
	{
		testPLA7495();
	}

	/**
	 * PLA-7495
	 */
	@Test
	public void testPLA7495()
	{
		final UserModel userModel1 = modelService.create(UserModel.class);
		userModel1.setUid("test");
		final UserModel userModel2 = modelService.create(UserModel.class);
		userModel2.setUid("test");
		modelService.save(userModel1);
		try
		{
			modelService.save(userModel2);
			fail("ModelSavingException expected because two users with same uid where created");
		}
		catch (final ModelSavingException e) //NOPMD
		{
			//OK
		}

		final UserModel userModel3 = modelService.create(UserModel.class);
		userModel3.setUid("test2");
		final UserModel userModel4 = modelService.create(UserModel.class);
		userModel4.setUid("test2");
		modelService.attach(userModel3);
		modelService.attach(userModel4);
		try
		{
			modelService.saveAll();
			fail("ModelSavingException expected because two users with same uid where created");
		}
		catch (final ModelSavingException e) //NOPMD
		{
			// OK
		}
	}


	@Test
	@Transactional
	public void testPLA7495NoSearchableUIDInTx()
	{
		testPLA7495NoSearchableUID();
	}

	@Test
	public void testPLA7495NoSearchableUID()
	{

		final ComposedType principalType = TypeManager.getInstance().getComposedType(Principal.class);
		final AttributeDescriptor uidAd = principalType.getAttributeDescriptor(Principal.UID);
		final boolean searchableBefore = uidAd.isSearchable();

		try
		{
			uidAd.setSearchable(false);
			final UserModel userModel1 = modelService.create(UserModel.class);
			userModel1.setUid("testOne");

			final UserModel userModel2 = modelService.create(UserModel.class);
			userModel2.setUid("testTwo");

			//
			try
			{
				modelService.save(userModel1);
				modelService.save(userModel2);
				fail("ModelSavingException expected because searching with no searchable attribute " + User.UID);
			}
			catch (final ModelSavingException e) //NOPMD
			{
				//OK
			}

			final UserModel userModel3 = modelService.create(UserModel.class);
			userModel3.setUid("test3");
			final UserModel userModel4 = modelService.create(UserModel.class);
			userModel4.setUid("test4");
			modelService.attach(userModel3);
			modelService.attach(userModel4);
			try
			{
				modelService.saveAll();
				fail("ModelSavingException expected because searching with no searchable attribute " + User.UID);
			}
			catch (final ModelSavingException e) //NOPMD
			{
				// OK
			}
		}
		finally
		{
			uidAd.setSearchable(searchableBefore);
		}
	}

	// Please note that this test doesn't work inside a transaction since unique attributes
	// are cached globally and only invalidated upon actual transaction commit!
	@Test
	public void testUniqueKeyInterceptor()
	{
		final ComposedType unitType = TypeManager.getInstance().getComposedType(Unit.class);

		final AttributeDescriptor codeAd = unitType.getAttributeDescriptor(Unit.CODE);
		final AttributeDescriptor typeAd = unitType.getAttributeDescriptor(Unit.UNITTYPE);

		final boolean codeBefore = codeAd.isUnique();
		final boolean typeBefore = typeAd.isUnique();

		try
		{
			typeAd.setUnique(false);
			codeAd.setUnique(false);

			assertEquals(Collections.EMPTY_SET, typeService.getUniqueAttributes("Unit"));

			final UnitModel u1 = modelService.create(UnitModel.class);
			u1.setCode("foo");
			u1.setUnitType("bar");

			final UnitModel u2 = modelService.create(UnitModel.class);
			u2.setCode("foo");
			u2.setUnitType("bar");

			modelService.saveAll(Arrays.asList(u1, u2)); // should be possible since there are no unique attributes

			typeAd.setUnique(true);
			codeAd.setUnique(true);

			assertEquals(new HashSet<String>(Arrays.asList(Unit.CODE, Unit.UNITTYPE)), typeService.getUniqueAttributes("Unit"));

			// test ctx check
			final UnitModel u3 = modelService.create(UnitModel.class);
			u3.setCode("hallo");
			u3.setUnitType("du");

			final UnitModel u4 = modelService.create(UnitModel.class);
			u4.setCode("hallo");
			u4.setUnitType("du");

			try
			{
				modelService.saveAll(Arrays.asList(u3, u4));
				fail("exepcetd " + ModelSavingException.class.getSimpleName());
			}
			catch (final ModelSavingException e)
			{
				assertNotNull(e.getCause());
				assertTrue(e.getCause() instanceof InterceptorException);
				assertNotNull(((InterceptorException) e.getCause()).getInterceptor());
				assertTrue(((InterceptorException) e.getCause()).getInterceptor() instanceof UniqueAttributesInterceptor);
			}

			// test database check
			modelService.save(u3); // should be find
			try
			{
				modelService.save(u4);
				fail("exepcetd " + ModelSavingException.class.getSimpleName());
			}
			catch (final ModelSavingException e)
			{
				assertNotNull(e.getCause());
				assertTrue(e.getCause() instanceof InterceptorException);
				assertNotNull(((InterceptorException) e.getCause()).getInterceptor());
				assertTrue(((InterceptorException) e.getCause()).getInterceptor() instanceof UniqueAttributesInterceptor);
			}
		}
		finally
		{
			codeAd.setUnique(codeBefore);
			typeAd.setUnique(typeBefore);
		}
	}

	@Test
	@Transactional
	public void testCurrencyCreateParamsInTx() throws JaloGenericCreationException, JaloAbstractTypeException
	{
		testCurrencyCreateParams();
	}

	@Test
	public void testCurrencyCreateParams() throws JaloGenericCreationException, JaloAbstractTypeException
	{
		final Map attrs = new HashMap();
		final Item zdm;
		attrs.put(Currency.SYMBOL, "&&&&&");
		attrs.put(Currency.ISOCODE, "3");
		final ComposedType type = JaloSession.getCurrentSession().getTenant().getJaloConnection().getTypeManager()
				.getComposedType("Currency");
		zdm = type.newInstance(jaloSession.getSessionContext(), attrs);
		final ItemModel modelItem = modelService.create("Currency");
		Assert.assertTrue(modelItem instanceof CurrencyModel);
		((CurrencyModel) modelItem).setSymbol("&&&&&&");
		((CurrencyModel) modelItem).setIsocode("4");
		modelService.save(modelItem);
		assertEquals(zdm.getComposedType(), ((Item) modelService.getSource(modelItem)).getComposedType());
	}

	@Test
	@Transactional
	public void testSetAttributeValuesTestInTx()
	{
		testSetAttributeValuesTest();
	}

	@Test
	public void testSetAttributeValuesTest()
	{
		new CoreBasicDataCreator().createRootMediaFolder();
		final ImpExMediaModel newMedia = modelService.create(ImpExMediaModel.class);
		modelService.initDefaults(newMedia);
		modelService.save(newMedia);
		assertNotNull(newMedia);
		modelService.setAttributeValue(newMedia, "removeOnSuccess", Boolean.TRUE);
		modelService.setAttributeValue(newMedia, "linesToSkip", Integer.valueOf(100));
		assertTrue(newMedia.isRemoveOnSuccess());
		assertEquals(100, newMedia.getLinesToSkip());
	}

	@Test
	@Transactional
	public void testSetAttributeLocalizedWithSessionLangInTx()
	{
		testSetAttributeLocalizedWithSessionLang();
	}

	@Test
	public void testSetAttributeLocalizedWithSessionLang()
	{
		jaloSession.getSessionContext().setLanguage(getOrCreateLanguage("de"));
		final TitleModel lang = modelService.create(TitleModel.class);
		modelService.setAttributeValue(lang, TitleModel.CODE, "bla");
		modelService.setAttributeValue(lang, TitleModel.NAME,
				Collections.singletonMap(modelService.get(getOrCreateLanguage("de")), "bla"));
		modelService.save(lang);
		assertEquals("bla", lang.getName(Locale.GERMANY));
	}

	@Test
	@Transactional
	public void testSetAttributeLocalizedWithoutSessionLangInTx()
	{
		testSetAttributeLocalizedWithoutSessionLang();
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenModelIsNullWhenGettingAttributeValueInGenericWay() throws Exception
	{
		try
		{
			// when
			modelService.getAttributeValue(null, TitleModel.NAME);
			fail("should throw NullPointerException");
		}
		catch (final NullPointerException e)
		{
			// then
			assertThat(e).hasMessage("model is required to obtain a value");
		}
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenAttributeQualifierlIsNullWhenGettingAttributeValueInGenericWay()
			throws Exception
	{
		try
		{
			// when
			modelService.getAttributeValue(new TitleModel(), null);
			fail("should throw NullPointerException");
		}
		catch (final NullPointerException e)
		{
			// then
			assertThat(e).hasMessage("attributeQualifier is required to obtain a value");
		}
	}

	@Test
	public void shouldReturnLocalizedValueForAttributeInGenericWay() throws Exception
	{
		// given
		final Map<Locale, String> names = new HashMap<>();
		names.put(Locale.ENGLISH, "fooEN");
		names.put(Locale.GERMAN, "fooDE");
		final TitleModel title = createTitle("foo", names);

		// when
		final String value = modelService.getAttributeValue(title, TitleModel.NAME, Locale.GERMAN);

		// then
		assertThat(value).isNotNull().isEqualTo("fooDE");
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenValueForGivenLocaleCannotBeFoundWhenGettingLocalizedAttributeValueInGenericWay()
			throws Exception
	{
		// given
		final Map<Locale, String> names = new HashMap<>();
		names.put(Locale.ENGLISH, "fooEN");
		names.put(Locale.GERMAN, "fooDE");
		final TitleModel title = createTitle("foo", names);

		try
		{
			// when
			modelService.getAttributeValue(title, TitleModel.NAME, Locale.CHINESE);
			fail("should throw IllegalArgumentException");
		}
		catch (final IllegalArgumentException e)
		{
			// then fine
		}
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenModelIsNullWhenGettingLocalizedAttributeValueInGenericWay() throws Exception
	{
		try
		{
			// when
			modelService.getAttributeValue(null, TitleModel.NAME, Locale.GERMAN);
			fail("should throw NullPointerException");
		}
		catch (final NullPointerException e)
		{
			// then
			assertThat(e).hasMessage("model is required to obtain a value");
		}
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenAttributeQualifierlIsNullWhenGettingLocalizedAttributeValueInGenericWay()
			throws Exception
	{
		try
		{
			// when
			modelService.getAttributeValue(new TitleModel(), null, Locale.GERMAN);
			fail("should throw NullPointerException");
		}
		catch (final NullPointerException e)
		{
			// then
			assertThat(e).hasMessage("attributeQualifier is required to obtain a value");
		}
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenLocalelIsNullWhenGettingLocalizedAttributeValueInGenericWay() throws Exception
	{
		try
		{
			// when
			modelService.getAttributeValue(new TitleModel(), TitleModel.NAME, null);
			fail("should throw NullPointerException");
		}
		catch (final NullPointerException e)
		{
			// then
			assertThat(e).hasMessage("locale is required to obtain a value");
		}
	}

	@Test
	public void shouldReturnMapOfLocalizedValuesForGivenLocalesForAttributeInGenericWay() throws Exception
	{
		// given
		final Map<Locale, String> names = new HashMap<>();
		names.put(Locale.ENGLISH, "fooEN");
		names.put(Locale.GERMAN, "fooDE");
		final TitleModel title = createTitle("foo", names);

		// when
		final Map<Locale, Object> values = modelService.getAttributeValues(title, TitleModel.NAME, Locale.ENGLISH, Locale.GERMAN);

		// then
		assertThat(values).isNotNull().isNotEmpty().hasSize(2);
		assertThat(values.get(Locale.GERMAN)).isNotNull().isEqualTo("fooDE");
		assertThat(values.get(Locale.ENGLISH)).isNotNull().isEqualTo("fooEN");
	}

	@Test
	public void mapOfLocalizedValuesForGivenLocalesObtainedInGenericWayShouldBeImmutable() throws Exception
	{
		// given
		final Map<Locale, String> names = new HashMap<>();
		names.put(Locale.ENGLISH, "fooEN");
		names.put(Locale.GERMAN, "fooDE");
		final TitleModel title = createTitle("foo", names);
		final Map<Locale, Object> values = modelService.getAttributeValues(title, TitleModel.NAME, Locale.ENGLISH, Locale.GERMAN);

		try
		{
			// when
			values.put(Locale.FRENCH, "NewValue");
			fail("should throw UnsupportedOperationException");
		}
		catch (final UnsupportedOperationException e)
		{
			// then fine
		}
	}

	@Test
	public void shouldReturnEmptyMapOfLocalizedValuesForGivenLocalesForAttributeInGenericWayWhenNonOfTheValuesCanBeObtained()
			throws Exception
	{
		// given
		final Map<Locale, String> names = new HashMap<>();
		names.put(Locale.ENGLISH, "fooEN");
		names.put(Locale.GERMAN, "fooDE");
		final TitleModel title = createTitle("foo", names);

		// when
		final Map<Locale, Object> values = modelService.getAttributeValues(title, TitleModel.NAME, Locale.ITALIAN, Locale.CHINESE);

		// then
		assertThat(values).isNotNull().isEmpty();
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenModelIsNullWhenGettingMapOfLocalizedAttributeValuesInGenericWay()
			throws Exception
	{
		try
		{
			// when
			modelService.getAttributeValues(null, TitleModel.NAME, Locale.GERMAN, Locale.ENGLISH);
			fail("should throw NullPointerException");
		}
		catch (final NullPointerException e)
		{
			// then
			assertThat(e).hasMessage("model is required to obtain a value");
		}
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenAttributeIsNullWhenGettingMapOfLocalizedAttributeValuesInGenericWay()
			throws Exception
	{
		try
		{
			// when
			modelService.getAttributeValues(new TitleModel(), null, Locale.GERMAN, Locale.ENGLISH);
			fail("should throw NullPointerException");
		}
		catch (final NullPointerException e)
		{
			// then
			assertThat(e).hasMessage("attributeQualifier is required to obtain a value");
		}
	}

	@Test
	public void shouldThrowNullPointerExceptionWhenLocalesArrayIsNulWhenGettingMapOfLocalizedAttributeValuesInGenericWay()
			throws Exception
	{
		try
		{
			// when
			modelService.getAttributeValues(new TitleModel(), TitleModel.NAME, null);
			fail("should throw NullPointerException");
		}
		catch (final NullPointerException e)
		{
			// then
			assertThat(e).hasMessage("locales cannot be null");
		}
	}

	@Test
	public void shouldSetLocalizedValueForAttributeInGenericWay() throws Exception
	{
		// given
		final Map<Locale, String> names = new HashMap<>();
		names.put(Locale.ENGLISH, "fooEN");
		final TitleModel title = createTitle("foo", names);
		final Map<Locale, String> germanNames = new HashMap<>();
		germanNames.put(Locale.GERMAN, "fooDE");

		// when
		modelService.setAttributeValue(title, Title.NAME, germanNames);

		// then
		assertThat(title.getName(Locale.GERMAN)).isNotNull().isEqualTo("fooDE");
	}

	private TitleModel createTitle(final String code, final Map<Locale, String> names)
	{
		final TitleModel mContainer = modelService.create(TitleModel.class);
		mContainer.setCode(code);

		for (final Map.Entry<Locale, String> entry : names.entrySet())
		{
			final Locale locale = entry.getKey();
			final String name = entry.getValue();
			mContainer.setName(name, locale);
		}

		modelService.save(mContainer);

		return mContainer;
	}


	@Test
	public void testSetAttributeLocalizedWithoutSessionLang()
	{
		jaloSession.getSessionContext().setLanguage(null);

		final Map<Locale, Object> params = new HashMap<Locale, Object>();
		params.put(Locale.GERMANY, "locvalueA");
		params.put(Locale.UK, "locvalueB");

		final TitleModel lang = modelService.create(TitleModel.class);
		modelService.setAttributeValue(lang, TitleModel.CODE, "bla");
		modelService.setAttributeValue(lang, TitleModel.NAME, params);
		modelService.save(lang);

		assertEquals("locvalueA", lang.getName(Locale.GERMANY));
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void testSetAttributeNonWritableInTx()
	{
		testSetAttributeNonWritable();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetAttributeNonWritable()
	{
		final TitleModel lang = modelService.create(TitleModel.class);
		modelService.setAttributeValue(lang, TitleModel.CODE, "foopa");
		modelService.setAttributeValue(lang, TitleModel.PK, PK.createFixedUUIDPK(0, System.currentTimeMillis()));
	}

	/**
	 * Waits until semaphore list is empty (when onEmpty is true) or is not empty when (when onEmpty is false).
	 */
	private void waitOn(final List semaphore, final boolean onEmpty)
	{
		while (true)
		{
			synchronized (semaphore)
			{
				if ((!semaphore.isEmpty()) ^ onEmpty)
				{
					return;
				}
			}
			try
			{
				Thread.sleep(100);
			}
			catch (final InterruptedException e)
			{
				Thread.currentThread().interrupt();
				return;
			}
		}
	}

	private class MyTestThread extends RegistrableThread
	{
		private final Tenant tenant;
		private final ModelService modelService;
		private final List<Boolean> semaphore;

		private final PK pkToLock;

		public MyTestThread(final Tenant tenant, final ModelService modelService, final List<Boolean> semaphore, final PK pkToLock)
		{
			super();
			this.tenant = tenant;
			this.modelService = modelService;
			this.semaphore = semaphore;
			this.pkToLock = pkToLock;
		}

		@Override
		public void internalRun()
		{
			//platform initialization in thread
			Registry.setCurrentTenant(tenant);


			final Transaction tx = Transaction.current();
			tx.begin();
			try
			{
				//setup semaphore
				synchronized (semaphore)
				{
					semaphore.add(Boolean.FALSE);
				}

				//locking of item
				modelService.lock(pkToLock);

				//setup semaphore
				synchronized (semaphore)
				{
					semaphore.add(Boolean.FALSE);
				}

				waitOn(semaphore, true);
			}
			finally
			{
				tx.rollback();
			}

			synchronized (semaphore)
			{
				//publish information about finishing transaction
				semaphore.add(Boolean.FALSE);
			}
		}

	}

	@Test
	public void testLockPK()
	{
		//no locking on HSQL
		if (Config.isHSQLDBUsed())
		{
			return;
		}

		//initialization
		final TitleModel lang = modelService.create(TitleModel.class);
		lang.setCode("testCode");
		modelService.save(lang);
		final List<Boolean> semaphore = Collections.synchronizedList((new ArrayList<Boolean>()));
		final Transaction transaction = Transaction.current();



		//external Thread
		final Thread tread = new MyTestThread(Registry.getCurrentTenant(), modelService, semaphore, lang.getPk());
		transaction.begin();
		try
		{
			//we do the lock
			modelService.lock(lang.getPk());

			//start external thread
			tread.start();
			//thread try to locks the same record but stops 
			waitOn(semaphore, false);
			//to be sure that thread reach lock
			Thread.sleep(100);


			synchronized (semaphore)
			{
				//only one flag on semaphore 
				if (semaphore.size() != 1)
				{
					fail();
				}

				semaphore.clear();
			}

		}
		catch (final InterruptedException e)
		{
			Thread.currentThread().interrupt();
			fail();
		}
		finally
		{
			transaction.rollback();
		}

		waitOn(semaphore, false);
		//thread did lock

		synchronized (semaphore)
		{
			//release thread
			semaphore.clear();
		}
	}
}
