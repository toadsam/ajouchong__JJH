package com.ajouchong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@PropertySource("classpath:/secure.properties")
@EnableFeignClients(basePackages = "com.ajouchong.oauth")
public class AjouchongApplication {

	public static void main(String[] args) {
		SpringApplication.run(AjouchongApplication.class, args);
	}

}
