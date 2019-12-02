package com.boke.rpc.test;


import com.boke.rpc.annotation.RpcService;

@RpcService
public class TestServiceImplTwo implements TestServiceTwo {

    public String say() {
        return "provider1";
    }
}
