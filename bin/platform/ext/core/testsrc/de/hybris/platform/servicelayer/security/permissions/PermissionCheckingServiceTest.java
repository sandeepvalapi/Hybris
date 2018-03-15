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
package de.hybris.platform.servicelayer.security.permissions;

import static de.hybris.platform.servicelayer.security.permissions.PermissionCheckValue.ALLOWED;
import static de.hybris.platform.servicelayer.security.permissions.PermissionCheckValue.CONFLICTING;
import static de.hybris.platform.servicelayer.security.permissions.PermissionCheckValue.DENIED;
import static de.hybris.platform.servicelayer.security.permissions.PermissionCheckValue.NOT_DEFINED;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.security.UserRightModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;


/**
 * Tests {@link PermissionCheckingService} service public API
 */
@IntegrationTest
public class PermissionCheckingServiceTest extends AbstractPermissionServiceTest
{
	private static final Logger LOG = Logger.getLogger(PermissionCheckingServiceTest.class); // NOPMD

	private static final String NOT_IMPORTANT_PERMISSION_NAME = "whatever";

	@Resource
	private PermissionManagementService permissionManagementService;

	@Resource
	private PermissionCheckingService permissionCheckingService;

	@Resource
	private ModelService modelService;

	@Resource
	private TypeService typeService;

	@Resource
	private UserService userService;

	private UserGroupModel group1;
	private UserGroupModel group2;
	private UserGroupModel group3;
	private UserGroupModel group3_1;


	@Test
	// PLA-13675
	public void testFalseCyclicGroupMembershipBug()
	{
		final UserGroupModel group00 = modelService.create(UserGroupModel.class);
		group00.setUid("g00");

		final UserGroupModel group01 = modelService.create(UserGroupModel.class);
		group01.setUid("g01");
		group01.setGroups((Set) Collections.singleton(group00));

		final UserModel user = modelService.create(UserModel.class);
		user.setUid("user");
		user.setGroups(new LinkedHashSet(Arrays.asList(group00, group01)));

		modelService.saveAll(group00, group01, user);

		// this will throw a exception in case of PLA-13675 not fixed !!!
		permissionCheckingService.checkTypePermission(LanguageModel._TYPECODE, user, "foo");
	}

	/**
	 * Test the way existing group hierarchy works
	 */
	@Test
	public void testExistingGroupHierarchyTraversal()
	{
		final UserGroupModel group1 = modelService.create(UserGroupModel.class);
		group1.setUid("group1");
		modelService.save(group1);

		final UserGroupModel group2 = modelService.create(UserGroupModel.class);
		group2.setUid("group2");
		modelService.save(group2);

		final UserGroupModel group3 = modelService.create(UserGroupModel.class);
		group3.setUid("group3");
		modelService.save(group3);

		final UserGroupModel group3_1 = modelService.create(UserGroupModel.class);
		group3_1.setUid("group3_1");
		group3_1.setGroups(new HashSet<PrincipalGroupModel>(Arrays.asList(group3)));
		modelService.save(group3_1);

		testUser1.setGroups(new HashSet<PrincipalGroupModel>(Arrays.asList(group1, group2, group3_1)));
		modelService.save(testUser1);

		Assert.assertEquals("group3_1 supergroup should be group3", group3, group3_1.getAllGroups().iterator().next());

		Assert.assertEquals("testUser1 should belong to 4 groups",
				new HashSet<PrincipalGroupModel>(Arrays.asList(group1, group2, group3, group3_1)), testUser1.getAllGroups());

		Assert.assertEquals("testUser1 should directly belong to 3 groups",
				new HashSet<PrincipalGroupModel>(Arrays.asList(group1, group2, group3_1)), testUser1.getGroups());

		permissionManagementService.createPermission(TEST_PERMISSION_1);

		// final PermissionAssignment positivePermissionAssignment1 = new PermissionAssignment(TEST_PERMISSION_1, group1);
		// final PermissionAssignment positivePermissionAssignment2 = new PermissionAssignment(TEST_PERMISSION_1, group2);
		final PermissionAssignment negativePermissionAssignment3 = new PermissionAssignment(TEST_PERMISSION_1, group3, true);

		// permissionManagementService.addGlobalPermission(positivePermissionAssignment1);
		// permissionManagementService.addGlobalPermission(positivePermissionAssignment2);
		permissionManagementService.addGlobalPermission(negativePermissionAssignment3);

		final Principal principal = modelService.getSource(testUser1);
		final UserRight right = modelService.getSource(getPermissionForName(TEST_PERMISSION_1));
		Assert.assertEquals(Boolean.FALSE, Boolean.valueOf(principal.checkGlobalPermission(right)));
	}


	/**
	 * Test the way existing type hierarchy works
	 */
	@Test
	public void testExistingTypeHierarchyTraversal()
	{

		final ComposedTypeModel itemType = typeService.getComposedTypeForCode(ItemModel._TYPECODE);
		final ComposedTypeModel catalogType = typeService.getComposedTypeForCode(CatalogModel._TYPECODE);
		final ComposedTypeModel classificationSystemType = typeService.getComposedTypeForCode(ClassificationSystemModel._TYPECODE);

		final UserGroupModel group1 = modelService.create(UserGroupModel.class);
		group1.setUid("group1");
		modelService.save(group1);

		final UserGroupModel group2 = modelService.create(UserGroupModel.class);
		group2.setUid("group2");
		group2.setGroups(new HashSet<PrincipalGroupModel>(Arrays.asList(group1)));
		modelService.save(group2);

		final UserGroupModel group3 = modelService.create(UserGroupModel.class);
		group3.setUid("group3");
		modelService.save(group3);

		final UserGroupModel group4 = modelService.create(UserGroupModel.class);
		group4.setUid("group4");
		modelService.save(group4);

		testUser1.setGroups(new HashSet<PrincipalGroupModel>(Arrays.asList(group2, group3, group4)));
		modelService.save(testUser1);

		Assert.assertEquals("group2 supergroup should be group1", group1, group2.getAllGroups().iterator().next());

		Assert.assertEquals("testUser1 should belong to 4 groups",
				new HashSet<PrincipalGroupModel>(Arrays.asList(group1, group2, group3, group4)), testUser1.getAllGroups());

		Assert.assertEquals("testUser1 should directly belong to 3 groups",
				new HashSet<PrincipalGroupModel>(Arrays.asList(group2, group3, group4)), testUser1.getGroups());

		permissionManagementService.createPermission(TEST_PERMISSION_1);

		final PermissionAssignment itemPermissionAssignment = new PermissionAssignment(TEST_PERMISSION_1, group3, true);
		final PermissionAssignment catalogPermissionAssignment = new PermissionAssignment(TEST_PERMISSION_1, group1);

		permissionManagementService.addTypePermission(itemType, itemPermissionAssignment);
		permissionManagementService.addTypePermission(catalogType, catalogPermissionAssignment);

		final ComposedType type = modelService.getSource(classificationSystemType);

		final Principal principal = modelService.getSource(testUser1);
		final UserRight right = modelService.getSource(getPermissionForName(TEST_PERMISSION_1));
		final boolean expected = true;
		final boolean actual = type.checkTypePermission(principal, right);
		Assert.assertEquals(expected, actual);
	}


	// Tests for null arguments

	@Test(expected = IllegalArgumentException.class)
	public void testCheckItemPermissionWithItemNull()
	{
		permissionCheckingService.checkItemPermission(null, testUser1, PermissionsConstants.READ);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckItemPermissionWithPrincipalNull()
	{
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");

		permissionCheckingService.checkItemPermission(newCountry, null, PermissionsConstants.READ);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckItemPermissionWithPermissionNameNull()
	{
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");

		permissionCheckingService.checkItemPermission(newCountry, testUser1, null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testCheckTypePermissionWithTypeNull()
	{
		permissionCheckingService.checkTypePermission((ComposedTypeModel) null, testUser1, PermissionsConstants.READ);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckTypePermissionWithTypeCodeNull()
	{
		permissionCheckingService.checkTypePermission((String) null, testUser1, PermissionsConstants.READ);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckTypePermissionWithPrincipalNull()
	{
		final ComposedTypeModel itemType = typeService.getComposedTypeForCode(ItemModel._TYPECODE);
		permissionCheckingService.checkTypePermission(itemType, null, PermissionsConstants.READ);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckTypePermissionWithPermissionNameNull()
	{
		final ComposedTypeModel itemType = typeService.getComposedTypeForCode(ItemModel._TYPECODE);
		permissionCheckingService.checkTypePermission(itemType, testUser1, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckAttributePermissionWithAttributeNull()
	{
		permissionCheckingService.checkAttributeDescriptorPermission(null, testUser1, PermissionsConstants.READ);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckAttributePermissionWithPrincipalNull()
	{
		final AttributeDescriptorModel item_pk = typeService.getAttributeDescriptor(ItemModel._TYPECODE, "pk");
		permissionCheckingService.checkAttributeDescriptorPermission(item_pk, null, PermissionsConstants.READ);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckAttributePermissionWithPermissionNameNull()
	{
		final AttributeDescriptorModel item_pk = typeService.getAttributeDescriptor(ItemModel._TYPECODE, "pk");
		permissionCheckingService.checkAttributeDescriptorPermission(item_pk, testUser1, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckAttributePermissionWithTypeCodeNull()
	{
		permissionCheckingService.checkAttributeDescriptorPermission(null, "pk", testUser1, PermissionsConstants.READ);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckAttributePermissionWithAttributeQualifierNull()
	{
		permissionCheckingService.checkAttributeDescriptorPermission(ItemModel._TYPECODE, null, testUser1,
				PermissionsConstants.READ);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckAttributePermissionByCodeWithPrincipalNull()
	{
		permissionCheckingService.checkAttributeDescriptorPermission(ItemModel._TYPECODE, "pk", null, PermissionsConstants.READ);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckAttributePermissionByCodeWithPermissionNameNull()
	{
		permissionCheckingService.checkAttributeDescriptorPermission(ItemModel._TYPECODE, "pk", testUser1, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckGlobalPermissionWithPrincipalNull()
	{
		permissionCheckingService.checkGlobalPermission(null, PermissionsConstants.READ);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckGlobalPermissionWithPermissionNameNull()
	{
		permissionCheckingService.checkGlobalPermission(testUser1, null);
	}

	@Test
	public void testCheckGlobalPermissionForAdmin()
	{
		Assert.assertTrue(permissionCheckingService
				.checkGlobalPermission(userService.getAdminUser(), NOT_IMPORTANT_PERMISSION_NAME).isGranted());
	}

	@Test
	public void testCheckAttributeDescriptorPermissionForAdmin()
	{
		Assert.assertTrue(permissionCheckingService.checkAttributeDescriptorPermission(ItemModel._TYPECODE, "pk",
				userService.getAdminUser(), NOT_IMPORTANT_PERMISSION_NAME).isGranted());
	}

	@Test
	public void testCheckItemPermissionForAdmin()
	{
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");

		Assert.assertTrue(permissionCheckingService.checkItemPermission(newCountry, userService.getAdminUser(),
				NOT_IMPORTANT_PERMISSION_NAME).isGranted());
	}

	@Test
	public void testCheckTypePermissionForAdmin()
	{
		final ComposedTypeModel itemType = typeService.getComposedTypeForCode(ItemModel._TYPECODE);
		Assert.assertTrue(permissionCheckingService.checkTypePermission(itemType, userService.getAdminUser(),
				NOT_IMPORTANT_PERMISSION_NAME).isGranted());
	}

	/**
	 * Checking for not-existing permissions
	 */
	@Test
	public void testNoExistingPermissions()
	{

		// Global permissions
		PermissionCheckValue result = permissionCheckingService.checkGlobalPermission(testUser1,
				String.valueOf(System.currentTimeMillis())).getCheckValue();

		Assert.assertEquals("Non-existing permission should yield NOT_DEFINED value", PermissionCheckValue.NOT_DEFINED, result);

		// Type permissions
		final ComposedTypeModel itemType = typeService.getComposedTypeForCode(ItemModel._TYPECODE);
		result = permissionCheckingService.checkTypePermission(itemType, testUser1, String.valueOf(System.currentTimeMillis()))
				.getCheckValue();
		Assert.assertEquals("Non-existing permission should yield NOT_DEFINED value", PermissionCheckValue.NOT_DEFINED, result);

		// Item permissions
		// create an item to assign permission with
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		modelService.save(newCountry);
		result = permissionCheckingService.checkItemPermission(newCountry, testUser1, String.valueOf(System.currentTimeMillis()))
				.getCheckValue();
		Assert.assertEquals("Non-existing permission should yield NOT_DEFINED value", PermissionCheckValue.NOT_DEFINED, result);
	}


	/**
	 * Tests global permission assignments on principal (no principal hierarchy involved)
	 */
	@Test
	public void testCheckGlobalPermissionDirectlyAssigned()
	{
		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_2);

		final PermissionAssignment pa1 = new PermissionAssignment(TEST_PERMISSION_1, testUser1);
		final PermissionAssignment pa2 = new PermissionAssignment(TEST_PERMISSION_1, testUser2, true);
		final PermissionAssignment pa3 = new PermissionAssignment(TEST_PERMISSION_2, testUser2);

		permissionManagementService.addGlobalPermission(pa1, pa2, pa3);

		final String[] permissionNames = { TEST_PERMISSION_1, TEST_PERMISSION_2 };
		final PrincipalModel[] principals = { testUser1, testUser2 };
		final PermissionCheckValue[] expectedCheckResults = { ALLOWED, DENIED, NOT_DEFINED, ALLOWED };

		for (int permissionIndex = 0; permissionIndex < permissionNames.length; permissionIndex++)
		{
			for (int principalIndex = 0; principalIndex < principals.length; principalIndex++)
			{
				final PermissionCheckValue actualResult = permissionCheckingService.checkGlobalPermission(principals[principalIndex],
						permissionNames[permissionIndex]).getCheckValue();
				final int expectedIndex = permissionIndex * (principals.length) + principalIndex;
				final PermissionCheckValue expectedResult = expectedCheckResults[expectedIndex];
				Assert.assertEquals("Actual checking result is different from expected:", expectedResult, actualResult);
			}
		}
	}


	/**
	 * Checks for correct behavior of global permission assignments involving principal groups hierarchy checking. The
	 * test involve testing for group hierarchy traversing rules, which are the same when checking for
	 * item/type/attribute permission assignments.
	 */
	@Test
	public void testCheckGlobalPermissionsWithinPrincipalHierarchy()
	{

		prepareGroups();

		final UserGroupModel group4 = modelService.create(UserGroupModel.class);
		group4.setUid("group4");
		modelService.save(group4);

		// Create permissions...
		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_2);
		permissionManagementService.createPermission(TEST_PERMISSION_3);
		permissionManagementService.createPermission(TEST_PERMISSION_4);


		/*
		 * Let's start from something simple: nice and simple global permission assignment.
		 */
		permissionManagementService.addGlobalPermission(new PermissionAssignment(TEST_PERMISSION_1, group1));
		PermissionCheckValue result = permissionCheckingService.checkGlobalPermission(testUser1, TEST_PERMISSION_1).getCheckValue();
		Assert.assertEquals("check result of TEST_PERMISSION_1 should be ALLOWED", ALLOWED, result);

		/*
		 * Now let's make a conflicting permission assignments by denying TEST_PERMISSION_1 on group3_1
		 */
		permissionManagementService.addGlobalPermission(new PermissionAssignment(TEST_PERMISSION_1, group3_1, true));
		result = permissionCheckingService.checkGlobalPermission(testUser1, TEST_PERMISSION_1).getCheckValue();
		Assert.assertEquals("check result of TEST_PERMISSION_1 should be CONFLICTING", CONFLICTING, result);

		/*
		 * Now let's save the day and use our last hope solution: Assignment directly to the principal. This should
		 * override all CONFLICTING assignments from principal groups...
		 */
		permissionManagementService.addGlobalPermission(new PermissionAssignment(TEST_PERMISSION_1, testUser1));
		result = permissionCheckingService.checkGlobalPermission(testUser1, TEST_PERMISSION_1).getCheckValue();
		Assert.assertEquals("check result of TEST_PERMISSION_5 should be ALLOWED", ALLOWED, result);

		/*
		 * Now let's try to do the same "CONFLICTING" setup as before but with TEST_PERMISSION_2, using group1 and group3.
		 * Note that group3 is not a parent (direct ancestor) of testUser1
		 */
		permissionManagementService.addGlobalPermission(new PermissionAssignment(TEST_PERMISSION_2, group1, false));
		permissionManagementService.addGlobalPermission(new PermissionAssignment(TEST_PERMISSION_2, group3, true));
		result = permissionCheckingService.checkGlobalPermission(testUser1, TEST_PERMISSION_2).getCheckValue();

		/*
		 * What should be the result? No, not CONFLICTING... Current algorithm does not do "depth-first" search. If it
		 * finds ANYTHING in set of current principal's parent groups, it DOES NOT look up the groups hierarchy... So the
		 * result is ALLOWED, because if finds a TEST_PERMISSION_2 assignment to group 1, so it does not bother to check
		 * group3.
		 */
		Assert.assertEquals("check result of TEST_PERMISSION_2 should be ALLOWED", ALLOWED, result);


		/*
		 * Now let's ensure that group hierarchy traversing really works. Let's globally deny TEST_PERMISSION_3 on group3
		 * (which is indirect ancestor of testUser1)
		 */
		permissionManagementService.addGlobalPermission(new PermissionAssignment(TEST_PERMISSION_3, group3, true));
		result = permissionCheckingService.checkGlobalPermission(testUser1, TEST_PERMISSION_3).getCheckValue();
		Assert.assertEquals("check result of TEST_PERMISSION_3 should be DENIED", DENIED, result);


		/*
		 * Now something "crazy" - let's ensure that our service DOES NOT find permission assignments unrelated to the
		 * user. Creating global TEST_PERMISSION_4 permission assignments to group4 should not influence testUser1, since
		 * he's not a member of that group.
		 */
		permissionManagementService.addGlobalPermission(new PermissionAssignment(TEST_PERMISSION_4, group4));
		result = permissionCheckingService.checkGlobalPermission(testUser1, TEST_PERMISSION_4).getCheckValue();
		Assert.assertEquals("check result of TEST_PERMISSION_4 should be NOT_DEFINED", NOT_DEFINED, result);
	}


	/**
	 * Long scenario-like test. Test different cases for type permission assignments. Unfortunately there's so many of
	 * possibilities here that it's impossible to cover them all.
	 */
	@Test
	public void testCheckTypePermissions()
	{
		// get types
		final ComposedTypeModel itemType = typeService.getComposedTypeForCode(ItemModel._TYPECODE);
		final ComposedTypeModel catalogType = typeService.getComposedTypeForCode(CatalogModel._TYPECODE);
		final ComposedTypeModel classificationSystemType = typeService.getComposedTypeForCode(ClassificationSystemModel._TYPECODE);

		prepareGroups();

		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_2);
		permissionManagementService.createPermission(TEST_PERMISSION_3);
		permissionManagementService.createPermission(TEST_PERMISSION_4);


		/*
		 * First, let's start from something simple: assignment of TEST_PERMISSION_1 and group1 to itemType - a simple
		 * test
		 */
		permissionManagementService.addTypePermission(itemType, new PermissionAssignment(TEST_PERMISSION_1, group1));

		/*
		 * Does our test user has access to classificationSystemType? He should have it, since he's allowed itemType, and
		 * so all it's sub types.
		 */
		PermissionCheckValue result = permissionCheckingService.checkTypePermission(classificationSystemType.getCode(), testUser1,
				TEST_PERMISSION_1).getCheckValue();
		Assert.assertEquals(
				"Permission assignment check result: [Type(ClassificationSystem), testUser1, TEST_PERMISSION_1], should be ALLOWED",
				ALLOWED, result);

		/*
		 * Now, let's deny the user by "denying" assignment of TEST_PERMISSION_1 and group2 to catalogType. Since
		 * catalogType is more specific than itemType, this will take the priority in checking.
		 */
		permissionManagementService.addTypePermission(catalogType, new PermissionAssignment(TEST_PERMISSION_1, group2, true));
		result = permissionCheckingService.checkTypePermission(classificationSystemType.getCode(), testUser1, TEST_PERMISSION_1)
				.getCheckValue();
		Assert.assertEquals(
				"Permission assignment check results: [Type(ClassificationSystem), testUser1, TEST_PERMISSION_1], should be DENIED",
				DENIED, result);

		/*
		 * Now, let's enable the user again by "granting" assignment of TEST_PERMISSION_1 and group3 to
		 * classificationSystemType. Since classificationSystemType is most specific type, it will take priority, even if
		 * assigned to some super-group of testUser1.
		 */
		permissionManagementService
				.addTypePermission(classificationSystemType, new PermissionAssignment(TEST_PERMISSION_1, group3));
		result = permissionCheckingService.checkTypePermission(classificationSystemType.getCode(), testUser1, TEST_PERMISSION_1)
				.getCheckValue();
		Assert.assertEquals(
				"Permission assignment check results: [Type(ClassificationSystem), testUser1, TEST_PERMISSION_1], should be ALLOWED",
				ALLOWED, result);

		/*
		 * Now, we have granted a lot to all members of group3. let's say we don't like testUser1 anymore. How can we make
		 * him denied again (without REMOVING permissions)? Since group3 grants access to classificationSystemType, we
		 * have to somehow deny access to classificationSystemType - denying some super type of it will not work, since
		 * this is most specific type. To do it we just have to find a level in principal hierarchy that is "lower" than
		 * group3. Fortunately, there's such a level: it's group3_1! Let's see if it works...
		 */
		permissionManagementService.addTypePermission(classificationSystemType, new PermissionAssignment(TEST_PERMISSION_1,
				group3_1, true));
		result = permissionCheckingService.checkTypePermission(classificationSystemType.getCode(), testUser1, TEST_PERMISSION_1)
				.getCheckValue();
		Assert.assertEquals(
				"Permission assignment check results: [Type(ClassificationSystem), testUser1, TEST_PERMISSION_1], should be DENIED",
				DENIED, result);

		/*
		 * Now we have a situation, when a parent (direct ancestor) group of testUser1 DENIES him TEST_PERMISSION_1 to
		 * classificationSystemType. We cannot override this using testUser1 groups - this will lead to a CONFLICTING
		 * check results...
		 */
		permissionManagementService
				.addTypePermission(classificationSystemType, new PermissionAssignment(TEST_PERMISSION_1, group2));
		result = permissionCheckingService.checkTypePermission(classificationSystemType.getCode(), testUser1, TEST_PERMISSION_1)
				.getCheckValue();
		Assert.assertEquals(
				"Permission assignment check results: [Type(ClassificationSystem), testUser1, TEST_PERMISSION_1], should be CONFLICTING",
				CONFLICTING, result);

		/*
		 * But we can always use brutal force and assign directly to the user Principal. Don't try this at home! :)
		 */
		permissionManagementService.addTypePermission(classificationSystemType, new PermissionAssignment(TEST_PERMISSION_1,
				testUser1));
		result = permissionCheckingService.checkTypePermission(classificationSystemType.getCode(), testUser1, TEST_PERMISSION_1)
				.getCheckValue();
		Assert.assertEquals(
				"Permission assignment check results: [Type(ClassificationSystem), testUser1, TEST_PERMISSION_1], should be ALLOWED",
				ALLOWED, result);

		/**
		 * Now a little quiz :) During this test we've created some contrived setup for group2. This group is now involved
		 * in following permission assignment:
		 * <p>
		 * catalogType: TEST_PERMISSION_1 (denied)
		 * </p>
		 * <p>
		 * classificationSystemType: TEST_PERMISSION_1 (granted)
		 * </p>
		 * We can use this setup to test if type hierarchy traversing works also in this case by checking directly group2
		 * type permission assignments.
		 */
		result = permissionCheckingService.checkTypePermission(catalogType.getCode(), testUser1, TEST_PERMISSION_1).getCheckValue();
		Assert.assertEquals("Permission assignment check results: [Type(catalog), group2, TEST_PERMISSION_1], should be DENIED",
				DENIED, result);

		/*
		 * This should be ALLOWED, because although we have DENIED on catalog type, classificationSystem is more specific
		 * and so overrides super type permission assignments.
		 */
		result = permissionCheckingService.checkTypePermission(classificationSystemType.getCode(), testUser1, TEST_PERMISSION_1)
				.getCheckValue();
		Assert.assertEquals(
				"Permission assignment check results: [Type(classificationSystem), group2, TEST_PERMISSION_1], should be ALLOWED",
				ALLOWED, result);
	}


	/**
	 * This focuses on testing if type permission assignments checks do fall-back to global permission assignments when
	 * nothing is found on the type level.
	 */
	@Test
	public void testCheckTypePermissionsGlobalPermissionsFallback()
	{
		// get types
		final ComposedTypeModel itemType = typeService.getComposedTypeForCode(ItemModel._TYPECODE);
		final ComposedTypeModel classificationSystemType = typeService.getComposedTypeForCode(ClassificationSystemModel._TYPECODE);

		prepareGroups();

		permissionManagementService.createPermission(TEST_PERMISSION_1);

		/*
		 * First, let's start from something simple: assignment of TEST_PERMISSION_1 and group3 to itemType - a simple
		 * test
		 */
		permissionManagementService.addTypePermission(itemType, new PermissionAssignment(TEST_PERMISSION_1, group3));

		/*
		 * Does our test user has access to classificationSystemType? He should have it!
		 */
		PermissionCheckValue result = permissionCheckingService.checkTypePermission(classificationSystemType.getCode(), testUser1,
				TEST_PERMISSION_1).getCheckValue();
		Assert.assertEquals(
				"Permission assignment check result: [Type(ClassificationSystem), testUser1, TEST_PERMISSION_1], should be ALLOWED",
				ALLOWED, result);

		/*
		 * Now, let's make a global denying permission assignment to TEST_PERMISSION_1 for group3_1 and see if the results
		 * are different. Result should still be ALLOWED. Note that we're adding denying permission assignment on a group
		 * that is "closer" to the principal than "allowing" group3 assignment. This verifies that entire type hierarchy
		 * is checked first, global permissions are checked later.
		 */
		permissionManagementService.addGlobalPermission(new PermissionAssignment(TEST_PERMISSION_1, group3_1, true));
		result = permissionCheckingService.checkTypePermission(classificationSystemType.getCode(), testUser1, TEST_PERMISSION_1)
				.getCheckValue();
		Assert.assertEquals(
				"Permission assignment check results: [Type(ItemType), testUser1, TEST_PERMISSION_1], should be ALLOWED", ALLOWED,
				result);

		/*
		 * Now, let's remove existing type permission assignment... We should be DENIED now
		 */
		permissionManagementService.removeTypePermission(itemType, new PermissionAssignment(TEST_PERMISSION_1, group3));
		result = permissionCheckingService.checkTypePermission(classificationSystemType.getCode(), testUser1, TEST_PERMISSION_1)
				.getCheckValue();
		Assert.assertEquals(
				"Permission assignment check results: [Type(ItemType), testUser1, TEST_PERMISSION_1], should be DENIED", DENIED,
				result);

		/*
		 * Now, let's restore "granting" permission assignment
		 */
		permissionManagementService.addTypePermission(itemType, new PermissionAssignment(TEST_PERMISSION_1, group3));
		result = permissionCheckingService.checkTypePermission(classificationSystemType.getCode(), testUser1, TEST_PERMISSION_1)
				.getCheckValue();
		Assert.assertEquals(
				"Permission assignment check results: [Type(ItemType), testUser1, TEST_PERMISSION_1], should be ALLOWED", ALLOWED,
				result);
	}

	/**
	 * Tests the item permission assignment algorithm: first item, then item's type, then global The idea is simple: We
	 * have 4 permissions: TEST_PERMISSION_1..4 We'll assign TEST_PERMISSION_1 on the item level, TEST_PERMISSION_2 on
	 * the type level, TEST_PERMISSION_3 on the global level. TEST_PERMISSION_4 will remain unassigned. Then we'll
	 * perform checks to ensure that our test user has all three permissions granted, (but not the TEST_PERMISSION_4)
	 */
	@Test
	public void testCheckItemPermission()
	{

		prepareGroups();

		// create an item to assign permission with
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		modelService.save(newCountry);


		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_2);
		permissionManagementService.createPermission(TEST_PERMISSION_3);
		permissionManagementService.createPermission(TEST_PERMISSION_4);


		/*
		 * Let's assign TEST_PERMISSION_1 to newCountry for group1
		 */
		permissionManagementService.addItemPermission(newCountry, new PermissionAssignment(TEST_PERMISSION_1, group1));

		/*
		 * Does our test user has TEST_PERMISSION_1 to newCountry? He should have it!
		 */
		PermissionCheckValue result = permissionCheckingService.checkItemPermission(newCountry, testUser1, TEST_PERMISSION_1)
				.getCheckValue();
		Assert.assertEquals("Permission assignment check result: [newCountry, testUser1, TEST_PERMISSION_1], should be ALLOWED",
				ALLOWED, result);

		/*
		 * Let's assign TEST_PERMISSION_2 to newCountry's type (Country) for group2
		 */
		permissionManagementService.addTypePermission(typeService.getComposedTypeForCode(newCountry.getItemtype()),
				new PermissionAssignment(TEST_PERMISSION_2, group2));
		/*
		 * Does our test user has TEST_PERMISSION_2 to newCountry? He should have it!
		 */
		result = permissionCheckingService.checkItemPermission(newCountry, testUser1, TEST_PERMISSION_2).getCheckValue();
		Assert.assertEquals("Permission assignment check result: [newCountry, testUser1, TEST_PERMISSION_2], should be ALLOWED",
				ALLOWED, result);

		/*
		 * Let's assign TEST_PERMISSION_3 to group3 globally
		 */
		permissionManagementService.addGlobalPermission(new PermissionAssignment(TEST_PERMISSION_3, group3));

		/*
		 * Now the user should have all three permissions on newCountry...
		 */
		result = permissionCheckingService.checkItemPermission(newCountry, testUser1, TEST_PERMISSION_1).getCheckValue();
		Assert.assertEquals("Permission assignment check result: [newCountry, testUser1, TEST_PERMISSION_1], should be ALLOWED",
				ALLOWED, result);
		result = permissionCheckingService.checkItemPermission(newCountry, testUser1, TEST_PERMISSION_2).getCheckValue();
		Assert.assertEquals("Permission assignment check result: [newCountry, testUser1, TEST_PERMISSION_2], should be ALLOWED",
				ALLOWED, result);
		result = permissionCheckingService.checkItemPermission(newCountry, testUser1, TEST_PERMISSION_3).getCheckValue();
		Assert.assertEquals("Permission assignment check result: [newCountry, testUser1, TEST_PERMISSION_3], should be ALLOWED",
				ALLOWED, result);

		/*
		 * ...But not on TEST_PERMISSION_4.
		 */
		result = permissionCheckingService.checkItemPermission(newCountry, testUser1, TEST_PERMISSION_4).getCheckValue();
		Assert.assertEquals(
				"Permission assignment check result: [newCountry, testUser1, TEST_PERMISSION_4], should be NOT_DEFINED", NOT_DEFINED,
				result);

		/*
		 * Now we can prove that item permission checking takes precedence over type permission checking. Let's try to
		 * DENY TEST_PERMISSION_1 on newCountry's type for group 1. The results for checking TEST_PERMISSION_1 should not
		 * change.
		 */
		permissionManagementService.addTypePermission(typeService.getComposedTypeForCode(newCountry.getItemtype()),
				new PermissionAssignment(TEST_PERMISSION_1, group1, true));
		result = permissionCheckingService.checkItemPermission(newCountry, testUser1, TEST_PERMISSION_1).getCheckValue();
		Assert.assertEquals("Permission assignment check result: [newCountry, testUser1, TEST_PERMISSION_1], should be ALLOWED",
				ALLOWED, result);


		/*
		 * On the other hand if we deny TEST_PERMISSION_2 on the item level, it won't be allowed anymore (permission
		 * assignments on items take precedence over permission assignments on types). We are using group2 for which
		 * there's already "granting" TEST_PERMISSION_2 assignment for newCountry's type.
		 */
		permissionManagementService.addItemPermission(newCountry, new PermissionAssignment(TEST_PERMISSION_2, group2, true));
		result = permissionCheckingService.checkItemPermission(newCountry, testUser1, TEST_PERMISSION_2).getCheckValue();
		Assert.assertEquals("Permission assignment check result: [newCountry, testUser1, TEST_PERMISSION_2], should be DENIED",
				DENIED, result);

		/*
		 * The same work for global permissions: now the user is granted TEST_PERMISSION_3 (from assignment on group3). If
		 * we put denying permission assignment on newCountry's type for group3, user will be denied. This proves that
		 * type permission assignments take precedence over global ones.
		 */

		// 1) Werify we're ALLOWED
		result = permissionCheckingService.checkItemPermission(newCountry, testUser1, TEST_PERMISSION_3).getCheckValue();
		Assert.assertEquals("Permission assignment check result: [newCountry, testUser1, TEST_PERMISSION_3], should be ALLOWED",
				ALLOWED, result);

		// 2) Deny on type level
		permissionManagementService.addTypePermission(typeService.getComposedTypeForCode(newCountry.getItemtype()),
				new PermissionAssignment(TEST_PERMISSION_3, group3, true));

		result = permissionCheckingService.checkItemPermission(newCountry, testUser1, TEST_PERMISSION_3).getCheckValue();
		Assert.assertEquals("Permission assignment check result: [newCountry, testUser1, TEST_PERMISSION_3], should be DENIED",
				DENIED, result);

		/*
		 * 3) Now we cannot overcome the "block" on type level by assigning TEST_PERMISSION_3 globally to the group 3_1,
		 * which is "closer" to the user than "denying" group3:
		 */

		permissionManagementService.addGlobalPermission(new PermissionAssignment(TEST_PERMISSION_3, group3_1));

		result = permissionCheckingService.checkItemPermission(newCountry, testUser1, TEST_PERMISSION_3).getCheckValue();
		Assert.assertEquals("Permission assignment check result: [newCountry, testUser1, TEST_PERMISSION_3], should be DENIED",
				DENIED, result);

	}

	@Test
	public void testAttributeDescriptorPermissions()
	{

		// get types
		final ComposedTypeModel itemType = typeService.getComposedTypeForCode(ItemModel._TYPECODE);
		final ComposedTypeModel catalogType = typeService.getComposedTypeForCode(CatalogModel._TYPECODE);
		final ComposedTypeModel classificationSystemType = typeService.getComposedTypeForCode(ClassificationSystemModel._TYPECODE);

		final String PK = "pk"; // NOPMD

		final AttributeDescriptorModel item_pk = typeService.getAttributeDescriptor(itemType.getCode(), PK);

		final AttributeDescriptorModel catalog_pk = typeService.getAttributeDescriptor(catalogType.getCode(), PK);
		final AttributeDescriptorModel classystem_pk = typeService.getAttributeDescriptor(classificationSystemType.getCode(), PK);

		prepareGroups();

		permissionManagementService.createPermission(TEST_PERMISSION_1);

		/*
		 * Let's try bottom-down approach to illustrate, how the attribute-assigned permissions are overriden on
		 * attribute/type level. We'll start with global permission and then we'll overwrite them on each type/attribute
		 * level to be sure how it works. For the test let's use PK attribute of ClassificationSystem type. Note that the
		 * permission assignments are placed on different user groups. The purpose of this is to show, that in this case
		 * principal hierarchy for each level (attribute, type, global) is traversed first, BEFORE checking next (upper)
		 * level. Upper levels for attribute are super-attributes and super-types (combined), upper levels for type are
		 * it's ancestor types.
		 */


		/*
		 * Add global permission
		 */
		permissionManagementService.addGlobalPermission(new PermissionAssignment(TEST_PERMISSION_1, group2));

		/*
		 * Does testUser1 have a "granted" TEST_PERMISSION_1 assignment for PK attribute of ClassificationSystem type? It
		 * seems he does.
		 */
		PermissionCheckValue result = permissionCheckingService.checkAttributeDescriptorPermission(classystem_pk, testUser1,
				TEST_PERMISSION_1).getCheckValue();
		Assert.assertEquals("Permission assignment check result: [classystem_pk, testUser1, TEST_PERMISSION_1], should be ALLOWED",
				ALLOWED, result);

		/*
		 * So far so good, lets DENY on "Item" type level.
		 */
		permissionManagementService.addTypePermission(itemType, new PermissionAssignment(TEST_PERMISSION_1, group3_1, true));

		/*
		 * The check should now report DENIED...
		 */
		result = permissionCheckingService.checkAttributeDescriptorPermission(classystem_pk, testUser1, TEST_PERMISSION_1)
				.getCheckValue();
		Assert.assertEquals("Permission assignment check result: [classystem_pk, testUser1, TEST_PERMISSION_1], should be DENIED",
				DENIED, result);

		/*
		 * So far so good, lets ALLOW on "Item.pk" attribute level. This overrides type-assigned permissions!
		 */
		permissionManagementService.addAttributePermission(item_pk, new PermissionAssignment(TEST_PERMISSION_1, group3));

		/*
		 * The check should now report ALLOW...
		 */
		result = permissionCheckingService.checkAttributeDescriptorPermission(classystem_pk, testUser1, TEST_PERMISSION_1)
				.getCheckValue();
		Assert.assertEquals("Permission assignment check result: [classystem_pk, testUser1, TEST_PERMISSION_1], should be ALLOWED",
				ALLOWED, result);

		/*
		 * So far so good, lets DENY on "Catalog" type level. This takes precedence over "Item.pk" attribute permission
		 * assignments.
		 */
		permissionManagementService.addTypePermission(catalogType, new PermissionAssignment(TEST_PERMISSION_1, group1, true));

		/*
		 * The check should now report DENIED...
		 */
		result = permissionCheckingService.checkAttributeDescriptorPermission(classystem_pk, testUser1, TEST_PERMISSION_1)
				.getCheckValue();
		Assert.assertEquals("Permission assignment check result: [classystem_pk, testUser1, TEST_PERMISSION_1], should be DENIED",
				DENIED, result);

		/*
		 * So far so good, lets ALLOW on "Catalog.pk" attribute level. This overrides type-assigned permissions!
		 */
		permissionManagementService.addAttributePermission(catalog_pk, new PermissionAssignment(TEST_PERMISSION_1, group2));

		/*
		 * The check should now report ALLOW...
		 */
		result = permissionCheckingService.checkAttributeDescriptorPermission(classystem_pk, testUser1, TEST_PERMISSION_1)
				.getCheckValue();
		Assert.assertEquals("Permission assignment check result: [classystem_pk, testUser1, TEST_PERMISSION_1], should be ALLOWED",
				ALLOWED, result);


		/*
		 * So far so good, lets DENY on "ClassificationSystem" type level. This takes precedence over "Catalog.pk"
		 * attribute permission assignments.
		 */
		permissionManagementService.addTypePermission(classificationSystemType, new PermissionAssignment(TEST_PERMISSION_1,
				group3_1, true));

		/*
		 * The check should now report DENIED...
		 */
		result = permissionCheckingService.checkAttributeDescriptorPermission(classystem_pk, testUser1, TEST_PERMISSION_1)
				.getCheckValue();
		Assert.assertEquals("Permission assignment check result: [classystem_pk, testUser1, TEST_PERMISSION_1], should be DENIED",
				DENIED, result);


		/*
		 * So far so good, lets ALLOW on "ClassificationSystem.pk" attribute level. This overrides type-assigned
		 * permissions!
		 */
		permissionManagementService.addAttributePermission(classystem_pk, new PermissionAssignment(TEST_PERMISSION_1, group3));

		/*
		 * The check should now report ALLOW...
		 */
		result = permissionCheckingService.checkAttributeDescriptorPermission(classystem_pk, testUser1, TEST_PERMISSION_1)
				.getCheckValue();
		Assert.assertEquals("Permission assignment check result: [classystem_pk, testUser1, TEST_PERMISSION_1], should be ALLOWED",
				ALLOWED, result);
	}

	protected void prepareGroups()
	{
		// Create groups
		group1 = modelService.create(UserGroupModel.class);
		group1.setUid("group1");
		modelService.save(group1);

		group2 = modelService.create(UserGroupModel.class);
		group2.setUid("group2");
		modelService.save(group2);

		group3 = modelService.create(UserGroupModel.class);
		group3.setUid("group3");
		modelService.save(group3);

		group3_1 = modelService.create(UserGroupModel.class);
		group3_1.setUid("group3_1");
		group3_1.setGroups(new HashSet<PrincipalGroupModel>(Arrays.asList(group3)));
		modelService.save(group3_1);

		// Assign user to groups
		testUser1.setGroups(new HashSet<PrincipalGroupModel>(Arrays.asList(group1, group2, group3_1)));
		modelService.save(testUser1);

		// Verify the setup
		Assert.assertEquals("group3_1 supergroup should be group3", group3, group3_1.getAllGroups().iterator().next());

		Assert.assertEquals("testUser1 should be a member of four groups",
				new HashSet<PrincipalGroupModel>(Arrays.asList(group1, group2, group3, group3_1)), testUser1.getAllGroups());

		Assert.assertEquals("testUser1 should be a direct member of three groups",
				new HashSet<PrincipalGroupModel>(Arrays.asList(group1, group2, group3_1)), testUser1.getGroups());
	}


	protected UserRightModel getPermissionForName(final String permissionName)
	{
		final String query = "SELECT {pk} FROM {" + UserRightModel._TYPECODE + "} WHERE {code}=?code";

		final Map<String, String> params = new HashMap<String, String>();
		params.put("code", permissionName);
		final SearchResult<UserRightModel> results = flexibleSearchService.search(query, params);

		if (results.getCount() == 1)
		{
			return results.getResult().get(0);
		}
		else
		{
			return null;
		}
	}
}
