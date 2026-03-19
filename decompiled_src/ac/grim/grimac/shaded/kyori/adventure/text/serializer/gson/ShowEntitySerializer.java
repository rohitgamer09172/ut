/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonParseException
 *  com.google.gson.TypeAdapter
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonToken
 *  com.google.gson.stream.JsonWriter
 */
package ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson;

import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.InvalidKeyException;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.event.HoverEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.SerializerFactory;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.json.JSONOptions;
import ac.grim.grimac.shaded.kyori.option.OptionState;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.UUID;

final class ShowEntitySerializer
extends TypeAdapter<HoverEvent.ShowEntity> {
    private final Gson gson;
    private final boolean emitKeyAsTypeAndUuidAsId;

    static TypeAdapter<HoverEvent.ShowEntity> create(Gson gson, OptionState opt) {
        return new ShowEntitySerializer(gson, opt.value(JSONOptions.EMIT_HOVER_SHOW_ENTITY_KEY_AS_TYPE_AND_UUID_AS_ID)).nullSafe();
    }

    private ShowEntitySerializer(Gson gson, boolean emitKeyAsTypeAndUuidAsId) {
        this.gson = gson;
        this.emitKeyAsTypeAndUuidAsId = emitKeyAsTypeAndUuidAsId;
    }

    public HoverEvent.ShowEntity read(JsonReader in) throws IOException {
        in.beginObject();
        Key type = null;
        UUID id = null;
        Component name = null;
        while (in.hasNext()) {
            String fieldName;
            switch (fieldName = in.nextName()) {
                case "id": {
                    if (in.peek() == JsonToken.BEGIN_ARRAY) {
                        id = (UUID)this.gson.fromJson(in, UUID.class);
                        break;
                    }
                    String string = in.nextString();
                    if (string.contains(":")) {
                        type = Key.key(string);
                    }
                    try {
                        id = UUID.fromString(string);
                    }
                    catch (IllegalArgumentException ignored) {
                        try {
                            type = Key.key(string);
                        }
                        catch (InvalidKeyException invalidKeyException) {}
                    }
                    break;
                }
                case "type": {
                    type = (Key)this.gson.fromJson(in, Key.class);
                    break;
                }
                case "uuid": {
                    id = (UUID)this.gson.fromJson(in, UUID.class);
                    break;
                }
                case "name": {
                    name = (Component)this.gson.fromJson(in, SerializerFactory.COMPONENT_TYPE);
                    break;
                }
                default: {
                    in.skipValue();
                }
            }
        }
        if (type == null || id == null) {
            throw new JsonParseException("A show entity hover event needs type and id fields to be deserialized");
        }
        in.endObject();
        return HoverEvent.ShowEntity.showEntity(type, id, name);
    }

    public void write(JsonWriter out, HoverEvent.ShowEntity value) throws IOException {
        out.beginObject();
        out.name(this.emitKeyAsTypeAndUuidAsId ? "type" : "id");
        this.gson.toJson((Object)value.type(), SerializerFactory.KEY_TYPE, out);
        out.name(this.emitKeyAsTypeAndUuidAsId ? "id" : "uuid");
        this.gson.toJson((Object)value.id(), SerializerFactory.UUID_TYPE, out);
        @Nullable Component name = value.name();
        if (name != null) {
            out.name("name");
            this.gson.toJson((Object)name, SerializerFactory.COMPONENT_TYPE, out);
        }
        out.endObject();
    }
}

