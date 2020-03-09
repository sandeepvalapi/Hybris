/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.constants;

import de.hybris.platform.commercefacades.constants.CommerceFacadesConstants;


/**
 * Constants used in the Web tier
 */
public final class WebConstants
{
	public static final String MODEL_KEY_ADDITIONAL_BREADCRUMB = "additionalBreadcrumb";

	public static final String BREADCRUMBS_KEY = "breadcrumbs";

	public static final String CONTINUE_URL = "session_continue_url";

	public static final String CART_RESTORATION = "cart_restoration";

	public static final String ANONYMOUS_CHECKOUT = "anonymous_checkout";

	public static final String ANONYMOUS_CHECKOUT_GUID = "anonymous_checkout_guid";

	public static final String URL_ENCODING_ATTRIBUTES = "encodingAttributes";

	public static final String LANGUAGE_ENCODING = "languageEncoding";

	public static final String CURRENCY_ENCODING = "currencyEncoding";

	public static final String CART_RESTORATION_ERROR_STATUS = "restorationError";

	public static final String CART_RESTORATION_SHOW_MESSAGE = "showRestoration";

	public static final String MULTI_DIMENSIONAL_PRODUCT = "multiDimensionalProduct";

	public static final String CART_MERGED = "cartMerged";

	public static final String ANONYMOUS_CONSENT_COOKIE = "anonymous-consents";

	/**
	 * @deprecated Use {@link CommerceFacadesConstants#USER_CONSENTS} instead
	 */
	@Deprecated(since = "1905")
	public static final String USER_CONSENTS = CommerceFacadesConstants.USER_CONSENTS;

	/**
	 * @deprecated Use {@link CommerceFacadesConstants#CONSENT_GIVEN} instead
	 */
	@Deprecated(since = "1905")
	public static final String CONSENT_GIVEN = CommerceFacadesConstants.CONSENT_GIVEN;

	/**
	 * @deprecated Use {@link CommerceFacadesConstants#CONSENT_WITHDRAWN} instead
	 */
	@Deprecated(since = "1905")
	public static final String CONSENT_WITHDRAWN = CommerceFacadesConstants.CONSENT_WITHDRAWN;

	/**
	 * @deprecated Use {@link CommerceFacadesConstants#CONSENT_TEMPLATES} instead
	 */
	@Deprecated(since = "1905")
	public static final String CONSENT_TEMPLATES = CommerceFacadesConstants.CONSENT_TEMPLATES;

	public static final String CLOSE_ACCOUNT = "closeAcc";

	public static final String EMAIL_REGEX = "email.regex";

	private WebConstants()
	{
		//empty
	}
}
