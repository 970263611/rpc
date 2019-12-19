package com.boke.rpc.proxy;

import com.boke.rpc.handler.ServiceHandler;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.Proxy;

public class ProxyFactory<T> implements FactoryBean<T> {

    private Class cla;

    public ProxyFactory(Class cla){
        this.cla = cla;
    }

    @Override
    public T getObject(){
        return (T) Proxy.newProxyInstance(cla.getClassLoader(),
                new Class[] {cla},new ServiceHandler<>(cla));
    }

    @Override
    public Class<?> getObjectType() {
        return cla;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
