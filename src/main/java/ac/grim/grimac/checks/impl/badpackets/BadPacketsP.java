/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;

@CheckData(name="BadPacketsP", description="Invalid click packets", experimental=true)
public class BadPacketsP
extends Check
implements PacketCheck {
    private int containerType = -1;
    private int containerId = -1;

    public BadPacketsP(GrimPlayer playerData) {
        super(playerData);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.OPEN_WINDOW) {
            WrapperPlayServerOpenWindow window = new WrapperPlayServerOpenWindow(event);
            this.containerType = window.getType();
            this.containerId = window.getContainerId();
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
            boolean flag;
            WrapperPlayClientClickWindow wrapper = new WrapperPlayClientClickWindow(event);
            WrapperPlayClientClickWindow.WindowClickType clickType = wrapper.getWindowClickType();
            int button = wrapper.getButton();
            switch (clickType) {
                default: {
                    throw new IncompatibleClassChangeError();
                }
                case PICKUP: 
                case QUICK_MOVE: 
                case CLONE: {
                    boolean bl;
                    if (button > 2 || button < 0) {
                        bl = true;
                        break;
                    }
                    bl = false;
                    break;
                }
                case SWAP: {
                    boolean bl;
                    if ((button > 8 || button < 0) && button != 40) {
                        bl = true;
                        break;
                    }
                    bl = false;
                    break;
                }
                case THROW: {
                    boolean bl;
                    if (button != 0 && button != 1) {
                        bl = true;
                        break;
                    }
                    bl = false;
                    break;
                }
                case QUICK_CRAFT: {
                    boolean bl;
                    if (button == 3 || button == 7 || button > 10 || button < 0) {
                        bl = true;
                        break;
                    }
                    bl = false;
                    break;
                }
                case PICKUP_ALL: {
                    boolean bl;
                    if (button != 0) {
                        bl = true;
                        break;
                    }
                    bl = false;
                    break;
                }
                case UNKNOWN: {
                    boolean bl = flag = true;
                }
            }
            if (flag && this.flagAndAlert("clickType=" + clickType.toString().toLowerCase() + ", button=" + button + (String)(wrapper.getWindowId() == this.containerId ? ", container=" + this.containerType : "")) && this.shouldModifyPackets()) {
                event.setCancelled(true);
                this.player.onPacketCancel();
            }
        }
    }
}

