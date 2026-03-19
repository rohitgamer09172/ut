/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.ServicePriority
 *  org.bukkit.plugin.java.JavaPlugin
 */
package ac.grim.grimac.platform.bukkit;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.GrimExternalAPI;
import ac.grim.grimac.api.GrimAPIProvider;
import ac.grim.grimac.api.GrimAbstractAPI;
import ac.grim.grimac.api.event.EventBus;
import ac.grim.grimac.api.event.events.CommandExecuteEvent;
import ac.grim.grimac.api.event.events.FlagEvent;
import ac.grim.grimac.api.event.events.GrimJoinEvent;
import ac.grim.grimac.api.event.events.GrimQuitEvent;
import ac.grim.grimac.api.events.CompletePredictionEvent;
import ac.grim.grimac.api.events.GrimReloadEvent;
import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.command.CloudCommandService;
import ac.grim.grimac.internal.platform.bukkit.resolver.BukkitResolverRegistrar;
import ac.grim.grimac.manager.init.Initable;
import ac.grim.grimac.manager.init.start.ExemptOnlinePlayersOnReload;
import ac.grim.grimac.platform.api.Platform;
import ac.grim.grimac.platform.api.PlatformLoader;
import ac.grim.grimac.platform.api.PlatformServer;
import ac.grim.grimac.platform.api.command.CommandService;
import ac.grim.grimac.platform.api.manager.ItemResetHandler;
import ac.grim.grimac.platform.api.manager.MessagePlaceHolderManager;
import ac.grim.grimac.platform.api.manager.PlatformPluginManager;
import ac.grim.grimac.platform.api.manager.cloud.CloudCommandAdapter;
import ac.grim.grimac.platform.api.player.PlatformPlayerFactory;
import ac.grim.grimac.platform.api.scheduler.PlatformScheduler;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.platform.api.sender.SenderFactory;
import ac.grim.grimac.platform.bukkit.BukkitPlatformServer;
import ac.grim.grimac.platform.bukkit.initables.BukkitBStats;
import ac.grim.grimac.platform.bukkit.initables.BukkitEventManager;
import ac.grim.grimac.platform.bukkit.initables.BukkitTickEndEvent;
import ac.grim.grimac.platform.bukkit.manager.BukkitItemResetHandler;
import ac.grim.grimac.platform.bukkit.manager.BukkitMessagePlaceHolderManager;
import ac.grim.grimac.platform.bukkit.manager.BukkitParserDescriptorFactory;
import ac.grim.grimac.platform.bukkit.manager.BukkitPermissionRegistrationManager;
import ac.grim.grimac.platform.bukkit.manager.BukkitPlatformPluginManager;
import ac.grim.grimac.platform.bukkit.player.BukkitPlatformPlayerFactory;
import ac.grim.grimac.platform.bukkit.scheduler.bukkit.BukkitPlatformScheduler;
import ac.grim.grimac.platform.bukkit.scheduler.folia.FoliaPlatformScheduler;
import ac.grim.grimac.platform.bukkit.sender.BukkitSenderFactory;
import ac.grim.grimac.platform.bukkit.utils.placeholder.PlaceholderAPIExpansion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEventsAPI;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.BrigadierSetting;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.CloudBrigadierManager;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.CloudBukkitCapabilities;
import ac.grim.grimac.shaded.incendo.cloud.execution.ExecutionCoordinator;
import ac.grim.grimac.shaded.incendo.cloud.paper.LegacyPaperCommandManager;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.lazy.LazyHolder;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class GrimACBukkitLoaderPlugin
extends JavaPlugin
implements PlatformLoader {
    public static GrimACBukkitLoaderPlugin LOADER;
    private final LazyHolder<PlatformScheduler> scheduler = LazyHolder.simple(this::createScheduler);
    private final LazyHolder<PacketEventsAPI<?>> packetEvents = LazyHolder.simple(() -> SpigotPacketEventsBuilder.build((Plugin)this));
    private final LazyHolder<BukkitSenderFactory> senderFactory = LazyHolder.simple(BukkitSenderFactory::new);
    private final LazyHolder<ItemResetHandler> itemResetHandler = LazyHolder.simple(BukkitItemResetHandler::new);
    private final LazyHolder<CommandService> commandService = LazyHolder.simple(this::createCommandService);
    private final CloudCommandAdapter commandAdapter = new BukkitParserDescriptorFactory();
    private final PlatformPlayerFactory platformPlayerFactory = new BukkitPlatformPlayerFactory();
    private final PlatformPluginManager pluginManager = new BukkitPlatformPluginManager();
    private final GrimPlugin plugin;
    private final PlatformServer platformServer = new BukkitPlatformServer();
    private final MessagePlaceHolderManager messagePlaceHolderManager = new BukkitMessagePlaceHolderManager();
    private final BukkitPermissionRegistrationManager permissionManager = new BukkitPermissionRegistrationManager();

    public GrimACBukkitLoaderPlugin() {
        BukkitResolverRegistrar registrar = new BukkitResolverRegistrar();
        registrar.registerAll(GrimAPI.INSTANCE.getExtensionManager());
        this.plugin = registrar.resolvePlugin((Plugin)this);
    }

    public void onLoad() {
        LOADER = this;
        GrimAPI.INSTANCE.load(this, this.getBukkitInitTasks());
    }

    private Initable[] getBukkitInitTasks() {
        return new Initable[]{new ExemptOnlinePlayersOnReload(), new BukkitEventManager(), new BukkitTickEndEvent(), new BukkitBStats(), () -> {
            if (BukkitMessagePlaceHolderManager.hasPlaceholderAPI) {
                new PlaceholderAPIExpansion().register();
            }
        }};
    }

    public void onEnable() {
        GrimAPI.INSTANCE.start();
    }

    public void onDisable() {
        GrimAPI.INSTANCE.stop();
    }

    @Override
    public PlatformScheduler getScheduler() {
        return this.scheduler.get();
    }

    @Override
    public PacketEventsAPI<?> getPacketEvents() {
        return this.packetEvents.get();
    }

    @Override
    public ItemResetHandler getItemResetHandler() {
        return this.itemResetHandler.get();
    }

    @Override
    public CommandService getCommandService() {
        return this.commandService.get();
    }

    public SenderFactory<CommandSender> getSenderFactory() {
        return this.senderFactory.get();
    }

    @Override
    public void registerAPIService() {
        GrimExternalAPI externalAPI = GrimAPI.INSTANCE.getExternalAPI();
        EventBus eventBus = externalAPI.getEventBus();
        GrimPlugin context = GrimAPI.INSTANCE.getGrimPlugin();
        eventBus.subscribe(context, GrimJoinEvent.class, event -> {
            ac.grim.grimac.api.events.GrimJoinEvent bukkitEvent = new ac.grim.grimac.api.events.GrimJoinEvent(event.getUser());
            Bukkit.getPluginManager().callEvent((Event)bukkitEvent);
        });
        eventBus.subscribe(context, GrimQuitEvent.class, event -> {
            ac.grim.grimac.api.events.GrimQuitEvent bukkitEvent = new ac.grim.grimac.api.events.GrimQuitEvent(event.getUser());
            Bukkit.getPluginManager().callEvent((Event)bukkitEvent);
        });
        eventBus.subscribe(context, ac.grim.grimac.api.event.events.GrimReloadEvent.class, event -> {
            GrimReloadEvent bukkitEvent = new GrimReloadEvent(event.isSuccess());
            Bukkit.getPluginManager().callEvent((Event)bukkitEvent);
        });
        eventBus.subscribe(context, FlagEvent.class, event -> {
            ac.grim.grimac.api.events.FlagEvent bukkitEvent = new ac.grim.grimac.api.events.FlagEvent(event.getUser(), event.getCheck(), event.getVerbose());
            Bukkit.getPluginManager().callEvent((Event)bukkitEvent);
            if (bukkitEvent.isCancelled()) {
                event.setCancelled(true);
            }
        });
        eventBus.subscribe(context, CommandExecuteEvent.class, event -> {
            ac.grim.grimac.api.events.CommandExecuteEvent bukkitEvent = new ac.grim.grimac.api.events.CommandExecuteEvent(event.getUser(), event.getCheck(), event.getVerbose(), event.getCommand());
            Bukkit.getPluginManager().callEvent((Event)bukkitEvent);
            if (bukkitEvent.isCancelled()) {
                event.setCancelled(true);
            }
        });
        eventBus.subscribe(context, ac.grim.grimac.api.event.events.CompletePredictionEvent.class, event -> {
            CompletePredictionEvent bukkitEvent = new CompletePredictionEvent(event.getUser(), event.getCheck(), "", event.getOffset());
            Bukkit.getPluginManager().callEvent((Event)bukkitEvent);
            if (bukkitEvent.isCancelled()) {
                event.setCancelled(true);
            }
        });
        GrimAPIProvider.init(externalAPI);
        Bukkit.getServicesManager().register(GrimAbstractAPI.class, (Object)externalAPI, (Plugin)LOADER, ServicePriority.Normal);
    }

    private PlatformScheduler createScheduler() {
        return GrimAPI.INSTANCE.getPlatform() == Platform.FOLIA ? new FoliaPlatformScheduler() : new BukkitPlatformScheduler();
    }

    private CommandService createCommandService() {
        try {
            return new CloudCommandService(this::createCloudCommandManager, this.commandAdapter);
        }
        catch (Throwable t) {
            LogUtil.warn("CRITICAL: Failed to initialize Command Framework. Grim will continue to run with no commands.", t);
            return () -> {};
        }
    }

    private CommandManager<Sender> createCloudCommandManager() {
        LegacyPaperCommandManager<Sender> manager = new LegacyPaperCommandManager<Sender>((Plugin)this, ExecutionCoordinator.simpleCoordinator(), this.senderFactory.get());
        if (manager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            try {
                manager.registerBrigadier();
                CloudBrigadierManager cbm = manager.brigadierManager();
                cbm.settings().set(BrigadierSetting.FORCE_EXECUTABLE, true);
            }
            catch (Throwable t) {
                LogUtil.error("Failed to register Brigadier native completions. Falling back to standard completions.", t);
            }
        } else if (manager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            manager.registerAsynchronousCompletions();
        }
        return manager;
    }

    public BukkitSenderFactory getBukkitSenderFactory() {
        return GrimACBukkitLoaderPlugin.LOADER.senderFactory.get();
    }

    @Override
    @Generated
    public PlatformPlayerFactory getPlatformPlayerFactory() {
        return this.platformPlayerFactory;
    }

    @Override
    @Generated
    public PlatformPluginManager getPluginManager() {
        return this.pluginManager;
    }

    @Override
    @Generated
    public GrimPlugin getPlugin() {
        return this.plugin;
    }

    @Override
    @Generated
    public PlatformServer getPlatformServer() {
        return this.platformServer;
    }

    @Override
    @Generated
    public MessagePlaceHolderManager getMessagePlaceHolderManager() {
        return this.messagePlaceHolderManager;
    }

    @Override
    @Generated
    public BukkitPermissionRegistrationManager getPermissionManager() {
        return this.permissionManager;
    }
}

