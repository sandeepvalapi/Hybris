/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.stocknotificationaddon.forms;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */

public class StockNotificationForm
{

	private List<NotificationChannelForm> channels = new ArrayList<>();

	public List<NotificationChannelForm> getChannels()
	{
		return channels;
	}

	public void setChannels(final List<NotificationChannelForm> channels)
	{
		this.channels = channels;
	}

}
