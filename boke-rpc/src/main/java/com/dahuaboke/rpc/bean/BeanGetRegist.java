package com.dahuaboke.rpc.bean;

import com.dahuaboke.rpc.handler.ServiceHandler;
import com.dahuaboke.rpc.proxy.ProxyFactory;
import com.dahuaboke.rpc.regist.RegistCenter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BeanGetRegist implements BeanDefinitionRegistryPostProcessor {

    public volatile static List<String> nodes;
    private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
    @Autowired
    private RegistCenter registCenter;

    public BeanGetRegist() {
        pool.scheduleAtFixedRate(new Task(registCenter), 0, 30, TimeUnit.SECONDS);
    }

    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        if (nodes != null) {
            Set<Class<?>> beanClazzs = null;
            try {
                beanClazzs = getBeanClass();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (Class beanClazz : beanClazzs) {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
                GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();

                //在这里，我们可以给该对象的属性注入对应的实例。
                //比如mybatis，就在这里注入了dataSource和sqlSessionFactory，
                // 注意，如果采用definition.getPropertyValues()方式的话，
                // 类似definition.getPropertyValues().add("interfaceType", beanClazz);
                // 则要求在FactoryBean（本应用中即ServiceFactory）提供setter方法，否则会注入失败
                // 如果采用definition.getConstructorArgumentValues()，
                // 则FactoryBean中需要提供包含该属性的构造方法，否则会注入失败
                definition.getConstructorArgumentValues().addGenericArgumentValue(beanClazz);

                //注意，这里的BeanClass是生成Bean实例的工厂，不是Bean本身。
                // FactoryBean是一种特殊的Bean，其返回的对象不是指定类的一个实例，
                // 其返回的是该工厂Bean的getObject方法所返回的对象。
                definition.setBeanClass(ProxyFactory.class);

                //这里采用的是byType方式注入，类似的还有byName等
                definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
                registry.registerBeanDefinition(beanClazz.getSimpleName(), definition);
            }
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.err.println("获取消费者列表失败");
            postProcessBeanDefinitionRegistry(registry);
        }
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    public static Set<Class<?>> getBeanClass() throws Exception {
        if (nodes != null && nodes.size() > 0) {
            HashMap<String, List<String>> map = new HashMap<>();
            Set<Class<?>> beanClazzs = new HashSet<Class<?>>();
            Set<String> classNames = new HashSet<String>();
            for (String node : nodes) {
                classNames.add(node.split("#")[0]);
            }
            for (String className : classNames) {
                List<String> list = new ArrayList<>();
                for (String node : nodes) {
                    if (node.split("#")[0].equals(className)) {
                        list.add(node.split("#")[1]);
                    }
                }
                try {
                    beanClazzs.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                map.put(className, list);
            }
            ServiceHandler.map = map;
            return beanClazzs;
        } else {
            throw new Exception();
        }
    }

    class Task implements Runnable {

        private RegistCenter registCenter;

        public Task(RegistCenter registCenter) {
            this.registCenter = registCenter;
        }

        @Override
        public void run() {
            nodes = registCenter.getChildren();
            if (nodes != null) {
                try {
                    BeanGetRegist.getBeanClass();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
