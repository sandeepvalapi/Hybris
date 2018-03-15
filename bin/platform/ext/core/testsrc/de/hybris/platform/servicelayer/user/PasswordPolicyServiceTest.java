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
package de.hybris.platform.servicelayer.user;


import static de.hybris.platform.servicelayer.user.impl.BlacklistFilePasswordPolicy.BLACKLISTED_PASSWORD;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

import com.google.common.collect.ImmutableSet;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import de.hybris.platform.servicelayer.user.exceptions.PasswordPolicyViolationException;
import de.hybris.platform.servicelayer.user.impl.PasswordPolicyMapping;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


@IntegrationTest
public class PasswordPolicyServiceTest extends ServicelayerBaseTest
{
	private static final String ENCODING = "pbkdf2";
	private static final String REGEX_POLICY_GROUP = "regexPasswordPolicyGroup";
	private static final String BLACKLIST_POLICY_GROUP = "blacklistPasswordPolicyGroup";

	private static final String MUST_CONTAIN_DIGIT = "mustcontaindigit";
	private static final String MUST_CONFORM_TO_SIZE = "certainsize";

	@Resource
	PasswordPolicy regexPasswordPolicy;

	@Resource
	PasswordPolicyService passwordPolicyService;

	@Resource
	ModelService modelService;

	@Resource
	UserService userService;


	PasswordPolicy policy;
	PropertyConfigSwitcher includedGroups;
	PropertyConfigSwitcher excludedGroups;

	@Before
	public void initPropertySwitcher()
	{
		includedGroups = new PropertyConfigSwitcher("password.policy.foo.groups.included");
		excludedGroups = new PropertyConfigSwitcher("password.policy.foo.groups.excluded");

		policy = Mockito.mock(PasswordPolicy.class);
		Mockito.when(policy.getPolicyName()).thenReturn("foo");
	}

	@Before
	public void revertProperties()
	{
		includedGroups.switchBackToDefault();
		excludedGroups.switchBackToDefault();
	}


	@Test
	public void wildcardInPolicyMappingShouldMatchAllGroups()
	{
		includedGroups.switchToValue("*, employees");

		final PasswordPolicyMapping policyMapping = PasswordPolicyMapping.forPolicy(policy);

		assertThat(policyMapping.isActiveForGroups(ImmutableSet.of("aaaa"))).isTrue();
	}

	@Test
	public void excludedGroupShouldNotMatch()
	{
		includedGroups.switchToValue("employees, toexclude");
		excludedGroups.switchToValue("toexclude");

		final PasswordPolicyMapping policyMapping = PasswordPolicyMapping.forPolicy(policy);

		assertThat(policyMapping.isActiveForGroups(asSet("toexclude"))).isFalse();
		assertThat(policyMapping.isActiveForGroups(asSet("employees"))).isTrue();
	}

	@Test
	public void excludedWildcardShouldNotAffectAnything()
	{
		includedGroups.switchToValue("*");
		final PasswordPolicyMapping policyMapping = PasswordPolicyMapping.forPolicy(policy);

		assertThat(policyMapping.isActiveForGroups(asSet("employees"))).isTrue();

		excludedGroups.switchToValue("*");
		final PasswordPolicyMapping policyMappingWithExcludedWildcard = PasswordPolicyMapping.forPolicy(policy);

		assertThat(policyMappingWithExcludedWildcard.isActiveForGroups(asSet("employees"))).isTrue();
	}

	@Test
	public void excludedGroupsShouldTakePrecedenceOverWildcard()
	{
		includedGroups.switchToValue("employees, toexclude, toexclude2, *");
		excludedGroups.switchToValue("toexclude, toexclude2");

		final PasswordPolicyMapping policyMapping = PasswordPolicyMapping.forPolicy(policy);

		assertThat(policyMapping.isActiveForGroups(Collections.emptySet())).isTrue();
		assertThat(policyMapping.isActiveForGroups(asSet("foo"))).isTrue();
		assertThat(policyMapping.isActiveForGroups(asSet("employees"))).isTrue();
		assertThat(policyMapping.isActiveForGroups(asSet("toexclude"))).isFalse();
		assertThat(policyMapping.isActiveForGroups(asSet("toexclude", "toexclude2"))).isFalse();
	}

	@Test
	public void shouldReportViolationForPasswordTooShortAndWithoutDigit()
	{
		final UserModel user = givenEmployee();

		final List<PasswordPolicyViolation> policyViolations = regexPasswordPolicy.verifyPassword(user, "qwertee", ENCODING);

		assertThat(policyViolations).hasSize(2);
		assertPasswordSizeViolation(policyViolations);
		assertPasswordMustContainDigitViolation(policyViolations);
	}

	@Test
	public void shouldNotReportPolicyCompliantPassword()
	{
		final UserModel user = givenEmployee();

		final List<PasswordPolicyViolation> policyViolations = regexPasswordPolicy.verifyPassword(user, "qwertee01aaaa", ENCODING);

		assertThat(policyViolations).isEmpty();
	}

	@Test
	public void employeeNotInGroupShouldNotTriggerPolicy()
	{
		final UserModel user = givenEmployeeInGroup("randomGroup");

		final List<PasswordPolicyViolation> policyViolations = passwordPolicyService.verifyPassword(user, "qwertee", ENCODING);

		assertThat(policyViolations).isEmpty();
	}

	@Test
	public void employeeInGroupShouldTriggerPolicy()
	{
		final UserModel user = givenEmployeeInGroup(REGEX_POLICY_GROUP);

		final List<PasswordPolicyViolation> policyViolations = passwordPolicyService.verifyPassword(user, "qwertee", ENCODING);

		assertThat(policyViolations).hasSize(2);
		assertThat(policyViolations).hasSize(2);
		assertPasswordSizeViolation(policyViolations);
		assertPasswordMustContainDigitViolation(policyViolations);
	}

	@Test(expected = PasswordPolicyViolationException.class)
	public void jaloShouldRaisePolicyViolationIfNonCompliantPassword() throws ConsistencyCheckException
	{
		final UserModel user = givenEmployeeInGroup(REGEX_POLICY_GROUP);
		final User jaloUser = modelService.getSource(user);

		jaloUser.setPassword("qwertee", ENCODING);
	}

	@Test
	public void jaloShouldNotRaisePolicyViolationIfCompliantPassword() throws ConsistencyCheckException
	{
		final UserModel user = givenEmployeeInGroup(REGEX_POLICY_GROUP);
		final User jaloUser = modelService.getSource(user);

		jaloUser.setPassword("qwertee123", ENCODING);
	}

	@Test(expected = PasswordPolicyViolationException.class)
	public void userServiceShouldNotAcceptNonCompliantPassword()
	{
		final UserModel user = givenEmployeeInGroup(REGEX_POLICY_GROUP);

		userService.setPassword(user, "qwertee", ENCODING);
	}

	@Test
	public void userServiceShouldAcceptCompliantPassword()
	{
		final UserModel user = givenEmployeeInGroup(REGEX_POLICY_GROUP);
		userService.setPassword(user, "qwertee123", ENCODING);

		assertThat(user.getEncodedPassword()).isNotNull();
	}


	@Test(expected = PasswordPolicyViolationException.class)
	public void passwordChangeByUserIdShouldFailForNonCompliantPassoword()
	{
		final EmployeeModel employeeModel = givenEmployeeInGroup(REGEX_POLICY_GROUP);

		userService.setPassword(employeeModel.getUid(), "qwertee", ENCODING);
	}

	@Test
	public void blacklistedPasswordShouldFail()
	{
		final EmployeeModel employeeModel = givenEmployeeInGroup(BLACKLIST_POLICY_GROUP);

		try
		{
			userService.setPassword(employeeModel.getUid(), "dragon", ENCODING);
			fail("Expected PasswordPolicyViolationException");
		}
		catch (final Exception ex)
		{
			assertThat(ex).isInstanceOf(PasswordPolicyViolationException.class);
			final PasswordPolicyViolationException passwordPolicyViolation = (PasswordPolicyViolationException) ex;

			assertThat(passwordPolicyViolation.getPolicyViolations()).hasSize(1);
			assertThat(passwordPolicyViolation.getPolicyViolations().get(0).getId()).isEqualTo(BLACKLISTED_PASSWORD);
		}
	}

	@Test
	public void shouldExecuteTwoPoliciesAtOnce()
	{
		final EmployeeModel employeeModel = givenEmployeeInGroup(BLACKLIST_POLICY_GROUP, REGEX_POLICY_GROUP);

		try
		{
			userService.setPassword(employeeModel.getUid(), "dragon", ENCODING);
			fail("Expected PasswordPolicyViolationException");
		}
		catch (final Exception ex)
		{
			assertThat(ex).isInstanceOf(PasswordPolicyViolationException.class);
			final PasswordPolicyViolationException passwordPolicyViolation = (PasswordPolicyViolationException) ex;
			assertThat(passwordPolicyViolation.getPolicyViolations()).hasSize(3);
		}
	}


	private EmployeeModel givenEmployee()
	{
		final EmployeeModel user = modelService.create(EmployeeModel.class);
		user.setUid(UUID.randomUUID().toString());

		return user;
	}

	private EmployeeModel givenEmployeeInGroup(final String... groupUids)
	{
		final Set<PrincipalGroupModel> userGroups = new HashSet<>();
		for (final String groupUid : groupUids)
		{
			final UserGroupModel group = modelService.create(UserGroupModel.class);
			group.setUid(groupUid);
			userGroups.add(group);
		}


		final EmployeeModel user = modelService.create(EmployeeModel.class);
		user.setUid(UUID.randomUUID().toString());
		user.setGroups(userGroups);
		modelService.saveAll();

		return user;
	}

	private void assertPasswordSizeViolation(final List<PasswordPolicyViolation> policyViolations)
	{
		assertThat(policyViolations.stream().map(PasswordPolicyViolation::getId).anyMatch(i -> i.equals(MUST_CONFORM_TO_SIZE)))
				.isTrue();
	}

	private void assertPasswordMustContainDigitViolation(final List<PasswordPolicyViolation> policyViolations)
	{
		assertThat(policyViolations.stream().map(PasswordPolicyViolation::getId).anyMatch(i -> i.equals(MUST_CONTAIN_DIGIT)))
				.isTrue();
	}

	private <T> Set<T> asSet(final T... elements)
	{
		return new HashSet<>(Arrays.asList(elements));
	}
}
