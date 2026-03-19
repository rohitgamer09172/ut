/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.Channel
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.netty.channel;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.channel.ChannelOperator;
import io.netty.channel.Channel;
import java.net.SocketAddress;
import java.util.List;

public class ChannelOperatorModernImpl
implements ChannelOperator {
    @Override
    public SocketAddress remoteAddress(Object channel) {
        return ((Channel)channel).remoteAddress();
    }

    @Override
    public SocketAddress localAddress(Object channel) {
        return ((Channel)channel).localAddress();
    }

    @Override
    public boolean isOpen(Object channel) {
        return ((Channel)channel).isOpen();
    }

    @Override
    public Object close(Object channel) {
        return ((Channel)channel).close();
    }

    @Override
    public Object write(Object channel, Object buffer) {
        return ((Channel)channel).write(buffer);
    }

    @Override
    public Object flush(Object channel) {
        return ((Channel)channel).flush();
    }

    @Override
    public Object writeAndFlush(Object channel, Object buffer) {
        return ((Channel)channel).writeAndFlush(buffer);
    }

    @Override
    public Object fireChannelRead(Object channel, Object buffer) {
        return ((Channel)channel).pipeline().fireChannelRead(buffer);
    }

    @Override
    public Object writeInContext(Object channel, String ctx, Object buffer) {
        return ((Channel)channel).pipeline().context(ctx).write(buffer);
    }

    @Override
    public Object flushInContext(Object channel, String ctx) {
        return ((Channel)channel).pipeline().context(ctx).flush();
    }

    @Override
    public Object writeAndFlushInContext(Object channel, String ctx, Object buffer) {
        return ((Channel)channel).pipeline().context(ctx).writeAndFlush(buffer);
    }

    @Override
    public Object getPipeline(Object channel) {
        return ((Channel)channel).pipeline();
    }

    @Override
    public Object fireChannelReadInContext(Object channel, String ctx, Object buffer) {
        return ((Channel)channel).pipeline().context(ctx).fireChannelRead(buffer);
    }

    @Override
    public List<String> pipelineHandlerNames(Object channel) {
        return ((Channel)channel).pipeline().names();
    }

    @Override
    public Object getPipelineHandler(Object channel, String name) {
        return ((Channel)channel).pipeline().get(name);
    }

    @Override
    public Object getPipelineContext(Object channel, String name) {
        return ((Channel)channel).pipeline().context(name);
    }

    @Override
    public void runInEventLoop(Object channel, Runnable runnable) {
        ((Channel)channel).eventLoop().execute(runnable);
    }

    @Override
    public Object pooledByteBuf(Object o) {
        return ((Channel)o).alloc().buffer();
    }
}

