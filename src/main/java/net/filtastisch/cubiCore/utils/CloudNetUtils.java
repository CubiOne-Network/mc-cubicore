package net.filtastisch.cubiCore.utils;

import eu.cloudnetservice.driver.event.EventManager;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.driver.provider.CloudServiceProvider;
import eu.cloudnetservice.driver.provider.ServiceTaskProvider;
import eu.cloudnetservice.driver.provider.SpecificCloudServiceProvider;
import eu.cloudnetservice.driver.registry.ServiceRegistry;
import eu.cloudnetservice.driver.service.ServiceInfoSnapshot;
import eu.cloudnetservice.driver.service.ServiceTask;
import eu.cloudnetservice.modules.bridge.BridgeDocProperties;
import eu.cloudnetservice.modules.bridge.BridgeServiceHelper;
import eu.cloudnetservice.modules.bridge.player.CloudOfflinePlayer;
import eu.cloudnetservice.modules.bridge.player.CloudPlayer;
import eu.cloudnetservice.modules.bridge.player.PlayerManager;
import eu.cloudnetservice.modules.bridge.player.ServicePlayer;
import eu.cloudnetservice.modules.bridge.player.executor.PlayerExecutor;
import eu.cloudnetservice.modules.bridge.player.executor.ServerSelectorType;
import eu.cloudnetservice.wrapper.configuration.WrapperConfiguration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Utility class for CloudNet v4 operations.
 * Provides simple access methods for common CloudNet functions.
 */
public class CloudNetUtils {

    /**
     * Returns the CloudServiceProvider.
     * @return CloudServiceProvider instance
     */
    public static CloudServiceProvider getCloudServiceProvider() {
        return InjectionLayer.ext().instance(CloudServiceProvider.class);
    }

    /**
     * Returns the PlayerManager.
     * @return PlayerManager instance
     */
    public static PlayerManager getCloudPlayerManager() {
        return getServiceRegistry().defaultInstance(PlayerManager.class);
    }

    /**
     * Returns the BridgeServiceHelper.
     * @return BridgeServiceHelper instance
     */
    public static BridgeServiceHelper getBridgeServiceHelper() {
        return InjectionLayer.ext().instance(BridgeServiceHelper.class);
    }

    /**
     * Returns the ServiceTaskProvider.
     * @return ServiceTaskProvider instance
     */
    public static ServiceTaskProvider getCloudServiceTaskProvider() {
        return InjectionLayer.ext().instance(ServiceTaskProvider.class);
    }

    /**
     * Returns the WrapperConfiguration.
     * @return WrapperConfiguration instance
     */
    public static WrapperConfiguration getWrapperConfiguration() {
        return InjectionLayer.ext().instance(WrapperConfiguration.class);
    }

    /**
     * Returns the ServiceRegistry.
     * @return ServiceRegistry instance
     */
    public static ServiceRegistry getServiceRegistry() {
        return InjectionLayer.ext().instance(ServiceRegistry.class);
    }

    /**
     * Returns the EventManager.
     * @return EventManager instance
     */

    public static EventManager getEventManager() {
        return InjectionLayer.ext().instance(EventManager.class);
    }

    /**
     * Returns the total online player count across the network.
     * @return online player count
     */
    public static int getGlobalOnlineCount() {
        return getCloudPlayerManager().onlineCount();
    }

    /**
     * Returns the total online player count asynchronously.
     * @return CompletableFuture with online player count
     */
    public static CompletableFuture<Integer> getGlobalOnlineCountAsync() {
        return getCloudPlayerManager().onlineCountAsync();
    }

    /**
     * Returns the total registered player count from the database.
     * @return registered player count
     */
    public static long getRegisteredPlayerCount() {
        return getCloudPlayerManager().registeredCount();
    }

    /**
     * Returns an online player by UUID.
     * @param uuid player UUID
     * @return CloudPlayer or null if not online
     */
    public static CloudPlayer getOnlinePlayer(UUID uuid) {
        return getCloudPlayerManager().onlinePlayer(uuid);
    }

    /**
     * Returns an online player by name.
     * @param name player name
     * @return CloudPlayer or null if not online
     */
    public static CloudPlayer getOnlinePlayerByName(String name) {
        return getCloudPlayerManager().firstOnlinePlayer(name);
    }

    /**
     * Returns all online players with a specific name.
     * @param name player name
     * @return list of CloudPlayers
     */
    public static List<CloudPlayer> getOnlinePlayersByName(String name) {
        return getCloudPlayerManager().onlinePlayers(name);
    }

    /**
     * Checks if a player is online.
     * @param uuid player UUID
     * @return true if online, false otherwise
     */
    public static boolean isPlayerOnline(UUID uuid) {
        return getCloudPlayerManager().onlinePlayer(uuid) != null;
    }

    /**
     * Returns the service name a player is connected to.
     * @param uuid player UUID
     * @return service name or null if not online
     */
    public static String getPlayerConnectedService(UUID uuid) {
        CloudPlayer player = getCloudPlayerManager().onlinePlayer(uuid);
        if (player == null) return null;
        return Objects.requireNonNull(player.connectedService()).serverName();
    }

    /**
     * Returns an offline player by UUID.
     * @param uuid player UUID
     * @return CloudOfflinePlayer or null if not found
     */
    public static CloudOfflinePlayer getOfflinePlayer(UUID uuid) {
        return getCloudPlayerManager().offlinePlayer(uuid);
    }

    /**
     * Returns an offline player by name.
     * @param name player name
     * @return CloudOfflinePlayer or null if not found
     */
    public static CloudOfflinePlayer getOfflinePlayerByName(String name) {
        return getCloudPlayerManager().firstOfflinePlayer(name);
    }

    /**
     * Returns all offline players with a specific name.
     * @param name player name
     * @return list of CloudOfflinePlayers
     */
    public static List<CloudOfflinePlayer> getOfflinePlayersByName(String name) {
        return getCloudPlayerManager().offlinePlayers(name);
    }

    /**
     * Returns the PlayerExecutor for a specific player.
     * @param uuid player UUID
     * @return PlayerExecutor for the player
     */
    public static PlayerExecutor getPlayerExecutor(UUID uuid) {
        return getCloudPlayerManager().playerExecutor(uuid);
    }

    /**
     * Returns the global PlayerExecutor for all players.
     * @return global PlayerExecutor
     */
    public static PlayerExecutor getGlobalPlayerExecutor() {
        return getCloudPlayerManager().globalPlayerExecutor();
    }

    /**
     * Connects a player to a specific service.
     * @param playerUUID player UUID
     * @param targetServiceName target service name
     */
    public static void connectPlayerToService(UUID playerUUID, String targetServiceName) {
        PlayerManager playerManager = getCloudPlayerManager();
        CloudPlayer cloudPlayer = playerManager.onlinePlayer(playerUUID);
        if (cloudPlayer != null) {
            ServiceInfoSnapshot targetService = getCloudServiceProvider().serviceByName(targetServiceName);
            if (targetService != null) {
                playerManager.playerExecutor(playerUUID).connect(targetServiceName);
            } else {
                System.out.println("Service " + targetServiceName + " wurde nicht gefunden.");
            }
        } else {
            System.out.println("Spieler ist nicht online oder konnte nicht gefunden werden.");
        }
    }

    /**
     * Connects a player to a specific service.
     * @param playerUUID player UUID
     * @param targetService target ServiceInfoSnapshot
     */
    public static void connectPlayerToService(UUID playerUUID, ServiceInfoSnapshot targetService) {
        PlayerManager playerManager = getCloudPlayerManager();
        CloudPlayer cloudPlayer = playerManager.onlinePlayer(playerUUID);
        if (cloudPlayer != null && targetService != null) {
            playerManager.playerExecutor(playerUUID).connect(targetService.name());
        }
    }

    /**
     * Connects a player to a service of a specific task.
     * @param playerUUID player UUID
     * @param taskName task name
     * @param selectorType service selector type
     */
    public static void connectPlayerToTask(UUID playerUUID, String taskName, ServerSelectorType selectorType) {
        getCloudPlayerManager().playerExecutor(playerUUID).connectToTask(taskName, selectorType);
    }

    /**
     * Connects a player to the least populated service of a task.
     * @param playerUUID player UUID
     * @param taskName task name
     */
    public static void connectPlayerToTaskLowestPlayers(UUID playerUUID, String taskName) {
        connectPlayerToTask(playerUUID, taskName, ServerSelectorType.LOWEST_PLAYERS);
    }

    /**
     * Connects a player to a random service of a task.
     * @param playerUUID player UUID
     * @param taskName task name
     */
    public static void connectPlayerToTaskRandom(UUID playerUUID, String taskName) {
        connectPlayerToTask(playerUUID, taskName, ServerSelectorType.RANDOM);
    }

    /**
     * Connects a player to a service of a specific group.
     * @param playerUUID player UUID
     * @param groupName group name
     * @param selectorType service selector type
     */
    public static void connectPlayerToGroup(UUID playerUUID, String groupName, ServerSelectorType selectorType) {
        getCloudPlayerManager().playerExecutor(playerUUID).connectToGroup(groupName, selectorType);
    }

    /**
     * Connects a player to the fallback server.
     * @param playerUUID player UUID
     */
    public static void connectPlayerToFallback(UUID playerUUID) {
        getCloudPlayerManager().playerExecutor(playerUUID).connectToFallback();
    }

    /**
     * Kicks a player with a specific message.
     * @param playerUUID player UUID
     * @param message kick message as Component
     */
    public static void kickPlayer(UUID playerUUID, Component message) {
        getCloudPlayerManager().playerExecutor(playerUUID).kick(message);
    }

    /**
     * Kicks all players in the network with a specific message.
     * @param message kick message as Component
     */
    public static void kickAllPlayers(Component message) {
        getCloudPlayerManager().globalPlayerExecutor().kick(message);
    }

    /**
     * Sends a chat message to a player.
     * @param playerUUID player UUID
     * @param message message as Component
     */
    public static void sendMessage(UUID playerUUID, Component message) {
        getCloudPlayerManager().playerExecutor(playerUUID).sendChatMessage(message);
    }

    /**
     * Sends a chat message to all players in the network.
     * @param message message as Component
     */
    public static void sendMessageToAll(Component message) {
        getCloudPlayerManager().globalPlayerExecutor().sendChatMessage(message);
    }

    /**
     * Sends a chat message to all players with a specific permission.
     * @param message message as Component
     * @param permission required permission
     */
    public static void sendMessageToAllWithPermission(Component message, String permission) {
        getCloudPlayerManager().globalPlayerExecutor().sendChatMessage(message, permission);
    }

    /**
     * Sends a title to a player.
     * @param playerUUID player UUID
     * @param title Title object
     */
    public static void sendTitle(UUID playerUUID, Title title) {
        getCloudPlayerManager().playerExecutor(playerUUID).sendTitle(title);
    }

    /**
     * Sends a title to all players in the network.
     * @param title Title object
     */
    public static void sendTitleToAll(Title title) {
        getCloudPlayerManager().globalPlayerExecutor().sendTitle(title);
    }

    /**
     * Makes a player execute a command.
     * @param playerUUID player UUID
     * @param command command without leading slash
     */
    public static void executeCommand(UUID playerUUID, String command) {
        getCloudPlayerManager().playerExecutor(playerUUID).spoofCommandExecution(command);
    }

    /**
     * Sends a plugin message to a player.
     * @param playerUUID player UUID
     * @param channel message channel
     * @param data message data
     */
    public static void sendPluginMessage(UUID playerUUID, String channel, byte[] data) {
        getCloudPlayerManager().playerExecutor(playerUUID).sendPluginMessage(channel, data);
    }

    /**
     * Returns a ServiceTask by name.
     * @param name task name
     * @return ServiceTask or null if not found
     */
    public static ServiceTask getTaskByName(String name) {
        return getCloudServiceTaskProvider().serviceTask(name);
    }

    /**
     * Returns all registered ServiceTasks.
     * @return collection of ServiceTasks
     */
    public static Collection<ServiceTask> getAllTasks() {
        return getCloudServiceTaskProvider().serviceTasks();
    }

    /**
     * Checks if a task exists.
     * @param name task name
     * @return true if the task exists, false otherwise
     */
    public static boolean taskExists(String name) {
        return getCloudServiceTaskProvider().serviceTask(name) != null;
    }

    /**
     * Returns all registered services.
     * @return collection of ServiceInfoSnapshots
     */
    public static Collection<ServiceInfoSnapshot> getAllServices() {
        return getCloudServiceProvider().services();
    }

    /**
     * Returns all running services.
     * @return collection of ServiceInfoSnapshots
     */
    public static Collection<ServiceInfoSnapshot> getRunningServices() {
        return getCloudServiceProvider().runningServices();
    }

    /**
     * Returns all services of a specific task.
     * @param taskName task name
     * @return collection of ServiceInfoSnapshots
     */
    public static Collection<ServiceInfoSnapshot> getServicesByTask(String taskName) {
        return getCloudServiceProvider().servicesByTask(taskName);
    }

    /**
     * Returns all services of a specific group.
     * @param groupName group name
     * @return collection of ServiceInfoSnapshots
     */
    public static Collection<ServiceInfoSnapshot> getServicesByGroup(String groupName) {
        return getCloudServiceProvider().servicesByGroup(groupName);
    }

    /**
     * Returns a service by name.
     * @param name service name
     * @return ServiceInfoSnapshot or null if not found
     */
    public static ServiceInfoSnapshot getServiceByName(String name) {
        return getCloudServiceProvider().serviceByName(name);
    }

    /**
     * Returns a service by UUID.
     * @param uuid service UUID
     * @return ServiceInfoSnapshot or null if not found
     */
    public static ServiceInfoSnapshot getServiceByUUID(UUID uuid) {
        return getCloudServiceProvider().service(uuid);
    }

    /**
     * Returns a SpecificCloudServiceProvider for a service by name.
     * @param name service name
     * @return SpecificCloudServiceProvider
     */
    public static SpecificCloudServiceProvider getSpecificServiceProvider(String name) {
        return getCloudServiceProvider().serviceProviderByName(name);
    }

    /**
     * Returns a SpecificCloudServiceProvider for a service by UUID.
     * @param uuid service UUID
     * @return SpecificCloudServiceProvider
     */
    public static SpecificCloudServiceProvider getSpecificServiceProvider(UUID uuid) {
        return getCloudServiceProvider().serviceProvider(uuid);
    }

    /**
     * Returns the total count of all registered services.
     * @return service count
     */
    public static int getServiceCount() {
        return getCloudServiceProvider().serviceCount();
    }

    /**
     * Returns the service count of a specific task.
     * @param taskName task name
     * @return service count
     */
    public static int getServiceCountByTask(String taskName) {
        return getCloudServiceProvider().serviceCountByTask(taskName);
    }

    /**
     * Returns the service count of a specific group.
     * @param groupName group name
     * @return service count
     */
    public static int getServiceCountByGroup(String groupName) {
        return getCloudServiceProvider().serviceCountByGroup(groupName);
    }

    /**
     * Checks if a service is empty (no players).
     * @param service ServiceInfoSnapshot
     * @return true if empty, false otherwise
     */
    public static boolean isServiceEmpty(ServiceInfoSnapshot service) {
        return BridgeServiceHelper.emptyService(service);
    }

    /**
     * Checks if a service is full.
     * @param service ServiceInfoSnapshot
     * @return true if full, false otherwise
     */
    public static boolean isServiceFull(ServiceInfoSnapshot service) {
        return BridgeServiceHelper.fullService(service);
    }

    /**
     * Checks if a service is starting.
     * @param service ServiceInfoSnapshot
     * @return true if starting, false otherwise
     */
    public static boolean isServiceStarting(ServiceInfoSnapshot service) {
        return BridgeServiceHelper.startingService(service);
    }

    /**
     * Checks if a service is in-game mode.
     * @param service ServiceInfoSnapshot
     * @return true if in-game, false otherwise
     */
    public static boolean isServiceInGame(ServiceInfoSnapshot service) {
        return BridgeServiceHelper.inGameService(service);
    }

    /**
     * Returns the estimated service state.
     * @param service ServiceInfoSnapshot
     * @return ServiceInfoState
     */
    public static BridgeServiceHelper.ServiceInfoState guessServiceState(ServiceInfoSnapshot service) {
        return BridgeServiceHelper.guessStateFromServiceInfoSnapshot(service);
    }

    /**
     * Returns the online player count on a service.
     * @param service ServiceInfoSnapshot
     * @return player count or 0 if unavailable
     */
    public static int getOnlineCountOnService(ServiceInfoSnapshot service) {
        return service.readPropertyOrDefault(BridgeDocProperties.ONLINE_COUNT, 0);
    }

    /**
     * Returns the max player count on a service.
     * @param service ServiceInfoSnapshot
     * @return max player count or 0 if unavailable
     */
    public static int getMaxPlayersOnService(ServiceInfoSnapshot service) {
        return service.readPropertyOrDefault(BridgeDocProperties.MAX_PLAYERS, 0);
    }

    /**
     * Returns the MOTD of a service.
     * @param service ServiceInfoSnapshot
     * @return MOTD or null if unavailable
     */
    public static String getMotdOnService(ServiceInfoSnapshot service) {
        return service.readProperty(BridgeDocProperties.MOTD);
    }

    /**
     * Returns the state of a service.
     * @param service ServiceInfoSnapshot
     * @return state or null if unavailable
     */
    public static String getStateOnService(ServiceInfoSnapshot service) {
        return service.readProperty(BridgeDocProperties.STATE);
    }

    /**
     * Returns the extra value of a service.
     * @param service ServiceInfoSnapshot
     * @return extra value or null if unavailable
     */
    public static String getExtraOnService(ServiceInfoSnapshot service) {
        return service.readProperty(BridgeDocProperties.EXTRA);
    }

    /**
     * Returns the version of a service.
     * @param service ServiceInfoSnapshot
     * @return version or null if unavailable
     */
    public static String getVersionOnService(ServiceInfoSnapshot service) {
        return service.readProperty(BridgeDocProperties.VERSION);
    }

    /**
     * Checks if a service is online (fully started).
     * @param service ServiceInfoSnapshot
     * @return true if online, false otherwise
     */
    public static boolean isServiceOnline(ServiceInfoSnapshot service) {
        return service.readPropertyOrDefault(BridgeDocProperties.IS_ONLINE, false);
    }

    /**
     * Returns all players on a service.
     * @param service ServiceInfoSnapshot
     * @return collection of ServicePlayers or empty collection
     */
    public static Collection<ServicePlayer> getPlayersOnService(ServiceInfoSnapshot service) {
        Collection<ServicePlayer> players = service.readProperty(BridgeDocProperties.PLAYERS);
        return players != null ? players : Collections.emptyList();
    }

    /**
     * Returns the total player count across all services of a task.
     * @param taskName task name
     * @return total player count
     */
    public static int getPlayerCountByTask(String taskName) {
        AtomicInteger count = new AtomicInteger(0);
        getCloudServiceProvider().servicesByTask(taskName).forEach(serviceInfoSnapshot ->
                count.getAndAdd(serviceInfoSnapshot.readPropertyOrDefault(BridgeDocProperties.ONLINE_COUNT, 0)));
        return count.get();
    }

    /**
     * Returns the max player capacity across all services of a task.
     * @param taskName task name
     * @return max player capacity
     */
    public static int getMaxPlayersByTask(String taskName) {
        AtomicInteger count = new AtomicInteger(0);
        getCloudServiceProvider().servicesByTask(taskName).forEach(serviceInfoSnapshot ->
                count.getAndAdd(serviceInfoSnapshot.readPropertyOrDefault(BridgeDocProperties.MAX_PLAYERS, 0)));
        return count.get();
    }

    /**
     * Returns the total player count across all services of a group.
     * @param groupName group name
     * @return total player count
     */
    public static int getPlayerCountByGroup(String groupName) {
        AtomicInteger count = new AtomicInteger(0);
        getCloudServiceProvider().servicesByGroup(groupName).forEach(serviceInfoSnapshot ->
                count.getAndAdd(serviceInfoSnapshot.readPropertyOrDefault(BridgeDocProperties.ONLINE_COUNT, 0)));
        return count.get();
    }

    /**
     * Returns the max player capacity across all services of a group.
     * @param groupName group name
     * @return max player capacity
     */
    public static int getMaxPlayersByGroup(String groupName) {
        AtomicInteger count = new AtomicInteger(0);
        getCloudServiceProvider().servicesByGroup(groupName).forEach(serviceInfoSnapshot ->
                count.getAndAdd(serviceInfoSnapshot.readPropertyOrDefault(BridgeDocProperties.MAX_PLAYERS, 0)));
        return count.get();
    }

    /**
     * Sets the current service to in-game mode and auto-starts a new service.
     */
    public static void changeToIngame() {
        getBridgeServiceHelper().changeToIngame();
    }

    /**
     * Sets the current service to in-game mode.
     * @param autoStartService true to auto-start a new service
     */
    public static void changeToIngame(boolean autoStartService) {
        getBridgeServiceHelper().changeToIngame(autoStartService);
    }

    /**
     * Sets the MOTD of the current service.
     * @param motd new MOTD
     */
    public static void setMotd(String motd) {
        getBridgeServiceHelper().motd().set(motd);
    }

    /**
     * Returns the MOTD of the current service.
     * @return current MOTD
     */
    public static String getMotd() {
        return getBridgeServiceHelper().motd().get();
    }

    /**
     * Sets the state of the current service.
     * @param state new state
     */
    public static void setState(String state) {
        getBridgeServiceHelper().state().set(state);
    }

    /**
     * Returns the state of the current service.
     * @return current state
     */
    public static String getState() {
        return getBridgeServiceHelper().state().get();
    }

    /**
     * Sets the extra value of the current service.
     * @param extra new extra value
     */
    public static void setExtra(String extra) {
        getBridgeServiceHelper().extra().set(extra);
    }

    /**
     * Returns the extra value of the current service.
     * @return current extra value
     */
    public static String getExtra() {
        return getBridgeServiceHelper().extra().get();
    }

    /**
     * Sets the max player count of the current service.
     * @param maxPlayers max player count
     */
    public static void setMaxPlayers(int maxPlayers) {
        getBridgeServiceHelper().maxPlayers().set(maxPlayers);
    }

    /**
     * Returns the max player count of the current service.
     * @return max player count
     */
    public static int getMaxPlayers() {
        return getBridgeServiceHelper().maxPlayers().get();
    }

    /**
     * Returns the current service name.
     * @return current service name
     */
    public static String getCurrentServiceName() {
        return getWrapperConfiguration().serviceConfiguration().serviceId().name();
    }

    /**
     * Returns the task service ID of the current service.
     * @return task service ID
     */
    public static int getCurrentTaskServiceId() {
        return getWrapperConfiguration().serviceConfiguration().serviceId().taskServiceId();
    }

    /**
     * Returns the task name of the current service.
     * @return task name
     */
    public static String getCurrentTaskName() {
        return getWrapperConfiguration().serviceConfiguration().serviceId().taskName();
    }

    /**
     * Returns the UUID of the current service.
     * @return service UUID
     */
    public static UUID getCurrentServiceUUID() {
        return getWrapperConfiguration().serviceConfiguration().serviceId().uniqueId();
    }

    /**
     * Finds the service with the fewest players of a specific task.
     * @param taskName task name
     * @return ServiceInfoSnapshot with fewest players, or null
     */
    public static ServiceInfoSnapshot findLowestPlayerService(String taskName) {
        return getCloudServiceProvider().servicesByTask(taskName).stream()
                .filter(service -> isServiceOnline(service) && !isServiceInGame(service) && !isServiceFull(service))
                .min(Comparator.comparingInt(CloudNetUtils::getOnlineCountOnService))
                .orElse(null);
    }

    /**
     * Finds a random available service of a specific task.
     * @param taskName task name
     * @return ServiceInfoSnapshot or null
     */
    public static ServiceInfoSnapshot findRandomAvailableService(String taskName) {
        List<ServiceInfoSnapshot> available = getCloudServiceProvider().servicesByTask(taskName).stream()
                .filter(service -> isServiceOnline(service) && !isServiceInGame(service) && !isServiceFull(service))
                .collect(Collectors.toList());
        if (available.isEmpty()) return null;
        return available.get(new Random().nextInt(available.size()));
    }

    /**
     * Returns all available (online, not in-game, not full) services of a task.
     * @param taskName task name
     * @return list of available services
     */
    public static List<ServiceInfoSnapshot> getAvailableServices(String taskName) {
        return getCloudServiceProvider().servicesByTask(taskName).stream()
                .filter(service -> isServiceOnline(service) && !isServiceInGame(service) && !isServiceFull(service))
                .collect(Collectors.toList());
    }

    /**
     * Returns all online players on a task.
     * @param taskName task name
     * @return list of CloudPlayers
     */
    public static List<CloudPlayer> getOnlinePlayersOnTask(String taskName) {
        return new ArrayList<>(getCloudPlayerManager().taskOnlinePlayers(taskName).players());
    }

    /**
     * Returns all online players on a group.
     * @param groupName group name
     * @return list of CloudPlayers
     */
    public static List<CloudPlayer> getOnlinePlayersOnGroup(String groupName) {
        return new ArrayList<>(getCloudPlayerManager().groupOnlinePlayers(groupName).players());
    }

    /**
     * Replaces placeholders in a string with service information.
     * @param value string with placeholders
     * @param group group (can be null)
     * @param service service (can be null)
     * @return string with replaced placeholders
     */
    public static String fillPlaceholders(String value, String group, ServiceInfoSnapshot service) {
        return BridgeServiceHelper.fillCommonPlaceholders(value, group, service);
    }
}
