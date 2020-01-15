package com.dahuaboke.rpc.bean;

import com.dahuaboke.rpc.annotation.RpcService;
import com.dahuaboke.rpc.client.RpcProviderClient;
import com.dahuaboke.rpc.regist.RegistCenter;
import com.dahuaboke.rpc.util.PortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.HashMap;
import java.util.Map;

public class BeanSetRegist implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RegistCenter registCenter;
    @Value("${rpc.localIp}")
    private String rpc_localIp;

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        final int port = PortUtil.portCanUse();
        //拿到spring容器
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        //拿到所有打了注解 RpcService 的bean
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RpcService.class);

        boolean isStart = false;

        Map<String, Object> handlerMap = new HashMap<String, Object>();

        for (String serviceName : beansWithAnnotation.keySet()) {
            Object serviceObject = beansWithAnnotation.get(serviceName);
            String classRealName = serviceObject.getClass().getName().split("\\$\\$")[0];
            String registName = "";
            //指定bean名字为接口名称
            Class<?>[] interfaces = new Class[0];
            try {
                interfaces = Class.forName(classRealName).getInterfaces();
                if (interfaces != null && interfaces.length > 0) {
                    registName = interfaces[0].getName();
                }
                if (registName == null || "".equals(registName)) {
                    System.err.println(serviceObject.getClass().getName() + " ---> 注册失败");
                    continue;
                }
                registCenter.register(registName, rpc_localIp + ":" + port);
                handlerMap.put(registName, serviceObject);
                isStart = true;
            } catch (ClassNotFoundException e) {
                System.out.println(classRealName + "没有实现接口");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (isStart) {
            System.out.println("RPC 服务提供者 开启 ---- > ip: " + rpc_localIp);
            new RpcProviderClient(handlerMap, port);
        }
    }
}
