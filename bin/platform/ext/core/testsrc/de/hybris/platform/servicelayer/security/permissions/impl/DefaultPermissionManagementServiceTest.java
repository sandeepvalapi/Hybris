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
package de.hybris.platform.servicelayer.security.permissions.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.security.UserRightModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.persistence.meta.MetaInformationEJB;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.persistence.property.JDBCValueMappings.ValueWriter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.security.permissions.AbstractPermissionServiceTest;
import de.hybris.platform.servicelayer.security.permissions.PermissionAssignment;
import de.hybris.platform.servicelayer.security.permissions.PermissionManagementService;
import de.hybris.platform.servicelayer.security.permissions.PermissionsConstants;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;


@IntegrationTest
public class DefaultPermissionManagementServiceTest extends AbstractPermissionServiceTest
{
	private static final Logger LOG = Logger.getLogger(DefaultPermissionManagementServiceTest.class);
    @Resource
    private UserService userService;

	//Null method arguments tests

	@Test(expected = IllegalArgumentException.class)
	public void testCreatePermissionWithPermissionNameNull()
	{
		permissionManagementService.createPermission(null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetItemPermissionsWithItemNull()
	{
		permissionManagementService.getItemPermissions(null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetItemPermissionsForPrincipalWithItemNull()
	{
		permissionManagementService.getItemPermissionsForPrincipal(null, testUser1);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetItemPermissionsForPrincipalWithPrincipalNull()
	{
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		permissionManagementService.getItemPermissionsForPrincipal(newCountry, (PrincipalModel) null);
		Assert.fail();
	}

	public void testGetItemPermissionsForPrincipalWithPrincipalArrayNull()
	{
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		permissionManagementService.getItemPermissionsForPrincipal(newCountry, (PrincipalModel[]) null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetItemPermissionsForNameWithItemNull()
	{
		permissionManagementService.getItemPermissionsForName(null, PermissionsConstants.READ);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetItemPermissionsForNameWithPermissionNameNull()
	{
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		permissionManagementService.getItemPermissionsForName(newCountry, (String) null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetItemPermissionsForNameWithPermissionNameArrayNull()
	{
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		permissionManagementService.getItemPermissionsForName(newCountry, (String[]) null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddItemPermissionWithItemNull()
	{
		permissionManagementService.addItemPermission(null, new PermissionAssignment(PermissionsConstants.READ, testUser1));
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddItemPermissionWithPermissionAssignmentNull()
	{
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		permissionManagementService.addItemPermission(newCountry, (PermissionAssignment) null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddItemPermissionWithPermissionAssignmentArrayNull()
	{
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		permissionManagementService.addItemPermission(newCountry, (PermissionAssignment[]) null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetItemPermissionWithItemNull()
	{
		permissionManagementService.setItemPermissions(null, new ArrayList<PermissionAssignment>());
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetItemPermissionWithPermissionsNull()
	{
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		permissionManagementService.setItemPermissions(newCountry, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetItemPermissionWithPermissionsContainingNull()
	{
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		final List<PermissionAssignment> badArgument = new ArrayList<PermissionAssignment>();
		badArgument.add(null);
		permissionManagementService.setItemPermissions(newCountry, badArgument);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveItemPermissionWithItemNull()
	{
		permissionManagementService.removeItemPermission(null, new PermissionAssignment(PermissionsConstants.READ, testUser1));
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveItemPermissionWithPermissionAssignmentNull()
	{
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		permissionManagementService.removeItemPermission(newCountry, (PermissionAssignment) null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveItemPermissionWithPermissionAssignmentArray()
	{
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		permissionManagementService.removeItemPermission(newCountry, (PermissionAssignment[]) null);
		Assert.fail();
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRemoveItemPermissionsWithItem()
	{
		permissionManagementService.removeItemPermissions(null, new ArrayList<PermissionAssignment>());
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveItemPermissionsWithPermissionAssignmentsNull()
	{
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		permissionManagementService.removeItemPermissions(newCountry, null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveItemPermissionsWithPermissionAssignmentsContainingNull()
	{
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		final List<PermissionAssignment> badArgument = new ArrayList<PermissionAssignment>();
		badArgument.add(null);
		permissionManagementService.removeItemPermissions(newCountry, badArgument);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveItemPermissionsForPrincipalWithItemNull()
	{
		permissionManagementService.removeItemPermissionsForPrincipal(null, testUser1);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveItemPermissionsForPrincipalWithPrincipalNull()
	{
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		permissionManagementService.removeItemPermissionsForPrincipal(newCountry, (PrincipalModel) null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveItemPermissionsForPrincipalWithPrincipalArrayNull()
	{
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		permissionManagementService.removeItemPermissionsForPrincipal(newCountry, (PrincipalModel[]) null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveItemPermissionsForNameWithItemNull()
	{
		permissionManagementService.removeItemPermissionsForName(null, PermissionsConstants.READ);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveItemPermissionsForNameWithPermissionNameNull()
	{
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		permissionManagementService.removeItemPermissionsForName(newCountry, (String) null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveItemPermissionsForNameWithPermissionNameArrayNull()
	{
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		permissionManagementService.removeItemPermissionsForName(newCountry, (String[]) null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testClearItemPermissionsWithItemNull()
	{
		permissionManagementService.clearItemPermissions(null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetGlobalPermissionsForPrincipalWithPrincipalNull()
	{
		permissionManagementService.getGlobalPermissionsForPrincipal((PrincipalModel) null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetGlobalPermissionsForPrincipalWithPrincipalArrayNull()
	{
		permissionManagementService.getGlobalPermissionsForPrincipal((PrincipalModel[]) null);
		Assert.fail();
	}


	@Test(expected = IllegalArgumentException.class)
	public void testGetGlobalPermissionsForNameWithPermissionNameNull()
	{
		permissionManagementService.getGlobalPermissionsForName((String) null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetGlobalPermissionsForNameWithPermissionNameArrayNull()
	{
		permissionManagementService.getGlobalPermissionsForName((String[]) null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddGlobalPermissionWithPermissionAssignmentNull()
	{
		permissionManagementService.addGlobalPermission((PermissionAssignment) null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddGlobalPermissionWithPermissionAssignmentArrayNull()
	{
		permissionManagementService.addGlobalPermission((PermissionAssignment[]) null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddGlobalPermissionsWithPermissionAssignmentsNull()
	{
		permissionManagementService.addGlobalPermissions(null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddGlobalPermissionsWithPermissionAssignmentsContainingNull()
	{
		final List<PermissionAssignment> badArgument = new ArrayList<PermissionAssignment>();
		badArgument.add(null);
		permissionManagementService.addGlobalPermissions(badArgument);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveGlobalPermissionWithPermissionAssignmentNull()
	{
		permissionManagementService.removeGlobalPermission((PermissionAssignment) null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveGlobalPermissionWithPermissionAssignmentArrayNull()
	{
		permissionManagementService.removeGlobalPermission((PermissionAssignment[]) null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveGlobalPermissionsWithPermissionAssignmentsNull()
	{
		permissionManagementService.removeGlobalPermissions(null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveGlobalPermissionsWithPermissionAssignmentsContainingNull()
	{
		final List<PermissionAssignment> badArgument = new ArrayList<PermissionAssignment>();
		badArgument.add(null);
		permissionManagementService.removeGlobalPermissions(badArgument);
		Assert.fail();
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRemoveGlobalPermissionsForPrincipalWithPrincipalNull()
	{
		permissionManagementService.removeGlobalPermissionsForPrincipal((PrincipalModel) null);
		Assert.fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveGlobalPermissionsForPrincipalWithPrincipalArrayNull()
	{
		permissionManagementService.removeGlobalPermissionsForPrincipal((PrincipalModel[]) null);
		Assert.fail();
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRemoveGlobalPermissionsForNameWithPermissionNameNull()
	{
		permissionManagementService.removeGlobalPermissionsForName((String) null);
		Assert.fail();

	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveGlobalPermissionsForNameWithPermissionNameArrayNull()
	{
		permissionManagementService.removeGlobalPermissionsForName((String[]) null);
		Assert.fail();
	}

	@Test
	public void testCreatePermission()
	{
		final String query = "SELECT {pk} FROM {" + UserRightModel._TYPECODE + "}";
		final int rightsCountBefore = flexibleSearchService.search(query).getCount();
		permissionManagementService.createPermission(TEST_PERMISSION_1);

		final SearchResult<UserRightModel> results = flexibleSearchService.search(query);

		Assert.assertTrue("new UserRight should have been created", results.getCount() == rightsCountBefore + 1);

		boolean found = false;
		for (final UserRightModel result : results.getResult())
		{
			if (TEST_PERMISSION_1.equals(result.getCode()))
			{
				found = true;
				break;
			}
		}

		Assert.assertTrue("new UserRight should have been found in the search results", found);
	}

	@Test(expected = ModelSavingException.class)
	public void testCreateDuplicatedPermission()
	{
		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_1);
		Assert.fail();
	}


	@Test(expected = IllegalArgumentException.class)
	public void testCreateNonExistingPermissionAssignment() throws SQLException
	{
		//create an item to assign permission with
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		modelService.save(newCountry);

		//create a permission - Ooops, forgot. What will happen?
		//permissionManagementService.createPermission(TEST_PERMISSION_1);

		//create a permission assignment object
		final PermissionAssignment permissionAssignment = new PermissionAssignment(TEST_PERMISSION_1, testUser1);

		//assign the permission.
		permissionManagementService.addItemPermission(newCountry, permissionAssignment);

		Assert.assertTrue("permission assignment should have been done", verifyAssignment(newCountry, permissionAssignment));
	}

	@Test
	public void testCreatePositivePermissionAssignment() throws SQLException
	{
		//create an item to assign permission with
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		modelService.save(newCountry);

		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);

		//create a permission assignment object
		final PermissionAssignment permissionAssignment = new PermissionAssignment(TEST_PERMISSION_1, testUser1);

		//assign the permission.
		permissionManagementService.addItemPermission(newCountry, permissionAssignment);

		Assert.assertTrue("permission assignment should have been done", verifyAssignment(newCountry, permissionAssignment));
	}

	@Test
	public void testCreateNegativePermissionAssignment() throws SQLException
	{
		//create an item to assign permission with
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		modelService.save(newCountry);

		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);

		//create a permission assignment object
		final PermissionAssignment permissionAssignment = new PermissionAssignment(TEST_PERMISSION_1, testUser1, true);

		//assign the permission.
		permissionManagementService.addItemPermission(newCountry, permissionAssignment);

		Assert.assertTrue("permission assignment should have been done", verifyAssignment(newCountry, permissionAssignment));
	}

	/**
	 * This test verifies the behavior when user tries to add two "conflicting" permission assignments on the same item
	 * using {@link PermissionManagementService#addItemPermission(ItemModel, PermissionAssignment...)} method. By
	 * "conflicting" we mean that permission assignments differ only by their value (positive/negative). The expected
	 * behavior is that the permission assignment added later overwrites the one added earlier, so we do not end up
	 * having two conflicting permission assignments on a single item.
	 */
	@Test
	public void testCreateConfilictingPermissionAssignment() throws SQLException
	{
		//create an item to assign permission with
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		modelService.save(newCountry);

		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);

		//create a permission assignment objects
		final PermissionAssignment positivePermAssignment = new PermissionAssignment(TEST_PERMISSION_1, testUser1, false);
		final PermissionAssignment negativePermAssignment = new PermissionAssignment(TEST_PERMISSION_1, testUser1, true);

		//assign the permission.
		permissionManagementService.addItemPermission(newCountry, positivePermAssignment);
		Assert.assertTrue("positive permission assignment should have been done",
				verifyAssignment(newCountry, positivePermAssignment));

		permissionManagementService.addItemPermission(newCountry, negativePermAssignment);
		Assert.assertTrue("negative permission assignment should have been done",
				verifyAssignment(newCountry, negativePermAssignment));

		Assert.assertFalse("positive permission assignment not be present", verifyAssignment(newCountry, positivePermAssignment));
	}


	@Test
	public void testSetPermissionAssignment() throws SQLException
	{
		//create an item to assign permission with
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		modelService.save(newCountry);

		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_2);
		permissionManagementService.createPermission(TEST_PERMISSION_3);


		//create a permission assignment objects
		final PermissionAssignment initialPermissionAssignment1 = new PermissionAssignment(TEST_PERMISSION_1, testUser1, false);
		final PermissionAssignment initialPermissionAssignment2 = new PermissionAssignment(TEST_PERMISSION_2, testUser1, false);
		final PermissionAssignment initialPermissionAssignment3 = new PermissionAssignment(TEST_PERMISSION_3, testUser2, false);

		permissionManagementService.addItemPermission(newCountry, initialPermissionAssignment1);
		permissionManagementService.addItemPermission(newCountry, initialPermissionAssignment2);
		permissionManagementService.addItemPermission(newCountry, initialPermissionAssignment3);

		Assert.assertTrue("initialPermissionAssignment1 permission assignment should have been done",
				verifyAssignment(newCountry, initialPermissionAssignment1));
		Assert.assertTrue("initialPermissionAssignment2 permission assignment should have been done",
				verifyAssignment(newCountry, initialPermissionAssignment2));
		Assert.assertTrue("initialPermissionAssignment3 permission assignment should have been done",
				verifyAssignment(newCountry, initialPermissionAssignment3));


		final PermissionAssignment finalPermissionAssignment1 = new PermissionAssignment(TEST_PERMISSION_1, testUser2, true);
		final PermissionAssignment finalPermissionAssignment2 = new PermissionAssignment(TEST_PERMISSION_2, testUser2, false);

		//assign the permission.
		permissionManagementService.setItemPermissions(newCountry,
				Arrays.asList(finalPermissionAssignment1, finalPermissionAssignment2));

		Assert.assertTrue("finalPermissionAssignment1 permission assignment should have been done",
				verifyAssignment(newCountry, finalPermissionAssignment1));
		Assert.assertTrue("finalPermissionAssignment2 permission assignment should have been done",
				verifyAssignment(newCountry, finalPermissionAssignment2));

		Assert.assertFalse("initialPermissionAssignment1 permission assignment not be present",
				verifyAssignment(newCountry, initialPermissionAssignment1));
		Assert.assertFalse("initialPermissionAssignment2 permission assignment not be present",
				verifyAssignment(newCountry, initialPermissionAssignment2));
		Assert.assertFalse("initialPermissionAssignment3 permission assignment not be present",
				verifyAssignment(newCountry, initialPermissionAssignment3));
	}


	@Test
	public void testRemovePermission() throws SQLException
	{
		//create an item to assign permission with
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		modelService.save(newCountry);

		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_2);
		permissionManagementService.createPermission(TEST_PERMISSION_3);


		//create a permission assignment objects
		final PermissionAssignment permissionAssignment1 = new PermissionAssignment(TEST_PERMISSION_1, testUser1, false);
		final PermissionAssignment permissionAssignment2 = new PermissionAssignment(TEST_PERMISSION_2, testUser1, false);
		final PermissionAssignment permissionAssignment3 = new PermissionAssignment(TEST_PERMISSION_2, testUser2, false);

		permissionManagementService.addItemPermission(newCountry, permissionAssignment1);
		permissionManagementService.addItemPermission(newCountry, permissionAssignment2);
		permissionManagementService.addItemPermission(newCountry, permissionAssignment3);

		Assert.assertTrue("permissionAssignment1 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment1));
		Assert.assertTrue("permissionAssignment2 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment2));
		Assert.assertTrue("permissionAssignment3 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment3));


		permissionManagementService.removeItemPermission(newCountry, permissionAssignment2);

		Assert.assertTrue("permissionAssignment1 permission assignment should be present",
				verifyAssignment(newCountry, permissionAssignment1));
		Assert.assertFalse("permissionAssignment2 permission assignment should not be present",
				verifyAssignment(newCountry, permissionAssignment2));
		Assert.assertTrue("permissionAssignment3 permission assignment should be present",
				verifyAssignment(newCountry, permissionAssignment3));
	}

	@Test
	public void testRemovePermissions() throws SQLException
	{
		//create an item to assign permission with
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		modelService.save(newCountry);

		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_2);


		//create a permission assignment objects
		final PermissionAssignment permissionAssignment1 = new PermissionAssignment(TEST_PERMISSION_1, testUser1, false);
		final PermissionAssignment permissionAssignment2 = new PermissionAssignment(TEST_PERMISSION_2, testUser1, true);
		final PermissionAssignment permissionAssignment3 = new PermissionAssignment(TEST_PERMISSION_1, testUser2, true);
		final PermissionAssignment permissionAssignment4 = new PermissionAssignment(TEST_PERMISSION_2, testUser2, false);

		permissionManagementService.addItemPermission(newCountry, permissionAssignment1);
		permissionManagementService.addItemPermission(newCountry, permissionAssignment2);
		permissionManagementService.addItemPermission(newCountry, permissionAssignment3);
		permissionManagementService.addItemPermission(newCountry, permissionAssignment4);

		Assert.assertTrue("permissionAssignment1 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment1));
		Assert.assertTrue("permissionAssignment2 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment2));
		Assert.assertTrue("permissionAssignment3 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment3));
		Assert.assertTrue("permissionAssignment4 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment4));


		permissionManagementService.removeItemPermissions(newCountry, Arrays.asList(permissionAssignment2, permissionAssignment4));

		Assert.assertTrue("permissionAssignment1 permission assignment should be present",
				verifyAssignment(newCountry, permissionAssignment1));
		Assert.assertFalse("permissionAssignment2 permission assignment should not be present",
				verifyAssignment(newCountry, permissionAssignment2));
		Assert.assertTrue("permissionAssignment3 permission assignment should be present",
				verifyAssignment(newCountry, permissionAssignment3));
		Assert.assertFalse("permissionAssignment4 permission assignment should not be present",
				verifyAssignment(newCountry, permissionAssignment4));
	}


	@Test
	public void testRemovePermissionsForPrincipal() throws SQLException
	{
		//create an item to assign permission with
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		modelService.save(newCountry);

		final PrincipalModel testUser3 = modelService.create(UserModel.class);
		testUser3.setUid(TEST_USER_3_UID);
		modelService.save(testUser3);

		final PrincipalModel testUser4 = modelService.create(UserModel.class);
		testUser4.setUid(TEST_USER_4_UID);
		modelService.save(testUser4);


		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_2);


		//create a permission assignment objects
		final PermissionAssignment permissionAssignment1 = new PermissionAssignment(TEST_PERMISSION_1, testUser1, true);
		final PermissionAssignment permissionAssignment2 = new PermissionAssignment(TEST_PERMISSION_2, testUser1, false);
		final PermissionAssignment permissionAssignment3 = new PermissionAssignment(TEST_PERMISSION_1, testUser2, true);
		final PermissionAssignment permissionAssignment4 = new PermissionAssignment(TEST_PERMISSION_2, testUser2, false);
		final PermissionAssignment permissionAssignment5 = new PermissionAssignment(TEST_PERMISSION_1, testUser3, true);
		final PermissionAssignment permissionAssignment6 = new PermissionAssignment(TEST_PERMISSION_2, testUser3, false);
		final PermissionAssignment permissionAssignment7 = new PermissionAssignment(TEST_PERMISSION_1, testUser4, true);
		final PermissionAssignment permissionAssignment8 = new PermissionAssignment(TEST_PERMISSION_2, testUser4, false);


		permissionManagementService.addItemPermission(newCountry, permissionAssignment1);
		permissionManagementService.addItemPermission(newCountry, permissionAssignment2);
		permissionManagementService.addItemPermission(newCountry, permissionAssignment3);
		permissionManagementService.addItemPermission(newCountry, permissionAssignment4);
		permissionManagementService.addItemPermission(newCountry, permissionAssignment5);
		permissionManagementService.addItemPermission(newCountry, permissionAssignment6);
		permissionManagementService.addItemPermission(newCountry, permissionAssignment7);
		permissionManagementService.addItemPermission(newCountry, permissionAssignment8);


		Assert.assertTrue("permissionAssignment1 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment1));
		Assert.assertTrue("permissionAssignment2 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment2));
		Assert.assertTrue("permissionAssignment3 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment3));
		Assert.assertTrue("permissionAssignment4 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment4));
		Assert.assertTrue("permissionAssignment5 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment5));
		Assert.assertTrue("permissionAssignment6 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment6));
		Assert.assertTrue("permissionAssignment7 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment7));
		Assert.assertTrue("permissionAssignment8 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment8));


		permissionManagementService.removeItemPermissionsForPrincipal(newCountry, testUser2, testUser4);

		Assert.assertTrue("permissionAssignment1 permission assignment should be present",
				verifyAssignment(newCountry, permissionAssignment1));
		Assert.assertTrue("permissionAssignment2 permission assignment should be present",
				verifyAssignment(newCountry, permissionAssignment2));
		Assert.assertFalse("permissionAssignment3 permission assignment not should be present",
				verifyAssignment(newCountry, permissionAssignment3));
		Assert.assertFalse("permissionAssignment4 permission assignment not should be present",
				verifyAssignment(newCountry, permissionAssignment4));
		Assert.assertTrue("permissionAssignment5 permission assignment should be present",
				verifyAssignment(newCountry, permissionAssignment5));
		Assert.assertTrue("permissionAssignment6 permission assignment should be present",
				verifyAssignment(newCountry, permissionAssignment6));
		Assert.assertFalse("permissionAssignment7 permission assignment should not be present",
				verifyAssignment(newCountry, permissionAssignment7));
		Assert.assertFalse("permissionAssignment8 permission assignment should not be present",
				verifyAssignment(newCountry, permissionAssignment8));
	}

	@Test
	public void testRemovePermissionsForName() throws SQLException
	{
		//create an item to assign permission with
		final CountryModel newCountry = modelService.create(CountryModel.class);
		newCountry.setIsocode("PL");
		modelService.save(newCountry);


		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_2);
		permissionManagementService.createPermission(TEST_PERMISSION_3);
		permissionManagementService.createPermission(TEST_PERMISSION_4);


		//create a permission assignment objects
		final PermissionAssignment permissionAssignment1_1 = new PermissionAssignment(TEST_PERMISSION_1, testUser1, true);
		final PermissionAssignment permissionAssignment2_1 = new PermissionAssignment(TEST_PERMISSION_2, testUser1, false);
		final PermissionAssignment permissionAssignment3_1 = new PermissionAssignment(TEST_PERMISSION_3, testUser1, true);
		final PermissionAssignment permissionAssignment4_1 = new PermissionAssignment(TEST_PERMISSION_4, testUser1, false);
		final PermissionAssignment permissionAssignment1_2 = new PermissionAssignment(TEST_PERMISSION_1, testUser2, true);
		final PermissionAssignment permissionAssignment2_2 = new PermissionAssignment(TEST_PERMISSION_2, testUser2, false);
		final PermissionAssignment permissionAssignment3_2 = new PermissionAssignment(TEST_PERMISSION_3, testUser2, true);
		final PermissionAssignment permissionAssignment4_2 = new PermissionAssignment(TEST_PERMISSION_4, testUser2, false);


		permissionManagementService.addItemPermission(newCountry, permissionAssignment1_1);
		permissionManagementService.addItemPermission(newCountry, permissionAssignment2_1);
		permissionManagementService.addItemPermission(newCountry, permissionAssignment3_1);
		permissionManagementService.addItemPermission(newCountry, permissionAssignment4_1);
		permissionManagementService.addItemPermission(newCountry, permissionAssignment1_2);
		permissionManagementService.addItemPermission(newCountry, permissionAssignment2_2);
		permissionManagementService.addItemPermission(newCountry, permissionAssignment3_2);
		permissionManagementService.addItemPermission(newCountry, permissionAssignment4_2);


		Assert.assertTrue("permissionAssignment1_1 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment1_1));
		Assert.assertTrue("permissionAssignment2_1 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment2_1));
		Assert.assertTrue("permissionAssignment3_1 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment3_1));
		Assert.assertTrue("permissionAssignment4_1 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment4_1));
		Assert.assertTrue("permissionAssignment1_2 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment1_2));
		Assert.assertTrue("permissionAssignment2_2 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment2_2));
		Assert.assertTrue("permissionAssignment3_2 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment3_2));
		Assert.assertTrue("permissionAssignment4_2 permission assignment should have been done",
				verifyAssignment(newCountry, permissionAssignment4_2));


		permissionManagementService.removeItemPermissionsForName(newCountry, TEST_PERMISSION_2, TEST_PERMISSION_3);

		Assert.assertTrue("permissionAssignment1_1 permission assignment should be present",
				verifyAssignment(newCountry, permissionAssignment1_1));
		Assert.assertFalse("permissionAssignment2_1 permission assignment should not be present",
				verifyAssignment(newCountry, permissionAssignment2_1));
		Assert.assertFalse("permissionAssignment3_1 permission assignment should not be present",
				verifyAssignment(newCountry, permissionAssignment3_1));
		Assert.assertTrue("permissionAssignment4_1 permission assignment should be present",
				verifyAssignment(newCountry, permissionAssignment4_1));
		Assert.assertTrue("permissionAssignment1_2 permission assignment should be present",
				verifyAssignment(newCountry, permissionAssignment1_2));
		Assert.assertFalse("permissionAssignment2_2 permission assignment should not present",
				verifyAssignment(newCountry, permissionAssignment2_2));
		Assert.assertFalse("permissionAssignment3_2 permission assignment should not present",
				verifyAssignment(newCountry, permissionAssignment3_2));
		Assert.assertTrue("permissionAssignment4_2 permission assignment should be present",
				verifyAssignment(newCountry, permissionAssignment4_2));
	}

	@Test
	public void testClearPermissions() throws SQLException
	{
		//create an item to assign permission with
		final CountryModel newCountry01 = modelService.create(CountryModel.class);
		newCountry01.setIsocode("PL");
		modelService.save(newCountry01);

		final CountryModel newCountry02 = modelService.create(CountryModel.class);
		newCountry02.setIsocode("IT");
		modelService.save(newCountry02);


		final PrincipalModel testUser3 = modelService.create(UserModel.class);
		testUser3.setUid(TEST_USER_3_UID);
		modelService.save(testUser3);

		final PrincipalModel testUser4 = modelService.create(UserModel.class);
		testUser4.setUid(TEST_USER_4_UID);
		modelService.save(testUser4);


		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_2);
		permissionManagementService.createPermission(TEST_PERMISSION_3);
		permissionManagementService.createPermission(TEST_PERMISSION_4);

		final String[] permissions =
		{ TEST_PERMISSION_1, TEST_PERMISSION_2, TEST_PERMISSION_3, TEST_PERMISSION_4 };

		final PrincipalModel[] principals =
		{ testUser1, testUser2, testUser3, testUser4 };

		final PermissionAssignment[] permissionAssignments = new PermissionAssignment[16];

		for (int i = 0; i < 16; i++)
		{
			final boolean value = (i % 2) == 0;
			permissionAssignments[i] = new PermissionAssignment(permissions[i % 4], principals[i / 4], value);
		}

		for (int i = 0; i < 16; i++)
		{
			permissionManagementService.addItemPermission(newCountry01, permissionAssignments[i]);
			permissionManagementService.addItemPermission(newCountry02, permissionAssignments[i]);
		}

		for (int i = 0; i < 16; i++)
		{
			Assert.assertTrue("permissionAssignment permission assignment should have been done",
					verifyAssignment(newCountry01, permissionAssignments[i]));
			Assert.assertTrue("permissionAssignment permission assignment should have been done",
					verifyAssignment(newCountry02, permissionAssignments[i]));
		}

		permissionManagementService.clearItemPermissions(newCountry01);

		for (int i = 0; i < 16; i++)
		{
			Assert.assertFalse("permissionAssignment permission assignment should not be present",
					verifyAssignment(newCountry01, permissionAssignments[i]));

			Assert.assertTrue("permissionAssignment permission assignment should be present",
					verifyAssignment(newCountry02, permissionAssignments[i]));
		}
	}

	@Test
	public void testCreateGlobalPositivePermissionAssignment() throws SQLException
	{
		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);

		//create a permission assignment object
		final PermissionAssignment permissionAssignment = new PermissionAssignment(TEST_PERMISSION_1, testUser1, false);

		//assign the permission.
		permissionManagementService.addGlobalPermission(permissionAssignment);

		Assert.assertTrue("permission assignment should have been done", verifyGlobalAssignment(permissionAssignment));
	}

	@Test
	public void testCreateGlobalNegativePermissionAssignment() throws SQLException
	{
		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);

		//create a permission assignment object
		final PermissionAssignment permissionAssignment = new PermissionAssignment(TEST_PERMISSION_1, testUser1, true);

		//assign the permission.
		permissionManagementService.addGlobalPermission(permissionAssignment);

		Assert.assertTrue("permission assignment should have been done", verifyGlobalAssignment(permissionAssignment));
	}

	/**
	 * This test verifies the behavior when user tries to add two "conflicting" global permission assignments. By
	 * "conflicting" we mean that permission assignments differ only by their value (positive/negative). The expected
	 * behavior is that the permission assignment added later overwrites the one added earlier, so we do not end up
	 * having two conflicting global permission assignments.
	 */
	@Test
	public void testCreateConfilictingGlobalPermissionAssignment() throws SQLException
	{
		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);

		//create a permission assignment objects
		final PermissionAssignment positivePermAssignment = new PermissionAssignment(TEST_PERMISSION_1, testUser1, false);
		final PermissionAssignment negativePermAssignment = new PermissionAssignment(TEST_PERMISSION_1, testUser1, true);

		//assign the permission.
		permissionManagementService.addGlobalPermission(positivePermAssignment);
		Assert.assertTrue("positive permission assignment should have been done", verifyGlobalAssignment(positivePermAssignment));

		permissionManagementService.addGlobalPermission(negativePermAssignment);
		Assert.assertTrue("negative permission assignment should have been done", verifyGlobalAssignment(negativePermAssignment));

		Assert.assertFalse("positive permission assignment should not be present", verifyGlobalAssignment(positivePermAssignment));
	}


	@Test
	public void testRemoveGlobalPermission() throws SQLException
	{

		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_2);
		permissionManagementService.createPermission(TEST_PERMISSION_3);


		//create a permission assignment objects
		final PermissionAssignment permissionAssignment1 = new PermissionAssignment(TEST_PERMISSION_1, testUser1, false);
		final PermissionAssignment permissionAssignment2 = new PermissionAssignment(TEST_PERMISSION_2, testUser1, false);
		final PermissionAssignment permissionAssignment3 = new PermissionAssignment(TEST_PERMISSION_2, testUser2, false);

		permissionManagementService.addGlobalPermission(permissionAssignment1);
		permissionManagementService.addGlobalPermission(permissionAssignment2);
		permissionManagementService.addGlobalPermission(permissionAssignment3);

		Assert.assertTrue("permissionAssignment1 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment1));
		Assert.assertTrue("permissionAssignment2 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment2));
		Assert.assertTrue("permissionAssignment3 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment3));


		permissionManagementService.removeGlobalPermission(permissionAssignment2);

		Assert.assertTrue("permissionAssignment1 permission assignment should be present",
				verifyGlobalAssignment(permissionAssignment1));
		Assert.assertFalse("permissionAssignment2 permission assignment should not be present",
				verifyGlobalAssignment(permissionAssignment2));
		Assert.assertTrue("permissionAssignment3 permission assignment should be present",
				verifyGlobalAssignment(permissionAssignment3));
	}

	@Test
	public void testRemoveGlobalPermissions() throws SQLException
	{
		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_2);


		//create a permission assignment objects
		final PermissionAssignment permissionAssignment1 = new PermissionAssignment(TEST_PERMISSION_1, testUser1, false);
		final PermissionAssignment permissionAssignment2 = new PermissionAssignment(TEST_PERMISSION_2, testUser1, true);
		final PermissionAssignment permissionAssignment3 = new PermissionAssignment(TEST_PERMISSION_1, testUser2, true);
		final PermissionAssignment permissionAssignment4 = new PermissionAssignment(TEST_PERMISSION_2, testUser2, false);

		permissionManagementService.addGlobalPermission(permissionAssignment1);
		permissionManagementService.addGlobalPermission(permissionAssignment2);
		permissionManagementService.addGlobalPermission(permissionAssignment3);
		permissionManagementService.addGlobalPermission(permissionAssignment4);

		Assert.assertTrue("permissionAssignment1 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment1));
		Assert.assertTrue("permissionAssignment2 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment2));
		Assert.assertTrue("permissionAssignment3 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment3));
		Assert.assertTrue("permissionAssignment4 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment4));


		permissionManagementService.removeGlobalPermissions(Arrays.asList(permissionAssignment2, permissionAssignment4));

		Assert.assertTrue("permissionAssignment1 permission assignment should be present",
				verifyGlobalAssignment(permissionAssignment1));
		Assert.assertFalse("permissionAssignment2 permission assignment should not be present",
				verifyGlobalAssignment(permissionAssignment2));
		Assert.assertTrue("permissionAssignment3 permission assignment should be present",
				verifyGlobalAssignment(permissionAssignment3));
		Assert.assertFalse("permissionAssignment4 permission assignment should not be present",
				verifyGlobalAssignment(permissionAssignment4));
	}

	@Test
	public void testRemoveGlobalPermissionsForPrincipal() throws SQLException
	{
		final PrincipalModel testUser3 = modelService.create(UserModel.class);
		testUser3.setUid(TEST_USER_3_UID);
		modelService.save(testUser3);

		final PrincipalModel testUser4 = modelService.create(UserModel.class);
		testUser4.setUid(TEST_USER_4_UID);
		modelService.save(testUser4);


		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_2);


		//create a permission assignment objects
		final PermissionAssignment permissionAssignment1 = new PermissionAssignment(TEST_PERMISSION_1, testUser1, true);
		final PermissionAssignment permissionAssignment2 = new PermissionAssignment(TEST_PERMISSION_2, testUser1, false);
		final PermissionAssignment permissionAssignment3 = new PermissionAssignment(TEST_PERMISSION_1, testUser2, true);
		final PermissionAssignment permissionAssignment4 = new PermissionAssignment(TEST_PERMISSION_2, testUser2, false);
		final PermissionAssignment permissionAssignment5 = new PermissionAssignment(TEST_PERMISSION_1, testUser3, true);
		final PermissionAssignment permissionAssignment6 = new PermissionAssignment(TEST_PERMISSION_2, testUser3, false);
		final PermissionAssignment permissionAssignment7 = new PermissionAssignment(TEST_PERMISSION_1, testUser4, true);
		final PermissionAssignment permissionAssignment8 = new PermissionAssignment(TEST_PERMISSION_2, testUser4, false);


		permissionManagementService.addGlobalPermission(permissionAssignment1);
		permissionManagementService.addGlobalPermission(permissionAssignment2);
		permissionManagementService.addGlobalPermission(permissionAssignment3);
		permissionManagementService.addGlobalPermission(permissionAssignment4);
		permissionManagementService.addGlobalPermission(permissionAssignment5);
		permissionManagementService.addGlobalPermission(permissionAssignment6);
		permissionManagementService.addGlobalPermission(permissionAssignment7);
		permissionManagementService.addGlobalPermission(permissionAssignment8);


		Assert.assertTrue("permissionAssignment1 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment1));
		Assert.assertTrue("permissionAssignment2 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment2));
		Assert.assertTrue("permissionAssignment3 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment3));
		Assert.assertTrue("permissionAssignment4 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment4));
		Assert.assertTrue("permissionAssignment5 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment5));
		Assert.assertTrue("permissionAssignment6 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment6));
		Assert.assertTrue("permissionAssignment7 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment7));
		Assert.assertTrue("permissionAssignment8 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment8));


		permissionManagementService.removeGlobalPermissionsForPrincipal(testUser2, testUser4);

		Assert.assertTrue("permissionAssignment1 permission assignment should be present",
				verifyGlobalAssignment(permissionAssignment1));
		Assert.assertTrue("permissionAssignment2 permission assignment should be present",
				verifyGlobalAssignment(permissionAssignment2));
		Assert.assertFalse("permissionAssignment3 permission assignment not should be present",
				verifyGlobalAssignment(permissionAssignment3));
		Assert.assertFalse("permissionAssignment4 permission assignment not should be present",
				verifyGlobalAssignment(permissionAssignment4));
		Assert.assertTrue("permissionAssignment5 permission assignment should be present",
				verifyGlobalAssignment(permissionAssignment5));
		Assert.assertTrue("permissionAssignment6 permission assignment should be present",
				verifyGlobalAssignment(permissionAssignment6));
		Assert.assertFalse("permissionAssignment7 permission assignment should not be present",
				verifyGlobalAssignment(permissionAssignment7));
		Assert.assertFalse("permissionAssignment8 permission assignment should not be present",
				verifyGlobalAssignment(permissionAssignment8));
	}

	@Test
	public void testRemoveGlobalPermissionsForName() throws SQLException
	{
		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_2);
		permissionManagementService.createPermission(TEST_PERMISSION_3);
		permissionManagementService.createPermission(TEST_PERMISSION_4);


		//create a permission assignment objects
		final PermissionAssignment permissionAssignment1_1 = new PermissionAssignment(TEST_PERMISSION_1, testUser1, true);
		final PermissionAssignment permissionAssignment2_1 = new PermissionAssignment(TEST_PERMISSION_2, testUser1, false);
		final PermissionAssignment permissionAssignment3_1 = new PermissionAssignment(TEST_PERMISSION_3, testUser1, true);
		final PermissionAssignment permissionAssignment4_1 = new PermissionAssignment(TEST_PERMISSION_4, testUser1, false);
		final PermissionAssignment permissionAssignment1_2 = new PermissionAssignment(TEST_PERMISSION_1, testUser2, true);
		final PermissionAssignment permissionAssignment2_2 = new PermissionAssignment(TEST_PERMISSION_2, testUser2, false);
		final PermissionAssignment permissionAssignment3_2 = new PermissionAssignment(TEST_PERMISSION_3, testUser2, true);
		final PermissionAssignment permissionAssignment4_2 = new PermissionAssignment(TEST_PERMISSION_4, testUser2, false);


		permissionManagementService.addGlobalPermission(permissionAssignment1_1);
		permissionManagementService.addGlobalPermission(permissionAssignment2_1);
		permissionManagementService.addGlobalPermission(permissionAssignment3_1);
		permissionManagementService.addGlobalPermission(permissionAssignment4_1);
		permissionManagementService.addGlobalPermission(permissionAssignment1_2);
		permissionManagementService.addGlobalPermission(permissionAssignment2_2);
		permissionManagementService.addGlobalPermission(permissionAssignment3_2);
		permissionManagementService.addGlobalPermission(permissionAssignment4_2);


		Assert.assertTrue("permissionAssignment1_1 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment1_1));
		Assert.assertTrue("permissionAssignment2_1 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment2_1));
		Assert.assertTrue("permissionAssignment3_1 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment3_1));
		Assert.assertTrue("permissionAssignment4_1 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment4_1));
		Assert.assertTrue("permissionAssignment1_2 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment1_2));
		Assert.assertTrue("permissionAssignment2_2 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment2_2));
		Assert.assertTrue("permissionAssignment3_2 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment3_2));
		Assert.assertTrue("permissionAssignment4_2 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment4_2));


		permissionManagementService.removeGlobalPermissionsForName(TEST_PERMISSION_2, TEST_PERMISSION_3);

		Assert.assertTrue("permissionAssignment1_1 permission assignment should be present",
				verifyGlobalAssignment(permissionAssignment1_1));
		Assert.assertFalse("permissionAssignment2_1 permission assignment should not be present",
				verifyGlobalAssignment(permissionAssignment2_1));
		Assert.assertFalse("permissionAssignment3_1 permission assignment should not be present",
				verifyGlobalAssignment(permissionAssignment3_1));
		Assert.assertTrue("permissionAssignment4_1 permission assignment should be present",
				verifyGlobalAssignment(permissionAssignment4_1));
		Assert.assertTrue("permissionAssignment1_2 permission assignment should be present",
				verifyGlobalAssignment(permissionAssignment1_2));
		Assert.assertFalse("permissionAssignment2_2 permission assignment should not present",
				verifyGlobalAssignment(permissionAssignment2_2));
		Assert.assertFalse("permissionAssignment3_2 permission assignment should not present",
				verifyGlobalAssignment(permissionAssignment3_2));
		Assert.assertTrue("permissionAssignment4_2 permission assignment should be present",
				verifyGlobalAssignment(permissionAssignment4_2));
	}


	@Test
	public void testGetGlobalPermissionsForPrincipal() throws SQLException
	{
		final PrincipalModel testUser3 = modelService.create(UserModel.class);
		testUser3.setUid(TEST_USER_3_UID);
		modelService.save(testUser3);

		final PrincipalModel testUser4 = modelService.create(UserModel.class);
		testUser4.setUid(TEST_USER_4_UID);
		modelService.save(testUser4);


		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_2);


		//create a permission assignment objects
		final PermissionAssignment permissionAssignment1 = new PermissionAssignment(TEST_PERMISSION_1, testUser1, true);
		final PermissionAssignment permissionAssignment2 = new PermissionAssignment(TEST_PERMISSION_2, testUser1, false);
		final PermissionAssignment permissionAssignment3 = new PermissionAssignment(TEST_PERMISSION_1, testUser2, true);
		final PermissionAssignment permissionAssignment4 = new PermissionAssignment(TEST_PERMISSION_2, testUser2, false);
		final PermissionAssignment permissionAssignment5 = new PermissionAssignment(TEST_PERMISSION_1, testUser3, true);
		final PermissionAssignment permissionAssignment6 = new PermissionAssignment(TEST_PERMISSION_2, testUser3, false);
		final PermissionAssignment permissionAssignment7 = new PermissionAssignment(TEST_PERMISSION_1, testUser4, true);
		final PermissionAssignment permissionAssignment8 = new PermissionAssignment(TEST_PERMISSION_2, testUser4, false);


		permissionManagementService.addGlobalPermission(permissionAssignment1);
		permissionManagementService.addGlobalPermission(permissionAssignment2);
		permissionManagementService.addGlobalPermission(permissionAssignment3);
		permissionManagementService.addGlobalPermission(permissionAssignment4);
		permissionManagementService.addGlobalPermission(permissionAssignment5);
		permissionManagementService.addGlobalPermission(permissionAssignment6);
		permissionManagementService.addGlobalPermission(permissionAssignment7);
		permissionManagementService.addGlobalPermission(permissionAssignment8);


		Assert.assertTrue("permissionAssignment1 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment1));
		Assert.assertTrue("permissionAssignment2 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment2));
		Assert.assertTrue("permissionAssignment3 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment3));
		Assert.assertTrue("permissionAssignment4 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment4));
		Assert.assertTrue("permissionAssignment5 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment5));
		Assert.assertTrue("permissionAssignment6 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment6));
		Assert.assertTrue("permissionAssignment7 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment7));
		Assert.assertTrue("permissionAssignment8 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment8));


		final Collection<PermissionAssignment> results = permissionManagementService.getGlobalPermissionsForPrincipal(testUser2,
				testUser4);
		Assert.assertEquals("Four permission assignments should be returned", 4, results.size());

		final Set<PermissionAssignment> expectedAssignments = new HashSet<PermissionAssignment>(Arrays.asList(
				permissionAssignment3, permissionAssignment4, permissionAssignment7, permissionAssignment8));

		for (final PermissionAssignment actual : results)
		{
			final Iterator<PermissionAssignment> iterator = expectedAssignments.iterator();
			while (iterator.hasNext())
			{
				final PermissionAssignment expected = iterator.next();
				if (expected.getPermissionName().equals(actual.getPermissionName()))
				{
					if (expected.getPrincipal().equals(actual.getPrincipal()))
					{
						if (expected.isGranted() == actual.isGranted())
						{
							iterator.remove();
						}
					}
				}
			}
		}

		Assert.assertEquals("All expected permission assignments should be matched", 0, expectedAssignments.size());
	}

	@Test
	public void testGetGlobalPermissionsForName() throws SQLException
	{
		final PrincipalModel testUser3 = modelService.create(UserModel.class);
		testUser3.setUid(TEST_USER_3_UID);
		modelService.save(testUser3);

		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_2);
		permissionManagementService.createPermission(TEST_PERMISSION_3);
		permissionManagementService.createPermission(TEST_PERMISSION_4);

		//create a permission assignment objects
		final PermissionAssignment permissionAssignment1 = new PermissionAssignment(TEST_PERMISSION_1, testUser1, true);
		final PermissionAssignment permissionAssignment2 = new PermissionAssignment(TEST_PERMISSION_2, testUser1, false);
		final PermissionAssignment permissionAssignment3 = new PermissionAssignment(TEST_PERMISSION_3, testUser1, true);
		final PermissionAssignment permissionAssignment4 = new PermissionAssignment(TEST_PERMISSION_4, testUser1, false);
		final PermissionAssignment permissionAssignment5 = new PermissionAssignment(TEST_PERMISSION_1, testUser2, true);
		final PermissionAssignment permissionAssignment6 = new PermissionAssignment(TEST_PERMISSION_2, testUser2, false);
		final PermissionAssignment permissionAssignment7 = new PermissionAssignment(TEST_PERMISSION_3, testUser2, true);
		final PermissionAssignment permissionAssignment8 = new PermissionAssignment(TEST_PERMISSION_4, testUser2, false);
		final PermissionAssignment permissionAssignment9 = new PermissionAssignment(TEST_PERMISSION_1, testUser3, true);
		final PermissionAssignment permissionAssignment10 = new PermissionAssignment(TEST_PERMISSION_2, testUser3, false);
		final PermissionAssignment permissionAssignment11 = new PermissionAssignment(TEST_PERMISSION_3, testUser3, true);
		final PermissionAssignment permissionAssignment12 = new PermissionAssignment(TEST_PERMISSION_4, testUser3, false);


		permissionManagementService.addGlobalPermission(permissionAssignment1);
		permissionManagementService.addGlobalPermission(permissionAssignment2);
		permissionManagementService.addGlobalPermission(permissionAssignment3);
		permissionManagementService.addGlobalPermission(permissionAssignment4);
		permissionManagementService.addGlobalPermission(permissionAssignment5);
		permissionManagementService.addGlobalPermission(permissionAssignment6);
		permissionManagementService.addGlobalPermission(permissionAssignment7);
		permissionManagementService.addGlobalPermission(permissionAssignment8);
		permissionManagementService.addGlobalPermission(permissionAssignment9);
		permissionManagementService.addGlobalPermission(permissionAssignment10);
		permissionManagementService.addGlobalPermission(permissionAssignment11);
		permissionManagementService.addGlobalPermission(permissionAssignment12);


		Assert.assertTrue("permissionAssignment1 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment1));
		Assert.assertTrue("permissionAssignment2 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment2));
		Assert.assertTrue("permissionAssignment3 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment3));
		Assert.assertTrue("permissionAssignment4 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment4));
		Assert.assertTrue("permissionAssignment5 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment5));
		Assert.assertTrue("permissionAssignment6 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment6));
		Assert.assertTrue("permissionAssignment7 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment7));
		Assert.assertTrue("permissionAssignment8 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment8));
		Assert.assertTrue("permissionAssignment9 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment9));
		Assert.assertTrue("permissionAssignment10 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment10));
		Assert.assertTrue("permissionAssignment11 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment11));
		Assert.assertTrue("permissionAssignment12 permission assignment should have been done",
				verifyGlobalAssignment(permissionAssignment12));


		final Collection<PermissionAssignment> results = permissionManagementService.getGlobalPermissionsForName(TEST_PERMISSION_2,
				TEST_PERMISSION_4);
		Assert.assertEquals("Four permission assignments should be returned", 6, results.size());

		final Set<PermissionAssignment> expectedAssignments = new HashSet<PermissionAssignment>(Arrays.asList(
				permissionAssignment2, permissionAssignment6, permissionAssignment10, permissionAssignment4, permissionAssignment8,
				permissionAssignment12));

		for (final PermissionAssignment actual : results)
		{
			final Iterator<PermissionAssignment> iterator = expectedAssignments.iterator();
			while (iterator.hasNext())
			{
				final PermissionAssignment expected = iterator.next();
				if (expected.getPermissionName().equals(actual.getPermissionName()))
				{
					if (expected.getPrincipal().equals(actual.getPrincipal()))
					{
						if (expected.isGranted() == actual.isGranted())
						{
							iterator.remove();
						}
					}
				}
			}
		}

		Assert.assertEquals("All expected permission assignments should be matched", 0, expectedAssignments.size());
	}

	@Test
	public void testGetDefinedPermissions()
	{

		Assert.assertEquals("no permissions should be initially available", Boolean.TRUE,
				Boolean.valueOf(permissionManagementService.getDefinedPermissions().isEmpty()));

		final Set<String> permissionsSet = new HashSet<String>(Arrays.asList(TEST_PERMISSION_1, TEST_PERMISSION_2,
				TEST_PERMISSION_3, TEST_PERMISSION_4));
		//create permissions
		for (final String permissionName : permissionsSet)
		{
			permissionManagementService.createPermission(permissionName);
		}

		final Set<String> results = new HashSet<String>(permissionManagementService.getDefinedPermissions());
		Assert.assertEquals("Four permissions should have been returned", 4, results.size());
		Assert.assertEquals("All permissions should be present in the results set", permissionsSet, results);
	}

	@Test
	public void testGetItemPermissionsForPrincipal() throws SQLException
	{
		//create additional principal
		final PrincipalModel testUser3 = modelService.create(UserModel.class);
		testUser3.setUid(TEST_USER_3_UID);
		modelService.save(testUser3);

		//create an item to assign permission with
		final CountryModel newCountry01 = modelService.create(CountryModel.class);
		newCountry01.setIsocode("PL");
		modelService.save(newCountry01);

		final CountryModel newCountry02 = modelService.create(CountryModel.class);
		newCountry02.setIsocode("IT");
		modelService.save(newCountry02);

		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_2);
		permissionManagementService.createPermission(TEST_PERMISSION_3);


		//create a permission assignment objects
		final PermissionAssignment permissionAssignment1 = new PermissionAssignment(TEST_PERMISSION_1, testUser1);
		final PermissionAssignment permissionAssignment2 = new PermissionAssignment(TEST_PERMISSION_2, testUser1);
		final PermissionAssignment permissionAssignment3 = new PermissionAssignment(TEST_PERMISSION_3, testUser1);
		final PermissionAssignment permissionAssignment4 = new PermissionAssignment(TEST_PERMISSION_1, testUser2, true);
		final PermissionAssignment permissionAssignment5 = new PermissionAssignment(TEST_PERMISSION_2, testUser2, true);
		final PermissionAssignment permissionAssignment6 = new PermissionAssignment(TEST_PERMISSION_3, testUser2, true);
		final PermissionAssignment permissionAssignment7 = new PermissionAssignment(TEST_PERMISSION_1, testUser3);
		final PermissionAssignment permissionAssignment8 = new PermissionAssignment(TEST_PERMISSION_2, testUser3);
		final PermissionAssignment permissionAssignment9 = new PermissionAssignment(TEST_PERMISSION_3, testUser3);


		final List<PermissionAssignment> permissionAssignmentList = new ArrayList<PermissionAssignment>(Arrays.asList(
				permissionAssignment1, permissionAssignment2, permissionAssignment3, permissionAssignment4, permissionAssignment5,
				permissionAssignment6, permissionAssignment7, permissionAssignment8, permissionAssignment9));

		boolean country01 = true;
		for (final PermissionAssignment permissionAssignment : permissionAssignmentList)
		{
			if (country01)
			{
				permissionManagementService.addItemPermission(newCountry01, permissionAssignment);
			}
			else
			{
				permissionManagementService.addItemPermission(newCountry02, permissionAssignment);
			}
			country01 = !country01;
		}

		country01 = true;
		for (final PermissionAssignment permissionAssignment : permissionAssignmentList)
		{
			if (country01)
			{
				Assert.assertTrue("permission assignment should have been done", verifyAssignment(newCountry01, permissionAssignment));

			}
			else
			{
				Assert.assertTrue("permission assignment should have been done", verifyAssignment(newCountry02, permissionAssignment));

			}
			country01 = !country01;
		}


		final Set<PermissionAssignment> actual = new HashSet<PermissionAssignment>(
				permissionManagementService.getItemPermissionsForPrincipal(newCountry01, testUser1, testUser2));
		Assert.assertEquals("Three permission assignments should have been found", 3, actual.size());

		final Set<PermissionAssignment> expected = new HashSet<PermissionAssignment>();

		expected.add(permissionAssignment1);
		expected.add(permissionAssignment3);
		expected.add(permissionAssignment5);

		Assert.assertEquals("All permission assignments should have been found", expected, actual);
	}

	@Test
	public void testGetItemPermissionsForName() throws SQLException
	{
		//create additional principal
		final PrincipalModel testUser3 = modelService.create(UserModel.class);
		testUser3.setUid(TEST_USER_3_UID);
		modelService.save(testUser3);

		//create an item to assign permission with
		final CountryModel newCountry01 = modelService.create(CountryModel.class);
		newCountry01.setIsocode("PL");
		modelService.save(newCountry01);

		final CountryModel newCountry02 = modelService.create(CountryModel.class);
		newCountry02.setIsocode("IT");
		modelService.save(newCountry02);

		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_2);
		permissionManagementService.createPermission(TEST_PERMISSION_3);


		//create a permission assignment objects
		final PermissionAssignment permissionAssignment1 = new PermissionAssignment(TEST_PERMISSION_1, testUser1);
		final PermissionAssignment permissionAssignment2 = new PermissionAssignment(TEST_PERMISSION_2, testUser1);
		final PermissionAssignment permissionAssignment3 = new PermissionAssignment(TEST_PERMISSION_3, testUser1);
		final PermissionAssignment permissionAssignment4 = new PermissionAssignment(TEST_PERMISSION_1, testUser2, true);
		final PermissionAssignment permissionAssignment5 = new PermissionAssignment(TEST_PERMISSION_2, testUser2, true);
		final PermissionAssignment permissionAssignment6 = new PermissionAssignment(TEST_PERMISSION_3, testUser2, true);
		final PermissionAssignment permissionAssignment7 = new PermissionAssignment(TEST_PERMISSION_1, testUser3);
		final PermissionAssignment permissionAssignment8 = new PermissionAssignment(TEST_PERMISSION_2, testUser3);
		final PermissionAssignment permissionAssignment9 = new PermissionAssignment(TEST_PERMISSION_3, testUser3);


		final List<PermissionAssignment> permissionAssignmentList = new ArrayList<PermissionAssignment>(Arrays.asList(
				permissionAssignment1, permissionAssignment2, permissionAssignment3, permissionAssignment4, permissionAssignment5,
				permissionAssignment6, permissionAssignment7, permissionAssignment8, permissionAssignment9));

		boolean country01 = true;
		for (final PermissionAssignment permissionAssignment : permissionAssignmentList)
		{
			if (country01)
			{
				permissionManagementService.addItemPermission(newCountry01, permissionAssignment);
			}
			else
			{
				permissionManagementService.addItemPermission(newCountry02, permissionAssignment);
			}
			country01 = !country01;
		}

		country01 = true;
		for (final PermissionAssignment permissionAssignment : permissionAssignmentList)
		{
			if (country01)
			{
				Assert.assertTrue("permission assignment should have been done", verifyAssignment(newCountry01, permissionAssignment));

			}
			else
			{
				Assert.assertTrue("permission assignment should have been done", verifyAssignment(newCountry02, permissionAssignment));

			}
			country01 = !country01;
		}


		final Set<PermissionAssignment> actual = new HashSet<PermissionAssignment>(
				permissionManagementService.getItemPermissionsForName(newCountry01, TEST_PERMISSION_1, TEST_PERMISSION_3));
		Assert.assertEquals("Four permission assignments should have been found", 4, actual.size());

		final Set<PermissionAssignment> expected = new HashSet<PermissionAssignment>();

		expected.add(permissionAssignment1);
		expected.add(permissionAssignment3);
		expected.add(permissionAssignment7);
		expected.add(permissionAssignment9);

		Assert.assertEquals("All permission assignments should have been found", expected, actual);
	}

	@Test
	public void testGetItemPermissions() throws SQLException
	{
		//create additional principal
		final PrincipalModel testUser3 = modelService.create(UserModel.class);
		testUser3.setUid(TEST_USER_3_UID);
		modelService.save(testUser3);

		//create an item to assign permission with
		final CountryModel newCountry01 = modelService.create(CountryModel.class);
		newCountry01.setIsocode("PL");
		modelService.save(newCountry01);

		final CountryModel newCountry02 = modelService.create(CountryModel.class);
		newCountry02.setIsocode("IT");
		modelService.save(newCountry02);

		//create a permission
		permissionManagementService.createPermission(TEST_PERMISSION_1);
		permissionManagementService.createPermission(TEST_PERMISSION_2);
		permissionManagementService.createPermission(TEST_PERMISSION_3);


		//create a permission assignment objects
		final PermissionAssignment permissionAssignment1 = new PermissionAssignment(TEST_PERMISSION_1, testUser1);
		final PermissionAssignment permissionAssignment2 = new PermissionAssignment(TEST_PERMISSION_2, testUser1);
		final PermissionAssignment permissionAssignment3 = new PermissionAssignment(TEST_PERMISSION_3, testUser1);
		final PermissionAssignment permissionAssignment4 = new PermissionAssignment(TEST_PERMISSION_1, testUser2, true);
		final PermissionAssignment permissionAssignment5 = new PermissionAssignment(TEST_PERMISSION_2, testUser2, true);
		final PermissionAssignment permissionAssignment6 = new PermissionAssignment(TEST_PERMISSION_3, testUser2, true);
		final PermissionAssignment permissionAssignment7 = new PermissionAssignment(TEST_PERMISSION_1, testUser3);
		final PermissionAssignment permissionAssignment8 = new PermissionAssignment(TEST_PERMISSION_2, testUser3);
		final PermissionAssignment permissionAssignment9 = new PermissionAssignment(TEST_PERMISSION_3, testUser3);


		final List<PermissionAssignment> permissionAssignmentList = new ArrayList<PermissionAssignment>(Arrays.asList(
				permissionAssignment1, permissionAssignment2, permissionAssignment3, permissionAssignment4, permissionAssignment5,
				permissionAssignment6, permissionAssignment7, permissionAssignment8, permissionAssignment9));

		boolean country01 = true;
		for (final PermissionAssignment permissionAssignment : permissionAssignmentList)
		{
			if (country01)
			{
				permissionManagementService.addItemPermission(newCountry01, permissionAssignment);
			}
			else
			{
				permissionManagementService.addItemPermission(newCountry02, permissionAssignment);
			}
			country01 = !country01;
		}

		country01 = true;
		for (final PermissionAssignment permissionAssignment : permissionAssignmentList)
		{
			if (country01)
			{
				Assert.assertTrue("permission assignment should have been done", verifyAssignment(newCountry01, permissionAssignment));

			}
			else
			{
				Assert.assertTrue("permission assignment should have been done", verifyAssignment(newCountry02, permissionAssignment));

			}
			country01 = !country01;
		}


		final Set<PermissionAssignment> actual = new HashSet<PermissionAssignment>(
				permissionManagementService.getItemPermissions(newCountry02));

		Assert.assertEquals("Four permission assignments should have been found", 4, actual.size());

		final Set<PermissionAssignment> expected = new HashSet<PermissionAssignment>();

		expected.add(permissionAssignment2);
		expected.add(permissionAssignment4);
		expected.add(permissionAssignment6);
		expected.add(permissionAssignment8);

		Assert.assertEquals("All permission assignments should have been found", expected, actual);
	}

    @Test
    public void shouldAllowToRemovePermissionsByPassingEmptyCollectionOfAssignments() throws Exception
    {
    	// given
        final TitleModel title = modelService.create(TitleModel.class);
        title.setCode("testTitle");
        modelService.saveAll(title);
        createPermissions(PermissionsConstants.READ, PermissionsConstants.CHANGE, PermissionsConstants.REMOVE);
        permissionManagementService.addItemPermission(title,
                new PermissionAssignment(PermissionsConstants.READ, userService.getAdminUser(), true));
        permissionManagementService.addItemPermission(title,
                new PermissionAssignment(PermissionsConstants.CHANGE, userService.getAdminUser(), true));
        permissionManagementService.addItemPermission(title,
                new PermissionAssignment(PermissionsConstants.REMOVE, userService.getAdminUser(), true));

    	// when
        permissionManagementService.setItemPermissions(title, Collections.emptyList());
        final Collection<PermissionAssignment> permissions = permissionManagementService.getItemPermissions(title);

        // then
        assertThat(permissions).isEmpty();
    }


    private void createPermissions(final String... permissions)
    {
        Arrays.asList(permissions).stream().forEach(code -> {
            final UserRightModel ur = modelService.create(UserRightModel.class);
            ur.setCode(code);
            modelService.save(ur);
        });
    }

	private boolean verifyAssignment(final ItemModel item, final PermissionAssignment permissionAssignment) throws SQLException
	{

		final Map<String, String> params = new HashMap<String, String>();
		params.put("code", permissionAssignment.getPermissionName());
		final FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {" + UserRightModel._TYPECODE
				+ "} WHERE {CODE} = ?code", params);
		final UserRightModel userRight = flexibleSearchService.searchUnique(query);

		return verifyAssignment(item.getPk(), permissionAssignment.getPrincipal().getPk(), userRight.getPk(),
				permissionAssignment.isDenied());
	}

	private boolean verifyAssignment(final PK itemPK, final PK principalPK, final PK permissionPK, final boolean negative)
			throws SQLException
	{
		final String PERMISSION = "PermissionPK";
		final String NEGATIVE = "Negative";
		final String PRINCIPAL = "PrincipalPK";
		final String ITEM = "ItemPK";

		final String query = "SELECT COUNT(*) FROM " + getACLENTRIESTABLE() + " WHERE " + NEGATIVE + "=? " + " AND " + ITEM
				+ "=? AND " + PRINCIPAL + "=? AND " + PERMISSION + "=?";

		final DataSource dataSource = getTenant().getDataSource();
		Connection conn = null;
		PreparedStatement stmt = null;
		try
		{
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(query);
			fillSelectQuery(stmt, negative, itemPK, principalPK, permissionPK);
			final ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next())
			{
				final int count = resultSet.getInt(1);
				return count == 1;
			}
			else
			{
				return false;
			}
		}
		finally
		{
			if (stmt != null)
			{
				try
				{
					stmt.close();
				}
				catch (final SQLException ex)
				{
					LOG.error(ex);
				}
			}

			if (conn != null)
			{
				try
				{
					conn.close();
				}
				catch (final SQLException ex)
				{
					LOG.error(ex);
				}
			}
		}
	}


	private boolean verifyGlobalAssignment(final PermissionAssignment permissionAssignment) throws SQLException
	{

		final Map<String, String> params = new HashMap<String, String>();
		params.put("code", permissionAssignment.getPermissionName());
		final FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {" + UserRightModel._TYPECODE
				+ "} WHERE {CODE} = ?code", params);
		final UserRightModel userRight = flexibleSearchService.searchUnique(query);

		return verifyAssignment(MetaInformationEJB.DEFAULT_PRIMARY_KEY, permissionAssignment.getPrincipal().getPk(),
				userRight.getPk(), permissionAssignment.isDenied());
	}


	//From: de.hybris.platform.persistence.security.ACLEntryJDBC
	private final String getACLENTRIESTABLE()
	{
		return Config.getString("db.tableprefix", "") + "aclentries";
	}

	//From: de.hybris.platform.persistence.security.ACLEntryJDBC
	private final void fillSelectQuery(final PreparedStatement stmt, final boolean negative, final PK itemPK,
			final PK principalPK, final PK permissionPK) throws SQLException
	{
		final JDBCValueMappings valueMapping = JDBCValueMappings.getInstance();
		final ValueWriter<PK, ?> pkWriter = valueMapping.PK_WRITER;
		valueMapping.getValueWriter(Boolean.class).setValue(stmt, 1, Boolean.valueOf(negative));

		pkWriter.setValue(stmt, 2, itemPK);
		pkWriter.setValue(stmt, 3, principalPK);
		pkWriter.setValue(stmt, 4, permissionPK);

		//stmt.setString( 2, itemPK.toString() );
		//stmt.setString( 3, ec.getPrincipal().toString() );
		//stmt.setString( 4, ec.getPermission().toString() );
	}

}
