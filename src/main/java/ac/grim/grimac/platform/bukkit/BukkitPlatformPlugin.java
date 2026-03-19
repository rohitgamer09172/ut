/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package ac.grim.grimac.platform.bukkit;

import ac.grim.grimac.platform.api.PlatformPlugin;
import org.bukkit.plugin.Plugin;

public class BukkitPlatformPlugin
implements PlatformPlugin {
    private final Plugin plugin;

    public BukkitPlatformPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        return this.plugin.isEnabled();
    }

    @Override
    public String getName() {
        return this.plugin.getName();
    }

    @Override
    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }
}

