package com.baz.oops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class OopsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OopsApplication.class, args);
	}

	//TODO: uncomment after dev
	/*@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("*//**")
						.allowedMethods("POST","GET","DELETE","PUT")
						.allowedOrigins("http://localhost:4200");
			}
		};
	}*/
}
