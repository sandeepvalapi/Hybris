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
package de.hybris.platform.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.order.CartFactory;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.strategies.AddToCartStrategy;
import de.hybris.platform.order.strategies.CreateCartFromQuoteStrategy;
import de.hybris.platform.order.strategies.OrderCalculation;
import de.hybris.platform.servicelayer.session.SessionService.SessionAttributeLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link CartService}
 */
public class DefaultCartService extends DefaultAbstractOrderService<CartModel, CartEntryModel> implements CartService
{
	private static final Logger LOG = Logger.getLogger(DefaultCartService.class);

	/**
	 * @deprecated since 6.2.0
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	private AddToCartStrategy addToCartStrategy;

	/**
	 * @deprecated since 6.2.0
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	private OrderCalculation orderCalculation;

	private CartFactory cartFactory;
	private CreateCartFromQuoteStrategy createCartFromQuoteStrategy;

	public static final String SESSION_CART_PARAMETER_NAME = "cart";

	/**
	 * @deprecated since 6.2.0
	 */
	@SuppressWarnings("deprecation")
	@Override
	@Deprecated
	public void addToCart(final CartModel cartModel, final ProductModel productModel, final long quantity,
			final UnitModel unitModel) throws InvalidCartException
	{
		addToCartStrategy.addToCart(cartModel, productModel, quantity, unitModel);
		calculateCart(cartModel);
	}

	@Override
	public boolean hasSessionCart()
	{
		try
		{
			return getSessionService().getAttribute(SESSION_CART_PARAMETER_NAME) != null;
		}
		catch (final JaloObjectNoLongerValidException ex)
		{
			if (LOG.isInfoEnabled())
			{
				LOG.info("Session Cart no longer valid. Removing from session. hasSessionCart will return false. " + ex.getMessage());
			}
			getSessionService().removeAttribute(SESSION_CART_PARAMETER_NAME);
			return false;
		}
	}

	/**
	 * @deprecated since 6.2.0
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public boolean hasCart()
	{
		return hasSessionCart();
	}

	@Override
	public void setSessionCart(final CartModel cart)
	{
		if (cart == null)
		{
			removeSessionCart();
		}
		else
		{
			getSessionService().setAttribute(SESSION_CART_PARAMETER_NAME, cart);
		}
	}

	@Override
	public CartModel getSessionCart()
	{
		try
		{
			return internalGetSessionCart();
		}
		catch (final JaloObjectNoLongerValidException ex)
		{
			if (LOG.isInfoEnabled())
			{
				LOG.info("Session Cart no longer valid. Removing from session. getSessionCart will create a new cart. "
						+ ex.getMessage());
			}
			getSessionService().removeAttribute(SESSION_CART_PARAMETER_NAME);
			return internalGetSessionCart();
		}
	}

	protected CartModel internalGetSessionCart()
	{
		final CartModel cart = getSessionService().getOrLoadAttribute(SESSION_CART_PARAMETER_NAME,
				new SessionAttributeLoader<CartModel>()
				{
					@Override
					public CartModel load()
					{
						return cartFactory.createCart();
					}
				});
		return cart;
	}

	/**
	 * @deprecated since 6.2.0
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public boolean calculateCart(final CartModel cartModel)
	{
		return orderCalculation.calculate(cartModel);
	}


	/**
	 * @deprecated since 6.2.0
	 */
	@SuppressWarnings("deprecation")
	@Deprecated
	@Override
	public void updateQuantities(final CartModel cart, final List<Long> quantities)
	{
		if (cart == null)
		{
			throw new IllegalArgumentException("cart was null");
		}

		final List<AbstractOrderEntryModel> entries = cart.getEntries();
		if (entries.size() < quantities.size())
		{
			throw new IllegalArgumentException(
					"got less entries than quantities (" + entries.size() + " < " + quantities.size() + ")");
		}

		int pos = 0;
		for (final AbstractOrderEntryModel entry : entries)
		{
			final Long value = quantities.get(pos++);
			if (value == null || value.longValue() < 1)
			{
				getModelService().remove(entry);
			}
			else
			{
				entry.setQuantity(value);
				getModelService().save(entry);
			}
		}
	}

	@Override
	public void updateQuantities(final CartModel cart, final Map<Integer, Long> quantities)
	{
		if (cart == null)
		{
			throw new IllegalArgumentException("cart cannot be null");
		}
		if (!MapUtils.isEmpty(quantities))
		{
			final Collection<CartEntryModel> toRemove = new LinkedList<CartEntryModel>();
			final Collection<CartEntryModel> toSave = new LinkedList<CartEntryModel>();
			for (final Map.Entry<CartEntryModel, Long> e : getEntryQuantityMap(cart, quantities).entrySet())
			{
				final CartEntryModel cartEntry = e.getKey();
				final Long quantity = e.getValue();
				if (quantity == null || quantity.longValue() < 1)
				{
					toRemove.add(cartEntry);
				}
				else
				{
					cartEntry.setQuantity(quantity);
					toSave.add(cartEntry);
				}
			}
			getModelService().removeAll(toRemove);
			getModelService().saveAll(toSave);
			getModelService().refresh(cart);
		}
	}

	private Map<CartEntryModel, Long> getEntryQuantityMap(final CartModel cart, final Map<Integer, Long> quantities)
	{
		final List<CartEntryModel> entries = (List) cart.getEntries();

		final Map<CartEntryModel, Long> ret = new LinkedHashMap<CartEntryModel, Long>();

		for (final Map.Entry<Integer, Long> q : quantities.entrySet())
		{
			final Integer entryNumber = q.getKey();
			final Long quantity = q.getValue();
			ret.put(getEntry(entries, entryNumber), quantity);
		}

		return ret;
	}

	private CartEntryModel getEntry(final List<CartEntryModel> entries, final Integer entryNumber)
	{
		for (final CartEntryModel e : entries)
		{
			if (entryNumber.equals(e.getEntryNumber()))
			{
				return e;
			}
		}
		throw new IllegalArgumentException("no cart entry found with entry number " + entryNumber + " (got " + entries + ")");
	}


	@Override
	public void removeSessionCart()
	{
		if (hasSessionCart())
		{
			final CartModel sessionCart = getSessionCart();
			getModelService().remove(sessionCart);
			getSessionService().removeAttribute(SESSION_CART_PARAMETER_NAME);
		}
	}


	@Override
	public CartModel clone(final ComposedTypeModel orderType, final ComposedTypeModel entryType, final AbstractOrderModel original,
			final String code)
	{

		return (CartModel) getCloneAbstractOrderStrategy().clone(orderType, entryType, original, code, CartModel.class,
				CartEntryModel.class);
	}

	@Override
	public void appendToCart(final CartModel sourceCart, final CartModel targetCart)
	{
		validateParameterNotNull(targetCart, "targetCart must not be null!");
		final int lastEntryNo = getNextEntryNumber(targetCart, null);
		//clone source entries

		final Collection<CartEntryModel> sourceClones = getCloneAbstractOrderStrategy()
				.cloneEntries(getAbstractOrderEntryTypeService().getAbstractOrderEntryType(targetCart), sourceCart);
		postProcessClonedEntries(sourceClones, lastEntryNo, targetCart);

		//add clones to target cart
		final List<AbstractOrderEntryModel> targetEntries = new ArrayList<AbstractOrderEntryModel>(targetCart.getEntries());
		targetEntries.addAll(sourceClones);
		targetCart.setEntries(targetEntries);
	}

	@Override
	public void changeCurrentCartUser(final UserModel user)
	{
		validateParameterNotNull(user, "user must not be null!");
		if (hasSessionCart())
		{
			final CartModel sessionCart = getSessionCart();
			sessionCart.setUser(user);
			getModelService().save(sessionCart);
		}
	}

	/**
	 * Rearrange cloned entries numbers according to last entry number from the target.
	 */
	protected void postProcessClonedEntries(final Collection<CartEntryModel> sourceClones, final int lastEntryNo,
			final CartModel targetCart)
	{
		int entryNumber = lastEntryNo;
		for (final CartEntryModel entry : sourceClones)
		{
			entry.setEntryNumber(Integer.valueOf(entryNumber++));
			entry.setOrder(targetCart);
		}
	}

	protected int getNextEntryNumber(final CartModel cart, final AbstractOrderEntryModel forMe)
	{
		final List all = cart.getEntries();
		if (all == null || all.isEmpty())
		{
			return 0;
		}
		else
		{
			final int size = all.size();
			AbstractOrderEntryModel lastOne = (AbstractOrderEntryModel) all.get(size - 1);
			if (lastOne.equals(forMe))
			{
				if (size == 1)
				{
					return 0;
				}
				else
				{
					lastOne = (AbstractOrderEntryModel) all.get(size - 2);
				}
			}

			return lastOne.getEntryNumber().intValue() + 1;
		}
	}

	@Required
	public void setAddToCartStrategy(@SuppressWarnings("deprecation") final AddToCartStrategy addToCartStrategy)
	{
		this.addToCartStrategy = addToCartStrategy;
	}

	public void setOrderCalculation(@SuppressWarnings("deprecation") final OrderCalculation orderCalculation)
	{
		this.orderCalculation = orderCalculation;
	}

	@Required
	public void setCartFactory(final CartFactory cartFactory)
	{
		this.cartFactory = cartFactory;
	}

	@Override
	public void changeSessionCartCurrency(final CurrencyModel currency)
	{
		validateParameterNotNull(currency, "currency must not be null!");
		if (hasSessionCart())
		{
			final CartModel sessionCart = getSessionCart();
			sessionCart.setCurrency(currency);
			getModelService().save(sessionCart);
		}

	}

	@Override
	public CartModel createCartFromQuote(final QuoteModel quote)
	{
		validateParameterNotNull(quote, "quote may not be null!");
		return getCreateCartFromQuoteStrategy().createCartFromQuote(quote);
	}

	protected CreateCartFromQuoteStrategy getCreateCartFromQuoteStrategy()
	{
		return createCartFromQuoteStrategy;
	}

	@Required
	public void setCreateCartFromQuoteStrategy(final CreateCartFromQuoteStrategy createCartFromQuoteStrategy)
	{
		this.createCartFromQuoteStrategy = createCartFromQuoteStrategy;
	}

}
