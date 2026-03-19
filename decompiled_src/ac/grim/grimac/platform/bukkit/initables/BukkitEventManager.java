/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package ac.grim.grimac.platform.bukkit.initables;

import ac.grim.grimac.manager.init.start.StartableInitable;
import ac.grim.grimac.platform.bukkit.GrimACBukkitLoaderPlugin;
import ac.grim.grimac.platform.bukkit.events.PistonEvent;
import ac.grim.grimac.utils.anticheat.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class BukkitEventManager
implements StartableInitable {
    @Override
    public void start() {
        LogUtil.info("Registering singular bukkit event... (PistonEvent)");
        Bukkit.getPluginManager().registerEvents((Listener)new PistonEvent(), (Plugin)GrimACBukkitLoaderPlugin.LOADER);
    }
}

