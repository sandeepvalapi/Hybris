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
package de.hybris.platform.acceleratorstorefrontcommons.tags;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;


/**
 * This file contains static methods that are used by JSP EL.
 */
public class HTMLSanitizer
{
	protected static final PolicyFactory POLICY = new HtmlPolicyBuilder()
			.allowElements("pre", "address", "em", "hr")
			.allowAttributes("class").onElements("em")
			.toFactory()
			.and(Sanitizers.BLOCKS)
			.and(Sanitizers.FORMATTING)
			.and(Sanitizers.LINKS)
			.and(Sanitizers.TABLES)
			.and(Sanitizers.STYLES);

	/**
	 * JSP EL Function to sanitize unsafe HTML string
	 *
	 * @param untrustedHTML
	 *           potentially unsafe HTML string
	 * @return safe HTML string with allowed elements only. All other elements that are not specified as allowed are
	 *         removed.
	 */
	public static String sanitizeHTML(final String untrustedHTML)
	{
		return POLICY.sanitize(untrustedHTML);
	}
}
