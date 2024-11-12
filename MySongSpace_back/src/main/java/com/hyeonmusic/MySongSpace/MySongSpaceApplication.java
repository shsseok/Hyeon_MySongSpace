package com.hyeonmusic.MySongSpace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
//@ComponentScan(excludeFilters = {
//		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = DataLoader.class)
//})
public class MySongSpaceApplication {
	public static void main(String[] args) {
		SpringApplication.run(MySongSpaceApplication.class, args);
	}

}
