package com.example.exampleproject;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
public class ExampleprojectApplication implements CommandLineRunner {

	@Autowired
	PropHolder propHolder;

	public static void main(String[] args) {
		SpringApplication.run(ExampleprojectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(propHolder.getProp1());
		System.out.println(propHolder.getProp2());
	}
}
