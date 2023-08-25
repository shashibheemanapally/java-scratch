package com.example.exampleproject;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
		pattern = "com.example.exampleproject.conditionalcomponentpackages.*"))
public class ExampleprojectApplication{
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ExampleprojectApplication.class, args);
		System.exit(SpringApplication.exit(context));
	}

}
