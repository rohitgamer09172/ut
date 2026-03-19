/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.platform.api;

import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import lombok.Generated;

public enum Platform {
    FABRIC("fabric"),
    BUKKIT("bukkit"),
    FOLIA("folia");

    private final String name;

    @Nullable
    public static Platform getByName(String name) {
        for (Platform platform : Platform.values()) {
            if (!platform.getName().equalsIgnoreCase(name)) continue;
            return platform;
        }
        return null;
    }

    @Generated
    private Platform(String name) {
        this.name = name;
    }

    @Generated
    public String getName() {
        return this.name;
    }
}

