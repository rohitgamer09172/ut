/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.profession;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.profession.StaticVillagerProfession;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.profession.VillagerProfession;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class VillagerProfessions {
    private static final VersionedRegistry<VillagerProfession> REGISTRY = new VersionedRegistry("villager_profession");
    public static final VillagerProfession NONE = VillagerProfessions.define("none");
    public static final VillagerProfession ARMORER = VillagerProfessions.define("armorer");
    public static final VillagerProfession BUTCHER = VillagerProfessions.define("butcher");
    public static final VillagerProfession CARTOGRAPHER = VillagerProfessions.define("cartographer");
    public static final VillagerProfession CLERIC = VillagerProfessions.define("cleric");
    public static final VillagerProfession FARMER = VillagerProfessions.define("farmer");
    public static final VillagerProfession FISHERMAN = VillagerProfessions.define("fisherman");
    public static final VillagerProfession FLETCHER = VillagerProfessions.define("fletcher");
    public static final VillagerProfession LEATHERWORKER = VillagerProfessions.define("leatherworker");
    public static final VillagerProfession LIBRARIAN = VillagerProfessions.define("librarian");
    public static final VillagerProfession MASON = VillagerProfessions.define("mason");
    public static final VillagerProfession NITWIT = VillagerProfessions.define("nitwit");
    public static final VillagerProfession SHEPHERD = VillagerProfessions.define("shepherd");
    public static final VillagerProfession TOOLSMITH = VillagerProfessions.define("toolsmith");
    public static final VillagerProfession WEAPONSMITH = VillagerProfessions.define("weaponsmith");

    private VillagerProfessions() {
    }

    public static VersionedRegistry<VillagerProfession> getRegistry() {
        return REGISTRY;
    }

    @Deprecated
    @ApiStatus.Internal
    public static VillagerProfession define(int id, String name) {
        return VillagerProfessions.define(name);
    }

    @ApiStatus.Internal
    public static VillagerProfession define(String name) {
        return REGISTRY.define(name, StaticVillagerProfession::new);
    }

    @Deprecated
    public static VillagerProfession getById(int id) {
        ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
        return VillagerProfessions.getById(version.toClientVersion(), id);
    }

    public static VillagerProfession getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static VillagerProfession getByName(String name) {
        return REGISTRY.getByName(name);
    }

    static {
        REGISTRY.unloadMappings();
    }
}

