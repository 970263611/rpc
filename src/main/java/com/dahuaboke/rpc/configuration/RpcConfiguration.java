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

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties({RpcProperties.class})
public class RpcConfiguration implements EnvironmentAware {

    private String rpc_regist_address;
    private String nodou_username;
    private String nodou_password;
    private String nodou_version;
    private String nodou_autoRemove;

    @Bean
    public RegistCenter RegistCenter() {
        RegistCenter registCenter = null;
        if (rpc_regist_address.startsWith("zookeeper://")) {
            registCenter = new ZookeeperRegist(rpc_regist_address.split("zookeeper://")[1]);
        } else if (rpc_regist_address.startsWith("nodou://")) {
            Map param = new HashMap();
            param.put("rpc_regist_address", "http://" + rpc_regist_address.split("nodou://")[1]);
            param.put("username", nodou_username);
            param.put("password", nodou_password);
            param.put("version", nodou_version);
            param.put("autoRemove", nodou_autoRemove);
            registCenter = new NodouRegist(param);
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
        return new BeanGetRegist(registCenter,nodou_autoRemove);
    }

    @Override
    public void setEnvironment(Environment environment) {
        rpc_regist_address = environment.getProperty("rpc.regist.address");
        nodou_username = environment.getProperty("rpc.nodou.username");
        nodou_password = environment.getProperty("rpc.nodou.password");
        nodou_version = environment.getProperty("rpc.nodou.version");
        nodou_autoRemove = environment.getProperty("rpc.nodou.autoRemove");
    }

}
