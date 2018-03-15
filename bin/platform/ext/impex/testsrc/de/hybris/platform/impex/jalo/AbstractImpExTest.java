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
package de.hybris.platform.impex.jalo;

import de.hybris.platform.core.Constants;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Config;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;


@Ignore
public abstract class AbstractImpExTest extends HybrisJUnit4Test // cannot use TA due to multi-threaded cronjobs 
{
	/** Used languages. */
	protected Language german, english;
	/** Used encodings * */
	protected EnumerationValue utf8, windows1252;
	/** Used unit */
	private Unit pieces;
	private String legacyModeBackup;

	@Before
	public void initJaloItems() throws JaloSystemException
	{
		legacyModeBackup = Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY);
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "true");
		german = getOrCreateLanguage("de");
		english = getOrCreateLanguage("en");
		jaloSession.getSessionContext().setLanguage(german);
		final EnumerationType encodingEnum = EnumerationManager.getInstance().getEnumerationType(Constants.ENUMS.ENCODINGENUM);
		try
		{
			utf8 = EnumerationManager.getInstance().getEnumerationValue(encodingEnum, "utf-8");
		}
		catch (final JaloItemNotFoundException e)
		{
			try
			{
				utf8 = EnumerationManager.getInstance().createEnumerationValue(encodingEnum, "utf-8");
			}
			catch (final ConsistencyCheckException cce)
			{
				throw new JaloSystemException(cce);
			}
		}
		try
		{
			windows1252 = EnumerationManager.getInstance().getEnumerationValue(encodingEnum, "windows-1252");
		}
		catch (final JaloItemNotFoundException e)
		{
			try
			{
				windows1252 = EnumerationManager.getInstance().createEnumerationValue(encodingEnum, "windows-1252");
			}
			catch (final ConsistencyCheckException cce)
			{
				throw new JaloSystemException(cce);
			}
		}
		pieces = ProductManager.getInstance().getUnit("pieces");
		if (pieces == null)
		{
			pieces = ProductManager.getInstance().createUnit("piecesType", "pieces");
		}
	}

	@After
	public void setLegacyMode()
	{
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, legacyModeBackup);
	}
}
