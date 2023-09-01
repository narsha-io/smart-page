package io.narsha.smartpage.spring.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "io.narsha.smartpage.spring")
public class SmartPageSpringTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartPageSpringTestApplication.class, args);
    }
}
