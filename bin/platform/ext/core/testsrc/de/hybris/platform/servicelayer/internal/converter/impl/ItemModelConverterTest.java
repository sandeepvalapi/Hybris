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
package de.hybris.platform.servicelayer.internal.converter.impl;

import static junit.framework.Assert.assertTrue;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.directpersistence.selfhealing.ItemToHeal;
import de.hybris.platform.directpersistence.selfhealing.impl.DefaultSelfHealingService;
import de.hybris.platform.directpersistence.statement.backend.ServiceCol;
import de.hybris.platform.directpersistence.statement.sql.FluentSqlBuilder;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.model.ModelContext;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.ItemPropertyValueCollection;

import java.util.Date;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.collect.Sets;


@IntegrationTest
public class ItemModelConverterTest extends ServicelayerBaseTest
{

	/**
	 *
	 */
	private static final String ROLLBACK_EXCEPTION_MESSAGE = "Exception to create a rollback";

	private DiscountModel discountModel;

	@Resource
	private ModelService modelService;

	@Resource
	private ConverterRegistry converterRegistry;

	@Resource
	private I18NService i18NService;

	@Resource
	private ModelContext modelContext;

	@Resource
	private TypeService typeService;

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Resource
	private DefaultSelfHealingService defaultSelfHealingService;



	@Before
	public void setUp()
	{
		createAndPrepareDiscount();

	}



	private void createAndPrepareDiscount()
	{
		final CurrencyModel currencyModel = modelService.create(CurrencyModel.class);
		currencyModel.setIsocode("EUR");

		discountModel = modelService.create(DiscountModel.class);
		discountModel.setCode("discount1");
		discountModel.setCurrency(currencyModel);

		modelService.saveAll(currencyModel, discountModel);
	}

	// PLA-10433
	@Test
	public void testJaloAttrInModelRefOldValue()
	{
		final String discountStringBefore = discountModel.getDiscountString();

		final CurrencyModel newCurrencyModel = modelService.create(CurrencyModel.class);
		newCurrencyModel.setIsocode("USD");
		modelService.save(newCurrencyModel);

		discountModel.setCurrency(newCurrencyModel);
		modelService.save(discountModel);

		final String discountStringAfter = discountModel.getDiscountString();

		assertFalse("The discountString values for two different currencies should be different too!",
				discountStringBefore.equals(discountStringAfter));
	}

	@Test
	public void testIsAttributeModifiedLocalized()
	{
		i18NService.setCurrentLocale(Locale.UK);
		final ModelConverter converter = converterRegistry.getModelConverterByModelType(TitleModel.class);

		final TitleModel model = modelService.create(TitleModel.class);
		model.setCode("test");
		model.setName("test1");
		modelService.save(model);

		assertFalse(converter.isModified(model, TitleModel.NAME));
		model.setName("test2");
		assertTrue("Attribute not marked as modified!", converter.isModified(model, TitleModel.NAME));
		modelService.save(model);

		i18NService.setCurrentLocale(Locale.UK);
		assertFalse(converter.isModified(model, TitleModel.NAME));
		model.setName("test2");
		assertTrue("Attribute not marked as modified!", converter.isModified(model, TitleModel.NAME));
		modelService.save(model);
	}

	@Test
	public void testUnchangedAttributeNotBeingDirtyAfterSave()
	{
		final TitleModel m = new TitleModel();
		m.setCode("t" + System.nanoTime());

		assertTrue(modelService.isNew(m));

		modelService.save(m);

		assertFalse(modelService.isNew(m));
		assertFalse(modelService.isModified(m));
		assertTrue(modelService.isUpToDate(m));

		// setter called but value not changed
		m.setCode(m.getCode());
		assertTrue(modelService.isModified(m));
		// another setter called - this time there is a change
		m.setName("name");
		assertTrue(modelService.isModified(m));

		modelService.save(m);

		assertFalse(modelService.isModified(m));
		assertTrue(modelService.isUpToDate(m));
	}

	@Test
	public void testModifiedTimeAlwaysReloaded() throws InterruptedException
	{
		final TitleModel m = new TitleModel();
		m.setCode("t" + System.nanoTime());
		modelService.save(m);
		m.setName("foo");
		modelService.save(m);

		final Date modTime1 = m.getModifiedtime();

		Thread.sleep(3 * 1000);

		m.setName("bar");
		modelService.save(m);

		final Date modTime2 = m.getModifiedtime();

		assertFalse(modTime1.equals(modTime2));
	}

	@Test
	public void testSelfHealingServiceForSingleAttributeWhenConvertMissingItem()
	{
		try
		{
			enableSelfHealing();
			//given
			final CatalogModel catalogModel = modelService.create(CatalogModel.class);
			catalogModel.setId("id");

			final CompanyModel companyModel = modelService.create(CompanyModel.class);
			companyModel.setId("id_comp");
			companyModel.setUid("id_comp");

			catalogModel.setBuyer(companyModel);

			modelService.saveAll();


			modelService.remove(companyModel);

			modelContext.clear();

			final CatalogModel catalogModelToTest = modelService.get(catalogModel.getPk());

			//when
			catalogModelToTest.getBuyer();

			defaultSelfHealingService.batchItems();

			//then
			final AttributeDescriptorModel attrDescBuyer = typeService.getAttributeDescriptor(CatalogModel._TYPECODE,
					CatalogModel.BUYER);


			final String sql = FluentSqlBuilder.genericBuilder().select(attrDescBuyer.getDatabaseColumn())
					.from(attrDescBuyer.getEnclosingType().getTable()).where().field(ServiceCol.PK_STRING.colName()).isEqual().toSql();

			final String persistedBuyer = jdbcTemplate.queryForObject(sql, new Object[]
			{ catalogModel.getPk().getLong() }, String.class);


			assertThat(persistedBuyer).isNullOrEmpty();
		}
		finally
		{
			restoreSelfHealing();
		}

	}

	@Test
	public void testSelfHealingServiceForCollectionAttributeWhenConvertMissingItem()
	{
		try
		{
			enableSelfHealing();

			//given
			final CatalogModel catalogModel = modelService.create(CatalogModel.class);
			catalogModel.setId("id_catalog");

			final CatalogVersionModel catalogVersionModel1 = modelService.create(CatalogVersionModel.class);
			catalogVersionModel1.setCatalog(catalogModel);
			catalogVersionModel1.setVersion("version1");

			final CatalogVersionModel catalogVersionModel2 = modelService.create(CatalogVersionModel.class);
			catalogVersionModel2.setCatalog(catalogModel);
			catalogVersionModel2.setVersion("version2");

			final CustomerModel customerModel = modelService.create(CustomerModel.class);
			customerModel.setUid("uid_customer");

			customerModel.setPreviewCatalogVersions(Sets.newHashSet(catalogVersionModel1, catalogVersionModel2));

			modelService.saveAll();

			assertThat(customerModel.getPreviewCatalogVersions()).hasSize(2);

			modelService.remove(catalogVersionModel2);

			modelContext.clear();

			final CustomerModel customerModelToTest = modelService.get(customerModel.getPk());

			//when
			customerModelToTest.getPreviewCatalogVersions();

			defaultSelfHealingService.batchItems();

			//then
			final ItemPropertyValue catalogVersionModel1ItemPropertyValue = new ItemPropertyValue(catalogVersionModel1.getPk());

			final ItemPropertyValueCollection itemPropertyValueCollection = new ItemPropertyValueCollection(
					Sets.newHashSet(catalogVersionModel1ItemPropertyValue));

			final ItemToHeal custumerToHeal = new ItemToHeal(customerModel.getPk(), CustomerModel._TYPECODE,
					CustomerModel.PREVIEWCATALOGVERSIONS, ((Item) modelService.getSource(customerModel)).getPersistenceVersion(),
					itemPropertyValueCollection);

		final AttributeDescriptorModel attrDescPreviewCatalogVersions = typeService.getAttributeDescriptor(CustomerModel._TYPECODE,
				CustomerModel.PREVIEWCATALOGVERSIONS);


			final String sql = FluentSqlBuilder.genericBuilder().select(attrDescPreviewCatalogVersions.getDatabaseColumn())
					.from(attrDescPreviewCatalogVersions.getEnclosingType().getTable()).where().field(ServiceCol.PK_STRING.colName())
					.isEqual().toSql();

			final String persistedPreviewCatalogVersionsDbValue = jdbcTemplate.queryForObject(sql, new Object[]
			{ custumerToHeal.getPk().getLong() }, String.class);


			final ItemPropertyValueCollection itemPropValueCollection = (ItemPropertyValueCollection) JDBCValueMappings.getInstance()
					.getValueReader(ItemPropertyValueCollection.class).convertValueToJava(persistedPreviewCatalogVersionsDbValue);

			assertThat(itemPropValueCollection).containsExactly(catalogVersionModel1ItemPropertyValue);
		}
		finally
		{
			restoreSelfHealing();
		}
	}


	@Test
	public void testSelfHealingServiceDoesNothingWhenRollback() throws Exception
	{
		try
		{
			enableSelfHealing();

			//given
			final CatalogModel catalogModel = modelService.create(CatalogModel.class);
			catalogModel.setId("id");

			final CompanyModel companyModel = modelService.create(CompanyModel.class);
			companyModel.setId("id_comp");
			companyModel.setUid("id_comp");

			catalogModel.setBuyer(companyModel);

			modelService.saveAll();

			//when
			try
			{
				Transaction.current().execute(new TransactionBody()
				{

					@Override
					public Object execute() throws Exception
					{
						modelService.remove(companyModel);

						modelContext.clear();
						Registry.getCurrentTenant().getCache().clear();

						final CatalogModel catalogModelToTest = modelService.get(catalogModel.getPk());

						catalogModelToTest.getBuyer();


						throw new Exception(ROLLBACK_EXCEPTION_MESSAGE);
					}
				});
			}
			catch (final Exception e)
			{
				if (!ROLLBACK_EXCEPTION_MESSAGE.equals(e.getMessage()))
				{
					throw e;
				}
			}



			defaultSelfHealingService.batchItems();

			//then
			final AttributeDescriptorModel attrDescBuyer = typeService.getAttributeDescriptor(CatalogModel._TYPECODE,
					CatalogModel.BUYER);


			final String sql = FluentSqlBuilder.genericBuilder().select(attrDescBuyer.getDatabaseColumn())
					.from(attrDescBuyer.getEnclosingType().getTable()).where().field(ServiceCol.PK_STRING.colName()).isEqual().toSql();

			final String persistedBuyer = jdbcTemplate.queryForObject(sql, new Object[]
			{ catalogModel.getPk().getLong() }, String.class);


			assertThat(persistedBuyer).isEqualTo(companyModel.getPk().getLongValueAsString());
		}
		finally
		{
			restoreSelfHealing();
		}

	}

	private Boolean selfHealingModeChange;
	private int selfHealingIntervalBefore = -1;

	protected void enableSelfHealing()
	{
		final DefaultSelfHealingService service = defaultSelfHealingService;

		if (!service.isEnabled())
		{
			selfHealingIntervalBefore = service.getInterval();
			service.setInterval(1); // 1 second
			service.setEnabled(true);
			service.applyWorkerSettings(); // starts worker
			selfHealingModeChange = Boolean.TRUE;
		}
	}

	protected void disableSelfHealing()
	{
		final DefaultSelfHealingService service = defaultSelfHealingService;

		if (service.isEnabled())
		{
			service.setEnabled(false);
			service.applyWorkerSettings(); // stops worker
			selfHealingModeChange = Boolean.FALSE;
		}
	}

	protected void restoreSelfHealing()
	{
		final DefaultSelfHealingService service = defaultSelfHealingService;
		if (Boolean.TRUE.equals(selfHealingModeChange))
		{
			service.setInterval(selfHealingIntervalBefore);
			service.setEnabled(false);
			service.applyWorkerSettings();
		}
		else if (Boolean.FALSE.equals(selfHealingModeChange))
		{
			service.setEnabled(true);
			service.applyWorkerSettings();
		}
	}


}
