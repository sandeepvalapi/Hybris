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
package de.hybris.platform.europe1.jalo;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.europe1.PDTRowTestDataBuilder;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.enums.ProductDiscountGroup;
import de.hybris.platform.europe1.enums.ProductPriceGroup;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.internal.model.impl.PersistenceTestUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.util.StandardDateRange;
import de.hybris.platform.util.persistence.PersistenceUtils;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class PDTRowTest extends ServicelayerBaseTest
{
	@Resource
	ModelService modelService;
	@Resource
	TypeService typeService;
	@Resource
	EnumerationService enumerationService;

	private static final PropertyConfigSwitcher pdtRowProductModified = new PropertyConfigSwitcher(
			Europe1Constants.PDTROW_MARK_PRODUCT_MODIFIED);

	ProductModel product;
	String priceGroupCode;
	String discountGroupCode;
	String userGroupCode;

	UserModel user;
	EnumerationValueModel userPriceGroup;


	@After
	public void tearDown()
	{
		pdtRowProductModified.switchBackToDefault();
	}

	@Before
	public void doBefore()
	{
		final PDTRowTestDataBuilder testDataBuilder = new PDTRowTestDataBuilder(modelService);
		priceGroupCode = UUID.randomUUID().toString();
		testDataBuilder.createProductPriceGroup(priceGroupCode);
		discountGroupCode = UUID.randomUUID().toString();
		testDataBuilder.createProductDiscountGroup(discountGroupCode);
		userGroupCode = UUID.randomUUID().toString();
		userPriceGroup = testDataBuilder.createUserPriceGroup(userGroupCode);

		product = testDataBuilder.createProduct("PROD-1");
		user = testDataBuilder.createUser(UUID.randomUUID().toString());
	}


	@Test
	public void canSetOnlySingleProductReferenceViaJalo() throws JaloGenericCreationException, JaloAbstractTypeException
	{
		final DiscountModel discount = modelService.create(DiscountModel.class);
		discount.setCode(UUID.randomUUID().toString());
		modelService.save(discount);

		try
		{
			ComposedType.newInstance(JaloSession.getCurrentSession().getSessionContext(), DiscountRow.class, DiscountRow.DISCOUNT,
					modelService.getSource(discount), DiscountRow.PRODUCT, modelService.getSource(product), DiscountRow.PRODUCTID,
					"lol");

			fail();
		}
		catch (final Exception e)
		{
			assertThat(e).isInstanceOf(JaloGenericCreationException.class);
		}

		try
		{
			ComposedType.newInstance(JaloSession.getCurrentSession().getSessionContext(), DiscountRow.class, DiscountRow.DISCOUNT,
					modelService.getSource(discount), DiscountRow.PRODUCT, modelService.getSource(product), DiscountRow.PG,
					ProductPriceGroup.valueOf(priceGroupCode));

			fail();
		}
		catch (final Exception e)
		{
			assertThat(e).isInstanceOf(JaloGenericCreationException.class);
		}

		try
		{
			ComposedType.newInstance(JaloSession.getCurrentSession().getSessionContext(), DiscountRow.class, DiscountRow.DISCOUNT,
					modelService.getSource(discount), DiscountRow.PRODUCT, modelService.getSource(product), DiscountRow.PG,
					ProductPriceGroup.valueOf(priceGroupCode), DiscountRow.PRODUCTID, "lol");

			fail();
		}
		catch (final Exception e)
		{
			assertThat(e).isInstanceOf(JaloGenericCreationException.class);
		}

		ComposedType.newInstance(JaloSession.getCurrentSession().getSessionContext(), DiscountRow.class, DiscountRow.DISCOUNT,
				modelService.getSource(discount), DiscountRow.PRODUCTID, "lol");
	}



	@Test
	public void shouldMarkProductAsModifiedWhenUpdating()
	{
		pdtRowProductModified.switchToValue("true");

		PersistenceUtils.<Void> doWithSLDPersistence(() -> {
			final DiscountModel discount = createDiscount();
			modelService.save(discount);

			final Date modifiedTime = product.getModifiedtime();

			sleepTwoSecs();

			final DiscountRowModel discountRowWithProduct = createDiscountRow(discount);
			discountRowWithProduct.setProduct(product);
			modelService.save(discountRowWithProduct);

			assertThat(product.getPk().getLong()).isEqualTo(discountRowWithProduct.getProductMatchQualifier());

			modelService.refresh(product);
			final Date laterModified = product.getModifiedtime();

			sleepTwoSecs();

			modelService.remove(discountRowWithProduct);

			modelService.refresh(product);
			final Date moreLaterModified = product.getModifiedtime();

			assertThat(modifiedTime.toInstant().toEpochMilli()).isLessThan(laterModified.toInstant().toEpochMilli());
			assertThat(laterModified.toInstant().toEpochMilli()).isLessThan(moreLaterModified.toInstant().toEpochMilli());

			return null;
		});
	}

	private void sleepTwoSecs()
	{
		try
		{
			Thread.sleep(2_000);
		}
		catch (final InterruptedException e)
		{
			e.printStackTrace();
		}
	}


	@Test
	public void shouldSaveViaSLDAndReplicateProductMatchQualifierBehaviour()
	{
		pdtRowProductModified.switchToValue("false");

		PersistenceUtils.<Void> doWithSLDPersistence(() -> {
			final DiscountModel discount = createDiscount();
			modelService.save(discount);

			final DiscountRowModel discountRowWithProduct = createDiscountRow(discount);
			discountRowWithProduct.setProduct(product);
			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, discountRowWithProduct);

			assertThat(product.getPk().getLong()).isEqualTo(discountRowWithProduct.getProductMatchQualifier());
			PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(discountRowWithProduct);

			final ProductDiscountGroup productDiscountGroup = ProductDiscountGroup.valueOf(discountGroupCode);

			final DiscountRowModel discountRowWithPg = createDiscountRow(discount);
			discountRowWithPg.setPg(productDiscountGroup);
			discountRowWithPg.setDateRange(new StandardDateRange(new Date(), new Date()));
			discountRowWithPg.setStartTime(new Date(1L));
			discountRowWithPg.setEndTime(new Date(2L));

			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, discountRowWithPg);

			final EnumerationValueModel pgModel = typeService.getEnumerationValue(productDiscountGroup.getType(),
					productDiscountGroup.getCode());

			assertThat(pgModel.getPk().getLong()).isEqualTo(discountRowWithPg.getProductMatchQualifier());
			PersistenceTestUtils.verifyThatUnderlyingPersistenceObjectIsSld(discountRowWithPg);

			final DiscountRowModel discountRowWithProductId = createDiscountRow(discount);
			discountRowWithProductId.setProductId("lool");
			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, discountRowWithProductId);

			assertThat(discountRowWithProductId.getProductMatchQualifier()).isEqualTo(Europe1PriceFactory.MATCH_BY_PRODUCT_ID);

			discountRowWithProductId.setProductMatchQualifier(65L);
			modelService.save(discountRowWithProductId);
			assertThat(discountRowWithProductId.getProductMatchQualifier()).isEqualTo(Europe1PriceFactory.MATCH_BY_PRODUCT_ID);

			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, discountRowWithProductId);

			return null;
		});
	}



	@Test
	public void rowCanOnlyHaveUserOrUserGroupAssignedInJalo() throws JaloGenericCreationException, JaloAbstractTypeException
	{
		final DiscountModel discount = createDiscount();
		modelService.save(discount);

		ComposedType.newInstance(JaloSession.getCurrentSession().getSessionContext(), DiscountRow.class, DiscountRow.DISCOUNT,
				modelService.getSource(discount), DiscountRow.PRODUCTID, "lol", DiscountRow.USER, modelService.getSource(user));

		try
		{
			ComposedType.newInstance(JaloSession.getCurrentSession().getSessionContext(), DiscountRow.class, DiscountRow.DISCOUNT,
					modelService.getSource(discount), DiscountRow.PRODUCTID, "lol", DiscountRow.USER, modelService.getSource(user),
					DiscountRow.UG, UserPriceGroup.valueOf(userGroupCode));

			fail();
		}
		catch (final Exception e)
		{
			assertThat(e).isInstanceOf(JaloGenericCreationException.class);
		}
	}


	@Test
	public void rowCanOnlyHaveUserOrUserGroupAssignedInSLD()
	{
		PersistenceUtils.<Void> doWithSLDPersistence(() -> {
			final DiscountModel discount = createDiscount();
			modelService.save(discount);

			final DiscountRowModel discountRowWithUser = createDiscountRow(discount);
			discountRowWithUser.setProductId(UUID.randomUUID().toString());
			discountRowWithUser.setUser(user);

			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, discountRowWithUser);

			assertThat(user.getPk().getLong()).isEqualTo(discountRowWithUser.getUserMatchQualifier().longValue());

			final DiscountRowModel discountRowWithUserGroup = createDiscountRow(discount);
			discountRowWithUserGroup.setProductId(UUID.randomUUID().toString());
			discountRowWithUserGroup.setUg(UserPriceGroup.valueOf(userGroupCode));

			PersistenceTestUtils.saveAndVerifyThatPersistedThroughSld(modelService, discountRowWithUserGroup);

			assertThat(user.getPk().getLong()).isEqualTo(discountRowWithUser.getUserMatchQualifier().longValue());

			try
			{
				final DiscountRowModel discountRowWithUserGroupAndUser = createDiscountRow(discount);
				discountRowWithUserGroupAndUser.setProductId(UUID.randomUUID().toString());
				discountRowWithUserGroupAndUser.setUser(user);
				discountRowWithUserGroupAndUser.setUg(UserPriceGroup.valueOf(userGroupCode));

				modelService.save(discountRowWithUserGroupAndUser);
				fail();
			}
			catch (final Exception e)
			{
				assertThat(e).isInstanceOf(ModelSavingException.class);
			}

			return null;
		});
	}

	private DiscountModel createDiscount()
	{
		final DiscountModel discount = modelService.create(DiscountModel.class);
		discount.setCode(UUID.randomUUID().toString());
		return discount;
	}

	private DiscountRowModel createDiscountRow(final DiscountModel discount)
	{
		final DiscountRowModel discountRowWithUser = modelService.create(DiscountRowModel.class);
		discountRowWithUser.setDiscount(discount);
		return discountRowWithUser;
	}
}
