package com.techlearning.API.Gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Challenges - Communication issue : client must remember every service URL
// 2 - Request must be done to different services
// 3 - Security and authentication
// 4 - To create one entry point for client
@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}
