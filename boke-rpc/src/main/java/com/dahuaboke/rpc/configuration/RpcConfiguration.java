package com.dahuaboke.rpc.configuration;

import com.dahuaboke.rpc.bean.BeanGetRegist;
import com.dahuaboke.rpc.bean.BeanSetRegist;
import com.dahuaboke.rpc.properties.RpcProperties;
import com.dahuaboke.rpc.realm.RpcProperty;
import com.dahuaboke.rpc.realm.file.FileProperty;
import com.dahuaboke.rpc.regist.RegistCenter;
import com.dahuaboke.rpc.regist.nodou.NodouRegist;
import com.dahuaboke.rpc.regist.zk.ZookeeperRegist;
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
        RegistCenter registCenter = null;
        if(rpc_regist_address.contains("zookeeper://")){
            registCenter = new ZookeeperRegist(rpc_regist_address.split("zookeeper://")[1]);
        }else if(rpc_regist_address.contains("nodou://")){
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
    public BeanGetRegist beanGetRegist() throws IOException {
        RpcProperty rpcProperty = new FileProperty();
        String rpc_regist_address = rpcProperty.getRegistAdd("rpc.regist.address");
        BeanGetRegist.init(rpc_regist_address);
        return new BeanGetRegist();
    }


}
