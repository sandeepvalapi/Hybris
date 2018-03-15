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
package de.hybris.platform.servicelayer.media.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.security.permissions.PermissionAssignment;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckResult;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckingService;
import de.hybris.platform.servicelayer.security.permissions.PermissionManagementService;
import de.hybris.platform.servicelayer.security.permissions.PermissionsConstants;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collection;
import java.util.HashSet;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;


/**
 * JUnit class for {@link DefaultMediaPermissionService}
 */
@IntegrationTest
public class DefaultMediaPermissionServiceIntegrationTest extends ServicelayerTransactionalBaseTest
{

	@Resource
	private DefaultMediaPermissionService mediaPermissionService;

	@Resource
	private PermissionCheckingService permissionCheckingService;
	@Resource
	private PermissionManagementService permissionManagementService;

	@Resource
	private DefaultMediaService mediaService;
	@Resource
	private UserService userService;
	@Resource
	private ModelService modelService;

	private CatalogVersionModel catalogVersion;

	private MediaModel testMediaItem;

	/**
	 * 
	 */
	@Before
	public void setUp() throws Exception
	{
		final MediaFormatModel format = modelService.create(MediaFormatModel.class);
		format.setName("Format_abc");
		format.setQualifier("format_abc");
		modelService.save(format);

		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId("my_favorite_catalog");
		modelService.save(catalog);

		catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setVersion("my_version");
		catalogVersion.setCatalog(catalog);
		modelService.save(catalogVersion);

		final MediaContainerModel container = modelService.create(MediaContainerModel.class);
		container.setQualifier("test1234");
		container.setCatalogVersion(this.catalogVersion);
		modelService.save(container);

		final String qualifier = "testMedia_" + format.getQualifier();
		final MediaModel media = modelService.create(CatalogUnawareMediaModel.class);
		media.setCode(qualifier);
		media.setMediaFormat(format);
		media.setMediaContainer(container);
		this.modelService.save(media);

		testMediaItem = getMediaByFormat(container, format);
		Assert.assertNotNull(testMediaItem);

		permissionManagementService.createPermission(PermissionsConstants.READ);
		final UserModel user = userService.getCurrentUser();
		final PermissionAssignment permissionAssignment = new PermissionAssignment(PermissionsConstants.READ, user);

		permissionManagementService.addItemPermission(testMediaItem, permissionAssignment);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.media.impl.DefaultMediaPermissionService#isReadAccessGranted(de.hybris.platform.core.model.media.MediaModel, de.hybris.platform.core.model.security.PrincipalModel)}
	 * .
	 */
	@Test
	public void testIsReadAccessGrantedTrue()
	{
		final boolean result = mediaPermissionService.isReadAccessGranted(testMediaItem, userService.getCurrentUser());
		Assert.assertTrue(result);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.media.impl.DefaultMediaPermissionService#isReadAccessGranted(de.hybris.platform.core.model.media.MediaModel, de.hybris.platform.core.model.security.PrincipalModel)}
	 * .
	 */
	@Test
	public void testIsReadAccessGrantedFalse()
	{
		final PermissionAssignment permissionAssignment_deny = new PermissionAssignment(PermissionsConstants.READ,
				userService.getCurrentUser(), true);
		permissionManagementService.addItemPermission(testMediaItem, permissionAssignment_deny); //this will overwrite the grant permission into deny 
		final boolean result = mediaPermissionService.isReadAccessGranted(testMediaItem, userService.getCurrentUser());
		Assert.assertFalse(result);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.media.impl.DefaultMediaPermissionService#grantReadPermission(de.hybris.platform.core.model.media.MediaModel, de.hybris.platform.core.model.security.PrincipalModel)}
	 * .
	 */
	@Test
	public void testGrantPermission()
	{
		final UserModel testUser = modelService.create(UserModel.class);
		testUser.setUid("testGroup");
		testUser.setName("Testgroup");
		modelService.save(testUser);

		final PermissionAssignment testAssignment = new PermissionAssignment(PermissionsConstants.READ, testUser);
		final Collection<PermissionAssignment> permAssignments = permissionManagementService.getItemPermissionsForPrincipal(
				testMediaItem, testUser);

		Assert.assertFalse(permAssignments.contains(testAssignment));

		mediaPermissionService.grantReadPermission(testMediaItem, testUser);
		final PermissionCheckResult checkResult = permissionCheckingService.checkItemPermission(testMediaItem, testUser,
				PermissionsConstants.READ);

		Assert.assertTrue(checkResult.isGranted());
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.media.impl.DefaultMediaPermissionService#denyReadPermission(de.hybris.platform.core.model.media.MediaModel, de.hybris.platform.core.model.security.PrincipalModel)}
	 * .
	 */
	@Test
	public void testDenyPermission()
	{
		final PermissionAssignment testAssignment = new PermissionAssignment(PermissionsConstants.READ,
				userService.getCurrentUser(), true);
		final Collection<PermissionAssignment> permAssignments = permissionManagementService.getItemPermissionsForPrincipal(
				testMediaItem, userService.getCurrentUser());

		Assert.assertFalse(permAssignments.contains(testAssignment));

		mediaPermissionService.denyReadPermission(testMediaItem, userService.getCurrentUser());
		final PermissionCheckResult checkResult = permissionCheckingService.checkItemPermission(testMediaItem,
				userService.getCurrentUser(), PermissionsConstants.READ);

		Assert.assertFalse(checkResult.isGranted());
	}


	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.media.impl.DefaultMediaPermissionService#getPermittedPrincipals(MediaModel)}
	 * .
	 */
	@Test
	public void testGetPermittedPrincipals()
	{

		Collection<PrincipalModel> permittedPrincipals = mediaPermissionService.getPermittedPrincipals(testMediaItem);
		Assert.assertTrue(permittedPrincipals.size() == 1);
		Assert.assertTrue(permittedPrincipals.contains(userService.getCurrentUser()));

		//create new user and assign proper permission
		final UserModel testUser = modelService.create(UserModel.class);
		testUser.setUid("testGroup");
		testUser.setName("Testgroup");
		modelService.save(testUser);
		final PermissionAssignment permissionAssignment_grant = new PermissionAssignment(PermissionsConstants.READ, testUser, false);
		permissionManagementService.addItemPermission(testMediaItem, permissionAssignment_grant);

		permittedPrincipals = mediaPermissionService.getPermittedPrincipals(testMediaItem);
		Assert.assertTrue(CollectionUtils.isNotEmpty(permittedPrincipals));
		Assert.assertTrue(permittedPrincipals.contains(userService.getCurrentUser()));
		Assert.assertTrue(permittedPrincipals.contains(testUser));

		for (final PrincipalModel principal : permittedPrincipals)
		{
			final PermissionCheckResult checkResult = permissionCheckingService.checkItemPermission(testMediaItem, principal,
					PermissionsConstants.READ);
			Assert.assertTrue(checkResult.isGranted());
		}

		//now deny the permission and re-check again
		final PermissionAssignment permissionAssignment_deny = new PermissionAssignment(PermissionsConstants.READ, testUser, true);
		permissionManagementService.addItemPermission(testMediaItem, permissionAssignment_deny);

		permittedPrincipals = mediaPermissionService.getPermittedPrincipals(testMediaItem);
		Assert.assertTrue(permittedPrincipals.size() == 1);
		Assert.assertTrue(permittedPrincipals.contains(userService.getCurrentUser()));

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.media.impl.DefaultMediaPermissionService#getPermittedPrincipals(MediaModel)}
	 * .
	 */
	@Test
	public void testGetDeniedPrincipals()
	{

		Collection<PrincipalModel> deniedPrincipals = mediaPermissionService.getDeniedPrincipals(testMediaItem);
		Assert.assertTrue(CollectionUtils.isEmpty(deniedPrincipals));

		//create new user and deny proper permission
		final UserModel testUser = modelService.create(UserModel.class);
		testUser.setUid("testGroup");
		testUser.setName("Testgroup");
		modelService.save(testUser);
		final PermissionAssignment permissionAssignment_deny = new PermissionAssignment(PermissionsConstants.READ, testUser, true);
		permissionManagementService.addItemPermission(testMediaItem, permissionAssignment_deny);

		deniedPrincipals = mediaPermissionService.getDeniedPrincipals(testMediaItem);
		Assert.assertTrue(CollectionUtils.isNotEmpty(deniedPrincipals));
		Assert.assertTrue(deniedPrincipals.contains(testUser));

		for (final PrincipalModel principal : deniedPrincipals)
		{
			final PermissionCheckResult checkResult = permissionCheckingService.checkItemPermission(testMediaItem, principal,
					PermissionsConstants.READ);
			Assert.assertTrue(checkResult.isDenied());
		}

		//now grant the permission and re-check again
		final PermissionAssignment permissionAssignment_grant = new PermissionAssignment(PermissionsConstants.READ, testUser, false);
		permissionManagementService.addItemPermission(testMediaItem, permissionAssignment_grant);

		deniedPrincipals = mediaPermissionService.getDeniedPrincipals(testMediaItem);
		Assert.assertTrue(CollectionUtils.isEmpty(deniedPrincipals));
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.media.impl.DefaultMediaPermissionService#setPermittedPrincipals(MediaModel, Collection)}
	 * .
	 */
	@Test
	public void testSetPermittedPrincipals()
	{
		Collection<PrincipalModel> permittedPrincipals = mediaPermissionService.getPermittedPrincipals(testMediaItem);
		Assert.assertTrue(permittedPrincipals.size() == 1);
		Assert.assertTrue(permittedPrincipals.contains(userService.getCurrentUser()));

		//create new user and assign proper permission
		final UserModel testUser = modelService.create(UserModel.class);
		testUser.setUid("testGroup");
		testUser.setName("Testgroup");
		modelService.save(testUser);

		final Collection<PrincipalModel> principalsToBeSet = new HashSet<PrincipalModel>();
		principalsToBeSet.add(userService.getCurrentUser());
		principalsToBeSet.add(testUser);

		//update the permitted principals list. Expected result - 2 assigned principals
		mediaPermissionService.setPermittedPrincipals(testMediaItem, principalsToBeSet);

		permittedPrincipals = mediaPermissionService.getPermittedPrincipals(testMediaItem);
		Assert.assertTrue(permittedPrincipals.size() == 2);
		Assert.assertTrue(permittedPrincipals.contains(userService.getCurrentUser()));
		Assert.assertTrue(permittedPrincipals.contains(testUser));

		principalsToBeSet.clear();
		//update the permitted principals list. Expected result - empty list (no assigned principals)
		mediaPermissionService.setPermittedPrincipals(testMediaItem, principalsToBeSet);

		permittedPrincipals = mediaPermissionService.getPermittedPrincipals(testMediaItem);
		Assert.assertTrue(CollectionUtils.isEmpty(permittedPrincipals));
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.servicelayer.media.impl.DefaultMediaPermissionService#setDeniedPrincipals(MediaModel, Collection)}
	 * .
	 */
	@Test
	public void testSetDeniedPrincipals()
	{
		Collection<PrincipalModel> deniedPrincipals = mediaPermissionService.getDeniedPrincipals(testMediaItem);
		Assert.assertTrue(CollectionUtils.isEmpty(deniedPrincipals));

		//create new user and assign proper permission
		final UserModel testUser = modelService.create(UserModel.class);
		testUser.setUid("testGroup");
		testUser.setName("Testgroup");
		modelService.save(testUser);

		final Collection<PrincipalModel> principalsToBeSet = new HashSet<PrincipalModel>();
		principalsToBeSet.add(userService.getCurrentUser());
		principalsToBeSet.add(testUser);

		//update the denied principals list. Expected result - 2 denied principals
		mediaPermissionService.setDeniedPrincipals(testMediaItem, principalsToBeSet);

		deniedPrincipals = mediaPermissionService.getDeniedPrincipals(testMediaItem);
		Assert.assertTrue(deniedPrincipals.size() == 2);
		Assert.assertTrue(deniedPrincipals.contains(userService.getCurrentUser()));
		Assert.assertTrue(deniedPrincipals.contains(testUser));

		principalsToBeSet.clear();
		//update the denied principals list. Expected result - empty list (no denied principals)
		mediaPermissionService.setDeniedPrincipals(testMediaItem, principalsToBeSet);

		deniedPrincipals = mediaPermissionService.getDeniedPrincipals(testMediaItem);
		Assert.assertTrue(CollectionUtils.isEmpty(deniedPrincipals));

	}


	private MediaModel getMediaByFormat(final MediaContainerModel container, final MediaFormatModel mediaFormatModel)
	{
		return mediaService.getMediaByFormat(container, mediaFormatModel);
	}
}
