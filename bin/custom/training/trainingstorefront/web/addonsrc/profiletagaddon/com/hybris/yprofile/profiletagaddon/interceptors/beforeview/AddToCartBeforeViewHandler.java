/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.hybris.yprofile.profiletagaddon.interceptors.beforeview;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.acceleratorstorefrontcommons.interceptors.BeforeViewHandler;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

public class AddToCartBeforeViewHandler implements BeforeViewHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AddToCartBeforeViewHandler.class);

    private static final String ADD_TO_CART_POPUP = "fragments/cart/addToCartPopup";

    private static final String NEW_ADD_TO_CART_POPUP = "addon:/profiletagaddon/fragments/cart/addToCartPopup";
    private static final String PRODUCT_KEY = "product";
    private static final String CATEGORIES_KEY = "categories";

    private ProductFacade productFacade;

    private ObjectMapper objectMapper;

    @Override
    public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView) {

        if(modelAndView != null) {

            final ModelMap model = modelAndView.getModelMap();
            final String viewName = modelAndView.getViewName();

            if (model != null && viewName != null && viewName.equals(ADD_TO_CART_POPUP)) {

                // first perform fail safe null check and cast of the product data object before getting the product key
                Optional.ofNullable(model.get(PRODUCT_KEY))
                        .filter(ProductData.class::isInstance)
                        .map(ProductData.class::cast)
                        .map(ProductData::getCode)
                        // then add categories
                        .ifPresent(productCode -> {
                            final ProductData productWithCategories = productFacade.getProductForCodeAndOptions(productCode, Collections.singleton(ProductOption.CATEGORIES));

                            final List<String> categoryList = productWithCategories.getCategories().stream()
                                    .map(categoryModel -> categoryModel.getCode())
                                    .collect(Collectors.toList());

                            try {
                                final String categories =  getObjectMapper().writeValueAsString(categoryList);
                                model.addAttribute(CATEGORIES_KEY, categories);
                            } catch (JsonProcessingException e) {
                                LOG.debug("Cannot add categories to a ModelAndView.", e);
                            }

                            modelAndView.setViewName(NEW_ADD_TO_CART_POPUP);
                            LOG.debug("Added categories to product {}.", productCode);
                        });

            } else {
                LOG.debug("Cannot add categories to the view {}.", viewName);
            }
        } else {
            LOG.debug("Cannot add categories to a ModelAndView being null.");
        }

    }

    public void setProductFacade(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @Required
    public void setObjectMapper(final ObjectMapper objectMapper)
    {
        objectMapper.configure( JsonParser.Feature.ALLOW_COMMENTS, true);
        this.objectMapper = objectMapper;

    }

    public ObjectMapper getObjectMapper()
    {
        return objectMapper;
    }
}
