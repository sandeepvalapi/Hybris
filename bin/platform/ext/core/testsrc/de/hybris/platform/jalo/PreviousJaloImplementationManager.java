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

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.persistence.ItemEJBImpl;
import de.hybris.platform.util.BridgeAbstraction;
import de.hybris.platform.util.BridgeInterface;
import de.hybris.platform.util.JaloObjectCreator;
import de.hybris.platform.util.SingletonCreator.Creator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
abstract class PreviousJaloImplementationManager
{
	private static final Map mappings = new HashMap();


	private static final Creator<Map<PK, String>> classCacheCreator = new Creator<Map<PK, String>>()
	{
		@Override
		protected Map<PK, String> create() throws Exception
		{
			return new ConcurrentHashMap<PK, String>();
		}

		@Override
		protected Object getID()
		{
			return "dummy";
		}
	};

	private static final Map<PK, String> getTypeClassCache()
	{
		return Registry.getSingleton(classCacheCreator);
	}

	private static String getCachedClassName(final BridgeInterface impl)
	{
		final PK typeKey = ((ItemEJBImpl) impl).getTypeKey(); // use cached type key

		final Map<PK, String> cache = getTypeClassCache(); // get cache map

		String ret = cache.get(typeKey); // get from cache

		if (ret == null) // miss
		{
			ret = impl.getJaloObjectClass().getName();
			cache.put(typeKey, ret);
		}
		return ret;
	}

	static final BridgeAbstraction createJaloObject(final Tenant tenant, final BridgeInterface impl)
	{
		final BridgeAbstraction ret;
		final ApplicationContext actx = Registry.getGlobalApplicationContext();

		final String clname = getCachedClassName(impl);

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
		//if( ret instanceof EventProxy ) ((EventProxy)ret).setIntercept( true );

		// done
		return ret;
	}
}
