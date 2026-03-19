/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureIndexUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentLike;
import ac.grim.grimac.shaded.kyori.adventure.text.format.NamedTextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextDecoration;
import ac.grim.grimac.shaded.kyori.adventure.util.Index;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class ChatTypeDecoration {
    private final String translationKey;
    private final List<Parameter> parameters;
    private final Style style;

    public ChatTypeDecoration(String translationKey, List<Parameter> parameters, Style style) {
        this.translationKey = translationKey;
        this.parameters = Collections.unmodifiableList(new ArrayList<Parameter>(parameters));
        this.style = style;
    }

    public static ChatTypeDecoration read(PacketWrapper<?> wrapper) {
        String translationKey = wrapper.readString();
        List<Parameter> parameters = wrapper.readList(ew -> (Parameter)ew.readEnum(Parameter.values()));
        Style style = wrapper.readStyle();
        return new ChatTypeDecoration(translationKey, parameters, style);
    }

    public static void write(PacketWrapper<?> wrapper, ChatTypeDecoration decoration) {
        wrapper.writeString(decoration.translationKey);
        wrapper.writeList(decoration.parameters, PacketWrapper::writeEnum);
        wrapper.writeStyle(decoration.style);
    }

    @Deprecated
    public static ChatTypeDecoration decode(NBT nbt, ClientVersion version) {
        return ChatTypeDecoration.decode(nbt, PacketWrapper.createDummyWrapper(version));
    }

    public static ChatTypeDecoration decode(NBT nbt, PacketWrapper<?> wrapper) {
        NBTCompound styleTag;
        ArrayList<Parameter> params;
        NBTCompound compound = (NBTCompound)nbt;
        String translationKey = compound.getStringTagValueOrThrow("translation_key");
        NBT paramsTag = compound.getTagOrThrow("parameters");
        if (paramsTag instanceof NBTList) {
            params = new ArrayList();
            NBTList paramsTagList = (NBTList)paramsTag;
            for (NBT paramTag : paramsTagList.getTags()) {
                String paramId = ((NBTString)paramTag).getValue();
                params.add(AdventureIndexUtil.indexValueOrThrow(Parameter.ID_INDEX, paramId));
            }
        } else {
            String paramId = ((NBTString)paramsTag).getValue();
            params = Collections.singletonList(AdventureIndexUtil.indexValueOrThrow(Parameter.ID_INDEX, paramId));
        }
        Style style = (styleTag = compound.getCompoundTagOrNull("style")) == null ? Style.empty() : wrapper.getSerializers().nbt().deserializeStyle(styleTag, wrapper);
        return new ChatTypeDecoration(translationKey, params, style);
    }

    @Deprecated
    public static NBT encode(ChatTypeDecoration decoration, ClientVersion version) {
        return ChatTypeDecoration.encode(decoration, PacketWrapper.createDummyWrapper(version));
    }

    public static NBT encode(ChatTypeDecoration decoration, PacketWrapper<?> wrapper) {
        NBTList<NBTString> paramsTag = NBTList.createStringList();
        for (Parameter param : decoration.parameters) {
            paramsTag.addTag(new NBTString(param.getId()));
        }
        NBTCompound compound = new NBTCompound();
        compound.setTag("translation_key", new NBTString(decoration.translationKey));
        compound.setTag("parameters", paramsTag);
        if (!decoration.style.isEmpty()) {
            compound.setTag("style", wrapper.getSerializers().nbt().serializeStyle(decoration.style, wrapper));
        }
        return compound;
    }

    public static ChatTypeDecoration withSender(String translationKey) {
        return new ChatTypeDecoration(translationKey, Arrays.asList(Parameter.SENDER, Parameter.CONTENT), Style.empty());
    }

    public static ChatTypeDecoration incomingDirectMessage(String translationKey) {
        return new ChatTypeDecoration(translationKey, Arrays.asList(Parameter.SENDER, Parameter.CONTENT), Style.style((TextColor)NamedTextColor.GRAY, TextDecoration.ITALIC));
    }

    public static ChatTypeDecoration outgoingDirectMessage(String translationKey) {
        return new ChatTypeDecoration(translationKey, Arrays.asList(Parameter.TARGET, Parameter.CONTENT), Style.style((TextColor)NamedTextColor.GRAY, TextDecoration.ITALIC));
    }

    public static ChatTypeDecoration teamMessage(String translationKey) {
        return new ChatTypeDecoration(translationKey, Arrays.asList(Parameter.TARGET, Parameter.SENDER, Parameter.CONTENT), Style.empty());
    }

    public Component decorate(Component component, ChatType.Bound chatType) {
        ComponentLike[] components = new ComponentLike[this.parameters.size()];
        for (int i = 0; i < components.length; ++i) {
            Parameter parameter = this.parameters.get(i);
            components[i] = (ComponentLike)parameter.selector.apply(component, chatType);
        }
        return Component.translatable(this.translationKey, null, this.style, components);
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    public List<Parameter> getParameters() {
        return this.parameters;
    }

    public Style getStyle() {
        return this.style;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChatTypeDecoration)) {
            return false;
        }
        ChatTypeDecoration that = (ChatTypeDecoration)obj;
        if (!this.translationKey.equals(that.translationKey)) {
            return false;
        }
        if (!this.parameters.equals(that.parameters)) {
            return false;
        }
        return this.style.equals(that.style);
    }

    public int hashCode() {
        return Objects.hash(this.translationKey, this.parameters, this.style);
    }

    public static enum Parameter {
        SENDER("sender", (component, type) -> type.getName()),
        TARGET("target", (component, type) -> type.getTargetName() != null ? type.getTargetName() : Component.empty()),
        CONTENT("content", (component, type) -> component);

        public static final Index<String, Parameter> ID_INDEX;
        private final String id;
        private final BiFunction<Component, ChatType.Bound, Component> selector;

        private Parameter(String id, BiFunction<Component, ChatType.Bound, Component> selector) {
            this.id = id;
            this.selector = selector;
        }

        public String getId() {
            return this.id;
        }

        @Deprecated
        @Nullable
        public static Parameter valueByName(String id) {
            return ID_INDEX.value(id);
        }

        static {
            ID_INDEX = Index.create(Parameter.class, Parameter::getId);
        }
    }
}

