/**
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.interceptors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hybris.merchandising.constants.MerchandisingConstants;
import com.hybris.merchandising.constants.MerchandisingaddonConstants;
import com.hybris.merchandising.context.ContextRepository;
import com.hybris.merchandising.context.ContextService;
import com.hybris.merchandising.model.Breadcrumbs;
import com.hybris.merchandising.model.ContextMap;
import com.hybris.merchandising.model.Facet;

import de.hybris.platform.acceleratorservices.data.RequestContextData;
import de.hybris.platform.acceleratorservices.storefront.data.JavaScriptVariableData;
import de.hybris.platform.acceleratorservices.util.SpringHelper;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.addonsupport.config.javascript.JavaScriptVariableDataFactory;
import de.hybris.platform.addonsupport.interceptors.BeforeViewHandlerAdaptee;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.strategies.ConsumedDestinationLocatorStrategy;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;


/**
 *
 * Implements {@link BeforeViewHandlerAdaptee} and intercepts the view request.
 *
 */
public class HybrisMerchandisingBeforeViewHandlerAdaptee implements BeforeViewHandlerAdaptee
{
	private static final Logger LOG = Logger.getLogger(HybrisMerchandisingBeforeViewHandlerAdaptee.class);

	private static final String SEARCH_PAGE_DATA = "searchPageData";
	private static final String BREADCRUMBS = "breadcrumbs";
	private static final String CMSPAGE = "cmsPage";
	private SessionService sessionService;
	private ContextService contextService;
	private ConsumedDestinationLocatorStrategy consumedDestinationLocatorStrategy;
	private BaseSiteService baseSiteService;

	protected static final String[] ACTIONS =
	{
			MerchandisingConstants.PAGE_CONTEXT_FACETS, MerchandisingConstants.PAGE_CONTEXT_CATEGORY,
			MerchandisingConstants.PAGE_CONTEXT_BREADCRUMBS, MerchandisingConstants.PAGE_CONTEXT_TENANT,
			MerchandisingConstants.SITE_ID, MerchandisingConstants.LANGUAGE,
			MerchandisingConstants.PRODUCT };

	private final ObjectMapper objectMapper = new ObjectMapper();

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.addonsupport.interceptors.BeforeViewHandlerAdaptee#beforeView(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.ui.ModelMap, java.lang.String)
	 */
	@Override
	public String beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelMap model,
			final String viewName)
			throws Exception
	{
		return Optional.ofNullable(sessionService.getCurrentSession())
				.map(currentSession -> {
					storeTenant(currentSession);
					storeFacets(currentSession, model);
					storeBaseSite();
					storeLanguage(currentSession);
					storeCurrentlyViewedProductCode(model);
					processCategoryBreadcrumbs(request, model, currentSession);
					return viewName;
				})
				.orElse(StringUtils.EMPTY);
	}

	private void processCategoryBreadcrumbs(final HttpServletRequest request, final ModelMap model, final Session currentSession)
	{
		try
		{
			storeCategory(getRequestContextData(request), currentSession, model);
			storeBreadcrumbs(currentSession, model);
			populateJSAddOnVariables(model);
		}
		finally
		{
			// Remove the HybrisConvert context data from memory.
			contextService.getContextRepository().clear();
		}
	}

	/**
	 * Retrieves the current base site in use and outputs it as add on variables.
	 */
	protected void storeBaseSite()
	{
		if(contextService.getContextRepository().get(MerchandisingConstants.SITE_ID) == null)
		{
			final String baseSiteId = getBaseSite();
			storeContextualData(MerchandisingConstants.SITE_ID, baseSiteId);
		}
	}

	/**
	 * Retrieves the current language from the session and outputs it as an add on variable.
	 * @param currentSession the {@link Session} of the current user.
	 */
	protected void storeLanguage(final Session currentSession)
	{
		if(contextService.getContextRepository().get(MerchandisingConstants.LANGUAGE) == null)
		{
			final LanguageModel language = currentSession.getAttribute(MerchandisingConstants.LANGUAGE);
			storeContextualData(MerchandisingConstants.LANGUAGE, language.getIsocode());
		}
	}

	/**
	 * Store tenant retrieves the configured YaaS tenant for us to use with Merchandising and stores it in the local session.
	 * @param currentSession the {@link Session} to add the tenant to.
	 */
	protected void storeTenant(final Session currentSession)
	{
		String tenant = currentSession.getAttribute(MerchandisingConstants.PAGE_CONTEXT_TENANT);
		if(tenant == null)
		{
			final ConsumedDestinationModel model = consumedDestinationLocatorStrategy.lookup(MerchandisingaddonConstants.STRATEGY_SERVICE);
			if(model != null) {
				tenant = model.getDestinationTarget().getId();
				currentSession.setAttribute(MerchandisingConstants.PAGE_CONTEXT_TENANT, tenant);
			}
		}
		storeContextualData(MerchandisingConstants.PAGE_CONTEXT_TENANT, tenant);
	}

	/**
	 * Retrieves the currently viewed product code from the {@link ModelMap} if present and
	 * stores in the context repository for rendering to page.
	 *
	 * @param model the {@link ModelMap} representing the current view.
	 */
	protected void storeCurrentlyViewedProductCode(final ModelMap model)
	{
		Optional.ofNullable((ProductData) model.get(MerchandisingConstants.PRODUCT))
				.map(ProductData :: getCode).ifPresent(productCode ->
					storeContextualData(MerchandisingConstants.PRODUCT, productCode));
	}

	private void storeContextualData(final String propertyName, final Serializable propertyValue)
	{
		final ContextMap contextMap = new ContextMap();
		contextMap.addProperty(propertyName, propertyValue);
		contextService.getContextRepository().put(propertyName, contextMap);
	}

	protected void storeCategory(final RequestContextData requestContextData, final Session currentSession, final ModelMap model)
	{
		Optional.ofNullable(model.get(SEARCH_PAGE_DATA))
				.ifPresent(searchPageData -> {
					if (searchPageData instanceof ProductCategorySearchPageData)
					{
						storeProductCategory(requestContextData, currentSession);
					}
					else
					{
						checkHomePage(currentSession, model);
					}
				});
	}

	private void checkHomePage(final Session currentSession, final ModelMap model)
	{
		final Object currentPageObject = model.get(CMSPAGE);
		if (currentPageObject instanceof ContentPageModel)
		{
			/**
			 * If we're on the homepage, clear the stored category.
			 */
			final ContentPageModel currentPage = (ContentPageModel) currentPageObject;
			if (currentPage.isHomepage())
			{
				currentSession.removeAttribute(MerchandisingConstants.PAGE_CONTEXT_CATEGORY);
			}
		}
	}

	private void storeProductCategory(final RequestContextData requestContextData, final Session currentSession)
	{
		/**
		 * Add the list of selected facets to the session for later retrieval
		 */
		if (requestContextData != null && requestContextData.getCategory() != null)
		{
			final CategoryModel cm = requestContextData.getCategory();
			currentSession.setAttribute(MerchandisingConstants.PAGE_CONTEXT_CATEGORY, cm.getCode());
			storeContextualData(MerchandisingConstants.PAGE_CONTEXT_CATEGORY, cm.getCode());
		}
		else
		{
			/**
			 * Remove the selected facets from the current session if there is a search page data object (a page with a
			 * search on it), but not facets have been selected
			 */
			currentSession.removeAttribute(MerchandisingConstants.PAGE_CONTEXT_CATEGORY);
		}
	}

	/**
	 * @param requestContextData
	 * @param currentSession
	 * @param model
	 */
	@SuppressWarnings("unchecked")
	protected void storeBreadcrumbs(final Session currentSession, final ModelMap model)
	{
		Optional.ofNullable(model.get(BREADCRUMBS))
				.map(obj -> (List<Breadcrumb>) obj)
				.map(this::getBreadcrumbs)
				.filter(Breadcrumbs :: hasBreadcrumbs)
				.ifPresent(breadcrumbs -> {
					currentSession.setAttribute(MerchandisingConstants.PAGE_CONTEXT_BREADCRUMBS,
							breadcrumbs);
					storeContextualData(MerchandisingConstants.PAGE_CONTEXT_BREADCRUMBS, breadcrumbs);
				});
	}

	/**
	 * @param breadcrumbs - list of {@link Breadcrumb}
	 * @return current breadcrumb trail.
	 */
	protected Breadcrumbs getBreadcrumbs(final List<Breadcrumb> breadcrumbs)
	{
		final Breadcrumbs bcs = new Breadcrumbs();
		breadcrumbs.stream().forEach(breadCrumb -> bcs.addBreadcrumb(breadCrumb.getUrl(), breadCrumb.getName()));
		return bcs;
	}

	@SuppressWarnings("unchecked")
	protected void storeFacets(final Session currentSession, final ModelMap model)
	{
		Optional.ofNullable(model.get(SEARCH_PAGE_DATA))
				.filter(obj -> obj instanceof ProductCategorySearchPageData)
				.map(ProductCategorySearchPageData.class::cast)
				.map(ProductCategorySearchPageData :: getBreadcrumbs)
				.ifPresent(breadcrumbs -> {
					if (!breadcrumbs.isEmpty())
					{
						final ArrayList<Facet> values = new ArrayList<>(getFacetBreadcrumbs(breadcrumbs).values());
						currentSession.setAttribute(MerchandisingConstants.PAGE_CONTEXT_FACETS, values);
						storeContextualData(MerchandisingConstants.PAGE_CONTEXT_FACETS, values);
					}
					else
					{
						/**
						 * Remove the selected facets from the current session if there is a search page data object (a page
						 * with a
						 * search on it), but not facets have been selected
						 */
						currentSession.removeAttribute(MerchandisingConstants.PAGE_CONTEXT_FACETS);
					}
				});
	}

	/**
	 * getFacetBreadcrumbs is a method for retrieving breadcrumb values for facets.
	 *
	 * @param breadcrumbs
	 *           the list of {@code BreadcrumbData} to use.
	 * @return a Map containing this.
	 */
	protected <STATE> Map<String, Facet> getFacetBreadcrumbs(final List<BreadcrumbData<STATE>> breadcrumbs)
	{
		final Map<String, Facet> selectedFacets = new HashMap<>();

		breadcrumbs.stream().map(
				breadcrumb -> Optional.ofNullable(selectedFacets.get(breadcrumb.getFacetCode())).map(selectedFacet -> {
					selectedFacet.addValue(breadcrumb.getFacetValueCode());
					return selectedFacet;
				}).orElseGet(() -> {
					final Facet facet = new Facet(breadcrumb.getFacetCode(), breadcrumb.getFacetName());
					facet.addValue(breadcrumb.getFacetValueCode());
					return facet;
				})).forEach(facet -> selectedFacets.put(facet.getCode(), facet));
		return selectedFacets;
	}

	protected void populateJSAddOnVariables(final ModelMap model)
	{
		doDebug("HybrisConvertBeforeViewHandlerAdaptee populateJSAddOnVariables called.");

		// Retrieve the JS AddOn variables from the model.
		Optional.ofNullable(sessionService.getAttribute(MerchandisingConstants.CONTEXT_STORE_KEY))
				.map(ContextRepository.class::cast)
				.ifPresent(contextRepository -> retrieveContextualDataFromActions(contextRepository, objectMapper,
						retrieveHybrisConvertJSAddOnVariables(model)));
	}

	/**
	 * Extract Contextual Data from various Actions
	 *
	 * @param contextRepo the {@code ContextRepository} containing the data to output.
	 * @param mapper the configured {@code ObjectMapper}.
	 * @param hybrisConvertJSAddOnVariables the JS variables to use.
	 */
	private void retrieveContextualDataFromActions(final ContextRepository contextRepo, final ObjectMapper mapper,
			final List<JavaScriptVariableData> hybrisConvertJSAddOnVariables)
	{
		Arrays.stream(ACTIONS)
				.forEach(action -> Optional.ofNullable(contextRepo.get(action))
						.map(hybrisConvertContextData -> createJson(mapper, hybrisConvertContextData))
						.map(json -> JavaScriptVariableDataFactory.create(action, json))
						.map(jsData -> persistAddOnVariable(jsData, hybrisConvertJSAddOnVariables)));
	}

	private static String createJson(final ObjectMapper mapper, final ContextMap hybrisConvertContextData)
	{
		String jsonValue = "";
		try
		{
			jsonValue = mapper.writeValueAsString(hybrisConvertContextData);
		}
		catch (final JsonProcessingException e)
		{
			LOG.error("Exception thrown generating add on JSON", e);
		}
		return jsonValue;
	}

	/**
	 * Retrieves the HybrisConvert AddOn JS variables from the model.
	 *
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<JavaScriptVariableData> retrieveHybrisConvertJSAddOnVariables(final ModelMap model)
	{
		return Optional.ofNullable((Map<String, List<JavaScriptVariableData>>) model
				.get(MerchandisingConstants.JS_ADDONS_CONTEXT_VARIABLES))
				.map(hmAllJSAddOnVariables -> hmAllJSAddOnVariables.get(MerchandisingaddonConstants.EXTENSIONNAME))
				.orElse(new ArrayList<JavaScriptVariableData>());
	}

	/**
	 * Stores a created {@code JavaScriptVariableData} in list to output.
	 *
	 * @param data
	 *           the {@code JavaScriptVariableData} object containing the value(s) to output.
	 * @param hybrisConvertJSAddOnVariables
	 *           the list to add them to.
	 * @return
	 */
	private boolean persistAddOnVariable(final JavaScriptVariableData data,
			final List<JavaScriptVariableData> hybrisConvertJSAddOnVariables)
	{
		return Optional.ofNullable(data)
				.map(hybrisConvertJSAddOnVariables::add)
				.orElse(false);
	}

	private static void doDebug(final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(message);
		}
	}

	protected RequestContextData getRequestContextData(final HttpServletRequest request)
	{
		return getBean(request, "requestContextData", RequestContextData.class);
	}

	/**
	 * Helper method to lookup a spring bean in the context of a request. This should only be used to lookup beans that
	 * are request scoped. The looked up bean is cached in the request attributes so it should not have a narrower scope
	 * than request scope. This method should not be used for beans that could be injected into this bean.
	 *
	 * @param request
	 *           the current request
	 * @param beanName
	 *           the name of the bean to lookup
	 * @param beanType
	 *           the expected type of the bean
	 * @param <T>
	 *           the expected type of the bean
	 * @return the bean found or <tt>null</tt>
	 */
	protected <T> T getBean(final HttpServletRequest request, final String beanName, final Class<T> beanType)
	{
		return SpringHelper.getSpringBean(request, beanName, beanType, true);
	}

	private String getBaseSite()
	{
		return Optional.ofNullable(baseSiteService.getCurrentBaseSite())
				.map(BaseSiteModel::getUid)
				.orElse(StringUtils.EMPTY);
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	public void setContextService(final ContextService contextService)
	{
		this.contextService = contextService;
	}

	public void setConsumedDestinationLocatorStrategy(final ConsumedDestinationLocatorStrategy consumedDestinationLocatorStrategy) {
		this.consumedDestinationLocatorStrategy = consumedDestinationLocatorStrategy;
	}

	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

}
