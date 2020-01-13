package com.dahuaboke.rpc.client;

import com.dahuaboke.rpc.common.RpcDecoder;
import com.dahuaboke.rpc.common.RpcEncoder;
import com.dahuaboke.rpc.model.RpcRequest;
import com.dahuaboke.rpc.model.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class RpcConsumerClient {

    private String host;
    private RpcResponse response;

    public RpcConsumerClient(String host, RpcRequest request) throws InterruptedException {
        this.host = host;
        start(request);
    }

    public RpcResponse getResponse() {
        return response;
    }

    private void start(RpcRequest request) throws InterruptedException {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            ConsumerHandler handle = new ConsumerHandler(request);
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.group(eventLoopGroup);
            bootstrap.remoteAddress(host.split(":")[0], Integer.parseInt(host.split(":")[1]));
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel)
                        throws Exception {
                    socketChannel.pipeline()
                            .addLast(new RpcEncoder(RpcRequest.class))
                            .addLast(new RpcDecoder(RpcResponse.class))
                            .addLast(handle);
                }
            });
            ChannelFuture channelFuture = bootstrap.connect(host.split(":")[0], Integer.parseInt(host.split(":")[1])).sync();
            if (channelFuture.isSuccess()) {
                System.out.println("连接服务器成功url：" + host.split(":")[0] + "端口：" + Integer.parseInt(host.split(":")[1]));
            }
            channelFuture.channel().closeFuture().sync();
            this.response = handle.getRpcResponse();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
