/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.prediction;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.checks.debug.AbstractDebugHandler;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.TextComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.format.NamedTextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.MiniMessage;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import ac.grim.grimac.utils.lists.EvictingQueue;
import ac.grim.grimac.utils.math.Vector3dm;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class DebugHandler
extends AbstractDebugHandler
implements PostPredictionCheck {
    private static final Component GRAY_ARROW = MiniMessage.miniMessage().deserialize("<gray>\u21920.03\u2192</gray>");
    private static final Component P_PREFIX = MiniMessage.miniMessage().deserialize("<reset>P: </reset>");
    private static final Component A_PREFIX = MiniMessage.miniMessage().deserialize("<reset>A: </reset>");
    private static final Component O_PREFIX = MiniMessage.miniMessage().deserialize("<reset>O: </reset>");
    private final Set<GrimPlayer> listeners = new CopyOnWriteArraySet<GrimPlayer>(new HashSet());
    private boolean outputToConsole = false;
    private boolean enabledFlags = false;
    private boolean lastMovementIsFlag = false;
    private final EvictingQueue<Component> predicted = new EvictingQueue(5);
    private final EvictingQueue<Component> actually = new EvictingQueue(5);
    private final EvictingQueue<Component> offset = new EvictingQueue(5);

    public DebugHandler(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPredictionComplete(PredictionComplete predictionComplete) {
        boolean thisFlag;
        if (!predictionComplete.isChecked()) {
            return;
        }
        double offset = predictionComplete.getOffset();
        if (this.listeners.isEmpty() && !this.outputToConsole) {
            return;
        }
        if (this.player.predictedVelocity.vector.lengthSquared() == 0.0 && offset == 0.0) {
            return;
        }
        String color = this.pickColor(offset, offset);
        Vector3dm predicted = this.player.predictedVelocity.vector;
        Vector3dm actually = this.player.actualMovement;
        String xColor = this.pickColor(Math.abs(predicted.getX() - actually.getX()), offset);
        String yColor = this.pickColor(Math.abs(predicted.getY() - actually.getY()), offset);
        String zColor = this.pickColor(Math.abs(predicted.getZ() - actually.getZ()), offset);
        Component p = ((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)Component.empty().append(P_PREFIX.color(NamedTextColor.NAMES.value(color)))).append(Component.text(predicted.getX()).color(NamedTextColor.NAMES.value(xColor)))).append(Component.space())).append(Component.text(predicted.getY()).color(NamedTextColor.NAMES.value(yColor)))).append(Component.space())).append(Component.text(predicted.getZ()).color(NamedTextColor.NAMES.value(zColor)));
        Component a = ((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)Component.empty().append(A_PREFIX.color(NamedTextColor.NAMES.value(color)))).append(Component.text(actually.getX()).color(NamedTextColor.NAMES.value(xColor)))).append(Component.space())).append(Component.text(actually.getY()).color(NamedTextColor.NAMES.value(yColor)))).append(Component.space())).append(Component.text(actually.getZ()).color(NamedTextColor.NAMES.value(zColor)));
        String canSkipTick = (this.player.couldSkipTick + " ").substring(0, 1);
        String actualMovementSkip = ("" + this.player.skippedTickInActualMovement).charAt(0) + " ";
        Component o = ((TextComponent)((TextComponent)((TextComponent)((TextComponent)Component.empty().append(Component.text(canSkipTick).color(NamedTextColor.GRAY))).append(GRAY_ARROW)).append(Component.text(actualMovementSkip).color(NamedTextColor.GRAY))).append(O_PREFIX.color(NamedTextColor.NAMES.value(color)))).append(Component.text(offset));
        String prefix = this.player.platformPlayer == null ? "null" : this.player.platformPlayer.getName() + " ";
        TextComponent prefixComponent = Component.text(prefix);
        boolean bl = thisFlag = !color.equals("gray") && !color.equals("green");
        if (this.enabledFlags) {
            if (this.lastMovementIsFlag) {
                this.predicted.clear();
                this.actually.clear();
                this.offset.clear();
            }
            this.predicted.add(p);
            this.actually.add(a);
            this.offset.add(o);
            this.lastMovementIsFlag = thisFlag;
        }
        if (thisFlag) {
            for (int i = 0; i < this.predicted.size(); ++i) {
                this.player.user.sendMessage((Component)this.predicted.get(i));
                this.player.user.sendMessage((Component)this.actually.get(i));
                this.player.user.sendMessage((Component)this.offset.get(i));
            }
        }
        for (GrimPlayer listener : this.listeners) {
            TextComponent listenerPrefix = listener == this.getPlayer() ? Component.empty() : prefixComponent;
            listener.sendMessage(listenerPrefix.append(p));
            listener.sendMessage(listenerPrefix.append(a));
            listener.sendMessage(listenerPrefix.append(o));
        }
        this.listeners.removeIf(player -> player.platformPlayer != null && !player.platformPlayer.isOnline());
        if (this.outputToConsole) {
            Sender consoleSender = GrimAPI.INSTANCE.getPlatformServer().getConsoleSender();
            consoleSender.sendMessage(p);
            consoleSender.sendMessage(a);
            consoleSender.sendMessage(o);
        }
    }

    private String pickColor(double offset, double totalOffset) {
        if (this.player.getSetbackTeleportUtil().blockOffsets) {
            return "gray";
        }
        if (offset <= 0.0 || totalOffset <= 0.0) {
            return "gray";
        }
        if (offset < 1.0E-4) {
            return "green";
        }
        if (offset < 0.01) {
            return "yellow";
        }
        return "red";
    }

    @Override
    public void toggleListener(GrimPlayer player) {
        if (!this.listeners.remove(player)) {
            this.listeners.add(player);
        }
    }

    @Override
    public boolean toggleConsoleOutput() {
        this.outputToConsole = !this.outputToConsole;
        return this.outputToConsole;
    }
}

