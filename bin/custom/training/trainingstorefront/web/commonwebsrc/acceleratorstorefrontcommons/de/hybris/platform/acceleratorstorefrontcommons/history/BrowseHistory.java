/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.history;

/**
 */
public interface BrowseHistory
{
	/**
	 * Adds the url to the browsing history.
	 *
	 * @param browseHistoryEntry the {@link BrowseHistoryEntry} that will be added to the history
	 */
	void addBrowseHistoryEntry(BrowseHistoryEntry browseHistoryEntry);
	
	BrowseHistoryEntry findEntryMatchUrlEndsWith(String match);
}
