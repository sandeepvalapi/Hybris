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
package de.hybris.platform.validation.pojos;

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.validation.enums.Severity;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.SizeConstraintModel;

import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.Length;

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
public class SamplePojo
{
    private static final Logger LOG = Logger.getLogger(SamplePojo.class);

    @Length(min = 5 , max =  10)
    private String stringField;

    public String getStringField()
    {
        return stringField;
    }

    public void setStringField(final String stringField)
    {
        this.stringField = stringField;
    }

    public static Collection<? extends AttributeConstraintModel> buildConstraintModel(final ModelService service){

        final SizeConstraintModel size = service.create(SizeConstraintModel.class);
        size.setId(SamplePojo.class.getName()+"-"+SizeConstraintModel._TYPECODE);
        size.setTarget(SamplePojo.class);
        size.setQualifier("stringField");
        size.setMin(Long.valueOf(5L));
        size.setMax(Long.valueOf(10L));
        size.setSeverity(Severity.ERROR);

        return Arrays.asList(size);
    }
}
