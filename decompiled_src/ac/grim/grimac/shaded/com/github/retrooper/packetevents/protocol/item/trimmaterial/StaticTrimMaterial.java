/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimmaterial;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.armormaterial.ArmorMaterial;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimmaterial.TrimMaterial;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticTrimMaterial
extends AbstractMappedEntity
implements TrimMaterial {
    private final String assetName;
    private final @Nullable ItemType ingredient;
    private final float itemModelIndex;
    private final Map<ArmorMaterial, String> overrideArmorMaterials;
    private final Component description;

    public StaticTrimMaterial(String assetName, @Nullable ItemType ingredient, Map<ArmorMaterial, String> overrideArmorMaterials, Component description) {
        this(null, assetName, ingredient, 0.0f, overrideArmorMaterials, description);
    }

    @ApiStatus.Internal
    public StaticTrimMaterial(@Nullable TypesBuilderData data, String assetName, @Nullable ItemType ingredient, Map<ArmorMaterial, String> overrideArmorMaterials, Component description) {
        this(data, assetName, ingredient, 0.0f, overrideArmorMaterials, description);
    }

    public StaticTrimMaterial(String assetName, @Nullable ItemType ingredient, float itemModelIndex, Map<ArmorMaterial, String> overrideArmorMaterials, Component description) {
        this(null, assetName, ingredient, itemModelIndex, overrideArmorMaterials, description);
    }

    @ApiStatus.Internal
    public StaticTrimMaterial(@Nullable TypesBuilderData data, String assetName, @Nullable ItemType ingredient, float itemModelIndex, Map<ArmorMaterial, String> overrideArmorMaterials, Component description) {
        super(data);
        this.assetName = assetName;
        this.ingredient = ingredient;
        this.itemModelIndex = itemModelIndex;
        this.overrideArmorMaterials = overrideArmorMaterials;
        this.description = description;
    }

    @Override
    public TrimMaterial copy(@Nullable TypesBuilderData newData) {
        return new StaticTrimMaterial(newData, this.assetName, this.ingredient, this.itemModelIndex, this.overrideArmorMaterials, this.description);
    }

    @Override
    public String getAssetName() {
        return this.assetName;
    }

    @Override
    @ApiStatus.Obsolete
    public ItemType getIngredient() {
        return this.ingredient != null ? this.ingredient : ItemTypes.AIR;
    }

    @Override
    public float getItemModelIndex() {
        return this.itemModelIndex;
    }

    @Override
    public Map<ArmorMaterial, String> getOverrideArmorMaterials() {
        return this.overrideArmorMaterials;
    }

    @Override
    public Component getDescription() {
        return this.description;
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StaticTrimMaterial)) {
            return false;
        }
        StaticTrimMaterial that = (StaticTrimMaterial)obj;
        if (Float.compare(that.itemModelIndex, this.itemModelIndex) != 0) {
            return false;
        }
        if (!this.assetName.equals(that.assetName)) {
            return false;
        }
        if (!Objects.equals(this.ingredient, that.ingredient)) {
            return false;
        }
        if (!this.overrideArmorMaterials.equals(that.overrideArmorMaterials)) {
            return false;
        }
        return this.description.equals(that.description);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.assetName, this.ingredient, Float.valueOf(this.itemModelIndex), this.overrideArmorMaterials, this.description);
    }

    @Override
    public String toString() {
        return "StaticTrimMaterial{assetName='" + this.assetName + '\'' + ", ingredient=" + this.ingredient + ", itemModelIndex=" + this.itemModelIndex + ", overrideArmorMaterials=" + this.overrideArmorMaterials + ", description=" + this.description + '}';
    }
}

