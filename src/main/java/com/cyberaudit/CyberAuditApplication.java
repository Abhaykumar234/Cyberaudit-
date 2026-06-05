package com.cyberaudit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CyberAuditApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CyberAuditApplication.class, args);
    }
}
