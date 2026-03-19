/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.InvalidKeyException;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.key.Keyed;
import ac.grim.grimac.shaded.kyori.adventure.nbt.api.BinaryTagHolder;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.event.DataComponentValue;
import ac.grim.grimac.shaded.kyori.adventure.text.event.HoverEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

final class HoverTag {
    private static final String HOVER = "hover";
    static final TagResolver RESOLVER = SerializableResolver.claimingStyle("hover", HoverTag::create, StyleClaim.claim("hover", Style::hoverEvent, HoverTag::emit));

    private HoverTag() {
    }

    static Tag create(ArgumentQueue args, Context ctx) throws ParsingException {
        String actionName = args.popOr("Hover event requires an action as its first argument").value();
        HoverEvent.Action<?> action = HoverEvent.Action.NAMES.value(actionName);
        ActionHandler<?> value = HoverTag.actionHandler(action);
        if (value == null) {
            throw ctx.newException("Don't know how to turn '" + args + "' into a hover event", args);
        }
        return Tag.styling(HoverEvent.hoverEvent(action, value.parse(args, ctx)));
    }

    static void emit(HoverEvent<?> event, TokenEmitter emitter) {
        ActionHandler<?> handler = HoverTag.actionHandler(event.action());
        emitter.tag(HOVER).argument(HoverEvent.Action.NAMES.key(event.action()));
        handler.emit(event.value(), emitter);
    }

    @Nullable
    static <V> ActionHandler<V> actionHandler(HoverEvent.Action<V> action) {
        ActionHandler<Component> ret = null;
        if (action == HoverEvent.Action.SHOW_TEXT) {
            ret = ShowText.INSTANCE;
        } else if (action == HoverEvent.Action.SHOW_ITEM) {
            ret = ShowItem.INSTANCE;
        } else if (action == HoverEvent.Action.SHOW_ENTITY) {
            ret = ShowEntity.INSTANCE;
        }
        return ret;
    }

    @NotNull
    static String compactAsString(@NotNull Key key) {
        if (key.namespace().equals("minecraft")) {
            return key.value();
        }
        return key.asString();
    }

    static interface ActionHandler<V> {
        @NotNull
        public V parse(@NotNull ArgumentQueue var1, @NotNull Context var2) throws ParsingException;

        public void emit(V var1, TokenEmitter var2);
    }

    static final class ShowText
    implements ActionHandler<Component> {
        private static final ShowText INSTANCE = new ShowText();

        private ShowText() {
        }

        @Override
        @NotNull
        public Component parse(@NotNull ArgumentQueue args, @NotNull Context ctx) throws ParsingException {
            return ctx.deserialize(args.popOr("show_text action requires a message").value());
        }

        @Override
        public void emit(Component event, TokenEmitter emit) {
            emit.argument(event);
        }
    }

    static final class ShowItem
    implements ActionHandler<HoverEvent.ShowItem> {
        private static final ShowItem INSTANCE = new ShowItem();

        private ShowItem() {
        }

        @Override
        public @NotNull HoverEvent.ShowItem parse(@NotNull ArgumentQueue args, @NotNull Context ctx) throws ParsingException {
            try {
                int count;
                Key key = Key.key(args.popOr("Show item hover needs at least an item ID").value());
                int n = count = args.hasNext() ? args.pop().asInt().orElseThrow(() -> ctx.newException("The count argument was not a valid integer")) : 1;
                if (args.hasNext()) {
                    String value = args.peek().value();
                    if (value.startsWith("{")) {
                        args.pop();
                        return ShowItem.legacyShowItem(key, count, value);
                    }
                    HashMap<Key, BinaryTagHolder> datas = new HashMap<Key, BinaryTagHolder>();
                    while (args.hasNext()) {
                        Key dataKey = Key.key(args.pop().value());
                        String dataVal = args.popOr("a value was expected for key " + dataKey).value();
                        datas.put(dataKey, BinaryTagHolder.binaryTagHolder(dataVal));
                    }
                    return HoverEvent.ShowItem.showItem((Keyed)key, count, datas);
                }
                return HoverEvent.ShowItem.showItem(key, count);
            }
            catch (InvalidKeyException | NumberFormatException ex) {
                throw ctx.newException("Exception parsing show_item hover", ex, args);
            }
        }

        private static @NotNull HoverEvent.ShowItem legacyShowItem(Key id, int count, String value) {
            return HoverEvent.ShowItem.showItem(id, count, BinaryTagHolder.binaryTagHolder(value));
        }

        @Override
        public void emit(HoverEvent.ShowItem event, TokenEmitter emit) {
            emit.argument(HoverTag.compactAsString(event.item()));
            if (event.count() != 1 || ShowItem.hasLegacy(event) || !event.dataComponents().isEmpty()) {
                emit.argument(Integer.toString(event.count()));
                if (ShowItem.hasLegacy(event)) {
                    ShowItem.emitLegacyHover(event, emit);
                } else {
                    for (Map.Entry<Key, DataComponentValue.TagSerializable> entry : event.dataComponentsAs(DataComponentValue.TagSerializable.class).entrySet()) {
                        emit.argument(entry.getKey().asMinimalString());
                        emit.argument(entry.getValue().asBinaryTag().string());
                    }
                }
            }
        }

        static boolean hasLegacy(HoverEvent.ShowItem event) {
            return event.nbt() != null;
        }

        static void emitLegacyHover(HoverEvent.ShowItem event, TokenEmitter emit) {
            if (event.nbt() != null) {
                emit.argument(event.nbt().string());
            }
        }
    }

    static final class ShowEntity
    implements ActionHandler<HoverEvent.ShowEntity> {
        static final ShowEntity INSTANCE = new ShowEntity();

        private ShowEntity() {
        }

        @Override
        public @NotNull HoverEvent.ShowEntity parse(@NotNull ArgumentQueue args, @NotNull Context ctx) throws ParsingException {
            try {
                Key key = Key.key(args.popOr("Show entity needs a type argument").value());
                UUID id = UUID.fromString(args.popOr("Show entity needs an entity UUID").value());
                if (args.hasNext()) {
                    Component name = ctx.deserialize(args.pop().value());
                    return HoverEvent.ShowEntity.showEntity(key, id, name);
                }
                return HoverEvent.ShowEntity.showEntity(key, id);
            }
            catch (InvalidKeyException | IllegalArgumentException ex) {
                throw ctx.newException("Exception parsing show_entity hover", ex, args);
            }
        }

        @Override
        public void emit(HoverEvent.ShowEntity event, TokenEmitter emit) {
            emit.argument(HoverTag.compactAsString(event.type())).argument(event.id().toString());
            if (event.name() != null) {
                emit.argument(event.name());
            }
        }
    }
}

