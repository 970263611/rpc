package com.boke.rpc.handler;

import com.boke.rpc.client.RpcConsumerClient;
import com.boke.rpc.model.RpcRequest;
import com.boke.rpc.model.RpcResponse;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ServiceHandler<T> implements InvocationHandler {

    public static List<String> nodes = new ArrayList<>();

    private Class<T> interfaceType;

    public ServiceHandler(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        String host = null;
        for(String node:nodes){
            if(node.contains(method.getDeclaringClass().getName())){
                host = node.split("#")[1];
            }
        }
        RpcRequest request = new RpcRequest();
        request.setRequestId("boke");
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        RpcConsumerClient client = new RpcConsumerClient(host, request);
        RpcResponse response = client.getResponse();
        if (response.isError()){
            throw new Exception();
        }else {
            return response.getResult();
        }
    }
}
