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
package de.hybris.platform.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.util.BridgeAbstraction;
import de.hybris.platform.util.BridgeInterface;
import de.hybris.platform.util.JaloObjectCreator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.google.common.base.Preconditions;


/**
 * The basic service for managing a set of jalo implementations. A program can explicitly load jalo-implementations at
 * any time. For example, the my.shoplogic.EJBImplementation is loaded with the following statement:
 * 
 * <pre>
 * Class.forName(my.shoplogic.EJBImplementation.class.getName());
 * </pre>
 * 
 * When the method getConnection is called, the ImplementationManager will attempt to locate a suitable implementation
 * from amongst those loaded at initialization and those loaded explicitly using the same classloader as the current
 * applet or application.
 * 
 */
abstract class NonCacheJaloImplementationManager
{
	private static final Map mappings = new HashMap();

	private static String getClassName(final BridgeInterface impl)
	{
		return impl.getJaloObjectClass().getName();
	}

	static final BridgeAbstraction createJaloObject(final Tenant tenant, final BridgeInterface impl)
	{
		final BridgeAbstraction ret;
		final ApplicationContext actx = Registry.getGlobalApplicationContext();

		final String clname = getClassName(impl);

		if (actx.containsBean(clname))
		{
			ret = (BridgeAbstraction) actx.getBean(clname);
		}
		else
		{
			Class cl = impl.getJaloObjectClass();
			Preconditions.checkArgument(BridgeAbstraction.class.isAssignableFrom(cl));

			JaloObjectCreator creator = null;

			// check for special creator
			final Object mapped = mappings.get(cl.getName());
			if (mapped != null)
			{
				if (mapped instanceof Class)
				{
					Preconditions.checkArgument(BridgeAbstraction.class.isAssignableFrom((Class) mapped));
					cl = (Class) mapped;
				}
				else
				{
					creator = (JaloObjectCreator) mapped;
				}
			}

			try
			{
				ret = creator != null ? creator.createInstance(tenant, impl) : (BridgeAbstraction) cl.newInstance();
			}
			catch (final Exception e)
			{
				e.printStackTrace(System.err);
				final String pk = impl instanceof Item.ItemImpl ? ((Item.ItemImpl) impl).getPK().toString() : "n/a";
				throw new JaloSystemException(e, "could not create jalo object instance of " + cl.getName() + " for item " + pk
						+ " impl " + impl, 0);
			}
		}

		ret.setImplementation(impl);
		ret.setTenant(tenant);

		return ret;
	}
}
