/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.banner;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.banner.BannerPattern;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticBannerPattern
extends AbstractMappedEntity
implements BannerPattern {
    private final ResourceLocation assetId;
    private final String translationKey;

    public StaticBannerPattern(ResourceLocation assetId, String translationKey) {
        this(null, assetId, translationKey);
    }

    @ApiStatus.Internal
    public StaticBannerPattern(@Nullable TypesBuilderData data, ResourceLocation assetId, String translationKey) {
        super(data);
        this.assetId = assetId;
        this.translationKey = translationKey;
    }

    @Override
    public BannerPattern copy(@Nullable TypesBuilderData newData) {
        return new StaticBannerPattern(newData, this.assetId, this.translationKey);
    }

    @Override
    public ResourceLocation getAssetId() {
        return this.assetId;
    }

    @Override
    public String getTranslationKey() {
        return this.translationKey;
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StaticBannerPattern)) {
            return false;
        }
        StaticBannerPattern that = (StaticBannerPattern)obj;
        if (!this.assetId.equals(that.assetId)) {
            return false;
        }
        return this.translationKey.equals(that.translationKey);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.assetId, this.translationKey);
    }

    @Override
    public String toString() {
        return "StaticBannerPattern{assetId=" + this.assetId + ", translationKey='" + this.translationKey + '\'' + '}';
    }
}

