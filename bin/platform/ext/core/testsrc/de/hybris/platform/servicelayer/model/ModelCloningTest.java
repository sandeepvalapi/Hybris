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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaContainer;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.RelationDescriptor;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelCloningContext;
import de.hybris.platform.testframework.HybrisJUnit4Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ModelCloningTest extends HybrisJUnit4Test
{
	@Resource
	private ModelService modelService;

	@Before
	public void setUp()
	{
		if (modelService == null)
		{
			modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		}
	}

	@Test
	public void testSetup()
	{
		assertNotNull(modelService);
	}

	@Test
	public void testCloneUser()
	{
		final CountryModel country = modelService.create(CountryModel.class);
		country.setIsocode("XXX");
		country.setActive(Boolean.TRUE);

		final UserModel userModel = modelService.create(UserModel.class);
		userModel.setUid("foo");
		userModel.setName("Foo Bar");
		userModel.setSessionLanguage((LanguageModel) modelService.get(jaloSession.getSessionContext().getLanguage()));

		final AddressModel adr1 = modelService.create(AddressModel.class);
		adr1.setOwner(userModel);
		adr1.setStreetname("Foostreet");
		adr1.setStreetnumber("11a");
		adr1.setTown("Bartown");
		adr1.setCountry(country);

		final AddressModel adr2 = modelService.create(AddressModel.class);
		adr2.setOwner(userModel);
		adr2.setStreetname("Trallal");
		adr2.setStreetnumber("888");
		adr2.setTown("Bartown");
		adr2.setCountry(country);

		userModel.setAddresses(Arrays.asList(adr1, adr2));
		userModel.setDefaultPaymentAddress(adr1);
		userModel.setDefaultShipmentAddress(adr2);

		modelService.saveAll(Arrays.asList(country, userModel));

		assertNotNull(userModel.getDefaultPaymentAddress());

		final UserModel uClone = modelService.clone(userModel);
		assertNotNull(uClone);
		assertTrue(modelService.isNew(uClone));
		assertNotSame(userModel, uClone);
		assertEquals(userModel.getUid(), uClone.getUid());
		assertEquals(userModel.getName(), uClone.getName());
		assertSame(userModel.getSessionLanguage(), uClone.getSessionLanguage());

		final AddressModel adr1Clone = uClone.getDefaultPaymentAddress();
		assertNotNull(adr1Clone);
		assertTrue(modelService.isNew(adr1Clone));
		assertNotSame(adr1, adr1Clone);
		assertEquals(uClone, adr1Clone.getOwner());
		assertEquals(adr1.getStreetname(), adr1Clone.getStreetname());
		assertEquals(adr1.getStreetnumber(), adr1Clone.getStreetnumber());
		assertEquals(adr1.getTown(), adr1Clone.getTown());

		final AddressModel adr2Clone = uClone.getDefaultShipmentAddress();
		assertNotNull(adr2Clone);
		assertTrue(modelService.isNew(adr2Clone));
		assertNotSame(adr2, adr2Clone);
		assertEquals(uClone, adr2Clone.getOwner());
		assertEquals(adr2.getStreetname(), adr2Clone.getStreetname());
		assertEquals(adr2.getStreetnumber(), adr2Clone.getStreetnumber());
		assertEquals(adr2.getTown(), adr2Clone.getTown());

		try
		{
			modelService.save(uClone);
			fail("ModelSavingException expected");
		}
		catch (final ModelSavingException e)
		{
			// fine
		}

		uClone.setUid("foo(2)");

		modelService.save(uClone);

		assertFalse(modelService.isNew(uClone));
		assertNotNull(modelService.getSource(uClone));

		assertFalse(modelService.isNew(adr1Clone));
		assertNotNull(modelService.getSource(adr1Clone));

		assertFalse(modelService.isNew(adr2Clone));
		assertNotNull(modelService.getSource(adr2Clone));

		// YTODO test localized attributes
	}

	@Test
	public void testIgnoreOneToMany()
	{
		final CatalogModel cat = modelService.create(CatalogModel.class);
		cat.setId("cat");

		final CatalogVersionModel catalogVersionModel = modelService.create(CatalogVersionModel.class);
		catalogVersionModel.setVersion("cv");
		catalogVersionModel.setCatalog(cat);

		final MediaFormatModel format1 = new MediaFormatModel();
		format1.setQualifier("format1");

		final MediaFormatModel format2 = new MediaFormatModel();
		format2.setQualifier("format2");

		final MediaFormatModel format3 = new MediaFormatModel();
		format3.setQualifier("format3");

		final MediaModel mediaModel1 = modelService.create(MediaModel.class);
		mediaModel1.setCatalogVersion(catalogVersionModel);
		mediaModel1.setCode("m1");
		mediaModel1.setMediaFormat(format1);

		final MediaModel mediaModel2 = modelService.create(MediaModel.class);
		mediaModel2.setCatalogVersion(catalogVersionModel);
		mediaModel2.setCode("m2");
		mediaModel2.setMediaFormat(format2);

		final MediaModel mediaModel3 = modelService.create(MediaModel.class);
		mediaModel3.setCatalogVersion(catalogVersionModel);
		mediaModel3.setCode("m3");
		mediaModel3.setMediaFormat(format3);

		final MediaContainerModel cont = new MediaContainerModel();
		cont.setCatalogVersion(catalogVersionModel);
		cont.setQualifier("cont");
		cont.setMedias(Arrays.asList(mediaModel1, mediaModel2, mediaModel3));

		modelService.saveAll(cat, catalogVersionModel, mediaModel1, mediaModel2, mediaModel3, cont);

		assertEquals(new HashSet<MediaModel>(Arrays.asList(mediaModel1, mediaModel2, mediaModel3)), new HashSet<MediaModel>(cont.getMedias()));

		final MediaContainer jaloCont = modelService.getSource(cont);
		final Media jaloM1 = modelService.getSource(mediaModel1);
		final Media jaloM2 = modelService.getSource(mediaModel2);
		final Media jaloM3 = modelService.getSource(mediaModel3);

		assertEquals(new HashSet<Media>(Arrays.asList(jaloM1, jaloM2, jaloM3)), new HashSet<Media>(jaloCont.getMedias()));

		assertFalse(modelService.isNew(cont));
		assertFalse(modelService.isNew(mediaModel1));
		assertFalse(modelService.isNew(mediaModel2));
		assertFalse(modelService.isNew(mediaModel3));

		final AttributeDescriptor attributeDescriptor = jaloCont.getComposedType().getAttributeDescriptorIncludingPrivate(MediaContainer.MEDIAS);
		assertNotNull(attributeDescriptor);
		assertTrue(attributeDescriptor instanceof RelationDescriptor);
		assertTrue(((RelationDescriptor) attributeDescriptor).getRelationType().isOneToMany());
		assertFalse(attributeDescriptor.isProperty());
		assertFalse(attributeDescriptor.isPartOf());

		final MediaContainerModel clone = modelService.clone(cont);

		// check if medias have been omitted
		assertNull(clone.getMedias());
		assertEquals(new HashSet<MediaModel>(Arrays.asList(mediaModel1, mediaModel2, mediaModel3)), new HashSet<MediaModel>(cont.getMedias()));

		// give new qualifier to avoid unique key issues
		clone.setQualifier("cont clone");

		// persist
		modelService.save(clone);

		// check if source item still got its medias
		assertEquals(new HashSet<Media>(Arrays.asList(jaloM1, jaloM2, jaloM3)), new HashSet<Media>(jaloCont.getMedias()));
		modelService.refresh(cont);
		assertEquals(new HashSet<MediaModel>(Arrays.asList(mediaModel1, mediaModel2, mediaModel3)), new HashSet<MediaModel>(cont.getMedias()));
	}

	@Test
	public void testCloneRegionAndCountry()
	{
		//new Country
		final CountryModel countryModel = modelService.create(CountryModel.class);
		countryModel.setActive(Boolean.TRUE);
		countryModel.setIsocode("testCountry");

		// new Region add to the Country
		final RegionModel rm1 = modelService.create(RegionModel.class);
		rm1.setIsocode("testRegion1");
		rm1.setCountry(countryModel);

		// new Region add to the Country
		final RegionModel rm2 = modelService.create(RegionModel.class);
		rm2.setIsocode("testRegion2");
		rm2.setCountry(countryModel);

		//save all model until here
		modelService.saveAll();

		//first regions should not be null
		assertNotNull(countryModel.getRegions());

		// first simple test if there are 2 Regions at the Country
		assertEquals(2, countryModel.getRegions().size());

		// Clone the Country which should take the Regions to the new Country because it is partOf relation
		final CountryModel cmCloned = modelService.clone(countryModel);

		//Cloned stuff should not be null
		assertNotNull(cmCloned);
		assertEquals(2, cmCloned.getRegions().size());

		// set the country isocode because it is unique
		cmCloned.setIsocode("testCountryCloned");
		modelService.saveAll(cmCloned);

		//there should be 2 Regions at the cloned Country, too
		assertEquals(2, cmCloned.getRegions().size());
	}

	@Test
	public void testIgnoreMandatorySelfReference()
	{
		// setup
		final UserGroupModel group = modelService.create(UserGroupModel.class);
		group.setUid("testGroup");
		group.setName("Testgroup");
		final AddressModel original = modelService.create(AddressModel.class);
		original.setFirstname("Foo");
		original.setLastname("Bar");
		original.setOwner(group);
		modelService.saveAll(group, original);
		final ModelCloningContext cloneContext = new DefaultModelCloningContext();

		// need a hack
		final Address jaloItem = modelService.getSource(original);
		jaloItem.setOriginal(jaloItem);
		modelService.refresh(original);

		assertEquals(original, original.getOriginal());

		final AddressModel clone = modelService.clone(original, cloneContext);

		assertNull(clone.getOriginal());
	}

	@Test
	public void testOptionalSelfReference()
	{
		// setup
		final UserGroupModel group = modelService.create(UserGroupModel.class);
		group.setUid("testGroup");
		group.setName("Testgroup");
		final AddressModel original = modelService.create(AddressModel.class);
		original.setFirstname("Foo");
		original.setLastname("Bar");
		original.setOwner(group);
		modelService.saveAll(group, original);
		final ModelCloningContext cloneContext = new DefaultModelCloningContext();

		// Please don't be confused about using Address.original - here we just want
		// to demonstrate that a item referencing itself produces clones that also
		// reference them self. This has nothing to do with the original purpose of
		// Address.original!
		final AttributeDescriptor attributeDescriptor = TypeManager.getInstance().getComposedType(Address.class)
				.getAttributeDescriptorIncludingPrivate(Address.ORIGINAL);
		final boolean writableBefore = attributeDescriptor.isWritable();
		try
		{
			attributeDescriptor.setWritable(true);
			final Address jaloItem = modelService.getSource(original);
			jaloItem.setOriginal(jaloItem);
			modelService.refresh(original);

			assertEquals(original, original.getOriginal());

			final AddressModel clone = modelService.clone(original, cloneContext);

			assertEquals(clone, clone.getOriginal());
		}
		finally
		{
			attributeDescriptor.setWritable(writableBefore);
		}
	}

	private CustomerModel createCustomerWithAddress()
	{
		final CustomerModel cust = modelService.create(CustomerModel.class);
		cust.setUid("cust" + System.nanoTime());
		cust.setName("foo");
		cust.setLoginDisabled(true);
		cust.setDescription("Some description");
		final AddressModel addr = modelService.create(AddressModel.class);
		addr.setOwner(cust);
		addr.setTown("town");
		addr.setStreetname("Street");
		cust.setAddresses(Collections.singleton(addr));
		modelService.save(cust);
		assertTrue(modelService.isUpToDate(cust));
		assertFalse(modelService.isNew(cust));
		assertTrue(modelService.isUpToDate(addr));
		assertFalse(modelService.isNew(addr));

		return cust;
	}

	@Test
	public void testCloneWithTargetType()
	{
		final CustomerModel cust = createCustomerWithAddress();
		final AddressModel addr = cust.getAddresses().iterator().next();

		final EmployeeModel empClone = modelService.clone(cust, EmployeeModel.class);
		empClone.setUid("emp" + System.nanoTime());
		modelService.save(empClone);
		assertTrue(modelService.isUpToDate(empClone));
		assertFalse(modelService.isNew(empClone));
		final Collection<AddressModel> copyAddresses = empClone.getAddresses();
		assertEquals(1, copyAddresses.size());
		final AddressModel addrClone = copyAddresses.iterator().next();
		assertTrue(modelService.isUpToDate(addrClone));
		assertFalse(modelService.isNew(addrClone));

		assertEquals(cust.getName(), empClone.getName());
		assertEquals(cust.isLoginDisabled(), empClone.isLoginDisabled());
		assertEquals(cust.getDescription(), empClone.getDescription());

		assertFalse(addr.getPk().equals(addrClone.getPk()));
		assertEquals(addr.getTown(), addrClone.getTown());
		assertEquals(addr.getStreetname(), addrClone.getStreetname());
		assertEquals(cust, addr.getOwner());
		assertEquals(empClone, addrClone.getOwner());
	}

	@Test
	public void testCloneWithTargetTypeAndContext()
	{
		final CustomerModel cust = createCustomerWithAddress();
		final AddressModel addr = cust.getAddresses().iterator().next();
		final EmployeeModel empClone = modelService.clone(cust, EmployeeModel.class, new ModelCloningContext()
		{
			@Override
			public boolean usePresetValue(final Object original, final String qualifier)
			{
				// own values for UID and DESCRIPTION
				return original == cust
						&& (UserModel.UID.equalsIgnoreCase(qualifier) || UserModel.DESCRIPTION.equalsIgnoreCase(qualifier));
			}

			@Override
			public boolean treatAsPartOf(final Object original, final String qualifier)
			{
				return false;
			}

			@Override
			public boolean skipAttribute(final Object original, final String qualifier)
			{
				// dont copy LOGINDISABLED
				return original == cust && (UserModel.LOGINDISABLED.equalsIgnoreCase(qualifier));
			}

			@Override
			public Object getPresetValue(final Object original, final String qualifier)
			{
				// own values for UID and DESCRIPTION
				if (original == cust)
				{
					if (UserModel.UID.equalsIgnoreCase(qualifier))
					{
						return "xxx" + System.nanoTime();
					}
					else if (UserModel.DESCRIPTION.equalsIgnoreCase(qualifier))
					{
						return "---";
					}
				}
				throw new IllegalStateException();
			}
		});
		modelService.save(empClone);

		assertTrue(modelService.isUpToDate(empClone));
		assertFalse(modelService.isNew(empClone));
		final Collection<AddressModel> copyAddresses = empClone.getAddresses();
		assertEquals(1, copyAddresses.size());
		final AddressModel addrClone = copyAddresses.iterator().next();
		assertTrue(modelService.isUpToDate(addrClone));
		assertFalse(modelService.isNew(addrClone));

		assertEquals(cust.getName(), empClone.getName());
		assertFalse(empClone.isLoginDisabled());
		assertEquals("---", empClone.getDescription());
		assertTrue(empClone.getUid().startsWith("xxx"));

		assertFalse(addr.getPk().equals(addrClone.getPk()));
		assertEquals(addr.getTown(), addrClone.getTown());
		assertEquals(addr.getStreetname(), addrClone.getStreetname());
		assertEquals(cust, addr.getOwner());
		assertEquals(empClone, addrClone.getOwner());
	}
}
