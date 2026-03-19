/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.armormaterial;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.armormaterial.ArmorMaterial;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.armormaterial.StaticArmorMaterial;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import java.util.HashMap;
import java.util.Map;

public final class ArmorMaterials {
    private static final Map<String, String> DFU = new HashMap<String, String>();
    private static final VersionedRegistry<ArmorMaterial> REGISTRY;
    public static final ArmorMaterial LEATHER;
    public static final ArmorMaterial CHAINMAIL;
    public static final ArmorMaterial IRON;
    public static final ArmorMaterial GOLD;
    public static final ArmorMaterial DIAMOND;
    public static final ArmorMaterial TURTLE_SCUTE;
    @Deprecated
    public static final ArmorMaterial TURTLE;
    public static final ArmorMaterial NETHERITE;
    public static final ArmorMaterial ARMADILLO_SCUTE;
    @Deprecated
    public static final ArmorMaterial ARMADILLO;
    public static final ArmorMaterial ELYTRA;
    public static final ArmorMaterial WHITE_CARPET;
    public static final ArmorMaterial ORANGE_CARPET;
    public static final ArmorMaterial MAGENTA_CARPET;
    public static final ArmorMaterial LIGHT_BLUE_CARPET;
    public static final ArmorMaterial YELLOW_CARPET;
    public static final ArmorMaterial LIME_CARPET;
    public static final ArmorMaterial PINK_CARPET;
    public static final ArmorMaterial GRAY_CARPET;
    public static final ArmorMaterial LIGHT_GRAY_CARPET;
    public static final ArmorMaterial CYAN_CARPET;
    public static final ArmorMaterial PURPLE_CARPET;
    public static final ArmorMaterial BLUE_CARPET;
    public static final ArmorMaterial BROWN_CARPET;
    public static final ArmorMaterial GREEN_CARPET;
    public static final ArmorMaterial RED_CARPET;
    public static final ArmorMaterial BLACK_CARPET;
    public static final ArmorMaterial TRADER_LLAMA;

    private ArmorMaterials() {
    }

    private static ArmorMaterial define(String name) {
        return REGISTRY.define(name, StaticArmorMaterial::new);
    }

    public static VersionedRegistry<ArmorMaterial> getRegistry() {
        return REGISTRY;
    }

    public static ArmorMaterial getByName(String name) {
        return REGISTRY.getByName(DFU.getOrDefault(name, name));
    }

    public static ArmorMaterial getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    static {
        DFU.put("turtle", "minecraft:turtle_scute");
        DFU.put("minecraft:turtle", "minecraft:turtle_scute");
        DFU.put("armadillo", "minecraft:armadillo_scute");
        DFU.put("minecraft:armadillo", "minecraft:armadillo_scute");
        REGISTRY = new VersionedRegistry("equipment_asset");
        LEATHER = ArmorMaterials.define("leather");
        CHAINMAIL = ArmorMaterials.define("chainmail");
        IRON = ArmorMaterials.define("iron");
        GOLD = ArmorMaterials.define("gold");
        DIAMOND = ArmorMaterials.define("diamond");
        TURTLE = TURTLE_SCUTE = ArmorMaterials.define("turtle_scute");
        NETHERITE = ArmorMaterials.define("netherite");
        ARMADILLO = ARMADILLO_SCUTE = ArmorMaterials.define("armadillo_scute");
        ELYTRA = ArmorMaterials.define("elytra");
        WHITE_CARPET = ArmorMaterials.define("white_carpet");
        ORANGE_CARPET = ArmorMaterials.define("orange_carpet");
        MAGENTA_CARPET = ArmorMaterials.define("magenta_carpet");
        LIGHT_BLUE_CARPET = ArmorMaterials.define("light_blue_carpet");
        YELLOW_CARPET = ArmorMaterials.define("yellow_carpet");
        LIME_CARPET = ArmorMaterials.define("lime_carpet");
        PINK_CARPET = ArmorMaterials.define("pink_carpet");
        GRAY_CARPET = ArmorMaterials.define("gray_carpet");
        LIGHT_GRAY_CARPET = ArmorMaterials.define("light_gray_carpet");
        CYAN_CARPET = ArmorMaterials.define("cyan_carpet");
        PURPLE_CARPET = ArmorMaterials.define("purple_carpet");
        BLUE_CARPET = ArmorMaterials.define("blue_carpet");
        BROWN_CARPET = ArmorMaterials.define("brown_carpet");
        GREEN_CARPET = ArmorMaterials.define("green_carpet");
        RED_CARPET = ArmorMaterials.define("red_carpet");
        BLACK_CARPET = ArmorMaterials.define("black_carpet");
        TRADER_LLAMA = ArmorMaterials.define("trader_llama");
        REGISTRY.unloadMappings();
    }
}

