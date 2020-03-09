/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.stocknotificationaddon.renderer.impl;

import de.hybris.platform.acceleratorcms.component.renderer.CMSComponentRenderer;
import de.hybris.platform.addonsupport.renderer.impl.DefaultAddOnCMSComponentRenderer;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.stocknotificationaddon.constants.StocknotificationaddonConstants;
import de.hybris.platform.stocknotificationfacades.StockNotificationFacade;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.springframework.beans.factory.annotation.Required;


/**
 * When product is out of stock, stock notification should be displayed instead of existing AddToCartAction.
 * This renderer is used to redefine AddToCartAction with stock notification function.
 */
public class AddToCartActionRendererForStockNotification<T extends AbstractCMSComponentModel> extends
		DefaultAddOnCMSComponentRenderer<T>
{
	private CMSComponentRenderer<T> defaultCmsComponentRenderer;
	private StockNotificationFacade stockNotificationFacade;


	@Override
	public void renderComponent(final PageContext pageContext, final T component) throws ServletException, IOException
	{
		final HttpServletRequest httpRequest = (HttpServletRequest) pageContext.getRequest();
		final ProductData product = (ProductData) httpRequest.getAttribute("product");
		if (product != null && StockLevelStatus.OUTOFSTOCK.equals(product.getStock().getStockLevelStatus()))
		{
			httpRequest.setAttribute("isWatching", stockNotificationFacade.isWatchingProduct(product));
			super.renderComponent(pageContext, component);
		}
		else
		{
			getDefaultCmsComponentRenderer().renderComponent(pageContext, component);
		}
	}

	@Override
	protected String getAddonUiExtensionName(final T component)
	{
		return StocknotificationaddonConstants.EXTENSIONNAME;
	}

	protected CMSComponentRenderer<T> getDefaultCmsComponentRenderer()
	{
		return defaultCmsComponentRenderer;
	}

	@Required
	public void setDefaultCmsComponentRenderer(final CMSComponentRenderer<T> defaultCmsComponentRenderer)
	{
		this.defaultCmsComponentRenderer = defaultCmsComponentRenderer;
	}

	protected StockNotificationFacade getStockNotificationFacade()
	{
		return stockNotificationFacade;
	}

	@Required
	public void setStockNotificationFacade(final StockNotificationFacade stockNotificationFacade)
	{
		this.stockNotificationFacade = stockNotificationFacade;
	}

}
