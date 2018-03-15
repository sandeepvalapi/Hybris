import de.hybris.platform.servicelayer.tenant.TenantService

class Foo implements TenantService {

    def id

    @Override
    String getCurrentTenantId() {
        "fakeTenant ${id}"
    }
}

new Foo(id: id)
