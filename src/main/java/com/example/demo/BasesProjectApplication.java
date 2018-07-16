package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.example.service", "com.example.model", "com.example.security","com.example.controller",
		"com.example.config" ,"com.example.response.base", "com.example.util", "com.example.service.impl" })
@EnableJpaRepositories("com.example.repository")
@EntityScan(basePackageClasses = { Jsr310JpaConverters.class, BasesProjectApplication.class, }, basePackages = {
		"com.example.entity", "com.example.entity.base" })
public class BasesProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasesProjectApplication.class, args);
	}
}
