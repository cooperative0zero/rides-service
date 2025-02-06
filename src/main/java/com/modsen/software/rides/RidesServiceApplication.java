package com.modsen.software.rides;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableAspectJAutoProxy
public class RidesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RidesServiceApplication.class, args);
	}

}
