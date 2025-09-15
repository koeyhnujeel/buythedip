package com.zunza.buythedip;

import org.springframework.boot.SpringApplication;

public class TestBuythedipApplication {

	public static void main(String[] args) {
		SpringApplication.from(BuythedipApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
