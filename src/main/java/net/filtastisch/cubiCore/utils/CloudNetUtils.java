package net.filtastisch.cubiCore.utils;

import dev.derklaro.aerogel.Injector;
import eu.cloudnetservice.driver.event.EventManager;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.driver.network.NetworkChannel;
import eu.cloudnetservice.driver.registry.ServiceRegistry;

public class CloudNetUtils {

    public static ServiceRegistry getServiceRegistry() {
        return InjectionLayer.ext().instance(ServiceRegistry.class);
    }

    public static EventManager getEventManager() {
        return InjectionLayer.ext().instance(EventManager.class);
    }

    public static NetworkChannel getNetworkChannel() {
        return InjectionLayer.ext().instance(NetworkChannel.class);
    }

}
