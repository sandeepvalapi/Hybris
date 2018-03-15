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
package de.hybris.platform.product.impl;

import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.europe1.model.PDTRowModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelInternalContext;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.type.TypeService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * {@link PrepareInterceptor} for the {@link PDTRowModel}s.
 * <p/>
 * Contains common logic for the {@link PDTRowModel} subtypes, like updating the product and user match qualifiers or
 * handling the Product/ProductGroup or User/UserGroup
 */
public abstract class PDTRowPrepareInterceptor implements PrepareInterceptor
{

	private TypeService typeService;
	private CatalogTypeService catalogTypeService;
	private PDTProductModificationMarker pdtProductModificationMarker;

	private static final Logger LOG = Logger.getLogger(PDTRowPrepareInterceptor.class);

	@Required
	public void setCatalogTypeService(final CatalogTypeService catalogTypeService)
	{
		this.catalogTypeService = catalogTypeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	@Required
	public void setPdtProductModificationMarker(final PDTProductModificationMarker pdtProductModificationMarker)
	{
		this.pdtProductModificationMarker = pdtProductModificationMarker;
	}

	public CatalogTypeService getCatalogTypeService()
	{
		return catalogTypeService;
	}

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof PDTRowModel)
		{
			final PDTRowModel pdtModel = (PDTRowModel) model;

			if (ctx.isNew(model))
			{
				updateCatalogVersion(pdtModel);
			}
			handleProductAndProductGroup(pdtModel, ctx);
			updateProductMatchQualifier(pdtModel, ctx);

			handleUserAndUserGroup(pdtModel, ctx);
			updateUserMatchQualifier(pdtModel, ctx);
			pdtProductModificationMarker.markProductAsModifiedIfFlagSet(pdtModel, ctx);
		}
	}

	protected void updateProductMatchQualifier(final PDTRowModel prModel, final InterceptorContext ctx) throws InterceptorException
	{
		// if product or productGroup has been changed - productMatchQualifier has to be updated as well
		if (ctx.isNew(prModel) || ctx.isModified(prModel, PDTRowModel.PRODUCT) || ctx.isModified(prModel, PDTRowModel.PG)
				|| ctx.isModified(prModel, PDTRowModel.PRODUCTID) || ctx.isModified(prModel, PDTRowModel.PRODUCTMATCHQUALIFIER))
		{
			final ProductModel product = prModel.getProduct();
			if (product != null)
			{
				final PK pk = ctx.isNew(product) ? getNewPKForNotSavedModel(product) : product.getPk();
				prModel.setProductMatchQualifier(Long.valueOf(pk.getLongValue()));
			}
			else if (prModel.getPg() != null)
			{
				try
				{
					final EnumerationValueModel pgModel = typeService.getEnumerationValue(prModel.getPg().getType(),
							prModel.getPg().getCode());
					prModel.setProductMatchQualifier(Long.valueOf(pgModel.getPk().getLongValue()));
				}
				catch (final UnknownIdentifierException e)
				{
					LOG.error("The Enumeration Value " + prModel.getPg().getCode()
							+ " for Product Group does NOT exist yet. Please save it before separately");
					throw new InterceptorException("The new EnumerationValue " + prModel.getPg().getCode() + " for Product Group "
							+ "cannot be created together with the new PDTRowModel. Please save it before, " + ""
							+ "in  separate transaction");
				}
			}
			else if (prModel.getProductId() != null)
			{
				prModel.setProductMatchQualifier(Long.valueOf(Europe1PriceFactory.MATCH_BY_PRODUCT_ID));
			}
			else
			{
				prModel.setProductMatchQualifier(Long.valueOf(Europe1PriceFactory.MATCH_ANY));
			}
		}
	}

	private PK getNewPKForNotSavedModel(final AbstractItemModel model)
	{
		final ItemModelInternalContext ictx = (ItemModelInternalContext) ModelContextUtils.getItemModelContext(model);
		final PK newPK = ictx.getNewPK();
		return newPK == null ? ictx.generateNewPK() : newPK;
	}

	protected void updateUserMatchQualifier(final PDTRowModel prModel, final InterceptorContext ctx) throws InterceptorException
	{
		if (ctx.isNew(prModel) || ctx.isModified(prModel, PDTRowModel.USER) || ctx.isModified(prModel, PDTRowModel.UG))
		{
			final UserModel user = prModel.getUser();
			if (user != null)
			{
				final PK pk = ctx.isNew(user) ? getNewPKForNotSavedModel(user) : user.getPk();
				prModel.setUserMatchQualifier(pk.getLong());
			}
			else if (prModel.getUg() != null)
			{
				try
				{
					final EnumerationValueModel ugModel = typeService.getEnumerationValue(prModel.getUg().getType(),
							prModel.getUg().getCode());
					prModel.setUserMatchQualifier(ugModel.getPk().getLong());
				}
				catch (final UnknownIdentifierException e)
				{
					LOG.error("The Enumeration Value " + prModel.getUg().getCode()
							+ " for User Group does NOT exist yet. Please save it before separately");
					throw new InterceptorException("The new EnumerationValue " + prModel.getUg().getCode() + " for User Group "
							+ "cannot be created together with the new PDTRowModel. Please save it before, " + ""
							+ "in  separate transaction");
				}
			}
			else
			{
				prModel.setUserMatchQualifier(Long.valueOf(PK.NULL_PK.getLongValue()));
			}
		}
	}



	// clear product or productGroup if necessary
	protected void handleProductAndProductGroup(final PDTRowModel model, final InterceptorContext ctx) throws InterceptorException
	{
		if (ctx.isModified(model, PDTRowModel.PRODUCT) || ctx.isModified(model, PDTRowModel.PRODUCTID)
				|| ctx.isModified(model, PDTRowModel.PG))
		{
			if (moreThanOneProductReferenceSet(model, ctx))
			{
				throw new InterceptorException("You can only set only one of the following: PRODUCT, PG, PRODUCTID");

			}
		}
	}

	private boolean moreThanOneProductReferenceSet(final PDTRowModel model, final InterceptorContext ctx)
	{
		int counter = 0;

		if (model.getProduct() != null)
		{
			counter++;
		}
		if (model.getPg() != null)
		{
			counter++;
		}
		if (model.getProductId() != null)
		{
			counter++;
		}

		return counter > 1;
	}

	// clear user or userGroup if necessary
	protected void handleUserAndUserGroup(final PDTRowModel model, final InterceptorContext ctx) throws InterceptorException
	{

		if (ctx.isModified(model, PDTRowModel.USER) || ctx.isModified(model, PDTRowModel.UG))
		{
			if (model.getUser() != null && model.getUg() != null)
			{
				throw new InterceptorException("You can only set only one of the following: USER, UG");
			}
		}

	}

	protected abstract void updateCatalogVersion(final PDTRowModel pdtModel);

}
