package com.boke.rpc.test;


import com.boke.rpc.annotation.RpcService;
import test.TestServiceOne;

@RpcService
public class TestServiceImplOne implements TestServiceOne {

    public void say() {
        System.out.println("boke-test-success-provider1");
    }

}
