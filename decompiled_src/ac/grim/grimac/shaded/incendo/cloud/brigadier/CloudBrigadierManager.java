/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.BoolArgumentType
 *  com.mojang.brigadier.arguments.DoubleArgumentType
 *  com.mojang.brigadier.arguments.FloatArgumentType
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.arguments.LongArgumentType
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  org.apiguardian.api.API
 *  org.apiguardian.api.API$Status
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package ac.grim.grimac.shaded.incendo.cloud.brigadier;

import ac.grim.grimac.shaded.geantyref.GenericTypeReflector;
import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.SenderMapper;
import ac.grim.grimac.shaded.incendo.cloud.SenderMapperHolder;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.BrigadierSetting;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.argument.ArgumentTypeFactory;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.argument.BrigadierMapping;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.argument.BrigadierMappingBuilder;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.argument.BrigadierMappingContributor;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.argument.BrigadierMappings;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.node.LiteralBrigadierNodeFactory;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.parser.WrappedBrigadierParser;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion.TooltipSuggestion;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.flag.CommandFlagParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.BooleanParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.ByteParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.DoubleParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.FloatParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.IntegerParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.LongParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.ShortParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.StringArrayParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.StringParser;
import ac.grim.grimac.shaded.incendo.cloud.setting.Configurable;
import ac.grim.grimac.shaded.incendo.cloud.type.range.ByteRange;
import ac.grim.grimac.shaded.incendo.cloud.type.range.DoubleRange;
import ac.grim.grimac.shaded.incendo.cloud.type.range.FloatRange;
import ac.grim.grimac.shaded.incendo.cloud.type.range.IntRange;
import ac.grim.grimac.shaded.incendo.cloud.type.range.LongRange;
import ac.grim.grimac.shaded.incendo.cloud.type.range.ShortRange;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import org.apiguardian.api.API;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class CloudBrigadierManager<C, S>
implements SenderMapperHolder<S, C> {
    private final BrigadierMappings<C, S> brigadierMappings = BrigadierMappings.create();
    private final LiteralBrigadierNodeFactory<C, S> literalBrigadierNodeFactory;
    private final Map<@NonNull Class<?>, @NonNull ArgumentTypeFactory<?>> defaultArgumentTypeSuppliers;
    private final Configurable<BrigadierSetting> settings = Configurable.enumConfigurable(BrigadierSetting.class);
    private final SenderMapper<S, C> brigadierSourceMapper;

    public CloudBrigadierManager(@NonNull CommandManager<C> commandManager, @NonNull SenderMapper<S, C> brigadierSourceMapper) {
        this.brigadierSourceMapper = Objects.requireNonNull(brigadierSourceMapper, "brigadierSourceMapper");
        this.defaultArgumentTypeSuppliers = new HashMap();
        this.literalBrigadierNodeFactory = new LiteralBrigadierNodeFactory(this, commandManager, commandManager.suggestionFactory().mapped(TooltipSuggestion::tooltipSuggestion));
        this.registerInternalMappings();
        ServiceLoader<BrigadierMappingContributor> loader = ServiceLoader.load(BrigadierMappingContributor.class, BrigadierMappingContributor.class.getClassLoader());
        loader.iterator().forEachRemaining(contributor -> contributor.contribute(commandManager, this));
        commandManager.registerCommandPreProcessor(ctx -> {
            if (!ctx.commandContext().contains("_cloud_brigadier_native_sender")) {
                ctx.commandContext().store("_cloud_brigadier_native_sender", this.brigadierSourceMapper.reverse(ctx.commandContext().sender()));
            }
        });
    }

    private void registerInternalMappings() {
        this.registerMapping(new TypeToken<ByteParser<C>>(){}, builder -> builder.to(argument -> IntegerArgumentType.integer((int)((ByteRange)argument.range()).minByte(), (int)((ByteRange)argument.range()).maxByte())).cloudSuggestions());
        this.registerMapping(new TypeToken<ShortParser<C>>(){}, builder -> builder.to(argument -> IntegerArgumentType.integer((int)((ShortRange)argument.range()).minShort(), (int)((ShortRange)argument.range()).maxShort())).cloudSuggestions());
        this.registerMapping(new TypeToken<IntegerParser<C>>(){}, builder -> builder.to(argument -> {
            if (!argument.hasMin() && !argument.hasMax()) {
                return IntegerArgumentType.integer();
            }
            if (argument.hasMin() && !argument.hasMax()) {
                return IntegerArgumentType.integer((int)((IntRange)argument.range()).minInt());
            }
            if (!argument.hasMin()) {
                return IntegerArgumentType.integer((int)Integer.MIN_VALUE, (int)((IntRange)argument.range()).maxInt());
            }
            return IntegerArgumentType.integer((int)((IntRange)argument.range()).minInt(), (int)((IntRange)argument.range()).maxInt());
        }).cloudSuggestions());
        this.registerMapping(new TypeToken<FloatParser<C>>(){}, builder -> builder.to(argument -> {
            if (!argument.hasMin() && !argument.hasMax()) {
                return FloatArgumentType.floatArg();
            }
            if (argument.hasMin() && !argument.hasMax()) {
                return FloatArgumentType.floatArg((float)((FloatRange)argument.range()).minFloat());
            }
            if (!argument.hasMin()) {
                return FloatArgumentType.floatArg((float)-3.4028235E38f, (float)((FloatRange)argument.range()).maxFloat());
            }
            return FloatArgumentType.floatArg((float)((FloatRange)argument.range()).minFloat(), (float)((FloatRange)argument.range()).maxFloat());
        }).cloudSuggestions());
        this.registerMapping(new TypeToken<DoubleParser<C>>(){}, builder -> builder.to(argument -> {
            if (!argument.hasMin() && !argument.hasMax()) {
                return DoubleArgumentType.doubleArg();
            }
            if (argument.hasMin() && !argument.hasMax()) {
                return DoubleArgumentType.doubleArg((double)((DoubleRange)argument.range()).minDouble());
            }
            if (!argument.hasMin()) {
                return DoubleArgumentType.doubleArg((double)-1.7976931348623157E308, (double)((DoubleRange)argument.range()).maxDouble());
            }
            return DoubleArgumentType.doubleArg((double)((DoubleRange)argument.range()).minDouble(), (double)((DoubleRange)argument.range()).maxDouble());
        }).cloudSuggestions());
        this.registerMapping(new TypeToken<LongParser<C>>(){}, builder -> builder.to(longParser -> {
            if (!longParser.hasMin() && !longParser.hasMax()) {
                return LongArgumentType.longArg();
            }
            if (longParser.hasMin() && !longParser.hasMax()) {
                return LongArgumentType.longArg((long)((LongRange)longParser.range()).minLong());
            }
            if (!longParser.hasMin()) {
                return LongArgumentType.longArg((long)Long.MIN_VALUE, (long)((LongRange)longParser.range()).maxLong());
            }
            return LongArgumentType.longArg((long)((LongRange)longParser.range()).minLong(), (long)((LongRange)longParser.range()).maxLong());
        }).cloudSuggestions());
        this.registerMapping(new TypeToken<BooleanParser<C>>(){}, builder -> builder.toConstant((ArgumentType<?>)BoolArgumentType.bool()));
        this.registerMapping(new TypeToken<StringParser<C>>(){}, builder -> builder.cloudSuggestions().to(argument -> {
            switch (argument.stringMode()) {
                case QUOTED: {
                    return StringArgumentType.string();
                }
                case GREEDY: 
                case GREEDY_FLAG_YIELDING: {
                    return StringArgumentType.greedyString();
                }
            }
            return StringArgumentType.word();
        }));
        this.registerMapping(new TypeToken<CommandFlagParser<C>>(){}, builder -> builder.cloudSuggestions().toConstant((ArgumentType<?>)StringArgumentType.greedyString()));
        this.registerMapping(new TypeToken<StringArrayParser<C>>(){}, builder -> builder.cloudSuggestions().toConstant((ArgumentType<?>)StringArgumentType.greedyString()));
        this.registerMapping(new TypeToken<WrappedBrigadierParser<C, ?>>(){}, builder -> builder.to(WrappedBrigadierParser::nativeArgumentType));
    }

    @API(status=API.Status.STABLE, since="2.0.0")
    public @NonNull Configurable<BrigadierSetting> settings() {
        return this.settings;
    }

    @Override
    public @NonNull SenderMapper<S, C> senderMapper() {
        return this.brigadierSourceMapper;
    }

    @API(status=API.Status.STABLE, since="1.2.0")
    public void setNativeNumberSuggestions(boolean nativeNumberSuggestions) {
        this.setNativeSuggestions(new TypeToken<ByteParser<C>>(){}, nativeNumberSuggestions);
        this.setNativeSuggestions(new TypeToken<ShortParser<C>>(){}, nativeNumberSuggestions);
        this.setNativeSuggestions(new TypeToken<IntegerParser<C>>(){}, nativeNumberSuggestions);
        this.setNativeSuggestions(new TypeToken<FloatParser<C>>(){}, nativeNumberSuggestions);
        this.setNativeSuggestions(new TypeToken<DoubleParser<C>>(){}, nativeNumberSuggestions);
        this.setNativeSuggestions(new TypeToken<LongParser<C>>(){}, nativeNumberSuggestions);
    }

    @API(status=API.Status.STABLE, since="1.2.0")
    public <T, K extends ArgumentParser<C, T>> void setNativeSuggestions(@NonNull TypeToken<K> argumentType, boolean nativeSuggestions) throws IllegalArgumentException {
        Class<?> parserClass = GenericTypeReflector.erase(argumentType.getType());
        BrigadierMapping<C, ?, S> mapping = this.brigadierMappings.mapping(parserClass);
        if (mapping == null) {
            throw new IllegalArgumentException("No mapper registered for type: " + GenericTypeReflector.erase(argumentType.getType()).toGenericString());
        }
        this.brigadierMappings.registerMapping(parserClass, mapping.withNativeSuggestions(nativeSuggestions));
    }

    @API(status=API.Status.STABLE, since="1.5.0")
    public <K extends ArgumentParser<C, ?>> void registerMapping(@NonNull TypeToken<K> parserType, Consumer<BrigadierMappingBuilder<K, S>> configurer) {
        BrigadierMappingBuilder builder = BrigadierMapping.builder();
        configurer.accept(builder);
        this.mappings().registerMappingUnsafe(GenericTypeReflector.erase(parserType.getType()), builder.build());
    }

    @API(status=API.Status.INTERNAL, since="2.0.0")
    public @NonNull BrigadierMappings<C, S> mappings() {
        return this.brigadierMappings;
    }

    @API(status=API.Status.STABLE, since="2.0.0")
    public @NonNull LiteralBrigadierNodeFactory<C, S> literalBrigadierNodeFactory() {
        return this.literalBrigadierNodeFactory;
    }

    @API(status=API.Status.STABLE, since="2.0.0")
    public <T> void registerDefaultArgumentTypeSupplier(@NonNull Class<T> clazz, @NonNull ArgumentTypeFactory<T> factory) {
        this.defaultArgumentTypeSuppliers.put(clazz, factory);
    }

    @API(status=API.Status.INTERNAL, since="2.0.0")
    public @NonNull Map<@NonNull Class<?>, @NonNull ArgumentTypeFactory<?>> defaultArgumentTypeFactories() {
        return Collections.unmodifiableMap(this.defaultArgumentTypeSuppliers);
    }
}

