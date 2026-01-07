package net.filtastisch.cubiCore.utils;

import dev.derklaro.aerogel.Injector;
import eu.cloudnetservice.driver.event.EventManager;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.driver.network.NetworkChannel;
import eu.cloudnetservice.driver.registry.ServiceRegistry;

public class CloudNetUtils {

    public static ServiceRegistry getServiceRegistry() {
        try (InjectionLayer<Injector> layer = InjectionLayer.ext()){
            return layer.instance(ServiceRegistry.class);
        }
    }

    public static EventManager getEventManager() {
        try (InjectionLayer<Injector> layer = InjectionLayer.ext()){
            return layer.instance(EventManager.class);
        }
    }

    public static NetworkChannel getNetworkChannel() {
        try (InjectionLayer<Injector> layer = InjectionLayer.ext()){
            return layer.instance(NetworkChannel.class);
        }
    }

}
