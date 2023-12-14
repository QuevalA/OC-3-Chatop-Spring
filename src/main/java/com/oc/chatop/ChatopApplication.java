package com.oc.chatop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class}
)
public class ChatopApplication {
	public static void main(String[] args) {
		SpringApplication.run(ChatopApplication.class, args);
	}
}
