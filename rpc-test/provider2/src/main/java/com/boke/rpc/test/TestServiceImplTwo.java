package com.boke.rpc.test;


import com.boke.rpc.annotation.RpcService;
import model.TestModel;
import test.TestServiceTwo;

@RpcService
public class TestServiceImplTwo implements TestServiceTwo {

    public String say() {
        return "provider2";
    }

    public TestModel test3(TestModel test) {
        int id = test.getId();
        String name = test.getName();
        return new TestModel(id+2,name + "provider2");
    }
}