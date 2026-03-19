/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.manager.init.start;

import ac.grim.grimac.manager.init.start.StartableInitable;
import ac.grim.grimac.platform.api.command.CommandService;
import ac.grim.grimac.utils.anticheat.LogUtil;

public record CommandRegister(CommandService service) implements StartableInitable
{
    @Override
    public void start() {
        try {
            if (this.service != null) {
                this.service.registerCommands();
            }
        }
        catch (Throwable t) {
            LogUtil.error("Failed to register commands! Grim will run without command support.", t);
        }
    }
}

