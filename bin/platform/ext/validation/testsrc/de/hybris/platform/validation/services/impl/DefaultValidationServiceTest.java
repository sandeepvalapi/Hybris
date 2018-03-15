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
package de.hybris.platform.validation.services.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.validation.daos.ConstraintDao;
import de.hybris.platform.validation.exceptions.ConfigurableHybrisConstraintViolation;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.extractor.ConstraintsExtractor;
import de.hybris.platform.validation.pojos.SamplePojo;
import de.hybris.platform.validation.services.ValidationService;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;
import javax.validation.Configuration;
import javax.validation.ValidationException;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import junit.framework.Assert;

/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */

/**
 * Test created for PLA-13400
 */
public class DefaultValidationServiceTest extends ServicelayerBaseTest
{
    private static final Logger LOG = Logger.getLogger(DefaultValidationServiceTest.class);


    @Resource
    private ConstraintDao constraintDao;

    @Resource
    private ConstraintsExtractor constraintsExtractor;

    @Resource
    private FlexibleSearchService flexibleSearchService;

    @Resource
    private ModelService modelService;

    @Resource
    private SessionService sessionService;

    @Resource
    private ConfigurableHybrisConstraintViolation hybrisConstraintViolation;

    @Test
    @Ignore("PLA-13400")
    public void testLoadFailure()
    {
        final ValidationService validation = prepareValidationService();

        modelService.saveAll(SamplePojo.buildConstraintModel(modelService));

        validation.reloadValidationEngine();
        //target.setStringField("foo"); //too short

        final SamplePojo sample = new SamplePojo();
        sample.setStringField("not correct too long value for field");

        final Set<HybrisConstraintViolation> violations = validation.validate(sample, validation.getActiveConstraintGroups());
        //for(final HybrisConstraintViolation viol : violations)
        //{
        //   LOG.info(viol.getLocalizedMessage());
        //}
        Assert.assertEquals("Expects one violation", 1, violations.size());
    }

    /**
     *
     * Creates some handcrafted ValidationService which :
     * <ul>
     * <li>on first call getOrCreateConfiguration adds upfront config, so if error occurs at second time the getOrCreateConfiguration reverts current config (in result leaving it unchanged)</li>
     * <li>fails only on second (an all further) call getOrCreateConfiguration</li>
     * </ul>
     *
     */
    private ValidationService prepareValidationService()
    {
        final AtomicInteger errorCallThreshold = new AtomicInteger(1);//fist call fine

        final DefaultValidationService validation = new DefaultValidationService()
        {
            @Override
            protected Configuration getOrCreateConfiguration(final boolean replace)
            {
                final Configuration cfg = super.getOrCreateConfiguration(replace);

                if (errorCallThreshold.getAndDecrement() == 0)
                {
                    throw new ValidationException("Expected exception"); //force exception which remove the eldest config

                }
                else
                {
                    appendXMLMapping(cfg); //add config upfront
                    return cfg;
                }
            }

            @Override
            protected ConfigurableHybrisConstraintViolation lookupViolation()
            {
                return hybrisConstraintViolation;
            }
        };

        validation.setConstraintDao(constraintDao);
        validation.setConstraintsExtractor(constraintsExtractor);
        validation.setCurrentTenant(Registry.getCurrentTenantNoFallback());
        validation.setFlexibleSearchService(flexibleSearchService);
        validation.setModelService(modelService);
        validation.setSessionService(sessionService);
        return validation;
    }
}
