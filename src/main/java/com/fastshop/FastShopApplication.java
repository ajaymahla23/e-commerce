package com.fastshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class FastShopApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(FastShopApplication.class, args);
		System.out.println("Running...");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(FastShopApplication.class);
	}

}
