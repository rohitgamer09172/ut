/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package ac.grim.grimac.shaded.incendo.cloud.brigadier.argument;

import ac.grim.grimac.shaded.incendo.cloud.brigadier.argument.BrigadierMapping;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.argument.BrigadierMappings;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import java.util.HashMap;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class BrigadierMappingsImpl<C, S>
implements BrigadierMappings<C, S> {
    private final Map<Class<?>, BrigadierMapping<?, ?, S>> mappers = new HashMap();

    BrigadierMappingsImpl() {
    }

    @Override
    public <T, K extends ArgumentParser<C, T>> @Nullable BrigadierMapping<C, K, S> mapping(@NonNull Class<K> parserType) {
        BrigadierMapping<?, ?, S> mapper = this.mappers.get(parserType);
        if (mapper == null) {
            return null;
        }
        return mapper;
    }

    @Override
    public <K extends ArgumentParser<C, ?>> void registerMappingUnsafe(@NonNull Class<K> parserType, @NonNull BrigadierMapping<?, ?, S> mapping) {
        this.mappers.put(parserType, mapping);
    }
}

