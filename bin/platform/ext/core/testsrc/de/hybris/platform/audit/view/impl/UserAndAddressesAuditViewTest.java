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
package de.hybris.platform.audit.view.impl;

import static de.hybris.platform.audit.AuditTestHelper.noDuplicatedReportEntries;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import de.hybris.platform.audit.AuditTestHelper;
import de.hybris.platform.audit.AuditableTest;
import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.demo.AuditTestConfigManager;
import de.hybris.platform.audit.internal.config.AtomicAttribute;
import de.hybris.platform.audit.internal.config.AuditReportConfig;
import de.hybris.platform.audit.internal.config.ReferenceAttribute;
import de.hybris.platform.audit.internal.config.ResolvesBy;
import de.hybris.platform.audit.internal.config.Type;
import de.hybris.platform.audit.internal.config.VirtualAttribute;
import de.hybris.platform.audit.provider.AuditRecordsProvider;
import de.hybris.platform.audit.view.AuditViewService;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import de.hybris.platform.persistence.audit.gateway.ReadAuditGateway;
import de.hybris.platform.persistence.audit.gateway.AuditSearchQuery;
import de.hybris.platform.persistence.audit.internal.AuditEnablementService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.assertj.core.groups.Tuple;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


public class UserAndAddressesAuditViewTest extends ServicelayerBaseTest implements AuditableTest
{
	@Resource
	protected WriteAuditGateway writeAuditGateway;
	@Resource
	protected ReadAuditGateway readAuditGateway;
	@Resource
	private ModelService modelService;
	@Resource
	private AuditViewService auditViewService;

	private AuditTestConfigManager auditTestConfigManager;

	@Resource
	private AuditRecordsProvider auditRecordsProvider;
	@Resource(name = "auditingEnablementService")
	private AuditEnablementService auditEnablementService;

	private AuditTestHelper auditTestHelper;

	@Before
	public void setUp() throws Exception
	{
		assumeAuditEnabled();
		auditTestConfigManager = new AuditTestConfigManager(auditEnablementService);
		assumeAuditEnabled();
		auditTestConfigManager.enableAuditingForTypes("User", "Title", "Address");

		auditTestHelper = new AuditTestHelper();
		auditTestHelper.clearAuditDataForTypes("User", "Title", "Address");
	}

	@After
	public void tearDown() throws Exception
	{
		auditTestHelper.removeCreatedItems();
	}

	@Test
	public void shouldProduceAuditViewWithDeletedAddressWithProperOrderWithVirtualAttribute()
	{
		final UserModel user = auditTestHelper.createItem(UserModel.class);
		user.setUid("user1a");
		user.setName("user1");
		user.setDescription("My user description");
		modelService.save(user);

		final AddressModel address = auditTestHelper.createItem(AddressModel.class);
		address.setStreetname("street");
		address.setStreetnumber("1");
		address.setOwner(user);
		modelService.save(address);

		modelService.remove(address);

		final AddressModel address2 = auditTestHelper.createItem(AddressModel.class);
		address2.setStreetname("newstreet");
		address2.setStreetnumber("2");
		address2.setOwner(user);
		modelService.save(address2);


		final AuditReportConfig testAuditReportConfig = createConfigWithVirtualAttribute();
		final List<ReportView> reportViews = auditViewService.getViewOn(TypeAuditReportConfig.builder()
				.withConfig(testAuditReportConfig).withRootTypePk(user.getPk()).withFullReport().build()).collect(toList());

		assertThat(reportViews) //
				.extracting(ReportView::getPayload) //
				.has(noDuplicatedReportEntries()) //
				.extracting("User") //
				.extracting(o1 -> extractVirtualReference((Map<String, Object>) o1, "ownedAddresses")) //
				.extracting(maps -> extractAttributesFromEntityList(maps, "streetname", "streetnumber")) //
				.containsSubsequence( //
						tuples(tuple(null, null)), //
						tuples(tuple("street", "1")), //
						tuples(tuple(null, null)), //
						tuples(tuple("newstreet", "2")) //
		);
	}


	@Test
	public void shouldProduceAuditViewWithMoreDeletedAddressWithProperOrderWithVirtualAttribute()
	{
		final UserModel user = auditTestHelper.createItem(UserModel.class);
		user.setUid("user1b");
		user.setName("user1");
		user.setDescription("My user description");
		modelService.save(user);

		final AddressModel address = auditTestHelper.createItem(AddressModel.class);
		address.setStreetname("street");
		address.setStreetnumber("1");
		address.setOwner(user);
		modelService.save(address);

		modelService.remove(address);

		final AddressModel address2 = auditTestHelper.createItem(AddressModel.class);
		address2.setStreetname("newstreet");
		address2.setStreetnumber("2");
		address2.setOwner(user);
		modelService.save(address2);

		modelService.remove(address2);

		final AddressModel address3 = auditTestHelper.createItem(AddressModel.class);
		address3.setStreetname("some new street");
		address3.setStreetnumber("3");
		address3.setOwner(user);
		modelService.save(address3);

		modelService.remove(address3);


		final AuditReportConfig testAuditReportConfig = createConfigWithVirtualAttribute();
		final List<ReportView> reportViews = auditViewService.getViewOn(TypeAuditReportConfig.builder()
				.withConfig(testAuditReportConfig).withRootTypePk(user.getPk()).withFullReport().build()).collect(toList());

		for (final ReportView reportView : reportViews)
		{
			System.out.println(reportView.getPayload() + " " + reportView.getTimestamp().getTime());
		}

		assertThat(reportViews) //
				.extracting(ReportView::getPayload) //
				.has(noDuplicatedReportEntries()) //
				.extracting("User") //
				.extracting(o1 -> extractVirtualReference((Map<String, Object>) o1, "ownedAddresses")) //
				.extracting(maps -> extractAttributesFromEntityList(maps, "streetname", "streetnumber")) //
				.containsSubsequence( //
						tuples(tuple(null, null)), //
						tuples(tuple("street", "1")), //
						tuples(tuple(null, null)), //
						tuples(tuple("newstreet", "2")), //
						tuples(tuple(null, null)), //
						tuples(tuple("some new street", "3")), //
						tuples(tuple(null, null)));
	}

	@Test
	public void shouldProduceAuditViewWithModifiedAddressWithProperOrderWithVirtualAttribute()
	{
		final UserModel user = auditTestHelper.createItem(UserModel.class);
		user.setUid("user1c");
		user.setName("user1");
		user.setDescription("My user description");
		modelService.save(user);

		final AddressModel address = auditTestHelper.createItem(AddressModel.class);
		address.setStreetname("street");
		address.setStreetnumber("1");
		address.setOwner(user);
		modelService.save(address);

		address.setStreetname("otherstreet");
		address.setStreetnumber("1b");
		modelService.save(address);


		final AddressModel address2 = auditTestHelper.createItem(AddressModel.class);
		address2.setStreetname("newstreet");
		address2.setStreetnumber("2");
		address2.setOwner(user);
		modelService.save(address2);



		final AuditReportConfig testAuditReportConfig = createConfigWithVirtualAttribute();
		final List<ReportView> reportViews = auditViewService.getViewOn(TypeAuditReportConfig.builder()
				.withConfig(testAuditReportConfig).withRootTypePk(user.getPk()).withFullReport().build()).collect(toList());

		for (final ReportView reportView : reportViews)
		{
			System.out.println(reportView.getPayload() + " " + reportView.getTimestamp().getTime());
		}

		assertThat(reportViews) //
				.extracting(ReportView::getPayload) //
				.has(noDuplicatedReportEntries()) //
				.extracting("User") //
				.extracting(o1 -> extractVirtualReference((Map<String, Object>) o1, "ownedAddresses")) //
				.extracting(maps -> extractAttributesFromEntityList(maps, "streetname", "streetnumber")).containsSubsequence( //
						tuples(tuple(null, null)), //
						tuples(tuple("street", "1")), //
						tuples(tuple("otherstreet", "1b")), //
						tuples(tuple("otherstreet", "1b"), tuple("newstreet", "2")) //
		);
	}

	@Test
	public void shouldProduceAuditViewWithDeletedAddressWithProperOrder()
	{
		final UserModel user = auditTestHelper.createItem(UserModel.class);
		user.setUid("user1d");
		user.setName("user1");
		user.setDescription("My user description");
		modelService.save(user);
		//{User={uid=user1, name=user1}}

		final AddressModel address = auditTestHelper.createItem(AddressModel.class);
		address.setStreetname("street");
		address.setStreetnumber("1");
		address.setOwner(user);
		modelService.save(address);

		user.setDefaultPaymentAddress(address);
		modelService.save(user);
		//{User={defaultpaymentaddress={streetname=street, streetnumber=1}, uid=user1, name=user1}}

		modelService.remove(address);
		//{User={uid=user1, name=user1}}

		final AddressModel address2 = auditTestHelper.createItem(AddressModel.class);
		address2.setStreetname("newstreet");
		address2.setStreetnumber("2");
		address2.setOwner(user);
		modelService.save(address2);

		user.setDefaultPaymentAddress(address2);
		modelService.save(user);
		//{User={defaultpaymentaddress={streetname=newstreet, streetnumber=2}, uid=user1, name=user1}}


		final AuditReportConfig testAuditReportConfig = createConfig();
		final List<ReportView> reportViews = auditViewService.getViewOn(TypeAuditReportConfig.builder()
				.withConfig(testAuditReportConfig).withRootTypePk(user.getPk()).withFullReport().build()).collect(toList());

		for (final ReportView reportView : reportViews)
		{
			System.out.println(reportView.getPayload() + " " + reportView.getTimestamp().getTime());
		}

		final TypeAuditReportConfig config = TypeAuditReportConfig.builder().withConfig(testAuditReportConfig)
				.withRootTypePk(user.getPk()).build();

		final List<AuditRecord> records = auditRecordsProvider.getRecords(config).collect(toList());
		System.out.println("!!!!! " + records.size());


		final List<AuditRecord> addressAuditRecords = readAuditGateway
				.search(AuditSearchQuery.forType(AddressModel._TYPECODE).build()).collect(toList());
		assertThat(addressAuditRecords).isNotNull().hasSize(3);

		assertThat(reportViews) //
				.extracting(ReportView::getPayload) //
				.has(noDuplicatedReportEntries()) //
				.extracting("User") //
				.extracting(o1 -> extractDirectReference((Map<String, Object>) o1, "defaultPaymentAddress")) //
				.extracting("streetname", "streetnumber") //
				.containsSubsequence(tuple(null, null), //
						tuple("street", "1"), //
						tuple(null, null), //
						tuple("newstreet", "2") //
		);
	}

	@Test
	public void shouldProduceAuditViewWithMoreDeletedAddressWithProperOrder()
	{
		final UserModel user = auditTestHelper.createItem(UserModel.class);
		user.setUid("user1e");
		user.setName("user1");
		user.setDescription("My user description");
		modelService.save(user);

		final AddressModel address = auditTestHelper.createItem(AddressModel.class);
		address.setStreetname("street");
		address.setStreetnumber("1");
		address.setOwner(user);
		modelService.save(address);

		user.setDefaultPaymentAddress(address);
		modelService.save(user);

		modelService.remove(address);

		final AddressModel address2 = auditTestHelper.createItem(AddressModel.class);
		address2.setStreetname("newstreet");
		address2.setStreetnumber("2");
		address2.setOwner(user);
		modelService.save(address2);

		user.setDefaultPaymentAddress(address2);
		modelService.save(user);

		modelService.remove(address2);

		final AddressModel address3 = auditTestHelper.createItem(AddressModel.class);
		address3.setStreetname("some new street");
		address3.setStreetnumber("3");
		address3.setOwner(user);
		modelService.save(address3);

		user.setDefaultPaymentAddress(address3);
		modelService.save(user);

		modelService.remove(address3);


		final AuditReportConfig testAuditReportConfig = createConfig();
		final List<ReportView> reportViews = auditViewService.getViewOn(TypeAuditReportConfig.builder()
				.withConfig(testAuditReportConfig).withRootTypePk(user.getPk()).withFullReport().build()).collect(toList());

		for (final ReportView reportView : reportViews)
		{
			System.out.println(reportView.getPayload() + " " + reportView.getTimestamp().getTime());
		}
		assertThat(reportViews) //
				.extracting(ReportView::getPayload) //
				.has(noDuplicatedReportEntries()) //
				.extracting("User") //
				.extracting(o1 -> extractDirectReference((Map<String, Object>) o1, "defaultPaymentAddress")) //
				.extracting("streetname", "streetnumber") //
				.containsSubsequence( //
						tuple(null, null), //
						tuple("street", "1"), //
						tuple(null, null), //
						tuple("newstreet", "2"), //
						tuple(null, null), //
						tuple("some new street", "3"), //
						tuple(null, null));
	}

	@Test
	public void shouldProduceAuditViewWithModifiedAddressWithProperOrder()
	{
		final UserModel user = auditTestHelper.createItem(UserModel.class);
		user.setUid("user1f");
		user.setName("user1");
		user.setDescription("My user description");
		modelService.save(user);
		//{User={uid=user1, name=user1}}

		final AddressModel address = auditTestHelper.createItem(AddressModel.class);
		address.setStreetname("street");
		address.setStreetnumber("1");
		address.setOwner(user);
		modelService.save(address);

		user.setDefaultPaymentAddress(address);
		modelService.save(user);
		//{User={defaultpaymentaddress={streetname=street, streetnumber=1}

		address.setStreetname("otherstreet");
		address.setStreetnumber("2");
		modelService.save(address);
		//{User={defaultpaymentaddress={streetname=otherstreet, streetnumber=2}


		final AddressModel address2 = auditTestHelper.createItem(AddressModel.class);
		address2.setStreetname("some new street");
		address2.setStreetnumber("3");
		address2.setOwner(user);
		modelService.save(address2);

		user.setDefaultPaymentAddress(address2);
		modelService.save(user);
		//{User={defaultpaymentaddress={streetname=some new street, streetnumber=3}

		modelService.remove(address2);
		//{User={uid=user1, name=user1}}


		final AuditReportConfig testAuditReportConfig = createConfig();
		final TypeAuditReportConfig config = TypeAuditReportConfig.builder().withConfig(testAuditReportConfig)
				.withRootTypePk(user.getPk()).withFullReport().build();
		final List<ReportView> reportViews = auditViewService.getViewOn(config).collect(toList());

		for (final ReportView reportView : reportViews)
		{
			System.out.println(reportView.getPayload() + " " + reportView.getTimestamp().getTime());
		}


		assertThat(reportViews) //
				.extracting(ReportView::getPayload) //
				.has(noDuplicatedReportEntries()) //
				.extracting("User") //
				.extracting(o1 -> extractDirectReference((Map<String, Object>) o1, "defaultPaymentAddress")) //
				.extracting("streetname", "streetnumber") //
				.containsSubsequence( //
						tuple(null, null), //
						tuple("street", "1"), //
						tuple("otherstreet", "2"), //
						tuple("some new street", "3"), //
						tuple(null, null));

	}

	@Test
	public void shouldProduceAuditViewWithDeletedAddressWithProperOrderWithMixedAttributes()
	{
		final UserModel user = auditTestHelper.createItem(UserModel.class);
		user.setUid("user1g");
		user.setName("user1");
		user.setDescription("My user description");
		modelService.save(user); // 1st

		final AddressModel address = auditTestHelper.createItem(AddressModel.class);
		address.setStreetname("street");
		address.setStreetnumber("1");
		address.setOwner(user);
		modelService.save(address); // 2nd

		user.setDefaultPaymentAddress(address);
		modelService.save(user); // 3rd

		modelService.remove(address); // 4th

		final AddressModel address2 = auditTestHelper.createItem(AddressModel.class);
		address2.setStreetname("newstreet");
		address2.setStreetnumber("2");
		address2.setOwner(user);
		modelService.save(address2); // 5th

		user.setDefaultPaymentAddress(address2);
		modelService.save(user); // 6th


		final AuditReportConfig testAuditReportConfig = createConfigWithMixedAttributes();
		final List<ReportView> reportViews = auditViewService.getViewOn(TypeAuditReportConfig.builder()
				.withConfig(testAuditReportConfig).withRootTypePk(user.getPk()).withFullReport().build()).collect(toList());

		for (final ReportView reportView : reportViews)
		{
			System.out.println(reportView.getPayload() + " " + reportView.getTimestamp().getTime());
		}

		assertThat(reportViews) //
				.extracting(ReportView::getPayload) //
				.has(noDuplicatedReportEntries()) //
				.extracting("User") //
				.extracting(o1 -> extractDirectReference((Map<String, Object>) o1, "defaultPaymentAddress")) //
				.extracting("streetname", "streetnumber") //
				.containsSubsequence( //
						tuple(null, null), //
						tuple(null, null), //
						tuple("street", "1"), //
						tuple(null, null), //
						tuple(null, null), //
						tuple("newstreet", "2") //
		);


		assertThat(reportViews) //
				.extracting(ReportView::getPayload).extracting("User") //
				.extracting(o1 -> extractVirtualReference((Map<String, Object>) o1, "ownedAddresses")) //
				.extracting(maps -> extractAttributesFromEntityList(maps, "streetname", "streetnumber")) //
				.containsSubsequence( //
						tuples(tuple(null, null)), //
						tuples(tuple("street", "1")), //
						tuples(tuple("street", "1")), //
						tuples(tuple(null, null)), //
						tuples(tuple("newstreet", "2")), //
						tuples(tuple("newstreet", "2")) //
		);
	}

	@Test
	public void shouldProduceAuditViewWithMoreDeletedAddressWithProperOrderWithMixedAttributes()
	{
		final UserModel user = auditTestHelper.createItem(UserModel.class);
		user.setUid("user1h");
		user.setName("user1");
		user.setDescription("My user description");
		modelService.save(user); // 1st

		final AddressModel address = auditTestHelper.createItem(AddressModel.class);
		address.setStreetname("street");
		address.setStreetnumber("1");
		address.setOwner(user);
		modelService.save(address); // 2nd

		user.setDefaultPaymentAddress(address);
		modelService.save(user); // 3rd

		modelService.remove(address); // 4th

		final AddressModel address2 = auditTestHelper.createItem(AddressModel.class);
		address2.setStreetname("newstreet");
		address2.setStreetnumber("2");
		address2.setOwner(user);
		modelService.save(address2); // 5th

		user.setDefaultPaymentAddress(address2);
		modelService.save(user); // 6th

		modelService.remove(address2); // 7th

		final AddressModel address3 = auditTestHelper.createItem(AddressModel.class);
		address3.setStreetname("some new street");
		address3.setStreetnumber("3");
		address3.setOwner(user);
		modelService.save(address3); // 8th

		user.setDefaultPaymentAddress(address3);
		modelService.save(user); // 9th

		modelService.remove(address3); // 10th


		final AuditReportConfig testAuditReportConfig = createConfigWithMixedAttributes();
		final List<ReportView> reportViews = auditViewService.getViewOn(TypeAuditReportConfig.builder()
				.withConfig(testAuditReportConfig).withRootTypePk(user.getPk()).withFullReport().build()).collect(toList());

		for (final ReportView reportView : reportViews)
		{
			System.out.println(reportView.getPayload() + " " + reportView.getTimestamp().getTime());
		}

		assertThat(reportViews) //
				.extracting(ReportView::getPayload) //
				.has(noDuplicatedReportEntries()) //
				.extracting("User") //
				.extracting(o1 -> extractDirectReference((Map<String, Object>) o1, "defaultPaymentAddress")) //
				.extracting("streetname", "streetnumber") //
				.containsSubsequence( //
						tuple(null, null), //
						tuple(null, null), //
						tuple("street", "1"), //
						tuple(null, null), //
						tuple(null, null), //
						tuple("newstreet", "2"), //
						tuple(null, null), //
						tuple(null, null), //
						tuple("some new street", "3"), //
						tuple(null, null) //
		);


		assertThat(reportViews) //
				.extracting(ReportView::getPayload).extracting("User") //
				.extracting(o1 -> extractVirtualReference((Map<String, Object>) o1, "ownedAddresses")) //
				.extracting(maps -> extractAttributesFromEntityList(maps, "streetname", "streetnumber")) //
				.containsSubsequence( //
						tuples(tuple(null, null)), //
						tuples(tuple("street", "1")), //
						tuples(tuple("street", "1")), //
						tuples(tuple(null, null)), //
						tuples(tuple("newstreet", "2")), //
						tuples(tuple("newstreet", "2")), //
						tuples(tuple(null, null)), //
						tuples(tuple("some new street", "3")), //
						tuples(tuple("some new street", "3")) //
		);
	}

	private AuditReportConfig createConfigWithVirtualAttribute()
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
						AtomicAttribute.builder().withQualifier("streetnumber").build(), //
						AtomicAttribute.builder().withQualifier("streetname").build() //
				) //
				.withReferenceAttributes( //
						ReferenceAttribute.builder().withQualifier("title").withType(title).withResolvesBy( //
								ResolvesBy.builder().withResolverBeanId("typeReferencesResolver").withExpression("title").build() //
						).build() //
				).build();

		final Type user = Type.builder().withCode("User") //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier("uid").build(), //
						AtomicAttribute.builder().withQualifier("name").build() //
				) //
				.withVirtualAttributes( //
						VirtualAttribute.builder().withExpression("ownedAddresses").withMany(Boolean.TRUE).withType(address)
								.withResolvesBy( //
										ResolvesBy.builder().withExpression("owner").withResolverBeanId("virtualReferencesResolver").build() //
								).build()).build();

		final AuditReportConfig reportConfig = AuditReportConfig.builder() //
				.withGivenRootType(user) //
				.withName("testConfig") //
				.withAuditRecordsProvider("auditRecordsProvider").withTypes(user, address, title) //
				.build();

		return reportConfig;
	}

	private AuditReportConfig createConfig()
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
						AtomicAttribute.builder().withQualifier("streetnumber").build(), //
						AtomicAttribute.builder().withQualifier("streetname").build() //
				) //
				.withReferenceAttributes( //
						ReferenceAttribute.builder().withQualifier("title").withType(title).withResolvesBy( //
								ResolvesBy.builder().withResolverBeanId("typeReferencesResolver").withExpression("title").build() //
						).build() //
				).build();

		final Type user = Type.builder().withCode("User") //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier("uid").build(), //
						AtomicAttribute.builder().withQualifier("name").build() //
				) //
				.withReferenceAttributes( //
						ReferenceAttribute.builder().withQualifier("defaultPaymentAddress").withType(address).withResolvesBy( //
								ResolvesBy.builder().withResolverBeanId("typeReferencesResolver").withExpression("defaultPaymentAddress")
										.build())
								.build() //
				).build();//

		final AuditReportConfig reportConfig = AuditReportConfig.builder() //
				.withGivenRootType(user) //
				.withName("testConfig") //
				.withAuditRecordsProvider("auditRecordsProvider").withTypes(user, address, title) //
				.build();

		return reportConfig;
	}

	private AuditReportConfig createConfigWithMixedAttributes()
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
						AtomicAttribute.builder().withQualifier("streetnumber").build(), //
						AtomicAttribute.builder().withQualifier("streetname").build() //
				) //
				.withReferenceAttributes( //
						ReferenceAttribute.builder().withQualifier("title").withType(title).withResolvesBy( //
								ResolvesBy.builder().withResolverBeanId("typeReferencesResolver").withExpression("title").build() //
						).build() //
				).build();

		final Type user = Type.builder().withCode("User") //
				.withAtomicAttributes( //
						AtomicAttribute.builder().withQualifier("uid").build(), //
						AtomicAttribute.builder().withQualifier("name").build() //
				) //
				.withVirtualAttributes( //
						VirtualAttribute.builder().withExpression("ownedAddresses").withMany(Boolean.TRUE).withType(address)
								.withResolvesBy( //
										ResolvesBy.builder().withExpression("owner").withResolverBeanId("virtualReferencesResolver").build() //
								).build() //
				) //
				.withReferenceAttributes( //
						ReferenceAttribute.builder().withQualifier("defaultPaymentAddress").withType(address).withResolvesBy( //
								ResolvesBy.builder().withResolverBeanId("typeReferencesResolver").withExpression("defaultPaymentAddress")
										.build())
								.build() //
				).build();//

		final AuditReportConfig reportConfig = AuditReportConfig.builder() //
				.withGivenRootType(user) //
				.withName("testConfig") //
				.withAuditRecordsProvider("auditRecordsProvider").withTypes(user, address, title) //
				.build();

		return reportConfig;
	}

	private Set<Tuple> extractAttributesFromEntityList(final List<Map<String, Object>> maps, final String... attributes)
	{
		return maps.stream().map(o -> tuple(Arrays.stream(attributes).map(o::get).collect(toList()).toArray())).collect(toSet());
	}

	private Set tuples(final Tuple... tuples)
	{
		return Sets.newHashSet(tuples);
	}

	private Object extractDirectReference(final Map<String, Object> o1, final String defaultpaymentaddress)
	{
		return o1.getOrDefault(defaultpaymentaddress, Collections.emptyMap());
	}

	private List<Map<String, Object>> extractVirtualReference(final Map<String, Object> o1, final String attribute)
	{
		final List<Map<String, Object>> ownedAddresses = (List<Map<String, Object>>) o1.getOrDefault(attribute,
				Collections.emptyList());
		if (ownedAddresses.isEmpty())
		{
			return Collections.singletonList(Collections.emptyMap());
		}
		return ownedAddresses;
	}
}
