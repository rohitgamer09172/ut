/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.api.scheduler;

public interface TaskHandle {
    public boolean isSync();

    public boolean isCancelled();

    public void cancel();
}

