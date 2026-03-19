/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonPrimitive
 *  com.google.gson.internal.LazilyParsedNumber
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.codec;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufInputStream;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufOutputStream;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTEnd;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLimiter;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTShort;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer.DefaultNBTSerializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LazilyParsedNumber;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NBTCodec {
    @Deprecated
    public static NBT jsonToNBT(JsonElement element) {
        if (element instanceof JsonPrimitive) {
            if (((JsonPrimitive)element).isBoolean()) {
                return new NBTByte(element.getAsBoolean());
            }
            if (((JsonPrimitive)element).isString()) {
                return new NBTString(element.getAsString());
            }
            if (((JsonPrimitive)element).isNumber()) {
                Number num = element.getAsNumber();
                if (num instanceof Float) {
                    return new NBTFloat(num.floatValue());
                }
                if (num instanceof Double) {
                    return new NBTDouble(num.doubleValue());
                }
                if (num instanceof Byte) {
                    return new NBTByte(num.byteValue());
                }
                if (num instanceof Short) {
                    return new NBTShort(num.shortValue());
                }
                if (num instanceof Integer || num instanceof LazilyParsedNumber) {
                    return new NBTInt(num.intValue());
                }
                if (num instanceof Long) {
                    return new NBTLong(num.longValue());
                }
            }
        } else {
            if (element instanceof JsonArray) {
                ArrayList<NBT> list = new ArrayList<NBT>();
                for (JsonElement var : (JsonArray)element) {
                    list.add(NBTCodec.jsonToNBT(var));
                }
                if (list.isEmpty()) {
                    return new NBTList<NBTCompound>(NBTType.COMPOUND);
                }
                NBTList l = new NBTList(((NBT)list.get(0)).getType());
                for (NBT nbt : list) {
                    l.addTagUnsafe(nbt);
                }
                return l;
            }
            if (element instanceof JsonObject) {
                JsonObject obj = (JsonObject)element;
                NBTCompound compound = new NBTCompound();
                for (Map.Entry jsonEntry : obj.entrySet()) {
                    compound.setTag((String)jsonEntry.getKey(), NBTCodec.jsonToNBT((JsonElement)jsonEntry.getValue()));
                }
                return compound;
            }
            if (element instanceof JsonNull || element == null) {
                return new NBTCompound();
            }
        }
        throw new IllegalStateException("Failed to convert JSON to NBT " + element.toString());
    }

    @Deprecated
    public static JsonElement nbtToJson(NBT nbt, boolean parseByteAsBool) {
        if (nbt instanceof NBTNumber) {
            if (nbt instanceof NBTByte && parseByteAsBool) {
                byte val = ((NBTByte)nbt).getAsByte();
                if (val == 0) {
                    return new JsonPrimitive(Boolean.valueOf(false));
                }
                if (val == 1) {
                    return new JsonPrimitive(Boolean.valueOf(true));
                }
            }
            return new JsonPrimitive(((NBTNumber)nbt).getAsNumber());
        }
        if (nbt instanceof NBTString) {
            return new JsonPrimitive(((NBTString)nbt).getValue());
        }
        if (nbt instanceof NBTList) {
            NBTList list = (NBTList)nbt;
            JsonArray jsonArray = new JsonArray();
            list.getTags().forEach(tag -> jsonArray.add(NBTCodec.nbtToJson(tag, parseByteAsBool)));
            return jsonArray;
        }
        if (nbt instanceof NBTEnd) {
            throw new IllegalStateException("Encountered the NBTEnd tag during the NBT to JSON conversion: " + nbt.toString());
        }
        if (nbt instanceof NBTCompound) {
            JsonObject jsonObject = new JsonObject();
            Map<String, NBT> compoundTags = ((NBTCompound)nbt).getTags();
            for (Map.Entry<String, NBT> entry : compoundTags.entrySet()) {
                JsonElement jsonValue = NBTCodec.nbtToJson(entry.getValue(), parseByteAsBool);
                jsonObject.add(entry.getKey(), jsonValue);
            }
            return jsonObject;
        }
        throw new IllegalStateException("Failed to convert NBT to JSON.");
    }

    public static NBT readNBTFromBuffer(Object byteBuf, ServerVersion serverVersion) {
        NBTLimiter limiter = NBTLimiter.forBuffer(byteBuf);
        return NBTCodec.readNBTFromBuffer(byteBuf, serverVersion, limiter);
    }

    public static NBT readNBTFromBuffer(Object byteBuf, ServerVersion serverVersion, NBTLimiter limiter) {
        Object t;
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            try {
                boolean named = serverVersion.isOlderThan(ServerVersion.V_1_20_2);
                return DefaultNBTSerializer.INSTANCE.deserializeTag(limiter, new ByteBufInputStream(byteBuf), named);
            }
            catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        }
        short length = ByteBufHelper.readShort(byteBuf);
        if (length < 0) {
            return null;
        }
        Object slicedBuffer = ByteBufHelper.readSlice(byteBuf, length);
        DataInputStream stream = new DataInputStream(new GZIPInputStream(new ByteBufInputStream(slicedBuffer)));
        try {
            t = DefaultNBTSerializer.INSTANCE.deserializeTag(limiter, stream);
        }
        catch (Throwable throwable) {
            try {
                try {
                    stream.close();
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
            catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        }
        stream.close();
        return t;
    }

    public static void writeNBTToBuffer(Object byteBuf, ServerVersion serverVersion, NBTCompound tag) {
        NBTCodec.writeNBTToBuffer(byteBuf, serverVersion, (NBT)tag);
    }

    public static void writeNBTToBuffer(Object byteBuf, ServerVersion serverVersion, NBT tag) {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            try (ByteBufOutputStream outputStream = new ByteBufOutputStream(byteBuf);){
                if (tag != null) {
                    boolean named = serverVersion.isOlderThan(ServerVersion.V_1_20_2);
                    DefaultNBTSerializer.INSTANCE.serializeTag(outputStream, tag, named);
                }
                DefaultNBTSerializer.INSTANCE.serializeTag(outputStream, NBTEnd.INSTANCE);
            }
            catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } else if (tag == null) {
            ByteBufHelper.writeShort(byteBuf, -1);
        } else {
            int lengthWriterIndex = ByteBufHelper.writerIndex(byteBuf);
            ByteBufHelper.writeShort(byteBuf, 0);
            int writerIndexDataStart = ByteBufHelper.writerIndex(byteBuf);
            try (DataOutputStream outputstream = new DataOutputStream(new GZIPOutputStream(new ByteBufOutputStream(byteBuf)));){
                DefaultNBTSerializer.INSTANCE.serializeTag(outputstream, tag);
            }
            catch (Exception e) {
                throw new IllegalStateException(e);
            }
            int writerIndexDataEnd = ByteBufHelper.writerIndex(byteBuf);
            ByteBufHelper.writerIndex(byteBuf, lengthWriterIndex);
            ByteBufHelper.writeShort(byteBuf, writerIndexDataEnd - writerIndexDataStart);
            ByteBufHelper.writerIndex(byteBuf, writerIndexDataEnd);
        }
    }
}

