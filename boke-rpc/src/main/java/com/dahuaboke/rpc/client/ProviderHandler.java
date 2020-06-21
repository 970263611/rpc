package com.dahuaboke.rpc.client;

import com.dahuaboke.rpc.model.RpcRequest;
import com.dahuaboke.rpc.model.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@ChannelHandler.Sharable
public class ProviderHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Object> handlerMap;

    public ProviderHandler(Map<String, Object> handlerMap){
        this.handlerMap = handlerMap;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcResponse rpcResponse = new RpcResponse();
        RpcRequest request = (RpcRequest) msg;
        String className = request.getClassName();
        Object serviceBean = handlerMap.get(className);
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        Class<?> forName = Class.forName(className);
        Method method = forName.getMethod(methodName, parameterTypes);
        Object obj = null;
        try {
            obj = method.invoke(serviceBean, parameters);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        rpcResponse.setResult(obj);
        //写入outbundle(即RpcEncoder)进行下一步处理（即编码）后发送到channel中给客户端
        ctx.writeAndFlush(rpcResponse);
        //释放msg
        ReferenceCountUtil.release(msg);
    }
}
