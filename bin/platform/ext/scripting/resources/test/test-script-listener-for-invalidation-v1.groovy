/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */

package test

import de.hybris.platform.scripting.events.TestScriptingEvent
import de.hybris.platform.servicelayer.event.events.AbstractEvent
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener
import de.hybris.platform.scripting.events.TestSingleton

class MyScriptingEventListener extends AbstractEventListener<AbstractEvent> {

    @Override
    void onEvent(AbstractEvent event) {
        if (event instanceof TestScriptingEvent) {
            TestSingleton.value = 1
        }
    }
}

new MyScriptingEventListener();