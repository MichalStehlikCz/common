package com.provys.common.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = "com.provys")
@ConfigurationPropertiesScan(basePackages = "com.provys")
public class TestApplication {

  public static void main(String[] args) {
    //noinspection resource
    SpringApplication.run(TestApplication.class, args);
  }

}
