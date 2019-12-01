package com.boke.rpc.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
