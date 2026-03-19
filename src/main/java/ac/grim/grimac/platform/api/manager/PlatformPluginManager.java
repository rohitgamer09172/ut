/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.api.manager;

import ac.grim.grimac.platform.api.PlatformPlugin;

public interface PlatformPluginManager {
    public PlatformPlugin[] getPlugins();

    public PlatformPlugin getPlugin(String var1);

    default public boolean isPluginEnabled(String pluginName) {
        PlatformPlugin plugin = this.getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }
}

