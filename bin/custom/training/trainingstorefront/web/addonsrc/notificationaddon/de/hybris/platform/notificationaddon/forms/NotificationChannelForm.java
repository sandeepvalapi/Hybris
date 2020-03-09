/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.notificationaddon.forms;

import de.hybris.platform.notificationfacades.data.NotificationPreferenceData;

import java.util.List;


public class NotificationChannelForm
{
	private List<NotificationPreferenceData> channels;

	public List<NotificationPreferenceData> getChannels()
	{
		return channels;
	}

	public void setChannels(final List<NotificationPreferenceData> channels)
	{
		this.channels = channels;
	}


}
