package com.boke.rpc;

import com.boke.rpc.bean.BeanGetRegist;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ConsumerApplication {

	public static void main(String[] args){
		SpringApplication.run(ConsumerApplication.class, args);
	}

}
