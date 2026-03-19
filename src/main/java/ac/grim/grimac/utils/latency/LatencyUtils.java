/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.latency;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import ac.grim.grimac.utils.common.arguments.CommonGrimArguments;
import ac.grim.grimac.utils.data.Pair;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class LatencyUtils {
    private final LinkedList<Pair<Integer, Runnable>> transactionMap = new LinkedList();
    private final GrimPlayer player;
    private final ArrayList<Runnable> tasksToRun = new ArrayList();

    public LatencyUtils(GrimPlayer player) {
        this.player = player;
    }

    public void addRealTimeTask(int transaction, Runnable runnable) {
        this.addRealTimeTask(transaction, false, runnable);
    }

    public void addRealTimeTaskAsync(int transaction, Runnable runnable) {
        this.addRealTimeTask(transaction, true, runnable);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addRealTimeTask(int transaction, boolean async, Runnable runnable) {
        if (this.player.lastTransactionReceived.get() >= transaction) {
            if (async) {
                this.player.runSafely(runnable);
            } else {
                runnable.run();
            }
            return;
        }
        LatencyUtils latencyUtils = this;
        synchronized (latencyUtils) {
            this.transactionMap.add(new Pair<Integer, Runnable>(transaction, runnable));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void handleNettySyncTransaction(int transaction) {
        LatencyUtils latencyUtils = this;
        synchronized (latencyUtils) {
            Pair pair;
            this.tasksToRun.clear();
            ListIterator iterator = this.transactionMap.listIterator();
            while (iterator.hasNext() && transaction + 1 >= (Integer)(pair = (Pair)iterator.next()).first()) {
                if (transaction == (Integer)pair.first() - 1) continue;
                this.tasksToRun.add((Runnable)pair.second());
                iterator.remove();
            }
            for (Runnable runnable : this.tasksToRun) {
                try {
                    runnable.run();
                }
                catch (Exception e) {
                    LogUtil.error("An error has occurred when running transactions for player: " + this.player.user.getName(), e);
                    if (!CommonGrimArguments.KICK_ON_TRANSACTION_ERRORS.value().booleanValue()) continue;
                    this.player.disconnect(MessageUtil.miniMessage(MessageUtil.replacePlaceholders(this.player, GrimAPI.INSTANCE.getConfigManager().getDisconnectPacketError())));
                }
            }
        }
    }
}

