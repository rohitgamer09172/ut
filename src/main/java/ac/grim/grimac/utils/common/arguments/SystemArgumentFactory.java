/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.common.arguments;

import ac.grim.grimac.platform.api.Platform;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.common.arguments.ArgumentOptions;
import ac.grim.grimac.utils.common.arguments.SystemArgument;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public record SystemArgumentFactory(Map<String, String> arguments, Map<Class<?>, Function<String, ?>> parsers, Consumer<SystemArgument<?>> creationListener, Consumer<ArgumentOptions.Builder<?>> optionModifier) {
    private <T> SystemArgument<T> createDefaultSupplier(ArgumentOptions<T> options) {
        T value = options.getModifier().apply(options.getDefaultSupplier().get());
        if (value == null && !options.isNullable()) {
            throw new IllegalArgumentException("Default value cannot be null for startup argument \"" + options.getKey() + "\"");
        }
        if (value != null && !options.getVerifier().test(value)) {
            throw new IllegalArgumentException("Invalid default value for startup argument \"" + options.getKey() + "\"");
        }
        SystemArgument<T> argument = new SystemArgument<T>(options.getKey(), options.getClazz(), value, false, options.getVisibility());
        this.creationListener.accept(argument);
        return argument;
    }

    public <T> SystemArgument<T> create(ArgumentOptions.Builder<T> builder) {
        ArgumentOptions<T> options;
        String value;
        if (this.optionModifier != null) {
            this.optionModifier.accept(builder);
        }
        if ((value = this.arguments.get((options = builder.build()).getKey().toLowerCase())) == null) {
            return this.createDefaultSupplier(options);
        }
        try {
            Function<String, ?> parser = this.parsers.get(options.getClazz());
            if (parser == null) {
                return this.createDefaultSupplier(options);
            }
            T parsed = options.getModifier().apply(parser.apply(value));
            if (parsed == null || !options.getVerifier().test(parsed)) {
                return this.createDefaultSupplier(options);
            }
            SystemArgument<T> newArgument = new SystemArgument<T>(options.getKey(), options.getClazz(), parsed, true, options.getVisibility());
            this.creationListener.accept(newArgument);
            return newArgument;
        }
        catch (Exception e) {
            SystemArgumentFactory.exception("Failed to parse value for startup argument \"" + options.getKey() + "\"", e);
            return this.createDefaultSupplier(options);
        }
    }

    public Map<String, String> getFoundArguments() {
        return this.arguments;
    }

    private static void exception(String message, Exception e) {
    }

    private static void warn(String message) {
    }

    public static class Builder {
        private final String prefix;
        private boolean envSupport = false;
        private Consumer<SystemArgument<?>> registerListener = argument -> {};
        private final Map<Class<?>, Function<String, ?>> parseBuilder;
        private Consumer<ArgumentOptions.Builder<?>> optionModifier = null;

        public static Builder of(String prefix) {
            return new Builder(prefix);
        }

        public Builder onRegister(Consumer<SystemArgument<?>> listener) {
            this.registerListener = listener;
            return this;
        }

        public Builder optionModifier(Consumer<ArgumentOptions.Builder<?>> modifier) {
            this.optionModifier = modifier;
            return this;
        }

        public Builder supportEnv() {
            this.envSupport = true;
            return this;
        }

        private Builder(String prefix) {
            this.prefix = prefix;
            this.parseBuilder = new HashMap();
            this.registerDefaultParsers();
        }

        protected void registerDefaultParsers() {
            this.registerParser(Boolean.class, Boolean::parseBoolean).registerParser(Byte.class, Byte::parseByte).registerParser(Short.class, Short::parseShort).registerParser(Integer.class, Integer::parseInt).registerParser(Float.class, Float::parseFloat).registerParser(Double.class, Double::parseDouble).registerParser(Long.class, Long::parseLong).registerParser(Character.class, s -> Character.valueOf(!s.isEmpty() ? s.charAt(0) : (char)'\u0000')).registerParser(char[].class, String::toCharArray).registerParser(String.class, String::valueOf).registerParser(Charset.class, Charset::forName).registerParser(Platform.class, Platform::getByName);
        }

        public <T> Builder registerParser(Class<T> type, Function<String, T> parser) {
            this.parseBuilder.put(type, parser);
            return this;
        }

        private void updateFromEnv(Map<String, String> builder) {
            for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
                if (!entry.getKey().startsWith(this.prefix.toUpperCase()) || builder.put(entry.getKey().toLowerCase(), entry.getValue()) == null) continue;
                SystemArgumentFactory.warn("Env variable overwriting system variable: " + entry.getKey());
            }
        }

        public SystemArgumentFactory build() {
            String findPrefix = "-d" + this.prefix.toLowerCase();
            Map<String, String> builder = this.getSystemPropertiesMap(findPrefix);
            if (this.envSupport) {
                try {
                    this.updateFromEnv(builder);
                }
                catch (Exception e) {
                    SystemArgumentFactory.exception("Failed to read environment variables", e);
                }
            }
            return new SystemArgumentFactory(Map.copyOf(builder), Map.copyOf(this.parseBuilder), this.registerListener, this.optionModifier);
        }

        @NotNull
        protected Map<String, String> getSystemPropertiesMap(String findPrefix) {
            HashMap<String, String> builder = new HashMap<String, String>();
            for (String line : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
                if (!line.toLowerCase().startsWith(findPrefix)) continue;
                int index = line.indexOf(61);
                if (index > 0 && index < line.length() - 1) {
                    String key = line.substring(2, index);
                    String value = line.substring(index + 1);
                    builder.put(key.toLowerCase(), value);
                    continue;
                }
                SystemArgumentFactory.warn("Invalid startup argument: " + line);
            }
            return builder;
        }
    }
}

