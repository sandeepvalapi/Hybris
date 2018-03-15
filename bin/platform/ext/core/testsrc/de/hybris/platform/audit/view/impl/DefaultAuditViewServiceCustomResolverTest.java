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

package de.hybris.platform.audit.view.impl;

import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.audit.AuditTestHelper;
import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.internal.config.AbstractTypedAttribute;
import de.hybris.platform.audit.internal.config.ResolvesBy;
import de.hybris.platform.audit.provider.internal.resolver.ReferencesResolver;
import de.hybris.platform.audit.provider.internal.resolver.impl.AuditTypeContext;
import de.hybris.platform.audit.view.AuditViewService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;


@IntegrationTest
public class DefaultAuditViewServiceCustomResolverTest extends ServicelayerBaseTest
{
	private static final String REFERENCE_DUMMY_RESOLVER = "test.referenceResolver";
	private static final String RELATION_DUMMY_RESOLVER = "test.relationResolver";
	private static final String VIRTUAL_REFERENCE_DUMMY_RESOLVER = "test.virtualReferenceResolver";

	private AuditTestHelper auditTestHelper;

	private ReferencesResolver referenceDummyResolver;
	private ReferencesResolver relationDummyResolver;
	private ReferencesResolver virtualReferenceDummyResolver;

	@Resource
	private ModelService modelService;

	@Resource
	private AuditViewService auditViewService;

	@Before
	public void setUp()
	{
		auditTestHelper = new AuditTestHelper();

		referenceDummyResolver = mock(ReferencesResolver.class);
		relationDummyResolver = mock(ReferencesResolver.class);
		virtualReferenceDummyResolver = mock(ReferencesResolver.class);

		final ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) Registry.getApplicationContext();
		final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ctx.getBeanFactory();

		beanFactory.registerSingleton(REFERENCE_DUMMY_RESOLVER, referenceDummyResolver);
		beanFactory.registerSingleton(RELATION_DUMMY_RESOLVER, relationDummyResolver);
		beanFactory.registerSingleton(VIRTUAL_REFERENCE_DUMMY_RESOLVER, virtualReferenceDummyResolver);
	}

	@After
	public void tearDown()
	{
		final ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) Registry.getApplicationContext();
		final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ctx.getBeanFactory();

		beanFactory.destroySingleton(REFERENCE_DUMMY_RESOLVER);
		beanFactory.destroySingleton(RELATION_DUMMY_RESOLVER);
		beanFactory.destroySingleton(VIRTUAL_REFERENCE_DUMMY_RESOLVER);
	}

	@Test
	public void shouldDelegateToCustomReferenceResolver()
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid(UUID.randomUUID().toString());
		user.setName("user1");
		user.setDescription("user description");
		modelService.save(user);

		user.setName("user2");
		modelService.save(user);

		final TypeAuditReportConfig config = TypeAuditReportConfig.builder() //
				.withConfig(auditTestHelper.loadConfigFromFile("audit.test/custom-resolvers-reference.xml", "UserReport")) //
				.withFullReport() //
				.withRootTypePk(user.getPk()) //
				.build();

		auditViewService.getViewOn(config).collect(Collectors.toList());

		verify(referenceDummyResolver, atLeast(1)).resolve(any(), argThat(new ExpressionArgumentMatcher("defaultpaymentaddress")),
				anyCollectionOf(AuditRecord.class));
		verify(relationDummyResolver, atLeast(1)).resolve(any(), any(), any());
		verify(virtualReferenceDummyResolver, atLeast(1)).resolve(any(), argThat(new ExpressionArgumentMatcher("owner")),
				anyCollectionOf(AuditRecord.class));
	}

	@Test
	public void shouldOverwriteExpressionInResolvesByTag()
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid(UUID.randomUUID().toString());
		user.setName("user1");
		user.setDescription("user description");
		modelService.save(user);

		user.setName("user2");
		modelService.save(user);

		final TypeAuditReportConfig config = TypeAuditReportConfig.builder() //
				.withConfig(auditTestHelper.loadConfigFromFile("audit.test/custom-resolvers-reference-overwrite-expression.xml",
						"UserReport")) //
				.withFullReport() //
				.withRootTypePk(user.getPk()) //
				.build();

		auditViewService.getViewOn(config).collect(Collectors.toList());

		verify(referenceDummyResolver, atLeast(1)).resolve(any(), argThat(new ExpressionArgumentMatcher("referenceExpression")),
				anyCollectionOf(AuditRecord.class));
		verify(relationDummyResolver, atLeast(1)).resolve(any(), any(), any());
		verify(virtualReferenceDummyResolver, atLeast(1)).resolve(any(),
				argThat(new ExpressionArgumentMatcher("virtualExpression")), anyCollectionOf(AuditRecord.class));
	}


	private static class ExpressionArgumentMatcher
			extends ArgumentMatcher<Map<AbstractTypedAttribute, AuditTypeContext<AuditRecord>>>
	{
		private final String expression;

		private ExpressionArgumentMatcher(final String expression)
		{
			this.expression = expression;
		}

		@Override
		public boolean matches(final Object argument)
		{
			final Map<AbstractTypedAttribute, AuditTypeContext> abstractTypedAttributes = (Map<AbstractTypedAttribute, AuditTypeContext>) argument;

			return abstractTypedAttributes.keySet().stream().map(AbstractTypedAttribute::getResolvesBy)
					.map(ResolvesBy::getExpression).allMatch(s -> s.equals(expression));
		}
	}
}
