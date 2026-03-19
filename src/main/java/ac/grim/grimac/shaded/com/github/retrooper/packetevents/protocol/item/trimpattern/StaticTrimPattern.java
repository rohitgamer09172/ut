/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimpattern;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimpattern.TrimPattern;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticTrimPattern
extends AbstractMappedEntity
implements TrimPattern {
    private final ResourceLocation assetId;
    private final @Nullable ItemType templateItem;
    private final Component description;
    private final boolean decal;

    public StaticTrimPattern(ResourceLocation assetId, @Nullable ItemType templateItem, Component description, boolean decal) {
        this(null, assetId, templateItem, description, decal);
    }

    @ApiStatus.Internal
    public StaticTrimPattern(@Nullable TypesBuilderData data, ResourceLocation assetId, @Nullable ItemType templateItem, Component description, boolean decal) {
        super(data);
        this.assetId = assetId;
        this.templateItem = templateItem;
        this.description = description;
        this.decal = decal;
    }

    @Override
    public TrimPattern copy(@Nullable TypesBuilderData newData) {
        return new StaticTrimPattern(newData, this.assetId, this.templateItem, this.description, this.decal);
    }

    @Override
    public ResourceLocation getAssetId() {
        return this.assetId;
    }

    @Override
    @ApiStatus.Obsolete
    public ItemType getTemplateItem() {
        return this.templateItem != null ? this.templateItem : ItemTypes.AIR;
    }

    @Override
    public Component getDescription() {
        return this.description;
    }

    @Override
    public boolean isDecal() {
        return this.decal;
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StaticTrimPattern)) {
            return false;
        }
        StaticTrimPattern that = (StaticTrimPattern)obj;
        if (this.decal != that.decal) {
            return false;
        }
        if (!this.assetId.equals(that.assetId)) {
            return false;
        }
        if (!Objects.equals(this.templateItem, that.templateItem)) {
            return false;
        }
        return this.description.equals(that.description);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.assetId, this.templateItem, this.description, this.decal);
    }

    @Override
    public String toString() {
        return "StaticTrimPattern{assetId=" + this.assetId + ", templateItem=" + this.templateItem + ", description=" + this.description + ", decal=" + this.decal + '}';
    }
}

