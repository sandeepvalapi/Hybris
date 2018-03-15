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
package de.hybris.platform.catalog.job.diff.impl;

import de.hybris.platform.catalog.enums.ProductDifferenceMode;
import de.hybris.platform.catalog.job.diff.CatalogVersionDifferenceFinder;
import de.hybris.platform.catalog.model.CatalogVersionDifferenceModel;
import de.hybris.platform.catalog.model.CompareCatalogVersionsCronJobModel;
import de.hybris.platform.catalog.model.ProductCatalogVersionDifferenceModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Difference finder for a {@link ProductModel#getApprovalStatus()}'s between two different
 * {@link CompareCatalogVersionsCronJobModel}'s <code>catalogVersions</code>.
 */
public class ProductPriceDiffFinder implements CatalogVersionDifferenceFinder<ProductModel, CatalogVersionDifferenceModel>
{
	private static final Logger LOG = Logger.getLogger(ProductPriceDiffFinder.class.getName());

	private EnumerationService enumerationService;

	private PriceService priceService;

	private FlexibleSearchService flexibleSearchService;

	private ModelService modelService;

	private SessionService sessionService;

	private double maximumPriceTolerance = 0.0;

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	@Required
	public void setPriceService(final PriceService priceService)
	{
		this.priceService = priceService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}


	protected ProductCatalogVersionDifferenceModel populateDifferenceModel(final ProductModel srcProduct,
			final ProductModel targetProduct, final CompareCatalogVersionsCronJobModel cronJob)
	{
		final ProductCatalogVersionDifferenceModel difference = modelService.create(ProductCatalogVersionDifferenceModel.class);
		difference.setSourceVersion(cronJob.getSourceVersion());
		difference.setTargetVersion(cronJob.getTargetVersion());
		difference.setCronJob(cronJob);
		difference.setSourceProduct(srcProduct);
		difference.setTargetProduct(targetProduct);

		final ProductDifferenceMode productDifferenceMode = (ProductDifferenceMode) enumerationService.getEnumerationValue(
				ProductDifferenceMode.PRODUCT_PRICEDIFFERENCE.getType(), ProductDifferenceMode.PRODUCT_PRICEDIFFERENCE.getCode());

		difference.setMode(productDifferenceMode);

		return difference;
	}

	@Override
	public int processDifferences(final CompareCatalogVersionsCronJobModel cronJobModel)
	{
		return comparePrices(cronJobModel, cronJobModel.getMaxPriceTolerance() == null ? 0.0 : cronJobModel.getMaxPriceTolerance()
				.doubleValue());
	}

	/**
	 * Returns true if product price difference should be done.
	 */
	private boolean shouldProcess(final CompareCatalogVersionsCronJobModel cronJobModel)
	{
		return BooleanUtils.isTrue(cronJobModel.getSearchPriceDifferences());
	}


	private int comparePrices(final CompareCatalogVersionsCronJobModel cronJob, final double maxPriceTolerance)
	{
		Collection<List<ProductModel>> productPairs;
		int start = 0;
		final int range = 1000;
		int processedStepsCounter = 0;
		if (shouldProcess(cronJob))
		{

			do
			{
				productPairs = getSameProductsAsPair(start, range, cronJob);
				start += range;

				for (final Iterator<List<ProductModel>> it = productPairs.iterator(); it.hasNext();)
				{
					final List<ProductModel> pair = it.next();
					final ProductModel product1 = pair.get(0);
					final ProductModel product2 = pair.get(1);

					try
					{

						final Collection<PriceInformation> newPriceInfos = (Collection<PriceInformation>) sessionService
								.executeInLocalView(new SessionExecutionBody()
								{
									@Override
									public Object execute()
									{
										return priceService.getPriceInformationsForProduct(product1);//p1.getPriceInformations(true);

									}
								},
										cronJob.getPriceCompareCustomer() == null ? (UserModel) sessionService.getAttribute("user")
												: cronJob.getPriceCompareCustomer());

						final Collection<PriceInformation> oldPriceInfos = (Collection<PriceInformation>) sessionService
								.executeInLocalView(new SessionExecutionBody()
								{
									@Override
									public Object execute()
									{
										return priceService.getPriceInformationsForProduct(product2);

									}
								},
										cronJob.getPriceCompareCustomer() == null ? (UserModel) sessionService.getAttribute("user")
												: cronJob.getPriceCompareCustomer());


						CatalogVersionDifferenceModel diff = null;

						if (newPriceInfos.size() != oldPriceInfos.size())
						{
							diff = populateDifferenceModel(product1, product2, cronJob);
							diff.setDifferenceText("Difference in price info count! oldPrices: " + oldPriceInfos.size() + " newPrices: "
									+ newPriceInfos.size());
						}
						for (final Iterator<PriceInformation> priceIt = newPriceInfos.iterator(); priceIt.hasNext();)
						{
							final PriceInformation priceInfo = priceIt.next();
							final PriceInformation equivalentPriceInfo = findEquivalentPriceInfo(priceInfo, oldPriceInfos);
							if (equivalentPriceInfo != null)
							{
								final double newPrice = priceInfo.getPriceValue().getValue();
								final double oldPrice = equivalentPriceInfo.getPriceValue().getValue();
								final double difference = oldPrice - newPrice;
								final double tolerance = Math.abs((difference * 100) / oldPrice);
								if (tolerance > maxPriceTolerance)
								{
									if (tolerance > maximumPriceTolerance)
									{
										maximumPriceTolerance = tolerance;
									}
									final StringBuilder diffText = new StringBuilder();
									if (diff == null)
									{
										diff = populateDifferenceModel(product1, product2, cronJob);
									}
									else
									{
										diffText.append(diff.getDifferenceText()).append("\n");
									}
									diffText.append("Difference above max tolerance ( " + maxPriceTolerance + " ) in price: (new: "
											+ newPrice + " old: " + oldPrice + " for PriceInfo: " + priceInfo + ".");
									diff.setDifferenceText(diffText.toString());
									diff.setDifferenceValue(new Double(tolerance));
								}
							}
							else
							{
								LOG.warn("No equivalent PriceInfo found for PriceInfo:" + priceInfo);
							}

						}
						if (diff != null)
						{
							modelService.save(diff);
							processedStepsCounter++;
						}
					}
					catch (final SystemException e)
					{
						LOG.error(e.getMessage(), e);
					}
				}
			}
			while (productPairs.size() == range);
		}

		return processedStepsCounter;
	}


	private Collection<List<ProductModel>> getSameProductsAsPair(final int start, final int count,
			final CompareCatalogVersionsCronJobModel ccvCronJob)
	{
		final Map values = new HashMap();
		values.put("version1", ccvCronJob.getSourceVersion());
		values.put("version2", ccvCronJob.getTargetVersion());


		final FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {p1:" + ItemModel.PK + "} as pk1, {p2:" + ItemModel.PK
				+ "} as pk2 FROM {" + ProductModel._TYPECODE + " AS p1}, {"//
				+ ProductModel._TYPECODE + " AS p2} " //
				+ "WHERE EXISTS ({{" //
				+ "SELECT {p3:" + ItemModel.PK + "} FROM {" + ProductModel._TYPECODE + " AS p3} " //
				+ "WHERE {p3:" + ProductModel.CATALOGVERSION + "} = ?version1 " //
				+ "AND {p1:" + ProductModel.CODE + "} = {p3:" + ProductModel.CODE + "} " //
				+ "AND {p2:" + ProductModel.CODE + "} = {p3:" + ProductModel.CODE + "} " //
				+ "}}) " //
				+ "AND {p1:" + ProductModel.CATALOGVERSION + "} = ?version2 " //
				+ "AND {p2:" + ProductModel.CATALOGVERSION + "} = ?version1 "//
		, values);
		query.setResultClassList(Arrays.asList(new Class[]
		{ ProductModel.class, ProductModel.class }));
		query.setStart(start);
		query.setCount(count);

		final SearchResult<List<ProductModel>> result = flexibleSearchService.search(query);
		return result.getResult();

	}


	private PriceInformation findEquivalentPriceInfo(final PriceInformation priceInfo, final Collection priceInfos)
	{
		PriceInformation equivalentPriceInfo = null;
		for (final Iterator it = priceInfos.iterator(); it.hasNext();)
		{
			final PriceInformation tempPriceInfo = (PriceInformation) it.next();
			if (tempPriceInfo.equalsWithoutPriceRow(priceInfo))
			{
				equivalentPriceInfo = tempPriceInfo;
				break;
			}
		}
		return equivalentPriceInfo;
	}

}
