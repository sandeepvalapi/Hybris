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
package de.hybris.platform.servicelayer.i18n;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.c2l.RegionModel;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;



public class I18NDaoMock implements I18NDao
{
	private final Map<String, LanguageModel> languages = new ConcurrentHashMap<String, LanguageModel>();


	public I18NDaoMock()
	{
		final LanguageModel model = new LanguageModel();
		model.setIsocode("en");
		model.setActive(Boolean.TRUE);
		languages.put(model.getIsocode(), model);
	}

	@Override
	public LanguageModel findLanguage(final String isocode)
	{
		final LanguageModel model = languages.get(isocode);
		return model;
	}


	@Override
	public Set<LanguageModel> findAllActiveLanguages()
	{
		final Set<LanguageModel> models = new HashSet<LanguageModel>();
		for (final LanguageModel model : new HashSet<LanguageModel>(languages.values()))
		{
			if (Boolean.TRUE.equals(model.getActive()))
			{
				models.add(model);
			}
		}
		return Collections.unmodifiableSet(models);
	}

	@Override
	public Set<LanguageModel> findAllLanguages()
	{
		return Collections.unmodifiableSet(new HashSet<LanguageModel>(languages.values()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.servicelayer.i18n.impl.I18NDao#findAllCountries()
	 */
	@Override
	public Set<CountryModel> findAllCountries()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.servicelayer.i18n.impl.I18NDao#findAllCurrencies()
	 */
	@Override
	public Set<CurrencyModel> findAllCurrencies()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.servicelayer.i18n.impl.I18NDao#findAllRegions()
	 */
	@Override
	public Set<RegionModel> findAllRegions()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.servicelayer.i18n.impl.I18NDao#findBaseCurrency()
	 */
	@Override
	public CurrencyModel findBaseCurrency()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.servicelayer.i18n.impl.I18NDao#findCountry(java.lang.String)
	 */
	@Override
	public CountryModel findCountry(final String isocode)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.servicelayer.i18n.impl.I18NDao#findCurrency(java.lang.String)
	 */
	@Override
	public CurrencyModel findCurrency(final String isocode)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.servicelayer.i18n.impl.I18NDao#findRegion(java.lang.String)
	 */
	@Override
	public RegionModel findRegion(final String code)
	{
		throw new UnsupportedOperationException();
	}
}
