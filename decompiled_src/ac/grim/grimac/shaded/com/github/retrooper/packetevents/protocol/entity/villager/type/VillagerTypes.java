/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.type;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.type.StaticVillagerType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.type.VillagerType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Collection;

public final class VillagerTypes {
    private static final VersionedRegistry<VillagerType> REGISTRY = new VersionedRegistry("villager_type");
    public static final VillagerType DESERT = VillagerTypes.define("desert");
    public static final VillagerType JUNGLE = VillagerTypes.define("jungle");
    public static final VillagerType PLAINS = VillagerTypes.define("plains");
    public static final VillagerType SAVANNA = VillagerTypes.define("savanna");
    public static final VillagerType SNOW = VillagerTypes.define("snow");
    public static final VillagerType SWAMP = VillagerTypes.define("swamp");
    public static final VillagerType TAIGA = VillagerTypes.define("taiga");

    private VillagerTypes() {
    }

    public static VersionedRegistry<VillagerType> getRegistry() {
        return REGISTRY;
    }

    @Deprecated
    @ApiStatus.Internal
    public static VillagerType define(int id, String name) {
        return VillagerTypes.define(name);
    }

    @ApiStatus.Internal
    public static VillagerType define(String name) {
        return REGISTRY.define(name, StaticVillagerType::new);
    }

    @Deprecated
    public static VillagerType getById(int id) {
        ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
        return VillagerTypes.getById(version.toClientVersion(), id);
    }

    public static VillagerType getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static VillagerType getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static Collection<VillagerType> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
    }
}

