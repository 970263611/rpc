package com.dahuaboke.rpc.regist;

import java.util.List;

public interface RegistCenter {

    void register(String data, String servicePath);

    List<String> getChildren();
}
