package com.example.e_commerce;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication
@EnableAspectJAutoProxy
public class ECommerceApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

	@Override

	public void run(ApplicationArguments args) throws Exception {
		System.out.println("running");
		System.out.println("running");
		System.out.println("running");

	}
}
