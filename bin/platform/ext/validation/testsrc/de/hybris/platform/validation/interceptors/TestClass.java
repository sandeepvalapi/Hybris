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
package de.hybris.platform.validation.interceptors;

import java.math.BigDecimal;
import java.math.BigInteger;


public class TestClass
{
    private String stringPropertyNoAccessor;
	private String stringProperty;
	private boolean smallBooleanProperty;
	private Boolean bigBooleanProperty;
	private BigDecimal bigDecimalProperty;
	private BigInteger bigIntegerProperty;
	private Byte byteProperty;

	public String getStringProperty()
	{
		return stringProperty;
	}

	public boolean isSmallBooleanProperty()
	{
		return smallBooleanProperty;
	}

	public Boolean getBigBooleanProperty()
	{
		return bigBooleanProperty;
	}

	public BigDecimal getBigDecimalProperty()
	{
		return bigDecimalProperty;
	}

	public BigInteger getBigIntegerProperty()
	{
		return bigIntegerProperty;
	}

	public Byte getByteProperty()
	{
		return byteProperty;
	}
}
