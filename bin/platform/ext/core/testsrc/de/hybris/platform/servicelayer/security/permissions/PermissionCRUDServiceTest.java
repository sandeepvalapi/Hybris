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

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


/**
 * Tests {@link PermissionCRUDService} service public API
 */
@IntegrationTest
public class PermissionCRUDServiceTest extends AbstractPermissionServiceTest
{
	@Resource
	private PermissionCRUDService permissionCRUDService;

	@Resource
	private PermissionManagementService permissionManagementService;


	@Resource
	private TypeService typeService;

	@Resource
	private UserService userService;


	@Test
	public void testCanReadOrChangeType()
	{
		//SETUP

		final ComposedTypeModel catalogType = typeService.getComposedTypeForCode(CatalogModel._TYPECODE);
		final ComposedTypeModel classificationSystemType = typeService.getComposedTypeForCode(ClassificationSystemModel._TYPECODE);

		final UserModel currentUser = userService.getCurrentUser();
		//Let's ensure we're not admins!
		Assert.assertFalse(userService.isAdmin(currentUser));

		//TEST

		/*
		 * Let's check if we can READ ClassificationSystem Type Without any permissions assigned we should be denied.
		 */
		Assert.assertFalse("Current user should be not allowed to READ ClassificationSystem Type",
				permissionCRUDService.canReadType(classificationSystemType));
		Assert.assertFalse("Current user should be not allowed to READ ClassificationSystem Type",
				permissionCRUDService.canReadType(classificationSystemType.getCode()));

		/*
		 * Let's grant READ permission to Catalog type for current user.
		 */
		permissionManagementService.createPermission(PermissionsConstants.READ);
		permissionManagementService
				.addTypePermission(catalogType, new PermissionAssignment(PermissionsConstants.READ, currentUser));

		/*
		 * With a permissions assigned we should be granted.
		 */
		Assert.assertTrue("Current user should be allowed to READ ClassificationSystem Type",
				permissionCRUDService.canReadType(classificationSystemType));
		Assert.assertTrue("Current user should be allowed to READ ClassificationSystem Type",
				permissionCRUDService.canReadType(classificationSystemType.getCode()));

		/*
		 * Now let's try to test if we can CHANGE ClassificationSystem Type Without any permissions assigned we should be
		 * denied.
		 */
		Assert.assertFalse("Current user should be not allowed to CHANGE ClassificationSystem Type",
				permissionCRUDService.canChangeType(classificationSystemType));
		Assert.assertFalse("Current user should be not allowed to CHANGE ClassificationSystem Type",
				permissionCRUDService.canChangeType(classificationSystemType.getCode()));

		/*
		 * Let's solve the problem using global permission assignment. This is a fall-back option, but should work.
		 */
		permissionManagementService.createPermission(PermissionsConstants.CHANGE);
		permissionManagementService.addGlobalPermission(new PermissionAssignment(PermissionsConstants.CHANGE, currentUser));

		Assert.assertTrue("Current user should be allowed to CHANGE ClassificationSystem Type",
				permissionCRUDService.canChangeType(classificationSystemType));
		Assert.assertTrue("Current user should be allowed to CHANGE ClassificationSystem Type",
				permissionCRUDService.canChangeType(classificationSystemType.getCode()));

	}

	@Test
	public void testCanCreateTypeInstance()
	{
		//SETUP

		final ComposedTypeModel catalogType = typeService.getComposedTypeForCode(CatalogModel._TYPECODE);
		final ComposedTypeModel classificationSystemType = typeService.getComposedTypeForCode(ClassificationSystemModel._TYPECODE);

		permissionManagementService.createPermission(PermissionsConstants.CREATE);

		final UserModel currentUser = userService.getCurrentUser();
		//Let's ensure we're not admins!
		Assert.assertFalse(userService.isAdmin(currentUser));

		//TEST

		/*
		 * Let's check if we can CREATE ClassificationSystem Type items: Without any permissions assigned we should be
		 * denied.
		 */
		Assert.assertFalse("Current user should be not allowed to CREATE ClassificationSystem Type instances",
				permissionCRUDService.canCreateTypeInstance(classificationSystemType));
		Assert.assertFalse("Current user should be not allowed to CREATE ClassificationSystem Type instances",
				permissionCRUDService.canCreateTypeInstance(classificationSystemType.getCode()));

		/*
		 * Let's grant CREATE permission to Catalog type for current user.
		 */
		permissionManagementService.addTypePermission(catalogType, new PermissionAssignment(PermissionsConstants.CREATE,
				currentUser));

		/*
		 * With a permissions assigned we should be granted.
		 */
		Assert.assertTrue("Current user should be allowed to CREATE ClassificationSystem Type instances",
				permissionCRUDService.canCreateTypeInstance(classificationSystemType));
		Assert.assertTrue("Current user should be allowed to CREATE ClassificationSystem Type instances",
				permissionCRUDService.canCreateTypeInstance(classificationSystemType.getCode()));

	}

	@Test
	public void testCanRemoveTypeInstance()
	{
		//SETUP

		final ComposedTypeModel catalogType = typeService.getComposedTypeForCode(CatalogModel._TYPECODE);
		final ComposedTypeModel classificationSystemType = typeService.getComposedTypeForCode(ClassificationSystemModel._TYPECODE);

		permissionManagementService.createPermission(PermissionsConstants.REMOVE);

		final UserModel currentUser = userService.getCurrentUser();
		//Let's ensure we're not admins!
		Assert.assertFalse(userService.isAdmin(currentUser));

		//TEST

		/*
		 * Let's check if we can REMOVE ClassificationSystem Type items: Without any permissions assigned we should be
		 * denied.
		 */
		Assert.assertFalse("Current user should be not allowed to REMOVE ClassificationSystem Type instances",
				permissionCRUDService.canRemoveTypeInstance(classificationSystemType));
		Assert.assertFalse("Current user should be not allowed to REMOVE ClassificationSystem Type instances",
				permissionCRUDService.canRemoveTypeInstance(classificationSystemType.getCode()));

		/*
		 * Let's grant REMOVE permission to Catalog type for current user.
		 */
		permissionManagementService.addTypePermission(catalogType, new PermissionAssignment(PermissionsConstants.REMOVE,
				currentUser));

		/*
		 * With a permissions assigned we should be granted.
		 */
		Assert.assertTrue("Current user should be allowed to REMOVE ClassificationSystem Type instances",
				permissionCRUDService.canRemoveTypeInstance(classificationSystemType));
		Assert.assertTrue("Current user should be allowed to REMOVE ClassificationSystem Type instances",
				permissionCRUDService.canRemoveTypeInstance(classificationSystemType.getCode()));
	}

	@Test
	public void testCanChangeTypePermission()
	{
		//SETUP

		final ComposedTypeModel catalogType = typeService.getComposedTypeForCode(CatalogModel._TYPECODE);
		final ComposedTypeModel classificationSystemType = typeService.getComposedTypeForCode(ClassificationSystemModel._TYPECODE);

		permissionManagementService.createPermission(PermissionsConstants.CHANGE_PERMISSIONS);

		final UserModel currentUser = userService.getCurrentUser();
		//Let's ensure we're not admins!
		Assert.assertFalse(userService.isAdmin(currentUser));

		//TEST

		/*
		 * Let's check if we can CHANGE_PERMISSIONS on ClassificationSystem Type: Without any permissions assigned we
		 * should be denied.
		 */
		Assert.assertFalse("Current user should be not allowed to CHANGE_PERMISSIONS on ClassificationSystem Type",
				permissionCRUDService.canChangeTypePermission(classificationSystemType));
		Assert.assertFalse("Current user should be not allowed to CHANGE_PERMISSIONS on ClassificationSystem Type",
				permissionCRUDService.canChangeTypePermission(classificationSystemType.getCode()));

		/*
		 * Let's grant CHANGE_PERMISSIONS permission to Catalog type for current user.
		 */
		permissionManagementService.addTypePermission(catalogType, new PermissionAssignment(
				PermissionsConstants.CHANGE_PERMISSIONS, currentUser));

		/*
		 * With a permissions assigned we should be granted.
		 */
		Assert.assertTrue("Current user should be allowed to CHANGE_PERMISSIONS on ClassificationSystem Type",
				permissionCRUDService.canChangeTypePermission(classificationSystemType));
		Assert.assertTrue("Current user should be allowed to CHANGE_PERMISSIONS on ClassificationSystem Type",
				permissionCRUDService.canChangeTypePermission(classificationSystemType.getCode()));
	}

	@Test
	public void testCanReadAtribute()
	{
		testAttributeDescriptor(PermissionsConstants.READ, new AttributeCheckHelper()
		{
			@Override
			public boolean isGranted(final AttributeDescriptorModel attribute)
			{
				return permissionCRUDService.canReadAttribute(attribute);
			}

			@Override
			public boolean isGranted(final String typeCode, final String attributeQualifier)
			{
				return permissionCRUDService.canReadAttribute(typeCode, attributeQualifier);
			}
		});
	}

	@Test
	public void testCanChangeAtribute()
	{
		testAttributeDescriptor(PermissionsConstants.CHANGE, new AttributeCheckHelper()
		{
			@Override
			public boolean isGranted(final AttributeDescriptorModel attribute)
			{
				return permissionCRUDService.canChangeAttribute(attribute);
			}

			@Override
			public boolean isGranted(final String typeCode, final String attributeQualifier)
			{
				return permissionCRUDService.canChangeAttribute(typeCode, attributeQualifier);
			}
		});
	}

	@Test
	public void testCanChangeAttributePermission()
	{
		testAttributeDescriptor(PermissionsConstants.CHANGE_PERMISSIONS, new AttributeCheckHelper()
		{
			@Override
			public boolean isGranted(final AttributeDescriptorModel attribute)
			{
				return permissionCRUDService.canChangeAttributePermission(attribute);
			}

			@Override
			public boolean isGranted(final String typeCode, final String attributeQualifier)
			{
				return permissionCRUDService.canChangeAttributePermission(typeCode, attributeQualifier);
			}
		});
	}

	private void testAttributeDescriptor(final String permissionName, final AttributeCheckHelper checkHelper)
	{
		//SETUP
		final String CATALOG_NAME = "name";

		final ComposedTypeModel catalogType = typeService.getComposedTypeForCode(CatalogModel._TYPECODE);
		final ComposedTypeModel classificationSystemType = typeService.getComposedTypeForCode(ClassificationSystemModel._TYPECODE);

		final AttributeDescriptorModel catalogNameAttribute = typeService.getAttributeDescriptor(catalogType, CATALOG_NAME);
		final AttributeDescriptorModel classificationSystemNameAttribute = typeService.getAttributeDescriptor(
				classificationSystemType, CATALOG_NAME);

		permissionManagementService.createPermission(permissionName);

		final UserModel currentUser = userService.getCurrentUser();
		//Let's ensure we're not admins!
		Assert.assertFalse(userService.isAdmin(currentUser));

		//TEST

		/*
		 * Let's check if we are granted permission permissionName to ClassificationSystem.name attribute: Without any
		 * permissions assigned we should be denied.
		 */
		Assert.assertFalse("Current user should not be granted \"" + permissionName
				+ "\" permission to ClassificationSystem.name attribute", checkHelper.isGranted(classificationSystemNameAttribute));
		Assert.assertFalse("Current user should not be granted \"" + permissionName
				+ "\" permission to ClassificationSystem.name attribute", checkHelper.isGranted(classificationSystemNameAttribute
				.getEnclosingType().getCode(), classificationSystemNameAttribute.getQualifier()));

		/*
		 * Let's grant global permission permissionName to for current user.
		 */
		permissionManagementService.addGlobalPermission(new PermissionAssignment(permissionName, currentUser));

		/*
		 * With a global permission assignment, permission should be granted.
		 */
		Assert.assertTrue("Current user should be granted \"" + permissionName
				+ "\" permission to ClassificationSystem.name attribute", checkHelper.isGranted(classificationSystemNameAttribute));
		Assert.assertTrue("Current user should be granted \"" + permissionName
				+ "\" permission to ClassificationSystem.name attribute", checkHelper.isGranted(classificationSystemNameAttribute
				.getEnclosingType().getCode(), classificationSystemNameAttribute.getQualifier()));


		/*
		 * Let's deny permission permissionName to Catalog type for current user. This should override global permissions
		 */
		permissionManagementService.addTypePermission(catalogType, new PermissionAssignment(permissionName, currentUser, true));


		/*
		 * Now the permission should be denied.
		 */
		Assert.assertFalse("Current user should not be granted \"" + permissionName
				+ "\" permission to ClassificationSystem.name attribute", checkHelper.isGranted(classificationSystemNameAttribute));
		Assert.assertFalse("Current user should not be granted \"" + permissionName
				+ "\" permission to ClassificationSystem.name attribute", checkHelper.isGranted(classificationSystemNameAttribute
				.getEnclosingType().getCode(), classificationSystemNameAttribute.getQualifier()));


		/*
		 * Let's grant permission permissionName to Catalog.name attribute for current user. This overrides Catalog type
		 * permission assignments (when checking for attribute permissions).
		 */
		permissionManagementService.addAttributePermission(catalogNameAttribute, new PermissionAssignment(permissionName,
				currentUser));

		/*
		 * Permission should be granted now.
		 */
		Assert.assertTrue("Current user should be granted \"" + permissionName
				+ "\" permission to ClassificationSystem.name attribute", checkHelper.isGranted(classificationSystemNameAttribute));
		Assert.assertTrue("Current user should be granted \"" + permissionName
				+ "\" permission to ClassificationSystem.name attribute", checkHelper.isGranted(classificationSystemNameAttribute
				.getEnclosingType().getCode(), classificationSystemNameAttribute.getQualifier()));

		/*
		 * Let's deny permission permissionName to ClassificationSystem type for current user.
		 */
		permissionManagementService.addTypePermission(classificationSystemType, new PermissionAssignment(permissionName,
				currentUser, true));

		/*
		 * Permission should be denied.
		 */
		Assert.assertFalse("Current user should not be granted \"" + permissionName
				+ "\" permission to ClassificationSystem.name attribute", checkHelper.isGranted(classificationSystemNameAttribute));
		Assert.assertFalse("Current user should not be granted \"" + permissionName
				+ "\" permission to ClassificationSystem.name attribute", checkHelper.isGranted(classificationSystemNameAttribute
				.getEnclosingType().getCode(), classificationSystemNameAttribute.getQualifier()));


		/*
		 * Let's grant permission permissionName to ClassificationSystem.name attribute for current user.
		 */
		permissionManagementService.addAttributePermission(classificationSystemNameAttribute, new PermissionAssignment(
				permissionName, currentUser));

		/*
		 * With a permissions assigned we should be granted.
		 */
		Assert.assertTrue("Current user should be granted \"" + permissionName
				+ "\" permission to ClassificationSystem.name attribute", checkHelper.isGranted(classificationSystemNameAttribute));
		Assert.assertTrue("Current user should be granted \"" + permissionName
				+ "\" permission to ClassificationSystem.name attribute", checkHelper.isGranted(classificationSystemNameAttribute
				.getEnclosingType().getCode(), classificationSystemNameAttribute.getQualifier()));
	}

	/**
	 * Helper interface to automate attribute descriptor testing
	 */
	private interface AttributeCheckHelper
	{
		boolean isGranted(AttributeDescriptorModel attribute);

		boolean isGranted(String typeCode, String attributeQualifier);
	}
}
