/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonElement
 */
package ac.grim.grimac.shaded.kyori.adventure.platform.bukkit;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import ac.grim.grimac.shaded.kyori.adventure.platform.bukkit.MinecraftReflection;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.ComponentSerializer;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

@ApiStatus.Experimental
public final class MinecraftComponentSerializer
implements ComponentSerializer<Component, Component, Object> {
    private static final MinecraftComponentSerializer INSTANCE = new MinecraftComponentSerializer();
    @Nullable
    private static final Class<?> CLASS_JSON_DESERIALIZER = MinecraftReflection.findClass("com.goo".concat("gle.gson.JsonDeserializer"));
    @Nullable
    private static final Class<?> CLASS_JSON_ELEMENT = MinecraftReflection.findClass("com.goo".concat("gle.gson.JsonElement"));
    @Nullable
    private static final Class<?> CLASS_JSON_PARSER = MinecraftReflection.findClass("com.goo".concat("gle.gson.JsonParser"));
    @Nullable
    private static final Class<?> CLASS_CHAT_COMPONENT = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("IChatBaseComponent"), MinecraftReflection.findMcClassName("network.chat.IChatBaseComponent"), MinecraftReflection.findMcClassName("network.chat.Component"));
    @Nullable
    private static final Class<?> CLASS_CRAFT_REGISTRY = MinecraftReflection.findCraftClass("CraftRegistry");
    @Nullable
    private static final Class<?> CLASS_REGISTRY_ACCESS = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("core.IRegistryCustom"), MinecraftReflection.findMcClassName("core.RegistryAccess"));
    @Nullable
    private static final MethodHandle PARSE_JSON = MinecraftReflection.findMethod(CLASS_JSON_PARSER, "parse", CLASS_JSON_ELEMENT, String.class);
    @Nullable
    private static final MethodHandle GET_REGISTRY = MinecraftReflection.findStaticMethod(CLASS_CRAFT_REGISTRY, "getMinecraftRegistry", CLASS_REGISTRY_ACCESS, new Class[0]);
    private static final AtomicReference<RuntimeException> INITIALIZATION_ERROR = new AtomicReference<UnsupportedOperationException>(new UnsupportedOperationException());
    private static final Object JSON_PARSER_INSTANCE;
    private static final Object MC_TEXT_GSON;
    private static final MethodHandle TEXT_SERIALIZER_DESERIALIZE;
    private static final MethodHandle TEXT_SERIALIZER_SERIALIZE;
    private static final MethodHandle TEXT_SERIALIZER_DESERIALIZE_TREE;
    private static final MethodHandle TEXT_SERIALIZER_SERIALIZE_TREE;
    private static final boolean SUPPORTED;

    public static boolean isSupported() {
        return SUPPORTED;
    }

    @NotNull
    public static MinecraftComponentSerializer get() {
        return INSTANCE;
    }

    @Override
    @NotNull
    public Component deserialize(@NotNull Object input) {
        if (!SUPPORTED) {
            throw INITIALIZATION_ERROR.get();
        }
        try {
            Object element;
            if (TEXT_SERIALIZER_SERIALIZE_TREE != null) {
                element = TEXT_SERIALIZER_SERIALIZE_TREE.invoke(input);
            } else if (MC_TEXT_GSON != null) {
                element = ((Gson)MC_TEXT_GSON).toJsonTree(input);
            } else {
                return BukkitComponentSerializer.gson().deserialize(TEXT_SERIALIZER_SERIALIZE.invoke(input));
            }
            return (Component)BukkitComponentSerializer.gson().serializer().fromJson(element.toString(), Component.class);
        }
        catch (Throwable error) {
            throw new UnsupportedOperationException(error);
        }
    }

    @Override
    @NotNull
    public Object serialize(@NotNull Component component) {
        if (!SUPPORTED) {
            throw INITIALIZATION_ERROR.get();
        }
        if (TEXT_SERIALIZER_DESERIALIZE_TREE != null || MC_TEXT_GSON != null) {
            JsonElement json = BukkitComponentSerializer.gson().serializer().toJsonTree((Object)component);
            try {
                if (TEXT_SERIALIZER_DESERIALIZE_TREE != null) {
                    Object unRelocatedJsonElement = PARSE_JSON.invoke(JSON_PARSER_INSTANCE, json.toString());
                    return TEXT_SERIALIZER_DESERIALIZE_TREE.invoke(unRelocatedJsonElement);
                }
                return ((Gson)MC_TEXT_GSON).fromJson(json, CLASS_CHAT_COMPONENT);
            }
            catch (Throwable error) {
                throw new UnsupportedOperationException(error);
            }
        }
        try {
            return TEXT_SERIALIZER_DESERIALIZE.invoke((String)BukkitComponentSerializer.gson().serialize(component));
        }
        catch (Throwable error) {
            throw new UnsupportedOperationException(error);
        }
    }

    static {
        Object gson = null;
        Object jsonParserInstance = null;
        MethodHandle textSerializerDeserialize = null;
        MethodHandle textSerializerSerialize = null;
        MethodHandle textSerializerDeserializeTree = null;
        MethodHandle textSerializerSerializeTree = null;
        try {
            if (CLASS_JSON_PARSER != null) {
                jsonParserInstance = CLASS_JSON_PARSER.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            }
            if (CLASS_CHAT_COMPONENT != null) {
                Field gsonField;
                Object registryAccess = GET_REGISTRY != null ? GET_REGISTRY.invoke() : null;
                Class<?> chatSerializerClass = Arrays.stream(CLASS_CHAT_COMPONENT.getClasses()).filter(c -> {
                    if (CLASS_JSON_DESERIALIZER != null) {
                        return CLASS_JSON_DESERIALIZER.isAssignableFrom((Class<?>)c);
                    }
                    for (Class<?> itf : c.getInterfaces()) {
                        if (!itf.getSimpleName().equals("JsonDeserializer")) continue;
                        return true;
                    }
                    return false;
                }).findAny().orElse(MinecraftReflection.findNmsClass("ChatSerializer"));
                if (chatSerializerClass != null && (gsonField = (Field)Arrays.stream(chatSerializerClass.getDeclaredFields()).filter(m -> Modifier.isStatic(m.getModifiers())).filter(m -> m.getType().equals(Gson.class)).findFirst().orElse(null)) != null) {
                    gsonField.setAccessible(true);
                    gson = gsonField.get(null);
                }
                ArrayList candidates = new ArrayList();
                if (chatSerializerClass != null) {
                    candidates.add(chatSerializerClass);
                }
                candidates.addAll(Arrays.asList(CLASS_CHAT_COMPONENT.getClasses()));
                for (Class clazz : candidates) {
                    Method[] declaredMethods = clazz.getDeclaredMethods();
                    Method deserialize = Arrays.stream(declaredMethods).filter(m -> Modifier.isStatic(m.getModifiers())).filter(m -> CLASS_CHAT_COMPONENT.isAssignableFrom(m.getReturnType())).filter(m -> m.getParameterCount() == 1 && m.getParameterTypes()[0].equals(String.class)).min(Comparator.comparing(Method::getName)).orElse(null);
                    Method serialize = Arrays.stream(declaredMethods).filter(m -> Modifier.isStatic(m.getModifiers())).filter(m -> m.getReturnType().equals(String.class)).filter(m -> m.getParameterCount() == 1 && CLASS_CHAT_COMPONENT.isAssignableFrom(m.getParameterTypes()[0])).findFirst().orElse(null);
                    Method deserializeTree = Arrays.stream(declaredMethods).filter(m -> Modifier.isStatic(m.getModifiers())).filter(m -> CLASS_CHAT_COMPONENT.isAssignableFrom(m.getReturnType())).filter(m -> m.getParameterCount() == 1 && m.getParameterTypes()[0].equals(CLASS_JSON_ELEMENT)).findFirst().orElse(null);
                    Method serializeTree = Arrays.stream(declaredMethods).filter(m -> Modifier.isStatic(m.getModifiers())).filter(m -> m.getReturnType().equals(CLASS_JSON_ELEMENT)).filter(m -> m.getParameterCount() == 1 && CLASS_CHAT_COMPONENT.isAssignableFrom(m.getParameterTypes()[0])).findFirst().orElse(null);
                    Method deserializeTreeWithRegistryAccess = Arrays.stream(declaredMethods).filter(m -> Modifier.isStatic(m.getModifiers())).filter(m -> CLASS_CHAT_COMPONENT.isAssignableFrom(m.getReturnType())).filter(m -> m.getParameterCount() == 2).filter(m -> m.getParameterTypes()[0].equals(CLASS_JSON_ELEMENT)).filter(m -> m.getParameterTypes()[1].isInstance(registryAccess)).findFirst().orElse(null);
                    Method serializeTreeWithRegistryAccess = Arrays.stream(declaredMethods).filter(m -> Modifier.isStatic(m.getModifiers())).filter(m -> m.getReturnType().equals(CLASS_JSON_ELEMENT)).filter(m -> m.getParameterCount() == 2).filter(m -> CLASS_CHAT_COMPONENT.isAssignableFrom(m.getParameterTypes()[0])).filter(m -> m.getParameterTypes()[1].isInstance(registryAccess)).findFirst().orElse(null);
                    if (deserialize != null) {
                        textSerializerDeserialize = MinecraftReflection.lookup().unreflect(deserialize);
                    }
                    if (serialize != null) {
                        textSerializerSerialize = MinecraftReflection.lookup().unreflect(serialize);
                    }
                    if (deserializeTree != null) {
                        textSerializerDeserializeTree = MinecraftReflection.lookup().unreflect(deserializeTree);
                    } else if (deserializeTreeWithRegistryAccess != null) {
                        deserializeTreeWithRegistryAccess.setAccessible(true);
                        textSerializerDeserializeTree = MethodHandles.insertArguments(MinecraftReflection.lookup().unreflect(deserializeTreeWithRegistryAccess), 1, registryAccess);
                    }
                    if (serializeTree != null) {
                        textSerializerSerializeTree = MinecraftReflection.lookup().unreflect(serializeTree);
                        continue;
                    }
                    if (serializeTreeWithRegistryAccess == null) continue;
                    serializeTreeWithRegistryAccess.setAccessible(true);
                    textSerializerSerializeTree = MethodHandles.insertArguments(MinecraftReflection.lookup().unreflect(serializeTreeWithRegistryAccess), 1, registryAccess);
                }
            }
        }
        catch (Throwable error) {
            INITIALIZATION_ERROR.set(new UnsupportedOperationException("Error occurred during initialization", error));
        }
        MC_TEXT_GSON = gson;
        JSON_PARSER_INSTANCE = jsonParserInstance;
        TEXT_SERIALIZER_DESERIALIZE = textSerializerDeserialize;
        TEXT_SERIALIZER_SERIALIZE = textSerializerSerialize;
        TEXT_SERIALIZER_DESERIALIZE_TREE = textSerializerDeserializeTree;
        TEXT_SERIALIZER_SERIALIZE_TREE = textSerializerSerializeTree;
        SUPPORTED = MC_TEXT_GSON != null || TEXT_SERIALIZER_DESERIALIZE != null && TEXT_SERIALIZER_SERIALIZE != null || TEXT_SERIALIZER_DESERIALIZE_TREE != null && TEXT_SERIALIZER_SERIALIZE_TREE != null;
    }
}

