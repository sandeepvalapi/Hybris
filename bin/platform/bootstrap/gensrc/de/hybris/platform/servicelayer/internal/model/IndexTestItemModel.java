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
package de.hybris.platform.servicelayer.internal.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type IndexTestItem first defined at extension core.
 */
@SuppressWarnings("all")
public class IndexTestItemModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "IndexTestItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>IndexTestItem.column1</code> attribute defined at extension <code>core</code>. */
	public static final String COLUMN1 = "column1";
	
	/** <i>Generated constant</i> - Attribute key of <code>IndexTestItem.column2</code> attribute defined at extension <code>core</code>. */
	public static final String COLUMN2 = "column2";
	
	/** <i>Generated constant</i> - Attribute key of <code>IndexTestItem.column3</code> attribute defined at extension <code>core</code>. */
	public static final String COLUMN3 = "column3";
	
	/** <i>Generated constant</i> - Attribute key of <code>IndexTestItem.column4</code> attribute defined at extension <code>core</code>. */
	public static final String COLUMN4 = "column4";
	
	/** <i>Generated constant</i> - Attribute key of <code>IndexTestItem.column5</code> attribute defined at extension <code>core</code>. */
	public static final String COLUMN5 = "column5";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public IndexTestItemModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public IndexTestItemModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _column1 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 * @param _column2 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 * @param _column3 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 * @param _column4 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 * @param _column5 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 */
	@Deprecated
	public IndexTestItemModel(final Short _column1, final Short _column2, final Short _column3, final Short _column4, final Short _column5)
	{
		super();
		setColumn1(_column1);
		setColumn2(_column2);
		setColumn3(_column3);
		setColumn4(_column4);
		setColumn5(_column5);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _column1 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 * @param _column2 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 * @param _column3 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 * @param _column4 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 * @param _column5 initial attribute declared by type <code>IndexTestItem</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public IndexTestItemModel(final Short _column1, final Short _column2, final Short _column3, final Short _column4, final Short _column5, final ItemModel _owner)
	{
		super();
		setColumn1(_column1);
		setColumn2(_column2);
		setColumn3(_column3);
		setColumn4(_column4);
		setColumn5(_column5);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IndexTestItem.column1</code> attribute defined at extension <code>core</code>. 
	 * @return the column1
	 */
	@Accessor(qualifier = "column1", type = Accessor.Type.GETTER)
	public Short getColumn1()
	{
		return getPersistenceContext().getPropertyValue(COLUMN1);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IndexTestItem.column2</code> attribute defined at extension <code>core</code>. 
	 * @return the column2
	 */
	@Accessor(qualifier = "column2", type = Accessor.Type.GETTER)
	public Short getColumn2()
	{
		return getPersistenceContext().getPropertyValue(COLUMN2);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IndexTestItem.column3</code> attribute defined at extension <code>core</code>. 
	 * @return the column3
	 */
	@Accessor(qualifier = "column3", type = Accessor.Type.GETTER)
	public Short getColumn3()
	{
		return getPersistenceContext().getPropertyValue(COLUMN3);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IndexTestItem.column4</code> attribute defined at extension <code>core</code>. 
	 * @return the column4
	 */
	@Accessor(qualifier = "column4", type = Accessor.Type.GETTER)
	public Short getColumn4()
	{
		return getPersistenceContext().getPropertyValue(COLUMN4);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IndexTestItem.column5</code> attribute defined at extension <code>core</code>. 
	 * @return the column5
	 */
	@Accessor(qualifier = "column5", type = Accessor.Type.GETTER)
	public Short getColumn5()
	{
		return getPersistenceContext().getPropertyValue(COLUMN5);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>IndexTestItem.column1</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the column1
	 */
	@Accessor(qualifier = "column1", type = Accessor.Type.SETTER)
	public void setColumn1(final Short value)
	{
		getPersistenceContext().setPropertyValue(COLUMN1, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>IndexTestItem.column2</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the column2
	 */
	@Accessor(qualifier = "column2", type = Accessor.Type.SETTER)
	public void setColumn2(final Short value)
	{
		getPersistenceContext().setPropertyValue(COLUMN2, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>IndexTestItem.column3</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the column3
	 */
	@Accessor(qualifier = "column3", type = Accessor.Type.SETTER)
	public void setColumn3(final Short value)
	{
		getPersistenceContext().setPropertyValue(COLUMN3, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>IndexTestItem.column4</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the column4
	 */
	@Accessor(qualifier = "column4", type = Accessor.Type.SETTER)
	public void setColumn4(final Short value)
	{
		getPersistenceContext().setPropertyValue(COLUMN4, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>IndexTestItem.column5</code> attribute defined at extension <code>core</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the column5
	 */
	@Accessor(qualifier = "column5", type = Accessor.Type.SETTER)
	public void setColumn5(final Short value)
	{
		getPersistenceContext().setPropertyValue(COLUMN5, value);
	}
	
}
