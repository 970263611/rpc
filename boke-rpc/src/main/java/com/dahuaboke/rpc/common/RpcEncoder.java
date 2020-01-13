package com.dahuaboke.rpc.common;

import com.dahuaboke.rpc.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author y15079
 * @create 2018-04-01 1:01
 * @desc
 * RPC 编码器
 **/
public class RpcEncoder extends MessageToByteEncoder{

	private Class<?> genericClass;

	//构造函数传入序列化的class
	public RpcEncoder(Class<?> genericClass) {
		this.genericClass = genericClass;
	}

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf out) throws Exception {
		//对象o序列化成二进制
		if (genericClass.isInstance(o)){
			byte[] data = SerializationUtil.serialize(o);
			out.writeInt(data.length);
			out.writeBytes(data);
		}
	}
}
