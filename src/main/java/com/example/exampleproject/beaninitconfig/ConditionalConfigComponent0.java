package com.example.exampleproject.beaninitconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example.exampleproject.conditionalcomponentpackages.componentpackage0")
@ConditionalOnProperty(name = "package-to-initiate-components", havingValue = "componentpackage0")
public class ConditionalConfigComponent0 {
}
