package com.dahuaboke.rpc.configuration;

import com.dahuaboke.rpc.bean.BeanGetRegist;
import com.dahuaboke.rpc.bean.BeanSetRegist;
import com.dahuaboke.rpc.properties.RpcProperties;
import com.dahuaboke.rpc.regist.RegistCenter;
import com.dahuaboke.rpc.regist.nodou.NodouRegist;
import com.dahuaboke.rpc.regist.zk.ZookeeperRegist;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties({RpcProperties.class})
public class RpcConfiguration implements EnvironmentAware {

    private String rpc_regist_address;
    private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

    @Bean
    public RegistCenter RegistCenter() {
        RegistCenter registCenter = null;
        if (rpc_regist_address.contains("zookeeper://")) {
            registCenter = new ZookeeperRegist(rpc_regist_address.split("zookeeper://")[1]);
        } else if (rpc_regist_address.contains("nodou://")) {
            registCenter = new NodouRegist(rpc_regist_address.split("nodou://")[1]);
        }
        return registCenter;
    }

    @Bean
    @ConditionalOnProperty(prefix = "rpc", value = "role", havingValue = "provider")
    public BeanSetRegist beanSetRegist() {
        return new BeanSetRegist();
    }

    @Bean
    @ConditionalOnProperty(prefix = "rpc", value = "role", havingValue = "consumer")
    public BeanGetRegist beanGetRegist(RegistCenter registCenter) {
        pool.scheduleAtFixedRate(new Task(registCenter), 5, 30, TimeUnit.SECONDS);
        BeanGetRegist.nodes = registCenter.getChildren();
        return new BeanGetRegist();
    }

    @Override
    public void setEnvironment(Environment environment) {
        rpc_regist_address = environment.getProperty("rpc.regist.address");
    }

    class Task implements Runnable {
        private RegistCenter registCenter;

        public Task(RegistCenter registCenter) {
            this.registCenter = registCenter;
        }

        @Override
        public void run() {
            BeanGetRegist.nodes = registCenter.getChildren();
            try {
                BeanGetRegist.getBeanClass();
                System.out.println(new Date() + "注册中心节点初始化成功");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
