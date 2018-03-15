package de.hybris.platform.persistence.audit.gateway.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.audit.gateway.LinkAuditRecord;
import de.hybris.platform.persistence.audit.gateway.AuditSearchQuery;
import de.hybris.platform.persistence.audit.gateway.GenericSearchRule;
import de.hybris.platform.persistence.audit.gateway.SearchRule;

import org.junit.Test;

@UnitTest
public class AuditSearchQueryTest
{

	@Test
	public void shouldBuildQueryForType() throws Exception
	{
		// given
		final String typeCode = "User";

		// when
		final AuditSearchQuery query = AuditSearchQuery.forType(typeCode).build();

		// then
		assertThat(query).isNotNull();
		assertThat(query.getTypeCode()).isEqualTo(typeCode);
		assertThat(query.isLinkRelatedQuery()).isFalse();
		assertThat(query.getLinkSide()).isNull();
		assertThat(query.hasStandardSearchRules()).isFalse();
		assertThat(query.hasPayloadSearchRules()).isFalse();
		assertThat(query.getPayloadSearchRules()).isEmpty();
		assertThat(query.getStandardSearchRules()).isEmpty();
	}

	@Test
	public void shouldBuildQueryForTypeAndPk() throws Exception
	{
		// given
		final String typeCode = "User";
		final PK pk1 = PK.createFixedCounterPK(4, 1);

		// when
		final AuditSearchQuery query = AuditSearchQuery.forType(typeCode).withPkSearchRules(pk1).build();

		// then
		assertThat(query).isNotNull();
		assertThat(query.getTypeCode()).isEqualTo(typeCode);
		assertThat(query.isLinkRelatedQuery()).isFalse();
		assertThat(query.getLinkSide()).isNull();
		assertThat(query.hasStandardSearchRules()).isTrue();
		assertThat(query.hasPayloadSearchRules()).isFalse();
		assertThat(query.getPayloadSearchRules()).isEmpty();
		assertThat(query.getStandardSearchRules()).hasSize(1);
		assertThat(query.getStandardSearchRules()).extracting(SearchRule::getFieldName).containsExactly("itempk");
		assertThat(query.getStandardSearchRules()).extracting(SearchRule::getValue).containsExactlyInAnyOrder(pk1.getLongValue());
	}


	@Test
	public void shouldBuildQueryForTypeAndSeveralPks() throws Exception
	{
		// given
		final String typeCode = "User";
		final PK pk1 = PK.createFixedCounterPK(4, 1);
		final PK pk2 = PK.createFixedCounterPK(4, 2);
		final PK pk3 = PK.createFixedCounterPK(4, 3);

		// when
		final AuditSearchQuery query = AuditSearchQuery.forType(typeCode).withPkSearchRules(pk1, pk2, pk3).build();

		// then
		assertThat(query).isNotNull();
		assertThat(query.getTypeCode()).isEqualTo(typeCode);
		assertThat(query.isLinkRelatedQuery()).isFalse();
		assertThat(query.getLinkSide()).isNull();
		assertThat(query.hasStandardSearchRules()).isTrue();
		assertThat(query.hasPayloadSearchRules()).isFalse();
		assertThat(query.getPayloadSearchRules()).isEmpty();
		assertThat(query.getStandardSearchRules()).hasSize(3);
		assertThat(query.getStandardSearchRules()).extracting(SearchRule::getFieldName).containsExactly("itempk", "itempk",
				"itempk");
		assertThat(query.getStandardSearchRules()).extracting(SearchRule::getValue).containsExactlyInAnyOrder(pk1.getLongValue(),
				pk2.getLongValue(), pk3.getLongValue());
	}

	@Test
	public void shouldBuildQueryForTypeAndPkAndAnotherStandardField() throws Exception
	{
		// given
		final String typeCode = "User";
		final PK pk1 = PK.createFixedCounterPK(4, 1);
		final GenericSearchRule<String> changingUserCriteria = new GenericSearchRule<>("changinguser", "administrator", false);

		// when
		final AuditSearchQuery query = AuditSearchQuery.forType(typeCode) //
				.withPkSearchRules(pk1) //
				.withSearchRule(changingUserCriteria) //
				.build(); //

		// then
		assertThat(query).isNotNull();
		assertThat(query.getTypeCode()).isEqualTo(typeCode);
		assertThat(query.isLinkRelatedQuery()).isFalse();
		assertThat(query.getLinkSide()).isNull();
		assertThat(query.hasStandardSearchRules()).isTrue();
		assertThat(query.hasPayloadSearchRules()).isFalse();
		assertThat(query.getPayloadSearchRules()).isEmpty();
		assertThat(query.getStandardSearchRules()).hasSize(2);
		assertThat(query.getStandardSearchRules()).extracting(SearchRule::getFieldName).containsExactly("itempk", "changinguser");
		assertThat(query.getStandardSearchRules()).extracting(SearchRule::getValue).containsExactlyInAnyOrder(pk1.getLongValue(),
				"administrator");
	}

	@Test
	public void shouldBuildQueryForTypeAndPayload() throws Exception
	{
		// given
		final String typeCode = "User";
		final String fieldNameInPayload = "someField";
		final String fieldValueInPayload = "someValue";

		// when
		final AuditSearchQuery query = AuditSearchQuery.forType(typeCode)
				.withPayloadSearchRule(fieldNameInPayload, fieldValueInPayload).build();

		// then
		assertThat(query).isNotNull();
		assertThat(query.getTypeCode()).isEqualTo(typeCode);
		assertThat(query.isLinkRelatedQuery()).isFalse();
		assertThat(query.getLinkSide()).isNull();
		assertThat(query.hasStandardSearchRules()).isFalse();
		assertThat(query.hasPayloadSearchRules()).isTrue();
		assertThat(query.getPayloadSearchRules()).hasSize(1);
		assertThat(query.getStandardSearchRules()).hasSize(0);
		assertThat(query.getPayloadSearchRules()).extracting(SearchRule::getFieldName).containsExactly("someField");
		assertThat(query.getPayloadSearchRules()).extracting(SearchRule::getValue).containsExactly("someValue");
	}

	@Test
	public void shouldBuildQueryForTypeAndPkAndPayload() throws Exception
	{
		// given
		final String typeCode = "User";
		final PK pk1 = PK.createFixedCounterPK(4, 1);
		final String fieldNameInPayload = "someField";
		final String fieldValueInPayload = "someValue";

		// when
		final AuditSearchQuery query = AuditSearchQuery.forType(typeCode).withPkSearchRules(pk1)
				.withPayloadSearchRule(fieldNameInPayload, fieldValueInPayload).build();

		// then
		assertThat(query).isNotNull();
		assertThat(query.getTypeCode()).isEqualTo(typeCode);
		assertThat(query.isLinkRelatedQuery()).isFalse();
		assertThat(query.getLinkSide()).isNull();
		assertThat(query.hasStandardSearchRules()).isTrue();
		assertThat(query.hasPayloadSearchRules()).isTrue();
		assertThat(query.getPayloadSearchRules()).hasSize(1);
		assertThat(query.getStandardSearchRules()).hasSize(1);
		assertThat(query.getStandardSearchRules()).extracting(SearchRule::getFieldName).containsExactly("itempk");
		assertThat(query.getPayloadSearchRules()).extracting(SearchRule::getFieldName).containsExactly("someField");
		assertThat(query.getStandardSearchRules()).extracting(SearchRule::getValue).containsExactly(pk1.getLongValue());
		assertThat(query.getPayloadSearchRules()).extracting(SearchRule::getValue).containsExactly("someValue");
	}

	@Test
	public void shouldBuildLinkQueryForTypeAndPkAndSearchBySource() throws Exception
	{
		// given
		final String linkTypeCode = "LinkTypeCode";
		final PK sourcePk = PK.createFixedCounterPK(100, 1);

		// when
		final AuditSearchQuery query = AuditSearchQuery.forLink(linkTypeCode).withPk(sourcePk).buildForSource();

		// then
		assertThat(query).isNotNull();
		assertThat(query.getTypeCode()).isEqualTo(linkTypeCode);
		assertThat(query.getLinkSide()).isEqualTo(LinkAuditRecord.LinkSide.SOURCE);
		assertThat(query.getStandardSearchRules()).hasSize(1);
		assertThat(query.getStandardSearchRules()).extracting(SearchRule::getFieldName).containsExactly("sourcepk");
	}

	@Test
	public void shouldBuildLinkQueryForTypeAndPkAndSearchByTarget() throws Exception
	{
		// given
		final String linkTypeCode = "LinkTypeCode";
		final PK sourcePk = PK.createFixedCounterPK(100, 1);

		// when
		final AuditSearchQuery query = AuditSearchQuery.forLink(linkTypeCode).withPk(sourcePk).buildForTarget();

		// then
		assertThat(query).isNotNull();
		assertThat(query.getTypeCode()).isEqualTo(linkTypeCode);
		assertThat(query.getLinkSide()).isEqualTo(LinkAuditRecord.LinkSide.TARGET);
		assertThat(query.getStandardSearchRules()).hasSize(1);
		assertThat(query.getStandardSearchRules()).extracting(SearchRule::getFieldName).containsExactly("targetpk");
	}

	@Test
	public void shouldNotAllowToBuildQueryWithNullType() throws Exception
	{
		// given
		final String typeCode = null;

		try
		{
			// when
			AuditSearchQuery.forType(typeCode).build();
			fail("Expected NullPointerException");
		}
		catch (final NullPointerException e)
		{
			// then fine
		}
	}

	@Test
	public void shouldNotAllowToBuildLinkQueryWithNullPk() throws Exception
	{
		// given
		final String linkTypeCode = "LinkTypeCode";
		final PK pk = null;

		try
		{
			// when
			AuditSearchQuery.forLink(linkTypeCode).withPk(pk).buildForSource();
			fail("Expected NullPointerException");
		}
		catch (final NullPointerException e)
		{
			// then fine
		}
	}

	@Test
	public void shouldNotAllowToBuildLinkQueryWithoutPk() throws Exception
	{
		// given
		final String linkTypeCode = "LinkTypeCode";

		try
		{
			// when
			AuditSearchQuery.forLink(linkTypeCode).buildForSource();
			fail("Expected NullPointerException");
		}
		catch (final NullPointerException e)
		{
			// then fine
		}
	}

}