/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package ac.grim.grimac.platform.bukkit.initables;

import ac.grim.grimac.manager.init.start.StartableInitable;
import ac.grim.grimac.platform.bukkit.GrimACBukkitLoaderPlugin;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.bstats.bukkit.Metrics;
import org.bukkit.plugin.Plugin;

public class BukkitBStats
implements StartableInitable {
    @Override
    public void start() {
        try {
            new Metrics((Plugin)GrimACBukkitLoaderPlugin.LOADER, 12820);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

