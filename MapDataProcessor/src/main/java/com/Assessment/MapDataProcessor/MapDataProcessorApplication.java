package com.Assessment.MapDataProcessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.Assessment")
public class MapDataProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MapDataProcessorApplication.class, args);
	}

}
