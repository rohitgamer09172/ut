/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Parsers {
    private static final VersionedRegistry<Parser> REGISTRY = new VersionedRegistry("command_argument_type");
    public static final Parser BRIGADIER_BOOL = Parsers.define("brigadier:bool", null, null);
    public static final Parser BRIGADIER_FLOAT = Parsers.define("brigadier:float", packetWrapper -> {
        byte flags = packetWrapper.readByte();
        float min = (flags & 1) != 0 ? packetWrapper.readFloat() : -3.4028235E38f;
        float max = (flags & 2) != 0 ? packetWrapper.readFloat() : Float.MAX_VALUE;
        return Arrays.asList(flags, Float.valueOf(min), Float.valueOf(max));
    }, (packetWrapper, properties) -> {
        byte flags = (Byte)properties.get(0);
        packetWrapper.writeByte(flags);
        if ((flags & 1) != 0) {
            packetWrapper.writeFloat(((Float)properties.get(1)).floatValue());
        }
        if ((flags & 2) != 0) {
            packetWrapper.writeFloat(((Float)properties.get(2)).floatValue());
        }
    });
    public static final Parser BRIGADIER_DOUBLE = Parsers.define("brigadier:double", packetWrapper -> {
        byte flags = packetWrapper.readByte();
        double min = (flags & 1) != 0 ? packetWrapper.readDouble() : -1.7976931348623157E308;
        double max = (flags & 2) != 0 ? packetWrapper.readDouble() : Double.MAX_VALUE;
        return Arrays.asList(flags, min, max);
    }, (packetWrapper, properties) -> {
        byte flags = (Byte)properties.get(0);
        packetWrapper.writeByte(flags);
        if ((flags & 1) != 0) {
            packetWrapper.writeDouble((Double)properties.get(1));
        }
        if ((flags & 2) != 0) {
            packetWrapper.writeDouble((Double)properties.get(2));
        }
    });
    public static final Parser BRIGADIER_INTEGER = Parsers.define("brigadier:integer", packetWrapper -> {
        byte flags = packetWrapper.readByte();
        int min = (flags & 1) != 0 ? packetWrapper.readInt() : Integer.MIN_VALUE;
        int max = (flags & 2) != 0 ? packetWrapper.readInt() : Integer.MAX_VALUE;
        return Arrays.asList(flags, min, max);
    }, (packetWrapper, properties) -> {
        byte flags = (Byte)properties.get(0);
        packetWrapper.writeByte(flags);
        if ((flags & 1) != 0) {
            packetWrapper.writeInt((Integer)properties.get(1));
        }
        if ((flags & 2) != 0) {
            packetWrapper.writeInt((Integer)properties.get(2));
        }
    });
    public static final Parser BRIGADIER_LONG = Parsers.define("brigadier:long", packetWrapper -> {
        byte flags = packetWrapper.readByte();
        long min = (flags & 1) != 0 ? packetWrapper.readLong() : Long.MIN_VALUE;
        long max = (flags & 2) != 0 ? packetWrapper.readLong() : Long.MAX_VALUE;
        return Arrays.asList(flags, min, max);
    }, (packetWrapper, properties) -> {
        byte flags = (Byte)properties.get(0);
        packetWrapper.writeByte(flags);
        if ((flags & 1) != 0) {
            packetWrapper.writeLong((Long)properties.get(1));
        }
        if ((flags & 2) != 0) {
            packetWrapper.writeLong((Long)properties.get(2));
        }
    });
    public static final Parser BRIGADIER_STRING = Parsers.define("brigadier:string", packetWrapper -> Collections.singletonList(packetWrapper.readVarInt()), (packetWrapper, properties) -> packetWrapper.writeVarInt((Integer)properties.get(0)));
    public static final Parser ENTITY = Parsers.define("entity", packetWrapper -> Collections.singletonList(packetWrapper.readByte()), (packetWrapper, properties) -> packetWrapper.writeByte(((Byte)properties.get(0)).intValue()));
    public static final Parser GAME_PROFILE = Parsers.define("game_profile", null, null);
    public static final Parser BLOCK_POS = Parsers.define("block_pos", null, null);
    public static final Parser COLUMN_POS = Parsers.define("column_pos", null, null);
    public static final Parser VEC3 = Parsers.define("vec3", null, null);
    public static final Parser VEC2 = Parsers.define("vec2", null, null);
    public static final Parser BLOCK_STATE = Parsers.define("block_state", null, null);
    public static final Parser BLOCK_PREDICATE = Parsers.define("block_predicate", null, null);
    public static final Parser ITEM_STACK = Parsers.define("item_stack", null, null);
    public static final Parser ITEM_PREDICATE = Parsers.define("item_predicate", null, null);
    public static final Parser COLOR = Parsers.define("color", null, null);
    public static final Parser COMPONENT = Parsers.define("component", null, null);
    public static final Parser STYLE = Parsers.define("style", null, null);
    public static final Parser MESSAGE = Parsers.define("message", null, null);
    public static final Parser NBT_COMPOUND_TAG = Parsers.define("nbt_compound_tag", null, null);
    @ApiStatus.Obsolete
    public static final Parser NBT = Parsers.define("nbt", null, null);
    public static final Parser NBT_TAG = Parsers.define("nbt_tag", null, null);
    public static final Parser NBT_PATH = Parsers.define("nbt_path", null, null);
    public static final Parser OBJECTIVE = Parsers.define("objective", null, null);
    public static final Parser OBJECTIVE_CRITERIA = Parsers.define("objective_criteria", null, null);
    public static final Parser OPERATION = Parsers.define("operation", null, null);
    public static final Parser PARTICLE = Parsers.define("particle", null, null);
    public static final Parser ANGLE = Parsers.define("angle", null, null);
    public static final Parser ROTATION = Parsers.define("rotation", null, null);
    public static final Parser SCOREBOARD_SLOT = Parsers.define("scoreboard_slot", null, null);
    public static final Parser SCORE_HOLDER = Parsers.define("score_holder", packetWrapper -> Collections.singletonList(packetWrapper.readByte()), (packetWrapper, properties) -> packetWrapper.writeByte(((Byte)properties.get(0)).intValue()));
    public static final Parser SWIZZLE = Parsers.define("swizzle", null, null);
    public static final Parser TEAM = Parsers.define("team", null, null);
    public static final Parser ITEM_SLOT = Parsers.define("item_slot", null, null);
    public static final Parser ITEM_SLOTS = Parsers.define("item_slots", null, null);
    public static final Parser RESOURCE_LOCATION = Parsers.define("resource_location", null, null);
    public static final Parser MOB_EFFECT = Parsers.define("mob_effect", null, null);
    public static final Parser FUNCTION = Parsers.define("function", null, null);
    public static final Parser ENTITY_ANCHOR = Parsers.define("entity_anchor", null, null);
    public static final Parser INT_RANGE = Parsers.define("int_range", null, null);
    public static final Parser FLOAT_RANGE = Parsers.define("float_range", null, null);
    public static final Parser ITEM_ENCHANTMENT = Parsers.define("item_enchantment", null, null);
    public static final Parser ENTITY_SUMMON = Parsers.define("entity_summon", null, null);
    public static final Parser DIMENSION = Parsers.define("dimension", null, null);
    public static final Parser GAMEMODE = Parsers.define("gamemode", null, null);
    public static final Parser TIME = Parsers.define("time", wrapper -> Collections.singletonList(wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_4) ? wrapper.readInt() : 0), (wrapper, properties) -> {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_4)) {
            wrapper.writeInt((Integer)properties.get(0));
        }
    });
    public static final Parser RESOURCE_OR_TAG = Parsers.define("resource_or_tag", packetWrapper -> Collections.singletonList(packetWrapper.readIdentifier()), (packetWrapper, properties) -> packetWrapper.writeIdentifier((ResourceLocation)properties.get(0)));
    public static final Parser RESOURCE_OR_TAG_KEY = Parsers.define("resource_or_tag_key", packetWrapper -> Collections.singletonList(packetWrapper.readIdentifier()), (packetWrapper, properties) -> packetWrapper.writeIdentifier((ResourceLocation)properties.get(0)));
    public static final Parser RESOURCE = Parsers.define("resource", packetWrapper -> Collections.singletonList(packetWrapper.readIdentifier()), (packetWrapper, properties) -> packetWrapper.writeIdentifier((ResourceLocation)properties.get(0)));
    public static final Parser RESOURCE_KEY = Parsers.define("resource_key", packetWrapper -> Collections.singletonList(packetWrapper.readIdentifier()), (packetWrapper, properties) -> packetWrapper.writeIdentifier((ResourceLocation)properties.get(0)));
    public static final Parser TEMPLATE_MIRROR = Parsers.define("template_mirror", null, null);
    public static final Parser TEMPLATE_ROTATION = Parsers.define("template_rotation", null, null);
    public static final Parser HEIGHTMAP = Parsers.define("heightmap", null, null);
    public static final Parser LOOT_TABLE = Parsers.define("loot_table", null, null);
    public static final Parser LOOT_PREDICATE = Parsers.define("loot_predicate", null, null);
    public static final Parser LOOT_MODIFIER = Parsers.define("loot_modifier", null, null);
    public static final Parser UUID = Parsers.define("uuid", null, null);
    public static final Parser RESOURCE_SELECTOR = Parsers.define("resource_selector", wrapper -> Collections.singletonList(wrapper.readIdentifier()), (wrapper, value) -> wrapper.writeIdentifier((ResourceLocation)value.get(0)));
    public static final Parser HEX_COLOR = Parsers.define("hex_color", null, null);
    public static final Parser DIALOG = Parsers.define("dialog", null, null);

    private Parsers() {
    }

    @ApiStatus.Internal
    public static Parser define(String key) {
        return Parsers.define(key, null, null);
    }

    @ApiStatus.Internal
    public static Parser define(String key, @Nullable Reader reader, @Nullable Writer writer) {
        return REGISTRY.define(key, data -> new Parser((TypesBuilderData)data, reader, writer));
    }

    public static Parser getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static Parser getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static List<Parser> getParsers() {
        return new ArrayList<Parser>(REGISTRY.getEntries());
    }

    public static VersionedRegistry<Parser> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }

    @FunctionalInterface
    public static interface Reader
    extends Function<PacketWrapper<?>, List<Object>> {
    }

    @FunctionalInterface
    public static interface Writer
    extends BiConsumer<PacketWrapper<?>, List<Object>> {
    }

    public static final class Parser
    extends AbstractMappedEntity {
        private final Reader reader;
        private final Writer writer;

        @Deprecated
        public Parser(String name, @Nullable Function<PacketWrapper<?>, List<Object>> read, @Nullable BiConsumer<PacketWrapper<?>, List<Object>> write) {
            this(new TypesBuilderData(new ResourceLocation(name), new int[0]), read == null ? null : read::apply, write == null ? null : write::accept);
        }

        @ApiStatus.Internal
        public Parser(@Nullable TypesBuilderData data, @Nullable Reader reader, @Nullable Writer writer) {
            super(data);
            this.reader = reader;
            this.writer = writer;
        }

        public Optional<List<Object>> readProperties(PacketWrapper<?> wrapper) {
            if (this.reader != null) {
                return Optional.of((List)this.reader.apply(wrapper));
            }
            return Optional.empty();
        }

        public void writeProperties(PacketWrapper<?> wrapper, List<Object> properties) {
            if (this.writer != null) {
                this.writer.accept(wrapper, properties);
            }
        }
    }
}

