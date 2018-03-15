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
package de.hybris.platform.flexiblesearch;


import static org.fest.assertions.Assertions.assertThat;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.security.PrincipalGroup;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.ClasspathImpExResource;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;


public class OracleInLimitationTest extends ServicelayerTransactionalTest
{
	@Resource
	ImportService importService;
	@Resource
	ModelService modelService;
	@Resource
	ClassificationService classificationService;
	@Resource
	FlexibleSearchService flexibleSearchService;
	@Resource
	private TypeService typeService;
	@Resource
	private SessionService sessionService;

	private final PropertyConfigSwitcher optimisticLockingConfigSwitcher = new PropertyConfigSwitcher("db.supported.params.limit");


	@Test
	public void selectTitleByName()
	{
		try
		{
			optimisticLockingConfigSwitcher.switchToValue("1");

			// given
			createTitles(1, 2, 5);

			// when
			final List<TitleModel> result = flexibleSearchService.<TitleModel> search(
					"SELECT {pk} FROM {Title} WHERE {name} in (?names)", ImmutableMap.of("names", names(1, 2, 3, 4))).getResult();

			// then
			assertThat(result).hasSize(2);

			final TitleModel title1 = result.stream().filter(i -> i.getCode().equals("T-1")).findAny().get();
			final TitleModel title2 = result.stream().filter(i -> i.getCode().equals("T-2")).findAny().get();

			assertThat(title1).isNotNull();
			assertThat(title2).isNotNull();

			assertThat(title1.getName()).isEqualTo("Title-1");
			assertThat(title2.getName()).isEqualTo("Title-2");
		}
		finally
		{
			optimisticLockingConfigSwitcher.switchBackToDefault();
		}
	}

	@Test
	public void shouldNotCachePreviousStatement()
	{
		try
		{
			optimisticLockingConfigSwitcher.switchToValue("1");

			// given
			createTitles(1, 2, 5);

			// when
			final List<TitleModel> result = flexibleSearchService
					.<TitleModel> search("SELECT {pk} FROM {Title} WHERE {name} in (?names)", ImmutableMap.of("names", names(1, 2, 5)))
					.getResult();

			// then
			assertThat(result).hasSize(3);

			// and when
			final List<TitleModel> result2 = flexibleSearchService
					.<TitleModel> search("SELECT {pk} FROM {Title} WHERE {name} in (?names)", ImmutableMap.of("names", names(1, 2)))
					.getResult();

			// then
			assertThat(result2).hasSize(2);
		}
		finally
		{
			optimisticLockingConfigSwitcher.switchBackToDefault();
		}
	}


	@Test
	public void selectTitleByCodeAndName()
	{
		try
		{
			optimisticLockingConfigSwitcher.switchToValue("1");
			// given
			createTitles(1, 2, 5, 8, 10);

			// when
			final List<TitleModel> result = flexibleSearchService
					.<TitleModel> search("SELECT {pk} FROM {Title} WHERE {name} in (?names) AND {code} in (?codes)",
							ImmutableMap.of("names", names(1, 2, 3, 4, 8, 10), "codes", codes(1, 3, 4, 10)))
					.getResult();

			// then
			final TitleModel title1 = result.stream().filter(i -> i.getCode().equals("T-1")).findAny().get();
			final TitleModel title10 = result.stream().filter(i -> i.getCode().equals("T-10")).findAny().get();

			assertThat(title1).isNotNull();
			assertThat(title10).isNotNull();

			assertThat(title1.getName()).isEqualTo("Title-1");
			assertThat(title10.getName()).isEqualTo("Title-10");
		}
		finally
		{
			optimisticLockingConfigSwitcher.switchBackToDefault();
		}
	}


	@Test
	public void shouldSelectOver1000Titles()
	{
		createTitlesInRange(1, 2_000);

		final SearchResult<Object> search = flexibleSearchService.search("SELECT {pk} FROM {Title} WHERE {name} in (?names)",
				ImmutableMap.of("names", namesInRange(1, 2_000)));

		final List<Object> result = search.getResult();
		assertThat(result).hasSize(2_000);
	}



	@Test
	public void shouldFetchMoreThan1000ProductFeatures()
	{
		// given
		final ClassificationSystemVersionModel catalogVersion = createCatalogVersion("classSys", "classSysVer");
		importResource("/testOracle.impex");
		final ProductModel pm = createProduct(catalogVersion, "testClassification");

		// when
		final FeatureList features = classificationService.getFeatures(pm);

		// then
		assertThat(features.getFeatures()).hasSize(1100);
	}

	@Test
	public void shouldWorkIfMoreThan1000GroupsAreAssignedToPrincipal()
	{
		final Set<PrincipalGroupModel> userGroups = createUserGroups(1500);
		createUser(userGroups);

		// when
		final List<Object> result = flexibleSearchService
				.search("SELECT  {pgr:source} FROM {PrincipalGroupRelation AS pgr} WHERE {pgr:target} in (?groups)",
						ImmutableMap.of("groups", userGroups))
				.getResult();

		final List<Object> distinctResult = flexibleSearchService
				.search("SELECT DISTINCT {pgr:source} FROM {PrincipalGroupRelation AS pgr} WHERE {pgr:target} in (?groups)",
						ImmutableMap.of("groups", userGroups))
				.getResult();

		// then
		assertThat(result).hasSize(1500);
		assertThat(distinctResult).hasSize(1);
	}

	@Test
	public void shouldAllowOver1000ParamsInSessionAttributes()
	{
		final Set<PrincipalGroupModel> userGroups = createUserGroups(1500);
		createUser(userGroups);

		final Set<PrincipalGroup> sources = new HashSet<>();
		for (final PrincipalGroupModel user : userGroups)
		{
			sources.add(modelService.getSource(user));
		}

		JaloSession.getCurrentSession().setAttribute("foo", sources);

		// when
		final List<Object> result = flexibleSearchService
				.search("SELECT {pgr:source} FROM {PrincipalGroupRelation AS pgr} WHERE {pgr:target} in (?session.foo)").getResult();

		// then
		assertThat(result).hasSize(1500);
	}

	@Test
	public void shouldNotCachePreviousStatementWithSessionParam()
	{
		try
		{
			optimisticLockingConfigSwitcher.switchToValue("1");

			// given
			createTitles(1, 2, 5);

			JaloSession.getCurrentSession().setAttribute("names", names(1, 2, 5));

			// when
			final List<TitleModel> result = flexibleSearchService
					.<TitleModel> search("SELECT {pk} FROM {Title} WHERE {name} in (?session.names)").getResult();

			// then
			assertThat(result).hasSize(3);

			JaloSession.getCurrentSession().setAttribute("names", names(1, 2));

			// and when
			final List<TitleModel> result2 = flexibleSearchService
					.<TitleModel> search("SELECT {pk} FROM {Title} WHERE {name} in (?session.names)").getResult();

			// then
			assertThat(result2).hasSize(2);
		}
		finally
		{
			optimisticLockingConfigSwitcher.switchBackToDefault();
		}
	}

	@Test
	public void shouldReplaceAllDuplicatedSessionAttributesFromRestriction()
	{
		try
		{
			optimisticLockingConfigSwitcher.switchToValue("1");

			final UserModel user = modelService.create(UserModel.class);
			user.setUid("foo");
			modelService.save(user);

			final ComposedTypeModel composedTypeModel = typeService.getComposedTypeForClass(TitleModel.class);
			createTitles(1, 2, 5);

			sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public void executeWithoutResult()
				{
					// given
					JaloSession.getCurrentSession().setAttribute("names", names(1, 2, 5));

					createRestriction(user, composedTypeModel, "TitleRestrictionActive", "{name} in (?session.names)", Boolean.TRUE);

					// when
					final List<TitleModel> result = flexibleSearchService.<TitleModel> search("SELECT {pk} FROM {Title}").getResult();

					// then
					assertThat(result).hasSize(3);

					JaloSession.getCurrentSession().setAttribute("names", names(1, 2));

					// and when
					final List<TitleModel> result2 = flexibleSearchService.<TitleModel> search("SELECT {pk} FROM {Title}").getResult();

					// then
					assertThat(result2).hasSize(2);
				}
			}, user);

		}
		finally
		{
			optimisticLockingConfigSwitcher.switchBackToDefault();
		}
	}

	@Test
	public void shouldReplaceAllDuplicatedSessionAttributes()
	{
		final Set<PrincipalGroupModel> userGroups = createUserGroups(1500);
		createUser(userGroups);


		final Set<PrincipalGroup> sources = new HashSet<>();
		for (final PrincipalGroupModel user : userGroups)
		{
			sources.add(modelService.getSource(user));
		}

		JaloSession.getCurrentSession().setAttribute("foo", sources);

		// when
		final List<Object> result = flexibleSearchService
				.search(
						"SELECT {pgr:source} FROM {PrincipalGroupRelation AS pgr} WHERE {pgr:target} in (?session.foo) or {pgr:target} in (?session.foo)")
				.getResult();

		// then
		assertThat(result).hasSize(1500);
	}

	private SearchRestrictionModel createRestriction(final PrincipalModel principal, final ComposedTypeModel type,
			final String code, final String query, final Boolean active)
	{
		final SearchRestrictionModel model = (SearchRestrictionModel) modelService.create(SearchRestrictionModel.class);
		model.setCode(code);
		model.setActive(active);
		model.setQuery(query);
		model.setRestrictedType(type);
		model.setPrincipal(principal);
		model.setGenerate(Boolean.TRUE);
		modelService.save(model);
		return model;
	}

	@Test
	public void shouldCorrectlyHandleJoinStatement()
	{
		// given
		final List<String> names = createOrdersAndCorrespondingUsers(1_500);

		// when
		final List<Object> result = flexibleSearchService
				.search("SELECT {o.PK} FROM {Order AS o JOIN User AS u ON {o.user}={u.PK}} WHERE {u.name} IN (?names)",
						ImmutableMap.of("names", names))
				.getResult();

		// then
		assertThat(result).hasSize(1_500);
	}

	private List<String> createOrdersAndCorrespondingUsers(final int size)
	{
		final List<String> userNames = new ArrayList<>();

		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setActive(Boolean.TRUE);
		curr.setIsocode("PLN");
		curr.setDigits(Integer.valueOf(2));
		curr.setConversion(Double.valueOf(0.76d));
		curr.setSymbol("PLN");

		for (int i = 1; i <= 1_500; ++i)
		{
			final UserModel testUser = modelService.create(UserModel.class);

			final String name = "testUser_" + i;
			testUser.setName(name);
			userNames.add(name);
			testUser.setUid("testUser_" + i);

			final OrderModel order = modelService.create(OrderModel.class);
			order.setDate(new Date());
			order.setCurrency(curr);
			order.setUser(testUser);
			order.setNet(Boolean.FALSE);
			order.setCode("test_order_" + i);
		}

		modelService.saveAll();
		return userNames;
	}

	private Set<PrincipalGroupModel> createUserGroups(final int howMany)
	{
		final Set<PrincipalGroupModel> userGroups = new HashSet<>();

		for (int i = 1; i <= howMany; ++i)
		{
			final UserGroupModel userGroup = modelService.create(UserGroupModel.class);
			userGroup.setUid("ug" + i);
			userGroups.add(userGroup);
		}
		modelService.saveAll(userGroups);

		return userGroups;
	}

	private UserModel createUser(final Set<PrincipalGroupModel> userGroups)
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setGroups(userGroups);
		user.setUid("testUser");
		modelService.save(user);

		return user;
	}


	private ClassificationSystemVersionModel createCatalogVersion(final String classSystem, final String classVersion)
	{
		final ClassificationSystemModel catalog = modelService.create(ClassificationSystemModel.class);
		catalog.setId(classSystem);

		final ClassificationSystemVersionModel catalogVersion = modelService.create(ClassificationSystemVersionModel.class);
		catalogVersion.setVersion(classVersion);
		catalogVersion.setCatalog(catalog);
		modelService.save(catalogVersion);

		return catalogVersion;
	}



	private void importResource(final String impex)
	{
		final ImportConfig importConfig = new ImportConfig();
		importConfig.setScript(new ClasspathImpExResource(impex, "UTF-8"));
		importConfig.setMaxThreads(1);
		importConfig.setSynchronous(true);
		importService.importData(importConfig);
	}

	private ProductModel createProduct(final ClassificationSystemVersionModel catalogVersion, final String classificationCode)
	{
		final ProductModel pm = modelService.create(ProductModel.class);
		pm.setCode("PROD-1");
		pm.setCatalogVersion(catalogVersion);
		modelService.save(pm);

		final ClassificationClassModel classificationClass = flexibleSearchService.<ClassificationClassModel> searchUnique(
				new FlexibleSearchQuery("SELECT {PK} FROM {" + ClassificationClassModel._TYPECODE + "} WHERE {code} = ?code",
						ImmutableMap.of("code", classificationCode)));

		classificationClass.setProducts(ImmutableList.of(pm));
		modelService.save(classificationClass);
		catalogVersion.setRootCategories(ImmutableList.of(classificationClass));
		modelService.save(catalogVersion);

		return pm;
	}

	public List<String> namesInRange(final int from, final int to)
	{
		final List names = new ArrayList<>();
		for (int i = from; i <= to; ++i)
		{
			names.add("Title-" + i);
		}
		return names;
	}

	public void createTitlesInRange(final int from, final int to)
	{
		for (int i = from; i <= to; ++i)
		{
			final TitleModel title = modelService.create(TitleModel.class);

			title.setCode("T-" + i);
			title.setName("Title-" + i);
		}
		modelService.saveAll();
	}

	public void createTitles(final Integer... numbers)
	{
		for (final Integer i : numbers)
		{
			final TitleModel title = modelService.create(TitleModel.class);

			title.setCode("T-" + i);
			title.setName("Title-" + i);
		}
		modelService.saveAll();
	}

	public List<String> names(final Integer... numbers)
	{
		return Arrays.asList(numbers).stream().map(i -> "Title-" + i).collect(Collectors.toList());
	}

	public List<String> codes(final Integer... numbers)
	{
		return Arrays.asList(numbers).stream().map(i -> "T-" + i).collect(Collectors.toList());
	}
}
