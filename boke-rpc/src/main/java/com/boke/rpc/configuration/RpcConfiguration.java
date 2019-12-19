package com.boke.rpc.configuration;

import com.boke.rpc.bean.BeanGetRegist;
import com.boke.rpc.bean.BeanSetRegist;
import com.boke.rpc.properties.RpcProperties;
import com.boke.rpc.realm.RpcProperty;
import com.boke.rpc.realm.file.FileProperty;
import com.boke.rpc.regist.RegistCenter;
import com.boke.rpc.regist.zk.ZookeeperRegist;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableConfigurationProperties({RpcProperties.class})
public class RpcConfiguration {

    private static List<String> nodes;
    @Value("${rpc.regist.address}")
    private String rpc_regist_address;

    @Bean
    public RegistCenter RegistCenter() {
        return new ZookeeperRegist(rpc_regist_address);
    }

    @Bean
    @ConditionalOnProperty(prefix = "rpc", value = "role", havingValue = "provider")
    public BeanSetRegist beanSetRegist() {
        return new BeanSetRegist();
    }

    @Bean
    @ConditionalOnProperty(prefix = "rpc", value = "role", havingValue = "consumer")
    public BeanGetRegist beanGetRegist() throws IOException {
        RpcProperty rpcProperty = new FileProperty();
        String rpc_regist_address = rpcProperty.getRegistAdd("rpc.regist.address");
        BeanGetRegist.init(rpc_regist_address);
        return new BeanGetRegist();
    }


}
