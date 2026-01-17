package groupe1.il3.framework.di.container;

import groupe1.il3.framework.di.provider.ServiceProvider;

import java.util.HashMap;
import java.util.Map;

public final class SimpleDiContainer implements DependencyInjectionContainer {
    private final Map<Class<?>, Object> instances;

    public SimpleDiContainer()
    {
        this.instances = new HashMap<>();
    }

    @Override
    public <T> void register(Class<T> type, T instance) {
        instances.put(type, instance);
    }

    @Override
    public void register(ServiceProvider provider) {
        provider.register(this);
    }

    @Override
    public <T> T resolve(Class<T> type) {
        T instance = (T) instances.get(type);

        if (instance == null) {
            throw new IllegalArgumentException("No registered instance found for type: " + type.getName());
        }

        return instance;
    }
}
