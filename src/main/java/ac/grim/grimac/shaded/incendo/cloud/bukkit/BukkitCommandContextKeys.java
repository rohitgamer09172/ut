/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apiguardian.api.API
 *  org.apiguardian.api.API$Status
 *  org.bukkit.command.CommandSender
 */
package ac.grim.grimac.shaded.incendo.cloud.bukkit;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.key.CloudKey;
import java.util.concurrent.Executor;
import org.apiguardian.api.API;
import org.bukkit.command.CommandSender;

public final class BukkitCommandContextKeys {
    public static final CloudKey<CommandSender> BUKKIT_COMMAND_SENDER = CloudKey.of("BukkitCommandSender", TypeToken.get(CommandSender.class));
    @API(status=API.Status.STABLE, since="2.0.0")
    public static final CloudKey<Executor> SENDER_SCHEDULER_EXECUTOR = CloudKey.of("SenderSchedulerExecutor", Executor.class);

    private BukkitCommandContextKeys() {
    }
}

