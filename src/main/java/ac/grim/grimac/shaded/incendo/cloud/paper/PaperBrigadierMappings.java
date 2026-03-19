/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apiguardian.api.API
 *  org.apiguardian.api.API$Status
 *  org.bukkit.World
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package ac.grim.grimac.shaded.incendo.cloud.paper;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.BukkitBrigadierMapper;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.CraftBukkitReflection;
import ac.grim.grimac.shaded.incendo.cloud.paper.parser.KeyedWorldParser;
import org.apiguardian.api.API;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(status=API.Status.INTERNAL)
final class PaperBrigadierMappings {
    private PaperBrigadierMappings() {
    }

    static <C> void register(@NonNull BukkitBrigadierMapper<C> mapper) {
        Class<World> keyed = CraftBukkitReflection.findClass("org.bukkit.Keyed");
        if (keyed != null && keyed.isAssignableFrom(World.class)) {
            mapper.mapSimpleNMS(new TypeToken<KeyedWorldParser<C>>(){}, "resource_location", true);
        }
    }
}

