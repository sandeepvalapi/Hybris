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

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.CommentUserSettingModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.model.ReplyModel;
import de.hybris.platform.comments.services.CommentDao;
import de.hybris.platform.comments.services.impl.DefaultCommentService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.HybrisJUnit4ClassRunner;
import de.hybris.platform.testframework.RunListeners;
import de.hybris.platform.testframework.runlistener.LogRunListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;




@IntegrationTest
@RunWith(HybrisJUnit4ClassRunner.class)
@RunListeners(
{ LogRunListener.class })
public class CommentServiceTest
{
	private DefaultCommentService commentService;
	@Mock
	private CommentDao commentDao;
	@Mock
	private ModelService modelService;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		commentService = new DefaultCommentService();
		commentService.setModelService(modelService);
		commentService.setCommentDao(commentDao);
	}

	@Test
	public void testCreateReply()
	{
		when(modelService.create(ReplyModel.class)).thenReturn(new ReplyModel());

		final UserModel user1 = new UserModel();
		user1.setName("testUser1");

		final CommentModel comment = new CommentModel();
		comment.setAuthor(user1);
		comment.setText("test comment");

		final ReplyModel reply1 = commentService.createReply(user1, comment, "test reply");
		Assert.assertSame(comment, reply1.getComment());

		final ReplyModel reply2 = commentService.createReply(user1, reply1, "test reply 2");

		Assert.assertSame(reply1, reply2.getParent());
		Assert.assertSame(comment, reply2.getComment());
		Mockito.verify(modelService, Mockito.times(2)).create(ReplyModel.class);
	}

	@Test
	public void testReturnsUserSettings()
	{
		final UserModel user1 = new UserModel();
		user1.setName("testUser1");


		final UserModel user2 = new UserModel();
		user2.setName("testUser2");

		final CommentModel comment = new CommentModel();
		final CommentUserSettingModel commentUserSettingModel1 = new CommentUserSettingModel();

		when(commentDao.findUserSettingsByComment(user1, comment)).thenReturn(Collections.singletonList(commentUserSettingModel1));
		when(commentDao.findUserSettingsByComment(user2, comment)).thenReturn(Collections.EMPTY_LIST);
		when(modelService.create(CommentUserSettingModel.class)).thenReturn(new CommentUserSettingModel());

		final CommentUserSettingModel userSetting1 = commentService.getUserSetting(user1, comment);
		final CommentUserSettingModel userSetting2 = commentService.getUserSetting(user2, comment);

		Assert.assertSame(commentUserSettingModel1, userSetting1);
		Assert.assertNotNull(userSetting2);
		Assert.assertNotSame(commentUserSettingModel1, userSetting2);

		Mockito.verify(commentDao, Mockito.times(2)).findUserSettingsByComment(Mockito.any(UserModel.class), Mockito.eq(comment));
		Mockito.verify(modelService, Mockito.times(1)).create(CommentUserSettingModel.class);
	}


	@Test
	public void testReturnsAvailableCommentTypes()
	{
		final DomainModel domain = new DomainModel();
		final CommentTypeModel commentType1 = new CommentTypeModel();
		final CommentTypeModel commentType2 = new CommentTypeModel();
		final List<CommentTypeModel> typeList = Arrays.asList(commentType1, commentType2);
		domain.setCommentTypes(typeList);

		final ComponentModel component = new ComponentModel();
		component.setDomain(domain);

		final Collection<CommentTypeModel> availableCommentTypes = commentService.getAvailableCommentTypes(component);
		Assert.assertTrue(compareCollections(typeList, availableCommentTypes));
	}

	@Test
	public void testReturnsDomainByCode()
	{
		final DomainModel domain1 = new DomainModel();
		final DomainModel domain2 = new DomainModel();

		when(commentDao.findDomainsByCode(Mockito.eq("domain1"))).thenReturn(Collections.singletonList(domain1));
		when(commentDao.findDomainsByCode(Mockito.eq("domain2"))).thenReturn(Collections.singletonList(domain2));
		when(commentDao.findDomainsByCode(Mockito.eq("domainCodeNotUnique"))).thenReturn(Arrays.asList(domain1, domain2));
		when(commentDao.findDomainsByCode(Mockito.eq("domainCodeNotExisting"))).thenReturn(Collections.EMPTY_LIST);

		try
		{
			commentService.getDomainForCode(null);
			Assert.fail("Code should not be reached.");
		}
		catch (final IllegalArgumentException e)
		{
			Mockito.verify(commentDao, Mockito.never()).findDomainsByCode(Mockito.anyString());
		}

		final DomainModel domainByCode1 = commentService.getDomainForCode("domain1");
		Assert.assertSame(domain1, domainByCode1);
		final DomainModel domainByCode2 = commentService.getDomainForCode("domain2");
		Assert.assertSame(domain2, domainByCode2);
		final DomainModel domainByCode3 = commentService.getDomainForCode("domainCodeNotUnique");
		Assert.assertSame(domain1, domainByCode3);
		final DomainModel domainByCode4 = commentService.getDomainForCode("domainCodeNotExisting");
		Assert.assertNull(domainByCode4);

		Mockito.verify(commentDao, Mockito.times(4)).findDomainsByCode(Mockito.anyString());
	}

	@Test
	public void testReturnsComponentByCode()
	{
		final DomainModel domain1 = new DomainModel();
		final ComponentModel comp1 = new ComponentModel();
		final ComponentModel comp2 = new ComponentModel();

		when(commentDao.findComponentsByDomainAndCode(Mockito.eq(domain1), Mockito.eq("comp1"))).thenReturn(
				Collections.singletonList(comp1));
		when(commentDao.findComponentsByDomainAndCode(Mockito.eq(domain1), Mockito.eq("comp2"))).thenReturn(
				Collections.singletonList(comp2));
		when(commentDao.findComponentsByDomainAndCode(Mockito.eq(domain1), Mockito.eq("compCodeNotUnique"))).thenReturn(
				Arrays.asList(comp1, comp2));
		when(commentDao.findComponentsByDomainAndCode(Mockito.eq(domain1), Mockito.eq("compCodeNotExisting"))).thenReturn(
				Collections.EMPTY_LIST);


		try
		{
			commentService.getComponentForCode(null, "something");
			Assert.fail("Code should not be reached.");
		}
		catch (final IllegalArgumentException e)
		{
			Mockito.verify(commentDao, Mockito.never()).findComponentsByDomainAndCode(Mockito.any(DomainModel.class),
					Mockito.anyString());
		}

		try
		{
			commentService.getComponentForCode(domain1, null);
			Assert.fail("Code should not be reached.");
		}
		catch (final IllegalArgumentException e)
		{
			Mockito.verify(commentDao, Mockito.never()).findComponentsByDomainAndCode(Mockito.any(DomainModel.class),
					Mockito.anyString());
		}

		final ComponentModel componentByCode1 = commentService.getComponentForCode(domain1, "comp1");
		Assert.assertSame(comp1, componentByCode1);

		final ComponentModel componentByCode2 = commentService.getComponentForCode(domain1, "comp2");
		Assert.assertSame(comp2, componentByCode2);

		final ComponentModel componentByCode3 = commentService.getComponentForCode(domain1, "compCodeNotUnique");
		Assert.assertSame(comp1, componentByCode3);

		final ComponentModel componentByCode4 = commentService.getComponentForCode(domain1, "compCodeNotExisting");
		Assert.assertNull(componentByCode4);

		Mockito.verify(commentDao, Mockito.times(4)).findComponentsByDomainAndCode(Mockito.eq(domain1), Mockito.anyString());

	}


	@Test
	public void testReturnsCommentTypeByCode()
	{
		final CommentTypeModel commentType1 = new CommentTypeModel();
		commentType1.setCode("testType1");
		final CommentTypeModel commentType2 = new CommentTypeModel();
		commentType1.setCode("testType2");

		final DomainModel domain = new DomainModel();
		domain.setCommentTypes(Arrays.asList(commentType1, commentType2));

		final ComponentModel component = new ComponentModel();
		component.setDomain(domain);

		final Collection<CommentTypeModel> availableCommentTypes = commentService.getAvailableCommentTypes(component);
		Assert.assertTrue(compareCollections(Arrays.asList(commentType1, commentType2), availableCommentTypes));
	}


	@Test
	public void testReturnsDirectReplies()
	{
		final CommentModel comment = new CommentModel();
		final ReplyModel reply1 = new ReplyModel();
		final ReplyModel reply2 = new ReplyModel();

		final List<ReplyModel> replies = new ArrayList<ReplyModel>();
		replies.add(reply1);
		replies.add(reply2);

		when(commentDao.findDirectRepliesByComment(Mockito.eq(comment), Mockito.anyInt(), Mockito.anyInt())).thenReturn(replies);

		final List<ReplyModel> directReplies = commentService.getDirectReplies(comment, -1, -1);
		Assert.assertTrue(compareCollections(replies, directReplies));

		Mockito.verify(commentDao, Mockito.times(1)).findDirectRepliesByComment(Mockito.any(CommentModel.class), Mockito.anyInt(),
				Mockito.anyInt());
	}

	private boolean compareCollections(final Collection coll1, final Collection coll2)
	{
		if (coll1.size() != coll2.size())
		{
			return false;
		}

		for (final Object object : coll2)
		{
			if (!coll1.contains(object))
			{
				return false;
			}
		}
		return true;
	}

	@Test
	public void testReturnsComments()
	{
		final UserModel user1 = new UserModel();
		user1.setName("testUser1");

		final UserModel user2 = new UserModel();
		user2.setName("testUser2");

		final ItemModel product1 = new ProductModel();

		final ComponentModel component = new ComponentModel();
		final List<ComponentModel> comps = Collections.singletonList(component);


		final DomainModel domain = new DomainModel();
		domain.setComponents(comps);

		final CommentModel comment1 = new CommentModel();
		comment1.setAuthor(user1);

		final CommentModel comment2 = new CommentModel();
		comment2.setAuthor(user1);
		comment2.setRelatedItems(Collections.singletonList(product1));

		final List<CommentModel> user1List = new ArrayList<CommentModel>();
		user1List.add(comment1);
		user1List.add(comment2);

		when(commentDao.findCommentsByUser(Mockito.eq(user1), Mockito.eq(comps), Mockito.anyInt(), Mockito.anyInt())).thenReturn(
				user1List);
		when(commentDao.findCommentsByUser(Mockito.eq(user2), Mockito.anyCollection(), Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn(Collections.EMPTY_LIST);

		when(
				commentDao.findCommentsByType(Mockito.eq(user1), Mockito.eq(comps), Mockito.anyCollection(), Mockito.anyInt(),
						Mockito.anyInt())).thenReturn(Collections.singletonList(comment2));
		when(
				commentDao.findCommentsByType(Mockito.eq(user2), Mockito.anyCollection(), Mockito.anyCollection(), Mockito.anyInt(),
						Mockito.anyInt())).thenReturn(Collections.EMPTY_LIST);

		final List<CommentModel> comments1 = commentService.getComments(user1, comps, -1, -1);
		Assert.assertTrue(compareCollections(user1List, comments1));
		final List<CommentModel> empty1 = commentService.getComments(user2, comps, -1, -1);
		Assert.assertTrue(empty1.isEmpty());

		final List<CommentModel> comments2 = commentService.getComments(user1, comps,
				Collections.singleton(new CommentTypeModel()), -1, -1);
		Assert.assertTrue(compareCollections(Collections.singleton(comment2), comments2));
		final List<CommentModel> empty2 = commentService.getComments(user2, comps, Collections.singleton(new CommentTypeModel()),
				-1, -1);
		Assert.assertTrue(empty2.isEmpty());

		final List<CommentModel> comments3 = commentService.getComments(user1, domain, -1, -1);
		Assert.assertTrue(compareCollections(user1List, comments3));
		final List<CommentModel> empty3 = commentService.getComments(user2, domain, -1, -1);
		Assert.assertTrue(empty3.isEmpty());

		final List<CommentModel> comments4 = commentService.getComments(user1, domain,
				Collections.singleton(new CommentTypeModel()), -1, -1);
		Assert.assertTrue(compareCollections(Collections.singleton(comment2), comments4));
		final List<CommentModel> empty4 = commentService.getComments(user2, domain, Collections.singleton(new CommentTypeModel()),
				-1, -1);
		Assert.assertTrue(empty4.isEmpty());

		Mockito.verify(commentDao, Mockito.times(2)).findCommentsByUser(Mockito.eq(user1), Mockito.eq(comps), Mockito.anyInt(),
				Mockito.anyInt());
		Mockito.verify(commentDao, Mockito.times(2)).findCommentsByUser(Mockito.eq(user2), Mockito.anyCollection(),
				Mockito.anyInt(), Mockito.anyInt());
		Mockito.verify(commentDao, Mockito.times(2)).findCommentsByType(Mockito.eq(user1), Mockito.eq(comps),
				Mockito.anyCollection(), Mockito.anyInt(), Mockito.anyInt());
		Mockito.verify(commentDao, Mockito.times(2)).findCommentsByType(Mockito.eq(user2), Mockito.anyCollection(),
				Mockito.anyCollection(), Mockito.anyInt(), Mockito.anyInt());
	}

	@Test
	public void testReturnsItemComments()
	{
		final UserModel user1 = new UserModel();
		user1.setName("testUser1");
		final UserModel user2 = new UserModel();
		user2.setName("testUser2");

		final ItemModel product1 = new ProductModel();
		final ItemModel product2 = new ProductModel();

		final ComponentModel component = new ComponentModel();
		final List<ComponentModel> comps = Collections.singletonList(component);

		final DomainModel domain = new DomainModel();
		domain.setComponents(comps);

		final CommentModel comment1 = new CommentModel();
		comment1.setAuthor(user1);
		comment1.setRelatedItems(Collections.singletonList(product1));

		final CommentModel comment2 = new CommentModel();
		comment2.setAuthor(user1);
		comment2.setRelatedItems(Collections.singletonList(product2));


		when(
				commentDao.findCommentsByItem(Mockito.eq(user1), Mockito.eq(comps), Mockito.eq(product1), Mockito.anyInt(),
						Mockito.anyInt())).thenReturn(Collections.singletonList(comment1));
		when(
				commentDao.findCommentsByItem(Mockito.eq(user1), Mockito.eq(comps), Mockito.eq(product2), Mockito.anyInt(),
						Mockito.anyInt())).thenReturn(Collections.singletonList(comment2));
		when(
				commentDao.findCommentsByItem(Mockito.eq(user2), Mockito.anyCollection(), Mockito.any(ItemModel.class),
						Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.EMPTY_LIST);
		when(
				commentDao.findCommentsByItemAndType(Mockito.eq(user1), Mockito.eq(comps), Mockito.eq(product1),
						Mockito.anyCollection(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.singletonList(comment1));
		when(
				commentDao.findCommentsByItemAndType(Mockito.eq(user2), Mockito.anyCollection(), Mockito.any(ItemModel.class),
						Mockito.anyCollection(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(Collections.EMPTY_LIST);

		final List<CommentModel> comments1 = commentService.getItemComments(product1, user1, comps, -1, -1);
		Assert.assertTrue(compareCollections(Collections.singletonList(comment1), comments1));
		final List<CommentModel> comments1_2 = commentService.getItemComments(product2, user1, comps, -1, -1);
		Assert.assertTrue(compareCollections(Collections.singletonList(comment2), comments1_2));
		final List<CommentModel> empty1 = commentService.getItemComments(product1, user2, comps, -1, -1);
		Assert.assertTrue(empty1.isEmpty());

		final List<CommentModel> comments2 = commentService.getItemComments(product1, user1, comps,
				Collections.singleton(new CommentTypeModel()), -1, -1);
		Assert.assertTrue(compareCollections(Collections.singleton(comment1), comments2));
		final List<CommentModel> empty2 = commentService.getItemComments(product1, user2, comps,
				Collections.singleton(new CommentTypeModel()), -1, -1);
		Assert.assertTrue(empty2.isEmpty());

		final List<CommentModel> comments3 = commentService.getItemComments(product1, user1, domain, -1, -1);
		Assert.assertTrue(compareCollections(Collections.singletonList(comment1), comments3));
		final List<CommentModel> empty3 = commentService.getItemComments(product1, user2, domain, -1, -1);
		Assert.assertTrue(empty3.isEmpty());

		final List<CommentModel> comments4 = commentService.getItemComments(product1, user1, domain,
				Collections.singleton(new CommentTypeModel()), -1, -1);
		Assert.assertTrue(compareCollections(Collections.singleton(comment1), comments4));
		final List<CommentModel> empty4 = commentService.getItemComments(product1, user2, domain,
				Collections.singleton(new CommentTypeModel()), -1, -1);
		Assert.assertTrue(empty4.isEmpty());

		Mockito.verify(commentDao, Mockito.times(2)).findCommentsByItem(Mockito.eq(user1), Mockito.eq(comps), Mockito.eq(product1),
				Mockito.anyInt(), Mockito.anyInt());
		Mockito.verify(commentDao, Mockito.times(1)).findCommentsByItem(Mockito.eq(user1), Mockito.eq(comps), Mockito.eq(product2),
				Mockito.anyInt(), Mockito.anyInt());
		Mockito.verify(commentDao, Mockito.times(2)).findCommentsByItem(Mockito.eq(user2), Mockito.anyCollection(),
				Mockito.any(ItemModel.class), Mockito.anyInt(), Mockito.anyInt());
		Mockito.verify(commentDao, Mockito.times(2)).findCommentsByItemAndType(Mockito.eq(user1), Mockito.eq(comps),
				Mockito.eq(product1), Mockito.anyCollection(), Mockito.anyInt(), Mockito.anyInt());
		Mockito.verify(commentDao, Mockito.times(2)).findCommentsByItemAndType(Mockito.eq(user2), Mockito.anyCollection(),
				Mockito.any(ItemModel.class), Mockito.anyCollection(), Mockito.anyInt(), Mockito.anyInt());
	}

}
