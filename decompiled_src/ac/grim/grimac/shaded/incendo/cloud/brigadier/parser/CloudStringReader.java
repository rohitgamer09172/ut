/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.StringReader
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package ac.grim.grimac.shaded.incendo.cloud.brigadier.parser;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import com.mojang.brigadier.StringReader;
import org.checkerframework.checker.nullness.qual.NonNull;

final class CloudStringReader
extends StringReader {
    private final CommandInput commandInput;

    static @NonNull CloudStringReader of(@NonNull CommandInput commandInput) {
        return new CloudStringReader(commandInput);
    }

    private CloudStringReader(@NonNull CommandInput commandInput) {
        super(commandInput.input());
        this.commandInput = commandInput;
        super.setCursor(commandInput.cursor());
    }

    public void setCursor(int cursor) {
        super.setCursor(cursor);
        this.commandInput.cursor(cursor);
    }

    public char read() {
        super.read();
        return this.commandInput.read();
    }

    public void skip() {
        super.skip();
        this.commandInput.moveCursor(1);
    }
}

