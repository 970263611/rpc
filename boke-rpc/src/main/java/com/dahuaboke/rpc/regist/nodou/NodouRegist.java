package com.dahuaboke.rpc.regist.nodou;

import com.dahuaboke.rpc.regist.RegistCenter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NodouRegist implements RegistCenter {

    private Map param;

    public NodouRegist(Map param) {
        this.param = param;
    }

    @Override
    public void register(String data, String servicePath) {
        RestTemplate restTemplate = new RestTemplate();
        param.put("nodeKey", data);
        param.put("nodeValue", servicePath);
        String result = restTemplate.postForObject(param.get("rpc_regist_address") + "/nodou", param, String.class);
        if ("ok".equals(result)) {
            System.out.println("注册成功，nodeKey：" + data + "。nodeValue：" + servicePath + "。version：" + param.get("version") + "。autoRemove：" + param.get("autoRemove"));
        }
    }

    @Override
    public List<String> getChildren() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", (String) param.get("username"));
        params.add("password", (String) param.get("password"));
        params.add("version", (String) param.get("version"));
        params.add("autoRemove", (String) param.get("autoRemove"));
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(param.get("rpc_regist_address") + "/nodou");
        URI uri = builder.queryParams(params).build().encode().toUri();
        Map result = restTemplate.getForObject(uri, Map.class);
        List<String> list = new ArrayList<>();
        if (result != null) {
            for (Object key : result.keySet()) {
                List<String> valueList = (List<String>) result.get(key);
                for (String value : valueList) {
                    list.add(key + "#" + value);
                }
            }
        }
        return list;
    }
}
