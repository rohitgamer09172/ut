/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.common.arguments;

import java.util.function.Function;
import java.util.function.Predicate;

public record SystemArgument<T>(String key, Class<T> clazz, T value, boolean set, Visibility visibility) {
    public boolean matches(Predicate<T> predicate) {
        return predicate.test(this.value);
    }

    public <K> K mapValue(Function<T, K> mapper, K otherwise) {
        try {
            return this.value == null ? otherwise : mapper.apply(this.value);
        }
        catch (Exception exception) {
            return otherwise;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        SystemArgument that = (SystemArgument)o;
        return this.key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return this.key.hashCode();
    }

    public static enum Visibility {
        VISIBLE,
        HIDDEN,
        SECRET;

    }
}

