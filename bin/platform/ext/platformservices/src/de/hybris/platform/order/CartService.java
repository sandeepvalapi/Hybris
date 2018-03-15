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
package de.hybris.platform.order;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.impl.DefaultCartFactory;
import de.hybris.platform.product.ProductService;

import java.util.List;
import java.util.Map;


/**
 * Service providing cart oriented functionality. On contrary to order, cart does not represent a legal contract with
 * the customer, but similarly to {@link OrderModel}, {@link CartModel} is a subtype of {@link AbstractOrderModel}, so
 * the service fulfills the contract of {@link AbstractOrderService} for the {@link CartModel} and
 * {@link CartEntryModel} types. Additionally it provides methods for handling the session cart.
 *
 * @spring.bean cartService
 */
public interface CartService extends AbstractOrderService<CartModel, CartEntryModel>
{
	/**
	 * Adds to the (existing) {@link CartModel} the (existing) {@link ProductModel} in the given {@link UnitModel} and
	 * with the given <code>quantity</code>. If in the cart already an entry with the given product and given unit exists
	 * the given <code>quantity</code> is added to the the quantity of this cart entry. After this the cart is
	 * calculated.
	 *
	 * @param cart
	 *           the cart, must exist
	 * @param product
	 *           the product which is added to the cart
	 * @param quantity
	 *           the quantity of the product
	 * @param unit
	 *           if <code>null</code> {@link ProductService#getOrderableUnit(ProductModel)} is used to determine the unit
	 * @throws InvalidCartException
	 *            if the <code>product</code> is a base product OR the quantity is less 1 or no usable unit was found
	 *            (only when given <code>unit</code> is also <code>null</code>)
	 *
	 * @deprecated since 6.2.0 - Use{@link AbstractOrderService#addNewEntry(AbstractOrderModel, ProductModel, long, UnitModel)} from
	 *             the cart service instead. Please mind the fact that
	 *             {@link AbstractOrderService#addNewEntry(AbstractOrderModel, ProductModel, long, UnitModel)} method
	 *             does not calculate or save the cart.
	 */
	@Deprecated
	void addToCart(CartModel cart, ProductModel product, long quantity, UnitModel unit) throws InvalidCartException;


	/**
	 * Attach the cart to a session. Please not that a previously existing session cart is being detached and remove
	 * during that operation. It will no longer be available afterwards.
	 */
	void setSessionCart(CartModel cart);

	/**
	 * Returns the cart attached to the current session. The session cart is, just like current user, current currency or
	 * language, a part of session data that lives in the session scope.
	 *
	 * If there is no cart attached to the current session yet, this method will automatically create a new cart and
	 * attaches it to the current session.
	 *
	 * The creation process is performed by the injected {@link CartFactory}. As default a {@link DefaultCartFactory} is
	 * being used.
	 */
	CartModel getSessionCart();

	/**
	 * If existing the current session cart is being detached from this session and removed. Afterwards it's no longer
	 * available.
	 *
	 * @see #getSessionCart()
	 * @see #setSessionCart(CartModel)
	 */
	void removeSessionCart();

	/**
	 * Returns <code>true</code> if the current session already holds a cart, <code>false</code> otherwise. Please use
	 * this instead of {@link #getSessionCart()} if you like to avoid unnecessary auto-creation of the session cart.
	 *
	 * @see #getSessionCart()
	 */
	boolean hasSessionCart();

	/**
	 * @deprecated since 6.2.0 - use{@link #hasSessionCart()}
	 */
	@Deprecated
	boolean hasCart();

	/**
	 * Calculates the given <code>cartModel</code> and returns <code>true</code> if each entry and after this the
	 * {@link CartModel} was calculated. Thereby any invalid entry will be automatically removed.
	 *
	 * @param cartModel
	 *           the {@link CartModel}
	 * @return <code>false</code> if the <code>cartModel</code> was already calculated.
	 *
	 * @deprecated since 6.2.0 - Use{@link CalculationService#calculate(AbstractOrderModel)} to calculate carts.
	 */
	@Deprecated
	boolean calculateCart(CartModel cartModel);

	/**
	 * Updates the quantities of each cart entry with the list of <code>quantities</code>. If the quantity value is 0 the
	 * cart entry is removed.
	 *
	 * @param cart
	 *           the cart where the quantities are updated
	 * @param quantities
	 *           the new quantity list for each cart entry
	 * @deprecated since 6.2.0 - Update cart entries quantities directly via{@link CartEntryModel#setQuantity(Long)} or use
	 *             {@link #updateQuantities(CartModel, Map)}
	 */
	@Deprecated
	void updateQuantities(CartModel cart, List<Long> quantities);

	/**
	 * Updates multiple cart entry quantities at once. Entries that receive a quantity &lt; 1 will be removed as well as
	 * entries that receive NULL as quantity value. Refreshes a given cart instance after that.
	 *
	 * Entries with entry numbers that do not occur in the parameter map are not touched.
	 *
	 * @param cart
	 *           the cart to update cart entry quantities at
	 * @param quantities
	 *           the entry specific quantities as map of { entry number -> quantity }
	 */
	void updateQuantities(CartModel cart, Map<Integer, Long> quantities);


	/**
	 * Clones all entries of source cart to the target cart. After this call, the target cart needs recalculation. To do
	 * it, use {@link CalculationService}. The target cart is also not persisted after this method call.
	 *
	 * @param sourceCart
	 *           the cart providing the entries to copy
	 * @param targetCart
	 *           the target cart to create new entries for
	 *
	 * @throws IllegalArgumentException
	 *            if targetCart is null.
	 */
	void appendToCart(final CartModel sourceCart, final CartModel targetCart);

	/**
	 * Changes the user of the session cart to the given user. Please mind that the session cart may need recalculation
	 * after this call, as the price discounts may be user dependent. In case there is no session cart at the time of
	 * this method call, one will be created.
	 *
	 * @param user
	 *           new session cart user
	 * @throws IllegalArgumentException
	 *            when user is null
	 */
	void changeCurrentCartUser(final UserModel user);

	/**
	 * Changes the currency of the session cart to the given currency. Please mind that the session cart need
	 * recalculation after this call. In case there is no session cart at the time of this method call, one will be
	 * created.
	 *
	 * @param currency
	 *           new session cart currency
	 */
	void changeSessionCartCurrency(CurrencyModel currency);

	/**
	 * Creates a new cart based on the given {@link QuoteModel}. Please note that it is the caller's responsibility to
	 * persist the new cart returned by this method. The quote passed into this method will not be affected by its logic.
	 *
	 * @param quote
	 *           the quote model
	 * @return the new cart model
	 * @throws IllegalArgumentException
	 *            when quote is null
	 */
	CartModel createCartFromQuote(final QuoteModel quote);

}
