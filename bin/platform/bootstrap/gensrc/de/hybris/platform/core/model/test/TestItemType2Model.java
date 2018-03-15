/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Mar 15, 2018 5:02:29 PM                     ---
 * ----------------------------------------------------------------
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *  
 */
package de.hybris.platform.core.model.test;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.test.TestItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type TestItemType2 first defined at extension core.
 */
@SuppressWarnings("all")
public class TestItemType2Model extends TestItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "TestItemType2";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItemType2.testProperty1</code> attribute defined at extension <code>core</code>. */
	public static final String TESTPROPERTY1 = "testProperty1";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItemType2.testProperty2</code> attribute defined at extension <code>core</code>. */
	public static final String TESTPROPERTY2 = "testProperty2";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItemType2.foo</code> attribute defined at extension <code>core</code>. */
	public static final String FOO = "foo";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItemType2.bar</code> attribute defined at extension <code>core</code>. */
	public static final String BAR = "bar";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItemType2.fooBar</code> attribute defined at extension <code>core</code>. */
	public static final String FOOBAR = "fooBar";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItemType2.intBar</code> attribute defined at extension <code>core</code>. */
	public static final String INTBAR = "intBar";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItemType2.gender</code> attribute defined at extension <code>core</code>. */
	public static final String GENDER = "gender";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItemType2.localizedFooBar</code> attribute defined at extension <code>core</code>. */
	public static final String LOCALIZEDFOOBAR = "localizedFooBar";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public TestItemType2Model()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public TestItemType2Model(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public TestItemType2Model(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItemType2.bar</code> attribute defined at extension <code>core</code>. 
	 * @return the bar
	 */
	@Accessor(qualifier = "bar", type = Accessor.Type.GETTER)
	public String getBar()
	{
		return getPersistenceContext().getPropertyValue(BAR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItemType2.foo</code> attribute defined at extension <code>core</code>. 
	 * @return the foo
	 */
	@Accessor(qualifier = "foo", type = Accessor.Type.GETTER)
	public String getFoo()
	{
		return getPersistenceContext().getPropertyValue(FOO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItemType2.fooBar</code> dynamic attribute defined at extension <code>core</code>. 
	 * @return the fooBar
	 */
	@Accessor(qualifier = "fooBar", type = Accessor.Type.GETTER)
	public String getFooBar()
	{
		return getPersistenceContext().getDynamicValue(this,FOOBAR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItemType2.gender</code> dynamic attribute defined at extension <code>core</code>. 
	 * @return the gender
	 */
	@Accessor(qualifier = "gender", type = Accessor.Type.GETTER)
	public Gender getGender()
	{
		return getPersistenceContext().getDynamicValue(this,GENDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItemType2.intBar</code> dynamic attribute defined at extension <code>core</code>. 
	 * @return the intBar
	 */
	@Accessor(qualifier = "intBar", type = Accessor.Type.GETTER)
	public int getIntBar()
	{
		return toPrimitive( (Integer) getPersistenceContext().getDynamicValue(this,INTBAR));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItemType2.localizedFooBar</code> dynamic attribute defined at extension <code>core</code>. 
	 * @return the localizedFooBar
	 */
	@Accessor(qualifier = "localizedFooBar", type = Accessor.Type.GETTER)
	public String getLocalizedFooBar()
	{
		return getLocalizedFooBar(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItemType2.localizedFooBar</code> dynamic attribute defined at extension <code>core</code>. 
	 * @param loc the value localization key 
	 * @return the localizedFooBar
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "localizedFooBar", type = Accessor.Type.GETTER)
	public String getLocalizedFooBar(final Locale loc)
	{
		return getPersistenceContext().getLocalizedDynamicValue(this,LOCALIZEDFOOBAR, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItemType2.testProperty1</code> attribute defined at extension <code>core</code>. 
	 * @return the testProperty1
	 */
	@Accessor(qualifier = "testProperty1", type = Accessor.Type.GETTER)
	public Integer getTestProperty1()
	{
		return getPersistenceContext().getPropertyValue(TESTPROPERTY1);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItemType2.testProperty2</code> attribute defined at extension <code>core</code>. 
	 * @return the testProperty2
	 */
	@Accessor(qualifier = "testProperty2", type = Accessor.Type.GETTER)
	public String getTestProperty2()
	{
		return getTestProperty2(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItemType2.testProperty2</code> attribute defined at extension <code>core</code>. 
	 * @param loc the value localization key 
	 * @return the testProperty2
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "testProperty2", type = Accessor.Type.GETTER)
	public String getTestProperty2(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(TESTPROPERTY2, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItemType2.bar</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the bar
	 */
	@Accessor(qualifier = "bar", type = Accessor.Type.SETTER)
	public void setBar(final String value)
	{
		getPersistenceContext().setPropertyValue(BAR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItemType2.foo</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the foo
	 */
	@Accessor(qualifier = "foo", type = Accessor.Type.SETTER)
	public void setFoo(final String value)
	{
		getPersistenceContext().setPropertyValue(FOO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItemType2.fooBar</code> dynamic attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the fooBar
	 */
	@Accessor(qualifier = "fooBar", type = Accessor.Type.SETTER)
	public void setFooBar(final String value)
	{
		getPersistenceContext().setDynamicValue(this,FOOBAR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItemType2.intBar</code> dynamic attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the intBar
	 */
	@Accessor(qualifier = "intBar", type = Accessor.Type.SETTER)
	public void setIntBar(final int value)
	{
		getPersistenceContext().setDynamicValue(this,INTBAR, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItemType2.localizedFooBar</code> dynamic attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the localizedFooBar
	 */
	@Accessor(qualifier = "localizedFooBar", type = Accessor.Type.SETTER)
	public void setLocalizedFooBar(final String value)
	{
		setLocalizedFooBar(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>TestItemType2.localizedFooBar</code> dynamic attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the localizedFooBar
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "localizedFooBar", type = Accessor.Type.SETTER)
	public void setLocalizedFooBar(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedDynamicValue(this,LOCALIZEDFOOBAR, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItemType2.testProperty1</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the testProperty1
	 */
	@Accessor(qualifier = "testProperty1", type = Accessor.Type.SETTER)
	public void setTestProperty1(final Integer value)
	{
		getPersistenceContext().setPropertyValue(TESTPROPERTY1, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItemType2.testProperty2</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the testProperty2
	 */
	@Accessor(qualifier = "testProperty2", type = Accessor.Type.SETTER)
	public void setTestProperty2(final String value)
	{
		setTestProperty2(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>TestItemType2.testProperty2</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the testProperty2
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "testProperty2", type = Accessor.Type.SETTER)
	public void setTestProperty2(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(TESTPROPERTY2, loc, value);
	}
	
}
