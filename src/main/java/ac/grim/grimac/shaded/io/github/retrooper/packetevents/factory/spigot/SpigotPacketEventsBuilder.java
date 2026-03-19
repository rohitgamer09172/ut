/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.factory.spigot;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEventsAPI;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.injector.ChannelInjector;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.player.PlayerManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.NettyManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.settings.PacketEventsSettings;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.LogManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.PEVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.SynchronizedRegistriesHandler;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.updatechecker.UpdateChecker;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.bstats.bukkit.Metrics;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.bstats.charts.SimplePie;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.bukkit.InternalBukkitListener;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.bukkit.InternalBukkitLoginListener;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.bukkit.InternalGlobalBukkitListener;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.bukkit.InternalPaperJoinListener;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.bukkit.InternalPaperListener;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.SpigotChannelInjector;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.connection.ServerConnectionInitializer;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.manager.InternalBukkitPacketListener;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.manager.player.PlayerManagerImpl;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.manager.protocol.ProtocolManagerImpl;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.manager.server.ServerManagerImpl;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.netty.NettyManagerImpl;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.BukkitLogManager;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.protocolsupport.ProtocolSupportUtil;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion.CustomPipelineUtil;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class SpigotPacketEventsBuilder {
    private static PacketEventsAPI<Plugin> API_INSTANCE;

    public static void clearBuildCache() {
        API_INSTANCE = null;
    }

    public static PacketEventsAPI<Plugin> build(Plugin plugin) {
        if (API_INSTANCE == null) {
            API_INSTANCE = SpigotPacketEventsBuilder.buildNoCache(plugin);
        }
        return API_INSTANCE;
    }

    public static PacketEventsAPI<Plugin> build(Plugin plugin, PacketEventsSettings settings) {
        if (API_INSTANCE == null) {
            API_INSTANCE = SpigotPacketEventsBuilder.buildNoCache(plugin, settings);
        }
        return API_INSTANCE;
    }

    public static PacketEventsAPI<Plugin> buildNoCache(Plugin plugin) {
        return SpigotPacketEventsBuilder.buildNoCache(plugin, new PacketEventsSettings());
    }

    public static PacketEventsAPI<Plugin> buildNoCache(final Plugin plugin, final PacketEventsSettings inSettings) {
        return new PacketEventsAPI<Plugin>(){
            private final PacketEventsSettings settings;
            private final ProtocolManager protocolManager;
            private final ServerManager serverManager;
            private final PlayerManager playerManager;
            private final NettyManager nettyManager;
            private final SpigotChannelInjector injector;
            private final LogManager logManager;
            private boolean loaded;
            private boolean initialized;
            private boolean lateBind;
            private boolean terminated;
            {
                this.settings = inSettings;
                this.protocolManager = new ProtocolManagerImpl();
                this.serverManager = new ServerManagerImpl();
                this.playerManager = new PlayerManagerImpl();
                this.nettyManager = new NettyManagerImpl();
                this.injector = new SpigotChannelInjector();
                this.logManager = new BukkitLogManager();
                this.lateBind = false;
                this.terminated = false;
            }

            @Override
            public void load() {
                if (!this.loaded) {
                    String id = plugin.getName().toLowerCase(Locale.ROOT);
                    PacketEvents.IDENTIFIER = "pe-" + id;
                    PacketEvents.ENCODER_NAME = "pe-encoder-" + id;
                    PacketEvents.DECODER_NAME = "pe-decoder-" + id;
                    PacketEvents.CONNECTION_HANDLER_NAME = "pe-connection-handler-" + id;
                    PacketEvents.SERVER_CHANNEL_HANDLER_NAME = "pe-connection-initializer-" + id;
                    PacketEvents.TIMEOUT_HANDLER_NAME = "pe-timeout-handler-" + id;
                    try {
                        SpigotReflectionUtil.init();
                        CustomPipelineUtil.init();
                        WrappedBlockState.ensureLoad();
                        SynchronizedRegistriesHandler.init();
                    }
                    catch (Exception ex) {
                        throw new IllegalStateException(ex);
                    }
                    PacketType.prepare();
                    boolean bl = this.lateBind = !this.injector.isServerBound();
                    if (!this.lateBind) {
                        this.injector.inject();
                    }
                    this.loaded = true;
                    this.getEventManager().registerListener(new InternalBukkitPacketListener());
                }
            }

            @Override
            public boolean isLoaded() {
                return this.loaded;
            }

            @Override
            public void init() {
                this.load();
                if (!this.initialized) {
                    Plugin plugin2 = (Plugin)PacketEvents.getAPI().getPlugin();
                    String bukkitVersion = Bukkit.getBukkitVersion();
                    AtomicBoolean stopping = new AtomicBoolean(false);
                    BiConsumer<PEVersion, UpdateChecker.UpdateCheckerStatus> unsupportedSoftwareLogic = (peVersion, status) -> {
                        if (bukkitVersion.contains("Unknown")) {
                            ServerVersion fallbackVersion = ServerVersion.V_1_8_8;
                            String failureToDetectVersionMsg = "Your server software is preventing us from checking the Minecraft Server version. This is what we found: " + bukkitVersion + ". We will assume the Server version is " + fallbackVersion.name() + "...\n If you need assistance, join our Discord server: https://discord.gg/DVHxPPxHZc";
                            plugin2.getLogger().warning(failureToDetectVersionMsg);
                        } else {
                            PEVersion latestSupportedVersion;
                            PEVersion bukkitServerVersion = PEVersion.fromString(bukkitVersion.substring(0, bukkitVersion.indexOf("-")));
                            if (bukkitServerVersion.isNewerThan(latestSupportedVersion = PEVersion.fromString(ServerVersion.getLatest().getReleaseName()))) {
                                String developmentBuildsMsg = "Please test the development builds, as they may already have support for your Minecraft version (hint: select the build that contains 'spigot'): https://ci.codemc.io/job/retrooper/job/packetevents";
                                String releaseBuildsMsg = "Please test the latest stable release, as it should already have support for your Minecraft version: https://modrinth.com/plugin/packetevents";
                                String newBuildsMsg = status == UpdateChecker.UpdateCheckerStatus.OUTDATED || status == UpdateChecker.UpdateCheckerStatus.FAILED || status == null ? releaseBuildsMsg : developmentBuildsMsg;
                                plugin2.getLogger().warning("Your build of PacketEvents does not support the Minecraft version " + bukkitServerVersion + "! The latest Minecraft version supported by your build of PacketEvents is " + latestSupportedVersion + ". " + newBuildsMsg + " If you're in need of any help, join our Discord server: https://discord.gg/DVHxPPxHZc");
                                Bukkit.getPluginManager().disablePlugin(plugin2);
                                stopping.set(true);
                            }
                        }
                    };
                    if (this.settings.shouldCheckForUpdates()) {
                        this.getUpdateChecker().handleUpdateCheck(unsupportedSoftwareLogic);
                    } else {
                        unsupportedSoftwareLogic.accept(null, null);
                    }
                    if (stopping.get()) {
                        return;
                    }
                    Metrics metrics = new Metrics(plugin2, 11327);
                    metrics.addCustomChart(new SimplePie("packetevents_version", () -> this.getVersion().toStringWithoutSnapshot()));
                    Bukkit.getPluginManager().registerEvents((Listener)new InternalGlobalBukkitListener(), plugin2);
                    try {
                        Class.forName("io.papermc.paper.connection.PlayerConnection");
                        if (this.serverManager.getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
                            Bukkit.getPluginManager().registerEvents((Listener)new InternalPaperJoinListener(plugin2), plugin2);
                        } else {
                            Bukkit.getPluginManager().registerEvents((Listener)new InternalPaperListener(plugin2), plugin2);
                        }
                    }
                    catch (ClassNotFoundException ignored) {
                        if (this.serverManager.getVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
                            Bukkit.getPluginManager().registerEvents((Listener)new InternalBukkitLoginListener(), plugin2);
                        }
                        Bukkit.getPluginManager().registerEvents((Listener)new InternalBukkitListener(plugin2), plugin2);
                    }
                    if (this.lateBind) {
                        Runnable lateBindTask = () -> {
                            if (this.injector.isServerBound()) {
                                this.injector.inject();
                            }
                        };
                        FoliaScheduler.runTaskOnInit(plugin2, lateBindTask);
                    }
                    if (!"true".equalsIgnoreCase(System.getenv("PE_IGNORE_INCOMPATIBILITY"))) {
                        this.checkCompatibility();
                    }
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
                        SpigotChannelInjector injector = (SpigotChannelInjector)PacketEvents.getAPI().getInjector();
                        injector.updatePlayer(user, player);
                    }
                    this.initialized = true;
                }
            }

            private void checkCompatibility() {
                int majorVersion;
                Plugin protocolLibPlugin;
                ViaVersionUtil.checkIfViaIsPresent();
                ProtocolSupportUtil.checkIfProtocolSupportIsPresent();
                Plugin viaPlugin = Bukkit.getPluginManager().getPlugin("ViaVersion");
                if (viaPlugin != null) {
                    String[] ver = viaPlugin.getDescription().getVersion().split("\\.", 3);
                    int major = Integer.parseInt(ver[0]);
                    int minor = Integer.parseInt(ver[1]);
                    if (major < 4 || major == 4 && minor < 5) {
                        PacketEvents.getAPI().getLogManager().severe("You are attempting to combine 2.0 PacketEvents with a ViaVersion older than 4.5.0, please update your ViaVersion!");
                        Plugin ourPlugin = this.getPlugin();
                        Bukkit.getPluginManager().disablePlugin(ourPlugin);
                        throw new IllegalStateException("ViaVersion incompatibility! Update to v4.5.0 or newer!");
                    }
                }
                if ((protocolLibPlugin = Bukkit.getPluginManager().getPlugin("ProtocolLib")) != null && (majorVersion = Integer.parseInt(protocolLibPlugin.getDescription().getVersion().split("\\.", 2)[0])) < 5) {
                    PacketEvents.getAPI().getLogManager().severe("You are attempting to combine 2.0 PacketEvents with a ProtocolLib version older than v5.0.0. This is no longer works, please update to their dev builds. https://ci.dmulloy2.net/job/ProtocolLib/lastBuild/");
                    Plugin ourPlugin = this.getPlugin();
                    Bukkit.getPluginManager().disablePlugin(ourPlugin);
                    throw new IllegalStateException("ProtocolLib incompatibility! Update to v5.0.0 or newer!");
                }
            }

            @Override
            public boolean isInitialized() {
                return this.initialized;
            }

            @Override
            public void terminate() {
                if (this.initialized) {
                    this.injector.uninject();
                    for (User user : this.protocolManager.getUsers()) {
                        ServerConnectionInitializer.destroyHandlers(user.getChannel());
                    }
                    this.getEventManager().unregisterAllListeners();
                    this.initialized = false;
                    this.terminated = true;
                }
            }

            @Override
            public boolean isTerminated() {
                return this.terminated;
            }

            @Override
            public Plugin getPlugin() {
                return plugin;
            }

            @Override
            public ProtocolManager getProtocolManager() {
                return this.protocolManager;
            }

            @Override
            public ServerManager getServerManager() {
                return this.serverManager;
            }

            @Override
            public PlayerManager getPlayerManager() {
                return this.playerManager;
            }

            @Override
            public PacketEventsSettings getSettings() {
                return this.settings;
            }

            @Override
            public NettyManager getNettyManager() {
                return this.nettyManager;
            }

            @Override
            public ChannelInjector getInjector() {
                return this.injector;
            }

            @Override
            public LogManager getLogManager() {
                return this.logManager;
            }
        };
    }
}

