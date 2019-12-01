package com.boke.rpc.test;


import com.boke.rpc.annotation.RpcService;

@RpcService
public class TestServiceImplTwo implements TestServiceTwo{

    public String say(){
        System.out.println("boke-test-success");
        return "666";
    }
}
