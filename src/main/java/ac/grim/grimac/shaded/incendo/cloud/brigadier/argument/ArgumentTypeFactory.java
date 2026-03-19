/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.ArgumentType
 *  org.apiguardian.api.API
 *  org.apiguardian.api.API$Status
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package ac.grim.grimac.shaded.incendo.cloud.brigadier.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import org.apiguardian.api.API;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(status=API.Status.STABLE, since="2.0.0")
public interface ArgumentTypeFactory<T> {
    public @Nullable ArgumentType<T> create();
}

