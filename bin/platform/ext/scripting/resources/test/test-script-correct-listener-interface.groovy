import de.hybris.platform.core.Registry
import de.hybris.platform.core.model.user.TitleModel
import de.hybris.platform.scripting.events.TestScriptingEvent
import de.hybris.platform.servicelayer.event.events.AbstractEvent
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener

class MyScriptingEventListener extends AbstractEventListener<AbstractEvent> {

    @Override
    void onEvent(AbstractEvent event) {
        if (event instanceof TestScriptingEvent) {
            def modelService = Registry.getApplicationContext().getBean("modelService")
            def title = modelService.create(TitleModel.class)
            title.code = ((TestScriptingEvent) event).getEventName()
            modelService.save(title)
            println 'hello groovy! ' + new Date();
        } else {
            println 'another event published '
            println event
        }
    }
}

new MyScriptingEventListener();