package com.dahuaboke.rpc.handler;

import com.dahuaboke.rpc.client.RpcConsumerClient;
import com.dahuaboke.rpc.model.RpcRequest;
import com.dahuaboke.rpc.model.RpcResponse;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceHandler<T> implements InvocationHandler {

    public static HashMap<String, List<String>> map = new HashMap<>();

    private Class<T> interfaceType;

    public ServiceHandler(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        String host = null;
        for (Map.Entry entry : map.entrySet()) {
            if (method.getDeclaringClass().getName().equals(entry.getKey())) {
                List<String> classList = (List<String>) entry.getValue();
                host = classList.get(getRandom(classList.size()));
            }
        }
        if (host == null) {
            throw new Exception();
        }
        RpcRequest request = new RpcRequest();
        request.setRequestId("boke");
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        RpcConsumerClient client = new RpcConsumerClient(host, request);
        RpcResponse response = client.getResponse();
        if (response.isError()) {
            throw new Exception();
        } else {
            return response.getResult();
        }
    }

    //获取随机数，随机调度
    public int getRandom(int max) {
        int min = 0;
        long randomNum = System.currentTimeMillis();
        return (int) (randomNum % (max - min) + min);
    }
}
