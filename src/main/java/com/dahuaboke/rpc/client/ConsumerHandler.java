package com.dahuaboke.rpc.client;

import com.dahuaboke.rpc.model.RpcRequest;
import com.dahuaboke.rpc.model.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class ConsumerHandler extends ChannelInboundHandlerAdapter {

    private RpcRequest rpcRequest;
    private RpcResponse rpcResponse;

    public ConsumerHandler(RpcRequest rpcRequest){
        this.rpcRequest = rpcRequest;
    }

    public RpcResponse getRpcResponse() {
        return rpcResponse;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(rpcRequest);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        rpcResponse = (RpcResponse) msg;
        ctx.close();
    }


}
