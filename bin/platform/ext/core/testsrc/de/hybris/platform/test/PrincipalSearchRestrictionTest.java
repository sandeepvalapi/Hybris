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
package de.hybris.platform.test;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;


@IntegrationTest
public class PrincipalSearchRestrictionTest extends ServicelayerBaseTest
{

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final PropertyConfigSwitcher persistenceLegacyMode = new PropertyConfigSwitcher("persistence.legacy.mode");

    @Resource
    private ModelService modelService;

    @Resource
    private SessionService sessionService;

    @Resource
    private TypeService typeService;


    @After
    public void tearDown()
    {
        persistenceLegacyMode.switchBackToDefault();
    }


    @Test
    public void shouldDisableSearchRestrictionsForPrincipalToPrincipalGroupRelationJalo()
    {

        persistenceLegacyMode.switchToValue("true");


        shouldDisableSearchRestrictionsForPrincipalToPrincipalGroupRelation();

    }


    @Test
    public void shouldDisableSearchRestrictionsForPrincipalToPrincipalGroupRelationSld()
    {

        persistenceLegacyMode.switchToValue("false");


        shouldDisableSearchRestrictionsForPrincipalToPrincipalGroupRelation();

    }

    @Test
    public void shouldGroupsCycleCheckValidatorWorkWithoutSearchRestrictions()
    {
        persistenceLegacyMode.switchToValue("false");

        final UserModel user = modelService.create(UserModel.class);

        user.setUid("testUser");


        final UserGroupModel superGroup = modelService.create(UserGroupModel.class);

        superGroup.setUid("superGroup");

        final UserGroupModel subGroup = modelService.create(UserGroupModel.class);

        subGroup.setUid("subGroup");

        final UserGroupModel userGroup = modelService.create(UserGroupModel.class);

        userGroup.setUid("userGroup");
        userGroup.setGroups(Collections.singleton(superGroup));
        userGroup.setMembers(Collections.singleton(subGroup));

        superGroup.setGroups(Collections.singleton(userGroup));


        thrown.expect(ModelSavingException.class);
        thrown.expectCause(IsInstanceOf.instanceOf(InterceptorException.class));
        thrown.expectMessage("Cycle in groups was detected!");


        modelService.saveAll();

    }

    private void shouldDisableSearchRestrictionsForPrincipalToPrincipalGroupRelation()
    {
        final UserModel user = modelService.create(UserModel.class);

        user.setUid("testUser");


        final UserGroupModel userGroupJacksons = modelService.create(UserGroupModel.class);

        userGroupJacksons.setUid("Jacksons");

        final UserGroupModel userGroupFlinstones = modelService.create(UserGroupModel.class);

        userGroupFlinstones.setUid("Flinstones");

        final Set<PrincipalGroupModel> groups = new HashSet<>();
        groups.add(userGroupJacksons);
        groups.add(userGroupFlinstones);

        user.setGroups(groups);

        modelService.saveAll();


        final SearchRestrictionModel search = modelService.create(SearchRestrictionModel.class);

        search.setPrincipal(user);

        final SearchRestrictionModel searchRestriction = modelService.create(SearchRestrictionModel.class);
        searchRestriction.setActive(Boolean.TRUE);
        searchRestriction.setGenerate(Boolean.TRUE);
        searchRestriction.setCode("not_Jacksons");
        searchRestriction.setPrincipal(user);
        searchRestriction.setQuery("{" + PrincipalGroupModel.UID + "} NOT IN ( 'Jacksons' )");
        searchRestriction.setRestrictedType(typeService.getComposedTypeForClass(PrincipalGroupModel.class));

        modelService.save(searchRestriction);


        final Integer size = sessionService.executeInLocalView(new SessionExecutionBody()
        {
            @Override
            public Object execute()
            {
                final UserModel userToCheck = modelService.get(user.getPk());

                return userToCheck.getGroups().size();
            }
        }, user);

        assertThat(size).isEqualTo(2);
    }
}
