/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.internal.properties;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.internal.properties.AdventurePropertiesImpl;
import ac.grim.grimac.shaded.kyori.adventure.util.PlatformAPI;
import java.util.Objects;
import java.util.function.Function;

@ApiStatus.Internal
public final class AdventureProperties {
    public static final Property<Boolean> DEBUG = AdventureProperties.property("debug", Boolean::parseBoolean, false);
    public static final Property<String> DEFAULT_TRANSLATION_LOCALE = AdventureProperties.property("defaultTranslationLocale", Function.identity(), null);
    public static final Property<Boolean> SERVICE_LOAD_FAILURES_ARE_FATAL = AdventureProperties.property("serviceLoadFailuresAreFatal", Boolean::parseBoolean, Boolean.TRUE, false);
    public static final Property<Boolean> TEXT_WARN_WHEN_LEGACY_FORMATTING_DETECTED = AdventureProperties.property("text.warnWhenLegacyFormattingDetected", Boolean::parseBoolean, Boolean.FALSE);
    public static final Property<Integer> DEFAULT_FLATTENER_NESTING_LIMIT = AdventureProperties.property("defaultFlattenerNestingLimit", Integer::parseInt, -1);

    private AdventureProperties() {
    }

    @NotNull
    public static <T> Property<T> property(@NotNull String name, @NotNull Function<String, T> parser, @Nullable T defaultValue) {
        return AdventureProperties.property(name, parser, defaultValue, true);
    }

    @NotNull
    public static <T> Property<T> property(@NotNull String name, @NotNull Function<String, T> parser, @Nullable T defaultValue, boolean allowProviderDefaultOverride) {
        return AdventurePropertiesImpl.property(name, parser, defaultValue, allowProviderDefaultOverride);
    }

    @ApiStatus.Internal
    @ApiStatus.NonExtendable
    public static interface Property<T> {
        @Nullable
        public T value();

        @NotNull
        default public T valueOr(@NotNull T defaultValue) {
            T value = this.value();
            return value == null ? Objects.requireNonNull(defaultValue, "defaultValue") : value;
        }
    }

    @PlatformAPI
    @ApiStatus.Internal
    public static interface DefaultOverrideProvider {
        @Nullable
        public <T> T overrideDefault(@NotNull Property<T> var1, @Nullable T var2);
    }
}

