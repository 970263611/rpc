package com.boke.rpc.test;


import com.boke.rpc.annotation.RpcService;

@RpcService
public class TestServiceImplOne implements TestServiceOne{

    public void say(){
        System.out.println("boke-test-success");
    }
}
