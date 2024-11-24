package com.ajouchong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableFeignClients(basePackages = "com.ajouchong.oauth")
public class AjouchongApplication {

	public static void main(String[] args) {
		SpringApplication.run(AjouchongApplication.class, args);
	}

}
