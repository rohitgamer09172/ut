/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.platform.facet;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.platform.facet.Facet;

public abstract class FacetBase<V>
implements Facet<V> {
    protected final Class<? extends V> viewerClass;

    protected FacetBase(@Nullable Class<? extends V> viewerClass) {
        this.viewerClass = viewerClass;
    }

    @Override
    public boolean isSupported() {
        return this.viewerClass != null;
    }

    @Override
    public boolean isApplicable(@NotNull V viewer) {
        return this.viewerClass != null && this.viewerClass.isInstance(viewer);
    }
}

