/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.job;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.model.CsTicketModel;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Locale;

import static org.junit.Assert.*;


@IntegrationTest
public class TicketsRetentionCronJobIntegrationTest extends ServicelayerTest
{
    private static final String TEST_BASESITE_UID = "testSite";

    @Resource
    private BaseSiteService baseSiteService;

    @Resource
    private FlexibleSearchService flexibleSearchService;

    @Resource
    private I18NService i18NService;

    @Resource
    private CronJobService cronJobService;

    @Resource
    private ModelService modelService;

    @Before
    public void setup() throws Exception
    {
        createCoreData();
        baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID(TEST_BASESITE_UID), false);
        importCsv("/trainingcore/import/common/cronjobs.impex", "utf-8");
        importCsv("/trainingcore/test/testTicketRetentionCronJob.impex", "utf-8");

        i18NService.setCurrentLocale(Locale.ENGLISH);
    }

    @Test
    public void testTicketsRetentionCronJob()
    {
        // order to be removed
        final CsTicketModel ticketModelTemplate = new CsTicketModel();
        ticketModelTemplate.setTicketID("test-ticket-7");
        final CsTicketModel ticketModel = flexibleSearchService.getModelByExample(ticketModelTemplate);
        assertNotNull("Comments", ticketModel.getComments());
        assertEquals("State", CsTicketState.CLOSED, ticketModel.getState());
        assertNotNull("RetentionDate", ticketModel.getRetentionDate());

        cronJobService.performCronJob(cronJobService.getCronJob("ticketsRetentionCronJob"), true);

        // ticket to be removed
        assertTrue("test-ticket-7 removed", modelService.isRemoved(ticketModel));

        // ticket not to be removed
        final CsTicketModel ticketModelTemplate2 = new CsTicketModel();
        ticketModelTemplate2.setTicketID("test-ticket-6");
        final CsTicketModel ticketNotToBeRemoved = flexibleSearchService.getModelByExample(ticketModelTemplate2);
        assertTrue("CsTicket not null", ticketNotToBeRemoved != null);
        assertFalse("CsTicket not removed", modelService.isRemoved(ticketNotToBeRemoved));
    }
}
