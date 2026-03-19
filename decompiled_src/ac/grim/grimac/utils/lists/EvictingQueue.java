/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.lists;

import java.util.ArrayList;

public class EvictingQueue<K>
extends ArrayList<K> {
    private final int maxSize;

    public EvictingQueue(int size) {
        this.maxSize = size;
    }

    @Override
    public boolean add(K k) {
        boolean r = super.add(k);
        if (this.size() > this.maxSize) {
            this.removeRange(0, this.size() - this.maxSize);
        }
        return r;
    }

    public K getYoungest() {
        return (K)this.get(this.size() - 1);
    }

    public K getOldest() {
        return (K)this.get(0);
    }
}

