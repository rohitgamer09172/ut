/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.mapdecoration;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.mapdecoration.MapDecorationType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.mapdecoration.StaticMapDecorationType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Collection;

public final class MapDecorationTypes {
    private static final VersionedRegistry<MapDecorationType> REGISTRY = new VersionedRegistry("map_decoration_type");
    private static final int LIGHT_GRAY_COLOR = 0x999999;
    private static final int COPPER_COLOR = 12741452;
    public static final MapDecorationType PLAYER = MapDecorationTypes.define("player", false, true);
    public static final MapDecorationType FRAME = MapDecorationTypes.define("frame", true, true);
    public static final MapDecorationType RED_MARKER = MapDecorationTypes.define("red_marker", false, true);
    public static final MapDecorationType BLUE_MARKER = MapDecorationTypes.define("blue_marker", false, true);
    public static final MapDecorationType TARGET_X = MapDecorationTypes.define("target_x", true, false);
    public static final MapDecorationType TARGET_POINT = MapDecorationTypes.define("target_point", true, false);
    public static final MapDecorationType PLAYER_OFF_MAP = MapDecorationTypes.define("player_off_map", false, true);
    public static final MapDecorationType PLAYER_OFF_LIMITS = MapDecorationTypes.define("player_off_limits", false, true);
    public static final MapDecorationType MANSION = MapDecorationTypes.define("mansion", true, 5393476, true, false);
    public static final MapDecorationType MONUMENT = MapDecorationTypes.define("monument", true, 3830373, true, false);
    public static final MapDecorationType BANNER_WHITE = MapDecorationTypes.define("banner_white", ResourceLocation.minecraft("white_banner"), true, true);
    public static final MapDecorationType BANNER_ORANGE = MapDecorationTypes.define("banner_orange", ResourceLocation.minecraft("orange_banner"), true, true);
    public static final MapDecorationType BANNER_MAGENTA = MapDecorationTypes.define("banner_magenta", ResourceLocation.minecraft("magenta_banner"), true, true);
    public static final MapDecorationType BANNER_LIGHT_BLUE = MapDecorationTypes.define("banner_light_blue", ResourceLocation.minecraft("light_blue_banner"), true, true);
    public static final MapDecorationType BANNER_YELLOW = MapDecorationTypes.define("banner_yellow", ResourceLocation.minecraft("yellow_banner"), true, true);
    public static final MapDecorationType BANNER_LIME = MapDecorationTypes.define("banner_lime", ResourceLocation.minecraft("lime_banner"), true, true);
    public static final MapDecorationType BANNER_PINK = MapDecorationTypes.define("banner_pink", ResourceLocation.minecraft("pink_banner"), true, true);
    public static final MapDecorationType BANNER_GRAY = MapDecorationTypes.define("banner_gray", ResourceLocation.minecraft("gray_banner"), true, true);
    public static final MapDecorationType BANNER_LIGHT_GRAY = MapDecorationTypes.define("banner_light_gray", ResourceLocation.minecraft("light_gray_banner"), true, true);
    public static final MapDecorationType BANNER_CYAN = MapDecorationTypes.define("banner_cyan", ResourceLocation.minecraft("cyan_banner"), true, true);
    public static final MapDecorationType BANNER_PURPLE = MapDecorationTypes.define("banner_purple", ResourceLocation.minecraft("purple_banner"), true, true);
    public static final MapDecorationType BANNER_BLUE = MapDecorationTypes.define("banner_blue", ResourceLocation.minecraft("blue_banner"), true, true);
    public static final MapDecorationType BANNER_BROWN = MapDecorationTypes.define("banner_brown", ResourceLocation.minecraft("brown_banner"), true, true);
    public static final MapDecorationType BANNER_GREEN = MapDecorationTypes.define("banner_green", ResourceLocation.minecraft("green_banner"), true, true);
    public static final MapDecorationType BANNER_RED = MapDecorationTypes.define("banner_red", ResourceLocation.minecraft("red_banner"), true, true);
    public static final MapDecorationType BANNER_BLACK = MapDecorationTypes.define("banner_black", ResourceLocation.minecraft("black_banner"), true, true);
    public static final MapDecorationType RED_X = MapDecorationTypes.define("red_x", true, false);
    public static final MapDecorationType VILLAGE_DESERT = MapDecorationTypes.define("village_desert", ResourceLocation.minecraft("desert_village"), true, 0x999999, true, false);
    public static final MapDecorationType VILLAGE_PLAINS = MapDecorationTypes.define("village_plains", ResourceLocation.minecraft("plains_village"), true, 0x999999, true, false);
    public static final MapDecorationType VILLAGE_SAVANNA = MapDecorationTypes.define("village_savanna", ResourceLocation.minecraft("savanna_village"), true, 0x999999, true, false);
    public static final MapDecorationType VILLAGE_SNOWY = MapDecorationTypes.define("village_snowy", ResourceLocation.minecraft("snowy_village"), true, 0x999999, true, false);
    public static final MapDecorationType VILLAGE_TAIGA = MapDecorationTypes.define("village_taiga", ResourceLocation.minecraft("taiga_village"), true, 0x999999, true, false);
    public static final MapDecorationType JUNGLE_TEMPLE = MapDecorationTypes.define("jungle_temple", true, 0x999999, true, false);
    public static final MapDecorationType SWAMP_HUT = MapDecorationTypes.define("swamp_hut", true, 0x999999, true, false);
    public static final MapDecorationType TRIAL_CHAMBERS = MapDecorationTypes.define("trial_chambers", true, 12741452, true, false);

    private MapDecorationTypes() {
    }

    @ApiStatus.Internal
    public static MapDecorationType define(String key, boolean showOnItemFrame, boolean trackCount) {
        return MapDecorationTypes.define(key, ResourceLocation.minecraft(key), showOnItemFrame, trackCount);
    }

    @ApiStatus.Internal
    public static MapDecorationType define(String key, ResourceLocation assetId, boolean showOnItemFrame, boolean trackCount) {
        return MapDecorationTypes.define(key, assetId, showOnItemFrame, -1, false, trackCount);
    }

    @ApiStatus.Internal
    public static MapDecorationType define(String key, boolean showOnItemFrame, int mapColor, boolean explorationMapElement, boolean trackCount) {
        return MapDecorationTypes.define(key, ResourceLocation.minecraft(key), showOnItemFrame, mapColor, explorationMapElement, trackCount);
    }

    @ApiStatus.Internal
    public static MapDecorationType define(String key, ResourceLocation assetId, boolean showOnItemFrame, int mapColor, boolean explorationMapElement, boolean trackCount) {
        return REGISTRY.define(key, data -> new StaticMapDecorationType((TypesBuilderData)data, assetId, showOnItemFrame, mapColor, explorationMapElement, trackCount));
    }

    public static VersionedRegistry<MapDecorationType> getRegistry() {
        return REGISTRY;
    }

    @Nullable
    public static MapDecorationType getByName(String name) {
        return REGISTRY.getByName(name);
    }

    @Nullable
    public static MapDecorationType getById(int id, ClientVersion version) {
        return REGISTRY.getById(version, id);
    }

    @Nullable
    public static MapDecorationType getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static Collection<MapDecorationType> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
    }
}

