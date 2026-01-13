package groupe1.il3.framework.di.container;

import groupe1.il3.framework.di.provider.ServiceProvider;

public interface DependencyInjectionContainer {
    <T> void register(Class<T> type, T instance);

    void register(ServiceProvider provider);

    <T> T resolve(Class<T> type);
}
