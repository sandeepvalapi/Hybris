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
package de.hybris.platform.comments.daos;

import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.CommentUserSettingModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.model.ReplyModel;
import de.hybris.platform.comments.services.CommentDao;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CommentDaoTest extends ServicelayerTransactionalTest
{
	private final static Logger LOG = Logger.getLogger(CommentDaoTest.class);

	@Resource
	private UserService userService;
	@Resource
	private CommentService commentService;
	@Resource
	private CommentDao commentDao;
	@Resource
	private ModelService modelService;
	@Resource
	private ProductService productService;

	private UserModel user1;
	private UserModel user2;

	private ProductModel product1;

	private CommentTypeModel commentType1;
	private CommentTypeModel commentType2;
	private ComponentModel component;
	private DomainModel domain;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultUsers();
		createDefaultCatalog();
		createCommentData();


		user1 = userService.getUserForUID("ariel");
		assertNotNull("User", user1);
		user2 = userService.getUserForUID("demo");
		assertNotNull("User", user2);

		product1 = productService.getProduct("testProduct0");
		assertNotNull("Product", product1);

		domain = commentService.getDomainForCode("testDomain");
		assertNotNull("Domain", domain);

		component = commentService.getComponentForCode(domain, "testComponent");
		assertNotNull("Component", component);

		commentType1 = commentService.getCommentTypeForCode(component, "testCommentType");
		assertNotNull("Comment type", commentType1);

		commentType2 = commentService.getCommentTypeForCode(component, "testCommentType2");
		assertNotNull("Comment type", commentType2);
	}

	public static void createCommentData() throws ImpExException
	{
		LOG.info("Creating comment data...");
		importCsv("/servicelayer/test/testComments.csv", "UTF-8");
		LOG.info("Done creating comment data.");
	}

	private Collection<CommentModel> createComments(final UserModel user, final int number)
	{
		final Collection<CommentModel> ret = new ArrayList<CommentModel>();
		for (int i = 0; i < number; i++)
		{
			final CommentModel comment = new CommentModel();
			comment.setAuthor(user);
			comment.setCode("testComment_" + user.getUid() + "_" + i);

			comment.setSubject("testsubject");
			comment.setText("test text");

			comment.setComponent(component);
			comment.setCommentType(commentType1);

			modelService.save(comment);
			ret.add(comment);
		}
		return ret;
	}


	@Test
	public void testFindComments()
	{
		createComments(user1, 4);
		final Collection<CommentModel> commentsUser2 = createComments(user2, 2);

		final List<CommentModel> findCommentsByUser = commentDao
				.findCommentsByUser(user1, Collections.singleton(component), -1, -1);
		Assert.assertEquals(4, findCommentsByUser.size());

		final List<CommentModel> findCommentsByUser2 = commentDao.findCommentsByUser(user2, Collections.singleton(component), -1,
				-1);
		Assert.assertEquals(2, findCommentsByUser2.size());

		final List<CommentModel> findCommentsByType = commentDao.findCommentsByType(user1, Collections.singleton(component),
				Collections.singleton(commentType1), -1, -1);
		Assert.assertEquals(4, findCommentsByType.size());

		final List<CommentModel> findCommentsByItem = commentDao.findCommentsByItem(user2, Collections.singleton(component),
				product1, -1, -1);
		Assert.assertTrue(findCommentsByItem.isEmpty());

		final CommentModel itemComment = commentsUser2.iterator().next();
		itemComment.setRelatedItems(Collections.singletonList((ItemModel) product1));
		modelService.save(itemComment);

		final List<CommentModel> findCommentsByItem2 = commentDao.findCommentsByItem(user2, Collections.singleton(component),
				product1, -1, -1);
		Assert.assertTrue(findCommentsByItem2.size() == 1);

		final CommentModel type2comment = new CommentModel();
		type2comment.setAuthor(user2);
		type2comment.setCode("type2comment");
		type2comment.setCommentType(commentType2);
		type2comment.setComponent(component);
		type2comment.setText("bla");
		type2comment.setRelatedItems(Collections.singletonList((ItemModel) product1));
		modelService.save(type2comment);

		final List<CommentModel> findCommentsByItem3 = commentDao.findCommentsByItem(user2, Collections.singleton(component),
				product1, -1, -1);
		Assert.assertTrue(findCommentsByItem3.size() == 2);

		final List<CommentModel> findCommentsByItemAndType1 = commentDao.findCommentsByItemAndType(user2,
				Collections.singleton(component), product1, Collections.singleton(commentType1), -1, -1);
		Assert.assertTrue(findCommentsByItemAndType1.iterator().next().equals(itemComment));

		final List<CommentModel> findCommentsByItemAndType2 = commentDao.findCommentsByItemAndType(user2,
				Collections.singleton(component), product1, Collections.singleton(commentType2), -1, -1);
		Assert.assertTrue(findCommentsByItemAndType2.iterator().next().equals(type2comment));
	}

	@Test
	public void testFindDirectReplies()
	{
		final Collection<CommentModel> comments = createComments(user1, 2);

		int count = 1;
		for (final CommentModel commentModel : comments)
		{
			for (int i = 0; i < count; i++)
			{
				final ReplyModel reply = commentService.createReply(user2, commentModel, "bla");
				modelService.save(reply);
				final ReplyModel replySecondLevel = commentService.createReply(user2, reply, "bla");
				modelService.save(replySecondLevel);
			}
			count++;
			modelService.saveAll(commentModel);
		}

		final Iterator<CommentModel> iterator = comments.iterator();
		final List<ReplyModel> findDirectRepliesByComment = commentDao.findDirectRepliesByComment(iterator.next(), -1, -1);
		Assert.assertEquals(1, findDirectRepliesByComment.size());

		final List<ReplyModel> findDirectRepliesByComment2 = commentDao.findDirectRepliesByComment(iterator.next(), -1, -1);
		Assert.assertEquals(2, findDirectRepliesByComment2.size());
	}

	@Test
	public void testFindUserSettings()
	{
		final Collection<CommentModel> createComments = createComments(user1, 2);

		final CommentModel commentModel = createComments.iterator().next();

		final List<CommentUserSettingModel> findUserSettingsByComment = commentDao.findUserSettingsByComment(user1, commentModel);
		Assert.assertTrue(findUserSettingsByComment.isEmpty());

		final CommentUserSettingModel userSetting = commentService.getUserSetting(user1, commentModel);
		final List<CommentUserSettingModel> findUserSettingsByComment2 = commentDao.findUserSettingsByComment(user1, commentModel);
		Assert.assertSame(userSetting, findUserSettingsByComment2.iterator().next());

	}

	@Test
	public void testFindComponents()
	{
		final List<ComponentModel> findComponentsByDomainAndCode = commentDao
				.findComponentsByDomainAndCode(domain, "testComponent");
		Assert.assertTrue(findComponentsByDomainAndCode.size() == 1);
		Assert.assertEquals(component, findComponentsByDomainAndCode.iterator().next());

		final List<ComponentModel> findComponentsByDomainAndCode2 = commentDao.findComponentsByDomainAndCode(domain,
				"testComponent2");
		Assert.assertEquals(0, findComponentsByDomainAndCode2.size());

		final DomainModel domain2 = commentDao.findDomainsByCode("testDomain2").iterator().next();
		Assert.assertNotNull(domain2);
		final List<ComponentModel> findComponentsByDomainAndCode3 = commentDao.findComponentsByDomainAndCode(domain2,
				"testComponent2");
		Assert.assertEquals(1, findComponentsByDomainAndCode3.size());
		Assert.assertNotSame(component, findComponentsByDomainAndCode3.iterator().next());
	}

	@Test
	public void testFindDomains()
	{
		final List<DomainModel> findDomainsByCode = commentDao.findDomainsByCode("testDomain");
		Assert.assertEquals(1, findDomainsByCode.size());
		Assert.assertTrue(findDomainsByCode.iterator().next().getCode().equals("testDomain"));
	}
}
