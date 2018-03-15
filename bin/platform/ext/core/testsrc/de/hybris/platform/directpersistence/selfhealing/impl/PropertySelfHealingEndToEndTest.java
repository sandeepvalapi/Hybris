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
package de.hybris.platform.directpersistence.selfhealing.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.directpersistence.DefaultSLDUnsafeTypesProvider;
import de.hybris.platform.directpersistence.impl.DefaultJaloAccessorsService;
import de.hybris.platform.directpersistence.selfhealing.ItemToHeal;
import de.hybris.platform.directpersistence.selfhealing.SelfHealingService;
import de.hybris.platform.directpersistence.selfhealing.impl.DefaultSelfHealingService.TestListener;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.internal.model.extractor.impl.DefaultPersistenceTypeService;
import de.hybris.platform.servicelayer.internal.model.impl.SourceTransformer;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.Config;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class PropertySelfHealingEndToEndTest extends ServicelayerBaseTest
{

	@Resource
	ModelService modelService;

	@Resource
	FlexibleSearchService flexibleSearchService;

	@Resource
	SelfHealingService selfHealingService;

	@Resource
	SourceTransformer sourceTransformer;


	@Test
	public void testPropertySelfHealingOnSLD() throws InterruptedException
	{
		enableDirectMode();
		ensureItemsCanUseSLD();

		enableSelfHealing();
		try
		{
			testPropertySelfHealingEndToEnd(true, true); // wait for 5 seconds
		}
		finally
		{
			restoreSelfHealing();
			restorePersistenceMode();
		}
	}

	@Test
	public void testPropertySelfHealingOffSLD() throws InterruptedException
	{
		enableDirectMode();
		ensureItemsCanUseSLD();

		disableSelfHealing();
		try
		{
			testPropertySelfHealingEndToEnd(false, true); // wait for 5 seconds
		}
		finally
		{
			restoreSelfHealing();
			restorePersistenceMode();
		}
	}

	@Test
	public void testPropertySelfHealingOffLegacyMode() throws InterruptedException
	{
		forceLegacyMode();
		disableSelfHealing();
		try
		{
			testPropertySelfHealingEndToEnd(true, false); // wait for 5 seconds
		}
		finally
		{
			restoreSelfHealing();
			restorePersistenceMode();
		}
	}

	@Test
	public void testPropertySelfHealingOnLegacyMode() throws InterruptedException
	{
		forceLegacyMode();
		enableSelfHealing();
		try
		{
			testPropertySelfHealingEndToEnd(true, false); // wait for 5 seconds
		}
		finally
		{
			restoreSelfHealing();
			restorePersistenceMode();
		}
	}


	protected void testPropertySelfHealingEndToEnd(final boolean enabled, final boolean sld) throws InterruptedException
	{
		final MediaContainerModel container = createContainerAndMedia();
		final MediaModel media = container.getMedias().iterator().next();

		assertEquals("media should be found via container PK", 1, lookupMediaByContainerPK(container.getPk()));

		modelService.remove(container);

		assertEquals("media should still be found via container PK (before self-healing)", 1,
				lookupMediaByContainerPK(container.getPk()));

		modelService.refresh(media);

		final PK itemToHealPK = media.getPk();

		if (sld && enabled)
		{
			try
			{
				final CountDownLatch added = new CountDownLatch(1);
				final CountDownLatch done = new CountDownLatch(1);
				((DefaultSelfHealingService) selfHealingService).setTestListener(new TestListener()
				{
					@Override
					public void notifyDone(final ItemToHeal i)
					{
						if (itemToHealPK.equals(i.getPk()))
						{
							done.countDown();
						}
					}

					@Override
					public void notifyAdded(final ItemToHeal i)
					{
						if (itemToHealPK.equals(i.getPk()))
						{
							added.countDown();
						}
					}
				});
				assertNull("media shouldn't have a container any more", media.getMediaContainer());
				assertTrue("item not added", added.await(10, TimeUnit.SECONDS));
				assertTrue("item not done", done.await(10, TimeUnit.SECONDS));
			}
			finally
			{
				((DefaultSelfHealingService) selfHealingService).removeTestListener();
			}
		}
		else
		{
			assertNull("media shouldn't have a container any more", media.getMediaContainer());
		}


		if (enabled)
		{
			assertEquals("media should not be found via container PK any more ( after self-healing)", 0,
					lookupMediaByContainerPK(container.getPk()));
		}
		else
		{
			if (sld)
			{
				// need to wait some time in case self healing is *wrongly* active here
				Thread.sleep(1500);
			}

			assertEquals("media should still be found via container PK ( self healing off)", 1,
					lookupMediaByContainerPK(container.getPk()));
		}
	}

	int lookupMediaByContainerPK(final PK containerPK)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT count({PK}) FROM {Media} WHERE {mediaContainer}=?cont",
				Collections.singletonMap("cont", containerPK));
		query.setDisableCaching(true); // self healing may not cause cache invalidation !!!
		query.setResultClassList(Collections.singletonList(Integer.class));

		return ((Integer) flexibleSearchService.search(query).getResult().get(0)).intValue();
	}

	void ensureItemsCanUseSLD()
	{
		assertFalse("Media cannot use SLD", sourceTransformer.mustBeBackedByJalo("Media"));
		assertFalse("MediaContainer cannot use SLD", sourceTransformer.mustBeBackedByJalo("MediaContainer"));
	}

	MediaContainerModel createContainerAndMedia()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId("cat");

		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setVersion("dog");
		catalogVersion.setCatalog(catalog);

		final MediaContainerModel container = modelService.create(MediaContainerModel.class);
		container.setQualifier("foo");
		container.setCatalogVersion(catalogVersion);

		final MediaModel media = modelService.create(MediaModel.class);
		media.setCode("bar");
		media.setMediaContainer(container);
		media.setCatalogVersion(catalogVersion);

		modelService.saveAll(catalog, catalogVersion, container, media);

		return container;
	}

	// ----------------------------------------------------------------
	// --- Helpers
	// ----------------------------------------------------------------


	private String cfgBefore;
	private String unsafeIgnoreBefore;
	private boolean cfgSaved;
	

	private Boolean selfHealingModeChange;
	private int selfHealingIntervalBefore = -1;

	protected void enableDirectMode()
	{
		if (!cfgSaved)
		{
			cfgBefore = Config.getParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE);
			unsafeIgnoreBefore = Config.getParameter(DefaultJaloAccessorsService.CFG_IGNORE_MARKED_UNSAFE);
			cfgSaved = true;
		}
		Config.setParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE, "false");
		Config.setParameter(DefaultJaloAccessorsService.CFG_IGNORE_MARKED_UNSAFE, "true");
		sourceTransformer.resetCache();
	}

	protected void forceLegacyMode()
	{
		if (!cfgSaved)
		{
			cfgBefore = Config.getParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE);
			unsafeIgnoreBefore = Config.getParameter(DefaultJaloAccessorsService.CFG_IGNORE_MARKED_UNSAFE);
			cfgSaved = true;
		}
		Config.setParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE, "true");
	}

	protected void restorePersistenceMode()
	{
		if (cfgSaved)
		{
			Config.setParameter(DefaultPersistenceTypeService.PERSISTENCE_LEGACY_MODE, cfgBefore);
			Config.setParameter(DefaultJaloAccessorsService.CFG_IGNORE_MARKED_UNSAFE, unsafeIgnoreBefore);
			sourceTransformer.resetCache();

			cfgBefore = null;
			unsafeIgnoreBefore = null;

			cfgSaved = false;
		}
	}

	protected void enableSelfHealing()
	{
		final DefaultSelfHealingService service = (DefaultSelfHealingService) selfHealingService;

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
		final DefaultSelfHealingService service = (DefaultSelfHealingService) selfHealingService;

		if (service.isEnabled())
		{
			service.setEnabled(false);
			service.applyWorkerSettings(); // stops worker
			selfHealingModeChange = Boolean.FALSE;
		}
	}

	protected void restoreSelfHealing()
	{
		final DefaultSelfHealingService service = (DefaultSelfHealingService) selfHealingService;
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
