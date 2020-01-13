package com.dahuaboke.rpc.realm.file;

import com.dahuaboke.rpc.realm.RpcProperty;
import org.apache.logging.log4j.util.PropertiesUtil;

import java.io.IOException;
import java.util.Properties;

public class FileProperty implements RpcProperty {

    public String getRegistAdd(String key) {
        Properties properties = new Properties();
        try {
            properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (String) properties.get(key);
    }
}
