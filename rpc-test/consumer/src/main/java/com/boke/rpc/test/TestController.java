package com.boke.rpc.test;

import model.TestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.TestServiceOne;
import test.TestServiceTwo;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private TestServiceOne testServiceOne;
    @Autowired
    private TestServiceTwo testServiceTwo;

    //http://localhost:8080/test1
    @RequestMapping("test1")
    public String test1(){
        testServiceOne.say();
        return "test1";
    }

    //http://localhost:8080/test2
    @RequestMapping("test2")
    public String test2(){
        return testServiceTwo.say();
    }

    //http://localhost:8080/test3?id=0&name=dwq
    @RequestMapping("test3")
    public TestModel test3(int id,String name){
        TestModel test = new TestModel(id,name);
        return testServiceTwo.test3(test);
    }

    //http://localhost:8080/test6?id=0&name=dwq
    @RequestMapping("test6")
    public List<TestModel> test6(int id, String name){
        TestModel test = new TestModel(id,name);
        return testServiceTwo.test6(test);
    }
}
