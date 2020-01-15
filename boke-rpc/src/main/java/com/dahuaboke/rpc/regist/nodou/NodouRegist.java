package com.dahuaboke.rpc.regist.nodou;

import com.dahuaboke.rpc.regist.RegistCenter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodouRegist implements RegistCenter {

    private String rpc_regist_address;

    public NodouRegist(String address) {
        this.rpc_regist_address = address;
    }

    @Override
    public void register(String data, String servicePath) {
        HashMap<String, String> paramsMap = new HashMap<>();
        String[] params = rpc_regist_address.split("\\?")[1].split("&");
        for (String param : params) {
            paramsMap.put(param.split("=")[0], param.split("=")[1]);
        }
        paramsMap.put("nameNode", data);
        paramsMap.put("nodeMsg", servicePath);

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://" + rpc_regist_address.split("\\?")[0];
        Map result = restTemplate.postForObject(url, paramsMap, Map.class);
        if (result != null && (boolean)result.get("state")) {
            System.out.println("注册成功，namenode：" + data + "nodemsg：" + servicePath);
        }
    }

    @Override
    public List<String> getChildren() {
        HashMap<String, String> paramsMap = new HashMap<>();
        String[] params = rpc_regist_address.split("\\?")[1].split("&");
        for (String param : params) {
            paramsMap.put(param.split("=")[0], param.split("=")[1]);
        }
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://" + rpc_regist_address.split("\\?")[0];
        Map result = restTemplate.postForObject(url, paramsMap, Map.class);
        List<String> list = new ArrayList<>();
        if (result != null && (boolean)result.get("state") && result.get("obj") != null) {
            Map map = (Map) result.get("obj");
            for(Object key : map.keySet()){
                List<String> valueList = (List<String>) map.get(key);
                for(String value : valueList){
                    list.add(key + "#" + value);
                }
            }
        }
        return list;
    }
}
