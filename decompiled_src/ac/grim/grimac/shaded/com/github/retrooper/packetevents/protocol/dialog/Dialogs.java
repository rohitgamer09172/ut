/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.CommonDialogData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.Dialog;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.DialogAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.DialogListDialog;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.ServerLinksDialog;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.button.CommonButtonData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Collections;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Dialogs {
    private static final ActionButton DEFAULT_BACK_BUTTON = new ActionButton(new CommonButtonData(Component.translatable("gui.back"), null, 200), null);
    private static final VersionedRegistry<Dialog> REGISTRY = new VersionedRegistry("dialog");
    public static final Dialog SERVER_LINKS = Dialogs.define("server_links", new ServerLinksDialog(new CommonDialogData(Component.translatable("menu.server_links.title"), Component.translatable("menu.server_links"), true, true, DialogAction.CLOSE, Collections.emptyList(), Collections.emptyList()), DEFAULT_BACK_BUTTON, 1, 310));
    public static final Dialog CUSTOM_OPTIONS = Dialogs.define("custom_options", new DialogListDialog(new CommonDialogData(Component.translatable("menu.custom_options.title"), Component.translatable("menu.custom_options"), true, true, DialogAction.CLOSE, Collections.emptyList(), Collections.emptyList()), new MappedEntitySet<Dialog>(new ResourceLocation("pause_screen_additions")), DEFAULT_BACK_BUTTON, 1, 310));
    public static final Dialog QUICK_ACTIONS = Dialogs.define("quick_actions", new DialogListDialog(new CommonDialogData(Component.translatable("menu.quick_actions.title"), Component.translatable("menu.quick_actions"), true, true, DialogAction.CLOSE, Collections.emptyList(), Collections.emptyList()), new MappedEntitySet<Dialog>(new ResourceLocation("quick_actions")), DEFAULT_BACK_BUTTON, 1, 310));

    private Dialogs() {
    }

    @ApiStatus.Internal
    public static Dialog define(String name, Dialog dialog) {
        return REGISTRY.define(name, dialog::copy);
    }

    public static VersionedRegistry<Dialog> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}

