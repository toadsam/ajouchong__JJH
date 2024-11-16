package com.ajouchong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@PropertySource("classpath:/secure.properties")
public class AjouchongApplication {

	public static void main(String[] args) {
		SpringApplication.run(AjouchongApplication.class, args);
	}

}
