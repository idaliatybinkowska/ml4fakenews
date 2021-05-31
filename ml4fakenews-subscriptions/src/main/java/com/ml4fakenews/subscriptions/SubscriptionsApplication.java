package com.ml4fakenews.subscriptions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SubscriptionsApplication {
	public static void main(String[] args) {
		SpringApplication.run(SubscriptionsApplication.class, args);
	}
}
