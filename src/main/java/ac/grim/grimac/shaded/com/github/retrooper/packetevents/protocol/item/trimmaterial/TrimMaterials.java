/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimmaterial;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.armormaterial.ArmorMaterial;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.armormaterial.ArmorMaterials;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimmaterial.StaticTrimMaterial;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimmaterial.TrimMaterial;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslatableComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class TrimMaterials {
    private static final VersionedRegistry<TrimMaterial> REGISTRY = new VersionedRegistry("trim_material");
    public static final TrimMaterial AMETHYST = TrimMaterials.define("amethyst", ItemTypes.AMETHYST_SHARD, 1.0f, 10116294);
    public static final TrimMaterial COPPER = TrimMaterials.define("copper", ItemTypes.COPPER_INGOT, 0.5f, 11823181);
    public static final TrimMaterial DIAMOND = TrimMaterials.define("diamond", ItemTypes.DIAMOND, 0.8f, 7269586);
    public static final TrimMaterial EMERALD = TrimMaterials.define("emerald", ItemTypes.EMERALD, 0.7f, 1155126);
    public static final TrimMaterial GOLD = TrimMaterials.define("gold", ItemTypes.GOLD_INGOT, 0.6f, 14594349);
    public static final TrimMaterial IRON = TrimMaterials.define("iron", ItemTypes.IRON_INGOT, 0.2f, 0xECECEC);
    public static final TrimMaterial LAPIS = TrimMaterials.define("lapis", ItemTypes.LAPIS_LAZULI, 0.9f, 4288151);
    public static final TrimMaterial NETHERITE = TrimMaterials.define("netherite", ItemTypes.NETHERITE_INGOT, 0.3f, 6445145);
    public static final TrimMaterial QUARTZ = TrimMaterials.define("quartz", ItemTypes.QUARTZ, 0.1f, 14931140);
    public static final TrimMaterial REDSTONE = TrimMaterials.define("redstone", ItemTypes.REDSTONE, 0.4f, 9901575);
    public static final TrimMaterial RESIN = TrimMaterials.define("resin", ItemTypes.RESIN_BRICK, 0.11f, 16545810);

    private TrimMaterials() {
    }

    @ApiStatus.Internal
    public static TrimMaterial define(String key, ItemType ingredient, float itemModelIndex, int color) {
        HashMap<ArmorMaterial, String> overrideArmorMaterials = new HashMap<ArmorMaterial, String>(2);
        String armorMaterialId = ResourceLocation.minecraft(key).toString();
        ArmorMaterial armorMaterial = ArmorMaterials.getByName(armorMaterialId);
        if (armorMaterial != null) {
            overrideArmorMaterials.put(armorMaterial, key + "_darker");
        }
        TranslatableComponent description = Component.translatable("trim_material.minecraft." + key, TextColor.color(color));
        return TrimMaterials.define(key, key, ingredient, itemModelIndex, overrideArmorMaterials, description);
    }

    @ApiStatus.Internal
    public static TrimMaterial define(String key, String assetName, ItemType ingredient, float itemModelIndex, Map<ArmorMaterial, String> overrideArmorMaterials, Component description) {
        return REGISTRY.define(key, data -> new StaticTrimMaterial((TypesBuilderData)data, assetName, ingredient, itemModelIndex, overrideArmorMaterials, description));
    }

    public static VersionedRegistry<TrimMaterial> getRegistry() {
        return REGISTRY;
    }

    public static TrimMaterial getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static TrimMaterial getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static Collection<TrimMaterial> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
    }
}

