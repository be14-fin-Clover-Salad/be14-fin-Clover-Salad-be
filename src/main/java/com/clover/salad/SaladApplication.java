package com.clover.salad;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.clover.salad.**.mapper")
public class SaladApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaladApplication.class, args);
	}

}
