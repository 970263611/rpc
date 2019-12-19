package com.boke.rpc.test;

import model.TestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.TestServiceOne;
import test.TestServiceTwo;

@RestController
public class TestController {

    @Autowired
    private TestServiceOne testServiceOne;
    @Autowired
    private TestServiceTwo testServiceTwo;

    @RequestMapping("test1")
    public String test1(){
        testServiceOne.say();
        return "test1";
    }

    @RequestMapping("test2")
    public String test2(){
        return testServiceTwo.say();
    }

    @RequestMapping("test3")
    public TestModel test3(int id,String name){
        TestModel test = new TestModel(id,name);
        return testServiceTwo.test3(test);
    }
}
