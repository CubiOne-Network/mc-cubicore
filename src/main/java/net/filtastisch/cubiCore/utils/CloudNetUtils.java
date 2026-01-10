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
 * Utility-Klasse für CloudNet v4 Operationen.
 * Bietet einfache Zugriffsmethoden für häufig verwendete CloudNet-Funktionen.
 */
public class CloudNetUtils {

    // ==================== Provider Getter ====================

    /**
     * Gibt den CloudServiceProvider zurück.
     * @return CloudServiceProvider Instanz
     */
    public static CloudServiceProvider getCloudServiceProvider() {
        return InjectionLayer.ext().instance(CloudServiceProvider.class);
    }

    /**
     * Gibt den PlayerManager zurück.
     * @return PlayerManager Instanz
     */
    public static PlayerManager getCloudPlayerManager() {
        return getServiceRegistry().defaultInstance(PlayerManager.class);
    }

    /**
     * Gibt den BridgeServiceHelper zurück.
     * @return BridgeServiceHelper Instanz
     */
    public static BridgeServiceHelper getBridgeServiceHelper() {
        return InjectionLayer.ext().instance(BridgeServiceHelper.class);
    }

    /**
     * Gibt den ServiceTaskProvider zurück.
     * @return ServiceTaskProvider Instanz
     */
    public static ServiceTaskProvider getCloudServiceTaskProvider() {
        return InjectionLayer.ext().instance(ServiceTaskProvider.class);
    }

    /**
     * Gibt die WrapperConfiguration zurück.
     * @return WrapperConfiguration Instanz
     */
    public static WrapperConfiguration getWrapperConfiguration() {
        return InjectionLayer.ext().instance(WrapperConfiguration.class);
    }

    /**
     * Gibt die ServiceRegistry zurück.
     * @return ServiceRegistry Instanz
     */
    public static ServiceRegistry getServiceRegistry() {
        return InjectionLayer.ext().instance(ServiceRegistry.class);
    }

    /**
     * Gibt den EventManager zurück.
     * @return EventManager Instanz
     */

    public static EventManager getEventManager() {
        return InjectionLayer.ext().instance(EventManager.class);
    }

    // ==================== Online Spieler Methoden ====================

    /**
     * Gibt die Anzahl aller online Spieler im Netzwerk zurück.
     * @return Anzahl der Online-Spieler
     */
    public static int getGlobalOnlineCount() {
        return getCloudPlayerManager().onlineCount();
    }

    /**
     * Gibt die Anzahl aller online Spieler im Netzwerk asynchron zurück.
     * @return CompletableFuture mit der Anzahl der Online-Spieler
     */
    public static CompletableFuture<Integer> getGlobalOnlineCountAsync() {
        return getCloudPlayerManager().onlineCountAsync();
    }

    /**
     * Gibt die Anzahl aller registrierten Spieler in der Datenbank zurück.
     * @return Anzahl der registrierten Spieler
     */
    public static long getRegisteredPlayerCount() {
        return getCloudPlayerManager().registeredCount();
    }

    /**
     * Gibt einen Online-Spieler anhand seiner UUID zurück.
     * @param uuid UUID des Spielers
     * @return CloudPlayer oder null wenn nicht online
     */
    public static CloudPlayer getOnlinePlayer(UUID uuid) {
        return getCloudPlayerManager().onlinePlayer(uuid);
    }

    /**
     * Gibt einen Online-Spieler anhand seines Namens zurück.
     * @param name Name des Spielers
     * @return CloudPlayer oder null wenn nicht online
     */
    public static CloudPlayer getOnlinePlayerByName(String name) {
        return getCloudPlayerManager().firstOnlinePlayer(name);
    }

    /**
     * Gibt alle Online-Spieler mit einem bestimmten Namen zurück.
     * @param name Name der Spieler
     * @return Liste von CloudPlayern
     */
    public static List<CloudPlayer> getOnlinePlayersByName(String name) {
        return getCloudPlayerManager().onlinePlayers(name);
    }

    /**
     * Prüft ob ein Spieler online ist.
     * @param uuid UUID des Spielers
     * @return true wenn online, sonst false
     */
    public static boolean isPlayerOnline(UUID uuid) {
        return getCloudPlayerManager().onlinePlayer(uuid) != null;
    }

    /**
     * Gibt den Namen des Services zurück, mit dem ein Spieler verbunden ist.
     * @param uuid UUID des Spielers
     * @return Service-Name oder null wenn nicht online
     */
    public static String getPlayerConnectedService(UUID uuid) {
        CloudPlayer player = getCloudPlayerManager().onlinePlayer(uuid);
        if (player == null) return null;
        return Objects.requireNonNull(player.connectedService()).serverName();
    }

    // ==================== Offline Spieler Methoden ====================

    /**
     * Gibt einen Offline-Spieler anhand seiner UUID zurück.
     * @param uuid UUID des Spielers
     * @return CloudOfflinePlayer oder null wenn nicht gefunden
     */
    public static CloudOfflinePlayer getOfflinePlayer(UUID uuid) {
        return getCloudPlayerManager().offlinePlayer(uuid);
    }

    /**
     * Gibt einen Offline-Spieler anhand seines Namens zurück.
     * @param name Name des Spielers
     * @return CloudOfflinePlayer oder null wenn nicht gefunden
     */
    public static CloudOfflinePlayer getOfflinePlayerByName(String name) {
        return getCloudPlayerManager().firstOfflinePlayer(name);
    }

    /**
     * Gibt alle Offline-Spieler mit einem bestimmten Namen zurück.
     * @param name Name der Spieler
     * @return Liste von CloudOfflinePlayern
     */
    public static List<CloudOfflinePlayer> getOfflinePlayersByName(String name) {
        return getCloudPlayerManager().offlinePlayers(name);
    }

    // ==================== PlayerExecutor Methoden ====================

    /**
     * Gibt den PlayerExecutor für einen bestimmten Spieler zurück.
     * @param uuid UUID des Spielers
     * @return PlayerExecutor für den Spieler
     */
    public static PlayerExecutor getPlayerExecutor(UUID uuid) {
        return getCloudPlayerManager().playerExecutor(uuid);
    }

    /**
     * Gibt den globalen PlayerExecutor zurück, der für alle Spieler gilt.
     * @return Globaler PlayerExecutor
     */
    public static PlayerExecutor getGlobalPlayerExecutor() {
        return getCloudPlayerManager().globalPlayerExecutor();
    }

    /**
     * Verbindet einen Spieler mit einem bestimmten Service.
     * @param playerUUID UUID des Spielers
     * @param targetServiceName Name des Ziel-Services
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
     * Verbindet einen Spieler mit einem bestimmten Service.
     * @param playerUUID UUID des Spielers
     * @param targetService Ziel-ServiceInfoSnapshot
     */
    public static void connectPlayerToService(UUID playerUUID, ServiceInfoSnapshot targetService) {
        PlayerManager playerManager = getCloudPlayerManager();
        CloudPlayer cloudPlayer = playerManager.onlinePlayer(playerUUID);
        if (cloudPlayer != null && targetService != null) {
            playerManager.playerExecutor(playerUUID).connect(targetService.name());
        }
    }

    /**
     * Verbindet einen Spieler mit einem Service einer bestimmten Task.
     * @param playerUUID UUID des Spielers
     * @param taskName Name der Task
     * @param selectorType Auswahl-Typ für den Service
     */
    public static void connectPlayerToTask(UUID playerUUID, String taskName, ServerSelectorType selectorType) {
        getCloudPlayerManager().playerExecutor(playerUUID).connectToTask(taskName, selectorType);
    }

    /**
     * Verbindet einen Spieler mit dem am wenigsten gefüllten Service einer Task.
     * @param playerUUID UUID des Spielers
     * @param taskName Name der Task
     */
    public static void connectPlayerToTaskLowestPlayers(UUID playerUUID, String taskName) {
        connectPlayerToTask(playerUUID, taskName, ServerSelectorType.LOWEST_PLAYERS);
    }

    /**
     * Verbindet einen Spieler mit einem zufälligen Service einer Task.
     * @param playerUUID UUID des Spielers
     * @param taskName Name der Task
     */
    public static void connectPlayerToTaskRandom(UUID playerUUID, String taskName) {
        connectPlayerToTask(playerUUID, taskName, ServerSelectorType.RANDOM);
    }

    /**
     * Verbindet einen Spieler mit einem Service einer bestimmten Gruppe.
     * @param playerUUID UUID des Spielers
     * @param groupName Name der Gruppe
     * @param selectorType Auswahl-Typ für den Service
     */
    public static void connectPlayerToGroup(UUID playerUUID, String groupName, ServerSelectorType selectorType) {
        getCloudPlayerManager().playerExecutor(playerUUID).connectToGroup(groupName, selectorType);
    }

    /**
     * Verbindet einen Spieler zum Fallback-Server.
     * @param playerUUID UUID des Spielers
     */
    public static void connectPlayerToFallback(UUID playerUUID) {
        getCloudPlayerManager().playerExecutor(playerUUID).connectToFallback();
    }

    /**
     * Kickt einen Spieler mit einer bestimmten Nachricht.
     * @param playerUUID UUID des Spielers
     * @param message Kick-Nachricht als Component
     */
    public static void kickPlayer(UUID playerUUID, Component message) {
        getCloudPlayerManager().playerExecutor(playerUUID).kick(message);
    }

    /**
     * Kickt alle Spieler im Netzwerk mit einer bestimmten Nachricht.
     * @param message Kick-Nachricht als Component
     */
    public static void kickAllPlayers(Component message) {
        getCloudPlayerManager().globalPlayerExecutor().kick(message);
    }

    /**
     * Sendet eine Chatnachricht an einen Spieler.
     * @param playerUUID UUID des Spielers
     * @param message Nachricht als Component
     */
    public static void sendMessage(UUID playerUUID, Component message) {
        getCloudPlayerManager().playerExecutor(playerUUID).sendChatMessage(message);
    }

    /**
     * Sendet eine Chatnachricht an alle Spieler im Netzwerk.
     * @param message Nachricht als Component
     */
    public static void sendMessageToAll(Component message) {
        getCloudPlayerManager().globalPlayerExecutor().sendChatMessage(message);
    }

    /**
     * Sendet eine Chatnachricht an alle Spieler mit einer bestimmten Permission.
     * @param message Nachricht als Component
     * @param permission Permission die der Spieler haben muss
     */
    public static void sendMessageToAllWithPermission(Component message, String permission) {
        getCloudPlayerManager().globalPlayerExecutor().sendChatMessage(message, permission);
    }

    /**
     * Sendet einen Title an einen Spieler.
     * @param playerUUID UUID des Spielers
     * @param title Title-Objekt
     */
    public static void sendTitle(UUID playerUUID, Title title) {
        getCloudPlayerManager().playerExecutor(playerUUID).sendTitle(title);
    }

    /**
     * Sendet einen Title an alle Spieler im Netzwerk.
     * @param title Title-Objekt
     */
    public static void sendTitleToAll(Title title) {
        getCloudPlayerManager().globalPlayerExecutor().sendTitle(title);
    }

    /**
     * Lässt einen Spieler einen Befehl ausführen.
     * @param playerUUID UUID des Spielers
     * @param command Befehl ohne führenden Slash
     */
    public static void executeCommand(UUID playerUUID, String command) {
        getCloudPlayerManager().playerExecutor(playerUUID).spoofCommandExecution(command);
    }

    /**
     * Sendet eine Plugin-Message an einen Spieler.
     * @param playerUUID UUID des Spielers
     * @param channel Channel der Nachricht
     * @param data Daten der Nachricht
     */
    public static void sendPluginMessage(UUID playerUUID, String channel, byte[] data) {
        getCloudPlayerManager().playerExecutor(playerUUID).sendPluginMessage(channel, data);
    }

    // ==================== Task Methoden ====================

    /**
     * Gibt eine ServiceTask anhand ihres Namens zurück.
     * @param name Name der Task
     * @return ServiceTask oder null wenn nicht gefunden
     */
    public static ServiceTask getTaskByName(String name) {
        return getCloudServiceTaskProvider().serviceTask(name);
    }

    /**
     * Gibt alle registrierten ServiceTasks zurück.
     * @return Collection von ServiceTasks
     */
    public static Collection<ServiceTask> getAllTasks() {
        return getCloudServiceTaskProvider().serviceTasks();
    }

    /**
     * Prüft ob eine Task existiert.
     * @param name Name der Task
     * @return true wenn die Task existiert, sonst false
     */
    public static boolean taskExists(String name) {
        return getCloudServiceTaskProvider().serviceTask(name) != null;
    }

    // ==================== Service Methoden ====================

    /**
     * Gibt alle registrierten Services zurück.
     * @return Collection von ServiceInfoSnapshots
     */
    public static Collection<ServiceInfoSnapshot> getAllServices() {
        return getCloudServiceProvider().services();
    }

    /**
     * Gibt alle laufenden Services zurück.
     * @return Collection von ServiceInfoSnapshots
     */
    public static Collection<ServiceInfoSnapshot> getRunningServices() {
        return getCloudServiceProvider().runningServices();
    }

    /**
     * Gibt alle Services einer bestimmten Task zurück.
     * @param taskName Name der Task
     * @return Collection von ServiceInfoSnapshots
     */
    public static Collection<ServiceInfoSnapshot> getServicesByTask(String taskName) {
        return getCloudServiceProvider().servicesByTask(taskName);
    }

    /**
     * Gibt alle Services einer bestimmten Gruppe zurück.
     * @param groupName Name der Gruppe
     * @return Collection von ServiceInfoSnapshots
     */
    public static Collection<ServiceInfoSnapshot> getServicesByGroup(String groupName) {
        return getCloudServiceProvider().servicesByGroup(groupName);
    }

    /**
     * Gibt einen Service anhand seines Namens zurück.
     * @param name Name des Services
     * @return ServiceInfoSnapshot oder null wenn nicht gefunden
     */
    public static ServiceInfoSnapshot getServiceByName(String name) {
        return getCloudServiceProvider().serviceByName(name);
    }

    /**
     * Gibt einen Service anhand seiner UUID zurück.
     * @param uuid UUID des Services
     * @return ServiceInfoSnapshot oder null wenn nicht gefunden
     */
    public static ServiceInfoSnapshot getServiceByUUID(UUID uuid) {
        return getCloudServiceProvider().service(uuid);
    }

    /**
     * Gibt einen SpecificCloudServiceProvider für einen Service anhand seines Namens zurück.
     * @param name Name des Services
     * @return SpecificCloudServiceProvider
     */
    public static SpecificCloudServiceProvider getSpecificServiceProvider(String name) {
        return getCloudServiceProvider().serviceProviderByName(name);
    }

    /**
     * Gibt einen SpecificCloudServiceProvider für einen Service anhand seiner UUID zurück.
     * @param uuid UUID des Services
     * @return SpecificCloudServiceProvider
     */
    public static SpecificCloudServiceProvider getSpecificServiceProvider(UUID uuid) {
        return getCloudServiceProvider().serviceProvider(uuid);
    }

    /**
     * Gibt die Gesamtanzahl aller registrierten Services zurück.
     * @return Anzahl der Services
     */
    public static int getServiceCount() {
        return getCloudServiceProvider().serviceCount();
    }

    /**
     * Gibt die Anzahl der Services einer bestimmten Task zurück.
     * @param taskName Name der Task
     * @return Anzahl der Services
     */
    public static int getServiceCountByTask(String taskName) {
        return getCloudServiceProvider().serviceCountByTask(taskName);
    }

    /**
     * Gibt die Anzahl der Services einer bestimmten Gruppe zurück.
     * @param groupName Name der Gruppe
     * @return Anzahl der Services
     */
    public static int getServiceCountByGroup(String groupName) {
        return getCloudServiceProvider().serviceCountByGroup(groupName);
    }

    // ==================== Service Status Methoden ====================

    /**
     * Prüft ob ein Service leer ist (keine Spieler).
     * @param service ServiceInfoSnapshot
     * @return true wenn leer, sonst false
     */
    public static boolean isServiceEmpty(ServiceInfoSnapshot service) {
        return BridgeServiceHelper.emptyService(service);
    }

    /**
     * Prüft ob ein Service voll ist.
     * @param service ServiceInfoSnapshot
     * @return true wenn voll, sonst false
     */
    public static boolean isServiceFull(ServiceInfoSnapshot service) {
        return BridgeServiceHelper.fullService(service);
    }

    /**
     * Prüft ob ein Service gerade startet.
     * @param service ServiceInfoSnapshot
     * @return true wenn startend, sonst false
     */
    public static boolean isServiceStarting(ServiceInfoSnapshot service) {
        return BridgeServiceHelper.startingService(service);
    }

    /**
     * Prüft ob ein Service im InGame-Modus ist.
     * @param service ServiceInfoSnapshot
     * @return true wenn InGame, sonst false
     */
    public static boolean isServiceInGame(ServiceInfoSnapshot service) {
        return BridgeServiceHelper.inGameService(service);
    }

    /**
     * Gibt den geschätzten Service-Status zurück.
     * @param service ServiceInfoSnapshot
     * @return ServiceInfoState
     */
    public static BridgeServiceHelper.ServiceInfoState guessServiceState(ServiceInfoSnapshot service) {
        return BridgeServiceHelper.guessStateFromServiceInfoSnapshot(service);
    }

    // ==================== Service Properties Methoden ====================

    /**
     * Gibt die Anzahl der online Spieler auf einem Service zurück.
     * @param service ServiceInfoSnapshot
     * @return Anzahl der Spieler oder 0 wenn nicht verfügbar
     */
    public static int getOnlineCountOnService(ServiceInfoSnapshot service) {
        return service.readPropertyOrDefault(BridgeDocProperties.ONLINE_COUNT, 0);
    }

    /**
     * Gibt die maximale Spieleranzahl auf einem Service zurück.
     * @param service ServiceInfoSnapshot
     * @return Maximale Spieleranzahl oder 0 wenn nicht verfügbar
     */
    public static int getMaxPlayersOnService(ServiceInfoSnapshot service) {
        return service.readPropertyOrDefault(BridgeDocProperties.MAX_PLAYERS, 0);
    }

    /**
     * Gibt die MOTD eines Services zurück.
     * @param service ServiceInfoSnapshot
     * @return MOTD oder null wenn nicht verfügbar
     */
    public static String getMotdOnService(ServiceInfoSnapshot service) {
        return service.readProperty(BridgeDocProperties.MOTD);
    }

    /**
     * Gibt den Status eines Services zurück.
     * @param service ServiceInfoSnapshot
     * @return Status oder null wenn nicht verfügbar
     */
    public static String getStateOnService(ServiceInfoSnapshot service) {
        return service.readProperty(BridgeDocProperties.STATE);
    }

    /**
     * Gibt den Extra-Wert eines Services zurück.
     * @param service ServiceInfoSnapshot
     * @return Extra-Wert oder null wenn nicht verfügbar
     */
    public static String getExtraOnService(ServiceInfoSnapshot service) {
        return service.readProperty(BridgeDocProperties.EXTRA);
    }

    /**
     * Gibt die Version eines Services zurück.
     * @param service ServiceInfoSnapshot
     * @return Version oder null wenn nicht verfügbar
     */
    public static String getVersionOnService(ServiceInfoSnapshot service) {
        return service.readProperty(BridgeDocProperties.VERSION);
    }

    /**
     * Prüft ob ein Service online (vollständig gestartet) ist.
     * @param service ServiceInfoSnapshot
     * @return true wenn online, sonst false
     */
    public static boolean isServiceOnline(ServiceInfoSnapshot service) {
        return service.readPropertyOrDefault(BridgeDocProperties.IS_ONLINE, false);
    }

    /**
     * Gibt alle Spieler auf einem Service zurück.
     * @param service ServiceInfoSnapshot
     * @return Collection von ServicePlayern oder leere Collection
     */
    public static Collection<ServicePlayer> getPlayersOnService(ServiceInfoSnapshot service) {
        Collection<ServicePlayer> players = service.readProperty(BridgeDocProperties.PLAYERS);
        return players != null ? players : Collections.emptyList();
    }

    // ==================== Aggregierte Spielerzahl Methoden ====================

    /**
     * Gibt die Gesamtzahl der Spieler auf allen Services einer Task zurück.
     * @param taskName Name der Task
     * @return Gesamtzahl der Spieler
     */
    public static int getPlayerCountByTask(String taskName) {
        AtomicInteger count = new AtomicInteger(0);
        getCloudServiceProvider().servicesByTask(taskName).forEach(serviceInfoSnapshot ->
                count.getAndAdd(serviceInfoSnapshot.readPropertyOrDefault(BridgeDocProperties.ONLINE_COUNT, 0)));
        return count.get();
    }

    /**
     * Gibt die maximale Spielerkapazität aller Services einer Task zurück.
     * @param taskName Name der Task
     * @return Maximale Spielerkapazität
     */
    public static int getMaxPlayersByTask(String taskName) {
        AtomicInteger count = new AtomicInteger(0);
        getCloudServiceProvider().servicesByTask(taskName).forEach(serviceInfoSnapshot ->
                count.getAndAdd(serviceInfoSnapshot.readPropertyOrDefault(BridgeDocProperties.MAX_PLAYERS, 0)));
        return count.get();
    }

    /**
     * Gibt die Gesamtzahl der Spieler auf allen Services einer Gruppe zurück.
     * @param groupName Name der Gruppe
     * @return Gesamtzahl der Spieler
     */
    public static int getPlayerCountByGroup(String groupName) {
        AtomicInteger count = new AtomicInteger(0);
        getCloudServiceProvider().servicesByGroup(groupName).forEach(serviceInfoSnapshot ->
                count.getAndAdd(serviceInfoSnapshot.readPropertyOrDefault(BridgeDocProperties.ONLINE_COUNT, 0)));
        return count.get();
    }

    /**
     * Gibt die maximale Spielerkapazität aller Services einer Gruppe zurück.
     * @param groupName Name der Gruppe
     * @return Maximale Spielerkapazität
     */
    public static int getMaxPlayersByGroup(String groupName) {
        AtomicInteger count = new AtomicInteger(0);
        getCloudServiceProvider().servicesByGroup(groupName).forEach(serviceInfoSnapshot ->
                count.getAndAdd(serviceInfoSnapshot.readPropertyOrDefault(BridgeDocProperties.MAX_PLAYERS, 0)));
        return count.get();
    }

    // ==================== Bridge Service Helper Methoden ====================

    /**
     * Setzt den aktuellen Service auf InGame-Modus und startet automatisch einen neuen Service.
     */
    public static void changeToIngame() {
        getBridgeServiceHelper().changeToIngame();
    }

    /**
     * Setzt den aktuellen Service auf InGame-Modus.
     * @param autoStartService true wenn automatisch ein neuer Service gestartet werden soll
     */
    public static void changeToIngame(boolean autoStartService) {
        getBridgeServiceHelper().changeToIngame(autoStartService);
    }

    /**
     * Setzt die MOTD des aktuellen Services.
     * @param motd Neue MOTD
     */
    public static void setMotd(String motd) {
        getBridgeServiceHelper().motd().set(motd);
    }

    /**
     * Gibt die MOTD des aktuellen Services zurück.
     * @return Aktuelle MOTD
     */
    public static String getMotd() {
        return getBridgeServiceHelper().motd().get();
    }

    /**
     * Setzt den Status des aktuellen Services.
     * @param state Neuer Status
     */
    public static void setState(String state) {
        getBridgeServiceHelper().state().set(state);
    }

    /**
     * Gibt den Status des aktuellen Services zurück.
     * @return Aktueller Status
     */
    public static String getState() {
        return getBridgeServiceHelper().state().get();
    }

    /**
     * Setzt den Extra-Wert des aktuellen Services.
     * @param extra Neuer Extra-Wert
     */
    public static void setExtra(String extra) {
        getBridgeServiceHelper().extra().set(extra);
    }

    /**
     * Gibt den Extra-Wert des aktuellen Services zurück.
     * @return Aktueller Extra-Wert
     */
    public static String getExtra() {
        return getBridgeServiceHelper().extra().get();
    }

    /**
     * Setzt die maximale Spieleranzahl des aktuellen Services.
     * @param maxPlayers Maximale Spieleranzahl
     */
    public static void setMaxPlayers(int maxPlayers) {
        getBridgeServiceHelper().maxPlayers().set(maxPlayers);
    }

    /**
     * Gibt die maximale Spieleranzahl des aktuellen Services zurück.
     * @return Maximale Spieleranzahl
     */
    public static int getMaxPlayers() {
        return getBridgeServiceHelper().maxPlayers().get();
    }

    // ==================== Utility Methoden ====================

    /**
     * Gibt den aktuellen Service-Namen zurück.
     * @return Name des aktuellen Services
     */
    public static String getCurrentServiceName() {
        return getWrapperConfiguration().serviceConfiguration().serviceId().name();
    }

    /**
     * Gibt die Task-ID des aktuellen Services zurück.
     * @return Task-ID
     */
    public static int getCurrentTaskServiceId() {
        return getWrapperConfiguration().serviceConfiguration().serviceId().taskServiceId();
    }

    /**
     * Gibt den Task-Namen des aktuellen Services zurück.
     * @return Task-Name
     */
    public static String getCurrentTaskName() {
        return getWrapperConfiguration().serviceConfiguration().serviceId().taskName();
    }

    /**
     * Gibt die UUID des aktuellen Services zurück.
     * @return Service-UUID
     */
    public static UUID getCurrentServiceUUID() {
        return getWrapperConfiguration().serviceConfiguration().serviceId().uniqueId();
    }

    /**
     * Findet den Service mit den wenigsten Spielern einer bestimmten Task.
     * @param taskName Name der Task
     * @return ServiceInfoSnapshot mit den wenigsten Spielern oder null
     */
    public static ServiceInfoSnapshot findLowestPlayerService(String taskName) {
        return getCloudServiceProvider().servicesByTask(taskName).stream()
                .filter(service -> isServiceOnline(service) && !isServiceInGame(service) && !isServiceFull(service))
                .min(Comparator.comparingInt(CloudNetUtils::getOnlineCountOnService))
                .orElse(null);
    }

    /**
     * Findet einen zufälligen verfügbaren Service einer bestimmten Task.
     * @param taskName Name der Task
     * @return ServiceInfoSnapshot oder null
     */
    public static ServiceInfoSnapshot findRandomAvailableService(String taskName) {
        List<ServiceInfoSnapshot> available = getCloudServiceProvider().servicesByTask(taskName).stream()
                .filter(service -> isServiceOnline(service) && !isServiceInGame(service) && !isServiceFull(service))
                .collect(Collectors.toList());
        if (available.isEmpty()) return null;
        return available.get(new Random().nextInt(available.size()));
    }

    /**
     * Gibt alle verfügbaren (online, nicht InGame, nicht voll) Services einer Task zurück.
     * @param taskName Name der Task
     * @return Liste von verfügbaren Services
     */
    public static List<ServiceInfoSnapshot> getAvailableServices(String taskName) {
        return getCloudServiceProvider().servicesByTask(taskName).stream()
                .filter(service -> isServiceOnline(service) && !isServiceInGame(service) && !isServiceFull(service))
                .collect(Collectors.toList());
    }

    /**
     * Gibt alle Spieler auf einem Task-Provider zurück.
     * @param taskName Name der Task
     * @return PlayerProvider für die Task
     */
    public static List<CloudPlayer> getOnlinePlayersOnTask(String taskName) {
        return new ArrayList<>(getCloudPlayerManager().taskOnlinePlayers(taskName).players());
    }

    /**
     * Gibt alle Spieler auf einer Gruppe zurück.
     * @param groupName Name der Gruppe
     * @return Liste von CloudPlayern
     */
    public static List<CloudPlayer> getOnlinePlayersOnGroup(String groupName) {
        return new ArrayList<>(getCloudPlayerManager().groupOnlinePlayers(groupName).players());
    }

    /**
     * Ersetzt Platzhalter in einem String mit Service-Informationen.
     * @param value Der String mit Platzhaltern
     * @param group Die Gruppe (kann null sein)
     * @param service Der Service (kann null sein)
     * @return Der String mit ersetzten Platzhaltern
     */
    public static String fillPlaceholders(String value, String group, ServiceInfoSnapshot service) {
        return BridgeServiceHelper.fillCommonPlaceholders(value, group, service);
    }
}
