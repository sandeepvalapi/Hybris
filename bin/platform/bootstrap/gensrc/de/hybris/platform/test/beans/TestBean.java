/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:38 PM
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
 */
package de.hybris.platform.test.beans;

import java.io.Serializable;

public  class TestBean  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>TestBean.stringProperty</code> property defined at extension <code>core</code>. */
		
	private String stringProperty;

	/** <i>Generated property</i> for <code>TestBean.integerProperty</code> property defined at extension <code>core</code>. */
		
	private Integer integerProperty;

	/** <i>Generated property</i> for <code>TestBean.booleanProperty</code> property defined at extension <code>core</code>. */
		
	private boolean booleanProperty;

	/** <i>Generated property</i> for <code>TestBean.nativeProperty</code> property defined at extension <code>core</code>. */
		
	private int nativeProperty;

	/** <i>Generated property</i> for <code>TestBean.equalsA</code> property defined at extension <code>core</code>. */
		
	private String equalsA;

	/** <i>Generated property</i> for <code>TestBean.equalsB</code> property defined at extension <code>core</code>. */
		
	private Integer equalsB;

	/** <i>Generated property</i> for <code>TestBean.equalsC</code> property defined at extension <code>core</code>. */
		
	private Boolean equalsC;
	
	public TestBean()
	{
		// default constructor
	}
	
		
	
	public void setStringProperty(final String stringProperty)
	{
		this.stringProperty = stringProperty;
	}

		
	
	public String getStringProperty() 
	{
		return stringProperty;
	}
	
		
	
	public void setIntegerProperty(final Integer integerProperty)
	{
		this.integerProperty = integerProperty;
	}

		
	
	public Integer getIntegerProperty() 
	{
		return integerProperty;
	}
	
		
	
	public void setBooleanProperty(final boolean booleanProperty)
	{
		this.booleanProperty = booleanProperty;
	}

		
	
	public boolean isBooleanProperty() 
	{
		return booleanProperty;
	}
	
		
	
	public void setNativeProperty(final int nativeProperty)
	{
		this.nativeProperty = nativeProperty;
	}

		
	
	public int getNativeProperty() 
	{
		return nativeProperty;
	}
	
		
	
	public void setEqualsA(final String equalsA)
	{
		this.equalsA = equalsA;
	}

		
	
	public String getEqualsA() 
	{
		return equalsA;
	}
	
		
	
	public void setEqualsB(final Integer equalsB)
	{
		this.equalsB = equalsB;
	}

		
	
	public Integer getEqualsB() 
	{
		return equalsB;
	}
	
		
	
	public void setEqualsC(final Boolean equalsC)
	{
		this.equalsC = equalsC;
	}

		
	
	public Boolean getEqualsC() 
	{
		return equalsC;
	}
	

	@Override
	public boolean equals(final Object o)
	{
	
		if (o == null) return false;
		if (o == this) return true;

		try
		{
			final TestBean other = (TestBean) o;
			return new org.apache.commons.lang.builder.EqualsBuilder()
			.append(getEqualsA(), other.getEqualsA()) 
			.append(getEqualsB(), other.getEqualsB()) 
			.append(getEqualsC(), other.getEqualsC()) 
			.isEquals();
		} 
		catch (ClassCastException c)
		{
			return false;
		}
	}
	
	@Override
	public int hashCode()
	{
		return new org.apache.commons.lang.builder.HashCodeBuilder()
		.append(getEqualsA()) 
		.append(getEqualsB()) 
		.append(getEqualsC()) 
		.toHashCode();
	}

}
