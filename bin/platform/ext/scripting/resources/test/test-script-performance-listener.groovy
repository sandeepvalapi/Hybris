import de.hybris.platform.core.Registry
import de.hybris.platform.core.model.user.TitleModel
import de.hybris.platform.scripting.events.TestPerformanceEvent
import de.hybris.platform.servicelayer.event.events.AbstractEvent
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener

class MyScriptingEventListener extends AbstractEventListener<AbstractEvent> {

    @Override
    void onEvent(AbstractEvent event) {
        if (event instanceof TestPerformanceEvent) {
            def modelService = Registry.getApplicationContext().getBean("modelService")
            for (i in 0..event.itemsToSaveCount - 1) {
                def title = modelService.create(TitleModel.class)
                title.code = 'testTitle' + i
                modelService.save(title)
            }
        } else {
            Math.random();
        }
    }
}

new MyScriptingEventListener();