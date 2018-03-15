/*
 *  [y] hybris Platform
 *
 *  Copyright (c) 2000-2017 SAP SE
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of SAP
 *  Hybris ("Confidential Information"). You shall not disclose such
 *  Confidential Information and shall use it only in accordance with the
 *  terms of the license agreement you entered into with SAP Hybris.
 */

package de.hybris.platform.audit.provider.internal.resolver.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.audit.provider.internal.resolver.AuditRecordInternalIndex;
import de.hybris.platform.audit.view.impl.AuditEvent;
import de.hybris.platform.core.PK;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class AuditTypeContextTest
{

	private final PK pk1 = PK.fromLong(1L);
	private final PK pk2 = PK.fromLong(2L);

	@Mock
	private AuditRecordInternalIndex<AuditEvent> auditRecordInternalIndex;

	@Mock
	private AuditEvent pk1EventA, pk1EventB, pk2EventA, pk2EventB;

	private final String type = "someType";

	private final String baseType = "someBaseType";


	@Before
	public void setUp()
	{
		when(auditRecordInternalIndex.getRecords(pk1)).thenReturn(Sets.newHashSet(pk1EventA, pk1EventB));
		when(auditRecordInternalIndex.getRecords(pk2)).thenReturn(Sets.newHashSet(pk2EventA, pk2EventB));
		when(auditRecordInternalIndex.getRecords(baseType)).thenReturn(Sets.newHashSet(pk1EventA, pk1EventB, pk2EventA, pk2EventB));
	}

	@Test
	public void getPayloadsPK() throws Exception
	{
		final AuditTypeContext<AuditEvent> context = new AuditTypeContext<>(auditRecordInternalIndex, type, baseType,
				Sets.newHashSet(), Sets.newHashSet());

		assertThat(context.getPayloads(pk1)).isEqualTo(Sets.newHashSet(pk1EventA, pk1EventB));
	}

	@Test
	public void getPayloadsType() throws Exception
	{
		final AuditTypeContext<AuditEvent> context = new AuditTypeContext<>(auditRecordInternalIndex, type, baseType,
				Sets.newHashSet(), Sets.newHashSet());

		assertThat(context.getPayloads(baseType)).isEqualTo(Sets.newHashSet(pk1EventA, pk1EventB, pk2EventA, pk2EventB));
	}
}