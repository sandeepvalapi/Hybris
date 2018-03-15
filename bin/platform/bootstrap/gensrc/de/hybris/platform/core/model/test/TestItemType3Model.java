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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.test.TestItemType2Model;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type TestItemType3 first defined at extension core.
 */
@SuppressWarnings("all")
public class TestItemType3Model extends TestItemType2Model
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "TestItemType3";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItemType3.xxx</code> attribute defined at extension <code>core</code>. */
	public static final String XXX = "xxx";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItemType3.prop</code> attribute defined at extension <code>core</code>. */
	public static final String PROP = "prop";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItemType3.prop2</code> attribute defined at extension <code>core</code>. */
	public static final String PROP2 = "prop2";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItemType3.prop3</code> attribute defined at extension <code>core</code>. */
	public static final String PROP3 = "prop3";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItemType3.itemTypeTwo</code> attribute defined at extension <code>core</code>. */
	public static final String ITEMTYPETWO = "itemTypeTwo";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItemType3.itemsTypeTwo</code> attribute defined at extension <code>core</code>. */
	public static final String ITEMSTYPETWO = "itemsTypeTwo";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public TestItemType3Model()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public TestItemType3Model(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _itemTypeTwo initial attribute declared by type <code>TestItemType3</code> at extension <code>core</code>
	 * @param _itemsTypeTwo initial attribute declared by type <code>TestItemType3</code> at extension <code>core</code>
	 */
	@Deprecated
	public TestItemType3Model(final TestItemType2Model _itemTypeTwo, final Collection<TestItemType2Model> _itemsTypeTwo)
	{
		super();
		setItemTypeTwo(_itemTypeTwo);
		setItemsTypeTwo(_itemsTypeTwo);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _itemTypeTwo initial attribute declared by type <code>TestItemType3</code> at extension <code>core</code>
	 * @param _itemsTypeTwo initial attribute declared by type <code>TestItemType3</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public TestItemType3Model(final TestItemType2Model _itemTypeTwo, final Collection<TestItemType2Model> _itemsTypeTwo, final ItemModel _owner)
	{
		super();
		setItemTypeTwo(_itemTypeTwo);
		setItemsTypeTwo(_itemsTypeTwo);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItemType3.itemsTypeTwo</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the itemsTypeTwo
	 */
	@Accessor(qualifier = "itemsTypeTwo", type = Accessor.Type.GETTER)
	public Collection<TestItemType2Model> getItemsTypeTwo()
	{
		return getPersistenceContext().getPropertyValue(ITEMSTYPETWO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItemType3.itemTypeTwo</code> attribute defined at extension <code>core</code>. 
	 * @return the itemTypeTwo
	 */
	@Accessor(qualifier = "itemTypeTwo", type = Accessor.Type.GETTER)
	public TestItemType2Model getItemTypeTwo()
	{
		return getPersistenceContext().getPropertyValue(ITEMTYPETWO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItemType3.prop</code> attribute defined at extension <code>core</code>. 
	 * @return the prop
	 */
	@Accessor(qualifier = "prop", type = Accessor.Type.GETTER)
	public String getProp()
	{
		return getPersistenceContext().getPropertyValue(PROP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItemType3.prop2</code> attribute defined at extension <code>core</code>. 
	 * @return the prop2
	 */
	@Accessor(qualifier = "prop2", type = Accessor.Type.GETTER)
	public String getProp2()
	{
		return getProp2(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItemType3.prop2</code> attribute defined at extension <code>core</code>. 
	 * @param loc the value localization key 
	 * @return the prop2
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "prop2", type = Accessor.Type.GETTER)
	public String getProp2(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(PROP2, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItemType3.prop3</code> attribute defined at extension <code>core</code>. 
	 * @return the prop3
	 */
	@Accessor(qualifier = "prop3", type = Accessor.Type.GETTER)
	public String getProp3()
	{
		return getPersistenceContext().getPropertyValue(PROP3);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItemType3.xxx</code> attribute defined at extension <code>core</code>. 
	 * @return the xxx
	 */
	@Accessor(qualifier = "xxx", type = Accessor.Type.GETTER)
	public String getXxx()
	{
		return getPersistenceContext().getPropertyValue(XXX);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItemType3.itemsTypeTwo</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the itemsTypeTwo
	 */
	@Accessor(qualifier = "itemsTypeTwo", type = Accessor.Type.SETTER)
	public void setItemsTypeTwo(final Collection<TestItemType2Model> value)
	{
		getPersistenceContext().setPropertyValue(ITEMSTYPETWO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItemType3.itemTypeTwo</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the itemTypeTwo
	 */
	@Accessor(qualifier = "itemTypeTwo", type = Accessor.Type.SETTER)
	public void setItemTypeTwo(final TestItemType2Model value)
	{
		getPersistenceContext().setPropertyValue(ITEMTYPETWO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItemType3.prop</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the prop
	 */
	@Accessor(qualifier = "prop", type = Accessor.Type.SETTER)
	public void setProp(final String value)
	{
		getPersistenceContext().setPropertyValue(PROP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItemType3.prop2</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the prop2
	 */
	@Accessor(qualifier = "prop2", type = Accessor.Type.SETTER)
	public void setProp2(final String value)
	{
		setProp2(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>TestItemType3.prop2</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the prop2
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "prop2", type = Accessor.Type.SETTER)
	public void setProp2(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(PROP2, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItemType3.prop3</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the prop3
	 */
	@Accessor(qualifier = "prop3", type = Accessor.Type.SETTER)
	public void setProp3(final String value)
	{
		getPersistenceContext().setPropertyValue(PROP3, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItemType3.xxx</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the xxx
	 */
	@Accessor(qualifier = "xxx", type = Accessor.Type.SETTER)
	public void setXxx(final String value)
	{
		getPersistenceContext().setPropertyValue(XXX, value);
	}
	
}
