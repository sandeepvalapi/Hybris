package de.hybris.platform.persistence.audit.gateway.impl;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.gateway.AuditSearchQuery;
import de.hybris.platform.persistence.audit.gateway.ReadAuditGateway;
import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.seed.TestDataCreator;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultReadAuditGatewayTest extends ServicelayerBaseTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private ReadAuditGateway readAuditGateway;
	@Resource
	private WriteAuditGateway writeAuditGateway;
	private UserModel user1, user2;

	@Before
	public void setUp() throws Exception
	{
		writeAuditGateway.removeAuditRecordsForType("User");

		final TestDataCreator creator = new TestDataCreator(modelService);
		user1 = creator.createUser("007", "James Bond");
		user1.setName("James Bond 01");
		modelService.save(user1);
		user1.setName("James Bond 02");
		modelService.save(user1);

		user2 = creator.createUser("008", "Krzysztof R.");
		user2.setName("Krzysio R.");
		modelService.save(user2);
	}

	@Test
	public void shouldSearchForAuditRecordsForTypeWithoutAnyCriterias() throws Exception
	{
		// given
		final AuditSearchQuery query = AuditSearchQuery.forType("User").build();

		// when
		final List<AuditRecord> result = readAuditGateway.search(query).collect(toList());

		// then
		assertThat(result).isNotEmpty().hasSize(5);
	}

	@Test
	public void shouldSearchForAuditRecordsForTypeAndParticularPk() throws Exception
	{
		// given
		final AuditSearchQuery query = AuditSearchQuery.forType("User").withPkSearchRules(user1.getPk()).build();

		// when
		final List<AuditRecord> result = readAuditGateway.search(query).collect(toList());

		// then
		assertThat(result).isNotEmpty().hasSize(3);
	}

	@Test
	public void shouldSearchForAuditRecordsForTypeAndFewPks() throws Exception
	{
		// given
		final AuditSearchQuery query = AuditSearchQuery.forType("User").withPkSearchRules(user1.getPk(), user2.getPk()).build();

		// when
		final List<AuditRecord> result = readAuditGateway.search(query).collect(toList());

		// then
		assertThat(result).isNotEmpty().hasSize(5);
	}

	@Test
	public void shouldSearchForAuditRecordsForTypeParticularPkAndExactValueInPayload() throws Exception
	{
		// given
		final AuditSearchQuery query = AuditSearchQuery.forType("User") //
				.withPkSearchRules(user1.getPk()) //
				.withPayloadSearchRule("name", "James Bond 02") //
				.build();

		// when
		final List<AuditRecord> result = readAuditGateway.search(query).collect(toList());

		// then
		assertThat(result).isNotEmpty().hasSize(1);
	}

	@Test
	public void shouldSearchForAuditRecordsForTypeParticularPkAndFewValuesInPayload() throws Exception
	{
		// given
		final AuditSearchQuery query = AuditSearchQuery.forType("User") //
				.withPkSearchRules(user1.getPk()) //
				.withPayloadSearchRule("name", "James Bond 01") //
				.withPayloadSearchRule("name", "James Bond 02") //
				.build();

		// when
		final List<AuditRecord> result = readAuditGateway.search(query).collect(toList());

		// then
		assertThat(result).isNotEmpty().hasSize(2);
	}
}