/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.api;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.platform.api.PlatformServer;
import ac.grim.grimac.platform.api.command.CommandService;
import ac.grim.grimac.platform.api.manager.ItemResetHandler;
import ac.grim.grimac.platform.api.manager.MessagePlaceHolderManager;
import ac.grim.grimac.platform.api.manager.PermissionRegistrationManager;
import ac.grim.grimac.platform.api.manager.PlatformPluginManager;
import ac.grim.grimac.platform.api.player.PlatformPlayerFactory;
import ac.grim.grimac.platform.api.scheduler.PlatformScheduler;
import ac.grim.grimac.platform.api.sender.SenderFactory;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEventsAPI;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public interface PlatformLoader {
    public PlatformScheduler getScheduler();

    public PlatformPlayerFactory getPlatformPlayerFactory();

    public PacketEventsAPI<?> getPacketEvents();

    public ItemResetHandler getItemResetHandler();

    public CommandService getCommandService();

    public SenderFactory<?> getSenderFactory();

    public GrimPlugin getPlugin();

    public PlatformPluginManager getPluginManager();

    public PlatformServer getPlatformServer();

    public void registerAPIService();

    @NotNull
    public MessagePlaceHolderManager getMessagePlaceHolderManager();

    public PermissionRegistrationManager getPermissionManager();
}

