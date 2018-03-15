/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.audit;


import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.tuple;

import de.hybris.platform.audit.internal.config.AtomicAttribute;
import de.hybris.platform.audit.internal.config.AuditConfigService;
import de.hybris.platform.audit.internal.config.AuditReportConfig;
import de.hybris.platform.audit.internal.config.ReferenceAttribute;
import de.hybris.platform.audit.internal.config.RelationAttribute;
import de.hybris.platform.audit.internal.config.ResolvesBy;
import de.hybris.platform.audit.internal.config.Type;
import de.hybris.platform.audit.internal.config.VirtualAttribute;
import de.hybris.platform.audit.internal.config.validation.AuditReportConfigValidatorTest;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.audit.AuditType;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.seed.TestDataCreator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Condition;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.groups.Tuple;
import org.assertj.core.presentation.StandardRepresentation;


public class AuditTestHelper
{
	private final Collection<Object> models = new HashSet<>();

	public UserModel prepareTestDataForIntegrationTest() throws InterruptedException
	{
		final ModelService modelService = getModelService();

		final TestDataCreator creator = new TestDataCreator(modelService);
		final UserModel user1 = creator.createUser("adam", "Adam");
		models.add(user1);
		final TitleModel title1 = creator.createTitle("Mr", "Mister");
		models.add(title1);
		final TitleModel title2 = creator.createTitle("Engr.", "Engineer");
		models.add(title2);

		final AddressModel address1 = creator.createAddress("Sosnowiec", "Moniuszki", user1);
		models.add(address1);
		final AddressModel address2 = creator.createAddress("Tokyo", "Konichiwa", user1);
		models.add(address2);
		final AddressModel address3 = creator.createAddress("New York", "55th St.", user1);
		models.add(address3);

		address1.setTitle(title1);
		address2.setTitle(title2);
		address3.setTitle(title1);

		user1.setDefaultPaymentAddress(address1);
		user1.setDefaultShipmentAddress(address3);

		final MediaModel testMedia = getModelService().create(CatalogUnawareMediaModel.class);
		models.add(testMedia);

		testMedia.setCode("nice profile picture of me");
		user1.setProfilePicture(testMedia);

		modelService.save(title1);
		modelService.save(title2);
		modelService.save(address1);
		modelService.save(address2);
		modelService.save(address3);
		modelService.save(testMedia);
		modelService.save(user1);

		//create historical data
		final WriteAuditGateway readAuditRecordsDAO = getWriteAuditGateway();
		readAuditRecordsDAO.removeAuditRecordsForType("User");
		readAuditRecordsDAO.removeAuditRecordsForType("Address");
		readAuditRecordsDAO.removeAuditRecordsForType("CatalogUnawareMedia");
		readAuditRecordsDAO.removeAuditRecordsForType("Title");

		user1.setName("SomeBetterNameForUser");
		modelService.save(user1);

		Thread.sleep(50);

		address1.setStreetname("Chopina");
		modelService.save(address1);

		testMedia.setCode("ugly picture of me");
		modelService.save(testMedia);

		// some additional not-referenced address to test filtering
		final UserModel user2 = creator.createUser("tom", "Tommy");
		models.add(user2);
		final AddressModel address4 = creator.createAddress("Warsaw", "Krakowskie Przedmiescie", user2);
		models.add(address4);
		address4.setStreetnumber("1");
		modelService.save(address4);

		//additional address which was referenced in the past
		final TitleModel historicalTitle = creator.createTitle("Sir", "Sir");
		models.add(historicalTitle);

		final AddressModel historicalAddress = modelService.create(AddressModel.class);
		models.add(historicalAddress);
		historicalAddress.setTitle(historicalTitle);
		historicalAddress.setTown("Krakow");
		historicalAddress.setStreetname("Rynek");
		historicalAddress.setOwner(user1);
		modelService.save(historicalAddress);

		modelService.remove(historicalTitle);
		modelService.remove(historicalAddress);

		modelService.remove(address3);
		return user1;
	}

	public AuditReportConfig createTestConfigForIntegrationTest()
	{
		final Type title = Type.builder().withCode("Title") //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier("code").build(), //
						AtomicAttribute.builder().withQualifier("name").build() //
		) //
				.build();

		final Type address = Type.builder() //
				.withCode("Address") //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier("town").build(), //
						AtomicAttribute.builder().withQualifier("streetname").build() //
		) //
				.withReferenceAttributes( //
						ReferenceAttribute.builder().withQualifier("title").withType(title).withResolvesBy( //
								ResolvesBy.builder().withResolverBeanId("typeReferencesResolver").withExpression("title").build() //
		).build() //
		).build();

		final Type media = Type.builder().withCode("Media") //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier("code").build() //
		) //
				.build();

		// this is handling of PrincipalGroupRelation and seems to be working fine
		final Type userGroup = Type.builder().withCode(UserGroupModel._TYPECODE).build();

		final Type user = Type.builder().withCode(UserModel._TYPECODE) //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier("uid").build(), //
						AtomicAttribute.builder().withQualifier("name").build() //
		) //
				.withReferenceAttributes( //
						ReferenceAttribute.builder().withQualifier("defaultPaymentAddress").withType(address)
								.withResolvesBy( //
										ResolvesBy.builder().withResolverBeanId("typeReferencesResolver")
												.withExpression("defaultPaymentAddress").build())
								.build(), //
						ReferenceAttribute.builder().withQualifier("defaultShipmentAddress").withType(address)
								.withResolvesBy( //
										ResolvesBy.builder().withResolverBeanId("typeReferencesResolver")
												.withExpression("defaultShipmentAddress").build())
								.build(), //
						ReferenceAttribute.builder().withQualifier("profilepicture").withType(media)
								.withResolvesBy( //
										ResolvesBy.builder().withResolverBeanId("typeReferencesResolver").withExpression("profilepicture")
												.build())
								.build() //
		) //
				.withRelationAttributes( //
						RelationAttribute.builder().withTargetType(userGroup).withRelation("PrincipalGroupRelation")
								.withResolvesBy( //
										ResolvesBy.builder().withResolverBeanId("manyToManyReferencesResolver").build())
								.build()) //
				.build();

		final AuditReportConfig reportConfig = AuditReportConfig.builder() //
				.withGivenRootType(user) //
				.withName("testConfig") //
				.withAuditRecordsProvider("auditRecordsProvider").withTypes(user, address, title, media, userGroup) //
				.build();

		return reportConfig;
	}

	public AuditReportConfig createTestConfigWithVirtualAttributeForIntegrationTest()
	{
		final Type title = Type.builder().withCode("Title") //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier("code").build(), //
						AtomicAttribute.builder().withQualifier("name").build() //
		) //
				.build();

		final Type address = Type.builder() //
				.withCode("Address") //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier("town").build(), //
						AtomicAttribute.builder().withQualifier("streetname").build() //
		) //
				.withReferenceAttributes( //
						ReferenceAttribute.builder().withQualifier("title").withType(title).withResolvesBy( //
								ResolvesBy.builder().withResolverBeanId("typeReferencesResolver").withExpression("title").build() //
		).build() //
		).build();

		final Type media = Type.builder().withCode("Media") //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier("code").build() //
		) //
				.build();

		// this is handling of PrincipalGroupRelation and seems to be working fine
		final Type userGroup = Type.builder().withCode(UserGroupModel._TYPECODE).build();

		final Type user = Type.builder().withCode(UserModel._TYPECODE) //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier("uid").build(), //
						AtomicAttribute.builder().withQualifier("name").build() //
		) //
				.withReferenceAttributes( //
						ReferenceAttribute.builder().withQualifier("profilepicture").withType(media)
								.withResolvesBy( //
										ResolvesBy.builder().withResolverBeanId("typeReferencesResolver").withExpression("profilepicture")
												.build())
								.build() //
		) //
				.withVirtualAttributes( //
						VirtualAttribute.builder().withExpression("ownedAddresses").withMany(Boolean.TRUE).withType(address)
								.withResolvesBy( //
										ResolvesBy.builder().withExpression("owner").withResolverBeanId("virtualReferencesResolver").build() //
		).build() //
		) //
				.withRelationAttributes( //
						RelationAttribute.builder().withTargetType(userGroup).withRelation("PrincipalGroupRelation")
								.withResolvesBy( //
										ResolvesBy.builder().withResolverBeanId("manyToManyReferencesResolver").build())
								.build()) //
				.build();

		final AuditReportConfig reportConfig = AuditReportConfig.builder() //
				.withGivenRootType(user) //
				.withName("testConfig") //
				.withAuditRecordsProvider("auditRecordsProvider").withTypes(user, address, title, media, userGroup) //
				.build();

		return reportConfig;

	}

	public void clearAuditDataForTypes(final String... types)
	{
		final WriteAuditGateway writeAuditGateway = getWriteAuditGateway();
		for (final String type : types)
		{
			writeAuditGateway.removeAuditRecordsForType(type);
		}
	}


	private static ModelService getModelService()
	{
		return Registry.getApplicationContext().getBean("modelService", ModelService.class);
	}

	private static WriteAuditGateway getWriteAuditGateway()
	{
		return Registry.getApplicationContext().getBean("writeAuditGateway", WriteAuditGateway.class);
	}

	public static Map<String, Object> getAuditRecordsAttributes(final AuditRecord auditRecord1, final String langIsoCode)
	{
		if (auditRecord1.getAuditType() == AuditType.DELETION || auditRecord1.getAuditType() == AuditType.CURRENT)
		{
			return auditRecord1.getAttributesBeforeOperation(langIsoCode);
		}
		return auditRecord1.getAttributesAfterOperation(langIsoCode);
	}

	public static Map<String, Object> getAuditRecordsAttributes(final AuditRecord auditRecord1)
	{
		if (auditRecord1.getAuditType() == AuditType.DELETION || auditRecord1.getAuditType() == AuditType.CURRENT)
		{
			return auditRecord1.getAttributesBeforeOperation();
		}
		return auditRecord1.getAttributesAfterOperation();
	}

	public static Condition<List<? extends Map<String, Object>>> noDuplicatedReportEntries()
	{
		return new DuplicatedEntriesCondition();
	}

	public <T> T createItem(final Class<T> itemClass)
	{
		final T item = getModelService().create(itemClass);
		models.add(item);
		return item;
	}

	public <T> T createItem(final Supplier<T> supplier)
	{
		final T item = supplier.get();
		models.add(item);
		return item;
	}

	public void removeCreatedItems()
	{
		getModelService().removeAll(models);
	}

	public static Function<Map<String, Object>, Object> extractingRecursiveMapAttribute(final String... attributePath)
	{
		return stringObjectMap -> {
			Map<String, Object> current = stringObjectMap;
			for (int i = 0; i < attributePath.length; i++)
			{
				if (current == null)
				{
					return null;
				}

				final String attribute = attributePath[i];
				if (i == attributePath.length - 1)
				{
					return current.get(attribute);
				}
				else
				{
					current = (Map<String, Object>) current.get(attribute);
				}
			}
			return null;
		};
	}

	@SafeVarargs
	public static Tuple extractRecursiveMapAttributes(final Map<String, Object> title, final List<String>... attributePaths)
	{
		final List<Object> values = new ArrayList<>();

		for (final List<String> attributePath : attributePaths)
		{
			Map<String, Object> current = title;
			for (int i = 0; i < attributePath.size(); i++)
			{
				final String attribute = attributePath.get(i);
				if (i == attributePath.size() - 1)
				{
					values.add(current.get(attribute));
				}
				else
				{
					current = (Map<String, Object>) current.get(attribute);
				}
			}
		}

		return tuple(values.toArray());
	}


	public static <T extends AuditRecord> List<T> sortRecords(final List<T> auditRecords)
	{
		final List<T> sortedAuditRecords = new ArrayList<>(auditRecords);
		sortedAuditRecords.sort(Comparator.comparing(AuditRecord::getAuditType, AuditTestHelper::compareAuditType)
				.thenComparing(AuditRecord::getTimestamp).thenComparing(AuditRecord::getVersion));

		return sortedAuditRecords;
	}

	private static int compareAuditType(final AuditType o1, final AuditType o2)
	{
		if (o1 != o2)
		{
			if (o1 == AuditType.CURRENT)
			{
				return 1;
			}
			else if (o2 == AuditType.CURRENT)
			{
				return -1;
			}
		}
		return 0;
	}

	private static class DuplicatedEntriesCondition extends Condition<List<? extends Map<String, Object>>>
	{

		private List<Map<String, Object>> expected;


		@Override
		public Description description()
		{
			return new TextDescription(expected.toString());
		}

		@Override
		public boolean matches(final List<? extends Map<String, Object>> value)
		{
			if (expected != null)
			{
				throw new IllegalStateException("this DuplcaitedEntriesCondition instance can't be reused");
			}


			final LinkedList<Map<String, Object>> reduced = new LinkedList<>();

			for (final Map<String, Object> map : value)
			{
				if (reduced.isEmpty() || !Objects.equals(reduced.getLast(), map))
				{
					reduced.add(map);
				}
			}

			expected = Collections.unmodifiableList(reduced);

			return Objects.equals(Integer.valueOf(expected.size()), Integer.valueOf(value.size()));
		}

		@Override
		public String toString()
		{
			return new StandardRepresentation().toStringOf(expected);
		}
	}

	public AuditReportConfig loadConfigFromFile(final String file, final String configName)
	{
		try (InputStream resourceAsStream = AuditReportConfigValidatorTest.class.getClassLoader().getResourceAsStream(file))
		{
			final String xml = IOUtils.toString(resourceAsStream, UTF_8);

			final AuditConfigService auditConfigService = getAuditConfigService();
			auditConfigService.storeConfiguration(configName, xml);
			return auditConfigService.getConfigForName(configName);
		}
		catch (final IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static AuditConfigService getAuditConfigService()
	{
		return Registry.getApplicationContext().getBean(AuditConfigService.class);
	}
}
