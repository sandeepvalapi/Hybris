import de.hybris.platform.core.Registry
import de.hybris.platform.core.model.user.TitleModel
import de.hybris.platform.scripting.events.TestScriptingEvent
import de.hybris.platform.servicelayer.event.events.AbstractEvent
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener

class MyScriptingEventListener extends AbstractEventListener<AbstractEvent> {

    @Override
    void onEvent(AbstractEvent event) {
        if (event instanceof TestScriptingEvent) {
            println 'hello groovy! ' + new Date();
            def modelService = Registry.getApplicationContext().getBean("modelService")
            def title = modelService.create(TitleModel.class)
            title.code = ((TestScriptingEvent) event).getEventName()
            title.pk.longValue //this should throw NullPointerException, which is expected here!
            modelService.save(title)

        } else {
            println 'another event published '
            println event
        }
    }
}

new MyScriptingEventListener();