package com.example.exampleproject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class PropHolder {
    @Value("${prop1}")
    private String prop1;
    @Value("${prop2}")
    private String prop2;

    @PostConstruct
    private void postConstructProps(){
        prop2 = "hello3";
    }

    public String getProp1() {
        return prop1;
    }

    public String getProp2() {
        return prop2;
    }


}
