/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation;


public enum ValidationResults
{
	SUCCESS("SUCCESS"), FAILED("FAILED"), REDIRECT_TO_CART("REDIRECT_TO_CART"), REDIRECT_TO_PAYMENT_TYPE(
			"REDIRECT_TO_PAYMENT_TYPE"), REDIRECT_TO_PICKUP_LOCATION("REDIRECT_TO_PICKUP_LOCATION"), REDIRECT_TO_DELIVERY_ADDRESS(
			"REDIRECT_TO_DELIVERY_ADDRESS"), REDIRECT_TO_DELIVERY_METHOD("REDIRECT_TO_DELIVERY_METHOD"), REDIRECT_TO_PAYMENT_METHOD(
			"REDIRECT_TO_PAYMENT_METHOD"), REDIRECT_TO_SUMMARY("REDIRECT_TO_SUMMARY"), REDIRECT_TO_DUMMY_STEP(
			"REDIRECT_TO_DUMMY_STEP");

	private String result;

	private ValidationResults(final String result)
	{
		this.result = result;
	}

	public String getResult()
	{
		return result;
	}
}
