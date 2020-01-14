package com.boke.rpc.test;


import com.dahuaboke.rpc.annotation.RpcService;
import model.TestModel;
import test.TestServiceTwo;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<TestModel> test6(TestModel test) {
        List<TestModel> list = new ArrayList<>();
        int id = test.getId();
        String name = test.getName();
        list.add(new TestModel(id+2,name + "provider2"));
        list.add(new TestModel(id+22,name + "provider2"));
        list.add(new TestModel(id+222,name + "provider2"));
        return list;
    }
}
