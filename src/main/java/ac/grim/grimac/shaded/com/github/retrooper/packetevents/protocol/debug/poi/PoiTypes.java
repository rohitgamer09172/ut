/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.poi;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.poi.PoiType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.poi.StaticPoiType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PoiTypes {
    private static final VersionedRegistry<PoiType> REGISTRY = new VersionedRegistry("point_of_interest_type");
    public static final PoiType ARMORER = PoiTypes.define("armorer");
    public static final PoiType BUTCHER = PoiTypes.define("butcher");
    public static final PoiType CARTOGRAPHER = PoiTypes.define("cartographer");
    public static final PoiType CLERIC = PoiTypes.define("cleric");
    public static final PoiType FARMER = PoiTypes.define("farmer");
    public static final PoiType FISHERMAN = PoiTypes.define("fisherman");
    public static final PoiType FLETCHER = PoiTypes.define("fletcher");
    public static final PoiType LEATHERWORKER = PoiTypes.define("leatherworker");
    public static final PoiType LIBRARIAN = PoiTypes.define("librarian");
    public static final PoiType MASON = PoiTypes.define("mason");
    public static final PoiType SHEPHERD = PoiTypes.define("shepherd");
    public static final PoiType TOOLSMITH = PoiTypes.define("toolsmith");
    public static final PoiType WEAPONSMITH = PoiTypes.define("weaponsmith");
    public static final PoiType HOME = PoiTypes.define("home");
    public static final PoiType MEETING = PoiTypes.define("meeting");
    public static final PoiType BEEHIVE = PoiTypes.define("beehive");
    public static final PoiType BEE_NEST = PoiTypes.define("bee_nest");
    public static final PoiType NETHER_PORTAL = PoiTypes.define("nether_portal");
    public static final PoiType LODESTONE = PoiTypes.define("lodestone");
    public static final PoiType TEST_INSTANCE = PoiTypes.define("test_instance");
    public static final PoiType LIGHTNING_ROD = PoiTypes.define("lightning_rod");

    private PoiTypes() {
    }

    public static VersionedRegistry<PoiType> getRegistry() {
        return REGISTRY;
    }

    @ApiStatus.Internal
    public static PoiType define(String name) {
        return REGISTRY.define(name, StaticPoiType::new);
    }

    static {
        REGISTRY.unloadMappings();
    }
}

