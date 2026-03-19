/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.change;

import ac.grim.grimac.utils.change.BlockModification;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Predicate;

public class PlayerBlockHistory {
    private final ConcurrentLinkedDeque<BlockModification> blockHistory = new ConcurrentLinkedDeque();

    public void add(BlockModification modification) {
        this.blockHistory.add(modification);
    }

    public Iterable<BlockModification> getRecentModifications(Predicate<BlockModification> filter) {
        return this.blockHistory.stream().filter(filter).toList();
    }

    public void cleanup(int maxTick) {
        while (!this.blockHistory.isEmpty() && maxTick - this.blockHistory.peekFirst().tick() > 0) {
            this.blockHistory.pollFirst();
        }
    }

    public int size() {
        return this.blockHistory.size();
    }

    public void clear() {
        this.blockHistory.clear();
    }
}

