package com.dahuaboke.rpc.common;

import com.dahuaboke.rpc.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> cls;

    public RpcEncoder(Class<?> cls) {
        this.cls = cls;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object object, ByteBuf out) throws Exception {
        if (cls.isInstance(object)) {
            byte[] data = SerializationUtil.serialize(object);
            out.writeInt(data.length);
            out.writeBytes(data);
        } else {
            throw new Exception("对象不合法");
        }
    }
}
