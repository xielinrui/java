package com.chaojilaji.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {
    @RequestMapping("/")
    String home(){
        return "hello spring-boot and spring cloud";
    }

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
