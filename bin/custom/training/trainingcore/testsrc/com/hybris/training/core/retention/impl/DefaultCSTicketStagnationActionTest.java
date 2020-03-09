/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.training.core.retention.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.processing.model.FlexibleSearchRetentionRuleModel;
import de.hybris.platform.retention.ItemToCleanup;
import de.hybris.platform.retention.job.AfterRetentionCleanupJobPerformable;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.enums.CsResolutionType;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketBusinessService;
import de.hybris.platform.ticket.service.TicketException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DefaultCSTicketStagnationActionTest
{
    @InjectMocks
    private final DefaultCSTicketStagnationAction cleanupAction = new DefaultCSTicketStagnationAction();
    @Mock
    private ModelService modelService;
    @Mock
    private TicketBusinessService ticketBusinessService;

    private FlexibleSearchRetentionRuleModel rule;
    private ItemToCleanup item;
    private AfterRetentionCleanupJobPerformable retentionJob;


    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);

        rule = new FlexibleSearchRetentionRuleModel();
        item = mock(ItemToCleanup.class);
        retentionJob = new AfterRetentionCleanupJobPerformable();
    }

    @Test
    public void shouldCleanupAndInvokeHooks() throws TicketException
    {
        final CsTicketModel csTicketModel = mock(CsTicketModel.class);
        given(modelService.get(any(PK.class))).willReturn(csTicketModel);

        cleanupAction.cleanup(retentionJob, rule, item);
        verify(ticketBusinessService).resolveTicket(refEq(csTicketModel),
            eq(CsInterventionType.TICKETMESSAGE),
            eq(CsResolutionType.CLOSED),
            any());
    }
}
