package com.example.batch.batch_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @EnableScheduling
@SpringBootApplication
public class BatchSystemApplication {

	public static void main(String[] args) {
		System.exit(
				SpringApplication.exit(SpringApplication.run(BatchSystemApplication.class, args)));
	}

}
