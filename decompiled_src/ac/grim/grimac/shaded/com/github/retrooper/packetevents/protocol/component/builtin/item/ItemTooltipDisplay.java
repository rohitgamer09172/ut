/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class ItemTooltipDisplay {
    private boolean hideTooltip;
    private Set<ComponentType<?>> hiddenComponents;

    public ItemTooltipDisplay(boolean hideTooltip, Set<ComponentType<?>> hiddenComponents) {
        this.hideTooltip = hideTooltip;
        this.hiddenComponents = hiddenComponents;
    }

    public static ItemTooltipDisplay read(PacketWrapper<?> wrapper) {
        boolean hideTooltip = wrapper.readBoolean();
        Set hiddenComponents = wrapper.readCollection(LinkedHashSet::new, ew -> ew.readMappedEntity(ComponentTypes.getRegistry()));
        return new ItemTooltipDisplay(hideTooltip, hiddenComponents);
    }

    public static void write(PacketWrapper<?> wrapper, ItemTooltipDisplay tooltipDisplay) {
        wrapper.writeBoolean(tooltipDisplay.hideTooltip);
        wrapper.writeCollection(tooltipDisplay.hiddenComponents, PacketWrapper::writeMappedEntity);
    }

    public boolean isHideTooltip() {
        return this.hideTooltip;
    }

    public void setHideTooltip(boolean hideTooltip) {
        this.hideTooltip = hideTooltip;
    }

    public Set<ComponentType<?>> getHiddenComponents() {
        return this.hiddenComponents;
    }

    public void setHiddenComponents(Set<ComponentType<?>> hiddenComponents) {
        this.hiddenComponents = hiddenComponents;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ItemTooltipDisplay)) {
            return false;
        }
        ItemTooltipDisplay that = (ItemTooltipDisplay)obj;
        if (this.hideTooltip != that.hideTooltip) {
            return false;
        }
        return this.hiddenComponents.equals(that.hiddenComponents);
    }

    public int hashCode() {
        return Objects.hash(this.hideTooltip, this.hiddenComponents);
    }
}

