package de.hybris.platform.order.impl;


import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Integration test for {@link DefaultEntryGroupService}
 */
@IntegrationTest
public class DefaultEntryGroupServiceIntegrationTest extends ServicelayerTest
{
	@Resource
	private DefaultEntryGroupService defaultEntryGroupService;

	@Resource
	private ModelService modelService;
	@Resource
	UserService userService;
	@Resource
	private CommonI18NService commonI18NService;

	private CartModel cart;
	private UserModel user;
	private CurrencyModel currency;

	@Before
	public void setUp() throws Exception
	{
		user = userService.getCurrentUser();
		currency = commonI18NService.getCurrentCurrency();
		cart = modelService.create(CartModel.class);
		cart.setCurrency(currency);
		cart.setDate(new Date());
		cart.setUser(user);
		cart.setCode("EntryGroupCart");
		modelService.save(cart);
	}

	@Test
	public void testForceOrderSaving()
	{
		final EntryGroup rootGroup = entryGroup(1);
		final EntryGroup entryGroup2 = entryGroup(2);
		final EntryGroup entryGroup3 = entryGroup(3);
		final EntryGroup entryGroup4 = entryGroup(4);
		final EntryGroup entryGroup5 = entryGroup(5);

		final List<EntryGroup> entryGroups = Arrays.asList(rootGroup, entryGroup2, entryGroup3, entryGroup4, entryGroup5);
		rootGroup.setChildren(Arrays.asList(entryGroup2, entryGroup5));
		entryGroup2.setChildren(Arrays.asList(entryGroup3, entryGroup4));

		cart.setEntryGroups(Arrays.asList(entryGroups.get(0)));
		modelService.save(cart);

		// update entry group in the cart
		final EntryGroup groupToUpdate = defaultEntryGroupService.getGroup(cart, 2);
		groupToUpdate.setErroneous(Boolean.TRUE);

		defaultEntryGroupService.forceOrderSaving(cart);

		modelService.refresh(cart);
		assertTrue(defaultEntryGroupService.getGroup(cart, 2).getErroneous());
	}

	protected EntryGroup entryGroup(final Integer number)
	{
		final EntryGroup result = new EntryGroup();
		result.setGroupNumber(number);
		result.setErroneous(Boolean.FALSE);
		return result;
	}
}
