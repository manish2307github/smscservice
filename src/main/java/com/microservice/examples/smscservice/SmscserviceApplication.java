package com.microservice.examples.smscservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import brave.sampler.Sampler;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
public class SmscserviceApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(SmscserviceApplication.class, args);
	}
	
	@Bean
	public Sampler defaultSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}

}


@RestController
class ServiceInstanceRestController {
	
	private static Logger LOG  = LogManager.getLogger(ServiceInstanceRestController.class);
	
	@Autowired
	private Environment env;
	

	@GetMapping("/hello")
	public String serviceInstancesByApplicationName() {
		LOG.info("Hello");
		return "Hello message send to smsc server, smsc service up and running";
	}
	
	@GetMapping("/sendsms/{message}/{mobileNum}")
	public String sendsms(@PathVariable String message, @PathVariable String mobileNum) {
		LOG.info("sendsms");
		return "Message " +  message + " has been sent successfully to " + mobileNum + "\n Service Ruuning on "+ env.getProperty("server.port");
	}
	
	@GetMapping("/fault-toleration")
	@HystrixCommand(fallbackMethod = "reliable")
	public String faultTolerartion() {
		LOG.info("faultTolerartion SMSC CLIENT");
		throw new RuntimeException("Service is down, please try after some time");
	}
	
	public String reliable() {
		LOG.info("reliable");
		return "fallback method smss service, please try after some time";
	}
}