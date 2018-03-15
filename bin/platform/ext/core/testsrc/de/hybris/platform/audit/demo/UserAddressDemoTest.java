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

package de.hybris.platform.audit.demo;

import de.hybris.platform.audit.AuditTestHelper;
import de.hybris.platform.audit.AuditableTest;
import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.internal.config.AuditReportConfig;
import de.hybris.platform.audit.view.AuditViewService;
import de.hybris.platform.audit.view.impl.ReportView;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Test;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class UserAddressDemoTest extends ServicelayerBaseTest implements AuditableTest
{

    @Resource
    private ModelService modelService;

    @Resource
    private AuditViewService auditViewService;

    private AuditTestHelper auditTestHelper = new AuditTestHelper();

    @Test
    public void shouldGenerateAuditForDeletedUser()
    {

        final UserModel owner = modelService.create(UserModel.class);
        owner.setUid(UUID.randomUUID().toString());
        modelService.save(owner);

        AddressModel address = modelService.create(AddressModel.class);
        address.setTown("Paris");
        address.setOwner(owner);

        final UserModel user = modelService.create(UserModel.class);
        user.setUid(UUID.randomUUID().toString());
        final String name = "someName";
        user.setName(name);
        user.setDefaultPaymentAddress(address);

        modelService.save(address);
        modelService.save(user);

        modelService.remove(user);

        String town = "Brussels";
        address.setTown(town);
        modelService.save(address);


        final AuditReportConfig testConfig = auditTestHelper.createTestConfigForIntegrationTest();
        final List<ReportView> reportView = auditViewService
                .getViewOn(
                        TypeAuditReportConfig.builder().withConfig(testConfig).withRootTypePk(user.getPk()).withFullReport().build())
                .collect(toList());

        for (ReportView report:reportView)
        {
            System.out.println("Payload: " + report.getPayload() + "  Timesamp: " + report.getTimestamp());
        }

        assertThat(reportView.size()).isEqualTo(2);

        final Map<String, Object> userMap = (Map<String, Object>) reportView.get(0).getPayload().get("User");
        assertThat(userMap.get("name")).isEqualTo(name);
        final String payload = reportView.get(1).getPayload().toString();
        assertThat(payload).contains("Deleted at");
        assertThat(payload).doesNotContain(town);
    }
}