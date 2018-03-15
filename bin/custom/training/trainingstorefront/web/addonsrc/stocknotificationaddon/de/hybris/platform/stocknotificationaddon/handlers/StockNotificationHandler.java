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
package de.hybris.platform.stocknotificationaddon.handlers;

import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestData;
import de.hybris.platform.notificationfacades.data.NotificationPreferenceData;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.stocknotificationaddon.forms.StockNotificationForm;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;



@Component("stockNotificationHandler")
public class StockNotificationHandler
{
	@Resource(name = "i18NFacade")
	protected I18NFacade i18NFacade;

	public void prepareInterestData(final StockNotificationForm stockNotificationForm,
			ProductInterestData productInterestData)
	{
		productInterestData.setEmailNotificationEnabled(stockNotificationForm.isEmailNotificationEnabled());
		productInterestData.setSmsNotificationEnabled(stockNotificationForm.isSmsNotificationEnabled());
		productInterestData.setNotificationType(NotificationType.BACK_IN_STOCK);
	}

	public StockNotificationForm prepareStockNotifcationFormByInterest(ProductInterestData productInterestData)
	{
		StockNotificationForm stockNotificationForm = new StockNotificationForm();
		BeanUtils.copyProperties(productInterestData, stockNotificationForm);
		return stockNotificationForm;
	}

	public StockNotificationForm prepareStockNotifcationFormByCustomer(NotificationPreferenceData notificationPreferenceData)
	{
		StockNotificationForm stockNotificationForm = new StockNotificationForm();
		stockNotificationForm.setEmailAddress(notificationPreferenceData.getEmailAddress());
		stockNotificationForm.setEmailNotificationEnabled(notificationPreferenceData.isEmailEnabled());
		stockNotificationForm.setMobileNumber(notificationPreferenceData.getMobileNumber());
		stockNotificationForm.setSmsNotificationEnabled(notificationPreferenceData.isSmsEnabled());
		return stockNotificationForm;
	}

}
