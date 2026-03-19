/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.command.handler;

import ac.grim.grimac.command.SenderRequirement;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.processors.requirements.RequirementFailureHandler;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public class GrimCommandFailureHandler
implements RequirementFailureHandler<Sender, SenderRequirement> {
    @Override
    public void handleFailure(@NotNull CommandContext<Sender> context, @NotNull SenderRequirement requirement) {
        context.sender().sendMessage(requirement.errorMessage(context.sender()));
    }
}

