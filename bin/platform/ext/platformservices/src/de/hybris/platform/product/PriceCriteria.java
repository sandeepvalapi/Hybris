/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.product;

import de.hybris.platform.core.model.product.ProductModel;

import java.util.Date;
import java.util.Objects;


public interface PriceCriteria
{

	public ProductModel getProduct();

	public Date getDate();

	public Boolean isNet();

	static class DefaultPriceCriteria implements PriceCriteria
	{
		private final ProductModel product;
		private final Date forDate;
		private final Boolean isNet;

		private DefaultPriceCriteria(final ProductModel product, final Date forDate, final Boolean isNet)
		{
			this.product = Objects.requireNonNull(product, "product mustn't be null");
			this.forDate = forDate;
			this.isNet = isNet;
		}

		public static DefaultPriceCriteria forProduct(final ProductModel product)
		{
			return new DefaultPriceCriteria(product, null, null);
		}

		public DefaultPriceCriteria forDate(final Date forDate)
		{
			return new DefaultPriceCriteria(this.product, forDate, this.isNet);
		}

		public DefaultPriceCriteria withNetPrice()
		{
			return new DefaultPriceCriteria(this.product, this.forDate, Boolean.TRUE);
		}

		public DefaultPriceCriteria withGrossPrice()
		{
			return new DefaultPriceCriteria(this.product, this.forDate, Boolean.FALSE);
		}

		public DefaultPriceCriteria withNetPrice(final Boolean isNet)
		{
			return new DefaultPriceCriteria(this.product, this.forDate, isNet);
		}

		@Override
		public ProductModel getProduct()
		{
			return product;
		}

		@Override
		public Date getDate()
		{
			return forDate;
		}

		@Override
		public Boolean isNet()
		{
			return isNet;
		}
	}
}
