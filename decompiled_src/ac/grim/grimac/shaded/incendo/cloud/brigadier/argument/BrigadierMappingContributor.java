/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.incendo.cloud.brigadier.argument;

import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.CloudBrigadierManager;

public interface BrigadierMappingContributor {
    public <C, S> void contribute(CommandManager<C> var1, CloudBrigadierManager<C, S> var2);
}

