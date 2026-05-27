package com.example.springaidemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SpringaidemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringaidemoApplication.class, args);
	}
}
