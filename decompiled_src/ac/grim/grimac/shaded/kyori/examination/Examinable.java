/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.examination;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import ac.grim.grimac.shaded.kyori.examination.Examiner;
import java.util.stream.Stream;

public interface Examinable {
    @NotNull
    default public String examinableName() {
        return this.getClass().getSimpleName();
    }

    @NotNull
    default public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.empty();
    }

    @NotNull
    default public <R> R examine(@NotNull Examiner<R> examiner) {
        return examiner.examine(this);
    }
}

