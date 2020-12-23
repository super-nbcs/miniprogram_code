package com.zfw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@Transactional
@ServletComponentScan(basePackages= {"com.zfw.servlet","com.zfw.filter"})
@EnableJpaRepositories(basePackages={"com.zfw"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
