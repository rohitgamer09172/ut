/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource
 *  com.destroystokyo.paper.event.brigadier.CommandRegisteredEvent
 *  org.bukkit.command.PluginIdentifiableCommand
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package ac.grim.grimac.shaded.incendo.cloud.paper;

import ac.grim.grimac.shaded.incendo.cloud.CommandTree;
import ac.grim.grimac.shaded.incendo.cloud.SenderMapper;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.BrigadierManagerHolder;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.CloudBrigadierCommand;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.CloudBrigadierManager;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.node.LiteralBrigadierNodeFactory;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.permission.BrigadierPermissionChecker;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.BukkitBackwardsBrigadierSenderMapper;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.BukkitBrigadierMapper;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.BukkitHelper;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandNode;
import ac.grim.grimac.shaded.incendo.cloud.paper.LegacyPaperCommandManager;
import ac.grim.grimac.shaded.incendo.cloud.paper.PaperBrigadierMappings;
import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.destroystokyo.paper.event.brigadier.CommandRegisteredEvent;
import java.util.regex.Pattern;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.checkerframework.checker.nullness.qual.NonNull;

class LegacyPaperBrigadier<C>
implements Listener,
BrigadierManagerHolder<C, BukkitBrigadierCommandSource> {
    private final CloudBrigadierManager<C, BukkitBrigadierCommandSource> brigadierManager;
    private final LegacyPaperCommandManager<C> paperCommandManager;

    LegacyPaperBrigadier(@NonNull LegacyPaperCommandManager<C> paperCommandManager) {
        this.paperCommandManager = paperCommandManager;
        this.brigadierManager = new CloudBrigadierManager<Object, BukkitBrigadierCommandSource>(this.paperCommandManager, SenderMapper.create(sender -> this.paperCommandManager.senderMapper().map(sender.getBukkitSender()), new BukkitBackwardsBrigadierSenderMapper(this.paperCommandManager.senderMapper())));
        BukkitBrigadierMapper<C> mapper = new BukkitBrigadierMapper<C>(this.paperCommandManager.owningPlugin().getLogger(), this.brigadierManager);
        mapper.registerBuiltInMappings();
        PaperBrigadierMappings.register(mapper);
    }

    @Override
    public final boolean hasBrigadierManager() {
        return true;
    }

    @Override
    public final @NonNull CloudBrigadierManager<C, BukkitBrigadierCommandSource> brigadierManager() {
        return this.brigadierManager;
    }

    @EventHandler
    public void onCommandRegister(@NonNull CommandRegisteredEvent<BukkitBrigadierCommandSource> event) {
        String label;
        if (!(event.getCommand() instanceof PluginIdentifiableCommand)) {
            return;
        }
        if (!((PluginIdentifiableCommand)event.getCommand()).getPlugin().equals((Object)this.paperCommandManager.owningPlugin())) {
            return;
        }
        CommandTree commandTree = this.paperCommandManager.commandTree();
        CommandNode node = commandTree.getNamedNode(label = event.getCommandLabel().contains(":") ? event.getCommandLabel().split(Pattern.quote(":"))[1] : event.getCommandLabel());
        if (node == null) {
            return;
        }
        BrigadierPermissionChecker<Object> permissionChecker = (sender, permission) -> {
            if (commandTree.getNamedNode(label) == null) {
                return false;
            }
            return this.paperCommandManager.testPermission(sender, permission).allowed();
        };
        LiteralBrigadierNodeFactory<Object, BukkitBrigadierCommandSource> literalFactory = this.brigadierManager.literalBrigadierNodeFactory();
        event.setLiteral(literalFactory.createNode(event.getLiteral().getLiteral(), node, new CloudBrigadierCommand<C, BukkitBrigadierCommandSource>(this.paperCommandManager, this.brigadierManager, command -> BukkitHelper.stripNamespace(this.paperCommandManager, command)), permissionChecker));
    }
}

