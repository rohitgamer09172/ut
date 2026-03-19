/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public abstract class ParsingException
extends RuntimeException {
    private static final long serialVersionUID = 4502774670340827070L;
    public static final int LOCATION_UNKNOWN = -1;

    protected ParsingException(@Nullable String message) {
        super(message);
    }

    protected ParsingException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    protected ParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @NotNull
    public abstract String originalText();

    @Nullable
    public abstract String detailMessage();

    public abstract int startIndex();

    public abstract int endIndex();
}

