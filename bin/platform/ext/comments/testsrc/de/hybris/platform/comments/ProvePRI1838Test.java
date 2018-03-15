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
package de.hybris.platform.comments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.comments.model.CommentAttachmentModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Test;


/**
 * Test for https://jira.hybris.com/browse/PRI-1838 to prove it is only a print problem.
 */
@IntegrationTest
public class ProvePRI1838Test extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;

	@Test
	public void testItemCreation()
	{
		final UserModel user1 = createUser("u1");
		final UserModel user2 = createUser("u2");
		assertNotSame(user1, user2);

		final CommentAttachmentModel commentAttachment = modelService.create(CommentAttachmentModel.class);
		commentAttachment.setItem(user1);
		assertEquals(user1, commentAttachment.getItem());
		//if this line will fail, we are in big trouble, see PRI-1838 + PLA-12453
	}

	private UserModel createUser(final String uid)
	{
		final UserModel user = modelService.create(UserModel.class);
		user.setUid(uid);
		modelService.save(user);
		assertNotNull(user);
		return user;
	}
}
