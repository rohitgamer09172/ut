/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.manager;

import lombok.Generated;

class ParsedCommand {
    public final int threshold;
    public final int interval;
    public final String command;
    public int executeCount;

    @Generated
    public ParsedCommand(int threshold, int interval, String command) {
        this.threshold = threshold;
        this.interval = interval;
        this.command = command;
    }
}

