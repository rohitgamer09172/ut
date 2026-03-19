/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cow;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cow.CowVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticCowVariant
extends AbstractMappedEntity
implements CowVariant {
    private final CowVariant.ModelType modelType;
    private final ResourceLocation assetId;

    public StaticCowVariant(CowVariant.ModelType modelType, ResourceLocation assetId) {
        this(null, modelType, assetId);
    }

    @ApiStatus.Internal
    public StaticCowVariant(@Nullable TypesBuilderData data, CowVariant.ModelType modelType, ResourceLocation assetId) {
        super(data);
        this.modelType = modelType;
        this.assetId = assetId;
    }

    @Override
    public CowVariant copy(@Nullable TypesBuilderData newData) {
        return new StaticCowVariant(newData, this.modelType, this.assetId);
    }

    @Override
    public CowVariant.ModelType getModelType() {
        return this.modelType;
    }

    @Override
    public ResourceLocation getAssetId() {
        return this.assetId;
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (!(obj instanceof StaticCowVariant)) {
            return false;
        }
        StaticCowVariant that = (StaticCowVariant)obj;
        if (!this.modelType.equals((Object)that.modelType)) {
            return false;
        }
        return this.assetId.equals(that.assetId);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(new Object[]{this.modelType, this.assetId});
    }
}

