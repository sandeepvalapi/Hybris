/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.assistedservicestorefront.controllers.cms;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceException;
import de.hybris.platform.assistedservicestorefront.constants.AssistedservicestorefrontConstants;
import de.hybris.platform.assistedservicestorefront.redirect.AssistedServiceRedirectStrategy;
import de.hybris.platform.assistedservicestorefront.security.AssistedServiceAgentAuthoritiesManager;
import de.hybris.platform.assistedservicestorefront.security.impl.AssistedServiceAgentLoginStrategy;
import de.hybris.platform.assistedservicestorefront.security.impl.AssistedServiceAgentLogoutStrategy;
import de.hybris.platform.assistedservicestorefront.security.impl.AssistedServiceAuthenticationToken;
import de.hybris.platform.assistedservicefacades.user.data.AutoSuggestionCustomerData;
import de.hybris.platform.assistedservicestorefront.util.SubscriptionFacadeReflectionWrapper;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sap.security.core.server.csi.XSSEncoder;


@Controller
@RequestMapping(value = "/assisted-service")
public class AssistedServiceComponentController extends AbstractController
{
	private static final String ASSISTED_SERVICE_COMPONENT = "addon:/assistedservicestorefront/cms/asm/assistedServiceComponent";

	private static final String ASM_MESSAGE_ATTRIBUTE = "asm_message";
	private static final String ASM_REDIRECT_URL_ATTRIBUTE = "redirect_url";
	private static final String ASM_ALERT_CLASS = "asm_alert_class";
	private static final String ENABLE_360_VIEW = "enable360View";
	private static final String CUSTOMER_ID = "customerId";
	private static final String CUSTOMER_NAME = "customerName";

	private static final Logger LOG = Logger.getLogger(AssistedServiceComponentController.class);

	@Resource(name = "assistedServiceFacade")
	private AssistedServiceFacade assistedServiceFacade;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "cartService")
	private CartService cartService;

	@Resource(name = "assistedServiceAgentLoginStrategy")
	private AssistedServiceAgentLoginStrategy assistedServiceAgentLoginStrategy;

	@Resource(name = "assistedServiceAgentLogoutStrategy")
	private AssistedServiceAgentLogoutStrategy assistedServiceAgentLogoutStrategy;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "subscriptionFacadeWrapper")
	private SubscriptionFacadeReflectionWrapper subscriptionFacadeWrapper;

	@Resource(name = "assistedServiceRedirectStrategy")
	private AssistedServiceRedirectStrategy assistedServiceRedirectStrategy;

	@Resource(name = "assistedServiceAgentAuthoritiesManager")
	private AssistedServiceAgentAuthoritiesManager authoritiesManager;

	@RequestMapping(value = "/quit", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void quitAssistedServiceMode()
	{
		assistedServiceFacade.quitAssistedServiceMode();
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginAssistedServiceAgent(final Model model, final HttpServletRequest request,
			final HttpServletResponse response, @RequestParam("username") final String username,
			@RequestParam("password") final String password)
	{
		try
		{
			assistedServiceFacade.loginAssistedServiceAgent(username, password);
			assistedServiceAgentLoginStrategy.login(username, request, response);
			assistedServiceFacade.emulateAfterLogin();
			refreshSpringSecurityToken();
			setSessionTimeout();
			model.addAttribute(ASM_REDIRECT_URL_ATTRIBUTE, assistedServiceRedirectStrategy.getRedirectPath());
		}
		catch (final AssistedServiceException e)
		{
			model.addAttribute(ASM_MESSAGE_ATTRIBUTE, e.getMessageCode());
			model.addAttribute(ASM_ALERT_CLASS, e.getAlertClass());
			model.addAttribute("username", this.encodeValue(username));
			LOG.debug(e.getMessage(), e);
		}
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		   return ASSISTED_SERVICE_COMPONENT;
	}

	@RequestMapping(value = "/logoutasm", method = RequestMethod.POST)
	public String logoutAssistedServiceAgent(final Model model, final HttpServletRequest request)
	{
		try
		{
			assistedServiceFacade.logoutAssistedServiceAgent();
		}
		catch (final AssistedServiceException e)
		{
			model.addAttribute(ASM_MESSAGE_ATTRIBUTE, e.getMessage());
			LOG.debug(e.getMessage(), e);
		}
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		model.addAttribute("customerReload", "reload");
		assistedServiceAgentLogoutStrategy.logout(request);
		return ASSISTED_SERVICE_COMPONENT;
	}

	@RequestMapping(value = "/personify-customer", method = RequestMethod.POST)
	public String emulateCustomer(final Model model, @RequestParam(CUSTOMER_ID) final String customerId,
			@RequestParam(CUSTOMER_NAME) final String customerName, @RequestParam("cartId") final String cartId)
	{
		try
		{
			assistedServiceFacade.emulateCustomer(customerId, cartId);
			refreshSpringSecurityToken();
			model.addAttribute(ASM_REDIRECT_URL_ATTRIBUTE, assistedServiceRedirectStrategy.getRedirectPath());
		}
		catch (final AssistedServiceException e)
		{
			model.addAttribute(ASM_MESSAGE_ATTRIBUTE, e.getMessageCode());
			model.addAttribute(ASM_ALERT_CLASS, e.getAlertClass());
			model.addAttribute(CUSTOMER_ID, this.encodeValue(customerId));
			model.addAttribute("cartId", this.encodeValue(cartId));
			model.addAttribute(CUSTOMER_NAME, this.encodeValue(customerName));
			LOG.debug(e.getMessage(), e);
		}
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		return ASSISTED_SERVICE_COMPONENT;
	}

	@RequestMapping(value = "/emulate", method = RequestMethod.GET)
	public String emulateCustomerByLink(final RedirectAttributes redirectAttrs,
			@RequestParam(value = CUSTOMER_ID, required = false) final String customerId,
			@RequestParam(value = "cartId", required = false) final String cartId,
			@RequestParam(value = "orderId", required = false) final String orderId,
			@RequestParam(value = "fwd", required = false) final String fwd,
			@RequestParam(value = "enable360View", required = false) final boolean enable360View)
	{
		try
		{
			// launch AS mode in case it has not been launched yet
			if (!assistedServiceFacade.isAssistedServiceModeLaunched())
			{
				LOG.debug("ASM launched after link-emulate request");
				assistedServiceFacade.launchAssistedServiceMode();
			}

			if (assistedServiceFacade.isAssistedServiceAgentLoggedIn())
			{
				assistedServiceFacade.stopEmulateCustomer();
				refreshSpringSecurityToken();
				LOG.debug(String.format("Previous emulation for customerId:[%s] has been stopped.",
						userService.getCurrentUser().getUid()));
			}

			//only set the flash attribute if this value is true and will only happen in case 360 view is initiated from customer list
			if (enable360View)
			{
				redirectAttrs.addFlashAttribute(ENABLE_360_VIEW, Boolean.valueOf(enable360View));
			}

			assistedServiceFacade.emulateCustomer(customerId, cartId, orderId);
			LOG.debug(String.format(
					"Link-emulate request successfuly started an emulation with parameters: customerId:[%s], cartId:[%s]", customerId,
					cartId));
			refreshSpringSecurityToken();
			return REDIRECT_PREFIX + assistedServiceRedirectStrategy.getRedirectPath();
		}
		catch (final AssistedServiceException e)
		{
			LOG.debug(e.getMessage(), e);
			redirectAttrs.addFlashAttribute(ASM_MESSAGE_ATTRIBUTE, e.getMessageCode());
			redirectAttrs.addFlashAttribute(ASM_ALERT_CLASS, e.getAlertClass());
			redirectAttrs.addFlashAttribute(CUSTOMER_ID, this.encodeValue(customerId));
			redirectAttrs.addFlashAttribute(CUSTOMER_NAME, this.encodeValue(customerId));
			redirectAttrs.addFlashAttribute("cartId", this.encodeValue(cartId));
			assistedServiceFacade.getAsmSession().setForwardUrl(fwd);
		}
		return REDIRECT_PREFIX + assistedServiceRedirectStrategy.getErrorRedirectPath();
	}

	@RequestMapping(value = "/create-account", method = RequestMethod.POST)
	public String createCustomer(final Model model, @RequestParam(CUSTOMER_ID) final String customerId,
			@RequestParam(CUSTOMER_NAME) final String customerName)
	{
		String redirectTo = ASSISTED_SERVICE_COMPONENT;
		try
		{
			assistedServiceFacade.createCustomer(customerId, customerName);
			// customerId is lower cased when user registered using customer account service
			final String customerIdLowerCased = customerId.toLowerCase();
			final CartModel sessionCart = cartService.getSessionCart();
			if (sessionCart != null && userService.isAnonymousUser(sessionCart.getUser())
					&& CollectionUtils.isNotEmpty(sessionCart.getEntries()))
			{
				// if there's already an anonymous cart with entries - bind it to customer
				bindCart(customerIdLowerCased, null, model);
				redirectTo = emulateCustomer(model, customerIdLowerCased, null, cartService.getSessionCart().getCode());
			}
			else
			{
				redirectTo = emulateCustomer(model, customerIdLowerCased, null, null);
			}
			subscriptionFacadeWrapper.updateProfile(new HashMap<String, String>());
		}
		catch (final AssistedServiceException e)
		{
			if (e.getMessage() != null && e.getMessage().toLowerCase().contains("duplicate"))
			{
				model.addAttribute(ASM_MESSAGE_ATTRIBUTE, "asm.createCustomer.duplicate.error");
				model.addAttribute(ASM_ALERT_CLASS, "ASM_alert_customer ASM_alert_create_new");
			}
			else
			{
				model.addAttribute(ASM_MESSAGE_ATTRIBUTE, "asm.createCustomer.error");
			}
			model.addAttribute(CUSTOMER_ID, this.encodeValue(customerId));
			model.addAttribute(CUSTOMER_NAME, this.encodeValue(customerName + ", " + customerId));
			LOG.debug(e.getMessage(), e);
		}
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		return redirectTo;
	}

	@RequestMapping(value = "/personify-stop", method = RequestMethod.POST)
	public String endEmulateCustomer(final Model model)
	{
		authoritiesManager.restoreInitialAuthorities();
		assistedServiceFacade.stopEmulateCustomer();
		refreshSpringSecurityToken();
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		model.addAttribute(ASM_REDIRECT_URL_ATTRIBUTE, "/");
		return ASSISTED_SERVICE_COMPONENT;
	}

	@RequestMapping(value = "/resetSession", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void resetSession()
	{
		return;
	}

	@RequestMapping(value = "/autocomplete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<AutoSuggestionCustomerData> autocomplete(@RequestParam(CUSTOMER_ID) final String customerId)
	{
		return assistedServiceFacade.getSuggestedCustomerData(customerId);
	}

	@RequestMapping(value = "/bind-cart", method = RequestMethod.POST)
	public String bindCart(@RequestParam(value = CUSTOMER_ID, required = false) final String customerId,
			@RequestParam(value = "cartId", required = false) final String cartId, final Model model)
	{
		try
		{
			assistedServiceFacade.bindCustomerToCart(customerId, cartId);
			refreshSpringSecurityToken();
			model.addAttribute(ASM_REDIRECT_URL_ATTRIBUTE, "/");
		}
		catch (final AssistedServiceException e)
		{
			model.addAttribute(ASM_MESSAGE_ATTRIBUTE, e.getMessage());
			model.addAttribute(CUSTOMER_ID, this.encodeValue(customerId));
			LOG.debug(e.getMessage(), e);
		}
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		return ASSISTED_SERVICE_COMPONENT;
	}

	@RequestMapping(value = "/add-to-cart", method = RequestMethod.POST)
	public String addToCartEventHandler(final Model model)
	{
		try
		{
			// since cart isn't empty anymore - emulate mode should be on
			assistedServiceFacade.emulateCustomer(userService.getCurrentUser().getUid(), cartService.getSessionCart().getCode());
		}
		catch (final AssistedServiceException e)
		{
			LOG.debug(e.getMessage(), e);
			return null; // there will be 'page not found' response in case of exception
		}
		return refresh(model);
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
	public String refresh(final Model model)
	{
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		return ASSISTED_SERVICE_COMPONENT;
	}


	protected void setSessionTimeout()
	{
		JaloSession.getCurrentSession().setTimeout(assistedServiceFacade.getAssistedServiceSessionTimeout());
		// since agent is logged in - change session timeout to the value from properties
		((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession()
				.setMaxInactiveInterval(assistedServiceFacade.getAssistedServiceSessionTimeout()); // in seconds
	}

	/**
	 * This method should be called after any facade method where user substitution may occur
	 */
	protected void refreshSpringSecurityToken()
	{
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AssistedServiceAuthenticationToken)
		{
			final UserModel currentUser = userService.getCurrentUser();
			if (currentUser == null || userService.isAnonymousUser(currentUser) || isASAgent(currentUser))
			{
				((AssistedServiceAuthenticationToken) authentication).setEmulating(false);
			}
			else
			{
				((AssistedServiceAuthenticationToken) authentication).setEmulating(true);
				authoritiesManager.addCustomerAuthoritiesToAgent(currentUser.getUid());
			}
		}
	}

	protected boolean isASAgent(final UserModel currentUser)
	{
		final Set<UserGroupModel> userGroups = userService.getAllUserGroupsForUser(currentUser);
		for (final UserGroupModel userGroup : userGroups)
		{
			if (AssistedservicestorefrontConstants.AS_AGENT_GROUP_UID.equals(userGroup.getUid()))
			{
				return true;
			}
		}
		return false;
	}

	protected String encodeValue(final String inputValue)
	{
		final String trimmedInputValue = StringUtils.isEmpty(inputValue) ? "" : inputValue.trim();

		try
		{
			return XSSEncoder.encodeHTML(trimmedInputValue);
		}
		catch (final UnsupportedEncodingException e)
		{
			LOG.error("Error occured during encoding the input value: " + inputValue, e);
		}
		return null;
	}
}
