/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.TypeAdapter
 *  com.google.gson.TypeAdapterFactory
 *  com.google.gson.reflect.TypeToken
 */
package ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson;

import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.BlockNBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslationArgument;
import ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.event.HoverEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.format.ShadowColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextDecoration;
import ac.grim.grimac.shaded.kyori.adventure.text.object.PlayerHeadObjectContents;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.BlockNBTComponentPosSerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.ClickEventActionSerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.ComponentSerializerImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.HoverEventActionSerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.KeySerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.ProfilePropertySerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.ShadowColorSerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.ShowEntitySerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.ShowItemSerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.StyleSerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.TextColorSerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.TextColorWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.TextDecorationSerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.TranslationArgumentSerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.UUIDSerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.json.JSONOptions;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer;
import ac.grim.grimac.shaded.kyori.option.OptionState;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.util.UUID;

final class SerializerFactory
implements TypeAdapterFactory {
    static final Class<Key> KEY_TYPE = Key.class;
    static final Class<Component> COMPONENT_TYPE = Component.class;
    static final Class<Style> STYLE_TYPE = Style.class;
    static final Class<ClickEvent.Action> CLICK_ACTION_TYPE = ClickEvent.Action.class;
    static final Class<HoverEvent.Action> HOVER_ACTION_TYPE = HoverEvent.Action.class;
    static final Class<HoverEvent.ShowItem> SHOW_ITEM_TYPE = HoverEvent.ShowItem.class;
    static final Class<HoverEvent.ShowEntity> SHOW_ENTITY_TYPE = HoverEvent.ShowEntity.class;
    static final Class<String> STRING_TYPE = String.class;
    static final Class<TextColorWrapper> COLOR_WRAPPER_TYPE = TextColorWrapper.class;
    static final Class<TextColor> COLOR_TYPE = TextColor.class;
    static final Class<ShadowColor> SHADOW_COLOR_TYPE = ShadowColor.class;
    static final Class<TextDecoration> TEXT_DECORATION_TYPE = TextDecoration.class;
    static final Class<BlockNBTComponent.Pos> BLOCK_NBT_POS_TYPE = BlockNBTComponent.Pos.class;
    static final Class<UUID> UUID_TYPE = UUID.class;
    static final Class<TranslationArgument> TRANSLATION_ARGUMENT_TYPE = TranslationArgument.class;
    static final Class<PlayerHeadObjectContents.ProfileProperty> PROFILE_PROPERTY_TYPE = PlayerHeadObjectContents.ProfileProperty.class;
    private final OptionState features;
    private final LegacyHoverEventSerializer legacyHoverSerializer;

    SerializerFactory(OptionState features, @Nullable LegacyHoverEventSerializer legacyHoverSerializer) {
        this.features = features;
        this.legacyHoverSerializer = legacyHoverSerializer;
    }

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class rawType = type.getRawType();
        if (COMPONENT_TYPE.isAssignableFrom(rawType)) {
            return ComponentSerializerImpl.create(this.features, gson);
        }
        if (KEY_TYPE.isAssignableFrom(rawType)) {
            return KeySerializer.INSTANCE;
        }
        if (STYLE_TYPE.isAssignableFrom(rawType)) {
            return StyleSerializer.create(this.legacyHoverSerializer, this.features, gson);
        }
        if (CLICK_ACTION_TYPE.isAssignableFrom(rawType)) {
            return ClickEventActionSerializer.INSTANCE;
        }
        if (HOVER_ACTION_TYPE.isAssignableFrom(rawType)) {
            return HoverEventActionSerializer.INSTANCE;
        }
        if (SHOW_ITEM_TYPE.isAssignableFrom(rawType)) {
            return ShowItemSerializer.create(gson, this.features);
        }
        if (SHOW_ENTITY_TYPE.isAssignableFrom(rawType)) {
            return ShowEntitySerializer.create(gson, this.features);
        }
        if (COLOR_WRAPPER_TYPE.isAssignableFrom(rawType)) {
            return TextColorWrapper.Serializer.INSTANCE;
        }
        if (COLOR_TYPE.isAssignableFrom(rawType)) {
            return this.features.value(JSONOptions.EMIT_RGB) != false ? TextColorSerializer.INSTANCE : TextColorSerializer.DOWNSAMPLE_COLOR;
        }
        if (SHADOW_COLOR_TYPE.isAssignableFrom(rawType)) {
            return ShadowColorSerializer.create(this.features);
        }
        if (TEXT_DECORATION_TYPE.isAssignableFrom(rawType)) {
            return TextDecorationSerializer.INSTANCE;
        }
        if (BLOCK_NBT_POS_TYPE.isAssignableFrom(rawType)) {
            return BlockNBTComponentPosSerializer.INSTANCE;
        }
        if (UUID_TYPE.isAssignableFrom(rawType)) {
            return UUIDSerializer.uuidSerializer(this.features);
        }
        if (TRANSLATION_ARGUMENT_TYPE.isAssignableFrom(rawType)) {
            return TranslationArgumentSerializer.create(gson);
        }
        if (PROFILE_PROPERTY_TYPE.isAssignableFrom(rawType)) {
            return ProfilePropertySerializer.INSTANCE;
        }
        return null;
    }
}

