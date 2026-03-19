/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.channel;

import java.net.SocketAddress;
import java.util.List;

public interface ChannelOperator {
    public SocketAddress remoteAddress(Object var1);

    public SocketAddress localAddress(Object var1);

    public boolean isOpen(Object var1);

    public Object close(Object var1);

    public Object write(Object var1, Object var2);

    public Object flush(Object var1);

    public Object writeAndFlush(Object var1, Object var2);

    public Object fireChannelRead(Object var1, Object var2);

    public Object writeInContext(Object var1, String var2, Object var3);

    public Object flushInContext(Object var1, String var2);

    public Object writeAndFlushInContext(Object var1, String var2, Object var3);

    public Object fireChannelReadInContext(Object var1, String var2, Object var3);

    public List<String> pipelineHandlerNames(Object var1);

    public Object getPipelineHandler(Object var1, String var2);

    public Object getPipelineContext(Object var1, String var2);

    public Object getPipeline(Object var1);

    public void runInEventLoop(Object var1, Runnable var2);

    public Object pooledByteBuf(Object var1);
}

