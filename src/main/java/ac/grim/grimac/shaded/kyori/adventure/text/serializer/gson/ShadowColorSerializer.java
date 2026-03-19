/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonParseException
 *  com.google.gson.TypeAdapter
 *  com.google.gson.stream.JsonReader
 *  com.google.gson.stream.JsonToken
 *  com.google.gson.stream.JsonWriter
 */
package ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson;

import ac.grim.grimac.shaded.kyori.adventure.text.format.ShadowColor;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.json.JSONOptions;
import ac.grim.grimac.shaded.kyori.option.OptionState;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

final class ShadowColorSerializer
extends TypeAdapter<ShadowColor> {
    private final boolean emitArray;

    static TypeAdapter<ShadowColor> create(OptionState options) {
        return new ShadowColorSerializer(options.value(JSONOptions.SHADOW_COLOR_MODE) == JSONOptions.ShadowColorEmitMode.EMIT_ARRAY).nullSafe();
    }

    private ShadowColorSerializer(boolean emitArray) {
        this.emitArray = emitArray;
    }

    public void write(JsonWriter out, ShadowColor value) throws IOException {
        if (this.emitArray) {
            out.beginArray().value((double)ShadowColorSerializer.componentAsFloat(value.red())).value((double)ShadowColorSerializer.componentAsFloat(value.green())).value((double)ShadowColorSerializer.componentAsFloat(value.blue())).value((double)ShadowColorSerializer.componentAsFloat(value.alpha())).endArray();
        } else {
            out.value((long)value.value());
        }
    }

    public ShadowColor read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.BEGIN_ARRAY) {
            in.beginArray();
            double r = in.nextDouble();
            double g = in.nextDouble();
            double b = in.nextDouble();
            double a = in.nextDouble();
            if (in.peek() != JsonToken.END_ARRAY) {
                throw new JsonParseException("Failed to parse shadow colour at " + in.getPath() + ": expected end of 4-element array but got " + in.peek() + " instead.");
            }
            in.endArray();
            return ShadowColor.shadowColor(ShadowColorSerializer.componentFromFloat(r), ShadowColorSerializer.componentFromFloat(g), ShadowColorSerializer.componentFromFloat(b), ShadowColorSerializer.componentFromFloat(a));
        }
        return ShadowColor.shadowColor(in.nextInt());
    }

    static float componentAsFloat(int element) {
        return (float)element / 255.0f;
    }

    static int componentFromFloat(double element) {
        return (int)((float)element * 255.0f);
    }
}

