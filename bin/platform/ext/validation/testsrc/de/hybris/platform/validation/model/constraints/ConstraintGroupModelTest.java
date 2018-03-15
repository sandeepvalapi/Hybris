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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.UniqueAttributesInterceptor;
import de.hybris.platform.validation.constants.ValidationConstants;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.interceptors.AbstractConstraintValidator;
import de.hybris.platform.validation.interceptors.ConstraintGroupInterfaceNameValidator;
import de.hybris.platform.validation.interceptors.ConstraintGroupPrepareInterceptor;
import de.hybris.platform.validation.interceptors.ConstraintGroupRemoveInterceptor;
import de.hybris.platform.validation.model.constraints.jsr303.AbstractConstraintTest;
import de.hybris.platform.validation.model.constraints.jsr303.AssertFalseConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.AssertTrueConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.DecimalMaxConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.MinConstraintModel;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.groups.Default;

import org.junit.Test;


@IntegrationTest
public class ConstraintGroupModelTest extends AbstractConstraintTest
{
	@Test
	public void testDefaultGroup()
	{
		final ConstraintGroupModel def = validationService.getDefaultConstraintGroup();
		assertNotNull(def);
		assertEquals(ValidationConstants.DEFAULT_CONSTRAINTGROUP_ID, def.getId());
		assertEquals(ValidationConstants.DEFAULT_CONSTRAINTGROUP_INTERFACENAME, def.getInterfaceName());
	}

	@Test
	public void testCreateRemoveCreateGroup()
	{
		final ConstraintGroupModel group = modelService.create(ConstraintGroupModel.class);
		group.setId("xxx");
		modelService.save(group);
		modelService.remove(group);

		final ConstraintGroupModel group1 = modelService.create(ConstraintGroupModel.class);
		group1.setId("xxx");
		modelService.save(group1);
		modelService.remove(group1);
	}

	@Test
	public void testCreateRemoveCreateGroup2()
	{
		final ConstraintGroupModel group = modelService.create(ConstraintGroupModel.class);
		group.setId("yyy");
		group.setInterfaceName("de.hybris.platform.validation.model.constraints.ConstraintGroupModelTest.MarkerInterface");
		modelService.save(group);
		modelService.remove(group);

		final ConstraintGroupModel group1 = modelService.create(ConstraintGroupModel.class);
		group1.setId("yyy");
		group1.setInterfaceName("de.hybris.platform.validation.model.constraints.ConstraintGroupModelTest.MarkerInterface");
		modelService.save(group1);
		modelService.remove(group1);
	}

	@Test
	public void testRemoveDefaultConstraintGroup()
	{
		try
		{
			modelService.remove(validationService.getDefaultConstraintGroup());
			fail("It sould not be possible to remove the default constraint group!");
		}
		catch (final Exception e)
		{
			checkException(e, ModelRemovalException.class, InterceptorException.class, ConstraintGroupRemoveInterceptor.class);
		}
	}

	@Test
	public void testChangeDefaultConstraintGroup1()
	{
		final ConstraintGroupModel def = validationService.getDefaultConstraintGroup();
		def.setId("myNewDefault");
		checkInterceptorExceptionOnSave("The default constraint group can not be modified!",
				ConstraintGroupPrepareInterceptor.class, def);
		def.setInterfaceName("another.new.interface.Path");
		checkInterceptorExceptionOnSave("The default constraint group can not be modified!",
				ConstraintGroupPrepareInterceptor.class, def);
	}

	@Test
	public void testChangeDefaultConstraintGroup2()
	{
		final ConstraintGroupModel def = validationService.getDefaultConstraintGroup();
		def.setInterfaceName(null);
		checkInterceptorExceptionOnSave("The default constraint group can not be modified!",
				ConstraintGroupPrepareInterceptor.class, def);
	}

	@Test
	public void testCreateAnotherDefaultGroup()
	{
		final ConstraintGroupModel newDef = modelService.create(ConstraintGroupModel.class);
		newDef.setId("DEFAULT");
		checkInterceptorExceptionOnSave("ambiguous unique keys {interfaceName=javax.validation.groups.Default} for model"
				+ " ConstraintGroupModel (<unsaved>) - found 1 item(s) using the same keys", UniqueAttributesInterceptor.class,
				newDef);
	}

	@Test
	public void testSwapDefaultConstraintGroup()
	{
		final ConstraintGroupModel def = validationService.getDefaultConstraintGroup();
		final ConstraintGroupModel newDef = modelService.create(ConstraintGroupModel.class);
		def.setId("oldDefault");
		def.setInterfaceName("need.new.InterfacePath");

		newDef.setId("Default");
		checkInterceptorExceptionOnSave("The default constraint group can not be modified!",
				ConstraintGroupPrepareInterceptor.class, def, newDef);
	}

	@Test
	public void testConstraintsAndDefaultConstraintGroup()
	{
		final ConstraintGroupModel def = validationService.getDefaultConstraintGroup();
		assertEquals(0, def.getConstraints().size());

		final AbstractConstraintModel assertTrueConstraint = createAssertTrue();
		final AbstractConstraintModel assertFalseConstraint = createAssertFalse();
		final AbstractConstraintModel minConstraint = createConstraintMin();
		final AbstractConstraintModel decimalMaxConstraint = createConstraintDecimalMax();
		modelService.saveAll();

		//need to reload the group!
		modelService.refresh(def);
		assertEquals(4, def.getConstraints().size()); //the default group contains all unbound constraints
		//but the constraints itself does not show it 
		assertEquals(0, assertTrueConstraint.getConstraintGroups().size());
		assertEquals(0, assertFalseConstraint.getConstraintGroups().size());
		assertEquals(0, minConstraint.getConstraintGroups().size());
		assertEquals(0, decimalMaxConstraint.getConstraintGroups().size());

		final ConstraintGroupModel group1 = createConstraintGroup(true, true, "CurrencyModelCheck", null, decimalMaxConstraint,
				minConstraint);
		assertEquals(2, group1.getConstraints().size());

		modelService.refresh(def);
		assertEquals(2, def.getConstraints().size());

		final ConstraintGroupModel group2 = createConstraintGroup(true, true, "SingleCurrencyModelCheck", null,
				decimalMaxConstraint);
		assertEquals(1, group2.getConstraints().size());

		modelService.refresh(def);
		modelService.refresh(group1);
		assertEquals(2, def.getConstraints().size());
		assertEquals(2, group1.getConstraints().size());

		modelService.refresh(decimalMaxConstraint);
		assertEquals(2, decimalMaxConstraint.getConstraintGroups().size());
	}

	@Test
	public void testSetDefaultConstraintGroupToAConstraint()
	{
		assertEquals(0, validationService.getDefaultConstraintGroup().getConstraints().size());

		final AbstractConstraintModel assertTrueConstraint = createAssertTrue();
		modelService.saveAll();
		assertEquals(1, validationService.getDefaultConstraintGroup().getConstraints().size());
		assertEquals(0, assertTrueConstraint.getConstraintGroups().size());

		assertTrueConstraint.setConstraintGroups(//
				new HashSet<ConstraintGroupModel>(Arrays.asList(validationService.getDefaultConstraintGroup())));

		try
		{
			modelService.saveAll();
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, InterceptorException.class, AbstractConstraintValidator.class);
			assertTrue(e.getCause().getMessage().contains("The default constraint group can not be set to any constraint!"));
		}
	}

	@Test
	public void testSetAConstraintToTheDefaultConstraintGroup()
	{
		final ConstraintGroupModel def = validationService.getDefaultConstraintGroup();
		final AbstractConstraintModel assertTrueConstraint = createAssertTrue();
		modelService.saveAll();

		def.setConstraints(Collections.singleton(assertTrueConstraint));
		try
		{
			modelService.saveAll();
		}
		catch (final ModelSavingException e)
		{
			assertTrue(e.getCause() instanceof InterceptorException);
			assertEquals("The default constraint group can not be set to any constraint1!", e.getCause().getMessage());
		}
		catch (final Exception e)
		{
			fail("No other exception here is expected!");
		}
	}

	@Test
	public void testInvalidInterfaceNames()
	{
		final ConstraintGroupModel group = createConstraintGroup(false, false, "test", ".", new AbstractConstraintModel[] {});
		checkInterceptorExceptionOnSave("'.' is not a valid interface name!", ConstraintGroupInterfaceNameValidator.class, group);

		group.setInterfaceName("de.11.22");
		checkInterceptorExceptionOnSave("'de.11.22' is not a valid interface name!", ConstraintGroupInterfaceNameValidator.class,
				group);

		group.setInterfaceName("de.a11.A22");
		modelService.save(group);

		group.setInterfaceName("java.lang.Integer");
		checkInterceptorExceptionOnSave("The given interface name 'java.lang.Integer' exists but it is not an interface!",
				ConstraintGroupInterfaceNameValidator.class, group);

		group.setInterfaceName("java.lang.Integer1");
		try
		{
			modelService.save(group);
		}
		catch (final ModelSavingException e)
		{
			assertTrue(e.getCause() instanceof IllegalArgumentException);
			assertTrue(e.getCause().getMessage().startsWith("Cannot compile given group interface"));
			assertTrue(e.getCause().getMessage().contains("java.lang"));
		}

		group.setInterfaceName(null);
		modelService.save(group);
		assertEquals("de.hybris.platform.validation.groupinterfaces.test", group.getInterfaceName());

	}


	@Test
	public void testGroupCheckOk1()
	{
		final AbstractConstraintModel assertTrueConstraint = createAssertTrue();
		final AbstractConstraintModel assertFalseConstraint = createAssertFalse();
		final AbstractConstraintModel minConstraint = createConstraintMin();
		final AbstractConstraintModel decimalMaxConstraint = createConstraintDecimalMax();
		modelService.saveAll();

		final ConstraintGroupModel group1 = createConstraintGroup(true, false, "CurrencyModelCheck", "", decimalMaxConstraint,
				minConstraint);
		final ConstraintGroupModel group2 = createConstraintGroup(true, false, "SingleCurrencyModelCheck", "", decimalMaxConstraint);
		final ConstraintGroupModel group3 = createConstraintGroup(true, false, "C2LItemModelCheck", "", assertTrueConstraint,
				minConstraint);
		validationService.reloadValidationEngine();
		assertEquals(1, validationService.getDefaultConstraintGroup().getConstraints().size());
		assertEquals(assertFalseConstraint, validationService.getDefaultConstraintGroup().getConstraints().iterator().next());

		final CurrencyModel curr1 = createCurrencyModelOK();

		assertTrue(validationService.validate(curr1).isEmpty());
		assertTrue(validationService.validate(curr1, Collections.singletonList(group1)).isEmpty());
		assertTrue(validationService.validate(curr1, Collections.singletonList(group2)).isEmpty());
		assertTrue(validationService.validate(curr1, constraintDao.getTargetClass(group3)).isEmpty());
		assertTrue(validationService.validate(curr1, Arrays.asList(group2, group3)).isEmpty());
		assertTrue(validationService.validate(curr1, Arrays.asList(group1, group2, group3)).isEmpty());
	}

	@Test
	public void testGroupCheckFails1()
	{
		final AbstractConstraintModel assertTrueConstraint = createAssertTrue();
		final AbstractConstraintModel assertFalseConstraint = createAssertFalse();
		final AbstractConstraintModel minConstraint = createConstraintMin();
		final AbstractConstraintModel decimalMaxConstraint = createConstraintDecimalMax();

		final ConstraintGroupModel group1 = // 
		createConstraintGroup(false, false, "CurrencyModelCheck", "", decimalMaxConstraint, minConstraint);

		final ConstraintGroupModel group2 = // 
		createConstraintGroup(false, false, "SingleCurrencyModelCheck", "", decimalMaxConstraint);

		final ConstraintGroupModel group3 = //
		createConstraintGroup(false, false, "C2LItemModelCheck", "", assertTrueConstraint, minConstraint);
		assertFalseConstraint.getId();
		modelService.saveAll();

		validationService.reloadValidationEngine();

		final CurrencyModel curr1 = createCurrencyModelFail();
		final Set<HybrisConstraintViolation> result = validationService.validate(curr1);
		assertEquals(1, result.size());
		assertEquals(2, validationService.validate(curr1, Collections.singletonList(group1)).size());
		assertEquals(1, validationService.validate(curr1, Collections.singletonList(group2)).size());
		assertEquals(2, validationService.validate(curr1, constraintDao.getTargetClass(group3)).size());
		assertEquals(3, validationService.validate(curr1, Arrays.asList(group2, group3)).size());
		assertEquals(3, validationService.validate(curr1, Arrays.asList(group1, group2, group3)).size());

		//all, with the default
		assertEquals(4, //
				validationService.validate(curr1, //
						Default.class, // 
						constraintDao.getTargetClass(group1),//
						constraintDao.getTargetClass(group2), //
						constraintDao.getTargetClass(group3)).size());
		assertEquals(
				4, //
				validationService.validate(curr1,
						Arrays.asList(validationService.getDefaultConstraintGroup(), group1, group2, group3)).size());
		assertEquals(4, //
				validationService.validate(curr1, //
						constraintDao.getTargetClass(validationService.getDefaultConstraintGroup()), // 
						constraintDao.getTargetClass(group1),//
						constraintDao.getTargetClass(group2), //
						constraintDao.getTargetClass(group3)).size());

		assertEquals(1, validationService.validate(curr1, Collections.EMPTY_SET).size());

		//testGroupsInSessionOk1
		final CurrencyModel curr2 = createCurrencyModelFail();
		curr2.setBase(Boolean.FALSE); //the default group is active
		modelService.save(curr2); //no exception here!
	}

	@Test
	public void testGroupsInSessionFails1()
	{
		final AbstractConstraintModel assertTrueConstraint = createAssertTrue();
		createAssertFalse();
		final AbstractConstraintModel minConstraint = createConstraintMin();
		final AbstractConstraintModel decimalMaxConstraint = createConstraintDecimalMax();

		createConstraintGroup(false, false, "CurrencyModelCheck", "", decimalMaxConstraint, minConstraint);

		final ConstraintGroupModel group2 = // 
		createConstraintGroup(false, false, "SingleCurrencyModelCheck", "", decimalMaxConstraint);

		createConstraintGroup(false, false, "C2LItemModelCheck", "", assertTrueConstraint, minConstraint);
		modelService.saveAll();

		validationService.reloadValidationEngine();

		final CurrencyModel curr1 = createCurrencyModelFail();
		curr1.setBase(Boolean.FALSE); //correct this value because the default group kicks in and this test fails but with the wrong attribute

		final Map<String, String> expectedViolations = new HashMap<String, String>(1);
		expectedViolations.put("conversion", Constraint.DECIMAL_MAX.msgKey);

		validationService.setActiveConstraintGroups(Collections.singletonList(group2));

		try
		{
			modelService.save(curr1);
			fail();
		}
		catch (final Exception e)
		{
			assertModelSavingExceptionWithMessageKey(e, expectedViolations);
		}
	}

	@Test
	public void testGroupsInSessionFails2()
	{
		final AbstractConstraintModel assertTrueConstraint = createAssertTrue();
		final AbstractConstraintModel assertFalseConstraint = createAssertFalse();
		final AbstractConstraintModel minConstraint = createConstraintMin();
		final AbstractConstraintModel decimalMaxConstraint = createConstraintDecimalMax();

		final ConstraintGroupModel group1 = // 
		createConstraintGroup(false, false, "CurrencyModelCheck", "", decimalMaxConstraint, minConstraint);

		final ConstraintGroupModel group2 = // 
		createConstraintGroup(false, false, "SingleCurrencyModelCheck", "", decimalMaxConstraint);

		final ConstraintGroupModel group3 = //
		createConstraintGroup(false, false, "C2LItemModelCheck", "", assertTrueConstraint, minConstraint);
		assertFalseConstraint.getId();
		modelService.saveAll();

		validationService.reloadValidationEngine();

		final CurrencyModel curr1 = createCurrencyModelFail();
		curr1.setBase(Boolean.FALSE); //correct this value because the default group kicks in and this test fails but with the wrong attribute

		final Map<String, String> expectedViolations = new HashMap<String, String>(3);
		expectedViolations.put("active", Constraint.ASSERT_TRUE.msgKey);
		expectedViolations.put("conversion", Constraint.DECIMAL_MAX.msgKey);
		expectedViolations.put("digits", Constraint.MIN.msgKey);

		validationService.setActiveConstraintGroups(Arrays.asList(group1, group2, group3));
		try
		{
			modelService.save(curr1);
			fail();
		}
		catch (final Exception e)
		{
			assertModelSavingExceptionWithMessageKey(e, expectedViolations);
		}
	}

	@Test
	public void cannotCreateConstraintGroupWithSameIDandWithoutInterface()
	{
		final ConstraintGroupModel group1 = modelService.create(ConstraintGroupModel.class);
		group1.setId("Group");

		final ConstraintGroupModel group2 = modelService.create(ConstraintGroupModel.class);
		group2.setId("Group");
		try
		{
			modelService.saveAll(group1, group2);
			fail();
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, InterceptorException.class, UniqueAttributesInterceptor.class);
			assertTrue(e.getCause().getMessage()
					.contains("{interfaceName=de.hybris.platform.validation.groupinterfaces." + "Group}"));
		}
	}

	@Test
	public void canCreateConstraintGroupWithSameIDbutDifferentInterfaces()
	{
		final ConstraintGroupModel group1 = modelService.create(ConstraintGroupModel.class);
		group1.setId("Group");
		group1.setInterfaceName("group1");

		final ConstraintGroupModel group2 = modelService.create(ConstraintGroupModel.class);
		group2.setId("Group");
		group2.setInterfaceName("group2");

		modelService.saveAll(group1, group2);
	}

	private void checkInterceptorExceptionOnSave(final String expectedErrorMessage, final Class interceptor,
			final Object... modelToBeSave)
	{
		try
		{
			modelService.saveAll(modelToBeSave);
			fail("expected an interceptor exception here");
		}
		catch (final Exception e)
		{
			checkException(e, ModelSavingException.class, InterceptorException.class, interceptor);
			assertTrue(e.getCause().getMessage().contains(expectedErrorMessage));
		}
	}

	//CurrencyModelCheck: for decimalMaxConstraint and minConstraint
	//SingleCurrencyModelCheck: for decimalMaxConstraint
	//C2LItemModelCheck: assertTrueConstraint and minConstraint
	private ConstraintGroupModel createConstraintGroup(final boolean save, final boolean reload, final String id,
			final String interfacename, final AbstractConstraintModel... constraints)
	{
		final ConstraintGroupModel group = modelService.create(ConstraintGroupModel.class);
		group.setId(id);
		group.setInterfaceName(interfacename);
		group.setConstraints(new HashSet(Arrays.asList(constraints)));
		if (save)
		{
			modelService.save(group);
		}
		if (reload)
		{
			validationService.reloadValidationEngine();
		}
		return group;
	}

	private AbstractConstraintModel createAssertTrue()
	{
		final AssertTrueConstraintModel assertTrueConst = modelService.create(AssertTrueConstraintModel.class);
		assertTrueConst.setId("assertTrueConst");
		assertTrueConst.setDescriptor(getAttrDesc(C2LItemModel.class, C2LItemModel.ACTIVE));
		return assertTrueConst;
	}

	private AbstractConstraintModel createAssertFalse()
	{
		final AssertFalseConstraintModel notnull = modelService.create(AssertFalseConstraintModel.class);
		notnull.setId("assertFalse");
		notnull.setDescriptor(getAttrDesc(CurrencyModel.class, CurrencyModel.BASE));
		return notnull;
	}

	private AbstractConstraintModel createConstraintMin()
	{
		final MinConstraintModel minConstraint = modelService.create(MinConstraintModel.class);
		minConstraint.setId("minConstraint");
		minConstraint.setDescriptor(getAttrDesc(CurrencyModel.class, CurrencyModel.DIGITS));
		minConstraint.setValue(Long.valueOf(3));
		return minConstraint;
	}

	private AbstractConstraintModel createConstraintDecimalMax()
	{
		final DecimalMaxConstraintModel decimalMax = modelService.create(DecimalMaxConstraintModel.class);
		decimalMax.setId("decimalMax");
		decimalMax.setDescriptor(getAttrDesc(CurrencyModel.class, CurrencyModel.CONVERSION));
		decimalMax.setValue(BigDecimal.valueOf(1));
		return decimalMax;
	}

	private AttributeDescriptorModel getAttrDesc(final Class clazz, final String qualifier)
	{
		return typeService.getAttributeDescriptor(typeService.getComposedType(clazz), qualifier);
	}

	private CurrencyModel createCurrencyModelOK()
	{
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setActive(Boolean.TRUE);
		curr.setBase(Boolean.FALSE);
		curr.setConversion(Double.valueOf(1));
		curr.setDigits(Integer.valueOf(4));
		curr.setIsocode("Ok");
		curr.setSymbol("OK");
		return curr;
	}

	private CurrencyModel createCurrencyModelFail()
	{
		final CurrencyModel curr = modelService.create(CurrencyModel.class);
		curr.setActive(Boolean.FALSE);
		curr.setBase(Boolean.TRUE);
		curr.setConversion(Double.valueOf(3));
		curr.setDigits(Integer.valueOf(0));
		curr.setIsocode("Fail");
		curr.setSymbol("F");
		return curr;
	}

	@SuppressWarnings("unused")
	private interface MarkerInterface
	{
		//testing existing interface
	}
}
