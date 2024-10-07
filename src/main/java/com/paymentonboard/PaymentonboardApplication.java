package com.paymentonboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = { "com.paymentonboard.repository" })
public class PaymentonboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentonboardApplication.class, args);
	}

}
