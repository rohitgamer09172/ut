/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.Command
 *  com.mojang.brigadier.tree.CommandNode
 *  org.apiguardian.api.API
 *  org.apiguardian.api.API$Status
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package ac.grim.grimac.shaded.incendo.cloud.brigadier.node;

import ac.grim.grimac.shaded.incendo.cloud.Command;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.permission.BrigadierPermissionChecker;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandNode;
import org.apiguardian.api.API;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(status=API.Status.INTERNAL, consumers={"ac.grim.grimac.shaded.incendo.cloud.brigadier.*"})
public interface BrigadierNodeFactory<C, S, N extends com.mojang.brigadier.tree.CommandNode<S>> {
    public @NonNull N createNode(@NonNull String var1, @NonNull CommandNode<C> var2, @NonNull com.mojang.brigadier.Command<S> var3, @NonNull BrigadierPermissionChecker<C> var4);

    public @NonNull N createNode(@NonNull String var1, @NonNull Command<C> var2, @NonNull com.mojang.brigadier.Command<S> var3, @NonNull BrigadierPermissionChecker<C> var4);

    public @NonNull N createNode(@NonNull String var1, @NonNull Command<C> var2, @NonNull com.mojang.brigadier.Command<S> var3);
}

