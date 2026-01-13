package groupe1.il3.framework.di.provider;

import groupe1.il3.framework.di.container.DependencyInjectionContainer;

public interface ServiceProvider {
    void register(DependencyInjectionContainer container);
}
