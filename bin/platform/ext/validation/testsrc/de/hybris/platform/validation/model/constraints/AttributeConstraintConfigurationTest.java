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
package de.hybris.platform.validation.model.constraints;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.test.TestItemType2Model;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.ValidationConfigurationException;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests AttributeConstraint validation.
 */
public class AttributeConstraintConfigurationTest extends AbstractConstraintTest
{

    public static final String ID = "_test_attribute_constraint_";

    @Test
    public void shouldCreateValidNullConstraint()
    {
        //given
        final AttributeConstraintModel constr = createAttributeConstraint(javax.validation.constraints.Null.class);
        final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
                ItemModel._TYPECODE, ItemModel.CREATIONTIME);
        constr.setDescriptor(descrModel);

        //when
        modelService.save(constr);

        //then
        assertEquals(ID, constr.getId());
        assertEquals(descrModel, constr.getDescriptor());
        assertEquals(javax.validation.constraints.Null.class, constr.getAnnotation());
    }

    @Test
    public void shouldCreateValidNotNullConstraint()
    {
        //given
        final AttributeConstraintModel constr = createAttributeConstraint(javax.validation.constraints.NotNull.class);
        final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
                ItemModel._TYPECODE, ItemModel.CREATIONTIME);
        constr.setDescriptor(descrModel);

        //when
        modelService.save(constr);

        //then
        assertEquals(ID, constr.getId());
        assertEquals(descrModel, constr.getDescriptor());
        assertEquals(javax.validation.constraints.NotNull.class, constr.getAnnotation());
    }

    @Test
    public void shouldCreateValidAssertFalseConstraint()
    {
        //given
        final AttributeConstraintModel constr = createAttributeConstraint(javax.validation.constraints.AssertFalse.class);
        final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
                UserModel._TYPECODE, UserModel.LOGINDISABLED);
        constr.setDescriptor(descrModel);

        //when
        modelService.save(constr);

        //then
        assertEquals(ID, constr.getId());
        assertEquals(descrModel, constr.getDescriptor());
        assertEquals(javax.validation.constraints.AssertFalse.class, constr.getAnnotation());
    }

    @Test
    public void shouldFailOnInvalidAssertFalseConstraint()
    {
        //given
        final AttributeConstraintModel constr = createAttributeConstraint(javax.validation.constraints.AssertFalse.class);
        final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
                ItemModel._TYPECODE, ItemModel.CREATIONTIME);
        constr.setDescriptor(descrModel);

        try
        {
            //when
            modelService.save(constr);
            fail("should throw ModelSavingException with ValidationConfigurationException as a cause");
        } catch (final ModelSavingException e)
        {
            //then
            assertTrue("should throw ModelSavingException with ValidationConfigurationException as a cause", e.getCause() instanceof ValidationConfigurationException);
        }
    }

    @Test
    public void shouldCreateValidAssertTrueConstraint()
    {
        //given
        final AttributeConstraintModel constr = createAttributeConstraint(javax.validation.constraints.AssertTrue.class);
        final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
                UserModel._TYPECODE, UserModel.LOGINDISABLED);
        constr.setDescriptor(descrModel);

        //when
        modelService.save(constr);

        //then
        assertEquals(ID, constr.getId());
        assertEquals(descrModel, constr.getDescriptor());
        assertEquals(javax.validation.constraints.AssertTrue.class, constr.getAnnotation());
    }

    @Test
    public void shouldFailOnInvalidAssertTrueConstraint()
    {
        //given
        final AttributeConstraintModel constr = createAttributeConstraint(javax.validation.constraints.AssertTrue.class);
        final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
                ItemModel._TYPECODE, ItemModel.CREATIONTIME);
        constr.setDescriptor(descrModel);

        try
        {
            //when
            modelService.save(constr);
            fail("should throw ModelSavingException with ValidationConfigurationException as a cause");
        } catch (final ModelSavingException e)
        {
            //then
            assertTrue("should throw ModelSavingException with ValidationConfigurationException as a cause", e.getCause() instanceof ValidationConfigurationException);
        }
    }

    @Test
    public void shouldFailOnInvalidDecimalMaxConstraint()
    {
        //given
        final AttributeConstraintModel constr = createAttributeConstraint(javax.validation.constraints.DecimalMax.class);
        final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
                ItemModel._TYPECODE, ItemModel.CREATIONTIME);
        constr.setDescriptor(descrModel);

        try
        {
            //when
            modelService.save(constr);
            fail("should throw ModelSavingException with ValidationConfigurationException as a cause");
        } catch (final ModelSavingException e)
        {
            //then
            assertTrue("should throw ModelSavingException with ValidationConfigurationException as a cause", e.getCause() instanceof ValidationConfigurationException);
        }
    }

    @Test
    public void shouldFailOnInvalidDecimalMinConstraint()
    {
        //given
        final AttributeConstraintModel constr = createAttributeConstraint(javax.validation.constraints.DecimalMin.class);
        final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
                ItemModel._TYPECODE, ItemModel.CREATIONTIME);
        constr.setDescriptor(descrModel);

        try
        {
            //when
            modelService.save(constr);
            fail("should throw ModelSavingException with ValidationConfigurationException as a cause");
        } catch (final ModelSavingException e)
        {
            //then
            assertTrue("should throw ModelSavingException with ValidationConfigurationException as a cause", e.getCause() instanceof ValidationConfigurationException);
        }
    }

    @Test
    public void shouldFailOnInvalidDigitsConstraint()
    {
        final AttributeConstraintModel constr = createAttributeConstraint(javax.validation.constraints.Digits.class);
        final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
                ItemModel._TYPECODE, ItemModel.CREATIONTIME);
        constr.setDescriptor(descrModel);

        try
        {
            modelService.save(constr);
            fail("should throw ModelSavingException with ValidationConfigurationException as a cause");
        } catch (final ModelSavingException e)
        {
            assertTrue("should throw ModelSavingException with ValidationConfigurationException as a cause", e.getCause() instanceof ValidationConfigurationException);
        }
    }

    @Test
    public void shouldFailOnInvalidMaxConstraint()
    {
        //given
        final AttributeConstraintModel constr = createAttributeConstraint(javax.validation.constraints.Max.class);
        final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
                ItemModel._TYPECODE, ItemModel.CREATIONTIME);
        constr.setDescriptor(descrModel);

        try
        {
            //when
            modelService.save(constr);
            fail("should throw ModelSavingException with ValidationConfigurationException as a cause");
        } catch (final ModelSavingException e)
        {
            //then
            assertTrue("should throw ModelSavingException with ValidationConfigurationException as a cause", e.getCause() instanceof ValidationConfigurationException);
        }
    }

    @Test
    public void shouldFailOnInvalidMinConstraint()
    {
        //given
        final AttributeConstraintModel constr = createAttributeConstraint(javax.validation.constraints.Min.class);
        final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
                ItemModel._TYPECODE, ItemModel.CREATIONTIME);
        constr.setDescriptor(descrModel);

        try
        {
            //when
            modelService.save(constr);
            fail("should throw ModelSavingException with ValidationConfigurationException as a cause");
        } catch (final ModelSavingException e)
        {
            //then
            assertTrue("should throw ModelSavingException with ValidationConfigurationException as a cause", e.getCause() instanceof ValidationConfigurationException);
        }
    }

    @Test
    public void shouldCreateValidFutureConstraint()
    {
        //given
        final AttributeConstraintModel constr = createAttributeConstraint(javax.validation.constraints.Future.class);
        final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
                ItemModel._TYPECODE, ItemModel.CREATIONTIME);
        constr.setDescriptor(descrModel);

        //when
        modelService.save(constr);

        //then
        assertEquals(ID, constr.getId());
        assertEquals(descrModel, constr.getDescriptor());
        assertEquals(javax.validation.constraints.Future.class, constr.getAnnotation());
    }

    @Test
    public void shouldFailOnInvalidFutureConstraint()
    {
        //given
        final AttributeConstraintModel constr = createAttributeConstraint(javax.validation.constraints.Future.class);
        final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
                TestItemType2Model._TYPECODE, TestItemType2Model.TESTPROPERTY2);
        constr.setDescriptor(descrModel);

        try
        {
            //when
            modelService.save(constr);
            fail("should throw ModelSavingException with ValidationConfigurationException as a cause");
        } catch (final ModelSavingException e)
        {
            //then
            assertTrue("should throw ModelSavingException with ValidationConfigurationException as a cause", e.getCause() instanceof ValidationConfigurationException);
        }
    }

    @Test
    public void shouldCreateValidPastConstraint()
    {
        //given
        final AttributeConstraintModel constr = createAttributeConstraint(javax.validation.constraints.Past.class);
        final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
                ItemModel._TYPECODE, ItemModel.CREATIONTIME);
        constr.setDescriptor(descrModel);

        //when
        modelService.save(constr);

        //then
        assertEquals(ID, constr.getId());
        assertEquals(descrModel, constr.getDescriptor());
        assertEquals(javax.validation.constraints.Past.class, constr.getAnnotation());
    }

    @Test
    public void shouldFailOnInvalidPastConstraint()
    {
        //given
        final AttributeConstraintModel constr = createAttributeConstraint(javax.validation.constraints.Past.class);
        final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
                TestItemType2Model._TYPECODE, TestItemType2Model.TESTPROPERTY2);
        constr.setDescriptor(descrModel);

        try
        {
            //when
            modelService.save(constr);
            fail("should throw ModelSavingException with ValidationConfigurationException as a cause");
        } catch (final ModelSavingException e)
        {
            //then
            assertTrue("should throw ModelSavingException with ValidationConfigurationException as a cause", e.getCause() instanceof ValidationConfigurationException);
        }
    }

    @Test
    public void shouldFailOnInvalidPatternConstraint()
    {
        //given
        final AttributeConstraintModel constr = createAttributeConstraint(javax.validation.constraints.Pattern.class);
        final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
                ItemModel._TYPECODE, ItemModel.CREATIONTIME);
        constr.setDescriptor(descrModel);

        try
        {
            //when
            modelService.save(constr);
            fail("should throw ModelSavingException with ValidationConfigurationException as a cause");
        } catch (final ModelSavingException e)
        {
            //then
            assertTrue("should throw ModelSavingException with ValidationConfigurationException as a cause", e.getCause() instanceof ValidationConfigurationException);
        }
    }

    @Test
    public void shouldCreateValidSizeConstraint()
    {
        //given
        final AttributeConstraintModel constr = createAttributeConstraint(javax.validation.constraints.Size.class);
        final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
                TestItemType2Model._TYPECODE, TestItemType2Model.TESTPROPERTY2);
        constr.setDescriptor(descrModel);

        //when
        modelService.save(constr);

        //then
        assertEquals(ID, constr.getId());
        assertEquals(descrModel, constr.getDescriptor());
        assertEquals(javax.validation.constraints.Size.class, constr.getAnnotation());
    }

    @Test
    public void shouldFailOnInvalidSizeConstraint()
    {
        final AttributeConstraintModel constr = createAttributeConstraint(javax.validation.constraints.Size.class);
        final AttributeDescriptorModel descrModel = typeService.getAttributeDescriptor(
                ItemModel._TYPECODE, ItemModel.CREATIONTIME);
        constr.setDescriptor(descrModel);

        try
        {
            modelService.save(constr);
            fail("should throw ModelSavingException with ValidationConfigurationException as a cause");
        } catch (final ModelSavingException e)
        {
            assertTrue("should throw ModelSavingException with ValidationConfigurationException as a cause", e.getCause() instanceof ValidationConfigurationException);
        }
    }

    private AttributeConstraintModel createAttributeConstraint(final Class annotationClass)
    {
        final AttributeConstraintModel constr = modelService.create(AttributeConstraintModel._TYPECODE);
        constr.setId(ID);
        constr.setAnnotation(annotationClass);
        return constr;
    }
}
