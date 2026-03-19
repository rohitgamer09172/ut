/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.netty;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.NettyManager;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufAllocationOperator;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufOperator;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.channel.ChannelOperator;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.netty.buffer.ByteBufAllocationOperatorModernImpl;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.netty.buffer.ByteBufOperatorModernImpl;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.netty.channel.ChannelOperatorModernImpl;

public class NettyManagerImpl
implements NettyManager {
    private static final ByteBufOperator BYTE_BUF_OPERATOR = new ByteBufOperatorModernImpl();
    private static final ByteBufAllocationOperator BYTE_BUF_ALLOCATION_OPERATOR = new ByteBufAllocationOperatorModernImpl();
    private static final ChannelOperator CHANNEL_OPERATOR = new ChannelOperatorModernImpl();

    @Override
    public ChannelOperator getChannelOperator() {
        return CHANNEL_OPERATOR;
    }

    @Override
    public ByteBufOperator getByteBufOperator() {
        return BYTE_BUF_OPERATOR;
    }

    @Override
    public ByteBufAllocationOperator getByteBufAllocationOperator() {
        return BYTE_BUF_ALLOCATION_OPERATOR;
    }
}

