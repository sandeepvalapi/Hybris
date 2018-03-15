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
package de.hybris.platform.variants.jalo;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.security.permissions.PermissionAssignment;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.security.permissions.PermissionManagementService;
import de.hybris.platform.servicelayer.security.permissions.PermissionsConstants;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.variants.model.VariantAttributeDescriptorModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class VariantsAttributePermissionCRUDServiceTest extends ServicelayerBaseTest
{

	@Resource
	private PermissionCRUDService permissionCRUDService;

	@Resource
	private PermissionManagementService permissionManagementService;

	@Resource
	private ModelService modelService;


	@Resource
	private TypeService typeService;

	@Resource
	private UserService userService;

	private ComposedTypeModel type, subType;
	private AttributeDescriptorModel attribute, subTypeAttribute;

	private VariantTypeModel variantType, variantSubType;
	private VariantAttributeDescriptorModel varAttribute, varSubTypeAttribute;

	@Before
	public void setUp()
	{
		permissionManagementService.createPermission(PermissionsConstants.READ);
		permissionManagementService.createPermission(PermissionsConstants.CHANGE);
		permissionManagementService.createPermission(PermissionsConstants.CHANGE_PERMISSIONS);
	}

	private void setUpVariantAttribute()
	{
		final VariantType varTypeJalo = VariantsManager.getInstance().createVariantType("SomeVariant");
		Map<String, Object> params = new HashMap<>();
		params.put(VariantAttributeDescriptor.QUALIFIER, "someVarAttr");
		params.put(VariantAttributeDescriptor.ENCLOSING_TYPE, varTypeJalo);
		params.put(VariantAttributeDescriptor.ATTRIBUTETYPE, TypeManager.getInstance().getType("java.lang.String"));
		final VariantAttributeDescriptor varAttributeJalo = VariantsManager.getInstance().createVariantAttributeDescriptor(params);
		params = new HashMap<>();
		params.put(VariantType.CODE, "SomeVariantSubType");
		params.put(VariantType.SUPERTYPE, varTypeJalo);
		final VariantType varSubTypeJalo = VariantsManager.getInstance().createVariantType(params);
		final VariantAttributeDescriptor varSubTypeAttributeJalo = (VariantAttributeDescriptor) varSubTypeJalo
				.getAttributeDescriptor(varAttributeJalo.getQualifier());

		variantType = modelService.get(varTypeJalo);
		varAttribute = modelService.get(varAttributeJalo);

		variantSubType = modelService.get(varSubTypeJalo);
		varSubTypeAttribute = modelService.get(varSubTypeAttributeJalo);
	}

	private void setUpNormalAttribute()
	{
		final String CATALOG_NAME = "name";

		type = typeService.getComposedTypeForCode(CatalogModel._TYPECODE);
		subType = typeService.getComposedTypeForCode(ClassificationSystemModel._TYPECODE);

		attribute = typeService.getAttributeDescriptor(type, CATALOG_NAME);
		subTypeAttribute = typeService.getAttributeDescriptor(subType, CATALOG_NAME);
	}

	@Test
	public void testNormalAttributePermissions()
	{
		setUpNormalAttribute();

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
		}, type, subType, attribute, subTypeAttribute);

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
		}, type, subType, attribute, subTypeAttribute);

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
		}, type, subType, attribute, subTypeAttribute);

	}

	@Test
	public void testVariantAttributePermissions()
	{
		setUpVariantAttribute();

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
		}, variantType, variantSubType, varAttribute, varSubTypeAttribute);


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
		}, variantType, variantSubType, varAttribute, varSubTypeAttribute);

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
		}, variantType, variantSubType, varAttribute, varSubTypeAttribute);
	}

	private void testAttributeDescriptor(final String permissionName, final AttributeCheckHelper checkHelper,
			final ComposedTypeModel type, final ComposedTypeModel subType, final AttributeDescriptorModel attribute,
			final AttributeDescriptorModel subTypeAttribute)
	{

		final UserModel currentUser = userService.getCurrentUser();
		//Let's ensure we're not admins!
		Assert.assertFalse(userService.isAdmin(currentUser));

		//TEST

		/*
		 * Without any permissions assigned we should be denied.
		 */
		Assert.assertFalse("Current user should not be granted \"" + permissionName + "\" permission to " + subTypeAttribute
				+ " attribute", checkHelper.isGranted(subTypeAttribute));
		Assert.assertFalse("Current user should not be granted \"" + permissionName + "\" permission to " + subTypeAttribute
				+ " attribute", checkHelper.isGranted(subTypeAttribute.getEnclosingType().getCode(), subTypeAttribute.getQualifier()));

		Assert.assertFalse("Current user should not be granted \"" + permissionName + "\" permission to " + attribute
				+ " attribute", checkHelper.isGranted(attribute));
		Assert.assertFalse("Current user should not be granted \"" + permissionName + "\" permission to " + attribute
				+ " attribute", checkHelper.isGranted(attribute.getEnclosingType().getCode(), attribute.getQualifier()));

		/*
		 * Let's grant global permission permissionName to for current user.
		 */
		permissionManagementService.addGlobalPermission(new PermissionAssignment(permissionName, currentUser));

		/*
		 * With a global permission assignment, permission should be granted.
		 */
		Assert.assertTrue("Current user should be granted \"" + permissionName + "\" permission to " + subTypeAttribute
				+ " attribute", checkHelper.isGranted(subTypeAttribute));
		Assert.assertTrue("Current user should be granted \"" + permissionName + "\" permission to " + subTypeAttribute
				+ " attribute", checkHelper.isGranted(subTypeAttribute.getEnclosingType().getCode(), subTypeAttribute.getQualifier()));

		Assert.assertTrue("Current user should be granted \"" + permissionName + "\" permission to " + attribute + " attribute",
				checkHelper.isGranted(attribute));
		Assert.assertTrue("Current user should be granted \"" + permissionName + "\" permission to " + attribute + " attribute",
				checkHelper.isGranted(attribute.getEnclosingType().getCode(), attribute.getQualifier()));


		/*
		 * Let's deny permission permissionName to 'type' type for current user. This should override global permissions
		 */
		permissionManagementService.addTypePermission(type, new PermissionAssignment(permissionName, currentUser, true));


		/*
		 * With type-level permission denied we should be denied.
		 */
		Assert.assertFalse("Current user should not be granted \"" + permissionName + "\" permission to " + subTypeAttribute
				+ " attribute", checkHelper.isGranted(subTypeAttribute));
		Assert.assertFalse("Current user should not be granted \"" + permissionName + "\" permission to " + subTypeAttribute
				+ " attribute", checkHelper.isGranted(subTypeAttribute.getEnclosingType().getCode(), subTypeAttribute.getQualifier()));

		Assert.assertFalse("Current user should not be granted \"" + permissionName + "\" permission to " + attribute
				+ " attribute", checkHelper.isGranted(attribute));
		Assert.assertFalse("Current user should not be granted \"" + permissionName + "\" permission to " + attribute
				+ " attribute", checkHelper.isGranted(attribute.getEnclosingType().getCode(), attribute.getQualifier()));


		/*
		 * Let's grant permission to attribute for current user. This overrides type level permission assignments (when
		 * checking for attribute permissions).
		 */
		permissionManagementService.addAttributePermission(attribute, new PermissionAssignment(permissionName, currentUser));

		/*
		 * With attribute level permission we should be granted now.
		 */
		Assert.assertTrue("Current user should be granted \"" + permissionName + "\" permission to " + subTypeAttribute
				+ " attribute", checkHelper.isGranted(subTypeAttribute));
		Assert.assertTrue("Current user should be granted \"" + permissionName + "\" permission to " + subTypeAttribute
				+ " attribute", checkHelper.isGranted(subTypeAttribute.getEnclosingType().getCode(), subTypeAttribute.getQualifier()));

		Assert.assertTrue("Current user should be granted \"" + permissionName + "\" permission to " + attribute + " attribute",
				checkHelper.isGranted(attribute));
		Assert.assertTrue("Current user should be granted \"" + permissionName + "\" permission to " + attribute + " attribute",
				checkHelper.isGranted(attribute.getEnclosingType().getCode(), attribute.getQualifier()));

		/*
		 * Let's deny permission permissionName to the sub type for current user.
		 */
		permissionManagementService.addTypePermission(subType, new PermissionAssignment(permissionName, currentUser, true));

		/*
		 * With sub type permissions denied permission should be denied for the sub attribute *but granted* for the super
		 * attribute!
		 */
		Assert.assertFalse("Current user should not be granted \"" + permissionName + "\" permission to " + subTypeAttribute
				+ " attribute", checkHelper.isGranted(subTypeAttribute));
		Assert.assertFalse("Current user should not be granted \"" + permissionName + "\" permission to " + subTypeAttribute
				+ " attribute", checkHelper.isGranted(subTypeAttribute.getEnclosingType().getCode(), subTypeAttribute.getQualifier()));

		Assert.assertTrue("Current user should be granted \"" + permissionName + "\" permission to " + attribute + " attribute",
				checkHelper.isGranted(attribute));
		Assert.assertTrue("Current user should be granted \"" + permissionName + "\" permission to " + attribute + " attribute",
				checkHelper.isGranted(attribute.getEnclosingType().getCode(), attribute.getQualifier()));

		/*
		 * Let's grant permission to the sub attribute for current user.
		 */
		permissionManagementService.addAttributePermission(subTypeAttribute, new PermissionAssignment(permissionName, currentUser));

		/*
		 * With granted permission for the sub attribute it should be accessible again (type is still denied)
		 */
		Assert.assertTrue("Current user should be granted \"" + permissionName + "\" permission to " + subTypeAttribute
				+ " attribute", checkHelper.isGranted(subTypeAttribute));
		Assert.assertTrue("Current user should be granted \"" + permissionName + "\" permission to " + subTypeAttribute
				+ " attribute", checkHelper.isGranted(subTypeAttribute.getEnclosingType().getCode(), subTypeAttribute.getQualifier()));

		Assert.assertTrue("Current user should be granted \"" + permissionName + "\" permission to " + attribute + " attribute",
				checkHelper.isGranted(attribute));
		Assert.assertTrue("Current user should be granted \"" + permissionName + "\" permission to " + attribute + " attribute",
				checkHelper.isGranted(attribute.getEnclosingType().getCode(), attribute.getQualifier()));
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
