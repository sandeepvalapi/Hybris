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
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type TestItem first defined at extension core.
 */
@SuppressWarnings("all")
public class TestItemModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "TestItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.a</code> attribute defined at extension <code>core</code>. */
	public static final String A = "a";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.b</code> attribute defined at extension <code>core</code>. */
	public static final String B = "b";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.boolean</code> attribute defined at extension <code>core</code>. */
	public static final String BOOLEAN = "boolean";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.byte</code> attribute defined at extension <code>core</code>. */
	public static final String BYTE = "byte";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.character</code> attribute defined at extension <code>core</code>. */
	public static final String CHARACTER = "character";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.date</code> attribute defined at extension <code>core</code>. */
	public static final String DATE = "date";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.double</code> attribute defined at extension <code>core</code>. */
	public static final String DOUBLE = "double";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.float</code> attribute defined at extension <code>core</code>. */
	public static final String FLOAT = "float";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.integer</code> attribute defined at extension <code>core</code>. */
	public static final String INTEGER = "integer";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.long</code> attribute defined at extension <code>core</code>. */
	public static final String LONG = "long";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.primitiveBoolean</code> attribute defined at extension <code>core</code>. */
	public static final String PRIMITIVEBOOLEAN = "primitiveBoolean";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.primitiveByte</code> attribute defined at extension <code>core</code>. */
	public static final String PRIMITIVEBYTE = "primitiveByte";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.primitiveChar</code> attribute defined at extension <code>core</code>. */
	public static final String PRIMITIVECHAR = "primitiveChar";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.primitiveDouble</code> attribute defined at extension <code>core</code>. */
	public static final String PRIMITIVEDOUBLE = "primitiveDouble";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.primitiveFloat</code> attribute defined at extension <code>core</code>. */
	public static final String PRIMITIVEFLOAT = "primitiveFloat";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.primitiveInteger</code> attribute defined at extension <code>core</code>. */
	public static final String PRIMITIVEINTEGER = "primitiveInteger";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.primitiveLong</code> attribute defined at extension <code>core</code>. */
	public static final String PRIMITIVELONG = "primitiveLong";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.primitiveShort</code> attribute defined at extension <code>core</code>. */
	public static final String PRIMITIVESHORT = "primitiveShort";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.serializable</code> attribute defined at extension <code>core</code>. */
	public static final String SERIALIZABLE = "serializable";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.string</code> attribute defined at extension <code>core</code>. */
	public static final String STRING = "string";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.longString</code> attribute defined at extension <code>core</code>. */
	public static final String LONGSTRING = "longString";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.testProperty0</code> attribute defined at extension <code>core</code>. */
	public static final String TESTPROPERTY0 = "testProperty0";
	
	/** <i>Generated constant</i> - Attribute key of <code>TestItem.testDumpProperty</code> attribute defined at extension <code>core</code>. */
	public static final String TESTDUMPPROPERTY = "testDumpProperty";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public TestItemModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public TestItemModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public TestItemModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.a</code> attribute defined at extension <code>core</code>. 
	 * @return the a
	 */
	@Accessor(qualifier = "a", type = Accessor.Type.GETTER)
	public String getA()
	{
		return getPersistenceContext().getPropertyValue(A);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.b</code> attribute defined at extension <code>core</code>. 
	 * @return the b
	 */
	@Accessor(qualifier = "b", type = Accessor.Type.GETTER)
	public String getB()
	{
		return getPersistenceContext().getPropertyValue(B);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.boolean</code> attribute defined at extension <code>core</code>. 
	 * @return the boolean
	 */
	@Accessor(qualifier = "boolean", type = Accessor.Type.GETTER)
	public Boolean getBoolean()
	{
		return getPersistenceContext().getPropertyValue(BOOLEAN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.byte</code> attribute defined at extension <code>core</code>. 
	 * @return the byte
	 */
	@Accessor(qualifier = "byte", type = Accessor.Type.GETTER)
	public Byte getByte()
	{
		return getPersistenceContext().getPropertyValue(BYTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.character</code> attribute defined at extension <code>core</code>. 
	 * @return the character
	 */
	@Accessor(qualifier = "character", type = Accessor.Type.GETTER)
	public Character getCharacter()
	{
		return getPersistenceContext().getPropertyValue(CHARACTER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.date</code> attribute defined at extension <code>core</code>. 
	 * @return the date
	 */
	@Accessor(qualifier = "date", type = Accessor.Type.GETTER)
	public Date getDate()
	{
		return getPersistenceContext().getPropertyValue(DATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.double</code> attribute defined at extension <code>core</code>. 
	 * @return the double
	 */
	@Accessor(qualifier = "double", type = Accessor.Type.GETTER)
	public Double getDouble()
	{
		return getPersistenceContext().getPropertyValue(DOUBLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.float</code> attribute defined at extension <code>core</code>. 
	 * @return the float
	 */
	@Accessor(qualifier = "float", type = Accessor.Type.GETTER)
	public Float getFloat()
	{
		return getPersistenceContext().getPropertyValue(FLOAT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.integer</code> attribute defined at extension <code>core</code>. 
	 * @return the integer
	 */
	@Accessor(qualifier = "integer", type = Accessor.Type.GETTER)
	public Integer getInteger()
	{
		return getPersistenceContext().getPropertyValue(INTEGER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.long</code> attribute defined at extension <code>core</code>. 
	 * @return the long
	 */
	@Accessor(qualifier = "long", type = Accessor.Type.GETTER)
	public Long getLong()
	{
		return getPersistenceContext().getPropertyValue(LONG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.longString</code> attribute defined at extension <code>core</code>. 
	 * @return the longString
	 */
	@Accessor(qualifier = "longString", type = Accessor.Type.GETTER)
	public String getLongString()
	{
		return getPersistenceContext().getPropertyValue(LONGSTRING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.primitiveBoolean</code> attribute defined at extension <code>core</code>. 
	 * @return the primitiveBoolean
	 */
	@Accessor(qualifier = "primitiveBoolean", type = Accessor.Type.GETTER)
	public Boolean getPrimitiveBoolean()
	{
		return getPersistenceContext().getPropertyValue(PRIMITIVEBOOLEAN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.primitiveByte</code> attribute defined at extension <code>core</code>. 
	 * @return the primitiveByte
	 */
	@Accessor(qualifier = "primitiveByte", type = Accessor.Type.GETTER)
	public Byte getPrimitiveByte()
	{
		return getPersistenceContext().getPropertyValue(PRIMITIVEBYTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.primitiveChar</code> attribute defined at extension <code>core</code>. 
	 * @return the primitiveChar
	 */
	@Accessor(qualifier = "primitiveChar", type = Accessor.Type.GETTER)
	public Character getPrimitiveChar()
	{
		return getPersistenceContext().getPropertyValue(PRIMITIVECHAR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.primitiveDouble</code> attribute defined at extension <code>core</code>. 
	 * @return the primitiveDouble
	 */
	@Accessor(qualifier = "primitiveDouble", type = Accessor.Type.GETTER)
	public Double getPrimitiveDouble()
	{
		return getPersistenceContext().getPropertyValue(PRIMITIVEDOUBLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.primitiveFloat</code> attribute defined at extension <code>core</code>. 
	 * @return the primitiveFloat
	 */
	@Accessor(qualifier = "primitiveFloat", type = Accessor.Type.GETTER)
	public Float getPrimitiveFloat()
	{
		return getPersistenceContext().getPropertyValue(PRIMITIVEFLOAT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.primitiveInteger</code> attribute defined at extension <code>core</code>. 
	 * @return the primitiveInteger
	 */
	@Accessor(qualifier = "primitiveInteger", type = Accessor.Type.GETTER)
	public Integer getPrimitiveInteger()
	{
		return getPersistenceContext().getPropertyValue(PRIMITIVEINTEGER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.primitiveLong</code> attribute defined at extension <code>core</code>. 
	 * @return the primitiveLong
	 */
	@Accessor(qualifier = "primitiveLong", type = Accessor.Type.GETTER)
	public Long getPrimitiveLong()
	{
		return getPersistenceContext().getPropertyValue(PRIMITIVELONG);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.primitiveShort</code> attribute defined at extension <code>core</code>. 
	 * @return the primitiveShort
	 */
	@Accessor(qualifier = "primitiveShort", type = Accessor.Type.GETTER)
	public Short getPrimitiveShort()
	{
		return getPersistenceContext().getPropertyValue(PRIMITIVESHORT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.serializable</code> attribute defined at extension <code>core</code>. 
	 * @return the serializable
	 */
	@Accessor(qualifier = "serializable", type = Accessor.Type.GETTER)
	public Object getSerializable()
	{
		return getPersistenceContext().getPropertyValue(SERIALIZABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.string</code> attribute defined at extension <code>core</code>. 
	 * @return the string
	 */
	@Accessor(qualifier = "string", type = Accessor.Type.GETTER)
	public String getString()
	{
		return getPersistenceContext().getPropertyValue(STRING);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.testDumpProperty</code> attribute defined at extension <code>core</code>. 
	 * @return the testDumpProperty
	 */
	@Accessor(qualifier = "testDumpProperty", type = Accessor.Type.GETTER)
	public String getTestDumpProperty()
	{
		return getPersistenceContext().getPropertyValue(TESTDUMPPROPERTY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TestItem.testProperty0</code> attribute defined at extension <code>core</code>. 
	 * @return the testProperty0
	 */
	@Accessor(qualifier = "testProperty0", type = Accessor.Type.GETTER)
	public String getTestProperty0()
	{
		return getPersistenceContext().getPropertyValue(TESTPROPERTY0);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.a</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the a
	 */
	@Accessor(qualifier = "a", type = Accessor.Type.SETTER)
	public void setA(final String value)
	{
		getPersistenceContext().setPropertyValue(A, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.b</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the b
	 */
	@Accessor(qualifier = "b", type = Accessor.Type.SETTER)
	public void setB(final String value)
	{
		getPersistenceContext().setPropertyValue(B, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.boolean</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the boolean
	 */
	@Accessor(qualifier = "boolean", type = Accessor.Type.SETTER)
	public void setBoolean(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(BOOLEAN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.byte</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the byte
	 */
	@Accessor(qualifier = "byte", type = Accessor.Type.SETTER)
	public void setByte(final Byte value)
	{
		getPersistenceContext().setPropertyValue(BYTE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.character</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the character
	 */
	@Accessor(qualifier = "character", type = Accessor.Type.SETTER)
	public void setCharacter(final Character value)
	{
		getPersistenceContext().setPropertyValue(CHARACTER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.date</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the date
	 */
	@Accessor(qualifier = "date", type = Accessor.Type.SETTER)
	public void setDate(final Date value)
	{
		getPersistenceContext().setPropertyValue(DATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.double</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the double
	 */
	@Accessor(qualifier = "double", type = Accessor.Type.SETTER)
	public void setDouble(final Double value)
	{
		getPersistenceContext().setPropertyValue(DOUBLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.float</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the float
	 */
	@Accessor(qualifier = "float", type = Accessor.Type.SETTER)
	public void setFloat(final Float value)
	{
		getPersistenceContext().setPropertyValue(FLOAT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.integer</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the integer
	 */
	@Accessor(qualifier = "integer", type = Accessor.Type.SETTER)
	public void setInteger(final Integer value)
	{
		getPersistenceContext().setPropertyValue(INTEGER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.long</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the long
	 */
	@Accessor(qualifier = "long", type = Accessor.Type.SETTER)
	public void setLong(final Long value)
	{
		getPersistenceContext().setPropertyValue(LONG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.longString</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the longString
	 */
	@Accessor(qualifier = "longString", type = Accessor.Type.SETTER)
	public void setLongString(final String value)
	{
		getPersistenceContext().setPropertyValue(LONGSTRING, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.primitiveBoolean</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the primitiveBoolean
	 */
	@Accessor(qualifier = "primitiveBoolean", type = Accessor.Type.SETTER)
	public void setPrimitiveBoolean(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(PRIMITIVEBOOLEAN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.primitiveByte</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the primitiveByte
	 */
	@Accessor(qualifier = "primitiveByte", type = Accessor.Type.SETTER)
	public void setPrimitiveByte(final Byte value)
	{
		getPersistenceContext().setPropertyValue(PRIMITIVEBYTE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.primitiveChar</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the primitiveChar
	 */
	@Accessor(qualifier = "primitiveChar", type = Accessor.Type.SETTER)
	public void setPrimitiveChar(final Character value)
	{
		getPersistenceContext().setPropertyValue(PRIMITIVECHAR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.primitiveDouble</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the primitiveDouble
	 */
	@Accessor(qualifier = "primitiveDouble", type = Accessor.Type.SETTER)
	public void setPrimitiveDouble(final Double value)
	{
		getPersistenceContext().setPropertyValue(PRIMITIVEDOUBLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.primitiveFloat</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the primitiveFloat
	 */
	@Accessor(qualifier = "primitiveFloat", type = Accessor.Type.SETTER)
	public void setPrimitiveFloat(final Float value)
	{
		getPersistenceContext().setPropertyValue(PRIMITIVEFLOAT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.primitiveInteger</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the primitiveInteger
	 */
	@Accessor(qualifier = "primitiveInteger", type = Accessor.Type.SETTER)
	public void setPrimitiveInteger(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PRIMITIVEINTEGER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.primitiveLong</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the primitiveLong
	 */
	@Accessor(qualifier = "primitiveLong", type = Accessor.Type.SETTER)
	public void setPrimitiveLong(final Long value)
	{
		getPersistenceContext().setPropertyValue(PRIMITIVELONG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.primitiveShort</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the primitiveShort
	 */
	@Accessor(qualifier = "primitiveShort", type = Accessor.Type.SETTER)
	public void setPrimitiveShort(final Short value)
	{
		getPersistenceContext().setPropertyValue(PRIMITIVESHORT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.serializable</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the serializable
	 */
	@Accessor(qualifier = "serializable", type = Accessor.Type.SETTER)
	public void setSerializable(final Object value)
	{
		getPersistenceContext().setPropertyValue(SERIALIZABLE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.string</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the string
	 */
	@Accessor(qualifier = "string", type = Accessor.Type.SETTER)
	public void setString(final String value)
	{
		getPersistenceContext().setPropertyValue(STRING, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.testDumpProperty</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the testDumpProperty
	 */
	@Accessor(qualifier = "testDumpProperty", type = Accessor.Type.SETTER)
	public void setTestDumpProperty(final String value)
	{
		getPersistenceContext().setPropertyValue(TESTDUMPPROPERTY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TestItem.testProperty0</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the testProperty0
	 */
	@Accessor(qualifier = "testProperty0", type = Accessor.Type.SETTER)
	public void setTestProperty0(final String value)
	{
		getPersistenceContext().setPropertyValue(TESTPROPERTY0, value);
	}
	
}
