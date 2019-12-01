package com.boke.rpc.client;

import com.boke.rpc.common.RpcDecoder;
import com.boke.rpc.common.RpcEncoder;
import com.boke.rpc.model.RpcRequest;
import com.boke.rpc.model.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class RpcConsumerClient{

    private String host;
    private final int port = 13579;
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
            bootstrap.remoteAddress(host, port);
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
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            if (channelFuture.isSuccess()) {
                System.out.println("连接服务器成功url：" + host + "端口：" + port);
            }
            channelFuture.channel().closeFuture().sync();
            this.response = handle.getRpcResponse();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
