/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.stocknotificationaddon.handlers;

import de.hybris.platform.customerinterestsfacades.data.ProductInterestData;
import de.hybris.platform.notificationfacades.data.NotificationPreferenceData;
import de.hybris.platform.notificationservices.enums.NotificationChannel;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.stocknotificationaddon.forms.NotificationChannelForm;
import de.hybris.platform.stocknotificationaddon.forms.StockNotificationForm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;



@Component("stockNotificationHandler")
public class StockNotificationHandler
{

	public void prepareInterestData(final StockNotificationForm stockNotificationForm,
			final ProductInterestData productInterestData)
	{
		final List<NotificationPreferenceData> enabledChannels = new ArrayList<>();
		stockNotificationForm.getChannels().forEach(c -> {
			final NotificationPreferenceData channel = new NotificationPreferenceData();
			channel.setChannel(NotificationChannel.valueOf(c.getChannel()));
			channel.setEnabled(c.isEnabled());
			channel.setVisible(c.isVisible());
			enabledChannels.add(channel);
		});

		productInterestData.setNotificationChannels(enabledChannels);
		productInterestData.setNotificationType(NotificationType.BACK_IN_STOCK);
	}

	public StockNotificationForm prepareStockNotifcationFormByInterest(final ProductInterestData productInterestData)
	{
		final StockNotificationForm stockNotificationForm = new StockNotificationForm();
		final List<NotificationChannelForm> channels = new ArrayList<>();

		productInterestData.getNotificationChannels().forEach(c -> {
			final NotificationChannelForm channel = new NotificationChannelForm();
			channel.setChannel(c.getChannel().getCode());
			channel.setValue(c.getValue());
			channel.setEnabled(c.isEnabled());
			channel.setVisible(c.isVisible());

			channels.add(channel);
		});
		stockNotificationForm.setChannels(channels);

		return stockNotificationForm;
	}

	public StockNotificationForm prepareStockNotifcationFormByCustomer(final List<NotificationPreferenceData> preferences)
	{
		final StockNotificationForm stockNotificationForm = new StockNotificationForm();
		final List<NotificationChannelForm> channels = new ArrayList<>();

		preferences.forEach(c -> {
			final NotificationChannelForm channel = new NotificationChannelForm();
			channel.setChannel(c.getChannel().getCode());
			channel.setValue(c.getValue());
			channel.setEnabled(c.isEnabled());
			channel.setVisible(c.isVisible());
			channels.add(channel);
		});
		stockNotificationForm.setChannels(channels);

		return stockNotificationForm;
	}

}
