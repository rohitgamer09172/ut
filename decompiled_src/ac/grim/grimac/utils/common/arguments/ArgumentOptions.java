/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.common.arguments;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.common.arguments.SystemArgument;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.Generated;

public class ArgumentOptions<T> {
    private final Class<T> clazz;
    private String key;
    private Supplier<T> defaultSupplier;
    private Predicate<T> verifier = t -> true;
    private Function<T, T> modifier = t -> t;
    private SystemArgument.Visibility visibility = SystemArgument.Visibility.VISIBLE;
    private boolean nullable = false;

    private ArgumentOptions(Class<T> clazz, String key, Supplier<T> defaultSupplier) {
        this.clazz = clazz;
        this.key = key;
        this.defaultSupplier = defaultSupplier;
    }

    public static <T> Builder<T> from(Class<T> clazz, String key, @NotNull @NotNull Supplier<@NotNull T> defaultValue) {
        return new Builder<T>(new ArgumentOptions<T>(clazz, key, defaultValue));
    }

    public static <T> Builder<T> from(Class<T> clazz, String key, T defaultValue) {
        return new Builder<Object>(new ArgumentOptions<Object>(clazz, key, () -> defaultValue)).nullable(defaultValue == null);
    }

    public static <T> Builder<T> from(Class<T> clazz, String key) {
        return new Builder<Object>(new ArgumentOptions<Object>(clazz, key, () -> null)).nullable(true);
    }

    @Generated
    public Class<T> getClazz() {
        return this.clazz;
    }

    @Generated
    public String getKey() {
        return this.key;
    }

    @Generated
    public Supplier<T> getDefaultSupplier() {
        return this.defaultSupplier;
    }

    @Generated
    public Predicate<T> getVerifier() {
        return this.verifier;
    }

    @Generated
    public Function<T, T> getModifier() {
        return this.modifier;
    }

    @Generated
    public SystemArgument.Visibility getVisibility() {
        return this.visibility;
    }

    @Generated
    public boolean isNullable() {
        return this.nullable;
    }

    public record Builder<T>(ArgumentOptions<T> options) {
        public Builder<T> key(String key) {
            this.options.key = key;
            return this;
        }

        public Builder<T> verifier(Predicate<T> predicate) {
            this.options.verifier = predicate;
            return this;
        }

        public Builder<T> modifier(Function<T, T> modifier) {
            this.options.modifier = modifier;
            return this;
        }

        public Builder<T> defaultSupplier(Supplier<T> supplier) {
            this.options.defaultSupplier = supplier;
            return this;
        }

        public Builder<T> visibility(SystemArgument.Visibility visibility) {
            this.options.visibility = visibility;
            return this;
        }

        private Builder<T> nullable(boolean nullable) {
            this.options.nullable = nullable;
            return this;
        }

        public ArgumentOptions<T> build() {
            return this.options;
        }
    }
}

