/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms.verification;


import de.hybris.platform.commercefacades.address.data.AddressVerificationResult;
import de.hybris.platform.commercefacades.user.data.AddressData;

import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Strategy for handling an {@link AddressVerificationResult} returned by some
 * address verification service.
 */
public interface AddressVerificationResultHandler
{

	/**
	 * Given the result returned by an address verification service, determine if the
	 * address is valid or requires corrections from the user.
	 *
	 * @return true if the result has errors or suggested addresses, false if it is valid.
	 */
	boolean handleResult(AddressVerificationResult verificationResult, AddressData newAddress, Model model,
			RedirectAttributes redirectModel, Errors bindingResult, boolean customerBypass, String successMsg);
}
