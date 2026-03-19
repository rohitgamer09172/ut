/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.renderer;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.BlockNBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.EntityNBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.KeybindComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.NBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.ScoreComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.SelectorComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.StorageNBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.TextComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslatableComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.VirtualComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.renderer.ComponentRenderer;

public abstract class AbstractComponentRenderer<C>
implements ComponentRenderer<C> {
    @Override
    @NotNull
    public Component render(@NotNull Component component, @NotNull C context) {
        if (component instanceof VirtualComponent) {
            component = this.renderVirtual((VirtualComponent)component, context);
        }
        if (component instanceof TextComponent) {
            return this.renderText((TextComponent)component, context);
        }
        if (component instanceof TranslatableComponent) {
            return this.renderTranslatable((TranslatableComponent)component, context);
        }
        if (component instanceof KeybindComponent) {
            return this.renderKeybind((KeybindComponent)component, context);
        }
        if (component instanceof ScoreComponent) {
            return this.renderScore((ScoreComponent)component, context);
        }
        if (component instanceof SelectorComponent) {
            return this.renderSelector((SelectorComponent)component, context);
        }
        if (component instanceof NBTComponent) {
            if (component instanceof BlockNBTComponent) {
                return this.renderBlockNbt((BlockNBTComponent)component, context);
            }
            if (component instanceof EntityNBTComponent) {
                return this.renderEntityNbt((EntityNBTComponent)component, context);
            }
            if (component instanceof StorageNBTComponent) {
                return this.renderStorageNbt((StorageNBTComponent)component, context);
            }
        }
        return component;
    }

    @NotNull
    protected abstract Component renderBlockNbt(@NotNull BlockNBTComponent var1, @NotNull C var2);

    @NotNull
    protected abstract Component renderEntityNbt(@NotNull EntityNBTComponent var1, @NotNull C var2);

    @NotNull
    protected abstract Component renderStorageNbt(@NotNull StorageNBTComponent var1, @NotNull C var2);

    @NotNull
    protected abstract Component renderKeybind(@NotNull KeybindComponent var1, @NotNull C var2);

    @NotNull
    protected abstract Component renderScore(@NotNull ScoreComponent var1, @NotNull C var2);

    @NotNull
    protected abstract Component renderSelector(@NotNull SelectorComponent var1, @NotNull C var2);

    @NotNull
    protected abstract Component renderText(@NotNull TextComponent var1, @NotNull C var2);

    @NotNull
    protected Component renderVirtual(@NotNull VirtualComponent component, @NotNull C context) {
        return component;
    }

    @NotNull
    protected abstract Component renderTranslatable(@NotNull TranslatableComponent var1, @NotNull C var2);
}

